package id.go.bpjskesehatan.entitas.kompetensi;

import java.io.Serializable;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * The persistent class for the DETAILMODELKOMPETENSIJOBTITLE database table.
 * 
 */
@JsonInclude(Include.NON_NULL)
@Entity
@NamedQuery(name = "Detailmodelkompetensijobtitle.findAll", query = "SELECT d FROM Detailmodelkompetensijobtitle d")
public class Listmodelkompetensijobtitle implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int kode;

	@Column(name = "ROW_STATUS")
	private Short row_status;

	// bi-directional many-to-one association to Kamuskompetensi
	@ManyToOne
	@JoinColumn(name = "KODEKAMUSKOMPETENSI")
	private Kamuskompetensi kamuskompetensi;

	// bi-directional many-to-one association to Modelkompetensijobtitle
	@ManyToOne
	@JoinColumn(name = "KODEMODELKOMPETENSIJOBTITLE")
	private Modelkompetensijobtitle modelkompetensijobtitle;

	private Integer jumlahkompetensi;

	public Listmodelkompetensijobtitle() {
	}

	public int getKode() {
		return this.kode;
	}

	public void setKode(int kode) {
		this.kode = kode;
	}

	public Short getRow_status() {
		return this.row_status;
	}

	public void setRow_status(Short row_status) {
		this.row_status = row_status;
	}

	public Kamuskompetensi getKamuskompetensi() {
		return this.kamuskompetensi;
	}

	public void setKamuskompetensi(Kamuskompetensi kamuskompetensi) {
		this.kamuskompetensi = kamuskompetensi;
	}

	public Modelkompetensijobtitle getModelkompetensijobtitle() {
		return this.modelkompetensijobtitle;
	}

	public void setModelkompetensijobtitle(Modelkompetensijobtitle modelkompetensijobtitle) {
		this.modelkompetensijobtitle = modelkompetensijobtitle;
	}

	public Integer getJumlahkompetensi() {
		return jumlahkompetensi;
	}

	public void setJumlahkompetensi(Integer jumlahkompetensi) {
		this.jumlahkompetensi = jumlahkompetensi;
	}

}