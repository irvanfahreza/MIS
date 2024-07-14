package id.go.bpjskesehatan.entitas.kinerja;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;
import java.util.List;

/**
 * The persistent class for the MASTERNILAI database table.
 * 
 */
@JsonInclude(Include.NON_NULL)
@Entity
@NamedQuery(name = "Masternilai.findAll", query = "SELECT m FROM Masternilai m")
public class Masternilai implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int kode;

	@Column(name = "CREATED_BY")
	private Integer created_by;

	@Column(name = "CREATED_TIME")
	private Timestamp created_time;

	private String deskripsi;

	@Column(name = "LASTMODIFIED_BY")
	private Integer lastmodified_by;

	@Column(name = "LASTMODIFIED_TIME")
	private Timestamp lastmodified_time;

	private String nama;

	@JsonProperty("row_status")
	@Column(name = "ROW_STATUS")
	private Short row_status;

	// bi-directional many-to-one association to Masternilaidetail
	@OneToMany(mappedBy = "masternilai")
	private List<Masternilaidetail> masternilaidetails;

	// bi-directional many-to-one association to Ubkomitmennilai
	@OneToMany(mappedBy = "masternilai")
	private List<Ubkomitmennilai> ubkomitmennilais;

	public Masternilai() {
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

	public String getNama() {
		return this.nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public Short getRow_status() {
		return row_status;
	}

	public void setRow_status(Short row_status) {
		this.row_status = row_status;
	}

	public List<Masternilaidetail> getMasternilaidetails() {
		return this.masternilaidetails;
	}

	public void setMasternilaidetails(List<Masternilaidetail> masternilaidetails) {
		this.masternilaidetails = masternilaidetails;
	}

	public Masternilaidetail addMasternilaidetail(Masternilaidetail masternilaidetail) {
		getMasternilaidetails().add(masternilaidetail);
		masternilaidetail.setMasternilai(this);

		return masternilaidetail;
	}

	public Masternilaidetail removeMasternilaidetail(Masternilaidetail masternilaidetail) {
		getMasternilaidetails().remove(masternilaidetail);
		masternilaidetail.setMasternilai(null);

		return masternilaidetail;
	}

	public List<Ubkomitmennilai> getUbkomitmennilais() {
		return this.ubkomitmennilais;
	}

	public void setUbkomitmennilais(List<Ubkomitmennilai> ubkomitmennilais) {
		this.ubkomitmennilais = ubkomitmennilais;
	}

	public Ubkomitmennilai addUbkomitmennilai(Ubkomitmennilai ubkomitmennilai) {
		getUbkomitmennilais().add(ubkomitmennilai);
		ubkomitmennilai.setMasternilai(this);

		return ubkomitmennilai;
	}

	public Ubkomitmennilai removeUbkomitmennilai(Ubkomitmennilai ubkomitmennilai) {
		getUbkomitmennilais().remove(ubkomitmennilai);
		ubkomitmennilai.setMasternilai(null);

		return ubkomitmennilai;
	}

}