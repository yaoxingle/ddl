package com.tellhow.dcloud.database.ddl.vo;

/**
 * @Description TODO
 * @Date 2020/5/26 13:47
 * @Created by YaoXingLe
 */
public abstract class  AbstarctConstraint {
    private static int startIndex = 10000;
    public String getIndexName(){
        String indexName = "SG_INDEX" + startIndex;
        startIndex ++;
        return indexName;
    }

    public abstract String toSql(Table table);
}
