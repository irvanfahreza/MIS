package id.go.bpjskesehatan.service.v2.kinerja;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;
import javax.servlet.ServletContext;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import id.go.bpjskesehatan.database.Koneksi;
import id.go.bpjskesehatan.entitas.karyawan.Penugasan;
import id.go.bpjskesehatan.entitas.kinerja.Komponen;
import id.go.bpjskesehatan.service.v2.karyawan.entitas.Pegawai;
import id.go.bpjskesehatan.service.v2.kinerja.entitas.CreatingFutureLeader;
import id.go.bpjskesehatan.service.v2.kinerja.entitas.CreatingFutureLeaderPegPromosi;
import id.go.bpjskesehatan.service.v2.kinerja.entitas.CreatingFutureLeaderVerifikasi;
import id.go.bpjskesehatan.service.v2.kinerja.entitas.EvaluasiKpi;
import id.go.bpjskesehatan.service.v2.kinerja.entitas.Inovasi;
import id.go.bpjskesehatan.service.v2.kinerja.entitas.InovasiVerifikasi;
import id.go.bpjskesehatan.service.v2.kinerja.entitas.KejadianKritisNegatif;
import id.go.bpjskesehatan.service.v2.kinerja.entitas.KejadianKritisNegatifVerifikasi;
import id.go.bpjskesehatan.service.v2.kinerja.entitas.PembinaanDetail;
import id.go.bpjskesehatan.service.v2.kinerja.entitas.PeriodeKinerjaDetail;
import id.go.bpjskesehatan.service.v2.kinerja.entitas.PublikasiKaryaIlmiah;
import id.go.bpjskesehatan.service.v2.kinerja.entitas.PublikasiKaryaIlmiahVerifikasi;
import id.go.bpjskesehatan.service.v2.kinerja.entitas.SettingLockHasilKerja;
import id.go.bpjskesehatan.service.v2.kinerja.entitas.SettingLockKpi;
import id.go.bpjskesehatan.service.v2.kinerja.entitas.SettingLockKriteriaKpi;
import id.go.bpjskesehatan.service.v2.kinerja.entitas.SettingLockRencanaAktifitas;
import id.go.bpjskesehatan.service.v2.kinerja.entitas.VerifikasiKpi;
import id.go.bpjskesehatan.util.MyException;
import id.go.bpjskesehatan.util.Utils;

public class KinerjaUtil {
	
	public static List<Komponen> getKomponenByPeriodeKinerja(Integer kodeperiodekinerja) throws SQLException, NamingException {
		Connection con = null;
		CallableStatement cs = null;
		String query = null;
		ResultSet rs = null;
		try {
			query = "exec kinerja.sp_listbobotkomponen 1,100,0,null,?";
			con = new Koneksi().getConnection();
			cs = con.prepareCall(query);
			cs.setString(1, String.format("kodeperiodekinerja = %d", kodeperiodekinerja));
			rs = cs.executeQuery();
			List<Komponen> rows = new ArrayList<>();
			while(rs.next()) {
				Komponen row = new Komponen();
				row.setKode(rs.getInt("kodekomponen"));
				row.setNama(rs.getString("namakomponen"));
				rows.add(row);
			}
			if(rows.size()>0) {
				return rows;
			}
		} 
		finally {
			if (rs != null) {
				 try {
					 rs.close();
				 } catch (SQLException e1) {
				 }
			}
			if (cs != null) {
				try {
					cs.close();
				} catch (SQLException e1) {
				}
			}

			if (con != null) {
				try {
					con.close();
				} catch (SQLException e1) {
				}
			}
		}
		return null;
	}
	
	public static List<Komponen> getKomponenByPeserta(Integer kodepeserta) throws SQLException, NamingException {
		Connection con = null;
		CallableStatement cs = null;
		String query = null;
		ResultSet rs = null;
		try {
			query = "select c.kode, c.nama \r\n" + 
					"from kinerja.bobotkomponen a \r\n" + 
					"inner join kinerja.periodekinerja b on a.kodeperiodekinerja=b.kode \r\n" + 
					"inner join kinerja.komponen c on a.kodekomponen=c.kode \r\n" + 
					"inner join kinerja.peserta d on b.kode=d.kodeperiodekinerja \r\n" + 
					"where d.kode=? order by kode";
			con = new Koneksi().getConnection();
			cs = con.prepareCall(query);
			cs.setInt(1, kodepeserta);
			rs = cs.executeQuery();
			List<Komponen> rows = new ArrayList<>();
			while(rs.next()) {
				Komponen row = new Komponen();
				row.setKode(rs.getInt("kode"));
				row.setNama(rs.getString("nama"));
				rows.add(row);
			}
			if(rows.size()>0) {
				return rows;
			}
		} 
		finally {
			if (rs != null) {
				 try {
					 rs.close();
				 } catch (SQLException e1) {
				 }
			}
			if (cs != null) {
				try {
					cs.close();
				} catch (SQLException e1) {
				}
			}

			if (con != null) {
				try {
					con.close();
				} catch (SQLException e1) {
				}
			}
		}
		return null;
	}
	
	public static List<PeriodeKinerjaDetail> getSiklusByPeriodeKinerja(Integer kodeperiodekinerja) throws SQLException, NamingException {
		Connection con = null;
		CallableStatement cs = null;
		String query = null;
		ResultSet rs = null;
		try {
			query = "exec kinerja.sp_listperiodekinerjadetail 1,100,0,null,?";
			con = new Koneksi().getConnection();
			cs = con.prepareCall(query);
			cs.setString(1, String.format("kodeperiodekinerja = %d", kodeperiodekinerja));
			rs = cs.executeQuery();
			List<PeriodeKinerjaDetail> rows = new ArrayList<>();
			while(rs.next()) {
				PeriodeKinerjaDetail row = new PeriodeKinerjaDetail();
				row.setKodesiklus(rs.getInt("kodesiklus"));
				row.setTglmulai(Utils.SqlDateToSqlString(rs.getDate("tglmulai")));
				row.setTglselesai(Utils.SqlDateToSqlString(rs.getDate("tglselesai")));
				rows.add(row);
			}
			if(rows.size()>0) {
				return rows;
			}
		} 
		finally {
			if (rs != null) {
				 try {
					 rs.close();
				 } catch (SQLException e1) {
				 }
			}
			if (cs != null) {
				try {
					cs.close();
				} catch (SQLException e1) {
				}
			}

			if (con != null) {
				try {
					con.close();
				} catch (SQLException e1) {
				}
			}
		}
		return null;
	}
	
	public static List<SettingLockHasilKerja> getSettingLockHasilKerja(Integer kodeperiodekerja, String kodejobtitle) throws SQLException, NamingException {
		Connection con = null;
		CallableStatement cs = null;
		String query = null;
		ResultSet rs = null;
		try {
			query = "exec [kinerja].[sp_listsettinglockhasilkerja] ?, ?";
			con = new Koneksi().getConnection();
			cs = con.prepareCall(query);
			cs.setInt(1, kodeperiodekerja);
			cs.setString(2, kodejobtitle);
			rs = cs.executeQuery();
			List<SettingLockHasilKerja> rows = new ArrayList<>();
			while(rs.next()) {
				SettingLockHasilKerja row = new SettingLockHasilKerja();
				row.setKode(rs.getInt("kode"));
				row.setKodetanggungjawab(rs.getInt("kodetanggungjawab"));
				row.setTanggungjawabutama(rs.getString("tanggungjawabutama"));
				row.setBobot(rs.getFloat("bobot"));
				row.setBobotlocked(rs.getInt("bobot_locked"));
				rows.add(row);
			}
			if(rows.size()>0) {
				return rows;
			}
		} 
		finally {
			if (rs != null) {
				 try {
					 rs.close();
				 } catch (SQLException e1) {
				 }
			}
			if (cs != null) {
				try {
					cs.close();
				} catch (SQLException e1) {
				}
			}

			if (con != null) {
				try {
					con.close();
				} catch (SQLException e1) {
				}
			}
		}
		return null;
	}
	
