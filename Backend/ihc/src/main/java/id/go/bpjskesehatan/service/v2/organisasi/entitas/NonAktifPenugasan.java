package id.go.bpjskesehatan.service.v2.organisasi.entitas;

public class NonAktifPenugasan {
	private Integer kode;
	private String npp;
	private String kodejabatan;
	private String kodeoffice;
	private String kodesubgrade;
	private Integer kodestatusjabatan;
	
	public String getNpp() {
		return npp;
	}
	public void setNpp(String npp) {
		this.npp = npp;
	}
	public String getKodejabatan() {
		return kodejabatan;
	}
	public void setKodejabatan(String kodejabatan) {
		this.kodejabatan = kodejabatan;
	}
	public String getKodeoffice() {
		return kodeoffice;
	}
	public void setKodeoffice(String kodeoffice) {
		this.kodeoffice = kodeoffice;
	}
	public String getKodesubgrade() {
		return kodesubgrade;
	}
	public void setKodesubgrade(String kodesubgrade) {
		this.kodesubgrade = kodesubgrade;
	}
	public Integer getKodestatusjabatan() {
		return kodestatusjabatan;
	}
	public void setKodestatusjabatan(Integer kodestatusjabatan) {
		this.kodestatusjabatan = kodestatusjabatan;
	}
	public Integer getKode() {
		return kode;
	}
	public void setKode(Integer kode) {
		this.kode = kode;
	}
}
