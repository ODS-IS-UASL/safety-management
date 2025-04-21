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
    above_ground_level INTEGER,
    track_direction INTEGER,
    speed DOUBLE PRECISION,
    vertical_speed DOUBLE PRECISION,
    route_deviation_rate DOUBLE PRECISION,
    route_deviation_rate_update_time TIMESTAMPTZ,
    reservation_id VARCHAR NOT NULL,
    airway_id VARCHAR,
    airway_section_id VARCHAR,
    operational_status VARCHAR,
    operator_id VARCHAR,
    flight_time VARCHAR,
    CONSTRAINT t_drone_location_pk PRIMARY KEY (reservation_id, get_location_timestamp)
);
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE safety_management.t_drone_location TO safety_management;

COMMENT ON TABLE safety_management.t_drone_location IS '運行情報蓄積';
COMMENT ON COLUMN safety_management.t_drone_location.subscription_id IS 'エリア情報のサブスクリプション ID';
COMMENT ON COLUMN safety_management.t_drone_location.uas_id IS '機体の登録 ID';
COMMENT ON COLUMN safety_management.t_drone_location.ua_type IS '機体の種別';
COMMENT ON COLUMN safety_management.t_drone_location.get_location_timestamp IS 'テレメトリ情報取得日時';
COMMENT ON COLUMN safety_management.t_drone_location.latitude IS '緯度';
COMMENT ON COLUMN safety_management.t_drone_location.longitude IS '経度';
COMMENT ON COLUMN safety_management.t_drone_location.above_ground_level IS '対地高度';
COMMENT ON COLUMN safety_management.t_drone_location.track_direction IS '機体の進行方向';
COMMENT ON COLUMN safety_management.t_drone_location.speed IS '機体の速度';
COMMENT ON COLUMN safety_management.t_drone_location.vertical_speed IS '機体の垂直速度';
COMMENT ON COLUMN safety_management.t_drone_location.route_deviation_rate IS '航路逸脱割合';
COMMENT ON COLUMN safety_management.t_drone_location.route_deviation_rate_update_time IS '航路逸脱割合更新時刻';
COMMENT ON COLUMN safety_management.t_drone_location.reservation_id IS '航路予約 ID';
COMMENT ON COLUMN safety_management.t_drone_location.airway_id IS '航路 ID';
COMMENT ON COLUMN safety_management.t_drone_location.airway_section_id IS '航路区画 ID';
COMMENT ON COLUMN safety_management.t_drone_location.operational_status IS '運航状況';
COMMENT ON COLUMN safety_management.t_drone_location.operator_id IS '運航者 ID';
COMMENT ON COLUMN safety_management.t_drone_location.flight_time IS '飛行時間';

-- 航路予約情報
DROP TABLE IF EXISTS safety_management.t_airway_reservation;
CREATE TABLE safety_management.t_airway_reservation (
    airway_reservation_id VARCHAR NOT NULL,
    start_at TIMESTAMPTZ,
    end_at TIMESTAMPTZ,
    reserved_at TIMESTAMPTZ,
    airway_section_ids VARCHAR ARRAY,
    operator_id VARCHAR,
    evaluation_results BOOLEAN,
    third_party_evaluation_results BOOLEAN,
    railway_operation_evaluation_results BOOLEAN,
    CONSTRAINT t_airway_reservation_pk PRIMARY KEY (airway_reservation_id)
);
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE safety_management.t_airway_reservation TO safety_management;

COMMENT ON TABLE safety_management.t_airway_reservation IS '航路予約情報';
COMMENT ON COLUMN safety_management.t_airway_reservation.airway_reservation_id IS '航路予約毎の識別ID';
COMMENT ON COLUMN safety_management.t_airway_reservation.start_at IS '予約開始日時';
COMMENT ON COLUMN safety_management.t_airway_reservation.end_at IS '予約終了日時';
COMMENT ON COLUMN safety_management.t_airway_reservation.reserved_at IS '予約登録日時';
COMMENT ON COLUMN safety_management.t_airway_reservation.airway_section_ids IS '航路区画ID配列';
COMMENT ON COLUMN safety_management.t_airway_reservation.operator_id IS '運航事業者ID';
COMMENT ON COLUMN safety_management.t_airway_reservation.evaluation_results IS '適合性評価結果';
COMMENT ON COLUMN safety_management.t_airway_reservation.third_party_evaluation_results IS ' 第三者立入監視情報 適合性評価結果';
COMMENT ON COLUMN safety_management.t_airway_reservation.railway_operation_evaluation_results IS '鉄道運航情報 適合性評価結果';