	public static List<SettingLockKpi> getSettingLockKpi(Integer kodetanggungjawab, Integer kodesettinglockhasilkerja) throws SQLException, NamingException {
		Connection con = null;
		CallableStatement cs = null;
		String query = null;
		ResultSet rs = null;
		try {
			query = "exec [kinerja].[sp_listsettinglockkpi] ?,?";
			con = new Koneksi().getConnection();
			cs = con.prepareCall(query);
			cs.setInt(1, kodetanggungjawab);
			cs.setInt(2, kodesettinglockhasilkerja);
			rs = cs.executeQuery();
			List<SettingLockKpi> rows = new ArrayList<>();
			while(rs.next()) {
				SettingLockKpi row = new SettingLockKpi();
				row.setKodedetailkpi(rs.getInt("kodedetailkpi"));
				row.setNama(rs.getString("nama"));
				row.setKode(rs.getInt("kode"));
				row.setTarget(rs.getString("target"));
				row.setUnitukuran(rs.getString("unitukuran"));
				row.setSumberdata(rs.getString("sumberdata"));
				row.setAsumsi(rs.getString("asumsi"));
				row.setBobot(rs.getFloat("bobot"));
				row.setTargetlocked(rs.getInt("target_locked"));
				row.setUnitukuranlocked(rs.getInt("unitukuran_locked"));
				row.setSumberdatalocked(rs.getInt("sumberdata_locked"));
				row.setAsumsilocked(rs.getInt("asumsi_locked"));
				row.setBobotlocked(rs.getInt("bobot_locked"));
				rows.add(row);
			}
			if(rows.size()>0) {
				return rows;
			}
		} 
		finally {
			if (rs != null) {
				 try {
					 rs.close();
				 } catch (SQLException e1) {
				 }
			}
			if (cs != null) {
				try {
					cs.close();
				} catch (SQLException e1) {
				}
			}

			if (con != null) {
				try {
					con.close();
				} catch (SQLException e1) {
				}
			}
		}
		return null;
	}
	
	public static List<SettingLockKriteriaKpi> getSettingLockKriteriaKpi(Integer kodeperiodekinerja, Integer kodesettinglockkpi) throws SQLException, NamingException {
		Connection con = null;
		CallableStatement cs = null;
		String query = null;
		ResultSet rs = null;
		try {
			query = "exec [kinerja].[sp_listsettinglockkriteriakpi] ?,?";
			con = new Koneksi().getConnection();
			cs = con.prepareCall(query);
			cs.setInt(1, kodeperiodekinerja);
			cs.setInt(2, kodesettinglockkpi);
			rs = cs.executeQuery();
			List<SettingLockKriteriaKpi> rows = new ArrayList<>();
			while(rs.next()) {
				SettingLockKriteriaKpi row = new SettingLockKriteriaKpi();
				row.setKodekriteria(rs.getInt("kodekriteria"));
				row.setDefinisi(rs.getString("definisi"));
				row.setRating(rs.getInt("rating"));
				row.setScore(rs.getInt("score"));
				row.setKode(rs.getInt("kode"));
				row.setDeskripsi(rs.getString("deskripsi"));
				row.setLocked(rs.getInt("locked"));
				rows.add(row);
			}
			if(rows.size()>0) {
				return rows;
			}
		} 
		finally {
			if (rs != null) {
				 try {
					 rs.close();
				 } catch (SQLException e1) {
				 }
			}
			if (cs != null) {
				try {
					cs.close();
				} catch (SQLException e1) {
				}
			}

			if (con != null) {
				try {
					con.close();
				} catch (SQLException e1) {
				}
			}
		}
		return null;
	}

	public static List<SettingLockHasilKerja> getPerencanaanHasilKerja(Integer kodepeserta) throws SQLException, NamingException {
		Connection con = null;
		CallableStatement cs = null;
		String query = null;
		ResultSet rs = null;
		try {
			query = "exec [kinerja].[sp_listperencanaantju] ?";
			con = new Koneksi().getConnection();
			cs = con.prepareCall(query);
			cs.setInt(1, kodepeserta);
			rs = cs.executeQuery();
			List<SettingLockHasilKerja> rows = new ArrayList<>();
			while(rs.next()) {
				SettingLockHasilKerja row = new SettingLockHasilKerja();
				row.setKode(rs.getInt("kode"));
				row.setKodetanggungjawab(rs.getInt("kodetanggungjawab"));
				row.setTanggungjawabutama(rs.getString("tanggungjawabutama"));
				row.setBobot(rs.getFloat("bobot"));
				row.setBobotlocked(rs.getInt("bobot_locked"));
				rows.add(row);
			}
			if(rows.size()>0) {
				return rows;
			}
		} 
		finally {
			if (rs != null) {
				 try {
					 rs.close();
				 } catch (SQLException e1) {
				 }
			}
			if (cs != null) {
				try {
					cs.close();
				} catch (SQLException e1) {
				}
			}

			if (con != null) {
				try {
					con.close();
				} catch (SQLException e1) {
				}
			}
		}
		return null;
	}
	
	public static List<SettingLockKpi> getPerencanaanKpi(Integer kodetanggungjawab, Integer kodehasilkerja, Integer kodepeserta) throws SQLException, NamingException {
		Connection con = null;
		CallableStatement cs = null;
		String query = null;
		ResultSet rs = null;
		try {
			query = "exec [kinerja].[sp_listperencanaankpi] ?,?,?";
			con = new Koneksi().getConnection();
			cs = con.prepareCall(query);
			cs.setInt(1, kodetanggungjawab);
			cs.setInt(2, kodehasilkerja);
			cs.setInt(3, kodepeserta);
			rs = cs.executeQuery();
			List<SettingLockKpi> rows = new ArrayList<>();
			while(rs.next()) {
				SettingLockKpi row = new SettingLockKpi();
				row.setKodedetailkpi(rs.getInt("kodedetailkpi"));
				row.setNama(rs.getString("namadetailkpi"));
				row.setKode(rs.getInt("kode"));
				row.setTarget(rs.getString("target"));
				row.setUnitukuran(rs.getString("unitukuran"));
				row.setSumberdata(rs.getString("sumberdata"));
				row.setAsumsi(rs.getString("asumsi"));
				row.setBobot(rs.getFloat("bobot"));
				row.setTargetlocked(rs.getInt("target_locked"));
				row.setUnitukuranlocked(rs.getInt("unitukuran_locked"));
				row.setSumberdatalocked(rs.getInt("sumberdata_locked"));
				row.setAsumsilocked(rs.getInt("asumsi_locked"));
				row.setBobotlocked(rs.getInt("bobot_locked"));
				row.setKodesettinglockkpi(rs.getInt("kodesettinglock_kpi"));
				rows.add(row);
			}
			if(rows.size()>0) {
				return rows;
			}
		} 
		finally {
			if (rs != null) {
				 try {
					 rs.close();
				 } catch (SQLException e1) {
				 }
			}
			if (cs != null) {
				try {
					cs.close();
				} catch (SQLException e1) {
				}
			}

			if (con != null) {
				try {
					con.close();
				} catch (SQLException e1) {
				}
			}
		}
		return null;
	}
	
