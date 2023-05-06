package com.cutefool.commons.orm.rds.functions;

import com.zaxxer.hikari.HikariDataSource;

/**
 * rds function builder
 *
 * @author liuyk@tsingyun.net
 */
public class KBFunction implements Functioned {

    @Override
    public String createTable(HikariDataSource hikariDataSource) {
        return "CREATE OR REPLACE PROCEDURE " + CREATE_TABLE + "(tableName varchar(200), sqlStr varchar(4000))\n" +
                "AS \n" +
                "c integer ;\n" +
                "BEGIN\n" +
                "    SELECT count(*) INTO c FROM ALL_TABLES WHERE TABLE_NAME = tableName;\n" +
                "    IF c<1\n" +
                "    THEN \n" +
                "      sqlStr=replace(sqlStr,'`'，'\"');\n" +
                "      EXECUTE IMMEDIATE sqlStr;\n" +
                "    END IF;\n" +
                "END;";
    }

    @Override
    public String modifyTableComment(HikariDataSource hikariDataSource) {
        return "CREATE OR REPLACE PROCEDURE " + MODIFY_TABLE_COMMENT + "(t varchar(200), a varchar(500))\n" +
                "AS \n" +
                "c integer ;\n" +
                "s varchar ;\n" +
                "BEGIN\n" +
                "    t=replace(t,'`','');\n" +
                "    SELECT count(*) INTO c FROM ALL_TABLES WHERE TABLE_NAME = t;\n" +
                "    IF c>0 THEN\n" +
                "    s := concat('COMMENT ON TABLE ',t,' IS ',E'\\'',a,E'\\';') ;\n" +
                "    EXECUTE IMMEDIATE s;\n" +
                "    END IF;\n" +
                "END;";
    }

    @Override
    public String modifyColumnComment(HikariDataSource hikariDataSource) {
        return "CREATE OR REPLACE PROCEDURE " + MODIFY_COLUMNS_COMMENTS + "(t varchar(200),c varchar(200),remark varchar(4000))\n" +
                "AS \n" +
                "tc integer ;\n" +
                "cc integer ;\n" +
                "s varchar ;\n" +
                "BEGIN\n" +
                "    t=replace(t,'`','');\n" +
                "    c=replace(c,'`','');\n" +
                "    SELECT count(*) INTO tc FROM ALL_TABLES WHERE TABLE_NAME = t;\n" +
                "    SELECT count(*) INTO cc FROM \"information_schema\".\"columns\"  WHERE TABLE_NAME = t AND COLUMN_NAME=c;\n" +
                "    IF tc>0 AND cc>0 THEN\n" +
                "    s := concat('COMMENT ON COLUMN ',t,'.',c,' IS ',E'\\'',remark,E'\\';') ;\n" +
                "    EXECUTE IMMEDIATE s;\n" +
                "    END IF;\n" +
                "END;";
    }

    @Override
    public String modifyColumnsDefault(HikariDataSource hikariDataSource) {
        return "CREATE OR REPLACE PROCEDURE " + MODIFY_COLUMNS_DEFAULT + "(t VARCHAR,c VARCHAR,v VARCHAR )\n" +
                "AS \n" +
                "cc integer ;\n" +
                "vStr varchar ;\n" +
                "BEGIN\n" +
                "t=replace(t,'`','');\n" +
                "c=replace(c,'`','');\n" +
                "SELECT count(*) INTO cc FROM \"information_schema\".\"columns\"  WHERE TABLE_NAME = t AND COLUMN_NAME=c;\n" +
                "IF cc>0 THEN\n" +
                " vStr := CONCAT('ALTER TABLE  ',t,' ALTER COLUMN  ',c,' SET DEFAULT ',E'\\'',v,E'\\'',';');\n" +
                "EXECUTE IMMEDIATE vStr;\n" +
                "END IF;\n" +
                "END";
    }

    @Override
    public String createIndex(HikariDataSource hikariDataSource) {
        return "CREATE OR REPLACE PROCEDURE " + CREATE_INDEX + "(t varchar, idx_type varchar, idx_name varchar, SqlStr varchar)\n" +
                "AS \n" +
                "cc integer ;\n" +
                "vStr varchar ;\n" +
                "BEGIN\n" +
                "t=replace(t,'`','');\n" +
                "SELECT count(*) INTO cc FROM sys_indexes WHERE tablename=t AND indexname=idx_name;\n" +
                "IF cc <= 0 THEN\n" +
                " vStr := CONCAT( 'CREATE ',idx_type,' index ',idx_name,' ON ', t,'(', SqlStr,');');\n" +
                " EXECUTE IMMEDIATE vStr;\n" +
                "END IF;\n" +
                "END;\n";
    }

    @Override
    public String dropIndex(HikariDataSource hikariDataSource) {
        return "CREATE OR REPLACE PROCEDURE " + DROP_INDEX + "(t varchar, idx_name varchar)\n" +
                "AS \n" +
                "cc integer ;\n" +
                "vStr varchar ;\n" +
                "BEGIN\n" +
                "t=replace(t,'`','');\n" +
                "SELECT count(*) INTO cc FROM sys_indexes WHERE indexname=idx_name AND tablename=t;\n" +
                "IF cc>0 THEN\n" +
                " vStr := CONCAT(' DROP INDEX ',idx_name);\n" +
                " EXECUTE IMMEDIATE vStr;\n" +
                "END IF;\n" +
                "END;\n";
    }

