package com.itxiaoer.commons.security.wx.web;

import com.itxiaoer.commons.core.page.Response;
import com.itxiaoer.commons.jwt.JwtToken;
import com.itxiaoer.commons.security.AuthenticationUtils;
import com.itxiaoer.commons.security.JwtTokenContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author : liuyk
 */
@Slf4j
@RestController
@SuppressWarnings("unused")
public class WxLoginController {

    @Resource
    private JwtTokenContext jwtTokenContext;

    @GetMapping("/token")
    public Response<JwtToken> getToken() {
        try {
            return Response.ok(jwtTokenContext.build(AuthenticationUtils.getUser()));
        } catch (Exception e) {
            return Response.ok(null);
        }
    }

}
