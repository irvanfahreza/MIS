package id.go.bpjskesehatan.service.v2.kompetensi.entitas;

public class Individu {
	private Integer kode;
	private String npp;
	private Integer kodelevelkompetensi;
	private String act;
	
	public Integer getKode() {
		return kode;
	}
	public void setKode(Integer kode) {
		this.kode = kode;
	}
	public String getNpp() {
		return npp;
	}
	public void setNpp(String npp) {
		this.npp = npp;
	}
	public Integer getKodelevelkompetensi() {
		return kodelevelkompetensi;
	}
	public void setKodelevelkompetensi(Integer kodelevelkompetensi) {
		this.kodelevelkompetensi = kodelevelkompetensi;
	}
	public String getAct() {
		return act;
	}
	public void setAct(String act) {
		this.act = act;
	}
}