package com.cutefool.commons.flyway.dm;
/*
 *  
 */

import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.internal.database.base.Schema;
import org.flywaydb.core.internal.database.base.Table;
import org.flywaydb.core.internal.jdbc.JdbcTemplate;

import java.sql.SQLException;
import java.util.List;

/**
 * @author 271007729@qq.com
 * @date 2022/8/1 3:47 PM
 */
@Slf4j
public class DMSchema extends Schema<DMDatabase, DMTable> {

    /**
     * Creates a new Oracle schema.
     *
     * @param jdbcTemplate The Jdbc Template for communicating with the DB.
     * @param database     The database-specific support.
     * @param name         The name of the schema.
     */
    DMSchema(JdbcTemplate jdbcTemplate, DMDatabase database, String name) {
        super(jdbcTemplate, database, name);
    }

    @Override
    protected boolean doExists() throws SQLException {
//        return database.queryReturnsRows("SELECT * FROM ALL_USERS WHERE USERNAME = ?", name);
        return true;
    }

    @Override
    protected boolean doEmpty() {
        return false;
    }

    @Override
    protected void doCreate() {
        log.warn("do not support create user.");
    }

    @Override
    protected void doDrop() {
        log.warn("do not support drop user.");
    }

    @Override
    protected void doClean() throws SQLException {
        log.warn("drop table flyway_schema_history only.");
        for (DMTable table : allTables()) {
            table.doDrop();
        }
    }

    /**
     * Clean Oracle Locator metadata for the schema. Works only for the user's default schema, prints a warning message
     * to log otherwise.
     *
     * @throws SQLException when performing cleaning failed.
     */

    @Override
    protected DMTable[] doAllTables() throws SQLException {
        List<String> tableNames = jdbcTemplate.queryForStringList("select TABLE_NAME  from all_tables WHERE owner = ?", name);
        DMTable[] tables = new DMTable[tableNames.size()];
        for (int i = 0; i < tableNames.size(); i++) {
            tables[i] = new DMTable(jdbcTemplate, database, this, tableNames.get(i));
        }
        return tables;
    }

    @Override
    public Table<DMDatabase, DMSchema> getTable(String tableName) {
        return new DMTable(jdbcTemplate, database, this, tableName);
    }

}
