package id.go.bpjskesehatan.entitas.organisasi;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import id.go.bpjskesehatan.Constant;

/**
 *
 * @author Bambang Purwanto
 */
@JsonInclude(Include.NON_NULL)
@Entity
public class OrganizationChart implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String kode;
	@Column(name = "nama")
	private String nama;
	@Column(name = "lampiran")
	private String lampiran;
	private String ekstensi;
	@Column(name = "tanggalaktif")
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constant.ServerTimezone)
	private Date tanggalaktif;
	@Column(name = "created_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Timestamp created_time;
	@Basic(optional = false)
	@Column(name = "created_by")
	private Integer created_by;
	@Column(name = "lastmodified_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Timestamp lastmodified_time;
	@Column(name = "lastmodified_by")
	private Integer lastmodified_by;
	@Basic(optional = false)
	@Column(name = "row_status")
	private Short row_status;
	@Column(name = "tanggal")
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constant.ServerTimezone)
	private Date tanggal;

	public OrganizationChart() {
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public String getLampiran() {
		return lampiran;
	}

	public void setLampiran(String lampiran) {
		this.lampiran = lampiran;
	}

	public String getEkstensi() {
		return ekstensi;
	}

	public void setEkstensi(String ekstensi) {
		this.ekstensi = ekstensi;
	}

	public Date getTanggalaktif() {
		return tanggalaktif;
	}

	public void setTanggalaktif(Date tanggalaktif) {
		this.tanggalaktif = tanggalaktif;
	}

	public Timestamp getCreated_time() {
		return created_time;
	}

	public void setCreated_time(Timestamp created_time) {
		this.created_time = created_time;
	}

	public Integer getCreated_by() {
		return created_by;
	}

	public void setCreated_by(Integer created_by) {
		this.created_by = created_by;
	}

	public Timestamp getLastmodified_time() {
		return lastmodified_time;
	}

	public void setLastmodified_time(Timestamp lastmodified_time) {
		this.lastmodified_time = lastmodified_time;
	}

	public Integer getLastmodified_by() {
		return lastmodified_by;
	}

	public void setLastmodified_by(Integer lastmodified_by) {
		this.lastmodified_by = lastmodified_by;
	}

	public Short getRow_status() {
		return row_status;
	}

	public void setRow_status(Short row_status) {
		this.row_status = row_status;
	}

	public Date getTanggal() {
		return tanggal;
	}

	public void setTanggal(Date tanggal) {
		this.tanggal = tanggal;
	}

	public String getKode() {
		return kode;
	}

	public void setKode(String kode) {
		this.kode = kode;
	}

}
