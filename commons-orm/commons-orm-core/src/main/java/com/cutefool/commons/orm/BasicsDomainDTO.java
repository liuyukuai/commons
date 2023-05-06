/*
 *
 */
package com.cutefool.commons.orm;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * BasicsDomain
 *
 * @author 271007729@qq.com
 * @date 2020/7/12 7:28 PM
 */
@Data
@SuppressWarnings("unused")
@EqualsAndHashCode(callSuper = true)
public class BasicsDomainDTO extends BasicsDomain {

    /**
     * 创建人名称
     */
    private String creatorName;

    /**
     * 修改人名称
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private String reviserName;


}
