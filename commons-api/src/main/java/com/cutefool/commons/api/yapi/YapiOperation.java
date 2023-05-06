/*
 *  
 */
package com.cutefool.commons.api.yapi;

import com.cutefool.commons.api.ApiCat;
import com.cutefool.commons.api.ApiOperation;
import com.cutefool.commons.api.ApiProperties;
import com.cutefool.commons.api.ApiSave;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import com.cutefool.commons.core.page.Response;
import com.cutefool.commons.core.util.JsonUtils;
import com.cutefool.commons.core.util.Lists;
import com.cutefool.commons.http.Http;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * yapi 操作类
 *
 * @author 271007729@qq.com
 * @date 2019/9/25 6:06 PM
 */
@Slf4j
@Component
@SuppressWarnings("WeakerAccess")
public class YapiOperation implements ApiOperation {

    @Resource
    private ApiProperties apiProperties;

    @Override
    public Response<String> create(ApiSave apiSave) {

        log.info("ApiSave {}", apiSave);

        // 参数map集合
        Map<String, Object> paramsMap = JsonUtils.toMap(apiSave).orElse(new HashMap<>(256));

        //请求参数
        paramsMap.put("req_query", Lists.empty(apiSave.getReqQuery()));
        // 请求头
        paramsMap.put("req_headers", Lists.empty(apiSave.getReqHeaders()));
        // 路径参数
        paramsMap.put("req_params", Lists.empty(apiSave.getReqParams()));
        // 请求体
        paramsMap.put("req_body_form", Lists.empty(apiSave.getReqBodyForm()));
        // 请求体类型
        paramsMap.put("req_body_type", "json");
        // 接口状态
        paramsMap.put("status", "done");
        // 请求参数是否为json_schema
        paramsMap.put("req_body_is_json_schema", "true");
        // 返回参数是否为json_schema
        paramsMap.put("res_body_is_json_schema", "true");

        // 请求体 json
        paramsMap.put("req_body_other", Yapis.parse(apiSave.getReqBody()));
        // 响应体
        paramsMap.put("res_body", Yapis.parse(apiSave.getResBody()));

        // 先创建分类
        Integer catId = this.getCatOrCreate(new ApiCat(apiSave.getMenu(), apiSave.getToken(), apiSave.getProjectId()));
        // id
        paramsMap.put("catid", catId);

        YapiResponse yapiResponse = Http.newInstance()
                                        .post(String.format(YapiConstant.API_SAVE_URI, apiProperties.getAddress()), paramsMap, YapiResponse.class);
        log.info("{}", yapiResponse);
        return Response.ok("");
    }

    public Integer getCatOrCreate(ApiCat apiCat) {
        String url = String.format(YapiConstant.API_LIST_CAT_URI, apiProperties.getAddress(), apiCat.getToken(), apiCat.getProjectId());
        // 查询
        YapiResponse<List<ApiCat>> response = Http.newInstance()
                                                  .get(url, new TypeReference<YapiResponse<List<ApiCat>>>() {
                                                  });

        // 是否有分类
        Integer catId = Lists.empty(response.getData())
                             .stream()
                             .filter(e -> Objects.equals(e.getName(), apiCat.getName()))
                             .findAny()
                             .map(ApiCat::getId)
                             .orElse(null);

        if (Objects.isNull(catId)) {
            // 创建分类
            url = String.format(YapiConstant.API_CREATE_CAT_URI, apiProperties.getAddress());
            YapiResponse<ApiCat> yapiResponse = Http.newInstance()
                                                    .post(url, apiCat, new TypeReference<YapiResponse<ApiCat>>() {
                                                    });
            return Optional.ofNullable(yapiResponse.getData()).map(ApiCat::getId).orElse(null);

        }
        return catId;
    }
}
