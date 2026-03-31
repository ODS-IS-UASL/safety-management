\c postgres;

set client_encoding to UTF8;

-- テーブルの作成
CREATE EXTENSION IF NOT EXISTS postgis;
CREATE EXTENSION IF NOT EXISTS postgis WITH SCHEMA uasl_design;
CREATE EXTENSION IF NOT EXISTS postgis_raster;
CREATE EXTENSION IF NOT EXISTS postgis_raster WITH SCHEMA uasl_design;
CREATE EXTENSION IF NOT EXISTS postgis_sfcgal;
CREATE EXTENSION IF NOT EXISTS postgis_sfcgal WITH SCHEMA uasl_design;

DROP TABLE IF EXISTS uasl_design.locations;
CREATE TABLE uasl_design.locations (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100),
    geom GEOMETRY(Point, 4326)
);
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE uasl_design.locations TO uasl_design;

DROP TABLE IF EXISTS uasl_design.max_fall_range;

CREATE TABLE uasl_design.max_fall_range(
	max_fall_range_id varchar(36) PRIMARY KEY,
	business_number varchar(200) NOT NULL,
	uasl_operator_id varchar(200) NOT NULL,
	name varchar(200),
	area_name varchar(200),
	type_id varchar(1) NOT NULL,
	region_id varchar(1) NOT NULL,
	elevation_terrain VARCHAR(200),
	geometry GEOMETRY(POLYGON, 4326),
	delete BOOLEAN NOT NULL DEFAULT FALSE,
	created_at timestamp default CURRENT_TIMESTAMP,
	updated_at timestamp default CURRENT_TIMESTAMP
);

GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE uasl_design.max_fall_range TO uasl_design;

COMMENT ON TABLE uasl_design.max_fall_range IS '最大落下範囲';
COMMENT ON COLUMN uasl_design.max_fall_range.max_fall_range_id IS '最大落下範囲ID';
COMMENT ON COLUMN uasl_design.max_fall_range.business_number IS '事業者番号';
COMMENT ON COLUMN uasl_design.max_fall_range.uasl_operator_id IS '航路運営者ID';
COMMENT ON COLUMN uasl_design.max_fall_range.name IS '名称';
COMMENT ON COLUMN uasl_design.max_fall_range.area_name IS 'エリア名';
COMMENT ON COLUMN uasl_design.max_fall_range.type_id IS '系統ID';
COMMENT ON COLUMN uasl_design.max_fall_range.region_id IS '地域ID';
COMMENT ON COLUMN uasl_design.max_fall_range.elevation_terrain IS '最大標高・地形';
COMMENT ON COLUMN uasl_design.max_fall_range.geometry  IS 'ジオメトリ';
COMMENT ON COLUMN uasl_design.max_fall_range.delete IS '削除';
COMMENT ON COLUMN uasl_design.max_fall_range.created_at  IS '登録日';
COMMENT ON COLUMN uasl_design.max_fall_range.updated_at  IS '更新日';

DROP TABLE IF EXISTS uasl_design.uasl_determination;

CREATE TABLE uasl_design.uasl_determination(
	uasl_determination_id int PRIMARY KEY,
	business_number varchar(200) NOT NULL,
	max_fall_range_id varchar(36) REFERENCES uasl_design.max_fall_range(max_fall_range_id)  NOT NULL,
	num_cross_section_divisions int NOT NULL,
	delete BOOLEAN NOT NULL DEFAULT FALSE,
	created_at timestamp default CURRENT_TIMESTAMP,
	updated_at timestamp default CURRENT_TIMESTAMP
);

GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE uasl_design.uasl_determination TO uasl_design;

COMMENT ON TABLE uasl_design.uasl_determination IS '航路画定';
COMMENT ON COLUMN uasl_design.uasl_determination.uasl_determination_id IS '航路画定ID';
COMMENT ON COLUMN uasl_design.uasl_determination.business_number IS '事業者番号';
COMMENT ON COLUMN uasl_design.uasl_determination.max_fall_range_id IS '最大落下許容範囲ID';
COMMENT ON COLUMN uasl_design.uasl_determination.num_cross_section_divisions IS '断面分割数';
COMMENT ON COLUMN uasl_design.uasl_determination.delete IS '削除';
COMMENT ON COLUMN uasl_design.uasl_determination.created_at IS '登録日';
COMMENT ON COLUMN uasl_design.uasl_determination.updated_at IS '更新日';


DROP TABLE IF EXISTS uasl_design.uasl_compatible_models;

CREATE TABLE uasl_design.uasl_compatible_models(
	uasl_compatible_models_id int PRIMARY KEY,
	uasl_determination_id int REFERENCES uasl_design.uasl_determination(uasl_determination_id)  NOT NULL,
	aircraft_info_id int NOT NULL,
	maker varchar(200) NOT NULL,
	model_number varchar(200) NOT NULL,
	name varchar(200) NOT NULL,
	type varchar(100) NOT NULL,
	ip varchar(100) NOT NULL,
	length int NOT NULL,
	weight int NOT NULL,
	maximum_takeoff_weight int NOT NULL,
	maximum_flight_time int NOT NULL,
	created_at timestamp default CURRENT_TIMESTAMP,
	updated_at timestamp default CURRENT_TIMESTAMP
);

GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE uasl_design.uasl_compatible_models TO uasl_design;

