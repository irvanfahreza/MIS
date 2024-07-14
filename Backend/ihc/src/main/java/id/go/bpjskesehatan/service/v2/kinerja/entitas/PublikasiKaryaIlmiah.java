package id.go.bpjskesehatan.service.v2.kinerja.entitas;

import id.go.bpjskesehatan.entitas.Base64File;

public class PublikasiKaryaIlmiah {
	private Integer kode;
	private String judul;
	private String publisher;
	private Integer rating;
	private String keterangan;
	private String lampiran;
	private Base64File base64file;
	private Integer show;
	private Integer btnadd;
	private Integer btnremove;
	private Integer ratingevaluasi;
	private String catatanevaluasi;
	private Integer kodeevaluasipublikasikaryailmiah;
	
	public PublikasiKaryaIlmiah() {
		this.judul = "";
		this.publisher = "";
		this.keterangan = "";
		this.lampiran = "";
	}
	
	public Integer getKode() {
		return kode;
	}
	public void setKode(Integer kode) {
		this.kode = kode;
	}
	public String getJudul() {
		return judul;
	}
	public void setJudul(String judul) {
		this.judul = judul;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
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
	public String getLampiran() {
		return lampiran;
	}
	public void setLampiran(String lampiran) {
		this.lampiran = lampiran;
	}
	public Base64File getBase64file() {
		return base64file;
	}
	public void setBase64file(Base64File base64file) {
		this.base64file = base64file;
	}
	public Integer getShow() {
		return show;
	}
	public void setShow(Integer show) {
		this.show = show;
	}
	public Integer getBtnadd() {
		return btnadd;
	}
	public void setBtnadd(Integer btnadd) {
		this.btnadd = btnadd;
	}
	public Integer getBtnremove() {
		return btnremove;
	}
	public void setBtnremove(Integer btnremove) {
		this.btnremove = btnremove;
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

	public Integer getKodeevaluasipublikasikaryailmiah() {
		return kodeevaluasipublikasikaryailmiah;
	}

	public void setKodeevaluasipublikasikaryailmiah(Integer kodeevaluasipublikasikaryailmiah) {
		this.kodeevaluasipublikasikaryailmiah = kodeevaluasipublikasikaryailmiah;
	}

	
}