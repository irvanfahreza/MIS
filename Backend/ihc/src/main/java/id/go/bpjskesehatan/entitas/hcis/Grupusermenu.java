package id.go.bpjskesehatan.entitas.hcis;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Grupusermenu implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String kode;
	private GrupUser grupuser;
	private String nama;
	private String state;
	private Integer tipe;
	private Integer level;
	private String kodeparent;
	private Integer create;
	private Integer read;
	private Integer update;
	private Integer delete;
	private Integer createprivilege;
	private Integer readprivilege;
	private Integer updateprivilege;
	private Integer deleteprivilege;
	private String icon;

	public String getKode() {
		return kode;
	}

	public void setKode(String kode) {
		this.kode = kode;
	}

	public GrupUser getGrupuser() {
		return grupuser;
	}

	public void setGrupuser(GrupUser grupuser) {
		this.grupuser = grupuser;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Integer getTipe() {
		return tipe;
	}

	public void setTipe(Integer tipe) {
		this.tipe = tipe;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getKodeparent() {
		return kodeparent;
	}

	public void setKodeparent(String kodeparent) {
		this.kodeparent = kodeparent;
	}

	public Integer getCreate() {
		return create;
	}

	public void setCreate(Integer create) {
		this.create = create;
	}

	public Integer getRead() {
		return read;
	}

	public void setRead(Integer read) {
		this.read = read;
	}

	public Integer getUpdate() {
		return update;
	}

	public void setUpdate(Integer update) {
		this.update = update;
	}

	public Integer getDelete() {
		return delete;
	}

	public void setDelete(Integer delete) {
		this.delete = delete;
	}

	public Integer getCreateprivilege() {
		return createprivilege;
	}

	public void setCreateprivilege(Integer createprivilege) {
		this.createprivilege = createprivilege;
	}

	public Integer getReadprivilege() {
		return readprivilege;
	}

	public void setReadprivilege(Integer readprivilege) {
		this.readprivilege = readprivilege;
	}

	public Integer getUpdateprivilege() {
		return updateprivilege;
	}

	public void setUpdateprivilege(Integer updateprivilege) {
		this.updateprivilege = updateprivilege;
	}

	public Integer getDeleteprivilege() {
		return deleteprivilege;
	}

	public void setDeleteprivilege(Integer deleteprivilege) {
		this.deleteprivilege = deleteprivilege;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
	
}
