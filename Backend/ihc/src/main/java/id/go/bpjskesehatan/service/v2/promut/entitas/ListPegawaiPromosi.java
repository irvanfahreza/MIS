package id.go.bpjskesehatan.service.v2.promut.entitas;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonFormat;

import id.go.bpjskesehatan.Constant;

public class ListPegawaiPromosi {

	private Integer kode;
	private String npp;
	private Integer kodepenugasan;
	private Integer r1;
	private Integer r2;
	private Integer r3;
	private Integer r4;
	private Integer r5;
	private Integer r6;
	private Integer r7;
	private Integer r8;
	private Integer r9;
	private Integer r10;
	private Integer r11;
	private Integer r12;
	private Integer r13;
	private Integer rekomendasi;
	private String kesimpulan;
	private Integer koderisikopergerakankarir;
	private Float ccijabatannett;
	private Float ccijabatantujuannett;
	private String catasdep;
	private String catdepdir;
	private String catdir;
	private Integer koderefdiagramtalenta;
	private Integer poincci;
	private Integer bobot1;
	private Integer bobot2;
	private Integer bobot3;
	private Integer bobot4;
	private Integer bobot5;
	private Integer poinicp;
	private Integer poinidp;
	private String hasilicp;
	private String hasilidp;
	private Integer kodetelaah;
	private Integer useract;
	private String nama;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constant.ServerTimezone)
	private Date tmtjabatan;
	private String jabatanasal;
	private String unitkerjaasal;
	private String kantorasal;
	private String risikopergerakankarir;
	private String label;
	private Float poin;
	private Integer poinltp;
	private Float hasil3;
	private Float hasil4;
	private Float hasil5;
	private Integer peringkatlatsar;
	private Integer peringkatmp;
	private Integer peringkatmm;
	private ArrayList<ListPredikat> predikat = new ArrayList<>();
	private ArrayList<ListPelanggaran> pelanggaran = new ArrayList<>();
	private ArrayList<ListPengalaman> pengalaman = new ArrayList<>();
	private ArrayList<ListPelatihan> pelatihan = new ArrayList<>();
	
	
	public String getNama() {
		return nama;
	}
	public void setNama(String nama) {
		this.nama = nama;
	}
	public Date getTmtjabatan() {
		return tmtjabatan;
	}
	public void setTmtjabatan(Date tmtjabatan) {
		this.tmtjabatan = tmtjabatan;
	}
	public String getJabatanasal() {
		return jabatanasal;
	}
	public void setJabatanasal(String jabatanasal) {
		this.jabatanasal = jabatanasal;
	}
	public String getUnitkerjaasal() {
		return unitkerjaasal;
	}
	public void setUnitkerjaasal(String unitkerjaasal) {
		this.unitkerjaasal = unitkerjaasal;
	}
	public String getKantorasal() {
		return kantorasal;
	}
	public void setKantorasal(String kantorasal) {
		this.kantorasal = kantorasal;
	}
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
	public String getRisikopergerakankarir() {
		return risikopergerakankarir;
	}
	public void setRisikopergerakankarir(String risikopergerakankarir) {
		this.risikopergerakankarir = risikopergerakankarir;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public Integer getKodepenugasan() {
		return kodepenugasan;
	}
	public void setKodepenugasan(Integer kodepenugasan) {
		this.kodepenugasan = kodepenugasan;
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
	public Integer getR10() {
		return r10;
	}
	public void setR10(Integer r10) {
		this.r10 = r10;
	}
	public Integer getR11() {
		return r11;
	}
	public void setR11(Integer r11) {
		this.r11 = r11;
	}
	public Integer getR12() {
		return r12;
	}
	public void setR12(Integer r12) {
		this.r12 = r12;
	}
	public Integer getR13() {
		return r13;
	}
	public void setR13(Integer r13) {
		this.r13 = r13;
	}
	/**
	 * @return the poin
	 */
	public Float getPoin() {
		return poin;
	}
	/**
	 * @param poin the poin to set
	 */
	public void setPoin(Float poin) {
		this.poin = poin;
	}
	/**
	 * @return the koderefdiagramtalenta
	 */
	public Integer getKoderefdiagramtalenta() {
		return koderefdiagramtalenta;
	}
	/**
	 * @param koderefdiagramtalenta the koderefdiagramtalenta to set
	 */
	public void setKoderefdiagramtalenta(Integer koderefdiagramtalenta) {
		this.koderefdiagramtalenta = koderefdiagramtalenta;
	}
	public Integer getBobot1() {
		return bobot1;
	}
	public void setBobot1(Integer bobot1) {
		this.bobot1 = bobot1;
	}
	public Integer getBobot2() {
		return bobot2;
	}
	public void setBobot2(Integer bobot2) {
		this.bobot2 = bobot2;
	}
	public Integer getBobot3() {
		return bobot3;
	}
	public void setBobot3(Integer bobot3) {
		this.bobot3 = bobot3;
	}
	public Integer getKodetelaah() {
		return kodetelaah;
	}
	public void setKodetelaah(Integer kodetelaah) {
		this.kodetelaah = kodetelaah;
	}
	/**
	 * @return the poinltp
	 */
	public Integer getPoinltp() {
		return poinltp;
	}
	/**
	 * @param poinltp the poinltp to set
	 */
	public void setPoinltp(Integer poinltp) {
		this.poinltp = poinltp;
	}
	public ArrayList<ListPengalaman> getPengalaman() {
		return pengalaman;
	}
	public void setPengalaman(ArrayList<ListPengalaman> pengalaman) {
		this.pengalaman = pengalaman;
	}
	public ArrayList<ListPelatihan> getPelatihan() {
		return pelatihan;
	}
	public void setPelatihan(ArrayList<ListPelatihan> pelatihan) {
		this.pelatihan = pelatihan;
	}
	/**
	 * @return the poincci
	 */
	public Integer getPoincci() {
		return poincci;
	}
	/**
	 * @param poincci the poincci to set
	 */
	public void setPoincci(Integer poincci) {
		this.poincci = poincci;
	}
	/**
	 * @return the bobot4
	 */
	public Integer getBobot4() {
		return bobot4;
	}
	/**
	 * @param bobot4 the bobot4 to set
	 */
	public void setBobot4(Integer bobot4) {
		this.bobot4 = bobot4;
	}
	/**
	 * @return the hasil3
	 */
	public Float getHasil3() {
		return hasil3;
	}
	/**
	 * @param hasil3 the hasil3 to set
	 */
	public void setHasil3(Float hasil3) {
		this.hasil3 = hasil3;
	}
	public Integer getPeringkatlatsar() {
		return peringkatlatsar;
	}
	public void setPeringkatlatsar(Integer peringkatlatsar) {
		this.peringkatlatsar = peringkatlatsar;
	}
	public Integer getPeringkatmp() {
		return peringkatmp;
	}
	public void setPeringkatmp(Integer peringkatmp) {
		this.peringkatmp = peringkatmp;
	}
	public Integer getPeringkatmm() {
		return peringkatmm;
	}
	public void setPeringkatmm(Integer peringkatmm) {
		this.peringkatmm = peringkatmm;
	}
	public Integer getBobot5() {
		return bobot5;
	}
	public void setBobot5(Integer bobot5) {
		this.bobot5 = bobot5;
	}
	public Integer getPoinicp() {
		return poinicp;
	}
	public void setPoinicp(Integer poinicp) {
		this.poinicp = poinicp;
	}
	public Integer getPoinidp() {
		return poinidp;
	}
	public void setPoinidp(Integer poinidp) {
		this.poinidp = poinidp;
	}
	public String getHasilicp() {
		return hasilicp;
	}
	public void setHasilicp(String hasilicp) {
		this.hasilicp = hasilicp;
	}
	public String getHasilidp() {
		return hasilidp;
	}
	public void setHasilidp(String hasilidp) {
		this.hasilidp = hasilidp;
	}
	public Float getHasil4() {
		return hasil4;
	}
	public void setHasil4(Float hasil4) {
		this.hasil4 = hasil4;
	}
	public Float getHasil5() {
		return hasil5;
	}
	public void setHasil5(Float hasil5) {
		this.hasil5 = hasil5;
	}

}
