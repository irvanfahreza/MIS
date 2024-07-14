package id.go.bpjskesehatan.entitas.organisasi;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 *
 * @author Bambang Purwanto
 */
@JsonInclude(Include.NON_NULL)
@Entity
public class UnitKerja implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	private String kode;
	private String nama;
	private String isneck;
	@Basic(optional = false)
	@Column(name = "created_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Timestamp created_time;
	@Basic(optional = false)
	@Column(name = "created_by")
	private Integer created_by;
	@Column(name = "lastmodified_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Timestamp lastmodified_time;
	@Column(name = "lastmodified_by")
	private Integer lastmodified_by;
	@Basic(optional = false)
	@Column(name = "row_status")
	private Short row_status;
	@JoinColumn(name = "kodehirarkiunitkerja", referencedColumnName = "kode")
	@ManyToOne
	@JsonProperty("hirarkiunitkerja")
	private HirarkiUnitKerja hirarkiunitkerja;
	@JoinColumn(name = "kodeparent", referencedColumnName = "kode")
	@ManyToOne
	@JsonProperty("parent")
	private UnitKerja parent;

	@JoinColumn(name = "kodeoffice", referencedColumnName = "kode")
	@ManyToOne
	@JsonProperty("office")
	private Office office;
	
	private OrganizationChart organizationchart;
	
	@Column(name = "nosuratinternal")
	private String nosuratinternal;
	@Column(name = "nosurateksternal")
	private String nosurateksternal;

	public UnitKerja() {
	}

	public OrganizationChart getOrganizationchart() {
		return organizationchart;
	}

	public void setOrganizationchart(OrganizationChart organizationchart) {
		this.organizationchart = organizationchart;
	}

	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
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

	public String getIsneck() {
		return isneck;
	}

	public void setIsneck(String isneck) {
		this.isneck = isneck;
	}

	public HirarkiUnitKerja getHirarkiunitkerja() {
		return hirarkiunitkerja;
	}

	public void setHirarkiunitkerja(HirarkiUnitKerja hirarkiunitkerja) {
		this.hirarkiunitkerja = hirarkiunitkerja;
	}

	public UnitKerja getParent() {
		return parent;
	}

	public void setParent(UnitKerja parent) {
		this.parent = parent;
	}

	public Date getCreatedTime() {
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

	public String getNosuratinternal() {
		return nosuratinternal;
	}

	public void setNosuratinternal(String nosuratinternal) {
		this.nosuratinternal = nosuratinternal;
	}

	public String getNosurateksternal() {
		return nosurateksternal;
	}

	public void setNosurateksternal(String nosurateksternal) {
		this.nosurateksternal = nosurateksternal;
	}

}
