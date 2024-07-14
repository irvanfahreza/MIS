package id.go.bpjskesehatan.service.v2.skpd.entitas;

import java.util.ArrayList;

import id.go.bpjskesehatan.service.v2.entitas.MataAnggaran;

public class SkpdQR {
	
	private Integer kode;
	private String nomor;
	private String keperluan;
	private String tglmulai;
	private String tglselesai;
	private Integer lama;
	private String namajeniskendaraan;
	private String kotaasal;
	private String kotatujuan;
	private String pejabatberwenang;
	private String namapejabatberwenang;
	private String tempat;
	private String tglsetujui;
	private ArrayList<ListPegawaiQR> pegawai = new ArrayList<>();
	private ArrayList<ListPegawaiQR> listpegawai = new ArrayList<>();
	private ArrayList<MataAnggaranQR> mataanggaran = new ArrayList<>();
	
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
	public String getTglmulai() {
		return tglmulai;
	}
	public void setTglmulai(String tglmulai) {
		this.tglmulai = tglmulai;
	}
	public String getTglselesai() {
		return tglselesai;
	}
	public void setTglselesai(String tglselesai) {
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
	public ArrayList<ListPegawaiQR> getPegawai() {
		return pegawai;
	}
	public void setPegawai(ArrayList<ListPegawaiQR> pegawai) {
		this.pegawai = pegawai;
	}
	public ArrayList<ListPegawaiQR> getListpegawai() {
		return listpegawai;
	}
	public void setListpegawai(ArrayList<ListPegawaiQR> listpegawai) {
		this.listpegawai = listpegawai;
	}
	public ArrayList<MataAnggaranQR> getMataanggaran() {
		return mataanggaran;
	}
	public void setMataanggaran(ArrayList<MataAnggaranQR> mataanggaran) {
		this.mataanggaran = mataanggaran;
	}

}
