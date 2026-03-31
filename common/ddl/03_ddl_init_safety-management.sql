\c postgres;

set client_encoding to UTF8;

-- テーブルの作成
CREATE EXTENSION IF NOT EXISTS postgis;
CREATE EXTENSION IF NOT EXISTS postgis WITH SCHEMA safety_management;
CREATE EXTENSION IF NOT EXISTS postgis_sfcgal;
CREATE EXTENSION IF NOT EXISTS postgis_sfcgal WITH SCHEMA safety_management;

DROP TABLE IF EXISTS safety_management.t_drone_location;
CREATE TABLE safety_management.t_drone_location (
    subscription_id VARCHAR,
    uas_id VARCHAR,
    ua_type VARCHAR,
    get_location_timestamp TIMESTAMPTZ NOT NULL,
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION,
    altitude INTEGER,
    track_direction INTEGER,
    speed DOUBLE PRECISION,
    vertical_speed DOUBLE PRECISION,
    route_deviation_rate DOUBLE PRECISION,
    route_deviation_rate_update_time TIMESTAMPTZ,
    reservation_id VARCHAR NOT NULL,
    uasl_id VARCHAR,
    uasl_section_id VARCHAR,
    operational_status VARCHAR,
    operator_id VARCHAR,
    flight_time VARCHAR,
    aircraft_info_id INTEGER,
    CONSTRAINT t_drone_location_pk PRIMARY KEY (reservation_id, get_location_timestamp)
);
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE safety_management.t_drone_location TO safety_management;

CREATE INDEX IF NOT EXISTS idx_t_drone_location_uas_ts_desc
  ON safety_management.t_drone_location (uas_id, get_location_timestamp DESC);
COMMENT ON INDEX safety_management.idx_t_drone_location_uas_ts_desc
  IS 'DBのCPU100%問題の対策: uas_id で絞り込み、最新レコード取得(ORDER BY timestamp DESC LIMIT)を高速化';

CREATE INDEX IF NOT EXISTS idx_t_drone_location_route_deviation_rate
  ON safety_management.t_drone_location (route_deviation_rate);

CREATE INDEX IF NOT EXISTS idx_t_drone_location_reservation_id_zero_deviation
  ON safety_management.t_drone_location (reservation_id)
  WHERE route_deviation_rate = 0.0;
COMMENT ON INDEX safety_management.idx_t_drone_location_reservation_id_zero_deviation
  IS '部分インデックス: 逸脱なし（route_deviation_rate=0）データに限定。reservation_id をキーにした検索を高速化する';

COMMENT ON TABLE safety_management.t_drone_location IS '運行情報蓄積';
COMMENT ON COLUMN safety_management.t_drone_location.subscription_id IS 'エリア情報のサブスクリプション ID';
COMMENT ON COLUMN safety_management.t_drone_location.uas_id IS '機体の登録 ID';
COMMENT ON COLUMN safety_management.t_drone_location.ua_type IS '機体の種別';
COMMENT ON COLUMN safety_management.t_drone_location.get_location_timestamp IS 'テレメトリ情報取得日時';
COMMENT ON COLUMN safety_management.t_drone_location.latitude IS '緯度';
COMMENT ON COLUMN safety_management.t_drone_location.longitude IS '経度';
COMMENT ON COLUMN safety_management.t_drone_location.altitude IS '標高';
COMMENT ON COLUMN safety_management.t_drone_location.track_direction IS '機体の進行方向';
COMMENT ON COLUMN safety_management.t_drone_location.speed IS '機体の速度';
COMMENT ON COLUMN safety_management.t_drone_location.vertical_speed IS '機体の垂直速度';
COMMENT ON COLUMN safety_management.t_drone_location.route_deviation_rate IS '航路逸脱割合';
COMMENT ON COLUMN safety_management.t_drone_location.route_deviation_rate_update_time IS '航路逸脱割合更新時刻';
COMMENT ON COLUMN safety_management.t_drone_location.reservation_id IS '航路予約 ID';
COMMENT ON COLUMN safety_management.t_drone_location.uasl_id IS '航路 ID';
COMMENT ON COLUMN safety_management.t_drone_location.uasl_section_id IS '航路区画 ID';
COMMENT ON COLUMN safety_management.t_drone_location.operational_status IS '運航状況';
COMMENT ON COLUMN safety_management.t_drone_location.operator_id IS '運航者 ID';
COMMENT ON COLUMN safety_management.t_drone_location.flight_time IS '飛行時間';
COMMENT ON COLUMN safety_management.t_drone_location.aircraft_info_id IS '機体情報 ID';

-- 航路予約情報
DROP TABLE IF EXISTS safety_management.t_uasl_reservation;
CREATE TABLE safety_management.t_uasl_reservation (
    uasl_reservation_id VARCHAR NOT NULL,
    start_at TIMESTAMPTZ,
    end_at TIMESTAMPTZ,
    reserved_at TIMESTAMPTZ,
    uasl_section_ids VARCHAR ARRAY,
    operator_id VARCHAR,
    evaluation_results BOOLEAN,
    third_party_evaluation_results BOOLEAN,
    railway_operation_evaluation_results BOOLEAN,
    planned_deviation BOOLEAN NOT NULL DEFAULT false,
    CONSTRAINT t_uasl_reservation_pk PRIMARY KEY (uasl_reservation_id)
);
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE safety_management.t_uasl_reservation TO safety_management;

