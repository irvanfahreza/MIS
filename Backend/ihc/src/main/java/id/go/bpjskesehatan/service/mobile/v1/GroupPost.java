package id.go.bpjskesehatan.service.mobile.v1;

public class GroupPost {
	private String operation;
	private String notification_key;
	private String notification_key_name;
	private String[] registration_ids;
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public String getNotification_key_name() {
		return notification_key_name;
	}
	public void setNotification_key_name(String notification_key_name) {
		this.notification_key_name = notification_key_name;
	}
	public String[] getRegistration_ids() {
		return registration_ids;
	}
	public void setRegistration_ids(String[] registration_ids) {
		this.registration_ids = registration_ids;
	}
	public String getNotification_key() {
		return notification_key;
	}
	public void setNotification_key(String notification_key) {
		this.notification_key = notification_key;
	}
}
