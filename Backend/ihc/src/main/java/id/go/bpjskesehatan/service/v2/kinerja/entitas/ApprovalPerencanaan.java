package id.go.bpjskesehatan.service.v2.kinerja.entitas;

import java.util.List;

public class ApprovalPerencanaan {
	private Integer kodeperencanaan;
	private Integer flag;
	private Integer kodepenugasan;
	private String catatan;
	private List<SettingLockHasilKerja> list;
	
	public Integer getKodeperencanaan() {
		return kodeperencanaan;
	}
	public void setKodeperencanaan(Integer kodeperencanaan) {
		this.kodeperencanaan = kodeperencanaan;
	}
	public Integer getFlag() {
		return flag;
	}
	public void setFlag(Integer flag) {
		this.flag = flag;
	}
	public Integer getKodepenugasan() {
		return kodepenugasan;
	}
	public void setKodepenugasan(Integer kodepenugasan) {
		this.kodepenugasan = kodepenugasan;
	}
	public List<SettingLockHasilKerja> getList() {
		return list;
	}
	public void setList(List<SettingLockHasilKerja> list) {
		this.list = list;
	}
	public String getCatatan() {
		return catatan;
	}
	public void setCatatan(String catatan) {
		this.catatan = catatan;
	}
	
}
