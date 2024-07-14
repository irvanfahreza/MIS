package id.go.bpjskesehatan.entitas.organisasi;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Id;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Grade implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "kode")
	private String kode;

	// @Transient
	// @JsonKey(name = "id")
	// @Column(name="id")
	// private Integer id;

	@Column(name = "created_by")
	private Integer created_by;

	@Column(name = "created_time")
	private Timestamp created_time;

	@Column(name = "nama")
	private String nama;

	@Column(name = "lastmodified_by")
	private Integer lastmodified_by;

	@Column(name = "lastmodified_time")
	private Timestamp lastmodified_time;

	private Double marketSalary;

	private BigDecimal maxValue;

	private BigDecimal meetValue;

	private BigDecimal minValue;

	@Column(name = "row_status")
	private Integer row_status;

	public Grade() {
	}

	public String getKode() {
		return kode;
	}

	public void setKode(String kode) {
		this.kode = kode;
	}

	public Integer getCreated_by() {
		return created_by;
	}

	public void setCreated_by(Integer created_by) {
		this.created_by = created_by;
	}

	public Timestamp getCreated_time() {
		return created_time;
	}

	public void setCreated_time(Timestamp created_time) {
		this.created_time = created_time;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public Integer getLastmodified_by() {
		return lastmodified_by;
	}

	public void setLastmodified_by(Integer lastmodified_by) {
		this.lastmodified_by = lastmodified_by;
	}

	public Timestamp getLastmodified_time() {
		return lastmodified_time;
	}

	public void setLastmodified_time(Timestamp lastmodified_time) {
		this.lastmodified_time = lastmodified_time;
	}

	public Double getMarketSalary() {
		return marketSalary;
	}

	public void setMarketSalary(Double marketSalary) {
		this.marketSalary = marketSalary;
	}

	public BigDecimal getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(BigDecimal maxValue) {
		this.maxValue = maxValue;
	}

	public BigDecimal getMeetValue() {
		return meetValue;
	}

	public void setMeetValue(BigDecimal meetValue) {
		this.meetValue = meetValue;
	}

	public BigDecimal getMinValue() {
		return minValue;
	}

	public void setMinValue(BigDecimal minValue) {
		this.minValue = minValue;
	}

	public Integer getRow_status() {
		return row_status;
	}

	public void setRow_status(Integer row_status) {
		this.row_status = row_status;
	}

}