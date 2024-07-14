package id.go.bpjskesehatan.service.hcplus.response;

public class Employee {
	
	private String token;
	private Integer employeeId;
	private String employeeName;
	private String employeeEmail;
	private String employeePhoto;
	private BirthDate employeeBirthDate;
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Integer getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public String getEmployeeEmail() {
		return employeeEmail;
	}
	public void setEmployeeEmail(String employeeEmail) {
		this.employeeEmail = employeeEmail;
	}
	public String getEmployeePhoto() {
		return employeePhoto;
	}
	public void setEmployeePhoto(String employeePhoto) {
		this.employeePhoto = employeePhoto;
	}
	public BirthDate getEmployeeBirthDate() {
		return employeeBirthDate;
	}
	public void setEmployeeBirthDate(BirthDate employeeBirthDate) {
		this.employeeBirthDate = employeeBirthDate;
	}
	
}
