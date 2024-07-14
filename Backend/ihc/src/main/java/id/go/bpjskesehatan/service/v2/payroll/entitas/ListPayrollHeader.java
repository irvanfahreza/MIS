package id.go.bpjskesehatan.service.v2.payroll.entitas;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;

import id.go.bpjskesehatan.service.v2.promut.entitas.ListPegawaiPromosi;
import id.go.bpjskesehatan.service.v2.promut.entitas.ListPredikat;

public class ListPayrollHeader {

	private Integer num;
	private Integer kode;
	private Integer tahun;
	private Integer bulan;
	private String kodeoffice;
	private BigDecimal totalpendapatan;
	private BigDecimal totalpotongan;
	private BigDecimal grandtotal;
	private String totalpendapatan2;
	private String totalpotongan2;
	private String grandtotal2;
	private Integer tipehitung;
	private Integer statusapprove;
	private Integer isdone;
	private Integer useract;
	private String nama;
	private Timestamp tglappr;
	private Integer kodejenispegawai;
	private String catatan;
	
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public Integer getKode() {
		return kode;
	}
	public void setKode(Integer kode) {
		this.kode = kode;
	}
	public Integer getTahun() {
		return tahun;
	}
	public void setTahun(Integer tahun) {
		this.tahun = tahun;
	}
	public Integer getBulan() {
		return bulan;
	}
	public void setBulan(Integer bulan) {
		this.bulan = bulan;
	}
	public String getKodeoffice() {
		return kodeoffice;
	}
	public void setKodeoffice(String kodeoffice) {
		this.kodeoffice = kodeoffice;
	}
	public BigDecimal getTotalpendapatan() {
		return totalpendapatan;
	}
	public void setTotalpendapatan(BigDecimal totalpendapatan) {
		this.totalpendapatan = totalpendapatan;
	}
	public BigDecimal getTotalpotongan() {
		return totalpotongan;
	}
	public void setTotalpotongan(BigDecimal totalpotongan) {
		this.totalpotongan = totalpotongan;
	}
	public BigDecimal getGrandtotal() {
		return grandtotal;
	}
	public void setGrandtotal(BigDecimal grandtotal) {
		this.grandtotal = grandtotal;
	}
	public Integer getTipehitung() {
		return tipehitung;
	}
	public void setTipehitung(Integer tipehitung) {
		this.tipehitung = tipehitung;
	}
	public Integer getStatusapprove() {
		return statusapprove;
	}
	public void setStatusapprove(Integer statusapprove) {
		this.statusapprove = statusapprove;
	}
	public Integer getIsdone() {
		return isdone;
	}
	public void setIsdone(Integer isdone) {
		this.isdone = isdone;
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
	public String getCatatan() {
		return catatan;
	}
	public void setCatatan(String catatan) {
		this.catatan = catatan;
	}
	public Timestamp getTglappr() {
		return tglappr;
	}
	public void setTglappr(Timestamp tglappr) {
		this.tglappr = tglappr;
	}
	public String getTotalpendapatan2() {
		return totalpendapatan2;
	}
	public void setTotalpendapatan2(String totalpendapatan2) {
		this.totalpendapatan2 = totalpendapatan2;
	}
	public String getTotalpotongan2() {
		return totalpotongan2;
	}
	public void setTotalpotongan2(String totalpotongan2) {
		this.totalpotongan2 = totalpotongan2;
	}
	public String getGrandtotal2() {
		return grandtotal2;
	}
	public void setGrandtotal2(String grandtotal2) {
		this.grandtotal2 = grandtotal2;
	}
	public Integer getKodejenispegawai() {
		return kodejenispegawai;
	}
	public void setKodejenispegawai(Integer kodejenispegawai) {
		this.kodejenispegawai = kodejenispegawai;
	}

}
