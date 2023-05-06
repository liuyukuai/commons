/*
 *
 */
package com.cutefool.commons.test;

/**
 * 可测试的接口
 *
 * @author 271007729@qq.com
 * @date 2020/4/30 11:13 AM
 */
@SuppressWarnings("unused")
public interface Testable<ID, LoginService> {

    /**
     * 默认实现方法
     *
     * @param loginUserService
     * @return testable
     */
    default Testable<ID, LoginService> before(LoginService loginUserService) {

        return this;
    }

    /**
     * 执行测试
     *
     * @return 对象
     */
    ID execute();

    /**
     * 删除数据
     *
     * @param id 主键
     */
    void delete(ID id);

}
