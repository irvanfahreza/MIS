package id.go.bpjskesehatan.service.v2.kinerja.entitas;

import java.util.List;

public class MasterNilai {
	private Integer kode;
	private String nama;
	private String deskripsi;
	private List<MasterNilaiDetail> details;
	
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
	public String getDeskripsi() {
		return deskripsi;
	}
	public void setDeskripsi(String deskripsi) {
		this.deskripsi = deskripsi;
	}
	public List<MasterNilaiDetail> getDetails() {
		return details;
	}
	public void setDetails(List<MasterNilaiDetail> details) {
		this.details = details;
	}
	
}