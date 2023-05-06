/*
 *
 */
package com.cutefool.commons.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 公共启动类
 *
 * @author 271007729@qq.com
 * @date 7/27/21 10:25 AM
 */

@SpringBootApplication
public class Bootstrap {

    public static void main(String[] args) {
        SpringApplication.run(Bootstrap.class, args);
    }
}
