package id.go.bpjskesehatan.entitas.kinerja;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.sql.Timestamp;


/**
 * The persistent class for the MASTERKOMUNIKASI database table.
 * 
 */
@JsonInclude(Include.NON_NULL)
@Entity
@NamedQuery(name="Masterkomunikasi.findAll", query="SELECT m FROM Masterkomunikasi m")
public class Masterkomunikasi implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int kode;

	@Column(name="CREATED_BY")
	private Integer created_by;

	@Column(name="CREATED_TIME")
	private Timestamp created_time;

	@Lob
	private String katapengantar;

	@Column(name="LASTMODIFIED_BY")
	private Integer lastmodified_by;

	@Column(name="LASTMODIFIED_TIME")
	private Timestamp lastmodified_time;

	private String nama;

	@Lob
	private String pengingat;

	@Lob
	private String petunjukpenggunaan;

	@Column(name="ROW_STATUS")
	private Short row_status;

	private short tipeumpanbalik;

	@Lob
	private String undangan;

	public Masterkomunikasi() {
	}

	public int getKode() {
		return this.kode;
	}

	public void setKode(int kode) {
		this.kode = kode;
	}

	public Integer getCreated_by() {
		return this.created_by;
	}

	public void setCreated_by(Integer created_by) {
		this.created_by = created_by;
	}

	public Timestamp getCreated_time() {
		return this.created_time;
	}

	public void setCreated_time(Timestamp created_time) {
		this.created_time = created_time;
	}

	public String getKatapengantar() {
		return this.katapengantar;
	}

	public void setKatapengantar(String katapengantar) {
		this.katapengantar = katapengantar;
	}

	public Integer getLastmodified_by() {
		return this.lastmodified_by;
	}

	public void setLastmodified_by(Integer lastmodified_by) {
		this.lastmodified_by = lastmodified_by;
	}

	public Timestamp getLastmodified_time() {
		return this.lastmodified_time;
	}

	public void setLastmodified_time(Timestamp lastmodified_time) {
		this.lastmodified_time = lastmodified_time;
	}

	public String getNama() {
		return this.nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public String getPengingat() {
		return this.pengingat;
	}

	public void setPengingat(String pengingat) {
		this.pengingat = pengingat;
	}

	public String getPetunjukpenggunaan() {
		return this.petunjukpenggunaan;
	}

	public void setPetunjukpenggunaan(String petunjukpenggunaan) {
		this.petunjukpenggunaan = petunjukpenggunaan;
	}

	public Short getRow_status() {
		return this.row_status;
	}

	public void setRow_status(Short row_status) {
		this.row_status = row_status;
	}

	public short getTipeumpanbalik() {
		return this.tipeumpanbalik;
	}

	public void setTipeumpanbalik(short tipeumpanbalik) {
		this.tipeumpanbalik = tipeumpanbalik;
	}

	public String getUndangan() {
		return this.undangan;
	}

	public void setUndangan(String undangan) {
		this.undangan = undangan;
	}

}