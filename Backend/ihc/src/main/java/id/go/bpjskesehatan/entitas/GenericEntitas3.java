package id.go.bpjskesehatan.entitas;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Id;
import javax.persistence.Transient;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("referensi")
public class GenericEntitas3 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private String kode;
	private String nama;
	@JsonProperty("ref1")
	private GenericEntitas genericEntitas2;
	@JsonProperty("ref2")
	private GenericEntitas genericEntitas3;
	private Integer row_status;
	private Integer created_by;
	private Timestamp created_time;
	private Integer lastmodified_by;
	private Timestamp lastmodified_time;
	@Transient
	private String table;

	public GenericEntitas3() {

	}

	public GenericEntitas3(String table) {
		super();
		this.table = table;
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

	public GenericEntitas getGenericEntitas2() {
		return genericEntitas2;
	}

	public void setGenericEntitas2(GenericEntitas genericEntitas2) {
		this.genericEntitas2 = genericEntitas2;
	}

	public GenericEntitas getGenericEntitas3() {
		return genericEntitas3;
	}

	public void setGenericEntitas3(GenericEntitas genericEntitas3) {
		this.genericEntitas3 = genericEntitas3;
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

	public Integer getRow_status() {
		return row_status;
	}

	public void setRow_status(Integer row_status) {
		this.row_status = row_status;
	}

	public String getTable() {
		return table;
	}

}
