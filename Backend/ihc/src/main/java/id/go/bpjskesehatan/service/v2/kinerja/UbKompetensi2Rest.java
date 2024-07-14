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
import id.go.bpjskesehatan.service.v2.kinerja.entitas.Peserta;
import id.go.bpjskesehatan.service.v2.kinerja.entitas.UbKompetensiGrupPenilaiPegawai;
import id.go.bpjskesehatan.service.v2.kinerja.entitas.UbKompetensiHeaderSoal;
import id.go.bpjskesehatan.service.v2.kinerja.entitas.UbKompetensiLevelItem;
import id.go.bpjskesehatan.service.v2.kinerja.entitas.UbKompetensiPenilaianHitung;
import id.go.bpjskesehatan.service.v2.kinerja.entitas.UbKompetensiPenilaianSimpan;
import id.go.bpjskesehatan.service.v2.kinerja.entitas.UbKompetensiPeserta;
import id.go.bpjskesehatan.service.v2.kinerja.entitas.UbKompetensiPesertaPenilai;
import id.go.bpjskesehatan.service.v2.kinerja.entitas.UbKompetensiPesertaPenilaiBySistem;
import id.go.bpjskesehatan.service.v2.kinerja.entitas.UbKompetensiSoal;
import id.go.bpjskesehatan.util.MyException;
import id.go.bpjskesehatan.util.SharedMethod;

@Path("/v2/ubkompetensi")
public class UbKompetensi2Rest {

	@Context
    private ServletContext context;
	
