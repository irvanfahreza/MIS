package id.go.bpjskesehatan.service.v2;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.CallableStatement;
import java.sql.Connection;
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
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import id.go.bpjskesehatan.database.Koneksi;
import id.go.bpjskesehatan.util.SharedMethod;
import id.go.bpjskesehatan.util.Utils;


@Path("/v2/list")
public class ListRest {	
	
	@POST
	@Path("/view/{servicename}/{page}/{row}")
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
		Statement cs2 = null;
		Statement cs3 = null;
		String order = null;
		String filter = null;
		String query = null;
		int rowlimit = Integer.parseInt(page) + Integer.parseInt(row) - 1;

		if (SharedMethod.VerifyToken(headers, metadata)) {
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
				
				cs2 = con.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
				rs = cs2.executeQuery("select top 1 * from " + servicename);
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
				
				cs3 = con.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
				rs = cs3.executeQuery(query);
				metaData = rs.getMetaData();
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
				rs.close();

			} catch (SQLException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
			} catch (NamingException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				//e.printStackTrace();
			} catch (IllegalAccessException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				//e.printStackTrace();
			} catch (InvocationTargetException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				//e.printStackTrace();
			} catch (SecurityException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				//e.printStackTrace();
			} catch (InstantiationException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				//e.printStackTrace();
			} catch (Exception e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				//e.printStackTrace();
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
				if (cs2 != null) {
					try {
						cs2.close();
					} catch (SQLException e) {
					}
				}
				if (cs3 != null) {
					try {
						cs3.close();
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
	@Path("/sp/{servicename}/{page}/{row}")
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
		CallableStatement cs2 = null;
		String order = null;
		String filter = null;
		String query = null;

		if (SharedMethod.VerifyToken(headers, metadata)) {
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
				
				cs2 = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
				cs2.setInt(1, Integer.parseInt(page));
				cs2.setInt(2, Integer.parseInt(row));
				cs2.setInt(3, 0);
				cs2.setString(4, order);
				cs2.setString(5, filter);
				rs = cs2.executeQuery();
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
				response.put("list", listdata);
				result.put("response", response);
				rs.close();

			} catch (SQLException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
			} catch (NamingException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				//e.printStackTrace();
			} catch (IllegalAccessException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				//e.printStackTrace();
			} catch (InvocationTargetException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				//e.printStackTrace();
			} catch (SecurityException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				//e.printStackTrace();
			} catch (InstantiationException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				//e.printStackTrace();
			} catch (Exception e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				//e.printStackTrace();
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
				if (cs2 != null) {
					try {
						cs2.close();
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
	@Path("/sp2/{servicename}/{page}/{row}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response ListSPService2(@Context HttpHeaders headers, @PathParam("servicename") String servicename,
			@PathParam("page") String page, @PathParam("row") String row, String data) {
		Map<String, Object> response = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> listdata = null;
		Map<String, Object> metadata = new HashMap<String, Object>();
		Connection con = null;
		ResultSet rs = null;
		CallableStatement cs = null;
		CallableStatement cs2 = null;
		String order = null;
		String filter = null;
		String filter2 = null;
		String query = null;

		if (SharedMethod.VerifyToken(headers, metadata)) {
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
						
					}
				}
				
				try {
					query = "exec " + servicename + " ?, ?, ?, ?, ?, ?";
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
					rs = cs.executeQuery();
					metadata.put("code", 2);
					metadata.put("message", Response.Status.NO_CONTENT.toString());
					metadata.put("rowcount", 0);
	
					if (rs.next()) {
						metadata.put("rowcount", rs.getInt("jumlah"));
					}
	
				}finally {
					rs.close();
				}

				
				cs2 = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
				cs2.setInt(1, Integer.parseInt(page));
				cs2.setInt(2, Integer.parseInt(row));
				cs2.setInt(3, 0);
				cs2.setString(4, order);
				cs2.setString(5, filter);
				cs2.setString(6, filter2);
				rs = cs2.executeQuery();
				ResultSetMetaData metaData = rs.getMetaData();
				Map<String, Object> hasil = null;
				listdata = new ArrayList<Map<String, Object>>();

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
				rs.close();

			} catch (SQLException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
			} catch (NamingException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				//e.printStackTrace();
			} catch (IllegalAccessException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				//e.printStackTrace();
			} catch (InvocationTargetException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				//e.printStackTrace();
			} catch (SecurityException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				//e.printStackTrace();
			} catch (InstantiationException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				//e.printStackTrace();
			} catch (Exception e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				//e.printStackTrace();
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
				
				if (cs2 != null) {
					try {
						cs2.close();
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
	@Path("/sp/{servicename}/{paramcount}")
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

		if (SharedMethod.VerifyToken(headers, metadata)) {
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
				metadata.put("code", 1);
				metadata.put("message", "Data kosong");
				while (rs.next()) {
					count++;
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
				//e.printStackTrace();
			} catch (JsonProcessingException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				//e.printStackTrace();
			} catch (IOException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				//e.printStackTrace();
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
	
	@GET
	@Path("/sp/{servicename}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response ListExecSPServicenoparam(@Context HttpHeaders headers, @PathParam("servicename") String servicename) {
		Map<String, Object> response = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> listdata = null;
		Map<String, Object> metadata = new HashMap<String, Object>();
		Connection con = null;
		// ResultSet rs = null;
		CallableStatement cs = null;
		String query = null;
		
		ResultSet rs = null;

		if (SharedMethod.VerifyToken(headers, metadata)) {
			try {
				con = new Koneksi().getConnection();

				query = "exec " + servicename + " ";
				cs = con.prepareCall(query);

				//cs.executeUpdate();
				// System.out.println(query);
				rs = cs.executeQuery();
				ResultSetMetaData metaData = rs.getMetaData();
				Map<String, Object> hasil = null;
				listdata = new ArrayList<Map<String, Object>>();
				int count=0;
				metadata.put("code", 1);
				metadata.put("message", "Data kosong");
				while (rs.next()) {
					count++;
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
				//e.printStackTrace();
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
	
}
