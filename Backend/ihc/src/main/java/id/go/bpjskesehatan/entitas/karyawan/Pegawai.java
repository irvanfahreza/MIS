package id.go.bpjskesehatan.entitas.karyawan;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import id.go.bpjskesehatan.Constant;
import id.go.bpjskesehatan.entitas.GenericEntitas;
import id.go.bpjskesehatan.entitas.hcis.MenuPegawai;

/**
 * The persistent class for the PEGAWAI database table.
 * 
 */

@JsonInclude(Include.NON_NULL)
@Entity
@NamedQuery(name = "Pegawai.findAll", query = "SELECT p FROM Pegawai p")
public class Pegawai implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String npp;

	@Column(name = "CREATED_BY")
	private Integer created_by;

	@Column(name = "CREATED_TIME")
	private Timestamp created_time;

	private String email;

	private String email2;

	private String gelarbelakang;

	private String gelardepan;

	private String hobi;

	private String hp;

	private String hp2;

	@Column(name = "kewarganegaraan")
	@JsonProperty("kewarganegaraan")
	private GenericEntitas kewarganegaraan;

	private Integer kode;

	@Column(name = "kodeagama")
	@JsonProperty("agama")
	private GenericEntitas agama;

	@Column(name = "kodeasalpenerimaan")
	@JsonProperty("asalpenerimaan")
	private GenericEntitas asalpenerimaan;
	
	@Column(name = "kodedati2asal")
	@JsonProperty("kotaasal")
	private GenericEntitas kotaasal;
	
	@Column(name = "kodedati2asal2")
	@JsonProperty("kotaasal2")
	private GenericEntitas kotaasal2;
	
	private GenericEntitas propinsiasal;
	private GenericEntitas propinsiasal2;

	@Column(name = "kodejeniskelamin")
	@JsonProperty("jeniskelamin")
	private GenericEntitas jeniskelamin;

	@Column(name = "kodesalutasi")
	@JsonProperty("salutasi")
	private GenericEntitas salutasi;

	@Column(name = "kodestatuskaryawan")
	@JsonProperty("statuskaryawan")
	private GenericEntitas statuskaryawan;

	@Column(name = "kodestatusnikah")
	@JsonProperty("statusnikah")
	private GenericEntitas statusnikah;

	@Column(name = "kodesukubangsa")
	@JsonProperty("sukubangsa")
	private GenericEntitas sukubangsa;

	@Column(name = "LASTMODIFIED_BY")
	private Integer lastmodified_by;

	@Column(name = "LASTMODIFIED_TIME")
	private Timestamp lastmodified_time;

	private String namabelakang;

	private String namadepan;

	private String namapanggilan;

	private String namatengah;

	private String nama;

	@Column(name = "ROW_STATUS")
	private Short row_status;

	private String telprumah;

	private String tempatlahir;

	@Temporal(TemporalType.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constant.ServerTimezone)
	private Date tgllahir;

	@Temporal(TemporalType.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constant.ServerTimezone)
	private Date tmtmasuk;

	private String website;

	@JsonProperty("penugasan")
	private Penugasan penugasan;

	// bi-directional many-to-one association to Detailalamat
	@OneToMany(mappedBy = "pegawai")
	private List<Detailalamat> detailalamats;

	// bi-directional many-to-one association to Detailalamat2
	@OneToMany(mappedBy = "pegawai")
	private List<Detailalamat2> detailalamat2s;

	// bi-directional many-to-one association to Detailalamat2hist
	@OneToMany(mappedBy = "pegawai")
	private List<Detailalamat2hist> detailalamat2hists;

	// bi-directional many-to-one association to Detailalamathist
	@OneToMany(mappedBy = "pegawai")
	private List<Detailalamathist> detailalamathists;

	// bi-directional many-to-one association to Infodarurat
	@OneToMany(mappedBy = "pegawai")
	private List<Infodarurat> infodarurats;

	// bi-directional many-to-one association to Infodarurathist
	@OneToMany(mappedBy = "pegawai")
	private List<Infodarurathist> infodarurathists;

	// bi-directional many-to-one association to Pegawaihist
	@OneToMany(mappedBy = "pegawai")
	private List<Pegawaihist> pegawaihists;

	// bi-directional many-to-one association to Penugasan
	@JsonProperty("listpenugasan")
	@OneToMany(mappedBy = "pegawai")
	private List<Penugasan> penugasans;
	
	private Integer install;
	private Integer install_step;
	
	private Integer ikutzakat;
	private Integer anggotakoperasi;
	
	private List<MenuPegawai> menus;
	
	public GenericEntitas getPropinsiasal() {
		return propinsiasal;
	}

	public void setPropinsiasal(GenericEntitas propinsiasal) {
		this.propinsiasal = propinsiasal;
	}

	public GenericEntitas getPropinsiasal2() {
		return propinsiasal2;
	}

	public void setPropinsiasal2(GenericEntitas propinsiasal2) {
		this.propinsiasal2 = propinsiasal2;
	}

	public GenericEntitas getKotaasal() {
		return kotaasal;
	}

	public void setKotaasal(GenericEntitas kotaasal) {
		this.kotaasal = kotaasal;
	}

	public GenericEntitas getKotaasal2() {
		return kotaasal2;
	}

	public void setKotaasal2(GenericEntitas kotaasal2) {
		this.kotaasal2 = kotaasal2;
	}

	public Integer getInstall() {
		return install;
	}

	public void setInstall(Integer install) {
		this.install = install;
	}

	public Pegawai() {
	}

	public String getNpp() {
		return this.npp;
	}

	public void setNpp(String npp) {
		this.npp = npp;
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

	public GenericEntitas getKewarganegaraan() {
		return this.kewarganegaraan;
	}

	public void setKewarganegaraan(GenericEntitas kewarganegaraan) {
		this.kewarganegaraan = kewarganegaraan;
	}

	public Integer getKode() {
		return this.kode;
	}

	public void setKode(Integer kode) {
		this.kode = kode;
	}

	public GenericEntitas getAgama() {
		return this.agama;
	}

	public void setAgama(GenericEntitas agama) {
		this.agama = agama;
	}

	public GenericEntitas getAsalpenerimaan() {
		return this.asalpenerimaan;
	}

	public void setAsalpenerimaan(GenericEntitas asalpenerimaan) {
		this.asalpenerimaan = asalpenerimaan;
	}

	public GenericEntitas getJeniskelamin() {
		return this.jeniskelamin;
	}

	public void setJeniskelamin(GenericEntitas jeniskelamin) {
		this.jeniskelamin = jeniskelamin;
	}

	public GenericEntitas getSalutasi() {
		return this.salutasi;
	}

	public void setSalutasi(GenericEntitas salutasi) {
		this.salutasi = salutasi;
	}

	public GenericEntitas getStatuskaryawan() {
		return this.statuskaryawan;
	}

	public void setStatuskaryawan(GenericEntitas statuskaryawan) {
		this.statuskaryawan = statuskaryawan;
	}

	public GenericEntitas getStatusnikah() {
		return this.statusnikah;
	}

	public void setStatusnikah(GenericEntitas statusnikah) {
		this.statusnikah = statusnikah;
	}

	public GenericEntitas getSukubangsa() {
		return this.sukubangsa;
	}

	public void setSukubangsa(GenericEntitas sukubangsa) {
		this.sukubangsa = sukubangsa;
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

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
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

	public Penugasan getPenugasan() {
		return penugasan;
	}

	public void setPenugasan(Penugasan penugasan) {
		this.penugasan = penugasan;
	}

	public List<Detailalamat> getDetailalamats() {
		return this.detailalamats;
	}

	public void setDetailalamats(List<Detailalamat> detailalamats) {
		this.detailalamats = detailalamats;
	}

	public Detailalamat addDetailalamat(Detailalamat detailalamat) {
		getDetailalamats().add(detailalamat);
		detailalamat.setPegawai(this);

		return detailalamat;
	}

	public Detailalamat removeDetailalamat(Detailalamat detailalamat) {
		getDetailalamats().remove(detailalamat);
		detailalamat.setPegawai(null);

		return detailalamat;
	}

	public List<Detailalamat2> getDetailalamat2s() {
		return this.detailalamat2s;
	}

	public void setDetailalamat2s(List<Detailalamat2> detailalamat2s) {
		this.detailalamat2s = detailalamat2s;
	}

	public Detailalamat2 addDetailalamat2(Detailalamat2 detailalamat2) {
		getDetailalamat2s().add(detailalamat2);
		detailalamat2.setPegawai(this);

		return detailalamat2;
	}

	public Detailalamat2 removeDetailalamat2(Detailalamat2 detailalamat2) {
		getDetailalamat2s().remove(detailalamat2);
		detailalamat2.setPegawai(null);

		return detailalamat2;
	}

	public List<Detailalamat2hist> getDetailalamat2hists() {
		return this.detailalamat2hists;
	}

	public void setDetailalamat2hists(List<Detailalamat2hist> detailalamat2hists) {
		this.detailalamat2hists = detailalamat2hists;
	}

	public Detailalamat2hist addDetailalamat2hist(Detailalamat2hist detailalamat2hist) {
		getDetailalamat2hists().add(detailalamat2hist);
		detailalamat2hist.setPegawai(this);

		return detailalamat2hist;
	}

	public Detailalamat2hist removeDetailalamat2hist(Detailalamat2hist detailalamat2hist) {
		getDetailalamat2hists().remove(detailalamat2hist);
		detailalamat2hist.setPegawai(null);

		return detailalamat2hist;
	}

	public List<Detailalamathist> getDetailalamathists() {
		return this.detailalamathists;
	}

	public void setDetailalamathists(List<Detailalamathist> detailalamathists) {
		this.detailalamathists = detailalamathists;
	}

	public Detailalamathist addDetailalamathist(Detailalamathist detailalamathist) {
		getDetailalamathists().add(detailalamathist);
		detailalamathist.setPegawai(this);

		return detailalamathist;
	}

	public Detailalamathist removeDetailalamathist(Detailalamathist detailalamathist) {
		getDetailalamathists().remove(detailalamathist);
		detailalamathist.setPegawai(null);

		return detailalamathist;
	}

	public List<Infodarurat> getInfodarurats() {
		return this.infodarurats;
	}

	public void setInfodarurats(List<Infodarurat> infodarurats) {
		this.infodarurats = infodarurats;
	}

	public Infodarurat addInfodarurat(Infodarurat infodarurat) {
		getInfodarurats().add(infodarurat);
		infodarurat.setPegawai(this);

		return infodarurat;
	}

	public Infodarurat removeInfodarurat(Infodarurat infodarurat) {
		getInfodarurats().remove(infodarurat);
		infodarurat.setPegawai(null);

		return infodarurat;
	}

	public List<Infodarurathist> getInfodarurathists() {
		return this.infodarurathists;
	}

	public void setInfodarurathists(List<Infodarurathist> infodarurathists) {
		this.infodarurathists = infodarurathists;
	}

	public Infodarurathist addInfodarurathist(Infodarurathist infodarurathist) {
		getInfodarurathists().add(infodarurathist);
		infodarurathist.setPegawai(this);

		return infodarurathist;
	}

	public Infodarurathist removeInfodarurathist(Infodarurathist infodarurathist) {
		getInfodarurathists().remove(infodarurathist);
		infodarurathist.setPegawai(null);

		return infodarurathist;
	}

	public List<Pegawaihist> getPegawaihists() {
		return this.pegawaihists;
	}

	public void setPegawaihists(List<Pegawaihist> pegawaihists) {
		this.pegawaihists = pegawaihists;
	}

	public Pegawaihist addPegawaihist(Pegawaihist pegawaihist) {
		getPegawaihists().add(pegawaihist);
		pegawaihist.setPegawai(this);

		return pegawaihist;
	}

	public Pegawaihist removePegawaihist(Pegawaihist pegawaihist) {
		getPegawaihists().remove(pegawaihist);
		pegawaihist.setPegawai(null);

		return pegawaihist;
	}

	public List<Penugasan> getPenugasans() {
		return this.penugasans;
	}

	public void setPenugasans(List<Penugasan> penugasans) {
		this.penugasans = penugasans;
	}

	public Penugasan addPenugasan(Penugasan penugasan) {
		getPenugasans().add(penugasan);

		return penugasan;
	}

	public Penugasan removePenugasan(Penugasan penugasan) {
		getPenugasans().remove(penugasan);

		return penugasan;
	}

	public Integer getInstall_step() {
		return install_step;
	}

	public void setInstall_step(Integer install_step) {
		this.install_step = install_step;
	}

	public Integer getIkutzakat() {
		return ikutzakat;
	}

	public void setIkutzakat(Integer ikutzakat) {
		this.ikutzakat = ikutzakat;
	}

	public Integer getAnggotakoperasi() {
		return anggotakoperasi;
	}

	public void setAnggotakoperasi(Integer anggotakoperasi) {
		this.anggotakoperasi = anggotakoperasi;
	}

	public List<MenuPegawai> getMenus() {
		return menus;
	}

	public void setMenus(List<MenuPegawai> menus) {
		this.menus = menus;
	}

}