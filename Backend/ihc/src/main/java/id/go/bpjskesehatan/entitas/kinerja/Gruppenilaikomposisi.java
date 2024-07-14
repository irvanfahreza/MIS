package id.go.bpjskesehatan.entitas.kinerja;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.sql.Timestamp;

/**
 * The persistent class for the GRUPPENILAIKOMPOSISI database table.
 * 
 */
@JsonInclude(Include.NON_NULL)
@Entity
@NamedQuery(name = "Gruppenilaikomposisi.findAll", query = "SELECT g FROM Gruppenilaikomposisi g")
public class Gruppenilaikomposisi implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Integer kode;

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

	// bi-directional many-to-one association to Gruppenilai
	@JsonProperty("gruppenilai")
	@ManyToOne
	@JoinColumn(name = "KODEGRUPPENILAI")
	private Gruppenilai gruppenilai;

	// bi-directional many-to-one association to Penilai
	@JsonProperty("penilai")
	@ManyToOne
	@JoinColumn(name = "KODEPENILAI")
	private Penilai penilai;

	@Column(name = "MAXPESERTA")
	private Integer maxpeserta;

	@Column(name = "BOBOT")
	private Double bobot;

	public Gruppenilaikomposisi() {
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

	public Gruppenilai getGruppenilai() {
		return this.gruppenilai;
	}

	public void setGruppenilai(Gruppenilai gruppenilai) {
		this.gruppenilai = gruppenilai;
	}

	public Penilai getPenilai() {
		return this.penilai;
	}

	public void setPenilai(Penilai penilai) {
		this.penilai = penilai;
	}

	public Integer getMaxpeserta() {
		return maxpeserta;
	}

	public void setMaxpeserta(Integer maxpeserta) {
		this.maxpeserta = maxpeserta;
	}

	public Double getBobot() {
		return bobot;
	}

	public void setBobot(Double bobot) {
		this.bobot = bobot;
	}

}