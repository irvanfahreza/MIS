package id.go.bpjskesehatan.service.v2.kompetensi;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
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
import id.go.bpjskesehatan.service.v2.kompetensi.entitas.Individu;
import id.go.bpjskesehatan.util.SharedMethod;

@Path("/v2/kompetensi")
public class Kompetensi2Rest {	
	
	@Context
    private ServletContext context;
	
	@POST
	@Path("/individu/action")
	@Produces("application/json")
	public Response simpanKompetensiIndividu(
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
				Individu json = mapper.readValue(data, Individu.class);
				
				query = "exec kompetensi.sp_action_kompetensiindividu ?,?,?,?,?";
				con = new Koneksi().getConnection();
				cs = con.prepareCall(query);
				switch (json.getAct()) {
				case "create":
					cs.setString(1, "create");
					cs.setNull(2, java.sql.Types.INTEGER);
					cs.setString(3, json.getNpp());
					cs.setInt(4, json.getKodelevelkompetensi());
					cs.setInt(5, authUser.getUserid());
					break;
				case "update":
					cs.setString(1, "update");
					cs.setInt(2, json.getKode());
					cs.setString(3, json.getNpp());
					cs.setInt(4, json.getKodelevelkompetensi());
					cs.setInt(5, authUser.getUserid());
					break;
				case "delete":
					cs.setString(1, "delete");
					cs.setInt(2, json.getKode());
					cs.setString(3, json.getNpp());
					cs.setInt(4, json.getKodelevelkompetensi());
					cs.setInt(5, authUser.getUserid());
					break;
				default:
					break;
				}
				
				cs.execute();
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
	
}