package id.go.bpjskesehatan.service.v2.djp;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import id.go.bpjskesehatan.database.Koneksi;
import id.go.bpjskesehatan.entitas.Metadata;
import id.go.bpjskesehatan.entitas.Result;
import id.go.bpjskesehatan.service.mobile.v1.AuthUser;
import id.go.bpjskesehatan.service.v2.djp.entitas.DetailDimensiJabatanTbl;
import id.go.bpjskesehatan.service.v2.djp.entitas.DetailKpiTbl;
import id.go.bpjskesehatan.service.v2.djp.entitas.SimpanTanggungJawab;
import id.go.bpjskesehatan.service.v2.entitas.ParamQuery;
import id.go.bpjskesehatan.util.SharedMethod;
import id.go.bpjskesehatan.util.Utils;

@Path("/v2/djp")
public class Djp2Rest {	
	
	@Context
    private ServletContext context;
	
	@GET
	@Path("/tanggungjawab/kodedjp/{kodedjp}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response getTanggungJawabByKodeDjp(
			@Context HttpHeaders headers, 
			@PathParam("kodedjp") Integer kodedjp
		) {
		Map<String, Object> response = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> listdata = null;
		Map<String, Object> metadata = new HashMap<String, Object>();
		
		AuthUser authUser = new AuthUser();
		if (SharedMethod.VerifyToken(authUser, headers, metadata)) {
			
			Connection con = null;
			ResultSet rs = null;
			CallableStatement cs = null;
			String query = null;
			
			ResultSetMetaData metaData;
			Map<String, Object> hasil = null;
			
			try {
				metadata.put("code", 2);
				metadata.put("message", Response.Status.NO_CONTENT.toString());
				
				query = "exec djp.sp_listtanggungjawab 1,100,0,null,?";
				con = new Koneksi().getConnection();
				cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
				cs.setString(1, "kodedjp="+kodedjp);
				rs = cs.executeQuery();
				
				listdata = new ArrayList<Map<String, Object>>();
				while (rs.next()) {
					metaData = rs.getMetaData();
					hasil = new HashMap<String, Object>();
					
					Integer kodetanggungjawab = 0;
					for (int i = 1; i <= metaData.getColumnCount(); i++) {
						if(rs.getObject(i)!=null && metaData.getColumnTypeName(i).equalsIgnoreCase("date")){
							hasil.put(metaData.getColumnName(i).toLowerCase(), Utils.SqlDateToSqlString(rs.getDate(i)));
						}
						else {
							hasil.put(metaData.getColumnName(i).toLowerCase(), rs.getObject(i));
						}
						if(metaData.getColumnName(i).toLowerCase().equals("kode")) {
							kodetanggungjawab = rs.getInt(i);
						}
					}
					
					ResultSet rs2 = null;
					CallableStatement cs2 = null;
					String query2 = null;
					try {
						query2 = "exec djp.sp_listdetailkpi 1,100,0,null,?";
						cs2 = con.prepareCall(query2, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
						cs2.setString(1, "kodetanggungjawab="+kodetanggungjawab);
						rs2 = cs2.executeQuery();
						List<Map<String, Object>> listdata2 = null;
						listdata2 = new ArrayList<Map<String, Object>>();
						ResultSetMetaData metaData2;
						Map<String, Object> hasil2 = null;
						int index = 0;
						while(rs2.next()) {
							metaData2 = rs2.getMetaData();
							hasil2 = new HashMap<String, Object>();
							for (int j = 1; j <= metaData2.getColumnCount(); j++) {
								hasil2.put(metaData2.getColumnName(j).toLowerCase(), rs2.getObject(j));
							}
							hasil2.put("btnadd", true);
							hasil2.put("btnremove", index==0?false:true);
							hasil2.put("deleted", 0);
							listdata2.add(hasil2);
							index++;
						}
						hasil.put("detailkpi", listdata2);
					} finally {
						// TODO: handle finally clause
					}
					
					rs2.close();
					cs2.close();
					
					try {
						query2 = "exec djp.sp_listdetaildimensijabatan 1,100,0,null,?";
						cs2 = con.prepareCall(query2, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
						cs2.setString(1, "kodetanggungjawab="+kodetanggungjawab);
						rs2 = cs2.executeQuery();
						List<Map<String, Object>> listdata2 = null;
						listdata2 = new ArrayList<Map<String, Object>>();
						ResultSetMetaData metaData2;
						Map<String, Object> hasil2 = null;
						int index = 0;
						while(rs2.next()) {
							metaData2 = rs2.getMetaData();
							hasil2 = new HashMap<String, Object>();
							for (int j = 1; j <= metaData2.getColumnCount(); j++) {
								hasil2.put(metaData2.getColumnName(j).toLowerCase(), rs2.getObject(j));
							}
							hasil2.put("btnadd", true);
							hasil2.put("btnremove", index==0?false:true);
							hasil2.put("deleted", 0);
							listdata2.add(hasil2);
							index++;
						}
						hasil.put("detaildimensijabatan", listdata2);
					} finally {
						// TODO: handle finally clause
					}
					
					listdata.add(hasil);
					metadata.put("code", 1);
					metadata.put("message", "OK");
				}
				response.put("list", listdata);
				result.put("response", response);
				
			} catch (SQLException e) {
				e.printStackTrace();
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
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
	@Path("/tanggungjawab/simpan")
	@Produces("application/json")
	public Response simpanTanggungJawab(
			@Context HttpHeaders headers, 
			String data, 
			@Context HttpServletRequest request) {

		Metadata metadata = new Metadata();
		Result<Metadata> result = new Result<Metadata>();

		AuthUser authUser = new AuthUser();
		if (SharedMethod.VerifyToken(authUser, headers, metadata)) {
			Connection con = null;
			CallableStatement cs = null;
			String query = null;
			
			try {
				ObjectMapper mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, true);
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				SimpanTanggungJawab json = mapper.readValue(data, SimpanTanggungJawab.class);
				
				query = "DECLARE @detailkpi djp.detailkpiAsTable;";
				query += "DECLARE @detaildimensijabatan djp.detaildimensijabatanAsTable;";
				List<ParamQuery> paramQueries = new ArrayList<>();
				Integer index = 0;
				for (DetailKpiTbl item : json.getDetailkpi()) {
					
					index++;
					ParamQuery param1 = new ParamQuery();
					param1.setIndex(index);
					param1.setType("int");
					param1.setValue(item.getKode().toString());
					paramQueries.add(param1);
					
					index++;
					ParamQuery param3 = new ParamQuery();
					param3.setIndex(index);
					param3.setType("string");
					param3.setValue(item.getNama());
					paramQueries.add(param3);
					
					index++;
					ParamQuery param4 = new ParamQuery();
					param4.setIndex(index);
					param4.setType("int");
					param4.setValue(item.getDeleted().toString());
					paramQueries.add(param4);
					
					query += "insert into @detailkpi (kode, nama, deleted) values (?,?,?);";
				}
				
				for (DetailDimensiJabatanTbl item : json.getDetaildimensijabatan()) {
					
					index++;
					ParamQuery param1 = new ParamQuery();
					param1.setIndex(index);
					param1.setType("int");
					param1.setValue(item.getKode().toString());
					paramQueries.add(param1);
					
					index++;
					ParamQuery param3 = new ParamQuery();
					param3.setIndex(index);
					param3.setType("string");
					param3.setValue(item.getNama());
					paramQueries.add(param3);
					
					index++;
					ParamQuery param4 = new ParamQuery();
					param4.setIndex(index);
					param4.setType("int");
					param4.setValue(item.getDeleted().toString());
					paramQueries.add(param4);
					
					query += "insert into @detaildimensijabatan (kode, nama, deleted) values (?,?,?);";
				}
				
				if(json.getKode()>0) {
					query += "exec djp.sp_tanggungjawab_update ?,?,?,@detailkpi,@detaildimensijabatan,?,?";
				}
				else {
					query += "exec djp.sp_tanggungjawab_insert ?,?,?,@detailkpi,@detaildimensijabatan,?";
				}
				
				con = new Koneksi().getConnection();
				cs = con.prepareCall(query);
				
				index = 0;
				for (ParamQuery param : paramQueries) {
					index++;
					if(param.getType().equalsIgnoreCase("int")) {
						cs.setInt(param.getIndex(), Integer.parseInt(param.getValue()));
					}
					else {
						cs.setString(param.getIndex(), param.getValue());
					}
				}
				cs.setInt(index+1, json.getKodedjp());
				cs.setString(index+2, json.getTanggungjawabutama());
				cs.setString(index+3, json.getWewenangutama());
				cs.setInt(index+4, authUser.getUserid());
				if(json.getKode()>0) {
					cs.setInt(index+5, json.getKode());
				}
				cs.executeUpdate();
				metadata.setCode(1);
				metadata.setMessage("Ok");
			} catch (SQLException e) {
				e.printStackTrace();
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
			} finally {
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
		
		result.setMetadata(metadata);
		return Response.ok(result).build();
	}
	
	@GET
	@Path("/tanggungjawab/history/kodedjp/{kodedjp}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response getHstoryTanggungJawabByKodeDjp(
			@Context HttpHeaders headers, 
			@PathParam("kodedjp") Integer kodedjp
		) {
		Map<String, Object> response = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> listdata = null;
		Map<String, Object> metadata = new HashMap<String, Object>();
		
		AuthUser authUser = new AuthUser();
		if (SharedMethod.VerifyToken(authUser, headers, metadata)) {
			
			Connection con = null;
			ResultSet rs = null;
			CallableStatement cs = null;
			String query = null;
			
			ResultSetMetaData metaData;
			Map<String, Object> hasil = null;
			
			try {
				metadata.put("code", 2);
				metadata.put("message", Response.Status.NO_CONTENT.toString());
				
				query = "exec djp.sp_listtanggungjawabhistory 1,100,0,null,?";
				con = new Koneksi().getConnection();
				cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
				cs.setString(1, "kodedjp="+kodedjp);
				rs = cs.executeQuery();
				
				listdata = new ArrayList<Map<String, Object>>();
				while (rs.next()) {
					metaData = rs.getMetaData();
					hasil = new HashMap<String, Object>();
					
					Integer kodetanggungjawab = 0;
					for (int i = 1; i <= metaData.getColumnCount(); i++) {
						if(rs.getObject(i)!=null && metaData.getColumnTypeName(i).equalsIgnoreCase("date")){
							hasil.put(metaData.getColumnName(i).toLowerCase(), Utils.SqlDateToSqlString(rs.getDate(i)));
						}
						else {
							hasil.put(metaData.getColumnName(i).toLowerCase(), rs.getObject(i));
						}
						if(metaData.getColumnName(i).toLowerCase().equals("kode")) {
							kodetanggungjawab = rs.getInt(i);
						}
					}
					
					ResultSet rs2 = null;
					CallableStatement cs2 = null;
					String query2 = null;
					try {
						query2 = "exec djp.sp_listdetailkpihistory 1,100,0,null,?";
						cs2 = con.prepareCall(query2, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
						cs2.setString(1, "kodetanggungjawab="+kodetanggungjawab);
						rs2 = cs2.executeQuery();
						List<Map<String, Object>> listdata2 = null;
						listdata2 = new ArrayList<Map<String, Object>>();
						ResultSetMetaData metaData2;
						Map<String, Object> hasil2 = null;
						int index = 0;
						while(rs2.next()) {
							metaData2 = rs2.getMetaData();
							hasil2 = new HashMap<String, Object>();
							for (int j = 1; j <= metaData2.getColumnCount(); j++) {
								hasil2.put(metaData2.getColumnName(j).toLowerCase(), rs2.getObject(j));
							}
							hasil2.put("btnadd", true);
							hasil2.put("btnremove", index==0?false:true);
							hasil2.put("deleted", 0);
							listdata2.add(hasil2);
							index++;
						}
						hasil.put("detailkpi", listdata2);
					} finally {
						// TODO: handle finally clause
					}
					
					rs2.close();
					cs2.close();
					
					try {
						query2 = "exec djp.sp_listdetaildimensijabatanhistory 1,100,0,null,?";
						cs2 = con.prepareCall(query2, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
						cs2.setString(1, "kodetanggungjawab="+kodetanggungjawab);
						rs2 = cs2.executeQuery();
						List<Map<String, Object>> listdata2 = null;
						listdata2 = new ArrayList<Map<String, Object>>();
						ResultSetMetaData metaData2;
						Map<String, Object> hasil2 = null;
						int index = 0;
						while(rs2.next()) {
							metaData2 = rs2.getMetaData();
							hasil2 = new HashMap<String, Object>();
							for (int j = 1; j <= metaData2.getColumnCount(); j++) {
								hasil2.put(metaData2.getColumnName(j).toLowerCase(), rs2.getObject(j));
							}
							hasil2.put("btnadd", true);
							hasil2.put("btnremove", index==0?false:true);
							hasil2.put("deleted", 0);
							listdata2.add(hasil2);
							index++;
						}
						hasil.put("detaildimensijabatan", listdata2);
					} finally {
						// TODO: handle finally clause
					}
					
					listdata.add(hasil);
					metadata.put("code", 1);
					metadata.put("message", "OK");
				}
				response.put("list", listdata);
				result.put("response", response);
				
			} catch (SQLException e) {
				e.printStackTrace();
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
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
}