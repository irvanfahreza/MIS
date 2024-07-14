package id.go.bpjskesehatan.service.v2.skpd.entitas;

public class ListPegawai {
	private Integer no;
	private Integer kode;
	private Integer kodepenugasan;
	private String npp;
	private String nama;
	private Integer deleted;
	private Integer isverif;
	
	public Integer getNo() {
		return no;
	}
	public void setNo(Integer no) {
		this.no = no;
	}
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
	public Integer getIsverif() {
		return isverif;
	}
	public void setIsverif(Integer isverif) {
		this.isverif = isverif;
	}
	
}
