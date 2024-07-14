package id.go.bpjskesehatan.service.v2.kinerja.entitas;

import java.util.List;

import id.go.bpjskesehatan.entitas.karyawan.Penugasan;
import id.go.bpjskesehatan.entitas.kinerja.Komponen;

public class SimpanPembinaanKinerja {
	private Integer kodepeserta;
	private Integer kodeperencanaan;
	private Integer status;
	private List<Komponen> komponens;
	private List<Penugasan> bawahans;
	
	public Integer getKodepeserta() {
		return kodepeserta;
	}
	public void setKodepeserta(Integer kodepeserta) {
		this.kodepeserta = kodepeserta;
	}
	public Integer getKodeperencanaan() {
		return kodeperencanaan;
	}
	public void setKodeperencanaan(Integer kodeperencanaan) {
		this.kodeperencanaan = kodeperencanaan;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public List<Komponen> getKomponens() {
		return komponens;
	}
	public void setKomponens(List<Komponen> komponens) {
		this.komponens = komponens;
	}
	public List<Penugasan> getBawahans() {
		return bawahans;
	}
	public void setBawahans(List<Penugasan> bawahans) {
		this.bawahans = bawahans;
	}
	
}