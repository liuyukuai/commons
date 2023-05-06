package com.cutefool.commons.orm.rds.functions;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * rds function builder
 *
 * @author liuyk@tsingyun.net
 */
public class MysqlFunction implements Functioned {

    @Override
    public String createTable(HikariDataSource hikariDataSource) {
        return "CREATE PROCEDURE " + CREATE_TABLE + "(TableName VARCHAR(100),SqlStr VARCHAR(4000) CHARACTER SET UTF8)\n" +
                "BEGIN\n" +
                "\n" +
                "SET @countSql := CONCAT( 'SELECT COUNT(*) INTO @Rows1 FROM INFORMATION_SCHEMA.TABLES WHERE table_schema= DATABASE() AND table_name = ','\\'',TableName,'\\'');\n" +
                "\n" +
                "PREPARE stmt FROM @countSql;\n" +
                "EXECUTE stmt ;\n" +
                "\n" +
                "IF (@Rows1<=0) THEN\n" +
                "SET SqlStr := SqlStr;\n" +
                "SET @SQL1 = SqlStr;\n" +
                "SELECT @SQL1;\n" +
                "PREPARE stmt1 FROM @SQL1;\n" +
                "EXECUTE stmt1;\n" +
                "END IF;\n" +
                "END \n";
    }

    @Override
    public String modifyTableComment(HikariDataSource hikariDataSource) {
        return "CREATE PROCEDURE " + MODIFY_TABLE_COMMENT + "(TableName VARCHAR(100),comments VARCHAR(2000))\n" +
                "BEGIN\n" +
                "SET @crateSql := CONCAT( 'ALTER TABLE ', TableName, ' COMMENT ', '\\'',comments,'\\';');\n" +
                "SELECT @crateSql;\n" +
                "PREPARE stmt FROM @crateSql;\n" +
                "EXECUTE stmt ;\n" +
                "END \n";
    }

    @Override
    public String modifyColumnComment(HikariDataSource hikariDataSource) {
        return "CREATE  PROCEDURE " + MODIFY_COLUMNS_COMMENTS + "(TableName VARCHAR(100),ColumnName VARCHAR(100),comments VARCHAR(4000) CHARACTER SET UTF8)\n" +
                "BEGIN\n" +
                "SET @Column_name :=replace(ColumnName,'`','');\n" +
                "SET @Table_name :=replace(TableName,'`','');\n" +
                "SET @countSql := CONCAT( 'SELECT COUNT(*) INTO @Rows1 FROM INFORMATION_SCHEMA.Columns WHERE table_schema= DATABASE() AND table_name = ','\\'',@Table_name,'\\'',' AND column_name = ','\\'',@Column_name,'\\'');\n" +
                "PREPARE stmt FROM @countSql;\n" +
                "EXECUTE stmt ;\n" +
                "IF (@Rows1>0)  THEN\n" +
                "SET @QueryInfo := CONCAT( 'SELECT column_type,`IS_NULLABLE`,COLUMN_DEFAULT INTO @RowsType, @RowsNull,@RowsDefault FROM INFORMATION_SCHEMA.Columns WHERE table_schema= DATABASE() AND table_name = ','\\'',@Table_name,'\\'',' AND column_name = ','\\'',@Column_name,'\\'');\n" +
                "PREPARE stmtType FROM @QueryInfo;\n" +
                "EXECUTE stmtType ;\n" +
                "SET @RowsDefault:=IFNULL(@RowsDefault,'');\n" +
                "IF @RowsDefault <> '' AND @RowsDefault <> 'CURRENT_TIMESTAMP' THEN\n" +
                "SET @RowsDefault:=CONCAT(' DEFAULT \\'',@RowsDefault,'\\'');\n" +
                "ELSE \n" +
                "SET @RowsDefault='';" +
                "END IF;\n" +
                "SET @SqlStr :='';\n" +
                "IF @RowsNull = 'YES' THEN\n" +
                "SET @SqlStr :=CONCAT(@RowsType,' NULL ',@RowsDefault);\n" +
                "ELSE\n" +
                "SET @SqlStr :=CONCAT(@RowsType,' NOT NULL ' ,@RowsDefault);\n" +
                "END IF;\n" +
                "SET @SqlStr := CONCAT('ALTER TABLE ',TableName,' MODIFY  ',ColumnName,' ',@SqlStr,' COMMENT ','\\'',comments,'\\';');\n" +
                "SELECT @SqlStr;\n" +
                "PREPARE stmt1 FROM @SqlStr;\n" +
                "EXECUTE stmt1;\n" +
                "END IF;\n" +
                "END";
    }

