package org.app.integration.spring.cache;

import java.io.IOException;
import java.lang.reflect.Method;

import org.apache.commons.lang3.ClassUtils;
import org.app.integration.spring.jackson.CustomObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.interceptor.KeyGenerator;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * the empty string and null are treated as the same.
 * 
 * generate cached key to be used by spring cache.
 * 
 * @author aaron
 * 
 */
public class MethodKeyGenerator implements KeyGenerator {
    private static final Logger logger = LoggerFactory.getLogger(MethodKeyGenerator.class);
    private static final String NULL_PARAM_KEY = "";

    ObjectMapper objectMapper = new CustomObjectMapper();

    @Override
    public Object generate(Object target, Method method, Object... params) {
        StringBuilder sb = new StringBuilder();
        appendMethodSignature(sb, method);
        for (Object param : params) {
            appendParam(sb, param);
        }
        String key = sb.toString();
        logger.debug("build cache key as : " + key);
        return key;
    }

    /**
     * for example: CodeDictionaryDao.findAliasCount(String,String):ccl_comments_category,id,
     * @param sb
     * @param method
     */
    private void appendMethodSignature(StringBuilder sb, Method method) {
        sb.append(method.getDeclaringClass().getSimpleName() + ".");
        sb.append(method.getName());
//        sb.append("(");
//        Class[] params = method.getParameterTypes();
//        for (int j = 0; j < params.length; j++) {
//            sb.append(params[j].getSimpleName());
//            if (j < (params.length - 1)){
//                sb.append(",");
//            }
//        }
//        sb.append(")");
        sb.append(":");
    }

    private void appendParam(StringBuilder key, Object param) {
        if (param == null) {
            key.append(NULL_PARAM_KEY);
        } else if (ClassUtils.isPrimitiveOrWrapper(param.getClass()) || param instanceof String) {
            key.append(param);
        } else {
            try {
                String paramString = objectMapper.writeValueAsString(param);
                key.append(paramString);
            } catch (IOException e) {
                logger.warn("Unable to deserialize an object as a cache key. " + "object is " + param.getClass(),e);
            }
        }
        key.append(",");
    }
}
