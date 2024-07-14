package id.go.bpjskesehatan.entitas.referensi;

import java.sql.Timestamp;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import id.go.bpjskesehatan.entitas.GenericEntitas;

@JsonInclude(Include.NON_NULL)
public class Anggaran {
	private Integer kode;
	@Column(name = "kodeprogramkegiatan")
	private GenericEntitas programkegiatan;
	@Column(name = "kodeakun")
	private GenericEntitas akun;
	private Short row_status;
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

	public GenericEntitas getProgramkegiatan() {
		return programkegiatan;
	}

	public void setProgramkegiatan(GenericEntitas programkegiatan) {
		this.programkegiatan = programkegiatan;
	}

	public GenericEntitas getAkun() {
		return akun;
	}

	public void setAkun(GenericEntitas akun) {
		this.akun = akun;
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

}
