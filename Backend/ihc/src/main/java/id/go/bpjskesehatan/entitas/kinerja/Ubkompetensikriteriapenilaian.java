package id.go.bpjskesehatan.entitas.kinerja;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.sql.Timestamp;

/**
 * The persistent class for the UBKOMPETENSIKRITERIAPENILAIAN database table.
 * 
 */
@JsonInclude(Include.NON_NULL)
@Entity
@NamedQuery(name = "Ubkompetensikriteriapenilaian.findAll", query = "SELECT u FROM Ubkompetensikriteriapenilaian u")
public class Ubkompetensikriteriapenilaian implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int kode;

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

	// bi-directional many-to-one association to Masterkriteriapenilaian
	@ManyToOne
	@JoinColumn(name = "KODEMASTERKRITERIAPENILAIAN")
	private Masterkriteriapenilaian masterkriteriapenilaian;

	// bi-directional many-to-one association to Periodeubkompetensi
	@ManyToOne
	@JoinColumn(name = "KODEPERIODEUBKOMPETENSI")
	private Periodeubkompetensi periodeubkompetensi;

	public Ubkompetensikriteriapenilaian() {
	}

	public int getKode() {
		return this.kode;
	}

	public void setKode(int kode) {
		this.kode = kode;
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

	public Masterkriteriapenilaian getMasterkriteriapenilaian() {
		return this.masterkriteriapenilaian;
	}

	public void setMasterkriteriapenilaian(Masterkriteriapenilaian masterkriteriapenilaian) {
		this.masterkriteriapenilaian = masterkriteriapenilaian;
	}

	public Periodeubkompetensi getPeriodeubkompetensi() {
		return this.periodeubkompetensi;
	}

	public void setPeriodeubkompetensi(Periodeubkompetensi periodeubkompetensi) {
		this.periodeubkompetensi = periodeubkompetensi;
	}

}