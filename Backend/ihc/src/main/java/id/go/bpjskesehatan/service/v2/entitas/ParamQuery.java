package id.go.bpjskesehatan.service.v2.entitas;

public class ParamQuery {
	private Integer index;
	private String type;
	private String value;
	private String name;
	private Integer values;
	private Integer types;
	
	
	public Integer getValues() {
		return values;
	}
	public void setValues(Integer values) {
		this.values = values;
	}
	public Integer getTypes() {
		return types;
	}
	public void setTypes(Integer types) {
		this.types = types;
	}
	public Integer getIndex() {
		return index;
	}
	public void setIndex(Integer index) {
		this.index = index;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}