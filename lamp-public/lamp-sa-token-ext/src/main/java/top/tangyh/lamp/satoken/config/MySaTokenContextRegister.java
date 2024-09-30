package top.tangyh.lamp.satoken.config;

import cn.dev33.satoken.context.SaTokenContext;
import cn.dev33.satoken.filter.SaPathCheckFilterForJakartaServlet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.tangyh.basic.constant.Constants;
import top.tangyh.lamp.common.properties.IgnoreProperties;
import top.tangyh.lamp.common.properties.SystemProperties;
import top.tangyh.lamp.satoken.spring.MySaTokenContextForSpringInJakartaServlet;

/**
 * 注册 Sa-Token 框架所需要的 Bean
 * @author tangyh
 * @since 2024/9/18 14:38
 */
@Slf4j
public class MySaTokenContextRegister {

    @Configuration
    @ConditionalOnProperty(prefix = Constants.PROJECT_PREFIX + ".webmvc", name = "header", havingValue = "true", matchIfMissing = true)
    public static class InnerConfig {
        public InnerConfig() {
            log.info("加载：{}", InnerConfig.class.getName());
        }

        @Bean
        @ConditionalOnClass
        public GlobalMvcConfigurer getGlobalMvcConfigurer(SystemProperties systemProperties, IgnoreProperties ignoreProperties) {
            return new GlobalMvcConfigurer(ignoreProperties, systemProperties);
        }

    }

    /**
     * 获取上下文处理器组件 (SpringBoot3 Jakarta Servlet 版)
     *
     * @return /
     */
    @Bean
    public SaTokenContext getSaTokenContextForSpringInJakartaServlet() {
        return new MySaTokenContextForSpringInJakartaServlet();
    }

    /**
     * 请求 path 校验过滤器
     *
     * @return /
     */
    @Bean
    public SaPathCheckFilterForJakartaServlet saPathCheckFilterForJakartaServlet() {
        return new SaPathCheckFilterForJakartaServlet();
    }
}
