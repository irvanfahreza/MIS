package id.go.bpjskesehatan.entitas;

import id.go.bpjskesehatan.entitas.hcis.GrupUser;
import id.go.bpjskesehatan.entitas.karyawan.Pegawai;

import java.io.Serializable;
import java.util.ArrayList;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Table(name = "hcis.users")
@JsonRootName("user")
@JsonInclude(Include.NON_NULL)
public class User implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;
	
	private String username;

	// @JsonProperty("password")
	// @Column(name = "pass")
	// private String password;
	private String npp;
	@JsonProperty("grupuser")
	@Column(name = "kodegrupuser")
	private GrupUser grupuser;
	private Integer status;
	private Pegawai pegawai;
	private Integer passexpired;
	private Integer blocked;
	private String pass;
	private String defaultpass;
	private Integer generatepass;
	private Integer enablemfa;
	private String requestid;
	private ArrayList<GrupUser> pegawaigrupuser = new ArrayList<>();

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNpp() {
		return npp;
	}

	public void setNpp(String npp) {
		this.npp = npp;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Pegawai getPegawai() {
		return pegawai;
	}

	public void setPegawai(Pegawai pegawai) {
		this.pegawai = pegawai;
	}
	
	public Integer getPassexpired() {
		return passexpired;
	}

	public void setPassexpired(Integer passexpired) {
		this.passexpired = passexpired;
	}

	public Integer getBlocked() {
		return blocked;
	}

	public void setBlocked(Integer blocked) {
		this.blocked = blocked;
	}

	public GrupUser getGrupuser() {
		return grupuser;
	}

	public void setGrupuser(GrupUser grupuser) {
		this.grupuser = grupuser;
	}

	public ArrayList<GrupUser> getPegawaigrupuser() {
		return pegawaigrupuser;
	}

	public void setPegawaigrupuser(ArrayList<GrupUser> pegawaigrupuser) {
		this.pegawaigrupuser = pegawaigrupuser;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getDefaultpass() {
		return defaultpass;
	}

	public void setDefaultpass(String defaultpass) {
		this.defaultpass = defaultpass;
	}

	public Integer getGeneratepass() {
		return generatepass;
	}

	public void setGeneratepass(Integer generatepass) {
		this.generatepass = generatepass;
	}

	public Integer getEnablemfa() {
		return enablemfa;
	}

	public void setEnablemfa(Integer enablemfa) {
		this.enablemfa = enablemfa;
	}

	public String getRequestid() {
		return requestid;
	}

	public void setRequestid(String requestid) {
		this.requestid = requestid;
	}	
	
}
