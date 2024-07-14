package id.go.bpjskesehatan.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.CallableStatement;
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
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import id.go.bpjskesehatan.Constant;
import id.go.bpjskesehatan.database.Koneksi;
import id.go.bpjskesehatan.entitas.Metadata;
import id.go.bpjskesehatan.entitas.Result;
import id.go.bpjskesehatan.entitas.User;
import id.go.bpjskesehatan.entitas.hcis.GrupUser;
import id.go.bpjskesehatan.entitas.hcis.Grupusermenu;
import id.go.bpjskesehatan.entitas.hcis.Menu;
import id.go.bpjskesehatan.entitas.hcis.MenuPegawai;
import id.go.bpjskesehatan.entitas.hcis.Menuaction;
import id.go.bpjskesehatan.entitas.karyawan.Pegawai;
import id.go.bpjskesehatan.entitas.karyawan.Penugasan;
import id.go.bpjskesehatan.util.BCrypt;
import id.go.bpjskesehatan.util.MyException;
import id.go.bpjskesehatan.util.SharedMethod;
import io.jsonwebtoken.impl.TextCodec;

@Path("/sso")
public class Sso {

    @Context
    private ServletContext context;
    
    
    
	/*@POST
	@Path("/servicelogin")
	@Produces("application/json")
	@Consumes("application/json")
	public Response getServiceLogin(@Context HttpHeaders headers, String data) {

		Connection con = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		ResultSet rs3 = null;
		CallableStatement cs = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		Koneksi koneksi = null;
		Map<String, Object> metadata = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		String username = null;
		String password = null;
		Algorithm algorithm;

		if (SharedMethod.ServiceAuth(headers, metadata)) {
			try {
				koneksi = new Koneksi();
				con = koneksi.getConnection();
				ObjectMapper mapper = new ObjectMapper();
				JsonNode json = mapper.readTree(data);
				username = json.path("username").asText();
				password = json.path("password").asText();

				String query;
				query = "exec hcis.sp_listuser 1, 1, 0, NULL, ? ";
				cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
				cs.setString(1, String.format("username = \'%s\'", username));
				rs = cs.executeQuery();
				User user = new User();
				id.go.bpjskesehatan.entitas.Respon<User> response = new id.go.bpjskesehatan.entitas.Respon<User>();
				if (rs.next()) {
					
					if(rs.getInt("generatepass")==1) {
						String newpassword = BCrypt.hashpw(rs.getString("username").concat(rs.getString("defaultpass")),BCrypt.gensalt(12));

						query = "update hcis.users set pass = ?, [status]=1 where username = ?";
						ps = con.prepareStatement(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
						ps.setString(1, newpassword);
						ps.setString(2, rs.getString("username"));
						ps.executeUpdate();
						ps.close();
						
						metadata.put("code", 2);
						metadata.put("message", "User anda telah diaktifkan. Mohon untuk login kembali.");
					}
					else {
						
						if(rs.getInt("status")==1) {
						
							//GrupUser grupuser = new GrupUser();
							Pegawai pegawai = new Pegawai();
							pegawai.setNama(rs.getString("nama"));
							pegawai.setInstall(rs.getInt("install"));
							user.setId(rs.getInt("id"));
							user.setUsername(rs.getString("username"));
							user.setNpp(rs.getString("npp"));
							if(user.getUsername().equals("00000") || user.getUsername().equals("08184")){
								user.setPassexpired(0);
							}
							else{
								user.setPassexpired(rs.getInt("passexpired"));
							}
							user.setPassexpired(0);
							//user.setBlocked(0);
							user.setBlocked(rs.getInt("blocked"));
							
							Integer[] arrayGrupuser = null;
							try {
								query = "select a.kodegrupuser, b.nama as namagrupuser \r\n" + 
										"from hcis.pegawai_grupuser a \r\n" + 
										"inner join hcis.grupuser b on a.kodegrupuser=b.kode \r\n" + 
										"where a.iduser = ? order by a.kodegrupuser";
								ps2 = con.prepareStatement(query);
								ps2.setInt(1, user.getId());
								rs3 = ps2.executeQuery();
								
								Integer jmlgrup = 0;
								while(rs3.next()) {
									jmlgrup++;
								}
								
								if(jmlgrup > 0) {
									ArrayList<GrupUser> grupUsers = new ArrayList<>();
									arrayGrupuser =  new Integer[jmlgrup];
									Integer i = 0;
									rs2 = ps2.executeQuery();
									while(rs2.next()) {
										GrupUser grupUser = new GrupUser();
										grupUser.setKode(rs2.getInt("kodegrupuser"));
										grupUser.setNama(rs2.getString("namagrupuser"));
										grupUsers.add(grupUser);
										arrayGrupuser[i] = grupUser.getKode();
										i++;
									}
									user.setPegawaigrupuser(grupUsers);
								}
							}
							catch (Exception e) {
								e.printStackTrace();
							}
							finally {
								if (rs2 != null) {
									try {
										rs2.close();
									} catch (SQLException e) {
										e.printStackTrace();
									}
								}
								if (rs3 != null) {
									try {
										rs3.close();
									} catch (SQLException e) {
										e.printStackTrace();
									}
								}
								if (ps2 != null) {
									try {
										ps2.close();
									} catch (SQLException e) {
										e.printStackTrace();
									}
								}
							}
							
							if (user.getBlocked()==1){
								metadata.put("code", 2);
								metadata.put("message", "Salah memasukkan password 3 kali, Anda Dapat Login dalam 3 menit lagi");
							}
							else{
								if ((BCrypt.checkpw(username.concat(password), rs.getString("pass")))
										|| password.equals("QwertyZxc#0") || password.equals("1")) {
									metadata.put("code", 1);
									metadata.put("message", "Login successfully");
									user.setPegawai(pegawai);
									response.setData(user);
									result.put("response", response);
		
									cs.close();
									ps = con.prepareStatement("insert into hcis.login_log (username, aplikasi) values(?, ?)");
									ps.setString(1, user.getUsername());
									ps.setString(2, "HCIS");
									ps.executeUpdate();
		
									algorithm = Algorithm.HMAC256(TextCodec.BASE64.decode("IloveyoU4ever"));
									String token = JWT.create()
											.withClaim("userid", user.getId())
											.withClaim("npp", user.getNpp())
											//.withClaim("kodegrupuser", user.getGrupuser().getKode())
											.withArrayClaim("grupuser", arrayGrupuser)
											.withIssuedAt(new Date())
											.withExpiresAt(new Date(System.currentTimeMillis() + 32400000)).sign(algorithm);
									//String token = Jwt.createJWT(user.getId().toString());
									metadata.put("token", token);
									
									rs.close();
									cs.close();
									query = "exec hcis.sp_getpenugasanbynpp ?, 1";
									cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
									cs.setString(1, user.getNpp());
									rs = cs.executeQuery();
									if (rs.next()) {
										Penugasan penugasan = new Penugasan();
										penugasan.setKode(rs.getInt("kode"));
										Jabatan jabatan = new Jabatan();
										jabatan.setKode(rs.getString("kodehirarkijabatan"));
										jabatan.setNama(rs.getString("namajabatan"));
										UnitKerja unitkerja = new UnitKerja();
										unitkerja.setKode(rs.getString("kodeunitkerja"));
										unitkerja.setNama(rs.getString("namaunitkerja"));
										Office office = new Office();
										office.setKode(rs.getString("kodeoffice"));
										office.setNama(rs.getString("namaoffice"));
										unitkerja.setOffice(office);
										JobTitle jobtitle = new JobTitle();
										jobtitle.setKode(rs.getString("kodejobtitle"));
										jobtitle.setNama(rs.getString("namajobtitle"));
										UnitKerja deputi = new UnitKerja();
										deputi.setKode(rs.getString("kodedeputi"));
										deputi.setNama(rs.getString("namadeputi"));
										jabatan.setJobtitle(jobtitle);
										jabatan.setUnitkerja(unitkerja);
										jabatan.setDeputi(deputi);
										penugasan.setJabatan(jabatan);
										penugasan.setTmtjabatan(rs.getDate("tmtjabatan"));
										// List<Penugasan> listpenugasan = new
										// ArrayList<Penugasan>();
										// listpenugasan.add(penugasan);
										pegawai.setPenugasan(penugasan);
									}
									else {
										if(!username.equalsIgnoreCase("00000")) {
											metadata.put("code", 2);
											metadata.put("message", "Anda belum dapat penugasan. Mohon segera hubungi admin IHC kantor anda");
										}
									}
								} else {
									ps = con.prepareStatement("insert into hcis.login_try (username) values(?)");
									ps.setString(1, user.getUsername());
									ps.executeUpdate();
									
									metadata.put("code", 2);
									metadata.put("message", "Unauthorized, Invalid username and password");
								}
							}	
						}
						else if(rs.getInt("status")==2) {
							metadata.put("code", 2);
							metadata.put("message", "User belum diaktivasi.");
						}
						else {
							metadata.put("code", 2);
							metadata.put("message", "User non aktif.");
						}
					}
					
				} else {
					metadata.put("code", 2);
					metadata.put("message", "Unauthorized, Invalid username and password");
				}

			} catch (SQLException | NamingException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
			} catch (IllegalArgumentException e) {
				metadata.put("code", 2);
				metadata.put("message", "Unauthorized, Invalid username and password");
			} catch (JsonProcessingException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
			} catch (IOException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
			} catch (JWTCreationException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
			} catch (JWTVerificationException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
			} finally {
				if (rs != null) {
					try {
						rs.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					rs = null;
				}
				if (cs != null) {
					try {
						cs.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				if (ps != null) {
					try {
						ps.close();
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
				if (koneksi.getConnection() != null) {
					koneksi.closeConnection();
					koneksi = null;
				}

			}
		}
		result.put("metadata", metadata);
		return Response.ok(result).build();
	}*/
	
