package id.go.bpjskesehatan.service;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import id.go.bpjskesehatan.database.Koneksi;
import id.go.bpjskesehatan.entitas.cuti.Cuti;
import id.go.bpjskesehatan.entitas.cuti.Harilibur;
import id.go.bpjskesehatan.entitas.cuti.KuotaDetil;
import id.go.bpjskesehatan.entitas.cuti.Tipe;
import id.go.bpjskesehatan.util.SharedMethod;
import id.go.bpjskesehatan.util.Utils;

@Path("/cuti")
public class CutiRest {
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
		case "tipe":
			obj = new Tipe();
			break;
		case "harilibur":
			obj = new Harilibur();
			break;
		case "cuti":
			obj = new Cuti();
			break;
		case "kuotadetil":
			obj = new KuotaDetil();
			break;
		default:
			return Response.status(Status.NOT_FOUND).build();
		}
		query = "exec cuti." + namasp + " ?, ?, ?, ?, ?";
		return RestMethod.getListData(headers, page, row, namaentitas, obj, query, data);
	}
	
	@POST
	@Path("/kuota/list/{tahun}/{kodeoffice}/{start}/{limit}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response SettingListKuota(@Context HttpHeaders headers,
			@PathParam("tahun") Integer tahun,
			@PathParam("kodeoffice") String kodeoffice,
			@PathParam("start") Integer start, 
			@PathParam("limit") Integer limit,
			String data) {
		
		Map<String, Object> response = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> metadata = new HashMap<String, Object>();
		List<Map<String, Object>> list = null;
		
		Connection con = null;
		ResultSet rs = null;
		CallableStatement cs = null;
		String query = null;
		
		String order = null;
		String filter = null;

		if (SharedMethod.ServiceAuth(headers, metadata)) {
		//if (true) {
			try {
				JsonNode json = null;
				if (data != null) {
					if (!data.isEmpty()) {
						ObjectMapper mapper = new ObjectMapper();
						json = mapper.readTree(data);

						order = json.path("sort").isMissingNode() ? null
								: SharedMethod.getSortedColumn(mapper.writeValueAsString(json.path("sort")));

						filter = json.path("filter").isMissingNode() ? null
								: SharedMethod.getFilteredColumn(mapper.writeValueAsString(json.path("filter")), null);
						
					}
				}
				
				query = "exec cuti.sp_listkuota ?, ?, ?, ?, ?, ?, ?";
				try {
					con = new Koneksi().getConnection();
					cs = con.prepareCall(query);
					cs.setInt(1, tahun);
					cs.setString(2, kodeoffice);
					cs.setInt(3, start);
					cs.setInt(4, limit);
					cs.setInt(5, 1);
					cs.setString(6, order);
					cs.setString(7, filter);
					rs = cs.executeQuery();
					if(rs.next()) {
						metadata.put("rowcount", rs.getInt("jumlah"));
					}
				}
				finally {
					if (rs != null) {
						try {
							rs.close();
						} catch (SQLException e) {
						}
					}
					if (cs != null) {
						try {
							cs.close();
						} catch (SQLException e) {
						}
					}
					if (con != null) {
						try {
							con.close();
						} catch (SQLException e) {
						}
					}
				}
				
				try {
					con = new Koneksi().getConnection();
					cs = con.prepareCall(query);
					cs.setInt(1, tahun);
					cs.setString(2, kodeoffice);
					cs.setInt(3, start);
					cs.setInt(4, limit);
					cs.setInt(5, 0);
					cs.setString(6, order);
					cs.setString(7, filter);
					rs = cs.executeQuery();
					
					list = new ArrayList<Map<String, Object>>();
					ResultSetMetaData metaData = rs.getMetaData();
					Map<String, Object> hasil = null;
					metadata.put("code", 0);
					metadata.put("message", "Data kosong");
					while (rs.next()) {
						hasil = new HashMap<String, Object>();
						for (int i = 1; i <= metaData.getColumnCount(); i++) {
							//hasil.put(metaData.getColumnName(i).toLowerCase(), rs.getObject(i));
							if(metaData.getColumnTypeName(i).equalsIgnoreCase("date")){
								hasil.put(metaData.getColumnName(i).toLowerCase(), Utils.SqlDateToSqlString(rs.getDate(i)));
							}
							else {
								hasil.put(metaData.getColumnName(i).toLowerCase(), rs.getObject(i));
							}	
						}
						list.add(hasil);
						metadata.put("code", 1);
						metadata.put("message", "OK");
					}
					response.put("list", list);
					result.put("response", response);
				}
				finally {
					if (rs != null) {
						try {
							rs.close();
						} catch (SQLException e) {
						}
					}
					if (cs != null) {
						try {
							cs.close();
						} catch (SQLException e) {
						}
					}
					if (con != null) {
						try {
							con.close();
						} catch (SQLException e) {
						}
					}
				}
			} catch (Exception e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				e.printStackTrace();
			}
		}
		result.put("metadata", metadata);
		return Response.ok(result).build();
	}

	@POST
	@Path("/{servicename}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response createData(@Context HttpHeaders headers, @PathParam("servicename") String servicename,
			String data) {

		Object obj = null;
		String namatabel = null;
		namatabel = "cuti." + servicename;
		switch (servicename) {
		case "tipe":
			obj = new Tipe();
			break;
		case "harilibur":
			obj = new Harilibur();
			break;
		case "cuti":
			obj = new Cuti();
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
		namatabel = "cuti." + servicename;
		switch (servicename) {
		case "tipe":
			obj = new Tipe();
			break;
		case "harilibur":
			obj = new Harilibur();
			break;
		case "cuti":
			obj = new Cuti();
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
		namatabel = "cuti." + servicename;
		switch (servicename) {
		case "tipe":
			obj = new Tipe();
			break;
		case "harilibur":
			obj = new Harilibur();
			break;
		case "cuti":
			obj = new Cuti();
			break;
		default:
			return Response.status(Status.NOT_FOUND).build();
		}
		return RestMethod.deleteData(headers, data, obj, namatabel);
	}
}
