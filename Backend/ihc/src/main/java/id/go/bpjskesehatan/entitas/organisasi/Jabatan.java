package id.go.bpjskesehatan.entitas.organisasi;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import com.fasterxml.jackson.annotation.JsonProperty;

import id.go.bpjskesehatan.entitas.karyawan.Penugasan;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 *
 * @author Bambang Purwanto
 */
@JsonInclude(Include.NON_NULL)
@Entity
public class Jabatan implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "kode")
	private String kode;
	@Column(name = "nama")
	private String nama;
	@Column(name = "kodeparent")
	@JsonProperty("parent")
	private Jabatan parent;
	@Column(name = "isneck")
	private String isneck;
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
	@JsonProperty("jobtitle")
	@JoinColumn(name = "kodejobtitle", referencedColumnName = "kode")
	@ManyToOne
	private JobTitle jobtitle;
	@JsonProperty("unitkerja")
	@JoinColumn(name = "kodeunitkerja", referencedColumnName = "kode")
	@ManyToOne
	private UnitKerja unitkerja;
	private Integer jumlahjabatan;
	private Penugasan penugasan;
	private UnitKerja deputi;

	public UnitKerja getDeputi() {
		return deputi;
	}

	public void setDeputi(UnitKerja deputi) {
		this.deputi = deputi;
	}

	public Jabatan() {
	}

	public String getKode() {
		return kode;
	}

	public void setKode(String kode) {
		this.kode = kode;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public Jabatan getParent() {
		return parent;
	}

	public void setParent(Jabatan parent) {
		this.parent = parent;
	}

	public String getIsneck() {
		return isneck;
	}

	public void setIsneck(String isneck) {
		this.isneck = isneck;
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

	public JobTitle getJobtitle() {
		return jobtitle;
	}

	public void setJobtitle(JobTitle jobtitle) {
		this.jobtitle = jobtitle;
	}

	public UnitKerja getUnitkerja() {
		return unitkerja;
	}

	public void setUnitkerja(UnitKerja unitkerja) {
		this.unitkerja = unitkerja;
	}

	public Integer getJumlahjabatan() {
		return jumlahjabatan;
	}

	public void setJumlahjabatan(Integer jumlahjabatan) {
		this.jumlahjabatan = jumlahjabatan;
	}

	public Penugasan getPenugasan() {
		return penugasan;
	}

	public void setPenugasan(Penugasan penugasan) {
		this.penugasan = penugasan;
	}

}
