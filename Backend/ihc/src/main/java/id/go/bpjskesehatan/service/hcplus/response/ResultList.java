package id.go.bpjskesehatan.service.hcplus.response;

import java.util.List;

public class ResultList<E> extends ResultBase {
	
	private List<E> data;
	
	public List<E> getData() {
		return data;
	}
	public void setData(List<E> data) {
		this.data = data;
	}
	
}