CREATE INDEX IF NOT EXISTS idx_t_uasl_reservation_start_end_at
  ON safety_management.t_uasl_reservation (start_at, end_at);

COMMENT ON TABLE safety_management.t_uasl_reservation IS '航路予約情報';
COMMENT ON COLUMN safety_management.t_uasl_reservation.uasl_reservation_id IS '航路予約毎の識別ID';
COMMENT ON COLUMN safety_management.t_uasl_reservation.start_at IS '予約開始日時';
COMMENT ON COLUMN safety_management.t_uasl_reservation.end_at IS '予約終了日時';
COMMENT ON COLUMN safety_management.t_uasl_reservation.reserved_at IS '予約登録日時';
COMMENT ON COLUMN safety_management.t_uasl_reservation.uasl_section_ids IS '航路区画ID配列';
COMMENT ON COLUMN safety_management.t_uasl_reservation.operator_id IS '運航事業者ID';
COMMENT ON COLUMN safety_management.t_uasl_reservation.evaluation_results IS '適合性評価結果';
COMMENT ON COLUMN safety_management.t_uasl_reservation.third_party_evaluation_results IS ' 第三者立入監視情報 適合性評価結果';
COMMENT ON COLUMN safety_management.t_uasl_reservation.railway_operation_evaluation_results IS '鉄道運航情報 適合性評価結果';
COMMENT ON COLUMN safety_management.t_uasl_reservation.planned_deviation IS '計画的な航路逸脱フラグ';

-- リモートID紐づけ情報
DROP TABLE IF EXISTS safety_management.t_remote_data;
CREATE TABLE safety_management.t_remote_data (
    uasl_reservation_id VARCHAR NOT NULL,
    serial_number VARCHAR,
    registration_id VARCHAR,
    utm_id VARCHAR,
    specific_session_id VARCHAR,
    aircraft_info_id INTEGER,
    request_id VARCHAR,
    CONSTRAINT t_remote_data_pk PRIMARY KEY (uasl_reservation_id)
);
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE safety_management.t_remote_data TO safety_management;

CREATE INDEX IF NOT EXISTS idx_t_remote_data_serial_number
  ON safety_management.t_remote_data (serial_number);

CREATE INDEX IF NOT EXISTS idx_t_remote_data_registration_id
  ON safety_management.t_remote_data (registration_id);

CREATE INDEX IF NOT EXISTS idx_t_remote_data_utm_id
  ON safety_management.t_remote_data (utm_id);

CREATE INDEX IF NOT EXISTS idx_t_remote_data_specific_session_id
  ON safety_management.t_remote_data (specific_session_id);

COMMENT ON TABLE safety_management.t_remote_data IS '航路予約情報';
COMMENT ON COLUMN safety_management.t_remote_data.uasl_reservation_id IS '航路予約毎の識別ID';
COMMENT ON COLUMN safety_management.t_remote_data.serial_number IS 'シリアルナンバー';
COMMENT ON COLUMN safety_management.t_remote_data.registration_id IS '登録ID';
COMMENT ON COLUMN safety_management.t_remote_data.utm_id IS 'セッションID';
COMMENT ON COLUMN safety_management.t_remote_data.specific_session_id IS 'フライト識別ID';
COMMENT ON COLUMN safety_management.t_remote_data.aircraft_info_id IS '機体情報ID';
COMMENT ON COLUMN safety_management.t_remote_data.request_id IS '一括予約リクエストID';

-- サブスクリプションID紐づけ情報
DROP TABLE IF EXISTS safety_management.t_subscription_data;
CREATE TABLE safety_management.t_subscription_data (
    uasl_reservation_id VARCHAR NOT NULL,
    subscription_id VARCHAR,
    area_info VARCHAR ARRAY,
    uasl_id VARCHAR,
    CONSTRAINT t_subscription_data_pk PRIMARY KEY (uasl_reservation_id)
);
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE safety_management.t_subscription_data TO safety_management;

COMMENT ON TABLE safety_management.t_subscription_data IS 'サブスクリプションID紐づけ情報';
COMMENT ON COLUMN safety_management.t_subscription_data.uasl_reservation_id IS '航路予約毎の識別ID';
COMMENT ON COLUMN safety_management.t_subscription_data.subscription_id IS 'サブスクリプションID';
COMMENT ON COLUMN safety_management.t_subscription_data.area_info IS 'エリア情報';
COMMENT ON COLUMN safety_management.t_subscription_data.uasl_id IS '航路ID';

-- 第三者立入監視情報
DROP TABLE IF EXISTS safety_management.t_monitoring_information;
CREATE TABLE safety_management.t_monitoring_information (
    uasl_reservation_id VARCHAR NOT NULL,
    monitoring_information VARCHAR,
    uasl_administrator_id VARCHAR,
    operator_id VARCHAR,
    uasl_id VARCHAR
);
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE safety_management.t_monitoring_information TO safety_management;

