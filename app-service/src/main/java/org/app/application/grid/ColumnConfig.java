package org.app.application.grid;

public class ColumnConfig {
	private String columnName;
	private boolean hide;
	private String entityName;
	private String type;
	private boolean hasAlias;

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public boolean isHide() {
		return hide;
	}

	public void setHide(boolean hide) {
		this.hide = hide;
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
