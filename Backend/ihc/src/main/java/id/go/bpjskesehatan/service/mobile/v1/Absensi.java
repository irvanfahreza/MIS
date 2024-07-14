package id.go.bpjskesehatan.service.mobile.v1;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import id.go.bpjskesehatan.database.Koneksi;
import id.go.bpjskesehatan.database.KoneksiAbsensi;
import id.go.bpjskesehatan.entitas.fr.Compare2Faces;
import id.go.bpjskesehatan.util.Invoker;
import id.go.bpjskesehatan.util.MyException;
import id.go.bpjskesehatan.util.Utils;

@Path("/mobile/v1/absensi")
public class Absensi {
	
    @Context
    private ServletContext context;
    
    private static void insertAbsenLog(String npp, Float latitude, Float longitude, String alamat, String keterangan, String namaImg, String lokasiKerja) throws Exception {
		Connection con = null;
		CallableStatement cs = null;
		
		try {
			con = new KoneksiAbsensi().getConnection();
			cs = con.prepareCall("exec dbo.sp_IHC_Mobile_Insert_TBAbsensi ?,?,?,?,?,?,?");
			cs.setString(1, npp);
			cs.setFloat(2, latitude);
			cs.setFloat(3, longitude);
			cs.setString(4, alamat);
			cs.setString(5, keterangan);
			cs.setString(6, namaImg);
			if(lokasiKerja==null)
				cs.setNull(7, java.sql.Types.VARCHAR);
			else
				cs.setString(7, lokasiKerja);
			cs.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
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
    
    private String saveImage(String base64Image, String npp) {
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
        	String namaFile = npp + "-" + timestamp.getTime() + ".jpeg";
    		
        	String pathFile = "file_fotoabsensi/";
			ftpClient = new FTPClient();
			ftpClient.connect(host,port);
        	ftpClient.login(user, pass);
        	ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        	
        	byte[] decodedBytes = Base64.getDecoder().decode(base64Image);
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
	
	@POST
	@Path("/checkinout")
	@Produces("application/json")
	@Consumes("application/json")
	public Response checkInOut(
			@Context HttpHeaders headers, 
			String data,
			@PathParam("check") String check) {

		Map<String, Object> metadata = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		
		if (AuthMobile.VerifyToken(headers, metadata)) {
		//if (true) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				JsonNode json = mapper.readTree(data);
				String npp = json.path("npp").asText();
				Float latitude = (float) json.path("latitude").asDouble();
				Float longitude = (float) json.path("longitude").asDouble();
				String alamat = json.path("alamat").asText();
				String keterangan = json.path("keterangan").asText();
				String base64img = json.path("base64img").asText();
				String lokasikerja = null;
				if(!json.path("lokasikerja").isMissingNode()) {
					lokasikerja = json.path("lokasikerja").asText();
				}
				
				String namafile = saveImage(base64img, npp);
				if(namafile!=null) {
					insertAbsenLog(npp, latitude, longitude, alamat, keterangan, namafile, lokasikerja);
					metadata.put("code", 1);
					metadata.put("message", "Upload sukses");
				}
				else {
					metadata.put("code", 0);
					metadata.put("message", "Upload gagal");
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
	
	@GET
	@Path("/lihat/{npp}")
	@Produces("application/json")
	@Consumes("application/json")
	public Response absenToday(
			@Context HttpHeaders headers, 
			String data,
			@PathParam("npp") String npp) {

		Map<String, Object> metadata = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		
		if (AuthMobile.VerifyToken(headers, metadata)) {
		//if (true) {
			Connection con = null;
			CallableStatement cs = null;
			ResultSet rs = null;
			
			try {
				con = new Koneksi().getConnection();
				cs = con.prepareCall("exec hcis.sp_getAbsensiMobileToday ?");
				cs.setString(1, npp);
				rs = cs.executeQuery();
				if(rs.next()) {
					metadata.put("code", 1);
					metadata.put("message", "Ok");
					metadata.put("absenterakhir", rs.getTimestamp("absenterakhir"));
					metadata.put("fabsenterakhir", rs.getString("fabsenterakhir"));
				}
				else {
					metadata.put("code", 1);
					metadata.put("message", "Ok");
					metadata.put("absenterakhir", null);
					metadata.put("fabsenterakhir", "");
				}
			} catch (Exception e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				e.printStackTrace();
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
		
		result.put("metadata", metadata);
		return Response.ok(result).build();
	}
	
	@GET
	@Path("/rekap/tahun/{tahun}/bulan/{bulan}/npp/{npp}")
	@Produces("application/json")
	@Consumes("application/json")
	public Response absenRekap(
			@Context HttpHeaders headers, 
			String data,
			@PathParam("tahun") Integer tahun,
			@PathParam("bulan") Integer bulan,
			@PathParam("npp") String npp) {

		Map<String, Object> response = new HashMap<String, Object>();
		Map<String, Object> metadata = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> listdata = null;
		
		if (AuthMobile.VerifyToken(headers, metadata)) {
		//if (true) {
			Connection con = null;
			CallableStatement cs = null;
			ResultSet rs = null;
			
			try {
				con = new Koneksi().getConnection();
				cs = con.prepareCall("exec hcis.sp_getLaporanAbsensiMobile ?,?,?");
				cs.setString(1, npp);
				cs.setInt(2, tahun);
				cs.setInt(3, bulan);
				rs = cs.executeQuery();
				ResultSetMetaData metaData = rs.getMetaData();
				Map<String, Object> hasil = null;
				listdata = new ArrayList<Map<String, Object>>();
				metadata.put("code", 0);
				metadata.put("message", "Data tidak ditemukan");
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
					metadata.put("code", 1);
					metadata.put("message", "Ok");
				}
				response.put("list", listdata);
				result.put("response", response);
			} catch (Exception e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				e.printStackTrace();
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
		
		result.put("metadata", metadata);
		return Response.ok(result).build();
	}
	
	@GET
	@Path("/jamkerja/{npp}")
	@Produces("application/json")
	@Consumes("application/json")
	public Response jamKerja(
			@Context HttpHeaders headers, 
			String data,
			@PathParam("npp") String npp) {

		Map<String, Object> metadata = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		
		if (AuthMobile.VerifyToken(headers, metadata)) {
		//if (true) {
			Connection con = null;
			CallableStatement cs = null;
			ResultSet rs = null;
			
			try {
				con = new Koneksi().getConnection();
				cs = con.prepareCall("exec hcis.sp_getJamKerjaMobileToday ?");
				cs.setString(1, npp);
				rs = cs.executeQuery();
				if(rs.next()) {
					metadata.put("code", 1);
					metadata.put("message", "Ok");
					metadata.put("jamkerja", rs.getString("jamkerja"));
				}
				else {
					metadata.put("code", 1);
					metadata.put("message", "Ok");
					metadata.put("jamkerja", "");
				}
			} catch (Exception e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				e.printStackTrace();
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
		
		result.put("metadata", metadata);
		return Response.ok(result).build();
	}
	
	@GET
	@Path("/foto/{filename}")
	@Produces({"image/jpeg","image/jpg","image/png"})
	public Response getFotoAbsensi(@Context HttpHeaders headers, @PathParam("filename") String filename) {
		ResponseBuilder response;
		OutputStream os = null;
		FTPClient client = null;
        try {
        	String path = "/tmp/";
        	client = new FTPClient();
        	os = new BufferedOutputStream(new FileOutputStream(path + filename));
        	String host = context.getInitParameter("ftp-host");
			Integer port = Integer.parseInt(context.getInitParameter("ftp-port"));
			String user = context.getInitParameter("ftp-user");
			String pass = context.getInitParameter("ftp-pass");
        	client.connect(host,port);
        	client.login(user, pass);
        	
        	String folder = "file_fotoabsensi";
        	Boolean zxc = client.retrieveFile(folder + "/" + filename, os);
        	if(zxc) {
            	File file = new File(path + filename);
            	response = Response.ok((Object) file);
            	response.header("Content-Disposition", "inline; filename=" + filename);
				return response.build();
            }
            else {
            	File del = new File(path + filename);
            	if(del.exists()) {
            		del.delete();
            	}
            }
           
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
            	if(client != null)
            		client.disconnect();
            	if(os != null)
            		os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
		
        response = Response.noContent();
		return response.build();
	}
	
	@GET
	@Path("/check/npp/{npp}/timezone/{timezone}/platform/{platform}")
	@Produces("application/json")
	@Consumes("application/json")
	public Response checkAbsensi(
			@Context HttpHeaders headers, 
			@PathParam("npp") String npp,
			@PathParam("timezone") String timezone,
			@PathParam("platform") String platform
		) {

		Map<String, Object> metadata = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		
		if (AuthMobile.VerifyToken(headers, metadata)) {
		//if (true) {
			Connection con = null;
			CallableStatement cs = null;
			ResultSet rs = null;
			
			try {
				con = new Koneksi().getConnection();
				cs = con.prepareCall("exec hcis.sp_checkabsensi ?,?,?");
				cs.setString(1, npp);
				cs.setString(2, timezone);
				cs.setString(3, platform);
				rs = cs.executeQuery();
				if(rs.next()) {
					metadata.put("code", 1);
					metadata.put("message", "Ok");
					metadata.put("title", rs.getString("judul"));
					metadata.put("description", rs.getString("deskripsi"));
				}
				else {
					metadata.put("code", 0);
					metadata.put("message", "Not Found");
				}
			} catch (Exception e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				e.printStackTrace();
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
		
		result.put("metadata", metadata);
		return Response.ok(result).build();
	}
	
	private static void insertFaceEnrollment(String npp, String base64img, Integer userAct) throws Exception {
		Connection con = null;
		CallableStatement cs = null;
		
		try {
			con = new Koneksi().getConnection();
			cs = con.prepareCall("insert into karyawan.faceenrollment(npp,img,row_status,created_by) values (?,?,?,?)");
			cs.setString(1, npp);
			cs.setString(2, base64img);
			cs.setInt(3, 1);
			cs.setInt(4, userAct);
			cs.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
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
	
	private static List<String> getFaceLib(String npp, Integer top) throws Exception {
		Connection con = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		List<String> faces = null;
		
		try {
			con = new Koneksi().getConnection();
			String query = String.format("select top %d img from karyawan.faceenrollment where npp=? order by kode", top);
			cs = con.prepareCall(query);
			cs.setString(1, npp);
			rs = cs.executeQuery();
			List<String> rows = new ArrayList<>();
			while(rs.next()) {
				rows.add(rs.getString("img"));
			}
			if(rows.size()>0) {
				faces = new ArrayList<>();
				faces = rows;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
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
		return faces;
	}
	
	private Boolean compare2face(id.go.bpjskesehatan.entitas.fr.ResultFr result, String img1, String img2) throws MyException, Exception {		
		try {
			String api = context.getInitParameter("fr.compare2faces.api");
			String apikey = context.getInitParameter("fr.compare2faces.apikey");
			Float threshold = Float.parseFloat(context.getInitParameter("fr.compare2faces.threshold"));
			String pass = apikey+":";
			String encoding = Base64.getEncoder().encodeToString((pass).getBytes(StandardCharsets.UTF_8));
			
			MultivaluedMap<String, Object> headers = new MultivaluedHashMap<String, Object>();
			headers.add("Authorization", "Basic " + encoding);
			headers.add("Content-Type", "application/json");
			headers.add("accept", "application/json");
			
			Compare2Faces body = new Compare2Faces();
            body.setImage1(img1);
            body.setImage2(img2);
            //body.setImage1("");
            //body.setImage2("");
            body.setThreshold(threshold);
            
            String stringRespon = Invoker.request("POST", api, headers, body);
            System.out.println(stringRespon);
            ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			id.go.bpjskesehatan.entitas.fr.ResponseFr respon = mapper.readValue(stringRespon, id.go.bpjskesehatan.entitas.fr.ResponseFr.class);
			result.setResult(respon.getResponse().getResult());
			result.setMatch(respon.getResponse().getMatch());
			result.setMessage(respon.getResponse().getMessage());
            if(result.getResult()==1) {
            	return true;
            }
		} catch (MyException e) {
			throw new MyException(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
		finally {
			
		}
		return false;
	}
	
	@POST
	@Path("/faceenrollment")
	@Produces("application/json")
	@Consumes("application/json")
	public Response faceEnrollment(
			@Context HttpHeaders headers, 
			String data) {

		Map<String, Object> metadata = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		
		AuthUser authUser = new AuthUser();
		if (AuthMobile.VerifyToken(authUser, headers, metadata)) {
			//if (true) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				JsonNode json = mapper.readTree(data);
				String base64img = json.path("base64img").asText();

				insertFaceEnrollment(authUser.getNpp(), base64img, authUser.getUserid());
				metadata.put("code", 1);
				metadata.put("message", "Ok");
			} catch (MyException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
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
	@Path("/facematching")
	@Produces("application/json")
	@Consumes("application/json")
	public Response faceMatching(
			@Context HttpHeaders headers, 
			String data) {

		Map<String, Object> metadata = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		
		AuthUser authUser = new AuthUser();
		if (AuthMobile.VerifyToken(authUser, headers, metadata)) {
			//if (true) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				JsonNode json = mapper.readTree(data);
				String base64img = json.path("base64img").asText();
				
				id.go.bpjskesehatan.entitas.fr.ResultFr res;
				res = new id.go.bpjskesehatan.entitas.fr.ResultFr();
				List<String> faces = getFaceLib(authUser.getNpp(), 3);
				Boolean isMatch = false;
				Double match = 0.0;
				String pesan = null;
				if(faces!=null) {
					for (String face : faces) {
						Boolean sukses = compare2face(res, base64img, face);
						match = res.getMatch();
						pesan = res.getMessage();
						if(sukses) {
							isMatch = true;
							break;
						}
					}
				}
				else {
					throw new MyException("Belum melakukan enrollment wajah");
				}
				
				if(isMatch) {
					metadata.put("code", 1);
					metadata.put("message", "Ok");
					metadata.put("match", match);
				}
				else {
					metadata.put("code", 0);
					metadata.put("message", pesan);
					metadata.put("match", match);
				}
			} catch (MyException e) {
				metadata.put("code", 2);
				metadata.put("message", e.getMessage());
				metadata.put("match", 0.0);
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
	@Path("/facematchingenrollment")
	@Produces("application/json")
	@Consumes("application/json")
	public Response faceMatchingEnrollment(
			@Context HttpHeaders headers, 
			String data) {

		Map<String, Object> metadata = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		
		AuthUser authUser = new AuthUser();
		if (AuthMobile.VerifyToken(authUser, headers, metadata)) {
			//if (true) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				JsonNode json = mapper.readTree(data);
				String base64img = json.path("base64img").asText();
				
				id.go.bpjskesehatan.entitas.fr.ResultFr res;
				res = new id.go.bpjskesehatan.entitas.fr.ResultFr();
				List<String> faces = getFaceLib(authUser.getNpp(), 1);
				Double match = 0.0;
				String pesan = null;
				if(faces!=null) {
					Boolean sukses = compare2face(res, base64img, faces.get(0));
					match = res.getMatch();
					pesan = res.getMessage();
					if(sukses) {
						metadata.put("code", 1);
						metadata.put("message", "Ok");
						metadata.put("match", match);
					}
					else {
						metadata.put("code", 0);
						metadata.put("message", pesan);
						metadata.put("match", match);
					}
				}
				else {
					metadata.put("code", 1);
					metadata.put("message", "Ok");
					metadata.put("match", 100.0);
				}
			} catch (MyException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
			} catch (Exception e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				e.printStackTrace();
			}
		}
		
		result.put("metadata", metadata);
		return Response.ok(result).build();
	}
	
	@GET
	@Path("/faceenrollment/list")
	@Produces("application/json")
	@Consumes("application/json")
	public Response listFaceEnrollment(
			@Context HttpHeaders headers) {

		Map<String, Object> response = new HashMap<String, Object>();
		Map<String, Object> metadata = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> listdata = null;
		
		AuthUser authUser = new AuthUser();
		if (AuthMobile.VerifyToken(authUser, headers, metadata)) {
		//if (true) {
			Connection con = null;
			CallableStatement cs = null;
			ResultSet rs = null;
			
			try {
				con = new Koneksi().getConnection();
				cs = con.prepareCall("select kode, created_time from karyawan.faceenrollment where npp=? order by kode");
				cs.setString(1, authUser.getNpp());
				rs = cs.executeQuery();
				ResultSetMetaData metaData = rs.getMetaData();
				Map<String, Object> hasil = null;
				listdata = new ArrayList<Map<String, Object>>();
				metadata.put("code", 1);
				metadata.put("message", "Data tidak ditemukan");
				hasil = new HashMap<String, Object>();
				while(rs.next()) {
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
			} catch (Exception e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				e.printStackTrace();
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
		
		result.put("metadata", metadata);
		return Response.ok(result).build();
	}
	
	@GET
	@Path("/faceenrollment/delete/{kode}")
	@Produces("application/json")
	@Consumes("application/json")
	public Response deleteFaceEnrollment(
			@Context HttpHeaders headers, 
			String data,
			@PathParam("kode") Integer kode) {

		Map<String, Object> metadata = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		
		if (AuthMobile.VerifyToken(headers, metadata)) {
		//if (true) {
			Connection con = null;
			CallableStatement cs = null;
			
			try {
				con = new Koneksi().getConnection();
				cs = con.prepareCall("delete from karyawan.faceenrollment where kode=?");
				cs.setInt(1, kode);
				cs.executeUpdate();
				metadata.put("code", 1);
				metadata.put("message", "Ok");
			} catch (Exception e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				e.printStackTrace();
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
		
		result.put("metadata", metadata);
		return Response.ok(result).build();
	}
	
}
