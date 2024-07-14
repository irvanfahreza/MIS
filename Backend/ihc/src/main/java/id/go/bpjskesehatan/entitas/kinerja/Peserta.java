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
import com.fasterxml.jackson.annotation.JsonProperty;

import id.go.bpjskesehatan.entitas.karyawan.Penugasan;

/**
 * The persistent class for the PESERTA database table.
 * 
 */
@JsonInclude(Include.NON_NULL)
@Entity
@NamedQuery(name = "Peserta.findAll", query = "SELECT p FROM Peserta p")
public class Peserta implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Integer kode;

	@Column(name = "CREATED_BY")
	private Integer created_by;

	@Column(name = "CREATED_TIME")
	private Timestamp created_time;

	private Integer kodepenugasan;

	@Column(name = "LASTMODIFIED_BY")
	private Integer lastmodified_by;

	@Column(name = "LASTMODIFIED_TIME")
	private Timestamp lastmodified_time;

	@Column(name = "ROW_STATUS")
	private Short row_status;

	private short statuskinerja;
	
	private String namastatuskinerja;
	
	private short invited;
	
	private String kodegrade;
	private String namagrade;
	
	private String kodejobprefix;
	private String namajobprefix;
	
	private float bobotmutasi;
	
	private String pic;
	private String picnama;

	// bi-directional many-to-one association to Peserta
	@JsonProperty("periodekinerja")
	@ManyToOne
	@JoinColumn(name = "KODEPERIODEKINERJA")
	private Periodekinerja periodekinerja;

	// bi-directional many-to-one association to Peserta
	@OneToMany(mappedBy = "peserta")
	private List<Peserta> pesertas;
	
	private Penugasan penugasan;
	
	private Integer kodeperencanaan;
	private Integer kodepembinaan;
	private Integer kodeevaluasi;
	private Integer kodeverifikasi;

	public Peserta() {
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

	public Integer getKodepenugasan() {
		return this.kodepenugasan;
	}

	public void setKodepenugasan(Integer kodepenugasan) {
		this.kodepenugasan = kodepenugasan;
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

	public short getStatuskinerja() {
		return this.statuskinerja;
	}

	public void setStatuskinerja(short statuskinerja) {
		this.statuskinerja = statuskinerja;
	}

	public String getNamastatuskinerja() {
		return namastatuskinerja;
	}

	public void setNamastatuskinerja(String namastatuskinerja) {
		this.namastatuskinerja = namastatuskinerja;
	}

	public Periodekinerja getPeriodekinerja() {
		return periodekinerja;
	}

	public void setPeriodekinerja(Periodekinerja periodekinerja) {
		this.periodekinerja = periodekinerja;
	}

	public List<Peserta> getPesertas() {
		return this.pesertas;
	}

	public void setPesertas(List<Peserta> pesertas) {
		this.pesertas = pesertas;
	}

	public Penugasan getPenugasan() {
		return penugasan;
	}

	public void setPenugasan(Penugasan penugasan) {
		this.penugasan = penugasan;
	}

	public short getInvited() {
		return invited;
	}

	public void setInvited(short invited) {
		this.invited = invited;
	}

	public String getKodegrade() {
		return kodegrade;
	}

	public void setKodegrade(String kodegrade) {
		this.kodegrade = kodegrade;
	}

	public String getNamagrade() {
		return namagrade;
	}

	public void setNamagrade(String namagrade) {
		this.namagrade = namagrade;
	}

	public String getKodejobprefix() {
		return kodejobprefix;
	}

	public void setKodejobprefix(String kodejobprefix) {
		this.kodejobprefix = kodejobprefix;
	}

	public String getNamajobprefix() {
		return namajobprefix;
	}

	public void setNamajobprefix(String namajobprefix) {
		this.namajobprefix = namajobprefix;
	}

	public float getBobotmutasi() {
		return bobotmutasi;
	}

	public void setBobotmutasi(float bobotmutasi) {
		this.bobotmutasi = bobotmutasi;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getPicnama() {
		return picnama;
	}

	public void setPicnama(String picnama) {
		this.picnama = picnama;
	}

	public Integer getKodeperencanaan() {
		return kodeperencanaan;
	}

	public void setKodeperencanaan(Integer kodeperencanaan) {
		this.kodeperencanaan = kodeperencanaan;
	}

	public Integer getKodepembinaan() {
		return kodepembinaan;
	}

	public void setKodepembinaan(Integer kodepembinaan) {
		this.kodepembinaan = kodepembinaan;
	}

	public Integer getKodeevaluasi() {
		return kodeevaluasi;
	}

	public void setKodeevaluasi(Integer kodeevaluasi) {
		this.kodeevaluasi = kodeevaluasi;
	}

	public Integer getKodeverifikasi() {
		return kodeverifikasi;
	}

	public void setKodeverifikasi(Integer kodeverifikasi) {
		this.kodeverifikasi = kodeverifikasi;
	}
	
}