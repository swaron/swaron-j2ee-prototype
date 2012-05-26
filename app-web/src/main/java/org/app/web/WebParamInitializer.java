package org.app.web;

import java.sql.DriverManager;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class WebParamInitializer{
	
	@Autowired(required=false)
	ServletContext servletContext;
	
	@PostConstruct
	public void initApp() {
		if(servletContext != null){
			initWebApp();
		}
	}
	public void initWebApp() {
		String appVersion = servletContext.getInitParameter("appVersion");
		if(!StringUtils.hasText(appVersion)  || appVersion.equals("${appVersion}")){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			appVersion = format.format(new Date());
		}
		servletContext.setAttribute("appVersion", appVersion);
		
		DriverManager.setLoginTimeout(10);
	}

}
