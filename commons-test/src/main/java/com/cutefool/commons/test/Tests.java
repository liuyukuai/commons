/*
 *
 */
package com.cutefool.commons.test;

import com.cutefool.commons.core.page.Response;
import com.cutefool.commons.core.page.ResponseCode;
import com.cutefool.commons.core.util.JsonUtils;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 测试工具类封装
 *
 * @author 271007729@qq.com
 * @date 2020/4/30 10:31 AM
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class Tests {

    private static final MediaType APPLICATION_JSON_UTF8 = new MediaType("application", "json", StandardCharsets.UTF_8);


    @Resource
    private MockMvc mockMvc;

    /**
     * 测试post请求
     *
     * @param uri  请求地址
     * @param body 请求内容
     * @return 请求返回值
     */
    public String post(String uri, String body) {
        return post(uri, body, TestSuppliers.ok());
    }

    /**
     * 测试post请求
     *
     * @param uri 请求地址
     * @return 请求返回值
     */
    public String post(String uri) {
        return post(uri, "", TestSuppliers.ok());
    }

    /**
     * 测试post请求
     *
     * @param uri 请求地址
     * @return 请求返回值
     */
    public String post(String uri, Supplier<List<ResultMatcher>> supplier) {
        return post(uri, "", supplier);
    }


    /**
     * 测试post请求
     *
     * @param uri      请求地址
     * @param body     请求内容
     * @param supplier 验证处理函数
     * @return 请求返回值
     */
    public String post(String uri, String body, Supplier<List<ResultMatcher>> supplier) {
        try {
            ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post(uri)
                    .contentType(APPLICATION_JSON_UTF8)
                    .content(body))
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(ResponseCode.SUCCESS.getCode()));
            return this.check(resultActions, supplier);
        } catch (Exception e) {
            //ignore
            e.printStackTrace();
            return "";

        }
    }

    /**
     * 测试put请求
     *
     * @param uri  请求地址
     * @param body 请求内容
     * @return 请求返回值
     */
    public String put(String uri, String body) {
        return put(uri, body, TestSuppliers.ok());
    }

    /**
     * 测试put请求
     *
     * @param uri 请求地址
     * @return 请求返回值
     */
    public String put(String uri) {
        return put(uri, "", TestSuppliers.ok());
    }

    /**
     * 测试put请求
     *
     * @param uri 请求地址
     * @return 请求返回值
     */
    public String put(String uri, Supplier<List<ResultMatcher>> supplier) {
        return put(uri, "", supplier);
    }

    /**
     * 测试put请求
     *
     * @param uri      请求地址
     * @param body     请求内容
     * @param supplier 验证处理函数
     * @return 请求返回值
     */
    public String put(String uri, String body, Supplier<List<ResultMatcher>> supplier) {
        try {
            ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put(uri)
                    .contentType(APPLICATION_JSON_UTF8)
                    .content(body))
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(ResponseCode.SUCCESS.getCode()));
            return this.check(resultActions, supplier);
        } catch (Exception e) {
            //ignore
            e.printStackTrace();
            return "";

        }
    }

    /**
     * 测试delete请求
     *
     * @param uri 请求地址
     * @return 请求返回值
     */
    public String delete(String uri) {
        try {
            ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete(uri)
                    .contentType(APPLICATION_JSON_UTF8))
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(ResponseCode.SUCCESS.getCode()));
            return this.check(resultActions, TestSuppliers.ok());
        } catch (Exception e) {
            //ignore
            e.printStackTrace();
            return "";

        }
    }


    /**
     * 测试get请求
     *
     * @param uri 请求地址
     */
    public void get(String uri) {
        get(uri, null, TestSuppliers.ok());

    }

    /**
     * 测试get请求
     *
     * @param uri 请求地址
     */
    public void get(String uri, Supplier<List<ResultMatcher>> supplier) {
        get(uri, null, supplier);

    }


    /**
     * 测试get请求
     *
     * @param uri    请求地址
     * @param params 请求内容
     */
    public void get(String uri, Map<String, String> params) {
        get(uri, params, TestSuppliers.ok());
    }

    /**
     * 测试post请求
     *
     * @param uri      请求地址
     * @param params   请求内容
     * @param supplier 验证处理函数
     */
    public void get(String uri, Map<String, String> params, Supplier<List<ResultMatcher>> supplier) {
        try {
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(uri)
                    .contentType(APPLICATION_JSON_UTF8);

            // 处理参数
            if (Objects.nonNull(params) && !params.isEmpty()) {
                params.forEach(requestBuilder::param);
            }

            ResultActions resultActions = mockMvc.perform(requestBuilder)
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(ResponseCode.SUCCESS.getCode()));

            // 校验
            List<ResultMatcher> resultMatchers = Optional.ofNullable(supplier)
                    .orElse(TestSuppliers.ok())
                    .get();

            // 校验所有规则
            for (ResultMatcher resultMatcher : resultMatchers) {
                resultActions.andExpect(resultMatcher);
            }
        } catch (Exception e) {
            //ignore
            e.printStackTrace();

        }
    }


    private String check(ResultActions resultActions, Supplier<List<ResultMatcher>> supplier) throws Exception {
        // 校验
        List<ResultMatcher> resultMatchers = Optional.ofNullable(supplier)
                .orElse(TestSuppliers.ok())
                .get();

        // 校验所有规则
        for (ResultMatcher resultMatcher : resultMatchers) {
            resultActions.andExpect(resultMatcher);
        }

        // 获取内容
        String content = resultActions
                .andReturn()
                .getResponse()
                .getContentAsString();
        // 转换对象
        return JsonUtils.toBean(content, Response.class)
                .map(Response::getData)
                .map(String::valueOf)
                .orElse("");
    }
}
