package id.go.bpjskesehatan.service.v2.kinerja.entitas;

import java.util.List;

public class SettingLockHasilKerja {
	private Integer kode;
	private Integer kodetanggungjawab;
	private String tanggungjawabutama;
	private Float bobot;
	private Integer bobotlocked;
	private List<SettingLockKpi> kpi;
	
	public Integer getKode() {
		return kode;
	}
	public void setKode(Integer kode) {
		this.kode = kode;
	}
	public Integer getKodetanggungjawab() {
		return kodetanggungjawab;
	}
	public void setKodetanggungjawab(Integer kodetanggungjawab) {
		this.kodetanggungjawab = kodetanggungjawab;
	}
	public String getTanggungjawabutama() {
		return tanggungjawabutama;
	}
	public void setTanggungjawabutama(String tanggungjawabutama) {
		this.tanggungjawabutama = tanggungjawabutama;
	}
	public Float getBobot() {
		return bobot;
	}
	public void setBobot(Float bobot) {
		this.bobot = bobot;
	}
	public Integer getBobotlocked() {
		return bobotlocked;
	}
	public void setBobotlocked(Integer bobotlocked) {
		this.bobotlocked = bobotlocked;
	}
	public List<SettingLockKpi> getKpi() {
		return kpi;
	}
	public void setKpi(List<SettingLockKpi> kpi) {
		this.kpi = kpi;
	}
}