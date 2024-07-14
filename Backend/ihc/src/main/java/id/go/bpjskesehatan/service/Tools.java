package id.go.bpjskesehatan.service;

import java.security.SecureRandom;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.servlet.ServletContext;

import com.sendgrid.Content;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;

import id.go.bpjskesehatan.database.Koneksi;
import id.go.bpjskesehatan.entitas.User;
import id.go.bpjskesehatan.entitas.hcis.GrupUser;
import id.go.bpjskesehatan.entitas.hcis.MenuPegawai;
import id.go.bpjskesehatan.entitas.karyawan.Pegawai;
import id.go.bpjskesehatan.entitas.karyawan.Penugasan;
import id.go.bpjskesehatan.entitas.organisasi.HirarkiUnitKerja;
import id.go.bpjskesehatan.entitas.organisasi.Jabatan;
import id.go.bpjskesehatan.entitas.organisasi.JobTitle;
import id.go.bpjskesehatan.entitas.organisasi.Office;
import id.go.bpjskesehatan.entitas.organisasi.UnitKerja;
import id.go.bpjskesehatan.util.BCrypt;

public class Tools {
	
	public static final Random RANDOM = new SecureRandom();
	public static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	public static final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
	public static Boolean aktivasiUser(String username, String newPass) {
		Connection con = null;
		PreparedStatement ps = null;
		String query = null;
		
		try {
			query = "update hcis.users set pass = ?, [status]=1 where username = ?";
			con = new Koneksi().getConnection();
			ps = con.prepareStatement(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			ps.setString(1, newPass);
			ps.setString(2, username);
			ps.executeUpdate();
			return true;
		} catch (Exception e) {
			
		}
		finally {
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
		return false;
	}
    
	public static List<MenuPegawai> getMenuPegawai(Integer iduser) {
		Connection con = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		String query = null;
		
		List<MenuPegawai> menus = new ArrayList<>();
		try {
			query = "exec hcis.sp_pegawaigrupuserbyid ?";
			con = new Koneksi().getConnection();
			cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			cs.setInt(1, iduser);
			rs =  cs.executeQuery();
			while (rs.next()) {
				MenuPegawai menu = new MenuPegawai();
				menu.setKode(rs.getString("kode"));
				menu.setName(rs.getString("state"));
				menu.setLabel(rs.getString("nama"));
				menu.setLevel(rs.getInt("level"));
				menu.setTipe(rs.getInt("tipe"));
				menu.setKodeparent(rs.getString("kodeparent"));
				menu.setIcon(rs.getString("icon"));
				menu.setCreate(rs.getBoolean("createprivilege"));
				menu.setRead(rs.getBoolean("readprivilege"));
				menu.setUpdate(rs.getBoolean("updateprivilege"));
				menu.setDelete(rs.getBoolean("deleteprivilege"));
				menus.add(menu);
			}
		} catch (Exception e) {
			
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
		
		return menus;
	}
    
	public static void insertLoginTry(String username) {
		Connection con = null;
		PreparedStatement ps = null;
		
		try {
			con = new Koneksi().getConnection();
			ps = con.prepareStatement("insert into hcis.login_try (username) values(?)");
			ps.setString(1, username);
			ps.executeUpdate();
		} catch (Exception e) {
			
		}
		finally {
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
    
	public static void insertLoginLog(String username, String app) {
		Connection con = null;
		PreparedStatement ps = null;
		
		try {
			con = new Koneksi().getConnection();
			ps = con.prepareStatement("insert into hcis.login_log (username, aplikasi) values(?, ?)");
			ps.setString(1, username);
			ps.setString(2, app);
			ps.executeUpdate();
		} catch (Exception e) {
			
		}
		finally {
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
	
	public static void insertLogoutLog(String username) {
		Connection con = null;
		PreparedStatement ps = null;
		
		try {
			con = new Koneksi().getConnection();
			ps = con.prepareStatement("insert into hcis.logout_log (username) values (?)");
			ps.setString(1, username);
			ps.executeUpdate();
		} catch (Exception e) {
			
		}
		finally {
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
    
	public static Penugasan getPenugasan(String npp) {
		Connection con = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		String query = null;
		
		Penugasan penugasan = null;
		try {
			query = "exec hcis.sp_getpenugasanbynpp ?, 1";
			con = new Koneksi().getConnection();
			cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			cs.setString(1, npp);
			rs = cs.executeQuery();
			if (rs.next()) {
				penugasan = new Penugasan();
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
				Office parent = new Office();
				parent.setKode(rs.getString("kodeofficeparent"));
				parent.setNama(rs.getString("namaofficeparent"));
				office.setParent(parent);
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
				Pegawai pegawai = new Pegawai();
				pegawai.setNpp(rs.getString("npp"));
				pegawai.setNama(rs.getString("nama"));
				penugasan.setPegawai(pegawai);
			}
		} catch (Exception e) {
			
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
		
		return penugasan;
	}
	
	public static Penugasan getPenugasanByKode(Integer kode) {
		Connection con = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		String query = null;
		
		Penugasan penugasan = null;
		try {
			query = "exec hcis.sp_getpenugasanbykode ?";
			con = new Koneksi().getConnection();
			cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			cs.setInt(1, kode);
			rs = cs.executeQuery();
			if (rs.next()) {
				penugasan = new Penugasan();
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
				Office parent = new Office();
				parent.setKode(rs.getString("kodeofficeparent"));
				parent.setNama(rs.getString("namaofficeparent"));
				office.setParent(parent);
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
				Pegawai pegawai = new Pegawai();
				pegawai.setNpp(rs.getString("npp"));
				pegawai.setNama(rs.getString("nama"));
				penugasan.setPegawai(pegawai);
			}
		} catch (Exception e) {
			
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
		
		return penugasan;
	}
	
	public static List<Penugasan> getListPenugasan(String npp) {
		Connection con = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		String query = null;
		
		List<Penugasan> penugasans = null;
		try {
			query = "exec hcis.sp_getpenugasanbynpp ?, 10";
			con = new Koneksi().getConnection();
			cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			cs.setString(1, npp);
			rs = cs.executeQuery();
			List<Penugasan> rows = new ArrayList<>();
			while (rs.next()) {
				Penugasan penugasan = new Penugasan();
				penugasan.setKode(rs.getInt("kode"));
				Jabatan jabatan = new Jabatan();
				jabatan.setKode(rs.getString("kodehirarkijabatan"));
				jabatan.setNama(rs.getString("namajabatan"));
				UnitKerja unitkerja = new UnitKerja();
				unitkerja.setKode(rs.getString("kodeunitkerja"));
				unitkerja.setNama(rs.getString("namaunitkerja"));
				HirarkiUnitKerja hirarkiUnitKerja = new HirarkiUnitKerja();
				hirarkiUnitKerja.setKode(rs.getString("kodehirarkiunitkerja"));
				hirarkiUnitKerja.setNama(rs.getString("namahirarkiunitkerja"));
				hirarkiUnitKerja.setLevel(rs.getShort("levelhirarkiunitkerja"));
				unitkerja.setHirarkiunitkerja(hirarkiUnitKerja);
				Office office = new Office();
				office.setKode(rs.getString("kodeoffice"));
				office.setNama(rs.getString("namaoffice"));
				Office parent = new Office();
				parent.setKode(rs.getString("kodeofficeparent"));
				parent.setNama(rs.getString("namaofficeparent"));
				office.setParent(parent);
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
				rows.add(penugasan);
			}
			
			if(rows.size()>0) {
				penugasans = new ArrayList<>();
				penugasans = rows;
			}
		} catch (Exception e) {
			
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
		
		return penugasans;
	}
    
	public static ArrayList<GrupUser> getGrupUser(Integer userid) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = null;
		
		ArrayList<GrupUser> grupUsers = new ArrayList<>();
		try {
			query = "select a.kodegrupuser, b.nama as namagrupuser \r\n" + 
					"from hcis.pegawai_grupuser a \r\n" + 
					"inner join hcis.grupuser b on a.kodegrupuser=b.kode \r\n" + 
					"where a.iduser = ? order by a.kodegrupuser";
			con = new Koneksi().getConnection();
			ps = con.prepareStatement(query);
			ps.setInt(1, userid);
			rs = ps.executeQuery();
			
			while(rs.next()) {
				GrupUser grupUser = new GrupUser();
				grupUser.setKode(rs.getInt("kodegrupuser"));
				grupUser.setNama(rs.getString("namagrupuser"));
				grupUsers.add(grupUser);
			}
		} catch (Exception e) {
			
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
		
		return grupUsers;
	}
    
	public static Pegawai validateUser(User user) {
		Connection con = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		String query = null;
		
		Pegawai pegawai = null;
		try {
			con = new Koneksi().getConnection();
			query = "exec hcis.sp_login ?";
			cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			cs.setString(1, user.getUsername());
			rs = cs.executeQuery();
			if(rs.next()) {
				pegawai = new Pegawai();
				pegawai.setNama(rs.getString("nama"));
				pegawai.setInstall(rs.getInt("install"));
				pegawai.setEmail(rs.getString("email"));
				
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
				user.setBlocked(rs.getInt("blocked"));
				user.setPass(rs.getString("pass"));
				user.setDefaultpass(rs.getString("defaultpass"));
				user.setGeneratepass(rs.getInt("generatepass"));
				user.setStatus(rs.getInt("status"));
				user.setEnablemfa(rs.getInt("enablemfa"));
			}
		} catch (Exception e) {
			
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
		
		return pegawai;
	}
	
	public static String generatePassword(int length) {
		StringBuilder returnValue = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            returnValue.append(Tools.ALPHABET.charAt(Tools.RANDOM.nextInt(Tools.ALPHABET.length())));
        }
        return new String(returnValue);
	}
	
	public static boolean sendEmail(ServletContext context, Integer tipe, String username, String nama, String email, String token, String expiredtime) throws Exception {
		/*
		 * tipe 1 = aktivasi user
		 * tipe 2 = reset password
		 * tipe 3 = verifikasi
		 * tipe 4 = aktivasi penugasan
		 * 
		 */
		
		try {
			String apiKey = context.getInitParameter("sendgrid.apikey");
			String template1 = context.getInitParameter("sendgrid.templateid.aktivasiuser");
			String template2 = context.getInitParameter("sendgrid.templateid.resetpassword");
			String template3 = context.getInitParameter("sendgrid.templateid.verification");
			String template4 = context.getInitParameter("sendgrid.templateid.aktivasipenugasan");
			String baseUrl = context.getInitParameter("ihc.baseurl");
			
			String linkreset = null;
			String subject = null;
			String templateId = null;
			
			if(tipe==1) {
				linkreset = baseUrl + "/#/access/activation/"+token+"/"+username;
				subject = "IHC - Link Aktivasi User";
				templateId = template1;
			}
			else if(tipe==2) {
				linkreset = baseUrl + "/#/access/resetpassword/"+token;
				subject = "IHC - Link Reset Password";
				templateId = template2;
			}
			else if(tipe==3) {
				linkreset = "";
				subject = "IHC - Kode Verifikasi";
				templateId = template3;
			}
			else if(tipe==4) {
				linkreset = "";
				subject = "IHC - Kode Aktivasi Penugasan Baru";
				templateId = template4;
			}
			
			com.sendgrid.Email from = new com.sendgrid.Email("noreply@bpjs-kesehatan.go.id","KEDEPUTIAN BIDANG MSDMRM");
		    
		    com.sendgrid.Email to = new com.sendgrid.Email(email.trim());
		    Content content = new Content("text/html", "1");
		    com.sendgrid.Mail mail = new com.sendgrid.Mail(from, subject, to, content);
		    mail.addCategory(subject);
		    mail.personalization.get(0).addSubstitution("#nama#", nama);
		    mail.personalization.get(0).addSubstitution("#username#", username);
		    mail.personalization.get(0).addSubstitution("#linkreset#", linkreset);
		    mail.personalization.get(0).addSubstitution("#expiredtime#", expiredtime);
		    mail.setTemplateId(templateId);
		    
		    SendGrid sg = new SendGrid(apiKey);
		    Request request = new Request();
		    request.setMethod(Method.POST);
		    request.setEndpoint("mail/send");
		    request.setBody(mail.build());
		    com.sendgrid.Response response = sg.api(request);
		    
		    if(response.getStatusCode()==202) {
		    	return true;
		    }
		    else {
		    	return false;
		    }
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}
	
	public static Boolean check3PasswordTerakhir(String username, String password) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Boolean ok = false;
		try {
			con = new Koneksi().getConnection();
			String query = "select top 3 pass from hcis.userspasshist where username=? order by id desc";
			ps = con.prepareStatement(query);
			ps.setString(1, username);
			rs = ps.executeQuery();
			ok = true;
			while (rs.next()) {
				if ((BCrypt.checkpw(username.concat(password), rs.getString("pass")))) {
					ok = false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
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
		return ok;
	}
	
	public static Boolean updatePassword(String username, String password) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		Boolean ok = false;
		try {
			con = new Koneksi().getConnection();
			String query = "update hcis.users set pass = ? where username = ?";
			ps = con.prepareStatement(query);
			ps.setString(1, password);
			ps.setString(2, username);
			ps.executeUpdate();
			ok = true;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
		finally {
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
		return ok;
	}
	
	public static String insertVerificationCode(ServletContext context, String requestId, String npp, String email) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		String query = null;
		
		String uniqueID = null;
		try {
			if(requestId==null) {
				uniqueID = UUID.randomUUID().toString();
			}
			else {
				uniqueID = requestId;
			}
			
			Random random = new Random();
			String verifCode = String.format("%04d", random.nextInt(10000));
			
			if(Tools.sendEmail(context, 3, verifCode, npp, email, "", "")) {
				query = "insert into hcis.verification (npp, email, request_id, verification_code, row_status) values (?, ?, ?, ?, ?)";
				con = new Koneksi().getConnection();
				ps = con.prepareStatement(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
				ps.setString(1, npp);
				ps.setString(2, email);
				ps.setString(3, uniqueID);
				ps.setString(4, verifCode);
				ps.setInt(5, 1);
				ps.executeUpdate();
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		finally {
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
		return uniqueID;
	}
	
	private static Boolean clearVerification(String requestId, String email, String verifCode) {
		Connection con = null;
		PreparedStatement ps = null;
		String query = null;
		
		Boolean sukses = false;
		try {
			query = "update hcis.verification set row_status=0, verification_time=getdate() where request_id=? and email=? and verification_code=?";
			con = new Koneksi().getConnection();
			ps = con.prepareStatement(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			ps.setString(1, requestId);
			ps.setString(2, email);
			ps.setString(3, verifCode);
			ps.executeUpdate();
			clearAllVerification(requestId, email);
		} catch (Exception e) {
			sukses = false;
			e.printStackTrace();
		}
		finally {
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
		return sukses;
	}
	
	private static Boolean clearAllVerification(String requestId, String email) {
		Connection con = null;
		PreparedStatement ps = null;
		String query = null;
		
		Boolean sukses = false;
		try {
			query = "update hcis.verification set row_status=0 where request_id=? and email=? and row_status=1";
			con = new Koneksi().getConnection();
			ps = con.prepareStatement(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			ps.setString(1, requestId);
			ps.setString(2, email);
			ps.executeUpdate();
			return true;
		} catch (Exception e) {
			sukses = false;
			e.printStackTrace();
		}
		finally {
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
		return sukses;
	}
	
	public static Boolean verifyCode(String requestId, String email, String verifCode) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = null;
		
		Boolean verified = false;
		try {
			query = "select top 1 1 from hcis.verification where request_id=? and email=? and verification_code=? and row_status=1";
			con = new Koneksi().getConnection();
			ps = con.prepareStatement(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			ps.setString(1, requestId);
			ps.setString(2, email);
			ps.setString(3, verifCode);
			rs = ps.executeQuery();
			if(rs.next()) {
				clearVerification(requestId, email, verifCode);
				verified = true;
			}
		} catch (Exception e) {
			verified = false;
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
		return verified;
	}
	
}
