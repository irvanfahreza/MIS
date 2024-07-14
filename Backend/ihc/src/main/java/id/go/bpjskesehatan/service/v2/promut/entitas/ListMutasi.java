package id.go.bpjskesehatan.service.v2.promut.entitas;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonFormat;

import id.go.bpjskesehatan.Constant;

public class ListMutasi {

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
	private String nama;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constant.ServerTimezone)
	private Date tmtjabatan;
	private String jabatanasal;
	private String unitkerjaasal;
	private String kantorasal;
	private String jabatantujuan;
	private String unitkerjatujuan;
	private String kantortujuan;
	private String statusjabatantujuan;
	private String risikopergerakankarir;
	private String label;
	private String kodejobgrade;
	private String namakelas;
	private Integer ik;
	private BigDecimal tunjdacil;
	private BigDecimal tunjkhusus;
	private Integer num;
	private Float poin;
	private String kodegrade;
	private Boolean rekom;
	private ArrayList<ListPredikat> predikat = new ArrayList<>();
	private ArrayList<ListPelanggaran> pelanggaran = new ArrayList<>();
	
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
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
	public String getJabatantujuan() {
		return jabatantujuan;
	}
	public void setJabatantujuan(String jabatantujuan) {
		this.jabatantujuan = jabatantujuan;
	}
	public String getUnitkerjatujuan() {
		return unitkerjatujuan;
	}
	public void setUnitkerjatujuan(String unitkerjatujuan) {
		this.unitkerjatujuan = unitkerjatujuan;
	}
	public String getKantortujuan() {
		return kantortujuan;
	}
	public void setKantortujuan(String kantortujuan) {
		this.kantortujuan = kantortujuan;
	}
	public String getStatusjabatantujuan() {
		return statusjabatantujuan;
	}
	public void setStatusjabatantujuan(String statusjabatantujuan) {
		this.statusjabatantujuan = statusjabatantujuan;
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
	public String getKodejobgrade() {
		return kodejobgrade;
	}
	public void setKodejobgrade(String kodejobgrade) {
		this.kodejobgrade = kodejobgrade;
	}
	public String getNamakelas() {
		return namakelas;
	}
	public void setNamakelas(String namakelas) {
		this.namakelas = namakelas;
	}
	public Integer getIk() {
		return ik;
	}
	public void setIk(Integer ik) {
		this.ik = ik;
	}
	public BigDecimal getTunjdacil() {
		return tunjdacil;
	}
	public void setTunjdacil(BigDecimal tunjdacil) {
		this.tunjdacil = tunjdacil;
	}
	public BigDecimal getTunjkhusus() {
		return tunjkhusus;
	}
	public void setTunjkhusus(BigDecimal tunjkhusus) {
		this.tunjkhusus = tunjkhusus;
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
	public Float getPoin() {
		return poin;
	}
	public void setPoin(Float poin) {
		this.poin = poin;
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
	public Boolean getRekom() {
		return rekom;
	}
	public void setRekom(Boolean rekom) {
		this.rekom = rekom;
	}
	public String getKodegrade() {
		return kodegrade;
	}
	public void setKodegrade(String kodegrade) {
		this.kodegrade = kodegrade;
	}
	/**
	 * @return the koderangkaian
	 */
	public Integer getKoderangkaian() {
		return koderangkaian;
	}
	/**
	 * @param koderangkaian the koderangkaian to set
	 */
	public void setKoderangkaian(Integer koderangkaian) {
		this.koderangkaian = koderangkaian;
	}

}