	public static List<SettingLockKriteriaKpi> getPerencanaanKriteriaKpi(Integer kodepeserta, Integer kodekpi, Integer kodesettinglockkpi) throws SQLException, NamingException {
		Connection con = null;
		CallableStatement cs = null;
		String query = null;
		ResultSet rs = null;
		try {
			query = "exec [kinerja].[sp_listperencanaankriteriakpi] ?,?,?";
			con = new Koneksi().getConnection();
			cs = con.prepareCall(query);
			cs.setInt(1, kodepeserta);
			cs.setInt(2, kodekpi);
			cs.setInt(3, kodesettinglockkpi);
			rs = cs.executeQuery();
			List<SettingLockKriteriaKpi> rows = new ArrayList<>();
			while(rs.next()) {
				SettingLockKriteriaKpi row = new SettingLockKriteriaKpi();
				row.setKodekriteria(rs.getInt("kodekriteria"));
				row.setDefinisi(rs.getString("definisi"));
				row.setRating(rs.getInt("rating"));
				row.setScore(rs.getInt("score"));
				row.setKode(rs.getInt("kode"));
				row.setDeskripsi(rs.getString("deskripsi"));
				row.setLocked(rs.getInt("locked"));
				rows.add(row);
			}
			if(rows.size()>0) {
				return rows;
			}
		} 
		finally {
			if (rs != null) {
				 try {
					 rs.close();
				 } catch (SQLException e1) {
				 }
			}
			if (cs != null) {
				try {
					cs.close();
				} catch (SQLException e1) {
				}
			}

			if (con != null) {
				try {
					con.close();
				} catch (SQLException e1) {
				}
			}
		}
		return null;
	}
	
	public static List<SettingLockRencanaAktifitas> getPerencanaanRencanaAktifitas(Integer kodekpi, Integer kodedetailkpi) throws SQLException, NamingException {
		Connection con = null;
		CallableStatement cs = null;
		String query = null;
		ResultSet rs = null;
		try {
			query = "select kode, deskripsi, target, targettgl from kinerja.perencanaanhaskerkpirencanaaktivitas where kodeperencanaanhaskerkpi = ?";
			con = new Koneksi().getConnection();
			cs = con.prepareCall(query);
			cs.setInt(1, kodekpi);
			rs = cs.executeQuery();
			List<SettingLockRencanaAktifitas> rows = new ArrayList<>();
			while(rs.next()) {
				SettingLockRencanaAktifitas row = new SettingLockRencanaAktifitas();
				row.setKode(rs.getInt("kode"));
				row.setDeskripsi(rs.getString("deskripsi"));
				row.setTarget(rs.getString("target"));
				row.setTargettgl(rs.getDate("targettgl"));
				row.setKodedetailkpi(kodedetailkpi);
				rows.add(row);
			}
			if(rows.size()>0) {
				return rows;
			}
		} 
		finally {
			if (rs != null) {
				 try {
					 rs.close();
				 } catch (SQLException e1) {
				 }
			}
			if (cs != null) {
				try {
					cs.close();
				} catch (SQLException e1) {
				}
			}

			if (con != null) {
				try {
					con.close();
				} catch (SQLException e1) {
				}
			}
		}
		return null;
	}
	
	public static List<SettingLockRencanaAktifitas> getPembinaanRencanaAktifitas(Integer kodeperencanaanhaskerkpi, Integer kodedetailkpi) throws SQLException, NamingException {
		Connection con = null;
		CallableStatement cs = null;
		String query = null;
		ResultSet rs = null;
		try {
			query = "select \r\n" + 
					"a.kode, \r\n" + 
					"iif(b.kode is not null, b.aktivitas, a.deskripsi) as deskripsi, \r\n" + 
					"iif(b.kode is not null, b.[target], a.[target]) as [target], \r\n" + 
					"iif(b.kode is not null, b.targettgl, a.targettgl) as targettgl, \r\n" + 
					"b.identifikasi, \r\n" + 
					"b.analisa, \r\n" + 
					"b.solusi, \r\n" + 
					"b.hasil \r\n" + 
					"from kinerja.perencanaanhaskerkpirencanaaktivitas a \r\n" + 
					"left join kinerja.pembinaanrencanaaktivitas b on a.kode=b.kodeperencanaanhaskerkpirencanaaktivitas \r\n" + 
					"where a.kodeperencanaanhaskerkpi=?";
			con = new Koneksi().getConnection();
			cs = con.prepareCall(query);
			cs.setInt(1, kodeperencanaanhaskerkpi);
			rs = cs.executeQuery();
			List<SettingLockRencanaAktifitas> rows = new ArrayList<>();
			while(rs.next()) {
				SettingLockRencanaAktifitas row = new SettingLockRencanaAktifitas();
				row.setKode(rs.getInt("kode"));
				row.setDeskripsi(rs.getString("deskripsi"));
				row.setTarget(rs.getString("target"));
				row.setTargettgl(rs.getDate("targettgl"));
				row.setIdentifikasi(rs.getString("identifikasi"));
				row.setAnalisa(rs.getString("analisa"));
				row.setSolusi(rs.getString("solusi"));
				row.setHasil(rs.getString("hasil"));
				row.setKodedetailkpi(kodedetailkpi);
				rows.add(row);
			}
			if(rows.size()>0) {
				return rows;
			}
		} 
		finally {
			if (rs != null) {
				 try {
					 rs.close();
				 } catch (SQLException e1) {
				 }
			}
			if (cs != null) {
				try {
					cs.close();
				} catch (SQLException e1) {
				}
			}

			if (con != null) {
				try {
					con.close();
				} catch (SQLException e1) {
				}
			}
		}
		return null;
	}
	
	public static List<PembinaanDetail> getPembinaanDetail(Integer kodeperencanaanhaskerkpi) throws SQLException, NamingException {
		Connection con = null;
		CallableStatement cs = null;
		String query = null;
		ResultSet rs = null;
		try {
			query = "select kode, pencapaian, detil, lampiran, catatan, flag from kinerja.pembinaandetail where kodeperencanaanhaskerkpi=?";
			con = new Koneksi().getConnection();
			cs = con.prepareCall(query);
			cs.setInt(1, kodeperencanaanhaskerkpi);
			rs = cs.executeQuery();
			List<PembinaanDetail> rows = new ArrayList<>();
			while(rs.next()) {
				PembinaanDetail row = new PembinaanDetail();
				row.setKode(rs.getInt("kode"));
				row.setPencapaian(rs.getString("pencapaian"));
				row.setDetil(rs.getString("detil"));
				row.setLampiran(rs.getString("lampiran"));
				row.setCatatan(rs.getString("catatan"));
				row.setFlag(rs.getInt("flag"));
				rows.add(row);
			}
			if(rows.size()>0) {
				return rows;
			}
		} 
		finally {
			if (rs != null) {
				 try {
					 rs.close();
				 } catch (SQLException e1) {
				 }
			}
			if (cs != null) {
				try {
					cs.close();
				} catch (SQLException e1) {
				}
			}

			if (con != null) {
				try {
					con.close();
				} catch (SQLException e1) {
				}
			}
		}
		return null;
	}
	
	public static Integer getKodePesertaByKodePerencanaan(Integer kodeperencanaan) throws SQLException, NamingException {
		Connection con = null;
		CallableStatement cs = null;
		String query = null;
		ResultSet rs = null;
		try {
			query = "select top 1 kodepeserta from kinerja.perencanaan where kode=?";
			con = new Koneksi().getConnection();
			cs = con.prepareCall(query);
			cs.setInt(1, kodeperencanaan);
			rs = cs.executeQuery();
			if(rs.next()) {
				return rs.getInt("kodepeserta");
			}
		} 
		finally {
			if (rs != null) {
				 try {
					 rs.close();
				 } catch (SQLException e1) {
				 }
			}
			if (cs != null) {
				try {
					cs.close();
				} catch (SQLException e1) {
				}
			}

			if (con != null) {
				try {
					con.close();
				} catch (SQLException e1) {
				}
			}
		}
		return null;
	}
	
