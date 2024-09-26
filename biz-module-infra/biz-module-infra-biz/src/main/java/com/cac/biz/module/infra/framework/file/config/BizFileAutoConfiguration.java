package com.cac.biz.module.infra.framework.file.config;

import com.cac.biz.module.infra.framework.file.core.client.FileClientFactory;
import com.cac.biz.module.infra.framework.file.core.client.FileClientFactoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 文件配置类
 *
 * @author 芋道源码
 */
@Configuration(proxyBeanMethods = false)
public class BizFileAutoConfiguration {

    @Bean
    public FileClientFactory fileClientFactory() {
        return new FileClientFactoryImpl();
    }

}