COMMENT ON TABLE safety_management.t_monitoring_information IS '第三者立入監視情報';
COMMENT ON COLUMN safety_management.t_monitoring_information.uasl_reservation_id IS '航路予約毎の識別ID';
COMMENT ON COLUMN safety_management.t_monitoring_information.monitoring_information IS '第三者立入監視情報';
COMMENT ON COLUMN safety_management.t_monitoring_information.uasl_administrator_id IS '航路運営者ID';
COMMENT ON COLUMN safety_management.t_monitoring_information.operator_id IS '運航事業者ID';
COMMENT ON COLUMN safety_management.t_monitoring_information.uasl_id IS '航路ID';

-- 航路逸脱情報
DROP TABLE IF EXISTS safety_management.t_uasl_deviation;
CREATE TABLE safety_management.t_uasl_deviation (
    uasl_reservation_id VARCHAR NOT NULL,
    route_deviation_rate DOUBLE PRECISION,
    route_deviation_amount VARCHAR,
    route_deviation_time JSON,
    uasl_administrator_id VARCHAR,
    operator_id VARCHAR,
    uasl_id VARCHAR,
    serial_number VARCHAR,
    registration_id VARCHAR,
    utm_id VARCHAR,
    specific_session_id VARCHAR,
    aircraft_info_id INTEGER,
    route_deviation_coordinates JSON,
    CONSTRAINT t_uasl_deviation_pk PRIMARY KEY (uasl_reservation_id)
);
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE safety_management.t_uasl_deviation TO safety_management;

COMMENT ON TABLE safety_management.t_uasl_deviation IS '航路逸脱情報';
COMMENT ON COLUMN safety_management.t_uasl_deviation.uasl_reservation_id IS '航路予約毎の識別ID';
COMMENT ON COLUMN safety_management.t_uasl_deviation.route_deviation_rate IS '航路逸脱情報.逸脱割合';
COMMENT ON COLUMN safety_management.t_uasl_deviation.route_deviation_amount IS '航路逸脱情報.逸脱量';
COMMENT ON COLUMN safety_management.t_uasl_deviation.route_deviation_time IS '逸脱検知時刻';
COMMENT ON COLUMN safety_management.t_uasl_deviation.uasl_administrator_id IS '航路運営者ID';
COMMENT ON COLUMN safety_management.t_uasl_deviation.operator_id IS '運航事業者ID';
COMMENT ON COLUMN safety_management.t_uasl_deviation.uasl_id IS '航路ID';
COMMENT ON COLUMN safety_management.t_uasl_deviation.serial_number IS 'シリアルナンバー';
COMMENT ON COLUMN safety_management.t_uasl_deviation.registration_id IS '登録ID';
COMMENT ON COLUMN safety_management.t_uasl_deviation.utm_id IS 'セッションID';
COMMENT ON COLUMN safety_management.t_uasl_deviation.specific_session_id IS 'フライト識別ID';
COMMENT ON COLUMN safety_management.t_uasl_deviation.aircraft_info_id IS '機体情報ID';
COMMENT ON COLUMN safety_management.t_uasl_deviation.route_deviation_coordinates IS '逸脱検知情報.航路区画ID.逸脱発生地点の座標';

-- 機体種別毎の逸脱量・逸脱割合の集計情報
DROP TABLE IF EXISTS safety_management.t_aircraft_type_deviation;
CREATE TABLE safety_management.t_aircraft_type_deviation (
  aircraft_type_deviation_id SERIAL PRIMARY KEY,
  aircraft_info_id INTEGER NOT NULL,
  serial_number VARCHAR,
  registration_id VARCHAR,
  utm_id VARCHAR,
  specific_session_id VARCHAR,
  route_deviation_rate DOUBLE PRECISION,
  route_deviation_amount VARCHAR,
  route_deviation_start_time TIMESTAMPTZ,
  route_deviation_end_time TIMESTAMPTZ,
  flight_count INTEGER
);
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE safety_management.t_aircraft_type_deviation TO safety_management;

COMMENT ON TABLE safety_management.t_aircraft_type_deviation IS '機体種別毎の逸脱量・逸脱割合の集計情報（月間サマリ）';
COMMENT ON COLUMN safety_management.t_aircraft_type_deviation.aircraft_type_deviation_id IS '機体種別逸脱集計ID（PK）';
COMMENT ON COLUMN safety_management.t_aircraft_type_deviation.aircraft_info_id IS '機体情報ID';
COMMENT ON COLUMN safety_management.t_aircraft_type_deviation.serial_number IS 'シリアルナンバー';
COMMENT ON COLUMN safety_management.t_aircraft_type_deviation.registration_id IS '登録ID';
COMMENT ON COLUMN safety_management.t_aircraft_type_deviation.utm_id IS 'セッションID';
COMMENT ON COLUMN safety_management.t_aircraft_type_deviation.specific_session_id IS 'フライト識別ID';
COMMENT ON COLUMN safety_management.t_aircraft_type_deviation.route_deviation_rate IS '航路逸脱割合（集計値）';
COMMENT ON COLUMN safety_management.t_aircraft_type_deviation.route_deviation_amount IS '航路逸脱量（集計値）';
COMMENT ON COLUMN safety_management.t_aircraft_type_deviation.route_deviation_start_time IS '逸脱検知開始時刻';
COMMENT ON COLUMN safety_management.t_aircraft_type_deviation.route_deviation_end_time IS '逸脱検知終了時刻';
COMMENT ON COLUMN safety_management.t_aircraft_type_deviation.flight_count IS '集計期間内のフライト数';

