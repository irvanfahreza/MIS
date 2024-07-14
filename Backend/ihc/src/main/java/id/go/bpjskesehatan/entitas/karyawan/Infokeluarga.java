package id.go.bpjskesehatan.entitas.karyawan;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import id.go.bpjskesehatan.Constant;
import id.go.bpjskesehatan.entitas.GenericEntitas;
import id.go.bpjskesehatan.entitas.referensi.Dati2;

import java.util.Date;
import java.sql.Timestamp;

/**
 * The persistent class for the INFOKELUARGA database table.
 * 
 */
@Entity
@JsonInclude(Include.NON_NULL)
@NamedQuery(name = "Infokeluarga.findAll", query = "SELECT i FROM Infokeluarga i")
public class Infokeluarga implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int kode;

	private String alamat;

	private short anakke;

	private String catatan;

	@Column(name = "CREATED_BY")
	private Integer created_by;

	@Column(name = "CREATED_TIME")
	private Timestamp created_time;

	private String hp;

	@JsonProperty("hubungankeluarga")
	@Column(name = "kodehubungankeluarga")
	private GenericEntitas hubungankeluarga;

	@JsonProperty("jeniskelamin")
	@Column(name = "kodejeniskelamin")
	private GenericEntitas jeniskelamin;

	@JsonProperty("pekerjaan")
	@Column(name = "kodepekerjaan")
	private GenericEntitas pekerjaan;

	@JsonProperty("dati2")
	@Column(name = "kodedati2")
	private Dati2 dati2;
	
	@Column(name = "LASTMODIFIED_BY")
	private Integer lastmodified_by;

	@Column(name = "LASTMODIFIED_TIME")
	private Timestamp lastmodified_time;

	private String nama;

	private String noasuransi;

	private String npp;

	private double prosentaseklaim;

	@Column(name = "ROW_STATUS")
	private Short row_status;

	private short tanggunganasuransi;

	private short tanggunganpajak;

	private String telprumah;
	
	private Integer inti;

	private String tempatlahir;

	@Temporal(TemporalType.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constant.ServerTimezone)
	private Date tgllahir;

	@Temporal(TemporalType.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constant.ServerTimezone)
	private Date tglnikah;
	
	private String lampiran;
	private Integer adalampiran;
	
	private Integer iskontakdarurat;
	private Integer istanggunganbpjsk;
	private Integer istanggunganinhealth;
	
	public Integer getInti() {
		return inti;
	}

	public void setInti(Integer inti) {
		this.inti = inti;
	}

	public Dati2 getDati2() {
		return dati2;
	}

	public void setDati2(Dati2 dati2) {
		this.dati2 = dati2;
	}

	public Integer getIskontakdarurat() {
		return iskontakdarurat;
	}

	public void setIskontakdarurat(Integer iskontakdarurat) {
		this.iskontakdarurat = iskontakdarurat;
	}

	public Integer getAdalampiran() {
		return adalampiran;
	}

	public void setAdalampiran(Integer adalampiran) {
		this.adalampiran = adalampiran;
	}

	public String getLampiran() {
		return lampiran;
	}

	public void setLampiran(String lampiran) {
		this.lampiran = lampiran;
	}

	public Infokeluarga() {
	}

	public int getKode() {
		return this.kode;
	}

	public void setKode(int kode) {
		this.kode = kode;
	}

	public String getAlamat() {
		return this.alamat;
	}

	public void setAlamat(String alamat) {
		this.alamat = alamat;
	}

	public short getAnakke() {
		return this.anakke;
	}

	public void setAnakke(short anakke) {
		this.anakke = anakke;
	}

	public String getCatatan() {
		return this.catatan;
	}

	public void setCatatan(String catatan) {
		this.catatan = catatan;
	}

	public Integer getCreated_by() {
		return this.created_by;
	}

	public void setCreated_by(Integer created_by) {
		this.created_by = created_by;
	}

	public Timestamp getCreated_time() {
		return this.created_time;
	}

	public void setCreated_time(Timestamp created_time) {
		this.created_time = created_time;
	}

	public String getHp() {
		return this.hp;
	}

	public void setHp(String hp) {
		this.hp = hp;
	}

	public GenericEntitas getHubungankeluarga() {
		return hubungankeluarga;
	}

	public void setHubungankeluarga(GenericEntitas hubungankeluarga) {
		this.hubungankeluarga = hubungankeluarga;
	}

	public GenericEntitas getJeniskelamin() {
		return jeniskelamin;
	}

	public void setJeniskelamin(GenericEntitas jeniskelamin) {
		this.jeniskelamin = jeniskelamin;
	}

	public GenericEntitas getPekerjaan() {
		return pekerjaan;
	}

	public void setPekerjaan(GenericEntitas pekerjaan) {
		this.pekerjaan = pekerjaan;
	}

	public void setTanggunganasuransi(short tanggunganasuransi) {
		this.tanggunganasuransi = tanggunganasuransi;
	}

	public Integer getLastmodified_by() {
		return this.lastmodified_by;
	}

	public void setLastmodified_by(Integer lastmodified_by) {
		this.lastmodified_by = lastmodified_by;
	}

	public Timestamp getLastmodified_time() {
		return this.lastmodified_time;
	}

	public void setLastmodified_time(Timestamp lastmodified_time) {
		this.lastmodified_time = lastmodified_time;
	}

	public String getNama() {
		return this.nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public String getNoasuransi() {
		return this.noasuransi;
	}

	public void setNoasuransi(String noasuransi) {
		this.noasuransi = noasuransi;
	}

	public String getNpp() {
		return this.npp;
	}

	public void setNpp(String npp) {
		this.npp = npp;
	}

	public double getProsentaseklaim() {
		return this.prosentaseklaim;
	}

	public void setProsentaseklaim(double prosentaseklaim) {
		this.prosentaseklaim = prosentaseklaim;
	}

	public Short getRow_status() {
		return this.row_status;
	}

	public void setRow_status(Short row_status) {
		this.row_status = row_status;
	}

	public short getTanggunganasuransi() {
		return this.tanggunganasuransi;
	}

	public void setTanggunganauransi(short tanggunganasuransi) {
		this.tanggunganasuransi = tanggunganasuransi;
	}

	public short getTanggunganpajak() {
		return this.tanggunganpajak;
	}

	public void setTanggunganpajak(short tanggunganpajak) {
		this.tanggunganpajak = tanggunganpajak;
	}

	public String getTelprumah() {
		return this.telprumah;
	}

	public void setTelprumah(String telprumah) {
		this.telprumah = telprumah;
	}

	public String getTempatlahir() {
		return this.tempatlahir;
	}

	public void setTempatlahir(String tempatlahir) {
		this.tempatlahir = tempatlahir;
	}

	public Date getTgllahir() {
		return this.tgllahir;
	}

	public void setTgllahir(Date tgllahir) {
		this.tgllahir = tgllahir;
	}

	public Date getTglnikah() {
		return this.tglnikah;
	}

	public void setTglnikah(Date tglnikah) {
		this.tglnikah = tglnikah;
	}

	public Integer getIstanggunganbpjsk() {
		return istanggunganbpjsk;
	}

	public void setIstanggunganbpjsk(Integer istanggunganbpjsk) {
		this.istanggunganbpjsk = istanggunganbpjsk;
	}

	public Integer getIstanggunganinhealth() {
		return istanggunganinhealth;
	}

	public void setIstanggunganinhealth(Integer istanggunganinhealth) {
		this.istanggunganinhealth = istanggunganinhealth;
	}

}