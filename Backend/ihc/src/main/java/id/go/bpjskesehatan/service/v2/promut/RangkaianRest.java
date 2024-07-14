package id.go.bpjskesehatan.service.v2.promut;

import java.lang.reflect.InvocationTargetException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import id.go.bpjskesehatan.database.Koneksi;
import id.go.bpjskesehatan.entitas.KomponenGaji;
import id.go.bpjskesehatan.entitas.Metadata;
import id.go.bpjskesehatan.entitas.Respon;
import id.go.bpjskesehatan.entitas.Result;
import id.go.bpjskesehatan.service.v2.promut.entitas.Rangkaian;
import id.go.bpjskesehatan.util.SharedMethod;
import id.go.bpjskesehatan.util.Utils;

@Path("/v2/rangkaian")
public class RangkaianRest {	

	@Context
	private ServletContext context;

	@POST
	@Path("/grid/{page}/{row}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response ListGrid(@Context HttpHeaders headers,
			@PathParam("page") String page, @PathParam("row") String row, String data) {

		Respon<Rangkaian> response = new Respon<Rangkaian>();
		Metadata metadata = new Metadata();
		Result<Rangkaian> result = new Result<Rangkaian>();

		Connection con = null;
		ResultSet rs = null;
		CallableStatement cs = null;
		String order = null;
		String filter = null;
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
					}
				}

				if(ok) {
					query = "exec telaah.sp_get_list_rangkaian ?, ?, ?, ?, ?";
					con = new Koneksi().getConnection();
					cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
					cs.setInt(1, Integer.parseInt(page));
					cs.setInt(2, Integer.parseInt(row));
					cs.setInt(3, 1);
					cs.setString(4, order);
					cs.setString(5, filter);
					rs = cs.executeQuery();
					metadata.setCode(0);
					metadata.setMessage(Response.Status.NO_CONTENT.toString());
					metadata.setRowcount(0);

					if (rs.next()) {
						metadata.setRowcount(rs.getInt("jumlah"));
					}
					rs.close();
					cs.close();

					cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
					cs.setInt(1, Integer.parseInt(page));
					cs.setInt(2, Integer.parseInt(row));
					cs.setInt(3, 0);
					cs.setString(4, order);
					cs.setString(5, filter);
					rs = cs.executeQuery();

					List<Rangkaian> rangkaian = new ArrayList<>();
					while (rs.next()) {
						Rangkaian rangkai = new Rangkaian();
						rangkai.setNum(rs.getInt("RowNumber"));
						rangkai.setKode(rs.getInt("kode"));
						rangkai.setTahun(rs.getInt("tahun"));
						rangkai.setNama(rs.getString("nama"));
						rangkai.setNosurat(rs.getString("nosurat"));
						rangkai.setCatatan(rs.getString("catatan"));
						rangkai.setAjukan(rs.getInt("ajukan"));
						rangkai.setCatasdep(rs.getString("catasdep"));
						rangkai.setCatdepdir(rs.getString("catdepdir"));
						rangkai.setCatdir(rs.getString("catdir"));
						rangkai.setSetujuasdep(rs.getInt("setujuasdep"));
						rangkai.setSetujudepdir(rs.getInt("setujudepdir"));
						rangkai.setSetujudir(rs.getInt("setujudir"));
						rangkai.setPembuat(rs.getInt("pembuat"));
						rangkai.setKodeoffice(rs.getString("kodeoffice"));
						rangkai.setTglajukan(rs.getDate("tglajukan"));
						rangkai.setUseract(rs.getInt("created_by"));
						rangkai.setKoderefrangkaian(rs.getInt("koderefrangkaian"));
						rangkai.setApproval(rs.getString("approval"));
						
						rangkaian.add(rangkai);
						metadata.setCode(1);
						metadata.setMessage("OK");
					}
					response.setList(rangkaian);
					result.setResponse(response);
				}
			} catch (SQLException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
			} catch (NamingException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
				e.printStackTrace();
			} catch (SecurityException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
				e.printStackTrace();
			} catch (InstantiationException e) {
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
	@Path("/save")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces("application/json")
	public Response simpanRangkaian(
			@Context HttpHeaders headers, 
			@FormDataParam("rangkaian") FormDataBodyPart post,
			@FormDataParam("act") String act) {

		Map<String, Object> metadata = new HashMap<String, Object>();
		Map<String, Object> metadataobj = new HashMap<String, Object>();

		if (SharedMethod.VerifyToken(headers, metadata)) {
			try {
				post.setMediaType(MediaType.APPLICATION_JSON_TYPE);
				Rangkaian rangkai = post.getValueAs(Rangkaian.class);

				Connection con = null;
				CallableStatement cs = null;
				String query = null;

				if(act.equalsIgnoreCase("create")) {
					try {
						query = "exec telaah.sp_insert_rangkaian ?,?,?,?,?,?,?,?;";
						//System.out.println(query);
						con = new Koneksi().getConnection();
						cs = con.prepareCall(query);
						cs.setString(1, rangkai.getNama());
						cs.setString(2, rangkai.getNosurat());
						cs.setString(3, rangkai.getCatatan());
						cs.setInt(4, rangkai.getPembuat());
						cs.setString(5, rangkai.getKodeoffice());
						cs.setInt(6, rangkai.getUseract());
						cs.setInt(7, rangkai.getKoderefrangkaian());
						cs.registerOutParameter(8, java.sql.Types.INTEGER);
						cs.execute();

						metadata.put("code", 1);
						metadata.put("message", "Simpan berhasil.");
						metadata.put("kode", cs.getString(8));

					}
					finally {
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
				else if(act.equalsIgnoreCase("update")) {
					try {
						query = "exec telaah.sp_update_rangkaian ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?;";
						//System.out.println(query);
						con = new Koneksi().getConnection();
						cs = con.prepareCall(query);
						cs.setString(1, rangkai.getNama());
						cs.setString(2, rangkai.getNosurat());
						cs.setString(3, rangkai.getCatatan());
						cs.setInt(4, rangkai.getPembuat());
						cs.setString(5, rangkai.getKodeoffice());
						cs.setInt(6, rangkai.getUseract());
						cs.setInt(7, rangkai.getAjukan());
						cs.setDate(8, rangkai.getTglajukan());
						cs.setInt(9, rangkai.getSetujuasdep());
						cs.setInt(10, rangkai.getSetujudepdir());
						cs.setInt(11, rangkai.getSetujudir());
						cs.setString(12, rangkai.getCatasdep());
						cs.setString(13, rangkai.getCatdepdir());
						cs.setString(14, rangkai.getCatdir());
						cs.setInt(15, rangkai.getKoderefrangkaian());
						cs.setInt(16, rangkai.getKode());
						cs.executeUpdate();

						metadata.put("code", 1);
						metadata.put("message", "Update berhasil.");

					}
					finally {
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
			}
			catch (SQLException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				e.printStackTrace();
			}
			catch (Exception e) {
				metadata.put("code", 0);
				metadata.put("message", "Simpan gagal.");
				e.printStackTrace();
			}
		}

		metadataobj.put("metadata", metadata);
		return Response.ok(metadataobj).build();
	}

	@POST
	@Path("/delete")
	@Produces("application/json")
	public Response hapusRangkaian(@Context HttpHeaders headers, String data) {

		Metadata metadata = new Metadata();
		Result<KomponenGaji> result = new Result<KomponenGaji>();

		Connection con = null;
		ResultSet rs = null;
		CallableStatement cs = null;
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
					if(json.path("kode").isMissingNode()){
						str.append("kode").append(", ");
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
					Integer kode = json.path("kode").asInt();
					//String lampiran = json.path("lampiran").asText();

					query = "exec telaah.sp_delete_rangkaian ?";
					con = new Koneksi().getConnection();

					cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
					cs.setInt(1, kode);
					cs.executeUpdate();

					metadata.setCode(1);
					metadata.setMessage("Hapus berhasil.");
				}
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
	@Path("/laporan/telaah/{tgl1}/{tgl2}/{kodeoffice}/{kodereftelaah}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response LaporanTelaah(@Context HttpHeaders headers,
			@PathParam("tgl1") String tgl1, 
			@PathParam("tgl2") String tgl2, 
			@PathParam("kodereftelaah") String kodereftelaah,
			@PathParam("kodeoffice") String kodeoffice,
			String data) {
		
		Map<String, Object> response = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> metadata = new HashMap<String, Object>();
		List<Map<String, Object>> list = null;
		
		Connection con = null;
		ResultSet rs = null;
		CallableStatement cs = null;
		String query = null;

		//if (SharedMethod.VerifyToken(headers, metadata)) {
		if (true) {
			try {
				if(kodereftelaah.equals("0")) {
					kodereftelaah = "";
				}
				if(kodeoffice.equals("0")) {
					kodeoffice = "";
				}
				
				java.sql.Date tanggal1 = new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(tgl1).getTime());
				java.sql.Date tanggal2 = new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(tgl2).getTime());
				
				query = "exec telaah.sp_get_laporan_telaah ?, ?, ?, ?";
				con = new Koneksi().getConnection();
				cs = con.prepareCall(query);
				cs.setDate(1, tanggal1);
				cs.setDate(2, tanggal2);
				cs.setString(3, kodereftelaah);
				cs.setString(4, kodeoffice);
				rs = cs.executeQuery();
				
				list = new ArrayList<Map<String, Object>>();
				ResultSetMetaData metaData = rs.getMetaData();
				Map<String, Object> hasil = null;
				metadata.put("code", 0);
				metadata.put("message", "Data kosong");
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
					list.add(hasil);
					metadata.put("code", 1);
					metadata.put("message", "OK");
				}
				response.put("list", list);
				result.put("response", response);
				rs.close();
				
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