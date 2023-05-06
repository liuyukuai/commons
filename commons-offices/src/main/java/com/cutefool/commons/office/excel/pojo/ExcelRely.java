/*
 *
 */
package com.cutefool.commons.office.excel.pojo;

import lombok.Data;

import java.util.Map;
import java.util.Set;

/**
 * Excel 解析前 依赖的参数【需要开发者维护，确保非空以及是否能修改】
 * 这里是抽取的公共类，实现类可按需添加或使用自己的依赖参数
 *
 * @author 271007729@qq.com
 * @date 2022/3/21 10:22
 */
@Data
public class ExcelRely {

    /**
     * 唯一名称
     */
    private Set<String> nameSet;

    /**
     * 用户-名称和ID映射  Map<用户名称,用户ID>
     */
    private Map<String, Long> mapUserName;

    /**
     * 部门-名称和编码映射  Map<部门名称,部门编码>
     */
    private Map<String, String> mapDeptName;

    /**
     * 当前登录用户
     */
    /// private User loginUser;

}
