package id.go.bpjskesehatan.service.v2.kinerja.entitas;

import java.util.List;

public class UbKomitmenHeaderSoal {
	private String kodekomitmen;
	private String namakomitmen;
	private Integer jmlitem;
	private Integer kodemastertipejawaban;
	private Boolean show;
	private Integer filled;
	private List<MasterItemJawaban> itemjawaban;
	
	public String getKodekomitmen() {
		return kodekomitmen;
	}
	public void setKodekomitmen(String kodekomitmen) {
		this.kodekomitmen = kodekomitmen;
	}
	public String getNamakomitmen() {
		return namakomitmen;
	}
	public void setNamakomitmen(String namakomitmen) {
		this.namakomitmen = namakomitmen;
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
}