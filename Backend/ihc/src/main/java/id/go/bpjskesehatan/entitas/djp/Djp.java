package id.go.bpjskesehatan.entitas.djp;

import id.go.bpjskesehatan.entitas.organisasi.JobTitle;

import java.io.Serializable;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.sql.Timestamp;

/**
 * The persistent class for the DJP database table.
 * 
 */
@JsonInclude(Include.NON_NULL)
@JsonRootName("djp")
@Entity
@NamedQuery(name = "Djp.findAll", query = "SELECT d FROM Djp d")
public class Djp implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Integer kode;

	@Lob
	private String hubungankerja;

	@Lob
	private String ilustrasi;

	@JsonProperty("jobtitle")
	@ManyToOne
	@JoinColumn(name = "kodejobtitle")
	private JobTitle jobtitle;

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

	public Djp() {
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

	public String getHubungankerja() {
		return this.hubungankerja;
	}

	public void setHubungankerja(String hubungankerja) {
		this.hubungankerja = hubungankerja;
	}

	public String getIlustrasi() {
		return this.ilustrasi;
	}

	public void setIlustrasi(String ilustrasi) {
		this.ilustrasi = ilustrasi;
	}

	public JobTitle getJobtitle() {
		return jobtitle;
	}

	public void setJobtitle(JobTitle jobtitle) {
		this.jobtitle = jobtitle;
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

	public String getLingkungankerja() {
		return this.lingkungankerja;
	}

	public void setLingkungankerja(String lingkungankerja) {
		this.lingkungankerja = lingkungankerja;
	}

	public String getMisi() {
		return this.misi;
	}

	public void setMisi(String misi) {
		this.misi = misi;
	}

	public String getPersyaratan() {
		return this.persyaratan;
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