package id.go.bpjskesehatan.util;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.annotation.JsonFormat;

import id.go.bpjskesehatan.Constant;
import id.go.bpjskesehatan.entitas.Metadata;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.impl.TextCodec;

public class Jwt {
	public static String company = "PSI-BPJS Kesehatan";
	public static long ttlMillis = 32400000; // 9 jam
	public static String secretKey = "IloveyoU4ever";
	
	private String Id;
	private String Subject;
	private String Issuer;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constant.ServerTimezone)
	private Date IssuedAt;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constant.ServerTimezone)
	private Date Expiration;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constant.ServerTimezone)
	private Date NotBefore;
	
	public Jwt(String token) {
		Claims claims = Jwts.parser().setSigningKey(TextCodec.BASE64.encode(secretKey)).parseClaimsJws(token).getBody();
			    
		this.Id = claims.getId();
		this.Subject = claims.getSubject();
		this.Issuer = claims.getIssuer();
		this.Expiration = claims.getExpiration();
		this.IssuedAt = claims.getIssuedAt();
		this.NotBefore = claims.getNotBefore();
	}

	public static String getCompany() {
		return company;
	}

	public static void setCompany(String company) {
		Jwt.company = company;
	}

	public static long getTtlMillis() {
		return ttlMillis;
	}

	public static void setTtlMillis(long ttlMillis) {
		Jwt.ttlMillis = ttlMillis;
	}

	public static String getSecretKey() {
		return secretKey;
	}

	public static void setSecretKey(String secretKey) {
		Jwt.secretKey = secretKey;
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getSubject() {
		return Subject;
	}

	public void setSubject(String subject) {
		Subject = subject;
	}

	public String getIssuer() {
		return Issuer;
	}

	public void setIssuer(String issuer) {
		Issuer = issuer;
	}

	public Date getIssuedAt() {
		return IssuedAt;
	}

	public void setIssuedAt(Date issuedAt) {
		IssuedAt = issuedAt;
	}

	public Date getExpiration() {
		return Expiration;
	}

	public void setExpiration(Date expiration) {
		Expiration = expiration;
	}

	public Date getNotBefore() {
		return NotBefore;
	}

	public void setNotBefore(Date notBefore) {
		NotBefore = notBefore;
	}
	
	public static String createJWT(String subject) {
		
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String id = Utils.generateHmacSHA256Signature(subject + "&" + timestamp.getTime(), secretKey);
		
		String issuer = Jwt.getCompany();
		long ttlMillis = Jwt.getTtlMillis();
		String secretKey = Jwt.getSecretKey();

		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);
		
		JwtBuilder builder = Jwts.builder().setId(id)
		                                .setIssuedAt(now)
		                                .setSubject(subject)
		                                .setIssuer(issuer)
		                                .signWith(SignatureAlgorithm.HS256, TextCodec.BASE64.encode(secretKey));

		if (ttlMillis >= 0) {
		    long expMillis = nowMillis + ttlMillis;
		    Date exp = new Date(expMillis);
		    builder.setExpiration(exp);
		}

		return builder.compact();
	}
	
	public static boolean claimJwt(HttpHeaders headers, Metadata metadata){
		String secretKey = Jwt.getSecretKey();
		boolean verify = false;
		if (headers.getRequestHeaders().containsKey("x-token")) {
			String compactJwt = headers.getHeaderString("x-token");
			try {
				Claims claims = Jwts.parser().setSigningKey(TextCodec.BASE64.encode(secretKey)).parseClaimsJws(compactJwt).getBody();
				verify = true;
			} catch (ExpiredJwtException e) {
				metadata.setCode(Response.Status.UNAUTHORIZED.getStatusCode());
				metadata.setMessage("Unauthorized, token expired.");
				verify = false;
			} catch (SignatureException e) {
				metadata.setCode(Response.Status.UNAUTHORIZED.getStatusCode());
				metadata.setMessage("Unauthorized, invalid token.");
				verify = false;
			} catch (UnsupportedJwtException e) {
				metadata.setCode(Response.Status.UNAUTHORIZED.getStatusCode());
				metadata.setMessage("Unauthorized, invalid token.");
				verify = false;
			} catch (Exception e){
				metadata.setCode(Response.Status.UNAUTHORIZED.getStatusCode());
				metadata.setMessage("Unauthorized, invalid token.");
				verify = false;
			}
		} else {
			metadata.setCode(Response.Status.UNAUTHORIZED.getStatusCode());
			metadata.setMessage("Unauthorized, missing token.");
			verify = false;
		}
		
		return verify;
	}
	
	public static boolean claimJwt(HttpHeaders headers, Map<String, Object> metadata){
		String secretKey = Jwt.getSecretKey();
		boolean verify = false;
		if (headers.getRequestHeaders().containsKey("x-token")) {
			String compactJwt = headers.getHeaderString("x-token");
			try {
				Claims claims = Jwts.parser().setSigningKey(TextCodec.BASE64.encode(secretKey)).parseClaimsJws(compactJwt).getBody();
				verify = true;
				
			} catch (ExpiredJwtException e) {
				metadata.put("code", Response.Status.UNAUTHORIZED.getStatusCode());
				metadata.put("message", "Unauthorized, token expired.");
				verify = false;
			} catch (SignatureException e) {
				metadata.put("code", Response.Status.UNAUTHORIZED.getStatusCode());
				metadata.put("message", "Unauthorized, invalid token.");
				verify = false;
			} catch (UnsupportedJwtException e) {
				metadata.put("code", Response.Status.UNAUTHORIZED.getStatusCode());
				metadata.put("message", "Unauthorized, invalid token.");
				verify = false;
			} catch (Exception e){
				metadata.put("code", Response.Status.UNAUTHORIZED.getStatusCode());
				metadata.put("message", "Unauthorized, invalid token.");
				verify = false;
			}
		} else {
			metadata.put("code", Response.Status.UNAUTHORIZED.getStatusCode());
			metadata.put("message", "Unauthorized, missing token.");
			verify = false;
		}
		
		return verify;
	}
}
