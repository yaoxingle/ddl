package com.tellhow.dcloud.database.ddl.vo;

import com.tellhow.dcloud.database.ddl.enums.DatabaseType;
import com.tellhow.dcloud.database.ddl.util.TableUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @Description TODO
 * @Date 2020/5/25 11:31
 * @Created by YaoXingLe
 */
@Data
@AllArgsConstructor
@ToString
public class Table {
    private String catalog;
    private String schema;
    private String name;
    private String type;
    List<Column> columns;
    PrimaryKey primaryKey;
    Set<ForeignKey> foreignKeys;
    private DatabaseType databaseType;

    public String generatePkSql() {
        return primaryKey.toSql(this);
    }

    public String generateCreateSql() {
        StringBuffer sb = new StringBuffer();
        sb.append("CREATE TABLE ").append(TableUtil.getFullTableName(catalog, schema, name, databaseType)).append("(");
        for (Column column : columns) {
            sb.append("\r\n").append(column.toSql()).append(",");
        }
        sb.deleteCharAt(sb.length()-1);
        sb.append("\r\n").append(");\r\n");
        return sb.toString();
    }

    public String generateFkSql() {
        StringBuffer sb = new StringBuffer();
        for (ForeignKey foreignKey : foreignKeys) {
            sb.append(foreignKey.toSql(this)).append(";\r\n");
        }
        return sb.toString();
    }
}
