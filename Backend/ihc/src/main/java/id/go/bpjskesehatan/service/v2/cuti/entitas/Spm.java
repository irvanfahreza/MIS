package id.go.bpjskesehatan.service.v2.cuti.entitas;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import id.go.bpjskesehatan.service.v2.entitas.Anggaran;

@JsonInclude(Include.NON_NULL)
public class Spm {

	private Integer kode;
	private String noarsip;
	private String nomor;
	private String kodeoffice;
	private String created_name;
	private Integer created_by;
	private Timestamp created_time;
	private Integer bulan;
	private Integer tahun;
	private String tgl;
	private BigDecimal total;
	private String kepada;
	private Anggaran anggaran;
	private Integer kodetipe;
	private String namatipe;
	private ArrayList<SpmDetil> spmdetil = new ArrayList<>();
	
	public Integer getKode() {
		return kode;
	}
	public void setKode(Integer kode) {
		this.kode = kode;
	}
	public String getNoarsip() {
		return noarsip;
	}
	public void setNoarsip(String noarsip) {
		this.noarsip = noarsip;
	}
	public Timestamp getCreated_time() {
		return created_time;
	}
	public void setCreated_time(Timestamp created_time) {
		this.created_time = created_time;
	}
	public ArrayList<SpmDetil> getSpmdetil() {
		return spmdetil;
	}
	public void setSpmdetil(ArrayList<SpmDetil> spmdetil) {
		this.spmdetil = spmdetil;
	}
	public Integer getCreated_by() {
		return created_by;
	}
	public void setCreated_by(Integer created_by) {
		this.created_by = created_by;
	}
	public String getCreated_name() {
		return created_name;
	}
	public void setCreated_name(String created_name) {
		this.created_name = created_name;
	}
	public Integer getBulan() {
		return bulan;
	}
	public void setBulan(Integer bulan) {
		this.bulan = bulan;
	}
	public Integer getTahun() {
		return tahun;
	}
	public void setTahun(Integer tahun) {
		this.tahun = tahun;
	}
	public String getTgl() {
		return tgl;
	}
	public void setTgl(String tgl) {
		this.tgl = tgl;
	}
	public String getKodeoffice() {
		return kodeoffice;
	}
	public void setKodeoffice(String kodeoffice) {
		this.kodeoffice = kodeoffice;
	}
	public BigDecimal getTotal() {
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	public Anggaran getAnggaran() {
		return anggaran;
	}
	public void setAnggaran(Anggaran anggaran) {
		this.anggaran = anggaran;
	}
	public String getKepada() {
		return kepada;
	}
	public void setKepada(String kepada) {
		this.kepada = kepada;
	}
	public Integer getKodetipe() {
		return kodetipe;
	}
	public void setKodetipe(Integer kodetipe) {
		this.kodetipe = kodetipe;
	}
	public String getNamatipe() {
		return namatipe;
	}
	public void setNamatipe(String namatipe) {
		this.namatipe = namatipe;
	}
	public String getNomor() {
		return nomor;
	}
	public void setNomor(String nomor) {
		this.nomor = nomor;
	}
	
}