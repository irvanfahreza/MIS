package id.go.bpjskesehatan.entitas.kinerja;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the GRUPPENILAI database table.
 * 
 */
@JsonInclude(Include.NON_NULL)
@Entity
@NamedQuery(name="Gruppenilai.findAll", query="SELECT g FROM Gruppenilai g")
public class Gruppenilai implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Integer kode;

	@Column(name="CREATED_BY")
	private Integer created_by;

	@Column(name="CREATED_TIME")
	private Timestamp created_time;

	@Column(name="LASTMODIFIED_BY")
	private Integer lastmodified_by;

	@Column(name="LASTMODIFIED_TIME")
	private Timestamp lastmodified_time;

	private String nama;

	@Column(name="ROW_STATUS")
	private Short row_status;

	//bi-directional many-to-one association to Gruppenilaijobtitle
	@OneToMany(mappedBy="gruppenilai")
	private List<Gruppenilaijobtitle> gruppenilaijobtitles;

	//bi-directional many-to-one association to Gruppenilaikomposisi
	@OneToMany(mappedBy="gruppenilai")
	private List<Gruppenilaikomposisi> gruppenilaikomposisis;

	public Gruppenilai() {
	}

	public Integer getKode() {
		return this.kode;
	}

	public void setKode(int kode) {
		this.kode = kode;
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

	public Short getRow_status() {
		return this.row_status;
	}

	public void setRow_status(Short row_status) {
		this.row_status = row_status;
	}

	public List<Gruppenilaijobtitle> getGruppenilaijobtitles() {
		return this.gruppenilaijobtitles;
	}

	public void setGruppenilaijobtitles(List<Gruppenilaijobtitle> gruppenilaijobtitles) {
		this.gruppenilaijobtitles = gruppenilaijobtitles;
	}

	public Gruppenilaijobtitle addGruppenilaijobtitle(Gruppenilaijobtitle gruppenilaijobtitle) {
		getGruppenilaijobtitles().add(gruppenilaijobtitle);
		gruppenilaijobtitle.setGruppenilai(this);

		return gruppenilaijobtitle;
	}

	public Gruppenilaijobtitle removeGruppenilaijobtitle(Gruppenilaijobtitle gruppenilaijobtitle) {
		getGruppenilaijobtitles().remove(gruppenilaijobtitle);
		gruppenilaijobtitle.setGruppenilai(null);

		return gruppenilaijobtitle;
	}

	public List<Gruppenilaikomposisi> getGruppenilaikomposisis() {
		return this.gruppenilaikomposisis;
	}

	public void setGruppenilaikomposisis(List<Gruppenilaikomposisi> gruppenilaikomposisis) {
		this.gruppenilaikomposisis = gruppenilaikomposisis;
	}

	public Gruppenilaikomposisi addGruppenilaikomposisi(Gruppenilaikomposisi gruppenilaikomposisi) {
		getGruppenilaikomposisis().add(gruppenilaikomposisi);
		gruppenilaikomposisi.setGruppenilai(this);

		return gruppenilaikomposisi;
	}

	public Gruppenilaikomposisi removeGruppenilaikomposisi(Gruppenilaikomposisi gruppenilaikomposisi) {
		getGruppenilaikomposisis().remove(gruppenilaikomposisi);
		gruppenilaikomposisi.setGruppenilai(null);

		return gruppenilaikomposisi;
	}

}