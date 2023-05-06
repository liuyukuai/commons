/*
 *  
 */
package com.cutefool.commons.xml;

import lombok.extern.slf4j.Slf4j;
import com.cutefool.commons.core.util.Strings;
import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;

/**
 * xml工具类
 *
 * @author 271007729@qq.com
 * @date 2021/8/9 11:32 PM
 */
@Slf4j
@SuppressWarnings({"unchecked", "unused"})
public final class Xml {


    public static <T> Optional<T> toBean(Class<T> clz, String xml) {
        if (StringUtils.isBlank(xml)) {
            return Optional.empty();
        }

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(clz);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            return Optional.ofNullable((T) unmarshaller.unmarshal(new StringReader(xml)));
        } catch (JAXBException e) {
            log.warn(e.getMessage());
            return Optional.empty();
        }
    }

    public static <T> Optional<T> toBean(Class<T> clz, File xmlFile) {
        if (Objects.isNull(xmlFile)) {
            return Optional.empty();
        }

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(clz);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            return Optional.ofNullable((T) unmarshaller.unmarshal(new FileReader(xmlFile)));
        } catch (Exception e) {
            log.warn(e.getMessage());
            return Optional.empty();
        }
    }


    public static String toXml(Object bean) {
        if (Objects.isNull(bean)) {
            return Strings.EMPTY;
        }

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(bean.getClass());
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter stringWriter = new StringWriter();
            marshaller.marshal(bean, stringWriter);
            return stringWriter.toString();
        } catch (JAXBException e) {
            log.warn(e.getMessage());
            return Strings.EMPTY;
        }

    }


    public static File toFile(Object bean, String dest) {
        File file = new File(dest);
        if (Objects.isNull(bean)) {
            return file;
        }
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(bean.getClass());
            Marshaller marshaller = jaxbContext.createMarshaller();
            //格式化输出，即按标签自动换行，否则就是一行输出
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            //设置编码（默认编码就是utf-8）
            marshaller.setProperty(Marshaller.JAXB_ENCODING, StandardCharsets.UTF_8);
            //是否省略xml头信息，默认不省略（false）
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.FALSE);
            //编组
            marshaller.marshal(bean, file);
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
        return file;
    }
}
