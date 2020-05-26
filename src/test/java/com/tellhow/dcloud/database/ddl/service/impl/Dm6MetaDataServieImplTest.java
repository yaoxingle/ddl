package com.tellhow.dcloud.database.ddl.service.impl;

import cn.hutool.core.io.FileUtil;
import com.tellhow.dcloud.database.ddl.config.TablePropertiesWrapper;
import com.tellhow.dcloud.database.ddl.service.MetaDataService;
import com.tellhow.dcloud.database.ddl.service.PersistentService;
import com.tellhow.dcloud.database.ddl.vo.Column;
import com.tellhow.dcloud.database.ddl.vo.ForeignKey;
import com.tellhow.dcloud.database.ddl.vo.PrimaryKey;
import com.tellhow.dcloud.database.ddl.vo.Table;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

import javax.sql.DataSource;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @Description TODO
 * @Date 2020/5/25 13:42
 * @Created by YaoXingLe
 */
@SpringBootTest
@Slf4j
class Dm6MetaDataServieImplTest {

    @Autowired
    MetaDataService metaDataService;

    @Autowired
    DataSource dataSource;

    @Autowired
    PersistentService persistentService;

    @Autowired
    static TablePropertiesWrapper tablePropertiesWrapper;

    static String catalog = "DC_CLOUD";

    static String schemaPattern = "SG_DATACENTER";

    //static String tableNamePattern = "SG_SYS_POINT_B";
    static String tableNamePattern = "SG_AUT_CRAC_B";


    static String[] types = null;

    @BeforeTestMethod
    public static void init() {
        log.info("BeforeAll init.");
        catalog = tablePropertiesWrapper.getValue().getSourceCatalog();
        schemaPattern = tablePropertiesWrapper.getValue().getSourceSchema();
    }

    @Test
    void findTables() throws Exception{
        List<Table> tables = metaDataService.findTables(catalog, schemaPattern, tableNamePattern, types);
        assertTrue(tables.size() > 0);
        for (Table table : tables) {
            log.info(table.toString());
        }
    }

    @Test
    void findColumn() throws Exception{
        List<Column> columns = metaDataService.findColumn(catalog, schemaPattern, tableNamePattern, null);
        assertTrue(columns.size() > 0);
        for (Column column : columns) {
            log.info(column.toString());
        }
        List<Column> columns1 = metaDataService.findColumn(catalog, schemaPattern, "SG_CON_HVDCSYS_B", null);
        assertTrue(columns1.size() > 0);
        for (Column column : columns1) {
            log.info(column.toString());
        }

    }

    @Test
    void findPrimaryKeys() throws Exception {
        PrimaryKey primaryKey = metaDataService.findPrimaryKey(dataSource.getConnection(), catalog, schemaPattern, tableNamePattern);
        assertTrue(primaryKey != null);
        log.info(primaryKey.toString());
    }

    @Test
    void findForeignKeys() throws Exception {
        Set<ForeignKey> foreignKeys = metaDataService.findForeignKeys(dataSource.getConnection(), catalog, schemaPattern, tableNamePattern);
        assertTrue(foreignKeys.size() > 0);
        for (ForeignKey foreignKey : foreignKeys) {
            log.info(foreignKey.toString());
        }
    }

    @Test
    void generateOneTableAllSql() throws Exception{
        List<Table> tables = metaDataService.findTables(catalog, schemaPattern, tableNamePattern, types);
        assertTrue(tables.size() > 0);
        Table table = tables.get(0);

        List<Column> columns = metaDataService.findColumn(catalog, schemaPattern, tableNamePattern, null);
        assertTrue(columns.size() > 0);
        table.setColumns(columns);

        PrimaryKey primaryKey = null;
        try {
            primaryKey = metaDataService.findPrimaryKey(dataSource.getConnection(), catalog, schemaPattern, table.getName());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        table.setPrimaryKey(primaryKey);

        try {
            Set<ForeignKey> foreignKeys = metaDataService.findForeignKeys(dataSource.getConnection(), catalog, schemaPattern, table.getName());
            table.setForeignKeys(foreignKeys);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String createSql = table.generateCreateSql();
        String pkSql = table.generatePkSql();
        String fkSql = table.generateFkSql();
        log.info("generateCreateSql={}", createSql);
        log.info("generatePkSql={}", pkSql);
        log.info("generateFkSql={}", fkSql);

        persistentService.append(createSql, "create.sql");
        persistentService.append(pkSql, "pk.sql");
        persistentService.append(pkSql, "fk.sql");
    }

    @Test
    void generateAllTableAllSql() throws Exception {
        long beginTime = System.currentTimeMillis();
        List<Table> tables = metaDataService.findTables(catalog, schemaPattern, null, null);
        Connection con = dataSource.getConnection();
        int total = tables.size();
        int index = 1;

        File dirFile = new File(FilePersistentServiceImpl.directory);
        FileUtil.del(dirFile);

        for (Table table : tables) {
            List<Column> columns = metaDataService.findColumn(catalog, schemaPattern, table.getName(), null);
            table.setColumns(columns);

            PrimaryKey primaryKey = metaDataService.findPrimaryKey(con, catalog, schemaPattern, table.getName());
            table.setPrimaryKey(primaryKey);

            Set<ForeignKey> foreignKeys = metaDataService.findForeignKeys(con, catalog, schemaPattern, table.getName());
            table.setForeignKeys(foreignKeys);

            String createSql = table.generateCreateSql();
            String pkSql = table.generatePkSql();
            String fkSql = table.generateFkSql();
            //log.info("generateCreateSql={}",createSql);
            //log.info("generatePkSql={}",pkSql);
            //log.info("generateFkSql={}",fkSql);
            persistentService.append(createSql, "create.sql");
            persistentService.append(pkSql, "pk.sql");
            persistentService.append(fkSql, "fk.sql");
            log.info("进度={}/{},表={}语句,生成完成.", index, total, table.getName());
            index++;
        }
        persistentService.flush();
        long endTime = System.currentTimeMillis();
        log.info("所有表语句,生成完成,耗时={}毫秒.", endTime - beginTime);
    }
}

@Slf4j
class LogThread extends Thread {

    public LogThread() {
        System.out.print("处理生成中,请耐心等待..");
    }

    @Override
    public void run() {
        while (!this.isInterrupted()) {
            System.out.print("..");
            try {
                Thread.currentThread().sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}