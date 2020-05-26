package com.tellhow.dcloud.database.ddl.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @Description TODO
 * @Date 2020/5/25 10:03
 * @Created by YaoXingLe
 */
@Configuration
@EnableConfigurationProperties(TableProperties.class)
public class TableAutoConfiguration {
    @Autowired
    TableProperties tableProperties;

    @ConditionalOnMissingBean
    @Bean
    public TablePropertiesWrapper tablePropertiesWrapper(){
        return new TablePropertiesWrapper(tableProperties);
    }
}
