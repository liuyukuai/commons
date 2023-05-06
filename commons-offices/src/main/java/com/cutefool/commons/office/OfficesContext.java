/*
 *
 */
package com.cutefool.commons.office;

import com.cutefool.commons.office.word.WordTemplate;
import lombok.Getter;
import com.cutefool.commons.core.util.Lists;
import com.cutefool.commons.office.xls.ExcelTemplate;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 271007729@qq.com
 * @date 7/30/21 10:37 AM
 */
@Getter
@SuppressWarnings({"WeakerAccess", "unused"})
public class OfficesContext implements OfficesType {

    /**
     * 目标文件
     */
    private File dest;

    /**
     * 源文件
     */
    private File source;

    /**
     * 处理单元格策略
     */
    private Map<String, List<OfficesStrategy>> strategies = new HashMap<>();

    /**
     * 处理单元格策略
     */
    private Map<String, List<OfficesCopyStrategy>> copyStrategies = new HashMap<>();

    protected OfficesContext(File source, File dest) {
        this.source = source;
        this.dest = dest;
    }

    public static OfficesContext of(File source, File dest) {
        return new OfficesContext(source, dest);
    }


    public OfficesContext bind(String key, OfficesStrategy strategy) {
        List<OfficesStrategy> officeStrategies = this.strategies.getOrDefault(key, Lists.newArrayList());
        officeStrategies.add(strategy);
        this.strategies.put(key, officeStrategies);
        return this;
    }

    public OfficesContext bind(String key, OfficesCopyStrategy strategy) {
        List<OfficesCopyStrategy> officeStrategies = this.copyStrategies.getOrDefault(key, Lists.newArrayList());
        officeStrategies.add(strategy);
        this.copyStrategies.put(key, officeStrategies);
        return this;
    }

    public void setStrategies(Map<String, List<OfficesStrategy>> strategies) {
        this.strategies = strategies;
    }


    public void setCopyStrategies(Map<String, List<OfficesCopyStrategy>> strategies) {
        this.copyStrategies = strategies;
    }


    @Override
    public WordTemplate word() {
        return new WordTemplate(this);
    }

    @Override
    public ExcelTemplate excel() {
        return new ExcelTemplate(this);
    }
}
