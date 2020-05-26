package com.tellhow.dcloud.database.ddl.config;

import com.tellhow.dcloud.database.ddl.enums.DatabaseType;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Description TODO
 * @Date 2020/5/25 17:30
 * @Created by YaoXingLe
 */
@ConfigurationProperties(prefix = "ddl.table")
@Data
public class TableProperties {
    private String sourceCatalog;
    private String sourceSchema;
    private String targetCatalog;
    private String targetSchema;
    private DatabaseType targetType;
}
