package id.go.bpjskesehatan.entitas.cuti;

import java.util.ArrayList;

public class SaveCutis {
	
	private String kodeoffice;
	private String unitverif;
	private String actverif;
	private Integer useract;
	private ArrayList<SaveCuti> cuti = new ArrayList<>();

	
	public String getKodeoffice() {
		return kodeoffice;
	}

	public void setKodeoffice(String kodeoffice) {
		this.kodeoffice = kodeoffice;
	}

	public ArrayList<SaveCuti> getCuti() {
		return cuti;
	}

	public void setCuti(ArrayList<SaveCuti> cuti) {
		this.cuti = cuti;
	}

	public String getActverif() {
		return actverif;
	}

	public void setActverif(String actverif) {
		this.actverif = actverif;
	}

	public Integer getUseract() {
		return useract;
	}

	public void setUseract(Integer useract) {
		this.useract = useract;
	}

	public String getUnitverif() {
		return unitverif;
	}

	public void setUnitverif(String unitverif) {
		this.unitverif = unitverif;
	}
}
