package id.go.bpjskesehatan.entitas.kinerja;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the MASTERKRITERIAPENILAIAN database table.
 * 
 */
@JsonInclude(Include.NON_NULL)
@Entity
@NamedQuery(name="Masterkriteriapenilaian.findAll", query="SELECT m FROM Masterkriteriapenilaian m")
public class Masterkriteriapenilaian implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int kode;

	@Column(name="CREATED_BY")
	private Integer created_by;

	@Column(name="CREATED_TIME")
	private Timestamp created_time;

	private String deskripsi;

	@Column(name="LASTMODIFIED_BY")
	private Integer lastmodified_by;

	@Column(name="LASTMODIFIED_TIME")
	private Timestamp lastmodified_time;

	private String nama;

	@Column(name="ROW_STATUS")
	private Short row_status;

	private int skor;

	private short tipeumpanbalik;

	//bi-directional many-to-one association to Ubkomitmenkriteriapenilaian
	@OneToMany(mappedBy="masterkriteriapenilaian")
	private List<Ubkomitmenkriteriapenilaian> ubkomitmenkriteriapenilaians;

	//bi-directional many-to-one association to Ubkompetensikriteriapenilaian
	@OneToMany(mappedBy="masterkriteriapenilaian")
	private List<Ubkompetensikriteriapenilaian> ubkompetensikriteriapenilaians;

	public Masterkriteriapenilaian() {
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
		return this.row_status;
	}

	public void setRow_status(Short row_status) {
		this.row_status = row_status;
	}

	public int getSkor() {
		return this.skor;
	}

	public void setSkor(int skor) {
		this.skor = skor;
	}

	public short getTipeumpanbalik() {
		return this.tipeumpanbalik;
	}

	public void setTipeumpanbalik(short tipeumpanbalik) {
		this.tipeumpanbalik = tipeumpanbalik;
	}

	public List<Ubkomitmenkriteriapenilaian> getUbkomitmenkriteriapenilaians() {
		return this.ubkomitmenkriteriapenilaians;
	}

	public void setUbkomitmenkriteriapenilaians(List<Ubkomitmenkriteriapenilaian> ubkomitmenkriteriapenilaians) {
		this.ubkomitmenkriteriapenilaians = ubkomitmenkriteriapenilaians;
	}

	public Ubkomitmenkriteriapenilaian addUbkomitmenkriteriapenilaian(Ubkomitmenkriteriapenilaian ubkomitmenkriteriapenilaian) {
		getUbkomitmenkriteriapenilaians().add(ubkomitmenkriteriapenilaian);
		ubkomitmenkriteriapenilaian.setMasterkriteriapenilaian(this);

		return ubkomitmenkriteriapenilaian;
	}

	public Ubkomitmenkriteriapenilaian removeUbkomitmenkriteriapenilaian(Ubkomitmenkriteriapenilaian ubkomitmenkriteriapenilaian) {
		getUbkomitmenkriteriapenilaians().remove(ubkomitmenkriteriapenilaian);
		ubkomitmenkriteriapenilaian.setMasterkriteriapenilaian(null);

		return ubkomitmenkriteriapenilaian;
	}

	public List<Ubkompetensikriteriapenilaian> getUbkompetensikriteriapenilaians() {
		return this.ubkompetensikriteriapenilaians;
	}

	public void setUbkompetensikriteriapenilaians(List<Ubkompetensikriteriapenilaian> ubkompetensikriteriapenilaians) {
		this.ubkompetensikriteriapenilaians = ubkompetensikriteriapenilaians;
	}

	public Ubkompetensikriteriapenilaian addUbkompetensikriteriapenilaian(Ubkompetensikriteriapenilaian ubkompetensikriteriapenilaian) {
		getUbkompetensikriteriapenilaians().add(ubkompetensikriteriapenilaian);
		ubkompetensikriteriapenilaian.setMasterkriteriapenilaian(this);

		return ubkompetensikriteriapenilaian;
	}

	public Ubkompetensikriteriapenilaian removeUbkompetensikriteriapenilaian(Ubkompetensikriteriapenilaian ubkompetensikriteriapenilaian) {
		getUbkompetensikriteriapenilaians().remove(ubkompetensikriteriapenilaian);
		ubkompetensikriteriapenilaian.setMasterkriteriapenilaian(null);

		return ubkompetensikriteriapenilaian;
	}

}