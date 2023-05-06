-- 创建周期配置任务
CALL CREATE_TABLES('JOB_CYCLE','CREATE TABLE `JOB_CYCLE` (
	`ID` BIGINT NOT NULL,
	`APP_ID` VARCHAR(50) NOT NULL,
	`NAME` VARCHAR(100) NOT NULL,
	`CYCLE` VARCHAR(50) NOT NULL,
	`DURATION` BIGINT NOT NULL,
	`PARAMETERS` VARCHAR(500) NULL,
	`EXECUTOR` VARCHAR(500) NULL,
	`DESCRIPTION` VARCHAR(500) NULL,
	`FIRST_TIME` DATETIME NOT NULL,
	`NEXT_TIME` DATETIME NULL,
	`IS_STOP` TINYINT NOT NULL,
	`CREATE_TIME` DATETIME NOT NULL,
	`CREATOR` BIGINT NULL,
	`REVISE_TIME` DATETIME NULL,
	`REVISER` BIGINT NULL,
	`TENANT_ID` BIGINT NULL,
	`IDENTIFIER` VARCHAR(200) NULL,
	`JOB_TYPE` VARCHAR(200) NULL,
	PRIMARY KEY (`ID`)
	);'
);
-- 设置注释周期配置任务
CALL MODIFY_TABLE_COMMENTS('JOB_CYCLE', '周期配置任务|周期配置任务');
-- 设置注释周期配置任务
CALL MODIFY_COLUMNS_COMMENTS('JOB_CYCLE', 'ID', '主键|主键');
CALL MODIFY_COLUMNS_COMMENTS('JOB_CYCLE', 'APP_ID', '应用|应用');
CALL MODIFY_COLUMNS_COMMENTS('JOB_CYCLE', 'NAME', '名称|名称');
CALL MODIFY_COLUMNS_COMMENTS('JOB_CYCLE', 'CYCLE', '周期|周期');
CALL MODIFY_COLUMNS_COMMENTS('JOB_CYCLE', 'DURATION', '周期间隔|周期间隔');
CALL MODIFY_COLUMNS_COMMENTS('JOB_CYCLE', 'PARAMETERS', '自定义参数|自定义参数');
CALL MODIFY_COLUMNS_COMMENTS('JOB_CYCLE', 'EXECUTOR', '任务执行器|任务执行器');
CALL MODIFY_COLUMNS_COMMENTS('JOB_CYCLE', 'DESCRIPTION', '描述|描述');
CALL MODIFY_COLUMNS_COMMENTS('JOB_CYCLE', 'FIRST_TIME', '第一次执行时间|第一次执行时间');
CALL MODIFY_COLUMNS_COMMENTS('JOB_CYCLE', 'NEXT_TIME', '下一次执行时间|下一次执行时间');
CALL MODIFY_COLUMNS_COMMENTS('JOB_CYCLE', 'IS_STOP', '是否终止 0：未终止1：已终止（不在执行）|是否终止 0：未终止1：已终止（不在执行）');
CALL MODIFY_COLUMNS_COMMENTS('JOB_CYCLE', 'CREATE_TIME', '创建时间|创建时间');
CALL MODIFY_COLUMNS_COMMENTS('JOB_CYCLE', 'CREATOR', '创建人|创建人');
CALL MODIFY_COLUMNS_COMMENTS('JOB_CYCLE', 'REVISE_TIME', '修改时间|修改时间');
CALL MODIFY_COLUMNS_COMMENTS('JOB_CYCLE', 'REVISER', '修改人|修改人');
CALL MODIFY_COLUMNS_COMMENTS('JOB_CYCLE', 'TENANT_ID', '租户|租户');
CALL MODIFY_COLUMNS_COMMENTS('JOB_CYCLE', 'IDENTIFIER', '业务唯一标识|业务唯一标识');
CALL MODIFY_COLUMNS_COMMENTS('JOB_CYCLE', 'JOB_TYPE', '任务类型|任务类型');
-- 设置默认值周期配置任务
CALL MODIFY_COLUMNS_DEFAULT('JOB_CYCLE', 'IS_STOP', '0');
CALL MODIFY_COLUMNS_DEFAULT('JOB_CYCLE', 'TENANT_ID', '0');
