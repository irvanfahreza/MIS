package id.go.bpjskesehatan.service.v2.kinerja.entitas;

import java.util.List;

import id.go.bpjskesehatan.entitas.kinerja.Komponen;

public class PeriodeKinerja {
	private Integer kode;
	private String nama;
	private String tglmulai;
	private String tglselesai;
	private Integer generated;
	private String namastatus;
	private Integer publish;
	private String btnpublish;
	private String onoff;
	private Integer status;
	private Integer kodeperiodekinerjaold;
	
	private List<Komponen> komponen;
	private List<PeriodeKinerjaDetail> siklus;
	
	public String getNama() {
		return nama;
	}
	public void setNama(String nama) {
		this.nama = nama;
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
	public Integer getGenerated() {
		return generated;
	}
	public void setGenerated(Integer generated) {
		this.generated = generated;
	}
	public List<Komponen> getKomponen() {
		return komponen;
	}
	public void setKomponen(List<Komponen> komponen) {
		this.komponen = komponen;
	}
	public List<PeriodeKinerjaDetail> getSiklus() {
		return siklus;
	}
	public void setSiklus(List<PeriodeKinerjaDetail> siklus) {
		this.siklus = siklus;
	}
	public String getNamastatus() {
		return namastatus;
	}
	public void setNamastatus(String namastatus) {
		this.namastatus = namastatus;
	}
	public Integer getKode() {
		return kode;
	}
	public void setKode(Integer kode) {
		this.kode = kode;
	}
	public String getBtnpublish() {
		return btnpublish;
	}
	public void setBtnpublish(String btnpublish) {
		this.btnpublish = btnpublish;
	}
	public Integer getPublish() {
		return publish;
	}
	public void setPublish(Integer publish) {
		this.publish = publish;
	}
	public String getOnoff() {
		return onoff;
	}
	public void setOnoff(String onoff) {
		this.onoff = onoff;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getKodeperiodekinerjaold() {
		return kodeperiodekinerjaold;
	}
	public void setKodeperiodekinerjaold(Integer kodeperiodekinerjaold) {
		this.kodeperiodekinerjaold = kodeperiodekinerjaold;
	}
	
}