package com.tellhow.dcloud.database.ddl.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.io.FileUtil;
import com.tellhow.dcloud.database.ddl.config.TablePropertiesWrapper;
import com.tellhow.dcloud.database.ddl.service.impl.FilePersistentServiceImpl;
import com.tellhow.dcloud.database.ddl.vo.Column;
import com.tellhow.dcloud.database.ddl.vo.ForeignKey;
import com.tellhow.dcloud.database.ddl.vo.PrimaryKey;
import com.tellhow.dcloud.database.ddl.vo.Table;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.File;
import java.sql.Connection;
import java.util.List;
import java.util.Set;

/**
 * @Description TODO
 * @Date 2020/5/26 11:24
 * @Created by YaoXingLe
 */
@Service
@Slf4j
public class TablesBuilder {
    @Autowired
    MetaDataService metaDataService;

    @Autowired
    PersistentService persistentService;

    @Autowired
    DataSource dataSource;

    @Autowired
    TablePropertiesWrapper tablePropertiesWrapper;

    public List<Table> create() throws Exception {
        TimeInterval timer = DateUtil.timer();
        String catalog = tablePropertiesWrapper.getValue().getSourceCatalog();
        String schemaPattern = tablePropertiesWrapper.getValue().getSourceSchema();
        List<Table> tables = metaDataService.findTables(catalog, schemaPattern, null, null);
        Connection con = dataSource.getConnection();
        File dirFile = new File(FilePersistentServiceImpl.directory);
        FileUtil.del(dirFile);
        for (Table table : tables) {
            List<Column> columns = metaDataService.findColumn(catalog, schemaPattern, table.getName(), null);
            table.setColumns(columns);

            PrimaryKey primaryKey = metaDataService.findPrimaryKey(con, catalog, schemaPattern, table.getName());
            table.setPrimaryKey(primaryKey);

            Set<ForeignKey> foreignKeys = metaDataService.findForeignKeys(con, catalog, schemaPattern, table.getName());
            table.setForeignKeys(foreignKeys);
        }
        long endTime = System.currentTimeMillis();
        log.info("所有表对象生成完成,耗时={}毫秒.", timer.interval());
        JdbcUtils.closeConnection(con);
        return tables;
    }
}
