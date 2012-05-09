package org.app.ws.rest.grid;

public class ColumnConfig {
	private String mapping;
	private boolean hide;
	private String name;
	private String type;
	private boolean hasAlias;

	public String getMapping() {
		return mapping;
	}

	public void setMapping(String mapping) {
		this.mapping = mapping;
	}

	public boolean isHide() {
		return hide;
	}

	public void setHide(boolean hide) {
		this.hide = hide;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isHasAlias() {
		return hasAlias;
	}

	public void setHasAlias(boolean hasAlias) {
		this.hasAlias = hasAlias;
	}

}
