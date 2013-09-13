package org.app.framework.extjs;

import java.util.ArrayList;

public class TableMetaData {
	String entity;
	String idProperty;
	ArrayList<Field> fields;
	ArrayList<Column> columns;

	public String getIdProperty() {
		return idProperty;
	}

	public void setIdProperty(String idProperty) {
		this.idProperty = idProperty;
	}

	public ArrayList<Field> getFields() {
		return fields;
	}

	public void setFields(ArrayList<Field> fields) {
		this.fields = fields;
	}

	public ArrayList<Column> getColumns() {
		return columns;
	}

	public void setColumns(ArrayList<Column> columns) {
		this.columns = columns;
	}

}