	public static List<Map<String, Object>> getListCatatanPerencanaan(Integer kodepeserta) throws SQLException, NamingException {
		Connection con = null;
		CallableStatement cs = null;
		String query = null;
		ResultSet rs = null;
		List<Map<String, Object>> list = new ArrayList<>();
		try {
			query = "select \r\n" + 
					"a.catatan, \r\n" + 
					"format(a.created_time, 'dd/MM/yyyy HH:mm:ss') as tanggal, \r\n" + 
					"d.nama \r\n" + 
					"from kinerja.perencanaancatatanapproval a \r\n" + 
					"inner join kinerja.perencanaan b on a.kodeperencanaan=b.kode \r\n" + 
					"inner join karyawan.penugasan c on a.kodepenugasan=c.kode \r\n" + 
					"inner join karyawan.vw_pegawai d on c.npp=d.npp \r\n" + 
					"where b.kodepeserta=? order by a.created_time";
			con = new Koneksi().getConnection();
			cs = con.prepareCall(query);
			cs.setInt(1, kodepeserta);
			rs = cs.executeQuery();
			while(rs.next()) {
				Map<String, Object> row = new HashMap<String, Object>();
				row.put("catatan", rs.getString("catatan"));
				row.put("tanggal", rs.getString("tanggal"));
				row.put("nama", rs.getString("nama"));
				list.add(row);
			}
			if(list.size()>0) {
				return list;
			}
		} 
		finally {
			if (rs != null) {
				 try {
					 rs.close();
				 } catch (SQLException e1) {
				 }
			}
			if (cs != null) {
				try {
					cs.close();
				} catch (SQLException e1) {
				}
			}

			if (con != null) {
				try {
					con.close();
				} catch (SQLException e1) {
				}
			}
		}
		return null;
	}
	
	public static Boolean simpanCatatanPerencanaan(Integer kodeperencanaan, Integer flag, String catatan, Integer kodepenugasan, Integer useract) throws SQLException, NamingException {
		Connection con = null;
		CallableStatement cs = null;
		String query = null;
		try {
			query = "exec kinerja.sp_perencanaan_approval ?,?,?,?,?";
			con = new Koneksi().getConnection();
			cs = con.prepareCall(query);
			cs.setInt(1, kodeperencanaan);
			cs.setInt(2, flag);
			cs.setString(3, catatan);
			cs.setInt(4, kodepenugasan);
			cs.setInt(5, useract);
			cs.execute();
			return true;
		} 
		finally {
			if (cs != null) {
				try {
					cs.close();
				} catch (SQLException e1) {
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e1) {
				}
			}
		}
	}
	
	public static List<Inovasi> getPembinaanInovasi(Integer kodeperencanaan) throws SQLException, NamingException {
		Connection con = null;
		CallableStatement cs = null;
		String query = null;
		ResultSet rs = null;
		try {
			query = "select a.kode, a.nama, a.rating, a.deskripsi, a.lampiran \r\n" + 
					"from kinerja.pembinaaninovasi a \r\n" + 
					"inner join kinerja.pembinaan b on a.kodepembinaan=b.kode \r\n" + 
					"where b.kodeperencanaan=?";
			con = new Koneksi().getConnection();
			cs = con.prepareCall(query);
			cs.setInt(1, kodeperencanaan);
			rs = cs.executeQuery();
			List<Inovasi> rows = new ArrayList<>();
			while(rs.next()) {
				Inovasi row = new Inovasi();
				row.setKode(rs.getInt("kode"));
				row.setNama(rs.getString("nama"));
				row.setRating(rs.getInt("rating"));
				row.setDeskripsi(rs.getString("deskripsi"));
				row.setLampiran(rs.getString("lampiran"));
				row.setShow(1);
				rows.add(row);
			}
			if(rows.size()>0) {
				return rows;
			}
		} 
		finally {
			if (rs != null) {
				 try {
					 rs.close();
				 } catch (SQLException e1) {
				 }
			}
			if (cs != null) {
				try {
					cs.close();
				} catch (SQLException e1) {
				}
			}

			if (con != null) {
				try {
					con.close();
				} catch (SQLException e1) {
				}
			}
		}
		return null;
	}
	
	public static List<Inovasi> getEvaluasiInovasi(Integer kodepembinaan) throws SQLException, NamingException {
		Connection con = null;
		CallableStatement cs = null;
		String query = null;
		ResultSet rs = null;
		try {
			query = "select a.kode, a.nama, a.rating, a.deskripsi, a.lampiran, c.rating as ratingevaluasi, c.catatan as catatanevaluasi, c.kode as kodeevaluasiinovasi \r\n" + 
					"from kinerja.pembinaaninovasi a \r\n" + 
					"inner join kinerja.pembinaan b on a.kodepembinaan=b.kode \r\n" + 
					"left join kinerja.evaluasiinovasi c on a.kode=c.kodepembinaaninovasi \r\n" + 
					"where b.kode=?";
			con = new Koneksi().getConnection();
			cs = con.prepareCall(query);
			cs.setInt(1, kodepembinaan);
			rs = cs.executeQuery();
			List<Inovasi> rows = new ArrayList<>();
			while(rs.next()) {
				Inovasi row = new Inovasi();
				row.setKode(rs.getInt("kode"));
				row.setNama(rs.getString("nama"));
				row.setRating(rs.getInt("rating"));
				row.setDeskripsi(rs.getString("deskripsi"));
				row.setLampiran(rs.getString("lampiran"));
				row.setRatingevaluasi(rs.getInt("ratingevaluasi"));
				row.setCatatanevaluasi(rs.getString("catatanevaluasi"));
				row.setKodeevaluasiinovasi(rs.getInt("kodeevaluasiinovasi"));
				row.setShow(1);
				rows.add(row);
			}
			if(rows.size()>0) {
				return rows;
			}
		} 
		finally {
			if (rs != null) {
				 try {
					 rs.close();
				 } catch (SQLException e1) {
				 }
			}
			if (cs != null) {
				try {
					cs.close();
				} catch (SQLException e1) {
				}
			}

			if (con != null) {
				try {
					con.close();
				} catch (SQLException e1) {
				}
			}
		}
		return null;
	}
	
	public static List<InovasiVerifikasi> getVerifikasiInovasi(Integer kodepembinaan) throws SQLException, NamingException {
		Connection con = null;
		CallableStatement cs = null;
		String query = null;
		ResultSet rs = null;
		try {
			query = "exec kinerja.sp_getverifikasiinovasi ?";
			con = new Koneksi().getConnection();
			cs = con.prepareCall(query);
			cs.setInt(1, kodepembinaan);
			rs = cs.executeQuery();
			List<InovasiVerifikasi> rows = new ArrayList<>();
			while(rs.next()) {
				InovasiVerifikasi row = new InovasiVerifikasi();
				row.setKode(rs.getInt("kode"));
				row.setNama(rs.getString("nama"));
				row.setRating(rs.getInt("rating"));
				row.setDeskripsi(rs.getString("deskripsi"));
				row.setCatatan(rs.getString("catatan"));
				rows.add(row);
			}
			if(rows.size()>0) {
				return rows;
			}
		} 
		finally {
			if (rs != null) {
				 try {
					 rs.close();
				 } catch (SQLException e1) {
				 }
			}
			if (cs != null) {
				try {
					cs.close();
				} catch (SQLException e1) {
				}
			}

			if (con != null) {
				try {
					con.close();
				} catch (SQLException e1) {
				}
			}
		}
		return null;
	}
	