-- Viewの作成
-- 航路区画IDをPKと考えた場合の航路情報
DROP VIEW IF EXISTS safety_management.v_uasl_design_section_geometry;
CREATE VIEW safety_management.v_uasl_design_section_geometry AS
WITH geometry_data AS (
    SELECT
        asct.uasl_section_id as uasl_section_id,
        ary.uasl_id,
        asct.name,
        ST_Force3D(ST_SetSRID(ST_Collect(
            CASE
                WHEN asct.point_a_id = aj.uasl_point_id AND asct.point_a_is_forward = false THEN uasl_design.uasl_point_reverse(aj.geometry)
                WHEN asct.point_b_id = aj.uasl_point_id AND asct.point_b_is_forward = false THEN uasl_design.uasl_point_reverse(aj.geometry)
                ELSE aj.geometry
            END
        ), 4326)) AS geometry
    FROM 
        uasl_design.uasl_section asct
    JOIN
        uasl_design.uasl_point aj ON asct.point_a_id = aj.uasl_point_id
        OR asct.point_b_id = aj.uasl_point_id
    JOIN
        uasl_design.uasl ary ON asct.uasl_id = ary.uasl_id
    GROUP BY
        asct.uasl_section_id, ary.uasl_id, asct.name
)
SELECT
    gd.uasl_section_id,
    gd.uasl_id,
    gd.name,
    gd.geometry,
    (SELECT ST_MakeSolid(ST_GeomFromText('POLYHEDRALSURFACE Z (
        ((' || b || ', ' || f || ', ' || g || ', ' || b || ')),
        ((' || b || ', ' || g || ', ' || c || ', ' || b || ')),
        ((' || a || ', ' || d || ', ' || h || ', ' || a || ')),
        ((' || a || ', ' || h || ', ' || e || ', ' || a || ')),
        ((' || b || ', ' || a || ', ' || e || ', ' || b || ')),
        ((' || b || ', ' || e || ', ' || f || ', ' || b || ')),
        ((' || f || ', ' || e || ', ' || h || ', ' || f || ')),
        ((' || f || ', ' || h || ', ' || g || ', ' || f || ')),
        ((' || g || ', ' || h || ', ' || d || ', ' || g || ')),
        ((' || g || ', ' || d || ', ' || c || ', ' || g || ')),
        ((' || c || ', ' || d || ', ' || a || ', ' || c || ')),
        ((' || c || ', ' || a || ', ' || b || ', ' || c || '))
     )')) As uasl_section_solid_geometry
     FROM (
        WITH numbered_points AS (
            SELECT (g).path, 
                   regexp_replace(ST_AsText((g).geom), '^POINT Z \((.+)\)$', '\1') AS coords,
                   row_number() OVER (ORDER BY (g).path ASC) AS rn
            FROM (
              SELECT ST_DumpPoints(gd.geometry) AS g
            ) AS dump_source
        )
        SELECT
          MAX(CASE WHEN rn = 1 THEN coords END) AS a,
          MAX(CASE WHEN rn = 2 THEN coords END) AS b,
          MAX(CASE WHEN rn = 3 THEN coords END) AS c,
          MAX(CASE WHEN rn = 4 THEN coords END) AS d,
          MAX(CASE WHEN rn = 6 THEN coords END) AS e,
          MAX(CASE WHEN rn = 7 THEN coords END) AS f,
          MAX(CASE WHEN rn = 8 THEN coords END) AS g,
          MAX(CASE WHEN rn = 9 THEN coords END) AS h
        FROM numbered_points
     ) AS np_agg
    )
FROM 
    geometry_data gd;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE safety_management.v_uasl_design_section_geometry TO safety_management;

COMMENT ON VIEW safety_management.v_uasl_design_section_geometry IS '航路区画情報';
COMMENT ON COLUMN safety_management.v_uasl_design_section_geometry.uasl_section_id IS '航路区画ID';
COMMENT ON COLUMN safety_management.v_uasl_design_section_geometry.uasl_id IS '航路ID';
COMMENT ON COLUMN safety_management.v_uasl_design_section_geometry.name IS '航路区画名';
COMMENT ON COLUMN safety_management.v_uasl_design_section_geometry.geometry IS '航路区画のジオメトリ';
COMMENT ON COLUMN safety_management.v_uasl_design_section_geometry.uasl_section_solid_geometry IS '航路区画の立体ジオメトリ';

-- 航路区画IDをPKと考えた場合の航路エリア情報
DROP VIEW IF EXISTS safety_management.v_uasl_design_area_info_section;
CREATE VIEW safety_management.v_uasl_design_area_info_section AS
SELECT
    section.uasl_section_id as uasl_section_id,
    uasl.uasl_id as uasl_id,
    '150.0' AS altitude,
    TO_CHAR(ST_YMax(ST_Envelope(fall.geometry)), 'FM999999999.0') AS lat_start,
    TO_CHAR(ST_XMin(ST_Envelope(fall.geometry)), 'FM999999999.0') AS lon_start,
    TO_CHAR(ST_YMin(ST_Envelope(fall.geometry)), 'FM999999999.0') AS lat_end,
    TO_CHAR(ST_XMax(ST_Envelope(fall.geometry)), 'FM999999999.0') AS lon_end,
    fall.geometry::geometry as geometry,
    ST_AsGeoJSON(fall.geometry) as area_info,
    json_agg(row_to_json(railway)):: text as railway_crossing_info
FROM
    uasl_design.uasl as uasl
    join uasl_design.uasl_section as section
    on (uasl.uasl_id = section.uasl_id)
    join uasl_design.uasl_determination as determination
    on (uasl.uasl_determination_id = determination.uasl_determination_id)
    join uasl_design.max_fall_range as fall
    on (determination.max_fall_range_id = fall.max_fall_range_id)
    left join uasl_design.railway_crossing_info as railway
    on (uasl.uasl_id = railway.uasl_id)
GROUP BY
    section.uasl_section_id, uasl.uasl_id, fall.geometry;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE safety_management.v_uasl_design_area_info_section TO safety_management;

COMMENT ON VIEW safety_management.v_uasl_design_area_info_section IS '航路エリア情報_航路区間';
COMMENT ON COLUMN safety_management.v_uasl_design_area_info_section.uasl_section_id IS '航路区画ID';
COMMENT ON COLUMN safety_management.v_uasl_design_area_info_section.uasl_id IS '航路ID';
COMMENT ON COLUMN safety_management.v_uasl_design_area_info_section.altitude IS '標高';
COMMENT ON COLUMN safety_management.v_uasl_design_area_info_section.lat_start IS '北西端緯度';
COMMENT ON COLUMN safety_management.v_uasl_design_area_info_section.lon_start IS '北西端経度';
COMMENT ON COLUMN safety_management.v_uasl_design_area_info_section.lat_end IS '南東端緯度';
COMMENT ON COLUMN safety_management.v_uasl_design_area_info_section.lon_end IS '南東端経度';
COMMENT ON COLUMN safety_management.v_uasl_design_area_info_section.geometry IS 'エリア情報(最大落下許容範囲)';
COMMENT ON COLUMN safety_management.v_uasl_design_area_info_section.area_info IS 'エリア情報をJSON文字列にしたもの';
COMMENT ON COLUMN safety_management.v_uasl_design_area_info_section.railway_crossing_info IS '鉄道との交点情報';

-- 航路エリア情報(予約データ)
DROP VIEW IF EXISTS safety_management.v_uasl_design_area_info_reservation;
CREATE VIEW safety_management.v_uasl_design_area_info_reservation AS
SELECT
    MAX(uasl.uasl_id) as uasl_id,
    fall.business_number as uasl_administrator_id, -- 2024年度は航路運営者IDは非対応のため事業者番号を参照
    reservation.uasl_reservation_id as uasl_reservation_id,
    reservation.start_at as start_at,
    reservation.end_at as end_at,
    reservation.operator_id as operator_id,
    reservation.evaluation_results as evaluation_results,
    reservation.third_party_evaluation_results as third_party_evaluation_results,
    reservation.railway_operation_evaluation_results as railway_operation_evaluation_results,
    '150.0' AS altitude,
    TO_CHAR(MAX(ST_YMax(ST_Envelope(fall.geometry))), 'FM999999999.0') AS lat_start,
    TO_CHAR(MAX(ST_XMin(ST_Envelope(fall.geometry))), 'FM999999999.0') AS lon_start,
    TO_CHAR(MAX(ST_YMin(ST_Envelope(fall.geometry))), 'FM999999999.0') AS lat_end,
    TO_CHAR(MAX(ST_XMax(ST_Envelope(fall.geometry))), 'FM999999999.0') AS lon_end,
    MAX(fall.geometry)::geometry as geometry,
    ST_AsGeoJSON(MAX(fall.geometry)) as area_info,
    json_agg(row_to_json(railway)):: text as railway_crossing_info
FROM
    uasl_design.uasl as uasl
    join uasl_design.uasl_section as section
    on (uasl.uasl_id = section.uasl_id)
    join uasl_design.uasl_determination as determination
    on (uasl.uasl_determination_id = determination.uasl_determination_id)
    join uasl_design.max_fall_range as fall
    on (determination.max_fall_range_id = fall.max_fall_range_id)
    left join  uasl_design.railway_crossing_info as railway
    on (uasl.uasl_id = railway.uasl_id)
    join (
      select  distinct on (uasl_reservation_id)
        uasl_reservation_id, start_at, end_at, reserved_at, UNNEST(uasl_section_ids) AS uasl_section_id, operator_id, evaluation_results, third_party_evaluation_results, railway_operation_evaluation_results
      from safety_management.t_uasl_reservation
      where start_at > now()
    ) reservation
    on section.uasl_section_id = reservation.uasl_section_id
GROUP BY
    fall.business_number, reservation.uasl_reservation_id, reservation.start_at, reservation.end_at, reservation.operator_id, evaluation_results, third_party_evaluation_results, railway_operation_evaluation_results;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE safety_management.v_uasl_design_area_info_reservation TO safety_management;

COMMENT ON VIEW safety_management.v_uasl_design_area_info_reservation IS '航路エリア情報_予約データ';
COMMENT ON COLUMN safety_management.v_uasl_design_area_info_reservation.uasl_id IS '航路ID';
COMMENT ON COLUMN safety_management.v_uasl_design_area_info_reservation.uasl_administrator_id IS '航路運営者ID';
COMMENT ON COLUMN safety_management.v_uasl_design_area_info_reservation.uasl_reservation_id IS '航路予約毎の識別ID';
COMMENT ON COLUMN safety_management.v_uasl_design_area_info_reservation.start_at IS '予約開始日時';
COMMENT ON COLUMN safety_management.v_uasl_design_area_info_reservation.end_at IS '予約終了日時';
COMMENT ON COLUMN safety_management.v_uasl_design_area_info_reservation.operator_id IS '運航者ID';
COMMENT ON COLUMN safety_management.v_uasl_design_area_info_reservation.evaluation_results IS '適合性評価結果';
COMMENT ON COLUMN safety_management.v_uasl_design_area_info_reservation.third_party_evaluation_results IS ' 第三者立入監視情報 適合性評価結果';
COMMENT ON COLUMN safety_management.v_uasl_design_area_info_reservation.railway_operation_evaluation_results IS '鉄道運航情報 適合性評価結果';
COMMENT ON COLUMN safety_management.v_uasl_design_area_info_reservation.altitude IS '標高';
COMMENT ON COLUMN safety_management.v_uasl_design_area_info_reservation.lat_start IS '北西端緯度';
COMMENT ON COLUMN safety_management.v_uasl_design_area_info_reservation.lon_start IS '北西端経度';
COMMENT ON COLUMN safety_management.v_uasl_design_area_info_reservation.lat_end IS '南東端緯度';
COMMENT ON COLUMN safety_management.v_uasl_design_area_info_reservation.lon_end IS '南東端経度';
COMMENT ON COLUMN safety_management.v_uasl_design_area_info_reservation.geometry IS 'エリア情報(最大落下許容範囲)';
COMMENT ON COLUMN safety_management.v_uasl_design_area_info_reservation.area_info IS 'エリア情報をJSON文字列にしたもの';
COMMENT ON COLUMN safety_management.v_uasl_design_area_info_reservation.railway_crossing_info IS '鉄道との交点情報';

-- 航路エリア情報(運航中データ)
DROP VIEW IF EXISTS safety_management.v_uasl_design_area_info_operation;
CREATE VIEW safety_management.v_uasl_design_area_info_operation AS
SELECT
    MAX(uasl.uasl_id) as uasl_id,
    fall.business_number as uasl_administrator_id, -- 2024年度は航路運営者IDは非対応のため事業者番号を参照
    reservation.uasl_reservation_id as uasl_reservation_id,
    reservation.start_at as start_at,
    reservation.end_at as end_at,
    reservation.operator_id as operator_id,
    reservation.evaluation_results as evaluation_results,
    reservation.third_party_evaluation_results as third_party_evaluation_results,
    reservation.railway_operation_evaluation_results as railway_operation_evaluation_results,
    '150.0' AS altitude,
    TO_CHAR(MAX(ST_YMax(ST_Envelope(fall.geometry))), 'FM999999999.0') AS lat_start,
    TO_CHAR(MAX(ST_XMin(ST_Envelope(fall.geometry))), 'FM999999999.0') AS lon_start,
    TO_CHAR(MAX(ST_YMin(ST_Envelope(fall.geometry))), 'FM999999999.0') AS lat_end,
    TO_CHAR(MAX(ST_XMax(ST_Envelope(fall.geometry))), 'FM999999999.0') AS lon_end,
    MAX(fall.geometry)::geometry as geometry,
    ST_AsGeoJSON(MAX(fall.geometry)) as area_info,
    json_agg(row_to_json(railway)):: text as railway_crossing_info
FROM
    uasl_design.uasl as uasl
    join uasl_design.uasl_section as section
    on (uasl.uasl_id = section.uasl_id)
    join uasl_design.uasl_determination as determination
    on (uasl.uasl_determination_id = determination.uasl_determination_id)
    join uasl_design.max_fall_range as fall
    on (determination.max_fall_range_id = fall.max_fall_range_id)
    left join  uasl_design.railway_crossing_info as railway
    on (uasl.uasl_id = railway.uasl_id)
    join (
      select  distinct on (uasl_reservation_id)
        uasl_reservation_id, start_at, end_at, reserved_at, UNNEST(uasl_section_ids) AS uasl_section_id, operator_id, evaluation_results, third_party_evaluation_results, railway_operation_evaluation_results
      from safety_management.t_uasl_reservation
      where start_at <= now() and end_at >= now()
    ) reservation
    on section.uasl_section_id = reservation.uasl_section_id
GROUP BY
     fall.business_number, reservation.uasl_reservation_id, reservation.start_at, reservation.end_at, reservation.operator_id, evaluation_results, third_party_evaluation_results, railway_operation_evaluation_results;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE safety_management.v_uasl_design_area_info_operation TO safety_management;

COMMENT ON VIEW safety_management.v_uasl_design_area_info_operation IS '航路エリア情報_運航中データ';
COMMENT ON COLUMN safety_management.v_uasl_design_area_info_operation.uasl_id IS '航路ID';
COMMENT ON COLUMN safety_management.v_uasl_design_area_info_operation.uasl_administrator_id IS '航路運営者ID';
COMMENT ON COLUMN safety_management.v_uasl_design_area_info_operation.uasl_reservation_id IS '航路予約毎の識別ID';
COMMENT ON COLUMN safety_management.v_uasl_design_area_info_operation.start_at IS '予約開始日時';
COMMENT ON COLUMN safety_management.v_uasl_design_area_info_operation.end_at IS '予約終了日時';
COMMENT ON COLUMN safety_management.v_uasl_design_area_info_operation.operator_id IS '運航者ID';
COMMENT ON COLUMN safety_management.v_uasl_design_area_info_operation.evaluation_results IS '適合性評価結果';
COMMENT ON COLUMN safety_management.v_uasl_design_area_info_operation.third_party_evaluation_results IS ' 第三者立入監視情報 適合性評価結果';
COMMENT ON COLUMN safety_management.v_uasl_design_area_info_operation.railway_operation_evaluation_results IS '鉄道運航情報 適合性評価結果';
COMMENT ON COLUMN safety_management.v_uasl_design_area_info_operation.altitude IS '標高';
COMMENT ON COLUMN safety_management.v_uasl_design_area_info_operation.lat_start IS '北西端緯度';
COMMENT ON COLUMN safety_management.v_uasl_design_area_info_operation.lon_start IS '北西端経度';
COMMENT ON COLUMN safety_management.v_uasl_design_area_info_operation.lat_end IS '南東端緯度';
COMMENT ON COLUMN safety_management.v_uasl_design_area_info_operation.lon_end IS '南東端経度';
COMMENT ON COLUMN safety_management.v_uasl_design_area_info_operation.geometry IS 'エリア情報(最大落下許容範囲)';
COMMENT ON COLUMN safety_management.v_uasl_design_area_info_operation.area_info IS 'エリア情報をJSON文字列にしたもの';
COMMENT ON COLUMN safety_management.v_uasl_design_area_info_operation.railway_crossing_info IS '鉄道との交点情報';

-- 航路逸脱情報（航路逸脱情報テーブル登録用データ)
DROP VIEW IF EXISTS safety_management.v_uasl_design_area_info_deviation;
CREATE VIEW safety_management.v_uasl_design_area_info_deviation AS
WITH transformed_uasl_section_geometry AS (
    SELECT
        uasl_section_id,
        ST_Transform(ST_SetSRID(uasl_section_solid_geometry, 4326), 4978) AS transformed_geometry
    FROM
        safety_management.v_uasl_design_section_geometry
)
SELECT
    closestUaslPoint.reservation_id,
    closestUaslPoint.get_location_timestamp,
    closestUaslPoint.latitude,
    closestUaslPoint.longitude,
    closestUaslPoint.altitude,
    closestUaslPoint.route_deviation_rate,
    ST_DistanceSphere(
        ST_Force2D(closestUaslPoint.geometry),
        ST_GeomFromText(
            'POINT(' || closestUaslPoint.longitude || ' ' || closestUaslPoint.latitude || ')',
            4326
        )
    ) AS horizontal_deviation,
    ABS(closestUaslPoint.altitude - ST_Z(closestUaslPoint.geometry)) AS vertical_deviation,
    closestUaslPoint.uasl_id,
    closestUaslPoint.uasl_section_id,
    closestUaslPoint.operator_id,
    t_remote_data.serial_number AS serial_number,
    t_remote_data.registration_id AS registration_id,
    t_remote_data.utm_id AS utm_id,
    t_remote_data.specific_session_id AS specific_session_id,
    t_remote_data.aircraft_info_id AS aircraft_info_id,
    fall.business_number AS uasl_administrator_id, -- 2024年度は航路運営者IDは非対応のため事業者番号を参照
    reservation.end_at,
    closestUaslPoint.operational_status
