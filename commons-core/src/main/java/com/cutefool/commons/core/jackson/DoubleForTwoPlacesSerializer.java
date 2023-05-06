package com.cutefool.commons.core.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.text.DecimalFormat;

/**
 * @author 271007729@qq.com
 * @date 2020/4/29 3:36 PM
 */
@SuppressWarnings("unused")
public class DoubleForTwoPlacesSerializer extends JsonSerializer<Double> {

    @Override
    public void serialize(Double value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (value != null) {
            //保留两位小数
            DecimalFormat df = new DecimalFormat("#.##");
            String format = df.format(value);
            if (StringUtils.isNotBlank(format)) {
                jsonGenerator.writeNumber(format);
            }
        }
    }
}
