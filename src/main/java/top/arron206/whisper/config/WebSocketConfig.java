package top.arron206.whisper.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.*;
import org.springframework.web.socket.config.annotation.*;


@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/socket/server").setAllowedOrigins("*").withSockJS(); //设置连接url并且设置跨域
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/chat"); //设置订阅的url
        config.setApplicationDestinationPrefixes("/api");   //设置访问url前缀
    }
}

