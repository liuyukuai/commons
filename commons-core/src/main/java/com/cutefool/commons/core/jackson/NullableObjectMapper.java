/*
 *
 */
package com.cutefool.commons.core.jackson;

import com.cutefool.commons.core.Constants;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * null值处理
 *
 * @author 271007729@qq.com
 * @date 2019/12/6 1:55 PM
 */
@SuppressWarnings("unused")
public class NullableObjectMapper extends ObjectMapper {

    private static final long serialVersionUID = -5769954742329739684L;

    public NullableObjectMapper() {
        super();
        // 空值处理为空串
        // 去掉空值处理，部分data对象会变成空，前端会有问题
//        this.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
//            @Override
//            public void serialize(Object value, JsonGenerator jsonGenerator, SerializerProvider sp)
//                    throws IOException {
//                jsonGenerator.writeString(StringUtils.EMPTY);
//            }
//        });

        // 时间格式化
        this.setDateFormat(new SimpleDateFormat(Constants.DATE_TIME_PATTERN, Locale.SIMPLIFIED_CHINESE));


        JavaTimeModule javaTimeModule = new JavaTimeModule();
        /* 序列化配置,针对java8 时间 **/
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern("HH:mm:ss")));

        /* 反序列化配置,针对java8 时间 **/
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern("HH:mm:ss")));


        // 处理JDK8类型
        this.registerModule(new ParameterNamesModule())
                .registerModule(new Jdk8Module())
                .registerModule(javaTimeModule);

        // 处理属性不全报错的问题
        this.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 处理字段为空转对象的问题
        this.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,false);
        // 支持下划线转驼峰
//        this.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        //处理Long转字符串
        SimpleModule longModule = new SimpleModule();
        longModule.addSerializer(Long.class, ToStringSerializer.instance);
        longModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        this.registerModule(longModule);
    }
}
