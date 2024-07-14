package id.go.bpjskesehatan.service.v2.promut.entitas;

public class ListPelanggaran {
	
	private Integer kode;
	private Integer no;
	private Integer kodetelaahmutasi;
	private Integer kodeinfopelanggaran;
	private String pelanggaran;
	private String npp;
	private String namajenispelanggaran;
	private Integer deleted;
	
	
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
	
	public Integer getDeleted() {
		return deleted;
	}
	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}
	public Integer getKodetelaahmutasi() {
		return kodetelaahmutasi;
	}
	public void setKodetelaahmutasi(Integer kodetelaahmutasi) {
		this.kodetelaahmutasi = kodetelaahmutasi;
	}
	public String getPelanggaran() {
		return pelanggaran;
	}
	public void setPelanggaran(String pelanggaran) {
		this.pelanggaran = pelanggaran;
	}
	public Integer getNo() {
		return no;
	}
	public void setNo(Integer no) {
		this.no = no;
	}
	public Integer getKodeinfopelanggaran() {
		return kodeinfopelanggaran;
	}
	public void setKodeinfopelanggaran(Integer kodeinfopelanggaran) {
		this.kodeinfopelanggaran = kodeinfopelanggaran;
	}
	public String getNamajenispelanggaran() {
		return namajenispelanggaran;
	}
	public void setNamajenispelanggaran(String namajenispelanggaran) {
		this.namajenispelanggaran = namajenispelanggaran;
	}
	
	
	

}
