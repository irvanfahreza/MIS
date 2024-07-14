package id.go.bpjskesehatan.entitas.kinerja;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * The persistent class for the INTERPRETASI database table.
 * 
 */
@Entity
@JsonInclude(Include.NON_NULL)
@NamedQuery(name = "Interpretasi.findAll", query = "SELECT i FROM Interpretasi i")
public class Interpretasi implements Serializable {
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

	@Column(name = "ROW_STATUS")
	private Short row_status;

	private Double scoredari;

	private Double scoreke;

	// bi-directional many-to-one association to Interpretasi
	// @JsonProperty("periodekinerja")
	@ManyToOne
	@JoinColumn(name = "KODEPERIODEKINERJA")
	private Periodekinerja periodekinerja;

	// bi-directional many-to-one association to Interpretasi
	@OneToMany(mappedBy = "interpretasi")
	private List<Interpretasi> interpretasis;

	public Interpretasi() {
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

	public Short getRow_status() {
		return this.row_status;
	}

	public void setRow_status(Short row_status) {
		this.row_status = row_status;
	}

	public Double getScoredari() {
		return this.scoredari;
	}

	public void setScoredari(double scoredari) {
		this.scoredari = scoredari;
	}

	public Double getScoreke() {
		return this.scoreke;
	}

	public void setScoreke(double scoreke) {
		this.scoreke = scoreke;
	}

	public Periodekinerja getPeriodekinerja() {
		return periodekinerja;
	}

	public void setPeriodekinerja(Periodekinerja periodekinerja) {
		this.periodekinerja = periodekinerja;
	}

	public List<Interpretasi> getInterpretasis() {
		return this.interpretasis;
	}

	public void setInterpretasis(List<Interpretasi> interpretasis) {
		this.interpretasis = interpretasis;
	}

}