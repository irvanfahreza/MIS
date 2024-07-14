package id.go.bpjskesehatan.entitas.kompetensi;

import id.go.bpjskesehatan.entitas.organisasi.JobTitle;

import java.io.Serializable;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.sql.Timestamp;
import java.util.List;

/**
 * The persistent class for the MODELKOMPETENSIJOBTITLE database table.
 * 
 */
@JsonInclude(Include.NON_NULL)
@Entity
@NamedQuery(name = "Modelkompetensijobtitle.findAll", query = "SELECT m FROM Modelkompetensijobtitle m")
public class Modelkompetensijobtitle implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int kode;

	@Column(name = "CREATED_BY")
	private Integer created_by;

	@Column(name = "CREATED_TIME")
	private Timestamp created_time;

	@JsonProperty("jobtitle")
	@ManyToOne
	@JoinColumn(name = "kodejobtitle")
	private JobTitle jobtitle;

	@Column(name = "LASTMODIFIED_BY")
	private Integer lastmodified_by;

	@Column(name = "LASTMODIFIED_TIME")
	private Timestamp lastmodified_time;

	@Column(name = "ROW_STATUS")
	private Short row_status;

	@OneToMany(mappedBy = "modelkompetensijobtitle")
	private List<Detailmodelkompetensijobtitle> detailmodelkompetensijobtitles;

	public Modelkompetensijobtitle() {
	}

	public int getKode() {
		return this.kode;
	}

	public void setKode(int kode) {
		this.kode = kode;
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

	public JobTitle getJobtitle() {
		return jobtitle;
	}

	public void setJobtitle(JobTitle jobtitle) {
		this.jobtitle = jobtitle;
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

	public Short getRow_status() {
		return this.row_status;
	}

	public void setRow_status(Short row_status) {
		this.row_status = row_status;
	}

	public List<Detailmodelkompetensijobtitle> getDetailmodelkompetensijobtitles() {
		return this.detailmodelkompetensijobtitles;
	}

	public void setDetailmodelkompetensijobtitles(List<Detailmodelkompetensijobtitle> detailmodelkompetensijobtitles) {
		this.detailmodelkompetensijobtitles = detailmodelkompetensijobtitles;
	}

	public Detailmodelkompetensijobtitle addDetailmodelkompetensijobtitle(
			Detailmodelkompetensijobtitle detailmodelkompetensijobtitle) {
		getDetailmodelkompetensijobtitles().add(detailmodelkompetensijobtitle);
		detailmodelkompetensijobtitle.setModelkompetensijobtitle(this);

		return detailmodelkompetensijobtitle;
	}

	public Detailmodelkompetensijobtitle removeDetailmodelkompetensijobtitle(
			Detailmodelkompetensijobtitle detailmodelkompetensijobtitle) {
		getDetailmodelkompetensijobtitles().remove(detailmodelkompetensijobtitle);
		detailmodelkompetensijobtitle.setModelkompetensijobtitle(null);

		return detailmodelkompetensijobtitle;
	}

}