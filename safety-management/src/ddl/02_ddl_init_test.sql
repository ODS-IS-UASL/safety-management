------------------------------------------------
-- postgresユーザー(管理者権限を持つユーザー)で実行
-------------------------------------------------- 
-- ユーザとスキーマの作成
CREATE ROLE test_safety_management WITH LOGIN PASSWORD 'test_safety_management';
ALTER USER test_safety_management WITH SUPERUSER;
ALTER USER test_safety_management CREATEDB;
CREATE DATABASE  test_postgis;
GRANT CONNECT ON DATABASE test_postgis TO test_safety_management;
GRANT CREATE ON DATABASE test_postgis TO test_safety_management;

\c test_postgis;

CREATE SCHEMA IF NOT EXISTS test_safety_management;
GRANT usage ON schema test_safety_management TO test_safety_management;

set client_encoding to UTF8;

CREATE EXTENSION IF NOT EXISTS postgis;
CREATE EXTENSION IF NOT EXISTS postgis WITH SCHEMA test_safety_management;
CREATE EXTENSION IF NOT EXISTS postgis SCHEMA test_safety_management;
CREATE EXTENSION IF NOT EXISTS postgis_sfcgal;
CREATE EXTENSION IF NOT EXISTS postgis_sfcgal WITH SCHEMA test_safety_management;
CREATE EXTENSION IF NOT EXISTS postgis_sfcgal SCHEMA test_safety_management;
CREATE EXTENSION IF NOT EXISTS hstore;
CREATE EXTENSION IF NOT EXISTS hstore WITH SCHEMA test_safety_management;
CREATE EXTENSION IF NOT EXISTS hstore SCHEMA test_safety_management;
GRANT USAGE ON SCHEMA test_safety_management TO test_safety_management;
GRANT EXECUTE ON ALL FUNCTIONS IN SCHEMA test_safety_management TO test_safety_management;

