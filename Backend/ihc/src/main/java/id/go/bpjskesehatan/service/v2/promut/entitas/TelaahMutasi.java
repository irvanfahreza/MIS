package id.go.bpjskesehatan.service.v2.promut.entitas;

import java.sql.Date;
import java.util.ArrayList;

import id.go.bpjskesehatan.service.v2.skpd.entitas.ListPegawai;

public class TelaahMutasi {

	private Integer kode;
	private String npp;
	private Integer kodepenugasan;
	private String kodehirarkijabatantujuan;
	private String kodeofficetujuan;
	private Integer kodestatusjabatantujuan;
	private String kesimpulan;
	private String catasdep;
	private String catdepdir;
	private String catdir;
	private Integer koderisikopergerakankarir;
	private Integer r1;
	private Integer r2;
	private Integer r3;
	private Integer r4;
	private Integer r5;
	private Integer r6;
	private Integer r7;
	private Integer r8;
	private Integer r9;
	private Integer rekomendasi;
	private Integer koderangkaian;
	private Integer useract;
	private Float ccijabatannett;
	private Float ccijabatantujuannett;
	private ArrayList<ListPredikat> predikat = new ArrayList<>();
	private ArrayList<ListPelanggaran> pelanggaran = new ArrayList<>();
	
	public Integer getKode() {
		return kode;
	}
	public void setKode(Integer kode) {
		this.kode = kode;
	}
	public String getNpp() {
		return npp;
	}
	public void setNpp(String npp) {
		this.npp = npp;
	}
	public Integer getKodepenugasan() {
		return kodepenugasan;
	}
	public void setKodepenugasan(Integer kodepenugasan) {
		this.kodepenugasan = kodepenugasan;
	}
	public String getKodehirarkijabatantujuan() {
		return kodehirarkijabatantujuan;
	}
	public void setKodehirarkijabatantujuan(String kodehirarkijabatantujuan) {
		this.kodehirarkijabatantujuan = kodehirarkijabatantujuan;
	}
	public String getKodeofficetujuan() {
		return kodeofficetujuan;
	}
	public void setKodeofficetujuan(String kodeofficetujuan) {
		this.kodeofficetujuan = kodeofficetujuan;
	}
	public Integer getKodestatusjabatantujuan() {
		return kodestatusjabatantujuan;
	}
	public void setKodestatusjabatantujuan(Integer kodestatusjabatantujuan) {
		this.kodestatusjabatantujuan = kodestatusjabatantujuan;
	}
	public String getKesimpulan() {
		return kesimpulan;
	}
	public void setKesimpulan(String kesimpulan) {
		this.kesimpulan = kesimpulan;
	}
	public Integer getKoderisikopergerakankarir() {
		return koderisikopergerakankarir;
	}
	public void setKoderisikopergerakankarir(Integer koderisikopergerakankarir) {
		this.koderisikopergerakankarir = koderisikopergerakankarir;
	}
	public Integer getR1() {
		return r1;
	}
	public void setR1(Integer r1) {
		this.r1 = r1;
	}
	public Integer getR2() {
		return r2;
	}
	public void setR2(Integer r2) {
		this.r2 = r2;
	}
	public Integer getR3() {
		return r3;
	}
	public void setR3(Integer r3) {
		this.r3 = r3;
	}
	public Integer getR4() {
		return r4;
	}
	public void setR4(Integer r4) {
		this.r4 = r4;
	}
	public Integer getR5() {
		return r5;
	}
	public void setR5(Integer r5) {
		this.r5 = r5;
	}
	public Integer getR6() {
		return r6;
	}
	public void setR6(Integer r6) {
		this.r6 = r6;
	}
	public Integer getR7() {
		return r7;
	}
	public void setR7(Integer r7) {
		this.r7 = r7;
	}
	public Integer getR8() {
		return r8;
	}
	public void setR8(Integer r8) {
		this.r8 = r8;
	}
	public Integer getR9() {
		return r9;
	}
	public void setR9(Integer r9) {
		this.r9 = r9;
	}
	public Integer getRekomendasi() {
		return rekomendasi;
	}
	public void setRekomendasi(Integer rekomendasi) {
		this.rekomendasi = rekomendasi;
	}
	public Integer getUseract() {
		return useract;
	}
	public void setUseract(Integer useract) {
		this.useract = useract;
	}
	public Float getCcijabatannett() {
		return ccijabatannett;
	}
	public void setCcijabatannett(Float ccijabatannett) {
		this.ccijabatannett = ccijabatannett;
	}
	public Float getCcijabatantujuannett() {
		return ccijabatantujuannett;
	}
	public void setCcijabatantujuannett(Float ccijabatantujuannett) {
		this.ccijabatantujuannett = ccijabatantujuannett;
	}
	public ArrayList<ListPredikat> getPredikat() {
		return predikat;
	}
	public void setPredikat(ArrayList<ListPredikat> predikat) {
		this.predikat = predikat;
	}
	public ArrayList<ListPelanggaran> getPelanggaran() {
		return pelanggaran;
	}
	public void setPelanggaran(ArrayList<ListPelanggaran> pelanggaran) {
		this.pelanggaran = pelanggaran;
	}
	public String getCatasdep() {
		return catasdep;
	}
	public void setCatasdep(String catasdep) {
		this.catasdep = catasdep;
	}
	public String getCatdepdir() {
		return catdepdir;
	}
	public void setCatdepdir(String catdepdir) {
		this.catdepdir = catdepdir;
	}
	public String getCatdir() {
		return catdir;
	}
	public void setCatdir(String catdir) {
		this.catdir = catdir;
	}
	public Integer getKoderangkaian() {
		return koderangkaian;
	}
	public void setKoderangkaian(Integer koderangkaian) {
		this.koderangkaian = koderangkaian;
	}
	

}
