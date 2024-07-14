package id.go.bpjskesehatan.service.v2.djp.entitas;

public class DetailKpiTbl {
	private Integer kode;
	private Integer kodetanggungjawab;
	private String nama;
	private Integer deleted;
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
}
