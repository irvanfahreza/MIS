package id.go.bpjskesehatan.service.v2.kinerja.entitas;

public class PeriodeKinerjaDetail {
	private Integer kodesiklus;
	private String tglmulai;
	private String tglselesai;
	
	public Integer getKodesiklus() {
		return kodesiklus;
	}
	public void setKodesiklus(Integer kodesiklus) {
		this.kodesiklus = kodesiklus;
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
}