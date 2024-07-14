package id.go.bpjskesehatan.service.mobile.v1;

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
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author RMA
 *
 */
public class FirebaseInvoker {
	
	private static class NullHostnameVerifier implements HostnameVerifier {
		@Override
		public boolean verify(String arg0, SSLSession arg1) {
			// TODO Auto-generated method stub
			return true;
		}
	}
	
	public static MultivaluedMap<String, Object> getHeaders(String serverKey, String projectId) {
		MultivaluedMap<String, Object> headers = new MultivaluedHashMap<String, Object>();
		headers.add("Content-Type", "application/json");
		headers.add("Authorization", "Key=" + serverKey);
		headers.add("project_id", projectId);
		return headers;
	}
	
	public static String Post(String url, MultivaluedMap<String, Object> headers, Object data) throws Exception {
		ResteasyClient client = null;
		Response response = null;
		try {
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
			response = invocationBuilder.post(Entity.entity(data, MediaType.APPLICATION_JSON));
			String responString = response.readEntity(String.class);
			
			ObjectMapper mapperr = new ObjectMapper();
			String postString = mapperr.writeValueAsString(data);
			System.out.println("POST URL -> " + url);
			System.out.println("POST -> " + postString);
			System.out.println("RESPON -> " + responString);
			
			return responString;
		} 
		catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
		finally {
			if (response != null)
				response.close();
			if (client != null)
				client.close();
		}
	}
	
	public static String Get(String url, MultivaluedMap<String, Object> headers) throws Exception {
		ResteasyClient client = null;
		Response response = null;
		try {
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
			response = invocationBuilder.get();
			String responString = response.readEntity(String.class);
			
			System.out.println("GET URL -> " + url);
			System.out.println("RESPON -> " + responString);
			
			return responString;
		} 
		catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
		finally {
			if (response != null)
				response.close();
			if (client != null)
				client.close();
		}
	}
}