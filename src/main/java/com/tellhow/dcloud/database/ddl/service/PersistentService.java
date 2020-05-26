package com.tellhow.dcloud.database.ddl.service;

/**
 * @Description TODO
 * @Date 2020/5/25 20:03
 * @Created by YaoXingLe
 */
public interface PersistentService {
    boolean append(Object obj, String fileName);
    void flush();
}
