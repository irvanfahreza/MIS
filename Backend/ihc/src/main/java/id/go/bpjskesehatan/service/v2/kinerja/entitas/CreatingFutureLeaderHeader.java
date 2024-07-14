package id.go.bpjskesehatan.service.v2.kinerja.entitas;

import java.util.List;

public class CreatingFutureLeaderHeader {
	private CreatingFutureLeader creatingfutureleader;
	private List<SettingLockKriteriaKpi> kriterias;
	private CreatingFutureLeaderVerifikasi creatingfutureleaderverifikasi;

	public List<SettingLockKriteriaKpi> getKriterias() {
		return kriterias;
	}

	public void setKriterias(List<SettingLockKriteriaKpi> kriterias) {
		this.kriterias = kriterias;
	}

	public CreatingFutureLeader getCreatingfutureleader() {
		return creatingfutureleader;
	}

	public void setCreatingfutureleader(CreatingFutureLeader creatingfutureleader) {
		this.creatingfutureleader = creatingfutureleader;
	}

	public CreatingFutureLeaderVerifikasi getCreatingfutureleaderverifikasi() {
		return creatingfutureleaderverifikasi;
	}

	public void setCreatingfutureleaderverifikasi(CreatingFutureLeaderVerifikasi creatingfutureleaderverifikasi) {
		this.creatingfutureleaderverifikasi = creatingfutureleaderverifikasi;
	}

}