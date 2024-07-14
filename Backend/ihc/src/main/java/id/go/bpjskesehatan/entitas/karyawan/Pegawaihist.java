package id.go.bpjskesehatan.entitas.karyawan;

import java.io.Serializable;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import id.go.bpjskesehatan.Constant;

import java.sql.Timestamp;
import java.util.Date;

/**
 * The persistent class for the PEGAWAIHIST database table.
 * 
 */
@JsonInclude(Include.NON_NULL)
@Entity
@NamedQuery(name = "Pegawaihist.findAll", query = "SELECT p FROM Pegawaihist p")
public class Pegawaihist implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int kode;

	@Column(name = "APPROVED_BY")
	private int approvedBy;

	@Column(name = "CREATED_BY")
	private Integer created_by;

	@Column(name = "CREATED_TIME")
	private Timestamp created_time;

	private String email;

	private String email2;

	private short flagapprove;

	private String gelarbelakang;

	private String gelardepan;

	private String hobi;

	private String hp;

	private String hp2;

	private int kewarganegaraan;

	private int kodeagama;

	private String kodeasalpenerimaan;

	private short kodejeniskelamin;

	private short kodesalutasi;

	private int kodestatuskaryawan;

	private short kodestatusnikah;

	private int kodesukubangsa;

	@Column(name = "LASTMODIFIED_BY")
	private Integer lastmodified_by;

	@Column(name = "LASTMODIFIED_TIME")
	private Timestamp lastmodified_time;

	private String namabelakang;

	private String namadepan;

	private String namapanggilan;

	private String namatengah;

	@Column(name = "ROW_STATUS")
	private Short row_status;

	private String telprumah;

	private String tempatlahir;

	private Timestamp tglapprove;

	@Temporal(TemporalType.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constant.ServerTimezone)
	private Date tgllahir;

	@Temporal(TemporalType.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constant.ServerTimezone)
	private Date tmtmasuk;

	private String website;

	// bi-directional many-to-one association to Pegawai
	@ManyToOne
	@JoinColumn(name = "NPP")
	private Pegawai pegawai;

	public Pegawaihist() {
	}

	public int getKode() {
		return this.kode;
	}

	public void setKode(int kode) {
		this.kode = kode;
	}

	public int getApprovedBy() {
		return this.approvedBy;
	}

	public void setApprovedBy(int approvedBy) {
		this.approvedBy = approvedBy;
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

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail2() {
		return this.email2;
	}

	public void setEmail2(String email2) {
		this.email2 = email2;
	}

	public short getFlagapprove() {
		return this.flagapprove;
	}

	public void setFlagapprove(short flagapprove) {
		this.flagapprove = flagapprove;
	}

	public String getGelarbelakang() {
		return this.gelarbelakang;
	}

	public void setGelarbelakang(String gelarbelakang) {
		this.gelarbelakang = gelarbelakang;
	}

	public String getGelardepan() {
		return this.gelardepan;
	}

	public void setGelardepan(String gelardepan) {
		this.gelardepan = gelardepan;
	}

	public String getHobi() {
		return this.hobi;
	}

	public void setHobi(String hobi) {
		this.hobi = hobi;
	}

	public String getHp() {
		return this.hp;
	}

	public void setHp(String hp) {
		this.hp = hp;
	}

	public String getHp2() {
		return this.hp2;
	}

	public void setHp2(String hp2) {
		this.hp2 = hp2;
	}

	public int getKewarganegaraan() {
		return this.kewarganegaraan;
	}

	public void setKewarganegaraan(int kewarganegaraan) {
		this.kewarganegaraan = kewarganegaraan;
	}

	public int getKodeagama() {
		return this.kodeagama;
	}

	public void setKodeagama(int kodeagama) {
		this.kodeagama = kodeagama;
	}

	public String getKodeasalpenerimaan() {
		return this.kodeasalpenerimaan;
	}

	public void setKodeasalpenerimaan(String kodeasalpenerimaan) {
		this.kodeasalpenerimaan = kodeasalpenerimaan;
	}

	public short getKodejeniskelamin() {
		return this.kodejeniskelamin;
	}

	public void setKodejeniskelamin(short kodejeniskelamin) {
		this.kodejeniskelamin = kodejeniskelamin;
	}

	public short getKodesalutasi() {
		return this.kodesalutasi;
	}

	public void setKodesalutasi(short kodesalutasi) {
		this.kodesalutasi = kodesalutasi;
	}

	public int getKodestatuskaryawan() {
		return this.kodestatuskaryawan;
	}

	public void setKodestatuskaryawan(int kodestatuskaryawan) {
		this.kodestatuskaryawan = kodestatuskaryawan;
	}

	public short getKodestatusnikah() {
		return this.kodestatusnikah;
	}

	public void setKodestatusnikah(short kodestatusnikah) {
		this.kodestatusnikah = kodestatusnikah;
	}

	public int getKodesukubangsa() {
		return this.kodesukubangsa;
	}

	public void setKodesukubangsa(int kodesukubangsa) {
		this.kodesukubangsa = kodesukubangsa;
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

	public String getNamabelakang() {
		return this.namabelakang;
	}

	public void setNamabelakang(String namabelakang) {
		this.namabelakang = namabelakang;
	}

	public String getNamadepan() {
		return this.namadepan;
	}

	public void setNamadepan(String namadepan) {
		this.namadepan = namadepan;
	}

	public String getNamapanggilan() {
		return this.namapanggilan;
	}

	public void setNamapanggilan(String namapanggilan) {
		this.namapanggilan = namapanggilan;
	}

	public String getNamatengah() {
		return this.namatengah;
	}

	public void setNamatengah(String namatengah) {
		this.namatengah = namatengah;
	}

	public Short getRow_status() {
		return this.row_status;
	}

	public void setRow_status(Short row_status) {
		this.row_status = row_status;
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

	public Timestamp getTglapprove() {
		return this.tglapprove;
	}

	public void setTglapprove(Timestamp tglapprove) {
		this.tglapprove = tglapprove;
	}

	public Date getTgllahir() {
		return this.tgllahir;
	}

	public void setTgllahir(Date tgllahir) {
		this.tgllahir = tgllahir;
	}

	public Date getTmtmasuk() {
		return this.tmtmasuk;
	}

	public void setTmtmasuk(Date tmtmasuk) {
		this.tmtmasuk = tmtmasuk;
	}

	public String getWebsite() {
		return this.website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public Pegawai getPegawai() {
		return this.pegawai;
	}

	public void setPegawai(Pegawai pegawai) {
		this.pegawai = pegawai;
	}

}