package id.go.bpjskesehatan.entitas.cuti;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import id.go.bpjskesehatan.Constant;
import id.go.bpjskesehatan.entitas.karyawan.Pegawai;

import java.sql.Date;
import java.sql.Timestamp;

@JsonInclude(Include.NON_NULL)
public class Cuti implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Integer kode;
	private Tipe tipe;
	private Integer kodetipe;
	private String npp;
	private Timestamp tglajukan;
	private Pegawai pegawai;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constant.ServerTimezone)
	private Date tglmulai;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constant.ServerTimezone)
	private Date tglselesai;
	private Integer jmlhari;
	private String deskripsi;
	private Integer statuspersetujuan;
	private String disetujuioleh;
	private Timestamp tglsetuju;
	private Short row_status;
	private Integer created_by;
	private Timestamp created_time;
	private Integer lastmodified_by;
	private Timestamp lastmodified_time;
	
	public String getDisetujuioleh() {
		return disetujuioleh;
	}
	public void setDisetujuioleh(String disetujuioleh) {
		this.disetujuioleh = disetujuioleh;
	}
	public Timestamp getTglsetuju() {
		return tglsetuju;
	}
	public void setTglsetuju(Timestamp tglsetuju) {
		this.tglsetuju = tglsetuju;
	}
	public Integer getKodetipe() {
		return kodetipe;
	}
	public void setKodetipe(Integer kodetipe) {
		this.kodetipe = kodetipe;
	}
	public String getNpp() {
		return npp;
	}
	public void setNpp(String npp) {
		this.npp = npp;
	}
	public Integer getKode() {
		return kode;
	}
	public void setKode(Integer kode) {
		this.kode = kode;
	}
	public Tipe getTipe() {
		return tipe;
	}
	public void setTipe(Tipe tipe) {
		this.tipe = tipe;
	}
	public Timestamp getTglajukan() {
		return tglajukan;
	}
	public void setTglajukan(Timestamp tglajukan) {
		this.tglajukan = tglajukan;
	}
	public Pegawai getPegawai() {
		return pegawai;
	}
	public void setPegawai(Pegawai pegawai) {
		this.pegawai = pegawai;
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
	public Integer getJmlhari() {
		return jmlhari;
	}
	public void setJmlhari(Integer jmlhari) {
		this.jmlhari = jmlhari;
	}
	public String getDeskripsi() {
		return deskripsi;
	}
	public void setDeskripsi(String deskripsi) {
		this.deskripsi = deskripsi;
	}
	public Integer getStatuspersetujuan() {
		return statuspersetujuan;
	}
	public void setStatuspersetujuan(Integer statuspersetujuan) {
		this.statuspersetujuan = statuspersetujuan;
	}
	public Short getRow_status() {
		return row_status;
	}
	public void setRow_status(Short row_status) {
		this.row_status = row_status;
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
	
}