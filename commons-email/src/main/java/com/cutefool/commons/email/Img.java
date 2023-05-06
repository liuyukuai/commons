/*
 *
 */
package com.cutefool.commons.email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.io.ClassPathResource;

/**
 * 图片对象
 *
 * @author 271007729@qq.com
 * @date 2019/9/20 1:27 PM
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("WeakerAccess")
public class Img {
    /**
     * 图片路径
     */
    private ClassPathResource resource;
    /**
     * 图片id
     */
    private String id;
}