-- リモートID紐づけ情報
DROP TABLE IF EXISTS safety_management.t_remote_data;
CREATE TABLE safety_management.t_remote_data (
    airway_reservation_id VARCHAR NOT NULL,
    serial_number VARCHAR,
    registration_id VARCHAR,
    utm_id VARCHAR,
    specific_sessoion_id VARCHAR,
    aircraft_info_id INTEGER,
    CONSTRAINT t_remote_data_pk PRIMARY KEY (airway_reservation_id)
);
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE safety_management.t_remote_data TO safety_management;

COMMENT ON TABLE safety_management.t_remote_data IS '航路予約情報';
COMMENT ON COLUMN safety_management.t_remote_data.airway_reservation_id IS '航路予約毎の識別ID';
COMMENT ON COLUMN safety_management.t_remote_data.serial_number IS 'シリアルナンバー';
COMMENT ON COLUMN safety_management.t_remote_data.registration_id IS '登録ID';
COMMENT ON COLUMN safety_management.t_remote_data.utm_id IS 'セッションID';
COMMENT ON COLUMN safety_management.t_remote_data.specific_sessoion_id IS 'フライト識別ID';
COMMENT ON COLUMN safety_management.t_remote_data.aircraft_info_id IS '機体情報ID';

-- サブスクリプションID紐づけ情報
DROP TABLE IF EXISTS safety_management.t_subscription_data;
CREATE TABLE safety_management.t_subscription_data (
    airway_reservation_id VARCHAR NOT NULL,
    subscription_id VARCHAR,
    area_info VARCHAR ARRAY,
    airway_id VARCHAR,
    CONSTRAINT t_subscription_data_pk PRIMARY KEY (airway_reservation_id)
);
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE safety_management.t_subscription_data TO safety_management;

COMMENT ON TABLE safety_management.t_subscription_data IS 'サブスクリプションID紐づけ情報';
COMMENT ON COLUMN safety_management.t_subscription_data.airway_reservation_id IS '航路予約毎の識別ID';
COMMENT ON COLUMN safety_management.t_subscription_data.subscription_id IS 'サブスクリプションID';
COMMENT ON COLUMN safety_management.t_subscription_data.area_info IS 'エリア情報';
COMMENT ON COLUMN safety_management.t_subscription_data.airway_id IS '航路ID';

-- 第三者立入監視情報
DROP TABLE IF EXISTS safety_management.t_monitoring_information;
CREATE TABLE safety_management.t_monitoring_information (
    airway_reservation_id VARCHAR NOT NULL,
    monitoring_information VARCHAR,
    airway_administrator_id VARCHAR,
    operator_id VARCHAR,
    airway_id VARCHAR
);
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE safety_management.t_monitoring_information TO safety_management;

COMMENT ON TABLE safety_management.t_monitoring_information IS '第三者立入監視情報';
COMMENT ON COLUMN safety_management.t_monitoring_information.airway_reservation_id IS '航路予約毎の識別ID';
COMMENT ON COLUMN safety_management.t_monitoring_information.monitoring_information IS '第三者立入監視情報';
COMMENT ON COLUMN safety_management.t_monitoring_information.airway_administrator_id IS '航路運営者ID';
COMMENT ON COLUMN safety_management.t_monitoring_information.operator_id IS '運航事業者ID';
COMMENT ON COLUMN safety_management.t_monitoring_information.airway_id IS '航路ID';

