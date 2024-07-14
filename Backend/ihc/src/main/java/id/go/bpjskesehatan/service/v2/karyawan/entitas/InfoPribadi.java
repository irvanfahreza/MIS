package id.go.bpjskesehatan.service.v2.karyawan.entitas;

public class InfoPribadi {
	private Pegawai pegawai;
	private DetilAlamat alamatsaatini;
	private DetilAlamat alamatktp;
	
	public Pegawai getPegawai() {
		return pegawai;
	}
	public void setPegawai(Pegawai pegawai) {
		this.pegawai = pegawai;
	}
	public DetilAlamat getAlamatsaatini() {
		return alamatsaatini;
	}
	public void setAlamatsaatini(DetilAlamat alamatsaatini) {
		this.alamatsaatini = alamatsaatini;
	}
	public DetilAlamat getAlamatktp() {
		return alamatktp;
	}
	public void setAlamatktp(DetilAlamat alamatktp) {
		this.alamatktp = alamatktp;
	}
}
