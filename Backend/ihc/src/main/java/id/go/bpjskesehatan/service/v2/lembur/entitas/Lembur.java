package id.go.bpjskesehatan.service.v2.lembur.entitas;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonProperty;


import id.go.bpjskesehatan.entitas.karyawan.Penugasan;
import id.go.bpjskesehatan.entitas.organisasi.Office;
import id.go.bpjskesehatan.service.v2.entitas.Akun;
import id.go.bpjskesehatan.service.v2.entitas.Program;

public class Lembur {
	private Integer kode;
	private String nomor;
	
	@Column(name = "kodeoffice")
	@JsonProperty("office")
	private Office office;
	
	private String kodepic;
	private Timestamp tglajukan;
	private String deskripsi;
	private String namakegiatan;
	private String catatantolak;
	
	@Column(name = "kodeakun")
	@JsonProperty("akun")
	private Akun akun;
	
	@Column(name = "kodeprogram")
	@JsonProperty("program")
	private Program program;	
	
	@Column(name = "pembuat")
	@JsonProperty("pembuat")
	private Penugasan pembuat;
	
	private String lampiran;
	private Integer status;
	private String namastatus;
	private Integer useract;
	private ArrayList<ListPegawai> pegawai = new ArrayList<>();
	private ArrayList<ListPegawai> listpegawai = new ArrayList<>();
	private ArrayList<SaveLemburTgls> tgls = new ArrayList<>();
	private String tgllembur;
	private String pegawailembur;
	private Integer ispembatalan;
	
	private Integer isverif;
	private String namaverif;
	private Integer laporan_useract;
	private String laporan_isi;
	private String laporan_lampiran;
	
	public Integer getKode() {
		return kode;
	}
	public void setKode(Integer kode) {
		this.kode = kode;
	}
	public Office getOffice() {
		return office;
	}
	public void setOffice(Office office) {
		this.office = office;
	}
	public Timestamp getTglajukan() {
		return tglajukan;
	}
	public void setTglajukan(Timestamp tglajukan) {
		this.tglajukan = tglajukan;
	}
	public String getDeskripsi() {
		return deskripsi;
	}
	public void setDeskripsi(String deskripsi) {
		this.deskripsi = deskripsi;
	}
	public Akun getAkun() {
		return akun;
	}
	public void setAkun(Akun akun) {
		this.akun = akun;
	}
	public Program getProgram() {
		return program;
	}
	public void setProgram(Program program) {
		this.program = program;
	}
	public String getLampiran() {
		return lampiran;
	}
	public void setLampiran(String lampiran) {
		this.lampiran = lampiran;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
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
	public Integer getUseract() {
		return useract;
	}
	public void setUseract(Integer useract) {
		this.useract = useract;
	}
	public String getNamastatus() {
		return namastatus;
	}
	public void setNamastatus(String namastatus) {
		this.namastatus = namastatus;
	}
	public Penugasan getPembuat() {
		return pembuat;
	}
	public void setPembuat(Penugasan pembuat) {
		this.pembuat = pembuat;
	}
	public String getNomor() {
		return nomor;
	}
	public void setNomor(String nomor) {
		this.nomor = nomor;
	}
	public ArrayList<SaveLemburTgls> getTgls() {
		return tgls;
	}
	public void setTgls(ArrayList<SaveLemburTgls> tgls) {
		this.tgls = tgls;
	}
	public String getTgllembur() {
		return tgllembur;
	}
	public void setTgllembur(String tgllembur) {
		this.tgllembur = tgllembur;
	}
	public String getPegawailembur() {
		return pegawailembur;
	}
	public void setPegawailembur(String pegawailembur) {
		this.pegawailembur = pegawailembur;
	}
	public String getNamakegiatan() {
		return namakegiatan;
	}
	public void setNamakegiatan(String namakegiatan) {
		this.namakegiatan = namakegiatan;
	}
	public Integer getIspembatalan() {
		return ispembatalan;
	}
	public void setIspembatalan(Integer ispembatalan) {
		this.ispembatalan = ispembatalan;
	}
	public String getKodepic() {
		return kodepic;
	}
	public void setKodepic(String kodepic) {
		this.kodepic = kodepic;
	}
	public String getCatatantolak() {
		return catatantolak;
	}
	public void setCatatantolak(String catatantolak) {
		this.catatantolak = catatantolak;
	}
	public Integer getIsverif() {
		return isverif;
	}
	public void setIsverif(Integer isverif) {
		this.isverif = isverif;
	}
	public String getNamaverif() {
		return namaverif;
	}
	public void setNamaverif(String namaverif) {
		this.namaverif = namaverif;
	}
	public Integer getLaporan_useract() {
		return laporan_useract;
	}
	public void setLaporan_useract(Integer laporan_useract) {
		this.laporan_useract = laporan_useract;
	}
	public String getLaporan_isi() {
		return laporan_isi;
	}
	public void setLaporan_isi(String laporan_isi) {
		this.laporan_isi = laporan_isi;
	}
	public String getLaporan_lampiran() {
		return laporan_lampiran;
	}
	public void setLaporan_lampiran(String laporan_lampiran) {
		this.laporan_lampiran = laporan_lampiran;
	}
	
}
