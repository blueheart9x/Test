SELECT t.id, t.email, t.full_name AS fullName, t.mobile, t.link_cv AS linkCv, t.avatar
, CONCAT('[', GROUP_CONCAT(JSON_OBJECT('job_name', t.name, 'procedureLst', t.procedureLst)), ']') AS jobLst
FROM (select u.id, u.email, uc.full_name, uc.mobile, uc.link_cv, uc.avatar, j.name
, CONCAT('[', GROUP_CONCAT(JSON_OBJECT('name', p.name, 'type', p.type, 'status', l.status)), ']') AS procedureLst
FROM user u INNER JOIN user_company uc ON u.id = uc.user_id
LEFT JOIN job j ON j.id = uc.job_id
LEFT JOIN letter l ON u.id = l.user_to
LEFT JOIN procedures p ON p.id = l.procedure_id
WHERE u.uuid = '94509a5c-23aa-4c32-98fb-9b1beb8b5bc5' AND uc.is_delete = 0 AND uc.company_id = 19
GROUP BY l.job_name ORDER BY l.created_at DESC) t;

select * from user_company uc inner join user u on u.id=uc.user_id
where u.uuid='94509a5c-23aa-4c32-98fb-9b1beb8b5bc5' and uc.company_id=19;
select * from letter where id=7;
select * from user order by id desc;
select * from user_company order by id desc;

SELECT t.id, t.email, t.full_name AS fullName, t.mobile, t.link_cv AS linkCv, t.avatar
, CONCAT('[', GROUP_CONCAT(JSON_OBJECT('job_name', t.job_name, 'created_at', t.created_at, 'procedureLst', t.procedureLst)), ']') AS jobLst
FROM (select u.id, u.email, uc.full_name, uc.mobile, uc.link_cv, uc.avatar, l.job_name, l.created_at
, CONCAT('[', GROUP_CONCAT(JSON_OBJECT('name', p.name, 'type', p.type, 'status', l.status)), ']') AS procedureLst
FROM user u INNER JOIN user_company uc ON u.id = uc.user_id
LEFT JOIN letter l ON u.id = l.user_to
LEFT JOIN procedures p ON p.id = l.procedure_id
WHERE u.uuid = '94509a5c-23aa-4c32-98fb-9b1beb8b5bc5' AND uc.is_delete = 0 AND uc.company_id = 19
GROUP BY l.job_name ORDER BY l.created_at DESC) t;



SELECT u.id, u.uuid, u.email, uc.link_cv AS linkCv, uc.full_name AS fullName, uc.mobile, uc.avatar,
 uc.created_at AS createdAt  , GROUP_CONCAT(j.name SEPARATOR '###') AS jobNameLst FROM  user_company uc 
 INNER JOIN user u ON (u.id = uc.user_id AND uc.company_id = 36) 
 LEFT JOIN job j ON j.id = uc.job_id 
 WHERE uc.is_delete = 0  GROUP BY u.email ORDER by u.id desc;
 
 
 SELECT t.id, t.email, t.full_name AS fullName, t.mobile, t.link_cv AS linkCv, t.avatar
, CONCAT('[', GROUP_CONCAT(JSON_OBJECT('job_name', t.name, 'job_id', t.jobId, 'procedureLst', t.procedureLst)), ']') AS jobLst,
t.ratingId
FROM (select u.id, u.email, uc.full_name, uc.mobile, uc.link_cv, uc.avatar, j.name, j.id as jobId
, CONCAT('[', GROUP_CONCAT(JSON_OBJECT('letterId', l.id ,'letterStatus', l.status, 'name', p.name, 'type', p.type)), ']') AS procedureLst
, (select id from _rating_result where object_type='LETTER' AND l.id = object_id AND user_id = u.id LIMIT 1) as ratingId
FROM user u INNER JOIN user_company uc ON u.id = uc.user_id
LEFT JOIN job j ON j.id = uc.job_id
LEFT JOIN letter l ON u.id = l.user_to
LEFT JOIN procedures p ON p.id = l.procedure_id
WHERE u.uuid = '94509a5c-23aa-4c32-98fb-9b1beb8b5bc5' AND uc.is_delete = 0 AND uc.company_id = 19
GROUP BY l.job_name ORDER BY l.created_at DESC) t;

select * from user order by id;
select * from _rating_result;
select * from letter order by appointment_time desc;

select * from user order by id desc;
select * from user_company order by id desc;

select * from elcom_user order by id desc;
select user0_.id as id1_0_0_, user0_.created_at as created_2_0_0_, user0_.full_name as full_nam3_0_0_, user0_.password as password4_0_0_, user0_.username as username5_0_0_ from elcom_user user0_ where user0_.id=11;
/*CREATE TABLE `elcom_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `password` varchar(256) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT '$2a$10$QlCVldsah3fjkyNOsq6Xi.t9x75UOjxISjTiVvFkamgDaO59vQb.O' COMMENT 'Mặc định: ''123456''',
  `full_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;*/



SELECT t.id, t.email, t.full_name AS fullName, t.mobile, t.link_cv AS linkCv, t.avatar
			, CONCAT('[', GROUP_CONCAT(JSON_OBJECT('job_name', t.name, 'job_id', t.jobId, 'procedureLst', t.procedureLst)), ']') AS jobLst
			-- , t.ratingResultId
			FROM (select u.id, u.email, uc.full_name, uc.mobile, uc.link_cv, uc.avatar, l.job_name as name, l.job_id as jobId
			, CONCAT('[', GROUP_CONCAT(DISTINCT(JSON_OBJECT('letterId', l.id ,'letterStatus', l.status, 'name', p.name, 'type', p.type))), ']') AS procedureLst
			-- , (select id from _rating_result where object_type='LETTER' AND l.id = object_id AND user_id = u.id LIMIT 1) as ratingResultId
			FROM user u INNER JOIN user_company uc ON u.id = uc.user_id
			LEFT JOIN letter l ON (u.id = l.user_to AND l.company_id=45)
			LEFT JOIN procedures p ON p.id = l.procedure_id
			WHERE u.uuid = '90abbf0a-6da1-48d0-8a3c-7f13dbe02af5'
            AND uc.is_delete = 0
            AND uc.company_id = 45
			GROUP BY l.job_name ORDER BY l.created_at DESC) t;
            
            select * from user where uuid='90abbf0a-6da1-48d0-8a3c-7f13dbe02af5';
            select * from user where email='hathithu9090@gmail.com';
            select * from user_company where user_id in (115, 11555555);
            select * from letter where user_to = 115;
            
            select * from media;