package id.go.bpjskesehatan.inspired;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

import id.go.bpjskesehatan.service.v2.CORSResponseFilter;

@ApplicationPath("/")
public class Main extends ResourceConfig {
	public Main() {
		super(
				MultiPartFeature.class);
		register(
				CORSResponseFilter.class
				);
	}
}
