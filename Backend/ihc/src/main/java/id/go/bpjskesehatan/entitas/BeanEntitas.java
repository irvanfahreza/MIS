package id.go.bpjskesehatan.entitas;

import java.io.Serializable;

public class BeanEntitas implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// /**
	// *
	// */
	// private static final long serialVersionUID = -7207320031463032453L;
//	private int id;
	private String kode;
	private String nama;

	public String getKode() {
		return kode;
	}

	public void setKode(String kode) {
		this.kode = kode;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

//	public int getId() {
//		return id;
//	}
//
//	public void setId(int id) {
//		this.id = id;
//	}

}
