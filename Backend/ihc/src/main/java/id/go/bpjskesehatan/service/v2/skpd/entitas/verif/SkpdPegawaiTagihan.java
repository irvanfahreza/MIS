package id.go.bpjskesehatan.service.v2.skpd.entitas.verif;

import java.math.BigDecimal;

public class SkpdPegawaiTagihan {
	private Integer kodereftagihan;
	private BigDecimal nilai;
	private Integer qty;
	private BigDecimal subtotal;
	
	public Integer getKodereftagihan() {
		return kodereftagihan;
	}
	public void setKodereftagihan(Integer kodereftagihan) {
		this.kodereftagihan = kodereftagihan;
	}
	public BigDecimal getNilai() {
		return nilai;
	}
	public void setNilai(BigDecimal nilai) {
		this.nilai = nilai;
	}
	public Integer getQty() {
		return qty;
	}
	public void setQty(Integer qty) {
		this.qty = qty;
	}
	public BigDecimal getSubtotal() {
		return subtotal;
	}
	public void setSubtotal(BigDecimal subtotal) {
		this.subtotal = subtotal;
	}
	
}