-- テーブルの作成
-- ドローン位置情報
DROP TABLE IF EXISTS test_safety_management.t_drone_location;
CREATE TABLE test_safety_management.t_drone_location (
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
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE test_safety_management.t_drone_location TO test_safety_management;

COMMENT ON TABLE test_safety_management.t_drone_location IS '運行情報蓄積';
COMMENT ON COLUMN test_safety_management.t_drone_location.subscription_id IS 'エリア情報のサブスクリプション ID';
COMMENT ON COLUMN test_safety_management.t_drone_location.uas_id IS '機体の登録 ID';
COMMENT ON COLUMN test_safety_management.t_drone_location.ua_type IS '機体の種別';
COMMENT ON COLUMN test_safety_management.t_drone_location.get_location_timestamp IS 'テレメトリ情報取得日時';
COMMENT ON COLUMN test_safety_management.t_drone_location.latitude IS '緯度';
COMMENT ON COLUMN test_safety_management.t_drone_location.longitude IS '経度';
COMMENT ON COLUMN test_safety_management.t_drone_location.above_ground_level IS '対地高度';
COMMENT ON COLUMN test_safety_management.t_drone_location.track_direction IS '機体の進行方向';
COMMENT ON COLUMN test_safety_management.t_drone_location.speed IS '機体の速度';
COMMENT ON COLUMN test_safety_management.t_drone_location.vertical_speed IS '機体の垂直速度';
COMMENT ON COLUMN test_safety_management.t_drone_location.route_deviation_rate IS '航路逸脱割合';
COMMENT ON COLUMN test_safety_management.t_drone_location.route_deviation_rate_update_time IS '航路逸脱割合更新時刻';
COMMENT ON COLUMN test_safety_management.t_drone_location.reservation_id IS '航路予約 ID';
COMMENT ON COLUMN test_safety_management.t_drone_location.airway_id IS '航路 ID';
COMMENT ON COLUMN test_safety_management.t_drone_location.airway_section_id IS '航路区画 ID';
COMMENT ON COLUMN test_safety_management.t_drone_location.operational_status IS '運航状況';
COMMENT ON COLUMN test_safety_management.t_drone_location.operator_id IS '運航者 ID';
COMMENT ON COLUMN test_safety_management.t_drone_location.flight_time IS '飛行時間';

-- 航路予約情報
DROP TABLE IF EXISTS test_safety_management.t_airway_reservation;
CREATE TABLE test_safety_management.t_airway_reservation (
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
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE test_safety_management.t_airway_reservation TO test_safety_management;

COMMENT ON TABLE test_safety_management.t_airway_reservation IS '航路予約情報';
COMMENT ON COLUMN test_safety_management.t_airway_reservation.airway_reservation_id IS '航路予約毎の識別ID';
COMMENT ON COLUMN test_safety_management.t_airway_reservation.start_at IS '予約開始日時';
COMMENT ON COLUMN test_safety_management.t_airway_reservation.end_at IS '予約終了日時';
COMMENT ON COLUMN test_safety_management.t_airway_reservation.reserved_at IS '予約登録日時';
COMMENT ON COLUMN test_safety_management.t_airway_reservation.airway_section_ids IS '航路区画ID配列';
COMMENT ON COLUMN test_safety_management.t_airway_reservation.operator_id IS '運航事業者ID';
COMMENT ON COLUMN test_safety_management.t_airway_reservation.evaluation_results IS '適合性評価結果';
COMMENT ON COLUMN test_safety_management.t_airway_reservation.third_party_evaluation_results IS ' 第三者立入監視情報 適合性評価結果';
COMMENT ON COLUMN test_safety_management.t_airway_reservation.railway_operation_evaluation_results IS '鉄道運航情報 適合性評価結果';

-- リモートID紐づけ情報
DROP TABLE IF EXISTS test_safety_management.t_remote_data;
CREATE TABLE test_safety_management.t_remote_data (
    airway_reservation_id VARCHAR NOT NULL,
    serial_number VARCHAR,
    registration_id VARCHAR,
    utm_id VARCHAR,
    specific_sessoion_id VARCHAR,
    aircraft_info_id INTEGER,
    CONSTRAINT t_remote_data_pk PRIMARY KEY (airway_reservation_id)
);
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE test_safety_management.t_remote_data TO test_safety_management;

COMMENT ON TABLE test_safety_management.t_remote_data IS '航路予約情報';
COMMENT ON COLUMN test_safety_management.t_remote_data.airway_reservation_id IS '航路予約毎の識別ID';
COMMENT ON COLUMN test_safety_management.t_remote_data.serial_number IS 'シリアルナンバー';
COMMENT ON COLUMN test_safety_management.t_remote_data.registration_id IS '登録ID';
COMMENT ON COLUMN test_safety_management.t_remote_data.utm_id IS 'セッションID';
COMMENT ON COLUMN test_safety_management.t_remote_data.specific_sessoion_id IS 'フライト識別ID';
COMMENT ON COLUMN test_safety_management.t_remote_data.aircraft_info_id IS '機体情報ID';

-- サブスクリプションID紐づけ情報
DROP TABLE IF EXISTS test_safety_management.t_subscription_data;
CREATE TABLE test_safety_management.t_subscription_data (
    airway_reservation_id VARCHAR NOT NULL,
    subscription_id VARCHAR,
    area_info VARCHAR ARRAY,
    airway_id VARCHAR,
    CONSTRAINT t_subscription_data_pk PRIMARY KEY (airway_reservation_id)
);
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE test_safety_management.t_subscription_data TO test_safety_management;

COMMENT ON TABLE test_safety_management.t_subscription_data IS 'サブスクリプションID紐づけ情報';
COMMENT ON COLUMN test_safety_management.t_subscription_data.airway_reservation_id IS '航路予約毎の識別ID';
COMMENT ON COLUMN test_safety_management.t_subscription_data.subscription_id IS 'サブスクリプションID';
COMMENT ON COLUMN test_safety_management.t_subscription_data.area_info IS 'エリア情報';
COMMENT ON COLUMN test_safety_management.t_subscription_data.airway_id IS '航路ID';

-- 第三者立入監視情報
DROP TABLE IF EXISTS test_safety_management.t_monitoring_information;
CREATE TABLE test_safety_management.t_monitoring_information (
    airway_reservation_id VARCHAR NOT NULL,
    monitoring_information VARCHAR,
    airway_administrator_id VARCHAR,
    operator_id VARCHAR,
    airway_id VARCHAR,
    CONSTRAINT t_monitoring_information_pk PRIMARY KEY (airway_reservation_id,monitoring_information)
);
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE test_safety_management.t_monitoring_information TO test_safety_management;

COMMENT ON TABLE test_safety_management.t_monitoring_information IS '第三者立入監視情報';
COMMENT ON COLUMN test_safety_management.t_monitoring_information.airway_reservation_id IS '航路予約毎の識別ID';
COMMENT ON COLUMN test_safety_management.t_monitoring_information.monitoring_information IS '第三者立入監視情報';
COMMENT ON COLUMN test_safety_management.t_monitoring_information.airway_administrator_id IS '航路運営者ID';
COMMENT ON COLUMN test_safety_management.t_monitoring_information.operator_id IS '運航事業者ID';
COMMENT ON COLUMN test_safety_management.t_monitoring_information.airway_id IS '航路ID';

-- 航路逸脱情報
DROP TABLE IF EXISTS test_safety_management.t_airway_deviation;
CREATE TABLE test_safety_management.t_airway_deviation (
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
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE test_safety_management.t_airway_deviation TO test_safety_management;

COMMENT ON TABLE test_safety_management.t_airway_deviation IS '航路逸脱情報';
COMMENT ON COLUMN test_safety_management.t_airway_deviation.airway_reservation_id IS '航路予約毎の識別ID';
COMMENT ON COLUMN test_safety_management.t_airway_deviation.route_deviation_rate IS '航路逸脱情報.逸脱割合';
COMMENT ON COLUMN test_safety_management.t_airway_deviation.route_deviation_amount IS '航路逸脱情報.逸脱量';
COMMENT ON COLUMN test_safety_management.t_airway_deviation.route_deviation_time IS '逸脱検知時刻';
COMMENT ON COLUMN test_safety_management.t_airway_deviation.airway_administrator_id IS '航路運営者ID';
COMMENT ON COLUMN test_safety_management.t_airway_deviation.operator_id IS '運航事業者ID';
COMMENT ON COLUMN test_safety_management.t_airway_deviation.airway_id IS '航路ID';
COMMENT ON COLUMN test_safety_management.t_airway_deviation.aircraft_info_id IS '機体情報ID';
COMMENT ON COLUMN test_safety_management.t_airway_deviation.route_deviation_coordinates IS '逸脱検知情報.航路区画ID.逸脱発生地点の座標';

-- Viewをテスト用にテーブル化して作成
DROP TABLE IF EXISTS test_safety_management.v_airway_design_section_geometry;
CREATE TABLE test_safety_management.v_airway_design_section_geometry (
    airway_section_id VARCHAR NOT NULL,
    airway_id VARCHAR,
    name VARCHAR,
    geometry GEOMETRY,
    airway_section_solid_geometry GEOMETRY,
    CONSTRAINT v_v_airway_design_section_geometry_pk PRIMARY KEY (airway_section_id)
);
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE test_safety_management.v_airway_design_section_geometry TO test_safety_management;

DROP TABLE IF EXISTS test_safety_management.v_airway_design_area_info_section;
CREATE TABLE test_safety_management.v_airway_design_area_info_section (
    airway_section_id VARCHAR NOT NULL,
    airway_id VARCHAR,
    altitude VARCHAR,
    lat_start VARCHAR,
    lon_start VARCHAR,
    lat_end VARCHAR,
    lon_end VARCHAR,
    geometry GEOMETRY(POLYGON, 4326),
    area_info VARCHAR,
    railway_crossing_info VARCHAR,
    CONSTRAINT v_airway_design_area_info_section_pk PRIMARY KEY (airway_section_id)
);
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE test_safety_management.v_airway_design_area_info_section TO test_safety_management;

DROP TABLE IF EXISTS test_safety_management.v_airway_design_area_info_reservation;
CREATE TABLE test_safety_management.v_airway_design_area_info_reservation (
    airway_id VARCHAR NOT NULL,
    airway_administrator_id VARCHAR,
    airway_reservation_id VARCHAR,
    start_at TIMESTAMPTZ,
    end_at TIMESTAMPTZ,
    operator_id VARCHAR,
    evaluation_results BOOLEAN,
    third_party_evaluation_results BOOLEAN,
    railway_operation_evaluation_results BOOLEAN,
    altitude VARCHAR,
    lat_start VARCHAR,
    lon_start VARCHAR,
    lat_end VARCHAR,
    lon_end VARCHAR,
    geometry GEOMETRY(POLYGON, 4326),
    area_info VARCHAR,
    railway_crossing_info VARCHAR,
    CONSTRAINT v_airway_design_area_info_reservation_pk PRIMARY KEY (airway_id)
);
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE test_safety_management.v_airway_design_area_info_reservation TO test_safety_management;

DROP TABLE IF EXISTS test_safety_management.v_airway_design_area_info_operation;
CREATE TABLE test_safety_management.v_airway_design_area_info_operation (
    airway_id VARCHAR NOT NULL,
    airway_administrator_id VARCHAR,
    airway_reservation_id VARCHAR,
    start_at TIMESTAMPTZ,
    end_at TIMESTAMPTZ,
    operator_id VARCHAR,
    evaluation_results BOOLEAN,
    third_party_evaluation_results BOOLEAN,
    railway_operation_evaluation_results BOOLEAN,
    altitude VARCHAR,
    lat_start VARCHAR,
    lon_start VARCHAR,
    lat_end VARCHAR,
    lon_end VARCHAR,
    geometry GEOMETRY(POLYGON, 4326),
    area_info VARCHAR,
    railway_crossing_info VARCHAR,
    CONSTRAINT v_airway_design_area_info_operation_pk PRIMARY KEY (airway_id)
);
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE test_safety_management.v_airway_design_area_info_operation TO test_safety_management;

DROP TABLE IF EXISTS test_safety_management.v_airway_design_area_info_deviation;
CREATE TABLE test_safety_management.v_airway_design_area_info_deviation (
    reservation_id VARCHAR NOT NULL,
    get_location_timestamp TIMESTAMPTZ,
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION,
    above_ground_level INTEGER,
    route_deviation_rate DOUBLE PRECISION,
    horizontal_deviation DOUBLE PRECISION,
    vertical_deviation DOUBLE PRECISION,
    airway_id VARCHAR,
    airway_section_id VARCHAR,
    operator_id VARCHAR,
    aircraft_info_id INTEGER,
    airway_administrator_id VARCHAR,
    end_at TIMESTAMPTZ,
    CONSTRAINT v_airway_design_area_info_deviation_pk PRIMARY KEY (reservation_id)
);
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE test_safety_management.v_airway_design_area_info_deviation TO test_safety_management;