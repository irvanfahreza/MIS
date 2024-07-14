package id.go.bpjskesehatan.service.mobile.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FirebaseRespon {
	private String notification_key;
	private String error;
	private Integer success;
	private Integer failure;
	public String getNotification_key() {
		return notification_key;
	}
	public void setNotification_key(String notification_key) {
		this.notification_key = notification_key;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public Integer getSuccess() {
		return success;
	}
	public void setSuccess(Integer success) {
		this.success = success;
	}
	public Integer getFailure() {
		return failure;
	}
	public void setFailure(Integer failure) {
		this.failure = failure;
	}
}
