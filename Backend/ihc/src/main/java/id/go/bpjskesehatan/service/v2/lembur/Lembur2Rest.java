package id.go.bpjskesehatan.service.v2.lembur;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
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
import id.go.bpjskesehatan.entitas.KomponenGaji;
import id.go.bpjskesehatan.entitas.Metadata;
import id.go.bpjskesehatan.entitas.Respon;
import id.go.bpjskesehatan.entitas.Result;
import id.go.bpjskesehatan.entitas.hcis.Notifikasi;
import id.go.bpjskesehatan.entitas.karyawan.Pegawai;
import id.go.bpjskesehatan.entitas.karyawan.Penugasan;
import id.go.bpjskesehatan.entitas.organisasi.Office;
import id.go.bpjskesehatan.service.v2.entitas.Akun;
import id.go.bpjskesehatan.service.v2.entitas.Program;
import id.go.bpjskesehatan.service.v2.lembur.entitas.Lembur;
import id.go.bpjskesehatan.service.v2.lembur.entitas.ListPegawai;
import id.go.bpjskesehatan.service.v2.lembur.entitas.Report;
import id.go.bpjskesehatan.service.v2.lembur.entitas.SaveLemburTgls;
import id.go.bpjskesehatan.util.SharedMethod;
import id.go.bpjskesehatan.util.Utils;

@Path("/v2/lembur")
public class Lembur2Rest {	
	
	@Context
    private ServletContext context;
	