FROM
(
    SELECT
        droneLocation.reservation_id,
        droneLocation.get_location_timestamp,
        droneLocation.latitude,
        droneLocation.longitude,
        droneLocation.altitude,
        droneLocation.uasl_id,
        droneLocation.uasl_section_id,
        droneLocation.operator_id,
        droneLocation.ua_type,
        droneLocation.route_deviation_rate,
        droneLocation.operational_status,
        ST_Transform(
            ST_3DClosestPoint(
                asg.transformed_geometry,
                ST_Transform(
                    ST_SetSRID(ST_MakePoint(droneLocation.longitude, droneLocation.latitude, droneLocation.altitude), 4326), 
                    4978
                )
            ),
            4326
        ) AS geometry
    FROM
        safety_management.t_drone_location droneLocation
    JOIN
        transformed_uasl_section_geometry asg
    ON
        droneLocation.uasl_section_id = asg.uasl_section_id
) closestUaslPoint
JOIN uasl_design.uasl as uasl
ON uasl.uasl_id = closestUaslPoint.uasl_id
JOIN uasl_design.uasl_determination as determination
ON uasl.uasl_determination_id = determination.uasl_determination_id
JOIN uasl_design.max_fall_range as fall
ON determination.max_fall_range_id = fall.max_fall_range_id
JOIN safety_management.t_uasl_reservation reservation
ON closestUaslPoint.reservation_id = reservation.uasl_reservation_id
JOIN safety_management.t_remote_data t_remote_data
ON closestUaslPoint.reservation_id = t_remote_data.uasl_reservation_id
ORDER BY
    closestUaslPoint.reservation_id,
    closestUaslPoint.get_location_timestamp;

