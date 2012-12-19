package org.app.repo.jpa.po;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the system_config database table.
 * 
 */
@Entity
@Table(name="system_config")
public class SystemConfig implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="system_config_id")
	private Integer systemConfigId;

	@Column(name="param_key")
	private String paramKey;

	@Column(name="param_value")
	private String paramValue;

	@Version
	@Column(name="update_time")
	private Timestamp updateTime;

    public SystemConfig() {
    }

	public Integer getSystemConfigId() {
		return this.systemConfigId;
	}

	public void setSystemConfigId(Integer systemConfigId) {
		this.systemConfigId = systemConfigId;
	}

	public String getParamKey() {
		return this.paramKey;
	}

	public void setParamKey(String paramKey) {
		this.paramKey = paramKey;
	}

	public String getParamValue() {
		return this.paramValue;
	}

	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}

	public Timestamp getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

}