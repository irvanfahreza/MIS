package id.go.bpjskesehatan.skpd;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import id.go.bpjskesehatan.Constant;
import id.go.bpjskesehatan.entitas.karyawan.Pegawai;
import id.go.bpjskesehatan.entitas.organisasi.Jabatan;
import id.go.bpjskesehatan.entitas.referensi.Anggaran;
import id.go.bpjskesehatan.entitas.referensi.Dati2;

import java.util.Date;
import java.sql.Timestamp;
import java.util.List;

/**
 * The persistent class for the SKPD database table.
 * 
 */
@JsonInclude(Include.NON_NULL)
@Entity
@NamedQuery(name = "Skpd.findAll", query = "SELECT s FROM Skpd s")
public class Skpd implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int kode;

	@Column(name = "kodeasal")
	private Dati2 asal;

	@JsonProperty("created_by")
	@Column(name = "CREATED_BY")
	private int createdBy;

	@JsonProperty("created_time")
	@Column(name = "CREATED_TIME")
	private Timestamp createdTime;

	@Lob
	private String deskripsi;

	private Pegawai disetujuioleh;

	private short flag;

	@Column(name = "kodejabatanpemberisetuju")
	private Jabatan jabatanpemberisetuju;

	@Column(name = "kodeanggaran")
	private Anggaran anggaran;

	@Column(name = "kodejeniskendaraan")
	private Jeniskendaraan jeniskendaraan;

	@JsonProperty("lastmodified_by")
	@Column(name = "LASTMODIFIED_BY")
	private int lastmodifiedBy;

	@JsonProperty("lastmodified_time")
	@Column(name = "LASTMODIFIED_TIME")
	private Timestamp lastmodifiedTime;

	private String maksud;

	@JsonProperty("row_status")
	@Column(name = "ROW_STATUS")
	private short rowStatus;

	private String tempat;

	@Temporal(TemporalType.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constant.ServerTimezone)
	private Date tglberangkat;

	@Temporal(TemporalType.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constant.ServerTimezone)
	private Date tglharuskembali;

	private Timestamp tglpengajuan;

	private Timestamp tglsetuju;

	@Column(name = "kodetujuan")
	private Dati2 tujuan;

	// bi-directional many-to-one association to Detailskpd
	@OneToMany(mappedBy = "skpd")
	private List<Detailskpd> detailskpds;

	public Skpd() {
	}

	public int getKode() {
		return this.kode;
	}

	public void setKode(int kode) {
		this.kode = kode;
	}

	public int getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedTime() {
		return this.createdTime;
	}

	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}

	public String getDeskripsi() {
		return this.deskripsi;
	}

	public void setDeskripsi(String deskripsi) {
		this.deskripsi = deskripsi;
	}

	public short getFlag() {
		return this.flag;
	}

	public void setFlag(short flag) {
		this.flag = flag;
	}

	public Anggaran getAnggaran() {
		return anggaran;
	}

	public void setAnggaran(Anggaran anggaran) {
		this.anggaran = anggaran;
	}

	public Jeniskendaraan getJeniskendaraan() {
		return jeniskendaraan;
	}

	public void setJeniskendaraan(Jeniskendaraan jeniskendaraan) {
		this.jeniskendaraan = jeniskendaraan;
	}

	public int getLastmodifiedBy() {
		return this.lastmodifiedBy;
	}

	public void setLastmodifiedBy(int lastmodifiedBy) {
		this.lastmodifiedBy = lastmodifiedBy;
	}

	public Timestamp getLastmodifiedTime() {
		return this.lastmodifiedTime;
	}

	public void setLastmodifiedTime(Timestamp lastmodifiedTime) {
		this.lastmodifiedTime = lastmodifiedTime;
	}

	public String getMaksud() {
		return this.maksud;
	}

	public void setMaksud(String maksud) {
		this.maksud = maksud;
	}

	public short getRowStatus() {
		return this.rowStatus;
	}

	public void setRowStatus(short rowStatus) {
		this.rowStatus = rowStatus;
	}

	public String getTempat() {
		return tempat;
	}

	public void setTempat(String tempat) {
		this.tempat = tempat;
	}

	public Date getTglberangkat() {
		return this.tglberangkat;
	}

	public void setTglberangkat(Date tglberangkat) {
		this.tglberangkat = tglberangkat;
	}

	public Date getTglharuskembali() {
		return this.tglharuskembali;
	}

	public void setTglharuskembali(Date tglharuskembali) {
		this.tglharuskembali = tglharuskembali;
	}

	public Timestamp getTglpengajuan() {
		return this.tglpengajuan;
	}

	public void setTglpengajuan(Timestamp tglpengajuan) {
		this.tglpengajuan = tglpengajuan;
	}

	public Timestamp getTglsetuju() {
		return this.tglsetuju;
	}

	public void setTglsetuju(Timestamp tglsetuju) {
		this.tglsetuju = tglsetuju;
	}

	public Dati2 getAsal() {
		return asal;
	}

	public void setAsal(Dati2 asal) {
		this.asal = asal;
	}

	public Pegawai getDisetujuioleh() {
		return disetujuioleh;
	}

	public void setDisetujuioleh(Pegawai disetujuioleh) {
		this.disetujuioleh = disetujuioleh;
	}

	public Jabatan getJabatanpemberisetuju() {
		return jabatanpemberisetuju;
	}

	public void setJabatanpemberisetuju(Jabatan jabatanpemberisetuju) {
		this.jabatanpemberisetuju = jabatanpemberisetuju;
	}

	public Dati2 getTujuan() {
		return tujuan;
	}

	public void setTujuan(Dati2 tujuan) {
		this.tujuan = tujuan;
	}

	public List<Detailskpd> getDetailskpds() {
		return this.detailskpds;
	}

	public void setDetailskpds(List<Detailskpd> detailskpds) {
		this.detailskpds = detailskpds;
	}

	public Detailskpd addDetailskpd(Detailskpd detailskpd) {
		getDetailskpds().add(detailskpd);
		detailskpd.setSkpd(this);

		return detailskpd;
	}

	public Detailskpd removeDetailskpd(Detailskpd detailskpd) {
		getDetailskpds().remove(detailskpd);
		detailskpd.setSkpd(null);

		return detailskpd;
	}

}