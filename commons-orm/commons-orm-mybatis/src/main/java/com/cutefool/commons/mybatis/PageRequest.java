package com.cutefool.commons.mybatis;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.cutefool.commons.core.page.Sort;

import java.util.List;

/**
 * 分页对象封装
 * commons-orm
 *
 * @author 271007729@qq.com
 * @date 2019-07-09 11:27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Deprecated
public class PageRequest {
    private int page;
    private int size;
    private List<Sort> sorts;

}
