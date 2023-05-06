package com.cutefool.commons.orm.rds.functions;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * rds function builder
 *
 * @author liuyk@tsingyun.net
 */
public class DMFunction implements Functioned {

    @Override
    public String createTable(HikariDataSource hikariDataSource) {
        return "CREATE OR REPLACE PROCEDURE " + this.doTable(hikariDataSource, CREATE_TABLE) + "(\"T\" IN VARCHAR(200),\"SQLSTR\" IN VARCHAR(4000))\n" +
                "AUTHID DEFINER\n" +
                "\n" +
                "AS \n" +
                "C integer ;\n" +
                "BEGIN\n" +
                "    SELECT count(*) INTO C FROM ALL_TABLES WHERE (TABLE_NAME = T OR TABLE_NAME = LOWER(T) OR TABLE_NAME = UPPER(T)) AND OWNER='" + hikariDataSource.getSchema() + "';\n" +
                "    IF C<1 THEN \n" +
                "      SQLSTR=replace(SQLSTR,'`','\"');\n" +
                "      SQLSTR=replace(SQLSTR,'JSON','LONGVARCHAR');\n" +
                "      SQLSTR=replace(SQLSTR,'json','LONGVARCHAR');\n" +
                "      SQLSTR=replace(SQLSTR,'LONGTEXT','LONGVARCHAR');\n" +
                "      SQLSTR=replace(SQLSTR,'longtext','LONGVARCHAR');\n" +
                "      SQLSTR=replace(SQLSTR,'MEDIUMTEXT','LONGVARCHAR');\n" +
                "      SQLSTR=replace(SQLSTR,'mediumtext','LONGVARCHAR');\n" +
                "      SELECT SQLSTR;\n" +
                "      EXECUTE IMMEDIATE SQLSTR;\n" +
                "    END IF;\n" +
                "END;";
    }

    @Override
    public String modifyTableComment(HikariDataSource hikariDataSource) {
        String schema = this.getSchema(hikariDataSource);
        return "CREATE OR REPLACE PROCEDURE " + this.doTable(hikariDataSource, MODIFY_TABLE_COMMENT) + "(\"T\" IN VARCHAR(32767),\"A\" IN VARCHAR(32767))\n" +
                "AUTHID DEFINER\n" +
                "\n" +
                "AS \n" +
                "C integer ;\n" +
                "S varchar ;\n" +
                "BEGIN\n" +
                "    T=replace(T,'`','');\n" +
                "    SELECT count(*) INTO C FROM ALL_TABLES WHERE (TABLE_NAME = T OR TABLE_NAME = LOWER(T) OR TABLE_NAME = UPPER(T)) AND OWNER='" + hikariDataSource.getSchema() + "';\n" +
                "    IF c>0 THEN\n" +
                "    S := concat('COMMENT ON TABLE ','" + schema + ".\"',T,'\" IS ','''',A,'''',';') ;\n" +
                " \tEXECUTE IMMEDIATE S;\n" +
                "    END IF;\n" +
                "END;";
    }

    @Override
    public String modifyColumnComment(HikariDataSource hikariDataSource) {
        String schema = this.getSchema(hikariDataSource);
        return "CREATE OR REPLACE PROCEDURE " + this.doTable(hikariDataSource, MODIFY_COLUMNS_COMMENTS) + "(\"T\" IN VARCHAR(200),\"C\" IN VARCHAR(200),\"REMARK\" IN VARCHAR(4000))\n" +
                "AUTHID DEFINER\n" +
                "\n" +
                "AS \n" +
                "TC integer ;\n" +
                "CC integer ;\n" +
                "S varchar ;\n" +
                "BEGIN\n" +
                "    T=replace(T,'`','');\n" +
                "    C=replace(C,'`','');\n" +
                "    SELECT count(*) INTO TC FROM ALL_TABLES WHERE (TABLE_NAME = T OR TABLE_NAME = LOWER(T) OR TABLE_NAME = UPPER(T)) AND OWNER='" + hikariDataSource.getSchema() + "';\n" +
                "    SELECT count(*) INTO CC FROM all_tab_columns  WHERE (TABLE_NAME = T OR TABLE_NAME = LOWER(T) OR TABLE_NAME = UPPER(T)) AND (COLUMN_NAME=C OR COLUMN_NAME=UPPER(C) OR COLUMN_NAME=LOWER(C)) AND OWNER='" + hikariDataSource.getSchema() + "';\n" +
                "    IF TC>0 AND CC>0 THEN\n" +
                "    S := concat('COMMENT ON COLUMN ','" + schema + ".\"',T,'\".\"',C,'\" IS ','''',REMARK,'''',';') ;\n" +
                "    EXECUTE IMMEDIATE S;\n" +
                "    END IF;\n" +
                "END;";
    }

