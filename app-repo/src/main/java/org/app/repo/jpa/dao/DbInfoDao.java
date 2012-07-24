package org.app.repo.jpa.dao;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.app.repo.jpa.po.DbInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.XStreamException;

@Repository
public class DbInfoDao extends AbstractDaoSupport {
//
//    @Value("${dbinfo.setting_location}")
//    private Resource settingLocation;
//
//    HashMap<String, DbInfo> settings;
//
//    @PostConstruct
//    public void init() {
//        loadSetting();
//    }
//
//    public Collection<DbInfo> listDataSource() {
//        return settings.values();
//    }
//
//    private void loadSetting() {
//        logger.debug("loading dbinfo setting from: {}", settingLocation);
//        XStream xStream = new XStream();
//        try {
//            settings = new HashMap<String, DbInfo>();
//            Collection<DbInfo> list = (Collection<DbInfo>) xStream.fromXML(settingLocation.getFile());
//            for (DbInfo dataSourceSetting : list) {
//                settings.put(dataSourceSetting.getName(), dataSourceSetting);
//            }
//        } catch (IOException e) {
//            logger.warn("failed to load db info setting from file : {}", settingLocation);
//        }catch (XStreamException e) {
//            logger.warn("failed to parse db info setting from file : {}", settingLocation);
//        }
//    }
//
//    private void saveSetting() {
//        logger.debug("saving dbinfo setting to: {}", settingLocation);
//        XStream xStream = new XStream();
//        try {
//            String xml = xStream.toXML(settings.values());
//            FileUtils.writeStringToFile(settingLocation.getFile(), xml);
//        } catch (IOException e) {
//            logger.warn("failed to save db info setting to file : {}", settingLocation);
//        }
//    }
//
//    public void addDataSource(DbInfo setting) {
//        settings.put(setting.getName(), setting);
//        saveSetting();
//    }
//
//    public void removeDataSource(String name) {
//        if (settings.containsKey(name)) {
//            settings.remove(name);
//            saveSetting();
//        }
//
//    }
//    public void updateDataSource(String name,DbInfo setting) {
//        settings.put(name, setting);
//        saveSetting();
//    }
//
//    public DbInfo findDataSourceByName(String name) {
//        return settings.get(name);
//    }
}
