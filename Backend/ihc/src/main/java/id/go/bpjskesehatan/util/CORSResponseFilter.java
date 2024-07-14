package id.go.bpjskesehatan.util;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;

public class CORSResponseFilter implements ContainerResponseFilter {

	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
			throws IOException {
		MultivaluedMap<String, Object> headers = responseContext.getHeaders();

		// headers.add("Access-Control-Allow-Origin", "*");
		// headers.add("Access-Control-Allow-Origin",
		// "http://podcastpedia.org"); //allows CORS requests only coming from
		// podcastpedia.org
		// headers.add("Access-Control-Allow-Methods", "GET, POST, DELETE,
		// PUT");
		// headers.add("X-Powered-By", "BPJS Kesehatan");
		headers.add("Server", "hcis-api");
		// headers.add("Access-Control-Allow-Headers", "x-timestamp,
		// Content-Type, x-signature, x-cons-id");

	}

}
