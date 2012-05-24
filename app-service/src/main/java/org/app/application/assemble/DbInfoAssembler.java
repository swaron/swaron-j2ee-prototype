package org.app.application.assemble;

import java.util.Properties;

import org.app.repo.jpa.model.DbInfo;

public class DbInfoAssembler {
    public static Properties createProperties(DbInfo dbInfo) {
        Properties properties = new Properties();
        properties.put("driverClassName", dbInfo.getDriverClass());
        properties.put("url", dbInfo.getUrl());
        properties.put("username", dbInfo.getDbUser());
        properties.put("password", dbInfo.getDbPasswd());
        return properties;
    }
}