COMMENT ON TABLE uasl_design.uasl_compatible_models IS '航路対応機種';
COMMENT ON COLUMN uasl_design.uasl_compatible_models.uasl_compatible_models_id IS '対応機種ID';
COMMENT ON COLUMN uasl_design.uasl_compatible_models.aircraft_info_id IS '機体情報ID';
COMMENT ON COLUMN uasl_design.uasl_compatible_models.uasl_determination_id IS '航路画定ID';
COMMENT ON COLUMN uasl_design.uasl_compatible_models.maker IS '製造メーカー名';
COMMENT ON COLUMN uasl_design.uasl_compatible_models.model_number IS '型式（モデル）';
COMMENT ON COLUMN uasl_design.uasl_compatible_models.name IS '機種名';
COMMENT ON COLUMN uasl_design.uasl_compatible_models.type IS '機体種別';
COMMENT ON COLUMN uasl_design.uasl_compatible_models.ip IS 'IP番号';
COMMENT ON COLUMN uasl_design.uasl_compatible_models.length IS '機体長';
COMMENT ON COLUMN uasl_design.uasl_compatible_models.weight IS '重量';
COMMENT ON COLUMN uasl_design.uasl_compatible_models.maximum_takeoff_weight IS '最大離陸重量';
COMMENT ON COLUMN uasl_design.uasl_compatible_models.maximum_flight_time IS '最大飛行時間';
COMMENT ON COLUMN uasl_design.uasl_compatible_models.created_at  IS '登録日';
COMMENT ON COLUMN uasl_design.uasl_compatible_models.updated_at  IS '更新日';


DROP TABLE IF EXISTS uasl_design.fall_range_seam;

CREATE TABLE uasl_design.fall_range_seam(
	fall_range_seam_id int PRIMARY KEY,
	uasl_determination_id int REFERENCES uasl_design.uasl_determination(uasl_determination_id)  NOT NULL,
	name varchar(200),
	geometry GEOMETRY(LINESTRING, 4326) NOT NULL,
	created_at timestamp default CURRENT_TIMESTAMP,
	updated_at timestamp default CURRENT_TIMESTAMP
);

GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE uasl_design.fall_range_seam TO uasl_design;

COMMENT ON COLUMN uasl_design.fall_range_seam.fall_range_seam_id IS '落下範囲節ID';
COMMENT ON COLUMN uasl_design.fall_range_seam.uasl_determination_id IS '航路画定ID';
COMMENT ON COLUMN uasl_design.fall_range_seam.name IS '名称';
COMMENT ON COLUMN uasl_design.fall_range_seam.geometry IS 'ジオメトリ';
COMMENT ON COLUMN uasl_design.fall_range_seam.created_at IS '登録日';
COMMENT ON COLUMN uasl_design.fall_range_seam.updated_at IS '更新日';




DROP TABLE IF EXISTS uasl_design.feasible_vol_seam;

CREATE TABLE uasl_design.feasible_vol_seam(
	feasible_vol_seam_id int PRIMARY KEY,
	uasl_determination_id int REFERENCES uasl_design.uasl_determination(uasl_determination_id)  NOT NULL,
	fall_range_seam_id int REFERENCES uasl_design.fall_range_seam(fall_range_seam_id),
	data text NOT NULL,
	created_at timestamp default CURRENT_TIMESTAMP,
	updated_at timestamp default CURRENT_TIMESTAMP
);

GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE uasl_design.feasible_vol_seam TO uasl_design;

COMMENT ON TABLE uasl_design.feasible_vol_seam IS '航路設定可能空間節';
COMMENT ON COLUMN uasl_design.feasible_vol_seam.feasible_vol_seam_id IS '航路設定可能空間節ID';
COMMENT ON COLUMN uasl_design.feasible_vol_seam.uasl_determination_id IS '航路画定ID';
COMMENT ON COLUMN uasl_design.feasible_vol_seam.fall_range_seam_id IS '落下範囲節ID';
COMMENT ON COLUMN uasl_design.feasible_vol_seam.data IS 'データ';
COMMENT ON COLUMN uasl_design.feasible_vol_seam.created_at  IS '登録日';
COMMENT ON COLUMN uasl_design.feasible_vol_seam.updated_at  IS '更新日';

DROP TABLE IF EXISTS uasl_design.uasl;

CREATE TABLE uasl_design.uasl(
	uasl_id varchar(250) PRIMARY KEY,
	uasl_determination_id int REFERENCES uasl_design.uasl_determination(uasl_determination_id)  NOT NULL,
	name varchar(200),
	parent_node_uasl_id varchar(250),
	uasl_operator_id varchar(200),
	flight_purpose varchar(200),
	external_guarantee BOOLEAN NOT NULL DEFAULT FALSE,
	created_at timestamp default CURRENT_TIMESTAMP,
	updated_at timestamp default CURRENT_TIMESTAMP
);

GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE uasl_design.uasl TO uasl_design;

COMMENT ON TABLE uasl_design.uasl IS '航路';
COMMENT ON COLUMN uasl_design.uasl.uasl_id IS '航路ID';
COMMENT ON COLUMN uasl_design.uasl.uasl_determination_id IS '航路画定ID';
COMMENT ON COLUMN uasl_design.uasl.name IS '名称';
COMMENT ON COLUMN uasl_design.uasl.parent_node_uasl_id IS '親ノード航路ID';
COMMENT ON COLUMN uasl_design.uasl.uasl_operator_id IS '航路運営者ID';
COMMENT ON COLUMN uasl_design.uasl.flight_purpose IS '飛行目的';
COMMENT ON COLUMN uasl_design.uasl.external_guarantee IS '外部保証フラグ';
COMMENT ON COLUMN uasl_design.uasl.created_at IS '登録日';
COMMENT ON COLUMN uasl_design.uasl.updated_at IS '更新日';


