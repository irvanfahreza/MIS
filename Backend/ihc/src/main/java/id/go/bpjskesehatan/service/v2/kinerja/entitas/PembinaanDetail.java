package id.go.bpjskesehatan.service.v2.kinerja.entitas;

import id.go.bpjskesehatan.entitas.Base64File;

public class PembinaanDetail {
	private Integer kode;
	private String pencapaian;
	private String detil;
	private String lampiran;
	private String catatan;
	private Integer flag;
	private Base64File base64file; 
	
	public Integer getKode() {
		return kode;
	}
	public void setKode(Integer kode) {
		this.kode = kode;
	}
	public String getPencapaian() {
		return pencapaian;
	}
	public void setPencapaian(String pencapaian) {
		this.pencapaian = pencapaian;
	}
	public String getDetil() {
		return detil;
	}
	public void setDetil(String detil) {
		this.detil = detil;
	}
	public String getLampiran() {
		return lampiran;
	}
	public void setLampiran(String lampiran) {
		this.lampiran = lampiran;
	}
	public String getCatatan() {
		return catatan;
	}
	public void setCatatan(String catatan) {
		this.catatan = catatan;
	}
	public Integer getFlag() {
		return flag;
	}
	public void setFlag(Integer flag) {
		this.flag = flag;
	}
	public Base64File getBase64file() {
		return base64file;
	}
	public void setBase64file(Base64File base64file) {
		this.base64file = base64file;
	}
}