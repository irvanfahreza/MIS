package id.go.MIS.inspired.manager;

import java.util.List;

import id.go.MIS.inspired.common.InspiredException;
import id.go.MIS.inspired.model.klaim_lob;

public interface MISManager {
	public boolean integrasiDataLOB() throws InspiredException;
	public List<klaim_lob> listLOB() throws InspiredException;
}
