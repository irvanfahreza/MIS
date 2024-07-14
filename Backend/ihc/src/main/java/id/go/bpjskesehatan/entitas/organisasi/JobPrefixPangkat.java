package id.go.bpjskesehatan.entitas.organisasi;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Id;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


/**
 * The persistent class for the job_level_pangkat database table.
 * 
 */
@JsonInclude(Include.NON_NULL)
public class JobPrefixPangkat implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id")
	private int id;
	
	@Column(name="created_by")
	private Integer created_by;

	@Column(name="created_time")
	private Timestamp created_time;	

	@JsonProperty("grade")
	@Column(name="kodegrade")
	private Grade grade;

	@JsonProperty("jobprefix")
	@Column(name="kodejobprefix")
	private JobPrefix jobprefix;

	@Column(name="lastmodified_by")
	private Integer lastmodified_by;

	@Column(name="lastmodified_time")
	private Timestamp lastmodified_time;

	private short level;

	@Column(name="row_status")
	private Short row_status;
	

	public JobPrefixPangkat() {
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Grade getGrade() {
		return grade;
	}

	public void setGrade(Grade grade) {
		this.grade = grade;
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

	public short getLevel() {
		return this.level;
	}

	public void setLevel(short level) {
		this.level = level;
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

}