-- 航路逸脱情報
DROP TABLE IF EXISTS safety_management.t_airway_deviation;
CREATE TABLE safety_management.t_airway_deviation (
    airway_reservation_id VARCHAR NOT NULL,
    route_deviation_rate DOUBLE PRECISION,
    route_deviation_amount VARCHAR,
    route_deviation_time JSON,
    airway_administrator_id VARCHAR,
    operator_id VARCHAR,
    airway_id VARCHAR,
    aircraft_info_id INTEGER,
    route_deviation_coordinates JSON,
    CONSTRAINT t_airway_deviation_pk PRIMARY KEY (airway_reservation_id)
);
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE safety_management.t_airway_deviation TO safety_management;

COMMENT ON TABLE safety_management.t_airway_deviation IS '航路逸脱情報';
COMMENT ON COLUMN safety_management.t_airway_deviation.airway_reservation_id IS '航路予約毎の識別ID';
COMMENT ON COLUMN safety_management.t_airway_deviation.route_deviation_rate IS '航路逸脱情報.逸脱割合';
COMMENT ON COLUMN safety_management.t_airway_deviation.route_deviation_amount IS '航路逸脱情報.逸脱量';
COMMENT ON COLUMN safety_management.t_airway_deviation.route_deviation_time IS '逸脱検知時刻';
COMMENT ON COLUMN safety_management.t_airway_deviation.airway_administrator_id IS '航路運営者ID';
COMMENT ON COLUMN safety_management.t_airway_deviation.operator_id IS '運航事業者ID';
COMMENT ON COLUMN safety_management.t_airway_deviation.airway_id IS '航路ID';
COMMENT ON COLUMN safety_management.t_airway_deviation.aircraft_info_id IS '機体情報ID';
COMMENT ON COLUMN safety_management.t_airway_deviation.route_deviation_coordinates IS '逸脱検知情報.航路区画ID.逸脱発生地点の座標';

