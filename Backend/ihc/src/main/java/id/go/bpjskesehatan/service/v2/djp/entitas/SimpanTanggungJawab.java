package id.go.bpjskesehatan.service.v2.djp.entitas;

import java.util.List;

public class SimpanTanggungJawab {
	private Integer kode;
	private Integer kodedjp;
	private String tanggungjawabutama;
	private String wewenangutama;
	private List<DetailKpiTbl> detailkpi;
	private List<DetailDimensiJabatanTbl> detaildimensijabatan;
	
	public Integer getKode() {
		return kode;
	}
	public void setKode(Integer kode) {
		this.kode = kode;
	}
	public Integer getKodedjp() {
		return kodedjp;
	}
	public void setKodedjp(Integer kodedjp) {
		this.kodedjp = kodedjp;
	}
	public String getTanggungjawabutama() {
		return tanggungjawabutama;
	}
	public void setTanggungjawabutama(String tanggungjawabutama) {
		this.tanggungjawabutama = tanggungjawabutama;
	}
	public String getWewenangutama() {
		return wewenangutama;
	}
	public void setWewenangutama(String wewenangutama) {
		this.wewenangutama = wewenangutama;
	}
	public List<DetailKpiTbl> getDetailkpi() {
		return detailkpi;
	}
	public void setDetailkpi(List<DetailKpiTbl> detailkpi) {
		this.detailkpi = detailkpi;
	}
	public List<DetailDimensiJabatanTbl> getDetaildimensijabatan() {
		return detaildimensijabatan;
	}
	public void setDetaildimensijabatan(List<DetailDimensiJabatanTbl> detaildimensijabatan) {
		this.detaildimensijabatan = detaildimensijabatan;
	}
}
