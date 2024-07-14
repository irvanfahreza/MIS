package id.go.bpjskesehatan.service.v2.promut.entitas;

import java.sql.Date;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonFormat;

import id.go.bpjskesehatan.Constant;
import id.go.bpjskesehatan.service.v2.skpd.entitas.ListPegawaiQR;

public class PenugasanLama {

	//private Integer kode;
	private String npp;
	private String namalengkap;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constant.ServerTimezone)
	private Date tgllahir;
	private Integer usia;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constant.ServerTimezone)
	private Date tanggalmpp;
	private Integer tahunmasakerja;
	private Integer bulanmasakerja;
	private String kota1;
	private String kota2;
	private Integer kodepenugasan;
	private String jabatan;
	private String kantor;
	private String pangkat;
	private String grade;
	private String subgrade;
	private String wilayah;
	private String kelaskantor;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constant.ServerTimezone)
	private Date tanggalsk;
	private Integer tahunmasajabatan;
	private Integer bulanmasajabatan;
	private Float ccinett;
	private Float poin;
	private Integer poinltp;
	private Integer kodediagramtalenta;
	private Integer poindiagramtalenta;
	private String posisidiagramtalenta;
	private ArrayList<ListPendidikan> pendidikan = new ArrayList<>();
	private ArrayList<ListPredikat> predikat = new ArrayList<>();
	private ArrayList<ListPelanggaran> pelanggaran = new ArrayList<>();
	private ArrayList<ListPengalaman> pengalaman = new ArrayList<>();
	private ArrayList<ListPelatihan> pelatihan = new ArrayList<>();
	
	public String getNpp() {
		return npp;
	}
	public void setNpp(String npp) {
		this.npp = npp;
	}
	public String getNamalengkap() {
		return namalengkap;
	}
	public void setNamalengkap(String namalengkap) {
		this.namalengkap = namalengkap;
	}
	public Date getTgllahir() {
		return tgllahir;
	}
	public void setTgllahir(Date tgllahir) {
		this.tgllahir = tgllahir;
	}
	public Integer getUsia() {
		return usia;
	}
	public void setUsia(Integer usia) {
		this.usia = usia;
	}
	public String getKota1() {
		return kota1;
	}
	public void setKota1(String kota1) {
		this.kota1 = kota1;
	}
	public String getKota2() {
		return kota2;
	}
	public void setKota2(String kota2) {
		this.kota2 = kota2;
	}
	public String getJabatan() {
		return jabatan;
	}
	public void setJabatan(String jabatan) {
		this.jabatan = jabatan;
	}
	public String getKantor() {
		return kantor;
	}
	public void setKantor(String kantor) {
		this.kantor = kantor;
	}
	public String getPangkat() {
		return pangkat;
	}
	public void setPangkat(String pangkat) {
		this.pangkat = pangkat;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getWilayah() {
		return wilayah;
	}
	public void setWilayah(String wilayah) {
		this.wilayah = wilayah;
	}
	public String getKelaskantor() {
		return kelaskantor;
	}
	public void setKelaskantor(String kelaskantor) {
		this.kelaskantor = kelaskantor;
	}
	public Date getTanggalmpp() {
		return tanggalmpp;
	}
	public void setTanggalmpp(Date tanggalmpp) {
		this.tanggalmpp = tanggalmpp;
	}
	public Date getTanggalsk() {
		return tanggalsk;
	}
	public void setTanggalsk(Date tanggalsk) {
		this.tanggalsk = tanggalsk;
	}
	public Integer getTahunmasajabatan() {
		return tahunmasajabatan;
	}
	public void setTahunmasajabatan(Integer tahunmasajabatan) {
		this.tahunmasajabatan = tahunmasajabatan;
	}
	public Integer getBulanmasajabatan() {
		return bulanmasajabatan;
	}
	public void setBulanmasajabatan(Integer bulanmasajabatan) {
		this.bulanmasajabatan = bulanmasajabatan;
	}
	public Integer getTahunmasakerja() {
		return tahunmasakerja;
	}
	public void setTahunmasakerja(Integer tahunmasakerja) {
		this.tahunmasakerja = tahunmasakerja;
	}
	public Integer getBulanmasakerja() {
		return bulanmasakerja;
	}
	public void setBulanmasakerja(Integer bulanmasakerja) {
		this.bulanmasakerja = bulanmasakerja;
	}
	public Integer getKodepenugasan() {
		return kodepenugasan;
	}
	public void setKodepenugasan(Integer kodepenugasan) {
		this.kodepenugasan = kodepenugasan;
	}
	public ArrayList<ListPendidikan> getPendidikan() {
		return pendidikan;
	}
	public void setPendidikan(ArrayList<ListPendidikan> pendidikan) {
		this.pendidikan = pendidikan;
	}
	public ArrayList<ListPredikat> getPredikat() {
		return predikat;
	}
	public void setPredikat(ArrayList<ListPredikat> predikat) {
		this.predikat = predikat;
	}
	public Float getCcinett() {
		return ccinett;
	}
	public void setCcinett(Float ccinett) {
		this.ccinett = ccinett;
	}
	public Float getPoin() {
		return poin;
	}
	public void setPoin(Float poin) {
		this.poin = poin;
	}
	public ArrayList<ListPelanggaran> getPelanggaran() {
		return pelanggaran;
	}
	public void setPelanggaran(ArrayList<ListPelanggaran> pelanggaran) {
		this.pelanggaran = pelanggaran;
	}
	public Integer getKodediagramtalenta() {
		return kodediagramtalenta;
	}
	public void setKodediagramtalenta(Integer kodediagramtalenta) {
		this.kodediagramtalenta = kodediagramtalenta;
	}
	public Integer getPoindiagramtalenta() {
		return poindiagramtalenta;
	}
	public void setPoindiagramtalenta(Integer poindiagramtalenta) {
		this.poindiagramtalenta = poindiagramtalenta;
	}
	public String getPosisidiagramtalenta() {
		return posisidiagramtalenta;
	}
	public void setPosisidiagramtalenta(String posisidiagramtalenta) {
		this.posisidiagramtalenta = posisidiagramtalenta;
	}
	/**
	 * @return the poinltp
	 */
	public Integer getPoinltp() {
		return poinltp;
	}
	/**
	 * @param poinltp the poinltp to set
	 */
	public void setPoinltp(Integer poinltp) {
		this.poinltp = poinltp;
	}
	public ArrayList<ListPengalaman> getPengalaman() {
		return pengalaman;
	}
	public void setPengalaman(ArrayList<ListPengalaman> pengalaman) {
		this.pengalaman = pengalaman;
	}
	public ArrayList<ListPelatihan> getPelatihan() {
		return pelatihan;
	}
	public void setPelatihan(ArrayList<ListPelatihan> pelatihan) {
		this.pelatihan = pelatihan;
	}
	/**
	 * @return the subgrade
	 */
	public String getSubgrade() {
		return subgrade;
	}
	/**
	 * @param subgrade the subgrade to set
	 */
	public void setSubgrade(String subgrade) {
		this.subgrade = subgrade;
	}

}
