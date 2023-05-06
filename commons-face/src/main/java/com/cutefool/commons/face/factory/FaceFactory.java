package com.cutefool.commons.face.factory;

import com.cutefool.commons.autoconfig.face.FaceConfiguration;
import com.cutefool.commons.face.FaceInvoker;
import com.cutefool.commons.face.enums.FaceType;
import com.cutefool.commons.spring.SpiSpringContext;

import javax.annotation.Resource;
import java.util.Objects;

@SuppressWarnings("unused")
public class FaceFactory {

    @Resource
    private FaceConfiguration faceConfiguration;

    public FaceInvoker getInvoker() {
        return this.getInvoker(FaceType.byType(faceConfiguration.getType()));
    }

    public FaceInvoker getInvoker(FaceType faceType) {
        Objects.requireNonNull(faceType);
        return SpiSpringContext.getSpi(FaceInvoker.FACE_PREFIX + faceType.getType(), FaceInvoker.class);
    }
}
