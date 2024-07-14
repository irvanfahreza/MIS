package id.go.bpjskesehatan.service.v2.kinerja.entitas;

public class SettingLockKriteriaKpi {
	private Integer kodekriteria;
	private String definisi;
	private Integer rating;
	private Integer score;
	private Integer kode;
	private String deskripsi;
	private Integer locked;
	
	public Integer getKodekriteria() {
		return kodekriteria;
	}
	public void setKodekriteria(Integer kodekriteria) {
		this.kodekriteria = kodekriteria;
	}
	public String getDefinisi() {
		return definisi;
	}
	public void setDefinisi(String definisi) {
		this.definisi = definisi;
	}
	public Integer getRating() {
		return rating;
	}
	public void setRating(Integer rating) {
		this.rating = rating;
	}
	public Integer getScore() {
		return score;
	}
	public void setScore(Integer score) {
		this.score = score;
	}
	public Integer getKode() {
		return kode;
	}
	public void setKode(Integer kode) {
		this.kode = kode;
	}
	public String getDeskripsi() {
		return deskripsi;
	}
	public void setDeskripsi(String deskripsi) {
		this.deskripsi = deskripsi;
	}
	public Integer getLocked() {
		return locked;
	}
	public void setLocked(Integer locked) {
		this.locked = locked;
	}
}
