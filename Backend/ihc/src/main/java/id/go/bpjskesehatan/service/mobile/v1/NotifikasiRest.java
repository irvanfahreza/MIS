package id.go.bpjskesehatan.service.mobile.v1;

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
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import id.go.bpjskesehatan.database.Koneksi;
import id.go.bpjskesehatan.util.Utils;

@Path("/mobile/v1/notifikasi")
public class NotifikasiRest {
	
    @Context
    private ServletContext context;
    
	@GET
	@Path("/list/iscount/{iscount}/start/{start}/limit/{limit}/npp/{npp}/kodejobtitle/{kodejobtitle}/kodeoffice/{kodeoffice}")
	@Produces("application/json")
	@Consumes("application/json")
	public Response listNotifikasiPerPegawai(
			@Context HttpHeaders headers, 
			@PathParam("iscount") Integer iscount,
			@PathParam("start") Integer start,
			@PathParam("limit") Integer limit,
			@PathParam("npp") String npp,
			@PathParam("kodejobtitle") String kodejobtitle,
			@PathParam("kodeoffice") String kodeoffice) {

		Map<String, Object> response = new HashMap<String, Object>();
		Map<String, Object> metadata = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> listdata = null;
		
		//if (AuthMobile.VerifyToken(headers, metadata)) {
		if (true) {
			Connection con = null;
			CallableStatement cs = null;
			ResultSet rs = null;
			String query = "exec hcis.sp_listnotifikasiperpegawai ?, ?, ?, ?, ?, ?, ?";
			
			try {
				con = new Koneksi().getConnection();
				cs = con.prepareCall(query);
				
				if(iscount==1) {
					cs.setString(1, npp);
					cs.setString(2, kodejobtitle);
					cs.setString(3, kodeoffice);
					cs.setInt(4, start);
					cs.setInt(5, limit);
					cs.setInt(6, 0);
					cs.setInt(7, 1);
				}
				else {
					cs.setString(1, npp);
					cs.setString(2, kodejobtitle);
					cs.setString(3, kodeoffice);
					cs.setInt(4, start);
					cs.setInt(5, limit);
					cs.setInt(6, 2);
					cs.setInt(7, 0);
				}
				
				rs = cs.executeQuery();
				ResultSetMetaData metaData = rs.getMetaData();
				Map<String, Object> hasil = null;
				listdata = new ArrayList<Map<String, Object>>();
				metadata.put("code", 0);
				metadata.put("message", "Data tidak ditemukan");
				while(rs.next()) {
					if(iscount==1) {
						metadata.put("rowcount", rs.getInt("row_count"));
					}
					else {
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
					
					metadata.put("code", 1);
					metadata.put("message", "Ok");
				}
				response.put("list", listdata);
				result.put("response", response);
			} catch (Exception e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				e.printStackTrace();
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
		}
		
		result.put("metadata", metadata);
		return Response.ok(result).build();
	}
	
	
}
