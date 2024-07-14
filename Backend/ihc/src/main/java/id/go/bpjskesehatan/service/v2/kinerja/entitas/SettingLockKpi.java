package id.go.bpjskesehatan.service.v2.kinerja.entitas;

import java.util.List;

public class SettingLockKpi {
	private Integer kodedetailkpi;
	private String nama;
	private Integer kode;
	private String target;
	private String unitukuran;
	private String sumberdata;
	private String asumsi;
	private Float bobot;
	private Integer targetlocked;
	private Integer unitukuranlocked;
	private Integer sumberdatalocked;
	private Integer asumsilocked;
	private Integer bobotlocked;
	private Integer kodesettinglockkpi;	
	private List<SettingLockKriteriaKpi> kriteria;
	private List<SettingLockRencanaAktifitas> rencanaaktifitas;
	private List<PembinaanDetail> pembinaandetails;
	private EvaluasiKpi evaluasi;
	private VerifikasiKpi verifikasi;
	
	public Integer getKodedetailkpi() {
		return kodedetailkpi;
	}
	public void setKodedetailkpi(Integer kodedetailkpi) {
		this.kodedetailkpi = kodedetailkpi;
	}
	public String getNama() {
		return nama;
	}
	public void setNama(String nama) {
		this.nama = nama;
	}
	public Integer getKode() {
		return kode;
	}
	public void setKode(Integer kode) {
		this.kode = kode;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getUnitukuran() {
		return unitukuran;
	}
	public void setUnitukuran(String unitukuran) {
		this.unitukuran = unitukuran;
	}
	public String getSumberdata() {
		return sumberdata;
	}
	public void setSumberdata(String sumberdata) {
		this.sumberdata = sumberdata;
	}
	public String getAsumsi() {
		return asumsi;
	}
	public void setAsumsi(String asumsi) {
		this.asumsi = asumsi;
	}
	public Float getBobot() {
		return bobot;
	}
	public void setBobot(Float bobot) {
		this.bobot = bobot;
	}
	public Integer getTargetlocked() {
		return targetlocked;
	}
	public void setTargetlocked(Integer targetlocked) {
		this.targetlocked = targetlocked;
	}
	public Integer getUnitukuranlocked() {
		return unitukuranlocked;
	}
	public void setUnitukuranlocked(Integer unitukuranlocked) {
		this.unitukuranlocked = unitukuranlocked;
	}
	public Integer getSumberdatalocked() {
		return sumberdatalocked;
	}
	public void setSumberdatalocked(Integer sumberdatalocked) {
		this.sumberdatalocked = sumberdatalocked;
	}
	public Integer getAsumsilocked() {
		return asumsilocked;
	}
	public void setAsumsilocked(Integer asumsilocked) {
		this.asumsilocked = asumsilocked;
	}
	public Integer getBobotlocked() {
		return bobotlocked;
	}
	public void setBobotlocked(Integer bobotlocked) {
		this.bobotlocked = bobotlocked;
	}
	public List<SettingLockKriteriaKpi> getKriteria() {
		return kriteria;
	}
	public void setKriteria(List<SettingLockKriteriaKpi> kriteria) {
		this.kriteria = kriteria;
	}
	public List<SettingLockRencanaAktifitas> getRencanaaktifitas() {
		return rencanaaktifitas;
	}
	public void setRencanaaktifitas(List<SettingLockRencanaAktifitas> rencanaaktifitas) {
		this.rencanaaktifitas = rencanaaktifitas;
	}
	public Integer getKodesettinglockkpi() {
		return kodesettinglockkpi;
	}
	public void setKodesettinglockkpi(Integer kodesettinglockkpi) {
		this.kodesettinglockkpi = kodesettinglockkpi;
	}
	public List<PembinaanDetail> getPembinaandetails() {
		return pembinaandetails;
	}
	public void setPembinaandetails(List<PembinaanDetail> pembinaandetails) {
		this.pembinaandetails = pembinaandetails;
	}
	public EvaluasiKpi getEvaluasi() {
		return evaluasi;
	}
	public void setEvaluasi(EvaluasiKpi evaluasi) {
		this.evaluasi = evaluasi;
	}
	public VerifikasiKpi getVerifikasi() {
		return verifikasi;
	}
	public void setVerifikasi(VerifikasiKpi verifikasi) {
		this.verifikasi = verifikasi;
	}
}