DROP TABLE IF EXISTS uasl_design.uasl_point;

CREATE TABLE uasl_design.uasl_point(
	uasl_point_id varchar(36) PRIMARY KEY,
	fall_range_seam_id int REFERENCES uasl_design.fall_range_seam(fall_range_seam_id),
	name varchar(200),
	geometry GEOMETRY(POLYGONZ, 4326) NOT NULL,
	deviation_geometry  GEOMETRY(POLYGONZ, 4326) NOT NULL,
	external_guarantee BOOLEAN NOT NULL DEFAULT FALSE,
	external_system_id  varchar(200) DEFAULT NULL,
	external_system_uasl_point_id  varchar(36) DEFAULT NULL,
	created_at timestamp default CURRENT_TIMESTAMP,
	updated_at timestamp default CURRENT_TIMESTAMP
);

GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE uasl_design.uasl_point TO uasl_design;

COMMENT ON TABLE uasl_design.uasl_point IS '航路点';
COMMENT ON COLUMN uasl_design.uasl_point.uasl_point_id IS '航路点ID';
COMMENT ON COLUMN uasl_design.uasl_point.fall_range_seam_id IS '落下範囲節ID';
COMMENT ON COLUMN uasl_design.uasl_point.name IS '名称';
COMMENT ON COLUMN uasl_design.uasl_point.geometry  IS 'ジオメトリ';
COMMENT ON COLUMN uasl_design.uasl_point.deviation_geometry IS '逸脱ジオメトリ';
COMMENT ON COLUMN uasl_design.uasl_point.external_guarantee IS '外部保証フラグ';
COMMENT ON COLUMN uasl_design.uasl_point.external_system_id  IS '外部システムID';
COMMENT ON COLUMN uasl_design.uasl_point.external_system_uasl_point_id  IS '外部システム航路点ID';
COMMENT ON COLUMN uasl_design.uasl_point.created_at IS '登録日';
COMMENT ON COLUMN uasl_design.uasl_point.updated_at IS '更新日';


DROP TABLE IF EXISTS uasl_design.uasl_section;

CREATE TABLE uasl_design.uasl_section(
	uasl_section_id varchar(36) PRIMARY KEY,
	uasl_id varchar(250) REFERENCES uasl_design.uasl(uasl_id) NOT NULL,
	name varchar(200),
	point_a_id varchar(36)  NOT NULL REFERENCES uasl_design.uasl_point(uasl_point_id),
	point_b_id varchar(36)  NOT NULL REFERENCES uasl_design.uasl_point(uasl_point_id),
	point_a_is_forward boolean NOT NULL DEFAULT TRUE,
	point_b_is_forward boolean NOT NULL DEFAULT TRUE,
	point_a_external_system_uasl_id varchar(250)  DEFAULT NULL,
	point_b_external_system_uasl_id varchar(250)  DEFAULT NULL,
	status int NOT NULL DEFAULT 1,
	created_at timestamp default CURRENT_TIMESTAMP,
	updated_at timestamp default CURRENT_TIMESTAMP
);

GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE uasl_design.uasl_section TO uasl_design;

COMMENT ON TABLE uasl_design.uasl_section IS '航路区画';
COMMENT ON COLUMN uasl_design.uasl_section.uasl_section_id IS '航路区画ID';
COMMENT ON COLUMN uasl_design.uasl_section.uasl_id IS '航路ID';
COMMENT ON COLUMN uasl_design.uasl_section.name IS '名称';
COMMENT ON COLUMN uasl_design.uasl_section.point_a_id IS '接続される一方の航路点ID';
COMMENT ON COLUMN uasl_design.uasl_section.point_b_id IS '接続されるもう一方の航路点ID';
COMMENT ON COLUMN uasl_design.uasl_section.point_a_is_forward IS '航路区画の始点航路点が順方向（反時計回り）かどうかを示すフラグ（TRUE：順方向、FALSE：逆方向）';
COMMENT ON COLUMN uasl_design.uasl_section.point_b_is_forward IS '航路区画の終点航路点が順方向（反時計回り）かどうかを示すフラグ（TRUE：順方向、FALSE：逆方向）';
COMMENT ON COLUMN uasl_design.uasl_section.point_a_external_system_uasl_id IS 'ポイントAの乗り入れ先外部航路ID';
COMMENT ON COLUMN uasl_design.uasl_section.point_b_external_system_uasl_id IS 'ポイントBの乗り入れ先外部航路ID';
COMMENT ON COLUMN uasl_design.uasl_section.status IS '状態 (1:規定値:有効=使用中、2:無効=論理削除)';
COMMENT ON COLUMN uasl_design.uasl_section.created_at IS '登録日';
COMMENT ON COLUMN uasl_design.uasl_section.updated_at IS '更新日';


DROP TABLE IF EXISTS uasl_design.uasl_point_map;
CREATE TABLE uasl_design.uasl_point_map(
	uasl_id varchar(250) REFERENCES uasl_design.uasl(uasl_id) NOT NULL,
	uasl_point_id varchar(36) REFERENCES uasl_design.uasl_point(uasl_point_id) NOT NULL,
	created_at timestamp default CURRENT_TIMESTAMP,
	updated_at timestamp default CURRENT_TIMESTAMP
);

GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE uasl_design.uasl_point_map TO uasl_design;

COMMENT ON TABLE uasl_design.uasl_point_map IS '航路点マッピング';
COMMENT ON COLUMN uasl_design.uasl_point_map.uasl_id IS '航路ID';
COMMENT ON COLUMN uasl_design.uasl_point_map.uasl_point_id IS '航路点ID';
COMMENT ON COLUMN uasl_design.uasl_point_map.created_at IS '登録日';
COMMENT ON COLUMN uasl_design.uasl_point_map.updated_at IS '更新日';

DROP TABLE IF EXISTS uasl_design.mapping_droneport_section;

CREATE TABLE uasl_design.mapping_droneport_section(
	mapping_droneport_section_id int PRIMARY KEY,
	uasl_section_id varchar(36) REFERENCES uasl_design.uasl_section(uasl_section_id) NOT NULL,
	droneport_id varchar(250)  NOT NULL,
	created_at timestamp default CURRENT_TIMESTAMP,
	updated_at timestamp default CURRENT_TIMESTAMP,
	unique (uasl_section_id, droneport_id)
);

GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE uasl_design.mapping_droneport_section TO uasl_design;

COMMENT ON TABLE uasl_design.mapping_droneport_section IS 'ドローンポート/航路区画マッピング';
COMMENT ON COLUMN uasl_design.mapping_droneport_section.mapping_droneport_section_id IS 'ドローンポート-航路区画マッピングID';
COMMENT ON COLUMN uasl_design.mapping_droneport_section.uasl_section_id IS '航路区画ID';
COMMENT ON COLUMN uasl_design.mapping_droneport_section.droneport_id IS 'ドローンポートID';
COMMENT ON COLUMN uasl_design.mapping_droneport_section.created_at IS '登録日';
COMMENT ON COLUMN uasl_design.mapping_droneport_section.updated_at IS '更新日';

DROP TABLE IF EXISTS uasl_design.railway_crossing_info;

CREATE TABLE uasl_design.railway_crossing_info(
	uasl_id varchar(250) PRIMARY KEY,
	station1 varchar(200),
	station2 varchar(200),
	relative_value varchar(200),
	created_at timestamp default CURRENT_TIMESTAMP,
	updated_at timestamp default CURRENT_TIMESTAMP
);

GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE uasl_design.railway_crossing_info TO uasl_design;

COMMENT ON TABLE uasl_design.railway_crossing_info IS '鉄道との交点情報';
COMMENT ON COLUMN uasl_design.railway_crossing_info.uasl_id IS '航路ID';
COMMENT ON COLUMN uasl_design.railway_crossing_info.station1 IS '駅名1';
COMMENT ON COLUMN uasl_design.railway_crossing_info.station2 IS '駅名2';
COMMENT ON COLUMN uasl_design.railway_crossing_info.relative_value IS '相対値';
COMMENT ON COLUMN uasl_design.railway_crossing_info.created_at IS '登録日';
COMMENT ON COLUMN uasl_design.railway_crossing_info.updated_at IS '更新日';

DROP TABLE IF EXISTS uasl_design.elevation;

CREATE TABLE uasl_design.elevation(
	rid serial PRIMARY KEY,
	rast raster
);

GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE uasl_design.elevation TO uasl_design;

COMMENT ON TABLE uasl_design.elevation IS '標高';
COMMENT ON COLUMN uasl_design.elevation.rid IS 'RID';
COMMENT ON COLUMN uasl_design.elevation.rast IS 'ラスター';


DROP TABLE IF EXISTS uasl_design.drone_model_config CASCADE;

CREATE TABLE uasl_design.drone_model_config(
	aircraft_info_id int NOT NULL,
	maker varchar(200) NOT NULL,
	model_number varchar(200) NOT NULL,
	name varchar(200) NOT NULL,
	type varchar(100) NOT NULL,
	ip varchar(32) NOT NULL,
	length int NOT NULL,
	weight int NOT NULL,
	maximum_takeoff_weight int NOT NULL,
	maximum_flight_time int NOT NULL,
	deviation_range int NOT NULL,
	falling_model varchar(200) NOT NULL,
	falling_model_type varchar(200) NOT NULL,
	created_at timestamp default CURRENT_TIMESTAMP,
	updated_at timestamp default CURRENT_TIMESTAMP,
	PRIMARY KEY (maker, model_number),
	CONSTRAINT chk_dmc_falling_model_type
		CHECK (falling_model_type IN (
			'BallisticModelParameters',
			'ParachuteModelParameters',
			'BallisticBufferModelParameters',
			'SimpleWindModelParameters',
			'StaticBufferModelParameters'
		))
);

GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE uasl_design.drone_model_config TO uasl_design;

