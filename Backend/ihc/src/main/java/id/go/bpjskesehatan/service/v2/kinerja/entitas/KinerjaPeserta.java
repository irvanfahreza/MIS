package id.go.bpjskesehatan.service.v2.kinerja.entitas;

import java.util.List;

public class KinerjaPeserta {
	private Integer kodeperiodekinerja;
	private List<Peserta> pesertas;
	
	public Integer getKodeperiodekinerja() {
		return kodeperiodekinerja;
	}
	public void setKodeperiodekinerja(Integer kodeperiodekinerja) {
		this.kodeperiodekinerja = kodeperiodekinerja;
	}
	public List<Peserta> getPesertas() {
		return pesertas;
	}
	public void setPesertas(List<Peserta> pesertas) {
		this.pesertas = pesertas;
	}
}