    @Override
    public String modifyColumnsDefault(HikariDataSource hikariDataSource) {
        return "CREATE PROCEDURE " + MODIFY_COLUMNS_DEFAULT + "(TableName VARCHAR(100),ColumnName VARCHAR(100),v VARCHAR(200))\n" +
                "BEGIN\n" +
                "\n" +
                "SET @Column_name :=replace(ColumnName,'`','');\n" +
                "SET @Table_name :=replace(TableName,'`','');\n" +
                "SET @countSql := CONCAT( 'SELECT COUNT(*) INTO @Rows1 FROM INFORMATION_SCHEMA.Columns WHERE table_schema= DATABASE() AND table_name = ','\\'',@Table_name,'\\'',' AND column_name = ','\\'',@Column_name,'\\'');\n" +
                "PREPARE stmt FROM @countSql;\n" +
                "EXECUTE stmt ;\n" +
                "\n" +
                "IF ( @Rows1>0)  THEN\n" +
                "SET @QueryInfo := CONCAT( 'SELECT column_type,`IS_NULLABLE`,COLUMN_COMMENT INTO @RowsType, @RowsNull, @RowsComments FROM INFORMATION_SCHEMA.Columns WHERE table_schema= DATABASE() AND table_name = ','\\'',@Table_name,'\\'',' AND column_name = ','\\'',@Column_name,'\\'');\n" +
                "PREPARE stmtType FROM @QueryInfo;\n" +
                "EXECUTE stmtType ;\n" +
                "SET @SqlStr :='';\n" +
                "IF @RowsNull = 'YES' THEN\n" +
                "SET @SqlStr :=CONCAT(@RowsType,' NULL DEFAULT \\'',v,'\\'');\n" +
                "ELSE\n" +
                "SET @SqlStr :=CONCAT(@RowsType,' NOT NULL DEFAULT \\'',v,'\\'');\n" +
                "END IF;\n" +
                "SET @SqlStr := CONCAT('ALTER TABLE ',TableName,' MODIFY  ',ColumnName,' ',@SqlStr,' COMMENT ','\\'',@RowsComments,'\\';');\n" +
                "SELECT @SqlStr;\n" +
                "PREPARE stmt1 FROM @SqlStr;\n" +
                "EXECUTE stmt1;\n" +
                "END IF;\n" +
                "END";
    }

    @Override
    public String createIndex(HikariDataSource hikariDataSource) {
        return "CREATE PROCEDURE " + CREATE_INDEX + "(t VARCHAR(50),idx_type VARCHAR(50),idx_name VARCHAR(500),SqlStr VARCHAR(4000) CHARACTER SET UTF8)\n" +
                "BEGIN\n" +
                "SET @Table_name :=replace(t,'`','');\n" +
                "SET @countSql := CONCAT( 'SELECT COUNT(*) INTO @Rows1 FROM INFORMATION_SCHEMA.statistics WHERE table_schema= DATABASE() AND table_name = ','\\'', @Table_name,'\\'',' AND index_name = ','\\'',idx_name,'\\'');\n" +
                "PREPARE stmt FROM @countSql;\n" +
                "EXECUTE stmt ;\n" +
                "-- 新增列\n" +
                "IF (@Rows1<=0) THEN\n" +
                "SET SqlStr := CONCAT( 'ALTER TABLE ',t,' ADD ',idx_type,' index ',idx_name, '(',SqlStr,');');\n" +
                "SELECT SqlStr;\n" +
                "SET @SQL1 = SqlStr;\n" +
                "PREPARE stmt1 FROM @SQL1;\n" +
                "EXECUTE stmt1;\n" +
                "END IF;\n" +
                "END";
    }

    @Override
    public String dropIndex(HikariDataSource hikariDataSource) {
        return "CREATE PROCEDURE " + DROP_INDEX + "(t VARCHAR(50),idx_name VARCHAR(500))\n" +
                "BEGIN\n" +
                "SET @Table_name :=replace(t,'`','');\n" +
                "SET @countSqls := CONCAT( 'SELECT COUNT(*) INTO @Rowss FROM INFORMATION_SCHEMA.statistics WHERE table_schema= DATABASE() AND table_name = ','\\'',@Table_name,'\\'',' AND index_name = ','\\'',idx_name,'\\'');\n" +
                "PREPARE stmts FROM @countSqls;\n" +
                "EXECUTE stmts ;\n" +
                "IF (@Rowss>0) THEN\n" +
                "SET @sqlStr := CONCAT('ALTER TABLE ',t,' DROP INDEX ',idx_name,';');\n" +
                "SELECT @sqlStr;\n" +
                "PREPARE stmt1 FROM @sqlStr;\n" +
                "EXECUTE stmt1;\n" +
                "END IF;\n" +
                "END";
    }

