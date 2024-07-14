package id.go.bpjskesehatan.entitas.karyawan;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import id.go.bpjskesehatan.entitas.GenericEntitas;

import java.sql.Timestamp;

/**
 * The persistent class for the INFOFISIK database table.
 * 
 */
@Entity
@JsonInclude(Include.NON_NULL)
@NamedQuery(name = "Infofisik.findAll", query = "SELECT i FROM Infofisik i")
public class Infofisik implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String npp;

	private String beratbadan;

	@Column(name = "CREATED_BY")
	private Integer created_by;

	@Column(name = "CREATED_TIME")
	private Timestamp created_time;

	@JsonProperty("golongandarah")
	@Column(name = "kodegolongandarah")
	private GenericEntitas golongandarah;

	@Column(name = "LASTMODIFIED_BY")
	private Integer lastmodified_by;

	@Column(name = "LASTMODIFIED_TIME")
	private Timestamp lastmodified_time;

	@Column(name = "ROW_STATUS")
	private Short row_status;

	private String tinggibadan;

	@JsonProperty("ukuranbaju")
	@Column(name = "kodeukuranbaju")
	private GenericEntitas ukuranbaju;

	@JsonProperty("ukurancelana")
	@Column(name = "kodeukurancelana")
	private GenericEntitas ukurancelana;

	@JsonProperty("ukurankepala")
	@Column(name = "kodeukurankepala")
	private GenericEntitas ukurankepala;

	@JsonProperty("ukuransepatu")
	@Column(name = "kodeukuransepatu")
	private GenericEntitas ukuransepatu;

	@JsonProperty("warnakulit")
	@Column(name = "kodewarnakulit")
	private GenericEntitas warnakulit;

	public Infofisik() {
	}

	public String getNpp() {
		return npp;
	}

	public void setNpp(String npp) {
		this.npp = npp;
	}

	public String getBeratbadan() {
		return beratbadan;
	}

	public void setBeratbadan(String beratbadan) {
		this.beratbadan = beratbadan;
	}

	public Integer getCreated_by() {
		return created_by;
	}

	public void setCreated_by(Integer created_by) {
		this.created_by = created_by;
	}

	public Timestamp getCreated_time() {
		return created_time;
	}

	public void setCreated_time(Timestamp created_time) {
		this.created_time = created_time;
	}

	public GenericEntitas getGolongandarah() {
		return golongandarah;
	}

	public void setGolongandarah(GenericEntitas golongandarah) {
		this.golongandarah = golongandarah;
	}

	public Integer getLastmodified_by() {
		return lastmodified_by;
	}

	public void setLastmodified_by(Integer lastmodified_by) {
		this.lastmodified_by = lastmodified_by;
	}

	public Timestamp getLastmodified_time() {
		return lastmodified_time;
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

	public String getTinggibadan() {
		return tinggibadan;
	}

	public void setTinggibadan(String tinggibadan) {
		this.tinggibadan = tinggibadan;
	}

	public GenericEntitas getUkuranbaju() {
		return ukuranbaju;
	}

	public void setUkuranbaju(GenericEntitas ukuranbaju) {
		this.ukuranbaju = ukuranbaju;
	}

	public GenericEntitas getUkurancelana() {
		return ukurancelana;
	}

	public void setUkurancelana(GenericEntitas ukurancelana) {
		this.ukurancelana = ukurancelana;
	}

	public GenericEntitas getUkurankepala() {
		return ukurankepala;
	}

	public void setUkurankepala(GenericEntitas ukurankepala) {
		this.ukurankepala = ukurankepala;
	}

	public GenericEntitas getUkuransepatu() {
		return ukuransepatu;
	}

	public void setUkuransepatu(GenericEntitas ukuransepatu) {
		this.ukuransepatu = ukuransepatu;
	}

	public GenericEntitas getWarnakulit() {
		return warnakulit;
	}

	public void setWarnakulit(GenericEntitas warnakulit) {
		this.warnakulit = warnakulit;
	}
	
	
	
}