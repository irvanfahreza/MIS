package id.go.bpjskesehatan.entitas.organisasi;

import id.go.bpjskesehatan.entitas.referensi.Dati2;
import id.go.bpjskesehatan.entitas.referensi.Propinsi;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * The persistent class for the job_office database table.
 * 
 */
@JsonInclude(Include.NON_NULL)
public class Office implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "KODE")
	private String kode;
	@Column(name = "NAMA")
	private String nama;
	@Column(name = "ALAMAT")
	private String alamat;
	@Column(name = "KODEPOS")
	private String kodepos;
	@Column(name = "TELP")
	private String telp;
	@Column(name = "FAX")
	private String fax;
	@Column(name = "ik")
	private Integer ik;
	@Column(name = "CREATED_TIME")
	private Timestamp created_time;
	@Column(name = "CREATED_BY")
	private Integer created_by;
	@Column(name = "LASTMODIFIED_TIME")
	private Timestamp lastmodified_time;
	@Column(name = "LASTMODIFIED_BY")
	private Integer lastmodified_by;
	@Column(name = "ROW_STATUS")
	private Short row_status;
	@JsonProperty("dati2")
	@JoinColumn(name = "KODEDATI2", referencedColumnName = "KODE")
	@ManyToOne
	private Dati2 dati2;
	@JsonProperty("officetype")
	@JoinColumn(name = "KODEOFFICETYPE", referencedColumnName = "KODE")
	@ManyToOne
	private OfficeType officetype;

	@JsonProperty("propinsi")
	@JoinColumn(name = "KODEpropinsi", referencedColumnName = "KODE")
	@ManyToOne
	private Propinsi propinsi;

	@JsonProperty("parent")
	@JoinColumn(name = "kodeparent", referencedColumnName = "kode")
	@ManyToOne
	private Office parent;

	@Column(name = "tunjdacil")
	private BigDecimal tunjdacil;
	
	@Column(name = "tunjkhusus")
	private BigDecimal tunjkhusus;
	
	@Column(name = "latitude")
	private Float latitude;
	
	@Column(name = "longitude")
	private Float longitude;
	
	@Column(name = "nosurateksternal")
	private String nosurateksternal;

	public Office() {
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

	public String getAlamat() {
		return alamat;
	}

	public void setAlamat(String alamat) {
		this.alamat = alamat;
	}

	public String getKodepos() {
		return kodepos;
	}

	public void setKodepos(String kodepos) {
		this.kodepos = kodepos;
	}

	public String getTelp() {
		return telp;
	}

	public void setTelp(String telp) {
		this.telp = telp;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public Timestamp getCreated_time() {
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

	public Timestamp getLastmodified_time() {
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

	public Dati2 getDati2() {
		return dati2;
	}

	public Short getRow_status() {
		return row_status;
	}

	public void setRow_status(Short row_status) {
		this.row_status = row_status;
	}

	public void setDati2(Dati2 dati2) {
		this.dati2 = dati2;
	}

	public OfficeType getOfficetype() {
		return officetype;
	}

	public void setOfficetype(OfficeType officetype) {
		this.officetype = officetype;
	}

	public Office getParent() {
		return parent;
	}

	public void setParent(Office parent) {
		this.parent = parent;
	}

	public Propinsi getPropinsi() {
		return propinsi;
	}

	public void setPropinsi(Propinsi propinsi) {
		this.propinsi = propinsi;
	}

	public Integer getIk() {
		return ik;
	}

	public void setIk(Integer ik) {
		this.ik = ik;
	}

	public BigDecimal getTunjdacil() {
		return tunjdacil;
	}

	public void setTunjdacil(BigDecimal tunjdacil) {
		this.tunjdacil = tunjdacil;
	}

	public BigDecimal getTunjkhusus() {
		return tunjkhusus;
	}

	public void setTunjkhusus(BigDecimal tunjkhusus) {
		this.tunjkhusus = tunjkhusus;
	}

	public String getNosurateksternal() {
		return nosurateksternal;
	}

	public void setNosurateksternal(String nosurateksternal) {
		this.nosurateksternal = nosurateksternal;
	}

	public Float getLatitude() {
		return latitude;
	}

	public void setLatitude(Float latitude) {
		this.latitude = latitude;
	}

	public Float getLongitude() {
		return longitude;
	}

	public void setLongitude(Float longitude) {
		this.longitude = longitude;
	}

}