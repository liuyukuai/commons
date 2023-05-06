/*
 *
 */
package com.cutefool.commons.flyway.dm;

import org.flywaydb.core.api.ResourceProvider;
import org.flywaydb.core.api.configuration.Configuration;
import org.flywaydb.core.internal.callback.CallbackExecutor;
import org.flywaydb.core.internal.database.base.BaseDatabaseType;
import org.flywaydb.core.internal.database.base.Database;
import org.flywaydb.core.internal.database.oracle.OracleSqlScriptExecutor;
import org.flywaydb.core.internal.jdbc.JdbcConnectionFactory;
import org.flywaydb.core.internal.jdbc.JdbcTemplate;
import org.flywaydb.core.internal.jdbc.StatementInterceptor;
import org.flywaydb.core.internal.parser.Parser;
import org.flywaydb.core.internal.parser.ParsingContext;
import org.flywaydb.core.internal.sqlscript.SqlScriptExecutorFactory;

import java.sql.Connection;
import java.sql.Types;

/**
 * @author 271007729@qq.com
 * @date 2022/8/1 3:47 PM
 */
public class DMDatabaseType extends BaseDatabaseType {
    @Override
    public String getName() {
        return "DM DBMS";
    }

    @Override
    public int getNullType() {
        return Types.VARCHAR;
    }

    @Override
    public boolean handlesJDBCUrl(String url) {
        return url.startsWith("jdbc:dm:");
    }

    @Override
    public String getDriverClass(String url, ClassLoader classLoader) {
        return "dm.jdbc.driver.DMDriver";
    }

    @Override
    public boolean handlesDatabaseProductNameAndVersion(String databaseProductName, String databaseProductVersion, Connection connection) {
        return databaseProductName.startsWith("DM DBMS");
    }

    @Override
    public Database<DMConnection> createDatabase(Configuration configuration, JdbcConnectionFactory jdbcConnectionFactory, StatementInterceptor statementInterceptor) {
        return new DMDatabase(configuration, jdbcConnectionFactory, statementInterceptor);
    }

    @Override
    public Parser createParser(Configuration configuration, ResourceProvider resourceProvider, ParsingContext parsingContext) {
        return new DMParser(configuration, parsingContext, 3);
    }

    @Override
    public SqlScriptExecutorFactory createSqlScriptExecutorFactory(JdbcConnectionFactory jdbcConnectionFactory, CallbackExecutor callbackExecutor, StatementInterceptor statementInterceptor) {
        return (connection, undo, batch, outputQueryResults) -> {
            return new OracleSqlScriptExecutor(new JdbcTemplate(connection, this)
                    , callbackExecutor, undo, batch, outputQueryResults, statementInterceptor
            );
        };

    }
}
