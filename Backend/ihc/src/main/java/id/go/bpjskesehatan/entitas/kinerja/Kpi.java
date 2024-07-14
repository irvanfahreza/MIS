package id.go.bpjskesehatan.entitas.kinerja;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import id.go.bpjskesehatan.entitas.karyawan.Detailkpi;

@JsonInclude(Include.NON_NULL)
public class Kpi implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	private Integer kode;
	private Integer kodekinerjatannggungjawab;
	private Integer kodekaryawandetailkpi;
	private String target;
	private String unitpengukuran;
	private String verifikasisumberdata;
	private String asumsi;
	private Double bobot;
	private Short row_status;
	private Integer created_by;
	private Timestamp created_time;
	private Integer lastmodified_by;
	private Timestamp lastmodified_time;
	
	private Tanggungjawab tanggungjawab;
	private Detailkpi detailkpi;
	public Integer getKode() {
		return kode;
	}
	public void setKode(Integer kode) {
		this.kode = kode;
	}
	public Integer getKodekinerjatannggungjawab() {
		return kodekinerjatannggungjawab;
	}
	public void setKodekinerjatannggungjawab(Integer kodekinerjatannggungjawab) {
		this.kodekinerjatannggungjawab = kodekinerjatannggungjawab;
	}
	public Integer getKodekaryawandetailkpi() {
		return kodekaryawandetailkpi;
	}
	public void setKodekaryawandetailkpi(Integer kodekaryawandetailkpi) {
		this.kodekaryawandetailkpi = kodekaryawandetailkpi;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getUnitpengukuran() {
		return unitpengukuran;
	}
	public void setUnitpengukuran(String unitpengukuran) {
		this.unitpengukuran = unitpengukuran;
	}
	public String getVerifikasisumberdata() {
		return verifikasisumberdata;
	}
	public void setVerifikasisumberdata(String verifikasisumberdata) {
		this.verifikasisumberdata = verifikasisumberdata;
	}
	public String getAsumsi() {
		return asumsi;
	}
	public void setAsumsi(String asumsi) {
		this.asumsi = asumsi;
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
	public Tanggungjawab getTanggungjawab() {
		return tanggungjawab;
	}
	public void setTanggungjawab(Tanggungjawab tanggungjawab) {
		this.tanggungjawab = tanggungjawab;
	}
	public Detailkpi getDetailkpi() {
		return detailkpi;
	}
	public void setDetailkpi(Detailkpi detailkpi) {
		this.detailkpi = detailkpi;
	}
	
}
