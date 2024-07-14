package id.go.bpjskesehatan.entitas.karyawan;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import id.go.bpjskesehatan.Constant;
import id.go.bpjskesehatan.entitas.GenericEntitas;

import java.util.Date;
import java.sql.Timestamp;

/**
 * The persistent class for the INFOASURANSI database table.
 * 
 */
@Entity
@JsonInclude(Include.NON_NULL)
@NamedQuery(name = "Infoasuransi.findAll", query = "SELECT i FROM Infoasuransi i")
public class Infoasuransi implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int kode;

	private String atasnama;

	private String deskripsi;

	private int jumlahtanggungan;

	@JsonProperty("jenisasuransi")
	@Column(name = "kodejenisasuransi")
	private GenericEntitas jenisasuransi;

	private String nama;

	private String npp;

	private String polis;

	@Temporal(TemporalType.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constant.ServerTimezone)
	private Date tat;

	@Temporal(TemporalType.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constant.ServerTimezone)
	private Date tmt;
	@JsonProperty("created_by")
	@Column(name = "CREATED_BY")
	private Integer created_by;

	@JsonProperty("created_time")
	@Column(name = "CREATED_TIME")
	private Timestamp created_time;

	@JsonProperty("lastmodified_by")
	@Column(name = "LASTMODIFIED_BY")
	private Integer lastmodified_by;

	@JsonProperty("lastmodified_time")
	@Column(name = "LASTMODIFIED_TIME")
	private Timestamp lastmodified_time;

	@JsonProperty("row_status")
	@Column(name = "ROW_STATUS")
	private Short row_status;

	public Infoasuransi() {
	}

	public int getKode() {
		return this.kode;
	}

	public void setKode(int kode) {
		this.kode = kode;
	}

	public String getAtasnama() {
		return this.atasnama;
	}

	public void setAtasnama(String atasnama) {
		this.atasnama = atasnama;
	}

	public Integer getCreated_by() {
		return this.created_by;
	}

	public void setCreated_by(Integer created_by) {
		this.created_by = created_by;
	}

	public Timestamp getCreated_time() {
		return this.created_time;
	}

	public void setCreated_time(Timestamp created_time) {
		this.created_time = created_time;
	}

	public String getDeskripsi() {
		return this.deskripsi;
	}

	public void setDeskripsi(String deskripsi) {
		this.deskripsi = deskripsi;
	}

	public int getJumlahtanggungan() {
		return this.jumlahtanggungan;
	}

	public void setJumlahtanggungan(int jumlahtanggungan) {
		this.jumlahtanggungan = jumlahtanggungan;
	}

	public GenericEntitas getJenisasuransi() {
		return jenisasuransi;
	}

	public void setJenisasuransi(GenericEntitas jenisasuransi) {
		this.jenisasuransi = jenisasuransi;
	}

	public Integer getLastmodified_by() {
		return this.lastmodified_by;
	}

	public void setLastmodified_by(Integer lastmodified_by) {
		this.lastmodified_by = lastmodified_by;
	}

	public Timestamp getLastmodified_time() {
		return this.lastmodified_time;
	}

	public void setLastmodified_time(Timestamp lastmodified_time) {
		this.lastmodified_time = lastmodified_time;
	}

	public String getNama() {
		return this.nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public String getNpp() {
		return this.npp;
	}

	public void setNpp(String npp) {
		this.npp = npp;
	}

	public String getPolis() {
		return this.polis;
	}

	public void setPolis(String polis) {
		this.polis = polis;
	}

	public Short getRow_status() {
		return this.row_status;
	}

	public void setRow_status(Short row_status) {
		this.row_status = row_status;
	}

	public Date getTat() {
		return this.tat;
	}

	public void setTat(Date tat) {
		this.tat = tat;
	}

	public Date getTmt() {
		return this.tmt;
	}

	public void setTmt(Date tmt) {
		this.tmt = tmt;
	}

}