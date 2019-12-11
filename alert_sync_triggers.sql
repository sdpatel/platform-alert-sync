

select a.alert_id AS id, rt.alert2_type_name AS type, a.frequency_days AS frequency, rds.datasource_name AS scope, dc.context, a.active_status, re.email,getdate(), 'U', 'alert'  from  alert as a, ref_alert_type rt, alert_datasource as ad, ref_datasource rds, datasource_context dc, ra_user_email re where a.alert_id = 1002183 and rt.alert_type_id = a.alert_type_id and ad.alert_id = a.alert_id and rds.datasource_id = ad.datasource_id and dc.datasource_id = ad.datasource_id and re.email_id = a.email_id;

select ad.alert_id AS id, rt.alert2_type_name AS type, a.frequency_days AS frequency, rds.datasource_name AS scope, dc.context, a.active_status, re.email,getdate(), 'U', 'alert'  from  alert_datasource ad, alert a, ref_alert_type rt, ref_datasource rds, datasource_context dc, ra_user_email re where ad.alert_id = 1002183 and a.alert_id = ad.alert_id and rt.alert_type_id = a.alert_type_id and rds.datasource_id = ad.datasource_id and dc.datasource_id = ad.datasource_id and re.email_id = a.email_id;

select ad.alert_id AS id, rt.alert2_type_name AS type, a.frequency_days AS frequency, rds.datasource_name AS scope, dc.context, a.active_status, re.email,getdate(), 'U', 'alert'  from  alert_datasource ad, alert a, ref_alert_type rt, ref_datasource rds, datasource_context dc, ra_user_email re where ad.alert_id = 4293137 and a.alert_id = ad.alert_id and rt.alert_type_id = a.alert_type_id and rds.datasource_id = ad.datasource_id and dc.datasource_id = ad.datasource_id and re.email_id = a.email_id;

ALTER TABLE alert_sync
 ADD type  VARCHAR(50),
 ADD frequency int(4),
 ADD scope VARCHAR(100),
 ADD context CARCHAR(32),
 ADD active_status char(1),
 ADD email VARCHAR(255);

ALTER TABLE alert_sync
  RENAME alert_id TO id;


DROP TRIGGER t


CREATE TRIGGER tI_alert_datasource
ON alert_datasource
FOR INSERT AS
insert alert_sync (id, type, frequency, scope, context, active_status, email, timestamp, trigger_action, trigger_table)
select ad.alert_id AS id, rt.alert2_type_name AS type, a.frequency_days AS frequency, rds.datasource_name AS scope,
dc.context, a.active_status, re.email,getdate(), 'I', 'alert_datasource'  from  inserted as ad, alert a,
ref_alert_type rt, ref_datasource rds, datasource_context dc, ra_user_email re where a.alert_id = ad.alert_id and rt.alert_type_id = a.alert_type_id and rds.datasource_id = ad.datasource_id and dc.datasource_id = ad.datasource_id and re.email_id = a.email_id;


CREATE TRIGGER tD_alert_datasource
ON alert_datasource
FOR DELETE AS
insert alert_sync (id, type, frequency, scope, context, active_status, email, timestamp, trigger_action, trigger_table)
select ad.alert_id AS id, rt.alert2_type_name AS type, a.frequency_days AS frequency, rds.datasource_name AS scope,
dc.context, a.active_status, re.email,getdate(), 'I', 'alert_datasource'  from  inserted as ad, alert a,
ref_alert_type rt, ref_datasource rds, datasource_context dc, ra_user_email re where a.alert_id = ad.alert_id and rt.alert_type_id = a.alert_type_id and rds.datasource_id = ad.datasource_id and dc.datasource_id = ad.datasource_id and re.email_id = a.email_id;


CREATE TRIGGER tU_alert
ON alert
FOR DELETE AS
insert alert_sync (id, type, frequency, scope, context, active_status, email, timestamp, trigger_action, trigger_table)
select a.alert_id AS id, rt.alert2_type_name AS type, a.frequency_days AS frequency, rds.datasource_name AS scope,dc.context, a.active_status, re.email,getdate(), 'I', 'alert_datasource'  from  updated as a, alert_datasource ad, ref_alert_type rt, ref_datasource rds, datasource_context dc, ra_user_email re where ad.alert_id = a.alert_id and rt.alert_type_id = a.alert_type_id and rds.datasource_id = ad.datasource_id and dc.datasource_id = ad.datasource_id and re.email_id = a.email_id;



select a.alert_id AS id, rt.alert2_type_name AS type, a.frequency_days AS frequency,  rds.datasource_name AS scope, dc.context, a.active_status, re.email,getdate(), 'U', 'alert'  FROM updated as a, alert_datasource ad, datasource_context dc, ref_alert_type rt, ref_datasource rds, ra_user_email re WHEREad.alert_id = a.alert_id AND dc.datasource_id = ad.datasource_id AND  rt.alert_type_id = a.alert_type_id AND rds.datasource_id = ad.datasource_id AND re.email_id = a.email_id

