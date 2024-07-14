package id.go.bpjskesehatan.service.v2.cuti.entitas;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import id.go.bpjskesehatan.Constant;

public class Kuota {
	private Integer sisa;
	private Integer kuota;
	private Integer terpakai;
	private String tahun7;
	private String tahun8;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constant.ServerTimezone)
	private Date tmtmasuk;
	private String terakhiraju;
	private Integer jmlcutidiluartanggungan;
	
	public Integer getKuota() {
		return kuota;
	}
	public void setKuota(Integer kuota) {
		this.kuota = kuota;
	}
	public Integer getTerpakai() {
		return terpakai;
	}
	public void setTerpakai(Integer terpakai) {
		this.terpakai = terpakai;
	}
	public String getTahun7() {
		return tahun7;
	}
	public void setTahun7(String tahun7) {
		this.tahun7 = tahun7;
	}
	public String getTahun8() {
		return tahun8;
	}
	public void setTahun8(String tahun8) {
		this.tahun8 = tahun8;
	}
	public Date getTmtmasuk() {
		return tmtmasuk;
	}
	public void setTmtmasuk(Date tmtmasuk) {
		this.tmtmasuk = tmtmasuk;
	}
	public String getTerakhiraju() {
		return terakhiraju;
	}
	public void setTerakhiraju(String terakhiraju) {
		this.terakhiraju = terakhiraju;
	}
	public Integer getJmlcutidiluartanggungan() {
		return jmlcutidiluartanggungan;
	}
	public void setJmlcutidiluartanggungan(Integer jmlcutidiluartanggungan) {
		this.jmlcutidiluartanggungan = jmlcutidiluartanggungan;
	}
	public Integer getSisa() {
		return sisa;
	}
	public void setSisa(Integer sisa) {
		this.sisa = sisa;
	}
}
