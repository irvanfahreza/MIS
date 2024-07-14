package id.go.bpjskesehatan.entitas.karyawan;

import java.io.Serializable;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * The persistent class for the LAMPIRANPENUGASAN database table.
 * 
 */
@JsonInclude(Include.NON_NULL)
@Entity
@NamedQuery(name = "Lampiranpenugasan.findAll", query = "SELECT l FROM Lampiranpenugasan l")
public class Lampiranpenugasan implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int kode;

	private String ekstensi;

	@Lob
	private byte[] lampiran;

	// bi-directional many-to-one association to Penugasan
	@JsonProperty("penugasan")
	@ManyToOne
	@JoinColumn(name = "KODEPENUGASAN")
	private Penugasan penugasan;

	public Lampiranpenugasan() {
	}

	public int getKode() {
		return this.kode;
	}

	public void setKode(int kode) {
		this.kode = kode;
	}

	public String getEkstensi() {
		return ekstensi;
	}

	public void setEkstensi(String ekstensi) {
		this.ekstensi = ekstensi;
	}

	public byte[] getLampiran() {
		return this.lampiran;
	}

	public void setLampiran(byte[] lampiran) {
		this.lampiran = lampiran;
	}

	public Penugasan getPenugasan() {
		return this.penugasan;
	}

	public void setPenugasan(Penugasan penugasan) {
		this.penugasan = penugasan;
	}

}