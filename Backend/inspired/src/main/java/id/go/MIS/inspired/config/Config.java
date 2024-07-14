package id.go.MIS.inspired.config;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import id.go.MIS.inspired.config.Constant.APP_ENVIRONMENT;
import id.go.MIS.inspired.config.Constant.APP_WEBSERVER;

public class Config {
	public static APP_WEBSERVER appWebServer = APP_WEBSERVER.Wildfly;
	public static APP_ENVIRONMENT appEnvironment = APP_ENVIRONMENT.DEV;
	public static long ttlMillis = 28800000; // 8 jam
	public static String STRKEY = "ZYGcoBoPcrb40h9l";
	
	public static MultivaluedMap<String, Object> getDefaultHeaders(String bearer) {
		MultivaluedMap<String, Object> headers = new MultivaluedHashMap<String, Object>();
		headers.add("Content-Type", "application/json");
		headers.add("accept", "application/json, text/plain, text/html");
		if(bearer != null) {
			headers.add("authorization", "bearer "+bearer);
		}
		return headers;
	}
	
}