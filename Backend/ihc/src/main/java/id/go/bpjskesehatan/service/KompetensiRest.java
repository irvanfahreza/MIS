package id.go.bpjskesehatan.service;

import id.go.bpjskesehatan.entitas.kompetensi.Detailmodelkompetensijobtitle;
import id.go.bpjskesehatan.entitas.kompetensi.Kamuskompetensi;
import id.go.bpjskesehatan.entitas.kompetensi.Kelompokkompetensi;
import id.go.bpjskesehatan.entitas.kompetensi.Klaskelompokkompetensi;
import id.go.bpjskesehatan.entitas.kompetensi.Kompetensi;
import id.go.bpjskesehatan.entitas.kompetensi.Levelkompetensi;
import id.go.bpjskesehatan.entitas.kompetensi.Listmodelkompetensijobtitle;
import id.go.bpjskesehatan.entitas.kompetensi.Modelkompetensijobtitle;
import id.go.bpjskesehatan.entitas.kompetensi.Periodekompetensi;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("kompetensi")
public class KompetensiRest {

	@POST
	@Path("/listmodelkompetensijobtitle/list/{page}/{row}")
	@Produces("application/json")
	public Response getListModelKompetensiJobtitle(@Context HttpHeaders headers,
			@PathParam("servicename") String servicename, @PathParam("page") String page, @PathParam("row") String row,
			String data) {
		String query = null;
		String namasp = null;
		// String namaentitas = null;
		Object obj = null;

		namasp = "sp_listmodelkompetensijobtitle_jumlahkompetensi";
		obj = new Listmodelkompetensijobtitle();

		query = "exec kompetensi." + namasp + " ?, ?, ?, ?, ?";
		return RestMethod.getListData(headers, page, row, "listmodelkompetensijobtitle", obj, query, data);
	}

	@POST
	@Path("/{servicename}/list/{page}/{row}")
	@Produces("application/json")
	public Response getListDataByFilterOrSort(@Context HttpHeaders headers,
			@PathParam("servicename") String servicename, @PathParam("page") String page, @PathParam("row") String row,
			String data) {
		String query = null;
		String namasp = null;
		String namaentitas = null;
		Object obj = null;
		switch (servicename) {
		case "klaskelompokkompetensi":
			namasp = "sp_listklaskelompokkompetensi";
			namaentitas = "klaskelompokkompetensi";
			obj = new Klaskelompokkompetensi();
			break;
		case "kelompokkompetensi":
			namasp = "sp_listkelompokkompetensi";
			namaentitas = "kelompokkompetensi";
			obj = new Kelompokkompetensi();
			break;
		case "kompetensi":
			namasp = "sp_listkompetensi";
			namaentitas = "kompetensi";
			obj = new Kompetensi();
			break;
		case "levelkompetensi":
			namasp = "sp_listlevelkompetensi";
			namaentitas = "levelkompetensi";
			obj = new Levelkompetensi();
			break;
		case "periodekompetensi":
			namasp = "sp_listperiodekompetensi";
			namaentitas = "periodekompetensi";
			obj = new Periodekompetensi();
			break;
		case "kamuskompetensi":
			namasp = "sp_listkamuskompetensi";
			namaentitas = "kamuskompetensi";
			obj = new Kamuskompetensi();
			break;
		case "modelkompetensijobtitle":
			namasp = "sp_listmodelkompetensijobtitle";
			namaentitas = "modelkompetensijobtitle";
			obj = new Modelkompetensijobtitle();
			break;
		case "detailmodelkompetensijobtitle":
			namasp = "sp_listdetailmodelkompetensijobtitle";
			namaentitas = "detailmodelkompetensijobtitle";
			obj = new Detailmodelkompetensijobtitle();
			break;
		default:
			return Response.status(Status.NOT_FOUND).build();
		}
		query = "exec kompetensi." + namasp + " ?, ?, ?, ?, ?";
		return RestMethod.getListData(headers, page, row, namaentitas, obj, query, data);
	}

