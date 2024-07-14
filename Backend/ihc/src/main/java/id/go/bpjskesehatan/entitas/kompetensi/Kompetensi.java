package id.go.bpjskesehatan.entitas.kompetensi;

import java.io.Serializable;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.sql.Timestamp;

/**
 * The persistent class for the KOMPETENSI database table.
 * 
 */
@JsonInclude(Include.NON_NULL)
public class Kompetensi implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String kode;

	private String nama;

	private String deskripsi;
	
	private Integer jumlahlevelkompetensi;
	
	@Column(name = "kodekelompokkompetensi")
	@JsonProperty("kelompokkompetensi")
	private Kelompokkompetensi kelompokkompetensi;

	@Column(name = "CREATED_BY")
	private Integer created_by;

	@Column(name = "CREATED_TIME")
	private Timestamp created_time;

	@Column(name = "LASTMODIFIED_BY")
	private Integer lastmodified_by;

	@Column(name = "LASTMODIFIED_TIME")
	private Timestamp lastmodified_time;

	@Column(name = "ROW_STATUS")
	private Short row_status;

	public Kompetensi() {
	}

	public String getKode() {
		return kode;
	}

	public void setKode(String kode) {
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
	
	public Integer getJumlahlevelkompetensi() {
		return jumlahlevelkompetensi;
	}

	public void setJumlahlevelkompetensi(Integer jumlahlevelkompetensi) {
		this.jumlahlevelkompetensi = jumlahlevelkompetensi;
	}

	public Kelompokkompetensi getKelompokkompetensi() {
		return kelompokkompetensi;
	}

	public void setKelompokkompetensi(Kelompokkompetensi kelompokkompetensi) {
		this.kelompokkompetensi = kelompokkompetensi;
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

	public Short getRow_status() {
		return row_status;
	}

	public void setRow_status(Short row_status) {
		this.row_status = row_status;
	}

}