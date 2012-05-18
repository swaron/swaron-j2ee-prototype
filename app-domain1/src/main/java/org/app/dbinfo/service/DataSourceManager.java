package org.app.dbinfo.service;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.app.dbinfo.vo.DataSourceSetting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.thoughtworks.xstream.XStream;

@Service
public class DataSourceManager {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${dbinfo.setting_location}")
    private Resource settingLocation;

    HashMap<String, DataSourceSetting> settings;

    @PostConstruct
    public void init() {
        loadSetting();
    }

    public Collection<DataSourceSetting> listDataSource() {
        return settings.values();
    }

    private void loadSetting() {
        logger.debug("loading dbinfo setting from: {}", settingLocation);
        XStream xStream = new XStream();
        try {
            Collection<DataSourceSetting> list = (Collection<DataSourceSetting>) xStream.fromXML(settingLocation
                    .getFile());
            HashMap<String, DataSourceSetting> map = new HashMap<String, DataSourceSetting>();
            for (DataSourceSetting dataSourceSetting : list) {
                map.put(dataSourceSetting.getName(), dataSourceSetting);
            }
            settings = map;
        } catch (IOException e) {
            logger.warn("failed to load db info setting from file : {}", settingLocation);
        }
    }

    private void saveSetting() {
        logger.debug("saving dbinfo setting to: {}", settingLocation);
        XStream xStream = new XStream();
        try {
            String xml = xStream.toXML(settings.values());
            FileUtils.writeStringToFile(settingLocation.getFile(), xml);
        } catch (IOException e) {
            logger.warn("failed to save db info setting to file : {}", settingLocation);
        }
    }

    public void addDataSource(DataSourceSetting setting) {
        settings.put(setting.getName(), setting);
        saveSetting();
    }

    public void removeDataSource(String name) {
        if (settings.containsKey(name)) {
            settings.remove(name);
            saveSetting();
        }

    }

    public DataSourceSetting findDataSourceByName(String name) {
        return settings.get(name);
    }
}
