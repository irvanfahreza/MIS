package id.go.bpjskesehatan.entitas.kinerja;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import id.go.bpjskesehatan.service.v2.kinerja.entitas.CreatingFutureLeaderHeader;
import id.go.bpjskesehatan.service.v2.kinerja.entitas.HasilKerjaHeader;
import id.go.bpjskesehatan.service.v2.kinerja.entitas.InovasiHeader;
import id.go.bpjskesehatan.service.v2.kinerja.entitas.KejadianKritisNegatifHeader;
import id.go.bpjskesehatan.service.v2.kinerja.entitas.KomitmenHeader;
import id.go.bpjskesehatan.service.v2.kinerja.entitas.PublikasiKaryaIlmiahHeader;

/**
 * The persistent class for the KOMPONEN database table.
 * 
 */
@JsonInclude(Include.NON_NULL)
@Entity
@NamedQuery(name = "Komponen.findAll", query = "SELECT k FROM Komponen k")
public class Komponen implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Integer kode;

	@Column(name = "CREATED_BY")
	private Integer created_by;

	@Column(name = "CREATED_TIME")
	private Timestamp created_time;

	@Column(name = "LASTMODIFIED_BY")
	private Integer lastmodified_by;

	@Column(name = "LASTMODIFIED_TIME")
	private Timestamp lastmodified_time;

	private String nama;

	@Column(name = "ROW_STATUS")
	private Short row_status;

	// bi-directional many-to-one association to Bobotkomponen
	@OneToMany(mappedBy = "komponen")
	private List<Bobotkomponen> bobotkomponens;
	
	private HasilKerjaHeader hasilkerja;
	private KomitmenHeader komitmen;
	private KejadianKritisNegatifHeader kejadiankritisnegatif;
	private InovasiHeader inovasi;
	private CreatingFutureLeaderHeader creatingfutureleader;
	private PublikasiKaryaIlmiahHeader publikasikaryailmiah;

	public Komponen() {
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

	public List<Bobotkomponen> getBobotkomponens() {
		return this.bobotkomponens;
	}

	public void setBobotkomponens(List<Bobotkomponen> bobotkomponens) {
		this.bobotkomponens = bobotkomponens;
	}

	public Bobotkomponen addBobotkomponen(Bobotkomponen bobotkomponen) {
		getBobotkomponens().add(bobotkomponen);
		bobotkomponen.setKomponen(this);

		return bobotkomponen;
	}

	public Bobotkomponen removeBobotkomponen(Bobotkomponen bobotkomponen) {
		getBobotkomponens().remove(bobotkomponen);
		bobotkomponen.setKomponen(null);

		return bobotkomponen;
	}

	public HasilKerjaHeader getHasilkerja() {
		return hasilkerja;
	}

	public void setHasilkerja(HasilKerjaHeader hasilkerja) {
		this.hasilkerja = hasilkerja;
	}

	public KomitmenHeader getKomitmen() {
		return komitmen;
	}

	public void setKomitmen(KomitmenHeader komitmen) {
		this.komitmen = komitmen;
	}

	public KejadianKritisNegatifHeader getKejadiankritisnegatif() {
		return kejadiankritisnegatif;
	}

	public void setKejadiankritisnegatif(KejadianKritisNegatifHeader kejadiankritisnegatif) {
		this.kejadiankritisnegatif = kejadiankritisnegatif;
	}

	public InovasiHeader getInovasi() {
		return inovasi;
	}

	public void setInovasi(InovasiHeader inovasi) {
		this.inovasi = inovasi;
	}

	public CreatingFutureLeaderHeader getCreatingfutureleader() {
		return creatingfutureleader;
	}

	public void setCreatingfutureleader(CreatingFutureLeaderHeader creatingfutureleader) {
		this.creatingfutureleader = creatingfutureleader;
	}

	public PublikasiKaryaIlmiahHeader getPublikasikaryailmiah() {
		return publikasikaryailmiah;
	}

	public void setPublikasikaryailmiah(PublikasiKaryaIlmiahHeader publikasikaryailmiah) {
		this.publikasikaryailmiah = publikasikaryailmiah;
	}

}