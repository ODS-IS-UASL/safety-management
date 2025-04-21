------------------------------------------------
-- postgresユーザー(管理者権限を持つユーザー)で実行
-------------------------------------------------- 

-- ユーザの作成
CREATE ROLE safety_management WITH LOGIN PASSWORD 'safety_management';
CREATE ROLE airway_design WITH LOGIN PASSWORD 'airway_design';

ALTER USER airway_design SUPERUSER;
ALTER USER safety_management WITH SUPERUSER;

CREATE SCHEMA IF NOT EXISTS airway_design;
GRANT usage ON schema airway_design TO airway_design;

CREATE SCHEMA IF NOT EXISTS safety_management;
GRANT usage ON schema safety_management TO safety_management;

GRANT EXECUTE ON ALL FUNCTIONS IN SCHEMA safety_management TO safety_management;
GRANT EXECUTE ON ALL FUNCTIONS IN SCHEMA safety_management TO airway_design;

GRANT EXECUTE ON ALL FUNCTIONS IN SCHEMA airway_design TO airway_design;
GRANT EXECUTE ON ALL FUNCTIONS IN SCHEMA airway_design TO safety_management;

GRANT SELECT ON ALL TABLES IN SCHEMA airway_design TO safety_management;
GRANT SELECT ON ALL TABLES IN SCHEMA safety_management TO airway_design;

