/*
 *  
 */
package com.cutefool.commons.ci.webpack.libs;

import lombok.Data;
import com.cutefool.commons.core.util.Maps;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * 配置
 *
 * @author 271007729@qq.com
 * @date 8/13/21 10:17 AM
 */
@Data
@SuppressWarnings("WeakerAccess")
public class WebpackConfig {

    private static final String NAME = "name";
    private static final String VERSION = "version";
    private static final String GROUP_ID = "groupId";
    private static final String DEPENDENCIES = "dependencies";


    /**
     * 所有数据
     */
    private Map<String, Object> configs;

    public String getName() {
        return String.valueOf(configs.get(NAME));
    }

    public String getVersion() {
        return String.valueOf(configs.get(VERSION));
    }

    public void setVersion(String version) {
        configs.put(VERSION, version);
    }

    public String getGroupId() {
        return String.valueOf(configs.get(GROUP_ID));
    }

    @SuppressWarnings("unchecked")
    public Map<String, String> getDependencies() {
        Map<String, Object> dependencies = (Map<String, Object>) configs.get(DEPENDENCIES);
        return Maps.empty(dependencies).entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> String.valueOf(e.getValue())));
    }

    public void setDependencies(Map<String, String> dependencies) {
        this.configs.put(DEPENDENCIES, dependencies);
    }
}
