package com.cutefool.commons.face.libs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FaceMatchLibs {

    /**
     * 当前人脸图片
     */
    private String currentImage;

    /**
     * 注册的人脸图片
     */
    private String registerImage;

}
