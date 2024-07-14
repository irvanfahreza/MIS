package id.go.bpjskesehatan.service.mobile.v1;

import java.util.Map;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import id.go.bpjskesehatan.entitas.Metadata;

public class AuthMobile {
	
	public static final String SECRETKEY = "IloveyoU4ever";
	
	public static boolean VerifyToken(AuthUser authUser, HttpHeaders headers, Map<String, Object> metadata) {
		try {
			if (headers.getRequestHeaders().containsKey("x-token")) {
				String token = headers.getHeaderString("x-token");
				Algorithm algorithm = Algorithm.HMAC256(SECRETKEY);
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
			} else {
				metadata.put("code", Response.Status.UNAUTHORIZED.getStatusCode());
				metadata.put("message", "Unauthorized");
			}
		} catch (TokenExpiredException e) {
//			metadata.put("code", Response.Status.UNAUTHORIZED.getStatusCode());
//			metadata.put("message", "Unauthorized, Expired Token");
			return true;
		} catch (SignatureVerificationException e) {
			metadata.put("code", Response.Status.UNAUTHORIZED.getStatusCode());
			metadata.put("message", "Unauthorized, Invalid Signature");
		} catch (Exception e) {
			metadata.put("code", Response.Status.UNAUTHORIZED.getStatusCode());
			metadata.put("message", "Unauthorized, Invalid Token");
		}
		return false;
	}
	
	public static boolean VerifyToken(AuthUser authUser, HttpHeaders headers, Metadata metadata) {
		try {
			if (headers.getRequestHeaders().containsKey("x-token")) {
				String token = headers.getHeaderString("x-token");
				Algorithm algorithm = Algorithm.HMAC256(SECRETKEY);
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
			} else {
				metadata.setCode(Response.Status.UNAUTHORIZED.getStatusCode());
				metadata.setMessage("Unauthorized");
			}
		} catch (TokenExpiredException e) {
//			metadata.setCode(Response.Status.UNAUTHORIZED.getStatusCode());
//			metadata.setMessage("Unauthorized, Expired Token");
			return true;
		} catch (SignatureVerificationException e) {
			metadata.setCode(Response.Status.UNAUTHORIZED.getStatusCode());
			metadata.setMessage("Unauthorized, Invalid Signature");
		} catch (Exception e) {
			metadata.setCode(Response.Status.UNAUTHORIZED.getStatusCode());
			metadata.setMessage("Unauthorized, Invalid Token");
		}
		return false;
	}
	
	public static boolean VerifyToken(HttpHeaders headers, Map<String, Object> metadata) {
		try {
			if (headers.getRequestHeaders().containsKey("x-token")) {
				String token = headers.getHeaderString("x-token");
				Algorithm algorithm = Algorithm.HMAC256(SECRETKEY);
				JWTVerifier verifier = JWT.require(algorithm).build();
				verifier.verify(token);
				return true;
			} else {
				metadata.put("code", Response.Status.UNAUTHORIZED.getStatusCode());
				metadata.put("message", "Unauthorized");
			}
		} catch (TokenExpiredException e) {
//			metadata.put("code", Response.Status.UNAUTHORIZED.getStatusCode());
//			metadata.put("message", "Unauthorized, Expired Token");
			return true;
		} catch (SignatureVerificationException e) {
			metadata.put("code", Response.Status.UNAUTHORIZED.getStatusCode());
			metadata.put("message", "Unauthorized, Invalid Signature");
		} catch (Exception e) {
			metadata.put("code", Response.Status.UNAUTHORIZED.getStatusCode());
			metadata.put("message", "Unauthorized, Invalid Token");
		}
		return false;
	}
	
	public static boolean VerifyToken(HttpHeaders headers, Metadata metadata) {
		try {
			if (headers.getRequestHeaders().containsKey("x-token")) {
				String token = headers.getHeaderString("x-token");
				Algorithm algorithm = Algorithm.HMAC256(SECRETKEY);
				JWTVerifier verifier = JWT.require(algorithm).build();
				verifier.verify(token);
				return true;
			} else {
				metadata.setCode(Response.Status.UNAUTHORIZED.getStatusCode());
				metadata.setMessage("Unauthorized");
			}
		} catch (TokenExpiredException e) {
//			metadata.setCode(Response.Status.UNAUTHORIZED.getStatusCode());
//			metadata.setMessage("Unauthorized, Expired Token");
			return true;
		} catch (SignatureVerificationException e) {
			metadata.setCode(Response.Status.UNAUTHORIZED.getStatusCode());
			metadata.setMessage("Unauthorized, Invalid Signature");
		} catch (Exception e) {
			metadata.setCode(Response.Status.UNAUTHORIZED.getStatusCode());
			metadata.setMessage("Unauthorized, Invalid Token");
		}
		return false;
	}
	
	public static boolean VerifyTokenValue(String token, Map<String, Object> metadata) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(SECRETKEY);
			JWTVerifier verifier = JWT.require(algorithm).build();
			verifier.verify(token);
			return true;
		} catch (TokenExpiredException e) {
//			metadata.put("code", Response.Status.UNAUTHORIZED.getStatusCode());
//			metadata.put("message", "Unauthorized, Expired Token");
			return true;
		} catch (SignatureVerificationException e) {
			metadata.put("code", Response.Status.UNAUTHORIZED.getStatusCode());
			metadata.put("message", "Unauthorized, Invalid Signature");
		} catch (Exception e) {
			metadata.put("code", Response.Status.UNAUTHORIZED.getStatusCode());
			metadata.put("message", "Unauthorized, Invalid Token");
		}
		return false;
	}
	
	public static boolean VerifyTokenValue(String token, Metadata metadata) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(SECRETKEY);
			JWTVerifier verifier = JWT.require(algorithm).build();
			verifier.verify(token);
			return true;
		} catch (TokenExpiredException e) {
//			metadata.setCode(Response.Status.UNAUTHORIZED.getStatusCode());
//			metadata.setMessage("Unauthorized, Expired Token");
			return true;
		} catch (SignatureVerificationException e) {
			metadata.setCode(Response.Status.UNAUTHORIZED.getStatusCode());
			metadata.setMessage("Unauthorized, Invalid Signature");
		} catch (Exception e) {
			metadata.setCode(Response.Status.UNAUTHORIZED.getStatusCode());
			metadata.setMessage("Unauthorized, Invalid Token");
		}
		return false;
	}
}
