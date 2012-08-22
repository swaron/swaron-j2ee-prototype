package org.app.framework.paging;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class PagingAssembler {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    ObjectMapper objectMapper;

    public PagingParam toPagingParam(PagingForm form) {
        PagingParam param = new PagingParam();
        param.setStart(form.getStart());
        param.setLimit(form.getLimit());
        LinkedHashMap<String, String> sortParams = readConfigListFromJson(form.getSort(), "property", "direction");
        param.setSort(sortParams);
        LinkedHashMap<String, String> filterParams = readConfigListFromJson(form.getFilter(), "property", "value");
        param.setFilter(filterParams);
        return param;
    }

    private LinkedHashMap<String, String> readConfigListFromJson(String json, String keyProp, String valueProp) {
        return readConfigListFromJson(json, keyProp, valueProp, true);
    }

    /**
     * turn filter parameters or sort parameters into a map 
     * @param json
     * @param keyProp
     * @param valueProp
     * @return
     */
    private LinkedHashMap<String, String> readConfigListFromJson(String json, String keyProp, String valueProp, boolean trimValue) {
        if(json == null){
            return null;
        }
        try {
            TypeReference<List<Map<String, String>>> ref = new TypeReference<List<Map<String, String>>>() {
            };
            List<Map<String, String>> list = objectMapper.readValue(json, ref);
            LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
            for (Map<String, String> map : list) {
                String prop =  map.get(keyProp);
                String value =  map.get(valueProp);
                if(trimValue){
                    value = StringUtils.trim(value);
                }
                params.put(prop, value);
            }
            return params;
        } catch (IOException e) {
            logger.info("failed to deserialize paging config of string " + json, e);
            return null;
        }
    }
}