	@POST
	@Path("/grid/{npp}/{page}/{row}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response ListGrid(@Context HttpHeaders headers,
			@PathParam("npp") String npp, @PathParam("page") String page, @PathParam("row") String row, String data) {
		
		Respon<Lembur> response = new Respon<Lembur>();
		Metadata metadata = new Metadata();
		Result<Lembur> result = new Result<Lembur>();
		
		Connection con = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		ResultSet rs3 = null;
		CallableStatement cs = null;
		PreparedStatement ps = null;
		PreparedStatement pstgls = null;
		String order = null;
		String filter = null;
		String filter2 = null;
		String filter3 = null;
		String query = null;
		Boolean ok = true;

		if (SharedMethod.VerifyToken(headers, metadata)) {
		//if (true) {
			try {
				JsonNode json = null;
				if (data != null) {
					if (!data.isEmpty()) {
						ObjectMapper mapper = new ObjectMapper();
						json = mapper.readTree(data);

						order = json.path("data").path("sort").isMissingNode() ? null
								: SharedMethod.getSortedColumn(mapper.writeValueAsString(json.path("data").path("sort")));

						filter = json.path("data").path("filter").isMissingNode() ? null
								: SharedMethod.getFilteredColumn(mapper.writeValueAsString(json.path("data").path("filter")), null);
						filter2 = json.path("data").path("filter2").isMissingNode() ? null
								: SharedMethod.getFilteredColumn(mapper.writeValueAsString(json.path("data").path("filter2")), null);
						filter3 = json.path("data").path("filter3").isMissingNode() ? null
								: SharedMethod.getFilteredColumn(mapper.writeValueAsString(json.path("data").path("filter3")), null);
						
					}
				}
				
				if(ok) {
					query = "exec lembur.sp_listlembur ?, ?, ?, ?, ?, ?, ?, ?";
					con = new Koneksi().getConnection();
					cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
					cs.setString(1, npp);
					cs.setInt(2, Integer.parseInt(page));
					cs.setInt(3, Integer.parseInt(row));
					cs.setInt(4, 1);
					cs.setString(5, order);
					cs.setString(6, filter);
					cs.setString(7, filter2);
					cs.setString(8, filter3);
					rs = cs.executeQuery();
					metadata.setCode(0);
					metadata.setMessage(Response.Status.NO_CONTENT.toString());
					metadata.setRowcount(0);

					if (rs.next()) {
						metadata.setRowcount(rs.getInt("jumlah"));
					}
					rs.close();
					cs.close();
					
					cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
					cs.setString(1, npp);
					cs.setInt(2, Integer.parseInt(page));
					cs.setInt(3, Integer.parseInt(row));
					cs.setInt(4, 0);
					cs.setString(5, order);
					cs.setString(6, filter);
					cs.setString(7, filter2);
					cs.setString(8, filter3);
					rs = cs.executeQuery();
					
					List<Lembur> lemburs = new ArrayList<>();
					while (rs.next()) {
						Lembur lembur = new Lembur();
						lembur.setKode(rs.getInt("kode"));
						lembur.setNomor(rs.getString("nomor"));
						lembur.setLaporan_isi(rs.getString("laporan_isi"));
						lembur.setLaporan_lampiran(rs.getString("laporan_lampiran"));
						lembur.setIsverif(rs.getInt("isverif"));
						
						Office office = new Office();
						office.setKode(rs.getString("kodeoffice"));
						office.setNama(rs.getString("namaoffice"));
						lembur.setOffice(office);
						
						Akun akun = new Akun();
						akun.setKode(rs.getString("kodeakun"));
						akun.setNama(rs.getString("namaakun"));
						lembur.setAkun(akun);
						
						Program program = new Program();
						program.setKode(rs.getString("kodeprogram"));
						program.setNama(rs.getString("namaprogram"));
						lembur.setProgram(program);
						
						lembur.setLampiran(rs.getString("lampiran")==null?"":rs.getString("lampiran"));
						lembur.setStatus(rs.getInt("status"));
						lembur.setNamastatus(rs.getString("namastatus"));
						
						lembur.setTgllembur(rs.getString("tgllembur"));
						lembur.setPegawailembur(rs.getString("pegawailembur"));
						
						lembur.setDeskripsi(rs.getString("deskripsi"));
						lembur.setNamakegiatan(rs.getString("namakegiatan"));
						lembur.setCatatantolak(rs.getString("catatantolak"));
						
						Penugasan penugasan = new Penugasan();
						penugasan.setKode(rs.getInt("pembuat"));
						Pegawai pegawai = new Pegawai();
						pegawai.setNpp(rs.getString("npppembuat"));
						pegawai.setNama(rs.getString("namapembuat"));
						penugasan.setPegawai(pegawai);
						lembur.setPembuat(penugasan);
						
						try {
							Integer tahun = Calendar.getInstance().get(Calendar.YEAR);
							ps = con.prepareStatement("select \r\n" + 
									"a.*, c.npp, c.nama, d.totaljam \r\n" + 
									"from lembur.pegawai a \r\n" + 
									"inner join karyawan.penugasan b on a.kodepenugasan=b.kode \r\n" + 
									"inner join karyawan.vw_pegawai c on b.npp=c.npp \r\n" + 
									"left join (select * from lembur.vw_totaljam where tahun=?) d on b.npp=d.npp \r\n" + 
									"where a.kodelembur = ? order by a.kode");
							ps.setInt(1, tahun);
							ps.setInt(2, lembur.getKode());
							rs2 = ps.executeQuery();
							ArrayList<ListPegawai> lemburpegawais = new ArrayList<>();
							while (rs2.next()) {
								ListPegawai lemburpegawai = new ListPegawai();
								lemburpegawai.setKode(rs2.getInt("kode"));
								lemburpegawai.setKodepenugasan(rs2.getInt("kodepenugasan"));
								lemburpegawai.setNpp(rs2.getString("npp"));
								lemburpegawai.setNama(rs2.getString("nama"));
								lemburpegawai.setDeleted(0);
								lemburpegawai.setTotaljam(rs2.getInt("totaljam"));
								lemburpegawais.add(lemburpegawai);
							}
							lembur.setPegawai(lemburpegawais);
						}
						finally {
							if (rs2 != null) {
								try {
									rs2.close();
								} catch (SQLException e) {
								}
							}
							if (ps != null) {
								try {
									ps.close();
								} catch (SQLException e) {
								}
							}
						}
						
						try {
							pstgls = con.prepareStatement("select *, iif(tgl < cast(getdate() as date),0,iif(tgl = cast(getdate() as date) and cast(getdate() as time) > '08:00:00',0,1)) as viewcheck from lembur.lemburtgl where kodelembur=?");
							pstgls.setInt(1, lembur.getKode());
							rs3 = pstgls.executeQuery();
							ArrayList<SaveLemburTgls> saveLemburTgls = new ArrayList<>();
							while (rs3.next()) {
								SaveLemburTgls tgls = new SaveLemburTgls();
								tgls.setKode(rs3.getInt("kode"));
								tgls.setTgl(rs3.getDate("tgl"));
								tgls.setDeleted(0);
								tgls.setBatalkan(false);
								tgls.setStatus(rs3.getInt("status"));
								tgls.setViewcheck(rs3.getBoolean("viewcheck"));
								saveLemburTgls.add(tgls);
							}
							lembur.setTgls(saveLemburTgls);
						}
						finally {
							if (rs3 != null) {
								try {
									rs3.close();
								} catch (SQLException e) {
								}
							}	
							if (pstgls != null) {
								try {
									pstgls.close();
								} catch (SQLException e) {
								}
							}	
						}
						
						/*ps = con.prepareStatement("select \r\n" + 
								"a.*, c.npp, c.nama\r\n" + 
								"from lembur.pegawai a\r\n" + 
								"inner join karyawan.penugasan b on a.kodepenugasan=b.kode\r\n" + 
								"inner join karyawan.vw_pegawai c on b.npp=c.npp\r\n" + 
								"where a.kodelembur = ?");
						ps.setInt(1, lembur.getKode());
						rs2 = ps.executeQuery();
						ArrayList<ListPegawai> lemburpegawais = new ArrayList<>();
						while (rs2.next()) {
							ListPegawai lemburpegawai = new ListPegawai();
							lemburpegawai.setKode(rs2.getInt("kode"));
							lemburpegawai.setKodepenugasan(rs2.getInt("kodepenugasan"));
							lemburpegawai.setNpp(rs2.getString("npp"));
							lemburpegawai.setNama(rs2.getString("nama"));
							lemburpegawai.setDeleted(0);
							lemburpegawais.add(lemburpegawai);
						}*/
						lemburs.add(lembur);
						
						metadata.setCode(1);
						metadata.setMessage("OK");
					}
					response.setList(lemburs);
					result.setResponse(response);
				}
			} catch (SQLException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
			} catch (NamingException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
				e.printStackTrace();
			} catch (SecurityException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
				e.printStackTrace();
			} catch (InstantiationException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
				e.printStackTrace();
			} catch (Exception e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
				e.printStackTrace();
			} finally {
				if (rs2 != null) {
					try {
						rs2.close();
					} catch (SQLException e) {
					}
				}
				
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
	@Path("/grid/{page}/{row}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response ListGridAll(@Context HttpHeaders headers,
			@PathParam("page") String page, @PathParam("row") String row, String data) {
		
		Respon<Lembur> response = new Respon<Lembur>();
		Metadata metadata = new Metadata();
		Result<Lembur> result = new Result<Lembur>();
		
		Connection con = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		ResultSet rs3 = null;
		CallableStatement cs = null;
		PreparedStatement ps = null;
		PreparedStatement pstgls = null;
		String order = null;
		String filter = null;
		String filter2 = null;
		String filter3 = null;
		String query = null;
		Boolean ok = true;

		if (SharedMethod.VerifyToken(headers, metadata)) {
		//if (true) {
			try {
				JsonNode json = null;
				if (data != null) {
					if (!data.isEmpty()) {
						ObjectMapper mapper = new ObjectMapper();
						json = mapper.readTree(data);

						order = json.path("data").path("sort").isMissingNode() ? null
								: SharedMethod.getSortedColumn(mapper.writeValueAsString(json.path("data").path("sort")));

						filter = json.path("data").path("filter").isMissingNode() ? null
								: SharedMethod.getFilteredColumn(mapper.writeValueAsString(json.path("data").path("filter")), null);
						filter2 = json.path("data").path("filter2").isMissingNode() ? null
								: SharedMethod.getFilteredColumn(mapper.writeValueAsString(json.path("data").path("filter2")), null);
						filter3 = json.path("data").path("filter3").isMissingNode() ? null
								: SharedMethod.getFilteredColumn(mapper.writeValueAsString(json.path("data").path("filter3")), null);
						
					}
				}
				
				if(ok) {
					query = "exec lembur.sp_listlemburall ?, ?, ?, ?, ?, ?, ?";
					con = new Koneksi().getConnection();
					cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
					cs.setInt(1, Integer.parseInt(page));
					cs.setInt(2, Integer.parseInt(row));
					cs.setInt(3, 1);
					cs.setString(4, order);
					cs.setString(5, filter);
					cs.setString(6, filter2);
					cs.setString(7, filter3);
					rs = cs.executeQuery();
					metadata.setCode(0);
					metadata.setMessage(Response.Status.NO_CONTENT.toString());
					metadata.setRowcount(0);

					if (rs.next()) {
						metadata.setRowcount(rs.getInt("jumlah"));
					}
					rs.close();
					cs.close();
					
					cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
					cs.setInt(1, Integer.parseInt(page));
					cs.setInt(2, Integer.parseInt(row));
					cs.setInt(3, 0);
					cs.setString(4, order);
					cs.setString(5, filter);
					cs.setString(6, filter2);
					cs.setString(7, filter3);
					rs = cs.executeQuery();
					
					List<Lembur> lemburs = new ArrayList<>();
					while (rs.next()) {
						Lembur lembur = new Lembur();
						lembur.setKode(rs.getInt("kode"));
						lembur.setNomor(rs.getString("nomor"));
						lembur.setLaporan_isi(rs.getString("laporan_isi"));
						
						Office office = new Office();
						office.setKode(rs.getString("kodeoffice"));
						office.setNama(rs.getString("namaoffice"));
						lembur.setOffice(office);
						
						Akun akun = new Akun();
						akun.setKode(rs.getString("kodeakun"));
						akun.setNama(rs.getString("namaakun"));
						lembur.setAkun(akun);
						
						Program program = new Program();
						program.setKode(rs.getString("kodeprogram"));
						program.setNama(rs.getString("namaprogram"));
						lembur.setProgram(program);
						 
						lembur.setLampiran(rs.getString("lampiran")==null?"":rs.getString("lampiran"));
						lembur.setStatus(rs.getInt("status"));
						lembur.setNamastatus(rs.getString("namastatus"));
						
						lembur.setTgllembur(rs.getString("tgllembur"));
						lembur.setPegawailembur(rs.getString("pegawailembur"));
						
						lembur.setDeskripsi(rs.getString("deskripsi"));
						lembur.setNamakegiatan(rs.getString("namakegiatan"));
						lembur.setCatatantolak(rs.getString("catatantolak"));
						
						Penugasan penugasan = new Penugasan();
						penugasan.setKode(rs.getInt("pembuat"));
						
						Pegawai pegawai = new Pegawai();
						pegawai.setNpp(rs.getString("npppembuat"));
						pegawai.setNama(rs.getString("namapembuat"));
						
						penugasan.setPegawai(pegawai);
						lembur.setPembuat(penugasan);
						
						try {
							Integer tahun = Calendar.getInstance().get(Calendar.YEAR);
							ps = con.prepareStatement("select \r\n" + 
									"a.*, c.npp, c.nama, d.totaljam \r\n" + 
									"from lembur.pegawai a \r\n" + 
									"inner join karyawan.penugasan b on a.kodepenugasan=b.kode \r\n" + 
									"inner join karyawan.vw_pegawai c on b.npp=c.npp \r\n" + 
									"left join (select * from lembur.vw_totaljam where tahun=?) d on b.npp=d.npp \r\n" + 
									"where a.kodelembur = ?");
							ps.setInt(1, tahun);
							ps.setInt(2, lembur.getKode());
							rs2 = ps.executeQuery();
							ArrayList<ListPegawai> lemburpegawais = new ArrayList<>();
							while (rs2.next()) {
								ListPegawai lemburpegawai = new ListPegawai();
								lemburpegawai.setKode(rs2.getInt("kode"));
								lemburpegawai.setKodepenugasan(rs2.getInt("kodepenugasan"));
								lemburpegawai.setNpp(rs2.getString("npp"));
								lemburpegawai.setNama(rs2.getString("nama"));
								lemburpegawai.setDeleted(0);
								lemburpegawai.setTotaljam(rs2.getInt("totaljam"));
								lemburpegawais.add(lemburpegawai);
							}
							lembur.setPegawai(lemburpegawais);
						}
						finally {
							if (rs2 != null) {
								try {
									rs2.close();
								} catch (SQLException e) {
								}
							}
							if (ps != null) {
								try {
									ps.close();
								} catch (SQLException e) {
								}
							}
						}
						
						try {
							pstgls = con.prepareStatement("select *, iif(tgl < cast(getdate() as date),0,iif(tgl = cast(getdate() as date) and cast(getdate() as time) > '08:00:00',0,1)) as viewcheck from lembur.lemburtgl where kodelembur=?");
							pstgls.setInt(1, lembur.getKode());
							rs3 = pstgls.executeQuery();
							ArrayList<SaveLemburTgls> saveLemburTgls = new ArrayList<>();
							while (rs3.next()) {
								SaveLemburTgls tgls = new SaveLemburTgls();
								tgls.setKode(rs3.getInt("kode"));
								tgls.setTgl(rs3.getDate("tgl"));
								tgls.setDeleted(0);
								tgls.setBatalkan(false);
								tgls.setStatus(rs3.getInt("status"));
								tgls.setViewcheck(rs3.getBoolean("viewcheck"));
								saveLemburTgls.add(tgls);
							}
							lembur.setTgls(saveLemburTgls);
						}
						finally {
							if (rs3 != null) {
								try {
									rs3.close();
								} catch (SQLException e) {
								}
							}	
							if (pstgls != null) {
								try {
									pstgls.close();
								} catch (SQLException e) {
								}
							}	
						}
						
						lemburs.add(lembur);
						
						metadata.setCode(1);
						metadata.setMessage("OK");
					}
					response.setList(lemburs);
					result.setResponse(response);
				}
			} catch (SQLException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
			} catch (NamingException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
				e.printStackTrace();
			} catch (SecurityException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
				e.printStackTrace();
			} catch (InstantiationException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
				e.printStackTrace();
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

		}
		result.setMetadata(metadata);
		return Response.ok(result).build();
	}	
	
	@POST
	@Path("/save")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces("application/json")
	public Response simpanLembur(
			@Context HttpHeaders headers, 
			@FormDataParam("file") final InputStream uploadedInputStream, 
			@FormDataParam("file") FormDataContentDisposition fileDetail,
			@FormDataParam("file") String file,
			@FormDataParam("lembur") FormDataBodyPart post,
			@FormDataParam("act") String act) {
		
		Map<String, Object> metadata = new HashMap<String, Object>();
		Map<String, Object> metadataobj = new HashMap<String, Object>();
		FTPClient ftpClient = null;

		if (SharedMethod.VerifyToken(headers, metadata)) {
			try {
				post.setMediaType(MediaType.APPLICATION_JSON_TYPE);
				Lembur lembur = post.getValueAs(Lembur.class);
				
				Boolean adafile = false;
				String pathFile = null;
				String host = null;
	        	Integer port = null;
	        	String user = null;
	        	String pass = null;
				String namaFile = "";
				
				if(!(uploadedInputStream==null || fileDetail==null) && file.length() > 0) {
					adafile = true;
					pathFile = "file_lembur/";
					host = context.getInitParameter("ftp-host");
		        	port = Integer.parseInt(context.getInitParameter("ftp-port"));
		        	user = context.getInitParameter("ftp-user");
		        	pass = context.getInitParameter("ftp-pass");
					
					namaFile = fileDetail.getFileName();
		        	StringTokenizer st = new StringTokenizer(namaFile, ".");
		        	String extension = ""; 
		        	while(st.hasMoreTokens()) {
		        		extension = "."+st.nextToken();
		        	}
		        	String npp = lembur.getOffice().getKode();
		        	Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		        	namaFile = npp + "-" + timestamp.getTime() + extension;
				}
				
				Connection con = null;
				CallableStatement cs = null;
				String query = null;
				
				if(act.equalsIgnoreCase("create")) {
					try {
						query = "DECLARE @TempList lembur.lemburpegawaiAsTable;";
						for (ListPegawai item : lembur.getListpegawai()) {
							if(item.getDeleted()==0)
								query += "insert into @TempList values (" + item.getKode() + ", " + item.getKodepenugasan() + ");";
						}
						
						query += "DECLARE @TempListTgls lembur.lemburTglsAsTable;";
						for (SaveLemburTgls item : lembur.getTgls()) {
							if(item.getDeleted()==0)
								query += "insert into @TempListTgls values (" + item.getKode() + ", '" + item.getTgl() + "', " + item.getStatus() + ");";
						}
						
						query += "exec lembur.sp_insertlembur ?,?,?,?,?,?,?,?,?,@TempList,@TempListTgls;";
						
						con = new Koneksi().getConnection();
						cs = con.prepareCall(query);
						cs.setString(1, lembur.getOffice().getKode());
						cs.setString(2, lembur.getKodepic());
						cs.setString(3, lembur.getNamakegiatan());
						cs.setString(4, lembur.getDeskripsi());
						cs.setString(5, lembur.getAkun().getKode());
						cs.setString(6, lembur.getProgram().getKode());
						if(adafile)
							cs.setString(7, namaFile);
						else
							cs.setNull(7, java.sql.Types.VARCHAR);
						cs.setInt(8, lembur.getUseract());
						cs.setInt(9, lembur.getPembuat().getKode());
						cs.executeUpdate();
						
						metadata.put("code", 1);
						metadata.put("message", "Simpan berhasil.");
						
						if(adafile) {
							try {
								ftpClient = new FTPClient();
								ftpClient.connect(host,port);
					        	ftpClient.login(user, pass);
					        	ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
					        	Boolean upload = ftpClient.storeFile(pathFile + namaFile, uploadedInputStream);
					        	if(!upload) {
					        		metadata.put("code", 0);
									metadata.put("message", "Upload gagal.");
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
				else if(act.equalsIgnoreCase("update")) {
					try {
						query = "DECLARE @TempList lembur.lemburpegawaiAsTable;";
						for (ListPegawai item : lembur.getListpegawai()) {
							if(item.getDeleted()==0)
								query += "insert into @TempList values (" + item.getKode() + ", " + item.getKodepenugasan() + ");";
						}
						
						query += "DECLARE @TempListTgls lembur.lemburTglsAsTable;";
						for (SaveLemburTgls item : lembur.getTgls()) {
							if(item.getDeleted()==0)
								query += "insert into @TempListTgls values (" + item.getKode() + ", '" + item.getTgl() + "', " + item.getStatus() + ");";
						}
						
						query += "exec lembur.sp_updatelembur ?,?,?,?,?,?,?,?,?,@TempList,@TempListTgls,?;";
						
						con = new Koneksi().getConnection();
						cs = con.prepareCall(query);
						cs.setString(1, lembur.getOffice().getKode());
						cs.setString(2, lembur.getKodepic());
						cs.setString(3, lembur.getNamakegiatan());
						cs.setString(4, lembur.getDeskripsi());
						cs.setString(5, lembur.getAkun().getKode());
						cs.setString(6, lembur.getProgram().getKode());
						if(adafile)
							cs.setString(7, namaFile);
						else
							cs.setNull(7, java.sql.Types.VARCHAR);
						cs.setInt(8, lembur.getUseract());
						cs.setInt(9, lembur.getPembuat().getKode());
						cs.setInt(10, lembur.getKode());
						cs.executeUpdate();
						
						metadata.put("code", 1);
						metadata.put("message", "Update berhasil.");
						
						if(adafile) {
							try {
								ftpClient = new FTPClient();
								ftpClient.connect(host,port);
					        	ftpClient.login(user, pass);
					        	ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
					        	
					        	if(!lembur.getLampiran().isEmpty()) {
					        		String deletedFile = lembur.getLampiran();
					        		ftpClient.deleteFile(pathFile + deletedFile);
					        	}
					        	
					        	Boolean upload = ftpClient.storeFile(pathFile + namaFile, uploadedInputStream);
					        	if(!upload) {
					        		metadata.put("code", 0);
									metadata.put("message", "Upload gagal.");
					        	}
							}
							catch (Exception e) {
								e.printStackTrace();
							}
							finally {
								if(ftpClient.isConnected())
									ftpClient.disconnect();
							}
						}
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
			catch (SQLException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
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
	@Path("/savereport")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces("application/json")
	public Response simpanLaporan(
			@Context HttpHeaders headers, 
			@FormDataParam("file_lampiran") final InputStream uploadedInputStream1, 
			@FormDataParam("file_lampiran") FormDataContentDisposition fileDetail1,
			@FormDataParam("file_lampiran") String file1,
			@FormDataParam("lembur") FormDataBodyPart post) {
		
		Respon<Lembur> response = new Respon<Lembur>();
		Metadata metadata = new Metadata();
		Result<Lembur> result = new Result<Lembur>();

		if (SharedMethod.VerifyToken(headers, metadata)) {
			try {
				String namaFile1 = null;
				
				post.setMediaType(MediaType.APPLICATION_JSON_TYPE);
				Lembur lembur = post.getValueAs(Lembur.class);
				
				namaFile1 = getNamaFile(lembur.getKode(), 1, uploadedInputStream1, fileDetail1, file1);
				
				Connection con = null;
				CallableStatement cs = null;
				String query = null;
				
				try {
					query = "exec lembur.sp_insertlemburlaporan ?,?,?,?;";
					
					con = new Koneksi().getConnection();
					cs = con.prepareCall(query);
					cs.setInt(1, lembur.getKode());
					cs.setInt(2, lembur.getLaporan_useract());
					cs.setString(3, lembur.getLaporan_isi());
					if(namaFile1!=null)
						cs.setString(4, namaFile1);
					else
						cs.setNull(4, java.sql.Types.VARCHAR);
					cs.executeUpdate();
					
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
				
				
				Lembur lembur_respon = new Lembur();
				if(namaFile1!=null) {
					insertFTPFile(namaFile1, uploadedInputStream1);
					lembur_respon.setLaporan_lampiran(namaFile1);
				}
				else {
					lembur_respon.setLaporan_lampiran(lembur.getLaporan_lampiran());
				}
				
				metadata.setCode(1);
				metadata.setMessage("Simpan berhasil.");
				
				lembur.setLaporan_lampiran(lembur_respon.getLaporan_lampiran());
				lembur.setLaporan_isi(lembur.getLaporan_isi());
				
				response.setData(lembur);
				result.setResponse(response);
				
			}
			catch (SQLException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
				e.printStackTrace();
			}
			catch (Exception e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
				e.printStackTrace();
			}
		}
		
		result.setMetadata(metadata);
		return Response.ok(result).build();
	}
	
	@POST
	@Path("/delete")
	@Produces("application/json")
	public Response hapusLembur(@Context HttpHeaders headers, String data) {
		
		Metadata metadata = new Metadata();
		Result<KomponenGaji> result = new Result<KomponenGaji>();
		
		Connection con = null;
		ResultSet rs = null;
		CallableStatement cs = null;
		String query = null;
		
		if (SharedMethod.VerifyToken(headers, metadata)) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				JsonNode json = mapper.readTree(data);
				Boolean ok = true;
				Boolean ok2 = true;
				String msg = "";
				StringBuilder str = new StringBuilder();
				
				if (!data.isEmpty()) {
					if(json.path("kode").isMissingNode()){
						str.append("kode").append(", ");
						ok = false;
					}
					if(json.path("lampiran").isMissingNode()){
						str.append("lampiran").append(", ");
						ok = false;
					}
					
					if(!ok){
						str.replace(str.length() - 2, str.length() - 1, "");
						metadata.setMessage("data " + str + " is missing.");
					}
					else if(!ok2){
						metadata.setMessage(msg);
					}
					
				}
				else{
					metadata.setCode(0);
					metadata.setMessage("missing data.");
					ok = false;
				}
				
				if(ok && ok2){
					Integer kode = json.path("kode").asInt();
					String lampiran = json.path("lampiran").asText();
					
					query = "exec lembur.sp_deletelembur ?";
					con = new Koneksi().getConnection();
					
					cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
					cs.setInt(1, kode);
					cs.executeUpdate();
					
					if(!lampiran.isEmpty()) {
						FTPClient ftpClient = null;
						try {
							String pathFile = "file_lembur/";
							String host = context.getInitParameter("ftp-host");
							Integer port = Integer.parseInt(context.getInitParameter("ftp-port"));
							String user = context.getInitParameter("ftp-user");
							String pass = context.getInitParameter("ftp-pass");
							ftpClient = new FTPClient();
							ftpClient.connect(host,port);
				        	ftpClient.login(user, pass);
				        	ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
				        	String deletedFile = lampiran;
			        		ftpClient.deleteFile(pathFile + deletedFile);
			        		ftpClient.logout();
						}
						finally {
							if(ftpClient.isConnected())
								ftpClient.disconnect();
						}
					}
					
					metadata.setCode(1);
					metadata.setMessage("Hapus berhasil.");
				}
			} catch (SQLException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
			} catch (NamingException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
				e.printStackTrace();
			} catch (SecurityException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
				e.printStackTrace();
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
		}
		
		result.setMetadata(metadata);
		return Response.ok(result).build();
	}
	
	@POST
	@Path("/ajukan")
	@Produces("application/json")
	public Response ajukanLembur(@Context HttpHeaders headers, String data) {
		
		Metadata metadata = new Metadata();
		Result<Object> result = new Result<Object>();
		
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement cs = null;
		String query = null;
		
		if (SharedMethod.VerifyToken(headers, metadata)) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				JsonNode json = mapper.readTree(data);
				Boolean ok = true;
				Boolean ok2 = true;
				String msg = "";
				StringBuilder str = new StringBuilder();
				
				if (!data.isEmpty()) {
					if(json.path("kode").isMissingNode()){
						str.append("kode").append(", ");
						ok = false;
					}
					if(json.path("useract").isMissingNode()){
						str.append("useract").append(", ");
						ok = false;
					}
					if(json.path("npp").isMissingNode()){
						str.append("npp").append(", ");
						ok = false;
					}
					if(json.path("detil").isMissingNode()){
						str.append("detil").append(", ");
						ok = false;
					}
					if(json.path("kodepenugasan").isMissingNode()){
						str.append("kodepenugasan").append(", ");
						ok = false;
					}
					if(json.path("kodejobtitleatasan").isMissingNode()){
						str.append("kodejobtitleatasan").append(", ");
						ok = false;
					}
					if(json.path("kodeofficeatasan").isMissingNode()){
						str.append("kodeofficeatasan").append(", ");
						ok = false;
					}
					if(json.path("nppatasan").isMissingNode()){
						str.append("nppatasan").append(", ");
						ok = false;
					}
					if(json.path("kodepenugasanatasan").isMissingNode()){
						str.append("kodepenugasanatasan").append(", ");
						ok = false;
					}
					
					if(!ok){
						str.replace(str.length() - 2, str.length() - 1, "");
						metadata.setMessage("data " + str + " is missing.");
					}
					else if(!ok2){
						metadata.setMessage(msg);
					}
					
				}
				else{
					metadata.setCode(0);
					metadata.setMessage("missing data.");
					ok = false;
				}
				
				if(ok && ok2){
					Integer kode = json.path("kode").asInt();
					Integer useract = json.path("useract").asInt();
					String npp = json.path("npp").asText();
					String detil = json.path("detil").asText();
					String kodejobtitleatasan = json.path("kodejobtitleatasan").asText();
					Integer kodepenugasan = json.path("kodepenugasan").asInt();
					String kodeofficeatasan = json.path("kodeofficeatasan").asText();
					String nppatasan = json.path("nppatasan").asText();
					Integer kodepenugasanatasan = json.path("kodepenugasanatasan").asInt();
					
					try {
						query = "update lembur.lembur set [status]=1, tglajukan=?, lastmodified_by=?, lastmodified_time=? where kode=?";
						con = new Koneksi().getConnection();
						cs = con.prepareStatement(query);
						cs.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
						cs.setInt(2, useract);
						cs.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
						cs.setInt(4, kode);
						cs.executeUpdate();
					} catch (Exception e) {
						throw new Exception(e.getMessage());
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
					
					try {
						String deskripsi = "Pengajuan Lembur";
						query = "update lembur.lembur set [status]=2, disetujuiolehatasanlangsung=? where kode=?; exec hcis.sp_insertnotifikasi ?,?,?,?,96,?,null,1,?,null,?,null,?,?";
						con = new Koneksi().getConnection();
						cs = con.prepareStatement(query);
						cs.setInt(1, kodepenugasanatasan);
						cs.setInt(2, kode);
						cs.setString(3, npp);
						cs.setString(4, nppatasan);
						cs.setString(5, deskripsi);
						cs.setString(6, detil);
						cs.setInt(7, kode);
						cs.setInt(8, useract);
						cs.setString(9, kodejobtitleatasan);
						cs.setInt(10, kodepenugasan);
						cs.setString(11, kodeofficeatasan);
						cs.executeUpdate();
					} catch (Exception e) {
						throw new Exception(e.getMessage());
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
					
					metadata.setCode(1);
					metadata.setMessage("Pengajuan lembur berhasil.");
				}
			} catch (SQLException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
			} catch (Exception e) {
				metadata.setCode(0);
				metadata.setMessage("Transaksi belum dapat diproses, mohon ulangi lagi");
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
		
		result.setMetadata(metadata);
		return Response.ok(result).build();
	}
	
	@POST
	@Path("/getcatatannotifikasi")
	@Produces("application/json")
	public Response GetCatatanNotifikasi(@Context HttpHeaders headers, String data) {
		
		Respon<Notifikasi> response = new Respon<Notifikasi>();
		Metadata metadata = new Metadata();
		Result<Notifikasi> result = new Result<Notifikasi>();
		
		Connection con = null;
		ResultSet rs = null;
		CallableStatement cs = null;
		String query = null;
		
		if (SharedMethod.VerifyToken(headers, metadata)) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				JsonNode json = mapper.readTree(data);
				Boolean ok = true;
				Boolean ok2 = true;
				String msg = "";
				StringBuilder str = new StringBuilder();
				
				if (!data.isEmpty()) {
					if(json.path("kodelembur").isMissingNode()){
						str.append("kodelembur").append(", ");
						ok = false;
					}
					
					if(!ok){
						str.replace(str.length() - 2, str.length() - 1, "");
						metadata.setMessage("data " + str + " is missing.");
					}
					else if(!ok2){
						metadata.setMessage(msg);
					}
					
				}
				else{
					metadata.setCode(0);
					metadata.setMessage("missing data.");
					ok = false;
				}
				
				if(ok && ok2){
					Integer kodelembur = json.path("kodelembur").asInt();
					query = "exec lembur.sp_getcatatannotifikasi ?";
					con = new Koneksi().getConnection();
					
					cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
					cs.setInt(1, kodelembur);
					rs = cs.executeQuery();
					ArrayList<Notifikasi> notifikasis = new ArrayList<>();
					
					metadata.setCode(0);
					metadata.setMessage("Data kosong.");
					while (rs.next()) {
						Notifikasi notifikasi = new Notifikasi();
						notifikasi.setKode(rs.getInt("kode"));
						notifikasi.setCatatan(rs.getString("catatan"));
						notifikasi.setLastmodified_time(rs.getTimestamp("lastmodified_time"));
						
						notifikasis.add(notifikasi);
						
						metadata.setCode(1);
						metadata.setMessage("Ok.");
					}
					response.setList(notifikasis);
					result.setResponse(response);
				}
			} catch (SQLException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
			} catch (NamingException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
				e.printStackTrace();
			} catch (SecurityException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
				e.printStackTrace();
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
		}
		
		result.setMetadata(metadata);
		return Response.ok(result).build();
	}
	
	@POST
	@Path("/grid/{tgl1}/{tgl2}/{jeniskantor}/{kodelokasi}/{nomor: .*}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response ListGridLaporanPelaksanaanLembur(@Context HttpHeaders headers,
			@PathParam("tgl1") String tgl1, 
			@PathParam("tgl2") String tgl2, 
			@PathParam("jeniskantor") Integer jeniskantor,
			@PathParam("kodelokasi") String kodelokasi,
			@PathParam("nomor") String nomor,
			String data) {
		
		Map<String, Object> response = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> metadata = new HashMap<String, Object>();
		List<Map<String, Object>> list = null;
		List<Map<String, Object>> list2 = null;
		List<Map<String, Object>> list3 = null;
		
		Connection con = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		ResultSet rs3 = null;
		CallableStatement cs = null;
		CallableStatement cs2 = null;
		CallableStatement cs3 = null;
		String query = null;
		String query2 = null;
		String query3 = null;

		if (SharedMethod.VerifyToken(headers, metadata)) {
		//if (true) {
			try {
				JsonNode json = null;
				if (data != null) {
					if (!data.isEmpty()) {
						ObjectMapper mapper = new ObjectMapper();
						json = mapper.readTree(data);						
					}
				}
				
				java.sql.Date tanggal1 = new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(tgl1).getTime());
				java.sql.Date tanggal2 = new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(tgl2).getTime());
				
				query = "exec lembur.sp_getlaporanlembur ?, ?, ?, ?, ?";
					
				con = new Koneksi().getConnection();
				cs = con.prepareCall(query);
				cs.setDate(1, tanggal1);
				cs.setDate(2, tanggal2);
				cs.setInt(3, jeniskantor);
				cs.setString(4, kodelokasi);
				cs.setString(5, nomor);
				rs = cs.executeQuery();
				
				list = new ArrayList<Map<String, Object>>();
				ResultSetMetaData metaData = rs.getMetaData();
				Map<String, Object> hasil = null;
				
				Integer kodelembur = 0;
				Short isverif = 0;
				while (rs.next()) {
					hasil = new HashMap<String, Object>();
					for (int i = 1; i <= metaData.getColumnCount(); i++) {
						if(rs.getObject(i)!=null && metaData.getColumnTypeName(i).equalsIgnoreCase("date")){
							hasil.put(metaData.getColumnName(i).toLowerCase(), Utils.SqlDateToSqlString(rs.getDate(i)));
						}
						else {
							hasil.put(metaData.getColumnName(i).toLowerCase(), rs.getObject(i));
						}
						if(metaData.getColumnName(i).equalsIgnoreCase("kode")) {
							kodelembur = (Integer) rs.getObject(i);
						}
						else if(metaData.getColumnName(i).equalsIgnoreCase("isverif")) {
							isverif = (Short) rs.getObject(i);
						}
					}
					
					/*------------------start pegawai---------------*/
					try {
						query2 = "exec lembur.sp_getlaporanlemburpegawai ?";
						cs2 = con.prepareCall(query2);
						cs2.setInt(1, kodelembur);
						rs2 = cs2.executeQuery();
						
						list2 = new ArrayList<Map<String, Object>>();
						ResultSetMetaData metaData2 = rs2.getMetaData();
						Map<String, Object> hasil2 = null;
						
						Integer kodepenugasan = 0;
						while (rs2.next()) {
							hasil2 = new HashMap<String, Object>();
							for (int i = 1; i <= metaData2.getColumnCount(); i++) {
								hasil2.put(metaData2.getColumnName(i).toLowerCase(), rs2.getObject(i));
								if(metaData2.getColumnName(i).equalsIgnoreCase("kodepenugasan")) {
									kodepenugasan = (Integer) rs2.getObject(i);
								}
							}
							
							/*---------------start tgl------------------*/
							try {
								query3 = "exec lembur.sp_getlaporanlemburtgl ?, ?, ?";
								cs3 = con.prepareCall(query3);
								cs3.setInt(1, kodelembur);
								cs3.setInt(2, kodepenugasan);
								cs3.setShort(3, isverif);
								rs3 = cs3.executeQuery();
								
								list3 = new ArrayList<Map<String, Object>>();
								ResultSetMetaData metaData3 = rs3.getMetaData();
								Map<String, Object> hasil3 = null;
								
								while (rs3.next()) {
									hasil3 = new HashMap<String, Object>();
									for (int i = 1; i <= metaData3.getColumnCount(); i++) {
										hasil3.put(metaData3.getColumnName(i).toLowerCase(), rs3.getObject(i));
									}
									list3.add(hasil3);
								}
								hasil2.put("tgls", list3);
								
							}
							finally {
								if (rs3 != null) {
									try {
										rs3.close();
									} catch (SQLException e) {
									}
								}
								if (cs3 != null) {
									try {
										cs3.close();
									} catch (SQLException e) {
									}
								}
							}
							/* end tgl */
							list2.add(hasil2);
						}
						hasil.put("pegawai", list2);
						
					}
					finally {
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
					/* end pegawai */
					
					list.add(hasil);
					metadata.put("code", 1);
					metadata.put("message", "OK");
				}
				response.put("list", list);
				result.put("response", response);
				rs.close();
				
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
	@Path("/laporanpelaksanaan/verif")
	@Consumes("application/json")
	@Produces("application/json")
	public Response verifLaporanPelaksanaan(@Context HttpHeaders headers,
			String data) {
		
		Respon<id.go.bpjskesehatan.service.v2.lembur.entitas.Lembur> response = new Respon<id.go.bpjskesehatan.service.v2.lembur.entitas.Lembur>();
		Metadata metadata = new Metadata();
		Result<id.go.bpjskesehatan.service.v2.lembur.entitas.Lembur> result = new Result<id.go.bpjskesehatan.service.v2.lembur.entitas.Lembur>();
		
		Connection con = null;
		CallableStatement cs = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = null;

		if (SharedMethod.VerifyToken(headers, metadata)) {
		//if (true) {
			try {
				
				ObjectMapper mapper = new ObjectMapper();
				id.go.bpjskesehatan.service.v2.lembur.entitas.Lembur json = mapper.readValue(data, id.go.bpjskesehatan.service.v2.lembur.entitas.Lembur.class);
				
				for (id.go.bpjskesehatan.service.v2.lembur.entitas.SaveLemburTgls tgls : json.getTgls()) {
					try {
						query = "exec lembur.sp_verif_tgl ?,?,?,?,?,?,?,?";
						con = new Koneksi().getConnection();
						cs = con.prepareCall(query);
						cs.setInt(1, tgls.getKodepegawai());
						cs.setInt(2, tgls.getKode());
						cs.setInt(3, tgls.getStatus());
						cs.setString(4, tgls.getKeterangan());
						cs.setTime(5, tgls.getAbsenmasuk());
						cs.setTime(6, tgls.getAbsenpulang());
						cs.setInt(7, tgls.getEditjam());
						cs.setInt(8, 1);
						cs.execute();
					}
					catch (SQLException e) {
						throw new Exception(e.getMessage());
					}
					catch (Exception e) {
						e.printStackTrace();
						throw new Exception("Verifikasi gagal.");
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
				
				for (id.go.bpjskesehatan.service.v2.lembur.entitas.SaveLemburTgls tgls : json.getTgls()) {
					try {
						query = "exec lembur.sp_verif_tgl ?,?,?,?,?,?,?,?";
						con = new Koneksi().getConnection();
						cs = con.prepareCall(query);
						cs.setInt(1, tgls.getKodepegawai());
						cs.setInt(2, tgls.getKode());
						cs.setInt(3, tgls.getStatus());
						cs.setString(4, tgls.getKeterangan());
						cs.setTime(5, tgls.getAbsenmasuk());
						cs.setTime(6, tgls.getAbsenpulang());
						cs.setInt(7, tgls.getEditjam());
						cs.setInt(8, 0);
						cs.executeUpdate();
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
				
				try {
					query = "exec lembur.sp_verif ?,?";
					con = new Koneksi().getConnection();
					cs = con.prepareCall(query);
					cs.setInt(1, json.getKode());
					cs.setInt(2, json.getUseract());
					cs.executeUpdate();
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
				
				id.go.bpjskesehatan.service.v2.lembur.entitas.Lembur lembur_respon = new id.go.bpjskesehatan.service.v2.lembur.entitas.Lembur();
				lembur_respon.setIsverif(1);
				try {
					query = "select b.nama from hcis.users a \r\n" + 
							"inner join karyawan.vw_pegawai b on a.npp=b.npp \r\n" + 
							"where a.id=?";
					con = new Koneksi().getConnection();
					ps = con.prepareStatement(query);
					ps.setInt(1, json.getUseract());
					rs = ps.executeQuery();
					if(rs.next()) {
						lembur_respon.setNamaverif(rs.getString("nama"));
					}
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
				response.setData(lembur_respon);
				result.setResponse(response);
				
				metadata.setCode(1);
				metadata.setMessage("Ok.");
				
			} catch (Exception e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
				e.printStackTrace();
			} finally {

			}
		}
		
		result.setMetadata(metadata);
		return Response.ok(result).build();
	}
	
	@POST
	@Path("/gettotaljam")
	@Produces("application/json")
	public Response GetTotalJam(@Context HttpHeaders headers, String data) {
		
		Respon<Report> response = new Respon<Report>();
		Metadata metadata = new Metadata();
		Result<Report> result = new Result<Report>();
		
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String query = null;
		
		if (SharedMethod.VerifyToken(headers, metadata)) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				JsonNode json = mapper.readTree(data);
				Boolean ok = true;
				Boolean ok2 = true;
				String msg = "";
				StringBuilder str = new StringBuilder();
				
				if (!data.isEmpty()) {
					if(json.path("npp").isMissingNode()){
						str.append("npp").append(", ");
						ok = false;
					}
					
					if(!ok){
						str.replace(str.length() - 2, str.length() - 1, "");
						metadata.setMessage("data " + str + " is missing.");
					}
					else if(!ok2){
						metadata.setMessage(msg);
					}
					
				}
				else{
					metadata.setCode(0);
					metadata.setMessage("missing data.");
					ok = false;
				}
				
				if(ok && ok2){
					String npp = json.path("npp").asText();
					Integer tahun = Calendar.getInstance().get(Calendar.YEAR);
					
					query = "select top 1 totaljam from lembur.vw_totaljam where npp=? and tahun=?";
					con = new Koneksi().getConnection();
					ps = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
					ps.setString(1, npp);
					ps.setInt(2, tahun);
					rs = ps.executeQuery();
					
					metadata.setCode(0);
					metadata.setMessage("Data kosong.");
					if (rs.next()) {
						Report report = new Report();
						report.setTotaljam(rs.getInt("totaljam"));
						response.setData(report);
						metadata.setCode(1);
						metadata.setMessage("Ok.");
						result.setResponse(response);
					}		
				}
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
	
	@POST
	@Path("/hitungtagihan")
	@Produces("application/json")
	public Response GetHitungTagihanByPenugasan(@Context HttpHeaders headers, String data) {
		
		Map<String, Object> response = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> metadata = new HashMap<String, Object>();
		List<Map<String, Object>> list = null;
		
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String query = null;
		
		if (SharedMethod.VerifyToken(headers, metadata)) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				JsonNode json = mapper.readTree(data);
				Boolean ok = true;
				Boolean ok2 = true;
				String msg = "";
				StringBuilder str = new StringBuilder();
				
				if (!data.isEmpty()) {
					if(json.path("kodepenugasan").isMissingNode()){
						str.append("kodepenugasan").append(", ");
						ok = false;
					}
					if(json.path("tgl").isMissingNode()){
						str.append("tgl").append(", ");
						ok = false;
					}
					if(json.path("absenmasuk").isMissingNode()){
						str.append("absenmasuk").append(", ");
						ok = false;
					}
					if(json.path("absenmasuk").isMissingNode()){
						str.append("absenmasuk").append(", ");
						ok = false;
					}
					
					if(!ok){
						str.replace(str.length() - 2, str.length() - 1, "");
						metadata.put("message", "data " + str + " is missing.");
					}
					else if(!ok2){
						metadata.put("message", msg);
					}
					
				}
				else{
					metadata.put("code", 0);
					metadata.put("message", "missing data.");
					ok = false;
				}
				
				if(ok && ok2){
					Integer kodepenugasan = json.path("kodepenugasan").asInt();
					
					String tgl_ = json.path("tgl").asText();
					SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");
					java.util.Date parsed=dateFormat.parse(tgl_);
					Date tgl = new Date(parsed.getTime());
					
					String absenmasuk_ = json.path("absenmasuk").asText();
					SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm:ss");
					java.util.Date date1 = sdf1.parse(absenmasuk_);
					Time absenmasuk = new Time(date1.getTime());
					
					String absenpulang_ = json.path("absenpulang").asText();
					SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
					java.util.Date date2 = sdf2.parse(absenpulang_);
					Time absenpulang = new Time(date2.getTime());
					
					try {
						query = "select top 1 * from lembur.get_komponenTagihanByPenugasan(?, ?, ?, ?)";
						con = new Koneksi().getConnection();
						ps = con.prepareStatement(query);
						ps.setInt(1, kodepenugasan);
						ps.setDate(2, tgl);
						ps.setTime(3, absenmasuk);
						ps.setTime(4, absenpulang);
						rs = ps.executeQuery();
						
						list = new ArrayList<Map<String, Object>>();
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
							list.add(hasil);
							metadata.put("code", 1);
							metadata.put("message", "Ok");
						}
						response.put("list", list);
						result.put("response", response);
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
					/* end tgl */	
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
		
		result.put("metadata", metadata);
		return Response.ok(result).build();
	}
	
	private String getNamaFile(Integer kode, Integer ke, final InputStream uploadedInputStream, FormDataContentDisposition fileDetail, String file) {
		
		String namaFile = null;
		
		if(!(uploadedInputStream==null || fileDetail==null) && file.length() > 0) {
			namaFile = fileDetail.getFileName();
        	StringTokenizer st = new StringTokenizer(namaFile, ".");
        	String extension = ""; 
        	while(st.hasMoreTokens()) {
        		extension = "."+st.nextToken();
        	}
        	Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        	namaFile = kode + "." + ke + "_" + timestamp.getTime() + extension;
		}
		
		//System.out.println("namafile="+namaFile);
		return namaFile;
	}
	
	private Boolean insertFTPFile(String namaFile, final InputStream uploadedInputStream) {
		FTPClient ftpClient = null;
		Boolean upload = false;
		
		String pathFile = "file_lembur_laporan/";
		String host = null;
    	Integer port = null;
    	String user = null;
    	String pass = null;
		
		try {
			host = context.getInitParameter("ftp-host");
        	port = Integer.parseInt(context.getInitParameter("ftp-port"));
        	user = context.getInitParameter("ftp-user");
        	pass = context.getInitParameter("ftp-pass");
			
			ftpClient = new FTPClient();
			ftpClient.connect(host,port);
        	ftpClient.login(user, pass);
        	ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        	upload = ftpClient.storeFile(pathFile + namaFile, uploadedInputStream);
        	ftpClient.logout();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if(ftpClient.isConnected())
				try {
					ftpClient.disconnect();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
		return upload;
	}
	
}