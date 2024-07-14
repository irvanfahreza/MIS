package id.go.bpjskesehatan.service.mobile.v1;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import id.go.bpjskesehatan.database.Koneksi;

@Path("/mobile/v1/firebase")
public class Firebase {
	
    @Context
    private ServletContext context;
    
    private static List<FirebasePegawai> getListTokenPegawaiBelumAbsen(Integer sts, Integer gmt) {
		Connection con = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		String query = null;
		List<FirebasePegawai> firebasePegawais = null;
		try {
			query = "exec hcis.sp_getPegawaiBelumAbsen ?,?";
			con = new Koneksi().getConnection();
			cs = con.prepareCall(query);
			cs.setInt(1, sts);
			cs.setInt(2, gmt);
			rs = cs.executeQuery();
			List<FirebasePegawai> rows = new ArrayList<FirebasePegawai>();
			while(rs.next()) {
				FirebasePegawai row = new FirebasePegawai();
				row.setToken(rs.getString("token"));
				row.setNpp(rs.getString("npp"));
				row.setNama(rs.getString("nama"));
				rows.add(row);
			}
			if(rows.size()>0) {
				firebasePegawais = rows;
			}
		} catch (Exception e) {
			
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
		return firebasePegawais;
	}
	
	@POST
	@Path("/register")
	@Produces("application/json")
	@Consumes("application/json")
	public Response register(
			@Context HttpHeaders headers, 
			String data) {

		Map<String, Object> metadata = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		
		if (AuthMobile.VerifyToken(headers, metadata)) {
		//if (true) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				JsonNode json = mapper.readTree(data);
				String npp = json.path("npp").asText();
				String token = json.path("token").asText();
				
				FirebaseUtils.register(context, FirebaseUtils.GRUP_SEMUAPEGAWAI, npp, token);
				
				metadata.put("code", 1);
				metadata.put("message", "register sukses");
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
	@Path("/unregister")
	@Produces("application/json")
	@Consumes("application/json")
	public Response unregister(
			@Context HttpHeaders headers, 
			String data) {

		Map<String, Object> metadata = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		
		if (AuthMobile.VerifyToken(headers, metadata)) {
		//if (true) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				JsonNode json = mapper.readTree(data);
				String npp = json.path("npp").asText();
				String token = json.path("token").asText();
				
				FirebaseUtils.unregister(context, FirebaseUtils.GRUP_SEMUAPEGAWAI, npp, token);
				
				metadata.put("code", 1);
				metadata.put("message", "unregister sukses");
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
	@Path("/sendtogroup")
	@Produces("application/json")
	@Consumes("application/json")
	public Response sendToGroup(
			@Context HttpHeaders headers, 
			String data) {

		Map<String, Object> metadata = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		
		//if (SharedMethod.VerifyToken(headers, metadata)) {
		if (true) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				JsonNode json = mapper.readTree(data);
				String title = json.path("title").asText();
				String body = json.path("body").asText();
				
				FirebaseUtils.sendToGroup(context, FirebaseUtils.GRUP_SEMUAPEGAWAI, title, body);
				
				metadata.put("code", 1);
				metadata.put("message", "Ok");
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
	@Path("/sendtopegawai")
	@Produces("application/json")
	@Consumes("application/json")
	public Response sendToPegawai(
			@Context HttpHeaders headers, 
			String data) {

		Map<String, Object> metadata = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		
		//if (SharedMethod.VerifyToken(headers, metadata)) {
		if (true) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				JsonNode json = mapper.readTree(data);
				String npp = json.path("npp").asText();
				String title = json.path("title").asText();
				String body = json.path("body").asText();
				
				FirebaseUtils.sendToPegawai(context, npp, title, body);
				
				metadata.put("code", 1);
				metadata.put("message", "Ok");
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
	@Path("/sendtobelumabsen")
	@Produces("application/json")
	@Consumes("application/json")
	public Response sendToBelumAbsen(
			@Context HttpHeaders headers, 
			String data) {

		Map<String, Object> metadata = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		
		//if (SharedMethod.VerifyToken(headers, metadata)) {
		if (true) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				JsonNode json = mapper.readTree(data);
				Integer sts = json.path("sts").asInt();
				Integer gmt = json.path("gmt").asInt();
				String body = json.path("body").asText();
				
				List<FirebasePegawai> rows = getListTokenPegawaiBelumAbsen(sts, gmt);
				if(rows != null) {
					for(FirebasePegawai row : rows) {
						FirebaseUtils.sendToToken(context, row.getToken(), row.getNama(), body);
					}
				}
				metadata.put("code", 1);
				metadata.put("message", "Ok");
			} catch (Exception e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				e.printStackTrace();
			}
		}
		
		result.put("metadata", metadata);
		return Response.ok(result).build();
	}
	
}
