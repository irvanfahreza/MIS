package id.go.bpjskesehatan.service.v2.kinerja;

import java.sql.CallableStatement;
import java.sql.Connection;
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
import id.go.bpjskesehatan.service.v2.entitas.ParamQuery;
import id.go.bpjskesehatan.service.v2.kinerja.entitas.MasterNilaiDetail;
import id.go.bpjskesehatan.service.v2.kinerja.entitas.Peserta;
import id.go.bpjskesehatan.service.v2.kinerja.entitas.UbKomitmenNilai;
import id.go.bpjskesehatan.service.v2.kinerja.entitas.UbKomitmenPenilaian;
import id.go.bpjskesehatan.service.v2.kinerja.entitas.UbKomitmenPeserta;
import id.go.bpjskesehatan.service.v2.kinerja.entitas.UbKomitmenPesertaPenilai;
import id.go.bpjskesehatan.service.v2.kinerja.entitas.UbKomitmenPesertaPenilaiBySistem;
import id.go.bpjskesehatan.util.MyException;
import id.go.bpjskesehatan.util.SharedMethod;

@Path("/v2/ubkomitmen")
public class UbKomitmen2Rest {

	@Context
    private ServletContext context;
	
	@POST
	@Path("/peserta/insert")
	@Consumes("application/json")
	@Produces("application/json")
	public Response insertPesertaUbkomitmen(
			@Context HttpHeaders headers, 
			String data
			) {
		
		Map<String, Object> metadata = new HashMap<String, Object>();
		Map<String, Object> metadataobj = new HashMap<String, Object>();

		AuthUser authUser = new AuthUser();
		if (SharedMethod.VerifyToken(authUser, headers, metadata)) {
			Connection con = null;
			CallableStatement cs = null;
			String query = null;
			try {
				ObjectMapper mapper = new ObjectMapper();
				UbKomitmenPeserta json = mapper.readValue(data, UbKomitmenPeserta.class);
				
				query = "DECLARE @TempList kinerja.pesertaAsTable;";
				List<ParamQuery> paramQueries = new ArrayList<>();
				Integer index = 0;
				ParamQuery param = null;
				for (Peserta item : json.getPesertas()) {
					index++;
					param = new ParamQuery();
					param.setIndex(index);
					param.setType("int");
					param.setValue(item.getKodepenugasan().toString());
					paramQueries.add(param);
					
					query += "insert into @TempList (kodepenugasan) values (?);";
				}
				query += "exec kinerja.sp_ubkomitmenpeserta_insert @TempList,?,?";
				con = new Koneksi().getConnection();
				cs = con.prepareCall(query);
				index = 0;
				for (ParamQuery row : paramQueries) {
					index++;
					if(row.getType().equalsIgnoreCase("int")) {
						cs.setInt(row.getIndex(), Integer.parseInt(row.getValue()));
					}
					else {
						cs.setString(row.getIndex(), row.getValue());
					}
				}
				cs.setInt(index+1, json.getKodeperiodeubkomitmen());
				cs.setInt(index+2, authUser.getUserid());
				cs.executeUpdate();
				
				metadata.put("code", 1);
				metadata.put("message", "Simpan sukses");
			}
			catch (Exception e) {
				metadata.put("code", 0);
				metadata.put("message", "Simpan gagal");
				e.printStackTrace();
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
		
		metadataobj.put("metadata", metadata);
		return Response.ok(metadataobj).build();
	}
	
	@POST
	@Path("/peserta/penilai/insert")
	@Consumes("application/json")
	@Produces("application/json")
	public Response insertPenilaiPesertaUbkomitmen(
			@Context HttpHeaders headers, 
			String data
			) {
		
		Map<String, Object> metadata = new HashMap<String, Object>();
		Map<String, Object> metadataobj = new HashMap<String, Object>();

		AuthUser authUser = new AuthUser();
		if (SharedMethod.VerifyToken(authUser, headers, metadata)) {
			Connection con = null;
			CallableStatement cs = null;
			String query = null;
			try {
				ObjectMapper mapper = new ObjectMapper();
				UbKomitmenPesertaPenilai json = mapper.readValue(data, UbKomitmenPesertaPenilai.class);
				
				query = "DECLARE @TempList kinerja.pesertaAsTable;";
				List<ParamQuery> paramQueries = new ArrayList<>();
				Integer index = 0;
				ParamQuery param = null;
				for (Peserta item : json.getPesertas()) {
					index++;
					param = new ParamQuery();
					param.setIndex(index);
					param.setType("int");
					param.setValue(item.getKodepenugasan().toString());
					paramQueries.add(param);
					
					query += "insert into @TempList (kodepenugasan) values (?);";
				}
				query += "exec kinerja.sp_ubkomitmenpesertapenilai_insert @TempList,?,?";
				con = new Koneksi().getConnection();
				cs = con.prepareCall(query);
				index = 0;
				for (ParamQuery row : paramQueries) {
					index++;
					if(row.getType().equalsIgnoreCase("int")) {
						cs.setInt(row.getIndex(), Integer.parseInt(row.getValue()));
					}
					else {
						cs.setString(row.getIndex(), row.getValue());
					}
				}
				cs.setInt(index+1, json.getKodeubkomitmenpeserta());
				cs.setInt(index+2, authUser.getUserid());
				cs.executeUpdate();
				
				metadata.put("code", 1);
				metadata.put("message", "Simpan sukses");
			}
			catch (SQLException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
			}
			catch (Exception e) {
				metadata.put("code", 0);
				metadata.put("message", "Simpan gagal");
				e.printStackTrace();
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
		
		metadataobj.put("metadata", metadata);
		return Response.ok(metadataobj).build();
	}
	
	@POST
	@Path("/peserta/penilai/generate")
	@Consumes("application/json")
	@Produces("application/json")
	public Response generatePenilaiPesertaUbkomitmen(
			@Context HttpHeaders headers, 
			String data
			) {
		
		Map<String, Object> metadata = new HashMap<String, Object>();
		Map<String, Object> metadataobj = new HashMap<String, Object>();

		AuthUser authUser = new AuthUser();
		if (SharedMethod.VerifyToken(authUser, headers, metadata)) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				UbKomitmenPesertaPenilaiBySistem json = mapper.readValue(data, UbKomitmenPesertaPenilaiBySistem.class);
				
				for (UbKomitmenPesertaPenilai row : json.getPenilais()) {
					UBKomitmenUtil.generateUbKomitmenPesertaPenilai(row.getKodeubkomitmenpeserta(), authUser.getUserid());
				}
				
				metadata.put("code", 1);
				metadata.put("message", "Simpan sukses");
			}
			catch (MyException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
			}
			catch (Exception e) {
				metadata.put("code", 0);
				metadata.put("message", "Simpan gagal");
				e.printStackTrace();
			}
		}
		
		metadataobj.put("metadata", metadata);
		return Response.ok(metadataobj).build();
	}
	
	@GET
	@Path("/getevaluasi/kodenotifikasi/{kodenotifikasi}/fkode/{kodeubkomitmenpesertapenilai}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response getEvaluasiUbKomitmen(
			@Context HttpHeaders headers, 
			@PathParam("kodenotifikasi") Integer kodenotifikasi,
			@PathParam("kodeubkomitmenpesertapenilai") Integer kodeubkomitmenpesertapenilai
		) {
		Map<String, Object> response = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> metadata = new HashMap<String, Object>();

		AuthUser authUser = new AuthUser();
		if (SharedMethod.VerifyToken(authUser, headers, metadata)) {
			try {
				response.put("pegawai", UBKomitmenUtil.getInfoNotifikasi(kodenotifikasi));
				response.put("kriteria", UBKomitmenUtil.getKriteriaPenilaian(kodenotifikasi));
				response.put("komunikasi", UBKomitmenUtil.getKomunikasi(kodenotifikasi));
				
				UbKomitmenPenilaian evaluasi = null;
				List<UbKomitmenNilai> nilais = UBKomitmenUtil.getUbKomitmenNilai(kodenotifikasi, kodeubkomitmenpesertapenilai);
				if(nilais!=null) {
					evaluasi = new UbKomitmenPenilaian();
					evaluasi.setList(nilais);
					evaluasi.setKodenotifikasi(kodenotifikasi);
					evaluasi.setKodeubkomitmenpesertapenilai(kodeubkomitmenpesertapenilai);
				}
				response.put("evaluasi", evaluasi);
				result.put("response", response);
				metadata.put("code", 1);
				metadata.put("message", "OK");
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
	@Path("/penilaian/simpan")
	@Produces("application/json")
	public Response simpanPenilaianUbKomitmen(@Context HttpHeaders headers, String data, @Context HttpServletRequest request) {

		Metadata metadata = new Metadata();
		Result<Object> result = new Result<Object>();

		AuthUser authUser = new AuthUser();
		if (SharedMethod.VerifyToken(authUser, headers, metadata)) {
			Connection con = null;
			CallableStatement cs = null;
			
			try {
				ObjectMapper mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, true);
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				UbKomitmenPenilaian json = mapper.readValue(data, UbKomitmenPenilaian.class);
				
				String query = "DECLARE @TempList kinerja.ubkomitmenpenilaianAsTable;";
				List<ParamQuery> paramQueries = new ArrayList<>();
				Integer index = 0;
				ParamQuery param = null;
				
				for (UbKomitmenNilai ubKomitmenNilai : json.getList()) {
					for (MasterNilaiDetail soal : ubKomitmenNilai.getMasternilai().getDetails()) {
						index++;
						param = new ParamQuery();
						param.setIndex(index);
						param.setName("kodemasternilaidetail");
						param.setType("int");
						param.setValue(soal.getKode().toString());
						paramQueries.add(param);
						
						index++;
						param = new ParamQuery();
						param.setIndex(index);
						param.setName("value");
						param.setType("string");
						param.setValue(soal.getValue());
						paramQueries.add(param);
						
						query += "insert into @TempList (kodemasternilaidetail, value) values (?,?);";
					}
				}
				
				query += "exec kinerja.sp_ubkomitmenpenilaian_insert @TempList,?,?,?,?";
				con = new Koneksi().getConnection();
				cs = con.prepareCall(query);
				
				index = 0;
				for (ParamQuery row : paramQueries) {
					index++;
					if(row.getType().equalsIgnoreCase("int")) {
						cs.setInt(row.getIndex(), Integer.parseInt(row.getValue()));
					}
					else {
						cs.setString(row.getIndex(), row.getValue());
					}
				}
				cs.setInt(index+1, json.getKodenotifikasi());
				cs.setInt(index+2, json.getKodeubkomitmenpesertapenilai());
				cs.setInt(index+3, json.getSubmitted());
				cs.setInt(index+4, authUser.getUserid());
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
}
