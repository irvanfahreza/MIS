package id.go.bpjskesehatan.entitas.hcis;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.sql.Timestamp;

/**
 * The persistent class for the "ACTION" database table.
 * 
 */
@Entity
@JsonInclude(Include.NON_NULL)
@NamedQuery(name = "Action.findAll", query = "SELECT a FROM Action a")
public class Action implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int kode;

	@Column(name = "CREATED_BY")
	private int createdBy;

	@Column(name = "CREATED_TIME")
	private Timestamp createdTime;

	@Column(name = "LASTMODIFIED_BY")
	private int lastmodifiedBy;

	@Column(name = "LASTMODIFIED_TIME")
	private Timestamp lastmodifiedTime;

	private String nama;

	@Column(name = "ROW_STATUS")
	private short rowStatus;

	public Action() {
	}

	public int getKode() {
		return this.kode;
	}

	public void setKode(int kode) {
		this.kode = kode;
	}

	public int getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedTime() {
		return this.createdTime;
	}

	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}

	public int getLastmodifiedBy() {
		return this.lastmodifiedBy;
	}

	public void setLastmodifiedBy(int lastmodifiedBy) {
		this.lastmodifiedBy = lastmodifiedBy;
	}

	public Timestamp getLastmodifiedTime() {
		return this.lastmodifiedTime;
	}

	public void setLastmodifiedTime(Timestamp lastmodifiedTime) {
		this.lastmodifiedTime = lastmodifiedTime;
	}

	public String getNama() {
		return this.nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public short getRowStatus() {
		return this.rowStatus;
	}

	public void setRowStatus(short rowStatus) {
		this.rowStatus = rowStatus;
	}

}