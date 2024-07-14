package id.go.bpjskesehatan.service.v2.kinerja.entitas;

import java.util.List;

public class UbKomitmenPenilaian {
	private Integer kodenotifikasi;
	private Integer kodeubkomitmenpesertapenilai;
	private Integer submitted;
	private List<UbKomitmenNilai> list;
	
	public Integer getKodenotifikasi() {
		return kodenotifikasi;
	}
	public void setKodenotifikasi(Integer kodenotifikasi) {
		this.kodenotifikasi = kodenotifikasi;
	}
	public Integer getKodeubkomitmenpesertapenilai() {
		return kodeubkomitmenpesertapenilai;
	}
	public void setKodeubkomitmenpesertapenilai(Integer kodeubkomitmenpesertapenilai) {
		this.kodeubkomitmenpesertapenilai = kodeubkomitmenpesertapenilai;
	}
	public Integer getSubmitted() {
		return submitted;
	}
	public void setSubmitted(Integer submitted) {
		this.submitted = submitted;
	}
	public List<UbKomitmenNilai> getList() {
		return list;
	}
	public void setList(List<UbKomitmenNilai> list) {
		this.list = list;
	}
}