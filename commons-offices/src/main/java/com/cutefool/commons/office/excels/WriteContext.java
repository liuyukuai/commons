package com.cutefool.commons.office.excels;

import lombok.Data;
import com.cutefool.commons.core.util.FilesUtils;
import com.cutefool.commons.office.excels.libs.SheetLibs;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

@Data
public class WriteContext {

    /**
     * 文件
     */
    private File file;

    /**
     * 需要写的sheet配置
     */
    private List<SheetLibs> sheets;

    private WriteContext() {
    }

    private WriteContext(File file, List<SheetLibs> sheets) {
        this.file = file;
        this.sheets = sheets;
    }

    /**
     * 通过文件名称
     *
     * @param fileName 文件名称
     * @param sheets   sheets
     * @return context
     */
    public static WriteContext create(String fileName, List<SheetLibs> sheets) {
        String dir = FilesUtils.randomDir();
        return create(new File(dir, fileName), sheets);
    }

    /**
     * 通过文件path
     *
     * @param path   path
     * @param sheets sheets
     * @return context
     */
    public static WriteContext create(Path path, List<SheetLibs> sheets) {
        return create(path.toFile(), sheets);
    }

    /**
     * 通过文件
     *
     * @param file   file
     * @param sheets sheets
     * @return context
     */
    public static WriteContext create(File file, List<SheetLibs> sheets) {
        return new WriteContext(file, sheets);
    }

}
