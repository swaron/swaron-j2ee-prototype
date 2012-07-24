package org.app.repo.jpa.po;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the db_info database table.
 * 
 */
@Entity
@Table(name="db_info")
public class DbInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="db_info_id", unique=true, nullable=false)
	private Integer dbInfoId;

	@Column(name="db_passwd", length=256)
	private String dbPasswd;

	@Column(name="db_user", length=256)
	private String dbUser;

	@Column(name="driver_class", nullable=false, length=256)
	private String driverClass;

	@Column(nullable=false, length=256)
	private String name;

	@Version
	@Column(name="update_time", nullable=false)
	private Timestamp updateTime;

	@Column(nullable=false, length=256)
	private String url;

	@Column(nullable=false, length=256)
	private String vendor;

    public DbInfo() {
    }

	public Integer getDbInfoId() {
		return this.dbInfoId;
	}

	public void setDbInfoId(Integer dbInfoId) {
		this.dbInfoId = dbInfoId;
	}

	public String getDbPasswd() {
		return this.dbPasswd;
	}

	public void setDbPasswd(String dbPasswd) {
		this.dbPasswd = dbPasswd;
	}

	public String getDbUser() {
		return this.dbUser;
	}

	public void setDbUser(String dbUser) {
		this.dbUser = dbUser;
	}

	public String getDriverClass() {
		return this.driverClass;
	}

	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Timestamp getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getVendor() {
		return this.vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

}