package org.app.repo.jpa.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the sec_group database table.
 * 
 */
@Entity
@Table(name="sec_group")
public class SecGroup implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="sec_group_id", unique=true, nullable=false)
	private Integer secGroupId;

	@Column(name="group_name", nullable=false, length=64)
	private String groupName;

	//bi-directional many-to-one association to SecGroupAuthority
	@OneToMany(mappedBy="secGroup")
	private List<SecGroupAuthority> secGroupAuthorities;

	//bi-directional many-to-one association to SecGroupMember
	@OneToMany(mappedBy="secGroup")
	private List<SecGroupMember> secGroupMembers;

    public SecGroup() {
    }

	public Integer getSecGroupId() {
		return this.secGroupId;
	}

	public void setSecGroupId(Integer secGroupId) {
		this.secGroupId = secGroupId;
	}

	public String getGroupName() {
		return this.groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public List<SecGroupAuthority> getSecGroupAuthorities() {
		return this.secGroupAuthorities;
	}

	public void setSecGroupAuthorities(List<SecGroupAuthority> secGroupAuthorities) {
		this.secGroupAuthorities = secGroupAuthorities;
	}
	
	public List<SecGroupMember> getSecGroupMembers() {
		return this.secGroupMembers;
	}

	public void setSecGroupMembers(List<SecGroupMember> secGroupMembers) {
		this.secGroupMembers = secGroupMembers;
	}
	
}