	@GET
	@Path("/{servicename}/id/{kode}")
	@Produces("application/json")
	public Response getListDataByKode(@Context HttpHeaders headers, @PathParam("servicename") String servicename,
			@PathParam("kode") String kode) {
		String query = null;
		String namasp = null;
		String namaentitas = null;
		Object obj = null;
		switch (servicename) {
		case "kelompokkompetensi":
			namasp = "sp_listkelompokkompetensi";
			namaentitas = "kompetensikompetensi";
			obj = new Kelompokkompetensi();
			break;

		default:
			return Response.status(Status.NOT_FOUND).build();
		}
		query = "exec kompetensi." + namasp + " ?, ?, ?, ?, ?";
		String data = "{\"filter\" : {\"kode\" : \"" + kode + "\"}}";
		return RestMethod.getListData(headers, "1", "10", namaentitas, obj, query, data);
	}

	@POST
	@Path("/{servicename}")
	@Consumes("application/json")
	public Response createData(@Context HttpHeaders headers, @PathParam("servicename") String servicename,
			String data) {

		Object obj = null;
		String namatabel = null;
		namatabel = "kompetensi." + servicename;
		switch (servicename) {
		case "klaskelompokkompetensi":
			obj = new Klaskelompokkompetensi();
			break;
		case "kelompokkompetensi":
			obj = new Kelompokkompetensi();
			break;
		case "kompetensi":
			obj = new Kompetensi();
			break;
		case "levelkompetensi":
			obj = new Levelkompetensi();
			break;
		case "periodekompetensi":
			obj = new Periodekompetensi();
			break;
		case "kamuskompetensi":
			obj = new Kamuskompetensi();
			break;
		case "modelkompetensijobtitle":
			obj = new Modelkompetensijobtitle();
			break;
		case "detailmodelkompetensijobtitle":
			obj = new Detailmodelkompetensijobtitle();
			break;
		default:
			return Response.status(Status.NOT_FOUND).build();
		}
		return RestMethod.createData(headers, data, obj, namatabel);
	}

	@PUT
	@Path("/{servicename}")
	@Consumes("application/json")
	public Response updateData(@Context HttpHeaders headers, @PathParam("servicename") String servicename,
			String data) {

		Object obj = null;
		String namatabel = null;
		namatabel = "kompetensi." + servicename;
		switch (servicename) {
		case "klaskelompokkompetensi":
			obj = new Klaskelompokkompetensi();
			break;
		case "kelompokkompetensi":
			obj = new Kelompokkompetensi();
			break;
		case "kompetensi":
			obj = new Kompetensi();
			break;
		case "levelkompetensi":
			obj = new Levelkompetensi();
			break;
		case "periodekompetensi":
			obj = new Periodekompetensi();
			break;
		case "kamuskompetensi":
			obj = new Kamuskompetensi();
			break;
		case "modelkompetensijobtitle":
			obj = new Modelkompetensijobtitle();
			break;
		case "detailmodelkompetensijobtitle":
			obj = new Detailmodelkompetensijobtitle();
			break;
		default:
			return Response.status(Status.NOT_FOUND).build();
		}
		return RestMethod.updateData(headers, data, obj, namatabel);
	}

	@POST
	@Path("/{servicename}/delete")
	@Consumes("application/json")
	public Response deleteData(@Context HttpHeaders headers, String data,
			@PathParam("servicename") String servicename) {
		Object obj = null;
		String namatabel = null;
		namatabel = "kompetensi." + servicename;
		switch (servicename) {
		case "klaskelompokkompetensi":
			obj = new Klaskelompokkompetensi();
			break;
		case "kelompokkompetensi":
			obj = new Kelompokkompetensi();
			break;
		case "kompetensi":
			obj = new Kompetensi();
			break;
		case "levelkompetensi":
			obj = new Levelkompetensi();
			break;
		case "periodekompetensi":
			obj = new Periodekompetensi();
			break;
		case "kamuskompetensi":
			obj = new Kamuskompetensi();
			break;
		case "modelkompetensijobtitle":
			obj = new Modelkompetensijobtitle();
			break;
		case "detailmodelkompetensijobtitle":
			obj = new Detailmodelkompetensijobtitle();
			break;
		}
		return RestMethod.deleteData(headers, data, obj, namatabel);
	}
}
