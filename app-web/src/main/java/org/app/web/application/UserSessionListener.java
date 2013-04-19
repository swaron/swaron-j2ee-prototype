package org.app.web.application;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.app.web.dto.admin.SessionInfo;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.session.SessionCreationEvent;
import org.springframework.security.core.session.SessionDestroyedEvent;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class UserSessionListener implements ApplicationListener<ApplicationEvent> {

	// ~ Instance fields
	// ================================================================================================

	protected final Log logger = LogFactory.getLog(SessionRegistryImpl.class);

	private final Map<String, SessionInfo> sessions = new ConcurrentHashMap<String, SessionInfo>();

	// ~ Methods
	// ========================================================================================================

	public Collection<SessionInfo> getAllSessions() {
		return sessions.values();
	}

	@Override
	public void onApplicationEvent(ApplicationEvent applicationEvent) {
		if (applicationEvent instanceof SessionCreationEvent) {
			SessionCreationEvent event = (SessionCreationEvent) applicationEvent;
			handleCreate(event);
		} else if (applicationEvent instanceof SessionDestroyedEvent) {
			SessionDestroyedEvent event = (SessionDestroyedEvent) applicationEvent;
			handleDestroy(event);
		} else {
			return;
		}
	}

	private void handleCreate(SessionCreationEvent event) {
		ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder
				.currentRequestAttributes();
//		String sessionId = servletRequestAttributes.getSessionId();
		HttpServletRequest request = servletRequestAttributes.getRequest();

		HttpSession session = (HttpSession) event.getSource();
		SessionInfo sessionInfo = new SessionInfo(session.getId(), session.getCreationTime(), session.getLastAccessedTime(),
				session.getMaxInactiveInterval(), request.getRemoteAddr(), request.getRequestURI());
		sessions.put(session.getId(), sessionInfo);
		logger.debug("add an session to UserSessionListener: " + ToStringBuilder.reflectionToString(sessionInfo));
	}

	private void handleDestroy(SessionDestroyedEvent event) {
		HttpSession session = (HttpSession) event.getSource();
		logger.debug("destory session from UserSessionListener: "
				+ ToStringBuilder.reflectionToString(sessions.get(session.getId())));
		sessions.remove(session.getId());

	}

}
