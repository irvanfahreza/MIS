package id.go.bpjskesehatan.service.v2.cuti.entitas;

public class SpmTemplate {
	private Integer kode;
	private String kepada;
	private String dari;
	private String dari_deputi;
	private Integer row_status;
	private Integer tanggal;
	private Integer bulan;
	private Integer tahun;
	private String tempat;
	
	public Integer getKode() {
		return kode;
	}
	public void setKode(Integer kode) {
		this.kode = kode;
	}
	public String getKepada() {
		return kepada;
	}
	public void setKepada(String kepada) {
		this.kepada = kepada;
	}
	public String getDari() {
		return dari;
	}
	public void setDari(String dari) {
		this.dari = dari;
	}
	public String getDari_deputi() {
		return dari_deputi;
	}
	public void setDari_deputi(String dari_deputi) {
		this.dari_deputi = dari_deputi;
	}
	public Integer getRow_status() {
		return row_status;
	}
	public void setRow_status(Integer row_status) {
		this.row_status = row_status;
	}
	public Integer getTanggal() {
		return tanggal;
	}
	public void setTanggal(Integer tanggal) {
		this.tanggal = tanggal;
	}
	public Integer getBulan() {
		return bulan;
	}
	public void setBulan(Integer bulan) {
		this.bulan = bulan;
	}
	public Integer getTahun() {
		return tahun;
	}
	public void setTahun(Integer tahun) {
		this.tahun = tahun;
	}
	public String getTempat() {
		return tempat;
	}
	public void setTempat(String tempat) {
		this.tempat = tempat;
	}
	
}
