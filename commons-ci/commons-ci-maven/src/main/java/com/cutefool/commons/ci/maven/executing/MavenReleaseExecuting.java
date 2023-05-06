/*
 *
 */
package com.cutefool.commons.ci.maven.executing;

import com.cutefool.commons.ci.maven.Maven;
import com.cutefool.commons.ci.maven.context.MavenContext;
import com.cutefool.commons.ci.maven.libs.Messages;
import com.cutefool.commons.ci.core.CiConsumer;
import com.cutefool.commons.ci.core.CiReleaseContext;
import com.cutefool.commons.core.page.Response;

/**
 * @author 271007729@qq.com
 * @date 7/11/21 5:06 PM
 */
@SuppressWarnings("unused")
public class MavenReleaseExecuting<T extends CiReleaseContext<T>> implements MavenExecuting<T, Boolean> {

    @Override
    public Response<Boolean> execute(T context) {
        CiConsumer<T> ciProcessConsumer = CiConsumer.empty(context.getCiProcessConsumer());
        MavenContext mavenContext = this.context(context);
        ciProcessConsumer.consumer(context, Messages.MAVEN_RELEASE_EXECUTING);
        Response<Boolean> execute = Maven.open(mavenContext)
                                         .release()
                                         .release(context.getRelease())
                                         .tag(context.getTag())
                                         .snapshot(context.getSnapshot())
                                         .execute();
        ciProcessConsumer.consumer(context, execute.isSuccess() ? Messages.MAVEN_RELEASE_SUCCESS : Messages.MAVEN_RELEASE_FAIL + " " + execute.getMsg());
        // 如果没有执行成功，需要回滚代码
        if (execute.isFailure()) {
            ciProcessConsumer.consumer(context, Messages.MAVEN_ROLLBACK);
            Response<Boolean> rollback = Maven.open(mavenContext).rollback().execute();
            if (rollback.isFailure()) {
                ciProcessConsumer.consumer(context, Messages.MAVEN_CLEAN);
                Maven.open(mavenContext).clean().execute();
            }
        }
        return execute;
    }
}
