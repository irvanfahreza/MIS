package id.go.bpjskesehatan.entitas.kinerja;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import id.go.bpjskesehatan.Constant;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the PERIODEKINERJA database table.
 * 
 */
@Entity
@JsonInclude(Include.NON_NULL)
@NamedQuery(name="Periodekinerja.findAll", query="SELECT p FROM Periodekinerja p")
public class Periodekinerja implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Integer kode;

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

	private Integer status;
	private String namastatus;
	
	private Integer hasilkerja;
	private Integer kompetensi;
	private Integer komitmen;
	private Integer tugastambahan;
	private Integer kejadiankritis;
	private Integer generated;
	private Integer publish;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constant.ServerTimezone)
	private Date tglmulai;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constant.ServerTimezone)
	private Date tglselesai;

	//bi-directional many-to-one association to Bobotkomponen
	@OneToMany(mappedBy="periodekinerja")
	private List<Bobotkomponen> bobotkomponens;

	//bi-directional many-to-one association to Kriteria
	@OneToMany(mappedBy="periodekinerja")
	private List<Kriteria> kriterias;

	//bi-directional many-to-one association to Periodekinerjadetail
	@OneToMany(mappedBy="periodekinerja")
	private List<Periodekinerjadetail> periodekinerjadetails;

	public Periodekinerja() {
	}

	public Integer getKode() {
		return this.kode;
	}

	public void setKode(Integer kode) {
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getNamastatus() {
		return namastatus;
	}

	public void setNamastatus(String namastatus) {
		this.namastatus = namastatus;
	}

	public Integer getHasilkerja() {
		return hasilkerja;
	}

	public void setHasilkerja(Integer hasilkerja) {
		this.hasilkerja = hasilkerja;
	}

	public Integer getKompetensi() {
		return kompetensi;
	}

	public void setKompetensi(Integer kompetensi) {
		this.kompetensi = kompetensi;
	}

	public Integer getKomitmen() {
		return komitmen;
	}

	public void setKomitmen(Integer komitmen) {
		this.komitmen = komitmen;
	}

	public Integer getTugastambahan() {
		return tugastambahan;
	}

	public void setTugastambahan(Integer tugastambahan) {
		this.tugastambahan = tugastambahan;
	}

	public Integer getKejadiankritis() {
		return kejadiankritis;
	}

	public void setKejadiankritis(Integer kejadiankritis) {
		this.kejadiankritis = kejadiankritis;
	}

	public Integer getGenerated() {
		return generated;
	}

	public void setGenerated(Integer generated) {
		this.generated = generated;
	}

	public Integer getPublish() {
		return publish;
	}

	public void setPublish(Integer publish) {
		this.publish = publish;
	}

	public Date getTglmulai() {
		return tglmulai;
	}

	public void setTglmulai(Date tglmulai) {
		this.tglmulai = tglmulai;
	}

	public Date getTglselesai() {
		return tglselesai;
	}

	public void setTglselesai(Date tglselesai) {
		this.tglselesai = tglselesai;
	}

	public List<Bobotkomponen> getBobotkomponens() {
		return this.bobotkomponens;
	}

	public void setBobotkomponens(List<Bobotkomponen> bobotkomponens) {
		this.bobotkomponens = bobotkomponens;
	}

	public Bobotkomponen addBobotkomponen(Bobotkomponen bobotkomponen) {
		getBobotkomponens().add(bobotkomponen);
		bobotkomponen.setPeriodekinerja(this);

		return bobotkomponen;
	}

	public Bobotkomponen removeBobotkomponen(Bobotkomponen bobotkomponen) {
		getBobotkomponens().remove(bobotkomponen);
		bobotkomponen.setPeriodekinerja(null);

		return bobotkomponen;
	}

	public List<Kriteria> getKriterias() {
		return this.kriterias;
	}

	public void setKriterias(List<Kriteria> kriterias) {
		this.kriterias = kriterias;
	}

	public Kriteria addKriteria(Kriteria kriteria) {
		getKriterias().add(kriteria);
		kriteria.setPeriodekinerja(this);

		return kriteria;
	}

	public Kriteria removeKriteria(Kriteria kriteria) {
		getKriterias().remove(kriteria);
		kriteria.setPeriodekinerja(null);

		return kriteria;
	}

	public List<Periodekinerjadetail> getPeriodekinerjadetails() {
		return this.periodekinerjadetails;
	}

	public void setPeriodekinerjadetails(List<Periodekinerjadetail> periodekinerjadetails) {
		this.periodekinerjadetails = periodekinerjadetails;
	}

	public Periodekinerjadetail addPeriodekinerjadetail(Periodekinerjadetail periodekinerjadetail) {
		getPeriodekinerjadetails().add(periodekinerjadetail);
		periodekinerjadetail.setPeriodekinerja(this);

		return periodekinerjadetail;
	}

	public Periodekinerjadetail removePeriodekinerjadetail(Periodekinerjadetail periodekinerjadetail) {
		getPeriodekinerjadetails().remove(periodekinerjadetail);
		periodekinerjadetail.setPeriodekinerja(null);

		return periodekinerjadetail;
	}

}