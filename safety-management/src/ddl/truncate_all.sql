-- safety_management スキーマの全テーブルを TRUNCATE する SQL
TRUNCATE TABLE 
    safety_management.t_uasl_reservation
    ,safety_management.t_remote_data
    ,safety_management.t_subscription_data
    ,safety_management.t_drone_location;
