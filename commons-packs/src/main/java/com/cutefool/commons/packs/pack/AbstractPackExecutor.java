/*
 *
 */
package com.cutefool.commons.packs.pack;

import com.cutefool.commons.packs.Packable;
import lombok.extern.slf4j.Slf4j;
import com.cutefool.commons.core.function.StreamConsumer;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;
import java.util.function.BiFunction;

/**
 * @author 271007729@qq.com
 * @date 8/8/21 10:39 PM
 */
@Slf4j
public abstract class AbstractPackExecutor implements Packable {

    /**
     * 源文件
     */
    private final String source;

    /**
     * 目标文件
     */
    private final String target;

    /**
     * 日志操作类
     */
    private StreamConsumer streamConsumer;

    /**
     * 流
     */
    private ArchiveOutputStream aos;

    String getTarget() {
        return target;
    }

    public AbstractPackExecutor streamConsumer(StreamConsumer streamConsumer) {
        this.streamConsumer = streamConsumer;
        return this;
    }

    AbstractPackExecutor aos(ArchiveOutputStream aos) {
        this.aos = aos;
        return this;
    }

    AbstractPackExecutor(String source, String target) {
        this.source = source;
        this.target = target;
    }

    boolean doExecute(BiFunction<Path, Path, ArchiveEntry> function) throws IOException {
        Path dirPath = Paths.get(source);
        Files.walkFileTree(dirPath, new SimpleFileVisitor<Path>() {

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                consumeLine("packing dir " + dir.toFile().getAbsolutePath());
                ArchiveEntry entry = function.apply(dirPath, dir);
                aos.putArchiveEntry(entry);
                aos.closeArchiveEntry();
                return super.preVisitDirectory(dir, attrs);
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                String absolutePath = file.toFile().getAbsolutePath();
                consumeLine("packing file " + absolutePath);
                // 如果该文件和目标文件一致，跳过该文件
                if (Objects.equals(absolutePath, target)) {
                    consumeLine("packing ignore " + absolutePath);
                    return FileVisitResult.SKIP_SUBTREE;
                }
                ArchiveEntry entry = function.apply(dirPath, file);
                aos.putArchiveEntry(entry);
                IOUtils.copy(Files.newInputStream(file.toFile().toPath()), aos);
                aos.closeArchiveEntry();
                return super.visitFile(file, attrs);
            }

        });
        return true;
    }

    @Override
    public void consumeLine(String s) {
        if (Objects.nonNull(streamConsumer)) {
            this.streamConsumer.consumeLine(s);
        }

    }
}
