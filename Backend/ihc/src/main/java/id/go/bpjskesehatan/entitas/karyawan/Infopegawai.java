package id.go.bpjskesehatan.entitas.karyawan;

import java.io.Serializable;
import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import id.go.bpjskesehatan.Constant;

@JsonInclude(Include.NON_NULL)
public class Infopegawai implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String npp;
	private String nama;
	private String kodejabatan;
	private String namajabatan;
	private String email;
	private String notelp;
	private String kodekantor;
	private String namakantor;
	private String kodeunitkerja;
	private String namaunitkerja;
	private String kodedeputi;
	private String namadeputi;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constant.ServerTimezone)
	private Date updatedate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constant.ServerTimezone)
	private Date createdate;

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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNotelp() {
		return notelp;
	}

	public void setNotelp(String notelp) {
		this.notelp = notelp;
	}

	public String getKodekantor() {
		return kodekantor;
	}

	public void setKodekantor(String kodekantor) {
		this.kodekantor = kodekantor;
	}

	public String getNamakantor() {
		return namakantor;
	}

	public void setNamakantor(String namakantor) {
		this.namakantor = namakantor;
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

	public String getKodedeputi() {
		return kodedeputi;
	}

	public void setKodedeputi(String kodedeputi) {
		this.kodedeputi = kodedeputi;
	}

	public String getNamadeputi() {
		return namadeputi;
	}

	public void setNamadeputi(String namadeputi) {
		this.namadeputi = namadeputi;
	}

	public Date getUpdatedate() {
		return updatedate;
	}

	public void setUpdatedate(Date updatedate) {
		this.updatedate = updatedate;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

}
