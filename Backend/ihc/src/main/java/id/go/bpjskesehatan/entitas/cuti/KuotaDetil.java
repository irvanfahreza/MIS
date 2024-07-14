package id.go.bpjskesehatan.entitas.cuti;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class KuotaDetil implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Integer kode;
	private Integer kodetipe;
	private Integer kodekuota;
	private Integer kodekomposisikuota;
	private String namakomposisikuota;
	private String keterangan;
	private Integer jml;	
	private Short row_status;
	private Integer created_by;
	private Timestamp created_time;
	private Integer lastmodified_by;
	private Timestamp lastmodified_time;
	private String tanggal;
	
	public Integer getKode() {
		return kode;
	}
	public void setKode(Integer kode) {
		this.kode = kode;
	}
	public Integer getKodetipe() {
		return kodetipe;
	}
	public void setKodetipe(Integer kodetipe) {
		this.kodetipe = kodetipe;
	}
	public Integer getKodekomposisikuota() {
		return kodekomposisikuota;
	}
	public void setKodekomposisikuota(Integer kodekomposisikuota) {
		this.kodekomposisikuota = kodekomposisikuota;
	}
	public String getNamakomposisikuota() {
		return namakomposisikuota;
	}
	public void setNamakomposisikuota(String namakomposisikuota) {
		this.namakomposisikuota = namakomposisikuota;
	}
	public String getKeterangan() {
		return keterangan;
	}
	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}
	public Integer getJml() {
		return jml;
	}
	public void setJml(Integer jml) {
		this.jml = jml;
	}
	public Short getRow_status() {
		return row_status;
	}
	public void setRow_status(Short row_status) {
		this.row_status = row_status;
	}
	public Integer getCreated_by() {
		return created_by;
	}
	public void setCreated_by(Integer created_by) {
		this.created_by = created_by;
	}
	public Timestamp getCreated_time() {
		return created_time;
	}
	public void setCreated_time(Timestamp created_time) {
		this.created_time = created_time;
	}
	public Integer getLastmodified_by() {
		return lastmodified_by;
	}
	public void setLastmodified_by(Integer lastmodified_by) {
		this.lastmodified_by = lastmodified_by;
	}
	public Timestamp getLastmodified_time() {
		return lastmodified_time;
	}
	public void setLastmodified_time(Timestamp lastmodified_time) {
		this.lastmodified_time = lastmodified_time;
	}
	public Integer getKodekuota() {
		return kodekuota;
	}
	public void setKodekuota(Integer kodekuota) {
		this.kodekuota = kodekuota;
	}
	public String getTanggal() {
		return tanggal;
	}
	public void setTanggal(String tanggal) {
		this.tanggal = tanggal;
	}
	
	
}