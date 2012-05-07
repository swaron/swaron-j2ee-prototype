package org.app.integration.spring.i18n;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.LocaleUtils;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.util.WebUtils;

public class LocaleResolver extends SessionLocaleResolver {


//    @Autowired
    private String[] availableLanguages = { "en", "zh" };

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        Locale locale = super.resolveLocale(request);
        if (!ArrayUtils.contains(availableLanguages, locale.getLanguage())) {
            locale = LocaleUtils.toLocale(availableLanguages[0]);
        }
        return locale;
    }

    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
        if (ArrayUtils.contains(availableLanguages, locale.getLanguage())) {
            WebUtils.setSessionAttribute(request, LOCALE_SESSION_ATTRIBUTE_NAME, locale);
        }
    }

}