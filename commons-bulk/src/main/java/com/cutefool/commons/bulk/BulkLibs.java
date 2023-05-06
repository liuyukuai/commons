package com.cutefool.commons.bulk;

import lombok.Data;
import com.cutefool.commons.core.Naming;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

@Data
@SuppressWarnings("ALL")
public class BulkLibs {

    /**
     * 源属性名称
     */
    private String sn;

    /**
     * 源属性
     */
    private Field sf;

    /**
     * 目标
     */
    private Set<Dest> destList;

    /**
     * 数据加载器
     */
    private Naming<?> naming;

    /**
     * 需要加载的数据ID集合
     */
    private Set ids;


    public static BulkLibs init() {
        BulkLibs bulkLibs = new BulkLibs();
        bulkLibs.setIds(new HashSet<>());
        bulkLibs.setDestList(new HashSet<>());
        bulkLibs.setMultiple(false);
        return bulkLibs;
    }

    public boolean multiple;


    @Data
    public static class Dest {

        private String name;

        /**
         * 目标属性名称
         */
        private String dn;

        /**
         * 目标属性是否存在
         */
        private boolean exists;

    }
}

