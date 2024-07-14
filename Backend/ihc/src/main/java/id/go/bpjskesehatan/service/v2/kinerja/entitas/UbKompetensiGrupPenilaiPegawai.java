package id.go.bpjskesehatan.service.v2.kinerja.entitas;

import java.util.List;

public class UbKompetensiGrupPenilaiPegawai {
	private Integer kodeperiodeubkompetensi;
	private Integer kodeubkompetensigruppenilai;
	private List<Peserta> pesertas;
	
	public Integer getKodeperiodeubkompetensi() {
		return kodeperiodeubkompetensi;
	}
	public void setKodeperiodeubkompetensi(Integer kodeperiodeubkompetensi) {
		this.kodeperiodeubkompetensi = kodeperiodeubkompetensi;
	}
	public Integer getKodeubkompetensigruppenilai() {
		return kodeubkompetensigruppenilai;
	}
	public void setKodeubkompetensigruppenilai(Integer kodeubkompetensigruppenilai) {
		this.kodeubkompetensigruppenilai = kodeubkompetensigruppenilai;
	}
	public List<Peserta> getPesertas() {
		return pesertas;
	}
	public void setPesertas(List<Peserta> pesertas) {
		this.pesertas = pesertas;
	}
	
}