package id.go.bpjskesehatan.service.v2.kinerja.entitas;

import id.go.bpjskesehatan.entitas.Base64File;
import id.go.bpjskesehatan.service.v2.karyawan.entitas.Pegawai;

public class CreatingFutureLeaderPegPromosi {
	private Integer kode;
	private String lampiran;
	private Base64File base64file;
	private Pegawai pegawai;
	private Integer show;
	private Integer btnadd;
	private Integer btnremove;
	
	public Integer getKode() {
		return kode;
	}
	public void setKode(Integer kode) {
		this.kode = kode;
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
	public Pegawai getPegawai() {
		return pegawai;
	}
	public void setPegawai(Pegawai pegawai) {
		this.pegawai = pegawai;
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
	
}