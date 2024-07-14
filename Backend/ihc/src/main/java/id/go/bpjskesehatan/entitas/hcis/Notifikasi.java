package id.go.bpjskesehatan.entitas.hcis;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Notifikasi implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	private Integer kode;
	private String fromnpp;
	private String fromnama;
	private String tonpp;
	private String deskripsi;
	private String detil;
	private Short tipe;
	private Integer fkode;
	private Short isread;
	private Short flag;
	private Short jenis;
	private Short row_status;
	private Integer created_by;
	private Timestamp created_time;
	private Integer lastmodified_by;
	private Timestamp lastmodified_time;
	private Integer deleted;
	private String catatan;
	private String tokodejobtitle;
	private Integer tokodepenugasan;
	private Integer fromkodepenugasan;
	
	public Integer getKode() {
		return kode;
	}
	public void setKode(Integer kode) {
		this.kode = kode;
	}
	public String getFromnpp() {
		return fromnpp;
	}
	public void setFromnpp(String fromnpp) {
		this.fromnpp = fromnpp;
	}
	public String getFromnama() {
		return fromnama;
	}
	public void setFromnama(String fromnama) {
		this.fromnama = fromnama;
	}
	public String getTonpp() {
		return tonpp;
	}
	public void setTonpp(String tonpp) {
		this.tonpp = tonpp;
	}
	public String getDeskripsi() {
		return deskripsi;
	}
	public void setDeskripsi(String deskripsi) {
		this.deskripsi = deskripsi;
	}
	public String getDetil() {
		return detil;
	}
	public void setDetil(String detil) {
		this.detil = detil;
	}
	public Short getTipe() {
		return tipe;
	}
	public void setTipe(Short tipe) {
		this.tipe = tipe;
	}
	public Integer getFkode() {
		return fkode;
	}
	public void setFkode(Integer fkode) {
		this.fkode = fkode;
	}
	public Short getIsread() {
		return isread;
	}
	public void setIsread(Short isread) {
		this.isread = isread;
	}
	public Short getFlag() {
		return flag;
	}
	public void setFlag(Short flag) {
		this.flag = flag;
	}
	public Short getJenis() {
		return jenis;
	}
	public void setJenis(Short jenis) {
		this.jenis = jenis;
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
	public Integer getDeleted() {
		return deleted;
	}
	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}
	public String getCatatan() {
		return catatan;
	}
	public void setCatatan(String catatan) {
		this.catatan = catatan;
	}
	public String getTokodejobtitle() {
		return tokodejobtitle;
	}
	public void setTokodejobtitle(String tokodejobtitle) {
		this.tokodejobtitle = tokodejobtitle;
	}
	public Integer getTokodepenugasan() {
		return tokodepenugasan;
	}
	public void setTokodepenugasan(Integer tokodepenugasan) {
		this.tokodepenugasan = tokodepenugasan;
	}
	public Integer getFromkodepenugasan() {
		return fromkodepenugasan;
	}
	public void setFromkodepenugasan(Integer fromkodepenugasan) {
		this.fromkodepenugasan = fromkodepenugasan;
	}
}
