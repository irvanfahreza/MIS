package id.go.bpjskesehatan.entitas.hcis;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.sql.Timestamp;

/**
 * The persistent class for the MENU database table.
 * 
 */
@JsonInclude(Include.NON_NULL)
@Entity
@NamedQuery(name = "Menu.findAll", query = "SELECT m FROM Menu m")
public class Menu implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String kode;

	@Column(name = "CREATE")
	private short create;

	@JsonProperty("created_by")
	@Column(name = "CREATED_BY")
	private int createdBy;

	@JsonProperty("created_time")
	@Column(name = "CREATED_TIME")
	private Timestamp createdTime;

	@Column(name = "DELETE")
	private short delete;

	@JsonProperty("lastmodified_by")
	@Column(name = "LASTMODIFIED_BY")
	private int lastmodifiedBy;

	@JsonProperty("lastmodified_time")
	@Column(name = "LASTMODIFIED_TIME")
	private Timestamp lastmodifiedTime;

	@Column(name = "LEVEL")
	private short level;

	private String nama;
	
	private String icon;

	private String kodeparent;

	@Column(name = "READ")
	private short read;

	@JsonProperty("row_status")
	@Column(name = "ROW_STATUS")
	private short rowStatus;

	@Column(name = "STATE")
	private String state;

	private short tipe;

	@Column(name = "UPDATE")
	private short update;

	public Menu() {
	}

	public String getIcon() {
		return icon;
	}


	public void setIcon(String icon) {
		this.icon = icon;
	}


	public String getKode() {
		return this.kode;
	}

	public void setKode(String kode) {
		this.kode = kode;
	}

	public short getCreate() {
		return this.create;
	}

	public void setCreate(short create) {
		this.create = create;
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

	public short getDelete() {
		return this.delete;
	}

	public void setDelete(short delete) {
		this.delete = delete;
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

	public short getLevel() {
		return this.level;
	}

	public void setLevel(short level) {
		this.level = level;
	}

	public String getNama() {
		return this.nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public String getKodeparent() {
		return kodeparent;
	}

	public void setKodeparent(String kodeparent) {
		this.kodeparent = kodeparent;
	}

	public short getRead() {
		return this.read;
	}

	public void setRead(short read) {
		this.read = read;
	}

	public short getRowStatus() {
		return this.rowStatus;
	}

	public void setRowStatus(short rowStatus) {
		this.rowStatus = rowStatus;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public short getTipe() {
		return this.tipe;
	}

	public void setTipe(short tipe) {
		this.tipe = tipe;
	}

	public short getUpdate() {
		return this.update;
	}

	public void setUpdate(short update) {
		this.update = update;
	}

}