package id.go.bpjskesehatan.entitas.cuti;

import java.io.Serializable;
import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import id.go.bpjskesehatan.Constant;

@JsonInclude(Include.NON_NULL)
public class Harilibur implements Serializable {
	private static final long serialVersionUID = 1L;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constant.ServerTimezone)
	private Date Tanggal;
	private String Tgl;
	private String Keterangan;
	
	public Date getTanggal() {
		return Tanggal;
	}
	public void setTanggal(Date tanggal) {
		Tanggal = tanggal;
	}
	public String getKeterangan() {
		return Keterangan;
	}
	public void setKeterangan(String keterangan) {
		Keterangan = keterangan;
	}
	public String getTgl() {
		return Tgl;
	}
	public void setTgl(String tgl) {
		Tgl = tgl;
	}
}