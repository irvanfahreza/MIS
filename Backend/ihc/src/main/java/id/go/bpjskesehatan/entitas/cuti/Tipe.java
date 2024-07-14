package id.go.bpjskesehatan.entitas.cuti;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.sql.Timestamp;

@JsonInclude(Include.NON_NULL)
public class Tipe implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Integer kode;
	private String alias;
	private String nama;
	private String deskripsi;
	private Integer jenis;
	private String namajenis;
	private Integer abaikanlibur;
	private String ketabaikanlibur;
	private Short row_status;
	private Integer created_by;
	private Timestamp created_time;
	private Integer lastmodified_by;
	private Timestamp lastmodified_time;
	private Integer kuota;
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
	public String getDeskripsi() {
		return deskripsi;
	}
	public void setDeskripsi(String deskripsi) {
		this.deskripsi = deskripsi;
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
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public Integer getKuota() {
		return kuota;
	}
	public void setKuota(Integer kuota) {
		this.kuota = kuota;
	}
	public Integer getJenis() {
		return jenis;
	}
	public void setJenis(Integer jenis) {
		this.jenis = jenis;
	}
	public String getNamajenis() {
		return namajenis;
	}
	public void setNamajenis(String namajenis) {
		this.namajenis = namajenis;
	}
	public Integer getAbaikanlibur() {
		return abaikanlibur;
	}
	public void setAbaikanlibur(Integer abaikanlibur) {
		this.abaikanlibur = abaikanlibur;
	}
	public String getKetabaikanlibur() {
		return ketabaikanlibur;
	}
	public void setKetabaikanlibur(String ketabaikanlibur) {
		this.ketabaikanlibur = ketabaikanlibur;
	}
}