GRANT SELECT ON TABLE safety_management.v_uasl_design_area_info_deviation TO safety_management;

COMMENT ON VIEW safety_management.v_uasl_design_area_info_deviation IS '航路逸脱情報';
COMMENT ON COLUMN safety_management.v_uasl_design_area_info_deviation.reservation_id IS '航路予約毎の識別ID';
COMMENT ON COLUMN safety_management.v_uasl_design_area_info_deviation.get_location_timestamp IS 'テレメトリ情報取得日時';
COMMENT ON COLUMN safety_management.v_uasl_design_area_info_deviation.latitude IS '緯度';
COMMENT ON COLUMN safety_management.v_uasl_design_area_info_deviation.longitude IS '経度';
COMMENT ON COLUMN safety_management.v_uasl_design_area_info_deviation.altitude IS '標高';
COMMENT ON COLUMN safety_management.v_uasl_design_area_info_deviation.route_deviation_rate IS '航路逸脱割合';
COMMENT ON COLUMN safety_management.v_uasl_design_area_info_deviation.horizontal_deviation IS '水平逸脱距離(m)';
COMMENT ON COLUMN safety_management.v_uasl_design_area_info_deviation.vertical_deviation IS '垂直逸脱距離(m)';
COMMENT ON COLUMN safety_management.v_uasl_design_area_info_deviation.uasl_id IS '航路ID';
COMMENT ON COLUMN safety_management.v_uasl_design_area_info_deviation.uasl_section_id IS '航路区画ID';
COMMENT ON COLUMN safety_management.v_uasl_design_area_info_deviation.operator_id IS '運航者ID';
COMMENT ON COLUMN safety_management.v_uasl_design_area_info_deviation.serial_number IS 'シリアルナンバー';
COMMENT ON COLUMN safety_management.v_uasl_design_area_info_deviation.registration_id IS '登録ID';
COMMENT ON COLUMN safety_management.v_uasl_design_area_info_deviation.utm_id IS 'セッションID';
COMMENT ON COLUMN safety_management.v_uasl_design_area_info_deviation.specific_session_id IS 'フライト識別ID';
COMMENT ON COLUMN safety_management.v_uasl_design_area_info_deviation.aircraft_info_id IS '機体情報ID';
COMMENT ON COLUMN safety_management.v_uasl_design_area_info_deviation.uasl_administrator_id IS '航路運営者ID';
COMMENT ON COLUMN safety_management.v_uasl_design_area_info_deviation.end_at IS '予約終了日時';
COMMENT ON COLUMN safety_management.v_uasl_design_area_info_deviation.operational_status IS '運航状況';