	public static List<PublikasiKaryaIlmiah> getPembinaanPublikasiKaryaIlmiah(Integer kodeperencanaan) throws SQLException, NamingException {
		Connection con = null;
		CallableStatement cs = null;
		String query = null;
		ResultSet rs = null;
		try {
			query = "select a.kode, a.judul, a.publisher, a.rating, a.keterangan, a.lampiran \r\n" + 
					"from kinerja.pembinaanpublikasikaryailmiah a \r\n" + 
					"inner join kinerja.pembinaan b on a.kodepembinaan=b.kode \r\n" + 
					"where b.kodeperencanaan=?";
			con = new Koneksi().getConnection();
			cs = con.prepareCall(query);
			cs.setInt(1, kodeperencanaan);
			rs = cs.executeQuery();
			List<PublikasiKaryaIlmiah> rows = new ArrayList<>();
			while(rs.next()) {
				PublikasiKaryaIlmiah row = new PublikasiKaryaIlmiah();
				row.setKode(rs.getInt("kode"));
				row.setJudul(rs.getString("judul"));
				row.setPublisher(rs.getString("publisher"));
				row.setRating(rs.getInt("rating"));
				row.setKeterangan(rs.getString("keterangan"));
				row.setLampiran(rs.getString("lampiran"));
				row.setShow(1);
				rows.add(row);
			}
			if(rows.size()>0) {
				return rows;
			}
		} 
		finally {
			if (rs != null) {
				 try {
					 rs.close();
				 } catch (SQLException e1) {
				 }
			}
			if (cs != null) {
				try {
					cs.close();
				} catch (SQLException e1) {
				}
			}

			if (con != null) {
				try {
					con.close();
				} catch (SQLException e1) {
				}
			}
		}
		return null;
	}
	
	public static List<PublikasiKaryaIlmiah> getEvaluasiPublikasiKaryaIlmiah(Integer kodepembinaan) throws SQLException, NamingException {
		Connection con = null;
		CallableStatement cs = null;
		String query = null;
		ResultSet rs = null;
		try {
			query = "select a.kode, a.judul, a.publisher, a.rating, a.keterangan, a.lampiran, \r\n" + 
					"c.rating as ratingevaluasi, c.catatan as catatanevaluasi, c.kode as kodeevaluasipublikasikaryailmiah \r\n" + 
					"from kinerja.pembinaanpublikasikaryailmiah a \r\n" + 
					"inner join kinerja.pembinaan b on a.kodepembinaan=b.kode \r\n" + 
					"left join kinerja.evaluasipublikasikaryailmiah c on a.kode=c.kodepembinaanpublikasikaryailmiah \r\n" + 
					"where b.kode=?";
			con = new Koneksi().getConnection();
			cs = con.prepareCall(query);
			cs.setInt(1, kodepembinaan);
			rs = cs.executeQuery();
			List<PublikasiKaryaIlmiah> rows = new ArrayList<>();
			while(rs.next()) {
				PublikasiKaryaIlmiah row = new PublikasiKaryaIlmiah();
				row.setKode(rs.getInt("kode"));
				row.setJudul(rs.getString("judul"));
				row.setPublisher(rs.getString("publisher"));
				row.setRating(rs.getInt("rating"));
				row.setKeterangan(rs.getString("keterangan"));
				row.setLampiran(rs.getString("lampiran"));
				row.setRatingevaluasi(rs.getInt("ratingevaluasi"));
				row.setCatatanevaluasi(rs.getString("catatanevaluasi"));
				row.setKodeevaluasipublikasikaryailmiah(rs.getInt("kodeevaluasipublikasikaryailmiah"));
				row.setShow(1);
				rows.add(row);
			}
			if(rows.size()>0) {
				return rows;
			}
		} 
		finally {
			if (rs != null) {
				 try {
					 rs.close();
				 } catch (SQLException e1) {
				 }
			}
			if (cs != null) {
				try {
					cs.close();
				} catch (SQLException e1) {
				}
			}

			if (con != null) {
				try {
					con.close();
				} catch (SQLException e1) {
				}
			}
		}
		return null;
	}
	
	public static List<PublikasiKaryaIlmiahVerifikasi> getVerifikasiPublikasiKaryaIlmiah(Integer kodepembinaan) throws SQLException, NamingException {
		Connection con = null;
		CallableStatement cs = null;
		String query = null;
		ResultSet rs = null;
		try {
			query = "exec kinerja.sp_getverifikasipublikasikaryailmiah ?";
			con = new Koneksi().getConnection();
			cs = con.prepareCall(query);
			cs.setInt(1, kodepembinaan);
			rs = cs.executeQuery();
			List<PublikasiKaryaIlmiahVerifikasi> rows = new ArrayList<>();
			while(rs.next()) {
				PublikasiKaryaIlmiahVerifikasi row = new PublikasiKaryaIlmiahVerifikasi();
				row.setKode(rs.getInt("kode"));
				row.setJudul(rs.getString("judul"));
				row.setPublisher(rs.getString("publisher"));
				row.setRating(rs.getInt("rating"));
				row.setKeterangan(rs.getString("keterangan"));
				row.setCatatan(rs.getString("catatan"));
				rows.add(row);
			}
			if(rows.size()>0) {
				return rows;
			}
		} 
		finally {
			if (rs != null) {
				 try {
					 rs.close();
				 } catch (SQLException e1) {
				 }
			}
			if (cs != null) {
				try {
					cs.close();
				} catch (SQLException e1) {
				}
			}

			if (con != null) {
				try {
					con.close();
				} catch (SQLException e1) {
				}
			}
		}
		return null;
	}
	
	public static List<CreatingFutureLeaderPegPromosi> getCreatingFutureLeaderPegPromosi(Integer kodepembinaancreatingfutureleader) throws SQLException, NamingException {
		Connection con = null;
		CallableStatement cs = null;
		String query = null;
		ResultSet rs = null;
		try {
			query = "select a.kode, a.npp, a.lampiran, b.nama \r\n" + 
					"from kinerja.pembinaancreatingfutureleaderpegpromosi a \r\n" + 
					"inner join karyawan.vw_pegawai b on a.npp=b.npp \r\n" + 
					"where a.kodepembinaancreatingfutureleader=?";
			con = new Koneksi().getConnection();
			cs = con.prepareCall(query);
			cs.setInt(1, kodepembinaancreatingfutureleader);
			rs = cs.executeQuery();
			List<CreatingFutureLeaderPegPromosi> rows = new ArrayList<>();
			while(rs.next()) {
				CreatingFutureLeaderPegPromosi row = new CreatingFutureLeaderPegPromosi();
				row.setKode(rs.getInt("kode"));
				row.setLampiran(rs.getString("lampiran"));
				Pegawai peg = new Pegawai();
				peg.setNpp(rs.getString("npp"));
				peg.setNama(rs.getString("nama"));
				row.setPegawai(peg);
				row.setShow(1);
				rows.add(row);
			}
			if(rows.size()>0) {
				return rows;
			}
		} 
		finally {
			if (rs != null) {
				 try {
					 rs.close();
				 } catch (SQLException e1) {
				 }
			}
			if (cs != null) {
				try {
					cs.close();
				} catch (SQLException e1) {
				}
			}

			if (con != null) {
				try {
					con.close();
				} catch (SQLException e1) {
				}
			}
		}
		return null;
	}
	
	public static CreatingFutureLeader getCreatingFutureLeader(Integer kodeperencanaan) throws SQLException, NamingException {
		Connection con = null;
		CallableStatement cs = null;
		String query = null;
		ResultSet rs = null;
		try {
			query = "select top 1 a.kode, a.jumlahtalentstar, a.jumlahtalentstarpromosi, a.rating, a.keterangan \r\n" + 
					"from kinerja.pembinaancreatingfutureleader a \r\n" + 
					"inner join kinerja.pembinaan b on a.kodepembinaan=b.kode \r\n" + 
					"where b.kodeperencanaan=?";
			con = new Koneksi().getConnection();
			cs = con.prepareCall(query);
			cs.setInt(1, kodeperencanaan);
			rs = cs.executeQuery();
			CreatingFutureLeader row = null;
			if(rs.next()) {
				row = new CreatingFutureLeader();
				row.setKode(rs.getInt("kode"));
				row.setJumlahtalentstar(rs.getInt("jumlahtalentstar"));
				row.setJumlahtalentstarpromosi(rs.getInt("jumlahtalentstarpromosi"));
				row.setRating(rs.getInt("rating"));
				row.setKeterangan(rs.getString("keterangan"));
				row.setPegpromosis(KinerjaUtil.getCreatingFutureLeaderPegPromosi(row.getKode()));
			}
			return row;
		} 
		finally {
			if (rs != null) {
				 try {
					 rs.close();
				 } catch (SQLException e1) {
				 }
			}
			if (cs != null) {
				try {
					cs.close();
				} catch (SQLException e1) {
				}
			}

			if (con != null) {
				try {
					con.close();
				} catch (SQLException e1) {
				}
			}
		}
	}
	
