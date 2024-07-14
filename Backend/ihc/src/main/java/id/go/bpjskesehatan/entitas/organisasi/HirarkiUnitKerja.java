package id.go.bpjskesehatan.entitas.organisasi;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 *
 * @author Bambang Purwanto
 */
@JsonInclude(Include.NON_NULL)
@Entity
public class HirarkiUnitKerja implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "KODE")
	private String kode;
	@Column(name = "NAMA")
	private String nama;
	@Column(name = "LEVEL")
	private Short level;
	@Basic(optional = false)
	@Column(name = "CREATED_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Timestamp created_time;
	@Basic(optional = false)
	@Column(name = "CREATED_BY")
	private Integer created_by;
	@Column(name = "LASTMODIFIED_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Timestamp lastmodified_time;
	@Column(name = "LASTMODIFIED_BY")
	private Integer lastmodified_by;
	@Basic(optional = false)
	@Column(name = "ROW_STATUS")
	private Short row_status;

	public HirarkiUnitKerja() {
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

	public Short getLevel() {
		return level;
	}

	public void setLevel(Short level) {
		this.level = level;
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

}