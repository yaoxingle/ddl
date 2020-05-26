package com.tellhow.dcloud.database.ddl.vo;

import com.tellhow.dcloud.database.ddl.util.TableUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

/**
 * @Description TODO
 * @Date 2020/5/25 14:34
 * @Created by YaoXingLe
 */
@Data
@AllArgsConstructor
@ToString
public class ForeignKey extends AbstarctConstraint{
    private String column;
    private String name;
    private String referencesTable;
    private String referencesColumn;


    public String toSql(Table table) {
       /* ALTER TABLE "SG_DATACENTER"."SG_ORG_EMPLOYEE_B"
        ADD CONSTRAINT "INDEX33557531"
        FOREIGN KEY("NATION")
        REFERENCES "SG_DATACENTER"."SG_DIC_NATION"("CODE");
        */
        StringBuffer sb = new StringBuffer();
        String fullTableName = TableUtil.getFullTableName(table.getCatalog(), table.getSchema(), table.getName(), table.getDatabaseType());
        String fullReferencesTable = TableUtil.getFullTableName(table.getCatalog(), table.getSchema(), referencesTable, table.getDatabaseType());
        sb.append("ALTER TABLE ").append(fullTableName)
                .append("\r\n ADD CONSTRAINT \"").append(getIndexName())
                .append("\" FOREIGN KEY(\"").append(column).append("\")")
                .append(" REFERENCES ").append(fullReferencesTable).append("(\"").append(referencesColumn).append("\")");
        return sb.toString();
    }



}
