package id.go.bpjskesehatan.entitas.karyawan;

import id.go.bpjskesehatan.entitas.GenericEntitas;

import java.io.Serializable;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * The persistent class for the Tandatangan database table.
 * 
 */
@Entity
@JsonInclude(Include.NON_NULL)
@NamedQuery(name = "Tandatangan.findAll", query = "SELECT l FROM Tandatangan l")
public class Tandatangan implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int kode;

	private String ekstensi;

	@Lob
	private byte[] lampiran;

	// bi-directional many-to-one association to Penugasan
	@ManyToOne
	@JoinColumn(name = "npp")
	private GenericEntitas pegawai;

	public Tandatangan() {
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

	public GenericEntitas getPegawai() {
		return pegawai;
	}

	public void setPegawai(GenericEntitas pegawai) {
		this.pegawai = pegawai;
	}

}