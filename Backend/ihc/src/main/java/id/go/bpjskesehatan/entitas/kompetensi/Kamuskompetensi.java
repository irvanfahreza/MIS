package id.go.bpjskesehatan.entitas.kompetensi;

import java.io.Serializable;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.sql.Timestamp;

/**
 * The persistent class for the KAMUSKOMPETENSI database table.
 * 
 */
@JsonInclude(Include.NON_NULL)
public class Kamuskompetensi implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Integer kode;

	@Column(name = "kodeperiodekompetensi")
	@JsonProperty("periodekompetensi")
	private Periodekompetensi periodekompetensi;

	@Column(name = "kodekelompokkompetensi")
	@JsonProperty("kelompokkompetensi")
	private Kelompokkompetensi kelompokkompetensi;

	@Column(name = "kodelevelkompetensi")
	@JsonProperty("levelkompetensi")
	private Levelkompetensi levelkompetensi;
	
	@Column(name = "kodekompetensi")
	@JsonProperty("kompetensi")
	private Kompetensi kompetensi;

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

	public Kamuskompetensi() {
	}

	public Integer getKode() {
		return kode;
	}

	public void setKode(Integer kode) {
		this.kode = kode;
	}

	public Periodekompetensi getPeriodekompetensi() {
		return periodekompetensi;
	}

	public void setPeriodekompetensi(Periodekompetensi periodekompetensi) {
		this.periodekompetensi = periodekompetensi;
	}

	public Kelompokkompetensi getKelompokkompetensi() {
		return kelompokkompetensi;
	}

	public void setKelompokkompetensi(Kelompokkompetensi kelompokkompetensi) {
		this.kelompokkompetensi = kelompokkompetensi;
	}

	public Levelkompetensi getLevelkompetensi() {
		return levelkompetensi;
	}

	public void setLevelkompetensi(Levelkompetensi levelkompetensi) {
		this.levelkompetensi = levelkompetensi;
	}

	public Kompetensi getKompetensi() {
		return kompetensi;
	}

	public void setKompetensi(Kompetensi kompetensi) {
		this.kompetensi = kompetensi;
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

	public Short getRow_status() {
		return row_status;
	}

	public void setRow_status(Short row_status) {
		this.row_status = row_status;
	}

}