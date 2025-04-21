\c postgres;

set client_encoding to UTF8;

-- テーブルの作成
CREATE EXTENSION IF NOT EXISTS postgis;
CREATE EXTENSION IF NOT EXISTS postgis WITH SCHEMA airway_design;

DROP TABLE IF EXISTS airway_design.locations;
CREATE TABLE airway_design.locations (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100),
    geom GEOMETRY(Point, 4326)
);
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE airway_design.locations TO airway_design;

DROP TABLE IF EXISTS airway_design.fall_tolerance_range;

CREATE TABLE airway_design.fall_tolerance_range(
	fall_tolerance_range_id varchar(36) PRIMARY KEY,
	business_number varchar(200) NOT NULL,
	airway_operator_id varchar(200) NOT NULL,
	name varchar(200),
	area_name varchar(200),
	elevation_terrain VARCHAR(200),
	geometry GEOMETRY(POLYGON, 4326),
	delete BOOLEAN NOT NULL DEFAULT FALSE,
	created_at timestamp default CURRENT_TIMESTAMP,
	updated_at timestamp default CURRENT_TIMESTAMP
);

GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE airway_design.fall_tolerance_range TO airway_design;

COMMENT ON TABLE airway_design.fall_tolerance_range IS '最大落下許容範囲';
COMMENT ON COLUMN airway_design.fall_tolerance_range.fall_tolerance_range_id IS '最大落下許容範囲ID';
COMMENT ON COLUMN airway_design.fall_tolerance_range.business_number IS '事業者番号';
COMMENT ON COLUMN airway_design.fall_tolerance_range.airway_operator_id IS '航路運営者ID';
COMMENT ON COLUMN airway_design.fall_tolerance_range.name IS '名称';
COMMENT ON COLUMN airway_design.fall_tolerance_range.area_name IS 'エリア名';
COMMENT ON COLUMN airway_design.fall_tolerance_range.elevation_terrain IS '最大標高・地形';
COMMENT ON COLUMN airway_design.fall_tolerance_range.geometry  IS 'ジオメトリ';
COMMENT ON COLUMN airway_design.fall_tolerance_range.delete IS '削除';
COMMENT ON COLUMN airway_design.fall_tolerance_range.created_at  IS '登録日';
COMMENT ON COLUMN airway_design.fall_tolerance_range.updated_at  IS '更新日';

DROP TABLE IF EXISTS airway_design.airway_determination;

CREATE TABLE airway_design.airway_determination(
	airway_determination_id int PRIMARY KEY,
	business_number varchar(200) NOT NULL,
	fall_tolerance_range_id varchar(36) REFERENCES airway_design.fall_tolerance_range(fall_tolerance_range_id)  NOT NULL,
	num_cross_section_divisions int NOT NULL,
	delete BOOLEAN NOT NULL DEFAULT FALSE,
	created_at timestamp default CURRENT_TIMESTAMP,
	updated_at timestamp default CURRENT_TIMESTAMP
);

GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE airway_design.airway_determination TO airway_design;

COMMENT ON TABLE airway_design.airway_determination IS '航路画定';
COMMENT ON COLUMN airway_design.airway_determination.airway_determination_id IS '航路画定ID';
COMMENT ON COLUMN airway_design.airway_determination.business_number IS '事業者番号';
COMMENT ON COLUMN airway_design.airway_determination.fall_tolerance_range_id IS '最大落下許容範囲ID';
COMMENT ON COLUMN airway_design.airway_determination.num_cross_section_divisions IS '断面分割数';
COMMENT ON COLUMN airway_design.airway_determination.delete IS '削除';
COMMENT ON COLUMN airway_design.airway_determination.created_at IS '登録日';
COMMENT ON COLUMN airway_design.airway_determination.updated_at IS '更新日';


DROP TABLE IF EXISTS airway_design.airway_compatible_models;

