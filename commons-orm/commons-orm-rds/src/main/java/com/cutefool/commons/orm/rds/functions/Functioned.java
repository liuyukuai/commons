package com.cutefool.commons.orm.rds.functions;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Arrays;

/**
 * rds function builder
 *
 * @author liuyk@tsingyun.net
 */
public interface Functioned {

    String CREATE_TABLE = "CREATE_TABLES";

    String MODIFY_TABLE_COMMENT = "MODIFY_TABLE_COMMENTS";

    String MODIFY_COLUMNS_COMMENTS = "MODIFY_COLUMNS_COMMENTS";

    String MODIFY_COLUMNS_DEFAULT = "MODIFY_COLUMNS_DEFAULT";

    String MODIFY_COLUMNS = "MODIFY_COLUMNS";

    String DROP_COLUMNS = "DROP_COLUMNS";

    String RENAME_COLUMNS = "RENAME_COLUMNS";

    String CREATE_INDEX = "CREATE_INDEX";

    String DROP_INDEX = "DROP_INDEX";

    String RENAME_INDEX = "RENAME_INDEX";

    String CREATE_VIEWS = "CREATE_VIEWS";

    String DROP_VIEWS = "DROP_VIEWS";

    String DROP_PROCEDURE = "DROP PROCEDURE IF EXISTS %s;";

    String FIND_IN_SET = "FIND_IN_SET";

    /**
     * create table if not exits
     *
     * @return sql
     */
    String createTable(HikariDataSource dataSource);

    /**
     * modifyTableComment
     *
     * @return sql
     */
    String modifyTableComment(HikariDataSource dataSource);

    /**
     * modifyComment
     *
     * @return sql
     */
    String modifyColumnComment(HikariDataSource hikariDataSource);

    /**
     * modifyColumnsDefault
     *
     * @return sql
     */
    String modifyColumnsDefault(HikariDataSource hikariDataSource);

    /**
     * modifyIndex
     *
     * @return sql
     */
    String createIndex(HikariDataSource hikariDataSource);


    /**
     * modifyIndex
     *
     * @return sql
     */
    String dropIndex(HikariDataSource hikariDataSource);

    /**
     * modifyIndex
     *
     * @return sql
     */
    String renameIndex(HikariDataSource hikariDataSource);

    /**
     * modifyColumn
     *
     * @return sql
     */
    String modifyColumn(HikariDataSource hikariDataSource);

    /**
     * removeColumn
     *
     * @return sql
     */
    String removeColumn(HikariDataSource hikariDataSource);

    /**
     * renameColumn
     *
     * @return sql
     */
    String renameColumn(HikariDataSource hikariDataSource);


    /**
     * modifyIndex
     *
     * @return sql
     */
    String createViews(HikariDataSource hikariDataSource);


    /**
     * modifyIndex
     *
     * @return sql
     */
    String dropViews(HikariDataSource hikariDataSource);

    default void drop(JdbcTemplate jdbcTemplate) {
        for (String s : Arrays.asList(CREATE_TABLE,
                MODIFY_TABLE_COMMENT,
                MODIFY_COLUMNS_COMMENTS,
                MODIFY_COLUMNS_DEFAULT,
                MODIFY_COLUMNS,
                DROP_COLUMNS,
                RENAME_COLUMNS,
                CREATE_INDEX,
                RENAME_INDEX,
                DROP_INDEX,
                CREATE_VIEWS,
                DROP_VIEWS
        )) {
            jdbcTemplate.execute(String.format(DROP_PROCEDURE, s));
        }
    }


    /**
     * 执行
     */
    default void doExecute(HikariDataSource hikariDataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(hikariDataSource);
        this.drop(jdbcTemplate);
        jdbcTemplate.execute(this.createTable(hikariDataSource));
        jdbcTemplate.execute(this.modifyTableComment(hikariDataSource));
        jdbcTemplate.execute(this.modifyColumnComment(hikariDataSource));
        jdbcTemplate.execute(this.modifyColumnsDefault(hikariDataSource));
        jdbcTemplate.execute(this.modifyColumn(hikariDataSource));
        jdbcTemplate.execute(this.removeColumn(hikariDataSource));
        jdbcTemplate.execute(this.renameColumn(hikariDataSource));
        jdbcTemplate.execute(this.createIndex(hikariDataSource));
        jdbcTemplate.execute(this.dropIndex(hikariDataSource));
        jdbcTemplate.execute(this.renameIndex(hikariDataSource));
        jdbcTemplate.execute(this.createViews(hikariDataSource));
        jdbcTemplate.execute(this.dropViews(hikariDataSource));
    }

    default String doTable(HikariDataSource configuration, String tableName) {
        String schema = configuration.getSchema();
        if (StringUtils.isBlank(schema)) {
            return tableName;
        }
        return schema + "." + tableName;
    }

    default String getSchema(HikariDataSource configuration) {
        return configuration.getSchema();
    }
}
