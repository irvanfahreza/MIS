package id.go.bpjskesehatan.service.hcplus;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

/**
 * @author RMA
 *
 */
public class Invoker {
	
	public static MultivaluedMap<String, Object> getHeaders() {
		MultivaluedMap<String, Object> headers = new MultivaluedHashMap<String, Object>();
		headers.add("Content-Type", "text/plain");
		headers.add("accept", "application/json");
		return headers;
	}
	
	public static MultivaluedMap<String, Object> getHeaders(String token) {
		MultivaluedMap<String, Object> headers = new MultivaluedHashMap<String, Object>();
		headers.add("Authorization", "Bearer " + token);
		headers.add("Content-Type", "text/plain");
		headers.add("accept", "application/json");
		return headers;
	}

	public static Response Post(String url, MultivaluedMap<String, Object> headers, Form data) throws Exception {
		ResteasyClient client = null;
		Response response = null;
		try {
			client = new ResteasyClientBuilder().build();
			ResteasyWebTarget webTarget = client.target(url);
			Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON_TYPE);
			invocationBuilder.headers(headers);
			Entity<Form> formdata = Entity.form(data);
			response = invocationBuilder.post(formdata);
			return response;
		} finally {
			/*if (response != null)
				response.close();*/
			if (client != null)
				client.close();
		}
	}
	
	public static Response Get(String url, MultivaluedMap<String, Object> headers) throws Exception {
		ResteasyClient client = null;
		Response response = null;
		try {
			client = new ResteasyClientBuilder().build();
			ResteasyWebTarget webTarget = client.target(url);
			Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON_TYPE);
			invocationBuilder.headers(headers);
			response = invocationBuilder.get();
			return response;
		} finally {
			/*if (response != null)
				response.close();*/
			if (client != null)
				client.close();
		}
	}
	
}