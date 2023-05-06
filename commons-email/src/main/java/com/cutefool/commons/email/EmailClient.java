/*
 *
 */
package com.cutefool.commons.email;

import lombok.extern.slf4j.Slf4j;
import com.cutefool.commons.core.util.Lists;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.List;

/**
 * 邮件发送
 *
 * @author 271007729@qq.com
 * @date 2019-09-06 04:40
 */
@Slf4j
@Component
@SuppressWarnings({"unused", "WeakerAccess"})
public class EmailClient {

    @Resource
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String userName;


    public boolean send(String subject, String content, String[] receivers) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSentDate(new Date());
        message.setSubject(subject);
        message.setText(content);
        message.setTo(receivers);
        message.setFrom(userName);
        javaMailSender.send(new SimpleMailMessage[]{message});
        return true;
    }

    public boolean sendHtml(String subject, String content, String[] receivers) {
        return sendHtml(subject, content, receivers, null);
    }


    public boolean sendHtml(String subject, String content, String[] receivers, List<Img> imgList) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            //true表示需要创建一个multipart message
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(userName);
            helper.setTo(receivers);
            helper.setSubject(subject);
            helper.setText(content, true);
            for (Img img : Lists.empty(imgList)) {
                helper.addInline(img.getId(), img.getResource());
            }
            javaMailSender.send(message);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }
}
