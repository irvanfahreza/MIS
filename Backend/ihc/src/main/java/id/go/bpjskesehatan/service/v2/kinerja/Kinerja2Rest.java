package id.go.bpjskesehatan.service.v2.kinerja;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;
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
import javax.ws.rs.core.Response.Status;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import id.go.bpjskesehatan.database.Koneksi;
import id.go.bpjskesehatan.entitas.Metadata;
import id.go.bpjskesehatan.entitas.Respon;
import id.go.bpjskesehatan.entitas.Result;
import id.go.bpjskesehatan.entitas.karyawan.Penugasan;
import id.go.bpjskesehatan.entitas.kinerja.Komponen;
import id.go.bpjskesehatan.entitas.kinerja.Periodekinerja;
import id.go.bpjskesehatan.entitas.organisasi.Jabatan;
import id.go.bpjskesehatan.entitas.organisasi.JobTitle;
import id.go.bpjskesehatan.entitas.organisasi.Office;
import id.go.bpjskesehatan.entitas.organisasi.UnitKerja;
import id.go.bpjskesehatan.service.HcisRest;
import id.go.bpjskesehatan.service.mobile.v1.AuthUser;
import id.go.bpjskesehatan.service.v2.entitas.ParamQuery;
import id.go.bpjskesehatan.service.v2.karyawan.entitas.Pegawai;
import id.go.bpjskesehatan.service.v2.kinerja.entitas.ApprovalPembinaan;
import id.go.bpjskesehatan.service.v2.kinerja.entitas.ApprovalPerencanaan;
import id.go.bpjskesehatan.service.v2.kinerja.entitas.CreatingFutureLeader;
import id.go.bpjskesehatan.service.v2.kinerja.entitas.CreatingFutureLeaderHeader;
import id.go.bpjskesehatan.service.v2.kinerja.entitas.CreatingFutureLeaderPegPromosi;
import id.go.bpjskesehatan.service.v2.kinerja.entitas.CreatingFutureLeaderVerifikasi;
import id.go.bpjskesehatan.service.v2.kinerja.entitas.EvaluasiKpi;
import id.go.bpjskesehatan.service.v2.kinerja.entitas.HasilKerjaHeader;
import id.go.bpjskesehatan.service.v2.kinerja.entitas.Inovasi;
import id.go.bpjskesehatan.service.v2.kinerja.entitas.InovasiHeader;
import id.go.bpjskesehatan.service.v2.kinerja.entitas.InovasiVerifikasi;
import id.go.bpjskesehatan.service.v2.kinerja.entitas.KejadianKritisNegatif;
import id.go.bpjskesehatan.service.v2.kinerja.entitas.KejadianKritisNegatifHeader;
import id.go.bpjskesehatan.service.v2.kinerja.entitas.KejadianKritisNegatifVerifikasi;
import id.go.bpjskesehatan.service.v2.kinerja.entitas.KinerjaEvaluasiVerifikasiHitung;
import id.go.bpjskesehatan.service.v2.kinerja.entitas.KinerjaPeserta;
import id.go.bpjskesehatan.service.v2.kinerja.entitas.KomitmenHeader;
import id.go.bpjskesehatan.service.v2.kinerja.entitas.PembinaanDetail;
import id.go.bpjskesehatan.service.v2.kinerja.entitas.PeriodeKinerja;
import id.go.bpjskesehatan.service.v2.kinerja.entitas.PeriodeKinerjaDetail;
import id.go.bpjskesehatan.service.v2.kinerja.entitas.Peserta;
import id.go.bpjskesehatan.service.v2.kinerja.entitas.PublikasiKaryaIlmiah;
import id.go.bpjskesehatan.service.v2.kinerja.entitas.PublikasiKaryaIlmiahHeader;
import id.go.bpjskesehatan.service.v2.kinerja.entitas.PublikasiKaryaIlmiahVerifikasi;
import id.go.bpjskesehatan.service.v2.kinerja.entitas.SettingLockHasilKerja;
import id.go.bpjskesehatan.service.v2.kinerja.entitas.SettingLockKpi;
import id.go.bpjskesehatan.service.v2.kinerja.entitas.SettingLockKriteriaKpi;
import id.go.bpjskesehatan.service.v2.kinerja.entitas.SettingLockRencanaAktifitas;
import id.go.bpjskesehatan.service.v2.kinerja.entitas.SimpanEvaluasiKinerja;
import id.go.bpjskesehatan.service.v2.kinerja.entitas.SimpanPembinaanKinerja;
import id.go.bpjskesehatan.service.v2.kinerja.entitas.SimpanSettingTargetDjp;
import id.go.bpjskesehatan.service.v2.kinerja.entitas.SimpanVerifikasiKinerja;
import id.go.bpjskesehatan.service.v2.kinerja.entitas.VerifikasiKpi;
import id.go.bpjskesehatan.util.MyException;
import id.go.bpjskesehatan.util.SharedMethod;
import id.go.bpjskesehatan.util.Utils;

@Path("/v2/kinerja")
public class Kinerja2Rest {	
	
	@Context
    private ServletContext context;
	
