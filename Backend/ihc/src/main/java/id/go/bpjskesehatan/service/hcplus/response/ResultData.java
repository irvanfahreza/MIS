package id.go.bpjskesehatan.service.hcplus.response;

public class ResultData<D> extends ResultBase {
	
	private D data;
	
	public D getData() {
		return data;
	}
	public void setData(D data) {
		this.data = data;
	}
	
}
