package id.go.bpjskesehatan.service.v2.kinerja.entitas;

import java.sql.Date;

public class SettingLockRencanaAktifitas {
	private Integer kode;
	private Integer kodedetailkpi;
	private String deskripsi;
	private String target;
	private Date targettgl;
	private String identifikasi;
	private String analisa;
	private String solusi;
	private String hasil;
	private Integer show;
	private Integer btnadd;
	private Integer btnremove;
	
	public Integer getKode() {
		return kode;
	}
	public void setKode(Integer kode) {
		this.kode = kode;
	}
	public String getDeskripsi() {
		return deskripsi;
	}
	public void setDeskripsi(String deskripsi) {
		this.deskripsi = deskripsi;
	}
	public Integer getShow() {
		return show;
	}
	public void setShow(Integer show) {
		this.show = show;
	}
	public Integer getBtnadd() {
		return btnadd;
	}
	public void setBtnadd(Integer btnadd) {
		this.btnadd = btnadd;
	}
	public Integer getBtnremove() {
		return btnremove;
	}
	public void setBtnremove(Integer btnremove) {
		this.btnremove = btnremove;
	}
	public Integer getKodedetailkpi() {
		return kodedetailkpi;
	}
	public void setKodedetailkpi(Integer kodedetailkpi) {
		this.kodedetailkpi = kodedetailkpi;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public Date getTargettgl() {
		return targettgl;
	}
	public void setTargettgl(Date targettgl) {
		this.targettgl = targettgl;
	}
	public String getIdentifikasi() {
		return identifikasi;
	}
	public void setIdentifikasi(String identifikasi) {
		this.identifikasi = identifikasi;
	}
	public String getAnalisa() {
		return analisa;
	}
	public void setAnalisa(String analisa) {
		this.analisa = analisa;
	}
	public String getSolusi() {
		return solusi;
	}
	public void setSolusi(String solusi) {
		this.solusi = solusi;
	}
	public String getHasil() {
		return hasil;
	}
	public void setHasil(String hasil) {
		this.hasil = hasil;
	}
}