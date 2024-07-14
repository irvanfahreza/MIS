package id.go.bpjskesehatan.entitas.kinerja;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Id;

public class Tugastambahan implements Serializable  {
	private static final long serialVersionUID = 1L;
	
	@Id
	private Integer kode;
	private Integer kodepeserta;
	private Integer kodebobotkomponen;
	private Double bobot;
	private Short row_status;
	private Integer created_by;
	private Timestamp created_time;
	private Integer lastmodified_by;
	private Timestamp lastmodified_time;
	
	private Peserta peserta;
	private Bobotkomponen bobotkomponen;
	public Integer getKode() {
		return kode;
	}
	public void setKode(Integer kode) {
		this.kode = kode;
	}
	public Integer getKodepeserta() {
		return kodepeserta;
	}
	public void setKodepeserta(Integer kodepeserta) {
		this.kodepeserta = kodepeserta;
	}
	public Integer getKodebobotkomponen() {
		return kodebobotkomponen;
	}
	public void setKodebobotkomponen(Integer kodebobotkomponen) {
		this.kodebobotkomponen = kodebobotkomponen;
	}
	public Double getBobot() {
		return bobot;
	}
	public void setBobot(Double bobot) {
		this.bobot = bobot;
	}
	public Short getRow_status() {
		return row_status;
	}
	public void setRow_status(Short row_status) {
		this.row_status = row_status;
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
	public Peserta getPeserta() {
		return peserta;
	}
	public void setPeserta(Peserta peserta) {
		this.peserta = peserta;
	}
	public Bobotkomponen getBobotkomponen() {
		return bobotkomponen;
	}
	public void setBobotkomponen(Bobotkomponen bobotkomponen) {
		this.bobotkomponen = bobotkomponen;
	}
	
	

}
