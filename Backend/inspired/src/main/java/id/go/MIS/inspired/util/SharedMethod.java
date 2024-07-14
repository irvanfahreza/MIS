package id.go.MIS.inspired.util;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


public class SharedMethod {
	
	public static ResponseEntity<Result<Object>> getResponse(HttpStatus httpStatus, String message){
		Result<Object> result = new Result<>();
		Metadata metadata = new Metadata();
		metadata.setCode(httpStatus.value());
		metadata.setMessage(message);
		result.setMetadata(metadata);
		return new ResponseEntity<Result<Object>>(result, httpStatus);
	}
	
	public static <T> ResponseEntity<Result<T>> getResponse(HttpStatus httpStatus, String message, T T){
		Result<T> result = new Result<>();
		Metadata metadata = new Metadata();
		metadata.setCode(httpStatus.value());
		metadata.setMessage(message);
		result.setMetadata(metadata);
		Respon<T> respon = new Respon<>();
		respon.setData(T);
		result.setResponse(respon);
		return new ResponseEntity<Result<T>>(result, httpStatus);
	}
	
	public static <T> ResponseEntity<Result<T>> getResponse(HttpStatus httpStatus, String message, List<T> T){
		Result<T> result = new Result<>();
		Metadata metadata = new Metadata();
		metadata.setCode(httpStatus.value());
		metadata.setMessage(message);
		result.setMetadata(metadata);
		Respon<T> respon = new Respon<>();
		respon.setList(T);
		result.setResponse(respon);
		return new ResponseEntity<Result<T>>(result, httpStatus);
	}
}