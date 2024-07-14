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
import id.go.bpjskesehatan.service.v2.kinerja.entitas.MasterItemJawaban;
import id.go.bpjskesehatan.service.v2.kinerja.entitas.UbKompetensiHeaderSoal;
import id.go.bpjskesehatan.service.v2.kinerja.entitas.UbKompetensiLevelItem;
import id.go.bpjskesehatan.service.v2.kinerja.entitas.UbKompetensiPenilaianSimpan;
import id.go.bpjskesehatan.service.v2.kinerja.entitas.UbKompetensiSoal;
import id.go.bpjskesehatan.util.MyException;
import id.go.bpjskesehatan.util.Utils;

public class UBKompetensiUtil {
	
	public static void generateUbKompetensiPesertaPenilai(Integer kodeubkompetensipeserta, Integer useract) throws MyException {
		Connection con = null;
		CallableStatement cs = null;
		String query = null;
		try {
			query = "exec kinerja.sp_generate_ubkompetensipesertapenilai ?,?";
			con = new Koneksi().getConnection();
			cs = con.prepareCall(query);
			cs.setInt(1, kodeubkompetensipeserta);
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
					if(rs.getObject(i)!=null && metaData.getColumnTypeName(i).equalsIgnoreCase("date")){
						hasil.put(metaData.getColumnName(i).toLowerCase(), Utils.SqlDateToSqlString(rs.getDate(i)));
					}
					else {
						hasil.put(metaData.getColumnName(i).toLowerCase(), rs.getObject(i));
					}
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
			query = "exec kinerja.sp_get_kriteriapenilaian_ubkompetensi ?";
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
					if(rs.getObject(i)!=null && metaData.getColumnTypeName(i).equalsIgnoreCase("date")){
						hasil.put(metaData.getColumnName(i).toLowerCase(), Utils.SqlDateToSqlString(rs.getDate(i)));
					}
					else {
						hasil.put(metaData.getColumnName(i).toLowerCase(), rs.getObject(i));
					}
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
			query = "exec kinerja.sp_get_komunikasi_ubkompetensi ?";
			con = new Koneksi().getConnection();
			cs = con.prepareCall(query);
			cs.setInt(1, kodenotifikasi);
			rs = cs.executeQuery();
			ResultSetMetaData metaData = rs.getMetaData();
			Map<String, Object> hasil = null;
			if(rs.next()) {
				hasil = new HashMap<String, Object>();
				for (int i = 1; i <= metaData.getColumnCount(); i++) {
					if(rs.getObject(i)!=null && metaData.getColumnTypeName(i).equalsIgnoreCase("date")){
						hasil.put(metaData.getColumnName(i).toLowerCase(), Utils.SqlDateToSqlString(rs.getDate(i)));
					}
					else {
						hasil.put(metaData.getColumnName(i).toLowerCase(), rs.getObject(i));
					}
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
	
	public static UbKompetensiPenilaianSimpan getUbKompetensiPenilaian(Integer kodeubkompetensipesertapenilai) throws SQLException, NamingException {
		Connection con = null;
		CallableStatement cs = null;
		String query = null;
		ResultSet rs = null;
		try {
			query = "select top 1 * from kinerja.ubkompetensipenilaian where kodeubkompetensipesertapenilai=?";
			con = new Koneksi().getConnection();
			cs = con.prepareCall(query);
			cs.setInt(1, kodeubkompetensipesertapenilai);
			rs = cs.executeQuery();
			if(rs.next()) {
				UbKompetensiPenilaianSimpan row = new UbKompetensiPenilaianSimpan();
				row.setSubmitted(rs.getInt("submitted"));
				return row;
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
	
	public static List<UbKompetensiHeaderSoal> getHeaderSoal(Integer kodenotifikasi, Integer kodeubkompetensipesertapenilai) throws SQLException, NamingException {
		Connection con = null;
		CallableStatement cs = null;
		String query = null;
		ResultSet rs = null;
		try {
			query = "exec kinerja.sp_ubkompetensi_header_soal ?, ?";
			con = new Koneksi().getConnection();
			cs = con.prepareCall(query);
			cs.setInt(1, kodenotifikasi);
			cs.setInt(2, kodeubkompetensipesertapenilai);
			rs = cs.executeQuery();
			List<UbKompetensiHeaderSoal> rows = new ArrayList<>();
			while(rs.next()) {
				UbKompetensiHeaderSoal row = new UbKompetensiHeaderSoal();
				row.setKodekompetensi(rs.getString("kodekompetensi"));
				row.setNamakompetensi(rs.getString("namakompetensi"));
				row.setLevel(rs.getInt("level"));
				row.setBataslevelbawah(rs.getInt("bataslevelbawah"));
				row.setBataslevelatas(rs.getInt("bataslevelatas"));
				row.setJmlitem(rs.getInt("jmlitem"));
				row.setKodemastertipejawaban(rs.getInt("kodemastertipejawaban"));
				row.setFilled(rs.getInt("filled"));
				row.setLeveluppoints(rs.getInt("leveluppoints"));
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
	
	public static List<MasterItemJawaban> getMasterItemJawaban(Integer kodemastertipejawaban) throws SQLException, NamingException {
		Connection con = null;
		CallableStatement cs = null;
		String query = null;
		ResultSet rs = null;
		try {
			query = "select nama, bobot from kinerja.masteritemjawaban where kodemastertipejawaban=? order by bobot";
			con = new Koneksi().getConnection();
			cs = con.prepareCall(query);
			cs.setInt(1, kodemastertipejawaban);
			rs = cs.executeQuery();
			List<MasterItemJawaban> rows = new ArrayList<>();
			while(rs.next()) {
				MasterItemJawaban row = new MasterItemJawaban();
				row.setNama(rs.getString("nama"));
				row.setBobot(rs.getInt("bobot"));
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
	
	public static List<UbKompetensiLevelItem> getLevelItem(Integer kodeubkompetensipesertapenilai, String kodekompetensi, Integer level, Integer bataslevelbawah, Integer bataslevelatas, Integer filled) {
		List<UbKompetensiLevelItem> rows = new ArrayList<>();
		if(filled == 0) {
			try {
				Integer dari, ke = 0;
				if(level - Math.abs(bataslevelbawah) < 1) dari = 1; else dari = level - Math.abs(bataslevelbawah);
				if(level + bataslevelatas > 5) ke = 5; else ke = level + bataslevelatas;
				
				for(int i=dari; i<=ke; i++) {
					UbKompetensiLevelItem row = new UbKompetensiLevelItem();
					row.setLevel(i);
					rows.add(row);
				}
				
				if(rows.size() > 0) {
					return rows;
				}
			} 
			catch (Exception e) {
				// TODO: handle exception
			}
		}
		else if(filled==1) {
			Connection con = null;
			CallableStatement cs = null;
			String query = null;
			ResultSet rs = null;
			try {
				query = "exec kinerja.sp_ubkompetensi_penilaian_level_filled ?,?";
				con = new Koneksi().getConnection();
				cs = con.prepareCall(query);
				cs.setInt(1, kodeubkompetensipesertapenilai);
				cs.setString(2, kodekompetensi);
				rs = cs.executeQuery();
				while(rs.next()) {
					UbKompetensiLevelItem row = new UbKompetensiLevelItem();
					row.setLevel(rs.getInt("level"));
					rows.add(row);
				}
				if(rows.size()>0) {
					return rows;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NamingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
		
		return null;
	}
	
	public static List<UbKompetensiSoal> getSoal(Integer kodeubkompetensipesertapenilai, String kodekompetensi, Integer level, Integer jmlitem, Integer filled) throws SQLException, NamingException {
		Connection con = null;
		CallableStatement cs = null;
		String query = null;
		ResultSet rs = null;
		try {
			query = "exec kinerja.sp_ubkompetensi_soal ?,?,?,?,?";
			con = new Koneksi().getConnection();
			cs = con.prepareCall(query);
			cs.setInt(1, kodeubkompetensipesertapenilai);
			cs.setString(2, kodekompetensi);
			cs.setInt(3, level);
			cs.setInt(4, jmlitem);
			cs.setInt(5, filled);
			rs = cs.executeQuery();
			List<UbKompetensiSoal> rows = new ArrayList<>();
			while(rs.next()) {
				UbKompetensiSoal row = new UbKompetensiSoal();
				row.setLevel(level);
				row.setKode(rs.getInt("kode"));
				row.setItem(rs.getString("item"));
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
	
}
