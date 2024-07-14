package id.go.bpjskesehatan.entitas.karyawan;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import id.go.bpjskesehatan.Constant;
import id.go.bpjskesehatan.entitas.GenericEntitas;

import java.util.Date;
import java.sql.Timestamp;
import java.util.List;

/**
 * The persistent class for the INFOPELANGGARAN database table.
 * 
 */
@JsonInclude(Include.NON_NULL)
@Entity
@NamedQuery(name = "Infopelanggaran.findAll", query = "SELECT i FROM Infopelanggaran i")
public class Infopelanggaran implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Integer kode;

	private String npp;

	@Column(name = "CREATED_BY")
	private Integer created_by;

	@Column(name = "CREATED_TIME")
	private Timestamp created_time;

	private String deskripsi;

	@JsonProperty("sanksipelanggaran")
	@Column(name = "kodesanksipelanggaran")
	private GenericEntitas sanksipelanggaran;

	@Lob
	private byte[] lampiran;

	@Column(name = "LASTMODIFIED_BY")
	private Integer lastmodified_by;

	@Column(name = "LASTMODIFIED_TIME")
	private Timestamp lastmodified_time;

	private String nomor;

	@Column(name = "ROW_STATUS")
	private Short row_status;

	@Temporal(TemporalType.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constant.ServerTimezone)
	private Date tanggal;

	@Temporal(TemporalType.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constant.ServerTimezone)
	private Date tglberlaku;

	// bi-directional many-to-one association to Detailinfopelanggaran
	@OneToMany(mappedBy = "infopelanggaran")
	private List<Detailinfopelanggaran> detailinfopelanggarans;

	public Infopelanggaran() {
	}

	public Integer getKode() {
		return kode;
	}

	public void setKode(Integer kode) {
		this.kode = kode;
	}

	public String getNpp() {
		return this.npp;
	}

	public void setNpp(String npp) {
		this.npp = npp;
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

	public String getDeskripsi() {
		return this.deskripsi;
	}

	public void setDeskripsi(String deskripsi) {
		this.deskripsi = deskripsi;
	}

	public GenericEntitas getSanksipelanggaran() {
		return sanksipelanggaran;
	}

	public void setSanksipelanggaran(GenericEntitas sanksipelanggaran) {
		this.sanksipelanggaran = sanksipelanggaran;
	}

	public byte[] getLampiran() {
		return this.lampiran;
	}

	public void setLampiran(byte[] lampiran) {
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

	public String getNomor() {
		return this.nomor;
	}

	public void setNomor(String nomor) {
		this.nomor = nomor;
	}

	public Short getRow_status() {
		return this.row_status;
	}

	public void setRow_status(Short row_status) {
		this.row_status = row_status;
	}

	public Date getTanggal() {
		return this.tanggal;
	}

	public void setTanggal(Date tanggal) {
		this.tanggal = tanggal;
	}

	public Date getTglberlaku() {
		return this.tglberlaku;
	}

	public void setTglberlaku(Date tglberlaku) {
		this.tglberlaku = tglberlaku;
	}

	public List<Detailinfopelanggaran> getDetailinfopelanggarans() {
		return this.detailinfopelanggarans;
	}

	public void setDetailinfopelanggarans(List<Detailinfopelanggaran> detailinfopelanggarans) {
		this.detailinfopelanggarans = detailinfopelanggarans;
	}

	public Detailinfopelanggaran addDetailinfopelanggaran(Detailinfopelanggaran detailinfopelanggaran) {
		getDetailinfopelanggarans().add(detailinfopelanggaran);
		detailinfopelanggaran.setInfopelanggaran(this);

		return detailinfopelanggaran;
	}

	public Detailinfopelanggaran removeDetailinfopelanggaran(Detailinfopelanggaran detailinfopelanggaran) {
		getDetailinfopelanggarans().remove(detailinfopelanggaran);
		detailinfopelanggaran.setInfopelanggaran(null);

		return detailinfopelanggaran;
	}

}