	public static CreatingFutureLeader getEvaluasiCreatingFutureLeader(Integer kodepembinaan) throws SQLException, NamingException {
		Connection con = null;
		CallableStatement cs = null;
		String query = null;
		ResultSet rs = null;
		try {
			query = "select top 1 a.kode, a.jumlahtalentstar, a.jumlahtalentstarpromosi, a.rating, a.keterangan, \r\n" + 
					"c.rating as ratingevaluasi, c.catatan as catatanevaluasi, c.kode as kodeevaluasicreatingfutureleader \r\n" + 
					"from kinerja.pembinaancreatingfutureleader a \r\n" + 
					"inner join kinerja.pembinaan b on a.kodepembinaan=b.kode \r\n" + 
					"left join kinerja.evaluasicreatingfutureleader c on a.kode=c.kodepembinaancreatingfutureleader \r\n" + 
					"where b.kode=?";
			con = new Koneksi().getConnection();
			cs = con.prepareCall(query);
			cs.setInt(1, kodepembinaan);
			rs = cs.executeQuery();
			CreatingFutureLeader row = null;
			if(rs.next()) {
				row = new CreatingFutureLeader();
				row.setKode(rs.getInt("kode"));
				row.setJumlahtalentstar(rs.getInt("jumlahtalentstar"));
				row.setJumlahtalentstarpromosi(rs.getInt("jumlahtalentstarpromosi"));
				row.setRating(rs.getInt("rating"));
				row.setKeterangan(rs.getString("keterangan"));
				row.setRatingevaluasi(rs.getInt("ratingevaluasi"));
				row.setCatatanevaluasi(rs.getString("catatanevaluasi"));
				row.setKodeevaluasicreatingfutureleader(rs.getInt("kodeevaluasicreatingfutureleader"));
				row.setPegpromosis(KinerjaUtil.getCreatingFutureLeaderPegPromosi(row.getKode()));
			}
			return row;
		} 
		finally {
			if (rs != null) {
				 try {
					 rs.close();
				 } catch (SQLException e1) {
				 }
			}
			if (cs != null) {
				try {
					cs.close();
				} catch (SQLException e1) {
				}
			}

			if (con != null) {
				try {
					con.close();
				} catch (SQLException e1) {
				}
			}
		}
	}
	
	public static CreatingFutureLeaderVerifikasi getVerifikasiCreatingFutureLeader(Integer kodepembinaan) throws SQLException, NamingException {
		Connection con = null;
		CallableStatement cs = null;
		String query = null;
		ResultSet rs = null;
		try {
			query = "select top 1 e.kode, e.rating, e.catatan \r\n" + 
					"from kinerja.pembinaancreatingfutureleader a \r\n" + 
					"inner join kinerja.pembinaan b on a.kodepembinaan=b.kode \r\n" + 
					"inner join kinerja.evaluasicreatingfutureleader c on a.kode=c.kodepembinaancreatingfutureleader \r\n" + 
					"left join kinerja.verifikasi d on c.kodeevaluasi=d.kodeevaluasi \r\n" + 
					"left join kinerja.verifikasicreatingfutureleader e on d.kode=e.kodeverifikasi \r\n" + 
					"where a.kodepembinaan=?";
			con = new Koneksi().getConnection();
			cs = con.prepareCall(query);
			cs.setInt(1, kodepembinaan);
			rs = cs.executeQuery();
			CreatingFutureLeaderVerifikasi row = new CreatingFutureLeaderVerifikasi();
			if(rs.next()) {
				row.setKode(rs.getInt("kode"));
				row.setRating(rs.getInt("rating"));
				row.setCatatan(rs.getString("catatan"));
			}
			return row;
		} 
		finally {
			if (rs != null) {
				 try {
					 rs.close();
				 } catch (SQLException e1) {
				 }
			}
			if (cs != null) {
				try {
					cs.close();
				} catch (SQLException e1) {
				}
			}

			if (con != null) {
				try {
					con.close();
				} catch (SQLException e1) {
				}
			}
		}
	}
	
