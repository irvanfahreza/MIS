package id.go.bpjskesehatan.service.v2.kinerja.entitas;

import id.go.bpjskesehatan.entitas.Base64File;

public class EvaluasiKpi {
	private Integer kode;
	private Integer rating;
	private String pencapaian;
	private String sumberdata;
	private String lampiran;
	private Base64File base64file;
	
	public Integer getKode() {
		return kode;
	}
	public void setKode(Integer kode) {
		this.kode = kode;
	}
	public Integer getRating() {
		return rating;
	}
	public void setRating(Integer rating) {
		this.rating = rating;
	}
	public String getPencapaian() {
		return pencapaian;
	}
	public void setPencapaian(String pencapaian) {
		this.pencapaian = pencapaian;
	}
	public String getSumberdata() {
		return sumberdata;
	}
	public void setSumberdata(String sumberdata) {
		this.sumberdata = sumberdata;
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
}