package id.go.bpjskesehatan.service.v2.organisasi;

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

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import id.go.bpjskesehatan.database.Koneksi;
import id.go.bpjskesehatan.entitas.Metadata;
import id.go.bpjskesehatan.entitas.Respon;
import id.go.bpjskesehatan.entitas.Result;
import id.go.bpjskesehatan.service.mobile.v1.AuthMobile;
import id.go.bpjskesehatan.service.mobile.v1.AuthUser;
import id.go.bpjskesehatan.service.v2.karyawan.entitas.Pegawai;
import id.go.bpjskesehatan.service.v2.organisasi.entitas.Pair;
import id.go.bpjskesehatan.service.v2.organisasi.entitas.SimpanPenugasan;
import id.go.bpjskesehatan.service.v2.organisasi.entitas.Tree;
import id.go.bpjskesehatan.util.MyException;
import id.go.bpjskesehatan.util.SharedMethod;
import id.go.bpjskesehatan.util.Utils;


@Path("/v2/organisasi")
public class Organisasi2Rest {	
	
	@Context
    private ServletContext context;
	
	@GET
	@Path("/getatasan/{kodepenugasan}/{kodejobtitleatasan: .*}")
	@Produces("application/json")
	public Response GetAtasan(@Context HttpHeaders headers, @PathParam("kodepenugasan") Integer kodepenugasan,
			@PathParam("kodejobtitleatasan") String kodejobtitleatasan) {
		
		Map<String, Object> response = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> metadata = new HashMap<String, Object>();
		
		if (SharedMethod.VerifyToken(headers, metadata) || AuthMobile.VerifyToken(headers, metadata)) {
			try {
				Boolean ok = true;
				Boolean ok2 = true;
				
				if(ok && ok2){
					Connection con = null;
					ResultSet rs = null;
					CallableStatement cs = null;
					String query = null;
					try {
						query = "exec organisasi.sp_getAtasan ?, ?";
						con = new Koneksi().getConnection();
						cs = con.prepareCall(query);
						cs.setInt(1, kodepenugasan);
						if(kodejobtitleatasan.isEmpty()) {
							cs.setNull(2, java.sql.Types.VARCHAR);
						}
						else {
							cs.setString(2, kodejobtitleatasan);
						}
						rs = cs.executeQuery();
						ResultSetMetaData metaData = rs.getMetaData();
						Map<String, Object> hasil = null;
						List<Map<String, Object>> listdata = new ArrayList<Map<String, Object>>();
						metadata.put("code", 2);
						metadata.put("message", "Tidak ada lagi atasan");
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
							metadata.put("message", "Ok");
						}
						response.put("list", listdata);
						result.put("response", response);
					}
					catch (Exception e) {
						throw new Exception(e.getMessage());
					}
					finally {
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
			} catch (Exception e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				e.printStackTrace();
			} 
		}
		
		result.put("metadata", metadata);
		return Response.ok(result).build();
	}
	
	@POST
	@Path("/grid/penugasan/{page}/{row}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response gridPenugasan(
			@Context HttpHeaders headers,  
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
				String npp = null;
				String kodeunitkerja = null;
				String kodelokasikantor = null;
				Integer tipepenugasan = 0;
				
				JsonNode json = null;
				if (data != null) {
					if (!data.isEmpty()) {
						ObjectMapper mapper = new ObjectMapper();
						json = mapper.readTree(data);
						
						if(!json.path("npp").isMissingNode()) {
							npp = json.path("npp").asText();
							//String addParam = "{\"npp\": \"=" + npp + "\"}";
							//data = TableCrud.setJsonStringParam(data, addParam);
				        	//json = mapper.readTree(data);
						}
						
						if(!json.path("kodeunitkerja").isMissingNode()) {
							if(!json.path("kodeunitkerja").asText().equals(""))
								kodeunitkerja = json.path("kodeunitkerja").asText();
						}
						if(!json.path("kodelokasikantor").isMissingNode()) {
							if(!json.path("kodelokasikantor").asText().equals(""))
								kodelokasikantor = json.path("kodelokasikantor").asText();
						}
						tipepenugasan = json.path("tipepenugasan").asInt();

						order = json.path("sort").isMissingNode() ? null
								: SharedMethod.getSortedColumn(mapper.writeValueAsString(json.path("sort")));

						filter = json.path("filter").isMissingNode() ? null
								: SharedMethod.getFilteredColumn(mapper.writeValueAsString(json.path("filter")), null);
					}
				}
				
				if(tipepenugasan==1) {
					query = "exec karyawan.sp_listvwpenugasan ?, ?, ?, ?, ?, ?, ?";
				}
				else if(tipepenugasan==2) {
					query = "exec karyawan.sp_listpenugasanall ?, ?, ?, ?, ?, ?, ?";
				}
				else if(tipepenugasan==3) {
					query = "exec karyawan.sp_listpenugasanrangkapjabatan ?, ?, ?, ?, ?, ?, ?";
				}
				else if(tipepenugasan==4) {
					query = "exec karyawan.sp_listpenugasannonaktif ?, ?, ?, ?, ?, ?, ?";
				}
				else if(tipepenugasan==5) {
					query = "exec karyawan.sp_listpenugasan_terakhir ?, ?, ?, ?, ?, ?, ?";
				}
				
				con = new Koneksi().getConnection();
				cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
				cs.setInt(1, Integer.parseInt(page));
				cs.setInt(2, Integer.parseInt(row));
				cs.setInt(3, 1);
				cs.setString(4, order);
				if(npp==null) {
					cs.setString(5, filter);
				}
				else {
					cs.setString(5, String.format("npp = '%s'", npp));
				}
				if(kodeunitkerja==null) {
					cs.setNull(6, java.sql.Types.VARCHAR);
				}
				else {
					cs.setString(6, String.format("kode = '%s'", kodeunitkerja));
				}
				if(kodelokasikantor==null) {
					cs.setNull(7, java.sql.Types.VARCHAR);
				}
				else {
					cs.setString(7, String.format("kode = '%s'", kodelokasikantor));
				}
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
				
				cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
				cs.setInt(1, Integer.parseInt(page));
				cs.setInt(2, Integer.parseInt(row));
				cs.setInt(3, 0);
				cs.setString(4, order);
				if(npp==null) {
					cs.setString(5, filter);
				}
				else {
					cs.setString(5, String.format("npp = '%s'", npp));
				}
				if(kodeunitkerja==null) {
					cs.setNull(6, java.sql.Types.VARCHAR);
				}
				else {
					cs.setString(6, String.format("kode = '%s'", kodeunitkerja));
				}
				if(kodelokasikantor==null) {
					cs.setNull(7, java.sql.Types.VARCHAR);
				}
				else {
					cs.setString(7, String.format("kode = '%s'", kodelokasikantor));
				}
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
						if(metaData.getColumnName(i).equalsIgnoreCase("lampiran")) {
							if(rs.getObject(i)!=null && rs.getObject(i).toString()!="") {
								hasil.put("attachments", true);
							}
							else {
								hasil.put("attachments", false);
							}
						}
						else if(metaData.getColumnName(i).equalsIgnoreCase("row_status")) {
							if(Integer.parseInt(rs.getObject(i).toString())==1) {
								hasil.put("kodestatus", true);
								hasil.put("namastatus", "Aktif");
								hasil.put("pengaruhkejabatansaatini", true);
							}
							else {
								hasil.put("kodestatus", false);
								hasil.put("namastatus", "Non aktif");
								hasil.put("pengaruhkejabatansaatini", false);
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
	
	private void insertPegawaiNonaktif(Integer kodepenugasan, String deskripsi, Integer kodejenisalasanresign) throws MyException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = null;
		try {
			query = "insert into karyawan.pegawainonaktif(kodepenugasan, deskripsi, kodejenisalasanresign) values (?, ?, ?)";
			con = new Koneksi().getConnection();
			ps = con.prepareStatement(query);
			ps.setInt(1, kodepenugasan);
			ps.setString(2, deskripsi);
			ps.setInt(3, kodejenisalasanresign);
			ps.executeUpdate();
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
	
	@POST
	@Path("/nonaktifkanpenugasan")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces("application/json")
	public Response NonAktifkanPenugasan(
			@Context HttpHeaders headers, 
			@FormDataParam("filelampiran") final InputStream filePenugasanInputStream, 
			@FormDataParam("filelampiran") FormDataContentDisposition filePenugasanDetail,
			@FormDataParam("filelampiran") String filePenugasan,
			@FormDataParam("postdata") FormDataBodyPart post,
			@PathParam("tambahan") String tambahan) {
		Connection con = null;
		PreparedStatement cs = null;
		ResultSet rs = null;
		Result<Pegawai> result = new Result<Pegawai>();
		Metadata metadata = new Metadata();
		String queri = null;
		
		AuthUser authUser = new AuthUser();
		if (SharedMethod.VerifyToken(authUser, headers, metadata)) {
			try {
				post.setMediaType(MediaType.APPLICATION_JSON_TYPE);
				id.go.bpjskesehatan.service.v2.organisasi.entitas.NonAktifkanPenugasan json = post.getValueAs(id.go.bpjskesehatan.service.v2.organisasi.entitas.NonAktifkanPenugasan.class);
				
				switch (json.getJenisnonaktif()) {
				case 1:
					try {
						queri = "update karyawan.penugasan set tatjabatan=?, row_status=0, ismutation=?, lastmodified_by=?, lastmodified_time=getdate() where kode=?";
						con = new Koneksi().getConnection();
						cs = con.prepareStatement(queri);
						cs.setDate(1, new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(json.getTanggal()).getTime()));
						cs.setInt(2, json.getIsmutation());
						cs.setInt(3, authUser.getUserid());
						cs.setInt(4, json.getPenugasan().getKode());
						cs.executeUpdate();
						metadata.setCode(1);
						metadata.setMessage("Ok.");
					} catch (Exception e) {
						metadata.setCode(0);
						metadata.setMessage(e.getMessage());
						e.printStackTrace();
					} finally {
						if (cs != null) {
							try {
								cs.close();
							} catch (SQLException e) {
								e.printStackTrace();
							}
						}
						if (con != null) {
							try {
								con.close();
							} catch (SQLException e) {
								e.printStackTrace();
							}
						}	
					}
					break;
				case 0:					
					try {
						Integer kodepenugasan = 0;
						queri = "insert into karyawan.penugasan(npp,kodehirarkijabatan,kodeoffice,tanggalsk,kodejenissk,nomorsk,tmtjabatan,kodesubgrade,kodestatusjabatan,tanggalmulai,created_by,row_status) values (?,?,?,?,?,?,?,?,?,?,?,?)";
						con = new Koneksi().getConnection();
						cs = con.prepareStatement(queri, new String[] { "kode" });
						cs.setString(1, json.getPenugasan().getNpp());
						cs.setString(2, json.getPenugasan().getKodejabatan());
						cs.setString(3, json.getPenugasan().getKodeoffice());
						cs.setDate(4, Utils.StringDateToSqlDate(json.getTanggal()));
						cs.setInt(5, json.getKodejenissk());
						cs.setString(6, json.getNomorsk());
						cs.setDate(7, Utils.StringDateToSqlDate(json.getTanggal()));
						cs.setString(8, json.getPenugasan().getKodesubgrade());
						cs.setInt(9, json.getPenugasan().getKodestatusjabatan());
						cs.setDate(10, Utils.StringDateToSqlDate(json.getTanggal()));
						cs.setInt(11, authUser.getUserid());
						cs.setInt(12, 0);
						cs.execute();
						rs = cs.getResultSet();
						if (rs == null)
							rs = cs.getGeneratedKeys();
						if (rs.next()) {
							kodepenugasan = Integer.parseInt(rs.getString(1));
						}
						if(kodepenugasan!=0) {
							insertPegawaiNonaktif(kodepenugasan, json.getDeskripsi(), json.getKodejenisalasan());
						}
						metadata.setCode(1);
						metadata.setMessage("Ok.");
					} catch (Exception e) {
						metadata.setCode(0);
						metadata.setMessage(e.getMessage());
						e.printStackTrace();
					} finally {
						if (cs != null) {
							try {
								cs.close();
							} catch (SQLException e) {
								e.printStackTrace();
							}
						}
						if (con != null) {
							try {
								con.close();
							} catch (SQLException e) {
								e.printStackTrace();
							}
						}	
					}
					
					if(!(filePenugasanInputStream==null || filePenugasanDetail==null) && filePenugasan.length() > 0) {
						String pathFile = "file_penugasan/";
						String host = context.getInitParameter("ftp-host");
						Integer port = Integer.parseInt(context.getInitParameter("ftp-port"));
						String user = context.getInitParameter("ftp-user");
						String pass = context.getInitParameter("ftp-pass");
						
						String namaFile = filePenugasanDetail.getFileName();
			        	StringTokenizer st = new StringTokenizer(namaFile, ".");
			        	String extension = ""; 
			        	while(st.hasMoreTokens()) {
			        		extension = "."+st.nextToken();
			        	}
			        	Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			        	namaFile = json.getPenugasan().getKode() + "-" + timestamp.getTime() + extension;
			        	
			        	FTPClient ftpClient = null;
			        	try {
							ftpClient = new FTPClient();
							ftpClient.connect(host,port);
				        	ftpClient.login(user, pass);
				        	ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
				        	Boolean upload = ftpClient.storeFile(pathFile + namaFile, filePenugasanInputStream);
				        	if(!upload) {
				        		
				        	}
				        	ftpClient.logout();
						}
						catch (Exception e) {
							e.printStackTrace();
						}
						finally {
							if(ftpClient.isConnected())
								ftpClient.disconnect();
						}
					}
					
					break;
				default:
					break;
				}
				
			} catch (Exception e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
				e.printStackTrace();
			}
		}
		result.setMetadata(metadata);
		return Response.ok(result).build();
	}
	
	private void insertPenugasan(AuthUser authUser, SimpanPenugasan json) throws MyException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = null;
		try {
			Integer row_status = 0;
			Integer ishistory = 1;
			if(json.getPengaruhkejabatansaatini()==1) {
				row_status = 1;
				ishistory = 0;
			}
			
			query = "insert into karyawan.penugasan (\r\n" + 
					"kodehirarkijabatan,\r\n" + 
					"lampiran,\r\n" + 
					"npp,\r\n" + 
					"tanggalsk,\r\n" + 
					"kodejenissk,\r\n" + 
					"nomorsk,\r\n" + 
					"tmtjabatan,\r\n" + 
					"kodesubgrade,\r\n" + 
					"kodestatusjabatan,\r\n" + 
					"masapercobaan,\r\n" + 
					"tanggalmulai,\r\n" + 
					"kodeoffice,\r\n" + 
					"created_by,\r\n" + 
					"row_status,\r\n" + 
					"ishistory,\r\n" + 
					"ispenundaan,\r\n" + 
					"isaps,\r\n" + 
					"alasanpenundaan,\r\n" + 
					"lampiranpenundaan\r\n" + 
					") values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			con = new Koneksi().getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, json.getKodejobtitle());
			ps.setString(2, json.getLampiran());
			ps.setString(3, json.getNpp());
			ps.setDate(4, Utils.StringDateToSqlDate(json.getTanggalsk()));
			ps.setInt(5, json.getKodejenissk());
			ps.setString(6, json.getNomorsk());
			ps.setDate(7, Utils.StringDateToSqlDate(json.getTmtjabatan()));
			ps.setString(8, json.getKodesubgrade());
			ps.setInt(9, json.getKodestatusjabatan());
			ps.setInt(10, json.getMasapercobaan());
			ps.setDate(11, Utils.StringDateToSqlDate(json.getTanggalmulai()));
			ps.setString(12, json.getKodeoffice());
			ps.setInt(13, authUser.getUserid());
			ps.setInt(14, row_status);
			ps.setInt(15, ishistory);
			ps.setInt(16, json.getIspenundaan());
			ps.setInt(17, json.getIsaps());
			ps.setString(18, json.getAlasanpenundaan());
			ps.setString(19, json.getLampiranpenundaan());
			ps.execute();
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
	
	private void updatePenugasan(AuthUser authUser, SimpanPenugasan json) throws MyException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = null;
		try {
			query = "update karyawan.penugasan set \r\n" + 
					"kodehirarkijabatan=?,\r\n" + 
					"lampiran=?,\r\n" + 
					"npp=?,\r\n" + 
					"tanggalsk=?,\r\n" + 
					"kodejenissk=?,\r\n" + 
					"nomorsk=?,\r\n" + 
					"tmtjabatan=?,\r\n" + 
					"kodesubgrade=?,\r\n" + 
					"kodestatusjabatan=?,\r\n" + 
					"masapercobaan=?,\r\n" + 
					"tanggalmulai=?,\r\n" + 
					"kodeoffice=?,\r\n" + 
					"lastmodified_by=?,\r\n" + 
					"ispenundaan=?,\r\n" + 
					"isaps=?,\r\n" + 
					"alasanpenundaan=?,\r\n" + 
					"lampiranpenundaan=?\r\n" + 
					" where kode=?";
			con = new Koneksi().getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, json.getKodejobtitle());
			ps.setString(2, json.getLampiran());
			ps.setString(3, json.getNpp());
			ps.setDate(4, Utils.StringDateToSqlDate(json.getTanggalsk()));
			ps.setInt(5, json.getKodejenissk());
			ps.setString(6, json.getNomorsk());
			ps.setDate(7, Utils.StringDateToSqlDate(json.getTmtjabatan()));
			ps.setString(8, json.getKodesubgrade());
			ps.setInt(9, json.getKodestatusjabatan());
			ps.setInt(10, json.getMasapercobaan());
			ps.setDate(11, Utils.StringDateToSqlDate(json.getTanggalmulai()));
			ps.setString(12, json.getKodeoffice());
			ps.setInt(13, authUser.getUserid());
			ps.setInt(14, json.getIspenundaan());
			ps.setInt(15, json.getIsaps());
			ps.setString(16, json.getAlasanpenundaan());
			ps.setString(17, json.getLampiranpenundaan());
			ps.setInt(18, json.getKode());
			ps.execute();
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
	
	private void insertPenugasanNonBagan(AuthUser authUser, SimpanPenugasan json) throws MyException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = null;
		try {
			query = "insert into karyawan.penugasannonbagan (\r\n" + 
					"namajabatan,\r\n" + 
					"lampiran,\r\n" + 
					"npp,\r\n" + 
					"tanggalsk,\r\n" + 
					"kodejenissk,\r\n" + 
					"nomorsk,\r\n" + 
					"tmtjabatan,\r\n" + 
					"kodesubgrade,\r\n" + 
					"kodestatusjabatan,\r\n" + 
					"masapercobaan,\r\n" + 
					"tanggalmulai,\r\n" + 
					"kodeoffice,\r\n" + 
					"created_by,\r\n" + 
					"namaunitkerja,\r\n" + 
					"row_status\r\n" + 
					") values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			con = new Koneksi().getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, json.getNamajabatanmanual());
			ps.setString(2, json.getLampiran());
			ps.setString(3, json.getNpp());
			ps.setDate(4, Utils.StringDateToSqlDate(json.getTanggalsk()));
			ps.setInt(5, json.getKodejenissk());
			ps.setString(6, json.getNomorsk());
			ps.setDate(7, Utils.StringDateToSqlDate(json.getTmtjabatan()));
			ps.setString(8, json.getKodesubgrade());
			ps.setInt(9, json.getKodestatusjabatan());
			ps.setInt(10, json.getMasapercobaan());
			ps.setDate(11, Utils.StringDateToSqlDate(json.getTanggalmulai()));
			ps.setString(12, json.getKodeoffice());
			ps.setInt(13, authUser.getUserid());
			ps.setString(14, json.getNamaunitkerjamanual());
			ps.setInt(15, 0);
			ps.executeUpdate();
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
	
	private void updatePenugasanNonBagan(AuthUser authUser, SimpanPenugasan json) throws MyException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = null;
		try {
			query = "update karyawan.penugasannonbagan set \r\n" + 
					"namajabatan=?,\r\n" + 
					"lampiran=?,\r\n" + 
					"npp=?,\r\n" + 
					"tanggalsk=?,\r\n" + 
					"kodejenissk=?,\r\n" + 
					"nomorsk=?,\r\n" + 
					"tmtjabatan=?,\r\n" + 
					"kodesubgrade=?,\r\n" + 
					"kodestatusjabatan=?,\r\n" + 
					"masapercobaan=?,\r\n" + 
					"tanggalmulai=?,\r\n" + 
					"kodeoffice=?,\r\n" + 
					"lastmodified_by=?,\r\n" + 
					"namaunitkerja=?,\r\n" + 
					"row_status=?\r\n" + 
					" where kode=?";
			con = new Koneksi().getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, json.getNamajabatanmanual());
			ps.setString(2, json.getLampiran());
			ps.setString(3, json.getNpp());
			ps.setDate(4, Utils.StringDateToSqlDate(json.getTanggalsk()));
			ps.setInt(5, json.getKodejenissk());
			ps.setString(6, json.getNomorsk());
			ps.setDate(7, Utils.StringDateToSqlDate(json.getTmtjabatan()));
			ps.setString(8, json.getKodesubgrade());
			ps.setInt(9, json.getKodestatusjabatan());
			ps.setInt(10, json.getMasapercobaan());
			ps.setDate(11, Utils.StringDateToSqlDate(json.getTanggalmulai()));
			ps.setString(12, json.getKodeoffice());
			ps.setInt(13, authUser.getUserid());
			ps.setString(14, json.getNamaunitkerjamanual());
			ps.setInt(15, 0);
			ps.executeUpdate();
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
	
	@POST
	@Path("/penugasan/simpan")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces("application/json")
	public Response simpanPenugasan(
			@Context HttpHeaders headers, 
			@FormDataParam("filelampiran") final InputStream filePenugasanInputStream, 
			@FormDataParam("filelampiran") FormDataContentDisposition filePenugasanDetail,
			@FormDataParam("filelampiran") String filePenugasan,
			@FormDataParam("filelampiranpenundaan") final InputStream filePenundaanInputStream, 
			@FormDataParam("filelampiranpenundaan") FormDataContentDisposition filePenundaanDetail,
			@FormDataParam("filelampiranpenundaan") String filePenundaan,
			@FormDataParam("postdata") FormDataBodyPart post) {
		Result<Pegawai> result = new Result<Pegawai>();
		Metadata metadata = new Metadata();
		
		AuthUser authUser = new AuthUser();
		if (SharedMethod.VerifyToken(authUser, headers, metadata)) {
			try {
				post.setMediaType(MediaType.APPLICATION_JSON_TYPE);
				SimpanPenugasan json = post.getValueAs(SimpanPenugasan.class);
				
				if(!(filePenugasanInputStream==null || filePenugasanDetail==null) && filePenugasan.length() > 0) {
					String pathFile = "file_penugasan/";
					String host = context.getInitParameter("ftp-host");
					Integer port = Integer.parseInt(context.getInitParameter("ftp-port"));
					String user = context.getInitParameter("ftp-user");
					String pass = context.getInitParameter("ftp-pass");
					
					String namaFile = filePenugasanDetail.getFileName();
		        	StringTokenizer st = new StringTokenizer(namaFile, ".");
		        	String extension = ""; 
		        	while(st.hasMoreTokens()) {
		        		extension = "."+st.nextToken();
		        	}
		        	Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		        	namaFile = json.getNpp() + "-" + timestamp.getTime() + extension;
		        	json.setLampiran(namaFile);
		        	
		        	FTPClient ftpClient = null;
		        	try {
						ftpClient = new FTPClient();
						ftpClient.connect(host,port);
			        	ftpClient.login(user, pass);
			        	ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			        	ftpClient.enterLocalPassiveMode();
			        	ftpClient.setBufferSize(0);
			        	Boolean upload = ftpClient.storeFile(pathFile + namaFile, filePenugasanInputStream);
			        	if(!upload) {
			        		
			        	}
			        	ftpClient.logout();
					}
					catch (Exception e) {
						e.printStackTrace();
					}
					finally {
						if(ftpClient.isConnected())
							ftpClient.disconnect();
					}
				}
				
				if(!(filePenundaanInputStream==null || filePenundaanDetail==null) && filePenundaan.length() > 0) {
					String pathFile = "file_penundaan/";
					String host = context.getInitParameter("ftp-host");
					Integer port = Integer.parseInt(context.getInitParameter("ftp-port"));
					String user = context.getInitParameter("ftp-user");
					String pass = context.getInitParameter("ftp-pass");
					
					String namaFile = filePenundaanDetail.getFileName();
		        	StringTokenizer st = new StringTokenizer(namaFile, ".");
		        	String extension = ""; 
		        	while(st.hasMoreTokens()) {
		        		extension = "."+st.nextToken();
		        	}
		        	Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		        	namaFile = json.getNpp() + "-" + timestamp.getTime() + extension;
		        	json.setLampiranpenundaan(namaFile);
		        	
		        	FTPClient ftpClient = null;
		        	try {
						ftpClient = new FTPClient();
						ftpClient.connect(host,port);
			        	ftpClient.login(user, pass);
			        	ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			        	ftpClient.enterLocalPassiveMode();
			        	ftpClient.setBufferSize(0);
			        	Boolean upload = ftpClient.storeFile(pathFile + namaFile, filePenundaanInputStream);
			        	if(!upload) {
			        		
			        	}
			        	ftpClient.logout();
					}
					catch (Exception e) {
						e.printStackTrace();
					}
					finally {
						if(ftpClient.isConnected())
							ftpClient.disconnect();
					}
				}
				
				switch (json.getAct()) {
				case "create":
					try {
						if(json.getIsnonbagan()==1) {
							insertPenugasanNonBagan(authUser, json);
						}
						else {
							insertPenugasan(authUser, json);
						}
						metadata.setCode(1);
						metadata.setMessage("Ok.");
					} catch (Exception e) {
						metadata.setCode(0);
						metadata.setMessage(e.getMessage());
						e.printStackTrace();
					} 
					break;
				case "update":	
					try {
						if(json.getIsnonbagan()==1) {
							updatePenugasanNonBagan(authUser, json);
						}
						else {
							updatePenugasan(authUser, json);
						}
						metadata.setCode(1);
						metadata.setMessage("Ok.");
					} catch (Exception e) {
						metadata.setCode(0);
						metadata.setMessage(e.getMessage());
						e.printStackTrace();
					}
					break;
				default:
					break;
				}
				
			} catch (Exception e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
				e.printStackTrace();
			}
		}
		result.setMetadata(metadata);
		return Response.ok(result).build();
	}
	
	@POST
	@Path("/penugasan/hapus")
	@Consumes("application/json")
	@Produces("application/json")
	public Response hapusPenugasan(
			@Context HttpHeaders headers, 
			String data
		) {
		Result<Pegawai> result = new Result<Pegawai>();
		Metadata metadata = new Metadata();
		
		AuthUser authUser = new AuthUser();
		if (SharedMethod.VerifyToken(authUser, headers, metadata)) {
			Connection con = null;
			PreparedStatement ps = null;
			ResultSet rs = null;
			String query = null;
			try {
				ObjectMapper mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, true);
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				SimpanPenugasan json = mapper.readValue(data, SimpanPenugasan.class);
				
				query = "delete from karyawan.penugasan where kode=?";
				if(json.getIsnonbagan()==1) {
					query = "delete from karyawan.penugasannonbagan where kode=?";
				}
				con = new Koneksi().getConnection();
				ps = con.prepareStatement(query);
				ps.setInt(1, json.getKode());
				ps.executeUpdate();
			} catch (SQLException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
			} catch (NullPointerException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
			} catch (Exception e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
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
		result.setMetadata(metadata);
		return Response.ok(result).build();
	}
	
	private List<Tree> buildTree (ArrayList<Pair> pairs){
        Map<String, Tree> hm = new HashMap<>();
        for(Pair p:pairs){

        	Tree item ;
            if(hm.containsKey(p.getChildId())){
            	item = hm.get(p.getChildId());
            }
            else{
            	item = new Tree();
            	item.setKode(p.getChildId());
            	item.setKodeparent(p.getParentId());
            	item.setLabel(p.getLabel());
                hm.put(p.getChildId(),item);
            }
        }
        
        for (Map.Entry<String, Tree> entry : hm.entrySet()) {
        	if(!entry.getValue().getKodeparent().equals("")) {
    			if(hm.containsKey(entry.getValue().getKodeparent())){
    				Tree parent = hm.get(entry.getValue().getKodeparent());
        			parent.addChildrenItem(entry.getValue());
        		}
    		}
    	}
        
        for(Pair p:pairs){
        	if(!p.getParentId().equals("")) {
        		hm.remove(p.getChildId());
        	}
        }
        
        List<Tree> list = new ArrayList<Tree>(); 
        for(Tree item : hm.values()){
            list.add(item);
        }
        
        return list;
	}
	
	@GET
	@Path("/tree/unitkerja/kodeorganizationchart/{kodeorganizationchart}")
	@Produces("application/json")
	public Response GetTreeUnitKerja(
			@Context HttpHeaders headers,
			@PathParam("kodeorganizationchart") String kodeorganizationchart) {
		
		Respon<Tree> response = new Respon<Tree>();
		Metadata metadata = new Metadata();
		Result<Tree> result = new Result<Tree>();
		
		Connection con = null;
		ResultSet rs = null;
		CallableStatement cs = null;
		String query = null;
		
		//AuthUser authUser = new AuthUser();
		//if (SharedMethod.VerifyToken(authUser, headers, metadata)) {
			try {
				query = "select a.kode, a.nama, b.kode as kodeunitkerja, b.nama as namaunitkerja, isnull(b.kodeparent, '') as kodeparentunitkerja \r\n" + 
						"from organisasi.organizationchart a \r\n" + 
						"inner join organisasi.unitkerja b on a.kode=b.kodeorganizationchart \r\n" + 
						"inner join organisasi.hirarkiunitkerja c on b.kodehirarkiunitkerja=c.kode \r\n" + 
						"where a.kode=? and b.row_status=1 and c.row_status=1 \r\n" + 
						"order by c.[level], b.kode";
				con = new Koneksi().getConnection();
				cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
				cs.setString(1, kodeorganizationchart);
				rs = cs.executeQuery();
				
				metadata.setCode(0);
				metadata.setMessage("Data kosong");
				
				ArrayList<Pair> pairs = new ArrayList<Pair>();
				while (rs.next()) {
					Pair pair = new Pair();
					pair.setChildId(rs.getString("kodeunitkerja"));
					pair.setParentId(rs.getString("kodeparentunitkerja"));
					pair.setLabel(rs.getString("namaunitkerja"));
					pairs.add(pair);
					
					metadata.setCode(1);
					metadata.setMessage("Ok.");
				}
		        
		        List<Tree> trees = new ArrayList<Tree>();
				trees = buildTree(pairs);
				
				response.setList(trees);
				result.setResponse(response);
			} catch (SQLException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
			} catch (Exception e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
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
		//}
		
		result.setMetadata(metadata);
		return Response.ok(result).build();
	}
	
	@GET
	@Path("/tree/jabatan/kodeorganizationchart/{kodeorganizationchart}")
	@Produces("application/json")
	public Response GetTreeJabatan(
			@Context HttpHeaders headers,
			@PathParam("kodeorganizationchart") String kodeorganizationchart) {
		
		Respon<Tree> response = new Respon<Tree>();
		Metadata metadata = new Metadata();
		Result<Tree> result = new Result<Tree>();
		
		Connection con = null;
		ResultSet rs = null;
		CallableStatement cs = null;
		String query = null;
		
		//AuthUser authUser = new AuthUser();
		//if (SharedMethod.VerifyToken(authUser, headers, metadata)) {
			try {
				query = "select\r\n" + 
						"iif(i.isstruktural=1,concat(h.kode,'#',d.kodejobtitle),concat(h.kode,'#',d.kodejobtitle,'#',g.npp))  as kodejobtitle, \r\n" + 
						"iif(d.kodeparentjobtitle is null, '', concat(iif(e.nama in ('Deputi Direksi Wilayah','Kepala Cabang','Kepala Kabupaten/Kota'),h.kodeparent,h.kode),'#',d.kodeparentjobtitle)) as kodeparentjobtitle, \r\n" + 
						"concat(h.nama,' - ',e.nama,' - ',g.nama) as [label] \r\n" + 
						"from organisasi.organizationchart a \r\n" + 
						"inner join organisasi.unitkerja b on a.kode=b.kodeorganizationchart \r\n" + 
						"inner join organisasi.hirarkiunitkerja c on b.kodehirarkiunitkerja=c.kode \r\n" + 
						"inner join organisasi.hirarkijabatan d on b.kode=d.kodeunitkerja \r\n" + 
						"inner join organisasi.jobtitle e on d.kodejobtitle=e.kode \r\n" + 
						"inner join karyawan.vw_penugasan f on d.kode=f.kodehirarkijabatan \r\n" + 
						"inner join karyawan.vw_pegawai g on f.npp=g.npp \r\n" + 
						"inner join organisasi.office h on f.kodeoffice=h.kode \r\n" + 
						"inner join organisasi.jobprefix i on e.kodejobprefix=i.kode \r\n" + 
						"where a.kode=? and b.row_status=1 and c.row_status=1 \r\n" + 
						"order by c.[level], b.kode, d.kodeparentjobtitle";
				con = new Koneksi().getConnection();
				cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
				cs.setString(1, kodeorganizationchart);
				rs = cs.executeQuery();
				
				metadata.setCode(0);
				metadata.setMessage("Data kosong");
				
				ArrayList<Pair> pairs = new ArrayList<Pair>();
				while (rs.next()) {
					Pair pair = new Pair();
					pair.setChildId(rs.getString("kodejobtitle"));
					pair.setParentId(rs.getString("kodeparentjobtitle"));
					pair.setLabel(rs.getString("label"));
					pairs.add(pair);
					
					metadata.setCode(1);
					metadata.setMessage("Ok.");
				}
		        
		        List<Tree> trees = new ArrayList<Tree>();
				trees = buildTree(pairs);
				
				response.setList(trees);
				result.setResponse(response);
			} catch (SQLException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
			} catch (Exception e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
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
		//}
		
		result.setMetadata(metadata);
		return Response.ok(result).build();
	}
	
}