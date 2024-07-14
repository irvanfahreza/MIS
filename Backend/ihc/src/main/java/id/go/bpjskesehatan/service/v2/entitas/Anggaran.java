package id.go.bpjskesehatan.service.v2.entitas;

import java.math.BigDecimal;

import id.go.bpjskesehatan.entitas.organisasi.Office;

public class Anggaran {
	private Integer tahun;
	private BigDecimal alokasi;
	private BigDecimal realisasi;
	private BigDecimal saldo;
	private Program program;
	private Akun akun;
	private Office office;
	
	public Integer getTahun() {
		return tahun;
	}
	public void setTahun(Integer tahun) {
		this.tahun = tahun;
	}
	public BigDecimal getAlokasi() {
		return alokasi;
	}
	public void setAlokasi(BigDecimal alokasi) {
		this.alokasi = alokasi;
	}
	public BigDecimal getRealisasi() {
		return realisasi;
	}
	public void setRealisasi(BigDecimal realisasi) {
		this.realisasi = realisasi;
	}
	public BigDecimal getSaldo() {
		return saldo;
	}
	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}
	public Program getProgram() {
		return program;
	}
	public void setProgram(Program program) {
		this.program = program;
	}
	public Akun getAkun() {
		return akun;
	}
	public void setAkun(Akun akun) {
		this.akun = akun;
	}
	public Office getOffice() {
		return office;
	}
	public void setOffice(Office office) {
		this.office = office;
	}
}
