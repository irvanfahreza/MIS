package id.go.bpjskesehatan.service.v2.payroll.entitas;

import java.math.BigDecimal;
import java.util.ArrayList;

public class ListPayroll {

	private Integer num;
	private Integer kode;
	private Integer tahun;
	private Integer bulan;
	private Integer kodepenugasan;
	private BigDecimal gajipokok;
	private Integer ik;
	private Integer persentupres;
	private BigDecimal pendapatan;
	private BigDecimal potongan;
	private BigDecimal total;
	private Integer row_status;
	private Integer useract;
	private String npp;
	private String nama;
	private String jabatan;
	private String unitkerja;
	private String kodekantor;
	private String kantor;
	private Integer kodestatuskaryawan;
	private String subgrade;
	private String grade;
	private String jobgrade;
	private String nmfile;
	private Integer statusapprove;
	private Integer kodejenispegawai;
	private Integer tahunslip;
	private Integer bulanslip;
	private ArrayList<ListDetilPayroll> detilPendapatan = new ArrayList<>();
	private ArrayList<ListDetilPayroll> detilPotongan = new ArrayList<>();
	private ArrayList<ListDetilPayroll> detilPendapatanLain = new ArrayList<>();
	private ArrayList<ListDetilPayroll> detilPotonganLain = new ArrayList<>();
	
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public Integer getKode() {
		return kode;
	}
	public void setKode(Integer kode) {
		this.kode = kode;
	}
	public Integer getTahun() {
		return tahun;
	}
	public void setTahun(Integer tahun) {
		this.tahun = tahun;
	}
	public Integer getBulan() {
		return bulan;
	}
	public void setBulan(Integer bulan) {
		this.bulan = bulan;
	}
	public Integer getKodepenugasan() {
		return kodepenugasan;
	}
	public void setKodepenugasan(Integer kodepenugasan) {
		this.kodepenugasan = kodepenugasan;
	}
	public BigDecimal getGajipokok() {
		return gajipokok;
	}
	public void setGajipokok(BigDecimal gajipokok) {
		this.gajipokok = gajipokok;
	}
	public Integer getIk() {
		return ik;
	}
	public void setIk(Integer ik) {
		this.ik = ik;
	}
	public Integer getPersentupres() {
		return persentupres;
	}
	public void setPersentupres(Integer persentupres) {
		this.persentupres = persentupres;
	}
	public BigDecimal getPendapatan() {
		return pendapatan;
	}
	public void setPendapatan(BigDecimal pendapatan) {
		this.pendapatan = pendapatan;
	}
	public BigDecimal getPotongan() {
		return potongan;
	}
	public void setPotongan(BigDecimal potongan) {
		this.potongan = potongan;
	}
	public BigDecimal getTotal() {
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	public Integer getRow_status() {
		return row_status;
	}
	public void setRow_status(Integer row_status) {
		this.row_status = row_status;
	}
	public Integer getUseract() {
		return useract;
	}
	public void setUseract(Integer useract) {
		this.useract = useract;
	}
	public String getNpp() {
		return npp;
	}
	public void setNpp(String npp) {
		this.npp = npp;
	}
	public String getNama() {
		return nama;
	}
	public void setNama(String nama) {
		this.nama = nama;
	}
	public String getJabatan() {
		return jabatan;
	}
	public void setJabatan(String jabatan) {
		this.jabatan = jabatan;
	}
	public String getUnitkerja() {
		return unitkerja;
	}
	public void setUnitkerja(String unitkerja) {
		this.unitkerja = unitkerja;
	}
	public String getKantor() {
		return kantor;
	}
	public void setKantor(String kantor) {
		this.kantor = kantor;
	}
	public Integer getKodestatuskaryawan() {
		return kodestatuskaryawan;
	}
	public void setKodestatuskaryawan(Integer kodestatuskaryawan) {
		this.kodestatuskaryawan = kodestatuskaryawan;
	}
	public ArrayList<ListDetilPayroll> getDetilPendapatan() {
		return detilPendapatan;
	}
	public void setDetilPendapatan(ArrayList<ListDetilPayroll> detilPendapatan) {
		this.detilPendapatan = detilPendapatan;
	}
	public ArrayList<ListDetilPayroll> getDetilPotongan() {
		return detilPotongan;
	}
	public void setDetilPotongan(ArrayList<ListDetilPayroll> detilPotongan) {
		this.detilPotongan = detilPotongan;
	}
	public ArrayList<ListDetilPayroll> getDetilPendapatanLain() {
		return detilPendapatanLain;
	}
	public void setDetilPendapatanLain(ArrayList<ListDetilPayroll> detilPendapatanLain) {
		this.detilPendapatanLain = detilPendapatanLain;
	}
	public ArrayList<ListDetilPayroll> getDetilPotonganLain() {
		return detilPotonganLain;
	}
	public void setDetilPotonganLain(ArrayList<ListDetilPayroll> detilPotonganLain) {
		this.detilPotonganLain = detilPotonganLain;
	}
	public String getKodekantor() {
		return kodekantor;
	}
	public void setKodekantor(String kodekantor) {
		this.kodekantor = kodekantor;
	}
	public String getSubgrade() {
		return subgrade;
	}
	public void setSubgrade(String subgrade) {
		this.subgrade = subgrade;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getJobgrade() {
		return jobgrade;
	}
	public void setJobgrade(String jobgrade) {
		this.jobgrade = jobgrade;
	}
	public Integer getStatusapprove() {
		return statusapprove;
	}
	public void setStatusapprove(Integer statusapprove) {
		this.statusapprove = statusapprove;
	}
	public String getNmfile() {
		return nmfile;
	}
	public void setNmfile(String nmfile) {
		this.nmfile = nmfile;
	}
	public Integer getKodejenispegawai() {
		return kodejenispegawai;
	}
	public void setKodejenispegawai(Integer kodejenispegawai) {
		this.kodejenispegawai = kodejenispegawai;
	}
	public Integer getTahunslip() {
		return tahunslip;
	}
	public void setTahunslip(Integer tahunslip) {
		this.tahunslip = tahunslip;
	}
	public Integer getBulanslip() {
		return bulanslip;
	}
	public void setBulanslip(Integer bulanslip) {
		this.bulanslip = bulanslip;
	}

}
