package id.go.bpjskesehatan.service.v2.organisasi.entitas;

import java.util.ArrayList;
import java.util.List;

public class Tree {
	private String kode;
	private String kodeparent;
	private String label;
	private List<Tree> children;
	
	public Tree() {
		this.children = new ArrayList<Tree>();
	}
	
	public Tree(String kode, String kodeparent) {
		this.kode = kode;
		this.kodeparent = kodeparent;
	}
	
	public String getKode() {
		return kode;
	}
	public void setKode(String kode) {
		this.kode = kode;
	}
	public String getKodeparent() {
		return kodeparent;
	}
	public void setKodeparent(String kodeparent) {
		this.kodeparent = kodeparent;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public List<Tree> getChildren() {
		return children;
	}
	public void setChildren(List<Tree> children) {
		this.children = children;
	}
	public void addChildrenItem(Tree children){
        if(!this.children.contains(children))
            this.children.add(children);
    }
}