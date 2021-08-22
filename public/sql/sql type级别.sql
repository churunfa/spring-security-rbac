
explain select * from `user` where username = 'root'
1	SIMPLE	user		const	unique_username	unique_username	83	const	1	100.00	


explain select count(*) from `user`
1	SIMPLE	user		index		unique_username	83		2	100.00	Using index


explain select secret from `user` where username = 'root'
1	SIMPLE	user		const	unique_username	unique_username	83	const	1	100.00	


explain select secret from `user` where id = 19
1	SIMPLE	user		const	PRIMARY	PRIMARY	8	const	1	100.00	


explain select * from `permission` where id = 1
1	SIMPLE	permission		const	PRIMARY	PRIMARY	4	const	1	100.00	


explain select * from `permission` where name = '/api/rbac/user/info'
1	SIMPLE	permission		const	unique_name	unique_name	202	const	1	100.00	


explain select count(*) from `user_permission_role` where user_id = 19 and type = 'permission' and permission_role_id = 15
1	SIMPLE	user_permission_role		ref	type_pid,uid_type_pid	type_pid	11	const,const,const	1	100.00	Using where; Using index


1	SIMPLE	user_permission_role		ref	type_pid,uid_type_pid	type_pid	11	const,const,const	1	100.00	Using where; Using index
1	SIMPLE	user_permission_role		ref	type_pid,uid_type_pid	type_pid	11	const,const,const	1	100.00	Using where; Using index



explain select count(*) from `role_permission` where role_id = 18 and permission_id = 15
1	SIMPLE	role_permission		ref	role_permission	role_permission	8	const,const	1	100.00	Using index


explain select count(*) from `user_permission_role` where `type` = 'permission' and `permission_role_id` = 15 and `enabled` = 1
1	SIMPLE	user_permission_role		ref	uid_type_pid	uid_type_pid	7	const,const	1	50.00	Using index condition; Using where


explain select count(*) from `role_permission` where `permission_id` = 15
1	SIMPLE	role_permission		ref	role_permission,permission_	permission_	4	const	1	100.00	Using index


explain select permission_role_id as id, `name`, resource, `describe` from user_permission_role where user_id = 19 and type = 'permission' and enabled = 1
1	SIMPLE	user_permission_role		ref	type_pid,uid_type_pid	type_pid	6	const,const	1	50.00	Using index condition; Using where


explain select permission_id as id, name, resource, `describe` from `role_permission` where role_id =18 and enabled = 1
explain select permission_id as id, name, resource, `describe` from `role_permission` where role_id =18 and enabled = 1


explain select * from `role` where id = 18
1	SIMPLE	role		const	PRIMARY	PRIMARY	4	const	1	100.00	


explain select * from `role` where name = 'root'
1	SIMPLE	role		const	unique_name	unique_name	82	const	1	100.00	


explain select count(*) from `user_permission_role` where `type` = 'role' and `permission_role_id` = 18 and `enabled` = 1
1	SIMPLE	user_permission_role		ref	uid_type_pid	uid_type_pid	7	const,const	1	50.00	Using index condition; Using where



explain select count(*) from `role_permission` where `role_id` = 18 and `enabled` = 1
1	SIMPLE	role_permission		ref	role_permission	role_permission	4	const	1	100.00	Using where


explain select permission_role_id as id, `name`, resource, `describe` from user_permission_role where user_id = 19 and type = 'role' and enabled = 1

1	SIMPLE	user_permission_role		ref	type_pid,uid_type_pid	type_pid	6	const,const	1	50.00	Using index condition; Using where

explain select * from role
1	SIMPLE	role		ALL					2	100.00	

explain select * from `user` limit 0, 100
1	SIMPLE	user		ALL					2	100.00	

explain select * from `user` where username like 'ro%' limit 0, 100
1	SIMPLE	user		range	unique_username	unique_username	83		1	100.00	Using where


explain select * from `user` left join user_permission_role on (`user`.id = user_id and type = 'role' and name = 'root') 删！
1	SIMPLE	user		ALL					2	100.00	
1	SIMPLE	user_permission_role		ref	type_pid,uid_type_pid	type_pid	6	security.user.id,const	1	100.00	Using where


explain select `user`.* from `user` left join user_permission_role on (`user`.id = user_id) where type = 'role' and name = 'root'
1	SIMPLE	user_permission_role		ref	type_pid,uid_type_pid	uid_type_pid	2	const	2	50.00	Using index condition; Using where
1	SIMPLE	user		eq_ref	PRIMARY	PRIMARY	8	security.user_permission_role.user_id	1	100.00	Using where


explain select `user`.* from `user` left join user_permission_role on (`user`.id = user_id) where type = 'role' and name = 'root' and username like 'ro%'
1	SIMPLE	user_permission_role		ref	type_pid,uid_type_pid	uid_type_pid	2	const	2	50.00	Using index condition; Using where
1	SIMPLE	user		eq_ref	PRIMARY,unique_username	PRIMARY	8	security.user_permission_role.user_id	1	50.00	Using where

explain update role_permission left join permission on role_permission.permission_id = permission.id
set role_permission.name = permission.name, role_permission.resource  = permission.resource, role_permission.describe = permission.describe


explain select count(*) from role left join role_permission on role.id = role_permission.role_id where role.name like 'ro%'

explain 
select role_permission.* from role_permission left join role on role.id = role_permission.role_id where role.name like 'A%' and enabled = 1


select * from role_permission left join role on role.id = role_permission.role_id where role.name like concat('root', '%') and `enabled` = 1

select role_permission.* from role_permission left join role on role.id = role_permission.role_id where role.name like concat('root', '%')  and `enabled` = 1 limit 0, 10

select permission_id from role_permission where permission_id = 1