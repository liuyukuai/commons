/*
 *
 */
package com.cutefool.commons.test;

import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author 271007729@qq.com
 * @date 2020/4/30 1:33 PM
 */
@SuppressWarnings("WeakerAccess")
public class TestSuppliers {

    private static Supplier<List<ResultMatcher>> DEFAULT_SUCCESS = () -> Collections.singletonList(MockMvcResultMatchers.jsonPath("$.data").isNotEmpty());


    public static Supplier<List<ResultMatcher>> ok() {
        return DEFAULT_SUCCESS;
    }


}
