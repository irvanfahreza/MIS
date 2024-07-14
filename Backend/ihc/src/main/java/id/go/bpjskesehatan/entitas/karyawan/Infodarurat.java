package id.go.bpjskesehatan.entitas.karyawan;

import id.go.bpjskesehatan.entitas.GenericEntitas;

import java.io.Serializable;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.sql.Timestamp;

/**
 * The persistent class for the INFODARURAT database table.
 * 
 */
@JsonInclude(Include.NON_NULL)
@Entity
@NamedQuery(name = "Infodarurat.findAll", query = "SELECT i FROM Infodarurat i")
public class Infodarurat implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int kode;

	private String alamat;

	private String fax;

	private String hp;

	private String hp2;

	@Column(name = "kodehubungankeluarga")
	@JsonProperty("hubungankeluarga")
	private GenericEntitas hubungankeluarga;

	@Column(name = "kodepekerjaan")
	@JsonProperty("pekerjaan")
	private GenericEntitas pekerjaan;

	private String nama;

	private String telprumah;

	// bi-directional many-to-one association to Pegawai
	@JsonProperty("pegawai")
	@ManyToOne
	@JoinColumn(name = "NPP")
	private Pegawai pegawai;
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

	public Infodarurat() {
	}

	public int getKode() {
		return this.kode;
	}

	public void setKode(int kode) {
		this.kode = kode;
	}

	public String getAlamat() {
		return this.alamat;
	}

	public void setAlamat(String alamat) {
		this.alamat = alamat;
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

	public String getFax() {
		return this.fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getHp() {
		return this.hp;
	}

	public void setHp(String hp) {
		this.hp = hp;
	}

	public String getHp2() {
		return this.hp2;
	}

	public void setHp2(String hp2) {
		this.hp2 = hp2;
	}

	public GenericEntitas getHubungankeluarga() {
		return hubungankeluarga;
	}

	public void setHubungankeluarga(GenericEntitas hubungankeluarga) {
		this.hubungankeluarga = hubungankeluarga;
	}

	public GenericEntitas getPekerjaan() {
		return pekerjaan;
	}

	public void setPekerjaan(GenericEntitas pekerjaan) {
		this.pekerjaan = pekerjaan;
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

	public String getTelprumah() {
		return this.telprumah;
	}

	public void setTelprumah(String telprumah) {
		this.telprumah = telprumah;
	}

	public Pegawai getPegawai() {
		return this.pegawai;
	}

	public void setPegawai(Pegawai pegawai) {
		this.pegawai = pegawai;
	}

}