    @Override
    public String modifyColumnsDefault(HikariDataSource hikariDataSource) {
        String schema = this.getSchema(hikariDataSource);
        return "CREATE OR REPLACE PROCEDURE " + this.doTable(hikariDataSource, MODIFY_COLUMNS_DEFAULT) + "(\"T\" IN VARCHAR(32767),\"C\" IN VARCHAR(32767),\"V\" IN VARCHAR(32767))\n" +
                "AUTHID DEFINER\n" +
                "\n" +
                "AS \n" +
                "CC integer ;\n" +
                "VSTR varchar ;\n" +
                "BEGIN\n" +
                "T=replace(T,'`','');\n" +
                "C=replace(C,'`','');\n" +
                "SELECT count(*) INTO CC FROM all_tab_columns  WHERE (TABLE_NAME = T OR TABLE_NAME = LOWER(T) OR TABLE_NAME = UPPER(T)) AND (COLUMN_NAME=C OR COLUMN_NAME=UPPER(C) OR COLUMN_NAME=LOWER(C)) AND OWNER='" + hikariDataSource.getSchema() + "';\n" +
                "IF CC>0 THEN\n" +
                " VSTR := CONCAT('ALTER TABLE  ','" + schema + ".\"',T,'\" ALTER COLUMN  \"',C,'\" SET DEFAULT ','''',V,'''',';');\n" +
                " EXECUTE IMMEDIATE VSTR;\n" +
                "END IF;\n" +
                "END;";
    }

    @Override
    public String createIndex(HikariDataSource hikariDataSource) {
        return "CREATE OR REPLACE PROCEDURE " + this.doTable(hikariDataSource, CREATE_INDEX) + "(\"T\" IN VARCHAR(32767),\"IDX_TYPE\" IN VARCHAR(32767),\"IDX_NAME\" IN VARCHAR(32767),\"SQLSTR\" IN VARCHAR(32767))\n" +
                "AUTHID DEFINER\n" +
                "\n" +
                "AS \n" +
                "CC integer ;\n" +
                "VSTR varchar ;\n" +
                "BEGIN\n" +
                "T=replace(T,'`','');\n" +
                "SELECT count(*) INTO CC FROM ALL_INDEXES WHERE (TABLE_NAME = T OR TABLE_NAME = LOWER(T) OR TABLE_NAME = UPPER(T)) AND (INDEX_NAME=IDX_NAME OR INDEX_NAME=UPPER(IDX_NAME) OR INDEX_NAME=LOWER(IDX_NAME)) AND OWNER='" + hikariDataSource.getSchema() + "';\n" +
                "IF CC <= 0 THEN\n" +
                " VSTR := CONCAT( 'CREATE ',IDX_TYPE,' index ',IDX_NAME,' ON ', T,'(', SQLSTR,');');\n" +
                " EXECUTE IMMEDIATE VSTR;\n" +
                "END IF;\n" +
                "END;";
    }

    @Override
    public String dropIndex(HikariDataSource hikariDataSource) {
        return "CREATE OR REPLACE PROCEDURE " + this.doTable(hikariDataSource, DROP_INDEX) + "(\"T\" IN VARCHAR(32767),\"IDX_NAME\" IN VARCHAR(32767))\n" +
                "AUTHID DEFINER\n" +
                "\n" +
                "AS \n" +
                "CC integer ;\n" +
                "VSTR varchar ;\n" +
                "BEGIN\n" +
                "T=replace(T,'`','');\n" +
                "SELECT count(*) INTO CC FROM ALL_INDEXES WHERE (TABLE_NAME = T OR TABLE_NAME = LOWER(T) OR TABLE_NAME = UPPER(T)) AND (INDEX_NAME=IDX_NAME OR INDEX_NAME=UPPER(IDX_NAME) OR INDEX_NAME=LOWER(IDX_NAME)) AND OWNER='" + hikariDataSource.getSchema() + "';\n" +
                "IF CC>0 THEN\n" +
                " VSTR := CONCAT(' DROP INDEX ',IDX_NAME);\n" +
                " EXECUTE IMMEDIATE VSTR;\n" +
                "END IF;\n" +
                "END;";
    }

