package id.go.bpjskesehatan.service.v2.kinerja.entitas;

import java.util.List;

public class CreatingFutureLeader {
	private Integer kode;
	private Integer jumlahtalentstar;
	private Integer jumlahtalentstarpromosi;
	private Integer rating;
	private String keterangan;
	private Integer ratingevaluasi;
	private String catatanevaluasi;
	private Integer kodeevaluasicreatingfutureleader;
	private List<CreatingFutureLeaderPegPromosi> pegpromosis;
	
	public Integer getKode() {
		return kode;
	}
	public void setKode(Integer kode) {
		this.kode = kode;
	}
	public Integer getJumlahtalentstar() {
		return jumlahtalentstar;
	}
	public void setJumlahtalentstar(Integer jumlahtalentstar) {
		this.jumlahtalentstar = jumlahtalentstar;
	}
	public Integer getJumlahtalentstarpromosi() {
		return jumlahtalentstarpromosi;
	}
	public void setJumlahtalentstarpromosi(Integer jumlahtalentstarpromosi) {
		this.jumlahtalentstarpromosi = jumlahtalentstarpromosi;
	}
	public Integer getRating() {
		return rating;
	}
	public void setRating(Integer rating) {
		this.rating = rating;
	}
	public String getKeterangan() {
		return keterangan;
	}
	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}
	public List<CreatingFutureLeaderPegPromosi> getPegpromosis() {
		return pegpromosis;
	}
	public void setPegpromosis(List<CreatingFutureLeaderPegPromosi> pegpromosis) {
		this.pegpromosis = pegpromosis;
	}
	public Integer getRatingevaluasi() {
		return ratingevaluasi;
	}
	public void setRatingevaluasi(Integer ratingevaluasi) {
		this.ratingevaluasi = ratingevaluasi;
	}
	public String getCatatanevaluasi() {
		return catatanevaluasi;
	}
	public void setCatatanevaluasi(String catatanevaluasi) {
		this.catatanevaluasi = catatanevaluasi;
	}
	public Integer getKodeevaluasicreatingfutureleader() {
		return kodeevaluasicreatingfutureleader;
	}
	public void setKodeevaluasicreatingfutureleader(Integer kodeevaluasicreatingfutureleader) {
		this.kodeevaluasicreatingfutureleader = kodeevaluasicreatingfutureleader;
	}
	
}