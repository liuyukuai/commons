package com.cutefool.commons.orm.rds.config;

import lombok.Data;
import com.cutefool.commons.core.util.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@ConfigurationProperties(prefix = "commons.database")
public class RdsDatabaseConfiguration {
    @Value("${commons.database.psm:tsingyun_psm}")
    private String psm;
    @Value("${commons.database.basics:tsingyun_basics}")
    private String basics;
    @Value("${commons.database.warning:tsingyun_warning}")
    private String warning;
    @Value("${commons.database.inspection:tsingyun_inspection}")
    private String inspection;
    @Value("${commons.database.equipment:tsingyun_equipment}")
    private String equipment;
    @Value("${commons.database.improve:tsingyun_improve}")
    private String improve;
    @Value("${commons.database.exclusions:}")
    private List<String> exclusions;
    @Value("${commons.database.includes:}")
    private List<String> includes;


    public static Map<String, String> routerMap() {
        Map<String, String> routerMap = Maps.hashMap();
        routerMap.put("psm", "TSINGYUN-PSM");
        routerMap.put("basics", "TSINGYUN-BASICS");
        routerMap.put("warning", "TSINGYUN-WARNING");
        routerMap.put("improve", "TSINGYUN-IMPROVE");
        routerMap.put("equipment", "TSINGYUN-EQUIPMENT");
        routerMap.put("inspection", "TSINGYUN-INSPECTION");
        return routerMap;
    }

    public Map<String, String> loadDatabase() {
        Map<String, String> routerMap = routerMap();
        Field[] fields = RdsDatabaseConfiguration.class.getDeclaredFields();
        return Stream.of(fields)
                .filter(e -> routerMap.containsKey(e.getName()))
                .map(e -> {
                    e.setAccessible(true);
                    Map<String, String> valueMap = Maps.hashMap();
                    Value annotation = e.getAnnotation(Value.class);
                    String value = Objects.nonNull(annotation) ? annotation.value() : "";
                    if (StringUtils.isNotBlank(value)) {
                        try {
                            Object o = e.get(this);
                            String s = routerMap.get(e.getName());
                            if (StringUtils.isNotBlank(s) && Objects.nonNull(o) && StringUtils.isNotBlank(o.toString())) {
                                valueMap.put(s, String.valueOf(o));
                            }
                        } catch (IllegalAccessException ex) {
                            ex.printStackTrace();
                        }
                    }
                    return valueMap;
                })
                .map(Map::entrySet)
                .flatMap(Set::stream)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
