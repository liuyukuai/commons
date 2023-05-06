/*
 *
 */
package com.cutefool.commons.orm;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import com.cutefool.commons.core.Excel;
import com.cutefool.commons.core.ExcelIgnore;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * BasicsDomain
 *
 * @author 271007729@qq.com
 * @date 2020/7/12 7:28 PM
 */
@Data
@MappedSuperclass
@SuppressWarnings("unused")
public class BasicsDomain extends RootDomain implements Serializable {

    /**
     * 创建人
     */
    @ExcelIgnore
    @Column(name = "CREATOR")
    private Long creator;

    /**
     * 修改人
     */
    @ExcelIgnore
    @JsonSerialize(using = ToStringSerializer.class)
    @Column(name = "REVISER")
    private Long reviser;

    /**
     * 创建时间
     */
    @Column(name = "CREATE_TIME")
    @Excel("创建时间")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @Excel("更新时间")
    @Column(name = "REVISE_TIME")
    private LocalDateTime reviseTime;
}
