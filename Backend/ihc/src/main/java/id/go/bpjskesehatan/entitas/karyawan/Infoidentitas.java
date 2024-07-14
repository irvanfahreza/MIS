package id.go.bpjskesehatan.entitas.karyawan;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import id.go.bpjskesehatan.Constant;
import id.go.bpjskesehatan.entitas.GenericEntitas;
import id.go.bpjskesehatan.entitas.referensi.Dati2;

import java.util.Date;
import java.sql.Timestamp;

/**
 * The persistent class for the IDENTITAS database table.
 * 
 */
@Entity
@JsonInclude(Include.NON_NULL)
@NamedQuery(name = "Infoidentitas.findAll", query = "SELECT i FROM Identita i")
public class Infoidentitas implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int kode;

	@Column(name = "CREATED_BY")
	private Integer created_by;

	@Column(name = "CREATED_TIME")
	private Timestamp created_time;

	@JsonProperty("dati2")
	@Column(name = "kodedati2")
	private Dati2 dati2;

	@JsonProperty("jenisidentitas")
	@Column(name = "kodejenisid")
	private GenericEntitas jenisidentitas;

	private String lampiran;

	@Column(name = "LASTMODIFIED_BY")
	private Integer lastmodified_by;

	@Column(name = "LASTMODIFIED_TIME")
	private Timestamp lastmodified_time;

	private String namapenerbit;

	private String noidentitas;

	private String npp;

	@Column(name = "ROW_STATUS")
	private Short row_status;

	@Temporal(TemporalType.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constant.ServerTimezone)
	private Date tab;

	@Temporal(TemporalType.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constant.ServerTimezone)
	private Date tmb;

	public Infoidentitas() {
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

	public Dati2 getDati2() {
		return dati2;
	}

	public void setDati2(Dati2 dati2) {
		this.dati2 = dati2;
	}

	public GenericEntitas getJenisidentitas() {
		return jenisidentitas;
	}

	public void setJenisidentitas(GenericEntitas jenisidentitas) {
		this.jenisidentitas = jenisidentitas;
	}

	public String getLampiran() {
		return lampiran;
	}

	public void setLampiran(String lampiran) {
		this.lampiran = lampiran;
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

	public String getNamapenerbit() {
		return this.namapenerbit;
	}

	public void setNamapenerbit(String namapenerbit) {
		this.namapenerbit = namapenerbit;
	}

	public String getNoidentitas() {
		return this.noidentitas;
	}

	public void setNoidentitas(String noidentitas) {
		this.noidentitas = noidentitas;
	}

	public String getNpp() {
		return this.npp;
	}

	public void setNpp(String npp) {
		this.npp = npp;
	}

	public Short getRow_status() {
		return this.row_status;
	}

	public void setRow_status(Short row_status) {
		this.row_status = row_status;
	}

	public Date getTab() {
		return this.tab;
	}

	public void setTab(Date tab) {
		this.tab = tab;
	}

	public Date getTmb() {
		return this.tmb;
	}

	public void setTmb(Date tmb) {
		this.tmb = tmb;
	}

}