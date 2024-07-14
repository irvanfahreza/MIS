package id.go.bpjskesehatan.entitas;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Autocomplete {
	private Integer kode;
	private String kode2;
	private String npp;
	private String nama;
	private Integer kodepenugasan;
	
	private Integer kodetipe;
	private String nomor;
	private String namapegawai;
	private String namasubgrade;
	private String namajobtitle;
	private String namaoffice;
	private String namaunitkerja;
	private BigDecimal gajipokok;
	private BigDecimal tunjjabatan;
	private BigDecimal tupres;
	private BigDecimal utilitas;
	private BigDecimal tunjangan;
	private String norek;
	private String kodeofficetype;
	private String namakelas;
	private String kodejobtitle;
	private String kodejobgrade;
	private Integer ik;
	private BigDecimal tunjdacil;
	private BigDecimal tunjkhusus;
	
	public Integer getKode() {
		return kode;
	}
	public void setKode(Integer kode) {
		this.kode = kode;
	}
	public String getNama() {
		return nama;
	}
	public void setNama(String nama) {
		this.nama = nama;
	}
	public String getNpp() {
		return npp;
	}
	public void setNpp(String npp) {
		this.npp = npp;
	}
	public String getKode2() {
		return kode2;
	}
	public void setKode2(String kode2) {
		this.kode2 = kode2;
	}
	public Integer getKodepenugasan() {
		return kodepenugasan;
	}
	public void setKodepenugasan(Integer kodepenugasan) {
		this.kodepenugasan = kodepenugasan;
	}
	public String getNamasubgrade() {
		return namasubgrade;
	}
	public void setNamasubgrade(String namasubgrade) {
		this.namasubgrade = namasubgrade;
	}
	public String getNamajobtitle() {
		return namajobtitle;
	}
	public void setNamajobtitle(String namajobtitle) {
		this.namajobtitle = namajobtitle;
	}
	public String getNamaoffice() {
		return namaoffice;
	}
	public void setNamaoffice(String namaoffice) {
		this.namaoffice = namaoffice;
	}
	public String getNamaunitkerja() {
		return namaunitkerja;
	}
	public void setNamaunitkerja(String namaunitkerja) {
		this.namaunitkerja = namaunitkerja;
	}
	public BigDecimal getTunjangan() {
		return tunjangan;
	}
	public void setTunjangan(BigDecimal tunjangan) {
		this.tunjangan = tunjangan;
	}
	public String getNamapegawai() {
		return namapegawai;
	}
	public void setNamapegawai(String namapegawai) {
		this.namapegawai = namapegawai;
	}
	public String getNomor() {
		return nomor;
	}
	public void setNomor(String nomor) {
		this.nomor = nomor;
	}
	public BigDecimal getGajipokok() {
		return gajipokok;
	}
	public void setGajipokok(BigDecimal gajipokok) {
		this.gajipokok = gajipokok;
	}
	public BigDecimal getTunjjabatan() {
		return tunjjabatan;
	}
	public void setTunjjabatan(BigDecimal tunjjabatan) {
		this.tunjjabatan = tunjjabatan;
	}
	public BigDecimal getTupres() {
		return tupres;
	}
	public void setTupres(BigDecimal tupres) {
		this.tupres = tupres;
	}
	public BigDecimal getUtilitas() {
		return utilitas;
	}
	public void setUtilitas(BigDecimal utilitas) {
		this.utilitas = utilitas;
	}
	public Integer getKodetipe() {
		return kodetipe;
	}
	public void setKodetipe(Integer kodetipe) {
		this.kodetipe = kodetipe;
	}
	public String getNorek() {
		return norek;
	}
	public void setNorek(String norek) {
		this.norek = norek;
	}
	public String getKodeofficetype() {
		return kodeofficetype;
	}
	public void setKodeofficetype(String kodeofficetype) {
		this.kodeofficetype = kodeofficetype;
	}
	public String getKodejobtitle() {
		return kodejobtitle;
	}
	public void setKodejobtitle(String kodejobtitle) {
		this.kodejobtitle = kodejobtitle;
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
	
}
