package id.go.bpjskesehatan.service.v2.karyawan;

import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import id.go.bpjskesehatan.database.Koneksi;
import id.go.bpjskesehatan.entitas.Metadata;
import id.go.bpjskesehatan.entitas.Result2;
import id.go.bpjskesehatan.service.mobile.v1.AuthUser;
import id.go.bpjskesehatan.service.v2.TableCrud;
import id.go.bpjskesehatan.service.v2.entitas.GeneratedKey;
import id.go.bpjskesehatan.service.v2.karyawan.entitas.DetilAlamat;
import id.go.bpjskesehatan.service.v2.karyawan.entitas.InfoPribadi;
import id.go.bpjskesehatan.service.v2.karyawan.entitas.Pegawai;
import id.go.bpjskesehatan.util.MyException;
import id.go.bpjskesehatan.util.SharedMethod;
import id.go.bpjskesehatan.util.Utils;

@Path("/v2/karyawan")
public class Karyawan2Rest {	
	
	@Context
    private ServletContext context;

	@GET
	@Path("/setting/infopribadi/getkaryawan/npp/{npp}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response getPegawaiByNPP(
			@Context HttpHeaders headers, 
			@PathParam("npp") String npp
		) {
		Map<String, Object> response = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> metadata = new HashMap<String, Object>();
		
		if (SharedMethod.VerifyToken(headers, metadata)) {
			
			Connection con = null;
			ResultSet rs = null;
			CallableStatement cs = null;
			String query = null;
			
			ResultSetMetaData metaData;
			Map<String, Object> hasil = null;
			
			try {
				SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
				metadata.put("code", 2);
				metadata.put("message", Response.Status.NO_CONTENT.toString());
				
				query = "exec karyawan.sp_listpegawai ?,?,?,?,?";
				con = new Koneksi().getConnection();
				cs = con.prepareCall(query);
				cs.setInt(1, 1);
				cs.setInt(2, 1);
				cs.setInt(3, 0);
				cs.setNull(4, java.sql.Types.VARCHAR);
				cs.setString(5, String.format("npp = \'%s\'", npp));
				rs = cs.executeQuery();
				
				if (rs.next()) {
					metaData = rs.getMetaData();
					hasil = new HashMap<String, Object>();
					for (int i = 1; i <= metaData.getColumnCount(); i++) {
						if(rs.getObject(i)!=null && metaData.getColumnTypeName(i).equalsIgnoreCase("date")){
							hasil.put(metaData.getColumnName(i).toLowerCase(), dateFormatter.format(rs.getDate(i)));
						}
						else {
							hasil.put(metaData.getColumnName(i).toLowerCase(), rs.getObject(i));
						}
					}
					
					ResultSet rs2 = null;
					CallableStatement cs2 = null;
					String query2 = null;
					try {
						query2 = "select top 1 a.npp, iif(b.kode is null,0,1) as ada_photo, iif(c.kode is null,0,1) as ada_ttd \r\n" + 
								"from karyawan.pegawai a \r\n" + 
								"left join karyawan.foto b on a.npp=a.npp \r\n" + 
								"left join karyawan.tandatangan c on a.npp=c.npp \r\n" + 
								"where a.npp=?";
						cs2 = con.prepareCall(query2, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
						cs2.setString(1, npp);
						rs2 = cs2.executeQuery();
						if(rs2.next()) {
							hasil.put("ada_photo", rs2.getBoolean("ada_photo"));
							hasil.put("ada_ttd", rs2.getBoolean("ada_ttd"));
						}
					} finally {
						if (rs2 != null) {
							try {
								rs2.close();
							} catch (SQLException e) {
							}
						}
						if (cs2 != null) {
							try {
								cs2.close();
							} catch (SQLException e) {
							}
						}
					}
					
					Map<String, Object> hasil2 = null;
					try {
						query2 = "exec karyawan.sp_listdetailalamat ?,?,?,?,?";
						cs2 = con.prepareCall(query2, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
						cs2.setInt(1, 1);
						cs2.setInt(2, 1);
						cs2.setInt(3, 0);
						cs2.setNull(4, java.sql.Types.VARCHAR);
						cs2.setString(5, String.format("npp = \'%s\'", npp));
						rs2 = cs2.executeQuery();
						if(rs2.next()) {
							metaData = rs2.getMetaData();
							hasil2 = new HashMap<String, Object>();
							for (int i = 1; i <= metaData.getColumnCount(); i++) {
								if(rs2.getObject(i)!=null && metaData.getColumnTypeName(i).equalsIgnoreCase("date")){
									hasil2.put(metaData.getColumnName(i).toLowerCase(), dateFormatter.format(rs2.getDate(i)));
								}
								else {
									hasil2.put(metaData.getColumnName(i).toLowerCase(), rs2.getObject(i));
								}
							}
							hasil.put("alamatsaatini", hasil2);
						}
					} finally {
						if (rs2 != null) {
							try {
								rs2.close();
							} catch (SQLException e) {
							}
						}
						if (cs2 != null) {
							try {
								cs2.close();
							} catch (SQLException e) {
							}
						}
					}
					
					Map<String, Object> hasil3 = null;
					try {
						query2 = "exec karyawan.sp_listdetailalamat2 ?,?,?,?,?";
						cs2 = con.prepareCall(query2, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
						cs2.setInt(1, 1);
						cs2.setInt(2, 1);
						cs2.setInt(3, 0);
						cs2.setNull(4, java.sql.Types.VARCHAR);
						cs2.setString(5, String.format("npp = \'%s\'", npp));
						rs2 = cs2.executeQuery();
						if(rs2.next()) {
							metaData = rs2.getMetaData();
							hasil3 = new HashMap<String, Object>();
							for (int i = 1; i <= metaData.getColumnCount(); i++) {
								if(rs2.getObject(i)!=null && metaData.getColumnTypeName(i).equalsIgnoreCase("date")){
									hasil3.put(metaData.getColumnName(i).toLowerCase(), dateFormatter.format(rs2.getDate(i)));
								}
								else {
									hasil3.put(metaData.getColumnName(i).toLowerCase(), rs2.getObject(i));
								}
							}
							hasil.put("alamatktp", hasil3);
						}
					} finally {
						if (rs2 != null) {
							try {
								rs2.close();
							} catch (SQLException e) {
							}
						}
						if (cs2 != null) {
							try {
								cs2.close();
							} catch (SQLException e) {
							}
						}
					}
					
					metadata.put("code", 1);
					metadata.put("message", "OK");
					response.put("data", hasil);
					result.put("response", response);
				}
			} catch (SQLException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
			} catch (Exception e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				e.printStackTrace();
			} finally {
				if (rs != null) {
					try {
						rs.close();
					} catch (SQLException e) {
					}
				}

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
		result.put("metadata", metadata);
		return Response.ok(result).build();
	}
	
	@GET
	@Path("/setting/infopribadi/getkodeposbyalamat/kodekelurahan/{kodekel}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response getkodeposbyalamat(
			@Context HttpHeaders headers, 
			@PathParam("kodekel") String kodekel
		) {
		Map<String, Object> response = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> metadata = new HashMap<String, Object>();
		
		if (SharedMethod.VerifyToken(headers, metadata)) {
			
			Connection con = null;
			ResultSet rs = null;
			CallableStatement cs = null;
			String query = null;
			Map<String, Object> hasil = null;
			try {
				metadata.put("code", 2);
				metadata.put("message", Response.Status.NO_CONTENT.toString());
				
				query = "select kodepos from referensi.kelurahan where kode=?";
				con = new Koneksi().getConnection();
				cs = con.prepareCall(query);
				cs.setString(1, kodekel);
				rs = cs.executeQuery();
				
				if (rs.next()) {
					hasil = new HashMap<String, Object>();
					hasil.put("kodepos", rs.getString("kodepos"));
					metadata.put("code", 1);
					metadata.put("message", "OK");
					response.put("data", hasil);
					result.put("response", response);
				}
			} catch (SQLException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
			} catch (Exception e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				e.printStackTrace();
			} finally {
				if (rs != null) {
					try {
						rs.close();
					} catch (SQLException e) {
					}
				}

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
		result.put("metadata", metadata);
		return Response.ok(result).build();
	}
	
	@GET
	@Path("/setting/infopribadi/getalamatbykodepos/kodepos/{kodepos}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response getalamatbykodepos(
			@Context HttpHeaders headers, 
			@PathParam("kodepos") String kodepos
		) {
		Map<String, Object> response = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> metadata = new HashMap<String, Object>();
		
		if (SharedMethod.VerifyToken(headers, metadata)) {
			
			Connection con = null;
			ResultSet rs = null;
			CallableStatement cs = null;
			String query = null;
			
			ResultSetMetaData metaData;
			Map<String, Object> hasil = null;
			
			try {
				metadata.put("code", 2);
				metadata.put("message", Response.Status.NO_CONTENT.toString());
				
				query = "exec referensi.sp_listkelurahan ?,?,?,?,?";
				con = new Koneksi().getConnection();
				cs = con.prepareCall(query);
				cs.setInt(1, 1);
				cs.setInt(2, 1);
				cs.setInt(3, 0);
				cs.setNull(4, java.sql.Types.VARCHAR);
				cs.setString(5, String.format("kodepos = \'%s\'", kodepos));
				rs = cs.executeQuery();
				
				if (rs.next()) {
					metaData = rs.getMetaData();
					hasil = new HashMap<String, Object>();
					for (int i = 1; i <= metaData.getColumnCount(); i++) {
						if(rs.getObject(i)!=null && metaData.getColumnTypeName(i).equalsIgnoreCase("date")){
							hasil.put(metaData.getColumnName(i).toLowerCase(), Utils.SqlDateToSqlString(rs.getDate(i)));
						}
						else {
							hasil.put(metaData.getColumnName(i).toLowerCase(), rs.getObject(i));
						}
					}
					metadata.put("code", 1);
					metadata.put("message", "OK");
					response.put("data", hasil);
					result.put("response", response);
				}
			} catch (SQLException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
			} catch (Exception e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				e.printStackTrace();
			} finally {
				if (rs != null) {
					try {
						rs.close();
					} catch (SQLException e) {
					}
				}

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
		result.put("metadata", metadata);
		return Response.ok(result).build();
	}
	
	private void insertKaryawan(AuthUser authUser, Pegawai pegawai) throws MyException {
		Connection con = null;
		CallableStatement cs = null;
		String query = null;
		try {
			query = "insert into karyawan.pegawai (\r\n" + 
					"	npp,\r\n" + 
					"	namadepan,\r\n" + 
					"	namatengah,\r\n" + 
					"	namabelakang,\r\n" + 
					"	namapanggilan,\r\n" + 
					"	kodejeniskelamin,\r\n" + 
					"	gelardepan,\r\n" + 
					"	gelarbelakang,\r\n" + 
					"	kodeagama,\r\n" + 
					"	website,\r\n" + 
					"	hobi,\r\n" + 
					"	kodesukubangsa,\r\n" + 
					"	kewarganegaraan,\r\n" + 
					"	tempatlahir,\r\n" + 
					"	tgllahir,\r\n" + 
					"	kodestatusnikah,\r\n" + 
					"	telprumah,\r\n" + 
					"	hp,\r\n" + 
					"	hp2,\r\n" + 
					"	email,\r\n" + 
					"	email2,\r\n" + 
					"	kodesalutasi,\r\n" + 
					"	kodeasalpenerimaan,\r\n" + 
					"	kodedati2asal,\r\n" + 
					"	kodedati2asal2,\r\n" + 
					"	tmtmasuk,\r\n" + 
					"	kodestatuskaryawan,\r\n" + 
					"	row_status,\r\n" + 
					"	ikutzakat,\r\n" + 
					"	anggotakoperasi,\r\n" + 
					"	created_by\r\n" + 
					") values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			con = new Koneksi().getConnection();
			cs = con.prepareCall(query);
			cs.setString(1, pegawai.getNpp());
			cs.setString(2, pegawai.getNamadepan());
			cs.setString(3, pegawai.getNamatengah());
			cs.setString(4, pegawai.getNamabelakang());
			cs.setString(5, pegawai.getNamapanggilan());
			cs.setInt(6, pegawai.getKodejeniskelamin());
			cs.setString(7, pegawai.getGelardepan());
			cs.setString(8, pegawai.getGelarbelakang());
			cs.setInt(9, pegawai.getKodeagama());
			cs.setString(10, pegawai.getWebsite());
			cs.setString(11, pegawai.getHobi());
			cs.setInt(12, pegawai.getKodesukubangsa());
			cs.setInt(13, pegawai.getKodekewarganegaraan());
			cs.setString(14, pegawai.getTempatlahir());
			cs.setDate(15, new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(pegawai.getTgllahir()).getTime()));
			cs.setInt(16, pegawai.getKodestatusnikah());
			cs.setString(17, pegawai.getTelprumah());
			cs.setString(18, pegawai.getHp());
			cs.setString(19, pegawai.getHp2());
			cs.setString(20, pegawai.getEmail());
			cs.setString(21, pegawai.getEmail2());
			cs.setInt(22, pegawai.getKodesalutasi());
			cs.setString(23, pegawai.getKodeasalpenerimaan());
			cs.setString(24, pegawai.getKodedati2asal());
			cs.setString(25, pegawai.getKodedati2asal2());
			cs.setDate(26, new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(pegawai.getTmtmasuk()).getTime()));
			cs.setInt(27, pegawai.getKodestatuskaryawan());
			cs.setInt(28, pegawai.getRow_status());
			cs.setInt(29, pegawai.getIkutzakat());
			cs.setInt(30, pegawai.getAnggotakoperasi());
			cs.setInt(31, authUser.getUserid());
			cs.executeUpdate();
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
	
	private void updateKaryawan(AuthUser authUser, Pegawai pegawai) throws MyException {
		Connection con = null;
		CallableStatement cs = null;
		String query = null;
		try {
			query = "update karyawan.pegawai set \r\n" + 
					"	npp=?,\r\n" + 
					"	namadepan=?,\r\n" + 
					"	namatengah=?,\r\n" + 
					"	namabelakang=?,\r\n" + 
					"	namapanggilan=?,\r\n" + 
					"	kodejeniskelamin=?,\r\n" + 
					"	gelardepan=?,\r\n" + 
					"	gelarbelakang=?,\r\n" + 
					"	kodeagama=?,\r\n" + 
					"	website=?,\r\n" + 
					"	hobi=?,\r\n" + 
					"	kodesukubangsa=?,\r\n" + 
					"	kewarganegaraan=?,\r\n" + 
					"	tempatlahir=?,\r\n" + 
					"	tgllahir=?,\r\n" + 
					"	kodestatusnikah=?,\r\n" + 
					"	telprumah=?,\r\n" + 
					"	hp=?,\r\n" + 
					"	hp2=?,\r\n" + 
					"	email=?,\r\n" + 
					"	email2=?,\r\n" + 
					"	kodesalutasi=?,\r\n" + 
					"	kodeasalpenerimaan=?,\r\n" + 
					"	kodedati2asal=?,\r\n" + 
					"	kodedati2asal2=?,\r\n" + 
					"	tmtmasuk=?,\r\n" + 
					"	kodestatuskaryawan=?,\r\n" + 
					"	row_status=?,\r\n" + 
					"	ikutzakat=?,\r\n" + 
					"	anggotakoperasi=?,\r\n" + 
					"	lastmodified_by=?,\r\n" + 
					"	lastmodified_time=getdate()\r\n" + 
					" where npp=?";
			con = new Koneksi().getConnection();
			cs = con.prepareCall(query);
			cs.setString(1, pegawai.getNpp());
			cs.setString(2, pegawai.getNamadepan());
			cs.setString(3, pegawai.getNamatengah());
			cs.setString(4, pegawai.getNamabelakang());
			cs.setString(5, pegawai.getNamapanggilan());
			cs.setInt(6, pegawai.getKodejeniskelamin());
			cs.setString(7, pegawai.getGelardepan());
			cs.setString(8, pegawai.getGelarbelakang());
			cs.setInt(9, pegawai.getKodeagama());
			cs.setString(10, pegawai.getWebsite());
			cs.setString(11, pegawai.getHobi());
			cs.setInt(12, pegawai.getKodesukubangsa());
			cs.setInt(13, pegawai.getKodekewarganegaraan());
			cs.setString(14, pegawai.getTempatlahir());
			cs.setDate(15, new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(pegawai.getTgllahir()).getTime()));
			cs.setInt(16, pegawai.getKodestatusnikah());
			cs.setString(17, pegawai.getTelprumah());
			cs.setString(18, pegawai.getHp());
			cs.setString(19, pegawai.getHp2());
			cs.setString(20, pegawai.getEmail());
			cs.setString(21, pegawai.getEmail2());
			cs.setInt(22, pegawai.getKodesalutasi());
			cs.setString(23, pegawai.getKodeasalpenerimaan());
			cs.setString(24, pegawai.getKodedati2asal());
			cs.setString(25, pegawai.getKodedati2asal2());
			cs.setDate(26, new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(pegawai.getTmtmasuk()).getTime()));
			cs.setInt(27, pegawai.getKodestatuskaryawan());
			cs.setInt(28, pegawai.getRow_status());
			cs.setInt(29, pegawai.getIkutzakat());
			cs.setInt(30, pegawai.getAnggotakoperasi());
			cs.setInt(31, authUser.getUserid());
			cs.setString(32, pegawai.getNpp());
			cs.executeUpdate();
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
	
	private void insertDetilAlamat(String tbl, AuthUser authUser, String npp, DetilAlamat detilAlamat) throws MyException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = null;
		try {
			query = "insert into karyawan.detailalamat"+tbl+" (\r\n" + 
					"	npp,\r\n" + 
					"	alamat,\r\n" + 
					"	kodepos,\r\n" + 
					"	kodenegara,\r\n" + 
					"	kodepropinsi,\r\n" + 
					"	kodedati2,\r\n" + 
					"	kodekecamatan,\r\n" + 
					"	kodekelurahan,\r\n" + 
					"	rt,\r\n" + 
					"	rw,\r\n" + 
					"	created_by\r\n" + 
					") values (?,?,?,?,?,?,?,?,?,?,?)";
			con = new Koneksi().getConnection();
			ps = con.prepareStatement(query, new String[] { "kode" });
			ps.setString(1, npp);
			ps.setString(2, detilAlamat.getAlamat());
			ps.setString(3, detilAlamat.getKodepos());
			ps.setInt(4, detilAlamat.getKodenegara());
			ps.setString(5, detilAlamat.getKodepropinsi());
			ps.setString(6, detilAlamat.getKodedati2());
			ps.setString(7, detilAlamat.getKodekecamatan());
			ps.setString(8, detilAlamat.getKodekelurahan());
			ps.setString(9, detilAlamat.getRt());
			ps.setString(10, detilAlamat.getRw());
			ps.setInt(11, authUser.getUserid());
			ps.execute();
			rs = ps.getResultSet();
			if (rs == null)
				rs = ps.getGeneratedKeys();
			if (rs.next()) {
				detilAlamat.setKode(rs.getInt("kode"));
			}
		} catch (SQLException e) {
			throw new MyException(e.getMessage());
		} catch (NullPointerException e) {
			e.printStackTrace();
			throw new MyException(e.getMessage());
		} catch (Exception e) {
			throw new MyException(e.getMessage());
		}
		finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			} 
			if (ps != null) {
				try {
					ps.close();
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
	
	private void updateDetilAlamat(String tbl, AuthUser authUser, String npp, DetilAlamat detilAlamat) throws MyException {
		Connection con = null;
		CallableStatement cs = null;
		String query = null;
		try {
			query = "update karyawan.detailalamat"+tbl+" set \r\n" + 
					"	npp=?,\r\n" + 
					"	alamat=?,\r\n" + 
					"	kodepos=?,\r\n" + 
					"	kodenegara=?,\r\n" + 
					"	kodepropinsi=?,\r\n" + 
					"	kodedati2=?,\r\n" + 
					"	kodekecamatan=?,\r\n" + 
					"	kodekelurahan=?,\r\n" + 
					"	rt=?,\r\n" + 
					"	rw=?,\r\n" + 
					"	created_by=?\r\n" + 
					" where kode=?";
			con = new Koneksi().getConnection();
			cs = con.prepareCall(query);
			cs.setString(1, npp);
			cs.setString(2, detilAlamat.getAlamat());
			cs.setString(3, detilAlamat.getKodepos());
			cs.setInt(4, detilAlamat.getKodenegara());
			cs.setString(5, detilAlamat.getKodepropinsi());
			cs.setString(6, detilAlamat.getKodedati2());
			cs.setString(7, detilAlamat.getKodekecamatan());
			cs.setString(8, detilAlamat.getKodekelurahan());
			cs.setString(9, detilAlamat.getRt());
			cs.setString(10, detilAlamat.getRw());
			cs.setInt(11, authUser.getUserid());
			cs.setInt(12, detilAlamat.getKode());
			cs.executeUpdate();
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
	
	private void simpanFoto(AuthUser authUser, String npp, InputStream blob, String ext) throws MyException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs =null;
		try {
			con = new Koneksi().getConnection();
			ps = con.prepareStatement("select top 1 npp from karyawan.foto where npp = ?");
			ps.setString(1, npp);
			rs = ps.executeQuery();
			if (rs.next()) {
				ps.close();
				ps = con.prepareStatement("update karyawan.foto set ekstensi = ?, lampiran = ? where npp = ?");
				ps.setString(1, ext.toLowerCase());
				ps.setBlob(2, blob);
				ps.setString(3, npp);
			} else {
				ps.close();
				ps = con.prepareStatement("insert into karyawan.foto (npp, ekstensi, lampiran) values(?, ?, ?)");
				ps.setString(1, npp);
				ps.setString(2, ext);
				ps.setBlob(3, blob);
			}
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new MyException(e.getMessage());
		} catch (NullPointerException e) {
			e.printStackTrace();
			throw new MyException(e.getMessage());
		} catch (Exception e) {
			throw new MyException(e.getMessage());
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
			if (ps != null) {
				try {
					ps.close();
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
	
	private void simpanTtd(AuthUser authUser, String npp, InputStream blob, String ext) throws MyException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs =null;
		try {
			con = new Koneksi().getConnection();
			ps = con.prepareStatement("select top 1 npp from karyawan.tandatangan where npp = ?");
			ps.setString(1, npp);
			rs = ps.executeQuery();
			if (rs.next()) {
				ps.close();
				ps = con.prepareStatement("update karyawan.tandatangan set ekstensi = ?, lampiran = ? where npp = ?");
				ps.setString(1, ext.toLowerCase());
				ps.setBlob(2, blob);
				ps.setString(3, npp);
			} else {
				ps.close();
				ps = con.prepareStatement("insert into karyawan.tandatangan (npp, ekstensi, lampiran) values(?, ?, ?)");
				ps.setString(1, npp);
				ps.setString(2, ext);
				ps.setBlob(3, blob);
			}
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new MyException(e.getMessage());
		} catch (NullPointerException e) {
			e.printStackTrace();
			throw new MyException(e.getMessage());
		} catch (Exception e) {
			throw new MyException(e.getMessage());
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
			if (ps != null) {
				try {
					ps.close();
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
	
	@POST
	@Path("/setting/infopribadi/setkaryawan")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces("application/json")
	public Response setKaryawan(
			@Context HttpHeaders headers, 
			@FormDataParam("filephoto") final InputStream filephotoInputStream, 
			@FormDataParam("filephoto") FormDataContentDisposition filephotoDetail,
			@FormDataParam("filephoto") String filephoto,
			@FormDataParam("filettd") final InputStream filettdInputStream, 
			@FormDataParam("filettd") FormDataContentDisposition filettdDetail,
			@FormDataParam("filettd") String filettd,
			@FormDataParam("infopribadi") FormDataBodyPart post,
			@FormDataParam("alamatktp") FormDataBodyPart alamatktpPost,
			@FormDataParam("act") String act) {
		
		Map<String, Object> metadata = new HashMap<String, Object>();
		Map<String, Object> metadataobj = new HashMap<String, Object>();

		AuthUser authUser = new AuthUser();
		if (SharedMethod.VerifyToken(authUser, headers, metadata)) {
			try {
				post.setMediaType(MediaType.APPLICATION_JSON_TYPE);
				InfoPribadi json = post.getValueAs(InfoPribadi.class);
				
				if(act.equalsIgnoreCase("create")) {
					insertKaryawan(authUser, json.getPegawai());
					metadata.put("code", 1);
					metadata.put("message", "Simpan berhasil.");
				}
				else if(act.equalsIgnoreCase("update")) {
					updateKaryawan(authUser, json.getPegawai());
					metadata.put("code", 1);
					metadata.put("message", "Edit berhasil.");
				}
				
				if(json.getAlamatsaatini().getKode()!=null && json.getAlamatsaatini().getKode()>0) {
					updateDetilAlamat("", authUser, json.getPegawai().getNpp(), json.getAlamatsaatini());
					metadata.put("code", 1);
					metadata.put("message", "Edit berhasil.");
				}
				else {
					insertDetilAlamat("", authUser, json.getPegawai().getNpp(), json.getAlamatsaatini());
					metadata.put("code", 1);
					metadata.put("message", "Simpan berhasil.");
				}
				metadata.put("kodedetilalamat", json.getAlamatsaatini().getKode());
				
				
				if(json.getAlamatktp().getKode()!=null && json.getAlamatktp().getKode()>0) {
					updateDetilAlamat("2", authUser, json.getPegawai().getNpp(), json.getAlamatktp());
					metadata.put("code", 1);
					metadata.put("message", "Edit berhasil.");
				}
				else {
					insertDetilAlamat("2", authUser, json.getPegawai().getNpp(), json.getAlamatktp());
					metadata.put("code", 1);
					metadata.put("message", "Simpan berhasil.");
				}
				metadata.put("kodedetilalamat2", json.getAlamatktp().getKode());
				
				if(!(filephotoInputStream==null || filephotoDetail==null) && filephoto.length() > 0) {
					try {
						String namaFile = filephotoDetail.getFileName();
			        	StringTokenizer st = new StringTokenizer(namaFile, ".");
			        	String extension = ""; 
			        	while(st.hasMoreTokens()) {
			        		extension = "."+st.nextToken();
			        	}
						simpanFoto(authUser, json.getPegawai().getNpp(), filephotoInputStream, extension);
						metadata.put("code", 1);
						metadata.put("message", "Foto berhasil diupload");
					} catch (Exception e) {
						e.printStackTrace();
						throw new MyException(e.getMessage());
					}
				}
				
				if(!(filettdInputStream==null || filettdDetail==null) && filettd.length() > 0) {
					try {
						String namaFile = filettdDetail.getFileName();
			        	StringTokenizer st = new StringTokenizer(namaFile, ".");
			        	String extension = ""; 
			        	while(st.hasMoreTokens()) {
			        		extension = "."+st.nextToken();
			        	}
			        	simpanTtd(authUser, json.getPegawai().getNpp(), filettdInputStream, extension);
						metadata.put("code", 1);
						metadata.put("message", "Ttd berhasil diupload");
					} catch (Exception e) {
						e.printStackTrace();
						throw new MyException(e.getMessage());
					}
				}
			}
			catch (Exception e) {
				metadata.put("code", 0);
				metadata.put("message", "Simpan gagal.");
				e.printStackTrace();
			}
		}
		
		metadataobj.put("metadata", metadata);
		return Response.ok(metadataobj).build();
	}
	
	@POST
	@Path("/setting/{info}/{act}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces("application/json")
	public Response cInfo(
			@Context HttpHeaders headers,
			@PathParam("info") String info,
			@PathParam("act") String act,
			@FormDataParam("filelampiran") final InputStream fileInputStream, 
			@FormDataParam("filelampiran") FormDataContentDisposition fileDetail,
			@FormDataParam("filelampiran") String filelampiran,
			@FormDataParam("data") String data) {
		
		Map<String, Object> metadata = new HashMap<String, Object>();
		Map<String, Object> metadataobj = new HashMap<String, Object>();

		AuthUser authUser = new AuthUser();
		if (SharedMethod.VerifyToken(authUser, headers, metadata)) {
			
			try {
				if(!(fileInputStream==null || fileDetail==null) && filelampiran.length() > 0) {
					String namaFile = fileDetail.getFileName();
		        	StringTokenizer st = new StringTokenizer(namaFile, ".");
		        	String extension = ""; 
		        	while(st.hasMoreTokens()) {
		        		extension = "."+st.nextToken();
		        	}
		        	Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		        	namaFile = info + "-" + timestamp.getTime() + extension;
		        	
		        	String lampiranParam = "{\"lampiran\": \"" + namaFile + "\"}";
		        	data = TableCrud.setJsonStringParam(data, lampiranParam);
		        	
		        	FTPClient ftpClient = null;
		        	try {
		        		String pathFile = "file_" + info + "/";
						String host = context.getInitParameter("ftp-host");
			        	Integer port = Integer.parseInt(context.getInitParameter("ftp-port"));
			        	String user = context.getInitParameter("ftp-user");
			        	String pass = context.getInitParameter("ftp-pass");
		        		
						ftpClient = new FTPClient();
						ftpClient.connect(host,port);
			        	ftpClient.login(user, pass);
			        	ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			        	Boolean upload = ftpClient.storeFile(pathFile + namaFile, fileInputStream);
			        	if(!upload) {
							throw new MyException("Upload gagal");
			        	}
			        	ftpClient.logout();
					}
					catch (Exception e) {
						throw new MyException("Upload gagal");
					}
					finally {
						if(ftpClient.isConnected())
							ftpClient.disconnect();
					}
				}
				
				switch (act) {
					case "create":
						GeneratedKey generatedKey = TableCrud.insertData("karyawan."+info, data);
						if(generatedKey!=null) {
							metadata.put("kode", generatedKey.getKode());
						}
						break;
					case "update":
						TableCrud.updateData("karyawan."+info, data);
						break;
					case "delete":
						TableCrud.deleteData("karyawan."+info, data);
						break;
					default:
						break;
				}
				metadata.put("code", 1);
				metadata.put("message", "Ok");
			}
			catch (SQLException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
			}
			catch (MyException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
			}
			catch (Exception e) {
				metadata.put("code", 0);
				metadata.put("message", "Gagal");
				e.printStackTrace();
			}
		}
		
		metadataobj.put("metadata", metadata);
		return Response.ok(metadataobj).build();
	}
	
	@POST
	@Path("/setting/{info}/read/{page}/{row}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response rInfo(
			@Context HttpHeaders headers, 
			@PathParam("info") String info,
			@PathParam("page") Integer page, 
			@PathParam("row") Integer row, 
			String data
		) {
		Map<String, Object> response = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> listdata = null;
		Map<String, Object> metadata = new HashMap<String, Object>();
		Connection con = null;
		ResultSet rs = null;
		CallableStatement cs = null;
		String order = null;
		String filter = null;
		String query = null;

		AuthUser authUser = new AuthUser();
		if (SharedMethod.VerifyToken(authUser, headers, metadata)) {
			try {
				JsonNode json = null;
				if (data != null) {
					if (!data.isEmpty()) {
						ObjectMapper mapper = new ObjectMapper();
						json = mapper.readTree(data);

						order = json.path("sort").isMissingNode() ? null
								: SharedMethod.getSortedColumn(mapper.writeValueAsString(json.path("sort")));

						filter = json.path("filter").isMissingNode() ? null
								: SharedMethod.getFilteredColumn(mapper.writeValueAsString(json.path("filter")), null);

					}
				}
				query = "exec karyawan.sp_list" + info + " ?,?,?,?,?";
				con = new Koneksi().getConnection();
				cs = con.prepareCall(query);
				cs.setInt(1, page);
				cs.setInt(2, row);
				cs.setInt(3, 1);
				cs.setString(4, order);
				cs.setString(5, filter);
				rs = cs.executeQuery();
				metadata.put("code", 1);
				metadata.put("message", Response.Status.NO_CONTENT.toString());
				metadata.put("rowcount", 0);
				if (rs.next()) {
					metadata.put("rowcount", rs.getInt("jumlah"));
				}

				listdata = new ArrayList<Map<String, Object>>();

				rs.close();
				cs.close();
				
				cs = con.prepareCall(query);
				cs.setInt(1, page);
				cs.setInt(2, row);
				cs.setInt(3, 0);
				cs.setString(4, order);
				cs.setString(5, filter);
				rs = cs.executeQuery();
				ResultSetMetaData metaData = rs.getMetaData();
				Map<String, Object> hasil = null;
				while (rs.next()) {
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
					metadata.put("code", 1);
					metadata.put("message", "OK");
				}
				response.put("list", listdata);
				result.put("response", response);
				rs.close();
			} catch (SQLException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
			} catch (Exception e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				e.printStackTrace();
			} finally {
				if (rs != null) {
					try {
						rs.close();
					} catch (SQLException e) {
					}
				}

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
		result.put("metadata", metadata);
		return Response.ok(result).build();
	}
	
	@POST
	@Path("/grid/karyawan/{kodeoffice}/{page}/{row}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response gridKaryawan(
			@Context HttpHeaders headers, 
			@PathParam("kodeoffice") String kodeoffice, 
			@PathParam("page") String page, 
			@PathParam("row") String row, 
			String data) {
		Map<String, Object> response = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> listdata = null;
		Map<String, Object> metadata = new HashMap<String, Object>();
		Connection con = null;
		ResultSet rs = null;
		CallableStatement cs = null;
		String order = null;
		String filter = null;
		String query = null;

		if (SharedMethod.VerifyToken(headers, metadata)) {
			try {
				JsonNode json = null;
				if (data != null) {
					if (!data.isEmpty()) {
						ObjectMapper mapper = new ObjectMapper();
						json = mapper.readTree(data);

						order = json.path("sort").isMissingNode() ? null
								: SharedMethod.getSortedColumn(mapper.writeValueAsString(json.path("sort")));

						filter = json.path("filter").isMissingNode() ? null
								: SharedMethod.getFilteredColumn(mapper.writeValueAsString(json.path("filter")), null);
					}
				}
				
				if(kodeoffice.equalsIgnoreCase("00")) {
					query = "exec karyawan.sp_listpegawai ?, ?, ?, ?, ?";
				}
				else {
					query = "exec karyawan.sp_listpegawaibykantor ?, ?, ?, ?, ?, ?, ?";
				}
				
				con = new Koneksi().getConnection();
				cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
				cs.setInt(1, Integer.parseInt(page));
				cs.setInt(2, Integer.parseInt(row));
				cs.setInt(3, 1);
				cs.setString(4, order);
				cs.setString(5, filter);
				if(!kodeoffice.equalsIgnoreCase("00")) {
					cs.setNull(6, java.sql.Types.VARCHAR);
					cs.setString(7, String.format("kode = '%s'", kodeoffice));
				}
				cs.execute();
				rs = cs.getResultSet();
				metadata.put("code", 1);
				metadata.put("message", Response.Status.NO_CONTENT.toString());
				metadata.put("rowcount", 0);

				if (rs.next()) {
					metadata.put("rowcount", rs.getInt("jumlah"));
				}

				listdata = new ArrayList<Map<String, Object>>();

				rs.close();
				cs.close();
				
				cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
				cs.setInt(1, Integer.parseInt(page));
				cs.setInt(2, Integer.parseInt(row));
				cs.setInt(3, 0);
				cs.setString(4, order);
				cs.setString(5, filter);
				if(!kodeoffice.equalsIgnoreCase("00")) {
					cs.setNull(6, java.sql.Types.VARCHAR);
					cs.setString(7, String.format("kode = '%s'", kodeoffice));
				}
				cs.execute();
				rs = cs.getResultSet();
				ResultSetMetaData metaData = rs.getMetaData();
				Map<String, Object> hasil = null;

				String npp = null;
				while (rs.next()) {
					hasil = new HashMap<String, Object>();
					for (int i = 1; i <= metaData.getColumnCount(); i++) {
						if(rs.getObject(i)!=null && metaData.getColumnTypeName(i).equalsIgnoreCase("date")){
							hasil.put(metaData.getColumnName(i).toLowerCase(), Utils.SqlDateToSqlString(rs.getDate(i)));
						}
						else {
							hasil.put(metaData.getColumnName(i).toLowerCase(), rs.getObject(i));
						}
						if(metaData.getColumnName(i).equalsIgnoreCase("npp")) {
							npp = rs.getObject(i).toString();
						}
					}
					
					ResultSet rs2 = null;
					CallableStatement cs2 = null;
					String query2 = null;
					try {
						query2 = "select top 1 a.npp, iif(b.kode is null,0,1) as ada_photo, iif(c.kode is null,0,1) as ada_ttd \r\n" + 
								"from karyawan.pegawai a \r\n" + 
								"left join karyawan.foto b on a.npp=a.npp \r\n" + 
								"left join karyawan.tandatangan c on a.npp=c.npp \r\n" + 
								"where a.npp=?";
						cs2 = con.prepareCall(query2, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
						cs2.setString(1, npp);
						rs2 = cs2.executeQuery();
						if(rs2.next()) {
							hasil.put("ada_photo", rs2.getBoolean("ada_photo"));
							hasil.put("ada_ttd", rs2.getBoolean("ada_ttd"));
						}
					} finally {
						if (rs2 != null) {
							try {
								rs2.close();
							} catch (SQLException e) {
							}
						}
						if (cs2 != null) {
							try {
								cs2.close();
							} catch (SQLException e) {
							}
						}
					}
					
					listdata.add(hasil);
					metadata.put("code", 1);
					metadata.put("message", "OK");
				}
				response.put("list", listdata);
				result.put("response", response);
				rs.close();
			} catch (SQLException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
			} catch (Exception e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				e.printStackTrace();
			} finally {
				if (rs != null) {
					try {
						rs.close();
					} catch (SQLException e) {
					}
				}

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
		result.put("metadata", metadata);
		return Response.ok(result).build();
	}
	
	@GET
	@Path("/installstep/npp/{npp}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response getInstallStep(
			@Context HttpHeaders headers, 
			@PathParam("npp") String npp
		) {
		Map<String, Object> response = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> metadata = new HashMap<String, Object>();
		
		if (SharedMethod.VerifyToken(headers, metadata)) {
			
			Connection con = null;
			ResultSet rs = null;
			CallableStatement cs = null;
			String query = null;
			Map<String, Object> hasil = null;
			try {
				metadata.put("code", 2);
				metadata.put("message", Response.Status.NO_CONTENT.toString());
				
				query = "select install, install_step from karyawan.pegawai where npp=?";
				con = new Koneksi().getConnection();
				cs = con.prepareCall(query);
				cs.setString(1, npp);
				rs = cs.executeQuery();
				
				if (rs.next()) {
					hasil = new HashMap<String, Object>();
					hasil.put("install", rs.getInt("install"));
					hasil.put("install_step", rs.getInt("install_step"));
					metadata.put("code", 1);
					metadata.put("message", "OK");
					response.put("data", hasil);
					result.put("response", response);
				}
			} catch (SQLException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
			} catch (Exception e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				e.printStackTrace();
			} finally {
				if (rs != null) {
					try {
						rs.close();
					} catch (SQLException e) {
					}
				}

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
		result.put("metadata", metadata);
		return Response.ok(result).build();
	}
	
	@POST
	@Path("/setinstallstep")
	@Consumes("application/json")
	@Produces("application/json")
	public Response setInstallStep(@Context HttpHeaders headers, String data) {
		ObjectMapper mapper = new ObjectMapper();
		Metadata metadata = new Metadata();
		Result2 result = new Result2();
		Connection con = null;
		CallableStatement cs = null;
		
		if (SharedMethod.VerifyToken(headers, metadata)) {
			try {
				JsonNode json = mapper.readTree(data);
				String npp = json.path("npp").asText();
				Integer level = json.path("level").asInt();
				
				String query = "update karyawan.pegawai set install_step=? where npp=?";
				con = new Koneksi().getConnection();
				cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
				cs.setInt(1, level);
				cs.setString(2, npp);
				cs.executeUpdate();
				metadata.setCode(1);
				metadata.setMessage("Ok");
			} catch (SQLException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
			} catch (NamingException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
			} catch (SecurityException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
			} catch (Exception e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
			} finally {
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
		result.setMetadata(metadata);
		return Response.ok(result).build();
	}
	
	@POST
	@Path("/setinstall")
	@Consumes("application/json")
	@Produces("application/json")
	public Response setInstall(@Context HttpHeaders headers, String data) {
		ObjectMapper mapper = new ObjectMapper();
		Metadata metadata = new Metadata();
		Result2 result = new Result2();
		Connection con = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		
		if (SharedMethod.VerifyToken(headers, metadata)) {
			try {
				JsonNode json = mapper.readTree(data);
				String npp = json.path("npp").asText();
				Integer totalstep = json.path("totalstep").asInt();
				
				String query = "select install_step from karyawan.pegawai where npp=?";
				con = new Koneksi().getConnection();
				cs = con.prepareCall(query);
				cs.setString(1, npp);
				rs = cs.executeQuery();
				if(rs.next()) {
					if(rs.getInt("install_step") >= 6) {
						cs.close();
						query = "update karyawan.pegawai set install_step=?, install=? where npp=?";
						cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
						cs.setInt(1, totalstep);
						cs.setInt(2, 1);
						cs.setString(3, npp);
						cs.executeUpdate();
						metadata.setCode(1);
						metadata.setMessage("Ok");
					}
					else {
						metadata.setCode(0);
						metadata.setMessage("Lengkapi data terlebih dahulu");
					}
				}
				else {
					metadata.setCode(0);
					metadata.setMessage("Oops. Gagal");
				}
				
				
				
			} catch (SQLException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
			} catch (NamingException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
			} catch (SecurityException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
			} catch (Exception e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
			} finally {
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
		result.setMetadata(metadata);
		return Response.ok(result).build();
	}
	
}