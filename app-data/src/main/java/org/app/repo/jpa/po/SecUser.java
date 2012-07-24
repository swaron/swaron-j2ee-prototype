package org.app.repo.jpa.po;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the sec_user database table.
 * 
 */
@Entity
@Table(name="sec_user")
public class SecUser implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="sec_user_id", unique=true, nullable=false)
	private Integer secUserId;

	@Column(nullable=false, length=100)
	private String email;

	@Column(nullable=false)
	private Boolean enabled;

	@Column(name="last_last_login_time")
	private Timestamp lastLastLoginTime;

	@Column(name="last_login_time")
	private Timestamp lastLoginTime;

	@Column(name="locked_time")
	private Timestamp lockedTime;

	@Column(name="login_failed_count", nullable=false)
	private Integer loginFailedCount;

	@Column(name="login_state")
	private Integer loginState;

	@Column(nullable=false, length=64)
	private String passwd;

	@Column(name="update_time", nullable=false)
	private Timestamp updateTime;

	@Column(nullable=false, length=64)
	private String username;

	//bi-directional many-to-one association to SecGroupMember
	@OneToMany(mappedBy="secUser")
	private List<SecGroupMember> secGroupMembers;

	//bi-directional many-to-one association to UserDetail
    @ManyToOne
	@JoinColumn(name="user_detail_id")
	private UserDetail userDetail;

	//bi-directional many-to-one association to UserDetail
	@OneToMany(mappedBy="secUser")
	private List<UserDetail> userDetails;

    public SecUser() {
    }

	public Integer getSecUserId() {
		return this.secUserId;
	}

	public void setSecUserId(Integer secUserId) {
		this.secUserId = secUserId;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getEnabled() {
		return this.enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Timestamp getLastLastLoginTime() {
		return this.lastLastLoginTime;
	}

	public void setLastLastLoginTime(Timestamp lastLastLoginTime) {
		this.lastLastLoginTime = lastLastLoginTime;
	}

	public Timestamp getLastLoginTime() {
		return this.lastLoginTime;
	}

	public void setLastLoginTime(Timestamp lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public Timestamp getLockedTime() {
		return this.lockedTime;
	}

	public void setLockedTime(Timestamp lockedTime) {
		this.lockedTime = lockedTime;
	}

	public Integer getLoginFailedCount() {
		return this.loginFailedCount;
	}

	public void setLoginFailedCount(Integer loginFailedCount) {
		this.loginFailedCount = loginFailedCount;
	}

	public Integer getLoginState() {
		return this.loginState;
	}

	public void setLoginState(Integer loginState) {
		this.loginState = loginState;
	}

	public String getPasswd() {
		return this.passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public Timestamp getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<SecGroupMember> getSecGroupMembers() {
		return this.secGroupMembers;
	}

	public void setSecGroupMembers(List<SecGroupMember> secGroupMembers) {
		this.secGroupMembers = secGroupMembers;
	}
	
	public UserDetail getUserDetail() {
		return this.userDetail;
	}

	public void setUserDetail(UserDetail userDetail) {
		this.userDetail = userDetail;
	}
	
	public List<UserDetail> getUserDetails() {
		return this.userDetails;
	}

	public void setUserDetails(List<UserDetail> userDetails) {
		this.userDetails = userDetails;
	}
	
}