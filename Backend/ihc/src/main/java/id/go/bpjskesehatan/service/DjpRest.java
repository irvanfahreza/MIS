package id.go.bpjskesehatan.service;

import id.go.bpjskesehatan.database.Koneksi;
import id.go.bpjskesehatan.entitas.GenericEntitas;
import id.go.bpjskesehatan.entitas.Metadata;
import id.go.bpjskesehatan.entitas.Result2;
import id.go.bpjskesehatan.entitas.djp.Detaildimensijabatan;
import id.go.bpjskesehatan.entitas.djp.Detailkpi;
import id.go.bpjskesehatan.entitas.djp.Detailpendidikanformal;
import id.go.bpjskesehatan.entitas.djp.Detailpendidikannonformal;
import id.go.bpjskesehatan.entitas.djp.Detailpengalamankerja;
import id.go.bpjskesehatan.entitas.djp.Djp;
import id.go.bpjskesehatan.entitas.djp.Djpindex;
import id.go.bpjskesehatan.entitas.djp.Standardjp;
import id.go.bpjskesehatan.entitas.djp.Tanggungjawab;
import id.go.bpjskesehatan.util.SharedMethod;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.NamingException;
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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("djp")
public class DjpRest {

	@POST
	@Path("/{servicename}/list/{page}/{row}")
	@Produces("application/json")
	public Response getListData(@Context HttpHeaders headers, @PathParam("servicename") String servicename,
			@PathParam("page") String page, @PathParam("row") String row, String data) {
		String query = null;
		String namasp = null;
		String namaentitas = null;
		Object obj = null;
		switch (servicename) {
		case "standardjp":
			namasp = "sp_liststandardjp";
			namaentitas = "standardjp";
			obj = new Standardjp();
			break;
		case "djpindex":
			namasp = "sp_listdjpindex";
			namaentitas = "djpindex";
			obj = new Djpindex();
			break;
		case "djp":
			namasp = "sp_listdjp";
			namaentitas = "djp";
			obj = new Djp();
			break;
		case "djpjobtitle":
			namasp = "sp_listdjpjobtitle";
			namaentitas = "djpjobtitle";
			obj = new GenericEntitas();
			break;
		case "tanggungjawab":
			namasp = "sp_listtanggungjawab";
			namaentitas = "tanggungjawab";
			obj = new Tanggungjawab();
			break;
		case "detaildimensijabatan":
			namasp = "sp_listdetaildimensijabatan";
			namaentitas = "detaildimensijabatan";
			obj = new Detaildimensijabatan();
			break;
		case "detailkpi":
			namasp = "sp_listdetailkpi";
			namaentitas = "detailkpi";
			obj = new Detailkpi();
			break;
		case "detailpendidikanformal":
			namasp = "sp_listdetailpendidikanformal";
			namaentitas = "detailpendidikanformal";
			obj = new Detailpendidikanformal();
			break;
		case "detailpendidikannonformal":
			namasp = "sp_listdetailpendidikannonformal";
			namaentitas = "detailpendidikannonformal";
			obj = new Detailpendidikannonformal();
			break;
		case "detailpengalamankerja":
			namasp = "sp_listdetailpengalamankerja";
			namaentitas = "detailpengalamankerja";
			obj = new Detailpengalamankerja();
			break;
		default:
			return Response.status(Status.NOT_FOUND).build();
		}
		query = "exec djp." + namasp + " ?, ?, ?, ?, ?";
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
		case "standardjp":
			namasp = "sp_liststandardjp";
			namaentitas = "standardjp";
			obj = new Standardjp();
			break;

		default:
			return Response.status(Status.NOT_FOUND).build();
		}
		query = "exec djp." + namasp + " ?, ?, ?, ?, ?";
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
		namatabel = "djp." + servicename;
		switch (servicename) {
		case "djp":
			obj = new Djp();
			break;
		case "standardjp":
			obj = new Standardjp();
			break;
		case "tanggungjawab":
			obj = new Tanggungjawab();
			break;
		case "detaildimensijabatan":
			obj = new Detaildimensijabatan();
			break;
		case "detailkpi":
			obj = new Detailkpi();
			break;
		case "detailpendidikanformal":
			obj = new Detailpendidikanformal();
			break;
		case "detailpendidikannonformal":
			obj = new Detailpendidikannonformal();
			break;
		case "detailpengalamankerja":
			obj = new Detailpengalamankerja();
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
		namatabel = "djp." + servicename;
		switch (servicename) {
		case "djp":
			obj = new Djp();
			break;
		case "standardjp":
			obj = new Standardjp();
			break;
		case "tanggungjawab":
			obj = new Tanggungjawab();
			break;
		case "detaildimensijabatan":
			obj = new Detaildimensijabatan();
			break;
		case "detailkpi":
			obj = new Detailkpi();
			break;
		case "detailpendidikanformal":
			obj = new Detailpendidikanformal();
			break;
		case "detailpendidikannonformal":
			obj = new Detailpendidikannonformal();
			break;
		case "detailpengalamankerja":
			obj = new Detailpengalamankerja();
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
		namatabel = "djp." + servicename;
		switch (servicename) {
		case "djp":
			obj = new Djp();
			break;
		case "standardjp":
			obj = new Standardjp();
			break;
		case "tanggungjawab":
			obj = new Tanggungjawab();
			break;
		case "detaildimensijabatan":
			obj = new Detaildimensijabatan();
			break;
		case "detailkpi":
			obj = new Detailkpi();
			break;
		case "detailpendidikanformal":
			obj = new Detailpendidikanformal();
			break;
		case "detailpendidikannonformal":
			obj = new Detailpendidikannonformal();
			break;
		case "detailpengalamankerja":
			obj = new Detailpengalamankerja();
			break;
		}
		return RestMethod.deleteData(headers, data, obj, namatabel);
	}
	
	@POST
	@Path("/copydjptodjpjabatan")
	@Consumes("application/json")
	@Produces("application/json")
	public Response copyDjptodjpjabatan(@Context HttpHeaders headers, String data) {
		// JSONObject json = new JSONObject(data);
		ObjectMapper mapper = new ObjectMapper();
		Metadata metadata = new Metadata();
		Result2 result = new Result2();
		Connection con = null;
		CallableStatement cs = null;
		Koneksi koneksi = null;
		try {
			JsonNode json = mapper.readTree(data);
			if ((json.path("kodepenugasan") != null) && (json.path("created_by") != null)) {
				String query = "exec djp.sp_copydjptodjpjabatan ?, ?";
				if (SharedMethod.ServiceAuth(headers, metadata)) {
					String kodepenugasan = json.path("kodepenugasan").asText();
					String created_by = json.path("created_by").asText();
					koneksi = new Koneksi();
					// koneksi.BuatKoneksi();
					con = koneksi.getConnection();
					cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
					cs.setString(1, kodepenugasan);
					cs.setString(2, created_by);
					cs.execute();
					metadata.setCode(1);
					metadata.setMessage("Data berhasil dicopy");
				}

			} else {
				metadata.setCode(2);
				metadata.setMessage("kodepenugasan, created_by kosong");
				metadata.setRowcount(0);
			}
		} catch (SQLException e) {
			metadata.setCode(0);
			metadata.setMessage(e.getMessage());
		} catch (NamingException e) {
			metadata.setCode(0);
			metadata.setMessage(e.getMessage());
		} catch (SecurityException e) {
			metadata.setCode(0);
			metadata.setMessage(e.getMessage());
		} catch (Exception e) {
			metadata.setCode(0);
			metadata.setMessage(e.getMessage());
		} finally {
			if (cs != null) {
				try {
					cs.close();
				} catch (SQLException e) {
				}
			}

			if (koneksi != null)
				koneksi.closeConnection();
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
				}
			}
		}
		result.setMetadata(metadata);
		// JSONObject xmlJSONObj = new JSONObject(result);
		return Response.ok(result).build();
	}
	
	@POST
	@Path("/copydjpjobtitletodjpjabatan")
	@Consumes("application/json")
	@Produces("application/json")
	public Response copyDjpjobtitletodjpjabatan(@Context HttpHeaders headers, String data) {
		// JSONObject json = new JSONObject(data);
		ObjectMapper mapper = new ObjectMapper();
		Metadata metadata = new Metadata();
		Result2 result = new Result2();
		Connection con = null;
		CallableStatement cs = null;
		Koneksi koneksi = null;
		try {
			JsonNode json = mapper.readTree(data);
			if ((json.path("kodepenugasan") != null) && (json.path("created_by") != null)) {
				String query = "exec djp.sp_copydjpjobtitletodjpjabatan ?, ?, ?";
				if (SharedMethod.ServiceAuth(headers, metadata)) {
					String kodejobtitle = json.path("kodejobtitle").asText();
					String kodepenugasan = json.path("kodepenugasan").asText();
					String created_by = json.path("created_by").asText();
					koneksi = new Koneksi();
					// koneksi.BuatKoneksi();
					con = koneksi.getConnection();
					cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
					cs.setString(1, kodejobtitle);
					cs.setString(2, kodepenugasan);
					cs.setString(3, created_by);
					cs.execute();
					metadata.setCode(1);
					metadata.setMessage("Data berhasil dicopy");
				}

			} else {
				metadata.setCode(2);
				metadata.setMessage("kodejobtitle, kodepenugasan, created_by kosong");
				metadata.setRowcount(0);
			}
		} catch (SQLException e) {
			metadata.setCode(0);
			metadata.setMessage(e.getMessage());
		} catch (NamingException e) {
			metadata.setCode(0);
			metadata.setMessage(e.getMessage());
		} catch (SecurityException e) {
			metadata.setCode(0);
			metadata.setMessage(e.getMessage());
		} catch (Exception e) {
			metadata.setCode(0);
			metadata.setMessage(e.getMessage());
		} finally {
			if (cs != null) {
				try {
					cs.close();
				} catch (SQLException e) {
				}
			}

			if (koneksi != null)
				koneksi.closeConnection();
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
				}
			}
		}
		result.setMetadata(metadata);
		// JSONObject xmlJSONObj = new JSONObject(result);
		return Response.ok(result).build();
	}
	
	@POST
	@Path("/copydjpjobtitletodjpjobtitle")
	@Consumes("application/json")
	@Produces("application/json")
	public Response copyDjpjobtitletodjpjobtitle(@Context HttpHeaders headers, String data) {
		// JSONObject json = new JSONObject(data);
		ObjectMapper mapper = new ObjectMapper();
		Metadata metadata = new Metadata();
		Result2 result = new Result2();
		Connection con = null;
		CallableStatement cs = null;
		Koneksi koneksi = null;
		try {
			JsonNode json = mapper.readTree(data);
			String query = "exec djp.sp_copydjpjobtitletodjpjobtitle ?, ?, ?";
			if (SharedMethod.ServiceAuth(headers, metadata)) {
				String fromkodejobtitle = json.path("fromkodejobtitle").asText();
				String tokodejobtitle = json.path("tokodejobtitle").asText();
				String created_by = json.path("created_by").asText();
				koneksi = new Koneksi();
				// koneksi.BuatKoneksi();
				con = koneksi.getConnection();
				cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
				cs.setString(1, fromkodejobtitle);
				cs.setString(2, tokodejobtitle);
				cs.setString(3, created_by);
				cs.execute();
				metadata.setCode(1);
				metadata.setMessage("Data berhasil dicopy");
			}
		} catch (SQLException e) {
			metadata.setCode(0);
			metadata.setMessage(e.getMessage());
		} catch (NamingException e) {
			metadata.setCode(0);
			metadata.setMessage(e.getMessage());
		} catch (SecurityException e) {
			metadata.setCode(0);
			metadata.setMessage(e.getMessage());
		} catch (Exception e) {
			metadata.setCode(0);
			metadata.setMessage(e.getMessage());
		} finally {
			if (cs != null) {
				try {
					cs.close();
				} catch (SQLException e) {
				}
			}

			if (koneksi != null)
				koneksi.closeConnection();
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
				}
			}
		}
		result.setMetadata(metadata);
		// JSONObject xmlJSONObj = new JSONObject(result);
		return Response.ok(result).build();
	}
	
	@POST
	@Path("/copydjpjabatantodjpjabatan")
	@Consumes("application/json")
	@Produces("application/json")
	public Response copyDjpjabatantodjpjabatan(@Context HttpHeaders headers, String data) {
		// JSONObject json = new JSONObject(data);
		ObjectMapper mapper = new ObjectMapper();
		Metadata metadata = new Metadata();
		Result2 result = new Result2();
		Connection con = null;
		CallableStatement cs = null;
		Koneksi koneksi = null;
		try {
			JsonNode json = mapper.readTree(data);
			if ((json.path("fromkodepenugasan") != null) && (json.path("tokodepenugasan") != null) && (json.path("created_by") != null)) {
				String query = "exec djp.sp_copydjpjabatantodjpjabatan ?, ?, ?";
				if (SharedMethod.ServiceAuth(headers, metadata)) {
					String fromkodepenugasan = json.path("fromkodepenugasan").asText();
					String tokodepenugasan = json.path("tokodepenugasan").asText();
					String created_by = json.path("created_by").asText();
					koneksi = new Koneksi();
					// koneksi.BuatKoneksi();
					con = koneksi.getConnection();
					cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
					cs.setString(1, fromkodepenugasan);
					cs.setString(2, tokodepenugasan);
					cs.setString(3, created_by);
					cs.execute();
					metadata.setCode(1);
					metadata.setMessage("Data berhasil dicopy");
				}

			} else {
				metadata.setCode(2);
				metadata.setMessage("fromkodepenugasan, tokodepenugasan, created_by kosong");
				metadata.setRowcount(0);
			}
		} catch (SQLException e) {
			metadata.setCode(0);
			metadata.setMessage(e.getMessage());
		} catch (NamingException e) {
			metadata.setCode(0);
			metadata.setMessage(e.getMessage());
		} catch (SecurityException e) {
			metadata.setCode(0);
			metadata.setMessage(e.getMessage());
		} catch (Exception e) {
			metadata.setCode(0);
			metadata.setMessage(e.getMessage());
		} finally {
			if (cs != null) {
				try {
					cs.close();
				} catch (SQLException e) {
				}
			}

			if (koneksi != null)
				koneksi.closeConnection();
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
				}
			}
		}
		result.setMetadata(metadata);
		// JSONObject xmlJSONObj = new JSONObject(result);
		return Response.ok(result).build();
	}
	
	@POST
	@Path("/copyalldjpjobtitletodjpjabatan")
	@Consumes("application/json")
	@Produces("application/json")
	public Response copyAlldjpjobtitletodjpjabatan(@Context HttpHeaders headers, String data) {
		// JSONObject json = new JSONObject(data);
		ObjectMapper mapper = new ObjectMapper();
		Metadata metadata = new Metadata();
		Result2 result = new Result2();
		Connection con = null;
		CallableStatement cs = null;
		Koneksi koneksi = null;
		try {
			JsonNode json = mapper.readTree(data);
			if ((json.path("statuspegawai") != null) && (json.path("created_by") != null)) {
				String query = "exec djp.sp_copyalldjpjobtitletodjpjabatan ?, ?";
				if (SharedMethod.ServiceAuth(headers, metadata)) {
					String statuspegawai = json.path("statuspegawai").asText();
					String created_by = json.path("created_by").asText();
					koneksi = new Koneksi();
					// koneksi.BuatKoneksi();
					con = koneksi.getConnection();
					cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
					cs.setString(1, statuspegawai);
					cs.setString(2, created_by);
					cs.execute();
					metadata.setCode(1);
					metadata.setMessage("Data berhasil dicopy");
				}

			} else {
				metadata.setCode(2);
				metadata.setMessage("statuspegawai, created_by kosong");
				metadata.setRowcount(0);
			}
		} catch (SQLException e) {
			metadata.setCode(0);
			metadata.setMessage(e.getMessage());
		} catch (NamingException e) {
			metadata.setCode(0);
			metadata.setMessage(e.getMessage());
		} catch (SecurityException e) {
			metadata.setCode(0);
			metadata.setMessage(e.getMessage());
		} catch (Exception e) {
			metadata.setCode(0);
			metadata.setMessage(e.getMessage());
		} finally {
			if (cs != null) {
				try {
					cs.close();
				} catch (SQLException e) {
				}
			}

			if (koneksi != null)
				koneksi.closeConnection();
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
				}
			}
		}
		result.setMetadata(metadata);
		// JSONObject xmlJSONObj = new JSONObject(result);
		return Response.ok(result).build();
	}
	
	@POST
	@Path("/createhistorydjpjobtitle")
	@Consumes("application/json")
	@Produces("application/json")
	public Response copyCreatehistorydjpjobtitle(@Context HttpHeaders headers, String data) {
		// JSONObject json = new JSONObject(data);
		ObjectMapper mapper = new ObjectMapper();
		Metadata metadata = new Metadata();
		Result2 result = new Result2();
		Connection con = null;
		CallableStatement cs = null;
		Koneksi koneksi = null;
		try {
			JsonNode json = mapper.readTree(data);
			if ((json.path("row_status") != null) && (json.path("created_by") != null)) {
				String query = "exec djp.sp_createhistorydjpjobtitle ?, ?";
				if (SharedMethod.ServiceAuth(headers, metadata)) {
					String row_status = json.path("row_status").asText();
					String created_by = json.path("created_by").asText();
					koneksi = new Koneksi();
					// koneksi.BuatKoneksi();
					con = koneksi.getConnection();
					cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
					cs.setString(1, row_status);
					cs.setString(2, created_by);
					cs.execute();
					metadata.setCode(1);
					metadata.setMessage("History berhasil dibuat");
				}

			} else {
				metadata.setCode(2);
				metadata.setMessage("row_status, created_by kosong");
				metadata.setRowcount(0);
			}
		} catch (SQLException e) {
			metadata.setCode(0);
			metadata.setMessage(e.getMessage());
		} catch (NamingException e) {
			metadata.setCode(0);
			metadata.setMessage(e.getMessage());
		} catch (SecurityException e) {
			metadata.setCode(0);
			metadata.setMessage(e.getMessage());
		} catch (Exception e) {
			metadata.setCode(0);
			metadata.setMessage(e.getMessage());
		} finally {
			if (cs != null) {
				try {
					cs.close();
				} catch (SQLException e) {
				}
			}

			if (koneksi != null)
				koneksi.closeConnection();
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
				}
			}
		}
		result.setMetadata(metadata);
		// JSONObject xmlJSONObj = new JSONObject(result);
		return Response.ok(result).build();
	}
	
	@POST
	@Path("/createhistorydjpjobtitle/kodejobtitle/{kodejobtitle}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response copyCreatehistorydjpjobtitleBykodejobtitle(@Context HttpHeaders headers, String data, @PathParam("kodejobtitle") String kodejobtitle) {
		// JSONObject json = new JSONObject(data);
		ObjectMapper mapper = new ObjectMapper();
		Metadata metadata = new Metadata();
		Result2 result = new Result2();
		Connection con = null;
		CallableStatement cs = null;
		Koneksi koneksi = null;
		try {
			JsonNode json = mapper.readTree(data);
			if ((json.path("row_status") != null) && (json.path("created_by") != null)) {
				String query = "exec djp.sp_createhistorydjpjobtitle ?, ?, ?";
				if (SharedMethod.ServiceAuth(headers, metadata)) {
					String row_status = json.path("row_status").asText();
					String created_by = json.path("created_by").asText();
					koneksi = new Koneksi();
					// koneksi.BuatKoneksi();
					con = koneksi.getConnection();
					cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
					cs.setString(1, row_status);
					cs.setString(2, created_by);
					cs.setString(3, kodejobtitle);
					cs.execute();
					metadata.setCode(1);
					metadata.setMessage("History " + kodejobtitle + " berhasil dibuat");
				}

			} else {
				metadata.setCode(2);
				metadata.setMessage("row_status, created_by kosong");
				metadata.setRowcount(0);
			}
		} catch (SQLException e) {
			metadata.setCode(0);
			metadata.setMessage(e.getMessage());
		} catch (NamingException e) {
			metadata.setCode(0);
			metadata.setMessage(e.getMessage());
		} catch (SecurityException e) {
			metadata.setCode(0);
			metadata.setMessage(e.getMessage());
		} catch (Exception e) {
			metadata.setCode(0);
			metadata.setMessage(e.getMessage());
		} finally {
			if (cs != null) {
				try {
					cs.close();
				} catch (SQLException e) {
				}
			}

			if (koneksi != null)
				koneksi.closeConnection();
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
				}
			}
		}
		result.setMetadata(metadata);
		// JSONObject xmlJSONObj = new JSONObject(result);
		return Response.ok(result).build();
	}
	
}
