package org.app.web.dto.admin;

import java.util.Calendar;

public class SessionInfo {
	private final String sessionId;
	private long lastRequest;
	private int maxIdle; // second
	private long creationTime;
	private String createTimeString;
	private String lastRequestTimeString;
	private String maxIdleString;
	private String remoteAddr;
	private String requestUri;

	public SessionInfo(String sessionId, long creationTime, long lastRequestTime, int maxIdle, String remoteAddr,
			String requestUri) {
		super();
		this.sessionId = sessionId;
		this.creationTime = creationTime;
		this.lastRequest = lastRequestTime;
		this.lastRequestTimeString = formatTime(Calendar.getInstance().getTimeInMillis() - lastRequestTime);
		this.maxIdleString = formatTime(maxIdle);
		this.maxIdle = maxIdle;
		this.remoteAddr = remoteAddr;
		this.requestUri = requestUri;

	}

	public long getLastRequest() {
		return lastRequest;
	}

	public void setLastRequest(long lastRequest) {
		this.lastRequest = lastRequest;
	}

	public int getMaxIdle() {
		return maxIdle;
	}

	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}

	public long getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(long creationTime) {
		this.creationTime = creationTime;
	}

	public String getCreateTimeString() {
		return createTimeString;
	}

	public void setCreateTimeString(String createTimeString) {
		this.createTimeString = createTimeString;
	}

	public String getLastRequestTimeString() {
		return lastRequestTimeString;
	}

	public void setLastRequestTimeString(String lastRequestTimeString) {
		this.lastRequestTimeString = lastRequestTimeString;
	}

	public String getMaxIdleString() {
		return maxIdleString;
	}

	public void setMaxIdleString(String maxIdleString) {
		this.maxIdleString = maxIdleString;
	}

	public String getRemoteAddr() {
		return remoteAddr;
	}

	public void setRemoteAddr(String remoteAddr) {
		this.remoteAddr = remoteAddr;
	}

	public String getRequestUri() {
		return requestUri;
	}

	public void setRequestUri(String requestUri) {
		this.requestUri = requestUri;
	}

	public String getSessionId() {
		return sessionId;
	}

	private static String formatTime(long seconds) {
		long hour = seconds / 3600, remainder = seconds % 3600, minute = remainder / 60, second = remainder % 60;
		return showOnNotNull(hour, "小时") + showOnNotNull(minute, "分") + showOnNotNull(second, "秒");

	}

	private static String showOnNotNull(long number, String suffix) {
		if (number == 0) {
			return "";
		}
		return number + suffix;
	}

	private static String twoDigitString(long number) {

		if (number == 0) {
			return "00";
		}

		if (number / 10 == 0) {
			return "0" + number;
		}

		return String.valueOf(number);
	}
}
