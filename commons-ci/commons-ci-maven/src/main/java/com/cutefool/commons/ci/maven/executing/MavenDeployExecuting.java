/*
 *
 */
package com.cutefool.commons.ci.maven.executing;

import com.cutefool.commons.ci.maven.Maven;
import com.cutefool.commons.ci.maven.context.MavenContext;
import com.cutefool.commons.ci.maven.libs.Messages;
import com.cutefool.commons.ci.core.CiConsumer;
import com.cutefool.commons.ci.core.CiDeployContext;
import com.cutefool.commons.core.page.Response;

/**
 * @author 271007729@qq.com
 * @date 7/11/21 5:06 PM
 */
@SuppressWarnings("unused")
public class MavenDeployExecuting<T extends CiDeployContext<T>> implements MavenExecuting<T, Boolean> {

    @Override
    public Response<Boolean> execute(T context) {
        CiConsumer<T> ciProcessConsumer = CiConsumer.empty(context.getCiProcessConsumer());
        MavenContext mavenContext = this.context(context);
        ciProcessConsumer.consumer(context, Messages.MAVEN_DEPLOY_EXECUTING);
        Response<Boolean> execute = Maven.open(mavenContext).deploy().execute();
        ciProcessConsumer.consumer(context, execute.isSuccess() ? Messages.MAVEN_DEPLOY_SUCCESS : Messages.MAVEN_DEPLOY_FAIL + " " + execute.getMsg());
        return execute;
    }
}
