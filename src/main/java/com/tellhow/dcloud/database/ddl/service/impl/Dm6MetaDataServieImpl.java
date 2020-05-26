package com.tellhow.dcloud.database.ddl.service.impl;

import com.tellhow.dcloud.database.ddl.config.TablePropertiesWrapper;
import com.tellhow.dcloud.database.ddl.enums.DatabaseType;
import com.tellhow.dcloud.database.ddl.service.MetaDataService;
import com.tellhow.dcloud.database.ddl.util.SpringUtil;
import com.tellhow.dcloud.database.ddl.vo.Column;
import com.tellhow.dcloud.database.ddl.vo.ForeignKey;
import com.tellhow.dcloud.database.ddl.vo.PrimaryKey;
import com.tellhow.dcloud.database.ddl.vo.Table;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Description TODO
 * @Date 2020/5/25 11:29
 * @Created by YaoXingLe
 */
@Component
@Slf4j
public class Dm6MetaDataServieImpl implements MetaDataService{
    @Autowired
    DataSource dataSource;

    @Override
    public List<Table> findTables(String catalog, String schemaPattern, String tableNamePattern, String[] types) throws Exception {
        TablePropertiesWrapper tablePropertiesWrapper = SpringUtil.getBean(TablePropertiesWrapper.class);
        Connection con = null;
        ResultSet rs = null;
        List<Table> list = new ArrayList<>();
        try {
            con = dataSource.getConnection();
            rs = con.getMetaData().getTables(catalog, schemaPattern, tableNamePattern, types);
            while (rs.next()) {
                //printRsColumn(rs);
                StringBuilder sb = new StringBuilder();
                String tableName = rs.getString("TABLE_NAME");
                String tableType = rs.getString("TABLE_TYPE");
                list.add(new Table(tablePropertiesWrapper.getValue().getTargetCatalog(),tablePropertiesWrapper.getValue().getTargetSchema(),
                        tableName,tableType,null,null,null, tablePropertiesWrapper.getValue().getTargetType()));
            }
        } catch (Exception e) {
            log.error("findTables,catalog={},schemaPattern={},tableNamePattern={},types={}发生异常.", catalog, schemaPattern, tableNamePattern, types, e);
            throw e;
        } finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeConnection(con);
        }
        return list;
    }

    private void printRsColumn(ResultSet rs) throws SQLException {
        for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
            log.info("columnName={}", rs.getMetaData().getColumnName(i + 1));
        }
    }

    @Override
    public Set<ForeignKey> findForeignKeys(Connection con, String catalog, String schemaPattern, String table) throws Exception {
        ResultSet rs = null;
        Set<ForeignKey> fkList = new HashSet<>();
        try {
            rs = con.getMetaData().getImportedKeys(catalog, schemaPattern, table);
            while (rs.next()) {
//               columnName=PKTABLE_CAT
//               columnName=PKTABLE_SCHEM
//               columnName=PKTABLE_NAME
//               columnName=PKCOLUMN_NAME
//               columnName=FKTABLE_CAT
//               columnName=FKTABLE_SCHEM
//               columnName=FKTABLE_NAME
//               columnName=FKCOLUMN_NAME
//               columnName=KEY_SEQ
//               columnName=UPDATE_RULE
//               columnName=DELETE_RULE
//               columnName=FK_NAME
//               columnName=PK_NAME
//               columnName=DEFERRABILITY
//                log.info(rs.getString("FKTABLE_SCHEM") + "-" + rs.getString("FKTABLE_NAME"));
                fkList.add(new ForeignKey(rs.getString("FKCOLUMN_NAME"), rs.getString("PK_NAME"), rs.getString("PKTABLE_NAME"), rs.getString("PKCOLUMN_NAME")));
            }
        } catch (Exception e) {
            log.error("findForeignKeys,table={}发生异常.", table, e);
            throw e;
        } finally {
            JdbcUtils.closeResultSet(rs);
        }
        return fkList;
    }

    @Override
    public PrimaryKey findPrimaryKey(Connection con, String catalog, String schemaPattern, String table) throws Exception {
        ResultSet rs = null;
        PrimaryKey primaryKey = new PrimaryKey();
        try {
            rs = con.getMetaData().getPrimaryKeys(catalog, schemaPattern, table);
            while (rs.next()) {
                //printRsColumn(rs);
                primaryKey.addKey(rs.getString("COLUMN_NAME"), rs.getString("PK_NAME"));
            }
        } catch (Exception e) {
            log.error("findPrimaryKeys,table={}发生异常.", table, e);
            throw e;
        } finally {
            JdbcUtils.closeResultSet(rs);
        }
        return primaryKey;
    }


    @Override
    public List<Column> findColumn(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern) throws Exception {
        Connection con = null;
        ResultSet rs = null;
        List<Column> columnList = new ArrayList<>();
        try {
            con = dataSource.getConnection();
            rs = con.getMetaData().getColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern);
            while (rs.next()) {
                /**
                 * ABLE_CAT:DC_CLOUD
                 * TABLE_SCHEM:SG_DATACENTER
                 * TABLE_NAME:    2型欠励限制模型参数  数据报表
                 * COLUMN_NAME:ID
                 * DATA_TYPE:12
                 * TYPE_NAME:VARCHAR
                 * COLUMN_SIZE:200
                 * BUFFER_LENGTH:200
                 * DECIMAL_DIGITS:null
                 * NUM_PREC_RADIX:10
                 * NULLABLE:1
                 * REMARKS:null
                 * COLUMN_DEF:null
                 * SQL_DATA_TYPE:0
                 * SQL_DATETIME_SUB:0
                 * CHAR_OCTET_LENGTH:200
                 * ORDINAL_POSITION:1
                 * IS_NULLABLE:YES
                 * SCOPE_CATLOG:null
                 * SCOPE_SCHEMA:null
                 * SCOPE_TABLE:null
                 * SOURCE_DATA_TYPE:0
                 */
//                for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
//                    System.out.println(rs.getMetaData().getColumnName(i + 1) + ":" + rs.getString(i + 1));
//                }
                String tableName = rs.getString("TABLE_NAME");
                String columnName = rs.getString("COLUMN_NAME");
                String typeName = rs.getString("TYPE_NAME");
                Integer size = rs.getInt("COLUMN_SIZE");
                String nullable = rs.getString("NULLABLE");
                Integer DECIMAL_DIGITS = rs.getInt("DECIMAL_DIGITS");
                //}
                columnList.add(new Column(tableName, columnName, typeName, size, DECIMAL_DIGITS, "1".equalsIgnoreCase(nullable) ? true : false));
            }
        } catch (Exception e) {
            log.error("findColumn,table={},column={}发生异常.", tableNamePattern, columnNamePattern, e);
            throw e;
        } finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeConnection(con);
        }
        return columnList;
    }
}
