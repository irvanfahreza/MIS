package id.go.bpjskesehatan.service.v2.skpd.entitas.verif;

import java.util.ArrayList;

public class Skpd {
	private Integer kode;
	private Integer kodeskpdmataanggaran;
	private Integer useract;
	private Integer isverif;
	private String namaverif;
	
	private ArrayList<SkpdPegawai> skpdpegawai = new ArrayList<>();
	
	public Integer getKode() {
		return kode;
	}
	public void setKode(Integer kode) {
		this.kode = kode;
	}
	public Integer getKodeskpdmataanggaran() {
		return kodeskpdmataanggaran;
	}
	public void setKodeskpdmataanggaran(Integer kodeskpdmataanggaran) {
		this.kodeskpdmataanggaran = kodeskpdmataanggaran;
	}
	public ArrayList<SkpdPegawai> getSkpdpegawai() {
		return skpdpegawai;
	}
	public void setSkpdpegawai(ArrayList<SkpdPegawai> skpdpegawai) {
		this.skpdpegawai = skpdpegawai;
	}
	public Integer getUseract() {
		return useract;
	}
	public void setUseract(Integer useract) {
		this.useract = useract;
	}
	public Integer getIsverif() {
		return isverif;
	}
	public void setIsverif(Integer isverif) {
		this.isverif = isverif;
	}
	public String getNamaverif() {
		return namaverif;
	}
	public void setNamaverif(String namaverif) {
		this.namaverif = namaverif;
	}
	
}
