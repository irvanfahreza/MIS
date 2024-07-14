package id.go.bpjskesehatan.service.v2.kinerja.entitas;

import java.util.List;

public class KejadianKritisNegatifHeader {
	private List<SettingLockKriteriaKpi> kriterias;
	private List<KejadianKritisNegatif> kejadiankritisnegatifs;
	private List<KejadianKritisNegatifVerifikasi> kejadiankritisnegatifverifikasis;

	public List<SettingLockKriteriaKpi> getKriterias() {
		return kriterias;
	}

	public void setKriterias(List<SettingLockKriteriaKpi> kriterias) {
		this.kriterias = kriterias;
	}

	public List<KejadianKritisNegatif> getKejadiankritisnegatifs() {
		return kejadiankritisnegatifs;
	}

	public void setKejadiankritisnegatifs(List<KejadianKritisNegatif> kejadiankritisnegatifs) {
		this.kejadiankritisnegatifs = kejadiankritisnegatifs;
	}

	public List<KejadianKritisNegatifVerifikasi> getKejadiankritisnegatifverifikasis() {
		return kejadiankritisnegatifverifikasis;
	}

	public void setKejadiankritisnegatifverifikasis(
			List<KejadianKritisNegatifVerifikasi> kejadiankritisnegatifverifikasis) {
		this.kejadiankritisnegatifverifikasis = kejadiankritisnegatifverifikasis;
	}
}