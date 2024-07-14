package id.go.bpjskesehatan.entitas.referensi;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 *
 * @author Bambang Purwanto
 */
@Entity
@JsonInclude(Include.NON_NULL)
@NamedQueries({ @NamedQuery(name = "Dati2.findAll", query = "SELECT d FROM Dati2 d") })
public class Dati2 implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "KODE")
	private String kode;
	@Column(name = "NAMA")
	private String nama;
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
	@Column(name = "kodepropinsi")
	@JsonProperty("propinsi")
	private Propinsi propinsi;

	public Dati2() {
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

	public void setCreated_time(Timestamp created_time) {
		this.created_time = created_time;
	}

	public Integer getCreated_by() {
		return created_by;
	}

	public void setCreated_by(Integer created_by) {
		this.created_by = created_by;
	}

	public Date getLastmodified_time() {
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

	public Timestamp getCreated_time() {
		return created_time;
	}

	public Propinsi getPropinsi() {
		return propinsi;
	}

	public void setPropinsi(Propinsi propinsi) {
		this.propinsi = propinsi;
	}

}
