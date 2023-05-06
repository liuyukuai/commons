/*
 *
 */
package com.cutefool.commons.orm;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import com.cutefool.commons.bulk.Bulked;
import com.cutefool.commons.core.ExcelIgnore;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * RootDomain
 *
 * @author 271007729@qq.com
 * @date 2020/7/12 7:28 PM
 */
@Data
@MappedSuperclass
@SuppressWarnings("unused")
public class RootDomain extends Extensible implements Serializable {

    /**
     * orgCode属性名称
     */
    public static final String FIELD_ORG_CODE = "orgCode";

    /**
     * tenantId属性名称
     */
    public static final String FIELD_TENANT_ID = "tenantId";

    /**
     * deleteStatus属性名称
     */
    public static final String FIELD_DELETE_STATUS = "deleteStatus";

    /**
     * id属性名称
     */
    public static final String FIELD_ID = "id";

    /**
     * sortId属性名称
     */
    public static final String FIELD_SORT_ID = "sortId";


    /**
     * id
     */
    @DtoExclude
    @JsonSerialize(using = ToStringSerializer.class)
    @Column(name = "ID")
    @ExcelIgnore
    private Long id;

    /**
     * 企业编码
     */

    @ExcelIgnore
    @Column(name = "ORG_CODE")
    @Bulked(value = "com.cutefool.commons.libs.objects.naming.NamingOrg", fields = "orgName", remarks = "所属企业")
    private String orgCode;

    /**
     * 租户
     */
    @ExcelIgnore
    @DtoExclude
    @JsonSerialize(using = ToStringSerializer.class)
    @Column(name = "TENANT_ID")
    private Long tenantId;


    /**
     * 是否删除 0：未删除1：已删除
     */
    @DtoExclude
    @Column(name = "DELETE_STATUS")
    @ExcelIgnore
    private Byte deleteStatus;

    /**
     * 序号
     */
    @Column(name = "SORT_ID")
    @ExcelIgnore
    private Integer sortId;
}
