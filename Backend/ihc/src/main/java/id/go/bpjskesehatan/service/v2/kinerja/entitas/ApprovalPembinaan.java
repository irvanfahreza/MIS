package id.go.bpjskesehatan.service.v2.kinerja.entitas;

import java.util.List;

import id.go.bpjskesehatan.entitas.kinerja.Komponen;

public class ApprovalPembinaan {
	private Integer kodepembinaan;
	private Integer flag;
	private Integer kodepenugasan;
	private String catatan;
	private List<Komponen> komponens;
	
	public Integer getKodepembinaan() {
		return kodepembinaan;
	}
	public void setKodepembinaan(Integer kodepembinaan) {
		this.kodepembinaan = kodepembinaan;
	}
	public Integer getFlag() {
		return flag;
	}
	public void setFlag(Integer flag) {
		this.flag = flag;
	}
	public Integer getKodepenugasan() {
		return kodepenugasan;
	}
	public void setKodepenugasan(Integer kodepenugasan) {
		this.kodepenugasan = kodepenugasan;
	}
	public String getCatatan() {
		return catatan;
	}
	public void setCatatan(String catatan) {
		this.catatan = catatan;
	}
	public List<Komponen> getKomponens() {
		return komponens;
	}
	public void setKomponens(List<Komponen> komponens) {
		this.komponens = komponens;
	}
}