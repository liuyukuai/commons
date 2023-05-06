/*
 *
 */
package com.cutefool.commons.office.excel.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * excel 级联字典
 *
 * @author 271007729@qq.com
 * @date 2021/12/21 10:23
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExcelCascade implements Serializable {

    /**
     * 数据ID
     */
    private Long id;

    /**
     * 编码
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 级联-子数据
     */
    private List<ExcelCascade> children;


}