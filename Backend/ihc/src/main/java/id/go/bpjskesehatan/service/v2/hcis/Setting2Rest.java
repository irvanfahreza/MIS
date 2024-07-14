package id.go.bpjskesehatan.service.v2.hcis;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import id.go.bpjskesehatan.database.Koneksi;
import id.go.bpjskesehatan.entitas.Metadata;
import id.go.bpjskesehatan.entitas.Respon;
import id.go.bpjskesehatan.entitas.Result;
import id.go.bpjskesehatan.service.v2.hcis.entitas.GrupUserMenu;
import id.go.bpjskesehatan.service.v2.lembur.entitas.Report;
import id.go.bpjskesehatan.util.SharedMethod;

@Path("/v2/setting")
public class Setting2Rest {	
	
	@Context
    private ServletContext context;

	@GET
	@Path("/getgrupusermenu/kodegrupuser/{kodegrupuser}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response getMenuByGrupUser(
			@Context HttpHeaders headers, 
			@PathParam("kodegrupuser") Integer kodegrupuser
		) {
		Respon<GrupUserMenu> response = new Respon<GrupUserMenu>();
		Metadata metadata = new Metadata();
		Result<GrupUserMenu> result = new Result<GrupUserMenu>();
		
		Connection con = null;
		ResultSet rs = null;
		CallableStatement cs = null;
		String query = null;
		
		if (SharedMethod.VerifyToken(headers, metadata)) {
			try {
				query = "exec hcis.sp_getmenubykodegrupuser ?";
				con = new Koneksi().getConnection();
				cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
				cs.setInt(1, kodegrupuser);
				rs = cs.executeQuery();
				metadata.setCode(1);
				metadata.setMessage("Data kosong.");
				List<GrupUserMenu> grupUserMenus = new ArrayList<>();
				while (rs.next()) {				
					GrupUserMenu grupUserMenu = new GrupUserMenu();
					grupUserMenu.setKodegrupuser(rs.getInt("kodegrupuser"));
					grupUserMenu.setKode(rs.getString("kode"));
					grupUserMenu.setLabel(rs.getString("nama"));
					grupUserMenu.setKodeparent(rs.getString("kodeparent"));
					grupUserMenu.setCreate(rs.getBoolean("create"));
					grupUserMenu.setRead(rs.getBoolean("read"));
					grupUserMenu.setUpdate(rs.getBoolean("update"));
					grupUserMenu.setDelete(rs.getBoolean("delete"));
					grupUserMenu.setCreateprivilege(rs.getBoolean("createprivilege"));
					grupUserMenu.setReadprivilege(rs.getBoolean("readprivilege"));
					grupUserMenu.setUpdateprivilege(rs.getBoolean("updateprivilege"));
					grupUserMenu.setDeleteprivilege(rs.getBoolean("deleteprivilege"));
					grupUserMenus.add(grupUserMenu);
				}
				response.setList(grupUserMenus);
				metadata.setCode(1);
				metadata.setMessage("Ok.");
				result.setResponse(response);
			} catch (SQLException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
			} catch (NamingException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
				e.printStackTrace();
			} catch (SecurityException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
				e.printStackTrace();
			} catch (Exception e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
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
		
		result.setMetadata(metadata);
		return Response.ok(result).build();
	}
	
	@POST
	@Path("/setgrupusermenu")
	@Produces("application/json")
	public Response setGrupUserMenu(@Context HttpHeaders headers, String data) {
		
		Metadata metadata = new Metadata();
		Result<Report> result = new Result<Report>();
		
		Connection con = null;
		PreparedStatement ps = null;
		String query = null;
		
		if (SharedMethod.VerifyToken(headers, metadata)) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				JsonNode json = mapper.readTree(data);
				Boolean ok = true;
				Boolean ok2 = true;
				String msg = "";
				StringBuilder str = new StringBuilder();
				
				if (!data.isEmpty()) {
					if(json.path("privilege").isMissingNode()){
						str.append("privilege").append(", ");
						ok = false;
					}
					if(json.path("kodegrupuser").isMissingNode()){
						str.append("kodegrupuser").append(", ");
						ok = false;
					}
					if(json.path("kodemenu").isMissingNode()){
						str.append("kodemenu").append(", ");
						ok = false;
					}
					if(json.path("kodeaction").isMissingNode()){
						str.append("kodeaction").append(", ");
						ok = false;
					}
					if(json.path("useract").isMissingNode()){
						str.append("useract").append(", ");
						ok = false;
					}
					
					if(!ok){
						str.replace(str.length() - 2, str.length() - 1, "");
						metadata.setMessage("data " + str + " is missing.");
					}
					else if(!ok2){
						metadata.setMessage(msg);
					}
					
				}
				else{
					metadata.setCode(0);
					metadata.setMessage("missing data.");
					ok = false;
				}
				
				if(ok && ok2){
					Boolean privilege = json.path("privilege").asBoolean();
					Integer kodegrupuser = json.path("kodegrupuser").asInt();
					String kodemenu = json.path("kodemenu").asText();
					Integer kodeaction = json.path("kodeaction").asInt();
					//Integer useract = json.path("useract").asInt();
					
					if(privilege) {
						query = "insert into hcis.menuaction(kodegrupuser, kodemenu, kodeaction, row_status) values (?, ?, ?, 1)";
					}
					else {
						query = "delete from hcis.menuaction where kodegrupuser=? and kodemenu=? and kodeaction=?";
					}
					con = new Koneksi().getConnection();
					ps = con.prepareCall(query);
					ps.setInt(1, kodegrupuser);
					ps.setString(2, kodemenu);
					ps.setInt(3, kodeaction);
					ps.executeUpdate();
					metadata.setCode(1);
					metadata.setMessage("Ok");
				}
			} catch (SQLException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
			} catch (Exception e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
				e.printStackTrace();
			} finally {
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
		
		result.setMetadata(metadata);
		return Response.ok(result).build();
	}
	
}