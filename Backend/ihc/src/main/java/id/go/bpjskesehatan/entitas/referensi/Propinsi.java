package id.go.bpjskesehatan.entitas.referensi;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
@NamedQueries({ @NamedQuery(name = "Propinsi.findAll", query = "SELECT p FROM Propinsi p") })
public class Propinsi implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "KODE")
	private String kode;
	@Basic(optional = false)
	@Column(name = "NAMA")
	private String nama;
	@Column(name = "CREATED_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constant.ServerTimezone)
	private Date createdTime;
	@Basic(optional = false)
	@Column(name = "CREATED_BY")
	private Integer created_by;
	@Column(name = "LASTMODIFIED_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constant.ServerTimezone)
	private Date lastmodifiedTime;
	@Column(name = "LASTMODIFIED_BY")
	private Integer lastmodified_by;
	@Basic(optional = false)
	@Column(name = "ROW_STATUS")
	private Short row_status;
	@OneToMany(mappedBy = "propinsi")
	private List<Dati2> dati2List;

	public Propinsi() {
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

	public Integer getCreated_by() {
		return created_by;
	}

	public void setCreated_by(Integer created_by) {
		this.created_by = created_by;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getLastmodifiedTime() {
		return lastmodifiedTime;
	}

	public void setLastmodifiedTime(Date lastmodifiedTime) {
		this.lastmodifiedTime = lastmodifiedTime;
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

	public List<Dati2> getDati2List() {
		return dati2List;
	}

	public void setDati2List(List<Dati2> dati2List) {
		this.dati2List = dati2List;
	}

}