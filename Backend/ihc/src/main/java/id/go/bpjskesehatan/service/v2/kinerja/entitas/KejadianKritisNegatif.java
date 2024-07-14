package id.go.bpjskesehatan.service.v2.kinerja.entitas;

public class KejadianKritisNegatif {
	private Integer kode;
	private String nama;
	private Integer rating;
	private String keterangan;
	private Integer kodeevaluasikejadiankritisnegatif;
	private Integer ratingevaluasi;
	private String catatanevaluasi;
	
	public Integer getKode() {
		return kode;
	}
	public void setKode(Integer kode) {
		this.kode = kode;
	}
	public String getNama() {
		return nama;
	}
	public void setNama(String nama) {
		this.nama = nama;
	}
	public Integer getRating() {
		return rating;
	}
	public void setRating(Integer rating) {
		this.rating = rating;
	}
	public String getKeterangan() {
		return keterangan;
	}
	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}
	public Integer getRatingevaluasi() {
		return ratingevaluasi;
	}
	public void setRatingevaluasi(Integer ratingevaluasi) {
		this.ratingevaluasi = ratingevaluasi;
	}
	public String getCatatanevaluasi() {
		return catatanevaluasi;
	}
	public void setCatatanevaluasi(String catatanevaluasi) {
		this.catatanevaluasi = catatanevaluasi;
	}
	public Integer getKodeevaluasikejadiankritisnegatif() {
		return kodeevaluasikejadiankritisnegatif;
	}
	public void setKodeevaluasikejadiankritisnegatif(Integer kodeevaluasikejadiankritisnegatif) {
		this.kodeevaluasikejadiankritisnegatif = kodeevaluasikejadiankritisnegatif;
	}
}