package org.app.repo.jpa.po;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the acl_sid database table.
 * 
 */
@Entity
@Table(name="acl_sid")
public class AclSid implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="acl_sid_id", unique=true, nullable=false)
	private Integer aclSidId;

	@Column(nullable=false)
	private Boolean principal;

	@Column(nullable=false, length=100)
	private String sid;

	//bi-directional many-to-one association to AclEntry
	@OneToMany(mappedBy="aclSid")
	private List<AclEntry> aclEntries;

	//bi-directional many-to-one association to AclObjectIdentity
	@OneToMany(mappedBy="aclSid")
	private List<AclObjectIdentity> aclObjectIdentities;

    public AclSid() {
    }

	public Integer getAclSidId() {
		return this.aclSidId;
	}

	public void setAclSidId(Integer aclSidId) {
		this.aclSidId = aclSidId;
	}

	public Boolean getPrincipal() {
		return this.principal;
	}

	public void setPrincipal(Boolean principal) {
		this.principal = principal;
	}

	public String getSid() {
		return this.sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public List<AclEntry> getAclEntries() {
		return this.aclEntries;
	}

	public void setAclEntries(List<AclEntry> aclEntries) {
		this.aclEntries = aclEntries;
	}
	
	public List<AclObjectIdentity> getAclObjectIdentities() {
		return this.aclObjectIdentities;
	}

	public void setAclObjectIdentities(List<AclObjectIdentity> aclObjectIdentities) {
		this.aclObjectIdentities = aclObjectIdentities;
	}
	
}