package org.app.repo.jpa.model;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the user_detail database table.
 * 
 */
@Entity
@Table(name="user_detail")
public class UserDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="user_detail_id", unique=true, nullable=false)
	private Integer userDetailId;

    @Temporal( TemporalType.DATE)
	private Date birthday;

	@Column(name="first_name", length=64)
	private String firstName;

	@Column(name="last_name", length=64)
	private String lastName;

	@Column(name="update_time", nullable=false)
	private Timestamp updateTime;

	//bi-directional many-to-one association to SecUser
	@OneToMany(mappedBy="userDetail")
	private List<SecUser> secUsers;

	//bi-directional many-to-one association to SecUser
    @ManyToOne
	@JoinColumn(name="sec_user_id", nullable=false)
	private SecUser secUser;

    public UserDetail() {
    }

	public Integer getUserDetailId() {
		return this.userDetailId;
	}

	public void setUserDetailId(Integer userDetailId) {
		this.userDetailId = userDetailId;
	}

	public Date getBirthday() {
		return this.birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Timestamp getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public List<SecUser> getSecUsers() {
		return this.secUsers;
	}

	public void setSecUsers(List<SecUser> secUsers) {
		this.secUsers = secUsers;
	}
	
	public SecUser getSecUser() {
		return this.secUser;
	}

	public void setSecUser(SecUser secUser) {
		this.secUser = secUser;
	}
	
}