package id.go.bpjskesehatan.service.v2.skpd.entitas;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonProperty;

import id.go.bpjskesehatan.entitas.karyawan.Penugasan;

public class Skpdpegawai {
	private Integer kode;
	
	@Column(name = "kodeskpd")
	@JsonProperty("skpd")
	private Skpd skpd;
	
	@Column(name = "kodepenugasan")
	@JsonProperty("penugasan")
	private Penugasan penugasan;

	public Integer getKode() {
		return kode;
	}

	public void setKode(Integer kode) {
		this.kode = kode;
	}

	public Skpd getSkpd() {
		return skpd;
	}

	public void setSkpd(Skpd skpd) {
		this.skpd = skpd;
	}

	public Penugasan getPenugasan() {
		return penugasan;
	}

	public void setPenugasan(Penugasan penugasan) {
		this.penugasan = penugasan;
	}
}
