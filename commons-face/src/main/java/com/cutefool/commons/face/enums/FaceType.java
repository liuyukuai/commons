package com.cutefool.commons.face.enums;

import com.cutefool.commons.face.baidu.BaiduConstants;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

public enum FaceType {
    /**
     * 百度
     */
    baidu(BaiduConstants.BAIDU_FACE_NAME);

    FaceType(String type) {
        this.type = type;
    }

    @Getter
    private final String type;

    /**
     * 查询人脸识别类型
     *
     * @param type 类型
     * @return 名称
     */
    public static FaceType byType(String type) {
        return Stream.of(FaceType.values())
                .filter(e -> Objects.equals(type, baidu.getType()))
                .findAny()
                .orElse(baidu);
    }
}
