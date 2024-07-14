package id.go.bpjskesehatan.entitas.hcis;

import java.io.Serializable;
import java.sql.Timestamp;

public class Pesan implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer kode;
	private Integer dari;
	private Integer kepada;
	private String pesan;
	private Integer isdelivered;
	private Integer created_by;
	private Timestamp created_time;
	private Integer lastmodified_by;
	private Timestamp lastmodified_time;

	public Integer getKode() {
		return kode;
	}

	public void setKode(Integer kode) {
		this.kode = kode;
	}

	public Integer getDari() {
		return dari;
	}

	public void setDari(Integer dari) {
		this.dari = dari;
	}

	public Integer getKepada() {
		return kepada;
	}

	public void setKepada(Integer kepada) {
		this.kepada = kepada;
	}

	public String getPesan() {
		return pesan;
	}

	public void setPesan(String pesan) {
		this.pesan = pesan;
	}

	public Integer getIsdelivered() {
		return isdelivered;
	}

	public void setIsdelivered(Integer isdelivered) {
		this.isdelivered = isdelivered;
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

}
