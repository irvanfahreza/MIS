package id.go.bpjskesehatan.service.absensi;

import java.sql.Connection;
import java.sql.PreparedStatement;
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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import id.go.bpjskesehatan.database.KoneksiAbsensi;
import id.go.bpjskesehatan.entitas.Metadata;
import id.go.bpjskesehatan.entitas.Result;
import id.go.bpjskesehatan.util.SharedMethod;
import id.go.bpjskesehatan.util.Utils;

@Path("/absensi/action")
public class ActionRest {	
	
	@POST
	@Path("/create/{servicename}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response createData(@Context HttpHeaders headers, @PathParam("servicename") String servicename,
			String data) {
		Result<Object> result = new Result<Object>();
		Metadata metadata = new Metadata();
		if (SharedMethod.ServiceAuth(headers, metadata)) {
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
		if (SharedMethod.ServiceAuth(headers, metadata)) {
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
		if (SharedMethod.ServiceAuth(headers, metadata)) {
			metadata = RestMethod.deleteData(data, servicename);
		}
		result.setMetadata(metadata);
		return Response.ok(result).build();
	}
	
	@POST
	@Path("/query")
	@Consumes("application/json")
	@Produces("application/json")
	public Response Query(@Context HttpHeaders headers, @PathParam("servicename") String servicename,
			@PathParam("page") String page, @PathParam("row") String row, String data) {
		Map<String, Object> response = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> listdata = null;
		Map<String, Object> metadata = new HashMap<String, Object>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = null;
		Boolean isResult;

		if (SharedMethod.ServiceAuth(headers, metadata)) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				JsonNode json = mapper.readTree(data);
				query = json.path("query").asText();
				
				con = new KoneksiAbsensi().getConnection();
				ps = con.prepareStatement(query);
				isResult = ps.execute();
				
				metadata.put("code", 0);
				metadata.put("message", "Gagal");
				if(isResult) {
					metadata.put("code", 0);
					metadata.put("message", "Data kosong");
					rs = ps.getResultSet();
					ResultSetMetaData metaData = rs.getMetaData();
					listdata = new ArrayList<Map<String, Object>>();
					Map<String, Object> hasil = null;
					while (rs.next()) {
						hasil = new HashMap<String, Object>();
						for (int i = 1; i <= metaData.getColumnCount(); i++) {
							if(rs.getObject(i)!=null && metaData.getColumnTypeName(i).equalsIgnoreCase("date")){
								hasil.put(metaData.getColumnName(i).toLowerCase(), Utils.SqlDateToSqlString(rs.getDate(i)));
							}
							else {
								hasil.put(metaData.getColumnName(i).toLowerCase(), rs.getObject(i));
							}
						}
						listdata.add(hasil);
						metadata.put("code", 1);
						metadata.put("message", "OK");
					}
					response.put("list", listdata);
					result.put("response", response);
				}
				else {
					metadata.put("code", 1);
					metadata.put("message", "Ok");
				}
			} catch (Exception e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				e.printStackTrace();
			} finally {
				if (rs != null) {
					try {
						rs.close();
					} catch (SQLException e) {
					}
				}
				if (ps != null) {
					try {
						ps.close();
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
		}
		result.put("metadata", metadata);
		return Response.ok(result).build();
	}
	
}
