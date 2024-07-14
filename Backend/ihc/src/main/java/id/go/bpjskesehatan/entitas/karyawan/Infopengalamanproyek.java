package id.go.bpjskesehatan.entitas.karyawan;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import id.go.bpjskesehatan.Constant;
import id.go.bpjskesehatan.entitas.GenericEntitas;

import java.util.Date;
import java.sql.Timestamp;

/**
 * The persistent class for the INFOPENGALAMANPROYEK database table.
 * 
 */
@JsonInclude(Include.NON_NULL)
@Entity
@NamedQuery(name = "Infopengalamanproyek.findAll", query = "SELECT i FROM Infopengalamanproyek i")
public class Infopengalamanproyek implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int kode;

	private String catatan;

	@Column(name = "CREATED_BY")
	private Integer created_by;

	@Column(name = "CREATED_TIME")
	private Timestamp created_time;

	@Lob
	private String deskripsi;

	private String hasil;

	private String jabatan;

	private String klien;

	@JsonProperty("statusproyek")
	@Column(name = "kodestatusproyek")
	private GenericEntitas statusproyek;

	@JsonProperty("tipeproyek")
	@Column(name = "kodetipeproyek")
	private GenericEntitas tipeproyek;

	@Column(name = "LASTMODIFIED_BY")
	private Integer lastmodified_by;

	@Column(name = "LASTMODIFIED_TIME")
	private Timestamp lastmodified_time;

	private String lokasi;

	private String nama;

	private String npp;

	private String penilaianatasan;

	@Column(name = "ROW_STATUS")
	private Short row_status;

	private String ruanglingkup;

	@Temporal(TemporalType.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constant.ServerTimezone)
	private Date tglmulai;

	@Temporal(TemporalType.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constant.ServerTimezone)
	private Date tglselesai;

	public Infopengalamanproyek() {
	}

	public int getKode() {
		return this.kode;
	}

	public void setKode(int kode) {
		this.kode = kode;
	}

	public String getCatatan() {
		return this.catatan;
	}

	public void setCatatan(String catatan) {
		this.catatan = catatan;
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

	public String getHasil() {
		return this.hasil;
	}

	public void setHasil(String hasil) {
		this.hasil = hasil;
	}

	public String getJabatan() {
		return this.jabatan;
	}

	public void setJabatan(String jabatan) {
		this.jabatan = jabatan;
	}

	public String getKlien() {
		return this.klien;
	}

	public void setKlien(String klien) {
		this.klien = klien;
	}

	public GenericEntitas getStatusproyek() {
		return statusproyek;
	}

	public void setStatusproyek(GenericEntitas statusproyek) {
		this.statusproyek = statusproyek;
	}

	public GenericEntitas getTipeproyek() {
		return tipeproyek;
	}

	public void setTipeproyek(GenericEntitas tipeproyek) {
		this.tipeproyek = tipeproyek;
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

	public String getLokasi() {
		return this.lokasi;
	}

	public void setLokasi(String lokasi) {
		this.lokasi = lokasi;
	}

	public String getNama() {
		return this.nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public String getNpp() {
		return this.npp;
	}

	public void setNpp(String npp) {
		this.npp = npp;
	}

	public String getPenilaianatasan() {
		return this.penilaianatasan;
	}

	public void setPenilaianatasan(String penilaianatasan) {
		this.penilaianatasan = penilaianatasan;
	}

	public Short getRow_status() {
		return this.row_status;
	}

	public void setRow_status(Short row_status) {
		this.row_status = row_status;
	}

	public String getRuanglingkup() {
		return this.ruanglingkup;
	}

	public void setRuanglingkup(String ruanglingkup) {
		this.ruanglingkup = ruanglingkup;
	}

	public Date getTglmulai() {
		return this.tglmulai;
	}

	public void setTglmulai(Date tglmulai) {
		this.tglmulai = tglmulai;
	}

	public Date getTglselesai() {
		return this.tglselesai;
	}

	public void setTglselesai(Date tglselesai) {
		this.tglselesai = tglselesai;
	}

}