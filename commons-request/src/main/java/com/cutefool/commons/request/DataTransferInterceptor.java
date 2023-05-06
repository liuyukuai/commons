/*
 *
 */
package com.cutefool.commons.request;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 拦截所有的feign请求
 *
 * @author 271007729@qq.com
 */
@Slf4j
public class DataTransferInterceptor implements RequestInterceptor {

    @Resource
    private RequestProperties requestProperties;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        try {
            // 判断是否需要传递header
            String allowHeader = requestProperties.getAllowHeader();
            if (log.isDebugEnabled()) {
                log.debug(" commons.request.allowHeaders = [{}] ", allowHeader);
            }
            if (StringUtils.isNotBlank(allowHeader)) {
                ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                if (requestAttributes != null) {
                    HttpServletRequest request = requestAttributes.getRequest();
                    // 优先从请求头中获取信息
                    List<String> keys = Stream.of(allowHeader.split(",")).collect(Collectors.toList());
                    for (String key : keys) {
                        String value = request.getHeader(key);
                        if (log.isDebugEnabled()) {
                            log.debug("get header from request key = [{}] , value = [{}]", key, value);
                        }
                        // 如果值不为空添加到请求头中去
                        if (StringUtils.isNotBlank(value)) {
                            requestTemplate.header(key, value);
                        }
                    }

                } else {
                    if (log.isDebugEnabled()) {
                        log.debug("RequestContextHolder.getRequestAttributes() is null");
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }
}
