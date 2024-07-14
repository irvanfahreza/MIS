/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package id.go.bpjskesehatan.entitas;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

/**
 *
 * @author bambangpurwanto
 * @param <E>
 */

@JsonInclude(Include.NON_NULL)
@JsonRootName("response")
public class Respon2 implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("data")
	private Object data;
	
	@JsonProperty("list")
	private List<Object> list;

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public List<Object> getList() {
		return list;
	}

	public void setList(List<Object> listdata) {
		this.list = listdata;
	}

}
