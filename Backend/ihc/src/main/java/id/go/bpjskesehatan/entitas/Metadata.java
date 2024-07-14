/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package id.go.bpjskesehatan.entitas;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 *
 * @author bambangpurwanto
 */

@JsonInclude(Include.NON_NULL)
public class Metadata implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int code;
	private String message;
	private Integer rowcount;
	private String id;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getRowcount() {
		return rowcount;
	}

	public void setRowcount(Integer rowcount) {
		this.rowcount = rowcount;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