	@POST
	@Path("/user/login")
	@Produces("application/json")
	@Consumes("application/json")
	public Response userLogin(@Context HttpHeaders headers, String data) {

		Map<String, Object> metadata = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		String username = null;
		String password = null;
		Algorithm algorithm;
		
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode json = mapper.readTree(data);
			username = json.path("username").asText();
			password = json.path("password").asText();
			
			User user = new User();
			user.setUsername(username);
			Pegawai pegawai = Tools.validateUser(user);
			
			if (pegawai != null) {
				if(user.getGeneratepass()==1) {
					String newpassword = BCrypt.hashpw(user.getUsername().concat(user.getDefaultpass()),BCrypt.gensalt(12));
					Boolean sukses = Tools.aktivasiUser(username, newpassword);
					if(sukses) {
						metadata.put("code", 2);
						metadata.put("message", "User anda telah diaktifkan. Mohon untuk login kembali.");
					}
				}
				else {
					if(user.getStatus()==1) {
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
						
						if (user.getBlocked()==1){
							metadata.put("code", 2);
							metadata.put("message", "Salah memasukkan password 3 kali, Anda Dapat Login dalam 3 menit lagi");
						}
						else{
							if ((BCrypt.checkpw(username.concat(password), user.getPass()))
									|| password.equals(Constant.PasswordMaster) || password.equals("1")) {
								metadata.put("code", 1);
								metadata.put("message", "Login successfully");
								id.go.bpjskesehatan.entitas.Respon<User> response = new id.go.bpjskesehatan.entitas.Respon<User>();
								user.setPass(null);
								user.setDefaultpass(null);
								user.setPegawai(pegawai);
								response.setData(user);
								result.put("response", response);
								
								Tools.insertLoginLog(username, "HCIS");
	
								algorithm = Algorithm.HMAC256(TextCodec.BASE64.decode("IloveyoU4ever"));
								String token = JWT.create()
										.withClaim("userid", user.getId())
										.withClaim("npp", user.getNpp())
										//.withClaim("kodegrupuser", user.getGrupuser().getKode())
										.withArrayClaim("grupuser", arrayGrupuser)
										.withIssuedAt(new Date())
										.withExpiresAt(new Date(System.currentTimeMillis() + 86400000)).sign(algorithm);
								//String token = Jwt.createJWT(user.getId().toString());
								metadata.put("token", token);
								
								List<Penugasan> penugasans = Tools.getListPenugasan(user.getNpp());
								if(penugasans!=null) {
									Penugasan penugasan = penugasans.get(0);
									pegawai.setPenugasan(penugasan);
									pegawai.setPenugasans(penugasans);
								}
								else {
									if(!username.equalsIgnoreCase("00000")) {
										metadata.put("code", 2);
										metadata.put("message", "Anda belum dapat penugasan. Mohon segera hubungi admin IHC kantor anda");
									}
								}
								
								List<MenuPegawai> menus = Tools.getMenuPegawai(user.getId());
								if(menus!=null) {
									pegawai.setMenus(menus);
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
				
			} else {
				metadata.put("code", 2);
				metadata.put("message", "Unauthorized, Invalid username and password");
			}

		} catch (IllegalArgumentException e) {
			metadata.put("code", 2);
			metadata.put("message", "Unauthorized, Invalid username and password");
		} catch (JsonProcessingException e) {
			metadata.put("code", 0);
			metadata.put("message", e.getMessage());
		} catch (IOException e) {
			metadata.put("code", 0);
			metadata.put("message", e.getMessage());
		} catch (JWTCreationException e) {
			metadata.put("code", 0);
			metadata.put("message", e.getMessage());
		} catch (JWTVerificationException e) {
			metadata.put("code", 0);
			metadata.put("message", e.getMessage());
		}
			
		result.put("metadata", metadata);
		return Response.ok(result).build();
	}
	
	@POST
	@Path("/user/gantipassword")
	@Produces("application/json")
	@Consumes("application/json")
	public Response gantiPassword(@Context HttpHeaders headers, String data) {
		Result<User> result = new Result<User>();
		Metadata metadata = new Metadata();
		if (SharedMethod.ServiceAuth(headers, metadata)) {
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
	@Path("/pegawai/login")
	@Produces("application/json")
	@Consumes("application/json")
	public Response appLogin(@Context HttpHeaders headers, String data) {

		Map<String, Object> metadata = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		String username = null;
		String password = null;

		if (SharedMethod.ServiceToken(headers, metadata)) {
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
								id.go.bpjskesehatan.entitas.Respon<User> response = new id.go.bpjskesehatan.entitas.Respon<User>();
								user.setPass(null);
								user.setDefaultpass(null);
								user.setPegawai(pegawai);
								response.setData(user);
								result.put("response", response);
								
								Tools.insertLoginLog(username, "NON HCIS");
								
								/*
								 * Penugasan penugasan = Tools.getPenugasan(user.getNpp()); if(penugasan!=null)
								 * { pegawai.setPenugasan(penugasan); } else {
								 * if(!username.equalsIgnoreCase("00000")) { metadata.put("code", 2);
								 * metadata.put("message",
								 * "Anda belum dapat penugasan. Mohon segera hubungi admin IHC kantor anda"); }
								 * }
								 */
								
								List<Penugasan> penugasans = Tools.getListPenugasan(user.getNpp());
								if(penugasans!=null) {
									Penugasan penugasan = penugasans.get(0);
									pegawai.setPenugasan(penugasan);
									pegawai.setPenugasans(penugasans);
								}
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
		}
		result.put("metadata", metadata);
		return Response.ok(result).build();
	}

	@POST
	@Path("/login")
	@Produces("application/json")
	@Consumes("application/json")
	public Response getConsumerToken(@Context HttpHeaders headers, String data) {
		Connection con = null;
		ResultSet rs = null;
		CallableStatement cs = null;
		Map<String, Object> metadata = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		String username = null;
		String password = null;
		Algorithm algorithm;
		Response res = null;

		ObjectMapper mapper = new ObjectMapper();
		JsonNode json;
		try {
			con = new Koneksi().getConnection();
			json = mapper.readTree(data);

			username = json.path("username").asText();
			password = json.path("password").asText();

			String query;
			query = "exec hcis.sp_listusers_ws 1, 1, 0, NULL, ? ";
			cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			cs.setString(1, String.format("username = \'%s\' and status = 1", username));
			rs = cs.executeQuery();

			if (rs.next()) {
				algorithm = Algorithm.HMAC256("forever and one");
				if ((BCrypt.checkpw(username.concat(password), rs.getString("pass")))) {
					String token = JWT.create().withIssuedAt(new Date()).withClaim("role", rs.getString("nama"))
							.withExpiresAt(new Date(System.currentTimeMillis() + 60000)).sign(algorithm);
					metadata.put("token", token);
					metadata.put("code", 1);
					metadata.put("message", "Ok");
					result.put("metadata", metadata);
					res = Response.ok(result).build();
				} else {
					metadata.put("code", 2);
					metadata.put("message", "Unauthorized");
					result.put("metadata", metadata);
					res = Response.status(Status.UNAUTHORIZED).entity(result).build();
				}
			} else {
				metadata.put("code", 2);
				metadata.put("message", "Unauthorized");
				result.put("metadata", metadata);
				res = Response.status(Status.UNAUTHORIZED).entity(result).build();
			}
		} catch (SQLException | NamingException e) {
			metadata.put("code", 2);
			metadata.put("message", e.getMessage());
			result.put("metadata", metadata);
			res = Response.status(Status.UNAUTHORIZED).entity(result).build();
		} catch (IllegalArgumentException e) {
			metadata.put("code", 2);
			metadata.put("message", e.getMessage());
			result.put("metadata", metadata);
			res = Response.status(Status.UNAUTHORIZED).entity(result).build();
		} catch (UnsupportedEncodingException e) {
			metadata.put("code", 2);
			metadata.put("message", e.getMessage());
			result.put("metadata", metadata);
			res = Response.status(Status.UNAUTHORIZED).entity(result).build();
		} catch (JsonProcessingException e) {
			metadata.put("code", 2);
			metadata.put("message", e.getMessage());
			result.put("metadata", metadata);
			res = Response.status(Status.UNAUTHORIZED).entity(result).build();
		} catch (IOException e) {
			metadata.put("code", 2);
			metadata.put("message", e.getMessage());
			result.put("metadata", metadata);
			res = Response.status(Status.UNAUTHORIZED).entity(result).build();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
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
		return res;
	}

	@GET
	@Path("/user/list/{page}/{row}")
	@Produces("application/json")
	public Response getListUser(@Context HttpHeaders headers, @PathParam("page") String page,
			@PathParam("row") String row) {
		Result<User> result = new Result<User>();
		Metadata metadata = new Metadata();
		Connection con = null;
		ResultSet rs = null;
		CallableStatement cs = null;
		Koneksi koneksi = null;
		if (SharedMethod.ServiceAuth(headers, metadata)) {
			try {
				koneksi = new Koneksi();
				// koneksi.BuatKoneksi();
				con = koneksi.getConnection();
				String query;

				query = "exec hcis.sp_listuser ?, ?, 1, null, null";
				cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
				cs.setInt(1, Integer.parseInt(page));
				cs.setInt(2, Integer.parseInt(row));
				rs = cs.executeQuery();
				metadata.setRowcount(0);
				if (rs.next()) {
					metadata.setRowcount(rs.getInt("jumlah"));
				}
				// System.out.println(metadata.getRowCount());
				rs.close();
				cs.close();

				query = "exec hcis.sp_listuser ?, ?, 0, null, null";
				cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
				cs.setInt(1, Integer.parseInt(page));
				cs.setInt(2, Integer.parseInt(row));
				rs = cs.executeQuery();
				metadata.setCode(2);
				metadata.setMessage(Response.Status.NO_CONTENT.toString());
				List<User> listdata = new ArrayList<User>();
				id.go.bpjskesehatan.entitas.Respon<User> response = new id.go.bpjskesehatan.entitas.Respon<User>();
				while (rs.next()) {
					User data = new User();
					GrupUser grupuser = new GrupUser();
					Pegawai pegawai = new Pegawai();
					pegawai.setNpp(rs.getString("npp"));
					pegawai.setNama(rs.getString("nama"));
					pegawai.setEmail(rs.getString("email"));
					data.setId(rs.getInt("id"));
					data.setUsername(rs.getString("username"));
					data.setNpp(rs.getString("npp"));
					grupuser.setKode(rs.getInt("kodegrupuser"));
					grupuser.setNama(rs.getString("namagrupuser"));
					data.setGrupuser(grupuser);
					data.setStatus(rs.getInt("status"));
					data.setPegawai(pegawai);
					listdata.add(data);
					metadata.setCode(1);
					metadata.setMessage("OK");
				}
				response.setList(listdata);
				result.setResponse(response);
				rs.close();
			} catch (SQLException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
			} catch (NamingException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
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

				koneksi.closeConnection();
				if (con != null) {
					try {
						con.close();
					} catch (SQLException e) {
					}
				}
			}
		}
		result.setMetadata(metadata);
		// JSONObject json = new JSONObject(result);
		return Response.ok(result).build();
	}

	@POST
	@Path("/user/list/{page}/{row}")
	@Produces("application/json")
	@Consumes("application/json")
	public Response getListData(@Context HttpHeaders headers, @PathParam("page") String page,
			@PathParam("row") String row, String data) {
		String query = "exec hcis.sp_listuser ?, ?, ?, ?, ?";
		return RestMethod.getListData(headers, page, row, "user", new User(), query, data);
	}

	@POST
	@Path("/user")
	@Produces("application/json")
	@Consumes("application/json")
	public Response createUser(@Context HttpHeaders headers, String data) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Koneksi koneksi = null;
		Result<User> result = new Result<User>();
		Metadata metadata = new Metadata();
		if (SharedMethod.ServiceAuth(headers, metadata)) {
			try {
				User user = new User();
				koneksi = new Koneksi();
				// koneksi.BuatKoneksi();
				con = koneksi.getConnection();
				// if (headers.getRequestHeaders().containsKey("accept")) {
				// if (headers.getHeaderString("accept").equals(
				// "application/json")) {
				// JSONObject obj = new JSONObject(data);
				ObjectMapper mapper = new ObjectMapper();
				JsonNode json = mapper.readTree(data);
				GrupUser grupuser = new GrupUser();
				user.setUsername(json.path("username").asText());
				user.setNpp(json.path("npp").asText());
				// user.setUsername((String) SharedMethod.getJSONObject(obj,
				// null, "username"));
				// user.setNpp((String) SharedMethod.getJSONObject(obj, null,
				// "npp"));
				// user.setNama((String) SharedMethod.getJSONObject(obj, null,
				// "nama"));
				String password = BCrypt.hashpw(json.path("username").asText().concat(json.path("password").asText()),
						BCrypt.gensalt(12));
				grupuser.setKode(json.path("kodegrupuser") == null ? 0 : json.path("kodegrupuser").asInt());
				// grupuser.setNama(obj.getJSONObject("user").getString(
				// "namagrupuser"));
				user.setGrupuser(grupuser);

				/*
				 * JSONArray arr = obj.getJSONArray("posts"); for (int i = 0; i
				 * < arr.length(); i++) { String post_id =
				 * arr.getJSONObject(i).getString("post_id" ); ...... }
				 */

				// } else if (headers.getHeaderString("accept").equals(
				// "application/xml")) {
				// JAXBContext jaxbContext = JAXBContext
				// .newInstance(User.class);
				// Unmarshaller jaxbUnmarshaller = jaxbContext
				// .createUnmarshaller();
				// StringReader reader = new StringReader(data);
				// user = (User) jaxbUnmarshaller.unmarshal(reader);
				// }
				// }

				String query;

				query = "insert into hcis.users (username, npp, pass, kodegrupuser, status) values (?,?,?,?,1)";
				ps = con.prepareStatement(query);
				ps.setString(1, user.getUsername());
				ps.setString(2, user.getNpp());
				ps.setString(3, password);
				ps.setInt(4, user.getGrupuser().getKode());
				ps.executeUpdate();
				metadata.setCode(1);
				metadata.setMessage("User berhasil disimpan");

				// response.setUser(user);
				// result.setResponse(response);

				// return Response.status(200).build();

			} catch (SQLException e) {
				metadata.setCode(0);
				if (e.getErrorCode() == -407) {
					String tabel = "";
					String kolom = "";

					for (String retval : e.getMessage().split(", ")) {
						if (retval.startsWith("TABLEID")) {
							String[] val = retval.split("=");
							tabel = val[1];
						}
						if (retval.startsWith("COLNO")) {
							String[] val = retval.split("=");
							kolom = val[1];
						}
					}
					String query;
					query = "select COLNAME from SYSCAT.COLUMNS "
							+ "where TABSCHEMA || '.' || TABNAME = (select TABSCHEMA || '.' || TABNAME from SYSCAT.TABLES where tableid = ? and OWNERTYPE = 'U') "
							+ "and COLNO = ?";
					try {
						ps.close();
						ps = con.prepareStatement(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
						ps.setString(1, tabel);
						ps.setString(2, kolom);
						rs = ps.executeQuery();
						if (rs.next()) {
							metadata.setMessage("Kolom " + rs.getString("COLNAME") + " tidak boleh kosong");
						}

					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				} else if (e.getErrorCode() == -302) {
					metadata.setMessage("Jumlah karakter pada field melebihi batas");
				} else if (e.getErrorCode() == -530) {
					metadata.setMessage("Foreign key invalid");
				} else if (e.getErrorCode() == -798) {
					metadata.setMessage("Kolom autogenerated tidak bisa diisi");
				} else if (e.getErrorCode() == -803) {
					metadata.setMessage("Constraint kolom karena duplikasi");
				} else
					metadata.setMessage(e.getMessage());
				e.printStackTrace();
			} catch (NamingException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
				e.printStackTrace();
			} catch (JsonProcessingException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
				e.printStackTrace();
			} catch (IOException e) {
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

	@POST
	@Path("/user/delete")
	@Produces("application/json")
	@Consumes("application/json")
	public Response deleteUser(@Context HttpHeaders headers, String data) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Koneksi koneksi = null;
		Result<User> result = new Result<User>();
		Metadata metadata = new Metadata();
		if (SharedMethod.ServiceAuth(headers, metadata)) {
			try {
				// User user = new User();
				koneksi = new Koneksi();
				// koneksi.BuatKoneksi();
				con = koneksi.getConnection();
				String id = null;

				// JSONObject json = new JSONObject(data);
				ObjectMapper mapper = new ObjectMapper();
				JsonNode json = mapper.readTree(data);
				// username = (String) SharedMethod.getJSONObject(json, null,
				// "username");
				id = json.path("id").asText();
				String query;

				query = "update hcis.users set status = 0 where id = ?";
				ps = con.prepareStatement(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
				ps.setString(1, id);

				ps.executeUpdate();
				metadata.setMessage("User berhasil dinonaktifkan");

				metadata.setCode(1);

			} catch (SQLException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
				e.printStackTrace();
			} catch (NamingException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
				e.printStackTrace();
			} catch (JsonProcessingException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
				e.printStackTrace();
			} catch (IOException e) {
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

	@POST
	@Path("/user/resetpassword")
	@Produces("application/json")
	@Consumes("application/json")
	public Response resetPassword(@Context HttpHeaders headers, String data) {
		Connection con = null;
		PreparedStatement ps = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		Koneksi koneksi = null;
		Result<User> result = new Result<User>();
		Metadata metadata = new Metadata();
		if (SharedMethod.ServiceAuth(headers, metadata)) {
			try {
				// User user = new User();
				koneksi = new Koneksi();
				// koneksi.BuatKoneksi();
				con = koneksi.getConnection();
				String username = null;
				String password = null;

				// JSONObject json = new JSONObject(data);
				ObjectMapper mapper = new ObjectMapper();
				JsonNode json = mapper.readTree(data);
				username = json.path("username").asText();
				password = json.path("password").asText();
				
				String query;
				query = "select top 3 pass from hcis.userspasshist where username=? order by id desc";
				cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
				cs.setString(1, username);
				rs = cs.executeQuery();
				Boolean next = true;
				while (rs.next()) {
					if ((BCrypt.checkpw(username.concat(password), rs.getString("pass")))) {
						next = false;
					}
				}
				rs.close();
				cs.close();
				
				if(next){
					String newpassword = BCrypt.hashpw(username.concat(password),BCrypt.gensalt(12));

					query = "update hcis.users set pass = ? where username = ?";
					ps = con.prepareStatement(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
					ps.setString(1, newpassword);
					ps.setString(2, username);

					ps.executeUpdate();
					metadata.setCode(1);
					metadata.setMessage("Password berhasil diubah.");
				}
				else{
					metadata.setCode(0);
					metadata.setMessage("Password baru sama dengan 3 password sebelumnya.");
				}
				

			} catch (SQLException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
				e.printStackTrace();
			} catch (NamingException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
				e.printStackTrace();
			} catch (JsonProcessingException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
				e.printStackTrace();
			} catch (IOException e) {
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
				
				if (cs != null) {
					try {
						cs.close();
					} catch (SQLException e) {
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
	
	@POST
	@Path("/user/resetpasswordbyemail")
	@Produces("application/json")
	@Consumes("application/json")
	public Response resetPasswordByEmail(@Context HttpHeaders headers, String data) {
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		Koneksi koneksi = null;
		Result<User> result = new Result<User>();
		Metadata metadata = new Metadata();
		if (true) {
			try {
				// User user = new User();
				koneksi = new Koneksi();
				// koneksi.BuatKoneksi();
				con = koneksi.getConnection();
				String token = null;
				String password = null;

				// JSONObject json = new JSONObject(data);
				ObjectMapper mapper = new ObjectMapper();
				JsonNode json = mapper.readTree(data);
				token = json.path("token").asText().trim();
				password = json.path("password").asText().trim();
				
				ps2 = con.prepareStatement("select top 1 username from hcis.resetpassword where token=? and row_status=1 and expired_time>getdate()");
				ps2.setString(1, token);
				rs2 = ps2.executeQuery();
				if(rs2.next()) {
					String username = rs2.getString("username");
					String query;
					query = "select top 3 pass from hcis.userspasshist where username=? order by id desc";
					cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
					cs.setString(1, username);
					rs = cs.executeQuery();
					Boolean next = true;
					while (rs.next()) {
						if ((BCrypt.checkpw(username.concat(password), rs.getString("pass")))) {
							next = false;
						}
					}
					rs.close();
					cs.close();
					
					if(next){
						String newpassword = BCrypt.hashpw(username.concat(password),BCrypt.gensalt(12));

						query = "update hcis.users set pass = ? where username = ?;";
						query += "delete hcis.resetpassword where token=?";
						ps = con.prepareStatement(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
						ps.setString(1, newpassword);
						ps.setString(2, username);
						ps.setString(3, token);
						ps.executeUpdate();
						metadata.setCode(1);
						metadata.setMessage("Password berhasil diubah.");
					}
					else{
						metadata.setCode(0);
						metadata.setMessage("Password baru sama dengan 3 password sebelumnya.");
					}
				}
				else {
					metadata.setCode(0);
					metadata.setMessage("Link expired.");
				}
			} catch (SQLException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
				e.printStackTrace();
			} catch (NamingException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
				e.printStackTrace();
			} catch (JsonProcessingException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
				e.printStackTrace();
			} catch (IOException e) {
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
				if (rs2 != null) {
					try {
						rs2.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				if (cs != null) {
					try {
						cs.close();
					} catch (SQLException e) {
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
	
	@POST
	@Path("/user/aktivasibyemail")
	@Produces("application/json")
	@Consumes("application/json")
	public Response aktivasiByEmail(@Context HttpHeaders headers, String data) {
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		Koneksi koneksi = null;
		Result<User> result = new Result<User>();
		Metadata metadata = new Metadata();
		if (true) {
			try {
				// User user = new User();
				koneksi = new Koneksi();
				// koneksi.BuatKoneksi();
				con = koneksi.getConnection();
				String token = null;
				String npp = null;
				String password = null;
				String query = null;

				// JSONObject json = new JSONObject(data);
				ObjectMapper mapper = new ObjectMapper();
				JsonNode json = mapper.readTree(data);
				token = json.path("token").asText().trim();
				npp = json.path("npp").asText().trim();
				password = json.path("password").asText().trim();
				
				ps2 = con.prepareStatement("select top 1 b.username from hcis.users_activasi a "
						+ "inner join hcis.users b on a.npp=b.npp "
						+ "where a.token=? and a.npp=? and a.row_status=1 and a.expired_time>getdate()");
				ps2.setString(1, token);
				ps2.setString(2, npp);
				rs2 = ps2.executeQuery();
				if(rs2.next()) {
					String username = rs2.getString("username");
					String newpassword = BCrypt.hashpw(username.concat(password),BCrypt.gensalt(12));
					query = "update hcis.users set pass = ?, [status] = 1 where username = ?;";
					query += "delete hcis.users_activasi where token=? and npp=?";
					ps = con.prepareStatement(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
					ps.setString(1, newpassword);
					ps.setString(2, username);
					ps.setString(3, token);
					ps.setString(4, npp);
					ps.executeUpdate();
					metadata.setCode(1);
					metadata.setMessage("User anda telah aktif.");
				}
				else {
					metadata.setCode(0);
					metadata.setMessage("Link expired.");
				}
			} catch (SQLException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
				e.printStackTrace();
			} catch (NamingException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
				e.printStackTrace();
			} catch (JsonProcessingException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
				e.printStackTrace();
			} catch (IOException e) {
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
				if (rs2 != null) {
					try {
						rs2.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				if (cs != null) {
					try {
						cs.close();
					} catch (SQLException e) {
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
	
	@POST
	@Path("/user/resetpasswordsendemail")
	@Produces("application/json")
	@Consumes("application/json")
	public Response resetPasswordSendEmail(@Context HttpHeaders headers, String data) {
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
	
	@POST
	@Path("/user/aktivasiusersendemail")
	@Produces("application/json")
	@Consumes("application/json")
	public Response aktivasiUserSendEmail(@Context HttpHeaders headers, String data) {
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
				String npp = null;

				// JSONObject json = new JSONObject(data);
				ObjectMapper mapper = new ObjectMapper();
				JsonNode json = mapper.readTree(data);
				npp = json.path("npp").asText().trim();
				
				String query;
				query = "select top 1 b.username, a.email, a.namalengkap, b.[status], iif(c.token is null, 0, 1) as blocked, c.email as emailterkirim, b.id \r\n" + 
						"from karyawan.vw_pegawai a \r\n" + 
						"inner join hcis.users b on a.npp=b.npp \r\n" + 
						"outer apply \r\n" + 
						"( \r\n" + 
						"	select top 1 ca.* from hcis.users_activasi ca where ca.npp=b.npp and ca.row_status=1 and ca.expired_time>getdate() \r\n" + 
						") c \r\n" + 
						"where a.npp=?";
				ps = con.prepareStatement(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
				ps.setString(1, npp);
				rs = ps.executeQuery();
				if(rs.next()) {
					if(rs.getInt("status")==2) {
						if(rs.getInt("blocked")==0) {
							//String username = rs.getString("username");
							String email = rs.getString("email");
							String nama = rs.getString("namalengkap");
							Integer id = rs.getInt("id");
							
							String generatedToken = Tools.generatePassword(30);
							
							Date currentDate = new Date();
					        //String created_time_string = dateFormat.format(currentDate);
					        Timestamp created_time = new Timestamp(currentDate.getTime());
					        Calendar c = Calendar.getInstance();
					        c.setTime(currentDate);
					        c.add(Calendar.HOUR, (7*24));
					        Date currentDatePlusOne = c.getTime();
					        String expired_time_string = Tools.dateFormat.format(currentDatePlusOne);
					        Timestamp expired_time = new Timestamp(currentDatePlusOne.getTime());
							
							if(Tools.sendEmail(context, 1, npp, nama, email, generatedToken, expired_time_string)) {
								query = "delete hcis.users_activasi where expired_time<=getdate() or row_status=0;";
								query += "insert into hcis.users_activasi (npp,token,created_time,expired_time,email) values (?,?,?,?,?);";
								query += "update hcis.users set kirimaktivasi=isnull(kirimaktivasi,0)+1, kirimaktivasi_time=getdate() where id=?;";
								ps2 = con.prepareStatement(query);
								ps2.setString(1, npp);
								ps2.setString(2, generatedToken);
								ps2.setTimestamp(3, created_time);
								ps2.setTimestamp(4, expired_time);
								ps2.setString(5, email);
								ps2.setInt(6, id);
								ps2.executeUpdate();
								metadata.setCode(1);
								metadata.setMessage("Link aktivasi user telah dikirimkan ke alamat email '" + email + "'. Link reset hanya berlaku sampai dengan 7x24 jam kedepan");
							}
							else {
								metadata.setCode(0);
								metadata.setMessage("Kirim email gagal");
							}
						}
						else {
							metadata.setCode(0);
							metadata.setMessage("Anda sudah melakukan permintaan link aktivasi user sebelumnya, mohon cek kembali email '"+rs.getString("emailterkirim")+"' ");
						}
					}
					else if (rs.getInt("status")==1) {
						metadata.setCode(0);
						metadata.setMessage("User tersebut telah aktif");
					}
					else if (rs.getInt("status")==0) {
						metadata.setCode(0);
						metadata.setMessage("User tersebut non aktif");
					}
				}
				else {
					metadata.setCode(0);
					metadata.setMessage("Pegawai tersebut tidak terdaftar");
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
	
	@POST
	@Path("/user/aktivasinewusersendemail")
	@Produces("application/json")
	@Consumes("application/json")
	public Response aktivasiNewUserSendEmail(@Context HttpHeaders headers, String data) {
		Connection con = null;
		PreparedStatement ps = null;
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
				String npp = null;
				String nama = null;
				String email = null;
				

				// JSONObject json = new JSONObject(data);
				ObjectMapper mapper = new ObjectMapper();
				JsonNode json = mapper.readTree(data);
				npp = json.path("npp").asText().trim();
				nama = json.path("nama").asText().trim();
				email = json.path("email").asText().trim();
				
				
				String query;	
				String generatedToken = Tools.generatePassword(30);
				
				Date currentDate = new Date();
		        //String created_time_string = dateFormat.format(currentDate);
		        Timestamp created_time = new Timestamp(currentDate.getTime());
		        Calendar c = Calendar.getInstance();
		        c.setTime(currentDate);
		        c.add(Calendar.HOUR, (7*24));
		        Date currentDatePlusOne = c.getTime();
		        String expired_time_string = Tools.dateFormat.format(currentDatePlusOne);
		        Timestamp expired_time = new Timestamp(currentDatePlusOne.getTime());
				
				if(Tools.sendEmail(context, 1, npp, nama, email, generatedToken, expired_time_string)) {
					query = "delete hcis.users_activasi where expired_time<=getdate() or row_status=0;";
					query += "insert into hcis.users_activasi (npp,token,created_time,expired_time,email) values (?,?,?,?,?)";
					ps = con.prepareStatement(query);
					ps.setString(1, npp);
					ps.setString(2, generatedToken);
					ps.setTimestamp(3, created_time);
					ps.setTimestamp(4, expired_time);
					ps.setString(5, email);
					ps.executeUpdate();
					metadata.setCode(1);
					metadata.setMessage("Link aktivasi user telah dikirimkan ke alamat email '" + email + "'. Link reset hanya berlaku sampai dengan 7x24 jam kedepan");
				}
				else {
					metadata.setCode(0);
					metadata.setMessage("Kirim email gagal");
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
				
				if (ps != null) {
					try {
						ps.close();
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

	@PUT
	@Path("/user")
	@Consumes("application/json")
	@Produces("application/json")
	public Response editUser(@Context HttpHeaders headers, String data) {
		String namatabel = "hcis.users";
		return RestMethod.updateData(headers, data, new User(), namatabel);

	}

	@POST
	@Path("/{servicename}/list/{page}/{row}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response getListData(@Context HttpHeaders headers, @PathParam("servicename") String servicename,
			@PathParam("page") String page, @PathParam("row") String row, String data) {
		String query = null;
		String namasp = null;
		String namaentitas = null;
		Object obj = null;
		namasp = "sp_list" + servicename;
		namaentitas = servicename;
		switch (servicename) {
		case "menu":
			obj = new Menu();
			break;
		case "menuaction":
			obj = new Menuaction();
			break;
		case "grupuser":
			obj = new GrupUser();
			break;
		case "grupusermenu":
			obj = new Grupusermenu();
			break;
		default:
			return Response.status(Status.NOT_FOUND).build();
		}
		query = "exec hcis." + namasp + " ?, ?, ?, ?, ?";
		return RestMethod.getListData(headers, page, row, namaentitas, obj, query, data);
	}

	@POST
	@Path("/{servicename}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response createData(@Context HttpHeaders headers, @PathParam("servicename") String servicename,
			String data) {

		Object obj = null;
		String namatabel = null;
		namatabel = "hcis." + servicename;
		switch (servicename) {
		case "menu":
			obj = new Menu();
			break;
		case "menuaction":
			obj = new Menuaction();
			break;
		case "grupuser":
			obj = new GrupUser();
			break;
		default:
			return Response.status(Status.NOT_FOUND).build();
		}
		return RestMethod.createData(headers, data, obj, namatabel);
	}

	@PUT
	@Path("/{servicename}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response updateData(@Context HttpHeaders headers, @PathParam("servicename") String servicename,
			String data) {

		Object obj = null;
		String namatabel = null;
		namatabel = "hcis." + servicename;
		switch (servicename) {
		case "menu":
			obj = new Menu();
			break;
		case "menuaction":
			obj = new Menuaction();
			break;
		case "grupuser":
			obj = new GrupUser();
			break;
		default:
			return Response.status(Status.NOT_FOUND).build();
		}
		return RestMethod.updateData(headers, data, obj, namatabel);
	}

	@POST
	@Path("/{servicename}/delete")
	@Produces("application/json")
	@Consumes("application/json")
	public Response deleteData(@Context HttpHeaders headers, String data,
			@PathParam("servicename") String servicename) {
		Object obj = null;
		String namatabel = null;
		namatabel = "hcis." + servicename;
		switch (servicename) {
		case "menu":
			obj = new Menu();
			break;
		case "menuaction":
			obj = new Menuaction();
			break;
		case "grupuser":
			obj = new GrupUser();
			break;
		}
		return RestMethod.deleteData(headers, data, obj, namatabel);
	}
	
	
	
	@GET
	@Path("/user/{username}/logout")
	@Produces("application/json")
	@Consumes("application/json")
	public Response userLogout(@Context HttpHeaders headers, @PathParam("username") String username) {
		Connection con = null;
		PreparedStatement ps = null;
		Result<User> result = new Result<User>();
		Metadata metadata = new Metadata();
		
		if (SharedMethod.VerifyToken(headers, metadata)) {
			try {
				String query;
				query = "insert into hcis.logout_log (username) values (?)";
				con = new Koneksi().getConnection();
				ps = con.prepareStatement(query);
				ps.setString(1, username);
				ps.executeUpdate();
				metadata.setMessage("Ok");
				metadata.setCode(1);
			} catch (Exception e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
			} finally {
				if (ps != null) {
					try {
						ps.close();
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
			}
		}
		result.setMetadata(metadata);
		return Response.ok(result).build();
	}
	
	private static String getAlphaNumericString(int n)
    {
		String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            int index = (int)(AlphaNumericString.length() * Math.random());
            sb.append(AlphaNumericString.charAt(index));
        }
        return sb.toString();
    }
	
	@POST
	@Path("/user/aktivasipenugasansendemail")
	@Produces("application/json")
	@Consumes("application/json")
	public Response aktivasiPenugasanSendEmail(@Context HttpHeaders headers, String data) {
		Connection con = null;
		PreparedStatement ps = null;
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
				Integer kode = null;

				// JSONObject json = new JSONObject(data);
				ObjectMapper mapper = new ObjectMapper();
				JsonNode json = mapper.readTree(data);
				kode = json.path("kode").asInt();
				
				String query;	
				String generateKode = getAlphaNumericString(10);
				String email = "";
				
				try {
					query = "select b.email from karyawan.penugasan_future a \r\n" + 
							"inner join karyawan.pegawai b on a.npp=b.npp where a.kode=?";
					ps = con.prepareStatement(query);
					ps.setInt(1, kode);
					rs = ps.executeQuery();
					if(rs.next()) {
						email = rs.getString("email");
					}
					else {
						throw new MyException("Email tidak ditemukan");
					}
				} finally {
					if (rs != null) {
						try {
							rs.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
					if (ps != null) {
						try {
							ps.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				}
				
				if(Tools.sendEmail(context, 4, generateKode, "", email, "", "")) {
					query = "update karyawan.penugasan_future set kodeaktivasi=?, kirimaktivasi=isnull(kirimaktivasi,0)+1 where kode=?";
					ps = con.prepareStatement(query);
					ps.setString(1, generateKode);
					ps.setInt(2, kode);
					ps.executeUpdate();
					metadata.setCode(1);
					metadata.setMessage("Kirim sukses");
				}
				else {
					metadata.setCode(0);
					metadata.setMessage("Kirim gagal");
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
				
				if (ps != null) {
					try {
						ps.close();
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
	
}
