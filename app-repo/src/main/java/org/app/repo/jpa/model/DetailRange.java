package org.app.repo.jpa.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the detail_range database table.
 * 
 */
@Entity
@Table(name="detail_range")
public class DetailRange implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="detail_range_id", unique=true, nullable=false)
	private Integer detailRangeId;

	private double max;

	private double min;

	@Column(nullable=false, length=1024)
	private String text;

	@Column(length=1024)
	private String val;

	//bi-directional many-to-one association to DetailType
    @ManyToOne
	@JoinColumn(name="detail_type_id", nullable=false)
	private DetailType detailType;

    public DetailRange() {
    }

	public Integer getDetailRangeId() {
		return this.detailRangeId;
	}

	public void setDetailRangeId(Integer detailRangeId) {
		this.detailRangeId = detailRangeId;
	}

	public double getMax() {
		return this.max;
	}

	public void setMax(double max) {
		this.max = max;
	}

	public double getMin() {
		return this.min;
	}

	public void setMin(double min) {
		this.min = min;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getVal() {
		return this.val;
	}

	public void setVal(String val) {
		this.val = val;
	}

	public DetailType getDetailType() {
		return this.detailType;
	}

	public void setDetailType(DetailType detailType) {
		this.detailType = detailType;
	}
	
}