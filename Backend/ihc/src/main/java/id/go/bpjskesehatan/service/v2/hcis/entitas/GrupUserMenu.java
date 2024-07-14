package id.go.bpjskesehatan.service.v2.hcis.entitas;

public class GrupUserMenu {
	private Integer kodegrupuser;
	private String kode;
	private String label;
	private String kodeparent;
	private Boolean create;
	private Boolean read;
	private Boolean update;
	private Boolean delete;
	private Boolean createprivilege;
	private Boolean readprivilege;
	private Boolean updateprivilege;
	private Boolean deleteprivilege;
	
	public Integer getKodegrupuser() {
		return kodegrupuser;
	}
	public void setKodegrupuser(Integer kodegrupuser) {
		this.kodegrupuser = kodegrupuser;
	}
	public String getKode() {
		return kode;
	}
	public void setKode(String kode) {
		this.kode = kode;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getKodeparent() {
		return kodeparent;
	}
	public void setKodeparent(String kodeparent) {
		this.kodeparent = kodeparent;
	}
	public Boolean getCreate() {
		return create;
	}
	public void setCreate(Boolean create) {
		this.create = create;
	}
	public Boolean getRead() {
		return read;
	}
	public void setRead(Boolean read) {
		this.read = read;
	}
	public Boolean getUpdate() {
		return update;
	}
	public void setUpdate(Boolean update) {
		this.update = update;
	}
	public Boolean getDelete() {
		return delete;
	}
	public void setDelete(Boolean delete) {
		this.delete = delete;
	}
	public Boolean getCreateprivilege() {
		return createprivilege;
	}
	public void setCreateprivilege(Boolean createprivilege) {
		this.createprivilege = createprivilege;
	}
	public Boolean getReadprivilege() {
		return readprivilege;
	}
	public void setReadprivilege(Boolean readprivilege) {
		this.readprivilege = readprivilege;
	}
	public Boolean getUpdateprivilege() {
		return updateprivilege;
	}
	public void setUpdateprivilege(Boolean updateprivilege) {
		this.updateprivilege = updateprivilege;
	}
	public Boolean getDeleteprivilege() {
		return deleteprivilege;
	}
	public void setDeleteprivilege(Boolean deleteprivilege) {
		this.deleteprivilege = deleteprivilege;
	}

}
