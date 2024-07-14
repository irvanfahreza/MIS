package id.go.bpjskesehatan.service.v2.kinerja.entitas;

import java.util.List;

public class PublikasiKaryaIlmiahHeader {
	private List<PublikasiKaryaIlmiah> publikasikaryailmiahs;
	private List<SettingLockKriteriaKpi> kriterias;
	private List<PublikasiKaryaIlmiahVerifikasi> publikasikaryailmiahverifikasis;

	public List<SettingLockKriteriaKpi> getKriterias() {
		return kriterias;
	}

	public void setKriterias(List<SettingLockKriteriaKpi> kriterias) {
		this.kriterias = kriterias;
	}

	public List<PublikasiKaryaIlmiah> getPublikasikaryailmiahs() {
		return publikasikaryailmiahs;
	}

	public void setPublikasikaryailmiahs(List<PublikasiKaryaIlmiah> publikasikaryailmiahs) {
		this.publikasikaryailmiahs = publikasikaryailmiahs;
	}

	public List<PublikasiKaryaIlmiahVerifikasi> getPublikasikaryailmiahverifikasis() {
		return publikasikaryailmiahverifikasis;
	}

	public void setPublikasikaryailmiahverifikasis(List<PublikasiKaryaIlmiahVerifikasi> publikasikaryailmiahverifikasis) {
		this.publikasikaryailmiahverifikasis = publikasikaryailmiahverifikasis;
	}
}