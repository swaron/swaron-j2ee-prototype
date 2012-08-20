package org.app.repo.jpa.po;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the acl_entry database table.
 * 
 */
@Entity
@Table(name="acl_entry")
public class AclEntry implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="acl_entry_id", unique=true, nullable=false)
	private Integer aclEntryId;

	@Column(name="ace_order", nullable=false)
	private Integer aceOrder;

	@Column(name="audit_failure", nullable=false)
	private Boolean auditFailure;

	@Column(name="audit_success", nullable=false)
	private Boolean auditSuccess;

	@Column(nullable=false)
	private Boolean granting;

	@Column(nullable=false)
	private Integer mask;

	//bi-directional many-to-one association to AclObjectIdentity
    @ManyToOne
	@JoinColumn(name="acl_object_identity", nullable=false)
	private AclObjectIdentity aclObjectIdentityBean;

	//bi-directional many-to-one association to AclSid
    @ManyToOne
	@JoinColumn(name="sid", nullable=false)
	private AclSid aclSid;

    public AclEntry() {
    }

	public Integer getAclEntryId() {
		return this.aclEntryId;
	}

	public void setAclEntryId(Integer aclEntryId) {
		this.aclEntryId = aclEntryId;
	}

	public Integer getAceOrder() {
		return this.aceOrder;
	}

	public void setAceOrder(Integer aceOrder) {
		this.aceOrder = aceOrder;
	}

	public Boolean getAuditFailure() {
		return this.auditFailure;
	}

	public void setAuditFailure(Boolean auditFailure) {
		this.auditFailure = auditFailure;
	}

	public Boolean getAuditSuccess() {
		return this.auditSuccess;
	}

	public void setAuditSuccess(Boolean auditSuccess) {
		this.auditSuccess = auditSuccess;
	}

	public Boolean getGranting() {
		return this.granting;
	}

	public void setGranting(Boolean granting) {
		this.granting = granting;
	}

	public Integer getMask() {
		return this.mask;
	}

	public void setMask(Integer mask) {
		this.mask = mask;
	}

	public AclObjectIdentity getAclObjectIdentityBean() {
		return this.aclObjectIdentityBean;
	}

	public void setAclObjectIdentityBean(AclObjectIdentity aclObjectIdentityBean) {
		this.aclObjectIdentityBean = aclObjectIdentityBean;
	}
	
	public AclSid getAclSid() {
		return this.aclSid;
	}

	public void setAclSid(AclSid aclSid) {
		this.aclSid = aclSid;
	}
	
}