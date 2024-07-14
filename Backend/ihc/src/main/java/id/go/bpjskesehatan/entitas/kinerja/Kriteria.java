package id.go.bpjskesehatan.entitas.kinerja;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.sql.Timestamp;
import java.util.List;

/**
 * The persistent class for the KRITERIA database table.
 * 
 */
@JsonInclude(Include.NON_NULL)
@Entity
@NamedQuery(name = "Kriteria.findAll", query = "SELECT k FROM Kriteria k")
public class Kriteria implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Integer kode;

	@Column(name = "CREATED_BY")
	private Integer created_by;

	@Column(name = "CREATED_TIME")
	private Timestamp created_time;

	private String definisi;

	private String deskripsi;

	@Column(name = "LASTMODIFIED_BY")
	private Integer lastmodified_by;

	@Column(name = "LASTMODIFIED_TIME")
	private Timestamp lastmodified_time;

	private Integer rating;

	@Column(name = "ROW_STATUS")
	private Short row_status;

	private Integer score;

	// bi-directional many-to-one association to Kriteria
	@JsonProperty("komponen")
	@ManyToOne
	@JoinColumn(name = "KODEKOMPONEN")
	private Komponen komponen;

	// bi-directional many-to-one association to Kriteria
	@OneToMany(mappedBy = "kriteria")
	private List<Kriteria> kriterias;

	// bi-directional many-to-one association to Periodekinerja
	@JsonProperty("periodekinerja")
	@ManyToOne
	@JoinColumn(name = "KODEPERIODEKINERJA")
	private Periodekinerja periodekinerja;

	public Kriteria() {
	}

	public Integer getKode() {
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

	public String getDefinisi() {
		return this.definisi;
	}

	public void setDefinisi(String definisi) {
		this.definisi = definisi;
	}

	public String getDeskripsi() {
		return this.deskripsi;
	}

	public void setDeskripsi(String deskripsi) {
		this.deskripsi = deskripsi;
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

	public Integer getRating() {
		return this.rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public Short getRow_status() {
		return this.row_status;
	}

	public void setRow_status(Short row_status) {
		this.row_status = row_status;
	}

	public Integer getScore() {
		return this.score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public Komponen getKomponen() {
		return komponen;
	}

	public void setKomponen(Komponen komponen) {
		this.komponen = komponen;
	}

	public List<Kriteria> getKriterias() {
		return this.kriterias;
	}

	public void setKriterias(List<Kriteria> kriterias) {
		this.kriterias = kriterias;
	}

	public Periodekinerja getPeriodekinerja() {
		return this.periodekinerja;
	}

	public void setPeriodekinerja(Periodekinerja periodekinerja) {
		this.periodekinerja = periodekinerja;
	}

}