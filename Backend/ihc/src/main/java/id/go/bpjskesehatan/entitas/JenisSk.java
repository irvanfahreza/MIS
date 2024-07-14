package id.go.bpjskesehatan.entitas;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Id;
import javax.persistence.Transient;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonInclude(Include.NON_NULL)
@JsonRootName("jenissk")
public class JenisSk implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private String kode;
	private String nama;
	private Integer row_status;
	private Integer created_by;
	private Timestamp created_time;
	private Integer lastmodified_by;
	private Timestamp lastmodified_time;
	private Integer isresign;
	private Integer israngkapjabatan;
	private Integer ispegawaitetap;
	private Integer lockjabatan;
	@Transient
	private String table;

	public JenisSk() {

	}

	public JenisSk(String table) {
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

	public Integer getIsresign() {
		return isresign;
	}

	public void setIsresign(Integer isresign) {
		this.isresign = isresign;
	}

	public Integer getIsrangkapjabatan() {
		return israngkapjabatan;
	}

	public void setIsrangkapjabatan(Integer israngkapjabatan) {
		this.israngkapjabatan = israngkapjabatan;
	}

	public Integer getIspegawaitetap() {
		return ispegawaitetap;
	}

	public void setIspegawaitetap(Integer ispegawaitetap) {
		this.ispegawaitetap = ispegawaitetap;
	}

	public Integer getLockjabatan() {
		return lockjabatan;
	}

	public void setLockjabatan(Integer lockjabatan) {
		this.lockjabatan = lockjabatan;
	}
	
}
