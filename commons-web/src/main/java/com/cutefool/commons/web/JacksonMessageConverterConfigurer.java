/*
 *
 */
package com.cutefool.commons.web;

import com.cutefool.commons.core.jackson.NullableObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class JacksonMessageConverterConfigurer implements WebMvcConfigurer {


    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        List<HttpMessageConverter<?>> news = new ArrayList<>(converters);

        // 清理掉默认的MappingJackson2HttpMessageConverter
        for (HttpMessageConverter<?> converter : news) {
            if (converter instanceof MappingJackson2HttpMessageConverter) {
                converters.remove(converter);
            }
        }
        converters.add(mappingJackson2HttpMessageConverter());
    }

    @Bean
    @Primary
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(new NullableObjectMapper());
        return converter;
    }
}
