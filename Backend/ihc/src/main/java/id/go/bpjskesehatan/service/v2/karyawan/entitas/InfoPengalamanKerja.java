package id.go.bpjskesehatan.service.v2.karyawan.entitas;

public class InfoPengalamanKerja {
	private String lampiran;
	private Integer kode;
	private String npp;
	private String perusahaan;
	private String jabatan;
	private String tanggungjawab;
	private String lokasikerja;
	private String tmt;
	private String tat;
	private String catatan;
	private Boolean statusaktif;
	
	public String getLampiran() {
		return lampiran;
	}
	public void setLampiran(String lampiran) {
		this.lampiran = lampiran;
	}
	public Integer getKode() {
		return kode;
	}
	public void setKode(Integer kode) {
		this.kode = kode;
	}
	public String getNpp() {
		return npp;
	}
	public void setNpp(String npp) {
		this.npp = npp;
	}
	public String getPerusahaan() {
		return perusahaan;
	}
	public void setPerusahaan(String perusahaan) {
		this.perusahaan = perusahaan;
	}
	public String getJabatan() {
		return jabatan;
	}
	public void setJabatan(String jabatan) {
		this.jabatan = jabatan;
	}
	public String getTanggungjawab() {
		return tanggungjawab;
	}
	public void setTanggungjawab(String tanggungjawab) {
		this.tanggungjawab = tanggungjawab;
	}
	public String getLokasikerja() {
		return lokasikerja;
	}
	public void setLokasikerja(String lokasikerja) {
		this.lokasikerja = lokasikerja;
	}
	public String getTmt() {
		return tmt;
	}
	public void setTmt(String tmt) {
		this.tmt = tmt;
	}
	public String getTat() {
		return tat;
	}
	public void setTat(String tat) {
		this.tat = tat;
	}
	public String getCatatan() {
		return catatan;
	}
	public void setCatatan(String catatan) {
		this.catatan = catatan;
	}
	public Boolean getStatusaktif() {
		return statusaktif;
	}
	public void setStatusaktif(Boolean statusaktif) {
		this.statusaktif = statusaktif;
	}
	
}
