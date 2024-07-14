package id.go.bpjskesehatan.service.v2.cuti.entitas;

import java.sql.Date;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import id.go.bpjskesehatan.Constant;
import id.go.bpjskesehatan.service.v2.entitas.MataAnggaran;
import id.go.bpjskesehatan.service.v2.skpd.entitas.ListPegawai;

@JsonInclude(Include.NON_NULL)
public class Skpd {
	
	private Integer kode;
	private String nomor;
	private String keperluan;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constant.ServerTimezone)
	private Date tglmaulai;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constant.ServerTimezone)
	private Date tglselesai;
	private Integer lama;
	private String namajeniskendaraan;
	private String kotaasal;
	private String kotatujuan;
	private String pejabatberwenang;
	private String namapejabatberwenang;
	private String tempat;
	private String tglsetujui;
	private ArrayList<ListPegawai> pegawai = new ArrayList<>();
	private ArrayList<ListPegawai> listpegawai = new ArrayList<>();
	private ArrayList<MataAnggaran> mataanggaran = new ArrayList<>();
	
	public Integer getKode() {
		return kode;
	}
	public void setKode(Integer kode) {
		this.kode = kode;
	}
	public String getNomor() {
		return nomor;
	}
	public void setNomor(String nomor) {
		this.nomor = nomor;
	}
	public String getKeperluan() {
		return keperluan;
	}
	public void setKeperluan(String keperluan) {
		this.keperluan = keperluan;
	}
	public Date getTglmaulai() {
		return tglmaulai;
	}
	public void setTglmaulai(Date tglmaulai) {
		this.tglmaulai = tglmaulai;
	}
	public Date getTglselesai() {
		return tglselesai;
	}
	public void setTglselesai(Date tglselesai) {
		this.tglselesai = tglselesai;
	}
	public Integer getLama() {
		return lama;
	}
	public void setLama(Integer lama) {
		this.lama = lama;
	}
	public String getNamajeniskendaraan() {
		return namajeniskendaraan;
	}
	public void setNamajeniskendaraan(String namajeniskendaraan) {
		this.namajeniskendaraan = namajeniskendaraan;
	}
	public String getKotaasal() {
		return kotaasal;
	}
	public void setKotaasal(String kotaasal) {
		this.kotaasal = kotaasal;
	}
	public String getKotatujuan() {
		return kotatujuan;
	}
	public void setKotatujuan(String kotatujuan) {
		this.kotatujuan = kotatujuan;
	}
	public String getPejabatberwenang() {
		return pejabatberwenang;
	}
	public void setPejabatberwenang(String pejabatberwenang) {
		this.pejabatberwenang = pejabatberwenang;
	}
	public String getNamapejabatberwenang() {
		return namapejabatberwenang;
	}
	public void setNamapejabatberwenang(String namapejabatberwenang) {
		this.namapejabatberwenang = namapejabatberwenang;
	}
	public String getTempat() {
		return tempat;
	}
	public void setTempat(String tempat) {
		this.tempat = tempat;
	}
	public String getTglsetujui() {
		return tglsetujui;
	}
	public void setTglsetujui(String tglsetujui) {
		this.tglsetujui = tglsetujui;
	}
	public ArrayList<ListPegawai> getPegawai() {
		return pegawai;
	}
	public void setPegawai(ArrayList<ListPegawai> pegawai) {
		this.pegawai = pegawai;
	}
	public ArrayList<ListPegawai> getListpegawai() {
		return listpegawai;
	}
	public void setListpegawai(ArrayList<ListPegawai> listpegawai) {
		this.listpegawai = listpegawai;
	}
	public ArrayList<MataAnggaran> getMataanggaran() {
		return mataanggaran;
	}
	public void setMataanggaran(ArrayList<MataAnggaran> mataanggaran) {
		this.mataanggaran = mataanggaran;
	}
	

}
