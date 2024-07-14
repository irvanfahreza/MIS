/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package id.go.bpjskesehatan.entitas;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

/**
 *
 * @author bambangpurwanto
 * @param <E>
 * @param <E>
 */

@JsonRootName("result")
@JsonInclude(Include.NON_NULL)
public class Result<E> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("response")
	private Respon<E> response;
	
	@JsonProperty("metadata")
	private Metadata metadata;

	public Respon<E> getResponse() {
		return response;
	}

	public void setResponse(Respon<E> response) {
		this.response = response;
	}

	public Metadata getMetadata() {
		return metadata;
	}

	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}

}
