package id.go.bpjskesehatan.service.v2.kinerja.entitas;

import java.util.List;

public class UbKompetensiHeaderSoal {
	private String kodekompetensi;
	private String namakompetensi;
	private Integer level;
	private Integer bataslevelbawah;
	private Integer bataslevelatas;
	private Integer jmlitem;
	private Integer kodemastertipejawaban;
	private Boolean show;
	private Integer filled;
	private Integer leveluppoints;
	private List<MasterItemJawaban> itemjawaban;
	private List<UbKompetensiLevelItem> levels;
	
	public String getKodekompetensi() {
		return kodekompetensi;
	}
	public void setKodekompetensi(String kodekompetensi) {
		this.kodekompetensi = kodekompetensi;
	}
	public String getNamakompetensi() {
		return namakompetensi;
	}
	public void setNamakompetensi(String namakompetensi) {
		this.namakompetensi = namakompetensi;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public Integer getBataslevelbawah() {
		return bataslevelbawah;
	}
	public void setBataslevelbawah(Integer bataslevelbawah) {
		this.bataslevelbawah = bataslevelbawah;
	}
	public Integer getBataslevelatas() {
		return bataslevelatas;
	}
	public void setBataslevelatas(Integer bataslevelatas) {
		this.bataslevelatas = bataslevelatas;
	}
	public Integer getJmlitem() {
		return jmlitem;
	}
	public void setJmlitem(Integer jmlitem) {
		this.jmlitem = jmlitem;
	}
	public List<MasterItemJawaban> getItemjawaban() {
		return itemjawaban;
	}
	public void setItemjawaban(List<MasterItemJawaban> itemjawaban) {
		this.itemjawaban = itemjawaban;
	}
	public List<UbKompetensiLevelItem> getLevels() {
		return levels;
	}
	public void setLevels(List<UbKompetensiLevelItem> levels) {
		this.levels = levels;
	}
	public Integer getKodemastertipejawaban() {
		return kodemastertipejawaban;
	}
	public void setKodemastertipejawaban(Integer kodemastertipejawaban) {
		this.kodemastertipejawaban = kodemastertipejawaban;
	}
	public Boolean getShow() {
		return show;
	}
	public void setShow(Boolean show) {
		this.show = show;
	}
	public Integer getFilled() {
		return filled;
	}
	public void setFilled(Integer filled) {
		this.filled = filled;
	}
	public Integer getLeveluppoints() {
		return leveluppoints;
	}
	public void setLeveluppoints(Integer leveluppoints) {
		this.leveluppoints = leveluppoints;
	}
}