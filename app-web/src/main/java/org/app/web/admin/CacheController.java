package org.app.web.admin;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.persistence.PersistenceUnit;

import net.sf.ehcache.Ehcache;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.SessionFactory;
import org.hibernate.ejb.HibernateEntityManagerFactory;
import org.hibernate.stat.Statistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@RequestMapping("/admin")
public class CacheController {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    @PersistenceUnit(unitName = "app-repo")
    HibernateEntityManagerFactory entityManagerFactory;
    
//    @PersistenceUnit(unitName = "crm")
//    HibernateEntityManagerFactory crmEntityManagerFactory;
    
    @Autowired
    EhCacheCacheManager ehCacheCacheManager;

    @RequestMapping("/sysInfo.do")
    public String index(Authentication authentication, Model model) {
        SessionFactory sessionFactory = entityManagerFactory.getSessionFactory();
        Statistics statistics = sessionFactory.getStatistics();
        
        addEntityModel(model, statistics);
        addCollectionModel(model, statistics);
        addQueryModel(model, statistics);
        addSecondLevelCacheModel(model, statistics);
        model.addAttribute("statistics", statistics);
        TreeMap<String, net.sf.ehcache.Statistics> cacheinfo = new TreeMap<String, net.sf.ehcache.Statistics>();
        for (String name : ehCacheCacheManager.getCacheNames()) {
            Ehcache cache = (Ehcache) ehCacheCacheManager.getCache(name).getNativeCache();
            cacheinfo.put(name, cache.getStatistics());
        }
        model.addAttribute("ehcache", cacheinfo);
        return "/admin/cache";
    }

    public void addSecondLevelCacheModel(Model model, Statistics statistics) {
        HashMap<String, Object> secondLevelCache = new HashMap<String, Object>();
        String[] secondLevelCacheRegionNames = statistics.getSecondLevelCacheRegionNames();
        for (String name : secondLevelCacheRegionNames) {
            secondLevelCache.put(name, describe(statistics.getSecondLevelCacheStatistics(name)));
        }
        model.addAttribute("secondLevelCache", secondLevelCache);
    }
    public void addEntityModel(Model model, Statistics statistics) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        String[] entityNames = statistics.getEntityNames();
        for (String name : entityNames) {
            map.put(name, describe(statistics.getEntityStatistics(name)));
        }
        model.addAttribute("entity", map);
    }
    
    public void addCollectionModel(Model model, Statistics statistics) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        String[] collectionRoleNames = statistics.getCollectionRoleNames();
        for (String name : collectionRoleNames) {
            map.put(name, describe(statistics.getCollectionStatistics(name)));
        }
        model.addAttribute("collection", map);
    }
    public void addQueryModel(Model model, Statistics statistics) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        String[] queryNames = statistics.getQueries();
        for (String name : queryNames) {
            map.put(name, describe(statistics.getQueryStatistics(name)));
        }
        model.addAttribute("query", map);
    }

    public Map<String, Object> describe(Object object) {
        Map<String, Object> map1 = new HashMap<String, Object>();
        try {
            map1 = BeanUtils.describe(object);
        } catch (Exception e) {
            logger.error("error on access object's properties while trying to describe it.");
        }
        Map<String, Object> map2 = new HashMap<String, Object>();
        for (Entry<String, Object> entry : map1.entrySet()) {
            if(filter(entry)){
                continue;
            }
            String[] words = StringUtils.splitByCharacterTypeCamelCase(entry.getKey());
            if (!ArrayUtils.isEmpty(words)) {
                words[0] = StringUtils.capitalize(words[0]);
            }
            String key = StringUtils.join(words," ");
            map2.put(key, entry.getValue());
        }
        return map2;
    }

    public boolean filter(Entry<String, Object> entry) {
        return entry.getKey().equals("class");
    }

    @RequestMapping("/cache/jpaStatistics.json")
    public void status(Authentication authentication, Model model) {
    }

    @RequestMapping(method = { RequestMethod.GET })
    public void index(ModelMap map) {
        SessionFactory sessionFactory = entityManagerFactory.getSessionFactory();
        Statistics statistics = sessionFactory.getStatistics();
        map.addAttribute("stats", statistics);
    }

    @RequestMapping("/cache/purgeEhcache.json")
    public void purgeAllCache(Authentication authentication, String name,  Model model) {
        Cache cache = ehCacheCacheManager.getCache(name);
        String result = "result";
        if(cache != null){
            cache.clear();
            model.addAttribute(result, "success");
        }else{
            model.addAttribute(result, "Cache Not Exist");
        }
    }
}
