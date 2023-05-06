/*
 *
 */
package com.cutefool.commons.ci.nexus;

/**
 * 常量类
 *
 * @author 271007729@qq.com
 * @date 7/27/21 3:43 PM
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class Constants {

    public static final String NEXUS_MAVEN_COMPONENTS_CREATE_URL = "%s/service/rest/v1/components?repository=%s";

    public static final String NEXUS_MAVEN_COMPONENTS_DELETE_URL = "%s/service/rest/v1/components/%s";

    public static final String NEXUS_MAVEN_COMPONENTS_SEARCH_URL = "%s/service/rest/v1/search?repository=%s&maven.groupId=%s&maven.baseVersion=%s";

}