    @Override
    public String renameIndex(HikariDataSource hikariDataSource) {
        return "CREATE OR REPLACE PROCEDURE " + this.doTable(hikariDataSource, RENAME_INDEX) + "(\"T\" IN VARCHAR(32767),\"S_NAME\" IN VARCHAR(32767),\"D_NAME\" IN VARCHAR(32767))\n" +
                "AUTHID DEFINER\n" +
                "\n" +
                "AS \n" +
                "SC integer ;\n" +
                "DC integer ;\n" +
                "S varchar ;\n" +
                "BEGIN\n" +
                "T=replace(T,'`','');\n" +
                "SELECT count(*) INTO SC FROM ALL_INDEXES WHERE (TABLE_NAME = T OR TABLE_NAME = LOWER(T) OR TABLE_NAME = UPPER(T)) AND (INDEX_NAME=S_NAME OR INDEX_NAME=UPPER(S_NAME) OR INDEX_NAME=LOWER(S_NAME)) AND OWNER='" + hikariDataSource.getSchema() + "';\n" +
                "SELECT count(*) INTO DC FROM ALL_INDEXES WHERE (TABLE_NAME = T OR TABLE_NAME = LOWER(T) OR TABLE_NAME = UPPER(T)) AND (INDEX_NAME=D_NAME OR INDEX_NAME=UPPER(D_NAME) OR INDEX_NAME=LOWER(D_NAME)) AND OWNER='" + hikariDataSource.getSchema() + "';\n" +
                "IF SC>0 AND DC<=0 THEN\n" +
                " S := CONCAT(' ALTER INDEX ',S_NAME,' RENAME TO ', D_NAME);\n" +
                " EXECUTE IMMEDIATE S;\n" +
                "END IF;\n" +
                "END;";
    }

    @Override
    public String modifyColumn(HikariDataSource hikariDataSource) {
        String schema = this.getSchema(hikariDataSource);
        return "CREATE OR REPLACE PROCEDURE " + this.doTable(hikariDataSource, MODIFY_COLUMNS) + "(\"T\" IN VARCHAR(32767),\"C\" IN VARCHAR(32767),\"SQLSTR\" IN VARCHAR(32767))\n" +
                "AUTHID DEFINER\n" +
                "\n" +
                "AS \n" +
                "CC integer ;\n" +
                "BEGIN\n" +
                "T=replace(T,'`','');\n" +
                "C=replace(C,'`','');\n" +
                "SELECT count(*) INTO CC FROM all_tab_columns  WHERE (TABLE_NAME = T OR TABLE_NAME = LOWER(T) OR TABLE_NAME = UPPER(T)) AND (COLUMN_NAME=C OR COLUMN_NAME=UPPER(C) OR COLUMN_NAME=LOWER(C)) AND OWNER='" + hikariDataSource.getSchema() + "';\n" +
                "IF CC <= 0 THEN\n" +
                " SQLSTR := CONCAT( 'ALTER TABLE ','" + schema + ".\"',T,'\" ADD COLUMN \"',C,'\" ',SQLSTR);\n" +
                " SQLSTR=replace(SQLSTR,'JSON','LONGVARCHAR');\n" +
                " SQLSTR=replace(SQLSTR,'json','LONGVARCHAR');\n" +
                " SQLSTR=replace(SQLSTR,'longtext','LONGVARCHAR');\n" +
                " SQLSTR=replace(SQLSTR,'LONGTEXT','LONGVARCHAR');\n" +
                " SQLSTR=replace(SQLSTR,'MEDIUMTEXT','LONGVARCHAR');\n" +
                " SQLSTR=replace(SQLSTR,'mediumtext','LONGVARCHAR');\n" +
                " EXECUTE IMMEDIATE SQLSTR;\n" +
                "ELSEIF CC>0  THEN\n" +
                " SQLSTR := CONCAT('ALTER TABLE ','" + schema + ".\"',T,'\" MODIFY  \"',C,'\" ',SQLSTR);\n" +
                " SQLSTR=replace(SQLSTR,'JSON','LONGVARCHAR');\n" +
                " SQLSTR=replace(SQLSTR,'JSON','LONGVARCHAR');\n" +
                " SQLSTR=replace(SQLSTR,'longtext','LONGVARCHAR');\n" +
                " SQLSTR=replace(SQLSTR,'LONGTEXT','LONGVARCHAR');\n" +
                " SQLSTR=replace(SQLSTR,'MEDIUMTEXT','LONGVARCHAR');\n" +
                " SQLSTR=replace(SQLSTR,'mediumtext','LONGVARCHAR');\n" +
                " EXECUTE IMMEDIATE SQLSTR;\n" +
                "END IF;\n" +
                "END;";
    }

