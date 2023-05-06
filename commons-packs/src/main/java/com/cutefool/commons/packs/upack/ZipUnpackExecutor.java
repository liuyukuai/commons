/*
 *
 */
package com.cutefool.commons.packs.upack;

import lombok.extern.slf4j.Slf4j;
import com.cutefool.commons.core.page.Response;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Function;

/**
 * @author 271007729@qq.com
 * @date 8/8/21 10:39 PM
 */
@Slf4j
public class ZipUnpackExecutor extends AbstractUnpackExecutor {


    public ZipUnpackExecutor(String source, String target) {
        super(source, target);
    }

    @Override
    public Response<Boolean> execute() {
        try (InputStream fis = Files.newInputStream(Paths.get(getSource()));
             InputStream bis = new BufferedInputStream(fis);
             ArchiveInputStream ais = new ZipArchiveInputStream(bis)) {
            return Response.ok(super.ais(ais).doExecute());
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
            return Response.failure(e.getMessage());
        }
    }

    @Override
    public Response<Boolean> execute(Function<File, Integer> modeFunction) {
        return execute();
    }
}
