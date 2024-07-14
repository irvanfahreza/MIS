package id.go.bpjskesehatan.service.v2.skpd.entitas;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonFormat;

import id.go.bpjskesehatan.Constant;
import id.go.bpjskesehatan.entitas.organisasi.Office;
import id.go.bpjskesehatan.entitas.referensi.Dati2;
import id.go.bpjskesehatan.service.v2.entitas.MataAnggaran;

public class Acara {
	private Integer kode;
	private Integer tujuan;
	private Integer jeniskegiatan;
	private String nama;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constant.ServerTimezone)
	private Date tglmulai;
	private String xtglmulai;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constant.ServerTimezone)
	private Date tglselesai;
	private String xtglselesai;
	private Office office;
	private Office pic;
	private Office officetujuan;
	private Dati2 dati2tujuan;
	private String tempat;
	private Time jam;
	private Integer createdby;
	private ArrayList<MataAnggaran> mataanggaran = new ArrayList<>();
	
	public Integer getKode() {
		return kode;
	}
	public void setKode(Integer kode) {
		this.kode = kode;
	}
	public String getNama() {
		return nama;
	}
	public void setNama(String nama) {
		this.nama = nama;
	}
	public Date getTglmulai() {
		return tglmulai;
	}
	public void setTglmulai(Date tglmulai) {
		this.tglmulai = tglmulai;
	}
	public String getXtglmulai() {
		return xtglmulai;
	}
	public void setXtglmulai(String xtglmulai) {
		this.xtglmulai = xtglmulai;
	}
	public Date getTglselesai() {
		return tglselesai;
	}
	public void setTglselesai(Date tglselesai) {
		this.tglselesai = tglselesai;
	}
	public String getXtglselesai() {
		return xtglselesai;
	}
	public void setXtglselesai(String xtglselesai) {
		this.xtglselesai = xtglselesai;
	}
	public Office getPic() {
		return pic;
	}
	public void setPic(Office pic) {
		this.pic = pic;
	}
	public Integer getTujuan() {
		return tujuan;
	}
	public void setTujuan(Integer tujuan) {
		this.tujuan = tujuan;
	}
	public Office getOfficetujuan() {
		return officetujuan;
	}
	public void setOfficetujuan(Office officetujuan) {
		this.officetujuan = officetujuan;
	}
	public Dati2 getDati2tujuan() {
		return dati2tujuan;
	}
	public void setDati2tujuan(Dati2 dati2tujuan) {
		this.dati2tujuan = dati2tujuan;
	}
	public String getTempat() {
		return tempat;
	}
	public void setTempat(String tempat) {
		this.tempat = tempat;
	}
	public Integer getJeniskegiatan() {
		return jeniskegiatan;
	}
	public void setJeniskegiatan(Integer jeniskegiatan) {
		this.jeniskegiatan = jeniskegiatan;
	}
	public Time getJam() {
		return jam;
	}
	public void setJam(Time jam) {
		this.jam = jam;
	}
	public ArrayList<MataAnggaran> getMataanggaran() {
		return mataanggaran;
	}
	public void setMataanggaran(ArrayList<MataAnggaran> mataanggaran) {
		this.mataanggaran = mataanggaran;
	}
	public Office getOffice() {
		return office;
	}
	public void setOffice(Office office) {
		this.office = office;
	}
	public Integer getCreatedby() {
		return createdby;
	}
	public void setCreatedby(Integer createdby) {
		this.createdby = createdby;
	}
	
}
