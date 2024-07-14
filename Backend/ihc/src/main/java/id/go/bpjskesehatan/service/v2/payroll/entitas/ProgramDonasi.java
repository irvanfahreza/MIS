package id.go.bpjskesehatan.service.v2.payroll.entitas;

import java.math.BigDecimal;

public class ProgramDonasi {

	private Integer kode;
	private Integer kodepenugasan;
	private Integer kodepaketdonasi;
	private String namapaketdonasi;
	private String bulanmulai;
	private String bulanakhir;
	private BigDecimal nominal;
	private Integer useract;
	private String act;
	private String statusaktif;
	
	public Integer getKodepenugasan() {
		return kodepenugasan;
	}
	public void setKodepenugasan(Integer kodepenugasan) {
		this.kodepenugasan = kodepenugasan;
	}
	public Integer getKodepaketdonasi() {
		return kodepaketdonasi;
	}
	public void setKodepaketdonasi(Integer kodepaketdonasi) {
		this.kodepaketdonasi = kodepaketdonasi;
	}
	public String getBulanmulai() {
		return bulanmulai;
	}
	public void setBulanmulai(String bulanmulai) {
		this.bulanmulai = bulanmulai;
	}
	public String getBulanakhir() {
		return bulanakhir;
	}
	public void setBulanakhir(String bulanakhir) {
		this.bulanakhir = bulanakhir;
	}
	public BigDecimal getNominal() {
		return nominal;
	}
	public void setNominal(BigDecimal nominal) {
		this.nominal = nominal;
	}
	public Integer getUseract() {
		return useract;
	}
	public void setUseract(Integer useract) {
		this.useract = useract;
	}
	public String getNamapaketdonasi() {
		return namapaketdonasi;
	}
	public void setNamapaketdonasi(String namapaketdonasi) {
		this.namapaketdonasi = namapaketdonasi;
	}
	public String getAct() {
		return act;
	}
	public void setAct(String act) {
		this.act = act;
	}
	public Integer getKode() {
		return kode;
	}
	public void setKode(Integer kode) {
		this.kode = kode;
	}
	public String getStatusaktif() {
		return statusaktif;
	}
	public void setStatusaktif(String statusaktif) {
		this.statusaktif = statusaktif;
	}
}
