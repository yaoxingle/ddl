package com.tellhow.dcloud.database.ddl.vo;

import cn.hutool.core.util.ObjectUtil;
import com.tellhow.dcloud.database.ddl.util.TableUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.util.ObjectUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * @Description TODO
 * @Date 2020/5/25 14:34
 * @Created by YaoXingLe
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PrimaryKey extends AbstarctConstraint{
    private Set<String> columns = new HashSet<>();
    private Set<String> names = new HashSet<>();

    public void addKey(String column,String name){
        columns.add(column);
        names.add(name);
    }

    public String toSql(Table table){
        if (!ObjectUtil.isEmpty(columns)) {
            StringBuffer sb = new StringBuffer();
            String fullTableName = TableUtil.getFullTableName(table.getCatalog(), table.getSchema(), table.getName(), table.getDatabaseType());
            sb.append("ALTER TABLE ").append(fullTableName);
            sb.append("\r\n ADD CONSTRAINT \"").append(getIndexName()).append("\" PRIMARY KEY(");
            for (String primaryKeyColumn : columns) {
                sb.append("\"").append(primaryKeyColumn).append("\",");
            }
            sb.deleteCharAt(sb.length()-1);
            sb.append(");\r\n");
            return sb.toString();
        }
        return "";
    }
}
