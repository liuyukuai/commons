-- -- 周期配置任务
-- CREATE TABLE IF NOT EXISTS `job_cycle` (
--   `id` BIGINT(20) NOT NULL COMMENT '主键',
--   `app_id` VARCHAR(50) NOT NULL COMMENT '应用',
--   `name` VARCHAR(100) NOT NULL COMMENT '名称',
--   `cycle` VARCHAR(50) NOT NULL COMMENT '周期',
--   `duration` BIGINT(20) NOT NULL COMMENT '周期间隔',
--   `parameters` VARCHAR(500) DEFAULT NULL COMMENT '自定义参数',
--   `executor` VARCHAR(500) DEFAULT NULL COMMENT '任务执行器',
--   `description` VARCHAR(500) DEFAULT NULL COMMENT '描述',
--   `first_time` datetime NOT NULL COMMENT '第一次执行时间',
--   `next_time` datetime DEFAULT NULL COMMENT '下一次执行时间',
--   `is_stop` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否终止 0：未终止1：已终止（不在执行）',
--   `create_time` datetime NOT NULL COMMENT '创建时间',
--   `creator` bigint(20) DEFAULT NULL COMMENT '创建人',
--   `revise_time` datetime DEFAULT NULL COMMENT '修改时间',
--   `reviser` bigint(20) DEFAULT NULL COMMENT '修改人',
--   `tenant_id` bigint(20) DEFAULT 0 COMMENT '租户',
--   PRIMARY KEY (`id`)
-- ) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='周期配置任务';
--
-- -- 任务实例
-- CREATE TABLE IF NOT EXISTS `job_instance` (
--   `id` BIGINT(20) NOT NULL COMMENT '主键',
--   `job_id` BIGINT(20) NOT NULL COMMENT 'jobId',
--   `app_id` VARCHAR(50) NOT NULL COMMENT '应用',
--   `trigger_time` datetime NOT NULL COMMENT '触发时间',
--   `execute_time` datetime NOT NULL COMMENT '执行时间',
--   `state` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否终止 0：未终止1：已终止（不在执行）',
--   `create_time` datetime NOT NULL COMMENT '创建时间',
--   `creator` bigint(20) DEFAULT NULL COMMENT '创建人',
--   `revise_time` datetime DEFAULT NULL COMMENT '修改时间',
--   `reviser` bigint(20) DEFAULT NULL COMMENT '修改人',
--   `tenant_id` bigint(20) DEFAULT 0 COMMENT '租户',
--   PRIMARY KEY (`id`)
-- ) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='任务实例';
--
--
-- CALL MODIFY_COLUMN('job_cycle','identifier',1,'varchar(200) DEFAULT NULL COMMENT "业务唯一标识"');
-- CALL MODIFY_COLUMN('job_cycle','job_type',1,'varchar(200) DEFAULT NULL COMMENT "任务类型"');