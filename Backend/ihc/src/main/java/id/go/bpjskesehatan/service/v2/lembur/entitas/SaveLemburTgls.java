package id.go.bpjskesehatan.service.v2.lembur.entitas;

import java.sql.Date;
import java.sql.Time;

import com.fasterxml.jackson.annotation.JsonFormat;

import id.go.bpjskesehatan.Constant;

public class SaveLemburTgls {

	private Integer kode;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constant.ServerTimezone)
	private Date tgl;
	private Integer deleted;
	private Integer vbatalkan;
	private Boolean batalkan;
	private Integer status;
	private Boolean viewcheck;
	
	private Integer kodepegawai;
	private String keterangan;
	private Time absenmasuk;
	private Time absenpulang;
	private Integer editjam;
	
	public Integer getKode() {
		return kode;
	}
	public void setKode(Integer kode) {
		this.kode = kode;
	}
	public Date getTgl() {
		return tgl;
	}
	public void setTgl(Date tgl) {
		this.tgl = tgl;
	}
	public Integer getDeleted() {
		return deleted;
	}
	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}
	public Boolean getBatalkan() {
		return batalkan;
	}
	public void setBatalkan(Boolean batalkan) {
		this.batalkan = batalkan;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getVbatalkan() {
		return vbatalkan;
	}
	public void setVbatalkan(Integer vbatalkan) {
		this.vbatalkan = vbatalkan;
	}
	public Boolean getViewcheck() {
		return viewcheck;
	}
	public void setViewcheck(Boolean viewcheck) {
		this.viewcheck = viewcheck;
	}
	public Integer getKodepegawai() {
		return kodepegawai;
	}
	public void setKodepegawai(Integer kodepegawai) {
		this.kodepegawai = kodepegawai;
	}
	public String getKeterangan() {
		return keterangan;
	}
	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}
	public Time getAbsenmasuk() {
		return absenmasuk;
	}
	public void setAbsenmasuk(Time absenmasuk) {
		this.absenmasuk = absenmasuk;
	}
	public Time getAbsenpulang() {
		return absenpulang;
	}
	public void setAbsenpulang(Time absenpulang) {
		this.absenpulang = absenpulang;
	}
	public Integer getEditjam() {
		return editjam;
	}
	public void setEditjam(Integer editjam) {
		this.editjam = editjam;
	}
}