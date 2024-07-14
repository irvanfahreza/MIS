package id.go.bpjskesehatan.inspired.util;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("response")
@JsonInclude(Include.NON_NULL)
public class Respon<T> implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("data")
	private T data;
	
	@JsonProperty("list")
	private List<T> list;

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> listdata) {
		this.list = listdata;
	}

}
