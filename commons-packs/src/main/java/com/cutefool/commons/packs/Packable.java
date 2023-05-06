/*
 *
 */
package com.cutefool.commons.packs;

import com.cutefool.commons.core.function.StreamConsumer;
import com.cutefool.commons.core.page.Response;

import java.io.File;
import java.util.function.Function;

/**
 * 可打包的
 *
 * @author 271007729@qq.com
 * @date 8/8/21 11:02 PM
 */
public interface Packable extends StreamConsumer {

    /**
     * 可执行接口
     *
     * @return true or false
     */
    Response<Boolean> execute();

    /**
     * 可执行接口
     *
     * @param modeFunction 文件权限接口
     * @return true or false
     */
    Response<Boolean> execute(Function<File, Integer> modeFunction);
}
