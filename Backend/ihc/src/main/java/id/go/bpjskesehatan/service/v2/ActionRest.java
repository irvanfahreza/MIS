package id.go.bpjskesehatan.service.v2;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import id.go.bpjskesehatan.entitas.*;
import id.go.bpjskesehatan.util.SharedMethod;
import id.go.bpjskesehatan.util.v2.RestMethod;

@Path("v2/action")
public class ActionRest {	
	
	@POST
	@Path("/create/{servicename}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response createData(@Context HttpHeaders headers, @PathParam("servicename") String servicename,
			String data) {
		Result<Object> result = new Result<Object>();
		Metadata metadata = new Metadata();
		if (SharedMethod.VerifyToken(headers, metadata)) {
			metadata = RestMethod.createData(data, servicename);
		}
		result.setMetadata(metadata);
		return Response.ok(result).build();
	}

	@PUT
	@Path("/update/{servicename}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response updateData(@Context HttpHeaders headers, @PathParam("servicename") String servicename,
			String data) {
		Result<Object> result = new Result<Object>();
		Metadata metadata = new Metadata();
		if (SharedMethod.VerifyToken(headers, metadata)) {
			metadata = RestMethod.updateData(data, servicename);
		}
		result.setMetadata(metadata);
		return Response.ok(result).build();
	}
	
	@POST
	@Path("/delete/{servicename}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response deleteData(@Context HttpHeaders headers, @PathParam("servicename") String servicename,
			String data) {
		Result<Object> result = new Result<Object>();
		Metadata metadata = new Metadata();
		if (SharedMethod.VerifyToken(headers, metadata)) {
			metadata = RestMethod.deleteData(data, servicename);
		}
		result.setMetadata(metadata);
		return Response.ok(result).build();
	}
	
}
