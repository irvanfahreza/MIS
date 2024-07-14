package id.go.MIS.inspired;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

import id.go.MIS.inspired.config.CORSResponseFilter;


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
