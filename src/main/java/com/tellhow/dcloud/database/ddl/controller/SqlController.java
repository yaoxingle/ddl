package com.tellhow.dcloud.database.ddl.controller;

import com.tellhow.dcloud.database.ddl.service.SqlBuilder;
import com.tellhow.dcloud.database.ddl.service.TablesBuilder;
import com.tellhow.dcloud.database.ddl.vo.Table;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description TODO
 * @Date 2020/5/25 10:25
 * @Created by YaoXingLe
 */
@Slf4j
@RestController
@RequestMapping("/action/sql")
public class SqlController {

    @Autowired
    TablesBuilder tablesBuilder;

    @Autowired
    SqlBuilder sqlBuilder;

    @RequestMapping(path = "/create")
    public String createSql() throws Exception{
        try {
            log.info("生成表SQL文件-开始.");
            List<Table> tables = tablesBuilder.create();
            for (int i = 0; i < tables.size(); i++) {
                Table table = tables.get(i);
                sqlBuilder.create(table);
                log.info("进度={}/{},表={}语句，生成完成.", (i + 1), tables.size(), table.getName());
            }
            sqlBuilder.flush();
            log.info("生成表SQL文件-结束.");
        } catch (Exception ex) {
            log.error("发生异常.", ex);
            throw ex;
        }
        return "成功";
    }

    SqlController() {
        System.out.println("SqlController init");
    }
}
