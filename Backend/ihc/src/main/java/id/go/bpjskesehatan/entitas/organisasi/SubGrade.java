package id.go.bpjskesehatan.entitas.organisasi;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Id;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * The persistent class for the job_grade database table.
 * 
 */
@JsonInclude(Include.NON_NULL)
public class SubGrade implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "kode")
	private String kode;

	@Column(name = "created_by")
	private Integer created_by;

	@Column(name = "created_time")
	private Timestamp created_time;

	@JsonProperty("grade")
	@Column(name = "kodegrade")
	private Grade grade;

	@Column(name = "nama")
	private String nama;

	@Column(name = "lastmodified_by")
	private Integer lastmodified_by;

	@Column(name = "lastmodified_time")
	private Timestamp lastmodified_time;

	//private BigDecimal maxValue;

	//private BigDecimal meetValue;

	//private BigDecimal minValue;
	
	@Column(name = "gajipokok")
	private BigDecimal gajipokok;
	
	@Column(name = "tupres")
	private BigDecimal tupres;
	
	@Column(name = "utilitas")
	private BigDecimal utilitas;
	
	@Column(name = "komunikasi")
	private BigDecimal komunikasi;
	
	@Column(name = "transportasi")
	private BigDecimal transportasi;
	
	@Column(name = "perumahan")
	private BigDecimal perumahan;
	
	@Column(name = "literbbm")
	private Integer literbbm;

	@Column(name = "row_status")
	private Short row_status;

	public SubGrade() {
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

	public Grade getGrade() {
		return grade;
	}

	public void setGrade(Grade grade) {
		this.grade = grade;
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

	public BigDecimal getGajipokok() {
		return gajipokok;
	}

	public void setGajipokok(BigDecimal gajipokok) {
		this.gajipokok = gajipokok;
	}

	public BigDecimal getTupres() {
		return tupres;
	}

	public void setTupres(BigDecimal tupres) {
		this.tupres = tupres;
	}

	public BigDecimal getUtilitas() {
		return utilitas;
	}

	public void setUtilitas(BigDecimal utilitas) {
		this.utilitas = utilitas;
	}

	public BigDecimal getKomunikasi() {
		return komunikasi;
	}

	public void setKomunikasi(BigDecimal komunikasi) {
		this.komunikasi = komunikasi;
	}

	public BigDecimal getTransportasi() {
		return transportasi;
	}

	public void setTransportasi(BigDecimal transportasi) {
		this.transportasi = transportasi;
	}

	public BigDecimal getPerumahan() {
		return perumahan;
	}

	public void setPerumahan(BigDecimal perumahan) {
		this.perumahan = perumahan;
	}

	public Integer getLiterbbm() {
		return literbbm;
	}

	public void setLiterbbm(Integer literbbm) {
		this.literbbm = literbbm;
	}
	
}