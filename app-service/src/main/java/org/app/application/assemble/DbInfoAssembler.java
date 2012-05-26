package org.app.application.assemble;

import java.util.Properties;

import org.app.repo.jpa.model.DbInfo;
import org.springframework.stereotype.Service;

@Service
public class DbInfoAssembler {
    public Properties createProperties(DbInfo dbInfo) {
        Properties properties = new Properties();
        properties.put("driverClassName", dbInfo.getDriverClass());
        properties.put("url", dbInfo.getUrl());
        properties.put("username", dbInfo.getDbUser());
        properties.put("password", dbInfo.getDbPasswd());
        properties.put("maxWait", 10000);
        properties.put("removeAbandoned", true);
        return properties;
    }
    
    public String createKey(DbInfo dbInfo) {
        StringBuilder builder = new StringBuilder();
        builder.append(dbInfo.getDbInfoId());
        builder.append("_");
        builder.append(dbInfo.getUpdateTime().getTime());
        return builder.toString();
    }
}
