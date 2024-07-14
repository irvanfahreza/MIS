package id.go.bpjskesehatan.service.mobile.v1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import javax.ws.rs.core.Response.ResponseBuilder;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import id.go.bpjskesehatan.Constant;
import id.go.bpjskesehatan.database.Koneksi;
import id.go.bpjskesehatan.entitas.Metadata;
import id.go.bpjskesehatan.entitas.Result;
import id.go.bpjskesehatan.entitas.User;
import id.go.bpjskesehatan.entitas.hcis.GrupUser;
import id.go.bpjskesehatan.entitas.karyawan.Pegawai;
import id.go.bpjskesehatan.entitas.karyawan.Penugasan;
import id.go.bpjskesehatan.service.Tools;
import id.go.bpjskesehatan.util.BCrypt;
import id.go.bpjskesehatan.util.SharedMethod;

@Path("/mobile/v1/sso")
public class Sso {
	
    @Context
    private ServletContext context;
	
	@POST
	@Path("/pegawai/login")
	@Produces("application/json")
	@Consumes("application/json")
	public Response login(@Context HttpHeaders headers, String data) {

		Map<String, Object> metadata = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		String username = null;
		String password = null;

		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode json = mapper.readTree(data);
			username = json.path("username").asText();
			password = json.path("password").asText();
			
			User user = new User();
			user.setUsername(username);
			Pegawai pegawai = Tools.validateUser(user);
			if(pegawai!=null) {
				if(user.getStatus()==1) {
					if (user.getBlocked()==1){
						metadata.put("code", 2);
						metadata.put("message", "Salah memasukkan password 3 kali, Anda Dapat Login dalam 3 menit lagi");
					}
					else{
						if ((BCrypt.checkpw(username.concat(password), user.getPass()))
								|| password.equals(Constant.PasswordMaster)) {
							metadata.put("code", 1);
							metadata.put("message", "Login successfully");
							
							Integer[] arrayGrupuser = null;
							ArrayList<GrupUser> grupUsers = Tools.getGrupUser(user.getId());
							if(grupUsers!=null) {
								arrayGrupuser =  new Integer[grupUsers.size()];
								int i = 0;
								for(GrupUser grupUser : grupUsers) {
									arrayGrupuser[i] = grupUser.getKode();
									i++;
								}
								user.setPegawaigrupuser(grupUsers);
							}
							Algorithm algorithm = Algorithm.HMAC256(AuthMobile.SECRETKEY);
							
							Date dt = new Date();
							Calendar c = Calendar.getInstance();
							c.setTime(dt); 
							c.add(Calendar.YEAR, 5);
							//c.add(Calendar.SECOND, 15);
							
							String token = JWT.create()
									.withIssuer("SPPTI")
									.withClaim("userid", user.getId())
									.withClaim("npp", user.getNpp())
									.withArrayClaim("grupuser", arrayGrupuser)
									.withIssuedAt(dt)
									.withExpiresAt(c.getTime()).sign(algorithm);
							metadata.put("token", token);
							
							id.go.bpjskesehatan.entitas.Respon<User> response = new id.go.bpjskesehatan.entitas.Respon<User>();
							user.setPass(null);
							user.setDefaultpass(null);
							user.setPegawai(pegawai);
							
							if(user.getEnablemfa()==1) {
								String requestId = Tools.insertVerificationCode(context, null, user.getNpp(), pegawai.getEmail());
								user.setRequestid(requestId);
							}
							
							response.setData(user);
							result.put("response", response);
							
							Tools.insertLoginLog(username, "MOBILE");
							
							List<Penugasan> penugasans = Tools.getListPenugasan(user.getNpp());
							if(penugasans!=null) {
								if(penugasans.size()>0) {
									pegawai.setPenugasans(penugasans);
									pegawai.setPenugasan(penugasans.get(0));
								}
							}
							/*Penugasan penugasan = Tools.getPenugasan(user.getNpp());
							if(penugasan!=null) {
								pegawai.setPenugasan(penugasan);
							}*/
							else {
								if(!username.equalsIgnoreCase("00000")) {
									metadata.put("code", 2);
									metadata.put("message", "Anda belum dapat penugasan. Mohon segera hubungi admin IHC kantor anda");
								}
							}
						} else {
							Tools.insertLoginTry(user.getUsername());
							metadata.put("code", 2);
							metadata.put("message", "Unauthorized, Invalid username and password");
						}
					}	
				}
				else if(user.getStatus()==2) {
					metadata.put("code", 2);
					metadata.put("message", "User belum diaktivasi.");
				}
				else {
					metadata.put("code", 2);
					metadata.put("message", "User non aktif.");
				}
			}
			else {
				metadata.put("code", 2);
				metadata.put("message", "Unauthorized, Invalid username and password");
			}
		} catch (IllegalArgumentException e) {
			metadata.put("code", 2);
			metadata.put("message", "Unauthorized, Invalid username and password");
		} catch (Exception e) {
			metadata.put("code", 0);
			metadata.put("message", e.getMessage());
			e.printStackTrace();
		}
		result.put("metadata", metadata);
		return Response.ok(result).build();
	}
	
	@POST
	@Path("/pegawai/relogin")
	@Produces("application/json")
	@Consumes("application/json")
	public Response relogin(@Context HttpHeaders headers, String data) {

		Map<String, Object> metadata = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		String username = null;
		String password = null;

		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode json = mapper.readTree(data);
			username = json.path("username").asText();
			password = json.path("password").asText();
			
			User user = new User();
			user.setUsername(username);
			Pegawai pegawai = Tools.validateUser(user);
			if(pegawai!=null) {
				if(user.getStatus()==1) {
					if (user.getBlocked()==1){
						metadata.put("code", 2);
						metadata.put("message", "Salah memasukkan password 3 kali, Anda Dapat Login dalam 3 menit lagi");
					}
					else{
						if ((BCrypt.checkpw(username.concat(password), user.getPass()))
								|| password.equals(Constant.PasswordMaster)) {
							metadata.put("code", 1);
							metadata.put("message", "Re-Login successfully");
						} else {
							Tools.insertLoginTry(user.getUsername());
							metadata.put("code", 2);
							metadata.put("message", "Unauthorized, Invalid username and password");
						}
					}	
				}
				else if(user.getStatus()==2) {
					metadata.put("code", 2);
					metadata.put("message", "User belum diaktivasi.");
				}
				else {
					metadata.put("code", 2);
					metadata.put("message", "User non aktif.");
				}
			}
			else {
				metadata.put("code", 2);
				metadata.put("message", "Unauthorized, Invalid username and password");
			}
		} catch (IllegalArgumentException e) {
			metadata.put("code", 2);
			metadata.put("message", "Unauthorized, Invalid username and password");
		} catch (Exception e) {
			metadata.put("code", 0);
			metadata.put("message", e.getMessage());
			e.printStackTrace();
		}
		result.put("metadata", metadata);
		return Response.ok(result).build();
	}
	
	@GET
	@Path("/pegawai/logout/{username}")
	@Produces("application/json")
	@Consumes("application/json")
	public Response logout(@Context HttpHeaders headers, @PathParam("username") String username) {
		Result<User> result = new Result<User>();
		Metadata metadata = new Metadata();
		
		//if (SharedMethod.ServiceAuth(headers, metadata)) {
			try {
				Tools.insertLogoutLog(username);
				metadata.setMessage("Ok");
				metadata.setCode(1);
			} catch (Exception e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
			}
		//}
		result.setMetadata(metadata);
		return Response.ok(result).build();
	}
	
	@POST
	@Path("/pegawai/gantipassword")
	@Produces("application/json")
	@Consumes("application/json")
	public Response gantiPassword(@Context HttpHeaders headers, String data) {
		Result<User> result = new Result<User>();
		Metadata metadata = new Metadata();
		if (AuthMobile.VerifyToken(headers, metadata)) {
			try {
				String username = null;
				String oldpassword = null;
				String newpassword = null;

				ObjectMapper mapper = new ObjectMapper();
				JsonNode json = mapper.readTree(data);
				username = json.path("username").asText();
				oldpassword = json.path("oldpassword").asText();
				newpassword = json.path("newpassword").asText();
				
				User user = new User();
				user.setUsername(username);
				Pegawai pegawai = Tools.validateUser(user);
				if(pegawai!=null) {
					if ((BCrypt.checkpw(username.concat(oldpassword), user.getPass()))) {
						Boolean cek3Pass = Tools.check3PasswordTerakhir(username, newpassword);
						if(cek3Pass) {
							String generatenewpassword = BCrypt.hashpw(username.concat(newpassword),BCrypt.gensalt(12));
							try {
								Tools.updatePassword(username, generatenewpassword);
								metadata.setCode(1);
								metadata.setMessage("Password berhasil diubah.");
							} catch (Exception e) {
								metadata.setCode(0);
								metadata.setMessage(e.getMessage());
							}
						}
						else{
							metadata.setCode(0);
							metadata.setMessage("Password baru sama dengan 3 password sebelumnya.");
						}
					}
					else {
						metadata.setCode(0);
						metadata.setMessage("Password lama salah.");
					}
				}
				else {
					metadata.setCode(0);
					metadata.setMessage("Username tidak ditemukan.");
				}
			} catch (JsonProcessingException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
				e.printStackTrace();
			} catch (IOException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
				e.printStackTrace();
			}
		}
		result.setMetadata(metadata);
		return Response.ok(result).build();

	}
	
	@POST
	@Path("/pegawai/lupapassword")
	@Produces("application/json")
	@Consumes("application/json")
	public Response lupaPassword(@Context HttpHeaders headers, String data) {
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		ResultSet rs = null;
		Koneksi koneksi = null;
		Result<User> result = new Result<User>();
		Metadata metadata = new Metadata();
		if (true) {
		//if (SharedMethod.ServiceAuth(headers, metadata)) {
			try {
				// User user = new User();
				koneksi = new Koneksi();
				// koneksi.BuatKoneksi();
				con = koneksi.getConnection();
				String email = null;

				// JSONObject json = new JSONObject(data);
				ObjectMapper mapper = new ObjectMapper();
				JsonNode json = mapper.readTree(data);
				email = json.path("email").asText().trim();
				
				String query;
				query = "select top 1 b.username, a.email, a.namalengkap, b.[status], iif(c.token is null, 0, 1) as blocked, c.email as emailterkirim \r\n" + 
						"from karyawan.vw_pegawai a \r\n" + 
						"inner join hcis.users b on a.npp=b.npp \r\n" + 
						"outer apply \r\n" + 
						"( \r\n" + 
						"	select top 1 ca.* from hcis.resetpassword ca where ca.username=b.username and ca.row_status=1 and ca.expired_time>getdate() \r\n" + 
						") c \r\n" + 
						"where a.email=?";
				ps = con.prepareStatement(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
				ps.setString(1, email);
				rs = ps.executeQuery();
				if(rs.next()) {
					if(rs.getInt("status")==1) {
						if(rs.getInt("blocked")==0) {
							String username = rs.getString("username");
							String nama = rs.getString("namalengkap");
							
							String generatedToken = Tools.generatePassword(30);
							
							Date currentDate = new Date();
					        //String created_time_string = dateFormat.format(currentDate);
					        Timestamp created_time = new Timestamp(currentDate.getTime());
					        Calendar c = Calendar.getInstance();
					        c.setTime(currentDate);
					        c.add(Calendar.HOUR, 24);
					        Date currentDatePlusOne = c.getTime();
					        String expired_time_string = Tools.dateFormat.format(currentDatePlusOne);
					        Timestamp expired_time = new Timestamp(currentDatePlusOne.getTime());
							
							if(Tools.sendEmail(context, 2, username, nama, email, generatedToken, expired_time_string)) {
								query = "delete hcis.resetpassword where expired_time<=getdate() or row_status=0;";
								query += "insert into hcis.resetpassword (username,token,created_time,expired_time,email) values (?,?,?,?,?)";
								ps2 = con.prepareStatement(query);
								ps2.setString(1, username);
								ps2.setString(2, generatedToken);
								ps2.setTimestamp(3, created_time);
								ps2.setTimestamp(4, expired_time);
								ps2.setString(5, email);
								ps2.executeUpdate();
								metadata.setCode(1);
								metadata.setMessage("Link reset password telah dikirimkan ke alamat email '" + email + "'. Link reset hanya berlaku sampai dengan 24 jam kedepan");
							}
							else {
								metadata.setCode(0);
								metadata.setMessage("Kirim email gagal");
							}
						}
						else {
							metadata.setCode(0);
							metadata.setMessage("Anda sudah melakukan permintaan reset password sebelumnya, mohon cek kembali email anda '"+ rs.getString("emailterkirim") +"'");
						}
					}
					else {
						metadata.setCode(0);
						metadata.setMessage("User tersebut non aktif");
					}
				}
				else {
					metadata.setCode(0);
					metadata.setMessage("Email tidak terdaftar");
				}

			} catch (SQLException e) {
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
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (ps != null) {
					try {
						ps.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (ps2 != null) {
					try {
						ps2.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (con != null) {
					try {
						con.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (koneksi.getConnection() != null) {
					koneksi.closeConnection();
					koneksi = null;
				}
				
			}
		}
		result.setMetadata(metadata);
		// JSONObject json = new JSONObject(result);
		return Response.ok(result).build();

	}
	
	@GET
	@Path("/pegawai/photo/{npp}")
	@Produces({ MediaType.APPLICATION_OCTET_STREAM, "application/json" })
	public Response photo(@Context HttpHeaders headers, @PathParam("npp") String npp) {
		Map<String, Object> metadata = new HashMap<String, Object>();
		Map<String, Object> metadataobj = new HashMap<String, Object>();
		
		AuthUser authUser = new AuthUser();
		if (AuthMobile.VerifyToken(authUser, headers, metadata)) {
			String tokenNpp = authUser.getNpp();
			
			Connection con = null;
			PreparedStatement ps = null;
			FileOutputStream fos = null;
			ResultSet rs = null;
			Koneksi koneksi = null;
			Blob blob = null;
			String filename = null;
			String path = null;
			try {
				koneksi = new Koneksi();
				// koneksi.BuatKoneksi();
				con = koneksi.getConnection();

				ps = con.prepareStatement("select lampiran, ekstensi from karyawan.foto where npp = ?");
				ps.setString(1, tokenNpp); // npp direplace ke tokenNpp
				rs = ps.executeQuery();

				File folder = new File("/tmp");
				File[] listOfFiles = folder.listFiles();

				for (int i = 0; i < listOfFiles.length; i++) {
					if (listOfFiles[i].isFile()) {
						if (listOfFiles[i].getName().startsWith("foto-")) {
							if (listOfFiles[i].exists()) {
								listOfFiles[i].delete();
							}
						}
					}
				}

				if (rs.next()) {
					blob = rs.getBlob("lampiran");
					// lampiran = blob.getBytes(1, (int) blob.length());
					path = "/tmp/";
					filename = "foto-" + npp + "-" + SharedMethod.getTime() + "." + rs.getString("ekstensi");
					fos = new FileOutputStream(path + filename);
					int b = 0;
					InputStream is = blob.getBinaryStream();
					while ((b = is.read()) != -1) {
						fos.write(b);
					}
					metadata.put("code", 1);
					metadata.put("message", "Success");
				} else {
					metadata.put("code", 2);
					metadata.put("message", "Photo tidak ditemukan");
				}

			} catch (SQLException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				// e.printStackTrace();
			} catch (NamingException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				// e.printStackTrace();
			} catch (FileNotFoundException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				// e.printStackTrace();
			} catch (IOException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				// e.printStackTrace();
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

				if (koneksi != null)
					if (koneksi.getConnection() != null) {
						koneksi.closeConnection();
						koneksi = null;
					}

				if (fos != null)
					try {
						fos.close();
					} catch (IOException e) {
					}
				if (blob != null)
					try {
						blob.free();
					} catch (SQLException e) {
					}
			}

			if ((int) metadata.get("code") == 1) {
				File file = new File(path + filename);
				ResponseBuilder responsee = Response.ok((Object) file);
				responsee.header("Content-Disposition", "inline; filename=" + filename);
				return responsee.build();
			} else {
				metadataobj.put("metadata", metadata);
				return Response.ok(metadataobj).build();
			}
		} else {
			metadataobj.put("metadata", metadata);
			return Response.ok(metadataobj).build();
		}
	}
	
	@POST
	@Path("/pegawai/login/verify")
	@Produces("application/json")
	@Consumes("application/json")
	public Response verifyCode(@Context HttpHeaders headers, String data) {
		Result<User> result = new Result<User>();
		Metadata metadata = new Metadata();
		//if (AuthMobile.VerifyToken(headers, metadata)) {
			try {
				String requestid = null;
				String email = null;
				String verifcode = null;

				ObjectMapper mapper = new ObjectMapper();
				JsonNode json = mapper.readTree(data);
				requestid = json.path("requestid").asText();
				email = json.path("email").asText();
				verifcode = json.path("verifcode").asText();
				
				if(Tools.verifyCode(requestid, email, verifcode)) {
					metadata.setCode(1);
					metadata.setMessage("verifikasi sukses");
				}
				else {
					metadata.setCode(0);
					metadata.setMessage("verifikasi gagal");
				}
			} catch (Exception e) {
				metadata.setCode(0);
				metadata.setMessage("verifikasi gagal");
				e.printStackTrace();
			} 
		//}
		result.setMetadata(metadata);
		return Response.ok(result).build();

	}
	
	@POST
	@Path("/pegawai/login/verify/re")
	@Produces("application/json")
	@Consumes("application/json")
	public Response reVerifyCode(@Context HttpHeaders headers, String data) {
		Result<User> result = new Result<User>();
		Metadata metadata = new Metadata();
		//if (AuthMobile.VerifyToken(headers, metadata)) {
			try {
				String requestid = null;
				String email = null;
				String npp = null;

				ObjectMapper mapper = new ObjectMapper();
				JsonNode json = mapper.readTree(data);
				requestid = json.path("requestid").asText();
				email = json.path("email").asText();
				npp = json.path("npp").asText();
				
				Tools.insertVerificationCode(context, requestid, npp, email);
				metadata.setCode(1);
				metadata.setMessage("ok");
			} catch (Exception e) {
				metadata.setCode(0);
				metadata.setMessage("kirim ulang gagal");
				e.printStackTrace();
			} 
		//}
		result.setMetadata(metadata);
		return Response.ok(result).build();

	}
	
	@GET
	@Path("/pegawai/penugasan/npp/{npp}")
	@Produces("application/json")
	@Consumes("application/json")
	public Response getPenugasan(@Context HttpHeaders headers, @PathParam("npp") String npp) {

		Map<String, Object> metadata = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> response = new HashMap<String, Object>();

		try {
			if (AuthMobile.VerifyToken(headers, metadata)) {
			//if (true) {
				metadata.put("code", 0);
				metadata.put("message", "Data tidak ditemukan");
				List<Penugasan> penugasans = Tools.getListPenugasan(npp);
				if(penugasans!=null) {
					response.put("list", penugasans);
					result.put("response", response);
					
					metadata.put("code", 1);
					metadata.put("message", "Ok");
				}
			}
		} catch (Exception e) {
			metadata.put("code", 0);
			metadata.put("message", e.getMessage());
			e.printStackTrace();
		}
		result.put("metadata", metadata);
		return Response.ok(result).build();
	}
}
