package com.cutefool.commons.web.advice;

import com.cutefool.commons.core.util.JsonUtils;
import com.cutefool.commons.core.util.Maps;
import com.cutefool.commons.core.util.Reflects;
import com.cutefool.commons.orm.Extensible;
import org.apache.commons.io.IOUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ReqBodyAdvice implements RequestBodyAdvice {


    @Override
    @SuppressWarnings("NullableProblems")
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        if (targetType instanceof Class) {
            Class<?> cls = (Class<?>) targetType;
            if (!Extensible.class.isAssignableFrom(cls)) {
                return inputMessage;
            }
            return new ExtensibleHttpInputMessage(cls, inputMessage);
        }
        return inputMessage;
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    public static final class ExtensibleHttpInputMessage implements HttpInputMessage {

        private final HttpHeaders httpHeaders;

        private InputStream body;

        @SuppressWarnings("ResultOfMethodCallIgnored")
        public ExtensibleHttpInputMessage(Class<?> cls, HttpInputMessage inputMessage) {
            this.httpHeaders = inputMessage.getHeaders();
            try {
                this.body = inputMessage.getBody();
                if (inputMessage.getBody().available() >= 0) {
                    byte[] bytes = new byte[inputMessage.getBody().available()];
                    inputMessage.getBody().read(bytes);
                    Map<String, Object> d = JsonUtils.toMap(new String(bytes, StandardCharsets.UTF_8)).orElse(Maps.hashMap());
                    List<String> fields = Reflects.fields(cls);
                    Map<String, Object> extensibleMap = d.entrySet().stream().filter(e -> Objects.nonNull(e.getValue())).filter(e -> !fields.contains(e.getKey())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                    d.put("extensible", extensibleMap);
                    this.body = IOUtils.toInputStream(JsonUtils.toJson(d), StandardCharsets.UTF_8);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        @SuppressWarnings("NullableProblems")
        public InputStream getBody() {
            return body;
        }

        @Override
        @SuppressWarnings("NullableProblems")
        public HttpHeaders getHeaders() {
            return httpHeaders;
        }
    }


}
