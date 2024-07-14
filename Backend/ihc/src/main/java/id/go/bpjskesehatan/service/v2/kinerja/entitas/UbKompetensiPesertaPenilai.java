package id.go.bpjskesehatan.service.v2.kinerja.entitas;

import java.util.List;

public class UbKompetensiPesertaPenilai {
	private Integer kodeubkompetensipeserta;
	private List<Peserta> pesertas;
	
	public Integer getKodeubkompetensipeserta() {
		return kodeubkompetensipeserta;
	}
	public void setKodeubkompetensipeserta(Integer kodeubkompetensipeserta) {
		this.kodeubkompetensipeserta = kodeubkompetensipeserta;
	}
	public List<Peserta> getPesertas() {
		return pesertas;
	}
	public void setPesertas(List<Peserta> pesertas) {
		this.pesertas = pesertas;
	}
}