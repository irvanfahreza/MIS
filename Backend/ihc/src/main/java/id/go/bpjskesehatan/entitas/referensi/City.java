package id.go.bpjskesehatan.entitas.referensi;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * The persistent class for the master_city database table.
 * 
 */
@Table(name = "master_city")
@JsonInclude(Include.NON_NULL)
public class City implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Integer cityId;

	private Object cityName;

	@Column(name = "created_by")
	private Integer created_by;

	@Column(name = "created_time")
	private Timestamp created_time;

	@Column(name = "lastmodified_by")
	private Integer lastmodified_by;

	@Column(name = "lastmodified_time")
	private Timestamp lastmodified_time;

	@Column(name = "row_status")
	private Short row_status;

	public City() {
	}

	public Integer getCityId() {
		return this.cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public Object getCityName() {
		return this.cityName;
	}

	public void setCityName(Object cityName) {
		this.cityName = cityName;
	}

	public Integer getCreated_by() {
		return this.created_by;
	}

	public void setCreated_by(Integer created_by) {
		this.created_by = created_by;
	}

	public Timestamp getCreated_time() {
		return this.created_time;
	}

	public void setCreated_time(Timestamp created_time) {
		this.created_time = created_time;
	}

	public Integer getLastmodified_by() {
		return this.lastmodified_by;
	}

	public void setLastmodified_by(Integer lastmodified_by) {
		this.lastmodified_by = lastmodified_by;
	}

	public Timestamp getLastmodified_time() {
		return this.lastmodified_time;
	}

	public void setLastmodified_time(Timestamp lastmodified_time) {
		this.lastmodified_time = lastmodified_time;
	}

	public Short getRow_status() {
		return row_status;
	}

	public void setRow_status(Short row_status) {
		this.row_status = row_status;
	}

}