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
 * The persistent class for the INFOPENDIDIKAN database table.
 * 
 */
@JsonInclude(Include.NON_NULL)
@Entity
@NamedQuery(name = "Infopendidikan.findAll", query = "SELECT i FROM Infopendidikan i")
public class Infopendidikan implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int kode;

	private String catatan;

	@Column(name = "CREATED_BY")
	private Integer created_by;

	@Column(name = "CREATED_TIME")
	private Timestamp created_time;

	private String gelar;

	private String hasil;
	
	private String namasekolah;

	@JsonProperty("institusipendidikan")
	@Column(name = "kodeinstitusipendidikan")
	private GenericEntitas institusipendidikan;

	@JsonProperty("jurusanpendidikan")
	@Column(name = "kodejurusanpendidikan")
	private GenericEntitas jurusanpendidikan;
	
	@JsonProperty("kategoripendidikan")
	@Column(name = "kodekategoripendidikan")
	private GenericEntitas kategoripendidikan;
	
	/*@JsonProperty("subjurusanpendidikan")
	@Column(name = "kodesubjurusanpendidikan")
	private GenericEntitas subjurusanpendidikan;*/

	@JsonProperty("pendidikan")
	@Column(name = "kodependidikan")
	private GenericEntitas pendidikan;

	private String lampiran;
	private Integer adalampiran;

	@Column(name = "LASTMODIFIED_BY")
	private Integer lastmodified_by;

	@Column(name = "LASTMODIFIED_TIME")
	private Timestamp lastmodified_time;

	private String noijazah;

	private String npp;
	
	private String namakonsentrasi;

	@Temporal(TemporalType.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constant.ServerTimezone)
	private Date periodemulai;

	@Temporal(TemporalType.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constant.ServerTimezone)
	private Date periodeselesai;

	@Column(name = "ROW_STATUS")
	private Short row_status;

	@Temporal(TemporalType.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constant.ServerTimezone)
	private Date tglijazah;
	
	/*public GenericEntitas getSubjurusanpendidikan() {
		return subjurusanpendidikan;
	}

	public void setSubjurusanpendidikan(GenericEntitas subjurusanpendidikan) {
		this.subjurusanpendidikan = subjurusanpendidikan;
	}*/

	public Infopendidikan() {
	}

	public GenericEntitas getKategoripendidikan() {
		return kategoripendidikan;
	}

	public void setKategoripendidikan(GenericEntitas kategoripendidikan) {
		this.kategoripendidikan = kategoripendidikan;
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

	public String getGelar() {
		return this.gelar;
	}

	public void setGelar(String gelar) {
		this.gelar = gelar;
	}

	public String getHasil() {
		return this.hasil;
	}

	public void setHasil(String hasil) {
		this.hasil = hasil;
	}

	public GenericEntitas getInstitusipendidikan() {
		return institusipendidikan;
	}

	public void setInstitusipendidikan(GenericEntitas institusipendidikan) {
		this.institusipendidikan = institusipendidikan;
	}

	public GenericEntitas getJurusanpendidikan() {
		return jurusanpendidikan;
	}

	public void setJurusanpendidikan(GenericEntitas jurusanpendidikan) {
		this.jurusanpendidikan = jurusanpendidikan;
	}

	public GenericEntitas getPendidikan() {
		return pendidikan;
	}

	public void setPendidikan(GenericEntitas pendidikan) {
		this.pendidikan = pendidikan;
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

	public String getNoijazah() {
		return this.noijazah;
	}

	public void setNoijazah(String noijazah) {
		this.noijazah = noijazah;
	}

	public String getNpp() {
		return this.npp;
	}

	public void setNpp(String npp) {
		this.npp = npp;
	}

	public Date getPeriodemulai() {
		return this.periodemulai;
	}

	public void setPeriodemulai(Date periodemulai) {
		this.periodemulai = periodemulai;
	}

	public Date getPeriodeselesai() {
		return this.periodeselesai;
	}

	public void setPeriodeselesai(Date periodeselesai) {
		this.periodeselesai = periodeselesai;
	}

	public Short getRow_status() {
		return this.row_status;
	}

	public void setRow_status(Short row_status) {
		this.row_status = row_status;
	}

	public Date getTglijazah() {
		return this.tglijazah;
	}

	public void setTglijazah(Date tglijazah) {
		this.tglijazah = tglijazah;
	}

	public Integer getAdalampiran() {
		return adalampiran;
	}

	public void setAdalampiran(Integer adalampiran) {
		this.adalampiran = adalampiran;
	}

	public String getNamasekolah() {
		return namasekolah;
	}

	public void setNamasekolah(String namasekolah) {
		this.namasekolah = namasekolah;
	}

	public String getNamakonsentrasi() {
		return namakonsentrasi;
	}

	public void setNamakonsentrasi(String namakonsentrasi) {
		this.namakonsentrasi = namakonsentrasi;
	}
	
}