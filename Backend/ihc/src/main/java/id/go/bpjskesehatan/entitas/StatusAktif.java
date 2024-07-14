package id.go.bpjskesehatan.entitas;

import java.io.Serializable;

public class StatusAktif implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int kodestatus;
	private String namastatus;

	public int getKodestatus() {
		return kodestatus;
	}

	public void setKodestatus(int kodestatus) {
		this.kodestatus = kodestatus;
	}

	public String getNamastatus() {
		return namastatus;
	}

	public void setNamastatus(String namastatus) {
		this.namastatus = namastatus;
	}
}
