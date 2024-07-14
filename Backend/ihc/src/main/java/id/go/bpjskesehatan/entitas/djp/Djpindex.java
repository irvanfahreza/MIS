package id.go.bpjskesehatan.entitas.djp;

import java.io.Serializable;

public class Djpindex implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String kode;
	private Integer kodemodelkompetensijobtitle;
	private String kodejobtitle;
	private String namajobtitle;
	private Short row_status;
	private Integer kodeperiodekompetensi;
	private String namaperiodekompetensi;

	public String getKode() {
		return kode;
	}

	public void setKode(String kode) {
		this.kode = kode;
	}

	public Integer getKodemodelkompetensijobtitle() {
		return kodemodelkompetensijobtitle;
	}

	public void setKodemodelkompetensijobtitle(Integer kodemodelkompetensijobtitle) {
		this.kodemodelkompetensijobtitle = kodemodelkompetensijobtitle;
	}

	public String getKodejobtitle() {
		return kodejobtitle;
	}

	public void setKodejobtitle(String kodejobtitle) {
		this.kodejobtitle = kodejobtitle;
	}

	public String getNamajobtitle() {
		return namajobtitle;
	}

	public void setNamajobtitle(String namajobtitle) {
		this.namajobtitle = namajobtitle;
	}

	public Short getRow_status() {
		return row_status;
	}

	public void setRow_status(Short row_status) {
		this.row_status = row_status;
	}

	public Integer getKodeperiodekompetensi() {
		return kodeperiodekompetensi;
	}

	public void setKodeperiodekompetensi(Integer kodeperiodekompetensi) {
		this.kodeperiodekompetensi = kodeperiodekompetensi;
	}

	public String getNamaperiodekompetensi() {
		return namaperiodekompetensi;
	}

	public void setNamaperiodekompetensi(String namaperiodekompetensi) {
		this.namaperiodekompetensi = namaperiodekompetensi;
	}

}
