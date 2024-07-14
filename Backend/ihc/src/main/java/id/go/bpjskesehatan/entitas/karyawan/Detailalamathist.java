package id.go.bpjskesehatan.entitas.karyawan;

import java.io.Serializable;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.sql.Timestamp;

/**
 * The persistent class for the DETAILALAMATHIST database table.
 * 
 */
@Entity
@JsonInclude(Include.NON_NULL)
@JsonRootName("detailalamathist")
@NamedQuery(name = "Detailalamathist.findAll", query = "SELECT d FROM Detailalamathist d")
public class Detailalamathist implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int kode;

	private String alamat;

	private String kodedati2;

	private String kodekecamatan;

	private String kodekelurahan;

	private int kodenegara;

	private String kodepos;

	private String kodepropinsi;

	private String rt;

	private String rw;

	// bi-directional many-to-one association to Pegawai
	@ManyToOne
	@JoinColumn(name = "NPP")
	private Pegawai pegawai;

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

	public Detailalamathist() {
	}

	public int getKode() {
		return this.kode;
	}

	public void setKode(int kode) {
		this.kode = kode;
	}

	public String getAlamat() {
		return this.alamat;
	}

	public void setAlamat(String alamat) {
		this.alamat = alamat;
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

	public String getKodedati2() {
		return this.kodedati2;
	}

	public void setKodedati2(String kodedati2) {
		this.kodedati2 = kodedati2;
	}

	public String getKodekecamatan() {
		return this.kodekecamatan;
	}

	public void setKodekecamatan(String kodekecamatan) {
		this.kodekecamatan = kodekecamatan;
	}

	public String getKodekelurahan() {
		return this.kodekelurahan;
	}

	public void setKodekelurahan(String kodekelurahan) {
		this.kodekelurahan = kodekelurahan;
	}

	public int getKodenegara() {
		return this.kodenegara;
	}

	public void setKodenegara(int kodenegara) {
		this.kodenegara = kodenegara;
	}

	public String getKodepos() {
		return this.kodepos;
	}

	public void setKodepos(String kodepos) {
		this.kodepos = kodepos;
	}

	public String getKodepropinsi() {
		return this.kodepropinsi;
	}

	public void setKodepropinsi(String kodepropinsi) {
		this.kodepropinsi = kodepropinsi;
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

	public String getRt() {
		return this.rt;
	}

	public void setRt(String rt) {
		this.rt = rt;
	}

	public String getRw() {
		return this.rw;
	}

	public void setRw(String rw) {
		this.rw = rw;
	}

	public Pegawai getPegawai() {
		return this.pegawai;
	}

	public void setPegawai(Pegawai pegawai) {
		this.pegawai = pegawai;
	}

}