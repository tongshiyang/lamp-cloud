package top.tangyh.lamp.generator.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.tangyh.basic.boot.config.BaseConfig;
import top.tangyh.basic.log.event.SysLogListener;
import top.tangyh.lamp.oauth.facade.LogFacade;

/**
 * 在线代码生成器模块-Web配置
 *
 * @author zuihou
 * @date 2022-02-28
 */
@Configuration
public class GeneratorWebConfiguration extends BaseConfig {

    /**
     * lamp.log.enabled = true 并且 lamp.log.type=DB时实例该类
     */
    @Bean
    @ConditionalOnExpression("${lamp.log.enabled:true} && 'DB'.equals('${lamp.log.type:LOGGER}')")
    public SysLogListener sysLogListener(LogFacade logApi) {
        return new SysLogListener(logApi::save);
    }
}
