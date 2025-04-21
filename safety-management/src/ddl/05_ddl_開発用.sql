-- airway_designスキーマからViewを作るためのデータ
-- 開発環境でのみ使用する

-- 必要であれば実行
-- -- データ削除
-- delete from airway_design.mapping_junction_section;
-- delete from airway_design.airway_junction;
-- delete from airway_design.airway_section;
-- delete from airway_design.airway;
-- delete from airway_design.despersion_node;
-- delete from airway_design.airway_determination;
-- delete from airway_design.fall_tolerance_range;

-- テスト用データ敷き込み
insert into airway_design.fall_tolerance_range (
  fall_tolerance_range_id,
  business_number,
  airway_operator_id,
  name,
  geometry
) values (
  'fallToleranceRange01',
  'businessNumber01',
  'operatorId01',
  'fallToleranceRange01',
  ST_GeomFromText('POLYGON((135.760497 35.012033,135.760497 30.011655, 130.760970 35.011655, 125.760970 25.012033,135.760497 35.012033))')
);

insert into airway_design.airway_determination (
  airway_determination_id,
  business_number,
  fall_tolerance_range_id,
  num_cross_section_divisions
) values (
  1,
  'businessNumber01',
  'fallToleranceRange01',
  1
);

insert into airway_design.despersion_node (
  despersion_node_id,
  airway_determination_id,
  name,
  geometry
) values (
  1,
  1,
  'node01',
  'LINESTRING(0 0, 1 1)'::geometry
);

insert into airway_design.airway (
  airway_id,
  airway_determination_id,
  name,
  parent_node_airway_id
) values (
  '1',
  1,
  'airwayX',
  '1'
);

insert into airway_design.airway_section (
  airway_section_id,
  airway_id,
  name
) values (
  '1',
  '1',
  'airwayXsection01'
), (
  '2',
  '1',
  'airwayXsection02'
);

insert into airway_design.airway_junction (
  airway_junction_id,
  despersion_node_id,
  name,
  airway_id,
  geometry,
  deviation_geometry
) values (
  '1',
  1,
  'junction01',
  '1',
  'POLYGON((141.355336 43.045666 100, 141.355336 43.045666 50, 141.355471 43.045669 50, 141.355471 43.045669 100, 141.355336 43.045666 100))'::geometry,
  'POLYGON((141.355336 43.045666 100, 141.355336 43.045666 50, 141.355471 43.045669 50, 141.355471 43.045669 100, 141.355336 43.045666 100))'::geometry
), (
  '2',
  1,
  'junction02',
  '1',
  'POLYGON((141.355335 43.045812 100, 141.355335 43.045812 50, 141.355420 43.045813 50, 141.355420 43.045813 100, 141.355335 43.045812 100))'::geometry,
  'POLYGON((141.355335 43.045812 100, 141.355335 43.045812 50, 141.355420 43.045813 50, 141.355420 43.045813 100, 141.355335 43.045812 100))'::geometry
), (
  '3',
  1,
  'junction03',
  '1',
  'POLYGON((141.355334 43.045968 100, 141.355334 43.045968 50, 141.355369 43.045967 50, 141.355369 43.045967 100, 141.355334 43.045968 100))'::geometry,
  'POLYGON((141.355334 43.045968 100, 141.355334 43.045968 50, 141.355369 43.045967 50, 141.355369 43.045967 100, 141.355334 43.045968 100))'::geometry
);

insert into airway_design.mapping_junction_section (
  mapping_junction_section_id,
  airway_section_id,
  airway_junction_id
) values (
  1,
  '1',
  '1'
), (
  2,
  '1',
  '2'
), (
  3,
  '2',
  '2'
), (
  4,
  '2',
  '3'
);

-- サブスクリプション登録と運航中ドローンの位置情報通知と逸脱判定のためのデータ
-- 開発環境でのみ使用する

-- 必要であれば実行
-- -- データ削除
-- delete from safety_management.t_airway_reservation;
-- delete from safety_management.t_remote_data;

-- テスト用データ敷き込み
insert into safety_management.t_airway_reservation (
  airway_reservation_id,
  start_at,
  end_at,
  reserved_at,
  airway_section_ids,
  operator_id,
  evaluation_results,
  third_party_evaluation_results,
  railway_operation_evaluation_results
) values (
  'reservationId01',
  '2024-12-09T08:11:28.00Z',
  '2024-12-09T08:11:30.00Z',
  '2024-12-09T08:10:29.00Z',
  ARRAY['1', '2'],
  'operatorId01',
  TRUE,
  TRUE,
  TRUE
), (
  'reservationId02',
  '2024-12-09T08:11:30.00Z',
  '2024-12-09T08:11:31.00Z',
  '2024-12-09T08:10:29.00Z',
  ARRAY['1', '2'],
  'operatorId01',
  TRUE,
  TRUE,
  TRUE
);

insert into safety_management.t_remote_data (
  airway_reservation_id,
  serial_number,
  registration_id,
  utm_id,
  specific_sessoion_id,
  aircraft_info_id
) values (
  'reservationId01',
  'serialNumber01',
  'JA.DJ.123456',
  'utmId01',
  'specificSessoionId01',
  1
), (
  'reservationId02',
  'serialNumber02',
  'JA.DJ.123456',
  'utmId02',
  'specificSessoionId02',
  2
);
