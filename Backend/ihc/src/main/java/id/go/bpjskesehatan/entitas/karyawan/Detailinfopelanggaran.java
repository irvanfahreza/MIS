package id.go.bpjskesehatan.entitas.karyawan;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.sql.Timestamp;

/**
 * The persistent class for the DETAILINFOPELANGGARAN database table.
 * 
 */
@JsonInclude(Include.NON_NULL)
@Entity
@NamedQuery(name = "Detailinfopelanggaran.findAll", query = "SELECT d FROM Detailinfopelanggaran d")
public class Detailinfopelanggaran implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	@JsonProperty("created_by")
	@Column(name = "CREATED_BY")
	private Integer created_by;

	@JsonProperty("created_time")
	@Column(name = "CREATED_TIME")
	private Timestamp created_time;

	@JsonProperty("lastmodified_by")
	@Column(name = "LASTMODIFIED_BY")
	private Integer lastmodified_by;

	@JsonProperty("lastmodified_time")
	@Column(name = "LASTMODIFIED_TIME")
	private Timestamp lastmodified_time;

	@JsonProperty("row_status")
	@Column(name = "ROW_STATUS")
	private Short row_status;
	// bi-directional many-to-one association to Infopelanggaran
	@ManyToOne
	@JoinColumn(name = "NPP")
	private Infopelanggaran infopelanggaran;

	public Detailinfopelanggaran() {
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
		return this.row_status;
	}

	public void setRow_status(Short row_status) {
		this.row_status = row_status;
	}

	public Infopelanggaran getInfopelanggaran() {
		return this.infopelanggaran;
	}

	public void setInfopelanggaran(Infopelanggaran infopelanggaran) {
		this.infopelanggaran = infopelanggaran;
	}

}