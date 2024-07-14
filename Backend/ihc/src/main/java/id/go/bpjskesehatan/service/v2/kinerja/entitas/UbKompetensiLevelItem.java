package id.go.bpjskesehatan.service.v2.kinerja.entitas;

import java.util.List;

public class UbKompetensiLevelItem {
	private Integer level;
	private Boolean show;
	private List<UbKompetensiSoal> soals;
	
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public List<UbKompetensiSoal> getSoals() {
		return soals;
	}
	public void setSoals(List<UbKompetensiSoal> soals) {
		this.soals = soals;
	}
	public Boolean getShow() {
		return show;
	}
	public void setShow(Boolean show) {
		this.show = show;
	}
}