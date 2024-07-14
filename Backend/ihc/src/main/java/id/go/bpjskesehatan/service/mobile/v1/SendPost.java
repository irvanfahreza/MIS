package id.go.bpjskesehatan.service.mobile.v1;

public class SendPost {
	private String to;
	private SendNotification notification;
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public SendNotification getNotification() {
		return notification;
	}
	public void setNotification(SendNotification notification) {
		this.notification = notification;
	}
}
