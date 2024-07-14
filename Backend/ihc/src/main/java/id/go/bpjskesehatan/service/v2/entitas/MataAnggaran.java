package id.go.bpjskesehatan.service.v2.entitas;

public class MataAnggaran {
	private Integer no;
	private Integer kode;
	private Program program;
	private Akun akun;
	private Integer deleted;
	
	public Integer getNo() {
		return no;
	}
	public void setNo(Integer no) {
		this.no = no;
	}
	public Integer getKode() {
		return kode;
	}
	public void setKode(Integer kode) {
		this.kode = kode;
	}
	public Program getProgram() {
		return program;
	}
	public void setProgram(Program program) {
		this.program = program;
	}
	public Akun getAkun() {
		return akun;
	}
	public void setAkun(Akun akun) {
		this.akun = akun;
	}
	public Integer getDeleted() {
		return deleted;
	}
	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}
}
