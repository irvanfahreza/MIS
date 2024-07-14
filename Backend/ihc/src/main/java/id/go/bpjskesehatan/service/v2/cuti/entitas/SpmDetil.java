package id.go.bpjskesehatan.service.v2.cuti.entitas;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class SpmDetil {

	private Integer no;
	private Integer kode;
	private Integer kodespm;
	private Integer kodecuti;
	private Integer kodetipe;
	private String nomor;
	private String npp;
	private String nama;
	private String namagrade;
	private String namasubgrade;
	private String namajobtitle;
	private String namaunitkerja;
	private String namaoffice;
	private Integer bulanslip;
	private BigDecimal gajipokok;
	private BigDecimal tunjjabatan;
	private BigDecimal nominaltunjanganbbm;
	private BigDecimal tupres;
	private BigDecimal utilitas;
	private BigDecimal total;
	private String norek;
	private Integer deleted;
	
	public Integer getKode() {
		return kode;
	}
	public void setKode(Integer kode) {
		this.kode = kode;
	}
	public Integer getKodespm() {
		return kodespm;
	}
	public void setKodespm(Integer kodespm) {
		this.kodespm = kodespm;
	}
	public Integer getKodecuti() {
		return kodecuti;
	}
	public void setKodecuti(Integer kodecuti) {
		this.kodecuti = kodecuti;
	}
	public String getNomor() {
		return nomor;
	}
	public void setNomor(String nomor) {
		this.nomor = nomor;
	}
	public String getNama() {
		return nama;
	}
	public void setNama(String nama) {
		this.nama = nama;
	}
	public String getNamagrade() {
		return namagrade;
	}
	public void setNamagrade(String namagrade) {
		this.namagrade = namagrade;
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
	public String getNamaunitkerja() {
		return namaunitkerja;
	}
	public void setNamaunitkerja(String namaunitkerja) {
		this.namaunitkerja = namaunitkerja;
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
	public BigDecimal getNominaltunjanganbbm() {
		return nominaltunjanganbbm;
	}
	public void setNominaltunjanganbbm(BigDecimal nominaltunjanganbbm) {
		this.nominaltunjanganbbm = nominaltunjanganbbm;
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
	public BigDecimal getTotal() {
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	public String getNorek() {
		return norek;
	}
	public void setNorek(String norek) {
		this.norek = norek;
	}
	public Integer getDeleted() {
		return deleted;
	}
	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}
	public String getNamaoffice() {
		return namaoffice;
	}
	public void setNamaoffice(String namaoffice) {
		this.namaoffice = namaoffice;
	}
	public Integer getNo() {
		return no;
	}
	public void setNo(Integer no) {
		this.no = no;
	}
	public Integer getBulanslip() {
		return bulanslip;
	}
	public void setBulanslip(Integer bulanslip) {
		this.bulanslip = bulanslip;
	}
	public Integer getKodetipe() {
		return kodetipe;
	}
	public void setKodetipe(Integer kodetipe) {
		this.kodetipe = kodetipe;
	}
	public String getNpp() {
		return npp;
	}
	public void setNpp(String npp) {
		this.npp = npp;
	}
}