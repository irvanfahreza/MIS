package id.go.bpjskesehatan.util;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Map;

import id.go.bpjskesehatan.database.Koneksi;
import id.go.bpjskesehatan.entitas.Metadata;
import id.go.bpjskesehatan.service.mobile.v1.AuthUser;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.impl.TextCodec;

import javax.naming.NamingException;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SharedMethod {

	public static boolean ServiceAuth(HttpHeaders headers, Metadata metadata) {
		/*if (HeaderInvalid(headers)) {
			metadata.setCode(Response.Status.UNAUTHORIZED.getStatusCode());
			metadata.setMessage("Unauthorized, invalid Headers");
			return false;
		} else if (SignatureInvalid(headers)) {
			metadata.setCode(Response.Status.UNAUTHORIZED.getStatusCode());
			metadata.setMessage("Unauthorized, invalid token");
			return false;
		} else if (ServiceExpired(headers)) {
			metadata.setCode(Response.Status.UNAUTHORIZED.getStatusCode());
			metadata.setMessage("Service Expired");
			return false;
		} else {
			return true;
		}*/
		if(VerifyToken(headers, metadata)) {
			return true;
		}
		else {
			return false;
		}
	}

	public static boolean ServiceAuth(HttpHeaders headers, Map<String, Object> metadata) {
		/*if (HeaderInvalid(headers)) {
			metadata.put("code", Response.Status.UNAUTHORIZED.getStatusCode());
			metadata.put("message", "Unauthorized, invalid Headers");
			return false;
		} else if (SignatureInvalid(headers)) {
			metadata.put("code", Response.Status.UNAUTHORIZED.getStatusCode());
			metadata.put("message", "Unauthorized, invalid token");
			return false;
		} else if (ServiceExpired(headers)) {
			metadata.put("code", Response.Status.UNAUTHORIZED.getStatusCode());
			metadata.put("message", "Service Expired");
			return false;
		} else {
			return true;
		}*/
		if(VerifyToken(headers, metadata)) {
			return true;
		}
		else {
			return false;
		}
	}

	public static boolean HeaderInvalid(HttpHeaders headers) {
		if ((!headers.getRequestHeaders().containsKey("x-timestamp"))
				|| (!headers.getRequestHeaders().containsKey("x-signature"))
				|| (!headers.getRequestHeaders().containsKey("x-cons-id")) || (headers == null)) {

			return true;
		} else {
			return false;
		}
	}

	public static boolean SignatureInvalid(HttpHeaders headers) {
		if ((Utils
				.generateHmacSHA256Signature(
						headers.getHeaderString("x-cons-id") + "&" + headers.getHeaderString("x-timestamp"), "hc1$18")
				.equals(headers.getHeaderString("x-signature")))) {
			return false;
		} else {
			return true;
		}
	}

	public static boolean ServiceExpired(HttpHeaders headers) {
		long timeStamp = System.currentTimeMillis() / 1000;
		long timeout = 0;
		try {
			timeout = timeStamp - Long.parseLong(headers.getHeaderString("x-timestamp"));
			if (timeout <= -300 || timeout >= 300) {
				return true;
			} else {
				return false;
			}
		} catch (NumberFormatException e) {
			return false;
		}

		/*
		 * long timeStamp = System.currentTimeMillis() / 1000; long timeout = 0;
		 * try { timeout = timeStamp -
		 * Long.parseLong(headers.getHeaderString("X-timestamp")); if (timeout
		 * <= -6000 || timeout >= 6000) { return true; } else { return false; }
		 * } catch (NumberFormatException e) { return false; }
		 */

		// return false;
	}

	public static boolean ServiceToken(HttpHeaders headers, Map<String, Object> metadata) {
		String token = null;
		Algorithm algorithm;
		try {
			if (headers.getRequestHeaders().containsKey("authorization")) {
				token = headers.getHeaderString("authorization");
				if (token.toLowerCase().startsWith("bearer") && token.length() > 7) {
					token = token.substring("bearer ".length());

					algorithm = Algorithm.HMAC256("forever and one");
					JWTVerifier verifier = JWT.require(algorithm).build();
					verifier.verify(token);
					return true;
				}
			} else {
				metadata.put("code", Response.Status.UNAUTHORIZED.getStatusCode());
				metadata.put("message", "Unauthorized");
			}
		} catch (IllegalArgumentException e) {
			metadata.put("code", Response.Status.UNAUTHORIZED.getStatusCode());
			metadata.put("message", "Unauthorized");
		} catch (JWTVerificationException e) {
			metadata.put("code", Response.Status.UNAUTHORIZED.getStatusCode());
			metadata.put("message", "Unauthorized, Invalid Token");
		}
		return false;
	}
	
	public static boolean VerifyToken(HttpHeaders headers, Map<String, Object> metadata) {
		String token = null;
		Algorithm algorithm;
		try {
			if (headers.getRequestHeaders().containsKey("Authorization")) {
				token = headers.getHeaderString("Authorization");
				if (token.toLowerCase().startsWith("rmazxc") && token.length() > 7) {
					token = token.substring("RmazXc ".length());
					algorithm = Algorithm.HMAC256(TextCodec.BASE64.decode("IloveyoU4ever"));
					JWTVerifier verifier = JWT.require(algorithm).build();
					verifier.verify(token);
					return true;
				}
			} else {
				metadata.put("code", Response.Status.UNAUTHORIZED.getStatusCode());
				metadata.put("message", "Unauthorized");
			}
		} catch (IllegalArgumentException e) {
			metadata.put("code", Response.Status.UNAUTHORIZED.getStatusCode());
			metadata.put("message", "Unauthorized");
		} catch (JWTVerificationException e) {
			metadata.put("code", Response.Status.UNAUTHORIZED.getStatusCode());
			metadata.put("message", "Unauthorized, Invalid Token");
		} catch (ExpiredJwtException e) {
			metadata.put("code", Response.Status.UNAUTHORIZED.getStatusCode());
			metadata.put("message", "Unauthorized, Expired Token");
		} catch (Exception e) {
			metadata.put("code", Response.Status.UNAUTHORIZED.getStatusCode());
			metadata.put("message", "Unauthorized, Expired Token");
		}
		return false;
	}
	
	public static boolean VerifyToken(HttpHeaders headers, Metadata metadata) {
		String token = null;
		Algorithm algorithm;
		try {
			if (headers.getRequestHeaders().containsKey("Authorization")) {
				token = headers.getHeaderString("Authorization");
				if (token.toLowerCase().startsWith("rmazxc") && token.length() > 7) {
					token = token.substring("RmazXc ".length());
					algorithm = Algorithm.HMAC256(TextCodec.BASE64.decode("IloveyoU4ever"));
					JWTVerifier verifier = JWT.require(algorithm).build();
					verifier.verify(token);
					return true;
				}
				else {
					metadata.setCode(Response.Status.UNAUTHORIZED.getStatusCode());
					metadata.setMessage("Unauthorized, Invalid Token");
				}
			} else {
				metadata.setCode(Response.Status.UNAUTHORIZED.getStatusCode());
				metadata.setMessage("Unauthorized");
			}
		} catch (IllegalArgumentException e) {
			metadata.setCode(Response.Status.UNAUTHORIZED.getStatusCode());
			metadata.setMessage("Unauthorized");
		} catch (JWTVerificationException e) {
			metadata.setCode(Response.Status.UNAUTHORIZED.getStatusCode());
			metadata.setMessage("Unauthorized, Invalid Token");
		} catch (ExpiredJwtException e) {
			metadata.setCode(Response.Status.UNAUTHORIZED.getStatusCode());
			metadata.setMessage("Unauthorized, Expired Token");
		} catch (Exception e) {
			metadata.setCode(Response.Status.UNAUTHORIZED.getStatusCode());
			metadata.setMessage("Unauthorized, Invalid Token");
		}
		return false;
	}
	
	public static boolean VerifyToken(AuthUser authUser, HttpHeaders headers, Map<String, Object> metadata) {
		String token = null;
		Algorithm algorithm;
		try {
			if (headers.getRequestHeaders().containsKey("Authorization")) {
				token = headers.getHeaderString("Authorization");
				if (token.toLowerCase().startsWith("rmazxc") && token.length() > 7) {
					token = token.substring("RmazXc ".length());
					algorithm = Algorithm.HMAC256(TextCodec.BASE64.decode("IloveyoU4ever"));
					JWTVerifier verifier = JWT.require(algorithm).build();
					verifier.verify(token);
					authUser.setToken(token);
					try {
						DecodedJWT jwt = JWT.decode(authUser.getToken());
					    Claim claimNpp = jwt.getClaim("npp");
					    if(!claimNpp.isNull()) {
					    	authUser.setNpp(claimNpp.asString());
					    }
					    Claim claimUserId = jwt.getClaim("userid");
					    if(!claimUserId.isNull()) {
					    	authUser.setUserid(claimUserId.asInt());
					    }
					} catch (Exception e) {
						// TODO: handle exception
					}
					return true;
				}
			} else {
				metadata.put("code", Response.Status.UNAUTHORIZED.getStatusCode());
				metadata.put("message", "Unauthorized");
			}
		} catch (IllegalArgumentException e) {
			metadata.put("code", Response.Status.UNAUTHORIZED.getStatusCode());
			metadata.put("message", "Unauthorized");
		} catch (JWTVerificationException e) {
			metadata.put("code", Response.Status.UNAUTHORIZED.getStatusCode());
			metadata.put("message", "Unauthorized, Invalid Token");
		} catch (ExpiredJwtException e) {
			metadata.put("code", Response.Status.UNAUTHORIZED.getStatusCode());
			metadata.put("message", "Unauthorized, Expired Token");
		} catch (Exception e) {
			metadata.put("code", Response.Status.UNAUTHORIZED.getStatusCode());
			metadata.put("message", "Unauthorized, Expired Token");
		}
		return false;
	}
	
	public static boolean VerifyToken(AuthUser authUser, HttpHeaders headers, Metadata metadata) {
		String token = null;
		Algorithm algorithm;
		try {
			if (headers.getRequestHeaders().containsKey("Authorization")) {
				token = headers.getHeaderString("Authorization");
				if (token.toLowerCase().startsWith("rmazxc") && token.length() > 7) {
					token = token.substring("RmazXc ".length());
					algorithm = Algorithm.HMAC256(TextCodec.BASE64.decode("IloveyoU4ever"));
					JWTVerifier verifier = JWT.require(algorithm).build();
					verifier.verify(token);
					authUser.setToken(token);
					try {
						DecodedJWT jwt = JWT.decode(authUser.getToken());
					    Claim claimNpp = jwt.getClaim("npp");
					    if(!claimNpp.isNull()) {
					    	authUser.setNpp(claimNpp.asString());
					    }
					    Claim claimUserId = jwt.getClaim("userid");
					    if(!claimUserId.isNull()) {
					    	authUser.setUserid(claimUserId.asInt());
					    }
					} catch (Exception e) {
						// TODO: handle exception
					}
					return true;
				}
				else {
					metadata.setCode(Response.Status.UNAUTHORIZED.getStatusCode());
					metadata.setMessage("Unauthorized, Invalid Token");
				}
			} else {
				metadata.setCode(Response.Status.UNAUTHORIZED.getStatusCode());
				metadata.setMessage("Unauthorized");
			}
		} catch (IllegalArgumentException e) {
			metadata.setCode(Response.Status.UNAUTHORIZED.getStatusCode());
			metadata.setMessage("Unauthorized");
		} catch (JWTVerificationException e) {
			metadata.setCode(Response.Status.UNAUTHORIZED.getStatusCode());
			metadata.setMessage("Unauthorized, Invalid Token");
		} catch (ExpiredJwtException e) {
			metadata.setCode(Response.Status.UNAUTHORIZED.getStatusCode());
			metadata.setMessage("Unauthorized, Expired Token");
		} catch (Exception e) {
			metadata.setCode(Response.Status.UNAUTHORIZED.getStatusCode());
			metadata.setMessage("Unauthorized, Invalid Token");
		}
		return false;
	}
	
	public static boolean VerifyToken2(String token, Metadata metadata) {
		Algorithm algorithm;
		try {
			if (!token.isEmpty()) {
				if (token.length() > 7) {
					algorithm = Algorithm.HMAC256(TextCodec.BASE64.decode("IloveyoU4ever"));
					JWTVerifier verifier = JWT.require(algorithm).build();
					verifier.verify(token);
					return true;
				}
				else {
					metadata.setCode(Response.Status.UNAUTHORIZED.getStatusCode());
					metadata.setMessage("Unauthorized, Invalid Token");
				}
			} else {
				metadata.setCode(Response.Status.UNAUTHORIZED.getStatusCode());
				metadata.setMessage("Unauthorized");
			}
		} catch (IllegalArgumentException e) {
			metadata.setCode(Response.Status.UNAUTHORIZED.getStatusCode());
			metadata.setMessage("Unauthorized");
		} catch (JWTVerificationException e) {
			metadata.setCode(Response.Status.UNAUTHORIZED.getStatusCode());
			metadata.setMessage("Unauthorized, Invalid Token");
		} catch (ExpiredJwtException e) {
			metadata.setCode(Response.Status.UNAUTHORIZED.getStatusCode());
			metadata.setMessage("Unauthorized, Expired Token");
		} catch (Exception e) {
			metadata.setCode(Response.Status.UNAUTHORIZED.getStatusCode());
			metadata.setMessage("Unauthorized, Invalid Token");
		}
		return false;
	}
	
	public static boolean VerifyToken2(String token, Map<String, Object> metadata) {
		Algorithm algorithm;
		try {
			if (!token.isEmpty()) {
				if (token.length() > 7) {
					algorithm = Algorithm.HMAC256(TextCodec.BASE64.decode("IloveyoU4ever"));
					JWTVerifier verifier = JWT.require(algorithm).build();
					verifier.verify(token);
					return true;
				}
				else {
					metadata.put("code", Response.Status.UNAUTHORIZED.getStatusCode());
					metadata.put("message", "Unauthorized, Invalid Token");
				}
			} else {
				metadata.put("code", Response.Status.UNAUTHORIZED.getStatusCode());
				metadata.put("message", "Unauthorized");
			}
		} catch (IllegalArgumentException e) {
			metadata.put("code", Response.Status.UNAUTHORIZED.getStatusCode());
			metadata.put("message", "Unauthorized");
		} catch (JWTVerificationException e) {
			metadata.put("code", Response.Status.UNAUTHORIZED.getStatusCode());
			metadata.put("message", "Unauthorized, Invalid Token");
		} catch (ExpiredJwtException e) {
			metadata.put("code", Response.Status.UNAUTHORIZED.getStatusCode());
			metadata.put("message", "Unauthorized, Expired Token");
		} catch (Exception e) {
			metadata.put("code", Response.Status.UNAUTHORIZED.getStatusCode());
			metadata.put("message", "Unauthorized, Invalid Token");
		}
		return false;
	}

	public static Object setAllSetters(ResultSet from, Object to)
			throws IllegalAccessException, InvocationTargetException, SQLException, NumberFormatException {
		Class<? extends Object> cls = to.getClass();
		for (Field field : cls.getDeclaredFields()) {
			for (Method method : cls.getMethods()) {
				if ((method.getName().startsWith("set"))
						&& (method.getName().length() == (field.getName().length() + 3))) {
					if (method.getName().toLowerCase().endsWith(field.getName().toLowerCase())) {
						// MZ: Method found, run it
						try {
							Column column = field.getAnnotation(Column.class);
							String columnName;

							if (column != null) {
								columnName = column.name().toLowerCase();
							} else {
								columnName = field.getName().toLowerCase();
							}

							// System.out.println(columnName);
							// System.out.println(field.getType().getSimpleName());

							Object value = null;
							method.setAccessible(true);
							if (field.getType().getSimpleName().toLowerCase().endsWith("int") || field.getType().getSimpleName().toLowerCase().endsWith("integer")) {
								try {
									value = (int) from.getInt(columnName);
								} catch (Exception e) {
									// TODO: handle exception
								}
							}
							else if (field.getType().getSimpleName().toLowerCase().endsWith("long")) {
								try {
									value = (int) from.getLong(columnName);
								} catch (Exception e) {
									// TODO: handle exception
								}
							}
							else if (field.getType().getSimpleName().toLowerCase().endsWith("string")) {
								try {
									value = (String) from.getString(columnName);
								} catch (Exception e) {
									// TODO: handle exception
								}
							}	
							else if (field.getType().getSimpleName().toLowerCase().endsWith("boolean")) {
								try {
									value = (boolean) from.getBoolean(columnName);
								} catch (Exception e) {
									// TODO: handle exception
								}
							}	
							else if (field.getType().getSimpleName().toLowerCase().endsWith("timestamp")) {
								try {
									value = (Timestamp) from.getTimestamp(columnName);
								} catch (Exception e) {
									// TODO: handle exception
								}
							}	
							else if (field.getType().getSimpleName().toLowerCase().endsWith("date")) {
								try {
									value = (Date) from.getDate(columnName);
								} catch (Exception e) {
									// TODO: handle exception
								}
							}	
							else if (field.getType().getSimpleName().toLowerCase().endsWith("double")) {
								try {
									value = (double) from.getDouble(columnName);
								} catch (Exception e) {
									// TODO: handle exception
								}
							}	
							else if (field.getType().getSimpleName().toLowerCase().endsWith("float")) {
								try {
									value = (float) from.getFloat(columnName);
								} catch (Exception e) {
									// TODO: handle exception
								}
							}	
							else if (field.getType().getSimpleName().toLowerCase().endsWith("time")) {
								try {
									value = (Time) from.getTime(columnName);
								} catch (Exception e) {
									// TODO: handle exception
								}
							}	
							else if (field.getType().getSimpleName().toLowerCase().endsWith("short")) {
								try {
									value = (short) from.getShort(columnName);
								} catch (Exception e) {
									// TODO: handle exception
								}
							}	
							else if (field.getType().getSimpleName().toLowerCase().endsWith("bigdecimal")) {
								try {
									value = (BigDecimal) from.getBigDecimal(columnName);
								} catch (Exception e) {
									// TODO: handle exception
								}
							}	

							// System.out.println(value);

							// if (!from.wasNull()) {
							if (value != null) {
								method.invoke(to, value);
							}

						} catch (IllegalAccessException | InvocationTargetException e) {
							throw e;
						}
					}
				}
			}
		}

		return to;

	}

	// public static Object setAllSetters(JSONObject from, Object to)
	// throws IllegalAccessException, InvocationTargetException,
	// NumberFormatException {
	// Class<? extends Object> cls = to.getClass();
	// String key = to.getClass().getAnnotation(JsonObject.class).name();
	// Iterator<?> keys = from.getJSONObject(key).keys();
	// while (keys.hasNext()) {
	// String kunci = (String) keys.next();
	// for (Field field : cls.getDeclaredFields()) {
	// for (Method method : cls.getDeclaredMethods()) {
	// if ((method.getName().startsWith("set"))
	// && (method.getName().length() == (field.getName().length() + 3))) {
	// if
	// (method.getName().toLowerCase().endsWith(field.getName().toLowerCase()))
	// {
	// JsonKey jsonkey = field.getAnnotation(JsonKey.class);
	// Column column = field.getAnnotation(Column.class);
	// String kolom = "";
	// if (jsonkey != null) {
	// kolom = jsonkey.name().toLowerCase();
	// } else if (column != null) {
	// kolom = column.name().toLowerCase();
	// } else {
	// kolom = field.getName().toLowerCase();
	// }
	// if (kolom.equals(kunci.toLowerCase())) {
	// try {
	//
	// String value = (String) getJSONObject(from, key, kunci);
	//
	// method.setAccessible(true);
	//
	// if (field.getType().getSimpleName().toLowerCase().endsWith("int"))
	// method.invoke(to, Integer.parseInt(value));
	// else if
	// (field.getType().getSimpleName().toLowerCase().endsWith("integer"))
	// method.invoke(to, Integer.parseInt(value));
	// else if (field.getType().getSimpleName().toLowerCase().endsWith("long"))
	// method.invoke(to, Long.parseLong(value));
	// else if
	// (field.getType().getSimpleName().toLowerCase().endsWith("string"))
	// method.invoke(to, value);
	// else if
	// (field.getType().getSimpleName().toLowerCase().endsWith("boolean")) {
	// boolean hasil = ("1".equals(value) ? true : false);
	// hasil = ("true".equals(value) ? true : false);
	// method.invoke(to, hasil);
	// } else if
	// (field.getType().getSimpleName().toLowerCase().endsWith("timestamp")) {
	// method.invoke(to, Timestamp.valueOf(value));
	// } else if
	// (field.getType().getSimpleName().toLowerCase().endsWith("date"))
	// method.invoke(to, Date.valueOf(value));
	// else if
	// (field.getType().getSimpleName().toLowerCase().endsWith("double"))
	// method.invoke(to, Double.parseDouble(value));
	// else if (field.getType().getSimpleName().toLowerCase().endsWith("float"))
	// method.invoke(to, Float.parseFloat(value));
	// else if (field.getType().getSimpleName().toLowerCase().endsWith("time"))
	// method.invoke(to, Time.valueOf(value));
	// else if (field.getType().getSimpleName().toLowerCase().endsWith("short"))
	// method.invoke(to, Short.parseShort(value));
	// // else
	// // method.invoke(ob, value.toString());
	//
	// } catch (IllegalAccessException | InvocationTargetException |
	// NumberFormatException e) {
	// throw e;
	// }
	// }
	// }
	// }
	// }
	// }
	// }
	//
	// return to;
	// }
	//
	// public static Object getJSONObject(JSONObject json, String object, String
	// key) {
	// if (object != null) {
	// if (json.getJSONObject(object).isNull(key)) {
	// return null;
	// } else {
	// return "".equals(json.getJSONObject(object).get(key)) ? null :
	// json.getJSONObject(object).get(key);
	// }
	// } else {
	// if (json.isNull(key)) {
	// return null;
	// } else {
	// return "".equals(json.get(key)) ? null : json.get(key);
	// }
	// }
	// }

	public static Object fromRStoBean(Object data, String query, String key) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		// Koneksi koneksi = null;
		try {
			// koneksi = new Koneksi();
			// koneksi.BuatKoneksi();
			con = new Koneksi().getConnection();
			ps = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			if (key != null) {
				ps.setString(1, key);
			}
			rs = ps.executeQuery();
			if (rs.next()) {
				return SharedMethod.setAllSetters(rs, data);
			}
		} catch (SQLException | NamingException | IllegalAccessException | InvocationTargetException e) {
			throw e;
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
				}
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException e) {
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
				}
		}

		return key;
	}

	// public static String createQuery(Object entity, String data) throws
	// JsonProcessingException, IOException {
	// return createQuery(entity, data, null);
	// }

	// public static String createQuery(Object entity, String data, String
	// validasi)
	// throws JsonProcessingException, IOException {
	// return createQuery(entity, data, null);
	// }

	public static String createQuery(Object entity, String data, String namatabel)
			throws JsonProcessingException, IOException {
		String table;
		if (namatabel == null)
			table = entity.getClass().getAnnotation(Table.class).name();
		else
			table = namatabel;
		// String query = "select kode as id from NEW table (insert into " +
		// table + " (";
		String query = "insert into " + table + " (";
		String value = "values (";
		// String jsonobjkey = null;
		// if (namajsonObject == null) {
		// jsonobjkey = table;
		// } else {
		// jsonobjkey = namajsonObject;
		// }
		// Iterator<?> keys = json.getJSONObject(jsonobjkey).keys();
		ObjectMapper mapper = new ObjectMapper();
		JsonNode json = mapper.readTree(data);

		Iterator<?> keys = json.fieldNames();
		// System.out.println(json.toString());
		while (keys.hasNext()) {
			String key1 = (String) keys.next();
			for (Field field : entity.getClass().getDeclaredFields()) {
				String columnName;
				if (field.getAnnotation(JoinColumn.class) != null) {
					columnName = field.getAnnotation(JoinColumn.class).name().toLowerCase();
				} else if (field.getAnnotation(Column.class) != null) {
					columnName = field.getAnnotation(Column.class).name().toLowerCase();
				} else {
					columnName = field.getName().toLowerCase();
				}

				if (columnName.toLowerCase().equals(key1.toLowerCase())) {
					if (field.getAnnotation(JoinColumn.class) != null) {
						columnName = field.getAnnotation(JoinColumn.class).name().toLowerCase();
					} else if (field.getAnnotation(Column.class) != null) {
						columnName = field.getAnnotation(Column.class).name().toLowerCase();
					} else {
						columnName = field.getName().toLowerCase();
					}
					if (field.getAnnotation(Transient.class) == null) {
						query = query.concat(columnName + ", ");
						value = value.concat("?, ");
					}

					// System.out.println(columnName + " : " +
					// key1.toLowerCase());
				}
			}
		}
		query = query.substring(0, query.length() - 2).concat(") ");
		value = value.substring(0, value.length() - 2).concat(")");
		query = query.concat(value);

		// System.out.println(query);
		return query;
	}

	public static String createQuery(String data, String namatabel) throws JsonProcessingException, IOException {

		// String query = "select kode as id from NEW table (insert into " +
		// table + " (";
		String query = "insert into " + namatabel + " (";
		String value = "values (";

		ObjectMapper mapper = new ObjectMapper();
		JsonNode json = mapper.readTree(data);

		Iterator<?> keys = json.fieldNames();
		// System.out.println(json.toString());
		while (keys.hasNext()) {
			String key1 = (String) keys.next();

			// String columnName;

			query = query.concat(key1 + ", ");
			value = value.concat("?, ");

		}
		query = query.substring(0, query.length() - 2).concat(") ");
		value = value.substring(0, value.length() - 2).concat(")");
		query = query.concat(value);

		// System.out.println(query);
		return query;
	}

	public static String updateQuery(Object entity, String data, String namatabel)
			throws JsonProcessingException, IOException {
		String table = null;
		if (namatabel == null)
			table = entity.getClass().getAnnotation(Table.class).name();
		else
			table = namatabel;
		String query = "update " + table + " set ";
		// String jsonobjkey = null;
		// if (namajsonObject == null) {
		// jsonobjkey = table;
		// } else {
		// jsonobjkey = namajsonObject;
		// }
		String id = "";
		// Iterator<?> keys = json.getJSONObject(jsonobjkey).keys();
		ObjectMapper mapper = new ObjectMapper();
		JsonNode json = mapper.readTree(data);

		Iterator<?> keys = json.fieldNames();
		while (keys.hasNext()) {
			String key = (String) keys.next();
			for (Field field : entity.getClass().getDeclaredFields()) {
				String columnName;
				if (field.getAnnotation(JoinColumn.class) != null) {
					columnName = field.getAnnotation(JoinColumn.class).name().toLowerCase();
				} else if (field.getAnnotation(Column.class) != null) {
					columnName = field.getAnnotation(Column.class).name().toLowerCase();
				} else {
					columnName = field.getName().toLowerCase();
				}
				if (columnName.equals(key.toLowerCase())) {

					if (field.getAnnotation(JoinColumn.class) != null) {
						columnName = field.getAnnotation(JoinColumn.class).name().toLowerCase();
					} else if (field.getAnnotation(Column.class) != null) {
						columnName = field.getAnnotation(Column.class).name().toLowerCase();
					} else {
						columnName = field.getName().toLowerCase();
					}

					if (field.getAnnotation(Transient.class) == null)
						query = query.concat(columnName + " = ?, ");
				}
				if (field.getAnnotation(Id.class) != null) {
					id = columnName;
				}
			}
		}
		query = query.substring(0, query.length() - 2);
		query = query.concat(" where " + id + " = ?");
		// System.out.println(query);
		return query;
	}

	public static String updateQuery(String data, String namatabel)
			throws JsonProcessingException, IOException, SQLException {
		String query = "update " + namatabel + " set ";
		ObjectMapper mapper = new ObjectMapper();
		JsonNode json = mapper.readTree(data);

		if (json.path("where").isMissingNode()) {
			throw new SQLException("Tidak ada Kode/Id");
		} else {
			Iterator<?> keys = json.fieldNames();
			while (keys.hasNext()) {
				String key = (String) keys.next();
				if (!key.equals("where")) {
					query = query.concat(key + " = ?, ");
				}
			}
			query = query.substring(0, query.length() - 2);
			keys = json.path("where").fieldNames();
			query = query.concat(" where ");
			while (keys.hasNext()) {
				String key = (String) keys.next();
				query = query.concat(key + " = ? and ");
			}
			query = query.substring(0, query.length() - 5);
			return query;
		}

	}

	// public static void setParameterEditQuery(PreparedStatement ps, Object
	// entity, String data)
	// throws SQLException, JsonProcessingException, IOException {
	// setParameterEditQuery(ps, entity, data, null);
	// }

	// public static String deleteQuery(Object entity, String data) throws
	// JsonProcessingException, IOException {
	// return deleteQuery(entity, data, null, null);
	// }

	public static String deleteQuery(Object entity, String data, String namaTabel)
			throws JsonProcessingException, IOException {
		String table = null;
		if (namaTabel == null)
			table = entity.getClass().getAnnotation(Table.class).name();
		else
			table = namaTabel;
		String query = "delete from " + table + " where ";
		// String jsonobjkey = null;
		// if (namajsonObject == null) {
		// if (entity.getClass().getAnnotation(JsonObject.class) != null) {
		// jsonobjkey =
		// entity.getClass().getAnnotation(JsonObject.class).name();
		// } else {
		// jsonobjkey = table;
		// }
		// } else {
		// jsonobjkey = namajsonObject;
		// }
		String id = "";
		ObjectMapper mapper = new ObjectMapper();
		JsonNode json = mapper.readTree(data);

		Iterator<?> keys = json.fieldNames();
		// Iterator<?> keys = json.getJSONObject(jsonobjkey).keys();
		// Iterator<?> keys = json.keys();
		while (keys.hasNext()) {
			String key = (String) keys.next();
			for (Field field : entity.getClass().getDeclaredFields()) {
				String columnName;
				if (field.getAnnotation(JoinColumn.class) != null) {
					columnName = field.getAnnotation(JoinColumn.class).name().toLowerCase();
				} else if (field.getAnnotation(Column.class) != null) {
					columnName = field.getAnnotation(Column.class).name().toLowerCase();
				} else {
					columnName = field.getName().toLowerCase();
				}
				if (columnName.equals(key.toLowerCase())) {

					if (field.getAnnotation(JoinColumn.class) != null) {
						columnName = field.getAnnotation(JoinColumn.class).name().toLowerCase();
					} else if (field.getAnnotation(Column.class) != null) {
						columnName = field.getAnnotation(Column.class).name().toLowerCase();
					} else {
						columnName = field.getName().toLowerCase();
					}

					// if (field.getAnnotation(Transient.class) == null)
					// query = query.concat(columnName + " = ?, ");
				}
				if (field.getAnnotation(Id.class) != null) {
					id = columnName;
				}
			}
		}
		// query = query.substring(0, query.length() - 2);
		query = query.concat(id + " = ?");
		// System.out.println(query);
		return query;
	}

	public static String deleteQuery(String data, String namatabel)
			throws JsonProcessingException, IOException, SQLException {
		String query = "delete from " + namatabel;
		ObjectMapper mapper = new ObjectMapper();
		JsonNode json = mapper.readTree(data);

		if (data == null || data.isEmpty()) {
			throw new SQLException("Tidak ada Kode/Id");
		} else {
			Iterator<?> keys = json.fieldNames();
			query = query.concat(" where ");
			while (keys.hasNext()) {
				String key = (String) keys.next();
				query = query.concat(key + " = ? and ");
			}
			query = query.substring(0, query.length() - 5);
			return query;
		}

	}

	public static void setParameterEditQuery(PreparedStatement ps, Object entity, String data)
			throws SQLException, JsonProcessingException, IOException {
		// String jsonobjkey = null;
		// if (namajsonObject == null)
		// jsonobjkey =
		// entity.getClass().getAnnotation(JsonProperty.class).value();
		// else
		// jsonobjkey = namajsonObject;
		String valueid = null;
		String value = "";
		String id = "";
		int i = 1;
		// Iterator<?> keys = json.getJSONObject(jsonobjkey).keys();
		ObjectMapper mapper = new ObjectMapper();
		JsonNode json = mapper.readTree(data);

		Iterator<?> keys = json.fieldNames();
		while (keys.hasNext()) {
			String key = (String) keys.next();
			for (Field field : entity.getClass().getDeclaredFields()) {
				String columnName;
				if (field.getAnnotation(JoinColumn.class) != null) {
					columnName = field.getAnnotation(JoinColumn.class).name().toLowerCase();
				} else if (field.getAnnotation(Column.class) != null) {
					columnName = field.getAnnotation(Column.class).name().toLowerCase();
				} else {
					columnName = field.getName().toLowerCase();
				}
				if (columnName.equals(key.toLowerCase())) {

					if (field.getAnnotation(Transient.class) == null) {
						// value =
						// json.getJSONObject(jsonobjkey).getString(key);
						value = json.path(key).asText();
						value = "".equals(value) ? null : value;
						ps.setString(i++, value);
					}
					if (field.getAnnotation(Id.class) != null) {
						// valueid =
						// json.getJSONObject(jsonobjkey).getString(key);
						valueid = json.path(key).asText();
						// System.out.println(columnName + " : " + value);
					}

					// System.out.println(columnName + " : " + value);
				}
				if (field.getAnnotation(Id.class) != null) {
					id = columnName;
				}
			}

		}
		// if (!json.isNull("where"))
		// if (SharedMethod.getJSONObject(json, "where", id) != null) {
		// valueid = (String) SharedMethod.getJSONObject(json, "where", id);
		// }
		if (!json.path("where").isNull())
			if (!json.path("where").path(id).isNull()) {
				valueid = json.path("where").path(id).asText();
			}
		ps.setString(i, valueid);
		if (valueid == null)
			throw new SQLException("Tidak ada Kode/Id");

		// System.out.println("where id = " + valueid);
	}

	public static void setParameterEditQuery(PreparedStatement ps, String data)
			throws SQLException, JsonProcessingException, IOException {

		String valueid = null;
		String value = "";
		int i = 1;

		ObjectMapper mapper = new ObjectMapper();
		JsonNode json = mapper.readTree(data);

		if (json.path("where").isMissingNode()) {
			throw new SQLException("Tidak ada Kode/Id");
		} else {

			Iterator<?> keys = json.fieldNames();
			while (keys.hasNext()) {
				String key = (String) keys.next();
				if (!key.equals("where")) {
					value = json.path(key).asText();
					value = "".equals(value) || value.equals("null") ? null : value;
					ps.setString(i++, value);
				}
			}

			keys = json.path("where").fieldNames();
			while (keys.hasNext()) {
				String key = (String) keys.next();
				valueid = json.path("where").path(key).asText();
				ps.setString(i++, valueid);
			}
			if (valueid == null)
				throw new SQLException("Nilai Kode/Id tidak ada");
		}
	}

	public static void setParameterCreateQuery(PreparedStatement ps, Object entity, String data)
			throws SQLException, JsonProcessingException, IOException {

		// String jsonobjkey = null;
		// if (namajsonObject == null)
		// jsonobjkey =
		// entity.getClass().getAnnotation(JsonProperty.class).value();
		// else
		// jsonobjkey = namajsonObject;
		// Iterator<?> keys = json.getJSONObject(jsonobjkey).keys();
		ObjectMapper mapper = new ObjectMapper();
		JsonNode json = mapper.readTree(data);

		Iterator<?> keys = json.fieldNames();
		String value;
		int i = 1;

		while (keys.hasNext()) {
			String key = (String) keys.next();
			String columnName = "";
			// System.out.println("key="+key);

			for (Field field : entity.getClass().getDeclaredFields()) {
				if (field.getAnnotation(JoinColumn.class) != null) {
					columnName = field.getAnnotation(JoinColumn.class).name().toLowerCase();
				} else if (field.getAnnotation(Column.class) != null) {
					columnName = field.getAnnotation(Column.class).name().toLowerCase();
				} else {
					columnName = field.getName().toLowerCase();
				}

				// System.out.println("columnName"+columnName);

				if (columnName.equals(key.toLowerCase())) {
					if (field.getAnnotation(Transient.class) == null) {
						value = json.path(key).asText();
						// value =
						// json.getJSONObject(jsonobjkey).getString(key);
						value = "".equals(value) ? null : value;
						ps.setString(i++, value);

						// System.out.println(columnName + " : " + value);
					}
				}

			}
		}
	}

	public static void setParameterCreateQuery(PreparedStatement ps, String data)
			throws SQLException, JsonProcessingException, IOException {

		ObjectMapper mapper = new ObjectMapper();
		JsonNode json = mapper.readTree(data);

		Iterator<?> keys = json.fieldNames();
		String value;
		int i = 1;

		while (keys.hasNext()) {
			String key = (String) keys.next();

			// System.out.println(key);

			value = json.path(key).asText();
			value = "".equals(value) || value.equals("null") ? null : value;
			ps.setString(i++, value);

		}
	}

	// public static String sortQuery(Object entity, JSONObject json) {
	// String table = entity.getClass().getAnnotation(Table.class).name();
	// String query = "insert into " + table + " (";
	// String value = "values (";
	// String jsonobjkey = null;
	// if (entity.getClass().getAnnotation(JsonObject.class) != null) {
	// jsonobjkey = entity.getClass().getAnnotation(JsonObject.class)
	// .name();
	// } else {
	// jsonobjkey = table;
	// }
	// Iterator<?> keys = json.getJSONObject(jsonobjkey).keys();
	// while (keys.hasNext()) {
	// String key1 = (String) keys.next();
	// for (Field field : entity.getClass().getDeclaredFields()) {
	// String columnName;
	// if (field.getAnnotation(JsonKey.class) != null) {
	// columnName = field.getAnnotation(JsonKey.class).name()
	// .toLowerCase();
	// } else if (field.getAnnotation(Column.class) != null) {
	// columnName = field.getAnnotation(Column.class).name()
	// .toLowerCase();
	// } else {
	// columnName = field.getName().toLowerCase();
	// }
	// // System.out.println(columnName + " : " + key1.toLowerCase());
	// if (columnName.toLowerCase().equals(key1.toLowerCase())) {
	// if (field.getAnnotation(Column.class) != null) {
	// columnName = field.getAnnotation(Column.class).name()
	// .toLowerCase();
	// } else {
	// columnName = field.getName().toLowerCase();
	// }
	// query = query.concat(columnName + ", ");
	// value = value.concat("?, ");
	// }
	// }
	// }
	// query = query.substring(0, query.length() - 2).concat(") ");
	// value = value.substring(0, value.length() - 2).concat(")");
	// query = query.concat(value);
	// // System.out.println(query);
	// return query;
	// }

	public static void setParameterDeleteQuery(PreparedStatement ps, Object entity, String data)
			throws SQLException, JsonProcessingException, IOException {
		// String jsonobjkey = null;
		// if (namajsonObject == null)
		// jsonobjkey =
		// entity.getClass().getAnnotation(JsonProperty.class).value();
		// else
		// jsonobjkey = namajsonObject;
		String valueid = "";
		// String value = "";
		int i = 1;
		// Iterator<?> keys = json.getJSONObject(jsonobjkey).keys();
		ObjectMapper mapper = new ObjectMapper();
		JsonNode json = mapper.readTree(data);

		Iterator<?> keys = json.fieldNames();

		while (keys.hasNext()) {
			String key = (String) keys.next();
			for (Field field : entity.getClass().getDeclaredFields()) {
				String columnName;
				if (field.getAnnotation(JoinColumn.class) != null) {
					columnName = field.getAnnotation(JoinColumn.class).name().toLowerCase();
				} else if (field.getAnnotation(Column.class) != null) {
					columnName = field.getAnnotation(Column.class).name().toLowerCase();
				} else {
					columnName = field.getName().toLowerCase();
				}
				if (columnName.equals(key.toLowerCase())) {
					if (field.getAnnotation(Id.class) != null) {
						// valueid =
						// json.getJSONObject(jsonobjkey).getString(key);
						valueid = json.path(key).asText();
					}
				}
			}

		}
		ps.setString(i, valueid);

		// System.out.println("where kode = " + valueid);
	}

	public static void setParameterDeleteQuery(PreparedStatement ps, String data)
			throws SQLException, JsonProcessingException, IOException {
		String valueid = null;
		int i = 1;

		ObjectMapper mapper = new ObjectMapper();
		JsonNode json = mapper.readTree(data);

		if (data == null || data.isEmpty()) {
			throw new SQLException("Tidak ada Kode/Id");
		} else {

			Iterator<?> keys = json.fieldNames();
			while (keys.hasNext()) {
				String key = (String) keys.next();
				valueid = json.path(key).asText();
				ps.setString(i++, valueid);
			}

		}
	}

	public static void Setter(Object obj, String methodname, Object value) {

		try {
			if (value != null) {
				obj.getClass().getMethod(methodname, value.getClass()).invoke(obj, value);
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			e.printStackTrace();
		}
	}

	// public static String getSortedColumn(Object entity, String data, boolean
	// isSubJson) throws Exception {
	// ObjectMapper mapper = new ObjectMapper();
	// JsonNode json = mapper.readTree(data);
	//
	// Iterator<?> keys = json.fieldNames();
	//// Iterator<?> keys = json.keys();
	// String value;
	// String order = null;
	// boolean koma = false;
	// String tablename = null;
	//// if (entity.getClass().getAnnotation(JsonObject.class) != null) {
	//// tablename =
	// entity.getClass().getAnnotation(JsonObject.class).name().toLowerCase();
	//// } else {
	//// tablename =
	// entity.getClass().getAnnotation(Table.class).name().toLowerCase();
	//// }
	// tablename =
	// entity.getClass().getAnnotation(Table.class).name().toLowerCase();
	// if (json.size() > 1) {
	// throw new Exception("Kolom yang disort tidak boleh lebih dari 1");
	// }
	//
	// if (keys.hasNext()) {
	// String key = (String) keys.next();
	// String columnName = "";
	// for (Field field : entity.getClass().getDeclaredFields()) {
	// if (field.getAnnotation(JsonObject.class) == null) {
	// if (field.getAnnotation(JsonKey.class) != null) {
	// columnName = field.getAnnotation(JsonKey.class).name().toLowerCase();
	// } else if (field.getAnnotation(Column.class) != null) {
	// columnName = field.getAnnotation(Column.class).name().toLowerCase();
	// } else {
	// columnName = field.getName().toLowerCase();
	// }
	//
	// if (columnName.equals(key.toLowerCase())) {
	// value = json.getString(key);
	// if (!value.contains("asc") && (!value.contains("desc"))) {
	// throw new Exception("Tipe sort harus ASC atau DESC");
	// }
	// if (field.getAnnotation(Column.class) != null) {
	// columnName = field.getAnnotation(Column.class).name().toLowerCase();
	// } else {
	// columnName = field.getName().toLowerCase();
	// }
	// if (order == null)
	// order = "";
	// if (order.length() > 0)
	// koma = true;
	// if (koma)
	// order = order.concat(", ");
	// if (isSubJson)
	// columnName = columnName.concat(tablename);
	// order = order.concat(columnName).concat(" ").concat(value);
	// koma = true;
	// // System.out.println(order);
	// }
	// } else {
	// String JsonKeyObject =
	// field.getAnnotation(JsonObject.class).name().toLowerCase();
	// if (JsonKeyObject.equals(key.toLowerCase())) {
	// Object object = new Object();
	// object = Class.forName(field.getType().getName()).newInstance();
	// if (order == null)
	// order = "";
	// if (order.length() > 0)
	// koma = true;
	// if (koma)
	// order = order.concat(", ");
	// order = order.concat(getSortedColumn(object, json.getJSONObject(key),
	// true));
	// koma = true;
	// // System.out.println(order);
	// }
	// }
	// }
	// }
	// // System.out.println(order);
	// return order;
	// }

	public static String getSortedColumn(String data) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode json = mapper.readTree(data);

		Iterator<?> keys = json.fieldNames();
		String value;
		String order = null;
		boolean koma = false;
		// String tablename = null;

		while (keys.hasNext()) {
			String key = (String) keys.next();
			// String columnName = "";

			value = json.path(key).asText();
			if (!value.contains("asc") && (!value.contains("desc"))) {
				throw new Exception("Tipe sort harus ASC atau DESC");
			}
			if (order == null)
				order = "";
			if (order.length() > 0)
				koma = true;
			if (koma)
				order = order.concat(", ");
			order = order.concat(key).concat(" ").concat(value);
			koma = true;

		}

		// System.out.println(order);
		return order;

	}

	// public static String getFilteredColumn(Object entity, JSONObject json,
	// boolean isSubJson, String jsonObjectName)
	// throws Exception {
	// Iterator<?> keys = json.keys();
	// String value;
	// String filter = null;
	// boolean dan = false;
	// String jsonName = null;
	// // if (entity.getClass().getAnnotation(JsonObject.class) != null) {
	// // tablename = entity.getClass().getAnnotation(JsonObject.class)
	// // .name().toLowerCase();
	// // } else {
	// // tablename = entity.getClass().getAnnotation(Table.class).name()
	// // .toLowerCase();
	// // }
	// while (keys.hasNext()) {
	// String key = (String) keys.next();
	// String columnName = "";
	// for (Field field : entity.getClass().getDeclaredFields()) {
	// if (field.getAnnotation(JsonObject.class) == null) {
	// if (field.getAnnotation(JsonKey.class) != null) {
	// columnName = field.getAnnotation(JsonKey.class).name().toLowerCase();
	// } else if (field.getAnnotation(Column.class) != null) {
	// columnName = field.getAnnotation(Column.class).name().toLowerCase();
	// } else {
	// columnName = field.getName().toLowerCase();
	// }
	//
	// if (columnName.equals(key.toLowerCase())) {
	// value = json.getString(key);
	// if (field.getAnnotation(Column.class) != null) {
	// columnName = field.getAnnotation(Column.class).name().toLowerCase();
	// } else {
	// columnName = field.getName().toLowerCase();
	// }
	// if (filter == null)
	// filter = "";
	// if (filter.length() > 0)
	// dan = true;
	// if (dan)
	// filter = filter.concat(" and ");
	// if (isSubJson)
	// columnName = columnName.concat(jsonObjectName);
	// filter = filter.concat(columnName).concat(" like ").concat("'%" + value +
	// "%'");
	// dan = true;
	// }
	// } else {
	// jsonName = field.getAnnotation(JsonObject.class).name().toLowerCase();
	// String JsonKeyObject =
	// field.getAnnotation(JsonObject.class).name().toLowerCase();
	// if (JsonKeyObject.equals(key.toLowerCase())) {
	// Object object = new Object();
	// object = Class.forName(field.getType().getName()).newInstance();
	// if (filter == null)
	// filter = "";
	// if (filter.length() > 0)
	// dan = true;
	// if (dan)
	// filter = filter.concat(" and ");
	// filter = filter.concat(getFilteredColumn(object, json.getJSONObject(key),
	// true, jsonName));
	// dan = true;
	// }
	// }
	// }
	// }
	// // System.out.println(filter);
	// return filter;
	// }

	public static String getFilteredColumn(String data, String jsonObjectName) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode json = mapper.readTree(data);

		Iterator<?> keys = json.fieldNames();
		String value;
		String filter = null;
		boolean dan = false;
		// String jsonName = null;

		while (keys.hasNext()) {
			String key = (String) keys.next();
			// String columnName = "";

			value = json.path(key).asText();

			if (filter == null)
				filter = "";
			if (filter.length() > 0)
				dan = true;
			if (dan)
				filter = filter.concat(" and ");

			// filter = filter.concat(key).concat(" like ").concat("'%" + value
			// + "%'");
			filter = filter.concat(key + " ").concat(value);
			dan = true;

		}
		// System.out.println(filter);
		return filter;
	}

	public static void ValidasiError(SQLException e, Map<String, Object> metadata, String prefixMessage) {
		PreparedStatement ps = null;
		Connection con = null;
		ResultSet rs = null;
		// Koneksi koneksi = null;

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
				// koneksi = new Koneksi();
				// koneksi.BuatKoneksi();
				con = new Koneksi().getConnection();

				ps = con.prepareStatement(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
				ps.setString(1, tabel);
				ps.setString(2, kolom);
				rs = ps.executeQuery();
				if (rs.next()) {
					metadata.put("message", prefixMessage + "Kolom " + rs.getString("COLNAME") + " tidak boleh kosong");
				}

			} catch (SQLException e1) {
				e1.printStackTrace();
			} catch (NamingException e1) {
				e1.printStackTrace();
			} finally {
				if (rs != null) {
					try {
						rs.close();
					} catch (SQLException e1) {
					}
				}

				if (ps != null) {
					try {
						ps.close();
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
		} else if (e.getErrorCode() == -530) {
			metadata.put("message", prefixMessage + "Foreign key invalid");
		} else if (e.getErrorCode() == -798) {
			metadata.put("message", prefixMessage + "Kolom autogenerated tidak bisa diisi");
		} else if (e.getErrorCode() == -803) {
			metadata.put("message", prefixMessage + "Constraint kolom karena duplikasi");
		} else
			metadata.put("message", e.getMessage());
	}

	public static String getTime() {
		java.util.Date date = new java.util.Date();
		return new java.text.SimpleDateFormat("yyMMddHHmmssSSS").format(date);
	}
	
	public static String getSQLTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		java.util.Date now = new java.util.Date();
	    String strDate = sdf.format(now);
	    return strDate;
	}

	public static String getErrorMessage(SQLException ex) {
		Connection con = null;
		ResultSet rs = null;
		Statement stmt = null;
		String query = null;
		String message = null;

		query = "select * from hcis.errorcode where kode = " + ex.getErrorCode();

		try {
			con = new Koneksi().getConnection();
			stmt = con.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			rs = stmt.executeQuery(query);
			if (rs.next()) {
				message = rs.getString("nama");
			} else {
				message = ex.getErrorCode() + " : " + ex.getMessage();
			}

		} catch (SQLException | NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}

			if (stmt != null) {
				try {
					stmt.close();
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

		if (ex.getErrorCode() == 515) {
			String kolom = ex.getMessage().split(", ")[0].split("'")[1];
			message = "Kolom " + kolom + " tidak boleh kosong";
		} else if (ex.getErrorCode() == 50000) {
			message = ex.getMessage();
		}

		return message;
	}
}
