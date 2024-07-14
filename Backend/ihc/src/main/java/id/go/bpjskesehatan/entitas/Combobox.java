package id.go.bpjskesehatan.entitas;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Combobox {
	private Integer kode;
	private Integer poin;
	@JsonProperty("kode2")
	private String kode2;
	private String nama;
	private Integer kodekategoripendidikan;
	private Integer row_status;
	private Integer lockjabatan;
	
	public Combobox() {
		
	}
	
	public Combobox(Integer kode, String nama) {
		this.kode = kode;
		this.nama = nama;
	}
	
	public Integer getKode() {
		return kode;
	}
	public void setKode(Integer kode) {
		this.kode = kode;
	}
	public String getKode2() {
		return kode2;
	}
	public void setKode2(String kode2) {
		this.kode2 = kode2;
	}
	public String getNama() {
		return nama;
	}
	public void setNama(String nama) {
		this.nama = nama;
	}

	/**
	 * @return the poin
	 */
	public Integer getPoin() {
		return poin;
	}

	/**
	 * @param poin the poin to set
	 */
	public void setPoin(Integer poin) {
		this.poin = poin;
	}

	public Integer getKodekategoripendidikan() {
		return kodekategoripendidikan;
	}

	public void setKodekategoripendidikan(Integer kodekategoripendidikan) {
		this.kodekategoripendidikan = kodekategoripendidikan;
	}

	public Integer getRow_status() {
		return row_status;
	}

	public void setRow_status(Integer row_status) {
		this.row_status = row_status;
	}

	public Integer getLockjabatan() {
		return lockjabatan;
	}

	public void setLockjabatan(Integer lockjabatan) {
		this.lockjabatan = lockjabatan;
	}
}
