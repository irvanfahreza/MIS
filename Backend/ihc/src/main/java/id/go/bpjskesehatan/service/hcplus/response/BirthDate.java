package id.go.bpjskesehatan.service.hcplus.response;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class BirthDate {
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
	private Date date;
	private Integer timezone_type;
	private String timezone;
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Integer getTimezone_type() {
		return timezone_type;
	}
	public void setTimezone_type(Integer timezone_type) {
		this.timezone_type = timezone_type;
	}
	public String getTimezone() {
		return timezone;
	}
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}
	
}
