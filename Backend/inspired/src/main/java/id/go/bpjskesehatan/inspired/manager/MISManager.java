package id.go.bpjskesehatan.inspired.manager;

import java.util.List;

import id.go.bpjskesehatan.inspired.common.InspiredException;
import id.go.bpjskesehatan.inspired.model.klaim_lob;

public interface MISManager {
	public boolean integrasiDataLOB() throws InspiredException;
	public List<klaim_lob> listLOB() throws InspiredException;
}
