package id.go.bpjskesehatan.service.v2.organisasi.entitas;

public class NonAktifkanPenugasan {
	private String tanggal;
	private Integer kodejenissk;
	private String nomorsk;
	private Integer kodejenisalasan;
	private String deskripsi;
	private Integer ismutation;
	private Integer jenisnonaktif;
	private NonAktifPenugasan penugasan;
	
	public String getTanggal() {
		return tanggal;
	}
	public void setTanggal(String tanggal) {
		this.tanggal = tanggal;
	}
	public Integer getKodejenissk() {
		return kodejenissk;
	}
	public void setKodejenissk(Integer kodejenissk) {
		this.kodejenissk = kodejenissk;
	}
	public String getNomorsk() {
		return nomorsk;
	}
	public void setNomorsk(String nomorsk) {
		this.nomorsk = nomorsk;
	}
	public Integer getKodejenisalasan() {
		return kodejenisalasan;
	}
	public void setKodejenisalasan(Integer kodejenisalasan) {
		this.kodejenisalasan = kodejenisalasan;
	}
	public String getDeskripsi() {
		return deskripsi;
	}
	public void setDeskripsi(String deskripsi) {
		this.deskripsi = deskripsi;
	}
	public Integer getIsmutation() {
		return ismutation;
	}
	public void setIsmutation(Integer ismutation) {
		this.ismutation = ismutation;
	}
	public Integer getJenisnonaktif() {
		return jenisnonaktif;
	}
	public void setJenisnonaktif(Integer jenisnonaktif) {
		this.jenisnonaktif = jenisnonaktif;
	}
	public NonAktifPenugasan getPenugasan() {
		return penugasan;
	}
	public void setPenugasan(NonAktifPenugasan penugasan) {
		this.penugasan = penugasan;
	}
}
