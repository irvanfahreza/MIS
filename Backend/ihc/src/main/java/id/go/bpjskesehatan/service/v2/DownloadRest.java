package id.go.bpjskesehatan.service.v2;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import org.apache.commons.net.ftp.FTPClient;

import id.go.bpjskesehatan.entitas.Metadata;
import id.go.bpjskesehatan.entitas.Result;
import id.go.bpjskesehatan.util.SharedMethod;

@Path("v2/download")
public class DownloadRest {	
	
	@Context
    private ServletContext context;
	
	@GET
	@Path("/{folder}/{token}/{filename}")
	@Produces({"application/pdf","image/jpeg","image/jpg","image/png"})
	public Response download(@Context HttpHeaders headers, @PathParam("token") String token, @PathParam("folder") String folder, @PathParam("filename") String filename) {
		Result<Object> result = new Result<Object>();
		Metadata metadata = new Metadata();
		
		if (SharedMethod.VerifyToken2(token, metadata)) {
			ResponseBuilder response;
			OutputStream os = null;
			FTPClient client = null;
	        try {
	        	String path = "/tmp/";
	        	client = new FTPClient();
	        	os = new BufferedOutputStream(new FileOutputStream(path + filename));
	        	String host = context.getInitParameter("ftp-host");
				Integer port = Integer.parseInt(context.getInitParameter("ftp-port"));
				String user = context.getInitParameter("ftp-user");
				String pass = context.getInitParameter("ftp-pass");
	        	client.connect(host,port);
	        	client.login(user, pass);
	        	
	        	Boolean zxc = client.retrieveFile(folder + "/" + filename, os);
	        	if(zxc) {
	            	File file = new File(path + filename);
	            	response = Response.ok((Object) file);
	            	response.header("Content-Disposition", "attachment; filename=" + filename);
					return response.build();
	            }
	            else {
	            	File del = new File(path + filename);
	            	if(del.exists()) {
	            		del.delete();
	            	}
	            }
	           
	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            try {
	            	if(client != null)
	            		client.disconnect();
	            	if(os != null)
	            		os.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	        response = Response.noContent();
			return response.build();
		}
		result.setMetadata(metadata);
		return Response.ok(result).build();
	}
	
}
