package com.cutefool.commons.flyway.kb;
/*
 *
 */

import org.flywaydb.core.internal.database.base.Table;
import org.flywaydb.core.internal.jdbc.JdbcTemplate;

import java.sql.SQLException;

/**
 * @author 271007729@qq.com
 * @date 2022/8/1 3:47 PM
 */
public class KBTable extends Table<KBDatabase, KBSchema> {

    public KBTable(JdbcTemplate jdbcTemplate, KBDatabase database, KBSchema schema, String name) {
        super(jdbcTemplate, database, schema, name);
    }

    @Override
    protected void doDrop() throws SQLException {
        jdbcTemplate.execute("DROP TABLE " + database.quote(schema.getName(), name) + " CASCADE CONSTRAINTS PURGE");
    }

    @Override
    protected boolean doExists() throws SQLException {
        int c = jdbcTemplate.queryForInt("select count(*) from ALL_TABLES where TABLE_NAME like ?", name);
        return c == 1;
    }

    @Override
    protected void doLock() throws SQLException {
        jdbcTemplate.execute("LOCK TABLE " + this + " IN EXCLUSIVE MODE");
    }
}
