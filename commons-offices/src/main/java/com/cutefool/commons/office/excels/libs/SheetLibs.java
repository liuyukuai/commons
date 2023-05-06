package com.cutefool.commons.office.excels.libs;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class SheetLibs {
    /**
     * sheet名称
     */
    private String name;
    /**
     * title
     */
    private String title;

    /**
     * 字段映射
     */
    private Map<String, String> mapping;

    /**
     * 数据
     */
    private List<?> data;


    public static SheetLibs create(String name, Map<String, String> mapping, List<?> data) {
        return create(name, name, mapping, data);
    }

    public static SheetLibs create(String name, String title, Map<String, String> mapping, List<?> data) {
        SheetLibs sheetLibs = new SheetLibs();
        sheetLibs.setData(data);
        sheetLibs.setName(name);
        sheetLibs.setTitle(title);
        sheetLibs.setMapping(mapping);
        return sheetLibs;
    }

}
