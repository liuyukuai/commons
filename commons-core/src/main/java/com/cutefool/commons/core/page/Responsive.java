package com.cutefool.commons.core.page;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author 271007729@qq.com
 * @date 2019-07-02 10:49
 */
@SuppressWarnings("unused")
public interface Responsive {

    /**
     * check response is success
     *
     * @return true: success,false: fail
     */
    boolean isSuccess();

    /**
     * check response is isFailure
     *
     * @return true: success,false: fail
     */
    @JsonIgnore
    default boolean isFailure() {
        return !this.isSuccess();
    }
}