	public static String saveDoc(ServletContext context, String base64file, String namafolder, String npp) {
    	String namaFileFTP = null;
    	
    	FTPClient ftpClient = null;
    	try {
    		String host = null;
        	Integer port = null;
        	String user = null;
        	String pass = null;
        	
    		host = context.getInitParameter("ftp-host");
        	port = Integer.parseInt(context.getInitParameter("ftp-port"));
        	user = context.getInitParameter("ftp-user");
        	pass = context.getInitParameter("ftp-pass");
			
        	Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        	String namaFile = npp + "-" + timestamp.getTime() + ".pdf";
    		
        	String pathFile = namafolder + "/";
			ftpClient = new FTPClient();
			ftpClient.connect(host,port);
        	ftpClient.login(user, pass);
        	ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        	
        	byte[] decodedBytes = Base64.getDecoder().decode(base64file);
        	ByteArrayInputStream bis = new ByteArrayInputStream(decodedBytes);
        	
        	Boolean upload = ftpClient.storeFile(pathFile + namaFile, bis);
        	if(upload) {
        		namaFileFTP = namaFile;
        	}
        	ftpClient.logout();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if(ftpClient.isConnected()) {
				try {
					ftpClient.disconnect();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
    	return namaFileFTP;
    }
	
	public static Integer getKodePerencanaanByKodePembinaan(Integer kodepembinaan) throws SQLException, NamingException {
		Connection con = null;
		CallableStatement cs = null;
		String query = null;
		ResultSet rs = null;
		try {
			query = "select top 1 kodeperencanaan from kinerja.pembinaan where kode=?";
			con = new Koneksi().getConnection();
			cs = con.prepareCall(query);
			cs.setInt(1, kodepembinaan);
			rs = cs.executeQuery();
			if(rs.next()) {
				return rs.getInt("kodeperencanaan");
			}
		} 
		finally {
			if (rs != null) {
				 try {
					 rs.close();
				 } catch (SQLException e1) {
				 }
			}
			if (cs != null) {
				try {
					cs.close();
				} catch (SQLException e1) {
				}
			}

			if (con != null) {
				try {
					con.close();
				} catch (SQLException e1) {
				}
			}
		}
		return null;
	}
	
	public static Integer getKodePembinaanByKodeEvaluasi(Integer kodeevaluasi) throws SQLException, NamingException {
		Connection con = null;
		CallableStatement cs = null;
		String query = null;
		ResultSet rs = null;
		try {
			query = "select top 1 kodepembinaan from kinerja.evaluasi where kode=?";
			con = new Koneksi().getConnection();
			cs = con.prepareCall(query);
			cs.setInt(1, kodeevaluasi);
			rs = cs.executeQuery();
			if(rs.next()) {
				return rs.getInt("kodepembinaan");
			}
		} 
		finally {
			if (rs != null) {
				 try {
					 rs.close();
				 } catch (SQLException e1) {
				 }
			}
			if (cs != null) {
				try {
					cs.close();
				} catch (SQLException e1) {
				}
			}

			if (con != null) {
				try {
					con.close();
				} catch (SQLException e1) {
				}
			}
		}
		return null;
	}
	
	public static Boolean simpanCatatanPembinaan(Integer kodepembinaan, Integer flag, String catatan, Integer kodepenugasan, Integer useract) throws SQLException, NamingException {
		Connection con = null;
		CallableStatement cs = null;
		String query = null;
		try {
			query = "exec kinerja.sp_pembinaan_approval ?,?,?,?,?";
			con = new Koneksi().getConnection();
			cs = con.prepareCall(query);
			cs.setInt(1, kodepembinaan);
			cs.setInt(2, flag);
			cs.setString(3, catatan);
			cs.setInt(4, kodepenugasan);
			cs.setInt(5, useract);
			cs.execute();
			return true;
		} 
		finally {
			if (cs != null) {
				try {
					cs.close();
				} catch (SQLException e1) {
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e1) {
				}
			}
		}
	}
	
	public static List<Penugasan> getListBawahan(Integer kodepeserta) throws SQLException, NamingException {
		Connection con = null;
		CallableStatement cs = null;
		String query = null;
		ResultSet rs = null;
		try {
			query = "select f.npp, f.nama, g.kode as kodepeserta \r\n" + 
					"from kinerja.peserta a \r\n" + 
					"inner join karyawan.penugasan b on a.kodepenugasan=b.kode \r\n" + 
					"inner join organisasi.hirarkijabatan c on b.kodehirarkijabatan=c.kode \r\n" + 
					"inner join organisasi.hirarkijabatan d on c.kodejobtitle=d.kodeparentjobtitle \r\n" + 
					"inner join karyawan.penugasan e on d.kode=e.kodehirarkijabatan and e.kodeoffice=b.kodeoffice \r\n" + 
					"inner join karyawan.vw_pegawai f on e.npp=f.npp \r\n" + 
					"inner join kinerja.peserta g on e.kode=g.kodepenugasan and a.kodeperiodekinerja=g.kodeperiodekinerja \r\n" + 
					"where a.kode=? order by f.nama";
			con = new Koneksi().getConnection();
			cs = con.prepareCall(query);
			cs.setInt(1, kodepeserta);
			rs = cs.executeQuery();
			List<Penugasan> rows = new ArrayList<>();
			while(rs.next()) {
				Penugasan row = new Penugasan();
				row.setKode(rs.getInt("kodepeserta"));
				id.go.bpjskesehatan.entitas.karyawan.Pegawai peg = new id.go.bpjskesehatan.entitas.karyawan.Pegawai();
				peg.setNpp(rs.getString("npp"));
				peg.setNama(rs.getString("nama"));
				row.setPegawai(peg);
				rows.add(row);
			}
			if(rows.size()>0) {
				return rows;
			}
		} 
		finally {
			if (rs != null) {
				 try {
					 rs.close();
				 } catch (SQLException e1) {
				 }
			}
			if (cs != null) {
				try {
					cs.close();
				} catch (SQLException e1) {
				}
			}

			if (con != null) {
				try {
					con.close();
				} catch (SQLException e1) {
				}
			}
		}
		return null;
	}
	
	public static List<Map<String, Object>> getListCatatanKomitmen(Integer kodepeserta, Integer kodepesertabawahan) throws SQLException, NamingException {
		Connection con = null;
		CallableStatement cs = null;
		String query = null;
		ResultSet rs = null;
		List<Map<String, Object>> list = new ArrayList<>();
		try {
			query = "select a.kode, a.catatan, a.created_time as waktu \r\n" + 
					"from \r\n" + 
					"kinerja.catatankomitmen a \r\n" + 
					"where a.kodepeserta=? and a.kodepesertabawahan=?";
			con = new Koneksi().getConnection();
			cs = con.prepareCall(query);
			cs.setInt(1, kodepeserta);
			cs.setInt(2, kodepesertabawahan);
			rs = cs.executeQuery();
			while(rs.next()) {
				Map<String, Object> row = new HashMap<String, Object>();
				row.put("kode", rs.getInt("kode"));
				row.put("catatan", rs.getString("catatan"));
				row.put("waktu", rs.getTimestamp("waktu"));
				list.add(row);
			}
			if(list.size()>0) {
				return list;
			}
		} 
		finally {
			if (rs != null) {
				 try {
					 rs.close();
				 } catch (SQLException e1) {
				 }
			}
			if (cs != null) {
				try {
					cs.close();
				} catch (SQLException e1) {
				}
			}

			if (con != null) {
				try {
					con.close();
				} catch (SQLException e1) {
				}
			}
		}
		return null;
	}
	
	public static List<Map<String, Object>> getListCatatanKejadianKritis(Integer kodepeserta, Integer kodepesertabawahan) throws SQLException, NamingException {
		Connection con = null;
		CallableStatement cs = null;
		String query = null;
		ResultSet rs = null;
		List<Map<String, Object>> list = new ArrayList<>();
		try {
			query = "select a.kode, a.nama, a.rating, a.keterangan, a.created_time as waktu \r\n" + 
					"from \r\n" + 
					"kinerja.catatankejadiankritisnegatif a \r\n" + 
					"where a.kodepeserta=? and a.kodepesertabawahan=?";
			con = new Koneksi().getConnection();
			cs = con.prepareCall(query);
			cs.setInt(1, kodepeserta);
			cs.setInt(2, kodepesertabawahan);
			rs = cs.executeQuery();
			while(rs.next()) {
				Map<String, Object> row = new HashMap<String, Object>();
				row.put("kode", rs.getInt("kode"));
				row.put("nama", rs.getString("nama"));
				row.put("rating", rs.getInt("rating"));
				row.put("keterangan", rs.getString("keterangan"));
				row.put("waktu", rs.getTimestamp("waktu"));
				list.add(row);
			}
			if(list.size()>0) {
				return list;
			}
		} 
		finally {
			if (rs != null) {
				 try {
					 rs.close();
				 } catch (SQLException e1) {
				 }
			}
			if (cs != null) {
				try {
					cs.close();
				} catch (SQLException e1) {
				}
			}

			if (con != null) {
				try {
					con.close();
				} catch (SQLException e1) {
				}
			}
		}
		return null;
	}
	
	public static List<KejadianKritisNegatif> getListCatatanKejadianKritis(Integer kodepeserta) throws SQLException, NamingException {
		Connection con = null;
		CallableStatement cs = null;
		String query = null;
		ResultSet rs = null;
		List<KejadianKritisNegatif> list = new ArrayList<>();
		try {
			query = "select a.kode, a.nama, a.rating, a.keterangan, a.created_time as waktu \r\n" + 
					"from \r\n" + 
					"kinerja.catatankejadiankritisnegatif a \r\n" + 
					"where a.kodepesertabawahan=?";
			con = new Koneksi().getConnection();
			cs = con.prepareCall(query);
			cs.setInt(1, kodepeserta);
			rs = cs.executeQuery();
			while(rs.next()) {
				KejadianKritisNegatif row = new KejadianKritisNegatif();
				row.setKode(rs.getInt("kode"));
				row.setNama(rs.getString("nama"));
				row.setRating(rs.getInt("rating"));
				row.setKeterangan(rs.getString("keterangan"));
				list.add(row);
			}
			if(list.size()>0) {
				return list;
			}
		} 
		finally {
			if (rs != null) {
				 try {
					 rs.close();
				 } catch (SQLException e1) {
				 }
			}
			if (cs != null) {
				try {
					cs.close();
				} catch (SQLException e1) {
				}
			}

			if (con != null) {
				try {
					con.close();
				} catch (SQLException e1) {
				}
			}
		}
		return null;
	}
	
	public static List<KejadianKritisNegatif> getListEvaluasiKejadianKritis(Integer kodepeserta) throws SQLException, NamingException {
		Connection con = null;
		CallableStatement cs = null;
		String query = null;
		ResultSet rs = null;
		List<KejadianKritisNegatif> list = new ArrayList<>();
		try {
			query = "select a.kode, a.nama, a.rating, a.keterangan, a.created_time as waktu, \r\n" + 
					"b.kode as kodeevaluasikejadiankritisnegatif, b.rating as ratingevaluasi, b.catatan as catatanevaluasi \r\n" + 
					"from \r\n" + 
					"kinerja.catatankejadiankritisnegatif a \r\n" + 
					"left join kinerja.evaluasikejadiankritisnegatif b on a.kode=b.kodecatatankejadiankritisnegatif \r\n" + 
					"where a.kodepesertabawahan=?";
			con = new Koneksi().getConnection();
			cs = con.prepareCall(query);
			cs.setInt(1, kodepeserta);
			rs = cs.executeQuery();
			while(rs.next()) {
				KejadianKritisNegatif row = new KejadianKritisNegatif();
				row.setKode(rs.getInt("kode"));
				row.setNama(rs.getString("nama"));
				row.setRating(rs.getInt("rating"));
				row.setKeterangan(rs.getString("keterangan"));
				row.setRatingevaluasi(rs.getInt("ratingevaluasi"));
				row.setCatatanevaluasi(rs.getString("catatanevaluasi"));
				row.setKodeevaluasikejadiankritisnegatif(rs.getInt("kodeevaluasikejadiankritisnegatif"));
				list.add(row);
			}
			if(list.size()>0) {
				return list;
			}
		} 
		finally {
			if (rs != null) {
				 try {
					 rs.close();
				 } catch (SQLException e1) {
				 }
			}
			if (cs != null) {
				try {
					cs.close();
				} catch (SQLException e1) {
				}
			}

			if (con != null) {
				try {
					con.close();
				} catch (SQLException e1) {
				}
			}
		}
		return null;
	}
	
	public static List<KejadianKritisNegatifVerifikasi> getListVerifikasiKejadianKritis(Integer kodepeserta) throws SQLException, NamingException {
		Connection con = null;
		CallableStatement cs = null;
		String query = null;
		ResultSet rs = null;
		List<KejadianKritisNegatifVerifikasi> list = new ArrayList<>();
		try {
			query = "exec kinerja.sp_getverifikasikejadiankritisnegatif ?";
			con = new Koneksi().getConnection();
			cs = con.prepareCall(query);
			cs.setInt(1, kodepeserta);
			rs = cs.executeQuery();
			while(rs.next()) {
				KejadianKritisNegatifVerifikasi row = new KejadianKritisNegatifVerifikasi();
				row.setKode(rs.getInt("kode"));
				row.setNama(rs.getString("nama"));
				row.setRating(rs.getInt("rating"));
				row.setKeterangan(rs.getString("keterangan"));
				row.setCatatan(rs.getString("catatan"));
				list.add(row);
			}
			if(list.size()>0) {
				return list;
			}
		} 
		finally {
			if (rs != null) {
				 try {
					 rs.close();
				 } catch (SQLException e1) {
				 }
			}
			if (cs != null) {
				try {
					cs.close();
				} catch (SQLException e1) {
				}
			}

			if (con != null) {
				try {
					con.close();
				} catch (SQLException e1) {
				}
			}
		}
		return null;
	}
	
	public static List<SettingLockKriteriaKpi> getKriterias(Integer kodekomponen, Integer kodepeserta) throws SQLException, NamingException {
		Connection con = null;
		CallableStatement cs = null;
		String query = null;
		ResultSet rs = null;
		try {
			query = "select \r\n" + 
					"a.kode, \r\n" + 
					"a.rating, \r\n" + 
					"a.score, \r\n" + 
					"a.definisi, \r\n" + 
					"a.deskripsi \r\n" + 
					"from kinerja.kriteria a \r\n" + 
					"inner join kinerja.peserta b on a.kodeperiodekinerja=b.kodeperiodekinerja \r\n" + 
					"where a.kodekomponen=? and b.kode=?";
			con = new Koneksi().getConnection();
			cs = con.prepareCall(query);
			cs.setInt(1, kodekomponen);
			cs.setInt(2, kodepeserta);
			rs = cs.executeQuery();
			List<SettingLockKriteriaKpi> rows = new ArrayList<>();
			while(rs.next()) {
				SettingLockKriteriaKpi row = new SettingLockKriteriaKpi();
				row.setDefinisi(rs.getString("definisi"));
				row.setRating(rs.getInt("rating"));
				row.setScore(rs.getInt("score"));
				row.setKode(rs.getInt("kode"));
				row.setDeskripsi(rs.getString("deskripsi"));
				rows.add(row);
			}
			if(rows.size()>0) {
				return rows;
			}
		} 
		finally {
			if (rs != null) {
				 try {
					 rs.close();
				 } catch (SQLException e1) {
				 }
			}
			if (cs != null) {
				try {
					cs.close();
				} catch (SQLException e1) {
				}
			}

			if (con != null) {
				try {
					con.close();
				} catch (SQLException e1) {
				}
			}
		}
		return null;
	}
	
	public static EvaluasiKpi getEvaluasiKpi(Integer kodeperencanaanhaskerkpi) throws SQLException, NamingException {
		Connection con = null;
		CallableStatement cs = null;
		String query = null;
		ResultSet rs = null;
		EvaluasiKpi row = new EvaluasiKpi();
		row.setLampiran("");
		try {
			query = "select top 1 kode, rating, pencapaian, sumberdata, lampiran \r\n" + 
					"from kinerja.evaluasihaskerkpi \r\n" + 
					"where kodeperencanaanhaskerkpi=?";
			con = new Koneksi().getConnection();
			cs = con.prepareCall(query);
			cs.setInt(1, kodeperencanaanhaskerkpi);
			rs = cs.executeQuery();
			if(rs.next()) {
				row.setKode(rs.getInt("kode"));
				row.setRating(rs.getInt("rating"));
				row.setPencapaian(rs.getString("pencapaian"));
				row.setSumberdata(rs.getString("sumberdata"));
				row.setLampiran(rs.getString("lampiran"));
			}
		} 
		finally {
			if (rs != null) {
				 try {
					 rs.close();
				 } catch (SQLException e1) {
				 }
			}
			if (cs != null) {
				try {
					cs.close();
				} catch (SQLException e1) {
				}
			}

			if (con != null) {
				try {
					con.close();
				} catch (SQLException e1) {
				}
			}
		}
		return row;
	}
	
	public static VerifikasiKpi getVerifikasiKpi(Integer kodeevaluasihaskerkpi) throws SQLException, NamingException {
		Connection con = null;
		CallableStatement cs = null;
		String query = null;
		ResultSet rs = null;
		VerifikasiKpi row = new VerifikasiKpi();
		try {
			query = "select top 1 kode, rating, catatan \r\n" + 
					"from kinerja.verifikasihaskerkpi \r\n" + 
					"where kodeevaluasihaskerkpi=?";
			con = new Koneksi().getConnection();
			cs = con.prepareCall(query);
			cs.setInt(1, kodeevaluasihaskerkpi);
			rs = cs.executeQuery();
			if(rs.next()) {
				row.setKode(rs.getInt("kode"));
				row.setRating(rs.getInt("rating"));
				row.setCatatan(rs.getString("catatan"));
			}
		} 
		finally {
			if (rs != null) {
				 try {
					 rs.close();
				 } catch (SQLException e1) {
				 }
			}
			if (cs != null) {
				try {
					cs.close();
				} catch (SQLException e1) {
				}
			}

			if (con != null) {
				try {
					con.close();
				} catch (SQLException e1) {
				}
			}
		}
		return row;
	}
	
	public static void settingSiklusPeserta(Integer kodepeserta, Integer kodesiklus, Integer useract) throws MyException {
		Connection con = null;
		CallableStatement cs = null;
		String query = null;
		try {
			query = "exec kinerja.sp_set_siklus_peserta ?,?,?";
			con = new Koneksi().getConnection();
			cs = con.prepareCall(query);
			cs.setInt(1, kodepeserta);
			cs.setInt(2, kodesiklus);
			cs.setInt(3, useract);
			cs.execute();
		} catch (SQLException e) {
			throw new MyException(e.getMessage());
		} catch (NullPointerException e) {
			e.printStackTrace();
			throw new MyException(e.getMessage());
		} catch (Exception e) {
			throw new MyException(e.getMessage());
		}
		finally {
			if (cs != null) {
				try {
					cs.close();
				} catch (SQLException e) {
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
				}
			}	
		}
	}
	
}
