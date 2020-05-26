package com.tellhow.dcloud.database.ddl.util;

import com.tellhow.dcloud.database.ddl.enums.DatabaseType;
import com.tellhow.dcloud.database.ddl.vo.Table;

/**
 * @Description TODO
 * @Date 2020/5/25 16:48
 * @Created by YaoXingLe
 */
public class TableUtil {
    public static String getFullTableName(String catalog, String schema, String table, DatabaseType type){
        if(type == DatabaseType.DM6){
            return "\"" + catalog + "\".\"" + schema + "\".\"" + table + "\"";
        }
        else if(type == DatabaseType.DM7){
            return "\"" + schema + "\".\"" + table + "\"";
        }
        else if(type == DatabaseType.GBASE){
            return "\"" + catalog + "\":\"" + schema + "\".\"" + table + "\"";
        }
        return table;
    }
}
