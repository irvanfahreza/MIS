package id.go.bpjskesehatan.service.v2.kinerja.entitas;

public class UbKomitmenNilai {
	private Integer kode;
	private Float bobot;
	private MasterNilai masternilai;
	private Boolean show;
	
	public Integer getKode() {
		return kode;
	}
	public void setKode(Integer kode) {
		this.kode = kode;
	}
	public Float getBobot() {
		return bobot;
	}
	public void setBobot(Float bobot) {
		this.bobot = bobot;
	}
	public MasterNilai getMasternilai() {
		return masternilai;
	}
	public void setMasternilai(MasterNilai masternilai) {
		this.masternilai = masternilai;
	}
	public Boolean getShow() {
		return show;
	}
	public void setShow(Boolean show) {
		this.show = show;
	}
}