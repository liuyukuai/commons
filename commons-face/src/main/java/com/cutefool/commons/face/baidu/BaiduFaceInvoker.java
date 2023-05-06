package com.cutefool.commons.face.baidu;

import com.baidu.aip.face.AipFace;
import com.cutefool.commons.face.FaceInvoker;
import com.cutefool.commons.face.libs.FaceLibs;
import com.cutefool.commons.face.libs.FaceMatchLibs;
import lombok.extern.slf4j.Slf4j;
import com.cutefool.commons.autoconfig.baidu.BaiduFaceConfiguration;
import com.cutefool.commons.autoconfig.face.FaceConfiguration;
import com.cutefool.commons.core.page.Response;
import com.cutefool.commons.core.util.JsonUtils;
import com.cutefool.commons.core.util.Lists;
import com.cutefool.commons.mock.MockProperties;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

@Slf4j
public class BaiduFaceInvoker implements FaceInvoker {

    private final AipFace aipFace;

    @Resource
    private MockProperties properties;
    @Resource
    private FaceConfiguration faceConfiguration;


    public BaiduFaceInvoker(BaiduFaceConfiguration baiduFaceConfiguration) {
        aipFace = new AipFace(baiduFaceConfiguration.getAppId(), baiduFaceConfiguration.getAk(), baiduFaceConfiguration.getSk());
    }


    private static HashMap<String, String> options(FaceLibs libs, String actionType) {
        HashMap<String, String> options = new HashMap<>();
        options.put("user_info", libs.getUserName());
        options.put("quality_control", "NORMAL");
        options.put("liveness_control", "LOW");
        options.put("action_type", actionType);
        return options;
    }


    private Response<Boolean> doFace(FaceLibs faceLibs, String actionType) {
        final HashMap<String, String> options = options(faceLibs, actionType);
        List<String> faces = faceLibs.getFaces();
        for (String e : faces) {
            JSONObject res = aipFace.addUser(e, BaiduConstants.BAIDU_FACE_IMAGE_TYPE, BaiduConstants.BAIDU_FACE_GROUP, faceLibs.getUserId() + "", options);
            Response<Boolean> response = this.doResult(res);
            if (response.isFailure()) {
                return response;
            }
        }
        return Response.ok();
    }


    private Response<Boolean> doResult(JSONObject res) {
        /*
        int code = res.getInt("error_code");
        String msg = res.getString("error_msg");
        // 如果不成功结束上传
        if (!Objects.equals(code, BaiduConstants.BAIDU_FACE_CODE_SUCCESS)) {
            return Response.failure(ResponseCode.BAIDU_FACE.getCode(), String.format(ResponseCode.BAIDU_FACE.getMessage(), msg));
        }
        return Response.ok();
         */

        String msg;
        try {
            Object scoreStr = Optional.ofNullable(res.get("result"))
                    .map(e -> (JSONObject) e)
                    .map(e -> e.get("score"))
                    .orElse("0");

            Double score = new Double(String.valueOf(scoreStr));
            Double configScore = new Double(this.faceConfiguration.getScoreMin());

            return Response.ok(score >= configScore);
        } catch (Exception e) {
            msg = e.getMessage();
            log.error("人脸对比结果解析失败，返回值：{}", JsonUtils.toJson(res), e);
        }
        return Response.failure(msg);
    }


    @Override
    public Response<Boolean> replaceFace(FaceLibs faceLibs) {
        return doFace(faceLibs, BaiduConstants.BAIDU_FACE_ACTION_REPLACE);
    }


    @Override
    public Response<Boolean> replaceFace(List<FaceLibs> faceLibs) {
        if (Lists.isEmpty(faceLibs)) {
            log.warn("face libs is empty.");
            return Response.ok();
        }
        for (FaceLibs faceLib : faceLibs) {
            Response<Boolean> res = doFace(faceLib, BaiduConstants.BAIDU_FACE_ACTION_REPLACE);
            if (res.isFailure()) {
                return res;
            }
        }
        return Response.ok();
    }


    @Override
    public Response<Boolean> appendFace(FaceLibs faceLibs) {
        return doFace(faceLibs, BaiduConstants.BAIDU_FACE_ACTION_APPEND);
    }


    @Override
    public Response<Boolean> appendFace(List<FaceLibs> faceLibs) {
        if (Lists.isEmpty(faceLibs)) {
            log.warn("face libs is empty.");
            return Response.ok();
        }
        for (FaceLibs faceLib : faceLibs) {
            Response<Boolean> res = doFace(faceLib, BaiduConstants.BAIDU_FACE_ACTION_APPEND);
            if (res.isFailure()) {
                return res;
            }
        }
        return Response.ok();
    }


    @Override
    public Response<Boolean> doMatch(FaceMatchLibs libs) {
        Boolean enabled = properties.getEnabled();
        String department = properties.getDepartment();
        if (Objects.equals(enabled, true) || Objects.equals(department, "test-mock")) {
            Random random = new Random();
            int i = random.nextInt(10);
            return Response.ok(i / 2 == 0);
        }

        JSONObject res = aipFace.faceMingJingMatch(libs.getCurrentImage(), BaiduConstants.BAIDU_FACE_IMAGE_TYPE, libs.getRegisterImage(), BaiduConstants.BAIDU_FACE_IMAGE_TYPE, null);
        System.out.println(res);
        Response<Boolean> response = this.doResult(res);
        if (response.isFailure()) {
            return response;
        }
        return Response.ok();
    }

    //24.7db627c45b650b47731e7e80f3d08dc1.2592000.1675714508.282335-29582872


    public static void main(String[] args) throws IOException {
        BaiduFaceConfiguration configuration = new BaiduFaceConfiguration();
        configuration.setAppId("29582872");
        configuration.setAk("kO6BeDkG1k6lTs1yGsCM4jw4");
        configuration.setSk("VY4U5HyPoHnCDFzI5BW3hHbwmwB36yHp");

        BaiduFaceInvoker baiduFaceInvoker = new BaiduFaceInvoker(configuration);


        File file = new File("/Users/liuyukuai/Downloads/未标题-2.jpg");
        String image = Base64.getEncoder().encodeToString(IOUtils.toByteArray(new FileInputStream(file)));

        FaceMatchLibs libs = new FaceMatchLibs();
        libs.setCurrentImage(image);
        libs.setRegisterImage(image);
        Response<Boolean> booleanResponse = baiduFaceInvoker.doMatch(libs);
    }

}
