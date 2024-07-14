package id.go.bpjskesehatan.util;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * @author RMA
 *
 */
public class Invoker {
	
	public static String ObjectToJsonString(Object map) {
		 String mapAsJson = null;
		try {
			mapAsJson = new ObjectMapper().writeValueAsString(map);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		 return mapAsJson;
	}
	
	private static class NullHostnameVerifier implements HostnameVerifier {
		@Override
		public boolean verify(String arg0, SSLSession arg1) {
			// TODO Auto-generated method stub
			return true;
		}
	}
	
	public static String request(String method, String url, MultivaluedMap<String, Object> headers, Object data) throws Exception {
		ResteasyClient client = null;
		Response response = null;
		try {
			System.out.println(url);
			System.out.println(ObjectToJsonString(headers));
			System.out.println(method.equalsIgnoreCase("post")?ObjectToJsonString(data):null);
			
			if(url.trim().substring(0,5).equalsIgnoreCase("https")) {
				TrustManager[] nullTrustManager = new TrustManager[]{
		                new X509TrustManager() {
							@Override
							public void checkClientTrusted(X509Certificate[] chain, String authType)
									throws CertificateException {
								// TODO Auto-generated method stub
							}
							@Override
							public void checkServerTrusted(X509Certificate[] chain, String authType)
									throws CertificateException {
								// TODO Auto-generated method stub	
							}
							@Override
							public X509Certificate[] getAcceptedIssuers() {
								// TODO Auto-generated method stub
								return null;
							}
		                }
		            };
				SSLContext sslContext = SSLContext.getInstance("SSL");
				sslContext.init(null, nullTrustManager, null);
				client = new ResteasyClientBuilder().hostnameVerifier(new NullHostnameVerifier()).sslContext(sslContext).build();
			}
			else {
				client = new ResteasyClientBuilder().build();
			}
			
			ResteasyWebTarget webTarget = client.target(url);
			Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON_TYPE);
			invocationBuilder.headers(headers);
			
			switch (method) {
				case "POST":
					String post = ObjectToJsonString(data);
					response = invocationBuilder.post(Entity.entity(post, MediaType.APPLICATION_JSON));
					break;
				case "GET":
					response = invocationBuilder.get();
					break;
				default:
					break;
			}
			
			String responString = response.readEntity(String.class);
			/*
			 * if(response.getStatus()==200) { return responString; }
			 */
	        return responString;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			if (response != null)
				response.close();
			if (client != null)
				client.close();
		}
	}
}