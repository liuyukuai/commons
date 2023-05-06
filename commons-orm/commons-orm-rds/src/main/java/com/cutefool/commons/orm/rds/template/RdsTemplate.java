package com.cutefool.commons.orm.rds.template;

import com.cutefool.commons.orm.rds.enums.RdsJdbcType;
import com.cutefool.commons.orm.rds.libs.RdsFieldsLibs;
import com.cutefool.commons.core.util.Strings;
import com.cutefool.commons.orm.rds.functions.Functioned;
import org.springframework.jdbc.core.JdbcTemplate;

public class RdsTemplate {

    private final JdbcTemplate jdbcTemplate;

    public RdsTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void create(RdsFieldsLibs rdsFieldsLibs) {
        // 创建字段 CALL CREATE_COLUMNS('SYS_TABLE','NAME','');
        String createSql = this.doCreate(rdsFieldsLibs);
        jdbcTemplate.execute(createSql);
        // 修改字段注释 CALL MODIFY_COLUMNS_COMMENTS('SYS_TABLE','NAME','')
        jdbcTemplate.execute(this.doCreateMoments(rdsFieldsLibs));
    }

    private String doCreate(RdsFieldsLibs rdsFieldsLibs) {
        return "CALL " + rdsFieldsLibs.getDbName() +
                "." +
                Functioned.MODIFY_COLUMNS +
                "('" +
                rdsFieldsLibs.getTableName() +
                "','" +
                rdsFieldsLibs.getName() +
                "','" +
                RdsJdbcType.sqlByType(rdsFieldsLibs.getType(), rdsFieldsLibs.getLen(), rdsFieldsLibs.getScale()) +
                "')";
    }

    private String doCreateMoments(RdsFieldsLibs rdsFieldsLibs) {
        return "CALL " + rdsFieldsLibs.getDbName() +
                "." +
                Functioned.MODIFY_COLUMNS_COMMENTS +
                "('" +
                rdsFieldsLibs.getTableName() +
                "','" +
                rdsFieldsLibs.getName() +
                "','" +
                Strings.empty(rdsFieldsLibs.getRemarks()) +
                "')";
    }

}
