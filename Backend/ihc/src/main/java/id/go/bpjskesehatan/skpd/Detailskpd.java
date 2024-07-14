package id.go.bpjskesehatan.skpd;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import id.go.bpjskesehatan.entitas.karyawan.Pegawai;
import id.go.bpjskesehatan.entitas.organisasi.Jabatan;

import java.sql.Timestamp;

/**
 * The persistent class for the DETAILSKPD database table.
 * 
 */
@JsonInclude(Include.NON_NULL)
@Entity
@NamedQuery(name = "Detailskpd.findAll", query = "SELECT d FROM Detailskpd d")
public class Detailskpd implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int kode;

	@JsonProperty("created_by")
	@Column(name = "CREATED_BY")
	private int createdBy;

	@JsonProperty("created_time")
	@Column(name = "CREATED_TIME")
	private Timestamp createdTime;

	@JsonProperty("lastmodified_by")
	@Column(name = "LASTMODIFIED_BY")
	private int lastmodifiedBy;

	@JsonProperty("lastmodified_time")
	@Column(name = "LASTMODIFIED_TIME")
	private Timestamp lastmodifiedTime;

	@Column(name = "npp")
	private Pegawai pegawai;

	@Column(name = "kodejabatan")
	private Jabatan jabatan;

	@JsonProperty("row_status")
	@Column(name = "ROW_STATUS")
	private short rowStatus;

	// bi-directional many-to-one association to Skpd

	@ManyToOne
	@JoinColumn(name = "KODESKPD")
	private Skpd skpd;

	public Detailskpd() {
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

	public Pegawai getPegawai() {
		return pegawai;
	}

	public void setPegawai(Pegawai pegawai) {
		this.pegawai = pegawai;
	}

	public short getRowStatus() {
		return this.rowStatus;
	}

	public void setRowStatus(short rowStatus) {
		this.rowStatus = rowStatus;
	}

	public Skpd getSkpd() {
		return this.skpd;
	}

	public void setSkpd(Skpd skpd) {
		this.skpd = skpd;
	}

	public Jabatan getJabatan() {
		return jabatan;
	}

	public void setJabatan(Jabatan jabatan) {
		this.jabatan = jabatan;
	}

}