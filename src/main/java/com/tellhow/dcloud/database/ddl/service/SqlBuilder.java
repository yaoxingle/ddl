package com.tellhow.dcloud.database.ddl.service;

import com.tellhow.dcloud.database.ddl.vo.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @Description TODO
 * @Date 2020/5/26 11:29
 * @Created by YaoXingLe
 */
@Service
public class SqlBuilder {
    @Autowired
    PersistentService persistentService;

    public void flush(){
        persistentService.flush();
    }

    public boolean create(Table table) {
        String createSql = table.generateCreateSql();
        String pkSql = table.generatePkSql();
        String fkSql = table.generateFkSql();
        //log.info("generateCreateSql={}",createSql);
        //log.info("generatePkSql={}",pkSql);
        //log.info("generateFkSql={}",fkSql);
        persistentService.append(createSql, "create.sql");
        persistentService.append(pkSql, "pk.sql");
        persistentService.append(fkSql, "fk.sql");


        return true;
    }
}
