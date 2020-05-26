package com.tellhow.dcloud.database.ddl.service;

import com.tellhow.dcloud.database.ddl.vo.Column;
import com.tellhow.dcloud.database.ddl.vo.ForeignKey;
import com.tellhow.dcloud.database.ddl.vo.PrimaryKey;
import com.tellhow.dcloud.database.ddl.vo.Table;

import java.sql.Connection;
import java.util.List;
import java.util.Set;

/**
 * @Description TODO
 * @Date 2020/5/25 11:29
 * @Created by YaoXingLe
 */
public interface MetaDataService {
    List<Table> findTables(String catalog, String schemaPattern, String tableNamePattern, String types[]) throws Exception;
    List<Column> findColumn(String catalog, String schemaPattern,String tableNamePattern, String columnNamePattern) throws Exception;
    Set<ForeignKey> findForeignKeys(Connection con, String catalog, String schemaPattern, String table) throws Exception;
    PrimaryKey findPrimaryKey(Connection con, String catalog, String schemaPattern, String table) throws Exception;
}
