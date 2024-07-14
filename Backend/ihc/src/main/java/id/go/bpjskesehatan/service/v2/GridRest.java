package id.go.bpjskesehatan.service.v2;

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
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import id.go.bpjskesehatan.database.Koneksi;
import id.go.bpjskesehatan.util.SharedMethod;
import id.go.bpjskesehatan.util.Utils;

@Path("v2/grid")
public class GridRest {	
		
	@POST
	@Path("/{schema}/{servicename}/{page}/{row}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response ListGridService(@Context HttpHeaders headers, @PathParam("schema") String schema, @PathParam("servicename") String servicename,
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
		Boolean ok = true;

		if (SharedMethod.VerifyToken(headers, metadata)) {
			try {
				JsonNode json = null;
				if (data != null) {
					if (!data.isEmpty()) {
						ObjectMapper mapper = new ObjectMapper();
						json = mapper.readTree(data);

						order = json.path("data").path("sort").isMissingNode() ? null
								: SharedMethod.getSortedColumn(mapper.writeValueAsString(json.path("data").path("sort")));

						filter = json.path("data").path("filter").isMissingNode() ? null
								: SharedMethod.getFilteredColumn(mapper.writeValueAsString(json.path("data").path("filter")), null);
						
						filter2 = json.path("data").path("filter2").isMissingNode() ? null
								: SharedMethod.getFilteredColumn(mapper.writeValueAsString(json.path("data").path("filter2")), null);
						
						filter3 = json.path("data").path("filter3").isMissingNode() ? null
								: SharedMethod.getFilteredColumn(mapper.writeValueAsString(json.path("data").path("filter3")), null);

					}
				}
				
				switch (schema) {
				case "organisasi":
					switch (servicename) {
					case "kuotajabatan":
						query = "exec organisasi.sp_listkuotajabatan ?, ?, ?, ?, ?";
						break;
					default:
						ok = false;
						return Response.status(404).build();
					}
					break;
				case "setting":
					switch (servicename) {
					case "grupuser":
						query = "exec hcis.sp_listgrupuser ?, ?, ?, ?, ?";
						break;
					case "user":
						query = "exec hcis.sp_listuser ?, ?, ?, ?, ?";
						break;
					case "pegawaigrupuser":
						query = "exec hcis.sp_listpegawaigrupuser ?, ?, ?, ?, ?";
						break;
					default:
						ok = false;
						return Response.status(404).build();
					}
					break;
				case "view":
					switch (servicename) {
					case "monitoringupload":
						query = "exec hcis.sp_list_monitoring_upload ?, ?, ?, ?, ?";
						break;
					case "monitoringnotif":
						query = "exec hcis.sp_list_monitoring_notif ?, ?, ?, ?, ?";
						break;
					case "monitoringentridatapegawai":
						query = "exec karyawan.sp_list_monitoring_entridatapegawai ?, ?, ?, ?, ?";
						break;
					default:
						ok = false;
						return Response.status(404).build();
					}
					break;
				case "payroll":
					switch (servicename) {
					case "angsurankoperasi":
						query = "exec payroll.sp_listangsurankoperasi ?, ?, ?, ?, ?";
						break;
					case "carloan":
						query = "exec payroll.sp_listcarloan ?, ?, ?, ?, ?";
						break;
					case "kelebihanpulsa":
						query = "exec payroll.sp_listkelebihanpulsa ?, ?, ?, ?, ?";
						break;
					case "pendlainlain":
						query = "exec payroll.sp_listpendlainlain ?, ?, ?, ?, ?";
						break;
					case "potlainlain":
						query = "exec payroll.sp_listpotlainlain ?, ?, ?, ?, ?";
						break;
					default:
						ok = false;
						return Response.status(404).build();
					}
					break;
				case "kinerja":
					switch (servicename) {
					case "predikat":
						query = "exec kinerja.sp_listpredikat ?, ?, ?, ?, ?";
						break;
					case "persentasetupres":
						query = "exec kinerja.sp_listpersentasetupres ?, ?, ?, ?, ?";
						break;
					case "predikatpegawai":
						query = "exec kinerja.sp_listpredikatpegawai ?, ?, ?, ?, ?";
						break;
					case "talentapegawai":
						query = "exec kinerja.sp_listtalentapegawai ?, ?, ?, ?, ?";
						break;
					default:
						ok = false;
						return Response.status(404).build();
					}
					break;
				case "cuti":
					switch (servicename) {
					case "hargabbm":
						query = "exec cuti.sp_listhargabbm ?, ?, ?, ?, ?";
						break;
					case "spmtemplate":
						query = "exec cuti.sp_spmtemplate ?, ?, ?, ?, ?";
						break;
					case "tipe":
						query = "exec cuti.sp_listtipe ?, ?, ?, ?, ?";
						break;
					case "harilibur":
						query = "exec cuti.sp_listharilibur ?, ?, ?, ?, ?";
						break;
					default:
						ok = false;
						return Response.status(404).build();
					}
					break;
				case "skpd":
					switch (servicename) {
					case "xxx":
						query = "";
						break;
					default:
						ok = false;
						return Response.status(404).build();
					}
					break;
				default:
					ok = false;
					return Response.status(404).build();
				}
				
				if(ok) {
					if(!json.path("data").path("filter2").isMissingNode()) {
						query = query.concat(", ?");
					}
					if(!json.path("data").path("filter2").isMissingNode() && !json.path("data").path("filter3").isMissingNode()) {
						query = query.concat(", ?");
					}
					
					con = new Koneksi().getConnection();
					cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
					cs.setInt(1, Integer.parseInt(page));
					cs.setInt(2, Integer.parseInt(row));
					cs.setInt(3, 1);
					cs.setString(4, order);
					cs.setString(5, filter);
					if(!json.path("data").path("filter2").isMissingNode()) {
						cs.setString(6, filter2);
					}
					if(!json.path("data").path("filter2").isMissingNode() && !json.path("data").path("filter3").isMissingNode()) {
						cs.setString(7, filter3);
					}
					cs.execute();
					rs = cs.getResultSet();
					metadata.put("code", 1);
					metadata.put("message", Response.Status.NO_CONTENT.toString());
					metadata.put("rowcount", 0);

					if (rs.next()) {
						metadata.put("rowcount", rs.getInt("jumlah"));
						switch (schema) {
						case "payroll":
							switch (servicename) {
							case "angsurankoperasi":
								metadata.put("total", rs.getBigDecimal("total"));
								break;
							case "carloan":
								metadata.put("total", rs.getBigDecimal("total"));
								break;
							case "kelebihanpulsa":
								metadata.put("total", rs.getBigDecimal("total"));
								break;
							}
							break;
						}
					}

					listdata = new ArrayList<Map<String, Object>>();

					rs.close();
					cs.close();
					
					cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
					cs.setInt(1, Integer.parseInt(page));
					cs.setInt(2, Integer.parseInt(row));
					cs.setInt(3, 0);
					cs.setString(4, order);
					cs.setString(5, filter);
					if(!json.path("data").path("filter2").isMissingNode()) {
						cs.setString(6, filter2);
					}
					if(!json.path("data").path("filter2").isMissingNode() && !json.path("data").path("filter3").isMissingNode()) {
						cs.setString(7, filter3);
					}
					cs.execute();
					rs = cs.getResultSet();
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
					response.put("list", listdata);
					result.put("response", response);
					rs.close();
				}
			} catch (SQLException e) {
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