COMMENT ON TABLE uasl_design.drone_model_config IS '機体情報（共通項目）';
COMMENT ON COLUMN uasl_design.drone_model_config.aircraft_info_id IS '機体情報ID';
COMMENT ON COLUMN uasl_design.drone_model_config.maker IS '製造メーカー名';
COMMENT ON COLUMN uasl_design.drone_model_config.model_number IS '型式（モデル）';
COMMENT ON COLUMN uasl_design.drone_model_config.name IS '機種名';
COMMENT ON COLUMN uasl_design.drone_model_config.type IS '機体種別';
COMMENT ON COLUMN uasl_design.drone_model_config.ip IS 'IP番号（防水・防塵性能を表すIPコード）';
COMMENT ON COLUMN uasl_design.drone_model_config.length IS '機体長';
COMMENT ON COLUMN uasl_design.drone_model_config.weight IS '重量';
COMMENT ON COLUMN uasl_design.drone_model_config.maximum_takeoff_weight IS '最大離陸重量';
COMMENT ON COLUMN uasl_design.drone_model_config.maximum_flight_time IS '最大飛行時間';
COMMENT ON COLUMN uasl_design.drone_model_config.deviation_range IS '逸脱範囲';
COMMENT ON COLUMN uasl_design.drone_model_config.falling_model IS '落下モデル';
COMMENT ON COLUMN uasl_design.drone_model_config.falling_model_type IS '落下モデルタイプ';
COMMENT ON COLUMN uasl_design.drone_model_config.created_at IS '登録日';
COMMENT ON COLUMN uasl_design.drone_model_config.updated_at IS '更新日';


DROP TABLE IF EXISTS uasl_design.ballistic_model_parameters;

CREATE TABLE uasl_design.ballistic_model_parameters(
	maker varchar(200) NOT NULL,
	model_number varchar(200) NOT NULL,
	characteristic_dimension numeric NOT NULL,
	gps_inaccuracy numeric NOT NULL,
	position_holding_error numeric NOT NULL,
	map_error numeric NOT NULL,
	max_flight_speed numeric NOT NULL,
	response_time numeric NOT NULL,
	parachute_open_time numeric NOT NULL,
	altitude_measurement_error_type varchar(32) NOT NULL,
	contingency_maneuvers_height_type varchar(32) NOT NULL,
	response_time_height_type varchar(32) NOT NULL,
	response_time_distance_type varchar(32) NOT NULL,
	contingency_maneuvers_distance_type varchar(32) NOT NULL,
	max_wind_speed_vector numeric[] NOT NULL,
	created_at timestamp default CURRENT_TIMESTAMP,
	updated_at timestamp default CURRENT_TIMESTAMP,
	PRIMARY KEY (maker, model_number),
	FOREIGN KEY (maker, model_number) REFERENCES uasl_design.drone_model_config(maker, model_number),
	CONSTRAINT chk_bmp_altitude_measurement_error_type
		CHECK (altitude_measurement_error_type IN ('barometer', 'gps')),
	CONSTRAINT chk_bmp_max_wind_speed_vector_length
		CHECK (array_length(max_wind_speed_vector, 1) = 16),
	CONSTRAINT chk_bmp_contingency_maneuvers_height_type
		CHECK (contingency_maneuvers_height_type IN ('multirotor', 'fixedWing')),
	CONSTRAINT chk_bmp_contingency_maneuvers_distance_type
		CHECK (contingency_maneuvers_distance_type IN ('multirotor', 'fixedWing')),
	CONSTRAINT chk_bmp_response_time_height_type
		CHECK (response_time_height_type IN ('manual', 'automatic')),
	CONSTRAINT chk_bmp_response_time_distance_type
		CHECK (response_time_distance_type IN ('manual', 'automatic'))
);

GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE uasl_design.ballistic_model_parameters TO uasl_design;

COMMENT ON TABLE uasl_design.ballistic_model_parameters IS '弾道モデル計算パラメータ';
COMMENT ON COLUMN uasl_design.ballistic_model_parameters.maker IS '製造メーカー名';
COMMENT ON COLUMN uasl_design.ballistic_model_parameters.model_number IS '型式（モデル）';
COMMENT ON COLUMN uasl_design.ballistic_model_parameters.characteristic_dimension IS '機体の最大長';
COMMENT ON COLUMN uasl_design.ballistic_model_parameters.gps_inaccuracy IS 'GPS精度';
COMMENT ON COLUMN uasl_design.ballistic_model_parameters.position_holding_error IS '位置保持精度';
COMMENT ON COLUMN uasl_design.ballistic_model_parameters.map_error IS '地図誤差';
COMMENT ON COLUMN uasl_design.ballistic_model_parameters.max_flight_speed IS '最大速度';
COMMENT ON COLUMN uasl_design.ballistic_model_parameters.response_time IS '応答時間';
COMMENT ON COLUMN uasl_design.ballistic_model_parameters.parachute_open_time IS 'パラシュートが展開されるまでの時間';
COMMENT ON COLUMN uasl_design.ballistic_model_parameters.altitude_measurement_error_type IS '高度の測定誤差の種別';
COMMENT ON COLUMN uasl_design.ballistic_model_parameters.contingency_maneuvers_height_type IS '運動エネルギーによる高さ補正の種別';
COMMENT ON COLUMN uasl_design.ballistic_model_parameters.response_time_height_type IS '操作の遅れによる高さ補正の種別';
COMMENT ON COLUMN uasl_design.ballistic_model_parameters.response_time_distance_type IS '操作の遅れによる距離補正の種別';
COMMENT ON COLUMN uasl_design.ballistic_model_parameters.contingency_maneuvers_distance_type IS '運動エネルギーによる距離補正の種別';
COMMENT ON COLUMN uasl_design.ballistic_model_parameters.max_wind_speed_vector IS '最大風速ベクトル';
COMMENT ON COLUMN uasl_design.ballistic_model_parameters.created_at IS '登録日';
COMMENT ON COLUMN uasl_design.ballistic_model_parameters.updated_at IS '更新日';


