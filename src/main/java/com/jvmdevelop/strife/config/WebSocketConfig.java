package com.jvmdevelop.strife.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final WebRtcHandler webRtcHandler;

    public WebSocketConfig(WebRtcHandler webRtcHandler) {
        this.webRtcHandler = webRtcHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webRtcHandler, "/webrtc/{channel}/{user}").setAllowedOrigins("*");
    }
}