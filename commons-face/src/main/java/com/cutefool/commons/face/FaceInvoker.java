package com.cutefool.commons.face;

import com.cutefool.commons.core.page.Response;
import com.cutefool.commons.face.libs.FaceLibs;
import com.cutefool.commons.face.libs.FaceMatchLibs;

import java.util.List;

public interface FaceInvoker {

    /**
     * 人脸识别前缀
     */
    String FACE_PREFIX = "spi_face_";

    /**
     * 人脸注册替换
     */
    Response<Boolean> replaceFace(FaceLibs faceLibs);

    /**
     * 人脸注册替换（批量）
     */
    Response<Boolean> replaceFace(List<FaceLibs> faceLibs);


    /**
     * 人脸注册追加
     */
    Response<Boolean> appendFace(FaceLibs faceLibs);

    /**
     * 人脸注册追加（批量）
     */
    Response<Boolean> appendFace(List<FaceLibs> faceLibs);


    /**
     * 人脸对比
     * https://ai.baidu.com/ai-doc/FACE/8k37c1rqz#%E4%BA%BA%E8%84%B8%E6%AF%94%E5%AF%B9v4
     */
    Response<Boolean> doMatch(FaceMatchLibs libs);
}