    @Override
    public String removeColumn(HikariDataSource hikariDataSource) {
        String schema = this.getSchema(hikariDataSource);
        return "CREATE OR REPLACE PROCEDURE " + this.doTable(hikariDataSource, DROP_COLUMNS) + "(\"T\" IN VARCHAR(32767),\"C\" IN VARCHAR(32767))\n" +
                "AUTHID DEFINER\n" +
                "\n" +
                "AS \n" +
                "CC integer ;\n" +
                "SQLSTR varchar;\n" +
                "BEGIN\n" +
                "T=replace(T,'`','');\n" +
                "C=replace(C,'`','');\n" +
                "SELECT count(*) INTO CC FROM all_tab_columns  WHERE (TABLE_NAME = T OR TABLE_NAME = LOWER(T) OR TABLE_NAME = UPPER(T)) AND (COLUMN_NAME=C OR COLUMN_NAME=UPPER(C) OR COLUMN_NAME=LOWER(C)) AND OWNER='" + hikariDataSource.getSchema() + "';\n" +
                "IF CC>0 THEN\n" +
                " SQLSTR := CONCAT('ALTER TABLE  ','" + schema + ".\"',T,'\" DROP COLUMN  ',C);\n" +
                " EXECUTE IMMEDIATE SQLSTR;\n" +
                "END IF;\n" +
                "END;";
    }

    @Override
    public String renameColumn(HikariDataSource hikariDataSource) {
        return "CREATE OR REPLACE PROCEDURE " + this.doTable(hikariDataSource, RENAME_COLUMNS) + "(\"T\" IN VARCHAR(32767),\"SNAME\" IN VARCHAR(32767),\"DNAME\" IN VARCHAR(32767),\"SQLSTR\" IN VARCHAR(32767))\n" +
                "AUTHID DEFINER\n" +
                "\n" +
                "AS \n" +
                "SC integer ;\n" +
                "DC integer ;\n" +
                "S varchar ;\n" +
                "BEGIN\n" +
                "T=replace(T,'`','');\n" +
                "SNAME=replace(SNAME,'`','');\n" +
                "DNAME=replace(DNAME,'`','');\n" +
                "SELECT count(*) INTO SC FROM all_tab_columns  WHERE (TABLE_NAME = T OR TABLE_NAME = LOWER(T) OR TABLE_NAME = UPPER(T)) AND (COLUMN_NAME=SNAME OR COLUMN_NAME=UPPER(SNAME) OR COLUMN_NAME=LOWER(SNAME)) AND OWNER='" + hikariDataSource.getSchema() + "';\n" +
                "SELECT count(*) INTO DC FROM all_tab_columns  WHERE (TABLE_NAME = T OR TABLE_NAME = LOWER(T) OR TABLE_NAME = UPPER(T)) AND (COLUMN_NAME=DNAME OR COLUMN_NAME=UPPER(DNAME) OR COLUMN_NAME=LOWER(DNAME)) AND OWNER='" + hikariDataSource.getSchema() + "';\n" +
                "IF SC>=0 AND DC <= 0 THEN\n" +
                "  S := CONCAT('ALTER TABLE ',T,' RENAME COLUMN \"', SNAME,'\"  TO ',DNAME,';');\n" +
                "  EXECUTE IMMEDIATE S;\n" +
                "END IF;\n" +
                "END;";
    }