    @Override
    public String renameIndex(HikariDataSource hikariDataSource) {
        return "CREATE OR REPLACE PROCEDURE " + RENAME_INDEX + "(t varchar, s_name varchar, d_name varchar)\n" +
                "AS \n" +
                "sc integer ;\n" +
                "dc integer ;\n" +
                "vStr varchar ;\n" +
                "BEGIN\n" +
                "t=replace(t,'`','');\n" +
                "SELECT count(*) INTO sc FROM sys_indexes WHERE indexname=s_name;\n" +
                "SELECT count(*) INTO dc FROM sys_indexes WHERE indexname=d_name;\n" +
                "IF sc>0 AND dc<=0 THEN\n" +
                " vStr := CONCAT(' ALTER INDEX ',s_name,' RENAME TO ', d_name);\n" +
                " EXECUTE IMMEDIATE vStr;\n" +
                "END IF;\n" +
                "END;\n";
    }

    @Override
    public String modifyColumn(HikariDataSource hikariDataSource) {
        return "CREATE OR REPLACE PROCEDURE " + MODIFY_COLUMNS + "(t VARCHAR,c VARCHAR,SqlStr VARCHAR )\n" +
                "AS \n" +
                "cc integer ;\n" +
                "vStr varchar ;\n" +
                "BEGIN\n" +
                "t=replace(t,'`','');\n" +
                "c=replace(c,'`','');\n" +
                "SELECT count(*) INTO cc FROM \"information_schema\".\"columns\"  WHERE TABLE_NAME = t AND COLUMN_NAME=c;\n" +
                "IF cc <= 0 THEN\n" +
                " SqlStr := CONCAT( 'ALTER TABLE ',t,' ADD COLUMN ',c,' ',SqlStr);\n" +
                " EXECUTE IMMEDIATE SqlStr;\n" +
                "ELSEIF cc>0  THEN\n" +
                " SqlStr := CONCAT('ALTER TABLE ',t,' MODIFY  ',c,' ',SqlStr);\n" +
                " EXECUTE IMMEDIATE SqlStr;\n" +
                "END IF;\n" +
                "END";
    }

    @Override
    public String removeColumn(HikariDataSource hikariDataSource) {
        return "CREATE OR REPLACE PROCEDURE " + DROP_COLUMNS + "(t VARCHAR,c VARCHAR)\n" +
                "AS \n" +
                "cc integer ;\n" +
                "SqlStr varchar ;\n" +
                "BEGIN\n" +
                "t=replace(t,'`','');\n" +
                "c=replace(c,'`','');\n" +
                "SELECT count(*) INTO cc FROM \"information_schema\".\"columns\"  WHERE TABLE_NAME = t AND COLUMN_NAME=c;\n" +
                "IF cc > 0 THEN\n" +
                " SqlStr := CONCAT('ALTER TABLE  ',t,' DROP COLUMN  ',c);\n" +
                " EXECUTE IMMEDIATE SqlStr;\n" +
                "END IF;\n" +
                "END";
    }

    @Override
    public String renameColumn(HikariDataSource hikariDataSource) {
        return "CREATE OR REPLACE PROCEDURE " + RENAME_COLUMNS + "(t varchar, sname varchar, dname varchar, SqlStr varchar)\n" +
                "AS \n" +
                "sc integer ;\n" +
                "dc integer ;\n" +
                "vStr varchar ;\n" +
                "BEGIN\n" +
                "t=replace(t,'`','');\n" +
                "sname=replace(sname,'`','');\n" +
                "dname=replace(dname,'`','');\n" +
                "SELECT count(*) INTO sc FROM \"information_schema\".\"columns\"  WHERE TABLE_NAME = t AND COLUMN_NAME=sname;\n" +
                "SELECT count(*) INTO dc FROM \"information_schema\".\"columns\"  WHERE TABLE_NAME = t AND COLUMN_NAME=dname;\n" +
                "IF sc>=0 AND dc <= 0 THEN\n" +
                "  vStr := CONCAT('ALTER TABLE ',t,' RENAME COLUMN ', sname,'  TO ',dname,';');\n" +
                "  EXECUTE IMMEDIATE vStr;\n" +
                "END IF;\n" +
                "END;";
    }

    @Override
    public String createViews(HikariDataSource hikariDataSource) {
        return "CREATE OR REPLACE PROCEDURE " + CREATE_VIEWS + "(tableName varchar(200), sqlStr varchar(4000))\n" +
                "AS \n" +
                "DS varchar ;\n" +
                "BEGIN\n" +
                "DS :=CONCAT('DROP VIEW IF EXISTS ', T);\n" +
                "EXECUTE IMMEDIATE DS;\n" +
                "SQLSTR=replace(SQLSTR,'`','\"');\n" +
                "EXECUTE IMMEDIATE SQLSTR;\n" +
                "END;";
    }

    @Override
    public String dropViews(HikariDataSource hikariDataSource) {
        return "CREATE OR REPLACE PROCEDURE " + DROP_VIEWS + "(tableName varchar(200))\n" +
                "AS \n" +
                "DS varchar ;\n" +
                "BEGIN\n" +
                "DS :=CONCAT('DROP VIEW IF EXISTS ', T);\n" +
                "EXECUTE IMMEDIATE DS;\n" +
                "END;";
    }
}
