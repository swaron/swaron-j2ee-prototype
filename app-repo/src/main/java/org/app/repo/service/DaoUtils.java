package org.app.repo.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.ManagedType;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

public class DaoUtils {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected EntityManager entityManager;

    
    public DaoUtils(EntityManager entityManager) {
    	this.entityManager = entityManager;
	}
    
    public <T> List<Predicate> buildFilters(Root<T> root, LinkedHashMap<String, String> filters) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        ArrayList<Predicate> predicates = new ArrayList<Predicate>();
        if (filters == null) {
            return predicates;
        }
        for (Entry<String, String> e : filters.entrySet()) {
            try {
                Path<Object> property = root.get(e.getKey());
                String value = e.getValue();
                if(StringUtils.isEmpty(value)){
                    continue;
                }
                Class<? extends Object> javaType = property.getJavaType();
                if (ClassUtils.isAssignable(javaType, String.class, true)) {
                    // handle string property
                    Path<String> stringProperty = root.<String> get(e.getKey());
                    Predicate like = builder.like(builder.lower(stringProperty), "%" + value.toLowerCase() + "%");
                    predicates.add(like);
                } else if (ClassUtils.isAssignable(javaType, long.class, true)) {
                    //handle long,int,short,char
                    try {
                        if(StringUtils.containsAny(value, "<>=")){
                            // pattern: >= 12.3, <=12, =12.3
                            Pattern pattern = Pattern.compile("([<>=]+)\\s?([\\d.]+)");
                            Matcher matcher = pattern.matcher(value);
                            if(!matcher.matches() || matcher.groupCount() < 2){
                                logger.debug("invalid filter string for number column, " + value);
                                continue;
                            }
                            String operator = matcher.group(1);
                            Double opValue = Double.valueOf(matcher.group(2));
                            Path<Number> numProperty = root.<Number> get(e.getKey());
                            if("==".equals(operator) || "=".equals(operator)){
                                predicates.add(builder.equal(numProperty, opValue));
                            }else if("<".equals(operator)){
                                predicates.add(builder.lt(numProperty, opValue));
                            }else if(">".equals(operator)){
                                predicates.add(builder.gt(numProperty, opValue));
                            }else if("<=".equals(operator)){
                                predicates.add(builder.le(numProperty, opValue));
                            }else if(">=".equals(operator)){
                                predicates.add(builder.ge(numProperty, opValue));
                            }else{
                                predicates.add(builder.notEqual(numProperty, opValue));
                            }
                        }else{
                            Predicate equal = builder.equal(property, Long.valueOf(value));
                            predicates.add(equal);
                        }
                    } catch (NumberFormatException ex) {
                        logger.warn("unable to parse long while appling filter, string value is {}",value);
                    }
                } else if (ClassUtils.isAssignable(javaType, double.class, true)) {
                    //handle double,float
                    try {
                        if(StringUtils.containsAny(value, "<>=")){
                            // pattern: >= 12.3, <=12, =12.3
                            Pattern pattern = Pattern.compile("([<>=]+)\\s?([\\d.]+)");
                            Matcher matcher = pattern.matcher(value);
                            if(!matcher.matches() || matcher.groupCount() < 2){
                                logger.debug("invalid filter string for number column, " + value);
                                continue;
                            }
                            String operator = matcher.group(1);
                            Double opValue = Double.valueOf(matcher.group(2));
                            Path<Double> numProperty = root.<Double> get(e.getKey());
                            if("==".equals(operator) || "=".equals(operator)){
                                predicates.add(builder.equal(numProperty, opValue));
                            }else if("<".equals(operator)){
                                predicates.add(builder.lt(numProperty, opValue));
                            }else if(">".equals(operator)){
                                predicates.add(builder.gt(numProperty, opValue));
                            }else if("<=".equals(operator)){
                                predicates.add(builder.le(numProperty, opValue));
                            }else if(">=".equals(operator)){
                                predicates.add(builder.ge(numProperty, opValue));
                            }else{
                                predicates.add(builder.notEqual(numProperty, opValue));
                            }
                        }else{
                                Predicate equal = builder.equal(property, Double.valueOf(value));
                                predicates.add(equal);
                        }
                    } catch (NumberFormatException ex) {
                        logger.warn("unable to parse double while appling filter, string value is {}",value);
                    }
                } else if (ClassUtils.isAssignable(javaType, boolean.class, true)) {
                    boolean requestOn;
                    try {
                        if(NumberUtils.isDigits(value)){
                            //try to treat Natural Number other than 0 as true
                            long longValue = Long.parseLong(value);
                            requestOn = (longValue != 0);
                        }else{
                            // treat case insensitive 'true' as true, others as false
                            requestOn = Boolean.parseBoolean(value);
                        }
                        Path<Boolean> booleanProperty = root.<Boolean> get(e.getKey());
                        if(requestOn){
                            predicates.add(builder.isTrue(booleanProperty));
                        }else{
                            predicates.add(builder.isFalse(booleanProperty));
                        }
                    } catch (NumberFormatException ex) {
                        logger.warn("unable to parse Long while appling filter, string value is {}",value);
                    }
                }else{
                    logger.debug("unsupported filter property. type is {}, property name is {}.",javaType,e.getKey());
                }
            } catch (RuntimeException ex) {
                logger.warn("some filter parameter not applied.", ex);
            }
        }
        
        return predicates;
    }
    
    public <T> List<Order> buildOrders(Root<T> root, LinkedHashMap<String, String> sorts) {
        List<Order> orders = new ArrayList<Order>();
        if (sorts == null) {
            return orders;
        }
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        for (Entry<String, String> sort : sorts.entrySet()) {
            try {
                if (sort.getValue().equalsIgnoreCase("asc")) {
                    orders.add(builder.asc(root.get(sort.getKey())));
                } else {
                    orders.add(builder.desc(root.get(sort.getKey())));
                }
            } catch (IllegalArgumentException e) {
                logger.warn("sorting parameter not applied, the field name NOT MATCHED", e);
            }
        }
        return orders;
    }
    
}
