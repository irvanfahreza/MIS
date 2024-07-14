package id.go.bpjskesehatan.entitas.cuti;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import id.go.bpjskesehatan.Constant;

@JsonInclude(Include.NON_NULL)
public class SaveCuti {

	private Integer kode;
	private String npp;
	private String nama;
	private Integer kodepenugasan;
	private Integer kodetipe;
	private String namatipe;
	private Integer dengantunjangan;
	private String telp;
	private String alamatcuti;
	private String alasancuti;
	private String lampiran;
	private Integer useract;
	private BigDecimal gajipokok;
	private BigDecimal tunjanganprestasi;
	private BigDecimal tunjanganjabatan;
	private BigDecimal tunjanganutilitas;
	private BigDecimal total;
	private BigDecimal total2;
	private BigDecimal nominaltunjanganbbm;
	private String pangkat;
	private String grade;
	private String kodejobgrade;
	private Timestamp tglajukan;
	private Integer statuspersetujuan;
	private String namajobtitle;
	private String namaunitkerja;
	private String namaoffice;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constant.ServerTimezone)
	private Date tglmulai;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constant.ServerTimezone)
	private Date tglselesai;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constant.ServerTimezone)
	private Date tglmulai2;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constant.ServerTimezone)
	private Date tglselesai2;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constant.ServerTimezone)
	private Date tglhpl;
	private Integer ispembatalan;
	private String statusterakhir;
	private Integer jenispengajuan;
	private String nomor;
	private Integer tunjanganbbm;
	private Integer verifsdm;
	private Integer verifsdmkepwil;
	private Integer verifsdmkc;
	private Integer verifkeu;
	private Integer ambilhari;
	private Integer jeniskantor;
	
	private String tglcuti;
	private String cetakspm;
	
	private String statusverifsdm;
	private String statusverifkeu;
	private String mengetahuidepdirsdm;
	private Integer bolehverifsdm;
	private Integer bolehverifkeu;
	private String catatantolak;
	private Integer sdmbolehtolak;
	
	private Boolean selected;
	private Integer vselected;
	private ArrayList<SaveCutiTgls> tgls = new ArrayList<>();
	
	private Integer tahuninput;
	
	private String kodejobtitleatasan;
	private String kodeofficeatasan;
	private String nppatasan;
	private Integer kodepenugasanatasan;
	
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
	public Integer getKodetipe() {
		return kodetipe;
	}
	public void setKodetipe(Integer kodetipe) {
		this.kodetipe = kodetipe;
	}
	public Integer getDengantunjangan() {
		return dengantunjangan;
	}
	public void setDengantunjangan(Integer dengantunjangan) {
		this.dengantunjangan = dengantunjangan;
	}
	public String getTelp() {
		return telp;
	}
	public void setTelp(String telp) {
		this.telp = telp;
	}
	public String getAlamatcuti() {
		return alamatcuti;
	}
	public void setAlamatcuti(String alamatcuti) {
		this.alamatcuti = alamatcuti;
	}
	public String getAlasancuti() {
		return alasancuti;
	}
	public void setAlasancuti(String alasancuti) {
		this.alasancuti = alasancuti;
	}
	public Integer getUseract() {
		return useract;
	}
	public void setUseract(Integer useract) {
		this.useract = useract;
	}
	public ArrayList<SaveCutiTgls> getTgls() {
		return tgls;
	}
	public void setTgls(ArrayList<SaveCutiTgls> tgls) {
		this.tgls = tgls;
	}
	public String getLampiran() {
		return lampiran;
	}
	public void setLampiran(String lampiran) {
		this.lampiran = lampiran;
	}
	public String getNamatipe() {
		return namatipe;
	}
	public void setNamatipe(String namatipe) {
		this.namatipe = namatipe;
	}
	public BigDecimal getGajipokok() {
		return gajipokok;
	}
	public void setGajipokok(BigDecimal gajipokok) {
		this.gajipokok = gajipokok;
	}
	public BigDecimal getTunjanganprestasi() {
		return tunjanganprestasi;
	}
	public void setTunjanganprestasi(BigDecimal tunjanganprestasi) {
		this.tunjanganprestasi = tunjanganprestasi;
	}
	public BigDecimal getTunjanganjabatan() {
		return tunjanganjabatan;
	}
	public void setTunjanganjabatan(BigDecimal tunjanganjabatan) {
		this.tunjanganjabatan = tunjanganjabatan;
	}
	public BigDecimal getTunjanganutilitas() {
		return tunjanganutilitas;
	}
	public void setTunjanganutilitas(BigDecimal tunjanganutilitas) {
		this.tunjanganutilitas = tunjanganutilitas;
	}
	public BigDecimal getTotal() {
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	public Timestamp getTglajukan() {
		return tglajukan;
	}
	public void setTglajukan(Timestamp tglajukan) {
		this.tglajukan = tglajukan;
	}
	public Integer getStatuspersetujuan() {
		return statuspersetujuan;
	}
	public void setStatuspersetujuan(Integer statuspersetujuan) {
		this.statuspersetujuan = statuspersetujuan;
	}
	public String getNamajobtitle() {
		return namajobtitle;
	}
	public void setNamajobtitle(String namajobtitle) {
		this.namajobtitle = namajobtitle;
	}
	public String getNamaunitkerja() {
		return namaunitkerja;
	}
	public void setNamaunitkerja(String namaunitkerja) {
		this.namaunitkerja = namaunitkerja;
	}
	public String getNama() {
		return nama;
	}
	public void setNama(String nama) {
		this.nama = nama;
	}
	public BigDecimal getTotal2() {
		return total2;
	}
	public void setTotal2(BigDecimal total2) {
		this.total2 = total2;
	}
	public Date getTglmulai() {
		return tglmulai;
	}
	public void setTglmulai(Date tglmulai) {
		this.tglmulai = tglmulai;
	}
	public Date getTglselesai() {
		return tglselesai;
	}
	public void setTglselesai(Date tglselesai) {
		this.tglselesai = tglselesai;
	}
	public Integer getIspembatalan() {
		return ispembatalan;
	}
	public void setIspembatalan(Integer ispembatalan) {
		this.ispembatalan = ispembatalan;
	}
	public String getStatusterakhir() {
		return statusterakhir;
	}
	public void setStatusterakhir(String statusterakhir) {
		this.statusterakhir = statusterakhir;
	}
	public Boolean getSelected() {
		return selected;
	}
	public void setSelected(Boolean selected) {
		this.selected = selected;
	}
	public Integer getVselected() {
		return vselected;
	}
	public void setVselected(Integer vselected) {
		this.vselected = vselected;
	}
	public String getNamaoffice() {
		return namaoffice;
	}
	public void setNamaoffice(String namaoffice) {
		this.namaoffice = namaoffice;
	}
	public Integer getJenispengajuan() {
		return jenispengajuan;
	}
	public void setJenispengajuan(Integer jenispengajuan) {
		this.jenispengajuan = jenispengajuan;
	}
	public String getPangkat() {
		return pangkat;
	}
	public void setPangkat(String pangkat) {
		this.pangkat = pangkat;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getNomor() {
		return nomor;
	}
	public void setNomor(String nomor) {
		this.nomor = nomor;
	}
	public BigDecimal getNominaltunjanganbbm() {
		return nominaltunjanganbbm;
	}
	public void setNominaltunjanganbbm(BigDecimal nominaltunjanganbbm) {
		this.nominaltunjanganbbm = nominaltunjanganbbm;
	}
	public Integer getTunjanganbbm() {
		return tunjanganbbm;
	}
	public void setTunjanganbbm(Integer tunjanganbbm) {
		this.tunjanganbbm = tunjanganbbm;
	}
	public Integer getVerifsdm() {
		return verifsdm;
	}
	public void setVerifsdm(Integer verifsdm) {
		this.verifsdm = verifsdm;
	}
	public Integer getVerifkeu() {
		return verifkeu;
	}
	public void setVerifkeu(Integer verifkeu) {
		this.verifkeu = verifkeu;
	}
	public String getStatusverifsdm() {
		return statusverifsdm;
	}
	public void setStatusverifsdm(String statusverifsdm) {
		this.statusverifsdm = statusverifsdm;
	}
	public String getStatusverifkeu() {
		return statusverifkeu;
	}
	public void setStatusverifkeu(String statusverifkeu) {
		this.statusverifkeu = statusverifkeu;
	}
	public Date getTglmulai2() {
		return tglmulai2;
	}
	public void setTglmulai2(Date tglmulai2) {
		this.tglmulai2 = tglmulai2;
	}
	public Date getTglselesai2() {
		return tglselesai2;
	}
	public void setTglselesai2(Date tglselesai2) {
		this.tglselesai2 = tglselesai2;
	}
	public Date getTglhpl() {
		return tglhpl;
	}
	public void setTglhpl(Date tglhpl) {
		this.tglhpl = tglhpl;
	}
	public Integer getAmbilhari() {
		return ambilhari;
	}
	public void setAmbilhari(Integer ambilhari) {
		this.ambilhari = ambilhari;
	}
	public String getMengetahuidepdirsdm() {
		return mengetahuidepdirsdm;
	}
	public void setMengetahuidepdirsdm(String mengetahuidepdirsdm) {
		this.mengetahuidepdirsdm = mengetahuidepdirsdm;
	}
	public String getTglcuti() {
		return tglcuti;
	}
	public void setTglcuti(String tglcuti) {
		this.tglcuti = tglcuti;
	}
	public String getCetakspm() {
		return cetakspm;
	}
	public void setCetakspm(String cetakspm) {
		this.cetakspm = cetakspm;
	}
	public Integer getVerifsdmkepwil() {
		return verifsdmkepwil;
	}
	public void setVerifsdmkepwil(Integer verifsdmkepwil) {
		this.verifsdmkepwil = verifsdmkepwil;
	}
	public Integer getVerifsdmkc() {
		return verifsdmkc;
	}
	public void setVerifsdmkc(Integer verifsdmkc) {
		this.verifsdmkc = verifsdmkc;
	}
	public Integer getJeniskantor() {
		return jeniskantor;
	}
	public void setJeniskantor(Integer jeniskantor) {
		this.jeniskantor = jeniskantor;
	}
	public Integer getBolehverifsdm() {
		return bolehverifsdm;
	}
	public void setBolehverifsdm(Integer bolehverifsdm) {
		this.bolehverifsdm = bolehverifsdm;
	}
	public Integer getBolehverifkeu() {
		return bolehverifkeu;
	}
	public void setBolehverifkeu(Integer bolehverifkeu) {
		this.bolehverifkeu = bolehverifkeu;
	}
	public String getCatatantolak() {
		return catatantolak;
	}
	public void setCatatantolak(String catatantolak) {
		this.catatantolak = catatantolak;
	}
	public Integer getSdmbolehtolak() {
		return sdmbolehtolak;
	}
	public void setSdmbolehtolak(Integer sdmbolehtolak) {
		this.sdmbolehtolak = sdmbolehtolak;
	}
	public String getKodejobgrade() {
		return kodejobgrade;
	}
	public void setKodejobgrade(String kodejobgrade) {
		this.kodejobgrade = kodejobgrade;
	}
	public Integer getTahuninput() {
		return tahuninput;
	}
	public void setTahuninput(Integer tahuninput) {
		this.tahuninput = tahuninput;
	}
	public String getKodejobtitleatasan() {
		return kodejobtitleatasan;
	}
	public void setKodejobtitleatasan(String kodejobtitleatasan) {
		this.kodejobtitleatasan = kodejobtitleatasan;
	}
	public String getKodeofficeatasan() {
		return kodeofficeatasan;
	}
	public void setKodeofficeatasan(String kodeofficeatasan) {
		this.kodeofficeatasan = kodeofficeatasan;
	}
	public String getNppatasan() {
		return nppatasan;
	}
	public void setNppatasan(String nppatasan) {
		this.nppatasan = nppatasan;
	}
	public Integer getKodepenugasanatasan() {
		return kodepenugasanatasan;
	}
	public void setKodepenugasanatasan(Integer kodepenugasanatasan) {
		this.kodepenugasanatasan = kodepenugasanatasan;
	}
}