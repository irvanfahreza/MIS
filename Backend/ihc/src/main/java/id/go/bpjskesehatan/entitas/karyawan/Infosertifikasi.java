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
 * The persistent class for the INFOSERTIFIKASI database table.
 * 
 */
@JsonInclude(Include.NON_NULL)
@Entity
@NamedQuery(name = "Infosertifikasi.findAll", query = "SELECT i FROM Infosertifikasi i")
public class Infosertifikasi implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int kode;

	private float biaya;

	private String catatan;

	@Column(name = "CREATED_BY")
	private Integer created_by;

	@Column(name = "CREATED_TIME")
	private Timestamp created_time;

	private String hasil;

	@JsonProperty("jenissertifikasi")
	@Column(name = "kodejenissertifikasi")
	private GenericEntitas jenissertifikasi;

	@JsonProperty("lingkupsertifikasi")
	@Column(name = "kodelingkupsertifikasi")
	private GenericEntitas lingkupsertifikasi;

	private String lampiran;

	@Column(name = "LASTMODIFIED_BY")
	private Integer lastmodified_by;

	@Column(name = "LASTMODIFIED_TIME")
	private Timestamp lastmodified_time;

	private String lokasi;

	private String nama;

	private String nomor;

	private String npp;

	private String penyelenggara;

	@Column(name = "ROW_STATUS")
	private Short row_status;

	@Temporal(TemporalType.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constant.ServerTimezone)
	private Date tanggal;

	@Temporal(TemporalType.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constant.ServerTimezone)
	private Date tglakhir;

	public Infosertifikasi() {
	}

	public int getKode() {
		return this.kode;
	}

	public void setKode(int kode) {
		this.kode = kode;
	}

	public float getBiaya() {
		return this.biaya;
	}

	public void setBiaya(float biaya) {
		this.biaya = biaya;
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

	public String getHasil() {
		return this.hasil;
	}

	public void setHasil(String hasil) {
		this.hasil = hasil;
	}

	public GenericEntitas getJenissertifikasi() {
		return jenissertifikasi;
	}

	public void setJenissertifikasi(GenericEntitas jenissertifikasi) {
		this.jenissertifikasi = jenissertifikasi;
	}

	public GenericEntitas getLingkupsertifikasi() {
		return lingkupsertifikasi;
	}

	public void setLingkupsertifikasi(GenericEntitas lingkupsertifikasi) {
		this.lingkupsertifikasi = lingkupsertifikasi;
	}

	public String getLampiran() {
		return lampiran;
	}

	public void setLampiran(String lampiran) {
		this.lampiran = lampiran;
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

	public String getNomor() {
		return this.nomor;
	}

	public void setNomor(String nomor) {
		this.nomor = nomor;
	}

	public String getNpp() {
		return this.npp;
	}

	public void setNpp(String npp) {
		this.npp = npp;
	}

	public String getPenyelenggara() {
		return this.penyelenggara;
	}

	public void setPenyelenggara(String penyelenggara) {
		this.penyelenggara = penyelenggara;
	}

	public Short getRow_status() {
		return this.row_status;
	}

	public void setRow_status(Short row_status) {
		this.row_status = row_status;
	}

	public Date getTanggal() {
		return this.tanggal;
	}

	public void setTanggal(Date tanggal) {
		this.tanggal = tanggal;
	}

	public Date getTglakhir() {
		return this.tglakhir;
	}

	public void setTglakhir(Date tglakhir) {
		this.tglakhir = tglakhir;
	}

}