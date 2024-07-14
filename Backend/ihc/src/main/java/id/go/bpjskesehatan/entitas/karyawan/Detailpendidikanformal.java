package id.go.bpjskesehatan.entitas.karyawan;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import id.go.bpjskesehatan.entitas.GenericEntitas;

@JsonInclude(Include.NON_NULL)
public class Detailpendidikanformal implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	private int kode;

	@JsonProperty("jurusanpendidikan")
	@JoinColumn(name = "kodejurusanpendidikan")
	private GenericEntitas jurusanpendidikan;

	@JsonProperty("pendidikan")
	@JoinColumn(name = "kodependidikan")
	private GenericEntitas pendidikan;

	// bi-directional many-to-one association to Djp
	@JsonProperty("djp")
	@ManyToOne
	@JoinColumn(name = "KODEDJP")
	private Djpindividu djp;
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

	public int getKode() {
		return kode;
	}

	public void setKode(int kode) {
		this.kode = kode;
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

	public GenericEntitas getJurusanpendidikan() {
		return jurusanpendidikan;
	}

	public void setJurusanpendidikan(GenericEntitas jurusanpendidikan) {
		this.jurusanpendidikan = jurusanpendidikan;
	}

	public GenericEntitas getPendidikan() {
		return pendidikan;
	}

	public void setPendidikan(GenericEntitas pendidikan) {
		this.pendidikan = pendidikan;
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

	public Djpindividu getDjp() {
		return djp;
	}

	public void setDjp(Djpindividu djp) {
		this.djp = djp;
	}

}
