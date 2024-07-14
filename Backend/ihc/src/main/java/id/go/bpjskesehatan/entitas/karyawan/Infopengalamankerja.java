package id.go.bpjskesehatan.entitas.karyawan;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import id.go.bpjskesehatan.Constant;

import java.util.Date;
import java.sql.Timestamp;

/**
 * The persistent class for the INFOPENGALAMANKERJA database table.
 * 
 */
@JsonInclude(Include.NON_NULL)
@Entity
@NamedQuery(name = "Infopengalamankerja.findAll", query = "SELECT i FROM Infopengalamankerja i")
public class Infopengalamankerja implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int kode;

	private String catatan;

	@Column(name = "CREATED_BY")
	private Integer created_by;

	@Column(name = "CREATED_TIME")
	private Timestamp created_time;

	private String jabatan;

	@Column(name = "LASTMODIFIED_BY")
	private Integer lastmodified_by;

	@Column(name = "LASTMODIFIED_TIME")
	private Timestamp lastmodified_time;

	private String lokasikerja;

	private String npp;

	private String perusahaan;
	
	private int adalampiran;
	
	public int getAdalampiran() {
		return adalampiran;
	}

	public void setAdalampiran(int adalampiran) {
		this.adalampiran = adalampiran;
	}

	@Lob
	private String lampiran;

	@Column(name = "ROW_STATUS")
	private Short row_status;

	private String tanggungjawab;

	@Temporal(TemporalType.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constant.ServerTimezone)
	private Date tat;

	@Temporal(TemporalType.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constant.ServerTimezone)
	private Date tmt;

	public Infopengalamankerja() {
	}

	public int getKode() {
		return this.kode;
	}

	public void setKode(int kode) {
		this.kode = kode;
	}

	public String getCatatan() {
		return this.catatan;
	}

	public void setCatatan(String catatan) {
		this.catatan = catatan;
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

	public String getJabatan() {
		return this.jabatan;
	}

	public void setJabatan(String jabatan) {
		this.jabatan = jabatan;
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

	public String getLokasikerja() {
		return this.lokasikerja;
	}

	public void setLokasikerja(String lokasikerja) {
		this.lokasikerja = lokasikerja;
	}

	public String getNpp() {
		return this.npp;
	}

	public void setNpp(String npp) {
		this.npp = npp;
	}

	public String getPerusahaan() {
		return this.perusahaan;
	}

	public void setPerusahaan(String perusahaan) {
		this.perusahaan = perusahaan;
	}

	public Short getRow_status() {
		return this.row_status;
	}

	public void setRow_status(Short row_status) {
		this.row_status = row_status;
	}

	public String getTanggungjawab() {
		return this.tanggungjawab;
	}

	public void setTanggungjawab(String tanggungjawab) {
		this.tanggungjawab = tanggungjawab;
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

	public String getLampiran() {
		return lampiran;
	}

	public void setLampiran(String lampiran) {
		this.lampiran = lampiran;
	}

}