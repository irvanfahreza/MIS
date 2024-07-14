package id.go.bpjskesehatan.entitas.hcis;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.sql.Timestamp;

/**
 * The persistent class for the MENUACTION database table.
 * 
 */
@JsonInclude(Include.NON_NULL)
@Entity
@NamedQuery(name = "Menuaction.findAll", query = "SELECT m FROM Menuaction m")
public class Menuaction implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int kode;

	@JsonProperty("created_by")
	@Column(name = "CREATED_BY")
	private int createdBy;

	@JsonProperty("created_time")
	@Column(name = "CREATED_TIME")
	private Timestamp createdTime;

	private int kodeaction;

	private int kodemenu;

	@JsonProperty("lastmodified_by")
	@Column(name = "LASTMODIFIED_BY")
	private int lastmodifiedBy;

	@JsonProperty("lastmodified_time")
	@Column(name = "LASTMODIFIED_TIME")
	private Timestamp lastmodifiedTime;

	@JsonProperty("row_status")
	@Column(name = "ROW_STATUS")
	private short rowStatus;

	// bi-directional many-to-one association to Grupuser
	@ManyToOne
	@JoinColumn(name = "KODEGRUPUSER")
	private GrupUser grupuser;

	public Menuaction() {
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

	public int getKodeaction() {
		return this.kodeaction;
	}

	public void setKodeaction(int kodeaction) {
		this.kodeaction = kodeaction;
	}

	public int getKodemenu() {
		return this.kodemenu;
	}

	public void setKodemenu(int kodemenu) {
		this.kodemenu = kodemenu;
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

	public short getRowStatus() {
		return this.rowStatus;
	}

	public void setRowStatus(short rowStatus) {
		this.rowStatus = rowStatus;
	}

	public GrupUser getGrupuser() {
		return this.grupuser;
	}

	public void setGrupuser(GrupUser grupuser) {
		this.grupuser = grupuser;
	}

}