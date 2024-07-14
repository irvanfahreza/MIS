package id.go.bpjskesehatan.service.v2.kinerja;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import id.go.bpjskesehatan.database.Koneksi;
import id.go.bpjskesehatan.service.v2.kinerja.entitas.MasterNilai;
import id.go.bpjskesehatan.service.v2.kinerja.entitas.MasterNilaiDetail;
import id.go.bpjskesehatan.service.v2.kinerja.entitas.UbKomitmenNilai;
import id.go.bpjskesehatan.util.MyException;

public class UBKomitmenUtil {
	
	public static void generateUbKomitmenPesertaPenilai(Integer kodeubkomitmenpeserta, Integer useract) throws MyException {
		Connection con = null;
		CallableStatement cs = null;
		String query = null;
		try {
			query = "exec kinerja.sp_generate_ubkomitmenpesertapenilai ?,?";
			con = new Koneksi().getConnection();
			cs = con.prepareCall(query);
			cs.setInt(1, kodeubkomitmenpeserta);
			cs.setInt(2, useract);
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
	
	public static Map<String, Object> getInfoNotifikasi(Integer kodenotifikasi) throws SQLException, NamingException {
		Connection con = null;
		CallableStatement cs = null;
		String query = null;
		ResultSet rs = null;
		try {
			query = "exec hcis.sp_getinfonotifikasi ?";
			con = new Koneksi().getConnection();
			cs = con.prepareCall(query);
			cs.setInt(1, kodenotifikasi);
			rs = cs.executeQuery();
			ResultSetMetaData metaData = rs.getMetaData();
			if(rs.next()) {
				Map<String, Object> hasil = new HashMap<String, Object>();
				for (int i = 1; i <= metaData.getColumnCount(); i++) {
					hasil.put(metaData.getColumnName(i).toLowerCase(), rs.getObject(i));
				}
				return hasil;
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
	
	public static List<Map<String, Object>> getKriteriaPenilaian(Integer kodenotifikasi) throws SQLException, NamingException {
		Connection con = null;
		CallableStatement cs = null;
		String query = null;
		ResultSet rs = null;
		try {
			query = "exec kinerja.sp_get_kriteriapenilaian_ubkomitmen ?";
			con = new Koneksi().getConnection();
			cs = con.prepareCall(query);
			cs.setInt(1, kodenotifikasi);
			rs = cs.executeQuery();
			ResultSetMetaData metaData = rs.getMetaData();
			List<Map<String, Object>> listdata = new ArrayList<Map<String, Object>>();;
			Map<String, Object> hasil = null;
			while(rs.next()) {
				hasil = new HashMap<String, Object>();
				for (int i = 1; i <= metaData.getColumnCount(); i++) {
					hasil.put(metaData.getColumnName(i).toLowerCase(), rs.getObject(i));
				}
				listdata.add(hasil);
			}
			return listdata;
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
	
	public static Map<String, Object> getKomunikasi(Integer kodenotifikasi) throws SQLException, NamingException {
		Connection con = null;
		CallableStatement cs = null;
		String query = null;
		ResultSet rs = null;
		try {
			query = "exec kinerja.sp_get_komunikasi_ubkomitmen ?";
			con = new Koneksi().getConnection();
			cs = con.prepareCall(query);
			cs.setInt(1, kodenotifikasi);
			rs = cs.executeQuery();
			ResultSetMetaData metaData = rs.getMetaData();
			Map<String, Object> hasil = null;
			if(rs.next()) {
				hasil = new HashMap<String, Object>();
				for (int i = 1; i <= metaData.getColumnCount(); i++) {
					hasil.put(metaData.getColumnName(i).toLowerCase(), rs.getObject(i));
				}
			}
			return hasil;
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
	
	public static List<MasterNilaiDetail> getMasterNilaiDetail(Integer kodemasternilai, Integer kodeubkomitmenpesertapenilai) throws SQLException, NamingException {
		Connection con = null;
		CallableStatement cs = null;
		String query = null;
		ResultSet rs = null;
		try {
			query = "exec kinerja.sp_ubkomitmen_soal ?, ?";
			con = new Koneksi().getConnection();
			cs = con.prepareCall(query);
			cs.setInt(1, kodemasternilai);
			cs.setInt(2, kodeubkomitmenpesertapenilai);
			rs = cs.executeQuery();
			List<MasterNilaiDetail> rows = new ArrayList<>();
			while(rs.next()) {
				MasterNilaiDetail row = new MasterNilaiDetail();
				row.setKode(rs.getInt("kode"));
				row.setPerilaku(rs.getString("perilaku"));
				row.setValue(rs.getString("value"));
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
	
	public static List<UbKomitmenNilai> getUbKomitmenNilai(Integer kodenotifikasi, Integer kodeubkomitmenpesertapenilai) throws SQLException, NamingException {
		Connection con = null;
		CallableStatement cs = null;
		String query = null;
		ResultSet rs = null;
		try {
			query = "exec kinerja.sp_ubkomitmennilai_header_soal ?, ?";
			con = new Koneksi().getConnection();
			cs = con.prepareCall(query);
			cs.setInt(1, kodenotifikasi);
			cs.setInt(2, kodeubkomitmenpesertapenilai);
			rs = cs.executeQuery();
			List<UbKomitmenNilai> rows = new ArrayList<>();
			while(rs.next()) {
				UbKomitmenNilai row = new UbKomitmenNilai();
				row.setKode(rs.getInt("kode"));
				row.setBobot(rs.getFloat("bobot"));
				MasterNilai masterNilai = new MasterNilai();
				masterNilai.setKode(rs.getInt("kodemasternilai"));
				masterNilai.setNama(rs.getString("nama"));
				masterNilai.setDeskripsi(rs.getString("deskripsi"));
				masterNilai.setDetails(getMasterNilaiDetail(rs.getInt("kodemasternilai"), kodeubkomitmenpesertapenilai));
				row.setMasternilai(masterNilai);
				row.setShow(false);
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
	
}
