package id.go.bpjskesehatan.service.v2.promut.entitas;

public class ListPengalaman {
	
	private Integer kode;
	private Integer no;
	private Integer kodetelaahmutasi;
	private String pengalaman;
	private String npp;
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
	public Integer getNo() {
		return no;
	}
	public void setNo(Integer no) {
		this.no = no;
	}
	/**
	 * @return the pengalaman
	 */
	public String getPengalaman() {
		return pengalaman;
	}
	/**
	 * @param pengalaman the pengalaman to set
	 */
	public void setPengalaman(String pengalaman) {
		this.pengalaman = pengalaman;
	}
	
	
	

}
