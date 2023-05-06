/*
 *  
 */
package com.cutefool.commons.packs.pack;

import lombok.extern.slf4j.Slf4j;
import com.cutefool.commons.core.page.Response;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author 271007729@qq.com
 * @date 8/8/21 10:39 PM
 */
@Slf4j
public class TarAbstractPackExecutor extends AbstractPackExecutor {
    /**
     * tar
     */
    private static final BiFunction<Path, Path, TarArchiveEntry> TAR_FUNCTION = (dir, file) -> new TarArchiveEntry(file.toFile(), dir.relativize(file).toString());


    public TarAbstractPackExecutor(String source, String target) {
        super(source, target);
    }

    @Override
    public Response<Boolean> execute() {
        return this.execute(null);
    }

    @Override
    public Response<Boolean> execute(Function<File, Integer> modeFunction) {
        try (OutputStream fos = Files.newOutputStream(Paths.get(super.getTarget()));
             OutputStream bos = new BufferedOutputStream(fos);
             TarArchiveOutputStream aos = new TarArchiveOutputStream(bos)) {
            aos.setLongFileMode(TarArchiveOutputStream.LONGFILE_POSIX);
            return Response.ok(super.aos(aos).doExecute((dir, file) -> {
                TarArchiveEntry apply = TAR_FUNCTION.apply(dir, file);
                if (Objects.nonNull(modeFunction)) {
                    Integer mode = modeFunction.apply(file.toFile());
                    apply.setMode(mode);
                }
                return apply;
            }));
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
            return Response.failure(e.getMessage());
        }
    }
}
