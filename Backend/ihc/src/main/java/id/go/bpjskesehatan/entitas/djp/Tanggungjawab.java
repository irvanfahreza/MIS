package id.go.bpjskesehatan.entitas.djp;

import java.io.Serializable;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.sql.Timestamp;
import java.util.List;

/**
 * The persistent class for the TANGGUNGJAWAB database table.
 * 
 */
@JsonInclude(Include.NON_NULL)
@JsonRootName("tanggungjawab")
@Entity
@NamedQuery(name = "Tanggungjawab.findAll", query = "SELECT t FROM Tanggungjawab t")
public class Tanggungjawab implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int kode;

	private String tanggungjawabutama;

	private String wewenangutama;

	// bi-directional many-to-one association to Djp
	@JsonProperty("djp")
	@Column(name = "kodedjp")
	private Djp djp;

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

	// bi-directional many-to-one association to Detaildimensijabatan
	@OneToMany(mappedBy = "tanggungjawab")
	private List<Detaildimensijabatan> detaildimensijabatans;

	// bi-directional many-to-one association to Detailkpi
	@OneToMany(mappedBy = "tanggungjawab")
	private List<Detailkpi> detailkpis;

	public Tanggungjawab() {
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
		return row_status;
	}

	public void setRow_status(Short row_status) {
		this.row_status = row_status;
	}

	public String getTanggungjawabutama() {
		return this.tanggungjawabutama;
	}

	public void setTanggungjawabutama(String tanggungjawabutama) {
		this.tanggungjawabutama = tanggungjawabutama;
	}

	public String getWewenangutama() {
		return this.wewenangutama;
	}

	public void setWewenangutama(String wewenangutama) {
		this.wewenangutama = wewenangutama;
	}

	public Djp getDjp() {
		return this.djp;
	}

	public void setDjp(Djp djp) {
		this.djp = djp;
	}

	public List<Detaildimensijabatan> getDetaildimensijabatans() {
		return this.detaildimensijabatans;
	}

	public void setDetaildimensijabatans(List<Detaildimensijabatan> detaildimensijabatans) {
		this.detaildimensijabatans = detaildimensijabatans;
	}

	public Detaildimensijabatan addDetaildimensijabatan(Detaildimensijabatan detaildimensijabatan) {
		getDetaildimensijabatans().add(detaildimensijabatan);
		detaildimensijabatan.setTanggungjawab(this);

		return detaildimensijabatan;
	}

	public Detaildimensijabatan removeDetaildimensijabatan(Detaildimensijabatan detaildimensijabatan) {
		getDetaildimensijabatans().remove(detaildimensijabatan);
		detaildimensijabatan.setTanggungjawab(null);

		return detaildimensijabatan;
	}

	public List<Detailkpi> getDetailkpis() {
		return this.detailkpis;
	}

	public void setDetailkpis(List<Detailkpi> detailkpis) {
		this.detailkpis = detailkpis;
	}

	public Detailkpi addDetailkpi(Detailkpi detailkpi) {
		getDetailkpis().add(detailkpi);
		detailkpi.setTanggungjawab(this);

		return detailkpi;
	}

	public Detailkpi removeDetailkpi(Detailkpi detailkpi) {
		getDetailkpis().remove(detailkpi);
		detailkpi.setTanggungjawab(null);

		return detailkpi;
	}

}