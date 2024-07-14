package id.go.bpjskesehatan.entitas.karyawan;

import java.io.Serializable;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.sql.Timestamp;

/**
 * The persistent class for the INFODARURATHIST database table.
 * 
 */
@JsonInclude(Include.NON_NULL)
@Entity
@NamedQuery(name = "Infodarurathist.findAll", query = "SELECT i FROM Infodarurathist i")
public class Infodarurathist implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int kode;

	private String alamat;

	@JsonProperty("approved_by")
	@Column(name = "APPROVED_BY")
	private int approvedBy;

	private String fax;

	private short flagapprove;

	private String hp;

	private String hp2;

	private int kodehubungankeluarga;

	private int kodepekerjaan;

	private String nama;

	private String telprumah;

	private Timestamp tglapprove;

	// bi-directional many-to-one association to Pegawai
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

	public Infodarurathist() {
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

	public int getApprovedBy() {
		return this.approvedBy;
	}

	public void setApprovedBy(int approvedBy) {
		this.approvedBy = approvedBy;
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

	public short getFlagapprove() {
		return this.flagapprove;
	}

	public void setFlagapprove(short flagapprove) {
		this.flagapprove = flagapprove;
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

	public int getKodehubungankeluarga() {
		return this.kodehubungankeluarga;
	}

	public void setKodehubungankeluarga(int kodehubungankeluarga) {
		this.kodehubungankeluarga = kodehubungankeluarga;
	}

	public int getKodepekerjaan() {
		return this.kodepekerjaan;
	}

	public void setKodepekerjaan(int kodepekerjaan) {
		this.kodepekerjaan = kodepekerjaan;
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
		return row_status;
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

	public Timestamp getTglapprove() {
		return this.tglapprove;
	}

	public void setTglapprove(Timestamp tglapprove) {
		this.tglapprove = tglapprove;
	}

	public Pegawai getPegawai() {
		return this.pegawai;
	}

	public void setPegawai(Pegawai pegawai) {
		this.pegawai = pegawai;
	}

}