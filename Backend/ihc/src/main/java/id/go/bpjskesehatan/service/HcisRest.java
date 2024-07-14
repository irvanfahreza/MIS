package id.go.bpjskesehatan.service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import id.go.bpjskesehatan.database.Koneksi;
import id.go.bpjskesehatan.entitas.hcis.Notifikasi;
import id.go.bpjskesehatan.service.v2.cuti.Cuti2Utils;
import id.go.bpjskesehatan.service.v2.lembur.Lembur2Utils;
import id.go.bpjskesehatan.service.v2.skpd.Skpd2Utils;
import id.go.bpjskesehatan.util.SharedMethod;
import id.go.bpjskesehatan.util.Utils;

@Path("/hcis")
public class HcisRest {
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
		case "notifikasi":
			obj = new Notifikasi();
			break;

		default:
			return Response.status(Status.NOT_FOUND).build();
		}
		query = "exec hcis." + namasp + " ?, ?, ?, ?, ?";
		return RestMethod.getListData(headers, page, row, namaentitas, obj, query, data);
	}

	@POST
	@Path("/{servicename}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response createData(@Context HttpHeaders headers, @PathParam("servicename") String servicename,
			String data) {

		return RestMethod.createData(headers, data, servicename);
	}

	@PUT
	@Path("/{servicename}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response updateData(@Context HttpHeaders headers, @PathParam("servicename") String servicename,
			String data) {
		return RestMethod.updateData(headers, data, servicename);
	}

	@POST
	@Path("/{servicename}/delete")
	@Consumes("application/json")
	@Produces("application/json")
	public Response deleteData(@Context HttpHeaders headers, @PathParam("servicename") String servicename,
			String data) {

		return RestMethod.deleteData(headers, data, servicename);
	}

	@POST
	@Path("/view/{servicename}/list/{page}/{row}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response ListViewService(@Context HttpHeaders headers, @PathParam("servicename") String servicename,
			@PathParam("page") String page, @PathParam("row") String row, String data) {
		Map<String, Object> response = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> listdata = null;
		Map<String, Object> metadata = new HashMap<String, Object>();
		Connection con = null;
		ResultSet rs = null;
		Statement cs = null;
		String order = null;
		String filter = null;
		String query = null;
		int rowlimit = Integer.parseInt(page) + Integer.parseInt(row) - 1;

		if (SharedMethod.ServiceAuth(headers, metadata)) {
			try {
				if (data != null) {
					if (!data.isEmpty()) {
						ObjectMapper mapper = new ObjectMapper();
						JsonNode json = mapper.readTree(data);

						order = json.path("sort").isMissingNode() ? null
								: SharedMethod.getSortedColumn(mapper.writeValueAsString(json.path("sort")));

						filter = json.path("filter").isMissingNode() ? null
								: SharedMethod.getFilteredColumn(mapper.writeValueAsString(json.path("filter")), null);

					}
				}
				query = "select count(*) as jumlah from " + servicename;
				if (filter != null)
					query = query.concat(" where " + filter);
				con = new Koneksi().getConnection();
				cs = con.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
				rs = cs.executeQuery(query);
				metadata.put("code", 2);
				metadata.put("message", Response.Status.NO_CONTENT.toString());
				metadata.put("rowcount", 0);

				if (rs.next()) {
					metadata.put("rowcount", rs.getInt("jumlah"));
				}

				listdata = new ArrayList<Map<String, Object>>();

				rs.close();
				rs = cs.executeQuery("select top 1 * from " + servicename);
				ResultSetMetaData metaData = rs.getMetaData();
				String key = null;
				if (rs.next())
					key = metaData.getColumnName(1);

				if (order != null)
					key = order;

				if (filter == null)
					query = "select * from ( " + "select ROW_NUMBER() OVER (ORDER BY " + key + ") AS RowNumber, * "
							+ "from " + servicename + ") a where a.RowNumber between " + page + " and " + rowlimit;
				else
					query = "select * from ( " + "select ROW_NUMBER() OVER (ORDER BY " + key + ") AS RowNumber, * "
							+ "from " + servicename + " " + "where " + filter + ") a where a.RowNumber between " + page
							+ " and " + rowlimit;

				rs.close();
				rs = cs.executeQuery(query);
				metaData = rs.getMetaData();
				Map<String, Object> hasil = null;

				while (rs.next()) {
					hasil = new HashMap<String, Object>();
					for (int i = 1; i <= metaData.getColumnCount(); i++) {
						if (rs.getObject(i) != null){
							if(metaData.getColumnTypeName(i).equalsIgnoreCase("date")){
								hasil.put(metaData.getColumnName(i).toLowerCase(), Utils.SqlDateToSqlString(rs.getDate(i)));
							}
							else {
								hasil.put(metaData.getColumnName(i).toLowerCase(), rs.getObject(i));
							}
						}
					}
					listdata.add(hasil);
					metadata.put("code", 1);
					metadata.put("message", "OK");
				}
				response.put("listdata", listdata);
				result.put("response", response);
				rs.close();

			} catch (SQLException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
			} catch (NamingException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				e.printStackTrace();
			} catch (SecurityException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				e.printStackTrace();
			} catch (InstantiationException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				e.printStackTrace();
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

		}
		result.put("metadata", metadata);
		return Response.ok(result).build();

	}

	@POST
	@Path("/sp/{servicename}/list/{page}/{row}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response ListSPService(@Context HttpHeaders headers, @PathParam("servicename") String servicename,
			@PathParam("page") String page, @PathParam("row") String row, String data) {
		Map<String, Object> response = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> listdata = null;
		Map<String, Object> metadata = new HashMap<String, Object>();
		Connection con = null;
		ResultSet rs = null;
		CallableStatement cs = null;
		String order = null;
		String filter = null;
		String query = null;

		if (SharedMethod.ServiceAuth(headers, metadata)) {
			try {
				if (data != null) {
					if (!data.isEmpty()) {
						ObjectMapper mapper = new ObjectMapper();
						JsonNode json = mapper.readTree(data);

						order = json.path("sort").isMissingNode() ? null
								: SharedMethod.getSortedColumn(mapper.writeValueAsString(json.path("sort")));

						filter = json.path("filter").isMissingNode() ? null
								: SharedMethod.getFilteredColumn(mapper.writeValueAsString(json.path("filter")), null);

					}
				}
				query = "exec " + servicename + " ?, ?, ?, ?, ?";
				// if (filter != null)
				// query = query.concat(" where " + filter);
				con = new Koneksi().getConnection();
				cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
				cs.setInt(1, Integer.parseInt(page));
				cs.setInt(2, Integer.parseInt(row));
				cs.setInt(3, 1);
				cs.setString(4, order);
				cs.setString(5, filter);
				rs = cs.executeQuery();
				metadata.put("code", 2);
				metadata.put("message", Response.Status.NO_CONTENT.toString());
				metadata.put("rowcount", 0);

				if (rs.next()) {
					metadata.put("rowcount", rs.getInt("jumlah"));
				}

				listdata = new ArrayList<Map<String, Object>>();

				rs.close();
				cs.setInt(1, Integer.parseInt(page));
				cs.setInt(2, Integer.parseInt(row));
				cs.setInt(3, 0);
				cs.setString(4, order);
				cs.setString(5, filter);
				rs = cs.executeQuery();
				ResultSetMetaData metaData = rs.getMetaData();
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
				response.put("listdata", listdata);
				result.put("response", response);
				rs.close();

			} catch (SQLException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
			} catch (NamingException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				e.printStackTrace();
			} catch (SecurityException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				e.printStackTrace();
			} catch (InstantiationException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				e.printStackTrace();
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

		}
		result.put("metadata", metadata);
		return Response.ok(result).build();

	}

	@POST
	@Path("/sp2/{servicename}/list/{page}/{row}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response ListSPServiceCustom(@Context HttpHeaders headers, @PathParam("servicename") String servicename,
			@PathParam("page") String page, @PathParam("row") String row, String data) {
		Map<String, Object> response = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> listdata = null;
		Map<String, Object> metadata = new HashMap<String, Object>();
		Connection con = null;
		ResultSet rs = null;
		CallableStatement cs = null;
		String order = null;
		String filter = null;
		String filter2 = null;
		String filter3 = null;
		String query = null;

		if (SharedMethod.ServiceAuth(headers, metadata)) {
			try {
				if (data != null) {
					if (!data.isEmpty()) {
						ObjectMapper mapper = new ObjectMapper();
						JsonNode json = mapper.readTree(data);

						order = json.path("sort").isMissingNode() ? null
								: SharedMethod.getSortedColumn(mapper.writeValueAsString(json.path("sort")));

						filter = json.path("filter").isMissingNode() ? null
								: SharedMethod.getFilteredColumn(mapper.writeValueAsString(json.path("filter")), null);

						filter2 = json.path("filter2").isMissingNode() ? null
								: SharedMethod.getFilteredColumn(mapper.writeValueAsString(json.path("filter2")), null);

						filter3 = json.path("filter3").isMissingNode() ? null
								: SharedMethod.getFilteredColumn(mapper.writeValueAsString(json.path("filter3")), null);

					}
				}
				query = "exec " + servicename + " ?, ?, ?, ?, ?, ?, ?";
				// if (filter != null)
				// query = query.concat(" where " + filter);
				con = new Koneksi().getConnection();
				cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
				cs.setInt(1, Integer.parseInt(page));
				cs.setInt(2, Integer.parseInt(row));
				cs.setInt(3, 1);
				cs.setString(4, order);
				cs.setString(5, filter);
				cs.setString(6, filter2);
				cs.setString(7, filter3);
				rs = cs.executeQuery();
				metadata.put("code", 2);
				metadata.put("message", Response.Status.NO_CONTENT.toString());
				metadata.put("rowcount", 0);

				if (rs.next()) {
					metadata.put("rowcount", rs.getInt("jumlah"));
				}

				listdata = new ArrayList<Map<String, Object>>();

				rs.close();
				cs.setInt(1, Integer.parseInt(page));
				cs.setInt(2, Integer.parseInt(row));
				cs.setInt(3, 0);
				cs.setString(4, order);
				cs.setString(5, filter);
				cs.setString(6, filter2);
				cs.setString(7, filter3);
				rs = cs.executeQuery();
				ResultSetMetaData metaData = rs.getMetaData();
				Map<String, Object> hasil = null;

				while (rs.next()) {
					hasil = new HashMap<String, Object>();
					for (int i = 1; i <= metaData.getColumnCount(); i++) {
						if (rs.getObject(i) != null){
							if(metaData.getColumnTypeName(i).equalsIgnoreCase("date")){
								hasil.put(metaData.getColumnName(i).toLowerCase(), Utils.SqlDateToSqlString(rs.getDate(i)));
							}
							else {
								hasil.put(metaData.getColumnName(i).toLowerCase(), rs.getObject(i));
							}
						}
					}
					listdata.add(hasil);
					metadata.put("code", 1);
					metadata.put("message", "OK");
				}
				response.put("listdata", listdata);
				result.put("response", response);
				rs.close();

			} catch (SQLException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
			} catch (NamingException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				e.printStackTrace();
			} catch (SecurityException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				e.printStackTrace();
			} catch (InstantiationException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				e.printStackTrace();
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

		}
		result.put("metadata", metadata);
		return Response.ok(result).build();

	}
	
	@POST
	@Path("/sp3/{servicename}/list/{page}/{row}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response ListSPServiceCustom3(@Context HttpHeaders headers, @PathParam("servicename") String servicename,
			@PathParam("page") String page, @PathParam("row") String row, String data) {
		Map<String, Object> response = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> listdata = null;
		Map<String, Object> metadata = new HashMap<String, Object>();
		Connection con = null;
		ResultSet rs = null;
		CallableStatement cs = null;
		String order = null;
		String filter = null;
		String filter2 = null;
		String filter3 = null;
		String filter4 = null;
		String query = null;

		if (SharedMethod.ServiceAuth(headers, metadata)) {
			try {
				if (data != null) {
					if (!data.isEmpty()) {
						ObjectMapper mapper = new ObjectMapper();
						JsonNode json = mapper.readTree(data);

						order = json.path("sort").isMissingNode() ? null
								: SharedMethod.getSortedColumn(mapper.writeValueAsString(json.path("sort")));

						filter = json.path("filter").isMissingNode() ? null
								: SharedMethod.getFilteredColumn(mapper.writeValueAsString(json.path("filter")), null);

						filter2 = json.path("filter2").isMissingNode() ? null
								: SharedMethod.getFilteredColumn(mapper.writeValueAsString(json.path("filter2")), null);

						filter3 = json.path("filter3").isMissingNode() ? null
								: SharedMethod.getFilteredColumn(mapper.writeValueAsString(json.path("filter3")), null);
						
						filter4 = json.path("filter4").isMissingNode() ? null
								: SharedMethod.getFilteredColumn(mapper.writeValueAsString(json.path("filter4")), null);

					}
				}
				query = "exec " + servicename + " ?, ?, ?, ?, ?, ?, ?, ?";
				// if (filter != null)
				// query = query.concat(" where " + filter);
				con = new Koneksi().getConnection();
				cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
				cs.setInt(1, Integer.parseInt(page));
				cs.setInt(2, Integer.parseInt(row));
				cs.setInt(3, 1);
				cs.setString(4, order);
				cs.setString(5, filter);
				cs.setString(6, filter2);
				cs.setString(7, filter3);
				cs.setString(8, filter4);
				rs = cs.executeQuery();
				metadata.put("code", 2);
				metadata.put("message", Response.Status.NO_CONTENT.toString());
				metadata.put("rowcount", 0);

				if (rs.next()) {
					metadata.put("rowcount", rs.getInt("jumlah"));
				}

				listdata = new ArrayList<Map<String, Object>>();

				rs.close();
				cs.setInt(1, Integer.parseInt(page));
				cs.setInt(2, Integer.parseInt(row));
				cs.setInt(3, 0);
				cs.setString(4, order);
				cs.setString(5, filter);
				cs.setString(6, filter2);
				cs.setString(7, filter3);
				rs = cs.executeQuery();
				ResultSetMetaData metaData = rs.getMetaData();
				Map<String, Object> hasil = null;

				while (rs.next()) {
					hasil = new HashMap<String, Object>();
					for (int i = 1; i <= metaData.getColumnCount(); i++) {
						if (rs.getObject(i) != null){
							if(metaData.getColumnTypeName(i).equalsIgnoreCase("date")){
								hasil.put(metaData.getColumnName(i).toLowerCase(), Utils.SqlDateToSqlString(rs.getDate(i)));
							}
							else {
								hasil.put(metaData.getColumnName(i).toLowerCase(), rs.getObject(i));
							}
						}
					}
					listdata.add(hasil);
					metadata.put("code", 1);
					metadata.put("message", "OK");
				}
				response.put("listdata", listdata);
				result.put("response", response);
				rs.close();

			} catch (SQLException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
			} catch (NamingException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				e.printStackTrace();
			} catch (SecurityException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				e.printStackTrace();
			} catch (InstantiationException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				e.printStackTrace();
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

		}
		result.put("metadata", metadata);
		return Response.ok(result).build();

	}

	@POST
	@Path("/sp/hitung/{servicename}/{paramcount}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response ListSPService(@Context HttpHeaders headers, @PathParam("servicename") String servicename,
			@PathParam("paramcount") String paramcount, String data) {
		// Map<String, Object> response = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		// List<Map<String, Object>> listdata = null;
		Map<String, Object> metadata = new HashMap<String, Object>();
		Connection con = null;
		// ResultSet rs = null;
		CallableStatement cs = null;
		String query = null;

		if (SharedMethod.ServiceAuth(headers, metadata)) {
			try {
				con = new Koneksi().getConnection();

				query = "exec " + servicename + " ";
				for (int i = 0; i < Integer.parseInt(paramcount); i++) {
					query = query.concat("?,");
				}
				query = query.substring(0, query.length() - 1);
				cs = con.prepareCall(query);
				if (data != null) {
					if (!data.isEmpty()) {
						ObjectMapper mapper = new ObjectMapper();
						JsonNode json = mapper.readTree(data);

						Iterator<?> keys = json.fieldNames();
						String value;

						int i = 1;
						while (keys.hasNext()) {
							String key = (String) keys.next();
							value = json.path(key).asText();
							cs.setString(i++, value);
							// System.out.println(key + ": " + value);
						}
					}
				}

				cs.execute();
				// System.out.println(query);

				metadata.put("code", 1);
				metadata.put("message", "Proses selesai.");

			} catch (SQLException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
			} catch (NamingException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				e.printStackTrace();
			} catch (JsonProcessingException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				e.printStackTrace();
			} catch (IOException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				e.printStackTrace();
			} finally {
				// if (rs != null) {
				// try {
				// rs.close();
				// } catch (SQLException e) {
				// }
				// }

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

		}
		result.put("metadata", metadata);
		return Response.ok(result).build();

	}
	
	@POST
	@Path("/sp/exec/{servicename}/{paramcount}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response ListExecSPService(@Context HttpHeaders headers, @PathParam("servicename") String servicename,
			@PathParam("paramcount") String paramcount, String data) {
		Map<String, Object> response = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> listdata = null;
		Map<String, Object> metadata = new HashMap<String, Object>();
		Connection con = null;
		// ResultSet rs = null;
		CallableStatement cs = null;
		String query = null;
		
		ResultSet rs = null;

		if (SharedMethod.ServiceAuth(headers, metadata)) {
			try {
				con = new Koneksi().getConnection();

				query = "exec " + servicename + " ";
				for (int i = 0; i < Integer.parseInt(paramcount); i++) {
					query = query.concat("?,");
				}
				query = query.substring(0, query.length() - 1);
				cs = con.prepareCall(query);
				if (data != null) {
					if (!data.isEmpty()) {
						ObjectMapper mapper = new ObjectMapper();
						JsonNode json = mapper.readTree(data);

						Iterator<?> keys = json.fieldNames();
						String value;

						int i = 1;
						while (keys.hasNext()) {
							String key = (String) keys.next();
							value = json.path(key).asText();
							cs.setString(i++, value);
							// System.out.println(key + ": " + value);
						}
					}
				}

				//cs.executeUpdate();
				// System.out.println(query);
				rs = cs.executeQuery();
				ResultSetMetaData metaData = rs.getMetaData();
				Map<String, Object> hasil = null;
				listdata = new ArrayList<Map<String, Object>>();
				int count=0;
				metadata.put("code", 2);
				metadata.put("message", "Data kosong");
				while (rs.next()) {
					count++;
					hasil = new HashMap<String, Object>();
					for (int i = 1; i <= metaData.getColumnCount(); i++) {
						if (rs.getObject(i) != null){
							if(metaData.getColumnTypeName(i).equalsIgnoreCase("date")){
								hasil.put(metaData.getColumnName(i).toLowerCase(), Utils.SqlDateToSqlString(rs.getDate(i)));
							}
							else {
								hasil.put(metaData.getColumnName(i).toLowerCase(), rs.getObject(i));
							}
						}
					}
					listdata.add(hasil);
					metadata.put("code", 1);
					metadata.put("message", "OK");
				}
				metadata.put("rowcount", count);
				response.put("list", listdata);
				result.put("response", response);
				rs.close();

			} catch (SQLException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
			} catch (NamingException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				e.printStackTrace();
			} catch (JsonProcessingException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				e.printStackTrace();
			} catch (IOException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				e.printStackTrace();
			} finally {
				// if (rs != null) {
				// try {
				// rs.close();
				// } catch (SQLException e) {
				// }
				// }

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

		}
		result.put("metadata", metadata);
		return Response.ok(result).build();

	}
	
	@POST
	@Path("/sp/execute/{servicename}/{paramcount}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response ListExecSP(@Context HttpHeaders headers, @PathParam("servicename") String servicename,
			@PathParam("paramcount") String paramcount, String data) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> metadata = new HashMap<String, Object>();
		Connection con = null;
		// ResultSet rs = null;
		CallableStatement cs = null;
		String query = null;
		
		ResultSet rs = null;

		if (SharedMethod.ServiceAuth(headers, metadata)) {
			try {
				con = new Koneksi().getConnection();

				query = "exec " + servicename + " ";
				for (int i = 0; i < Integer.parseInt(paramcount); i++) {
					query = query.concat("?,");
				}
				query = query.substring(0, query.length() - 1);
				cs = con.prepareCall(query);
				if (data != null) {
					if (!data.isEmpty()) {
						ObjectMapper mapper = new ObjectMapper();
						JsonNode json = mapper.readTree(data);

						Iterator<?> keys = json.fieldNames();
						String value;

						int i = 1;
						while (keys.hasNext()) {
							String key = (String) keys.next();
							value = json.path(key).asText();
							cs.setString(i++, value);
							// System.out.println(key + ": " + value);
						}
					}
				}
				
				//cs.executeUpdate();
				// System.out.println(query);
				boolean results = cs.execute();
				//rs = cs.executeQuery();
				
				ResultSetMetaData metaData;
				
				metadata.put("code", 1);
				metadata.put("message", "Proses selesai.");
				
				do {
			        if(results) {
			           rs = cs.getResultSet();
			           metaData = rs.getMetaData();
			           while (rs.next()) {
			        	   for (int i = 1; i <= metaData.getColumnCount(); i++) {
			        		   if(metaData.getColumnName(i).equalsIgnoreCase("code")) {
			        			   metadata.put("code", rs.getObject(i));
			        		   }
			        		   
			        		   if(metaData.getColumnName(i).equalsIgnoreCase("message")) {
			        			   metadata.put("message", rs.getObject(i));
			        		   }
			        		   
			        		   if(metaData.getColumnName(i).equalsIgnoreCase("kode")) {
			        			   metadata.put("kode", rs.getObject(i));
			        		   }
			        		   
							}
			           }
			           rs.close();
			        }
			        
			        results = cs.getMoreResults();
				} while(results);
				//rs.close();
				cs.close();
			      
				
				/*if (rs.next()) {
					metadata.put("code", rs.getInt("code"));
					metadata.put("message", rs.getString("message"));
					rs.close();
					cs.close();
				} else {
					metadata.put("code", 0);
					metadata.put("message", "Error.");
				}*/

			} catch (SQLException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
			} catch (NamingException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				e.printStackTrace();
			} catch (JsonProcessingException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				e.printStackTrace();
			} catch (IOException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				e.printStackTrace();
			} finally {
				// if (rs != null) {
				// try {
				// rs.close();
				// } catch (SQLException e) {
				// }
				// }

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

		}
		result.put("metadata", metadata);
		return Response.ok(result).build();

	}
	
	@POST
	@Path("/pegawai/notifikasi/list")
	@Consumes("application/json")
	@Produces("application/json")
	public Response ListNotifikasi(@Context HttpHeaders headers, String data) {
		Map<String, Object> response = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> listdata = null;
		Map<String, Object> metadata = new HashMap<String, Object>();
		Connection con = null;
		// ResultSet rs = null;
		CallableStatement cs = null;
		String query = null;
		
		ResultSet rs = null;

		if (SharedMethod.ServiceAuth(headers, metadata)) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				JsonNode json = mapper.readTree(data);
				String a = json.path("a").asText();
				String b = json.path("b").asText();
				String c = json.path("c").asText();
				Integer d = json.path("d").asInt();
				Integer e = json.path("e").asInt();
				Integer f = json.path("f").asInt();
				
				query = "exec hcis.sp_listnotifikasiperpegawai ?,?,?,?,?,?,?";
				try {
					con = new Koneksi().getConnection();
					cs = con.prepareCall(query);
					cs.setString(1, a);
					cs.setString(2, b);
					cs.setString(3, c);
					cs.setInt(4, d);
					cs.setInt(5, e);
					cs.setInt(6, f);
					cs.setInt(7, 1);
					rs = cs.executeQuery();
					if(rs.next()) {
						metadata.put("rowcount", rs.getInt("row_count"));
					}
				} 
				finally {
					if (rs != null) {
						 try {
							 rs.close();
						 } catch (SQLException e1) {
						 }
					}
					if (cs != null) {
						try {
							cs.close();
						} catch (SQLException e1) {
						}
					}

					if (con != null) {
						try {
							con.close();
						} catch (SQLException e1) {
						}
					}
				}
				
				try {
					con = new Koneksi().getConnection();
					cs = con.prepareCall(query);
					cs.setString(1, a);
					cs.setString(2, b);
					cs.setString(3, c);
					cs.setInt(4, d);
					cs.setInt(5, e);
					cs.setInt(6, f);
					cs.setInt(7, 0);
					rs = cs.executeQuery();
					
					ResultSetMetaData metaData = rs.getMetaData();
					Map<String, Object> hasil = null;
					listdata = new ArrayList<Map<String, Object>>();
					metadata.put("code", 2);
					metadata.put("message", "Data kosong");
					while(rs.next()) {
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
				finally {
					if (rs != null) {
						 try {
							 rs.close();
						 } catch (SQLException e1) {
						 }
					}
					if (cs != null) {
						try {
							cs.close();
						} catch (SQLException e1) {
						}
					}

					if (con != null) {
						try {
							con.close();
						} catch (SQLException e1) {
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
	
	public static Map<String, Object> getRowDetil(Integer tipe, Integer fkode) throws SQLException, NamingException {
		Connection con = null;
		CallableStatement cs = null;
		String query = null;
		ResultSet rs = null;
		try {
			switch (tipe) {
			case 1:
				query = "exec kinerja.sp_getpesertabykode ?"; // kodepeserta
				break;
			case 2:
			case 3:
			case 8:
				query = "exec kinerja.sp_getpesertabykodeperencanaan ?"; // kodeperencanaan
				break;
			case 4:
			case 5:
				query = "exec kinerja.sp_getpesertabykodepembinaan ?"; // kodepembinaan
				break;
			case 6:
				query = "exec kinerja.sp_getnotifikasi_umpanbalikkomitmen ?";
				break;
			case 7:
				query = "exec kinerja.sp_getnotifikasi_umpanbalikkompetensi ?";
				break;
			case 97:
				query = "exec karyawan.sp_listpenugasanall ?,?,?,?,?";
				break;
			default:
				return null;
			}
			
			con = new Koneksi().getConnection();
			cs = con.prepareCall(query);
			switch (tipe) {
			case 1:
				cs.setInt(1, fkode);
				break;
			case 2:
			case 3:
			case 8:
				cs.setInt(1, fkode);
				break;
			case 4:
			case 5:
				cs.setInt(1, fkode);
				break;
			case 6:
			case 7:
				cs.setInt(1, fkode);
				break;
			case 97:
				cs.setInt(1, 1);
				cs.setInt(2, 1);
				cs.setInt(3, 0);
				cs.setNull(4, java.sql.Types.VARCHAR);
				cs.setString(5, String.format("kode=%d and isnonbagan=0", fkode));
				break;
			default:
				cs.setInt(1, 1);
				cs.setInt(2, 1);
				cs.setInt(3, 0);
				cs.setNull(4, java.sql.Types.VARCHAR);
				cs.setString(5, String.format("kode=%d", fkode));
				break;
			}
			rs = cs.executeQuery();
			ResultSetMetaData metaData = rs.getMetaData();
			Map<String, Object> hasil = null;
			if(rs.next()) {
				hasil = new HashMap<String, Object>();
				for (int i = 1; i <= metaData.getColumnCount(); i++) {
					if(rs.getObject(i)!=null && metaData.getColumnTypeName(i).equalsIgnoreCase("date")){
						hasil.put(metaData.getColumnName(i).toLowerCase(), Utils.SqlDateToSqlString(rs.getDate(i)));
					}
					else {
						hasil.put(metaData.getColumnName(i).toLowerCase(), rs.getObject(i));
					}
				}
				return hasil;
			}
		} 
		finally {
			if (rs != null) {
				 try {
					 rs.close();
				 } catch (SQLException e1) {
				 }
			}
			if (cs != null) {
				try {
					cs.close();
				} catch (SQLException e1) {
				}
			}

			if (con != null) {
				try {
					con.close();
				} catch (SQLException e1) {
				}
			}
		}
		return null;
	}
	
	private static List<Map<String, Object>> getNotesDetil(Integer tipe, Integer fkode) throws SQLException, NamingException {
		Connection con = null;
		CallableStatement cs = null;
		String query = null;
		ResultSet rs = null;
		try {
			switch (tipe) {
			case 99:
				query = "exec skpd.sp_getcatatannotifikasi ?";
				break;
			case 96:
				query = "exec lembur.sp_getcatatannotifikasi ?";
				break;
			default:
				return null;
			}
			
			con = new Koneksi().getConnection();
			cs = con.prepareCall(query);
			cs.setInt(1, fkode);
			rs = cs.executeQuery();
			ResultSetMetaData metaData = rs.getMetaData();
			List<Map<String, Object>> listdata = new ArrayList<Map<String, Object>>();;
			Map<String, Object> hasil = null;
			while(rs.next()) {
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
			}
			return listdata;
		} 
		finally {
			if (rs != null) {
				 try {
					 rs.close();
				 } catch (SQLException e1) {
				 }
			}
			if (cs != null) {
				try {
					cs.close();
				} catch (SQLException e1) {
				}
			}

			if (con != null) {
				try {
					con.close();
				} catch (SQLException e1) {
				}
			}
		}
	}
	
	@POST
	@Path("/pegawai/detilnotifikasi/list")
	@Consumes("application/json")
	@Produces("application/json")
	public Response ListDetilNotifikasi(@Context HttpHeaders headers, String data) {
		Map<String, Object> response = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> metadata = new HashMap<String, Object>();
		Connection con = null;
		CallableStatement cs = null;
		String query = null;
		ResultSet rs = null;

		if (SharedMethod.ServiceAuth(headers, metadata)) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				JsonNode json = mapper.readTree(data);
				Integer kode = json.path("kode").asInt();
				Integer tipe = json.path("tipe").asInt();
				Integer fkode = json.path("fkode").asInt();
				//String lnpp = json.path("lnpp").asText();
				//String tokodejobtitle = json.path("tokodejobtitle").asText();
				//String tokodeoffice = json.path("tokodeoffice").asText();
				
				try {
					con = new Koneksi().getConnection();
					query = "exec hcis.sp_listnotifikasi ?,?,?,?,?";
					cs = con.prepareCall(query);
					cs.setInt(1, 1);
					cs.setInt(2, 1);
					cs.setInt(3, 0);
					cs.setNull(4, java.sql.Types.VARCHAR);
					cs.setString(5, String.format("kode = %d", kode));
					rs = cs.executeQuery();
					
					ResultSetMetaData metaData = rs.getMetaData();
					Map<String, Object> hasil = null;
					metadata.put("code", 2);
					metadata.put("message", "Data kosong");
					if(rs.next()) {
						hasil = new HashMap<String, Object>();
						for (int i = 1; i <= metaData.getColumnCount(); i++) {
							if(rs.getObject(i)!=null && metaData.getColumnTypeName(i).equalsIgnoreCase("date")){
								hasil.put(metaData.getColumnName(i).toLowerCase(), Utils.SqlDateToSqlString(rs.getDate(i)));
							}
							else {
								hasil.put(metaData.getColumnName(i).toLowerCase(), rs.getObject(i));
							}
						}
						response.put("notifikasi", hasil);
						result.put("response", response);
						metadata.put("code", 1);
						metadata.put("message", "OK");
					}
				} 
				finally {
					if (rs != null) {
						 try {
							 rs.close();
						 } catch (SQLException e1) {
						 }
					}
					if (cs != null) {
						try {
							cs.close();
						} catch (SQLException e1) {
						}
					}

					if (con != null) {
						try {
							con.close();
						} catch (SQLException e1) {
						}
					}
				}
				
				switch (tipe) {
				case 1:
					response.put("row", getRowDetil(tipe, fkode));
					break;
				case 2:
				case 3:
				case 8:
					response.put("row", getRowDetil(tipe, fkode));
					break;
				case 4:
				case 5:
					response.put("row", getRowDetil(tipe, fkode));
					break;
				case 6:
				case 7:
					response.put("row", getRowDetil(tipe, kode));
					break;
				case 96:
					response.put("row", Lembur2Utils.getLemburByKode(fkode));
					response.put("notes", getNotesDetil(tipe, fkode));
					break;
				case 97:
					response.put("row", getRowDetil(tipe, fkode));
				case 98:
					response.put("row", Cuti2Utils.getCutiByKode(fkode));
					break;
				case 99:
					response.put("row", Skpd2Utils.getSkpdByKode(fkode));
					response.put("notes", getNotesDetil(tipe, fkode));
					break;
				default:
					break;
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
	@Path("/pegawai/notifikasi/proses")
	@Consumes("application/json")
	@Produces("application/json")
	public Response prosesNotifikasi(@Context HttpHeaders headers, String data) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> metadata = new HashMap<String, Object>();
		Connection con = null;
		CallableStatement cs = null;
		String query = null;

		if (SharedMethod.ServiceAuth(headers, metadata)) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				JsonNode json = mapper.readTree(data);
				Integer kode = json.path("kode").asInt();
				String act = json.path("act").asText();
				String catatan = json.path("catatan").asText();
				Integer useract = json.path("useract").asInt();
				String tokodejobtitle = json.path("tokodejobtitle").asText();
				String tokodeoffice = json.path("tokodeoffice").asText();
				String tonpp = json.path("tonpp").asText();
				Integer tokodepenugasan = json.path("tokodepenugasan").asInt();
				
				try {
					con = new Koneksi().getConnection();
					query = "exec hcis.sp_updatenotifikasi ?,?,?,?,?,?,?,?";
					cs = con.prepareCall(query);
					cs.setInt(1, kode);
					cs.setString(2, act);
					cs.setInt(3, useract);
					cs.setString(4, catatan);
					cs.setString(5, tokodejobtitle);
					cs.setString(6, tokodeoffice);
					cs.setString(7, tonpp);
					cs.setInt(8,tokodepenugasan);
					cs.executeUpdate();
					metadata.put("code", 1);
					metadata.put("message", "Ok");
				} 
				catch (SQLException e) {
					metadata.put("code", 0);
					metadata.put("message", e.getMessage());
				}
				catch (Exception e) {
					e.printStackTrace();
					metadata.put("code", 0);
					metadata.put("message", e.getMessage());
				}
				finally {
					if (cs != null) {
						try {
							cs.close();
						} catch (SQLException e1) {
						}
					}

					if (con != null) {
						try {
							con.close();
						} catch (SQLException e1) {
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
	@Path("/pegawai/notifikasi/batalcuti")
	@Consumes("application/json")
	@Produces("application/json")
	public Response batalcutiNotifikasi(@Context HttpHeaders headers, String data) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> metadata = new HashMap<String, Object>();
		Connection con = null;
		CallableStatement cs = null;
		String query = null;

		if (SharedMethod.ServiceAuth(headers, metadata)) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				JsonNode json = mapper.readTree(data);
				Integer kodenotifikasi = json.path("kodenotifikasi").asInt();
				String act = json.path("act").asText();
				String catatan = json.path("catatan").asText();
				Integer useract = json.path("useract").asInt();
				
				try {
					con = new Koneksi().getConnection();
					query = "exec hcis.sp_batalcuti ?,?,?,?";
					cs = con.prepareCall(query);
					cs.setInt(1, kodenotifikasi);
					cs.setString(2, act);
					cs.setString(3, catatan);
					cs.setInt(4, useract);
					cs.executeUpdate();
					metadata.put("code", 1);
					metadata.put("message", "Ok");
				} 
				catch (Exception e) {
					metadata.put("code", 1);
					metadata.put("message", e.getMessage());
				}
				finally {
					if (cs != null) {
						try {
							cs.close();
						} catch (SQLException e1) {
						}
					}

					if (con != null) {
						try {
							con.close();
						} catch (SQLException e1) {
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
	@Path("/pegawai/notifikasi/setisread")
	@Consumes("application/json")
	@Produces("application/json")
	public Response setisreadNotifikasi(@Context HttpHeaders headers, String data) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> metadata = new HashMap<String, Object>();
		Connection con = null;
		CallableStatement cs = null;
		String query = null;

		if (SharedMethod.ServiceAuth(headers, metadata)) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				JsonNode json = mapper.readTree(data);
				Integer kodenotifikasi = json.path("kodenotifikasi").asInt();
				Integer useract = json.path("useract").asInt();
				
				try {
					con = new Koneksi().getConnection();
					query = "exec hcis.sp_readnotifikasi ?,?,?";
					cs = con.prepareCall(query);
					cs.setInt(1, kodenotifikasi);
					cs.setInt(2, 1);
					cs.setInt(3, useract);
					cs.executeUpdate();
					metadata.put("code", 1);
					metadata.put("message", "Ok");
				} 
				catch (Exception e) {
					metadata.put("code", 1);
					metadata.put("message", e.getMessage());
				}
				finally {
					if (cs != null) {
						try {
							cs.close();
						} catch (SQLException e1) {
						}
					}

					if (con != null) {
						try {
							con.close();
						} catch (SQLException e1) {
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
				
				con = new Koneksi().getConnection();
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
