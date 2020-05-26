package com.tellhow.dcloud.database.ddl.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileAppender;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.tellhow.dcloud.database.ddl.service.PersistentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @Description TODO
 * @Date 2020/5/25 20:04
 * @Created by YaoXingLe
 */
@Component
@Slf4j
public class FilePersistentServiceImpl implements PersistentService {
    public static final String directory = System.getProperty("user.dir") + File.separator + "sqls";

    static {
        log.info("sql文件存储路径={}",directory);
    }

    private static Map<String,FileAppender> appenders = new HashMap<>();

    private FileAppender getFileAppender(String fileName){
        if(!appenders.containsKey(fileName)){
            File dirFile = new File(directory);
            FileUtil.mkdir(dirFile);
            String filePath = directory + File.separator + fileName;
            File file = new File(filePath);
            FileAppender appender = new FileAppender(file, 100, true);
            appenders.put(fileName,appender);
            return appender;
        }
        return appenders.get(fileName);
    }

    @Override
    public void flush(){
        Iterator<Map.Entry<String, FileAppender>> iterator = appenders.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, FileAppender> next =  iterator.next();
            next.getValue().flush();
        }
        appenders.clear();
    }

    @Override
    public boolean append(Object obj, String fileName) {
        if(ObjectUtil.isEmpty(obj) || StrUtil.isEmpty(obj.toString())) {
            return true;
        }
        try {
            FileAppender appender = getFileAppender(fileName);
            appender.append(obj.toString());
            return true;
        } catch (Exception ex) {
            log.error("写入文件={}发生异常", fileName, ex);
            return false;
        }finally {

        }
    }
}
