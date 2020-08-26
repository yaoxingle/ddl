package com.tellhow.dcloud.database.ddl.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

/**
 * @Description TODO
 * @Date 2020/5/25 11:31
 * @Created by YaoXingLe
 */
@Data
@AllArgsConstructor
@ToString
public class Column {
    private String tableName;
    private String name;
    private String type;
    private Integer size;
    private Integer decimalDigits;
    private boolean nullable;


    public String toSql() {
        StringBuffer columnBuffer = new StringBuffer();
        columnBuffer.append("\t\"").append(name).append("\"");
        columnBuffer.append("\t").append(getMappingType(type))
                .append(getColumnSize())
                .append(getNullable());
        return columnBuffer.toString();
    }

    private String getNullable() {
        if (nullable == false) {
            return "\tNOT NULL";
        }
        return "";
    }

    private String getMappingType(String type){
        if("MONEY".equalsIgnoreCase(type)){
            return "DECIMAL";
        }
        return type;
    }

    private String getColumnSize() {
        if ("VARCHAR".equalsIgnoreCase(type)) {
            return "(" + size + ")";
        } else if ("VARCHAR2".equalsIgnoreCase(type)) {
            return "(" + size + ")";
        } else if ("DECIMAL".equalsIgnoreCase(type)) {
            return "(" +size + "," + decimalDigits + ")";
        } else if ("TIMESTAMP".equalsIgnoreCase(type)) {
            return "(" + decimalDigits + ")";
        } else if("MONEY".equalsIgnoreCase(type)){
            return "(19,4)";
        } else if("CHAR".equalsIgnoreCase(type)) {
            return "(" + size + ")";
        }
        return "";
    }

}
