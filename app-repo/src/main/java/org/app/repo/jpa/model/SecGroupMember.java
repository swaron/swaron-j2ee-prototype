package org.app.repo.jpa.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the sec_group_member database table.
 * 
 */
@Entity
@Table(name="sec_group_member")
public class SecGroupMember implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="sec_group_member_id")
	private Integer secGroupMemberId;

	//bi-directional many-to-one association to SecGroup
    @ManyToOne
	@JoinColumn(name="sec_group_id")
	private SecGroup secGroup;

	//bi-directional many-to-one association to SecUser
    @ManyToOne
	@JoinColumn(name="sec_user_id")
	private SecUser secUser;

    public SecGroupMember() {
    }

	public Integer getSecGroupMemberId() {
		return this.secGroupMemberId;
	}

	public void setSecGroupMemberId(Integer secGroupMemberId) {
		this.secGroupMemberId = secGroupMemberId;
	}

	public SecGroup getSecGroup() {
		return this.secGroup;
	}

	public void setSecGroup(SecGroup secGroup) {
		this.secGroup = secGroup;
	}
	
	public SecUser getSecUser() {
		return this.secUser;
	}

	public void setSecUser(SecUser secUser) {
		this.secUser = secUser;
	}
	
}