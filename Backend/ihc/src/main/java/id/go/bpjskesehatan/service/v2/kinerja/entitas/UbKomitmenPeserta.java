package id.go.bpjskesehatan.service.v2.kinerja.entitas;

import java.util.List;

public class UbKomitmenPeserta {
	private Integer kodeperiodeubkomitmen;
	private List<Peserta> pesertas;
	
	public Integer getKodeperiodeubkomitmen() {
		return kodeperiodeubkomitmen;
	}
	public void setKodeperiodeubkomitmen(Integer kodeperiodeubkomitmen) {
		this.kodeperiodeubkomitmen = kodeperiodeubkomitmen;
	}
	public List<Peserta> getPesertas() {
		return pesertas;
	}
	public void setPesertas(List<Peserta> pesertas) {
		this.pesertas = pesertas;
	}
}