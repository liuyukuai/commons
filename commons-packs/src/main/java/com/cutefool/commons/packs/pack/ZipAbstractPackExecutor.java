/*
 *
 */
package com.cutefool.commons.packs.pack;

import lombok.extern.slf4j.Slf4j;
import com.cutefool.commons.core.page.Response;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;

import java.io.*;
import java.nio.file.*;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author 271007729@qq.com
 * @date 8/8/21 10:39 PM
 */
@Slf4j
public class ZipAbstractPackExecutor extends AbstractPackExecutor {
    /**
     * packs
     */
    private static final BiFunction<Path, Path, ZipArchiveEntry> ZIP_FUNCTION = (dir, file) -> {
        File f = file.toFile();
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(file.toFile(), dir.relativize(file).toString());
        System.out.println(f.getAbsolutePath());
        if (Files.isExecutable(file)) {
            zipArchiveEntry.setUnixMode(755);
        }
        return zipArchiveEntry;
    };


    public ZipAbstractPackExecutor(String source, String target) {
        super(source, target);
    }

    @Override
    public Response<Boolean> execute() {
        return execute(null);
    }

    @Override
    public Response<Boolean> execute(Function<File, Integer> modeFunction) {
        try (OutputStream fos = Files.newOutputStream(Paths.get(super.getTarget()));
             OutputStream bos = new BufferedOutputStream(fos);
             ArchiveOutputStream aos = new ZipArchiveOutputStream(bos)) {
            return Response.ok(super.aos(aos).doExecute((dir, file) -> {
                ZipArchiveEntry apply = ZIP_FUNCTION.apply(dir, file);
                if (Objects.nonNull(modeFunction)) {
                    Integer mode = modeFunction.apply(file.toFile());
                    apply.setUnixMode(mode);
                }
                return apply;
            }));
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
            return Response.failure(e.getMessage());
        }
    }
}
