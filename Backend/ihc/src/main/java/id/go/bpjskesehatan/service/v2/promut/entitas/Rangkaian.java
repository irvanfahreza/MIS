package id.go.bpjskesehatan.service.v2.promut.entitas;

import java.sql.Date;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonFormat;

import id.go.bpjskesehatan.Constant;
import id.go.bpjskesehatan.service.v2.skpd.entitas.ListPegawai;

public class Rangkaian {

	private Integer num;
	private Integer kode;
	private Integer tahun;
	private String nama;
	private String nosurat;
	private String catatan;
	private Integer ajukan;
	private Integer setujuasdep;
	private Integer setujudepdir;
	private Integer setujudir;
	private String catasdep;
	private String catdepdir;
	private String catdir;
	private Integer pembuat;
	private String kodeoffice;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constant.ServerTimezone)
	private Date tglajukan;
	private Integer useract;
	private Integer koderefrangkaian;
	private String approval;
	
	/**
	 * @return the num
	 */
	public Integer getNum() {
		return num;
	}
	/**
	 * @param num the num to set
	 */
	public void setNum(Integer num) {
		this.num = num;
	}
	public Integer getKode() {
		return kode;
	}
	public void setKode(Integer kode) {
		this.kode = kode;
	}
	public String getNama() {
		return nama;
	}
	public void setNama(String nama) {
		this.nama = nama;
	}
	public String getNosurat() {
		return nosurat;
	}
	public void setNosurat(String nosurat) {
		this.nosurat = nosurat;
	}
	public String getCatatan() {
		return catatan;
	}
	public void setCatatan(String catatan) {
		this.catatan = catatan;
	}
	public Integer getAjukan() {
		return ajukan;
	}
	public void setAjukan(Integer ajukan) {
		this.ajukan = ajukan;
	}
	public Integer getSetujuasdep() {
		return setujuasdep;
	}
	public void setSetujuasdep(Integer setujuasdep) {
		this.setujuasdep = setujuasdep;
	}
	public Integer getSetujudepdir() {
		return setujudepdir;
	}
	public void setSetujudepdir(Integer setujudepdir) {
		this.setujudepdir = setujudepdir;
	}
	public Integer getSetujudir() {
		return setujudir;
	}
	public void setSetujudir(Integer setujudir) {
		this.setujudir = setujudir;
	}
	public String getCatasdep() {
		return catasdep;
	}
	public void setCatasdep(String catasdep) {
		this.catasdep = catasdep;
	}
	public String getCatdepdir() {
		return catdepdir;
	}
	public void setCatdepdir(String catdepdir) {
		this.catdepdir = catdepdir;
	}
	public String getCatdir() {
		return catdir;
	}
	public void setCatdir(String catdir) {
		this.catdir = catdir;
	}
	public Integer getPembuat() {
		return pembuat;
	}
	public void setPembuat(Integer pembuat) {
		this.pembuat = pembuat;
	}
	public String getKodeoffice() {
		return kodeoffice;
	}
	public void setKodeoffice(String kodeoffice) {
		this.kodeoffice = kodeoffice;
	}
	public Date getTglajukan() {
		return tglajukan;
	}
	public void setTglajukan(Date tglajukan) {
		this.tglajukan = tglajukan;
	}
	public Integer getUseract() {
		return useract;
	}
	public void setUseract(Integer useract) {
		this.useract = useract;
	}
	/**
	 * @return the approval
	 */
	public String getApproval() {
		return approval;
	}
	/**
	 * @param approval the approval to set
	 */
	public void setApproval(String approval) {
		this.approval = approval;
	}
	/**
	 * @return the koderefrangkaian
	 */
	public Integer getKoderefrangkaian() {
		return koderefrangkaian;
	}
	/**
	 * @param koderefrangkaian the koderefrangkaian to set
	 */
	public void setKoderefrangkaian(Integer koderefrangkaian) {
		this.koderefrangkaian = koderefrangkaian;
	}
	/**
	 * @return the tahun
	 */
	public Integer getTahun() {
		return tahun;
	}
	/**
	 * @param tahun the tahun to set
	 */
	public void setTahun(Integer tahun) {
		this.tahun = tahun;
	}
}
