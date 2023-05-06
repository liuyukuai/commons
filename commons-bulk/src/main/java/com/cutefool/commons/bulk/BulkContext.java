package com.cutefool.commons.bulk;

import lombok.Data;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * bulked 上下文
 */
@Data
public class BulkContext {

    /**
     * 包含的属性
     *
     */
    private List<String> includes;

    /**
     * 排除的属性
     *
     */
    private List<String> excludes;

    /**
     * 包含的填充属性
     */
    private List<String> includeBulks;

    /**
     * 排除的填充属性
     */
    private List<String> excludeBulks;

    /**
     * 是否导出
     */
    private boolean export;


    public static BulkContext init(Bulking bulking) {
        Objects.requireNonNull(bulking);
        BulkContext context = new BulkContext();
        context.setIncludeBulks(Stream.of(bulking.includeBulks()).collect(Collectors.toList()));
        context.setExcludeBulks(Stream.of(bulking.excludeBulks()).collect(Collectors.toList()));
        context.setExcludes(Stream.of(bulking.excludes()).collect(Collectors.toList()));
        context.setIncludes(Stream.of(bulking.includes()).collect(Collectors.toList()));
        context.setExport(false);
        return context;
    }

    public static BulkContext init() {
        return new BulkContext();
    }

}
