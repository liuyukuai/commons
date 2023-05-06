/*
 *  
 */
package com.cutefool.commons.qrcode.libs;

import lombok.extern.slf4j.Slf4j;
import com.cutefool.commons.core.util.Lists;
import com.cutefool.commons.fonts.Fonts;
import com.cutefool.commons.qrcode.config.LogoConfig;
import com.cutefool.commons.qrcode.config.QrCodeConfig;
import com.cutefool.commons.qrcode.config.WordConfig;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

/**
 * 图片操作类
 *
 * @author 271007729@qq.com
 * @date 4/22/21 11:47 AM
 */
@Slf4j
@SuppressWarnings("unused")
public class Pictures {


    public static byte[] writeBytes(InputStream is, QrCodeConfig qrCodeConfig) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            BufferedImage draw = draw(is, qrCodeConfig);

            if (Objects.nonNull(draw)) {
                ImageIO.write(draw, qrCodeConfig.getSuffix(), outputStream);
            }
            return outputStream.toByteArray();

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public static void writeFiles(InputStream is, File dest, QrCodeConfig qrCodeConfig) {
        BufferedImage draw = draw(is, qrCodeConfig);
        writeFiles(draw, dest, qrCodeConfig.getSuffix());
    }


    public static void writeFiles(InputStream is, File dest, WordConfig config) {
        BufferedImage draw = draw(is, config);
        writeFiles(draw, dest, config.getSuffix());

    }

    private static BufferedImage draw(InputStream is, WordConfig config) {
        try {
            BufferedImage image = ImageIO.read(is);
            int width = image.getWidth();
            int height = image.getHeight();
            // 高度增加30
            BufferedImage bufferedImage = new BufferedImage(width, height + config.getBottomHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = bufferedImage.createGraphics();
            graphics.setBackground(Color.WHITE);
            graphics.clearRect(0, 0, width, height + config.getBottomHeight());
            //这里减去25是为了防止字和图重合
            graphics.drawImage(image, 0, 0, width, height, null);
            drawWord(graphics, config, width, height + config.getBottomHeight(), (config.getBottomHeight() + 30) / 2);
            graphics.dispose();
            return bufferedImage;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    private static void writeFiles(BufferedImage draw, File dest, String suffix) {
        if (Objects.nonNull(draw)) {
            try {
                ImageIO.write(draw, suffix, dest);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }


    private static BufferedImage draw(InputStream is, QrCodeConfig qrCodeConfig) {
        try {

            Image image = ImageIO.read(is);
            int width = getWidth(qrCodeConfig);
            int height = getHeight(qrCodeConfig);

            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = bufferedImage.createGraphics();
            graphics.setBackground(Color.WHITE);
            graphics.clearRect(0, 0, width, height);
            //这里减去25是为了防止字和图重合
            graphics.drawImage(image, qrCodeConfig.getBorderLeft(), qrCodeConfig.getBorderTop(), qrCodeConfig.getWidth(), qrCodeConfig.getHeight(), null);
            if (Objects.nonNull(qrCodeConfig.getLogoConfig())) {
                drawLogo(graphics, qrCodeConfig);
            }

            if (Objects.nonNull(qrCodeConfig.getWordConfig()) || Lists.isEmpty(qrCodeConfig.getWordConfig().getWords())) {
                drawWord(graphics, qrCodeConfig);
            }

            graphics.dispose();
            return bufferedImage;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }

    }

    private static void drawWord(Graphics2D graphics, QrCodeConfig qrCodeConfig) {
        int bottomHeight = (qrCodeConfig.getBottomHeight() + qrCodeConfig.getBorderBottom()) / 2;
        drawWord(graphics, qrCodeConfig.getWordConfig(), getWidth(qrCodeConfig), getHeight(qrCodeConfig), bottomHeight);
    }

    private static void drawWord(Graphics2D graphics, WordConfig wordConfig, int width, int height, int bottomHeight) {
        // 设置生成图片的文字样式
        Font font = Fonts.load("微软雅黑", wordConfig.getFontSize());
        graphics.setFont(font);
        graphics.setPaint(Color.BLACK);
        FontRenderContext context = graphics.getFontRenderContext();
        List<String> words = wordConfig.getWords();
        for (int i = 0; i < words.size(); i++) {
            Rectangle2D bounds = font.getStringBounds(words.get(i), context);
            double x = (width - bounds.getWidth()) / 2;
            double y = height - bottomHeight + bounds.getHeight() / (2 * words.size()) - (bounds.getHeight() - wordConfig.getFontSize()) - 5 * words.size();
            graphics.drawString(words.get(i), (int) x, (int) y + (int) (bounds.getHeight() * i));
        }
    }

    private static void drawLogo(Graphics2D graphics, QrCodeConfig qrCodeConfig) {
        LogoConfig logoConfig = qrCodeConfig.getLogoConfig();
        try {
            Image src = ImageIO.read(logoConfig.getIs());
            int width = src.getWidth(null);
            int height = src.getHeight(null);
            if (logoConfig.isCompress()) {
                if (width > logoConfig.getWidth()) {
                    width = logoConfig.getWidth();
                }
                if (height > logoConfig.getHeight()) {
                    height = logoConfig.getHeight();
                }
                Image image = src.getScaledInstance(width, height,
                        Image.SCALE_SMOOTH);
                BufferedImage tag = new BufferedImage(width, height,
                        BufferedImage.TYPE_INT_RGB);
                Graphics g = tag.getGraphics();
                // 绘制缩小后的图
                g.drawImage(image, 0, 0, null);
                g.dispose();
                src = image;
            }
            // 插入LOGO
            int x = qrCodeConfig.getBorderLeft() + (qrCodeConfig.getWidth() - width) / 2;
            // 特殊处理高度
            int y = qrCodeConfig.getBorderTop() + (qrCodeConfig.getHeight() - width) / 2;
            graphics.drawImage(src, x, y, width, height, null);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            InputStream is = logoConfig.getIs();
            if (Objects.nonNull(is)) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private static int getHeight(QrCodeConfig qrCodeConfig) {
        WordConfig wordConfig = qrCodeConfig.getWordConfig();
        if (Objects.isNull(wordConfig) || Lists.isEmpty(wordConfig.getWords())) {
            return qrCodeConfig.getHeight() + qrCodeConfig.getBorderTop() + qrCodeConfig.getBorderBottom();
        }
        return qrCodeConfig.getHeight() + qrCodeConfig.getBorderTop() + qrCodeConfig.getBorderBottom() + qrCodeConfig.getBottomHeight();
    }

    private static int getWidth(QrCodeConfig qrCodeConfig) {
        return qrCodeConfig.getWidth() + qrCodeConfig.getBorderLeft() + qrCodeConfig.getBorderRight();
    }

}
