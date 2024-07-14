package id.go.bpjskesehatan.service.v2.skpd.entitas;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import id.go.bpjskesehatan.Constant;
import id.go.bpjskesehatan.entitas.karyawan.Penugasan;
import id.go.bpjskesehatan.entitas.organisasi.Office;
import id.go.bpjskesehatan.service.v2.entitas.Akun;
import id.go.bpjskesehatan.service.v2.entitas.MataAnggaran;
import id.go.bpjskesehatan.service.v2.entitas.Program;
import id.go.bpjskesehatan.skpd.Jeniskendaraan;

public class Skpd {
	private Integer kode;
	private String nomor;
	
	@Column(name = "kodeoffice")
	@JsonProperty("office")
	private Office office;
	
	@Column(name = "kodeacara")
	@JsonProperty("acara")
	private Acara acara;
	
	private Timestamp tglajukan;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constant.ServerTimezone)
	private Date tglmulai;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constant.ServerTimezone)
	private Date tglselesai;
	private String deskripsi;
	private String keperluan;
	
	@Column(name = "kodeakun")
	@JsonProperty("akun")
	private Akun akun;
	
	@Column(name = "kodeprogram")
	@JsonProperty("program")
	private Program program;
	
	@Column(name = "kodejeniskendaraan")
	@JsonProperty("jeniskendaraan")
	private Jeniskendaraan jeniskendaraan;
	
	@Column(name = "pembuat")
	@JsonProperty("pembuat")
	private Penugasan pembuat;
	
	private Integer tujuan;
	private Integer jeniskegiatan;
	
	private Integer laporan_useract;
	private String lampiran;
	
	private String laporan_catatan_ringkas;
	private String laporan_tindak_lanjut;
	private String laporan_catatan_atasan;
	private String lampiran_kegiatan_skpd;
	private String lampiran_kegiatan_notulen;
	private String lampiran_kegiatan_foto;
	private String lampiran_catatan_revisi_peserta;
	private String lampiran_catatan_revisi_kehadiran;
	private String lampiran_catatan_foto;
	
	private Integer status;
	private String namastatus;
	private Integer useract;
	private ArrayList<ListPegawai> pegawai = new ArrayList<>();
	private ArrayList<ListPegawai> listpegawai = new ArrayList<>();
	private ArrayList<MataAnggaran> mataanggaran = new ArrayList<>();
	
	private Integer isverif;
	
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
	public Acara getAcara() {
		return acara;
	}
	public void setAcara(Acara acara) {
		this.acara = acara;
	}
	public Timestamp getTglajukan() {
		return tglajukan;
	}
	public void setTglajukan(Timestamp tglajukan) {
		this.tglajukan = tglajukan;
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
	public String getDeskripsi() {
		return deskripsi;
	}
	public void setDeskripsi(String deskripsi) {
		this.deskripsi = deskripsi;
	}
	public String getKeperluan() {
		return keperluan;
	}
	public void setKeperluan(String keperluan) {
		this.keperluan = keperluan;
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
	public Jeniskendaraan getJeniskendaraan() {
		return jeniskendaraan;
	}
	public void setJeniskendaraan(Jeniskendaraan jeniskendaraan) {
		this.jeniskendaraan = jeniskendaraan;
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
	public ArrayList<MataAnggaran> getMataanggaran() {
		return mataanggaran;
	}
	public void setMataanggaran(ArrayList<MataAnggaran> mataanggaran) {
		this.mataanggaran = mataanggaran;
	}
	public Integer getTujuan() {
		return tujuan;
	}
	public void setTujuan(Integer tujuan) {
		this.tujuan = tujuan;
	}
	public Integer getJeniskegiatan() {
		return jeniskegiatan;
	}
	public void setJeniskegiatan(Integer jeniskegiatan) {
		this.jeniskegiatan = jeniskegiatan;
	}
	public String getLampiran_kegiatan_skpd() {
		return lampiran_kegiatan_skpd;
	}
	public void setLampiran_kegiatan_skpd(String lampiran_kegiatan_skpd) {
		this.lampiran_kegiatan_skpd = lampiran_kegiatan_skpd;
	}
	public String getLampiran_kegiatan_notulen() {
		return lampiran_kegiatan_notulen;
	}
	public void setLampiran_kegiatan_notulen(String lampiran_kegiatan_notulen) {
		this.lampiran_kegiatan_notulen = lampiran_kegiatan_notulen;
	}
	public String getLampiran_kegiatan_foto() {
		return lampiran_kegiatan_foto;
	}
	public void setLampiran_kegiatan_foto(String lampiran_kegiatan_foto) {
		this.lampiran_kegiatan_foto = lampiran_kegiatan_foto;
	}
	public String getLampiran_catatan_revisi_peserta() {
		return lampiran_catatan_revisi_peserta;
	}
	public void setLampiran_catatan_revisi_peserta(String lampiran_catatan_revisi_peserta) {
		this.lampiran_catatan_revisi_peserta = lampiran_catatan_revisi_peserta;
	}
	public String getLampiran_catatan_revisi_kehadiran() {
		return lampiran_catatan_revisi_kehadiran;
	}
	public void setLampiran_catatan_revisi_kehadiran(String lampiran_catatan_revisi_kehadiran) {
		this.lampiran_catatan_revisi_kehadiran = lampiran_catatan_revisi_kehadiran;
	}
	public String getLampiran_catatan_foto() {
		return lampiran_catatan_foto;
	}
	public void setLampiran_catatan_foto(String lampiran_catatan_foto) {
		this.lampiran_catatan_foto = lampiran_catatan_foto;
	}
	public Integer getLaporan_useract() {
		return laporan_useract;
	}
	public void setLaporan_useract(Integer laporan_useract) {
		this.laporan_useract = laporan_useract;
	}
	public String getLaporan_catatan_ringkas() {
		return laporan_catatan_ringkas;
	}
	public void setLaporan_catatan_ringkas(String laporan_catatan_ringkas) {
		this.laporan_catatan_ringkas = laporan_catatan_ringkas;
	}
	public String getLaporan_tindak_lanjut() {
		return laporan_tindak_lanjut;
	}
	public void setLaporan_tindak_lanjut(String laporan_tindak_lanjut) {
		this.laporan_tindak_lanjut = laporan_tindak_lanjut;
	}
	public String getLaporan_catatan_atasan() {
		return laporan_catatan_atasan;
	}
	public void setLaporan_catatan_atasan(String laporan_catatan_atasan) {
		this.laporan_catatan_atasan = laporan_catatan_atasan;
	}
	public Integer getIsverif() {
		return isverif;
	}
	public void setIsverif(Integer isverif) {
		this.isverif = isverif;
	}
	
}
