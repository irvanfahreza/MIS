package id.go.bpjskesehatan.service.mobile.v1;

public class AuthUser {
	private String token;
	private String npp;
	private Integer userid;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getNpp() {
		return npp;
	}

	public void setNpp(String npp) {
		this.npp = npp;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}
	
}