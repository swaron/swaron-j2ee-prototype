package org.app.repo.jpa.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the detail_type database table.
 * 
 */
@Entity
@Table(name="detail_type")
public class DetailType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="detail_type_id")
	private Integer detailTypeId;

	@Column(name="category_type_id")
	private Integer categoryTypeId;

	@Column(name="normalized_unit")
	private String normalizedUnit;

	private String text;

	//bi-directional many-to-one association to DetailRange
	@OneToMany(mappedBy="detailType")
	private List<DetailRange> detailRanges;

    public DetailType() {
    }

	public Integer getDetailTypeId() {
		return this.detailTypeId;
	}

	public void setDetailTypeId(Integer detailTypeId) {
		this.detailTypeId = detailTypeId;
	}

	public Integer getCategoryTypeId() {
		return this.categoryTypeId;
	}

	public void setCategoryTypeId(Integer categoryTypeId) {
		this.categoryTypeId = categoryTypeId;
	}

	public String getNormalizedUnit() {
		return this.normalizedUnit;
	}

	public void setNormalizedUnit(String normalizedUnit) {
		this.normalizedUnit = normalizedUnit;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<DetailRange> getDetailRanges() {
		return this.detailRanges;
	}

	public void setDetailRanges(List<DetailRange> detailRanges) {
		this.detailRanges = detailRanges;
	}
	
}