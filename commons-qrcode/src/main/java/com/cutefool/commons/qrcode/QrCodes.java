/*
 *  
 */
package com.cutefool.commons.qrcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.cutefool.commons.qrcode.config.QrCodeConfig;
import com.cutefool.commons.qrcode.libs.Pictures;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 二维码工具类
 *
 * @author 271007729@qq.com
 * @date 4/22/21 10:40 AM
 */
@SuppressWarnings("unused")
public class QrCodes {

    /**
     * 生成一个二维码图片
     *
     * @param qrCodeConfig 二维码配置
     * @return 字节流数组
     */

    public static byte[] bytes(QrCodeConfig qrCodeConfig) throws WriterException, IOException {

        BitMatrix bitMatrix = bitMatrix(qrCodeConfig);

        ByteArrayOutputStream os = new ByteArrayOutputStream();

        MatrixToImageWriter.writeToStream(bitMatrix, qrCodeConfig.getSuffix(), os);

        // 需要补充图片内容
        if (qrCodeConfig.isDraw()) {
            ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
            return Pictures.writeBytes(is, qrCodeConfig);
        }
        return os.toByteArray();
    }

    /**
     * 生成一个二维码图片
     *
     * @param qrCodeConfig 二维码配置
     */

    public static void files(File file, QrCodeConfig qrCodeConfig) throws WriterException, IOException {

        BitMatrix bitMatrix = bitMatrix(qrCodeConfig);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, qrCodeConfig.getSuffix(), os);
        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
        // 需要补充图片内容
        if (qrCodeConfig.isDraw()) {
            Pictures.writeFiles(is, file, qrCodeConfig);
            return;
        }
        ImageIO.write(ImageIO.read(is), qrCodeConfig.getSuffix(), file);
    }


    private static BitMatrix bitMatrix(QrCodeConfig qrCodeConfig) throws WriterException {
        // 二维码基本参数设置
        Map<EncodeHintType, Object> hints = new HashMap<>(16);
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");

        // 设置纠错等级L/M/Q/H,纠错等级越高越不易识别，当前设置等级为最高等级H
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);

        // 可设置范围为0-10，但仅四个变化0 1(2) 3(4 5 6) 7(8 9 10)
        hints.put(EncodeHintType.MARGIN, 0);

        // 生成图片类型为QRCode
        BarcodeFormat format = BarcodeFormat.QR_CODE;
        // 创建位矩阵对象
        return new MultiFormatWriter().encode(qrCodeConfig.getContent(), format, qrCodeConfig.getWidth(), qrCodeConfig.getHeight(), hints);
    }

//    public static void main(String[] args) throws IOException, WriterException {
//
//        File file = new File("/Users/liuyukuai/workspace/a.png");
//
//        QrCodeConfig config = QrCodeConfig.create("11111", Lists.newArrayList("我我是一个小虎牙达到的地方"));
//        WordConfig wordConfig = WordConfig.create("分离热区单元（T501、T502、T605）系统", 24);
//        wordConfig.append("就这样了不讲价了！！！！！");
//        config.setWordConfig(wordConfig);
//        config.setHeight(500);
//        QrCodes.files(file, config);
//    }
}
