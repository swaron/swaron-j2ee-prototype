package org.app.repo.jpa.po;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the acl_object_identity database table.
 * 
 */
@Entity
@Table(name="acl_object_identity")
public class AclObjectIdentity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="acl_object_identity_id", unique=true, nullable=false)
	private Integer aclObjectIdentityId;

	@Column(name="entries_inheriting", nullable=false)
	private Boolean entriesInheriting;

	@Column(name="object_id_identity", nullable=false)
	private Integer objectIdIdentity;

	//bi-directional many-to-one association to AclEntry
	@OneToMany(mappedBy="aclObjectIdentityBean")
	private List<AclEntry> aclEntries;

	//bi-directional many-to-one association to AclClass
    @ManyToOne
	@JoinColumn(name="object_id_class", nullable=false)
	private AclClass aclClass;

	//bi-directional many-to-one association to AclObjectIdentity
    @ManyToOne
	@JoinColumn(name="parent_object")
	private AclObjectIdentity aclObjectIdentity;

	//bi-directional many-to-one association to AclObjectIdentity
	@OneToMany(mappedBy="aclObjectIdentity")
	private List<AclObjectIdentity> aclObjectIdentities;

	//bi-directional many-to-one association to AclSid
    @ManyToOne
	@JoinColumn(name="owner_sid", nullable=false)
	private AclSid aclSid;

    public AclObjectIdentity() {
    }

	public Integer getAclObjectIdentityId() {
		return this.aclObjectIdentityId;
	}

	public void setAclObjectIdentityId(Integer aclObjectIdentityId) {
		this.aclObjectIdentityId = aclObjectIdentityId;
	}

	public Boolean getEntriesInheriting() {
		return this.entriesInheriting;
	}

	public void setEntriesInheriting(Boolean entriesInheriting) {
		this.entriesInheriting = entriesInheriting;
	}

	public Integer getObjectIdIdentity() {
		return this.objectIdIdentity;
	}

	public void setObjectIdIdentity(Integer objectIdIdentity) {
		this.objectIdIdentity = objectIdIdentity;
	}

	public List<AclEntry> getAclEntries() {
		return this.aclEntries;
	}

	public void setAclEntries(List<AclEntry> aclEntries) {
		this.aclEntries = aclEntries;
	}
	
	public AclClass getAclClass() {
		return this.aclClass;
	}

	public void setAclClass(AclClass aclClass) {
		this.aclClass = aclClass;
	}
	
	public AclObjectIdentity getAclObjectIdentity() {
		return this.aclObjectIdentity;
	}

	public void setAclObjectIdentity(AclObjectIdentity aclObjectIdentity) {
		this.aclObjectIdentity = aclObjectIdentity;
	}
	
	public List<AclObjectIdentity> getAclObjectIdentities() {
		return this.aclObjectIdentities;
	}

	public void setAclObjectIdentities(List<AclObjectIdentity> aclObjectIdentities) {
		this.aclObjectIdentities = aclObjectIdentities;
	}
	
	public AclSid getAclSid() {
		return this.aclSid;
	}

	public void setAclSid(AclSid aclSid) {
		this.aclSid = aclSid;
	}
	
}