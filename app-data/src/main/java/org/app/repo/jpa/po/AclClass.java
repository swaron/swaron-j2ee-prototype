package org.app.repo.jpa.po;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the acl_class database table.
 * 
 */
@Entity
@Table(name="acl_class")
public class AclClass implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="acl_class_id", unique=true, nullable=false)
	private Integer aclClassId;

	@Column(name="class", nullable=false, length=100)
	private String class_;

	//bi-directional many-to-one association to AclObjectIdentity
	@OneToMany(mappedBy="aclClass")
	private List<AclObjectIdentity> aclObjectIdentities;

    public AclClass() {
    }

	public Integer getAclClassId() {
		return this.aclClassId;
	}

	public void setAclClassId(Integer aclClassId) {
		this.aclClassId = aclClassId;
	}

	public String getClass_() {
		return this.class_;
	}

	public void setClass_(String class_) {
		this.class_ = class_;
	}

	public List<AclObjectIdentity> getAclObjectIdentities() {
		return this.aclObjectIdentities;
	}

	public void setAclObjectIdentities(List<AclObjectIdentity> aclObjectIdentities) {
		this.aclObjectIdentities = aclObjectIdentities;
	}
	
}