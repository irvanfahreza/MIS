package id.go.bpjskesehatan.entitas.kinerja;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import id.go.bpjskesehatan.Constant;

import java.util.Date;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the PERIODEUBKOMPETENSI database table.
 * 
 */
@Entity
@JsonInclude(Include.NON_NULL)
@NamedQuery(name="Periodeubkompetensi.findAll", query="SELECT p FROM Periodeubkompetensi p")
public class Periodeubkompetensi implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int kode;

	@Column(name="CREATED_BY")
	private Integer created_by;

	@Column(name="CREATED_TIME")
	private Timestamp created_time;

	@Column(name="LASTMODIFIED_BY")
	private Integer lastmodified_by;

	@Column(name="LASTMODIFIED_TIME")
	private Timestamp lastmodified_time;

	private String nama;

	@Column(name="ROW_STATUS")
	private Short row_status;

	@Column(name="STATUS")
	private short status;

	@Temporal(TemporalType.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constant.ServerTimezone)
	private Date tglmulai;

	@Temporal(TemporalType.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constant.ServerTimezone)
	private Date tglselesai;
	
	private String statusperiode;

	//bi-directional many-to-one association to Periodekinerja
	@ManyToOne
	@JoinColumn(name="KODEPERIODEKINERJA")
	private Periodekinerja periodekinerja;

	//bi-directional many-to-one association to Ubkompetensikriteriapenilaian
	@OneToMany(mappedBy="periodeubkompetensi")
	private List<Ubkompetensikriteriapenilaian> ubkompetensikriteriapenilaians;

	public Periodeubkompetensi() {
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

	public short getStatus() {
		return this.status;
	}

	public void setStatus(short status) {
		this.status = status;
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

	public String getStatusperiode() {
		return statusperiode;
	}

	public void setStatusperiode(String statusperiode) {
		this.statusperiode = statusperiode;
	}

	public Periodekinerja getPeriodekinerja() {
		return this.periodekinerja;
	}

	public void setPeriodekinerja(Periodekinerja periodekinerja) {
		this.periodekinerja = periodekinerja;
	}

	public List<Ubkompetensikriteriapenilaian> getUbkompetensikriteriapenilaians() {
		return this.ubkompetensikriteriapenilaians;
	}

	public void setUbkompetensikriteriapenilaians(List<Ubkompetensikriteriapenilaian> ubkompetensikriteriapenilaians) {
		this.ubkompetensikriteriapenilaians = ubkompetensikriteriapenilaians;
	}

	public Ubkompetensikriteriapenilaian addUbkompetensikriteriapenilaian(Ubkompetensikriteriapenilaian ubkompetensikriteriapenilaian) {
		getUbkompetensikriteriapenilaians().add(ubkompetensikriteriapenilaian);
		ubkompetensikriteriapenilaian.setPeriodeubkompetensi(this);

		return ubkompetensikriteriapenilaian;
	}

	public Ubkompetensikriteriapenilaian removeUbkompetensikriteriapenilaian(Ubkompetensikriteriapenilaian ubkompetensikriteriapenilaian) {
		getUbkompetensikriteriapenilaians().remove(ubkompetensikriteriapenilaian);
		ubkompetensikriteriapenilaian.setPeriodeubkompetensi(null);

		return ubkompetensikriteriapenilaian;
	}

}