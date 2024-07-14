package id.go.bpjskesehatan.service.v2.kinerja.entitas;

import java.util.List;

public class UbKompetensiPeserta {
	private Integer kodeperiodeubkompetensi;
	private List<Peserta> pesertas;
	
	public Integer getKodeperiodeubkompetensi() {
		return kodeperiodeubkompetensi;
	}
	public void setKodeperiodeubkompetensi(Integer kodeperiodeubkompetensi) {
		this.kodeperiodeubkompetensi = kodeperiodeubkompetensi;
	}
	public List<Peserta> getPesertas() {
		return pesertas;
	}
	public void setPesertas(List<Peserta> pesertas) {
		this.pesertas = pesertas;
	}
}