    @Override
    public String createViews(HikariDataSource hikariDataSource) {
        return "CREATE OR REPLACE PROCEDURE " + this.doTable(hikariDataSource, CREATE_VIEWS) + "(\"T\" IN VARCHAR(200),\"SQLSTR\" IN VARCHAR)\n" +
                "AUTHID DEFINER\n" +
                "\n" +
                "AS \n" +
                "DS varchar ;\n" +
                "DC varchar ;\n" +
                "BEGIN\n" +
                "DS :=CONCAT('DROP VIEW IF EXISTS ', T);\n" +
                "EXECUTE IMMEDIATE DS;\n" +
                "SQLSTR=replace(SQLSTR,'`','\"');\n" +
                "EXECUTE IMMEDIATE SQLSTR;\n" +
                "END;";
    }

    @Override
    public String dropViews(HikariDataSource hikariDataSource) {
        return "CREATE OR REPLACE PROCEDURE " + this.doTable(hikariDataSource, DROP_VIEWS) + "(\"T\" IN VARCHAR(200))\n" +
                "AUTHID DEFINER\n" +
                "\n" +
                "AS \n" +
                "DS varchar ;\n" +
                "BEGIN\n" +
                "DS :=CONCAT('DROP VIEW IF EXISTS ', T);\n" +
                "EXECUTE IMMEDIATE DS;\n" +
                "END;";
    }

    public String findInSet(HikariDataSource hikariDataSource) {
        return "CREATE OR REPLACE FUNCTION " + this.doTable(hikariDataSource, FIND_IN_SET) + "( piv_str1 varchar2, piv_str2 varchar2, p_sep varchar2 := ',' ) RETURN NUMBER IS l_idx number := 0;\n" +
                "str varchar2 ( 500 );\n" +
                "piv_str varchar2 ( 500 ) := piv_str2;\n" +
                "res number := 0;\n" +
                "loopIndex number := 0;\n" +
                "BEGIN\n" +
                "\tIF\n" +
                "\t\tinstr( piv_str, p_sep, 1 ) = 0 THEN\n" +
                "\t\tIF\n" +
                "\t\t\tpiv_str = piv_str1 THEN\n" +
                "\t\t\t\tres := 1;\n" +
                "\t\tEND IF;\n" +
                "\t\tELSE\n" +
                "\t\tLOOP\n" +
                "\t\t\t\tl_idx := instr( piv_str, p_sep );\n" +
                "\t\t\tloopIndex := loopIndex + 1;\n" +
                "\t\t\tIF\n" +
                "\t\t\t\tl_idx > 0 THEN\n" +
                "\t\t\t\t\tstr := substr( piv_str, 1, l_idx - 1 );\n" +
                "\t\t\t\tIF\n" +
                "\t\t\t\t\tstr = piv_str1 THEN\n" +
                "\t\t\t\t\t\tres := loopIndex;\n" +
                "\t\t\t\t\tEXIT;\n" +
                "\t\t\t\tEND IF;\n" +
                "\t\t\t\tpiv_str := substr( piv_str, l_idx + length( p_sep ) );\n" +
                "\t\t\t\tELSE\n" +
                "\t\t\t\tIF\n" +
                "\t\t\t\t\tpiv_str = piv_str1 THEN\n" +
                "\t\t\t\t\t\tres := loopIndex;\n" +
                "\t\t\t\tEND IF;\n" +
                "\t\t\t\tEXIT;\n" +
                "\t\t\tEND IF;\n" +
                "\t\tEND LOOP;\n" +
                "\tEND IF;\n" +
                "\tRETURN res;\n" +
                "END FIND_IN_SET;";
    }

    @Override
    public void drop(JdbcTemplate jdbcTemplate) {
        Functioned.super.drop(jdbcTemplate);
        jdbcTemplate.execute(String.format(DROP_PROCEDURE, FIND_IN_SET));
    }

    @Override
    public void doExecute(HikariDataSource hikariDataSource) {
        Functioned.super.doExecute(hikariDataSource);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(hikariDataSource);
        jdbcTemplate.execute(this.findInSet(hikariDataSource));
    }
}
