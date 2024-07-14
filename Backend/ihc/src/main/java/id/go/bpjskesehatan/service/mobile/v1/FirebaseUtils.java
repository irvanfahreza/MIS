package id.go.bpjskesehatan.service.mobile.v1;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import id.go.bpjskesehatan.database.Koneksi;

public class FirebaseUtils {	
	
	public static final String GRUP_SEMUAPEGAWAI = "ihc-grup-semuapegawai";
	
	private static String getFirebaseServerKey(ServletContext context) {
		String serverKey = null;
		try {
			serverKey = context.getInitParameter("firebase.serverKey");
		} catch (Exception e) {
			// TODO: handle exception
		}
		return serverKey;
	}
	
	private static String getFirebaseProjectId(ServletContext context) {
		String projectId = null;
		try {
			projectId = context.getInitParameter("firebase.projectId");
		} catch (Exception e) {
			// TODO: handle exception
		}
		return projectId;
	}
	
	private static String getFirebaseBaseUrl(ServletContext context) {
		String baseUrl = null;
		try {
			baseUrl = context.getInitParameter("firebase.baseUrl");
		} catch (Exception e) {
			// TODO: handle exception
		}
		return baseUrl;
	}
	
	private static List<FirebasePegawai> getListTokenPegawaiByNpp(String npp) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = null;
		List<FirebasePegawai> firebasePegawais = null;
		try {
			query = "select a.token, a.npp, b.namalengkap as nama from hcis.firebase_pegawai a inner join karyawan.vw_pegawai b on a.npp=b.npp where a.npp=?";
			con = new Koneksi().getConnection();
			ps = con.prepareCall(query);
			ps.setString(1, npp);
			rs = ps.executeQuery();
			List<FirebasePegawai> rows = new ArrayList<FirebasePegawai>();
			while(rs.next()) {
				FirebasePegawai row = new FirebasePegawai();
				row.setToken(rs.getString("token"));
				row.setNpp(rs.getString("npp"));
				row.setNama(rs.getString("nama"));
				rows.add(row);
			}
			if(rows.size()>0) {
				firebasePegawais = rows;
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
		return firebasePegawais;
	}
	
	private static String getFirebaseGrupToken(ServletContext context, String namaGrup) {
		String notifKey = null;
		try {
			String url = getFirebaseBaseUrl(context) + "notification?notification_key_name="+namaGrup;
			String responString = FirebaseInvoker.Get(url, FirebaseInvoker.getHeaders(getFirebaseServerKey(context), getFirebaseProjectId(context)));
			ObjectMapper mapper = new ObjectMapper();
	        FirebaseRespon respon = mapper.readValue(responString, FirebaseRespon.class);
	        if(respon.getNotification_key() != null) {
	        	notifKey = respon.getNotification_key();
	        }
		} catch (Exception e) {
			// TODO: handle exception
		}
		return notifKey;
	}
	
	private static String setFirebaseGrupToken(ServletContext context, GroupPost postdata) {
		String notifKey = null;
		try {
			String url = getFirebaseBaseUrl(context) + "notification";
			String responString = FirebaseInvoker.Post(url, FirebaseInvoker.getHeaders(getFirebaseServerKey(context), getFirebaseProjectId(context)), postdata);
			ObjectMapper mapper = new ObjectMapper();
	        FirebaseRespon respon = mapper.readValue(responString, FirebaseRespon.class);
	        if(respon.getNotification_key() != null) {
	        	notifKey = respon.getNotification_key();
	        }
		} catch (Exception e) {
			// TODO: handle exception
		}
		return notifKey;
	}
	
	
	private static void insertFirebasePegawai(String npp, String token) {
		Connection con = null;
		CallableStatement cs = null;
		String query = null;
		
		try {
			query = "exec hcis.sp_Firebase_insertPegawai ?,?";
			con = new Koneksi().getConnection();
			cs = con.prepareCall(query);
			cs.setString(1, npp);
			cs.setString(2, token);
			cs.executeUpdate();
		} catch (Exception e) {
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
	
	private static void deleteFirebasePegawai(String npp, String token) {
		Connection con = null;
		CallableStatement cs = null;
		String query = null;
		
		try {
			query = "exec hcis.sp_Firebase_deletePegawai ?,?";
			con = new Koneksi().getConnection();
			cs = con.prepareCall(query);
			cs.setString(1, npp);
			cs.setString(2, token);
			cs.executeUpdate();
		} catch (Exception e) {
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
	
	public static void register(ServletContext context, String namaGrup, String npp, String token) {
		try {
			String notifKey = getFirebaseGrupToken(context, namaGrup);
			if(notifKey == null) {
				GroupPost postdata = new GroupPost();
				String[] registration_ids = new String[1];
				registration_ids[0] = token;
				postdata.setRegistration_ids(registration_ids);
				postdata.setOperation("create");
				postdata.setNotification_key_name(namaGrup);
				notifKey = setFirebaseGrupToken(context, postdata);
			}
			else {
				GroupPost postdata = new GroupPost();
				String[] registration_ids = new String[1];
				registration_ids[0] = token;
				postdata.setRegistration_ids(registration_ids);
				postdata.setOperation("add");
				postdata.setNotification_key(notifKey);
				postdata.setNotification_key_name(namaGrup);
				notifKey = setFirebaseGrupToken(context, postdata);
			}
			
			insertFirebasePegawai(npp, token);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void unregister(ServletContext context, String namaGrup, String npp, String token) {
		try {
			String notifKey = getFirebaseGrupToken(context, namaGrup);
			if(notifKey != null) {
				GroupPost postdata = new GroupPost();
				String[] registration_ids = new String[1];
				registration_ids[0] = token;
				postdata.setRegistration_ids(registration_ids);
				postdata.setOperation("remove");
				postdata.setNotification_key_name(namaGrup);
				postdata.setNotification_key(notifKey);
				notifKey = setFirebaseGrupToken(context, postdata);
				deleteFirebasePegawai(npp, token);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Boolean sendToGroup(ServletContext context, String namaGrup, String title, String body) throws Exception {
		Boolean result = false;
		try {
			SendPost sendPost = new SendPost();
			String grupToken = getFirebaseGrupToken(context, namaGrup);
			sendPost.setTo(grupToken);
			SendNotification sendNotification = new SendNotification();
			sendNotification.setTitle(title);
			sendNotification.setBody(body);
			sendPost.setNotification(sendNotification);
			
			String url = getFirebaseBaseUrl(context) + "send";
			String responString = FirebaseInvoker.Post(url, FirebaseInvoker.getHeaders(getFirebaseServerKey(context), getFirebaseProjectId(context)), sendPost);
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, true);
	        FirebaseRespon respon = mapper.readValue(responString, FirebaseRespon.class);
	        if(respon.getSuccess() > 0) {
	        	result = true;
	        }
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return result;
	}
	
	public static void sendToPegawai(ServletContext context, String npp, String title, String body) throws Exception {
		try {
			List<FirebasePegawai> firebasePegawais = getListTokenPegawaiByNpp(npp);
			if(firebasePegawais!=null) {
				for (FirebasePegawai rows : firebasePegawais) {
					SendPost sendPost = new SendPost();
					sendPost.setTo(rows.getToken());
					SendNotification sendNotification = new SendNotification();
					if(title.isEmpty() || title.equalsIgnoreCase("")) {
						sendNotification.setTitle(rows.getNama());
					}
					else {
						sendNotification.setTitle(title);
					}
					sendNotification.setBody(body);
					sendPost.setNotification(sendNotification);
					
					String url = getFirebaseBaseUrl(context) + "send";
					String responString = FirebaseInvoker.Post(url, FirebaseInvoker.getHeaders(getFirebaseServerKey(context), getFirebaseProjectId(context)), sendPost);
					ObjectMapper mapper = new ObjectMapper();
					mapper.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, true);
			        FirebaseRespon respon = mapper.readValue(responString, FirebaseRespon.class);
			        if(respon.getSuccess() > 0) {
			        	
			        }
				}
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public static Boolean sendToToken(ServletContext context, String token, String title, String body) throws Exception {
		Boolean sukses = false;
		try {
			SendPost sendPost = new SendPost();
			sendPost.setTo(token);
			SendNotification sendNotification = new SendNotification();
			sendNotification.setTitle(title);
			sendNotification.setBody(body);
			sendPost.setNotification(sendNotification);
			
			String url = getFirebaseBaseUrl(context) + "send";
			String responString = FirebaseInvoker.Post(url, FirebaseInvoker.getHeaders(getFirebaseServerKey(context), getFirebaseProjectId(context)), sendPost);
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, true);
	        FirebaseRespon respon = mapper.readValue(responString, FirebaseRespon.class);
	        if(respon.getSuccess() > 0) {
	        	sukses = true;
	        }
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return sukses;
	}
	
}