    @Override
    public String renameIndex(HikariDataSource hikariDataSource) {
        return "CREATE PROCEDURE " + RENAME_INDEX + "(t VARCHAR(50),s_name VARCHAR(50),d_name VARCHAR(4000))\n" +
                "BEGIN\n" +
                "\n" +
                "SET @Table_name :=replace(t,'`','');\n" +
                "SET @countSqls := CONCAT( 'SELECT COUNT(*) INTO @Rowss FROM INFORMATION_SCHEMA.statistics WHERE table_schema= DATABASE() AND table_name = ','\\'',@Table_name,'\\'',' AND index_name = ','\\'',s_name,'\\'');\n" +
                "SET @countSqld := CONCAT( 'SELECT COUNT(*) INTO @Rowsd FROM INFORMATION_SCHEMA.statistics WHERE table_schema= DATABASE() AND table_name = ','\\'',@Table_name,'\\'',' AND index_name = ','\\'',d_name,'\\'');\n" +
                "\n" +
                "PREPARE stmts FROM @countSqls;\n" +
                "EXECUTE stmts ;\n" +
                "\n" +
                "PREPARE stmtd FROM @countSqld;\n" +
                "EXECUTE stmtd ;\n" +
                "\n" +
                "IF ( @Rowss>0 AND @Rowsd<=0) THEN\n" +
                "SET @SqlStr := CONCAT( 'ALTER TABLE ',t,' RENAME INDEX ',s_name, ' TO ' , d_name,';');\n" +
                "SELECT @SqlStr;\n" +
                "PREPARE stmt1 FROM @SqlStr;\n" +
                "EXECUTE stmt1;\n" +
                "END IF;\n" +
                "END";
    }

    @Override
    public String modifyColumn(HikariDataSource hikariDataSource) {
        return "CREATE PROCEDURE " + MODIFY_COLUMNS + "(TableName VARCHAR(100),ColumnName VARCHAR(100),SqlStr VARCHAR(4000))\n" +
                "BEGIN\n" +
                "\n" +
                "SET @Column_name :=replace(ColumnName,'`','');\n" +
                "SET @Table_name :=replace(TableName,'`','');\n" +
                "SET @countSql := CONCAT( 'SELECT COUNT(*) INTO @Rows1 FROM INFORMATION_SCHEMA.Columns WHERE table_schema= DATABASE() AND table_name = ','\\'',@Table_name,'\\'',' AND column_name = ','\\'',@Column_name,'\\'');\n" +
                "\n" +
                "PREPARE stmt FROM @countSql;\n" +
                "EXECUTE stmt ;\n" +
                "-- 新增列\n" +
                "IF (@Rows1<=0) THEN\n" +
                "SET @SqlStr := CONCAT( 'ALTER TABLE ',TableName,' ADD COLUMN ',ColumnName,' ',SqlStr);\n" +
                "PREPARE stmt1 FROM @SqlStr;\n" +
                "EXECUTE stmt1;\n" +
                "ELSEIF (@Rows1>0)  THEN\n" +
                "SET @SqlStr := CONCAT('ALTER TABLE ',TableName,' MODIFY  ',ColumnName,' ',SqlStr);\n" +
                "PREPARE stmt1 FROM @SqlStr;\n" +
                "EXECUTE stmt1;\n" +
                "END IF;\n" +
                "END";
    }

