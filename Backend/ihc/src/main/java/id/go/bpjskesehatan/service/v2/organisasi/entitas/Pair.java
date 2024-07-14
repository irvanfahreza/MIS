package id.go.bpjskesehatan.service.v2.organisasi.entitas;

public class Pair {
	private String childId;
    private String parentId;
    private String label;

    public Pair() {
    
    }
    
    public Pair(String childId, String parentId, String label) {
        this.childId = childId;
        this.parentId = parentId;
        this.label = label;
    }
    
    public String getChildId() {
        return childId;
    }
    public void setChildId(String childId) {
        this.childId = childId;
    }
    public String getParentId() {
        return parentId;
    }
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
}