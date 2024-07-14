package id.go.bpjskesehatan.service.v2.lembur.entitas;

public class ListPegawai {
	private Integer kode;
	private Integer kodepenugasan;
	private String npp;
	private String nama;
	private Integer deleted;
	private Integer totaljam;
	
	public Integer getKode() {
		return kode;
	}
	public void setKode(Integer kode) {
		this.kode = kode;
	}
	public Integer getKodepenugasan() {
		return kodepenugasan;
	}
	public void setKodepenugasan(Integer kodepenugasan) {
		this.kodepenugasan = kodepenugasan;
	}
	public String getNpp() {
		return npp;
	}
	public void setNpp(String npp) {
		this.npp = npp;
	}
	public String getNama() {
		return nama;
	}
	public void setNama(String nama) {
		this.nama = nama;
	}
	public Integer getDeleted() {
		return deleted;
	}
	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}
	public Integer getTotaljam() {
		return totaljam;
	}
	public void setTotaljam(Integer totaljam) {
		this.totaljam = totaljam;
	}
	
}
