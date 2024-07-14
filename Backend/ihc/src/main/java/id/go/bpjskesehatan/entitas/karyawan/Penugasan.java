package id.go.bpjskesehatan.entitas.karyawan;

import id.go.bpjskesehatan.Constant;
import id.go.bpjskesehatan.entitas.GenericEntitas;
import id.go.bpjskesehatan.entitas.organisasi.Jabatan;

import java.io.Serializable;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * The persistent class for the PENUGASAN database table.
 * 
 */
@JsonInclude(Include.NON_NULL)
@Entity
@NamedQuery(name = "Penugasan.findAll", query = "SELECT p FROM Penugasan p")
public class Penugasan implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Integer kode;

	@Column(name = "kodegrade")
	@JsonProperty("grade")
	private GenericEntitas grade;

	@Column(name = "kodejabatan")
	@JsonProperty("jabatan")
	private Jabatan jabatan;

	@Column(name = "kodejenissk")
	@JsonProperty("jenissk")
	private GenericEntitas jenissk;

	@Column(name = "kodestatusjabatan")
	@JsonProperty("statusjabatan")
	private GenericEntitas statusjabatan;

	@Column(name = "kodesubgrade")
	@JsonProperty("subgrade")
	private GenericEntitas subgrade;

	private Integer masapercobaan;

	private String nomorsk;

	@Temporal(TemporalType.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constant.ServerTimezone)
	private Date tanggalsk;

	@Temporal(TemporalType.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constant.ServerTimezone)
	private Date tmtjabatan;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constant.ServerTimezone)
	private Date tatjabatan;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constant.ServerTimezone)
	private Date tanggalmulai;
	
	private Integer ismutation;
	
	private String lampiran;

	@Column(name = "created_time")
	private Timestamp created_time;
	@Column(name = "created_by")
	private Integer created_by;
	@Column(name = "lastmodified_time")
	private Timestamp lastmodified_time;
	@Column(name = "lastmodified_by")
	private Integer lastmodified_by;
	@Column(name = "row_status")
	private Short row_status;

	// bi-directional many-to-one association to Lampiranpenugasan
	@OneToMany(mappedBy = "penugasan")
	private List<Lampiranpenugasan> lampiranpenugasans;

	@Column(name = "npp")
	@JsonProperty("pegawai")
	private Pegawai pegawai;

	public Penugasan() {
	}

	public String getLampiran() {
		return lampiran;
	}

	public void setLampiran(String lampiran) {
		this.lampiran = lampiran;
	}

	public Integer getKode() {
		return this.kode;
	}

	public void setKode(Integer kode) {
		this.kode = kode;
	}

	public GenericEntitas getGrade() {
		return grade;
	}

	public void setGrade(GenericEntitas grade) {
		this.grade = grade;
	}

	public Jabatan getJabatan() {
		return jabatan;
	}

	public void setJabatan(Jabatan jabatan) {
		this.jabatan = jabatan;
	}

	public GenericEntitas getJenissk() {
		return jenissk;
	}

	public void setJenissk(GenericEntitas jenissk) {
		this.jenissk = jenissk;
	}

	public GenericEntitas getStatusjabatan() {
		return statusjabatan;
	}

	public void setStatusjabatan(GenericEntitas statusjabatan) {
		this.statusjabatan = statusjabatan;
	}

	public GenericEntitas getSubgrade() {
		return subgrade;
	}

	public void setSubgrade(GenericEntitas subgrade) {
		this.subgrade = subgrade;
	}

	public Integer getMasapercobaan() {
		return this.masapercobaan;
	}

	public void setMasapercobaan(Integer masapercobaan) {
		this.masapercobaan = masapercobaan;
	}

	public String getNomorsk() {
		return this.nomorsk;
	}

	public void setNomorsk(String nomorsk) {
		this.nomorsk = nomorsk;
	}

	public Date getTanggalsk() {
		return this.tanggalsk;
	}

	public void setTanggalsk(Date tanggalsk) {
		this.tanggalsk = tanggalsk;
	}

	public Date getTmtjabatan() {
		return this.tmtjabatan;
	}

	public void setTmtjabatan(Date tmtjabatan) {
		this.tmtjabatan = tmtjabatan;
	}

	public Date getTatjabatan() {
		return tatjabatan;
	}

	public void setTatjabatan(Date tatjabatan) {
		this.tatjabatan = tatjabatan;
	}

	public Date getTanggalmulai() {
		return tanggalmulai;
	}

	public void setTanggalmulai(Date tanggalmulai) {
		this.tanggalmulai = tanggalmulai;
	}

	public List<Lampiranpenugasan> getLampiranpenugasans() {
		return this.lampiranpenugasans;
	}

	public void setLampiranpenugasans(List<Lampiranpenugasan> lampiranpenugasans) {
		this.lampiranpenugasans = lampiranpenugasans;
	}

	public Lampiranpenugasan addLampiranpenugasan(Lampiranpenugasan lampiranpenugasan) {
		getLampiranpenugasans().add(lampiranpenugasan);
		lampiranpenugasan.setPenugasan(this);

		return lampiranpenugasan;
	}

	public Lampiranpenugasan removeLampiranpenugasan(Lampiranpenugasan lampiranpenugasan) {
		getLampiranpenugasans().remove(lampiranpenugasan);
		lampiranpenugasan.setPenugasan(null);

		return lampiranpenugasan;
	}

	public Pegawai getPegawai() {
		return pegawai;
	}

	public void setPegawai(Pegawai pegawai) {
		this.pegawai = pegawai;
	}

	public Timestamp getCreated_time() {
		return created_time;
	}

	public void setCreated_time(Timestamp created_time) {
		this.created_time = created_time;
	}

	public Integer getCreated_by() {
		return created_by;
	}

	public void setCreated_by(Integer created_by) {
		this.created_by = created_by;
	}

	public Timestamp getLastmodified_time() {
		return lastmodified_time;
	}

	public void setLastmodified_time(Timestamp lastmodified_time) {
		this.lastmodified_time = lastmodified_time;
	}

	public Integer getLastmodified_by() {
		return lastmodified_by;
	}

	public void setLastmodified_by(Integer lastmodified_by) {
		this.lastmodified_by = lastmodified_by;
	}

	public Short getRow_status() {
		return row_status;
	}

	public void setRow_status(Short row_status) {
		this.row_status = row_status;
	}

	public Integer getIsmutation() {
		return ismutation;
	}

	public void setIsmutation(Integer ismutation) {
		this.ismutation = ismutation;
	}

}