CREATE TABLE airway_design.airway_compatible_models(
	airway_compatible_models_id int PRIMARY KEY,
	airway_determination_id int REFERENCES airway_design.airway_determination(airway_determination_id)  NOT NULL,
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

GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE airway_design.airway_compatible_models TO airway_design;

COMMENT ON TABLE airway_design.airway_compatible_models IS '航路対応機種';
COMMENT ON COLUMN airway_design.airway_compatible_models.airway_compatible_models_id IS '対応機種ID';
COMMENT ON COLUMN airway_design.airway_compatible_models.aircraft_info_id IS '機体情報ID';
COMMENT ON COLUMN airway_design.airway_compatible_models.airway_determination_id IS '航路画定ID';
COMMENT ON COLUMN airway_design.airway_compatible_models.maker IS '製造メーカー名';
COMMENT ON COLUMN airway_design.airway_compatible_models.model_number IS '型式（モデル）';
COMMENT ON COLUMN airway_design.airway_compatible_models.name IS '機種名';
COMMENT ON COLUMN airway_design.airway_compatible_models.type IS '機体種別';
COMMENT ON COLUMN airway_design.airway_compatible_models.ip IS 'IP番号';
COMMENT ON COLUMN airway_design.airway_compatible_models.length IS '機体長';
COMMENT ON COLUMN airway_design.airway_compatible_models.weight IS '重量';
COMMENT ON COLUMN airway_design.airway_compatible_models.maximum_takeoff_weight IS '最大離陸重量';
COMMENT ON COLUMN airway_design.airway_compatible_models.maximum_flight_time IS '最大飛行時間';
COMMENT ON COLUMN airway_design.airway_compatible_models.created_at  IS '登録日';
COMMENT ON COLUMN airway_design.airway_compatible_models.updated_at  IS '更新日';


DROP TABLE IF EXISTS airway_design.despersion_node;

CREATE TABLE airway_design.despersion_node(
	despersion_node_id int PRIMARY KEY,
	airway_determination_id int REFERENCES airway_design.airway_determination(airway_determination_id)  NOT NULL,
	name varchar(200),
	geometry GEOMETRY(LINESTRING, 4326) NOT NULL,
	created_at timestamp default CURRENT_TIMESTAMP,
	updated_at timestamp default CURRENT_TIMESTAMP
);

GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE airway_design.despersion_node TO airway_design;

COMMENT ON COLUMN airway_design.despersion_node.despersion_node_id IS '落下範囲節ID';
COMMENT ON COLUMN airway_design.despersion_node.airway_determination_id IS '航路画定ID';
COMMENT ON COLUMN airway_design.despersion_node.name IS '名称';
COMMENT ON COLUMN airway_design.despersion_node.geometry IS 'ジオメトリ';
COMMENT ON COLUMN airway_design.despersion_node.created_at IS '登録日';
COMMENT ON COLUMN airway_design.despersion_node.updated_at IS '更新日';




DROP TABLE IF EXISTS airway_design.fall_space;

CREATE TABLE airway_design.fall_space(
	fall_space_id int PRIMARY KEY,
	airway_determination_id int REFERENCES airway_design.airway_determination(airway_determination_id)  NOT NULL,
	despersion_node_id int REFERENCES airway_design.despersion_node(despersion_node_id),
	data text NOT NULL,
	created_at timestamp default CURRENT_TIMESTAMP,
	updated_at timestamp default CURRENT_TIMESTAMP
);

GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE airway_design.fall_space TO airway_design;

COMMENT ON TABLE airway_design.fall_space IS '落下空間';
COMMENT ON COLUMN airway_design.fall_space.fall_space_id IS '落下空間ID';
COMMENT ON COLUMN airway_design.fall_space.airway_determination_id IS '航路画定ID';
COMMENT ON COLUMN airway_design.fall_space.despersion_node_id IS '落下範囲節ID';
COMMENT ON COLUMN airway_design.fall_space.data IS 'データ';
COMMENT ON COLUMN airway_design.fall_space.created_at  IS '登録日';
COMMENT ON COLUMN airway_design.fall_space.updated_at  IS '更新日';

DROP TABLE IF EXISTS airway_design.airway;

CREATE TABLE airway_design.airway(
	airway_id varchar(250) PRIMARY KEY,
	airway_determination_id int REFERENCES airway_design.airway_determination(airway_determination_id)  NOT NULL,
	name varchar(200),
	parent_node_airway_id varchar(250),
	airway_operator_id varchar(200),
	flight_purpose varchar(200),
	created_at timestamp default CURRENT_TIMESTAMP,
	updated_at timestamp default CURRENT_TIMESTAMP
);

GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE airway_design.airway TO airway_design;

COMMENT ON TABLE airway_design.airway IS '航路';
COMMENT ON COLUMN airway_design.airway.airway_id IS '航路ID';
COMMENT ON COLUMN airway_design.airway.airway_determination_id IS '航路画定ID';
COMMENT ON COLUMN airway_design.airway.name IS '名称';
COMMENT ON COLUMN airway_design.airway.parent_node_airway_id IS '親ノード航路ID';
COMMENT ON COLUMN airway_design.airway.airway_operator_id IS '航路運営者ID';
COMMENT ON COLUMN airway_design.airway.flight_purpose IS '飛行目的';
COMMENT ON COLUMN airway_design.airway.created_at IS '登録日';
COMMENT ON COLUMN airway_design.airway.updated_at IS '更新日';

DROP TABLE IF EXISTS airway_design.airway_section;

CREATE TABLE airway_design.airway_section(
	airway_section_id varchar(36) PRIMARY KEY,
	airway_id varchar(250) REFERENCES airway_design.airway(airway_id) NOT NULL,
	name varchar(200),
	created_at timestamp default CURRENT_TIMESTAMP,
	updated_at timestamp default CURRENT_TIMESTAMP
);

GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE airway_design.airway_section TO airway_design;

COMMENT ON TABLE airway_design.airway_section IS '航路区画';
COMMENT ON COLUMN airway_design.airway_section.airway_section_id IS '航路区画ID';
COMMENT ON COLUMN airway_design.airway_section.airway_id IS '航路ID';
COMMENT ON COLUMN airway_design.airway_section.name IS '名称';
COMMENT ON COLUMN airway_design.airway_section.created_at IS '登録日';
COMMENT ON COLUMN airway_design.airway_section.updated_at IS '更新日';



DROP TABLE IF EXISTS airway_design.airway_junction;

CREATE TABLE airway_design.airway_junction(
	airway_junction_id varchar(36) PRIMARY KEY,
	despersion_node_id int REFERENCES airway_design.despersion_node(despersion_node_id)  NOT NULL,
	name varchar(200),
	airway_id varchar(250) REFERENCES airway_design.airway(airway_id)  NOT NULL,
	geometry GEOMETRY(POLYGONZ, 4326) NOT NULL,
	deviation_geometry  GEOMETRY(POLYGONZ, 4326) NOT NULL,
	created_at timestamp default CURRENT_TIMESTAMP,
	updated_at timestamp default CURRENT_TIMESTAMP
);

GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE airway_design.airway_junction TO airway_design;

COMMENT ON TABLE airway_design.airway_junction IS 'ジャンクション';
COMMENT ON COLUMN airway_design.airway_junction.airway_junction_id IS 'ジャンクションID';
COMMENT ON COLUMN airway_design.airway_junction.despersion_node_id IS '落下範囲節ID';
COMMENT ON COLUMN airway_design.airway_junction.name IS '名称';
COMMENT ON COLUMN airway_design.airway_junction.airway_id IS '航路ID';
COMMENT ON COLUMN airway_design.airway_junction.geometry  IS 'ジオメトリ';
COMMENT ON COLUMN airway_design.airway_junction.deviation_geometry IS '逸脱ジオメトリ';
COMMENT ON COLUMN airway_design.airway_junction.created_at IS '登録日';
COMMENT ON COLUMN airway_design.airway_junction.updated_at IS '更新日';


DROP TABLE IF EXISTS airway_design.mapping_junction_section;

CREATE TABLE airway_design.mapping_junction_section(
	mapping_junction_section_id int PRIMARY KEY,
	airway_section_id varchar(36) REFERENCES airway_design.airway_section(airway_section_id) NOT NULL,
	airway_junction_id varchar(36) REFERENCES airway_design.airway_junction(airway_junction_id)  NOT NULL,
	created_at timestamp default CURRENT_TIMESTAMP,
	updated_at timestamp default CURRENT_TIMESTAMP
);

GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE airway_design.mapping_junction_section TO airway_design;

COMMENT ON TABLE airway_design.mapping_junction_section IS 'ジャンクション/航路区画マッピング';
COMMENT ON COLUMN airway_design.mapping_junction_section.mapping_junction_section_id IS 'ジャンクション-航路区画マッピングID';
COMMENT ON COLUMN airway_design.mapping_junction_section.airway_section_id IS '航路区画ID';
COMMENT ON COLUMN airway_design.mapping_junction_section.airway_junction_id IS 'ジャンクションID';
COMMENT ON COLUMN airway_design.mapping_junction_section.created_at IS '登録日';
COMMENT ON COLUMN airway_design.mapping_junction_section.updated_at IS '更新日';

DROP TABLE IF EXISTS airway_design.mapping_droneport_section;

CREATE TABLE airway_design.mapping_droneport_section(
	mapping_droneport_section_id int PRIMARY KEY,
	airway_section_id varchar(36) REFERENCES airway_design.airway_section(airway_section_id) NOT NULL,
	droneport_id varchar(250)  NOT NULL,
	created_at timestamp default CURRENT_TIMESTAMP,
	updated_at timestamp default CURRENT_TIMESTAMP,
	unique (airway_section_id, droneport_id)
);

GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE airway_design.mapping_droneport_section TO airway_design;

COMMENT ON TABLE airway_design.mapping_droneport_section IS 'ドローンポート/航路区画マッピング';
COMMENT ON COLUMN airway_design.mapping_droneport_section.mapping_droneport_section_id IS 'ドローンポート-航路区画マッピングID';
COMMENT ON COLUMN airway_design.mapping_droneport_section.airway_section_id IS '航路区画ID';
COMMENT ON COLUMN airway_design.mapping_droneport_section.droneport_id IS 'ドローンポートID';
COMMENT ON COLUMN airway_design.mapping_droneport_section.created_at IS '登録日';
COMMENT ON COLUMN airway_design.mapping_droneport_section.updated_at IS '更新日';

DROP TABLE IF EXISTS airway_design.railway_crossing_info;

CREATE TABLE airway_design.railway_crossing_info(
	airway_id varchar(250) PRIMARY KEY,
	station1 varchar(200),
	station2 varchar(200),
	relative_value varchar(200),
	created_at timestamp default CURRENT_TIMESTAMP,
	updated_at timestamp default CURRENT_TIMESTAMP
);

GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE airway_design.railway_crossing_info TO airway_design;

COMMENT ON TABLE airway_design.railway_crossing_info IS '鉄道との交点情報';
COMMENT ON COLUMN airway_design.railway_crossing_info.airway_id IS '航路ID';
COMMENT ON COLUMN airway_design.railway_crossing_info.station1 IS '駅名1';
COMMENT ON COLUMN airway_design.railway_crossing_info.station2 IS '駅名2';
COMMENT ON COLUMN airway_design.railway_crossing_info.relative_value IS '相対値';
COMMENT ON COLUMN airway_design.railway_crossing_info.created_at IS '登録日';
COMMENT ON COLUMN airway_design.railway_crossing_info.updated_at IS '更新日';

CREATE SEQUENCE airway_design.airway_determination_id_seq;
CREATE SEQUENCE airway_design.fall_space_id_seq;
CREATE SEQUENCE airway_design.despersion_node_id_seq;
CREATE SEQUENCE airway_design.airway_compatible_models_id;
CREATE SEQUENCE airway_design.mapping_junction_section_id_seq;
CREATE SEQUENCE airway_design.mapping_droneport_section_id_seq;

GRANT USAGE ON SEQUENCE airway_design.airway_determination_id_seq TO airway_design;
GRANT USAGE ON SEQUENCE airway_design.fall_space_id_seq TO airway_design;
GRANT USAGE ON SEQUENCE airway_design.despersion_node_id_seq TO airway_design;
GRANT USAGE ON SEQUENCE airway_design.airway_compatible_models_id TO airway_design;
GRANT USAGE ON SEQUENCE airway_design.mapping_junction_section_id_seq TO airway_design;
GRANT USAGE ON SEQUENCE airway_design.mapping_droneport_section_id_seq TO airway_design;

-- ビュー
CREATE VIEW airway_design.airway_section_geometry AS
SELECT
    asct.airway_section_id,
    ary.airway_id,
    asct.name,
    ST_Force3D(ST_SetSRID(ST_Collect(aj.geometry), 4326)) AS geometry  
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

GRANT SELECT ON airway_design.airway_section_geometry TO airway_design;

COMMENT ON VIEW airway_design.airway_section_geometry IS '航路区画ジオメトリービュー';
COMMENT ON COLUMN airway_design.airway_section_geometry.airway_section_id IS '航路区画ID';
COMMENT ON COLUMN airway_design.airway_section_geometry.airway_id IS '航路ID';
COMMENT ON COLUMN airway_design.airway_section_geometry.name IS '名称';
COMMENT ON COLUMN airway_design.airway_section_geometry.geometry  IS 'ジオメトリ';