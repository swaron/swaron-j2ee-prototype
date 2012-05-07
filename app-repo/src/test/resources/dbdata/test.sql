--insert into sec_group(group_name) values('test')
--select * from sec_user u where u.username = some( select sec_group.group_name from sec_group);
--drop table db_version;
--create table db_version (
--	db_version_id bigint identity primary key,
--	value varchar,
--	last_updated timestamp
--);
select * from Detail_Range;