    @Override
    public String removeColumn(HikariDataSource hikariDataSource) {
        return "CREATE PROCEDURE " + DROP_COLUMNS + "(TableName VARCHAR(1000),ColumnName VARCHAR(500))\n" +
                "BEGIN\n" +
                "\n" +
                "SET @Column_name :=replace(ColumnName,'`','');\n" +
                "SET @Table_name :=replace(TableName,'`','');\n" +
                "SET @countSql := CONCAT( 'SELECT COUNT(*) INTO @Rows1 FROM INFORMATION_SCHEMA.Columns WHERE table_schema= DATABASE() AND table_name = ','\\'',@Table_name,'\\'',' AND column_name = ','\\'',@Column_name,'\\'');\n" +
                "\n" +
                "PREPARE stmt FROM @countSql;\n" +
                "EXECUTE stmt ;\n" +
                "-- 删除列\n" +
                "IF (@Rows1>0) THEN\n" +
                "SET @SqlStr := CONCAT('ALTER TABLE  ',TableName,' DROP COLUMN  ',ColumnName);\n" +
                "PREPARE stmt1 FROM @SqlStr;\n" +
                "EXECUTE stmt1;\n" +
                "END IF;\n" +
                "END";
    }

    @Override
    public String renameColumn(HikariDataSource hikariDataSource) {
        return "CREATE PROCEDURE " + RENAME_COLUMNS + "(TableName VARCHAR(100),sname VARCHAR(100),dname VARCHAR(100), sqlStr VARCHAR(4000))\n" +
                "BEGIN\n" +
                "\n" +
                "SET @sname :=replace(sname,'`','');\n" +
                "SET @dname :=replace(dname,'`','');\n" +
                "SET @Table_name :=replace(TableName,'`','');\n" +
                "SET @countSqls := CONCAT( 'SELECT COUNT(*) INTO @Rowss FROM INFORMATION_SCHEMA.Columns WHERE table_schema= DATABASE() AND table_name = ','\\'',@Table_name,'\\'',' AND column_name = ','\\'',@sname,'\\'');\n" +
                "SET @countSqld := CONCAT( 'SELECT COUNT(*) INTO @Rowsd FROM INFORMATION_SCHEMA.Columns WHERE table_schema= DATABASE() AND table_name = ','\\'',@Table_name,'\\'',' AND column_name = ','\\'',@dname,'\\'');\n" +
                "\n" +
                "PREPARE stmts FROM @countSqls;\n" +
                "EXECUTE stmts ;\n" +
                "PREPARE stmtd FROM @countSqld;\n" +
                "EXECUTE stmtd ;\n" +
                "\n" +
                "IF (@Rowss>0 AND @Rowsd<=0) THEN\n" +
                "SET sqlStr := CONCAT( 'ALTER TABLE ',TableName,' CHANGE ',sname,' ',dname,' ', sqlStr,';');\n" +
                "ELSE SET sqlStr :='';\n" +
                "END IF;\n" +
                "-- 执行命令\n" +
                "IF (sqlStr<>'') THEN\n" +
                "SET @SQL1 := sqlStr;\n" +
                "SELECT @SQL1;\n" +
                "PREPARE stmt1 FROM @SQL1;\n" +
                "EXECUTE stmt1;\n" +
                "END IF;\n" +
                "END";
    }

    @Override
    public String createViews(HikariDataSource hikariDataSource) {
        return "CREATE PROCEDURE " + CREATE_VIEWS + "(TableName VARCHAR(200),SqlStr VARCHAR(21845) CHARACTER SET UTF8)\n" +
                "BEGIN\n" +
                "\n" +
                "SET @dropSql := CONCAT( 'DROP VIEW IF EXISTS ',TableName);\n" +
                "PREPARE dropSql FROM @dropSql;\n" +
                "EXECUTE dropSql;\n" +
                "SET SqlStr := SqlStr;\n" +
                "SET @SQL1 = SqlStr;\n" +
                "SELECT @SQL1;\n" +
                "PREPARE stmt1 FROM @SQL1;\n" +
                "EXECUTE stmt1;\n" +
                "END \n";
    }

    @Override
    public String dropViews(HikariDataSource hikariDataSource) {
        return "CREATE PROCEDURE " + DROP_VIEWS + "(TableName VARCHAR(100))\n" +
                "BEGIN\n" +
                "\n" +
                "SET @dropSql := CONCAT( 'DROP VIEW IF EXISTS ',TableName);\n" +
                "PREPARE dropSql FROM @dropSql;\n" +
                "EXECUTE dropSql;\n" +
                "END \n";
    }

