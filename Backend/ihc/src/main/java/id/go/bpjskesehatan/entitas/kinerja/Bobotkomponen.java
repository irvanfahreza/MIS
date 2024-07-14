package id.go.bpjskesehatan.entitas.kinerja;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.sql.Timestamp;

/**
 * The persistent class for the BOBOTKOMPONEN database table.
 * 
 */
@Entity
@JsonInclude(Include.NON_NULL)
@NamedQuery(name = "Bobotkomponen.findAll", query = "SELECT b FROM Bobotkomponen b")
public class Bobotkomponen implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Integer kode;

	private Double bobot;

	@Column(name = "CREATED_BY")
	private Integer created_by;

	@Column(name = "CREATED_TIME")
	private Timestamp created_time;

	@Column(name = "LASTMODIFIED_BY")
	private Integer lastmodified_by;

	@Column(name = "LASTMODIFIED_TIME")
	private Timestamp lastmodified_time;

	@Column(name = "ROW_STATUS")
	private Short row_status;

	// bi-directional many-to-one association to Komponen
	@JsonProperty("komponen")
	@ManyToOne
	@JoinColumn(name = "KODEKOMPONEN")
	private Komponen komponen;

	// bi-directional many-to-one association to Periodekinerja
	@JsonProperty("periodekinerja")
	@ManyToOne
	@JoinColumn(name = "KODEPERIODEKINERJA")
	private Periodekinerja periodekinerja;

	public Bobotkomponen() {
	}

	public Integer getKode() {
		return this.kode;
	}

	public void setKode(int kode) {
		this.kode = kode;
	}

	public Double getBobot() {
		return this.bobot;
	}

	public void setBobot(double bobot) {
		this.bobot = bobot;
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

	public Komponen getKomponen() {
		return this.komponen;
	}

	public void setKomponen(Komponen komponen) {
		this.komponen = komponen;
	}

	public Periodekinerja getPeriodekinerja() {
		return this.periodekinerja;
	}

	public void setPeriodekinerja(Periodekinerja periodekinerja) {
		this.periodekinerja = periodekinerja;
	}

}