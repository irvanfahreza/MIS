package id.go.bpjskesehatan.service.v2.promut.entitas;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;

public class ListPromosi {

	private Integer num;
	private Integer kode;
	private Integer tahun;
	private String kodehirarkijabatantujuan;
	private String kodeofficetujuan;
	private Integer kodestatusjabatantujuan;
	private Integer kodereftelaah;
	private Integer koderangkaian;
	private Integer useract;
	private String jabatantujuan;
	private String unitkerjatujuan;
	private String kantortujuan;
	private String statusjabatantujuan;
	private String namakelas;
	private String kodejobgrade;
	private String kodejobtitle;
	private Integer ik;
	private BigDecimal tunjdacil;
	private BigDecimal tunjkhusus;
	private ArrayList<ListPegawaiPromosi> pegawai = new ArrayList<>();
	private ArrayList<ListPredikat> predikat = new ArrayList<>();
	private ArrayList<ListPelanggaran> pelanggaran = new ArrayList<>();
	private ArrayList<ListPengalaman> pengalaman = new ArrayList<>();
	private ArrayList<ListPelatihan> pelatihan = new ArrayList<>();
	
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
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
	public Integer getUseract() {
		return useract;
	}
	public void setUseract(Integer useract) {
		this.useract = useract;
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
	/**
	 * @return the kodejobgrade
	 */
	public String getKodejobgrade() {
		return kodejobgrade;
	}
	/**
	 * @param kodejobgrade the kodejobgrade to set
	 */
	public void setKodejobgrade(String kodejobgrade) {
		this.kodejobgrade = kodejobgrade;
	}
	/**
	 * @return the pegawai
	 */
	public ArrayList<ListPegawaiPromosi> getPegawai() {
		return pegawai;
	}
	/**
	 * @param pegawai the pegawai to set
	 */
	public void setPegawai(ArrayList<ListPegawaiPromosi> pegawai) {
		this.pegawai = pegawai;
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
	/**
	 * @return the kodereftelaah
	 */
	public Integer getKodereftelaah() {
		return kodereftelaah;
	}
	/**
	 * @param kodereftelaah the kodereftelaah to set
	 */
	public void setKodereftelaah(Integer kodereftelaah) {
		this.kodereftelaah = kodereftelaah;
	}
	/**
	 * @return the kodejobtitle
	 */
	public String getKodejobtitle() {
		return kodejobtitle;
	}
	/**
	 * @param kodejobtitle the kodejobtitle to set
	 */
	public void setKodejobtitle(String kodejobtitle) {
		this.kodejobtitle = kodejobtitle;
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
	 * @return the tahun
	 */
	public Integer getTahun() {
		return tahun;
	}
	/**
	 * @param tahun the tahun to set
	 */
	public void setTahun(Integer tahun) {
		this.tahun = tahun;
	}

}
