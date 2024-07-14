package id.go.bpjskesehatan.entitas.karyawan;

import java.io.Serializable;
import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Djpindividuindex implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String npp;
	private String nama;
	private Integer kodedjp;
	private String kodejabatan;
	private String namajabatan;
	private String kodejobtitle;
	private String namajobtitle;
	private String kodemodelkompetensijobtitle;
	private String kodeunitkerja;
	private String namaunitkerja;
	private Timestamp lastmodified_time;
	private Timestamp created_time;
	private Short row_status;

	public String getNpp() {
		return npp;
	}

	public void setNpp(String npp) {
		this.npp = npp;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public Integer getKodedjp() {
		return kodedjp;
	}

	public void setKodedjp(Integer kodedjp) {
		this.kodedjp = kodedjp;
	}

	public String getKodejabatan() {
		return kodejabatan;
	}

	public void setKodejabatan(String kodejabatan) {
		this.kodejabatan = kodejabatan;
	}

	public String getNamajabatan() {
		return namajabatan;
	}

	public void setNamajabatan(String namajabatan) {
		this.namajabatan = namajabatan;
	}

	public String getKodejobtitle() {
		return kodejobtitle;
	}

	public void setKodejobtitle(String kodejobtitle) {
		this.kodejobtitle = kodejobtitle;
	}

	public String getNamajobtitle() {
		return namajobtitle;
	}

	public void setNamajobtitle(String namajobtitle) {
		this.namajobtitle = namajobtitle;
	}

	public String getKodemodelkompetensijobtitle() {
		return kodemodelkompetensijobtitle;
	}

	public void setKodemodelkompetensijobtitle(String kodemodelkompetensijobtitle) {
		this.kodemodelkompetensijobtitle = kodemodelkompetensijobtitle;
	}

	public String getKodeunitkerja() {
		return kodeunitkerja;
	}

	public void setKodeunitkerja(String kodeunitkerja) {
		this.kodeunitkerja = kodeunitkerja;
	}

	public String getNamaunitkerja() {
		return namaunitkerja;
	}

	public void setNamaunitkerja(String namaunitkerja) {
		this.namaunitkerja = namaunitkerja;
	}

	public Timestamp getLastmodified_time() {
		return lastmodified_time;
	}

	public void setLastmodified_time(Timestamp lastmodified_time) {
		this.lastmodified_time = lastmodified_time;
	}

	public Timestamp getCreated_time() {
		return created_time;
	}

	public void setCreated_time(Timestamp created_time) {
		this.created_time = created_time;
	}

	public Short getRow_status() {
		return row_status;
	}

	public void setRow_status(Short row_status) {
		this.row_status = row_status;
	}

}
