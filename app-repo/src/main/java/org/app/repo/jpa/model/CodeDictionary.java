package org.app.repo.jpa.model;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the code_dictionary database table.
 * 
 */
@Entity
@Table(name="code_dictionary")
public class CodeDictionary implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="code_dictionary_id", unique=true, nullable=false)
	private Integer codeDictionaryId;

	@Column(nullable=false, length=256)
	private String code;

	@Column(name="column_name", nullable=false, length=256)
	private String columnName;

	@Column(length=1024)
	private String description;

	@Column(nullable=false, length=256)
	private String name;

	@Column(name="name_en", length=256)
	private String nameEn;

	@Column(name="name_i18n_key", length=256)
	private String nameI18nKey;

	@Column(name="name_zh", length=256)
	private String nameZh;

	@Column(name="sort_order")
	private Integer sortOrder;

	@Column(name="table_name", nullable=false, length=256)
	private String tableName;

	@Column(name="update_time", nullable=false)
	private Timestamp updateTime;

    public CodeDictionary() {
    }

	public Integer getCodeDictionaryId() {
		return this.codeDictionaryId;
	}

	public void setCodeDictionaryId(Integer codeDictionaryId) {
		this.codeDictionaryId = codeDictionaryId;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getColumnName() {
		return this.columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNameEn() {
		return this.nameEn;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	public String getNameI18nKey() {
		return this.nameI18nKey;
	}

	public void setNameI18nKey(String nameI18nKey) {
		this.nameI18nKey = nameI18nKey;
	}

	public String getNameZh() {
		return this.nameZh;
	}

	public void setNameZh(String nameZh) {
		this.nameZh = nameZh;
	}

	public Integer getSortOrder() {
		return this.sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

	public String getTableName() {
		return this.tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public Timestamp getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

}