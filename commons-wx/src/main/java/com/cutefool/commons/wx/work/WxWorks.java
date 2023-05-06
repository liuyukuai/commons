/*
 *
 */
package com.cutefool.commons.wx.work;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import com.cutefool.commons.core.util.Strings;
import com.cutefool.commons.wx.WxConstants;
import com.cutefool.commons.wx.libs.TokenResponse;
import com.cutefool.commons.wx.libs.UserIdResponse;
import com.cutefool.commons.wx.libs.UserResponse;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.cutefool.commons.wx.work.WxWorkConstants.GET_TOKEN_URL;

/**
 * 企业微信相关常量
 *
 * @author 271007729@qq.com
 * @date 10/15/21 4:01 PM
 */
@Slf4j
@SuppressWarnings({"WeakerAccess", "unused"})
public class WxWorks {

    private static final String KEY = "token";

    @Resource
    private WxWorkProperties wxWorkProperties;

    /**
     * 缓存
     */
    private Cache<String, String> tokenCache = Caffeine.newBuilder().expireAfterAccess(1, TimeUnit.HOURS).build();


    public String getToken() {
        String token = tokenCache.getIfPresent(KEY);
        if (StringUtils.isNotBlank(token)) {
            return token;
        }
        TokenResponse response = WxConstants.execute(String.format(GET_TOKEN_URL, wxWorkProperties.getAppId(), wxWorkProperties.getSecret()), TokenResponse.class);
        if (Objects.nonNull(response)) {
            String accessToken = response.getAccessToken();
            if (StringUtils.isNotBlank(accessToken)) {
                tokenCache.put(KEY, accessToken);
            }
            return response.getAccessToken();
        }
        return "";
    }

    public String loadUserIdByCode(String code) {
        String uri = String.format(WxWorkConstants.GET_USER_ID_URL, this.getToken(), code);
        UserIdResponse response = WxConstants.execute(uri, UserIdResponse.class);
        if (Objects.nonNull(response)) {
            return response.getUserId();
        }
        return Strings.EMPTY;
    }

    public Optional<UserResponse> loadUserByCode(String code) {
        String userId = this.loadUserIdByCode(code);
        if (StringUtils.isNotBlank(userId)) {
            return this.loadUserByUserId(userId);
        }
        return Optional.empty();
    }


    public Optional<UserResponse> loadUserByUserId(String userId) {
        String uri = String.format(WxWorkConstants.GET_USER_INFO_URL, this.getToken(), userId);
        UserResponse userResponse = WxConstants.execute(uri, UserResponse.class);
        return Optional.ofNullable(userResponse);
    }
}
