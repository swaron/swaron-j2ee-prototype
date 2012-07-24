package org.app.repo.jpa.po;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the sec_group_authority database table.
 * 
 */
@Entity
@Table(name="sec_group_authority")
public class SecGroupAuthority implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="sec_group_authority_id", unique=true, nullable=false)
	private Integer secGroupAuthorityId;

	@Column(nullable=false, length=64)
	private String authority;

	//bi-directional many-to-one association to SecGroup
    @ManyToOne
	@JoinColumn(name="sec_group_id", nullable=false)
	private SecGroup secGroup;

    public SecGroupAuthority() {
    }

	public Integer getSecGroupAuthorityId() {
		return this.secGroupAuthorityId;
	}

	public void setSecGroupAuthorityId(Integer secGroupAuthorityId) {
		this.secGroupAuthorityId = secGroupAuthorityId;
	}

	public String getAuthority() {
		return this.authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public SecGroup getSecGroup() {
		return this.secGroup;
	}

	public void setSecGroup(SecGroup secGroup) {
		this.secGroup = secGroup;
	}
	
}