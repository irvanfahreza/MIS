package id.go.bpjskesehatan.service.v2.kinerja.entitas;

import java.util.List;

public class SimpanSettingTargetDjp {
	private Integer kodepeserta;
	private Integer kodeperiodekinerja;
	private String kodejobtitle;
	private Integer status;
	private List<SettingLockHasilKerja> list;
	
	public Integer getKodepeserta() {
		return kodepeserta;
	}
	public void setKodepeserta(Integer kodepeserta) {
		this.kodepeserta = kodepeserta;
	}
	public List<SettingLockHasilKerja> getList() {
		return list;
	}
	public void setList(List<SettingLockHasilKerja> list) {
		this.list = list;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getKodeperiodekinerja() {
		return kodeperiodekinerja;
	}
	public void setKodeperiodekinerja(Integer kodeperiodekinerja) {
		this.kodeperiodekinerja = kodeperiodekinerja;
	}
	public String getKodejobtitle() {
		return kodejobtitle;
	}
	public void setKodejobtitle(String kodejobtitle) {
		this.kodejobtitle = kodejobtitle;
	}
	
}