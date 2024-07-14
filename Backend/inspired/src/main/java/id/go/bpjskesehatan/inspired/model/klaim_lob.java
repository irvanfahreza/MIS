package id.go.bpjskesehatan.inspired.model;

public class klaim_lob {
	private Integer id;
	private String lob;
	private String penyebab_klaim;
	private Integer jumlah_nasabah;
	private Float beban_klaim;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getLob() {
		return lob;
	}
	public void setLob(String lob) {
		this.lob = lob;
	}
	public String getPenyebab_klaim() {
		return penyebab_klaim;
	}
	public void setPenyebab_klaim(String penyebab_klaim) {
		this.penyebab_klaim = penyebab_klaim;
	}
	public Integer getJumlah_nasabah() {
		return jumlah_nasabah;
	}
	public void setJumlah_nasabah(Integer jumlah_nasabah) {
		this.jumlah_nasabah = jumlah_nasabah;
	}
	public Float getBeban_klaim() {
		return beban_klaim;
	}
	public void setBeban_klaim(Float beban_klaim) {
		this.beban_klaim = beban_klaim;
	}
	
	
}
