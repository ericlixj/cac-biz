package com.cac.biz.framework.websocket.core.security;

import com.cac.biz.framework.security.config.AuthorizeRequestsCustomizer;
import com.cac.biz.framework.websocket.config.WebSocketProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;

/**
 * WebSocket 的权限自定义
 *
 * @author 芋道源码
 */
@RequiredArgsConstructor
public class WebSocketAuthorizeRequestsCustomizer extends AuthorizeRequestsCustomizer {

    private final WebSocketProperties webSocketProperties;

    @Override
    public void customize(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry) {
        registry.antMatchers(webSocketProperties.getPath()).permitAll();
    }

}
