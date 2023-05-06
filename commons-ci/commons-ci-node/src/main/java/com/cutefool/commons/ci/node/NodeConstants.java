/*
 *
 */
package com.cutefool.commons.ci.node;

/**
 * @author 271007729@qq.com
 * @date 8/14/21 1:00 PM
 */
public class NodeConstants {

    public static final String YARN_VERSION = "yarn -version";

    public static final String YARN_INSTALL = "%s install -g yarn";

    public static final String YARN_INSTALL_EXECUTE = "yarn install";

    public static final String YARN_BUILD_EXECUTE = "yarn run build";

    public static final String YARN_INSTALL_WITH_REGISTRY_EXECUTE = "yarn install --registry=%s";

}
