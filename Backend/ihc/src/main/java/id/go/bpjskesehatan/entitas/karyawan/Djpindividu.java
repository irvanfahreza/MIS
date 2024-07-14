package id.go.bpjskesehatan.entitas.karyawan;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Lob;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Djpindividu implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// @JsonProperty("penugasan")
	// @Column(name = "kodepenugasan")
	// private Penugasan penugasan;
	private Integer kodepenugasan;

	@Id
	private Integer kode;
	
	private String npp;
	private String nama;
	private String kodejobtitle;
	private String namajobtitle;
	private String kodejabatan;
	private String namajabatan;
	private String kodeunitkerja;
	private String namaunitkerja;

	@Lob
	private String hubungankerja;

	@Lob
	private String ilustrasi;

	@Lob
	private String lingkungankerja;

	@Lob
	private String misi;

	@Lob
	private String persyaratan;
	@JsonProperty("created_by")
	@Column(name = "CREATED_BY")
	private Integer created_by;

	@JsonProperty("created_time")
	@Column(name = "CREATED_TIME")
	private Timestamp created_time;

	@JsonProperty("lastmodified_by")
	@Column(name = "LASTMODIFIED_BY")
	private Integer lastmodified_by;

	@JsonProperty("lastmodified_time")
	@Column(name = "LASTMODIFIED_TIME")
	private Timestamp lastmodified_time;

	@JsonProperty("row_status")
	@Column(name = "ROW_STATUS")
	private Short row_status;

	public Integer getKodepenugasan() {
		return kodepenugasan;
	}

	public void setKodepenugasan(Integer kodepenugasan) {
		this.kodepenugasan = kodepenugasan;
	}

	public Integer getKode() {
		return kode;
	}

	public void setKode(Integer kode) {
		this.kode = kode;
	}

	public String getNpp() {
		return npp;
	}

	public void setNpp(String npp) {
		this.npp = npp;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public String getKodejobtitle() {
		return kodejobtitle;
	}

	public void setKodejobtitle(String kodejobtitle) {
		this.kodejobtitle = kodejobtitle;
	}

	public String getNamajobtitle() {
		return namajobtitle;
	}

	public void setNamajobtitle(String namajobtitle) {
		this.namajobtitle = namajobtitle;
	}

	public String getKodejabatan() {
		return kodejabatan;
	}

	public void setKodejabatan(String kodejabatan) {
		this.kodejabatan = kodejabatan;
	}

	public String getNamajabatan() {
		return namajabatan;
	}

	public void setNamajabatan(String namajabatan) {
		this.namajabatan = namajabatan;
	}

	public String getKodeunitkerja() {
		return kodeunitkerja;
	}

	public void setKodeunitkerja(String kodeunitkerja) {
		this.kodeunitkerja = kodeunitkerja;
	}

	public String getNamaunitkerja() {
		return namaunitkerja;
	}

	public void setNamaunitkerja(String namaunitkerja) {
		this.namaunitkerja = namaunitkerja;
	}

	public Integer getCreated_by() {
		return created_by;
	}

	public void setCreated_by(Integer created_by) {
		this.created_by = created_by;
	}

	public Timestamp getCreated_time() {
		return created_time;
	}

	public void setCreated_time(Timestamp created_time) {
		this.created_time = created_time;
	}

	public String getHubungankerja() {
		return hubungankerja;
	}

	public void setHubungankerja(String hubungankerja) {
		this.hubungankerja = hubungankerja;
	}

	public String getIlustrasi() {
		return ilustrasi;
	}

	public void setIlustrasi(String ilustrasi) {
		this.ilustrasi = ilustrasi;
	}

	public Integer getLastmodified_by() {
		return lastmodified_by;
	}

	public void setLastmodified_by(Integer lastmodified_by) {
		this.lastmodified_by = lastmodified_by;
	}

	public Timestamp getLastmodified_time() {
		return lastmodified_time;
	}

	public void setLastmodified_time(Timestamp lastmodified_time) {
		this.lastmodified_time = lastmodified_time;
	}

	public String getLingkungankerja() {
		return lingkungankerja;
	}

	public void setLingkungankerja(String lingkungankerja) {
		this.lingkungankerja = lingkungankerja;
	}

	public String getMisi() {
		return misi;
	}

	public void setMisi(String misi) {
		this.misi = misi;
	}

	public String getPersyaratan() {
		return persyaratan;
	}

	public void setPersyaratan(String persyaratan) {
		this.persyaratan = persyaratan;
	}

	public Short getRow_status() {
		return row_status;
	}

	public void setRow_status(Short row_status) {
		this.row_status = row_status;
	}

}
