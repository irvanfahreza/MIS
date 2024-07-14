package id.go.bpjskesehatan.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import id.go.bpjskesehatan.entitas.referensi.Anggaran;
import id.go.bpjskesehatan.skpd.Detailskpd;
import id.go.bpjskesehatan.skpd.Jeniskendaraan;
import id.go.bpjskesehatan.skpd.Skpd;

@Path("/skpd")
public class SkpdRest {

	@POST
	@Path("/{servicename}/list/{page}/{row}")
	@Produces("application/json")
	public Response getListData(@Context HttpHeaders headers, @PathParam("servicename") String servicename,
			@PathParam("page") String page, @PathParam("row") String row, String data) {
		String query = null;
		String namasp = null;
		String namaentitas = null;
		Object obj = null;
		namasp = "sp_list" + servicename;
		namaentitas = servicename;
		switch (servicename) {
		case "anggaran":
			obj = new Anggaran();
			break;
		case "jeniskendaraan":
			obj = new Jeniskendaraan();
			break;
		case "skpd":
			obj = new Skpd();
			break;
		case "detailskpd":
			obj = new Detailskpd();
			break;
		default:
			return Response.status(Status.NOT_FOUND).build();
		}
		query = "exec skpd." + namasp + " ?, ?, ?, ?, ?";
		return RestMethod.getListData(headers, page, row, namaentitas, obj, query, data);
	}

	@POST
	@Path("/{servicename}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response createData(@Context HttpHeaders headers, @PathParam("servicename") String servicename,
			String data) {

		Object obj = null;
		String namatabel = null;
		namatabel = "skpd." + servicename;
		switch (servicename) {
		case "anggaran":
			obj = new Anggaran();
			break;
		case "jeniskendaraan":
			obj = new Jeniskendaraan();
			break;
		case "skpd":
			obj = new Skpd();
			break;
		case "detailskpd":
			obj = new Detailskpd();
			break;
		default:
			return Response.status(Status.NOT_FOUND).build();
		}
		return RestMethod.createData(headers, data, obj, namatabel);
	}

	@PUT
	@Path("/{servicename}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response updateData(@Context HttpHeaders headers, @PathParam("servicename") String servicename,
			String data) {

		Object obj = null;
		String namatabel = null;
		namatabel = "skpd." + servicename;
		switch (servicename) {
		case "anggaran":
			obj = new Anggaran();
			break;
		case "jeniskendaraan":
			obj = new Jeniskendaraan();
			break;
		case "skpd":
			obj = new Skpd();
			break;
		case "detailskpd":
			obj = new Detailskpd();
			break;
		default:
			return Response.status(Status.NOT_FOUND).build();
		}
		return RestMethod.updateData(headers, data, obj, namatabel);
	}

	@POST
	@Path("/{servicename}/delete")
	@Consumes("application/json")
	@Produces("application/json")
	public Response deleteData(@Context HttpHeaders headers, @PathParam("servicename") String servicename,
			String data) {

		Object obj = null;
		String namatabel = null;
		namatabel = "skpd." + servicename;
		switch (servicename) {
		case "anggaran":
			obj = new Anggaran();
			break;
		case "jeniskendaraan":
			obj = new Jeniskendaraan();
			break;
		case "skpd":
			obj = new Skpd();
			break;
		case "detailskpd":
			obj = new Detailskpd();
			break;
		default:
			return Response.status(Status.NOT_FOUND).build();
		}
		return RestMethod.deleteData(headers, data, obj, namatabel);
	}
}
