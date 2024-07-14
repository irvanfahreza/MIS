package id.go.bpjskesehatan.service.v2.payroll.entitas;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;

public class ListDetilPayroll {

	private Integer kode;
	private Integer kodepayroll;
	private Integer kodekomponen;
	private String keterangan;
	private BigDecimal nilai;
	private Integer row_status;
	private Integer useract;
	private String nama;
	private Integer disabled;
	private Integer deleted;
	
	public Integer getKode() {
		return kode;
	}
	public void setKode(Integer kode) {
		this.kode = kode;
	}
	public Integer getKodepayroll() {
		return kodepayroll;
	}
	public void setKodepayroll(Integer kodepayroll) {
		this.kodepayroll = kodepayroll;
	}
	public Integer getKodekomponen() {
		return kodekomponen;
	}
	public void setKodekomponen(Integer kodekomponen) {
		this.kodekomponen = kodekomponen;
	}
	public String getKeterangan() {
		return keterangan;
	}
	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}
	public BigDecimal getNilai() {
		return nilai;
	}
	public void setNilai(BigDecimal nilai) {
		this.nilai = nilai;
	}
	public Integer getRow_status() {
		return row_status;
	}
	public void setRow_status(Integer row_status) {
		this.row_status = row_status;
	}
	public Integer getUseract() {
		return useract;
	}
	public void setUseract(Integer useract) {
		this.useract = useract;
	}
	public String getNama() {
		return nama;
	}
	public void setNama(String nama) {
		this.nama = nama;
	}
	public Integer getDisabled() {
		return disabled;
	}
	public void setDisabled(Integer disabled) {
		this.disabled = disabled;
	}
	public Integer getDeleted() {
		return deleted;
	}
	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

}
