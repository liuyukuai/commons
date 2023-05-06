package com.cutefool.commons.core.page;

import com.cutefool.commons.core.Exclude;
import com.cutefool.commons.core.Transfer;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import com.cutefool.commons.core.function.ThirdFunction;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;

/**
 * @author 271007729@qq.com
 * @date 2019-07-03 09:51
 */
@Slf4j
@Builder
@ToString
@EqualsAndHashCode
@SuppressWarnings({"unused", "WeakerAccess"})
public class Paging implements Transfer<String, String> {


    /**
     * 分页页数，默认值1，最小值为1
     */
    @Setter
    @Getter
    @Exclude
    private String page;
    /**
     * 每页条数，默认值10
     */
    @Setter
    @Getter
    @Exclude
    private String size;

    /**
     * 排序规则 eg: xxx_desc, xxx_asc
     */
    @Setter
    @Getter
    @Exclude
    private String sort;

    public Paging() {
    }

    private Paging(String page, String size, String sort) {
        this.page = page;
        this.size = size;
        this.sort = sort;
    }

    public Paging setSort(String name, String direction) {
        if (StringUtils.isBlank(this.sort)) {
            this.sort = name + "_" + direction;
            return this;
        }
        this.sort = this.sort + "," + name + "_" + direction;
        return this;
    }

    public static Paging of() {
        return new Paging();
    }


    public static Paging of(String page, String size) {
        return of(page, size, "");
    }


    public static Paging of(String page, String size, String sort) {
        return new Paging(page, size, sort);
    }

    /**
     * 解析page对象,若参数错误，默认desc
     *
     * @param thirdFunction 解析函数
     * @param <R>           解析后的对象
     * @param <F>           function
     * @return 返回解析结果
     */
    public <R, F extends ThirdFunction<R, Integer, Integer, List<Sort>>> R get(F thirdFunction) {
        if (StringUtils.isBlank(sort)) {
            return thirdFunction.apply(PageUtils.page(page), PageUtils.size(size), Collections.emptyList());
        }
        List<Sort> sorts = Sorts.newInstance().sorts(sort, this);
        return thirdFunction.apply(PageUtils.page(page), PageUtils.size(size), sorts);
    }

    /**
     * 将排序的类型转换为数据库对应的值
     * 默认排序名称和数据库名称相同
     *
     * @param name 排序的名称
     * @return 返回数据库的名称
     */
    @Override
    public String apply(String name) {
        return name;
    }
}
