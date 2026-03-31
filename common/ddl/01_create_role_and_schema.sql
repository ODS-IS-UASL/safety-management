------------------------------------------------
-- postgresユーザー(管理者権限を持つユーザー)で実行
-------------------------------------------------- 

-- ユーザの作成
CREATE ROLE safety_management WITH LOGIN PASSWORD 'safety_management';
CREATE ROLE uasl_design WITH LOGIN PASSWORD 'uasl_design';

ALTER USER uasl_design SUPERUSER;
ALTER USER safety_management WITH SUPERUSER;

CREATE SCHEMA IF NOT EXISTS uasl_design;
GRANT usage ON schema uasl_design TO uasl_design;

CREATE SCHEMA IF NOT EXISTS safety_management;
GRANT usage ON schema safety_management TO safety_management;

GRANT EXECUTE ON ALL FUNCTIONS IN SCHEMA safety_management TO safety_management;
GRANT EXECUTE ON ALL FUNCTIONS IN SCHEMA safety_management TO uasl_design;

GRANT EXECUTE ON ALL FUNCTIONS IN SCHEMA uasl_design TO uasl_design;
GRANT EXECUTE ON ALL FUNCTIONS IN SCHEMA uasl_design TO safety_management;

GRANT SELECT ON ALL TABLES IN SCHEMA uasl_design TO safety_management;
GRANT SELECT ON ALL TABLES IN SCHEMA safety_management TO uasl_design;

