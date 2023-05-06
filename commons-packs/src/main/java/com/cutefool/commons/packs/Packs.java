/*
 *
 */
package com.cutefool.commons.packs;

import com.cutefool.commons.packs.pack.TarAbstractPackExecutor;
import com.cutefool.commons.packs.pack.ZipAbstractPackExecutor;
import com.cutefool.commons.packs.upack.TarUnpackExecutor;
import com.cutefool.commons.packs.upack.ZipUnpackExecutor;

/**
 * 压缩解压工具类
 *
 * @author 271007729@qq.com
 * @date 8/8/21 10:05 PM
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class Packs {


    public static ZipAbstractPackExecutor zip(String source, String target) {
        return new ZipAbstractPackExecutor(source, target);
    }

    public static TarAbstractPackExecutor tar(String source, String target) {
        return new TarAbstractPackExecutor(source, target);
    }

    public static ZipUnpackExecutor unzip(String source, String target) {
        return new ZipUnpackExecutor(source, target);
    }

    public static TarUnpackExecutor unTar(String source, String target) {
        return new TarUnpackExecutor(source, target);
    }

    public static void main(String[] args) {


        Packs.zip("/Users/liuyukuai/workspace/docs", "/Users/liuyukuai/workspace/docs/docs.tar.gz")
                .streamConsumer(System.out::println)
                .execute();

        Packs.unzip("/Users/liuyukuai/workspace/docs/docs.tar.gz", "/Users/liuyukuai/workspace/docs3")
                .streamConsumer(System.out::println)
                .execute();
    }

}
