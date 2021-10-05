package com.joezhou.app.config;

import com.alibaba.druid.pool.DruidDataSource;
import io.seata.rm.datasource.DataSourceProxy;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author JoeZhou
 */
@Configuration
public class DataSourceProxyConfig {

    /**
     * 配置阿里巴巴druid连接池的数据源
     *
     * @return 阿里巴巴druid连接池实例
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DruidDataSource druidDataSource() {
        return new DruidDataSource();
    }

    /**
     * 配置数据源代理类
     *
     * @param druidDataSource 阿里巴巴druid连接池实例
     * @return 数据源代理类实例
     */
    @Bean
    @Primary
    public DataSourceProxy dataSourceProxy(DruidDataSource druidDataSource) {
        return new DataSourceProxy(druidDataSource);
    }
}
