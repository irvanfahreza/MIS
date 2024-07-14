package id.go.bpjskesehatan.entitas.hcis;

public class MenuPegawai{
	private String kode;
	private String name;
	private String label;
	private Integer level;
	private Integer tipe;
	private String kodeparent;
	private String icon;
	private Boolean create;
	private Boolean read;
	private Boolean update;
	private Boolean delete;
	public String getKode() {
		return kode;
	}
	public void setKode(String kode) {
		this.kode = kode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public Integer getTipe() {
		return tipe;
	}
	public void setTipe(Integer tipe) {
		this.tipe = tipe;
	}
	public String getKodeparent() {
		return kodeparent;
	}
	public void setKodeparent(String kodeparent) {
		this.kodeparent = kodeparent;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
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

}