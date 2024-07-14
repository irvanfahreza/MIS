package id.go.bpjskesehatan.service.v2.kinerja.entitas;

import java.util.List;

public class UbKomitmenPesertaPenilai {
	private Integer kodeubkomitmenpeserta;
	private List<Peserta> pesertas;
	
	public Integer getKodeubkomitmenpeserta() {
		return kodeubkomitmenpeserta;
	}
	public void setKodeubkomitmenpeserta(Integer kodeubkomitmenpeserta) {
		this.kodeubkomitmenpeserta = kodeubkomitmenpeserta;
	}
	public List<Peserta> getPesertas() {
		return pesertas;
	}
	public void setPesertas(List<Peserta> pesertas) {
		this.pesertas = pesertas;
	}
}