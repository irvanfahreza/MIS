package id.go.bpjskesehatan.entitas;

import java.math.BigDecimal;

public class KomponenGaji {
	private BigDecimal gajipokok;
	private BigDecimal tunjprestasi;
	private BigDecimal tunjjabatan;
	private BigDecimal tunjutilitas;
	private BigDecimal total;
	private BigDecimal total2;
	private String pangkat;
	private String grade;
	private Integer tunjanganbbm;
	private BigDecimal nominaltunjanganbbm;
	private String kodejobgrade;
	
	public BigDecimal getGajipokok() {
		return gajipokok;
	}
	public void setGajipokok(BigDecimal gajipokok) {
		this.gajipokok = gajipokok;
	}
	public BigDecimal getTunjprestasi() {
		return tunjprestasi;
	}
	public void setTunjprestasi(BigDecimal tunjprestasi) {
		this.tunjprestasi = tunjprestasi;
	}
	public BigDecimal getTunjjabatan() {
		return tunjjabatan;
	}
	public void setTunjjabatan(BigDecimal tunjjabatan) {
		this.tunjjabatan = tunjjabatan;
	}
	public BigDecimal getTunjutilitas() {
		return tunjutilitas;
	}
	public void setTunjutilitas(BigDecimal tunjutilitas) {
		this.tunjutilitas = tunjutilitas;
	}
	public BigDecimal getTotal() {
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	public BigDecimal getTotal2() {
		return total2;
	}
	public void setTotal2(BigDecimal total2) {
		this.total2 = total2;
	}
	public String getPangkat() {
		return pangkat;
	}
	public void setPangkat(String pangkat) {
		this.pangkat = pangkat;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public Integer getTunjanganbbm() {
		return tunjanganbbm;
	}
	public void setTunjanganbbm(Integer tunjanganbbm) {
		this.tunjanganbbm = tunjanganbbm;
	}
	public BigDecimal getNominaltunjanganbbm() {
		return nominaltunjanganbbm;
	}
	public void setNominaltunjanganbbm(BigDecimal nominaltunjanganbbm) {
		this.nominaltunjanganbbm = nominaltunjanganbbm;
	}
	public String getKodejobgrade() {
		return kodejobgrade;
	}
	public void setKodejobgrade(String kodejobgrade) {
		this.kodejobgrade = kodejobgrade;
	}
}
