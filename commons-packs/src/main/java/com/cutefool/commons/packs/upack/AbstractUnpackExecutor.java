/*
 *
 */
package com.cutefool.commons.packs.upack;

import lombok.extern.slf4j.Slf4j;
import com.cutefool.commons.core.function.StreamConsumer;
import com.cutefool.commons.packs.Packable;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.file.Files;
import java.util.Objects;

/**
 * @author 271007729@qq.com
 * @date 8/8/21 10:39 PM
 */
@Slf4j
public abstract class AbstractUnpackExecutor implements Packable {

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
    private ArchiveInputStream ais;


    public AbstractUnpackExecutor streamConsumer(StreamConsumer streamConsumer) {
        this.streamConsumer = streamConsumer;
        return this;
    }


    String getSource() {
        return source;
    }

    AbstractUnpackExecutor ais(ArchiveInputStream ais) {
        this.ais = ais;
        return this;
    }


    AbstractUnpackExecutor(String source, String target) {
        this.source = source;
        this.target = target;
    }

    boolean doExecute() throws IOException {

        ArchiveEntry entry;
        while (Objects.nonNull(entry = ais.getNextEntry())) {
            if (!ais.canReadEntryData(entry) || StringUtils.isBlank(entry.getName())) {
                continue;
            }
            File file = new File(target + File.separator + entry.getName());
            consumeLine("unpacking " + file.getAbsolutePath());
            if (entry.isDirectory()) {
                if (!file.exists() && !file.mkdirs()) {
                    consumeLine("failed to create directory = " + file.getAbsolutePath());
                }
            } else {
                File parent = file.getParentFile();
                if (!parent.exists() && !parent.mkdirs()) {
                    consumeLine("failed to create directory = " + parent.getAbsolutePath());
                }
                try (OutputStream o = Files.newOutputStream(file.toPath())) {
                    IOUtils.copy(ais, o);
                }
            }
        }
        return true;
    }

    @Override
    public void consumeLine(String s) {
        if (Objects.nonNull(streamConsumer)) {
            this.streamConsumer.consumeLine(s);
        }
    }
}