    public String modifyColumn_(HikariDataSource hikariDataSource) {
        return "CREATE  PROCEDURE `MODIFY_COLUMN`(TableName VARCHAR(50),ColumnName VARCHAR(50),CType INT,SqlStr VARCHAR(4000) CHARACTER SET UTF8)\n" +
                "BEGIN\n" +
                "\n" +
                "SET @countSql := CONCAT( 'SELECT COUNT(*) INTO @Rows1 FROM INFORMATION_SCHEMA.Columns WHERE table_schema= DATABASE() AND table_name = ','\\'',TableName,'\\'',' AND column_name = ','\\'',ColumnName,'\\'');\n" +
                "\n" +
                "PREPARE stmt FROM @countSql;\n" +
                "EXECUTE stmt ;\n" +
                "\n" +
                "-- 新增列\n" +
                "IF (CType=1 AND @Rows1<=0) THEN\n" +
                "SET SqlStr := CONCAT( 'ALTER TABLE ',TableName,' ADD COLUMN ',ColumnName,' ',SqlStr);\n" +
                "-- 修改列类型\n" +
                "ELSEIF (CType=2 AND @Rows1>0)  THEN\n" +
                "SET SqlStr := CONCAT('ALTER TABLE ',TableName,' MODIFY  ',ColumnName,' ',SqlStr);\n" +
                "-- 修改列名称\n" +
                "ELSEIF (CType=3 AND @Rows1>0) THEN\n" +
                "SET SqlStr := CONCAT('ALTER TABLE  ',TableName,' CHANGE  ',ColumnName,' ',SqlStr);\n" +
                "-- 删除列\n" +
                "ELSEIF (CType=4 AND @Rows1>0) THEN\n" +
                "SET SqlStr := CONCAT('ALTER TABLE  ',TableName,' DROP COLUMN  ',ColumnName);\n" +
                "ELSE  SET SqlStr :='';\n" +
                "END IF;\n" +
                "-- 执行命令\n" +
                "IF (SqlStr<>'') THEN\n" +
                "SET @SQL1 = SqlStr;\n" +
                "PREPARE stmt1 FROM @SQL1;\n" +
                "EXECUTE stmt1;\n" +
                "END IF;\n" +
                "END";
    }

    public String modifyIndex_(HikariDataSource hikariDataSource) {
        return "CREATE PROCEDURE `MODIFY_INDEX`(TableName VARCHAR(50),indexName VARCHAR(50),CType INT,SqlStr VARCHAR(4000) CHARACTER SET UTF8)\n" +
                "BEGIN\n" +
                "\n" +
                "SET @countSql := CONCAT( 'SELECT COUNT(*) INTO @Rows1 FROM INFORMATION_SCHEMA.statistics WHERE table_schema= DATABASE() AND table_name = ','\\'',TableName,'\\'',' AND index_name = ','\\'',indexName,'\\'');\n" +
                "\n" +
                "PREPARE stmt FROM @countSql;\n" +
                "EXECUTE stmt ;\n" +
                "\n" +
                "-- 新增列\n" +
                "IF (CType=1 AND @Rows1<=0) THEN\n" +
                "SET SqlStr := CONCAT( 'ALTER TABLE ',TableName,' ADD ',SqlStr);\n" +
                "-- 重命名\n" +
                "ELSEIF (CType=3 AND @Rows1>0) THEN\n" +
                "SET SqlStr := CONCAT( 'ALTER TABLE ',TableName,' RENAME INDEX ',indexName, ' TO ' , SqlStr);\n" +
                "-- 删除列\n" +
                "ELSEIF (CType=4 AND @Rows1>0) THEN\n" +
                "SET SqlStr := CONCAT('ALTER TABLE ',TableName,' DROP INDEX ',indexName);\n" +
                "ELSE  SET SqlStr :='';\n" +
                "END IF;\n" +
                "-- 执行命令\n" +
                "IF (SqlStr<>'') THEN\n" +
                "SET @SQL1 = SqlStr;\n" +
                "PREPARE stmt1 FROM @SQL1;\n" +
                "EXECUTE stmt1;\n" +
                "END IF;\n" +
                "END";
    }

    @Override
    @SuppressWarnings("all")
    public void doExecute(HikariDataSource hikariDataSource) {
        Functioned.super.doExecute(hikariDataSource);
        // 兼容历史函数，项目替换完成收去掉即可
        JdbcTemplate jdbcTemplate = new JdbcTemplate(hikariDataSource);
        jdbcTemplate.execute(String.format(DROP_PROCEDURE, "MODIFY_COLUMN"));
        jdbcTemplate.execute(String.format(DROP_PROCEDURE, "MODIFY_INDEX"));
        jdbcTemplate.execute(this.modifyColumn_(hikariDataSource));
        jdbcTemplate.execute(this.modifyIndex_(hikariDataSource));
    }
}
