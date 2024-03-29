package com.cutefool.commons.flyway.kb;
/*
 *
 */

import org.flywaydb.core.internal.database.base.Connection;
import org.flywaydb.core.internal.database.base.Schema;

import java.sql.SQLException;

/**
 * @author 271007729@qq.com
 * @date 2022/8/1 3:47 PM
 */
public class KBConnection extends Connection<KBDatabase> {
    KBConnection(KBDatabase database, java.sql.Connection connection) {
        super(database, connection);
    }

    @Override
    protected String getCurrentSchemaNameOrSearchPath() throws SQLException {
        return jdbcTemplate.queryForString("SELECT SYS_CONTEXT('USERENV', 'CURRENT_SCHEMA') FROM DUAL");
    }

    @Override
    public void doChangeCurrentSchemaOrSearchPathTo(String schema) {
        // do nothing
    }

    @Override
    public Schema<KBDatabase, KBTable> getSchema(String name) {
        return new KBSchema(jdbcTemplate, database, name);
    }
}
