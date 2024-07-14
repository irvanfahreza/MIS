package id.go.bpjskesehatan.service.v2.skpd.entitas.verif;

import java.math.BigDecimal;
import java.util.ArrayList;

public class SkpdPegawai {
	private Integer kode;
	private BigDecimal total;
	private ArrayList<SkpdPegawaiTgl> skpdpegawaitgl = new ArrayList<>();
	private ArrayList<SkpdPegawaiTagihan> skpdpegawaitagihan = new ArrayList<>();
	
	public Integer getKode() {
		return kode;
	}
	public void setKode(Integer kode) {
		this.kode = kode;
	}
	public ArrayList<SkpdPegawaiTgl> getSkpdpegawaitgl() {
		return skpdpegawaitgl;
	}
	public void setSkpdpegawaitgl(ArrayList<SkpdPegawaiTgl> skpdpegawaitgl) {
		this.skpdpegawaitgl = skpdpegawaitgl;
	}
	public ArrayList<SkpdPegawaiTagihan> getSkpdpegawaitagihan() {
		return skpdpegawaitagihan;
	}
	public void setSkpdpegawaitagihan(ArrayList<SkpdPegawaiTagihan> skpdpegawaitagihan) {
		this.skpdpegawaitagihan = skpdpegawaitagihan;
	}
	public BigDecimal getTotal() {
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	
}
