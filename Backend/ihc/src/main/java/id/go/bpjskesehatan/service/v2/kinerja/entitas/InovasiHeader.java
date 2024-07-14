package id.go.bpjskesehatan.service.v2.kinerja.entitas;

import java.util.List;

public class InovasiHeader {
	private List<SettingLockKriteriaKpi> kriterias;
	private List<Inovasi> inovasis;
	private List<InovasiVerifikasi> verifikasis;

	public List<SettingLockKriteriaKpi> getKriterias() {
		return kriterias;
	}

	public void setKriterias(List<SettingLockKriteriaKpi> kriterias) {
		this.kriterias = kriterias;
	}

	public List<Inovasi> getInovasis() {
		return inovasis;
	}

	public void setInovasis(List<Inovasi> inovasis) {
		this.inovasis = inovasis;
	}

	public List<InovasiVerifikasi> getVerifikasis() {
		return verifikasis;
	}

	public void setVerifikasis(List<InovasiVerifikasi> verifikasis) {
		this.verifikasis = verifikasis;
	}
}