DROP TABLE IF EXISTS uasl_design.parachute_model_parameters;

CREATE TABLE uasl_design.parachute_model_parameters(
	maker varchar(200) NOT NULL,
	model_number varchar(200) NOT NULL,
	vertical_speed numeric NOT NULL,
	max_wind_speed numeric NOT NULL,
	gps_inaccuracy numeric NOT NULL,
	position_holding_error numeric NOT NULL,
	map_error numeric NOT NULL,
	max_flight_speed numeric NOT NULL,
	response_time numeric NOT NULL,
	parachute_open_time numeric NOT NULL,
	altitude_measurement_error_type varchar(32) NOT NULL,
	contingency_maneuvers_height_type varchar(32) NOT NULL,
	response_time_height_type varchar(32) NOT NULL,
	response_time_distance_type varchar(32) NOT NULL,
	contingency_maneuvers_distance_type varchar(32) NOT NULL,
	max_wind_speed_vector numeric[] NOT NULL,
	created_at timestamp default CURRENT_TIMESTAMP,
	updated_at timestamp default CURRENT_TIMESTAMP,
	PRIMARY KEY (maker, model_number),
	FOREIGN KEY (maker, model_number) REFERENCES uasl_design.drone_model_config(maker, model_number),
	CONSTRAINT chk_pmp_altitude_measurement_error_type
		CHECK (altitude_measurement_error_type IN ('barometer', 'gps')),
	CONSTRAINT chk_pmp_max_wind_speed_vector_length
		CHECK (array_length(max_wind_speed_vector, 1) = 16),
	CONSTRAINT chk_pmp_contingency_maneuvers_height_type
		CHECK (contingency_maneuvers_height_type IN ('multirotor', 'fixedWing', 'parachute')),
	CONSTRAINT chk_pmp_contingency_maneuvers_distance_type
		CHECK (contingency_maneuvers_distance_type IN ('multirotor', 'fixedWing')),
	CONSTRAINT chk_pmp_response_time_height_type
		CHECK (response_time_height_type IN ('manual', 'automatic')),
	CONSTRAINT chk_pmp_response_time_distance_type
		CHECK (response_time_distance_type IN ('manual', 'automatic'))
);

GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE uasl_design.parachute_model_parameters TO uasl_design;

COMMENT ON TABLE uasl_design.parachute_model_parameters IS 'パラシュートモデル計算パラメータ';
COMMENT ON COLUMN uasl_design.parachute_model_parameters.maker IS '製造メーカー名';
COMMENT ON COLUMN uasl_design.parachute_model_parameters.model_number IS '型式（モデル）';
COMMENT ON COLUMN uasl_design.parachute_model_parameters.vertical_speed IS '鉛直速度';
COMMENT ON COLUMN uasl_design.parachute_model_parameters.max_wind_speed IS '最大風速';
COMMENT ON COLUMN uasl_design.parachute_model_parameters.gps_inaccuracy IS 'GPS精度';
COMMENT ON COLUMN uasl_design.parachute_model_parameters.position_holding_error IS '位置保持精度';
COMMENT ON COLUMN uasl_design.parachute_model_parameters.map_error IS '地図誤差';
COMMENT ON COLUMN uasl_design.parachute_model_parameters.max_flight_speed IS '最大速度';
COMMENT ON COLUMN uasl_design.parachute_model_parameters.response_time IS '応答時間';
COMMENT ON COLUMN uasl_design.parachute_model_parameters.parachute_open_time IS 'パラシュートが展開されるまでの時間';
COMMENT ON COLUMN uasl_design.parachute_model_parameters.altitude_measurement_error_type IS '高度の測定誤差の種別';
COMMENT ON COLUMN uasl_design.parachute_model_parameters.contingency_maneuvers_height_type IS '運動エネルギーによる高さ補正の種別';
COMMENT ON COLUMN uasl_design.parachute_model_parameters.response_time_height_type IS '操作の遅れによる高さ補正の種別';
COMMENT ON COLUMN uasl_design.parachute_model_parameters.response_time_distance_type IS '操作の遅れによる距離補正の種別';
COMMENT ON COLUMN uasl_design.parachute_model_parameters.contingency_maneuvers_distance_type IS '運動エネルギーによる距離補正の種別';
COMMENT ON COLUMN uasl_design.parachute_model_parameters.max_wind_speed_vector IS '最大風速ベクトル';
COMMENT ON COLUMN uasl_design.parachute_model_parameters.created_at IS '登録日';
COMMENT ON COLUMN uasl_design.parachute_model_parameters.updated_at IS '更新日';


DROP TABLE IF EXISTS uasl_design.ballistic_buffer_model_parameters;

