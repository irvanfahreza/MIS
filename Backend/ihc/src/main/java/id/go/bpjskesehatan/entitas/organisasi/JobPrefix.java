package id.go.bpjskesehatan.entitas.organisasi;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Id;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * The persistent class for the job_prefix database table.
 * 
 */

@JsonInclude(Include.NON_NULL)
public class JobPrefix implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "created_by")
	private Integer created_by;

	@Column(name = "created_time")
	private Timestamp created_time;

	@Id
	@Column(name = "kode")
	private String kode;

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	@Column(name = "nama")
	private String nama;

	@Column(name = "lastmodified_by")
	private Integer lastmodified_by;

	public String getKode() {
		return kode;
	}

	public void setKode(String kode) {
		this.kode = kode;
	}

	@Column(name = "lastmodified_time")
	private Timestamp lastmodified_time;

	@Column(name = "row_status")
	private Short row_status;

	public Short getRow_status() {
		return row_status;
	}

	public void setRow_status(Short row_status) {
		this.row_status = row_status;
	}

	public JobPrefix() {
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

}