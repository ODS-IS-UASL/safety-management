-- airway_designスキーマからViewを作るためのテーブル
-- 開発環境でのみ使用する
------------------------------------------------
-- postgresユーザー(管理者権限を持つユーザー)で実行
------------------------------------------------
-- ユーザとスキーマの作成
CREATE ROLE airway_design WITH LOGIN PASSWORD 'airway_design';
ALTER USER airway_design WITH SUPERUSER;

\c safety_management;
CREATE SCHEMA IF NOT EXISTS airway_design;
GRANT usage ON schema airway_design TO airway_design;

set client_encoding to UTF8;

-- テーブルの作成
CREATE EXTENSION IF NOT EXISTS postgis;
CREATE EXTENSION IF NOT EXISTS postgis WITH SCHEMA airway_design;
CREATE EXTENSION IF NOT EXISTS postgis SCHEMA airway_design;
CREATE EXTENSION IF NOT EXISTS hstore;
CREATE EXTENSION IF NOT EXISTS hstore WITH SCHEMA airway_design;
CREATE EXTENSION IF NOT EXISTS hstore SCHEMA airway_design;
GRANT USAGE ON SCHEMA airway_design TO airway_design;
GRANT EXECUTE ON ALL FUNCTIONS IN SCHEMA airway_design TO airway_design;


DROP TABLE IF EXISTS airway_design.fall_tolerance_range;
CREATE TABLE airway_design.fall_tolerance_range(
	fall_tolerance_range_id varchar(36) PRIMARY KEY,
	business_number varchar(200) NOT NULL,
	name varchar(200),
	geometry GEOMETRY(POLYGON, 4326)
);
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE airway_design.fall_tolerance_range TO safety_management;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE airway_design.fall_tolerance_range TO airway_design;

DROP TABLE IF EXISTS airway_design.airway_determination;
CREATE TABLE airway_design.airway_determination(
	airway_determination_id int PRIMARY KEY,
	business_number varchar(200) NOT NULL,
	fall_tolerance_range_id varchar(36) REFERENCES airway_design.fall_tolerance_range(fall_tolerance_range_id)  NOT NULL
);
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE airway_design.airway_determination TO safety_management;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE airway_design.airway_determination TO airway_design;

DROP TABLE IF EXISTS airway_design.despersion_node;
CREATE TABLE airway_design.despersion_node(
	despersion_node_id int PRIMARY KEY,
	airway_determination_id int REFERENCES airway_design.airway_determination(airway_determination_id)  NOT NULL,
	name varchar(200),
	geometry GEOMETRY(LINESTRING, 4326) NOT NULL
);
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE airway_design.despersion_node TO safety_management;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE airway_design.despersion_node TO airway_design;

DROP TABLE IF EXISTS airway_design.airway;
CREATE TABLE airway_design.airway(
	airway_id varchar(250) PRIMARY KEY,
	airway_determination_id int REFERENCES airway_design.airway_determination(airway_determination_id)  NOT NULL,
	name varchar(200),
	parent_node_airway_id varchar(250)
);
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE airway_design.airway TO safety_management;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE airway_design.airway TO airway_design;

DROP TABLE IF EXISTS airway_design.airway_section;
CREATE TABLE airway_design.airway_section(
	airway_section_id varchar(36) PRIMARY KEY,
	airway_id varchar(250) REFERENCES airway_design.airway(airway_id) NOT NULL,
	name varchar(200)
);
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE airway_design.airway_section TO safety_management;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE airway_design.airway_section TO airway_design;

DROP TABLE IF EXISTS airway_design.airway_junction;

CREATE TABLE airway_design.airway_junction(
	airway_junction_id varchar(36) PRIMARY KEY,
	despersion_node_id int REFERENCES airway_design.despersion_node(despersion_node_id)  NOT NULL,
	name varchar(200),
	airway_id varchar(250) REFERENCES airway_design.airway(airway_id)  NOT NULL,
	geometry GEOMETRY(POLYGONZ, 4326) NOT NULL,
	deviation_geometry  GEOMETRY(POLYGONZ, 4326) NOT NULL
);
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE airway_design.airway_junction TO safety_management;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE airway_design.airway_junction TO airway_design;

DROP TABLE IF EXISTS airway_design.mapping_junction_section;
CREATE TABLE airway_design.mapping_junction_section(
	mapping_junction_section_id int PRIMARY KEY,
	airway_section_id varchar(36) REFERENCES airway_design.airway_section(airway_section_id) NOT NULL,
	airway_junction_id varchar(36) REFERENCES airway_design.airway_junction(airway_junction_id)  NOT NULL
);

GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE airway_design.mapping_junction_section TO safety_management;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE airway_design.mapping_junction_section TO airway_design;

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

CREATE SEQUENCE airway_design.fall_tolerance_range_id_seq;
CREATE SEQUENCE airway_design.airway_determination_id_seq;
CREATE SEQUENCE airway_design.airway_id_seq;
CREATE SEQUENCE airway_design.airway_section_id_seq;

-- テスト用データ
delete from airway_design.fall_tolerance_range;
insert into airway_design.fall_tolerance_range (fall_tolerance_range_id , business_number , name , geometry) values 
('1', '1', 'TEST1', ST_GeomFromText('POLYGON((135.760497 35.012033,135.760497 30.011655, 130.760970 35.011655, 125.760970 25.012033,135.760497 35.012033))')),
('2', '2', 'TEST2', ST_GeomFromText('POLYGON((136.760497 36.012033,135.760497 31.011655, 131.760970 35.011655, 126.760970 26.012033,136.760497 36.012033))')),
('3', '3', 'TEST3', ST_GeomFromText('POLYGON((137.760497 37.012033,135.760497 32.011655, 132.760970 35.011655, 127.760970 27.012033,137.760497 37.012033))'));

delete from airway_design.airway_determination;
insert into airway_design.airway_determination (airway_determination_id , business_number , fall_tolerance_range_id) values 
(11,'1','1'),
(12,'1','1'),
(13,'1','1'),
(21,'2','2'),
(22,'2','2'),
(23,'2','2'),
(31,'3','3'),
(32,'3','3'),
(33,'3','3');

delete from airway_design.airway;
insert into airway_design.airway (airway_id , airway_determination_id , name , parent_node_airway_id) values 
('1-1-1',11,'name','1'),
('1-1-2',11,'name','1'),
('1-1-3',11,'name','1'),
('1-2-1',12,'name','1'),
('1-2-2',12,'name','1'),
('1-2-3',12,'name','1'),
('1-3-1',13,'name','1'),
('1-3-2',13,'name','1'),
('1-3-3',13,'name','1'),
('2-1-1',21,'name','1'),
('2-1-2',21,'name','1'),
('2-1-3',21,'name','1'),
('2-2-1',22,'name','1'),
('2-2-2',22,'name','1'),
('2-2-3',22,'name','1'),
('2-3-1',23,'name','1'),
('2-3-2',23,'name','1'),
('2-3-3',23,'name','1'),
('3-1-1',31,'name','1'),
('3-1-2',31,'name','1'),
('3-1-3',31,'name','1'),
('3-2-1',32,'name','1'),
('3-2-2',32,'name','1'),
('3-2-3',32,'name','1'),
('3-3-1',33,'name','1'),
('3-3-2',33,'name','1'),
('3-3-3',33,'name','1');

delete from airway_design.airway_section;
insert into airway_design.airway_section (airway_section_id , airway_id , name) values 
('1','1-1-1','name'),
('2','1-1-2','name'),
('3','1-1-3','name'),
('4','1-2-1','name'),
('5','1-2-2','name'),
('6','1-2-3','name'),
('7','1-3-1','name'),
('8','1-3-2','name'),
('9','1-3-3','name'),
('10','2-1-1','name'),
('11','2-1-2','name'),
('12','2-1-3','name'),
('13','2-2-1','name'),
('14','2-2-2','name'),
('15','2-2-3','name'),
('16','2-3-1','name'),
('17','2-3-2','name'),
('18','2-3-3','name'),
('19','3-1-1','name'),
(20,'3-1-2','name'),
(21,'3-1-3','name'),
(22,'3-2-1','name'),
(23,'3-2-2','name'),
(24,'3-2-3','name'),
(25,'3-3-1','name'),
(26,'3-3-2','name'),
(27,'3-3-3','name');

delete from airway_design.railway_crossing_info;
insert into airway_design.railway_crossing_info (airway_id , station1 , station2 , relative_value , created_at , updated_at) values
('1-1-1', '秩父', '御花畑', '0.2', '2025-12-01 09:00:00', '2025-12-01 09:00:00'),
('1-1-2', '秩父', '御花畑', '0.5', '2025-12-02 10:00:00', '2025-12-02 10:00:00'),
('1-1-3', '秩父', '御花畑', '0.3', '2025-12-03 11:00:00', '2025-12-03 11:00:00'),
('1-2-1', '秩父', '御花畑', '0.4', '2025-12-04 12:00:00', '2025-12-04 12:00:00'),
('1-2-2', '秩父', '御花畑', '0.6', '2025-12-05 13:00:00', '2025-12-05 13:00:00');

