package id.go.bpjskesehatan.service.v2.kinerja.entitas;

import java.util.List;

public class KomitmenHeader {
	private List<SettingLockKriteriaKpi> kriterias;

	public List<SettingLockKriteriaKpi> getKriterias() {
		return kriterias;
	}

	public void setKriterias(List<SettingLockKriteriaKpi> kriterias) {
		this.kriterias = kriterias;
	}
}