-- 機体種別毎の最大風速
DROP VIEW IF EXISTS safety_management.v_uasl_design_max_wind_speed;
CREATE VIEW safety_management.v_uasl_design_max_wind_speed AS
SELECT
    dmc.aircraft_info_id,
    dmc.maker,
    dmc.model_number,
    CASE
        WHEN dmc.falling_model_type = 'ParachuteModelParameters' THEN pmp.max_wind_speed
        WHEN dmc.falling_model_type = 'SimpleWindModelParameters' THEN swmp.max_wind_speed
        ELSE NULL
    END AS max_wind_speed
FROM
    uasl_design.drone_model_config dmc
LEFT JOIN
    uasl_design.parachute_model_parameters pmp
    ON dmc.maker = pmp.maker 
    AND dmc.model_number = pmp.model_number
LEFT JOIN
    uasl_design.simple_wind_model_parameters swmp
    ON dmc.maker = swmp.maker 
    AND dmc.model_number = swmp.model_number;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE safety_management.v_uasl_design_max_wind_speed TO safety_management;

COMMENT ON VIEW safety_management.v_uasl_design_max_wind_speed IS '機体種別毎の最大風速';
COMMENT ON COLUMN safety_management.v_uasl_design_max_wind_speed.aircraft_info_id IS '機体情報ID';
COMMENT ON COLUMN safety_management.v_uasl_design_max_wind_speed.maker IS '製造メーカー名';
COMMENT ON COLUMN safety_management.v_uasl_design_max_wind_speed.model_number IS '型式（モデル）';
COMMENT ON COLUMN safety_management.v_uasl_design_max_wind_speed.max_wind_speed IS '最大風速';