	@POST
	@Path("/peserta/insert")
	@Consumes("application/json")
	@Produces("application/json")
	public Response insertPesertaUbkompetensi(
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
				UbKompetensiPeserta json = mapper.readValue(data, UbKompetensiPeserta.class);
				
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
				query += "exec kinerja.sp_ubkompetensipeserta_insert @TempList,?,?";
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
				cs.setInt(index+1, json.getKodeperiodeubkompetensi());
				cs.setInt(index+2, authUser.getUserid());
				cs.executeUpdate();
				
				metadata.put("code", 1);
				metadata.put("message", "Simpan sukses");
			}
			catch (SQLException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				//e.printStackTrace();
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
	public Response insertPenilaiPesertaUbkompetensi(
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
				UbKompetensiPesertaPenilai json = mapper.readValue(data, UbKompetensiPesertaPenilai.class);
				
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
				query += "exec kinerja.sp_ubkompetensipesertapenilai_insert @TempList,?,?";
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
				cs.setInt(index+1, json.getKodeubkompetensipeserta());
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
	public Response generatePenilaiPesertaUbkompetensi(
			@Context HttpHeaders headers, 
			String data
			) {
		
		Map<String, Object> metadata = new HashMap<String, Object>();
		Map<String, Object> metadataobj = new HashMap<String, Object>();

		AuthUser authUser = new AuthUser();
		if (SharedMethod.VerifyToken(authUser, headers, metadata)) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				UbKompetensiPesertaPenilaiBySistem json = mapper.readValue(data, UbKompetensiPesertaPenilaiBySistem.class);
				
				for (UbKompetensiPesertaPenilai rows : json.getPenilais()) {
					UBKompetensiUtil.generateUbKompetensiPesertaPenilai(rows.getKodeubkompetensipeserta(), authUser.getUserid());
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
	@Path("/getevaluasi/kodenotifikasi/{kodenotifikasi}/fkode/{kodeubkompetensipesertapenilai}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response getEvaluasiUbKompetensi(
			@Context HttpHeaders headers, 
			@PathParam("kodenotifikasi") Integer kodenotifikasi,
			@PathParam("kodeubkompetensipesertapenilai") Integer kodeubkompetensipesertapenilai
		) {
		Map<String, Object> response = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> metadata = new HashMap<String, Object>();

		AuthUser authUser = new AuthUser();
		if (SharedMethod.VerifyToken(authUser, headers, metadata)) {
			try {
				response.put("pegawai", UBKompetensiUtil.getInfoNotifikasi(kodenotifikasi));
				//response.put("kriteria", UBKompetensiUtil.getKriteriaPenilaian(kodenotifikasi));
				response.put("komunikasi", UBKompetensiUtil.getKomunikasi(kodenotifikasi));
				
				List<UbKompetensiHeaderSoal> kompetensis = UBKompetensiUtil.getHeaderSoal(
						kodenotifikasi, kodeubkompetensipesertapenilai
					);
				if(kompetensis != null) {
					for (UbKompetensiHeaderSoal kompetensi : kompetensis) {
						kompetensi.setShow(false);
						kompetensi.setItemjawaban(UBKompetensiUtil.getMasterItemJawaban(kompetensi.getKodemastertipejawaban()));
						
						List<UbKompetensiLevelItem> levels = UBKompetensiUtil.getLevelItem(
								kodeubkompetensipesertapenilai, 
								kompetensi.getKodekompetensi(), 
								kompetensi.getLevel(), 
								kompetensi.getBataslevelbawah(), 
								kompetensi.getBataslevelatas(), 
								kompetensi.getFilled()
							);
						if(levels != null) {
							kompetensi.setLevels(levels);
							
							for (UbKompetensiLevelItem levelItem : levels) {
								levelItem.setShow(false);
								levelItem.setSoals(UBKompetensiUtil.getSoal(
										kodeubkompetensipesertapenilai, 
										kompetensi.getKodekompetensi(), 
										levelItem.getLevel(), 
										kompetensi.getJmlitem(), 
										kompetensi.getFilled()
									));
							}
						}
						
					}
				}
				
				UbKompetensiPenilaianSimpan evaluasi = UBKompetensiUtil.getUbKompetensiPenilaian(kodeubkompetensipesertapenilai);
				if(evaluasi==null) {
					evaluasi = new UbKompetensiPenilaianSimpan();
					evaluasi.setSubmitted(0);
				}
				evaluasi.setKodenotifikasi(kodenotifikasi);
				evaluasi.setKodeubkompetensipesertapenilai(kodeubkompetensipesertapenilai);
				evaluasi.setList(kompetensis);
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
	public Response simpanPenilaianUbKompetensi(@Context HttpHeaders headers, String data, @Context HttpServletRequest request) {

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
				UbKompetensiPenilaianSimpan json = mapper.readValue(data, UbKompetensiPenilaianSimpan.class);
				
				String query = "DECLARE @TempList kinerja.ubkompetensipenilaianAsTable;";
				List<ParamQuery> paramQueries = new ArrayList<>();
				Integer index = 0;
				ParamQuery param = null;
				
				for (UbKompetensiHeaderSoal headerSoal : json.getList()) {
					for (UbKompetensiLevelItem levelItem : headerSoal.getLevels()) {
						for (UbKompetensiSoal soal : levelItem.getSoals()) {
							index++;
							param = new ParamQuery();
							param.setIndex(index);
							param.setName("kodelevelkompetensiindikatoritem");
							param.setType("int");
							param.setValue(soal.getKode().toString());
							paramQueries.add(param);
							
							index++;
							param = new ParamQuery();
							param.setIndex(index);
							param.setName("item");
							param.setType("string");
							param.setValue(soal.getItem());
							paramQueries.add(param);
							
							index++;
							param = new ParamQuery();
							param.setIndex(index);
							param.setName("value");
							param.setType("string");
							param.setValue(soal.getValue());
							paramQueries.add(param);
							
							index++;
							param = new ParamQuery();
							param.setIndex(index);
							param.setName("kodemastertipejawaban");
							param.setType("int");
							param.setValue(headerSoal.getKodemastertipejawaban().toString());
							paramQueries.add(param);
							
							index++;
							param = new ParamQuery();
							param.setIndex(index);
							param.setName("levelmodel");
							param.setType("int");
							param.setValue(headerSoal.getLevel().toString());
							paramQueries.add(param);
							
							query += "insert into @TempList (kodelevelkompetensiindikatoritem, item, [value], kodemastertipejawaban, levelmodel) values (?,?,?,?,?);";
						}
					}
				}
				
				query += "exec kinerja.sp_ubkompetensipenilaian_insert @TempList,?,?,?,?";
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
				cs.setInt(index+2, json.getKodeubkompetensipesertapenilai());
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
	
	@POST
	@Path("/penilaian/hitung")
	@Produces("application/json")
	public Response hitungPenilaianUbKompetensi(@Context HttpHeaders headers, String data, @Context HttpServletRequest request) {

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
				UbKompetensiPenilaianHitung json = mapper.readValue(data, UbKompetensiPenilaianHitung.class);
				
				String query = "exec kinerja.sp_ubkompetensi_hitung ?,?";
				con = new Koneksi().getConnection();
				cs = con.prepareCall(query);
				cs.setInt(1, json.getKodeperiodeubkompetensi());
				cs.setInt(2, authUser.getUserid());
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
	
	@POST
	@Path("/gruppenilai/pegawai/insert")
	@Consumes("application/json")
	@Produces("application/json")
	public Response insertPegawaiGrupPenilaiUbkompetensi(
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
				UbKompetensiGrupPenilaiPegawai json = mapper.readValue(data, UbKompetensiGrupPenilaiPegawai.class);
				
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
				query += "exec kinerja.sp_ubkompetensigruppenilaipegawai_insert @TempList,?,?,?";
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
				cs.setInt(index+1, json.getKodeperiodeubkompetensi());
				cs.setInt(index+2, json.getKodeubkompetensigruppenilai());
				cs.setInt(index+3, authUser.getUserid());
				cs.executeUpdate();
				
				metadata.put("code", 1);
				metadata.put("message", "Simpan sukses");
			}
			catch (SQLException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				//e.printStackTrace();
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
}
