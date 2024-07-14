package id.go.bpjskesehatan.service.v2.karyawan.entitas;

public class InfoAsuransi {
	private Integer kode;
	private String npp;
	private Integer kodejenisasuransi;
	private String nama;
	private String polis;
	private String atasnama;
	private String tmt;
	private String tat;
	private Integer jumlahtanggungan;
	private String deskripsi;
	private Boolean statusaktif;
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
	public Integer getKodejenisasuransi() {
		return kodejenisasuransi;
	}
	public void setKodejenisasuransi(Integer kodejenisasuransi) {
		this.kodejenisasuransi = kodejenisasuransi;
	}
	public String getNama() {
		return nama;
	}
	public void setNama(String nama) {
		this.nama = nama;
	}
	public String getPolis() {
		return polis;
	}
	public void setPolis(String polis) {
		this.polis = polis;
	}
	public String getAtasnama() {
		return atasnama;
	}
	public void setAtasnama(String atasnama) {
		this.atasnama = atasnama;
	}
	public String getTmt() {
		return tmt;
	}
	public void setTmt(String tmt) {
		this.tmt = tmt;
	}
	public String getTat() {
		return tat;
	}
	public void setTat(String tat) {
		this.tat = tat;
	}
	public Integer getJumlahtanggungan() {
		return jumlahtanggungan;
	}
	public void setJumlahtanggungan(Integer jumlahtanggungan) {
		this.jumlahtanggungan = jumlahtanggungan;
	}
	public String getDeskripsi() {
		return deskripsi;
	}
	public void setDeskripsi(String deskripsi) {
		this.deskripsi = deskripsi;
	}
	public Boolean getStatusaktif() {
		return statusaktif;
	}
	public void setStatusaktif(Boolean statusaktif) {
		this.statusaktif = statusaktif;
	}
	
}
