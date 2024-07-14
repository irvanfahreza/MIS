package id.go.bpjskesehatan.service.v2.kinerja.entitas;

import java.util.List;

public class UbKomitmenPenilaianSimpan {
	private Integer kodenotifikasi;
	private Integer kodeubkomitmenpesertapenilai;
	private Integer submitted;
	private List<UbKompetensiHeaderSoal> list;
	
	public Integer getSubmitted() {
		return submitted;
	}
	public void setSubmitted(Integer submitted) {
		this.submitted = submitted;
	}
	public List<UbKompetensiHeaderSoal> getList() {
		return list;
	}
	public void setList(List<UbKompetensiHeaderSoal> list) {
		this.list = list;
	}
	public Integer getKodeubkomitmenpesertapenilai() {
		return kodeubkomitmenpesertapenilai;
	}
	public void setKodeubkomitmenpesertapenilai(Integer kodeubkomitmenpesertapenilai) {
		this.kodeubkomitmenpesertapenilai = kodeubkomitmenpesertapenilai;
	}
	public Integer getKodenotifikasi() {
		return kodenotifikasi;
	}
	public void setKodenotifikasi(Integer kodenotifikasi) {
		this.kodenotifikasi = kodenotifikasi;
	}
	
}