-- Viewの作成
-- 航路区画IDをPKと考えた場合の航路情報
DROP VIEW IF EXISTS safety_management.v_airway_design_section_geometry;
CREATE VIEW safety_management.v_airway_design_section_geometry AS
SELECT
    asct.airway_section_id as airway_section_id,
    ary.airway_id,
    asct.name,
    ST_Force3D(ST_SetSRID(ST_Collect(aj.geometry), 4326)) AS geometry,
    (SELECT ST_MakeSolid(ST_GeomFromText('POLYHEDRALSURFACE Z (
        ((' || b || ', ' || f || ', ' || g || ', ' || c || ', ' || b || ')),
        ((' || a || ', ' || d || ', ' || h || ', ' || e || ', ' || a || ')),
        ((' || b || ', ' || a || ', ' || e || ', ' || f || ', ' || b || ')),
        ((' || f || ', ' || e || ', ' || h || ', ' || g || ', ' || f || ')),
        ((' || g || ', ' || h || ', ' || d || ', ' || c || ', ' || g || ')),
        ((' || c || ', ' || d || ', ' || a || ', ' || b || ', ' || c || '))
     )')) As airway_section_solid_geometry
     FROM (
        WITH numbered_points AS (
            SELECT (g).path, 
                   regexp_replace(ST_AsText((g).geom), '^POINT Z \((.+)\)$', '\1') AS coords,
                   row_number() OVER (ORDER BY (g).path ASC) AS rn
            FROM (
              SELECT ST_DumpPoints(ST_Force3D(ST_SetSRID(ST_Collect(aj.geometry), 4326))) AS g
            )
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
     )
    )
FROM 
    airway_design.airway_section asct
JOIN 
    airway_design.mapping_junction_section mapjs ON asct.airway_section_id = mapjs.airway_section_id
JOIN 
    airway_design.airway_junction aj ON mapjs.airway_junction_id = aj.airway_junction_id
JOIN 
    airway_design.airway ary ON asct.airway_id = ary.airway_id
GROUP BY
    asct.airway_section_id, ary.airway_id, asct.name;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE safety_management.v_airway_design_section_geometry TO safety_management;

COMMENT ON VIEW safety_management.v_airway_design_section_geometry IS '航路区画情報';
COMMENT ON COLUMN safety_management.v_airway_design_section_geometry.airway_section_id IS '航路区画ID';
COMMENT ON COLUMN safety_management.v_airway_design_section_geometry.airway_id IS '航路ID';
COMMENT ON COLUMN safety_management.v_airway_design_section_geometry.name IS '航路区画名';
COMMENT ON COLUMN safety_management.v_airway_design_section_geometry.geometry IS '航路区画のジオメトリ';
COMMENT ON COLUMN safety_management.v_airway_design_section_geometry.airway_section_solid_geometry IS '航路区画の立体ジオメトリ';

-- 航路区画IDをPKと考えた場合の航路エリア情報
DROP VIEW IF EXISTS safety_management.v_airway_design_area_info_section;
CREATE VIEW safety_management.v_airway_design_area_info_section AS
SELECT
    section.airway_section_id as airway_section_id,
    airway.airway_id as airway_id,
    '150.0' AS altitude,
    TO_CHAR(ST_YMax(ST_Envelope(fall.geometry)), 'FM999999999.0') AS lat_start,
    TO_CHAR(ST_XMin(ST_Envelope(fall.geometry)), 'FM999999999.0') AS lon_start,
    TO_CHAR(ST_YMin(ST_Envelope(fall.geometry)), 'FM999999999.0') AS lat_end,
    TO_CHAR(ST_XMax(ST_Envelope(fall.geometry)), 'FM999999999.0') AS lon_end,
    fall.geometry::geometry as geometry,
    ST_AsGeoJSON(fall.geometry) as area_info,
    json_agg(row_to_json(railway)):: text as railway_crossing_info
FROM
    airway_design.airway as airway
    join airway_design.airway_section as section
    on (airway.airway_id = section.airway_id)
    join airway_design.airway_determination as determination
    on (airway.airway_determination_id = determination.airway_determination_id)
    join airway_design.fall_tolerance_range as fall
    on (determination.fall_tolerance_range_id = fall.fall_tolerance_range_id)
    left join airway_design.railway_crossing_info as railway
    on (airway.airway_id = railway.airway_id)
GROUP BY
    section.airway_section_id, airway.airway_id, fall.geometry;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE safety_management.v_airway_design_area_info_section TO safety_management;

COMMENT ON VIEW safety_management.v_airway_design_area_info_section IS '航路エリア情報_航路区間';
COMMENT ON COLUMN safety_management.v_airway_design_area_info_section.airway_section_id IS '航路区画ID';
COMMENT ON COLUMN safety_management.v_airway_design_area_info_section.airway_id IS '航路ID';
COMMENT ON COLUMN safety_management.v_airway_design_area_info_section.altitude IS '対地高度';
COMMENT ON COLUMN safety_management.v_airway_design_area_info_section.lat_start IS '北西端緯度';
COMMENT ON COLUMN safety_management.v_airway_design_area_info_section.lon_start IS '北西端経度';
COMMENT ON COLUMN safety_management.v_airway_design_area_info_section.lat_end IS '南東端緯度';
COMMENT ON COLUMN safety_management.v_airway_design_area_info_section.lon_end IS '南東端経度';
COMMENT ON COLUMN safety_management.v_airway_design_area_info_section.geometry IS 'エリア情報(最大落下許容範囲)';
COMMENT ON COLUMN safety_management.v_airway_design_area_info_section.area_info IS 'エリア情報をJSON文字列にしたもの';
COMMENT ON COLUMN safety_management.v_airway_design_area_info_section.railway_crossing_info IS '鉄道との交点情報';

-- 航路エリア情報(予約データ)
DROP VIEW IF EXISTS safety_management.v_airway_design_area_info_reservation;
CREATE VIEW safety_management.v_airway_design_area_info_reservation AS
SELECT
    MAX(airway.airway_id) as airway_id,
    fall.business_number as airway_administrator_id, -- 2024年度は航路運営者IDは非対応のため事業者番号を参照
    reservation.airway_reservation_id as airway_reservation_id,
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
    airway_design.airway as airway
    join airway_design.airway_section as section
    on (airway.airway_id = section.airway_id)
    join airway_design.airway_determination as determination
    on (airway.airway_determination_id = determination.airway_determination_id)
    join airway_design.fall_tolerance_range as fall
    on (determination.fall_tolerance_range_id = fall.fall_tolerance_range_id)
    left join  airway_design.railway_crossing_info as railway
    on (airway.airway_id = railway.airway_id)
    join (
      select  distinct on (airway_reservation_id)
        airway_reservation_id, start_at, end_at, reserved_at, UNNEST(airway_section_ids) AS airway_section_id, operator_id, evaluation_results, third_party_evaluation_results, railway_operation_evaluation_results
      from safety_management.t_airway_reservation
      where start_at > now()
    ) reservation
    on section.airway_section_id = reservation.airway_section_id
GROUP BY
    fall.business_number, reservation.airway_reservation_id, reservation.start_at, reservation.end_at, reservation.operator_id, evaluation_results, third_party_evaluation_results, railway_operation_evaluation_results;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE safety_management.v_airway_design_area_info_reservation TO safety_management;

COMMENT ON VIEW safety_management.v_airway_design_area_info_reservation IS '航路エリア情報_予約データ';
COMMENT ON COLUMN safety_management.v_airway_design_area_info_reservation.airway_id IS '航路ID';
COMMENT ON COLUMN safety_management.v_airway_design_area_info_reservation.airway_administrator_id IS '航路運営者ID';
COMMENT ON COLUMN safety_management.v_airway_design_area_info_reservation.airway_reservation_id IS '航路予約毎の識別ID';
COMMENT ON COLUMN safety_management.v_airway_design_area_info_reservation.start_at IS '予約開始日時';
COMMENT ON COLUMN safety_management.v_airway_design_area_info_reservation.end_at IS '予約終了日時';
COMMENT ON COLUMN safety_management.v_airway_design_area_info_reservation.operator_id IS '運航者ID';
COMMENT ON COLUMN safety_management.v_airway_design_area_info_reservation.evaluation_results IS '適合性評価結果';
COMMENT ON COLUMN safety_management.v_airway_design_area_info_reservation.third_party_evaluation_results IS ' 第三者立入監視情報 適合性評価結果';
COMMENT ON COLUMN safety_management.v_airway_design_area_info_reservation.railway_operation_evaluation_results IS '鉄道運航情報 適合性評価結果';
COMMENT ON COLUMN safety_management.v_airway_design_area_info_reservation.altitude IS '対地高度';
COMMENT ON COLUMN safety_management.v_airway_design_area_info_reservation.lat_start IS '北西端緯度';
COMMENT ON COLUMN safety_management.v_airway_design_area_info_reservation.lon_start IS '北西端経度';
COMMENT ON COLUMN safety_management.v_airway_design_area_info_reservation.lat_end IS '南東端緯度';
COMMENT ON COLUMN safety_management.v_airway_design_area_info_reservation.lon_end IS '南東端経度';
COMMENT ON COLUMN safety_management.v_airway_design_area_info_reservation.geometry IS 'エリア情報(最大落下許容範囲)';
COMMENT ON COLUMN safety_management.v_airway_design_area_info_reservation.area_info IS 'エリア情報をJSON文字列にしたもの';
COMMENT ON COLUMN safety_management.v_airway_design_area_info_reservation.railway_crossing_info IS '鉄道との交点情報';

-- 航路エリア情報(運航中データ)
DROP VIEW IF EXISTS safety_management.v_airway_design_area_info_operation;
CREATE VIEW safety_management.v_airway_design_area_info_operation AS
SELECT
    MAX(airway.airway_id) as airway_id,
    fall.business_number as airway_administrator_id, -- 2024年度は航路運営者IDは非対応のため事業者番号を参照
    reservation.airway_reservation_id as airway_reservation_id,
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
    airway_design.airway as airway
    join airway_design.airway_section as section
    on (airway.airway_id = section.airway_id)
    join airway_design.airway_determination as determination
    on (airway.airway_determination_id = determination.airway_determination_id)
    join airway_design.fall_tolerance_range as fall
    on (determination.fall_tolerance_range_id = fall.fall_tolerance_range_id)
    left join  airway_design.railway_crossing_info as railway
    on (airway.airway_id = railway.airway_id)
    join (
      select  distinct on (airway_reservation_id)
        airway_reservation_id, start_at, end_at, reserved_at, UNNEST(airway_section_ids) AS airway_section_id, operator_id, evaluation_results, third_party_evaluation_results, railway_operation_evaluation_results
      from safety_management.t_airway_reservation
      where start_at <= now() and end_at >= now()
    ) reservation
    on section.airway_section_id = reservation.airway_section_id
GROUP BY
     fall.business_number, reservation.airway_reservation_id, reservation.start_at, reservation.end_at, reservation.operator_id, evaluation_results, third_party_evaluation_results, railway_operation_evaluation_results;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE safety_management.v_airway_design_area_info_operation TO safety_management;

COMMENT ON VIEW safety_management.v_airway_design_area_info_operation IS '航路エリア情報_運航中データ';
COMMENT ON COLUMN safety_management.v_airway_design_area_info_operation.airway_id IS '航路ID';
COMMENT ON COLUMN safety_management.v_airway_design_area_info_operation.airway_administrator_id IS '航路運営者ID';
COMMENT ON COLUMN safety_management.v_airway_design_area_info_operation.airway_reservation_id IS '航路予約毎の識別ID';
COMMENT ON COLUMN safety_management.v_airway_design_area_info_operation.start_at IS '予約開始日時';
COMMENT ON COLUMN safety_management.v_airway_design_area_info_operation.end_at IS '予約終了日時';
COMMENT ON COLUMN safety_management.v_airway_design_area_info_operation.operator_id IS '運航者ID';
COMMENT ON COLUMN safety_management.v_airway_design_area_info_operation.evaluation_results IS '適合性評価結果';
COMMENT ON COLUMN safety_management.v_airway_design_area_info_operation.third_party_evaluation_results IS ' 第三者立入監視情報 適合性評価結果';
COMMENT ON COLUMN safety_management.v_airway_design_area_info_operation.railway_operation_evaluation_results IS '鉄道運航情報 適合性評価結果';
COMMENT ON COLUMN safety_management.v_airway_design_area_info_operation.altitude IS '対地高度';
COMMENT ON COLUMN safety_management.v_airway_design_area_info_operation.lat_start IS '北西端緯度';
COMMENT ON COLUMN safety_management.v_airway_design_area_info_operation.lon_start IS '北西端経度';
COMMENT ON COLUMN safety_management.v_airway_design_area_info_operation.lat_end IS '南東端緯度';
COMMENT ON COLUMN safety_management.v_airway_design_area_info_operation.lon_end IS '南東端経度';
COMMENT ON COLUMN safety_management.v_airway_design_area_info_operation.geometry IS 'エリア情報(最大落下許容範囲)';
COMMENT ON COLUMN safety_management.v_airway_design_area_info_operation.area_info IS 'エリア情報をJSON文字列にしたもの';
COMMENT ON COLUMN safety_management.v_airway_design_area_info_operation.railway_crossing_info IS '鉄道との交点情報';

-- 航路逸脱情報（航路逸脱情報テーブル登録用データ)
DROP VIEW IF EXISTS safety_management.v_airway_design_area_info_deviation;
CREATE VIEW safety_management.v_airway_design_area_info_deviation AS
SELECT
    closestAirwayPoint.reservation_id,
    closestAirwayPoint.get_location_timestamp,
    closestAirwayPoint.latitude,
    closestAirwayPoint.longitude,
    closestAirwayPoint.above_ground_level,
    closestAirwayPoint.route_deviation_rate,
    ST_DistanceSphere(
        ST_Force2D(closestAirwayPoint.geometry),
        ST_GeomFromText(
            'POINT(' || closestAirwayPoint.longitude || ' ' || closestAirwayPoint.latitude || ')',
            4326
        )
    ) AS horizontal_deviation,
    ABS(closestAirwayPoint.above_ground_level - ST_Z(closestAirwayPoint.geometry)) AS vertical_deviation,
    closestAirwayPoint.airway_id,
    closestAirwayPoint.airway_section_id,
    closestAirwayPoint.operator_id, 
    t_remote_data.aircraft_info_id AS aircraft_info_id,
    fall.business_number AS airway_administrator_id, -- 2024年度は航路運営者IDは非対応のため事業者番号を参照
    reservation.end_at,
    closestAirwayPoint.operational_status
FROM
(
    SELECT
        droneLocation.reservation_id,
        droneLocation.get_location_timestamp,
        droneLocation.latitude,
        droneLocation.longitude,
        droneLocation.above_ground_level,
        droneLocation.airway_id,
        droneLocation.airway_section_id,
        droneLocation.operator_id,
        droneLocation.ua_type,
        droneLocation.route_deviation_rate,
        droneLocation.operational_status,
        ST_3DClosestPoint(
            asg.geometry,
            ST_GeomFromText(
                'POINT(' || droneLocation.longitude || ' ' || droneLocation.latitude || ' ' || droneLocation.above_ground_level || ')',
                4326
            )
        ) AS geometry
    FROM
        safety_management.t_drone_location droneLocation
    JOIN
        airway_design.airway_section_geometry asg
    ON
        droneLocation.airway_section_id = asg.airway_section_id
) closestAirwayPoint
JOIN airway_design.airway as airway
ON airway.airway_id = closestAirwayPoint.airway_id
JOIN airway_design.airway_determination as determination
ON airway.airway_determination_id = determination.airway_determination_id
JOIN airway_design.fall_tolerance_range as fall
ON determination.fall_tolerance_range_id = fall.fall_tolerance_range_id
JOIN safety_management.t_airway_reservation reservation
ON closestAirwayPoint.reservation_id = reservation.airway_reservation_id
JOIN safety_management.t_remote_data t_remote_data
ON closestAirwayPoint.reservation_id = t_remote_data.airway_reservation_id
ORDER BY
    closestAirwayPoint.reservation_id,
    closestAirwayPoint.get_location_timestamp;

GRANT SELECT ON TABLE safety_management.v_airway_design_area_info_deviation TO safety_management;

COMMENT ON VIEW safety_management.v_airway_design_area_info_deviation IS '航路逸脱情報';
COMMENT ON COLUMN safety_management.v_airway_design_area_info_deviation.reservation_id IS '航路予約毎の識別ID';
COMMENT ON COLUMN safety_management.v_airway_design_area_info_deviation.get_location_timestamp IS 'テレメトリ情報取得日時';
COMMENT ON COLUMN safety_management.v_airway_design_area_info_deviation.latitude IS '緯度';
COMMENT ON COLUMN safety_management.v_airway_design_area_info_deviation.longitude IS '経度';
COMMENT ON COLUMN safety_management.v_airway_design_area_info_deviation.above_ground_level IS '対地高度';
COMMENT ON COLUMN safety_management.v_airway_design_area_info_deviation.route_deviation_rate IS '航路逸脱割合';
COMMENT ON COLUMN safety_management.v_airway_design_area_info_deviation.horizontal_deviation IS '水平逸脱距離(m)';
COMMENT ON COLUMN safety_management.v_airway_design_area_info_deviation.vertical_deviation IS '垂直逸脱距離(m)';
COMMENT ON COLUMN safety_management.v_airway_design_area_info_deviation.airway_id IS '航路ID';
COMMENT ON COLUMN safety_management.v_airway_design_area_info_deviation.airway_section_id IS '航路区画ID';
COMMENT ON COLUMN safety_management.v_airway_design_area_info_deviation.operator_id IS '運航者ID';
COMMENT ON COLUMN safety_management.v_airway_design_area_info_deviation.aircraft_info_id IS '機体情報ID';
COMMENT ON COLUMN safety_management.v_airway_design_area_info_deviation.airway_administrator_id IS '航路運営者ID';
COMMENT ON COLUMN safety_management.v_airway_design_area_info_deviation.end_at IS '予約終了日時';
COMMENT ON COLUMN safety_management.v_airway_design_area_info_deviation.operational_status IS '運航状況';