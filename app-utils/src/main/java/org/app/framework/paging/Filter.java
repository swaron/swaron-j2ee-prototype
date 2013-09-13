package org.app.framework.paging;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class Filter {
	
	public static String OPERATOR_NOT_NULL = "notnull";
	public static String OPERATOR_NULL = "null";
	public static String ANY_MATCH = "any";
	public static String EXACT_MATCH = "exact";
	//start match is default in Ext, refer to Ext.util.Filter
	public static String START_MATCH = "start";
	
    String property;
    Object value;
    String matchStyle;
    String operator;
    
    /**
     * 抽取value里面的比较符号
     */
    public void normalize() {
    	if(value == null){
    		return;
    	}
    	if(value instanceof String){
    		String stringValue = (String) value;
    		if (StringUtils.containsAny(stringValue, "!<>=")) {
    			// pattern: >= 12, <=12, =12
    			Pattern pattern = Pattern.compile("([!<>=]+)\\s?(.+)");
    			Matcher matcher = pattern.matcher(stringValue);
    			if (!matcher.matches() || matcher.groupCount() < 2) {
    				//没有值，不处理
    			}else{
                    String parseOperator = matcher.group(1);
                    String parseValue = matcher.group(2);
                    this.value = parseValue;
                    if(StringUtils.isEmpty(this.operator)){
                    	this.operator = parseOperator;
                    }
    			}
    		}
    	}
    	//如果是数组，并且数组为空，那么通常情况是不想查到任何记录，保留matchStyle。 而不是查询 == null的数据
//    	if (value instanceof Object[] && ((Object[]) value).length == 0) {
//    		value = null;
//		} else if (value instanceof Collection && ((Collection) value).isEmpty()) {
//			value = null;
//		}
    	if(StringUtils.isEmpty(this.operator)){
    		this.operator = "==";
    	}
	}
    public Filter() {
    }
    
    public Filter(String property, Object value) {
        this.property = property;
        this.value = value;
    }
    
    public Filter(String property, Object value, String matchStyle) {
    	this.property = property;
    	this.value = value;
    	this.matchStyle = matchStyle;
    }

    public Filter(String property,  Object value, String matchStyle, String operator) {
        this.property = property;
        this.value = value;
        this.matchStyle = matchStyle;
        this.operator = operator;
    }
    
    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getMatchStyle() {
		return matchStyle;
	}

	public void setMatchStyle(String matchStyle) {
		this.matchStyle = matchStyle;
	}

	public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }


    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

}
