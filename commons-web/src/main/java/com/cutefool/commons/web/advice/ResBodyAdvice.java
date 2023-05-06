package com.cutefool.commons.web.advice;

import com.cutefool.commons.bulk.BulkContext;
import com.cutefool.commons.bulk.BulkOperations;
import com.cutefool.commons.bulk.Bulking;
import com.cutefool.commons.core.page.PageResponse;
import com.cutefool.commons.core.page.Response;
import com.cutefool.commons.orm.rds.config.RdsConfiguration;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;

@RestControllerAdvice
public class ResBodyAdvice implements ResponseBodyAdvice<Object> {


    private final RdsConfiguration rdsConfiguration;

    public ResBodyAdvice(RdsConfiguration rdsConfiguration) {
        this.rdsConfiguration = rdsConfiguration;
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
//        Class<?> declaringClass = returnType.getExecutable().getDeclaringClass();


        if (body instanceof Response) {
//            ((Response<?>) body).setMark(Encrypts.encode(rdsConfiguration.getDb()));
//            Object oneSpi = SpiSpringContext.getOneSpi(declaringClass);
//            if (oneSpi instanceof Controllable) {
//                ((Response<?>) body).setMark(((Controllable<?>) oneSpi).table());
//                // 去掉属性返回 TODO
////                ((Response<?>) body).setFields(((Controllable<?>) oneSpi).fieldsMap());
//            }
            // 获取返回的数据
            Object data = ((Response<?>) body).getData();
            if (Objects.isNull(data)) {
                return body;
            }
            Method method = returnType.getMethod();
            if (Objects.nonNull(method)) {
                Bulking bulked = method.getAnnotation(Bulking.class);
                if (Objects.nonNull(bulked)) {
                    BulkContext context = BulkContext.init(bulked);
                    if (data instanceof PageResponse) {
                        List<?> bulking = BulkOperations.bulking((List<?>) ((PageResponse<?>) data).getList(), context);
                        return Response.ok(PageResponse.apply(((PageResponse<?>) data).getTotal(), bulking));
                    }
                    return Response.ok(BulkOperations.bulking(data, context));
                }
            }
        }
        return body;
    }
}