CREATE TABLE uasl_design.ballistic_buffer_model_parameters(
	maker varchar(200) NOT NULL,
	model_number varchar(200) NOT NULL,
	characteristic_dimension numeric NOT NULL,
	static_buffer numeric NOT NULL,
	gps_inaccuracy numeric NOT NULL,
	position_holding_error numeric NOT NULL,
	map_error numeric NOT NULL,
	max_flight_speed numeric NOT NULL,
	response_time numeric NOT NULL,
	altitude_measurement_error_type varchar(32) NOT NULL,
	contingency_maneuvers_height_type varchar(32) NOT NULL,
	response_time_height_type varchar(32) NOT NULL,
	response_time_distance_type varchar(32) NOT NULL,
	contingency_maneuvers_distance_type varchar(32) NOT NULL,
	created_at timestamp default CURRENT_TIMESTAMP,
	updated_at timestamp default CURRENT_TIMESTAMP,
	PRIMARY KEY (maker, model_number),
	FOREIGN KEY (maker, model_number) REFERENCES uasl_design.drone_model_config(maker, model_number),
	CONSTRAINT chk_bbp_altitude_measurement_error_type
		CHECK (altitude_measurement_error_type IN ('barometer', 'gps')),
	CONSTRAINT chk_bbp_contingency_maneuvers_height_type
		CHECK (contingency_maneuvers_height_type IN ('multirotor', 'fixedWing')),
	CONSTRAINT chk_bbp_contingency_maneuvers_distance_type
		CHECK (contingency_maneuvers_distance_type IN ('multirotor', 'fixedWing')),
	CONSTRAINT chk_bbp_response_time_height_type
		CHECK (response_time_height_type IN ('manual', 'automatic')),
	CONSTRAINT chk_bbp_response_time_distance_type
		CHECK (response_time_distance_type IN ('manual', 'automatic'))
);

GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE uasl_design.ballistic_buffer_model_parameters TO uasl_design;

COMMENT ON TABLE uasl_design.ballistic_buffer_model_parameters IS 'BallisticBufferモデル計算パラメータ';
COMMENT ON COLUMN uasl_design.ballistic_buffer_model_parameters.maker IS '製造メーカー名';
COMMENT ON COLUMN uasl_design.ballistic_buffer_model_parameters.model_number IS '型式（モデル）';
COMMENT ON COLUMN uasl_design.ballistic_buffer_model_parameters.characteristic_dimension IS '機体の最大長';
COMMENT ON COLUMN uasl_design.ballistic_buffer_model_parameters.static_buffer IS '固定バッファ';
COMMENT ON COLUMN uasl_design.ballistic_buffer_model_parameters.gps_inaccuracy IS 'GPS精度';
COMMENT ON COLUMN uasl_design.ballistic_buffer_model_parameters.position_holding_error IS '位置保持精度';
COMMENT ON COLUMN uasl_design.ballistic_buffer_model_parameters.map_error IS '地図誤差';
COMMENT ON COLUMN uasl_design.ballistic_buffer_model_parameters.max_flight_speed IS '最大速度';
COMMENT ON COLUMN uasl_design.ballistic_buffer_model_parameters.response_time IS '応答時間';
COMMENT ON COLUMN uasl_design.ballistic_buffer_model_parameters.altitude_measurement_error_type IS '高度の測定誤差の種別';
COMMENT ON COLUMN uasl_design.ballistic_buffer_model_parameters.contingency_maneuvers_height_type IS '運動エネルギーによる高さ補正の種別';
COMMENT ON COLUMN uasl_design.ballistic_buffer_model_parameters.response_time_height_type IS '操作の遅れによる高さ補正の種別';
COMMENT ON COLUMN uasl_design.ballistic_buffer_model_parameters.response_time_distance_type IS '操作の遅れによる距離補正の種別';
COMMENT ON COLUMN uasl_design.ballistic_buffer_model_parameters.contingency_maneuvers_distance_type IS '運動エネルギーによる距離補正の種別';
COMMENT ON COLUMN uasl_design.ballistic_buffer_model_parameters.created_at IS '登録日';
COMMENT ON COLUMN uasl_design.ballistic_buffer_model_parameters.updated_at IS '更新日';


DROP TABLE IF EXISTS uasl_design.simple_wind_model_parameters;

CREATE TABLE uasl_design.simple_wind_model_parameters(
	maker varchar(200) NOT NULL,
	model_number varchar(200) NOT NULL,
	max_wind_speed numeric NOT NULL,
	max_flight_speed numeric NOT NULL,
	created_at timestamp default CURRENT_TIMESTAMP,
	updated_at timestamp default CURRENT_TIMESTAMP,
	PRIMARY KEY (maker, model_number),
	FOREIGN KEY (maker, model_number) REFERENCES uasl_design.drone_model_config(maker, model_number)
);

GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE uasl_design.simple_wind_model_parameters TO uasl_design;

COMMENT ON TABLE uasl_design.simple_wind_model_parameters IS 'SimpleWindモデル計算パラメータ';
COMMENT ON COLUMN uasl_design.simple_wind_model_parameters.maker IS '製造メーカー名';
COMMENT ON COLUMN uasl_design.simple_wind_model_parameters.model_number IS '型式（モデル）';
COMMENT ON COLUMN uasl_design.simple_wind_model_parameters.max_wind_speed IS '最大風速';
COMMENT ON COLUMN uasl_design.simple_wind_model_parameters.max_flight_speed IS '最大速度';
COMMENT ON COLUMN uasl_design.simple_wind_model_parameters.created_at IS '登録日';
COMMENT ON COLUMN uasl_design.simple_wind_model_parameters.updated_at IS '更新日';


DROP TABLE IF EXISTS uasl_design.static_buffer_model_parameters;

CREATE TABLE uasl_design.static_buffer_model_parameters(
	maker varchar(200) NOT NULL,
	model_number varchar(200) NOT NULL,
	static_buffer numeric NOT NULL,
	created_at timestamp default CURRENT_TIMESTAMP,
	updated_at timestamp default CURRENT_TIMESTAMP,
	PRIMARY KEY (maker, model_number),
	FOREIGN KEY (maker, model_number) REFERENCES uasl_design.drone_model_config(maker, model_number)
);

GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE uasl_design.static_buffer_model_parameters TO uasl_design;

COMMENT ON TABLE uasl_design.static_buffer_model_parameters IS 'StaticBufferモデル計算パラメータ';
COMMENT ON COLUMN uasl_design.static_buffer_model_parameters.maker IS '製造メーカー名';
COMMENT ON COLUMN uasl_design.static_buffer_model_parameters.model_number IS '型式（モデル）';
COMMENT ON COLUMN uasl_design.static_buffer_model_parameters.static_buffer IS '固定バッファ';
COMMENT ON COLUMN uasl_design.static_buffer_model_parameters.created_at IS '登録日';
COMMENT ON COLUMN uasl_design.static_buffer_model_parameters.updated_at IS '更新日';

CREATE SEQUENCE uasl_design.uasl_determination_id_seq;
CREATE SEQUENCE uasl_design.feasible_vol_seam_id_seq;
CREATE SEQUENCE uasl_design.fall_range_seam_id_seq;
CREATE SEQUENCE uasl_design.uasl_compatible_models_id;
CREATE SEQUENCE uasl_design.mapping_droneport_section_id_seq;

GRANT USAGE ON SEQUENCE uasl_design.uasl_determination_id_seq TO uasl_design;
GRANT USAGE ON SEQUENCE uasl_design.feasible_vol_seam_id_seq TO uasl_design;
GRANT USAGE ON SEQUENCE uasl_design.fall_range_seam_id_seq TO uasl_design;
GRANT USAGE ON SEQUENCE uasl_design.uasl_compatible_models_id TO uasl_design;
GRANT USAGE ON SEQUENCE uasl_design.mapping_droneport_section_id_seq TO uasl_design;

-- ビュー
CREATE VIEW uasl_design.uasl_section_geometry AS
SELECT
    asct.uasl_section_id,
    ary.uasl_id,
    asct.name,
    ST_Force3D(ST_SetSRID(ST_Collect(aj.geometry), 4326)) AS geometry
FROM
    uasl_design.uasl_section asct
JOIN
    uasl_design.uasl_point aj ON asct.point_a_id = aj.uasl_point_id
    OR asct.point_b_id = aj.uasl_point_id
JOIN
    uasl_design.uasl ary ON asct.uasl_id = ary.uasl_id
JOIN
    uasl_design.uasl_determination ON ary.uasl_determination_id = uasl_determination.uasl_determination_id
    AND uasl_determination.delete IS NOT TRUE
GROUP BY
    asct.uasl_section_id, ary.uasl_id, asct.name;

GRANT SELECT ON uasl_design.uasl_section_geometry TO uasl_design;

COMMENT ON VIEW uasl_design.uasl_section_geometry IS '航路区画ジオメトリービュー';
COMMENT ON COLUMN uasl_design.uasl_section_geometry.uasl_section_id IS '航路区画ID';
COMMENT ON COLUMN uasl_design.uasl_section_geometry.uasl_id IS '航路ID';
COMMENT ON COLUMN uasl_design.uasl_section_geometry.name IS '名称';
COMMENT ON COLUMN uasl_design.uasl_section_geometry.geometry  IS 'ジオメトリ';

-- インデックス
CREATE INDEX ON uasl_design.elevation USING gist (st_convexhull("rast"));

-- ファンクション
DROP FUNCTION IF EXISTS uasl_design.uasl_point_reverse(GEOMETRY);
CREATE OR REPLACE FUNCTION uasl_design.uasl_point_reverse(uasl_point_geom GEOMETRY)
RETURNS GEOMETRY AS $$
  WITH temp_dump AS (
    SELECT ST_DumpPoints(uasl_point_geom) AS dump_data
  ), temp_index AS (
    SELECT
      (dump_data).path[2] AS index,
      (dump_data).geom AS geom
    FROM
      temp_dump
  ), temp_array AS (
    SELECT
      array_agg(geom ORDER BY index ASC) AS array_geometry
    FROM
      temp_index
  )
  SELECT
    CASE
      WHEN CARDINALITY(array_geometry) = 5 THEN
        ST_GeomFromText('POLYGON Z(('
        || ST_X(array_geometry[4]) || ' '  || ST_Y(array_geometry[4]) || ' ' || ST_Z(array_geometry[4]) || ','
        || ST_X(array_geometry[3]) || ' '  || ST_Y(array_geometry[3]) || ' ' || ST_Z(array_geometry[3]) || ','
        || ST_X(array_geometry[2]) || ' '  || ST_Y(array_geometry[2]) || ' ' || ST_Z(array_geometry[2]) || ','
        || ST_X(array_geometry[1]) || ' '  || ST_Y(array_geometry[1]) || ' ' || ST_Z(array_geometry[1]) || ','
        || ST_X(array_geometry[4]) || ' '  || ST_Y(array_geometry[4]) || ' ' || ST_Z(array_geometry[4]) ||'))',
        4326)
      ELSE
        NULL
    END
  FROM
    temp_array;
$$ LANGUAGE sql
IMMUTABLE
STRICT
PARALLEL SAFE;
COMMENT ON FUNCTION uasl_design.uasl_point_reverse(GEOMETRY) IS '航路点反転 引数に航路点のジオメトリ又は逸脱ジオメトリを指定する,';
GRANT EXECUTE ON FUNCTION uasl_design.uasl_point_reverse(GEOMETRY) TO uasl_design;