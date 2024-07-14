package id.go.bpjskesehatan.entitas.organisasi;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 *
 * @author Bambang Purwanto
 */
@JsonInclude(Include.NON_NULL)
@Entity
public class JobTitle implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "kode")
	private String kode;

	@Column(name = "nama")
	private String nama;

	@Column(name = "created_time")
	private Timestamp created_time;

	@Column(name = "created_by")
	private Integer created_by;
	@Column(name = "lastmodified_time")
	private Timestamp lastmodified_time;

	@Column(name = "lastmodified_by")
	private Integer lastmodified_by;

	@Column(name = "row_status")
	private Short row_status;

	@JsonProperty("jobprefix")
	@Column(name = "kodejobprefix")
	private JobPrefix jobprefix;

	@JsonProperty("functionalscope")
	@Column(name = "kodeFunctionalScope")
	private FunctionalScope functionalscope;
	
	@Column(name = "tunjjabatan")
	private BigDecimal tunjjabatan;
	
	private Integer tunjanganbbm;

	public JobTitle() {
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

	public JobPrefix getJobprefix() {
		return jobprefix;
	}

	public void setJobprefix(JobPrefix jobprefix) {
		this.jobprefix = jobprefix;
	}

	public FunctionalScope getFunctionalscope() {
		return functionalscope;
	}

	public void setFunctionalscope(FunctionalScope functionalscope) {
		this.functionalscope = functionalscope;
	}

	public BigDecimal getTunjjabatan() {
		return tunjjabatan;
	}

	public void setTunjjabatan(BigDecimal tunjjabatan) {
		this.tunjjabatan = tunjjabatan;
	}

	public Integer getTunjanganbbm() {
		return tunjanganbbm;
	}

	public void setTunjanganbbm(Integer tunjanganbbm) {
		this.tunjanganbbm = tunjanganbbm;
	}
	
}