	@POST
	@Path("/periode/list/{start}/{limit}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response getGridPeriodeKinerja(
			@Context HttpHeaders headers, 
			@PathParam("start") Integer start,
			@PathParam("limit") Integer limit,
			String data
		) {
		Respon<PeriodeKinerja> response = new Respon<PeriodeKinerja>();
		Metadata metadata = new Metadata();
		Result<PeriodeKinerja> result = new Result<PeriodeKinerja>();
		
		Connection con = null;
		ResultSet rs = null;
		CallableStatement cs = null;
		String order = null;
		String filter = null;
		String query = null;

		if (SharedMethod.VerifyToken(headers, metadata)) {
			try {
				JsonNode json = null;
				if (data != null) {
					if (!data.isEmpty()) {
						ObjectMapper mapper = new ObjectMapper();
						json = mapper.readTree(data);

						order = json.path("sort").isMissingNode() ? null
								: SharedMethod.getSortedColumn(mapper.writeValueAsString(json.path("sort")));
						filter = json.path("filter").isMissingNode() ? null
								: SharedMethod.getFilteredColumn(mapper.writeValueAsString(json.path("filter")), null);
					}
				}
				
				query = "exec kinerja.sp_listperiodekinerja ?, ?, ?, ?, ?";
				con = new Koneksi().getConnection();
				cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
				cs.setInt(1, start);
				cs.setInt(2, limit);
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
				cs.setInt(1, start);
				cs.setInt(2, limit);
				cs.setInt(3, 0);
				cs.setString(4, order);
				cs.setString(5, filter);
				rs = cs.executeQuery();
				
				List<PeriodeKinerja> rows = new ArrayList<>();
				while (rs.next()) {
					PeriodeKinerja row = new PeriodeKinerja();
					row.setKode(rs.getInt("kode"));
					row.setNama(rs.getString("nama"));
					row.setTglmulai(Utils.SqlDateToSqlString(rs.getDate("tglmulai")));
					row.setTglselesai(Utils.SqlDateToSqlString(rs.getDate("tglselesai")));
					row.setNamastatus(rs.getString("namastatus"));
					row.setGenerated(rs.getInt("generated")>0?1:0);
					row.setPublish(rs.getInt("publish"));
					row.setStatus(rs.getInt("status"));
					
					row.setSiklus(KinerjaUtil.getSiklusByPeriodeKinerja(row.getKode()));
					row.setKomponen(KinerjaUtil.getKomponenByPeriodeKinerja(row.getKode()));
					rows.add(row);
					metadata.setCode(1);
					metadata.setMessage("OK");
				}
				response.setList(rows);
				result.setResponse(response);
			} catch (SQLException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
			} catch (NamingException e) {
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
	@Path("/periode/insert")
	@Consumes("application/json")
	@Produces("application/json")
	public Response insertPeriodeKinerja(
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
				PeriodeKinerja json = mapper.readValue(data, PeriodeKinerja.class);
				
				query = "DECLARE @siklus kinerja.periodedetailAsTable;";
				List<ParamQuery> paramQueries = new ArrayList<>();
				Integer index = 0;
				ParamQuery param = null;
				for (PeriodeKinerjaDetail item : json.getSiklus()) {
					index++;
					param = new ParamQuery();
					param.setIndex(index);
					param.setType("int");
					param.setValue(item.getKodesiklus().toString());
					paramQueries.add(param);
					
					index++;
					param = new ParamQuery();
					param.setIndex(index);
					param.setType("date");
					param.setValue(item.getTglmulai());
					paramQueries.add(param);
					
					index++;
					param = new ParamQuery();
					param.setIndex(index);
					param.setType("date");
					param.setValue(item.getTglselesai());
					paramQueries.add(param);
					
					query += "insert into @siklus (kodesiklus, tglmulai, tglselesai) values (?, ?, ?);";
				}
				
				query += "DECLARE @komponen kinerja.komponenAsTable;";
				for (Komponen item : json.getKomponen()) {
					index++;
					param = new ParamQuery();
					param.setIndex(index);
					param.setType("int");
					param.setValue(item.getKode().toString());
					paramQueries.add(param);
					
					query += "insert into @komponen (kode) values (?);";
				}
				
				query += "exec kinerja.sp_insertperiode @siklus,@komponen,?,?,?,?,?,?";
				con = new Koneksi().getConnection();
				cs = con.prepareCall(query);
				index = 0;
				for (ParamQuery row : paramQueries) {
					index++;
					if(row.getType().equalsIgnoreCase("int")) {
						cs.setInt(row.getIndex(), Integer.parseInt(row.getValue()));
					}
					else if(row.getType().equalsIgnoreCase("date")) {
						cs.setDate(row.getIndex(), Utils.StringDateToSqlDate(row.getValue()));
					}
					else {
						cs.setString(row.getIndex(), row.getValue());
					}
				}
				cs.setString(index+1, json.getNama());
				cs.setInt(index+2, json.getGenerated());
				cs.setDate(index+3, Utils.StringDateToSqlDate(json.getTglmulai()));
				cs.setDate(index+4, Utils.StringDateToSqlDate(json.getTglselesai()));
				cs.setInt(index+5, authUser.getUserid());
				cs.setInt(index+6, json.getKodeperiodekinerjaold());
				cs.execute();
				
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
	@Path("/periode/update")
	@Consumes("application/json")
	@Produces("application/json")
	public Response updatePeriodeKinerja(
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
				PeriodeKinerja json = mapper.readValue(data, PeriodeKinerja.class);
				
				query = "DECLARE @siklus kinerja.periodedetailAsTable;";
				List<ParamQuery> paramQueries = new ArrayList<>();
				Integer index = 0;
				ParamQuery param = null;
				for (PeriodeKinerjaDetail item : json.getSiklus()) {
					index++;
					param = new ParamQuery();
					param.setIndex(index);
					param.setType("int");
					param.setValue(item.getKodesiklus().toString());
					paramQueries.add(param);
					
					index++;
					param = new ParamQuery();
					param.setIndex(index);
					param.setType("date");
					param.setValue(item.getTglmulai());
					paramQueries.add(param);
					
					index++;
					param = new ParamQuery();
					param.setIndex(index);
					param.setType("date");
					param.setValue(item.getTglselesai());
					paramQueries.add(param);
					
					query += "insert into @siklus (kodesiklus, tglmulai, tglselesai) values (?, ?, ?);";
				}
				
				query = "DECLARE @komponen kinerja.komponenAsTable;";
				for (Komponen item : json.getKomponen()) {
					index++;
					param = new ParamQuery();
					param.setIndex(index);
					param.setType("int");
					param.setValue(item.getKode().toString());
					paramQueries.add(param);
					
					query += "insert into @komponen (kode) values (?);";
				}
				
				query += "exec kinerja.sp_updateperiode @siklus,@komponen,?,?,?,?,?,?";
				con = new Koneksi().getConnection();
				cs = con.prepareCall(query);
				index = 0;
				for (ParamQuery row : paramQueries) {
					index++;
					if(row.getType().equalsIgnoreCase("int")) {
						cs.setInt(row.getIndex(), Integer.parseInt(row.getValue()));
					}
					else if(row.getType().equalsIgnoreCase("date")) {
						cs.setDate(row.getIndex(), Utils.StringDateToSqlDate(row.getValue()));
					}
					else {
						cs.setString(row.getIndex(), row.getValue());
					}
				}
				cs.setString(index+1, json.getNama());
				cs.setInt(index+2, json.getGenerated());
				cs.setDate(index+3, Utils.StringDateToSqlDate(json.getTglmulai()));
				cs.setDate(index+4, Utils.StringDateToSqlDate(json.getTglselesai()));
				cs.setInt(index+5, authUser.getUserid());
				cs.setInt(index+6, json.getKode());
				cs.execute();
				
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
	@Path("/peserta/insert")
	@Consumes("application/json")
	@Produces("application/json")
	public Response insertPesertaKinerja(
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
				KinerjaPeserta json = mapper.readValue(data, KinerjaPeserta.class);
				
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
				query += "exec kinerja.sp_peserta_insert @TempList,?,?";
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
				cs.setInt(index+1, json.getKodeperiodekinerja());
				cs.setInt(index+2, authUser.getUserid());
				cs.executeUpdate();
				
				metadata.put("code", 1);
				metadata.put("message", "Simpan sukses");
			}
			catch (SQLException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				e.printStackTrace();
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
	@Path("/grid/targetdjp/kodeperiodekinerja/{kodeperiodekinerja}/{start}/{limit}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response GridTargetDjp(
			@Context HttpHeaders headers,
			@PathParam("kodeperiodekinerja") Integer kodeperiodekinerja,
			@PathParam("start") Integer start, 
			@PathParam("limit") Integer limit, 
			String data
	) {
		Map<String, Object> response = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> listdata = null;
		Map<String, Object> metadata = new HashMap<String, Object>();
		Connection con = null;
		ResultSet rs = null;
		CallableStatement cs = null;
		String order = null;
		String filter = null;
		String query = null;

		if (SharedMethod.VerifyToken(headers, metadata)) {
			try {
				JsonNode json = null;
				if (data != null) {
					if (!data.isEmpty()) {
						ObjectMapper mapper = new ObjectMapper();
						json = mapper.readTree(data);

						order = json.path("sort").isMissingNode() ? null
								: SharedMethod.getSortedColumn(mapper.writeValueAsString(json.path("sort")));

						filter = json.path("filter").isMissingNode() ? null
								: SharedMethod.getFilteredColumn(mapper.writeValueAsString(json.path("filter")), null);
					}
				}
				
				query = "exec kinerja.sp_listtargetdjpjobtitle ?,?,?,?,?,?";
				con = new Koneksi().getConnection();
				cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
				cs.setInt(1, kodeperiodekinerja);
				cs.setInt(2, start);
				cs.setInt(3, limit);
				cs.setInt(4, 1);
				cs.setString(5, order);
				cs.setString(6, filter);
				rs = cs.executeQuery();
				metadata.put("code", 1);
				metadata.put("message", Response.Status.NO_CONTENT.toString());
				metadata.put("rowcount", 0);
				if (rs.next()) {
					metadata.put("rowcount", rs.getInt("jumlah"));
				}

				listdata = new ArrayList<Map<String, Object>>();

				rs.close();
				cs.close();
				
				cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
				cs.setInt(1, kodeperiodekinerja);
				cs.setInt(2, start);
				cs.setInt(3, limit);
				cs.setInt(4, 0);
				cs.setString(5, order);
				cs.setString(6, filter);
				rs = cs.executeQuery();
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
	
	@GET
	@Path("/settingtargetdjp/kodeperiodekinerja/{kodeperiodekinerja}/kodejobtitle/{kodejobtitle}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response getSettingTargetDJP(
			@Context HttpHeaders headers, 
			@PathParam("kodeperiodekinerja") Integer kodeperiodekinerja,
			@PathParam("kodejobtitle") String kodejobtitle
		) {
		Map<String, Object> response = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> metadata = new HashMap<String, Object>();

		AuthUser authUser = new AuthUser();
		if (SharedMethod.VerifyToken(authUser, headers, metadata)) {
			try {
				List<SettingLockHasilKerja> list = KinerjaUtil.getSettingLockHasilKerja(kodeperiodekinerja, kodejobtitle);
				if(list!=null) {
					for(SettingLockHasilKerja hasilkerja : list) {
						List<SettingLockKpi> kpis = KinerjaUtil.getSettingLockKpi(hasilkerja.getKodetanggungjawab(), hasilkerja.getKode());
						if(kpis!=null) {
							hasilkerja.setKpi(kpis);
							for(SettingLockKpi kpi : kpis) {
								List<SettingLockKriteriaKpi> kriteriakpis = KinerjaUtil.getSettingLockKriteriaKpi(kodeperiodekinerja, kpi.getKode());
								kpi.setKriteria(kriteriakpis);
							}
						}
					}
				}
				
				response.put("list", list);
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
	@Path("/settingtargetdjp/simpan")
	@Consumes("application/json")
	@Produces("application/json")
	public Response setSettingTargetDJP(
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
				mapper.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, true);
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				SimpanSettingTargetDjp json = mapper.readValue(data, SimpanSettingTargetDjp.class);
				
				query = "DECLARE @Hasilkerja kinerja.settinglockhasilkerjaAsTable;";
				List<ParamQuery> paramQueries = new ArrayList<>();
				Integer index = 0;
				ParamQuery param = null;
				for (SettingLockHasilKerja hasilkerja : json.getList()) {
					index++;
					param = new ParamQuery();
					param.setIndex(index);
					param.setType("int");
					param.setValue(hasilkerja.getKode().toString());
					paramQueries.add(param);
					
					index++;
					param = new ParamQuery();
					param.setIndex(index);
					param.setType("int");
					param.setValue(hasilkerja.getKodetanggungjawab().toString());
					paramQueries.add(param);
					
					index++;
					param = new ParamQuery();
					param.setIndex(index);
					param.setType("string");
					param.setValue(hasilkerja.getTanggungjawabutama());
					paramQueries.add(param);
					
					index++;
					param = new ParamQuery();
					param.setIndex(index);
					param.setType("float");
					param.setValue(hasilkerja.getBobot().toString());
					paramQueries.add(param);
					
					index++;
					param = new ParamQuery();
					param.setIndex(index);
					param.setType("int");
					param.setValue(hasilkerja.getBobotlocked().toString());
					paramQueries.add(param);
					
					query += "insert into @Hasilkerja (kode, kodetanggungjawab, namatanggungjawab, bobot, bobot_locked) values (?, ?, ?, ?, ?);";
				}
				
				query += "DECLARE @Kpi kinerja.settinglockkpiAsTable;";
				for (SettingLockHasilKerja hasilkerja : json.getList()) {
					for(SettingLockKpi kpi : hasilkerja.getKpi()) {
						index++;
						param = new ParamQuery();
						param.setIndex(index);
						param.setType("int");
						param.setValue(hasilkerja.getKodetanggungjawab().toString());
						paramQueries.add(param);
						
						index++;
						param = new ParamQuery();
						param.setIndex(index);
						param.setType("int");
						param.setValue(kpi.getKode().toString());
						paramQueries.add(param);
						
						index++;
						param = new ParamQuery();
						param.setIndex(index);
						param.setType("int");
						param.setValue(hasilkerja.getKode().toString());
						paramQueries.add(param);
						
						index++;
						param = new ParamQuery();
						param.setIndex(index);
						param.setType("int");
						param.setValue(kpi.getKodedetailkpi().toString());
						paramQueries.add(param);
						
						index++;
						param = new ParamQuery();
						param.setIndex(index);
						param.setType("string");
						param.setValue(kpi.getNama());
						paramQueries.add(param);
						
						index++;
						param = new ParamQuery();
						param.setIndex(index);
						param.setType("string");
						param.setValue(kpi.getTarget());
						paramQueries.add(param);
						
						index++;
						param = new ParamQuery();
						param.setIndex(index);
						param.setType("string");
						param.setValue(kpi.getUnitukuran());
						paramQueries.add(param);
						
						index++;
						param = new ParamQuery();
						param.setIndex(index);
						param.setType("string");
						param.setValue(kpi.getSumberdata());
						paramQueries.add(param);
						
						index++;
						param = new ParamQuery();
						param.setIndex(index);
						param.setType("string");
						param.setValue(kpi.getAsumsi());
						paramQueries.add(param);
						
						index++;
						param = new ParamQuery();
						param.setIndex(index);
						param.setType("float");
						param.setValue(kpi.getBobot().toString());
						paramQueries.add(param);
						
						index++;
						param = new ParamQuery();
						param.setIndex(index);
						param.setType("int");
						param.setValue(kpi.getTargetlocked().toString());
						paramQueries.add(param);
						
						index++;
						param = new ParamQuery();
						param.setIndex(index);
						param.setType("int");
						param.setValue(kpi.getUnitukuranlocked().toString());
						paramQueries.add(param);
						
						index++;
						param = new ParamQuery();
						param.setIndex(index);
						param.setType("int");
						param.setValue(kpi.getSumberdatalocked().toString());
						paramQueries.add(param);
						
						index++;
						param = new ParamQuery();
						param.setIndex(index);
						param.setType("int");
						param.setValue(kpi.getAsumsilocked().toString());
						paramQueries.add(param);
						
						index++;
						param = new ParamQuery();
						param.setIndex(index);
						param.setType("int");
						param.setValue(kpi.getBobotlocked().toString());
						paramQueries.add(param);
						
						query += "insert into @Kpi (kodetanggungjawab, kode, kodesettinglock_hasilkerja, kodedetailkpi, namadetailkpi, target, unitukuran, sumberdata, asumsi, bobot, target_locked, unitukuran_locked, sumberdata_locked, asumsi_locked, bobot_locked) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
					}
				}
				
				query += "DECLARE @Kriteriakpi kinerja.settinglockkriteriakpiAsTable;";
				for (SettingLockHasilKerja hasilkerja : json.getList()) {
					for(SettingLockKpi kpi : hasilkerja.getKpi()) {
						for(SettingLockKriteriaKpi kriteria : kpi.getKriteria()) {
							index++;
							param = new ParamQuery();
							param.setIndex(index);
							param.setType("int");
							param.setValue(hasilkerja.getKodetanggungjawab().toString());
							paramQueries.add(param);
							
							index++;
							param = new ParamQuery();
							param.setIndex(index);
							param.setType("int");
							param.setValue(kpi.getKodedetailkpi().toString());
							paramQueries.add(param);
							
							index++;
							param = new ParamQuery();
							param.setIndex(index);
							param.setType("int");
							param.setValue(kriteria.getKode().toString());
							paramQueries.add(param);
							
							index++;
							param = new ParamQuery();
							param.setIndex(index);
							param.setType("int");
							param.setValue(kriteria.getKodekriteria().toString());
							paramQueries.add(param);
							
							index++;
							param = new ParamQuery();
							param.setIndex(index);
							param.setType("int");
							param.setValue(kpi.getKode().toString());
							paramQueries.add(param);
							
							index++;
							param = new ParamQuery();
							param.setIndex(index);
							param.setType("string");
							param.setValue(kriteria.getDeskripsi().toString());
							paramQueries.add(param);
							
							index++;
							param = new ParamQuery();
							param.setIndex(index);
							param.setType("int");
							param.setValue(kriteria.getLocked().toString());
							paramQueries.add(param);
							
							query += "insert into @Kriteriakpi (kodetanggungjawab, kodedetailkpi, kode, kodekriteria, kodesettinglock_kpi, deskripsi, locked) values (?,?,?,?,?,?,?);";
						}
					}
				}
				
				query += "exec kinerja.sp_settinglockdjp_simpan @Hasilkerja,@Kpi,@Kriteriakpi,?,?,?";
				con = new Koneksi().getConnection();
				cs = con.prepareCall(query);
				index = 0;
				for (ParamQuery row : paramQueries) {
					index++;
					if(row.getType().equalsIgnoreCase("int")) {
						cs.setInt(row.getIndex(), Integer.parseInt(row.getValue()));
					}
					else if(row.getType().equalsIgnoreCase("float")) {
						cs.setFloat(row.getIndex(), Float.parseFloat(row.getValue()));
					}
					else {
						cs.setString(row.getIndex(), row.getValue());
					}
				}
				cs.setInt(index+1, json.getKodeperiodekinerja());
				cs.setString(index+2, json.getKodejobtitle());
				cs.setInt(index+3, authUser.getUserid());
				cs.executeUpdate();
				
				metadata.put("code", 1);
				metadata.put("message", "Simpan sukses");
			}
			catch (SQLException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				e.printStackTrace();
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
	
	@GET
	@Path("/perencanaan/kinerja/kodepeserta/{kodepeserta}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response getPerencanaanKinerja(
			@Context HttpHeaders headers, 
			@PathParam("kodepeserta") Integer kodepeserta
		) {
		Map<String, Object> response = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> metadata = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();

		AuthUser authUser = new AuthUser();
		if (SharedMethod.VerifyToken(authUser, headers, metadata)) {
			try {
				List<SettingLockHasilKerja> list = KinerjaUtil.getPerencanaanHasilKerja(kodepeserta);
				if(list!=null) {
					for(SettingLockHasilKerja hasilkerja : list) {
						List<SettingLockKpi> kpis = KinerjaUtil.getPerencanaanKpi(hasilkerja.getKodetanggungjawab(), hasilkerja.getKode(), kodepeserta);
						if(kpis!=null) {
							hasilkerja.setKpi(kpis);
							for(SettingLockKpi kpi : kpis) {
								List<SettingLockKriteriaKpi> kriteriakpis = KinerjaUtil.getPerencanaanKriteriaKpi(kodepeserta, kpi.getKode(), kpi.getKodesettinglockkpi());
								kpi.setKriteria(kriteriakpis);
								
								List<SettingLockRencanaAktifitas> rencanaAktifitas = KinerjaUtil.getPerencanaanRencanaAktifitas(kpi.getKode(), kpi.getKodedetailkpi());
								if(rencanaAktifitas!=null) {
									for(int i=0;i<rencanaAktifitas.size();i++) {
										rencanaAktifitas.get(i).setShow(1);
										rencanaAktifitas.get(i).setBtnadd(0);
										rencanaAktifitas.get(i).setBtnremove(1);
										if(i==0) {
											rencanaAktifitas.get(i).setBtnadd(1);
											rencanaAktifitas.get(i).setBtnremove(0);
										}
									}
									kpi.setRencanaaktifitas(rencanaAktifitas);
								}
								else {
									SettingLockRencanaAktifitas aktivitas = new SettingLockRencanaAktifitas();
									aktivitas.setKode(0);
									aktivitas.setDeskripsi("");
									aktivitas.setTarget("");
									aktivitas.setKodedetailkpi(kpi.getKodedetailkpi());
									aktivitas.setShow(1);
									aktivitas.setBtnadd(1);
									aktivitas.setBtnremove(0);
									List<SettingLockRencanaAktifitas> aktivitass = new ArrayList<SettingLockRencanaAktifitas>();
									aktivitass.add(aktivitas);
									kpi.setRencanaaktifitas(aktivitass);
								}
							}
						}
					}
				}
				
				List<Map<String, Object>> catatans = KinerjaUtil.getListCatatanPerencanaan(kodepeserta);
				data.put("list", list);
				data.put("catatans", catatans);
				data.put("pegawai", HcisRest.getRowDetil(1, kodepeserta));
				
				response.put("data", data);
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
	@Path("/perencanaan/kinerja/simpan")
	@Consumes("application/json")
	@Produces("application/json")
	public Response setPerencanaanKinerja(
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
				mapper.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, true);
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				SimpanSettingTargetDjp json = mapper.readValue(data, SimpanSettingTargetDjp.class);
				
				query = "DECLARE @Hasilkerja kinerja.settinglockhasilkerjaAsTable;";
				List<ParamQuery> paramQueries = new ArrayList<>();
				Integer index = 0;
				ParamQuery param = null;
				for (SettingLockHasilKerja hasilkerja : json.getList()) {
					index++;
					param = new ParamQuery();
					param.setIndex(index);
					param.setType("int");
					param.setValue(hasilkerja.getKode().toString());
					paramQueries.add(param);
					
					index++;
					param = new ParamQuery();
					param.setIndex(index);
					param.setType("int");
					param.setValue(hasilkerja.getKodetanggungjawab().toString());
					paramQueries.add(param);
					
					index++;
					param = new ParamQuery();
					param.setIndex(index);
					param.setType("string");
					param.setValue(hasilkerja.getTanggungjawabutama());
					paramQueries.add(param);
					
					index++;
					param = new ParamQuery();
					param.setIndex(index);
					param.setType("float");
					param.setValue(hasilkerja.getBobot().toString());
					paramQueries.add(param);
					
					query += "insert into @Hasilkerja (kode, kodetanggungjawab, namatanggungjawab, bobot) values (?, ?, ?, ?);";
				}
				
				query += "DECLARE @Kpi kinerja.settinglockkpiAsTable;";
				for (SettingLockHasilKerja hasilkerja : json.getList()) {
					for(SettingLockKpi kpi : hasilkerja.getKpi()) {
						index++;
						param = new ParamQuery();
						param.setIndex(index);
						param.setType("int");
						param.setValue(hasilkerja.getKodetanggungjawab().toString());
						paramQueries.add(param);
						
						index++;
						param = new ParamQuery();
						param.setIndex(index);
						param.setType("int");
						param.setValue(kpi.getKodedetailkpi().toString());
						paramQueries.add(param);
						
						index++;
						param = new ParamQuery();
						param.setIndex(index);
						param.setType("string");
						param.setValue(kpi.getNama());
						paramQueries.add(param);
						
						index++;
						param = new ParamQuery();
						param.setIndex(index);
						param.setType("string");
						param.setValue(kpi.getTarget());
						paramQueries.add(param);
						
						index++;
						param = new ParamQuery();
						param.setIndex(index);
						param.setType("string");
						param.setValue(kpi.getUnitukuran());
						paramQueries.add(param);
						
						index++;
						param = new ParamQuery();
						param.setIndex(index);
						param.setType("string");
						param.setValue(kpi.getSumberdata());
						paramQueries.add(param);
						
						index++;
						param = new ParamQuery();
						param.setIndex(index);
						param.setType("string");
						param.setValue(kpi.getAsumsi());
						paramQueries.add(param);
						
						index++;
						param = new ParamQuery();
						param.setIndex(index);
						param.setType("float");
						param.setValue(kpi.getBobot().toString());
						paramQueries.add(param);
						
						query += "insert into @Kpi (kodetanggungjawab, kodedetailkpi, namadetailkpi, target, unitukuran, sumberdata, asumsi, bobot) values (?, ?, ?, ?, ?, ?, ?, ?);";
					}
				}
				
				query += "DECLARE @Kriteriakpi kinerja.settinglockkriteriakpiAsTable;";
				query += "DECLARE @RencanaAktivitas kinerja.settinglockrencanaaktivitasAsTable;";
				for (SettingLockHasilKerja hasilkerja : json.getList()) {
					for(SettingLockKpi kpi : hasilkerja.getKpi()) {
						for(SettingLockKriteriaKpi kriteria : kpi.getKriteria()) {
							index++;
							param = new ParamQuery();
							param.setIndex(index);
							param.setType("int");
							param.setValue(hasilkerja.getKodetanggungjawab().toString());
							paramQueries.add(param);
							
							index++;
							param = new ParamQuery();
							param.setIndex(index);
							param.setType("int");
							param.setValue(kpi.getKodedetailkpi().toString());
							paramQueries.add(param);
							
							index++;
							param = new ParamQuery();
							param.setIndex(index);
							param.setType("int");
							param.setValue(kriteria.getKodekriteria().toString());
							paramQueries.add(param);
							
							index++;
							param = new ParamQuery();
							param.setIndex(index);
							param.setType("int");
							param.setValue(kpi.getKode().toString());
							paramQueries.add(param);
							
							index++;
							param = new ParamQuery();
							param.setIndex(index);
							param.setType("string");
							param.setValue(kriteria.getDeskripsi().toString());
							paramQueries.add(param);
							
							index++;
							param = new ParamQuery();
							param.setIndex(index);
							param.setType("int");
							param.setValue(kriteria.getRating().toString());
							paramQueries.add(param);
							
							index++;
							param = new ParamQuery();
							param.setIndex(index);
							param.setType("string");
							param.setValue(kriteria.getDefinisi());
							paramQueries.add(param);
							
							query += "insert into @Kriteriakpi (kodetanggungjawab, kodedetailkpi, kodekriteria, kodesettinglock_kpi, deskripsi, rating, definisi) values (?,?,?,?,?,?,?);";
						}
						
						for(SettingLockRencanaAktifitas aktivitas : kpi.getRencanaaktifitas()) {
							if(
								(aktivitas.getKode()==0 && !aktivitas.getDeskripsi().isEmpty() && aktivitas.getShow()==1) ||
								(aktivitas.getKode()>0 && aktivitas.getShow()==0)	
							) {
								index++;
								param = new ParamQuery();
								param.setIndex(index);
								param.setType("int");
								param.setValue(aktivitas.getKode().toString());
								paramQueries.add(param);
								
								index++;
								param = new ParamQuery();
								param.setIndex(index);
								param.setType("int");
								param.setValue(kpi.getKodedetailkpi().toString());
								paramQueries.add(param);
								
								index++;
								param = new ParamQuery();
								param.setIndex(index);
								param.setType("string");
								param.setValue(aktivitas.getDeskripsi());
								paramQueries.add(param);
								
								index++;
								param = new ParamQuery();
								param.setIndex(index);
								param.setType("int");
								param.setValue(aktivitas.getShow().toString());
								paramQueries.add(param);
								
								index++;
								param = new ParamQuery();
								param.setIndex(index);
								param.setType("string");
								param.setValue(aktivitas.getTarget());
								paramQueries.add(param);
								
								index++;
								param = new ParamQuery();
								param.setIndex(index);
								param.setType("date");
								param.setValue(Utils.SqlDateToSqlString(aktivitas.getTargettgl()));
								paramQueries.add(param);
								
								query += "insert into @RencanaAktivitas (kode, kodedetailkpi, deskripsi, show, target, targettgl) values (?,?,?,?,?,?);";
							}
						}
					}
				}
				
				query += "exec kinerja.sp_perencanaan_simpan @Hasilkerja,@Kpi,@Kriteriakpi,@RencanaAktivitas,?,?,?,?";
				con = new Koneksi().getConnection();
				cs = con.prepareCall(query);
				index = 0;
				for (ParamQuery row : paramQueries) {
					index++;
					if(row.getType().equalsIgnoreCase("int")) {
						cs.setInt(row.getIndex(), Integer.parseInt(row.getValue()));
					}
					else if(row.getType().equalsIgnoreCase("float")) {
						cs.setFloat(row.getIndex(), Float.parseFloat(row.getValue()));
					}
					else if(row.getType().equalsIgnoreCase("date")) {
						cs.setDate(row.getIndex(), Utils.StringDateToSqlDate(row.getValue()));
					}
					else {
						cs.setString(row.getIndex(), row.getValue());
					}
				}
				cs.setInt(index+1, json.getKodepeserta());
				cs.setInt(index+2, json.getStatus());
				cs.setInt(index+3, authUser.getUserid());
				cs.registerOutParameter(index+4, java.sql.Types.INTEGER);
				cs.executeUpdate();
				
				metadata.put("code", 1);
				metadata.put("message", "Simpan sukses");
			}
			catch (SQLException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				e.printStackTrace();
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
	
	@GET
	@Path("/perencanaan/kinerja/tinjau/kodeperencanaan/{kodeperencanaan}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response getPeninjauanPerencanaanKinerja(
			@Context HttpHeaders headers, 
			@PathParam("kodeperencanaan") Integer kodeperencanaan
		) {
		Map<String, Object> response = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> metadata = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();

		AuthUser authUser = new AuthUser();
		if (SharedMethod.VerifyToken(authUser, headers, metadata)) {
			try {
				Integer kodepeserta = KinerjaUtil.getKodePesertaByKodePerencanaan(kodeperencanaan);
				List<SettingLockHasilKerja> list = KinerjaUtil.getPerencanaanHasilKerja(kodepeserta);
				if(list!=null) {
					for(SettingLockHasilKerja hasilkerja : list) {
						List<SettingLockKpi> kpis = KinerjaUtil.getPerencanaanKpi(hasilkerja.getKodetanggungjawab(), hasilkerja.getKode(), kodepeserta);
						if(kpis!=null) {
							hasilkerja.setKpi(kpis);
							for(SettingLockKpi kpi : kpis) {
								List<SettingLockKriteriaKpi> kriteriakpis = KinerjaUtil.getPerencanaanKriteriaKpi(kodepeserta, kpi.getKode(), kpi.getKodesettinglockkpi());
								kpi.setKriteria(kriteriakpis);
								
								List<SettingLockRencanaAktifitas> rencanaAktifitas = KinerjaUtil.getPerencanaanRencanaAktifitas(kpi.getKode(), kpi.getKodedetailkpi());
								if(rencanaAktifitas!=null) {
									for(int i=0;i<rencanaAktifitas.size();i++) {
										rencanaAktifitas.get(i).setShow(1);
										rencanaAktifitas.get(i).setBtnadd(0);
										rencanaAktifitas.get(i).setBtnremove(1);
										if(i==0) {
											rencanaAktifitas.get(i).setBtnadd(1);
											rencanaAktifitas.get(i).setBtnremove(0);
										}
									}
									kpi.setRencanaaktifitas(rencanaAktifitas);
								}
								else {
									SettingLockRencanaAktifitas aktivitas = new SettingLockRencanaAktifitas();
									aktivitas.setKode(0);
									aktivitas.setDeskripsi("");
									aktivitas.setKodedetailkpi(kpi.getKodedetailkpi());
									aktivitas.setShow(1);
									aktivitas.setBtnadd(1);
									aktivitas.setBtnremove(0);
									List<SettingLockRencanaAktifitas> aktivitass = new ArrayList<SettingLockRencanaAktifitas>();
									aktivitass.add(aktivitas);
									kpi.setRencanaaktifitas(aktivitass);
								}
							}
						}
					}
				}
				
				List<Map<String, Object>> catatans = KinerjaUtil.getListCatatanPerencanaan(kodepeserta);
				data.put("list", list);
				data.put("catatans", catatans);
				data.put("pegawai", HcisRest.getRowDetil(2, kodeperencanaan));
				
				response.put("data", data);
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
	@Path("/perencanaan/kinerja/tinjau/simpan")
	@Consumes("application/json")
	@Produces("application/json")
	public Response setApprovalPerencanaanKinerja(
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
				mapper.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, true);
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				ApprovalPerencanaan json = mapper.readValue(data, ApprovalPerencanaan.class);
				
				Integer kodepeserta = KinerjaUtil.getKodePesertaByKodePerencanaan(json.getKodeperencanaan());
				query = "DECLARE @Hasilkerja kinerja.settinglockhasilkerjaAsTable;";
				List<ParamQuery> paramQueries = new ArrayList<>();
				Integer index = 0;
				ParamQuery param = null;
				for (SettingLockHasilKerja hasilkerja : json.getList()) {
					index++;
					param = new ParamQuery();
					param.setIndex(index);
					param.setType("int");
					param.setValue(hasilkerja.getKode().toString());
					paramQueries.add(param);
					
					index++;
					param = new ParamQuery();
					param.setIndex(index);
					param.setType("int");
					param.setValue(hasilkerja.getKodetanggungjawab().toString());
					paramQueries.add(param);
					
					index++;
					param = new ParamQuery();
					param.setIndex(index);
					param.setType("string");
					param.setValue(hasilkerja.getTanggungjawabutama());
					paramQueries.add(param);
					
					index++;
					param = new ParamQuery();
					param.setIndex(index);
					param.setType("float");
					param.setValue(hasilkerja.getBobot().toString());
					paramQueries.add(param);
					
					query += "insert into @Hasilkerja (kode, kodetanggungjawab, namatanggungjawab, bobot) values (?, ?, ?, ?);";
				}
				
				query += "DECLARE @Kpi kinerja.settinglockkpiAsTable;";
				for (SettingLockHasilKerja hasilkerja : json.getList()) {
					for(SettingLockKpi kpi : hasilkerja.getKpi()) {
						index++;
						param = new ParamQuery();
						param.setIndex(index);
						param.setType("int");
						param.setValue(hasilkerja.getKodetanggungjawab().toString());
						paramQueries.add(param);
						
						index++;
						param = new ParamQuery();
						param.setIndex(index);
						param.setType("int");
						param.setValue(kpi.getKodedetailkpi().toString());
						paramQueries.add(param);
						
						index++;
						param = new ParamQuery();
						param.setIndex(index);
						param.setType("string");
						param.setValue(kpi.getNama());
						paramQueries.add(param);
						
						index++;
						param = new ParamQuery();
						param.setIndex(index);
						param.setType("string");
						param.setValue(kpi.getTarget());
						paramQueries.add(param);
						
						index++;
						param = new ParamQuery();
						param.setIndex(index);
						param.setType("string");
						param.setValue(kpi.getUnitukuran());
						paramQueries.add(param);
						
						index++;
						param = new ParamQuery();
						param.setIndex(index);
						param.setType("string");
						param.setValue(kpi.getSumberdata());
						paramQueries.add(param);
						
						index++;
						param = new ParamQuery();
						param.setIndex(index);
						param.setType("string");
						param.setValue(kpi.getAsumsi());
						paramQueries.add(param);
						
						index++;
						param = new ParamQuery();
						param.setIndex(index);
						param.setType("float");
						param.setValue(kpi.getBobot().toString());
						paramQueries.add(param);
						
						query += "insert into @Kpi (kodetanggungjawab, kodedetailkpi, namadetailkpi, target, unitukuran, sumberdata, asumsi, bobot) values (?, ?, ?, ?, ?, ?, ?, ?);";
					}
				}
				
				query += "DECLARE @Kriteriakpi kinerja.settinglockkriteriakpiAsTable;";
				query += "DECLARE @RencanaAktivitas kinerja.settinglockrencanaaktivitasAsTable;";
				for (SettingLockHasilKerja hasilkerja : json.getList()) {
					for(SettingLockKpi kpi : hasilkerja.getKpi()) {
						for(SettingLockKriteriaKpi kriteria : kpi.getKriteria()) {
							index++;
							param = new ParamQuery();
							param.setIndex(index);
							param.setType("int");
							param.setValue(hasilkerja.getKodetanggungjawab().toString());
							paramQueries.add(param);
							
							index++;
							param = new ParamQuery();
							param.setIndex(index);
							param.setType("int");
							param.setValue(kpi.getKodedetailkpi().toString());
							paramQueries.add(param);
							
							index++;
							param = new ParamQuery();
							param.setIndex(index);
							param.setType("int");
							param.setValue(kriteria.getKodekriteria().toString());
							paramQueries.add(param);
							
							index++;
							param = new ParamQuery();
							param.setIndex(index);
							param.setType("int");
							param.setValue(kpi.getKode().toString());
							paramQueries.add(param);
							
							index++;
							param = new ParamQuery();
							param.setIndex(index);
							param.setType("string");
							param.setValue(kriteria.getDeskripsi().toString());
							paramQueries.add(param);
							
							index++;
							param = new ParamQuery();
							param.setIndex(index);
							param.setType("int");
							param.setValue(kriteria.getRating().toString());
							paramQueries.add(param);
							
							index++;
							param = new ParamQuery();
							param.setIndex(index);
							param.setType("string");
							param.setValue(kriteria.getDefinisi());
							paramQueries.add(param);
							
							query += "insert into @Kriteriakpi (kodetanggungjawab, kodedetailkpi, kodekriteria, kodesettinglock_kpi, deskripsi, rating, definisi) values (?,?,?,?,?,?,?);";
						}
						
						for(SettingLockRencanaAktifitas aktivitas : kpi.getRencanaaktifitas()) {
							if(
								(aktivitas.getKode()==0 && !aktivitas.getDeskripsi().isEmpty() && aktivitas.getShow()==1) ||
								(aktivitas.getKode()>0 && aktivitas.getShow()==0)	
							) {
								index++;
								param = new ParamQuery();
								param.setIndex(index);
								param.setType("int");
								param.setValue(aktivitas.getKode().toString());
								paramQueries.add(param);
								
								index++;
								param = new ParamQuery();
								param.setIndex(index);
								param.setType("int");
								param.setValue(kpi.getKodedetailkpi().toString());
								paramQueries.add(param);
								
								index++;
								param = new ParamQuery();
								param.setIndex(index);
								param.setType("string");
								param.setValue(aktivitas.getDeskripsi());
								paramQueries.add(param);
								
								index++;
								param = new ParamQuery();
								param.setIndex(index);
								param.setType("int");
								param.setValue(aktivitas.getShow().toString());
								paramQueries.add(param);
								
								query += "insert into @RencanaAktivitas (kode, kodedetailkpi, deskripsi, show) values (?,?,?,?);";
							}
						}
					}
				}
				
				query += "exec kinerja.sp_perencanaan_simpan @Hasilkerja,@Kpi,@Kriteriakpi,@RencanaAktivitas,?,?,?,?";
				con = new Koneksi().getConnection();
				cs = con.prepareCall(query);
				index = 0;
				for (ParamQuery row : paramQueries) {
					index++;
					if(row.getType().equalsIgnoreCase("int")) {
						cs.setInt(row.getIndex(), Integer.parseInt(row.getValue()));
					}
					else if(row.getType().equalsIgnoreCase("float")) {
						cs.setFloat(row.getIndex(), Float.parseFloat(row.getValue()));
					}
					else {
						cs.setString(row.getIndex(), row.getValue());
					}
				}
				cs.setInt(index+1, kodepeserta);
				cs.setInt(index+2, 0);
				cs.setInt(index+3, authUser.getUserid());
				cs.registerOutParameter(index+4, java.sql.Types.INTEGER);
				cs.executeUpdate();
				
				KinerjaUtil.simpanCatatanPerencanaan(
					json.getKodeperencanaan(), 
					json.getFlag(), 
					json.getCatatan(), 
					json.getKodepenugasan(), 
					authUser.getUserid()
				);
				
				metadata.put("code", 1);
				metadata.put("message", "Simpan sukses");
			}
			catch (SQLException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				e.printStackTrace();
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
	
	@GET
	@Path("/pembinaan/kinerja/kodeperencanaan/{kodeperencanaan}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response getPembinaanKinerja(
			@Context HttpHeaders headers, 
			@PathParam("kodeperencanaan") Integer kodeperencanaan
		) {
		Map<String, Object> response = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> metadata = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();

		AuthUser authUser = new AuthUser();
		if (SharedMethod.VerifyToken(authUser, headers, metadata)) {
			try {
				SimpanPembinaanKinerja pembinaankinerja = new SimpanPembinaanKinerja();
				pembinaankinerja.setKodeperencanaan(kodeperencanaan);
				Integer kodepeserta = KinerjaUtil.getKodePesertaByKodePerencanaan(kodeperencanaan);
				pembinaankinerja.setKodepeserta(kodepeserta);
				List<Komponen> komponens = KinerjaUtil.getKomponenByPeserta(kodepeserta);
				if(komponens!=null) {
					for(Komponen komponen : komponens) {
						if(komponen.getKode()==1) {
							List<SettingLockHasilKerja> hasilkerjas = KinerjaUtil.getPerencanaanHasilKerja(kodepeserta);
							if(hasilkerjas!=null) {
								for(SettingLockHasilKerja hasilkerja : hasilkerjas) {
									List<SettingLockKpi> kpis = KinerjaUtil.getPerencanaanKpi(hasilkerja.getKodetanggungjawab(), hasilkerja.getKode(), kodepeserta);
									if(kpis!=null) {
										hasilkerja.setKpi(kpis);
										for(SettingLockKpi kpi : kpis) {
											List<SettingLockKriteriaKpi> kriteriakpis = KinerjaUtil.getPerencanaanKriteriaKpi(kodepeserta, kpi.getKode(), kpi.getKodesettinglockkpi());
											kpi.setKriteria(kriteriakpis);
											
											List<SettingLockRencanaAktifitas> rencanaAktifitas = KinerjaUtil.getPerencanaanRencanaAktifitas(kpi.getKode(), kpi.getKodedetailkpi());
											kpi.setRencanaaktifitas(rencanaAktifitas);
											
											List<PembinaanDetail> details = KinerjaUtil.getPembinaanDetail(kpi.getKode());
											if(details==null) {
												details = new ArrayList<>();
											}
											PembinaanDetail detail = new PembinaanDetail();
											detail.setKode(0);
											detail.setPencapaian("");
											detail.setDetil("");
											detail.setLampiran("");
											detail.setCatatan("");
											detail.setFlag(0);
											details.add(detail);
											kpi.setPembinaandetails(details);
										}
									}
								}
							}
							HasilKerjaHeader hasilKerjaHeader = new HasilKerjaHeader();
							hasilKerjaHeader.setHasilkerjas(hasilkerjas);
							komponen.setHasilkerja(hasilKerjaHeader);
						}
						else if(komponen.getKode()==3) {
							KomitmenHeader komitmenHeader = new KomitmenHeader();
							komitmenHeader.setKriterias(KinerjaUtil.getKriterias(komponen.getKode(), kodepeserta));
							komponen.setKomitmen(komitmenHeader);
						}
						else if(komponen.getKode()==5) {
							KejadianKritisNegatifHeader kejadianKritisNegatifHeader = new KejadianKritisNegatifHeader();
							kejadianKritisNegatifHeader.setKriterias(KinerjaUtil.getKriterias(komponen.getKode(), kodepeserta));
							komponen.setKejadiankritisnegatif(kejadianKritisNegatifHeader);
						}
						else if(komponen.getKode()==6) {
							List<Inovasi> inovasis = KinerjaUtil.getPembinaanInovasi(kodeperencanaan);
							if(inovasis!=null) {
								for(int i=0;i<inovasis.size();i++) {
									inovasis.get(i).setShow(1);
									inovasis.get(i).setBtnadd(0);
									inovasis.get(i).setBtnremove(1);
									if(i==0) {
										inovasis.get(i).setBtnadd(1);
										inovasis.get(i).setBtnremove(0);
									}
								}
							}
							else {
								inovasis = new ArrayList<>();
								Inovasi inovasi = new Inovasi();
								inovasi.setNama("");
								inovasi.setDeskripsi("");
								inovasi.setLampiran("");
								inovasi.setShow(1);
								inovasi.setBtnadd(1);
								inovasi.setBtnremove(0);
								inovasis.add(inovasi);
							}
							InovasiHeader inovasiHeader = new InovasiHeader();
							inovasiHeader.setInovasis(inovasis);
							inovasiHeader.setKriterias(KinerjaUtil.getKriterias(komponen.getKode(), kodepeserta));
							komponen.setInovasi(inovasiHeader);
						}
						else if(komponen.getKode()==7) {
							CreatingFutureLeader creatingfutureleader = KinerjaUtil.getCreatingFutureLeader(kodeperencanaan);
							if(creatingfutureleader!=null) {
								List<CreatingFutureLeaderPegPromosi> pegpromosis = creatingfutureleader.getPegpromosis();
								if(pegpromosis!=null) {
									for(int i=0;i<pegpromosis.size();i++) {
										pegpromosis.get(i).setShow(1);
										pegpromosis.get(i).setBtnadd(0);
										pegpromosis.get(i).setBtnremove(1);
										if(i==0) {
											pegpromosis.get(i).setBtnadd(1);
											pegpromosis.get(i).setBtnremove(0);
										}
									}
								}
							}
							else {
								creatingfutureleader = new CreatingFutureLeader();
								List<CreatingFutureLeaderPegPromosi> pegpromosis = new ArrayList<>();
								CreatingFutureLeaderPegPromosi pegpromosi = new CreatingFutureLeaderPegPromosi();
								pegpromosi.setShow(1);
								pegpromosi.setBtnadd(1);
								pegpromosi.setBtnremove(0);
								Pegawai pegawai = new Pegawai();
								pegawai.setNpp("");
								pegawai.setNama("");
								pegpromosi.setLampiran("");
								pegpromosi.setPegawai(pegawai);
								pegpromosis.add(pegpromosi);
								creatingfutureleader.setPegpromosis(pegpromosis);
							}
							CreatingFutureLeaderHeader creatingFutureLeaderHeader = new CreatingFutureLeaderHeader();
							creatingFutureLeaderHeader.setKriterias(KinerjaUtil.getKriterias(komponen.getKode(), kodepeserta));
							creatingFutureLeaderHeader.setCreatingfutureleader(creatingfutureleader);
							komponen.setCreatingfutureleader(creatingFutureLeaderHeader);
						}
						else if(komponen.getKode()==8) {
							List<PublikasiKaryaIlmiah> publikasikaryailmiahs = KinerjaUtil.getPembinaanPublikasiKaryaIlmiah(kodeperencanaan);
							if(publikasikaryailmiahs!=null) {
								for(int i=0;i<publikasikaryailmiahs.size();i++) {
									publikasikaryailmiahs.get(i).setShow(1);
									publikasikaryailmiahs.get(i).setBtnadd(0);
									publikasikaryailmiahs.get(i).setBtnremove(1);
									if(i==0) {
										publikasikaryailmiahs.get(i).setBtnadd(1);
										publikasikaryailmiahs.get(i).setBtnremove(0);
									}
								}
							}
							else {
								publikasikaryailmiahs = new ArrayList<>();
								PublikasiKaryaIlmiah pki = new PublikasiKaryaIlmiah();
								pki.setShow(1);
								pki.setBtnadd(1);
								pki.setBtnremove(0);
								publikasikaryailmiahs.add(pki);
							}
							PublikasiKaryaIlmiahHeader publikasiKaryaIlmiahHeader = new PublikasiKaryaIlmiahHeader();
							publikasiKaryaIlmiahHeader.setKriterias(KinerjaUtil.getKriterias(komponen.getKode(), kodepeserta));
							publikasiKaryaIlmiahHeader.setPublikasikaryailmiahs(publikasikaryailmiahs);
							komponen.setPublikasikaryailmiah(publikasiKaryaIlmiahHeader);
						}
					}
				}
				pembinaankinerja.setKomponens(komponens);
				List<Penugasan> bawahans = KinerjaUtil.getListBawahan(kodepeserta);
				pembinaankinerja.setBawahans(bawahans);
				
				List<Map<String, Object>> catatans = KinerjaUtil.getListCatatanPerencanaan(kodepeserta);
				data.put("pembinaan", pembinaankinerja);
				data.put("catatans", catatans);
				data.put("pegawai", HcisRest.getRowDetil(2, kodeperencanaan));
				
				response.put("data", data);
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
	@Path("/pembinaan/kinerja/simpan")
	@Consumes("application/json")
	@Produces("application/json")
	public Response setPembinaanKinerja(
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
				mapper.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, true);
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				SimpanPembinaanKinerja json = mapper.readValue(data, SimpanPembinaanKinerja.class);
				
				query = "DECLARE @Hasilkerja kinerja.settinglockhasilkerjaAsTable;";
				List<ParamQuery> paramQueries = new ArrayList<>();
				Integer index = 0;
				ParamQuery param = null;
				
				List<SettingLockHasilKerja> hasilkerjas = null;
				List<Inovasi> inovasis = null;
				CreatingFutureLeader creatingfutureleader = null;
				List<PublikasiKaryaIlmiah> publikasikaryailmiahs = null;
				for (Komponen komponen : json.getKomponens()) {
					switch (komponen.getKode()) {
					case 1:
						hasilkerjas = komponen.getHasilkerja().getHasilkerjas();
						break;
					case 6:
						inovasis = komponen.getInovasi().getInovasis();
						break;
					case 7:
						creatingfutureleader = komponen.getCreatingfutureleader().getCreatingfutureleader();
						break;
					case 8:
						publikasikaryailmiahs = komponen.getPublikasikaryailmiah().getPublikasikaryailmiahs();
						break;
					default:
						break;
					}
				}
				
				if(creatingfutureleader==null) 
					creatingfutureleader = new CreatingFutureLeader();
				
				query += "DECLARE @RencanaAktivitas kinerja.settinglockrencanaaktivitasAsTable;";
				query += "DECLARE @PembinaanDetail kinerja.pembinaandetailAsTable;";
				query += "DECLARE @PembinaanInovasi kinerja.pembinaaninovasiAsTable;";
				query += "DECLARE @PembinaanPublikasiKaryaIlmiah kinerja.pembinaanpublikasikaryailmiahAsTable;";
				query += "DECLARE @PembinaanCreatingFutureLeaderPegPromosi kinerja.pembinaancreatingfutureleaderpegpromosiAsTable;";
				
				for (SettingLockHasilKerja hasilkerja : hasilkerjas) {
					for(SettingLockKpi kpi : hasilkerja.getKpi()) {
						if(kpi.getRencanaaktifitas()!=null) {
							for(SettingLockRencanaAktifitas aktivitas : kpi.getRencanaaktifitas()) {
								index++;
								param = new ParamQuery();
								param.setIndex(index);
								param.setType("int");
								param.setValue(aktivitas.getKode().toString());
								paramQueries.add(param);
								
								index++;
								param = new ParamQuery();
								param.setIndex(index);
								param.setType("int");
								param.setValue(kpi.getKodedetailkpi().toString());
								paramQueries.add(param);
								
								index++;
								param = new ParamQuery();
								param.setIndex(index);
								param.setType("string");
								param.setValue(aktivitas.getDeskripsi());
								paramQueries.add(param);
								
								index++;
								param = new ParamQuery();
								param.setIndex(index);
								param.setType("int");
								param.setValue("1");
								paramQueries.add(param);
								
								index++;
								param = new ParamQuery();
								param.setIndex(index);
								param.setType("string");
								param.setValue(aktivitas.getTarget());
								paramQueries.add(param);
								
								index++;
								param = new ParamQuery();
								param.setIndex(index);
								param.setType("date");
								param.setValue(Utils.SqlDateToSqlString(aktivitas.getTargettgl()));
								paramQueries.add(param);
								
								query += "insert into @RencanaAktivitas (kode, kodedetailkpi, deskripsi, show, target, targettgl) values (?,?,?,?,?,?);";
							}
						}
						
						if(kpi.getPembinaandetails()!=null) {
							for(PembinaanDetail pembinaan : kpi.getPembinaandetails()) {
								if(!pembinaan.getPencapaian().isEmpty()) {
									index++;
									param = new ParamQuery();
									param.setIndex(index);
									param.setType("int");
									param.setValue(pembinaan.getKode().toString());
									paramQueries.add(param);
									
									index++;
									param = new ParamQuery();
									param.setIndex(index);
									param.setType("int");
									param.setValue(kpi.getKode().toString());
									paramQueries.add(param);
									
									index++;
									param = new ParamQuery();
									param.setIndex(index);
									param.setType("string");
									param.setValue(pembinaan.getPencapaian());
									paramQueries.add(param);
									
									index++;
									param = new ParamQuery();
									param.setIndex(index);
									param.setType("string");
									param.setValue(pembinaan.getDetil());
									paramQueries.add(param);
									
									String lampiranpembinaan = pembinaan.getLampiran();
									if(pembinaan.getBase64file()!=null) {
										lampiranpembinaan = KinerjaUtil.saveDoc(context, pembinaan.getBase64file().getBase64(), "file_pembinaan", authUser.getNpp());
									}
									index++;
									param = new ParamQuery();
									param.setIndex(index);
									param.setType("string");
									param.setValue(lampiranpembinaan);
									paramQueries.add(param);
									
									index++;
									param = new ParamQuery();
									param.setIndex(index);
									param.setType("string");
									param.setValue(pembinaan.getCatatan());
									paramQueries.add(param);
									
									query += "insert into @PembinaanDetail (kode, kodeperencanaanhaskerkpi, pencapaian, detil, lampiran, catatan) values (?,?,?,?,?,?);";
								}
							}
						}
					}
				}
				
				if(inovasis!=null) {
					for (Inovasi inovasi : inovasis) {
						if(inovasi.getKode()==null) inovasi.setKode(0);
						if(
							(inovasi.getKode()==0 && !inovasi.getNama().isEmpty() && inovasi.getShow()==1) ||
							(inovasi.getKode()>0 && inovasi.getShow()==0)	
						) {
							index++;
							param = new ParamQuery();
							param.setIndex(index);
							param.setType("int");
							param.setValue(inovasi.getKode().toString());
							paramQueries.add(param);
							
							index++;
							param = new ParamQuery();
							param.setIndex(index);
							param.setType("string");
							param.setValue(inovasi.getNama());
							paramQueries.add(param);
							
							index++;
							param = new ParamQuery();
							param.setIndex(index);
							param.setType("int");
							param.setValue(inovasi.getRating().toString());
							paramQueries.add(param);
							
							index++;
							param = new ParamQuery();
							param.setIndex(index);
							param.setType("string");
							param.setValue(inovasi.getDeskripsi());
							paramQueries.add(param);
							
							String lampiraninovasi = inovasi.getLampiran();
							if(inovasi.getBase64file()!=null) {
								lampiraninovasi = KinerjaUtil.saveDoc(context, inovasi.getBase64file().getBase64(), "file_pembinaan", authUser.getNpp());
							}
							index++;
							param = new ParamQuery();
							param.setIndex(index);
							param.setType("string");
							param.setValue(lampiraninovasi);
							paramQueries.add(param);
							
							index++;
							param = new ParamQuery();
							param.setIndex(index);
							param.setType("int");
							param.setValue(inovasi.getShow().toString());
							paramQueries.add(param);
							
							query += "insert into @PembinaanInovasi (kode, nama, rating, deskripsi, lampiran, show) values (?,?,?,?,?,?);";
						}
					}
				}
					
				if(publikasikaryailmiahs!=null) {
					for (PublikasiKaryaIlmiah pki : publikasikaryailmiahs) {
						if(pki.getKode()==null) pki.setKode(0);
						if(
							(pki.getKode()==0 && !pki.getJudul().isEmpty() && pki.getShow()==1) ||
							(pki.getKode()>0 && pki.getShow()==0)	
						) {
							index++;
							param = new ParamQuery();
							param.setIndex(index);
							param.setType("int");
							param.setValue(pki.getKode().toString());
							paramQueries.add(param);
							
							index++;
							param = new ParamQuery();
							param.setIndex(index);
							param.setType("string");
							param.setValue(pki.getJudul());
							paramQueries.add(param);
							
							index++;
							param = new ParamQuery();
							param.setIndex(index);
							param.setType("string");
							param.setValue(pki.getPublisher());
							paramQueries.add(param);
							
							index++;
							param = new ParamQuery();
							param.setIndex(index);
							param.setType("int");
							param.setValue(pki.getRating().toString());
							paramQueries.add(param);
							
							index++;
							param = new ParamQuery();
							param.setIndex(index);
							param.setType("string");
							param.setValue(pki.getKeterangan());
							paramQueries.add(param);
							
							String lampiranpki = pki.getLampiran();
							if(pki.getBase64file()!=null) {
								lampiranpki = KinerjaUtil.saveDoc(context, pki.getBase64file().getBase64(), "file_pembinaan", authUser.getNpp());
							}
							index++;
							param = new ParamQuery();
							param.setIndex(index);
							param.setType("string");
							param.setValue(lampiranpki);
							paramQueries.add(param);
							
							index++;
							param = new ParamQuery();
							param.setIndex(index);
							param.setType("int");
							param.setValue(pki.getShow().toString());
							paramQueries.add(param);
							
							query += "insert into @PembinaanPublikasiKaryaIlmiah (kode, judul, publisher, rating, keterangan, lampiran, show) values (?,?,?,?,?,?,?);";
						}
					}
				}
				
				if(creatingfutureleader.getPegpromosis()!=null) {
					for (CreatingFutureLeaderPegPromosi peg : creatingfutureleader.getPegpromosis()) {
						if(peg.getKode()==null) peg.setKode(0);
						if(
							(peg.getKode()==0 && !peg.getPegawai().getNpp().isEmpty() && peg.getShow()==1) ||
							(peg.getKode()>0 && peg.getShow()==0)	
						) {
							index++;
							param = new ParamQuery();
							param.setIndex(index);
							param.setType("int");
							param.setValue(peg.getKode().toString());
							paramQueries.add(param);
							
							index++;
							param = new ParamQuery();
							param.setIndex(index);
							param.setType("string");
							param.setValue(peg.getPegawai().getNpp());
							paramQueries.add(param);
							
							String lampiranpeg = peg.getLampiran();
							if(peg.getBase64file()!=null) {
								lampiranpeg = KinerjaUtil.saveDoc(context, peg.getBase64file().getBase64(), "file_pembinaan", authUser.getNpp());
							}
							index++;
							param = new ParamQuery();
							param.setIndex(index);
							param.setType("string");
							param.setValue(lampiranpeg);
							paramQueries.add(param);
							
							index++;
							param = new ParamQuery();
							param.setIndex(index);
							param.setType("int");
							param.setValue(peg.getShow().toString());
							paramQueries.add(param);
							
							query += "insert into @PembinaanCreatingFutureLeaderPegPromosi (kode, npp, lampiran, show) values (?,?,?,?);";
						}
					}
				}
				
				query += "exec kinerja.sp_pembinaan_simpan @RencanaAktivitas,@PembinaanDetail,@PembinaanInovasi,@PembinaanPublikasiKaryaIlmiah,@PembinaanCreatingFutureLeaderPegPromosi,?,?,?,?,?,?,?";
				con = new Koneksi().getConnection();
				cs = con.prepareCall(query);
				index = 0;
				for (ParamQuery row : paramQueries) {
					index++;
					if(row.getType().equalsIgnoreCase("int")) {
						cs.setInt(row.getIndex(), Integer.parseInt(row.getValue()));
					}
					else if(row.getType().equalsIgnoreCase("float")) {
						cs.setFloat(row.getIndex(), Float.parseFloat(row.getValue()));
					}
					else if(row.getType().equalsIgnoreCase("date")) {
						cs.setDate(row.getIndex(), Utils.StringDateToSqlDate(row.getValue()));
					}
					else {
						cs.setString(row.getIndex(), row.getValue());
					}
				}
				cs.setInt(index+1, json.getKodeperencanaan());
				cs.setInt(index+2, json.getStatus());
				if(creatingfutureleader.getJumlahtalentstar()!=null)
					cs.setInt(index+3, creatingfutureleader.getJumlahtalentstar());
				else
					cs.setNull(index+3, java.sql.Types.INTEGER);
				if(creatingfutureleader.getRating()!=null)
					cs.setInt(index+4, creatingfutureleader.getRating());
				else
					cs.setNull(index+4, java.sql.Types.INTEGER);
				if(creatingfutureleader.getKeterangan()!=null)
					cs.setString(index+5, creatingfutureleader.getKeterangan());
				else
					cs.setNull(index+5, java.sql.Types.VARCHAR);
				cs.setInt(index+6, authUser.getUserid());
				cs.registerOutParameter(index+7, java.sql.Types.INTEGER);
				cs.executeUpdate();
				
				metadata.put("code", 1);
				metadata.put("message", "Simpan sukses");
			}
			catch (SQLException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				e.printStackTrace();
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
	
	@GET
	@Path("/pembinaan/kinerja/tinjau/kodepembinaan/{kodepembinaan}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response getPeninjauanPembinaanKinerja(
			@Context HttpHeaders headers, 
			@PathParam("kodepembinaan") Integer kodepembinaan
		) {
		Map<String, Object> response = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> metadata = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();

		AuthUser authUser = new AuthUser();
		if (SharedMethod.VerifyToken(authUser, headers, metadata)) {
			try {
				SimpanPembinaanKinerja pembinaankinerja = new SimpanPembinaanKinerja();
				Integer kodeperencanaan = KinerjaUtil.getKodePerencanaanByKodePembinaan(kodepembinaan);
				pembinaankinerja.setKodeperencanaan(kodeperencanaan);
				Integer kodepeserta = KinerjaUtil.getKodePesertaByKodePerencanaan(kodeperencanaan);
				pembinaankinerja.setKodepeserta(kodepeserta);
				List<Komponen> komponens = KinerjaUtil.getKomponenByPeserta(kodepeserta);
				if(komponens!=null) {
					for(Komponen komponen : komponens) {
						if(komponen.getKode()==1) {
							List<SettingLockHasilKerja> hasilkerjas = KinerjaUtil.getPerencanaanHasilKerja(kodepeserta);
							if(hasilkerjas!=null) {
								for(SettingLockHasilKerja hasilkerja : hasilkerjas) {
									List<SettingLockKpi> kpis = KinerjaUtil.getPerencanaanKpi(hasilkerja.getKodetanggungjawab(), hasilkerja.getKode(), kodepeserta);
									if(kpis!=null) {
										hasilkerja.setKpi(kpis);
										for(SettingLockKpi kpi : kpis) {
											List<SettingLockKriteriaKpi> kriteriakpis = KinerjaUtil.getPerencanaanKriteriaKpi(kodepeserta, kpi.getKode(), kpi.getKodesettinglockkpi());
											kpi.setKriteria(kriteriakpis);
											
											List<SettingLockRencanaAktifitas> rencanaAktifitas = KinerjaUtil.getPerencanaanRencanaAktifitas(kpi.getKode(), kpi.getKodedetailkpi());
											kpi.setRencanaaktifitas(rencanaAktifitas);
											
											List<PembinaanDetail> details = KinerjaUtil.getPembinaanDetail(kpi.getKode());
											kpi.setPembinaandetails(details);
										}
									}
								}
							}
							HasilKerjaHeader hasilKerjaHeader = new HasilKerjaHeader();
							hasilKerjaHeader.setHasilkerjas(hasilkerjas);
							komponen.setHasilkerja(hasilKerjaHeader);
						}
						else if(komponen.getKode()==3) {
							KomitmenHeader komitmenHeader = new KomitmenHeader();
							komitmenHeader.setKriterias(KinerjaUtil.getKriterias(komponen.getKode(), kodepeserta));
							komponen.setKomitmen(komitmenHeader);
						}
						else if(komponen.getKode()==5) {
							KejadianKritisNegatifHeader kejadianKritisNegatifHeader = new KejadianKritisNegatifHeader();
							kejadianKritisNegatifHeader.setKriterias(KinerjaUtil.getKriterias(komponen.getKode(), kodepeserta));
							komponen.setKejadiankritisnegatif(kejadianKritisNegatifHeader);
						}
						else if(komponen.getKode()==6) {
							List<Inovasi> inovasis = KinerjaUtil.getPembinaanInovasi(kodeperencanaan);
							InovasiHeader inovasiHeader = new InovasiHeader();
							inovasiHeader.setInovasis(inovasis);
							inovasiHeader.setKriterias(KinerjaUtil.getKriterias(komponen.getKode(), kodepeserta));
							komponen.setInovasi(inovasiHeader);
						}
						else if(komponen.getKode()==7) {
							CreatingFutureLeader creatingfutureleader = KinerjaUtil.getCreatingFutureLeader(kodeperencanaan);
							CreatingFutureLeaderHeader creatingFutureLeaderHeader = new CreatingFutureLeaderHeader();
							creatingFutureLeaderHeader.setKriterias(KinerjaUtil.getKriterias(komponen.getKode(), kodepeserta));
							creatingFutureLeaderHeader.setCreatingfutureleader(creatingfutureleader);
							komponen.setCreatingfutureleader(creatingFutureLeaderHeader);
						}
						else if(komponen.getKode()==8) {
							List<PublikasiKaryaIlmiah> publikasikaryailmiahs = KinerjaUtil.getPembinaanPublikasiKaryaIlmiah(kodeperencanaan);
							PublikasiKaryaIlmiahHeader publikasiKaryaIlmiahHeader = new PublikasiKaryaIlmiahHeader();
							publikasiKaryaIlmiahHeader.setKriterias(KinerjaUtil.getKriterias(komponen.getKode(), kodepeserta));
							publikasiKaryaIlmiahHeader.setPublikasikaryailmiahs(publikasikaryailmiahs);
							komponen.setPublikasikaryailmiah(publikasiKaryaIlmiahHeader);
						}
					}
				}
				pembinaankinerja.setKomponens(komponens);
				List<Penugasan> bawahans = KinerjaUtil.getListBawahan(kodepeserta);
				pembinaankinerja.setBawahans(bawahans);
				
				List<Map<String, Object>> catatans = KinerjaUtil.getListCatatanPerencanaan(kodepeserta);
				data.put("pembinaan", pembinaankinerja);
				data.put("catatans", catatans);
				data.put("pegawai", HcisRest.getRowDetil(5, kodepembinaan));
				
				response.put("data", data);
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
	@Path("/pembinaan/kinerja/tinjau/simpan")
	@Consumes("application/json")
	@Produces("application/json")
	public Response setApprovalPembinaanKinerja(
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
				mapper.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, true);
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				ApprovalPembinaan json = mapper.readValue(data, ApprovalPembinaan.class);
				
				Integer kodeperencanaan = KinerjaUtil.getKodePerencanaanByKodePembinaan(json.getKodepembinaan());
				query = "DECLARE @Hasilkerja kinerja.settinglockhasilkerjaAsTable;";
				List<ParamQuery> paramQueries = new ArrayList<>();
				Integer index = 0;
				ParamQuery param = null;
				
				List<SettingLockHasilKerja> hasilkerjas = null;
				List<Inovasi> inovasis = null;
				CreatingFutureLeader creatingfutureleader = null;
				List<PublikasiKaryaIlmiah> publikasikaryailmiahs = null;
				for (Komponen komponen : json.getKomponens()) {
					switch (komponen.getKode()) {
					case 1:
						hasilkerjas = komponen.getHasilkerja().getHasilkerjas();
						break;
					case 6:
						inovasis = komponen.getInovasi().getInovasis();
						break;
					case 7:
						creatingfutureleader = komponen.getCreatingfutureleader().getCreatingfutureleader();
						break;
					case 8:
						publikasikaryailmiahs = komponen.getPublikasikaryailmiah().getPublikasikaryailmiahs();
						break;
					default:
						break;
					}
				}
				
				if(creatingfutureleader==null) 
					creatingfutureleader = new CreatingFutureLeader();
				
				query += "DECLARE @RencanaAktivitas kinerja.settinglockrencanaaktivitasAsTable;";
				query += "DECLARE @PembinaanDetail kinerja.pembinaandetailAsTable;";
				query += "DECLARE @PembinaanInovasi kinerja.pembinaaninovasiAsTable;";
				query += "DECLARE @PembinaanPublikasiKaryaIlmiah kinerja.pembinaanpublikasikaryailmiahAsTable;";
				query += "DECLARE @PembinaanCreatingFutureLeaderPegPromosi kinerja.pembinaancreatingfutureleaderpegpromosiAsTable;";
				
				for (SettingLockHasilKerja hasilkerja : hasilkerjas) {
					for(SettingLockKpi kpi : hasilkerja.getKpi()) {
						if(kpi.getRencanaaktifitas()!=null) {
							for(SettingLockRencanaAktifitas aktivitas : kpi.getRencanaaktifitas()) {
								index++;
								param = new ParamQuery();
								param.setIndex(index);
								param.setType("int");
								param.setValue(aktivitas.getKode().toString());
								paramQueries.add(param);
								
								index++;
								param = new ParamQuery();
								param.setIndex(index);
								param.setType("int");
								param.setValue(kpi.getKodedetailkpi().toString());
								paramQueries.add(param);
								
								index++;
								param = new ParamQuery();
								param.setIndex(index);
								param.setType("string");
								param.setValue(aktivitas.getDeskripsi());
								paramQueries.add(param);
								
								index++;
								param = new ParamQuery();
								param.setIndex(index);
								param.setType("int");
								param.setValue("1");
								paramQueries.add(param);
								
								index++;
								param = new ParamQuery();
								param.setIndex(index);
								param.setType("string");
								param.setValue(aktivitas.getTarget());
								paramQueries.add(param);
								
								index++;
								param = new ParamQuery();
								param.setIndex(index);
								param.setType("date");
								param.setValue(Utils.SqlDateToSqlString(aktivitas.getTargettgl()));
								paramQueries.add(param);
								
								query += "insert into @RencanaAktivitas (kode, kodedetailkpi, deskripsi, show, target, targettgl) values (?,?,?,?,?,?);";
							}
						}
						
						if(kpi.getPembinaandetails()!=null) {
							for(PembinaanDetail pembinaan : kpi.getPembinaandetails()) {
								if(!pembinaan.getPencapaian().isEmpty()) {
									index++;
									param = new ParamQuery();
									param.setIndex(index);
									param.setType("int");
									param.setValue(pembinaan.getKode().toString());
									paramQueries.add(param);
									
									index++;
									param = new ParamQuery();
									param.setIndex(index);
									param.setType("int");
									param.setValue(kpi.getKode().toString());
									paramQueries.add(param);
									
									index++;
									param = new ParamQuery();
									param.setIndex(index);
									param.setType("string");
									param.setValue(pembinaan.getPencapaian());
									paramQueries.add(param);
									
									index++;
									param = new ParamQuery();
									param.setIndex(index);
									param.setType("string");
									param.setValue(pembinaan.getDetil());
									paramQueries.add(param);
									
									String lampiranpembinaan = pembinaan.getLampiran();
									if(pembinaan.getBase64file()!=null) {
										lampiranpembinaan = KinerjaUtil.saveDoc(context, pembinaan.getBase64file().getBase64(), "file_pembinaan", authUser.getNpp());
									}
									index++;
									param = new ParamQuery();
									param.setIndex(index);
									param.setType("string");
									param.setValue(lampiranpembinaan);
									paramQueries.add(param);
									
									index++;
									param = new ParamQuery();
									param.setIndex(index);
									param.setType("string");
									param.setValue(pembinaan.getCatatan());
									paramQueries.add(param);
									
									query += "insert into @PembinaanDetail (kode, kodeperencanaanhaskerkpi, pencapaian, detil, lampiran, catatan) values (?,?,?,?,?,?);";
								}
							}
						}
					}
				}
				
				if(inovasis!=null) {
					for (Inovasi inovasi : inovasis) {
						if(inovasi.getKode()==null) inovasi.setKode(0);
						if(
							(inovasi.getKode()==0 && !inovasi.getNama().isEmpty() && inovasi.getShow()==1) ||
							(inovasi.getKode()>0 && inovasi.getShow()==0)	
						) {
							index++;
							param = new ParamQuery();
							param.setIndex(index);
							param.setType("int");
							param.setValue(inovasi.getKode().toString());
							paramQueries.add(param);
							
							index++;
							param = new ParamQuery();
							param.setIndex(index);
							param.setType("string");
							param.setValue(inovasi.getNama());
							paramQueries.add(param);
							
							index++;
							param = new ParamQuery();
							param.setIndex(index);
							param.setType("int");
							param.setValue(inovasi.getRating().toString());
							paramQueries.add(param);
							
							index++;
							param = new ParamQuery();
							param.setIndex(index);
							param.setType("string");
							param.setValue(inovasi.getDeskripsi());
							paramQueries.add(param);
							
							String lampiraninovasi = inovasi.getLampiran();
							if(inovasi.getBase64file()!=null) {
								lampiraninovasi = KinerjaUtil.saveDoc(context, inovasi.getBase64file().getBase64(), "file_pembinaan", authUser.getNpp());
							}
							index++;
							param = new ParamQuery();
							param.setIndex(index);
							param.setType("string");
							param.setValue(lampiraninovasi);
							paramQueries.add(param);
							
							index++;
							param = new ParamQuery();
							param.setIndex(index);
							param.setType("int");
							param.setValue(inovasi.getShow().toString());
							paramQueries.add(param);
							
							query += "insert into @PembinaanInovasi (kode, nama, rating, deskripsi, lampiran, show) values (?,?,?,?,?,?);";
						}
					}
				}
					
				if(publikasikaryailmiahs!=null) {
					for (PublikasiKaryaIlmiah pki : publikasikaryailmiahs) {
						if(pki.getKode()==null) pki.setKode(0);
						if(
							(pki.getKode()==0 && !pki.getJudul().isEmpty() && pki.getShow()==1) ||
							(pki.getKode()>0 && pki.getShow()==0)	
						) {
							index++;
							param = new ParamQuery();
							param.setIndex(index);
							param.setType("int");
							param.setValue(pki.getKode().toString());
							paramQueries.add(param);
							
							index++;
							param = new ParamQuery();
							param.setIndex(index);
							param.setType("string");
							param.setValue(pki.getJudul());
							paramQueries.add(param);
							
							index++;
							param = new ParamQuery();
							param.setIndex(index);
							param.setType("string");
							param.setValue(pki.getPublisher());
							paramQueries.add(param);
							
							index++;
							param = new ParamQuery();
							param.setIndex(index);
							param.setType("int");
							param.setValue(pki.getRating().toString());
							paramQueries.add(param);
							
							index++;
							param = new ParamQuery();
							param.setIndex(index);
							param.setType("string");
							param.setValue(pki.getKeterangan());
							paramQueries.add(param);
							
							String lampiranpki = pki.getLampiran();
							if(pki.getBase64file()!=null) {
								lampiranpki = KinerjaUtil.saveDoc(context, pki.getBase64file().getBase64(), "file_pembinaan", authUser.getNpp());
							}
							index++;
							param = new ParamQuery();
							param.setIndex(index);
							param.setType("string");
							param.setValue(lampiranpki);
							paramQueries.add(param);
							
							index++;
							param = new ParamQuery();
							param.setIndex(index);
							param.setType("int");
							param.setValue(pki.getShow().toString());
							paramQueries.add(param);
							
							query += "insert into @PembinaanPublikasiKaryaIlmiah (kode, judul, publisher, rating, keterangan, lampiran, show) values (?,?,?,?,?,?,?);";
						}
					}
				}
				
				if(creatingfutureleader.getPegpromosis()!=null) {
					for (CreatingFutureLeaderPegPromosi peg : creatingfutureleader.getPegpromosis()) {
						if(peg.getKode()==null) peg.setKode(0);
						if(
							(peg.getKode()==0 && !peg.getPegawai().getNpp().isEmpty() && peg.getShow()==1) ||
							(peg.getKode()>0 && peg.getShow()==0)	
						) {
							index++;
							param = new ParamQuery();
							param.setIndex(index);
							param.setType("int");
							param.setValue(peg.getKode().toString());
							paramQueries.add(param);
							
							index++;
							param = new ParamQuery();
							param.setIndex(index);
							param.setType("string");
							param.setValue(peg.getPegawai().getNpp());
							paramQueries.add(param);
							
							String lampiranpeg = "";
							if(peg.getBase64file()!=null) {
								lampiranpeg = KinerjaUtil.saveDoc(context, peg.getBase64file().getBase64(), "file_pembinaan", authUser.getNpp());
							}
							index++;
							param = new ParamQuery();
							param.setIndex(index);
							param.setType("string");
							param.setValue(lampiranpeg);
							paramQueries.add(param);
							
							index++;
							param = new ParamQuery();
							param.setIndex(index);
							param.setType("int");
							param.setValue(peg.getShow().toString());
							paramQueries.add(param);
							
							query += "insert into @PembinaanCreatingFutureLeaderPegPromosi (kode, npp, lampiran, show) values (?,?,?,?);";
						}
					}
				}
				
				query += "exec kinerja.sp_pembinaan_simpan @RencanaAktivitas,@PembinaanDetail,@PembinaanInovasi,@PembinaanPublikasiKaryaIlmiah,@PembinaanCreatingFutureLeaderPegPromosi,?,?,?,?,?,?,?";
				con = new Koneksi().getConnection();
				cs = con.prepareCall(query);
				index = 0;
				for (ParamQuery row : paramQueries) {
					index++;
					if(row.getType().equalsIgnoreCase("int")) {
						cs.setInt(row.getIndex(), Integer.parseInt(row.getValue()));
					}
					else if(row.getType().equalsIgnoreCase("float")) {
						cs.setFloat(row.getIndex(), Float.parseFloat(row.getValue()));
					}
					else if(row.getType().equalsIgnoreCase("date")) {
						cs.setDate(row.getIndex(), Utils.StringDateToSqlDate(row.getValue()));
					}
					else {
						cs.setString(row.getIndex(), row.getValue());
					}
				}
				cs.setInt(index+1, kodeperencanaan);
				cs.setInt(index+2, 0);
				if(creatingfutureleader.getJumlahtalentstar()!=null)
					cs.setInt(index+3, creatingfutureleader.getJumlahtalentstar());
				else
					cs.setNull(index+3, java.sql.Types.INTEGER);
				if(creatingfutureleader.getRating()!=null)
					cs.setInt(index+4, creatingfutureleader.getRating());
				else
					cs.setNull(index+4, java.sql.Types.INTEGER);
				if(creatingfutureleader.getKeterangan()!=null)
					cs.setString(index+5, creatingfutureleader.getKeterangan());
				else
					cs.setNull(index+5, java.sql.Types.VARCHAR);
				cs.setInt(index+6, authUser.getUserid());
				cs.registerOutParameter(index+7, java.sql.Types.INTEGER);
				cs.executeUpdate();
				
				KinerjaUtil.simpanCatatanPembinaan(
					json.getKodepembinaan(), 
					json.getFlag(), 
					json.getCatatan(), 
					json.getKodepenugasan(), 
					authUser.getUserid()
				);
				
				metadata.put("code", 1);
				metadata.put("message", "Simpan sukses");
			}
			catch (SQLException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				e.printStackTrace();
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
	
	@GET
	@Path("/evaluasi/kinerja/kodepembinaan/{kodepembinaan}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response getEvaluasiKinerja(
			@Context HttpHeaders headers, 
			@PathParam("kodepembinaan") Integer kodepembinaan
		) {
		Map<String, Object> response = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> metadata = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();

		AuthUser authUser = new AuthUser();
		if (SharedMethod.VerifyToken(authUser, headers, metadata)) {
			try {
				SimpanEvaluasiKinerja evaluasikinerja = new SimpanEvaluasiKinerja();
				Integer kodeperencanaan = KinerjaUtil.getKodePerencanaanByKodePembinaan(kodepembinaan);
				Integer kodepeserta = KinerjaUtil.getKodePesertaByKodePerencanaan(kodeperencanaan);
				evaluasikinerja.setKodepembinaan(kodepembinaan);
				evaluasikinerja.setKodeperencanaan(kodeperencanaan);
				evaluasikinerja.setKodepeserta(kodepeserta);
				List<Komponen> komponens = KinerjaUtil.getKomponenByPeserta(kodepeserta);
				if(komponens!=null) {
					for(Komponen komponen : komponens) {
						if(komponen.getKode()==1) {
							List<SettingLockHasilKerja> hasilkerjas = KinerjaUtil.getPerencanaanHasilKerja(kodepeserta);
							if(hasilkerjas!=null) {
								for(SettingLockHasilKerja hasilkerja : hasilkerjas) {
									List<SettingLockKpi> kpis = KinerjaUtil.getPerencanaanKpi(hasilkerja.getKodetanggungjawab(), hasilkerja.getKode(), kodepeserta);
									if(kpis!=null) {
										hasilkerja.setKpi(kpis);
										for(SettingLockKpi kpi : kpis) {
											List<SettingLockKriteriaKpi> kriteriakpis = KinerjaUtil.getPerencanaanKriteriaKpi(kodepeserta, kpi.getKode(), kpi.getKodesettinglockkpi());
											kpi.setKriteria(kriteriakpis);
											
											List<SettingLockRencanaAktifitas> rencanaAktifitas = KinerjaUtil.getPerencanaanRencanaAktifitas(kpi.getKode(), kpi.getKodedetailkpi());
											kpi.setRencanaaktifitas(rencanaAktifitas);
											
											List<PembinaanDetail> details = KinerjaUtil.getPembinaanDetail(kpi.getKode());
											kpi.setPembinaandetails(details);
											
											EvaluasiKpi evaluasi = KinerjaUtil.getEvaluasiKpi(kpi.getKode());
											kpi.setEvaluasi(evaluasi);
										}
									}
								}
							}
							HasilKerjaHeader hasilKerjaHeader = new HasilKerjaHeader();
							hasilKerjaHeader.setHasilkerjas(hasilkerjas);
							komponen.setHasilkerja(hasilKerjaHeader);
						}
						else if(komponen.getKode()==3) {
							KomitmenHeader komitmenHeader = new KomitmenHeader();
							komitmenHeader.setKriterias(KinerjaUtil.getKriterias(komponen.getKode(), kodepeserta));
							komponen.setKomitmen(komitmenHeader);
						}
						else if(komponen.getKode()==5) {
							KejadianKritisNegatifHeader kejadianKritisNegatifHeader = new KejadianKritisNegatifHeader();
							kejadianKritisNegatifHeader.setKriterias(KinerjaUtil.getKriterias(komponen.getKode(), kodepeserta));
							List<KejadianKritisNegatif> kejadianKritisNegatifs = KinerjaUtil.getListEvaluasiKejadianKritis(kodepeserta); 
							kejadianKritisNegatifHeader.setKejadiankritisnegatifs(kejadianKritisNegatifs);
							komponen.setKejadiankritisnegatif(kejadianKritisNegatifHeader);
						}
						else if(komponen.getKode()==6) {
							List<Inovasi> inovasis = KinerjaUtil.getEvaluasiInovasi(kodepembinaan);
							InovasiHeader inovasiHeader = new InovasiHeader();
							inovasiHeader.setInovasis(inovasis);
							inovasiHeader.setKriterias(KinerjaUtil.getKriterias(komponen.getKode(), kodepeserta));
							komponen.setInovasi(inovasiHeader);
						}
						else if(komponen.getKode()==7) {
							CreatingFutureLeader creatingfutureleader = KinerjaUtil.getEvaluasiCreatingFutureLeader(kodepembinaan);
							CreatingFutureLeaderHeader creatingFutureLeaderHeader = new CreatingFutureLeaderHeader();
							creatingFutureLeaderHeader.setKriterias(KinerjaUtil.getKriterias(komponen.getKode(), kodepeserta));
							creatingFutureLeaderHeader.setCreatingfutureleader(creatingfutureleader);
							komponen.setCreatingfutureleader(creatingFutureLeaderHeader);
						}
						else if(komponen.getKode()==8) {
							List<PublikasiKaryaIlmiah> publikasikaryailmiahs = KinerjaUtil.getEvaluasiPublikasiKaryaIlmiah(kodepembinaan);
							PublikasiKaryaIlmiahHeader publikasiKaryaIlmiahHeader = new PublikasiKaryaIlmiahHeader();
							publikasiKaryaIlmiahHeader.setKriterias(KinerjaUtil.getKriterias(komponen.getKode(), kodepeserta));
							publikasiKaryaIlmiahHeader.setPublikasikaryailmiahs(publikasikaryailmiahs);
							komponen.setPublikasikaryailmiah(publikasiKaryaIlmiahHeader);
						}
					}
				}
				evaluasikinerja.setKomponens(komponens);
				//List<Penugasan> bawahans = KinerjaUtil.getListBawahan(kodepeserta);
				//evaluasikinerja.setBawahans(bawahans);
				
				List<Map<String, Object>> catatans = KinerjaUtil.getListCatatanPerencanaan(kodepeserta);
				data.put("evaluasi", evaluasikinerja);
				data.put("catatans", catatans);
				data.put("pegawai", HcisRest.getRowDetil(5, kodepembinaan));
				
				response.put("data", data);
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
	@Path("/evaluasi/kinerja/simpan")
	@Consumes("application/json")
	@Produces("application/json")
	public Response setEvaluasiKinerja(
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
				SimpanEvaluasiKinerja json = mapper.readValue(data, SimpanEvaluasiKinerja.class);
				
				query = "DECLARE @Hasilkerja kinerja.settinglockhasilkerjaAsTable;";
				List<ParamQuery> paramQueries = new ArrayList<>();
				Integer index = 0;
				ParamQuery param = null;
				
				List<SettingLockHasilKerja> hasilkerjas = null;
				List<KejadianKritisNegatif> kejadiankritisnegatifs = null;
				List<Inovasi> inovasis = null;
				CreatingFutureLeader creatingfutureleader = null;
				List<PublikasiKaryaIlmiah> publikasikaryailmiahs = null;
				for (Komponen komponen : json.getKomponens()) {
					switch (komponen.getKode()) {
					case 1:
						hasilkerjas = komponen.getHasilkerja().getHasilkerjas();
						break;
					case 5:
						kejadiankritisnegatifs = komponen.getKejadiankritisnegatif().getKejadiankritisnegatifs();
						break;
					case 6:
						inovasis = komponen.getInovasi().getInovasis();
						break;
					case 7:
						creatingfutureleader = komponen.getCreatingfutureleader().getCreatingfutureleader();
						break;
					case 8:
						publikasikaryailmiahs = komponen.getPublikasikaryailmiah().getPublikasikaryailmiahs();
						break;
					default:
						break;
					}
				}
				
				//if(creatingfutureleader==null) 
					//creatingfutureleader = new CreatingFutureLeader();
				
				query += "DECLARE @Haskers kinerja.evaluasihaskerkpiAsTable;";
				query += "DECLARE @Kkns kinerja.evaluasinonhaskerAsTable;";
				query += "DECLARE @Inovasis kinerja.evaluasinonhaskerAsTable;";
				query += "DECLARE @Cfl kinerja.evaluasinonhaskerAsTable;";
				query += "DECLARE @Pkis kinerja.evaluasinonhaskerAsTable;";
				
				for (SettingLockHasilKerja hasilkerja : hasilkerjas) {
					for(SettingLockKpi kpi : hasilkerja.getKpi()) {
						
						index++;
						param = new ParamQuery();
						param.setIndex(index);
						param.setType("int");
						param.setValue(kpi.getEvaluasi().getKode()==null?null:kpi.getEvaluasi().getKode().toString());
						paramQueries.add(param);
						
						index++;
						param = new ParamQuery();
						param.setIndex(index);
						param.setType("int");
						param.setValue(kpi.getKode().toString());
						paramQueries.add(param);
						
						index++;
						param = new ParamQuery();
						param.setIndex(index);
						param.setType("int");
						param.setValue(kpi.getEvaluasi().getRating().toString());
						paramQueries.add(param);
						
						index++;
						param = new ParamQuery();
						param.setIndex(index);
						param.setType("string");
						param.setValue(kpi.getEvaluasi().getPencapaian());
						paramQueries.add(param);
						
						index++;
						param = new ParamQuery();
						param.setIndex(index);
						param.setType("string");
						param.setValue(kpi.getEvaluasi().getSumberdata());
						paramQueries.add(param);
						
						String lampiranevaluasi = kpi.getEvaluasi().getLampiran();
						if(kpi.getEvaluasi().getBase64file()!=null) {
							lampiranevaluasi = KinerjaUtil.saveDoc(context, kpi.getEvaluasi().getBase64file().getBase64(), "file_evaluasi", authUser.getNpp());
						}
						index++;
						param = new ParamQuery();
						param.setIndex(index);
						param.setType("string");
						param.setValue(lampiranevaluasi);
						paramQueries.add(param);
						
						query += "insert into @Haskers (kode, kodeperencanaanhaskerkpi, rating, pencapaian, sumberdata, lampiran) values (?,?,?,?,?,?);";
					}
				}
				
				if(kejadiankritisnegatifs!=null) {
					for (KejadianKritisNegatif kkn : kejadiankritisnegatifs) {
						
						index++;
						param = new ParamQuery();
						param.setIndex(index);
						param.setType("int");
						param.setValue(kkn.getKodeevaluasikejadiankritisnegatif()==null?null:kkn.getKodeevaluasikejadiankritisnegatif().toString());
						paramQueries.add(param);
						
						index++;
						param = new ParamQuery();
						param.setIndex(index);
						param.setType("int");
						param.setValue(kkn.getKode().toString());
						paramQueries.add(param);
						
						index++;
						param = new ParamQuery();
						param.setIndex(index);
						param.setType("int");
						param.setValue(kkn.getRatingevaluasi().toString());
						paramQueries.add(param);
						
						index++;
						param = new ParamQuery();
						param.setIndex(index);
						param.setType("string");
						param.setValue(kkn.getCatatanevaluasi());
						paramQueries.add(param);
						
						query += "insert into @Kkns (kode, fkode, rating, catatan) values (?,?,?,?);";
					}
				}
				
				if(inovasis!=null) {
					for (Inovasi inovasi : inovasis) {
						
						index++;
						param = new ParamQuery();
						param.setIndex(index);
						param.setType("int");
						param.setValue(inovasi.getKodeevaluasiinovasi()==null?null:inovasi.getKodeevaluasiinovasi().toString());
						paramQueries.add(param);
						
						index++;
						param = new ParamQuery();
						param.setIndex(index);
						param.setType("int");
						param.setValue(inovasi.getKode().toString());
						paramQueries.add(param);
						
						index++;
						param = new ParamQuery();
						param.setIndex(index);
						param.setType("int");
						param.setValue(inovasi.getRatingevaluasi().toString());
						paramQueries.add(param);
						
						index++;
						param = new ParamQuery();
						param.setIndex(index);
						param.setType("string");
						param.setValue(inovasi.getCatatanevaluasi());
						paramQueries.add(param);
						
						query += "insert into @Inovasis (kode, fkode, rating, catatan) values (?,?,?,?);";
					}
				}
					
				if(publikasikaryailmiahs!=null) {
					for (PublikasiKaryaIlmiah pki : publikasikaryailmiahs) {
						
						index++;
						param = new ParamQuery();
						param.setIndex(index);
						param.setType("int");
						param.setValue(pki.getKodeevaluasipublikasikaryailmiah()==null?null:pki.getKodeevaluasipublikasikaryailmiah().toString());
						paramQueries.add(param);
						
						index++;
						param = new ParamQuery();
						param.setIndex(index);
						param.setType("int");
						param.setValue(pki.getKode().toString());
						paramQueries.add(param);
						
						index++;
						param = new ParamQuery();
						param.setIndex(index);
						param.setType("int");
						param.setValue(pki.getRatingevaluasi().toString());
						paramQueries.add(param);
						
						index++;
						param = new ParamQuery();
						param.setIndex(index);
						param.setType("string");
						param.setValue(pki.getCatatanevaluasi());
						paramQueries.add(param);
						
						query += "insert into @Pkis (kode, fkode, rating, catatan) values (?,?,?,?);";
					}
				}
				
				if(creatingfutureleader!=null) {
					index++;
					param = new ParamQuery();
					param.setIndex(index);
					param.setType("int");
					param.setValue(creatingfutureleader.getKodeevaluasicreatingfutureleader()==null?null:creatingfutureleader.getKodeevaluasicreatingfutureleader().toString());
					paramQueries.add(param);
					
					index++;
					param = new ParamQuery();
					param.setIndex(index);
					param.setType("int");
					param.setValue(creatingfutureleader.getKode().toString());
					paramQueries.add(param);
					
					index++;
					param = new ParamQuery();
					param.setIndex(index);
					param.setType("int");
					param.setValue(creatingfutureleader.getRatingevaluasi().toString());
					paramQueries.add(param);
					
					index++;
					param = new ParamQuery();
					param.setIndex(index);
					param.setType("string");
					param.setValue(creatingfutureleader.getCatatanevaluasi());
					paramQueries.add(param);
					
					query += "insert into @Cfl (kode, fkode, rating, catatan) values (?,?,?,?);";
				}
				
				query += "exec kinerja.sp_evaluasi_simpan @Haskers,@Kkns,@Inovasis,@Cfl,@Pkis,?,?,?";
				con = new Koneksi().getConnection();
				cs = con.prepareCall(query);
				index = 0;
				for (ParamQuery row : paramQueries) {
					index++;
					if(row.getType().equalsIgnoreCase("int")) {
						if(row.getValue()!=null) {
							cs.setInt(row.getIndex(), Integer.parseInt(row.getValue()));
						}
						else {
							cs.setNull(row.getIndex(), java.sql.Types.INTEGER);
						}
					}
					else if(row.getType().equalsIgnoreCase("float")) {
						if(row.getValue()!=null) {
							cs.setFloat(row.getIndex(), Float.parseFloat(row.getValue()));
						}
						else {
							cs.setNull(row.getIndex(), java.sql.Types.FLOAT);
						}
					}
					else if(row.getType().equalsIgnoreCase("date")) {
						if(row.getValue()!=null) {
							cs.setDate(row.getIndex(), Utils.StringDateToSqlDate(row.getValue()));
						}
						else {
							cs.setNull(row.getIndex(), java.sql.Types.DATE);
						}
					}
					else {
						if(row.getValue()!=null) {
							cs.setString(row.getIndex(), row.getValue());
						}
						else {
							cs.setNull(row.getIndex(), java.sql.Types.VARCHAR);
						}
					}
				}
				cs.setInt(index+1, json.getKodepembinaan());
				cs.setInt(index+2, json.getStatus());
				cs.setInt(index+3, authUser.getUserid());
				cs.executeUpdate();
				
				metadata.put("code", 1);
				metadata.put("message", "Simpan sukses");
			}
			catch (SQLException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				e.printStackTrace();
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
	
	@GET
	@Path("/catatan/komitmen/kodepeserta/{kodepeserta}/kodepesertabawahan/{kodepesertabawahan}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response getCatatanKomitmen(
			@Context HttpHeaders headers, 
			@PathParam("kodepeserta") Integer kodepeserta,
			@PathParam("kodepesertabawahan") Integer kodepesertabawahan
		) {
		Map<String, Object> response = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> metadata = new HashMap<String, Object>();

		AuthUser authUser = new AuthUser();
		if (SharedMethod.VerifyToken(authUser, headers, metadata)) {
			try {
				List<Map<String, Object>> list = KinerjaUtil.getListCatatanKomitmen(kodepeserta, kodepesertabawahan);
				response.put("list", list);
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
	
	@GET
	@Path("/catatan/kejadiankritisnegatif/kodepeserta/{kodepeserta}/kodepesertabawahan/{kodepesertabawahan}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response getCatatanKejadianKritisNegatif(
			@Context HttpHeaders headers, 
			@PathParam("kodepeserta") Integer kodepeserta,
			@PathParam("kodepesertabawahan") Integer kodepesertabawahan
		) {
		Map<String, Object> response = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> metadata = new HashMap<String, Object>();

		AuthUser authUser = new AuthUser();
		if (SharedMethod.VerifyToken(authUser, headers, metadata)) {
			try {
				List<Map<String, Object>> list = KinerjaUtil.getListCatatanKejadianKritis(kodepeserta, kodepesertabawahan);
				response.put("list", list);
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
	
	@GET
	@Path("/peserta/setting/siklus/kodepeserta/{kodepeserta}/kodesiklus/{kodesiklus}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response settingSiklusPeserta(
			@Context HttpHeaders headers,
			@PathParam("kodepeserta") Integer kodepeserta,
			@PathParam("kodesiklus") Integer kodesiklus
		) {
		
		Map<String, Object> metadata = new HashMap<String, Object>();
		Map<String, Object> metadataobj = new HashMap<String, Object>();

		AuthUser authUser = new AuthUser();
		if (SharedMethod.VerifyToken(authUser, headers, metadata)) {
			try {
				KinerjaUtil.settingSiklusPeserta(kodepeserta, kodesiklus, authUser.getUserid());
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
	@Path("/verifikasi/kinerja/kodeevaluasi/{kodeevaluasi}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response getVerifikasiKinerja(
			@Context HttpHeaders headers, 
			@PathParam("kodeevaluasi") Integer kodeevaluasi
		) {
		Map<String, Object> response = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> metadata = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();

		AuthUser authUser = new AuthUser();
		if (SharedMethod.VerifyToken(authUser, headers, metadata)) {
			try {
				SimpanVerifikasiKinerja verifikasikinerja = new SimpanVerifikasiKinerja();
				Integer kodepembinaan = KinerjaUtil.getKodePembinaanByKodeEvaluasi(kodeevaluasi);
				Integer kodeperencanaan = KinerjaUtil.getKodePerencanaanByKodePembinaan(kodepembinaan);
				Integer kodepeserta = KinerjaUtil.getKodePesertaByKodePerencanaan(kodeperencanaan);
				verifikasikinerja.setKodeevaluasi(kodeevaluasi);
				verifikasikinerja.setKodepembinaan(kodepembinaan);
				verifikasikinerja.setKodeperencanaan(kodeperencanaan);
				verifikasikinerja.setKodepeserta(kodepeserta);
				List<Komponen> komponens = KinerjaUtil.getKomponenByPeserta(kodepeserta);
				if(komponens!=null) {
					for(Komponen komponen : komponens) {
						if(komponen.getKode()==1) {
							List<SettingLockHasilKerja> hasilkerjas = KinerjaUtil.getPerencanaanHasilKerja(kodepeserta);
							if(hasilkerjas!=null) {
								for(SettingLockHasilKerja hasilkerja : hasilkerjas) {
									List<SettingLockKpi> kpis = KinerjaUtil.getPerencanaanKpi(hasilkerja.getKodetanggungjawab(), hasilkerja.getKode(), kodepeserta);
									if(kpis!=null) {
										hasilkerja.setKpi(kpis);
										for(SettingLockKpi kpi : kpis) {
											List<SettingLockKriteriaKpi> kriteriakpis = KinerjaUtil.getPerencanaanKriteriaKpi(kodepeserta, kpi.getKode(), kpi.getKodesettinglockkpi());
											kpi.setKriteria(kriteriakpis);
											
											List<SettingLockRencanaAktifitas> rencanaAktifitas = KinerjaUtil.getPerencanaanRencanaAktifitas(kpi.getKode(), kpi.getKodedetailkpi());
											kpi.setRencanaaktifitas(rencanaAktifitas);
											
											List<PembinaanDetail> details = KinerjaUtil.getPembinaanDetail(kpi.getKode());
											kpi.setPembinaandetails(details);
											
											EvaluasiKpi evaluasi = KinerjaUtil.getEvaluasiKpi(kpi.getKode());
											kpi.setEvaluasi(evaluasi);
											
											VerifikasiKpi verifikasi = KinerjaUtil.getVerifikasiKpi(evaluasi.getKode());
											kpi.setVerifikasi(verifikasi);
										}
									}
								}
							}
							HasilKerjaHeader hasilKerjaHeader = new HasilKerjaHeader();
							hasilKerjaHeader.setHasilkerjas(hasilkerjas);
							komponen.setHasilkerja(hasilKerjaHeader);
						}
						else if(komponen.getKode()==3) {
							KomitmenHeader komitmenHeader = new KomitmenHeader();
							komitmenHeader.setKriterias(KinerjaUtil.getKriterias(komponen.getKode(), kodepeserta));
							komponen.setKomitmen(komitmenHeader);
						}
						else if(komponen.getKode()==5) {
							KejadianKritisNegatifHeader kejadianKritisNegatifHeader = new KejadianKritisNegatifHeader();
							kejadianKritisNegatifHeader.setKriterias(KinerjaUtil.getKriterias(komponen.getKode(), kodepeserta));
							
							List<KejadianKritisNegatif> kejadianKritisNegatifs = KinerjaUtil.getListEvaluasiKejadianKritis(kodepeserta); 
							kejadianKritisNegatifHeader.setKejadiankritisnegatifs(kejadianKritisNegatifs);
							
							List<KejadianKritisNegatifVerifikasi> kejadianKritisNegatifVerifikasis = KinerjaUtil.getListVerifikasiKejadianKritis(kodepeserta);
							kejadianKritisNegatifHeader.setKejadiankritisnegatifverifikasis(kejadianKritisNegatifVerifikasis);
							
							komponen.setKejadiankritisnegatif(kejadianKritisNegatifHeader);
						}
						else if(komponen.getKode()==6) {
							List<Inovasi> inovasis = KinerjaUtil.getEvaluasiInovasi(kodepembinaan);
							InovasiHeader inovasiHeader = new InovasiHeader();
							inovasiHeader.setInovasis(inovasis);
							
							List<InovasiVerifikasi> verifikasis = KinerjaUtil.getVerifikasiInovasi(kodepembinaan);
							inovasiHeader.setVerifikasis(verifikasis);
							
							inovasiHeader.setKriterias(KinerjaUtil.getKriterias(komponen.getKode(), kodepeserta));
							komponen.setInovasi(inovasiHeader);
						}
						else if(komponen.getKode()==7) {
							CreatingFutureLeader creatingfutureleader = KinerjaUtil.getEvaluasiCreatingFutureLeader(kodepembinaan);
							CreatingFutureLeaderVerifikasi creatingFutureLeaderVerifikasi = KinerjaUtil.getVerifikasiCreatingFutureLeader(kodepembinaan);
							
							CreatingFutureLeaderHeader creatingFutureLeaderHeader = new CreatingFutureLeaderHeader();
							creatingFutureLeaderHeader.setKriterias(KinerjaUtil.getKriterias(komponen.getKode(), kodepeserta));
							creatingFutureLeaderHeader.setCreatingfutureleader(creatingfutureleader);
							creatingFutureLeaderHeader.setCreatingfutureleaderverifikasi(creatingFutureLeaderVerifikasi);
							komponen.setCreatingfutureleader(creatingFutureLeaderHeader);
						}
						else if(komponen.getKode()==8) {
							List<PublikasiKaryaIlmiah> publikasikaryailmiahs = KinerjaUtil.getEvaluasiPublikasiKaryaIlmiah(kodepembinaan);
							List<PublikasiKaryaIlmiahVerifikasi> publikasikaryailmiahverifikasis = KinerjaUtil.getVerifikasiPublikasiKaryaIlmiah(kodepembinaan);
							
							PublikasiKaryaIlmiahHeader publikasiKaryaIlmiahHeader = new PublikasiKaryaIlmiahHeader();
							publikasiKaryaIlmiahHeader.setKriterias(KinerjaUtil.getKriterias(komponen.getKode(), kodepeserta));
							publikasiKaryaIlmiahHeader.setPublikasikaryailmiahs(publikasikaryailmiahs);
							publikasiKaryaIlmiahHeader.setPublikasikaryailmiahverifikasis(publikasikaryailmiahverifikasis);
							komponen.setPublikasikaryailmiah(publikasiKaryaIlmiahHeader);
						}
					}
				}
				verifikasikinerja.setKomponens(komponens);
				
				List<Map<String, Object>> catatans = KinerjaUtil.getListCatatanPerencanaan(kodepeserta);
				data.put("verifikasi", verifikasikinerja);
				data.put("catatans", catatans);
				data.put("pegawai", HcisRest.getRowDetil(5, kodepembinaan));
				
				response.put("data", data);
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
	@Path("/verifikasi/kinerja/simpan")
	@Consumes("application/json")
	@Produces("application/json")
	public Response setVerifikasiKinerja(
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
				SimpanVerifikasiKinerja json = mapper.readValue(data, SimpanVerifikasiKinerja.class);
				
				query = "DECLARE @Hasilkerja kinerja.settinglockhasilkerjaAsTable;";
				List<ParamQuery> paramQueries = new ArrayList<>();
				Integer index = 0;
				ParamQuery param = null;
				
				List<SettingLockHasilKerja> hasilkerjas = null;
				List<KejadianKritisNegatifVerifikasi> kejadiankritisnegatifs = null;
				List<InovasiVerifikasi> inovasis = null;
				CreatingFutureLeaderVerifikasi creatingfutureleader = null;
				List<PublikasiKaryaIlmiahVerifikasi> publikasikaryailmiahs = null;
				for (Komponen komponen : json.getKomponens()) {
					switch (komponen.getKode()) {
					case 1:
						hasilkerjas = komponen.getHasilkerja().getHasilkerjas();
						break;
					case 5:
						kejadiankritisnegatifs = komponen.getKejadiankritisnegatif().getKejadiankritisnegatifverifikasis();
						break;
					case 6:
						inovasis = komponen.getInovasi().getVerifikasis();
						break;
					case 7:
						creatingfutureleader = komponen.getCreatingfutureleader().getCreatingfutureleaderverifikasi();
						break;
					case 8:
						publikasikaryailmiahs = komponen.getPublikasikaryailmiah().getPublikasikaryailmiahverifikasis();
						break;
					default:
						break;
					}
				}
				
				//if(creatingfutureleader==null) 
					//creatingfutureleader = new CreatingFutureLeader();
				
				query += "DECLARE @Haskers kinerja.verifikasihaskerkpiAsTable;";
				query += "DECLARE @Kkns kinerja.verifikasinonhaskerAsTable;";
				query += "DECLARE @Inovasis kinerja.verifikasinonhaskerAsTable;";
				query += "DECLARE @Cfl kinerja.verifikasinonhaskerAsTable;";
				query += "DECLARE @Pkis kinerja.verifikasinonhaskerAsTable;";
				
				for (SettingLockHasilKerja hasilkerja : hasilkerjas) {
					for(SettingLockKpi kpi : hasilkerja.getKpi()) {
						
						index++;
						param = new ParamQuery();
						param.setIndex(index);
						param.setType("int");
						param.setValue(kpi.getVerifikasi().getKode()==null?null:kpi.getVerifikasi().getKode().toString());
						paramQueries.add(param);
						
						index++;
						param = new ParamQuery();
						param.setIndex(index);
						param.setType("int");
						param.setValue(kpi.getEvaluasi().getKode().toString());
						paramQueries.add(param);
						
						index++;
						param = new ParamQuery();
						param.setIndex(index);
						param.setType("int");
						param.setValue(kpi.getVerifikasi().getRating().toString());
						paramQueries.add(param);
						
						index++;
						param = new ParamQuery();
						param.setIndex(index);
						param.setType("string");
						param.setValue(kpi.getVerifikasi().getCatatan());
						paramQueries.add(param);
						
						query += "insert into @Haskers (kode, kodeevaluasihaskerkpi, rating, catatan) values (?,?,?,?);";
					}
				}
				
				if(kejadiankritisnegatifs!=null) {
					for (KejadianKritisNegatifVerifikasi kkn : kejadiankritisnegatifs) {
						
						index++;
						param = new ParamQuery();
						param.setIndex(index);
						param.setType("int");
						param.setValue(kkn.getKode()==null?null:kkn.getKode().toString());
						paramQueries.add(param);
						
						index++;
						param = new ParamQuery();
						param.setIndex(index);
						param.setType("string");
						param.setValue(kkn.getNama());
						paramQueries.add(param);
						
						index++;
						param = new ParamQuery();
						param.setIndex(index);
						param.setType("int");
						param.setValue(kkn.getRating().toString());
						paramQueries.add(param);
						
						index++;
						param = new ParamQuery();
						param.setIndex(index);
						param.setType("string");
						param.setValue(kkn.getKeterangan());
						paramQueries.add(param);
						
						index++;
						param = new ParamQuery();
						param.setIndex(index);
						param.setType("string");
						param.setValue(kkn.getCatatan());
						paramQueries.add(param);
						
						query += "insert into @Kkns (kode, nama, rating, keterangan, catatan) values (?,?,?,?,?);";
					}
				}
				
				if(inovasis!=null) {
					for (InovasiVerifikasi inovasi : inovasis) {
						
						index++;
						param = new ParamQuery();
						param.setIndex(index);
						param.setType("int");
						param.setValue(inovasi.getKode()==null?null:inovasi.getKode().toString());
						paramQueries.add(param);
						
						index++;
						param = new ParamQuery();
						param.setIndex(index);
						param.setType("string");
						param.setValue(inovasi.getNama());
						paramQueries.add(param);
						
						index++;
						param = new ParamQuery();
						param.setIndex(index);
						param.setType("int");
						param.setValue(inovasi.getRating().toString());
						paramQueries.add(param);
						
						index++;
						param = new ParamQuery();
						param.setIndex(index);
						param.setType("string");
						param.setValue(inovasi.getDeskripsi());
						paramQueries.add(param);
						
						index++;
						param = new ParamQuery();
						param.setIndex(index);
						param.setType("string");
						param.setValue(inovasi.getCatatan());
						paramQueries.add(param);
						
						query += "insert into @Inovasis (kode, nama, rating, deskripsi, catatan) values (?,?,?,?,?);";
					}
				}
					
				if(publikasikaryailmiahs!=null) {
					for (PublikasiKaryaIlmiahVerifikasi pki : publikasikaryailmiahs) {
						
						index++;
						param = new ParamQuery();
						param.setIndex(index);
						param.setType("int");
						param.setValue(pki.getKode()==null?null:pki.getKode().toString());
						paramQueries.add(param);
						
						index++;
						param = new ParamQuery();
						param.setIndex(index);
						param.setType("string");
						param.setValue(pki.getJudul());
						paramQueries.add(param);
						
						index++;
						param = new ParamQuery();
						param.setIndex(index);
						param.setType("string");
						param.setValue(pki.getPublisher());
						paramQueries.add(param);
						
						index++;
						param = new ParamQuery();
						param.setIndex(index);
						param.setType("int");
						param.setValue(pki.getRating().toString());
						paramQueries.add(param);
						
						index++;
						param = new ParamQuery();
						param.setIndex(index);
						param.setType("string");
						param.setValue(pki.getKeterangan());
						paramQueries.add(param);
						
						index++;
						param = new ParamQuery();
						param.setIndex(index);
						param.setType("string");
						param.setValue(pki.getCatatan());
						paramQueries.add(param);
						
						query += "insert into @Pkis (kode, judul, publisher, rating, keterangan, catatan) values (?,?,?,?,?,?);";
					}
				}
				
				if(creatingfutureleader!=null) {
					index++;
					param = new ParamQuery();
					param.setIndex(index);
					param.setType("int");
					param.setValue(creatingfutureleader.getKode()==null?null:creatingfutureleader.getKode().toString());
					paramQueries.add(param);
					
					index++;
					param = new ParamQuery();
					param.setIndex(index);
					param.setType("int");
					param.setValue(creatingfutureleader.getRating().toString());
					paramQueries.add(param);
					
					index++;
					param = new ParamQuery();
					param.setIndex(index);
					param.setType("string");
					param.setValue(creatingfutureleader.getCatatan());
					paramQueries.add(param);
					
					query += "insert into @Cfl (kode, rating, catatan) values (?,?,?);";
				}
				
				query += "exec kinerja.sp_verifikasi_simpan @Haskers,@Kkns,@Inovasis,@Cfl,@Pkis,?,?,?";
				con = new Koneksi().getConnection();
				cs = con.prepareCall(query);
				index = 0;
				for (ParamQuery row : paramQueries) {
					index++;
					if(row.getType().equalsIgnoreCase("int")) {
						if(row.getValue()!=null) {
							cs.setInt(row.getIndex(), Integer.parseInt(row.getValue()));
						}
						else {
							cs.setNull(row.getIndex(), java.sql.Types.INTEGER);
						}
					}
					else if(row.getType().equalsIgnoreCase("float")) {
						if(row.getValue()!=null) {
							cs.setFloat(row.getIndex(), Float.parseFloat(row.getValue()));
						}
						else {
							cs.setNull(row.getIndex(), java.sql.Types.FLOAT);
						}
					}
					else if(row.getType().equalsIgnoreCase("date")) {
						if(row.getValue()!=null) {
							cs.setDate(row.getIndex(), Utils.StringDateToSqlDate(row.getValue()));
						}
						else {
							cs.setNull(row.getIndex(), java.sql.Types.DATE);
						}
					}
					else {
						if(row.getValue()!=null) {
							cs.setString(row.getIndex(), row.getValue());
						}
						else {
							cs.setNull(row.getIndex(), java.sql.Types.VARCHAR);
						}
					}
				}
				cs.setInt(index+1, json.getKodeevaluasi());
				cs.setInt(index+2, json.getStatus());
				cs.setInt(index+3, authUser.getUserid());
				cs.executeUpdate();
				
				metadata.put("code", 1);
				metadata.put("message", "Simpan sukses");
			}
			catch (SQLException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				e.printStackTrace();
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
	@Path("/{penilaian}/kinerja/hitung")
	@Produces("application/json")
	public Response hitungEvaluasiVerifikasi(
			@Context HttpHeaders headers, 
			@PathParam("penilaian") String penilaian,
			String data, 
			@Context HttpServletRequest request
		) {

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
				KinerjaEvaluasiVerifikasiHitung json = mapper.readValue(data, KinerjaEvaluasiVerifikasiHitung.class);
				
				String query = "";
				if(penilaian.equalsIgnoreCase("evaluasi")) {
					query = "exec kinerja.sp_hitung_evalusi_kinerja ?,?,?";
				}
				else if(penilaian.equalsIgnoreCase("verifikasi")) {
					query = "exec kinerja.sp_hitung_verifikasi_kinerja ?,?,?";
				}
				else {
					return Response.status(Status.BAD_REQUEST).build();
				}
				
				con = new Koneksi().getConnection();
				cs = con.prepareCall(query);
				cs.setInt(1, json.getKodeperiodekinerja());
				cs.setInt(2, json.getKodepeserta());
				cs.setInt(3, authUser.getUserid());
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
	
	@POST
	@Path("/perpegawai/list/{start}/{limit}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response getListPesertaPegawai(
			@Context HttpHeaders headers, 
			@PathParam("start") Integer start, 
			@PathParam("limit") Integer limit,
			String data
		) {
		Map<String, Object> response = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> metadata = new HashMap<String, Object>();

		AuthUser authUser = new AuthUser();
		if (SharedMethod.VerifyToken(authUser, headers, metadata)) {
			Connection con = null;
			ResultSet rs = null;
			CallableStatement cs = null;
			String order = null;
			String filter = null;
			String query = null;
			try {
				JsonNode json = null;
				if (data != null) {
					if (!data.isEmpty()) {
						ObjectMapper mapper = new ObjectMapper();
						json = mapper.readTree(data);
						order = json.path("sort").isMissingNode() ? null
								: SharedMethod.getSortedColumn(mapper.writeValueAsString(json.path("sort")));
						filter = json.path("filter").isMissingNode() ? null
								: SharedMethod.getFilteredColumn(mapper.writeValueAsString(json.path("filter")), null);
					}
				}
				
				query = "exec kinerja.sp_listpesertabynpp ?,?,?,?,?,?";
				con = new Koneksi().getConnection();
				cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
				cs.setInt(1, start);
				cs.setInt(2, limit);
				cs.setInt(3, 1);
				cs.setString(4, order);
				cs.setString(5, filter);
				cs.setString(6, authUser.getNpp());
				rs = cs.executeQuery();
				metadata.put("code", 1);
				metadata.put("message", Response.Status.NO_CONTENT.toString());
				metadata.put("rowcount", 0);

				if (rs.next()) {
					metadata.put("rowcount", rs.getInt("jumlah"));
				}
				
				rs.close();
				cs.close();
				
				cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
				cs.setInt(1, start);
				cs.setInt(2, limit);
				cs.setInt(3, 0);
				cs.setString(4, order);
				cs.setString(5, filter);
				cs.setString(6, authUser.getNpp());
				rs = cs.executeQuery();
				List<id.go.bpjskesehatan.entitas.kinerja.Peserta> rows = new ArrayList<>();
				while(rs.next()) {
					id.go.bpjskesehatan.entitas.kinerja.Peserta row = new id.go.bpjskesehatan.entitas.kinerja.Peserta();
					row.setKode(rs.getInt("kode"));
					row.setKodepenugasan(rs.getInt("kodepenugasan"));
					row.setRow_status(rs.getShort("row_status"));
					row.setNamastatuskinerja(rs.getString("namastatuskinerja"));
					row.setStatuskinerja(rs.getShort("statuskinerja"));
					row.setInvited(rs.getShort("invited"));
					row.setKodegrade(rs.getString("kodegrade"));
					row.setNamagrade(rs.getString("namagrade"));
					row.setKodejobprefix(rs.getString("kodejobprefix"));
					row.setNamajobprefix(rs.getString("namajobprefix"));
					row.setBobotmutasi(rs.getFloat("bobotmutasi"));
					row.setPic(rs.getString("pic"));
					row.setPicnama(rs.getString("picnama"));
					row.setKodeperencanaan(rs.getInt("kodeperencanaan"));
					row.setKodepembinaan(rs.getInt("kodepembinaan"));
					
					Periodekinerja periodekinerja = new Periodekinerja();
					periodekinerja.setKode(rs.getInt("kodeperiodekinerja"));
					periodekinerja.setNama(rs.getString("namaperiodekinerja"));
					row.setPeriodekinerja(periodekinerja);
					Penugasan penugasan = new Penugasan();
					penugasan.setKode(rs.getInt("kodepenugasan"));
					id.go.bpjskesehatan.entitas.karyawan.Pegawai pegawai = new id.go.bpjskesehatan.entitas.karyawan.Pegawai();
					pegawai.setNpp(rs.getString("npppegawai"));
					pegawai.setNama(rs.getString("namapegawai"));
					penugasan.setPegawai(pegawai);
					Jabatan jabatan = new Jabatan();
					jabatan.setKode(rs.getString("kodejabatan"));
					jabatan.setNama(rs.getString("namajabatan"));
					JobTitle jt = new JobTitle();
					jt.setKode(rs.getString("kodejobtitle"));
					jt.setNama(rs.getString("namajabatan"));
					jabatan.setJobtitle(jt);
					UnitKerja unitkerja = new UnitKerja();
					unitkerja.setKode(rs.getString("kodeunitkerja"));
					unitkerja.setNama(rs.getString("namaunitkerja"));
					Office office = new Office();
					office.setKode(rs.getString("kodeoffice"));
					office.setNama(rs.getString("namaoffice"));
					unitkerja.setOffice(office);
					jabatan.setUnitkerja(unitkerja);
					penugasan.setJabatan(jabatan);
					penugasan.setIsmutation(rs.getInt("ismutation"));
					penugasan.setTatjabatan(rs.getDate("tatjabatan"));
					row.setPenugasan(penugasan);
					rows.add(row);
				}
				
				response.put("list", rows);
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
	
}