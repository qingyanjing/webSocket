package com.huangpeng.messages.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @Date 2020-05-28
 * @author huangpeng
 * 这个配置类检测带注解@ServerEndpoint的bean并注册它们
 */
@Configuration
public class WebSocketConfig {
    /**
     * 给spring容器注入这个ServerEndpointExporter对象
     * 相当于xml：
     * <beans>
     * <bean id="serverEndpointExporter" class="org.springframework.web.socket.server.standard.ServerEndpointExporter"/>
     * </beans>
     * <p>
     * 检测所有带有@serverEndpoint注解的bean并注册他们。
     * @return
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        System.out.println(WebSocketConfig.class.getMethods().toString() + "已被注入");
        return new ServerEndpointExporter();
    }
}
