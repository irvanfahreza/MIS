package id.go.bpjskesehatan.service.v2.cuti;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import id.go.bpjskesehatan.database.Koneksi;
import id.go.bpjskesehatan.entitas.KomponenGaji;
import id.go.bpjskesehatan.entitas.Metadata;
import id.go.bpjskesehatan.entitas.Respon;
import id.go.bpjskesehatan.entitas.Result;
import id.go.bpjskesehatan.entitas.cuti.SaveCuti;
import id.go.bpjskesehatan.entitas.cuti.SaveCutiTgls;
import id.go.bpjskesehatan.entitas.cuti.SaveCutis;
import id.go.bpjskesehatan.entitas.hcis.Notifikasi;
import id.go.bpjskesehatan.entitas.karyawan.Penugasan;
import id.go.bpjskesehatan.entitas.organisasi.Office;
import id.go.bpjskesehatan.service.Tools;
import id.go.bpjskesehatan.service.mobile.v1.AuthUser;
import id.go.bpjskesehatan.service.v2.cuti.entitas.Infopribadi;
import id.go.bpjskesehatan.service.v2.cuti.entitas.Kuota;
import id.go.bpjskesehatan.service.v2.cuti.entitas.Spm;
import id.go.bpjskesehatan.service.v2.cuti.entitas.SpmDetil;
import id.go.bpjskesehatan.service.v2.entitas.Akun;
import id.go.bpjskesehatan.service.v2.entitas.Anggaran;
import id.go.bpjskesehatan.service.v2.entitas.Program;
import id.go.bpjskesehatan.util.MyException;
import id.go.bpjskesehatan.util.SharedMethod;
import id.go.bpjskesehatan.util.Utils;

@Path("/v2/cuti")
public class Cuti2Rest {	
	
	@Context
    private ServletContext context;

	@POST
	@Path("/grid/{page}/{row}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response ListGrid(@Context HttpHeaders headers,
			@PathParam("page") String page, @PathParam("row") String row, String data) {
		
		Respon<SaveCuti> response = new Respon<SaveCuti>();
		Metadata metadata = new Metadata();
		Result<SaveCuti> result = new Result<SaveCuti>();
		
		Connection con = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		CallableStatement cs = null;
		PreparedStatement ps = null;
		String order = null;
		String filter = null;
		String filter2 = null;
		String filter3 = null;
		String query = null;
		Boolean ok = true;

		AuthUser authUser = new AuthUser();
		if (SharedMethod.VerifyToken(authUser, headers, metadata)) {
		//if (true) {
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
				
				String npp = "";
				if(json.path("data").path("filter").path("npp").isMissingNode()) {
					throw new MyException("Filter npp tidak ditemukan");
				}
				else {
					String nppFilter = json.path("data").path("filter").path("npp").asText();
					npp = nppFilter.replaceAll("[^0-9.]", "");
					if(!authUser.getNpp().equalsIgnoreCase(npp)) {
						throw new MyException("Data kosong");
					}
				}
				
				
				if(ok) {
					query = "exec cuti.sp_listcuti ?, ?, ?, ?, ?, ?, ?";
					con = new Koneksi().getConnection();
					cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
					cs.setInt(1, Integer.parseInt(page));
					cs.setInt(2, Integer.parseInt(row));
					cs.setInt(3, 1);
					cs.setString(4, order);
					cs.setString(5, filter);
					cs.setString(6, filter2);
					cs.setString(7, filter3);
					rs = cs.executeQuery();
					metadata.setCode(1);
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
					cs.setString(6, filter2);
					cs.setString(7, filter3);
					rs = cs.executeQuery();
					
					List<SaveCuti> saveCutis = new ArrayList<>();
					while (rs.next()) {
						SaveCuti cuti = new SaveCuti();
						cuti.setKode(rs.getInt("kode"));
						cuti.setNpp(rs.getString("npp"));
						cuti.setNama(rs.getString("nama"));
						cuti.setKodepenugasan(rs.getInt("kodepenugasan"));
						cuti.setKodetipe(rs.getInt("kodetipe"));
						cuti.setNamatipe(rs.getString("namatipe"));
						cuti.setDengantunjangan(rs.getInt("dengantunjangan"));
						cuti.setTelp(rs.getString("telp"));
						cuti.setAlamatcuti(rs.getString("alamatcuti"));
						cuti.setAlasancuti(rs.getString("alasancuti"));
						cuti.setLampiran(rs.getString("lampiran")==null?"":rs.getString("lampiran"));
						cuti.setGajipokok(rs.getBigDecimal("gajipokok"));
						cuti.setTunjanganjabatan(rs.getBigDecimal("tunjjabatan"));
						cuti.setTunjanganprestasi(rs.getBigDecimal("tupres"));
						cuti.setTunjanganutilitas(rs.getBigDecimal("utilitas"));
						cuti.setTotal(rs.getBigDecimal("total"));
						cuti.setTotal2(rs.getBigDecimal("total2"));
						cuti.setTunjanganbbm((rs.getBigDecimal("nominaltunjanganbbm")==null || rs.getBigDecimal("nominaltunjanganbbm").compareTo(new BigDecimal("0.00"))==0)?0:1);
						cuti.setNominaltunjanganbbm(rs.getBigDecimal("nominaltunjanganbbm"));
						cuti.setPangkat(rs.getString("pangkat"));
						cuti.setGrade(rs.getString("grade"));
						cuti.setNamajobtitle(rs.getString("namajobtitle"));
						cuti.setNamaunitkerja(rs.getString("namaunitkerja"));
						cuti.setTglajukan(rs.getTimestamp("tglajukan"));
						cuti.setStatuspersetujuan(rs.getInt("statuspersetujuan"));
						cuti.setTglmulai(rs.getDate("tglmulai"));
						cuti.setTglselesai(rs.getDate("tglselesai"));
						cuti.setStatusterakhir(rs.getString("statusterakhir"));
						cuti.setJenispengajuan(rs.getInt("jenispengajuan"));
						cuti.setIspembatalan(0);
						cuti.setSelected(false);
						cuti.setVselected(0);
						cuti.setNamaoffice(rs.getString("namaoffice"));
						cuti.setNomor(rs.getString("nomor"));
						cuti.setVerifsdm(rs.getInt("verifsdm"));
						cuti.setVerifkeu(rs.getInt("verifkeu"));
						cuti.setStatusverifsdm(rs.getString("statusverifsdm"));
						cuti.setStatusverifkeu(rs.getString("statusverifkeu"));
						cuti.setAmbilhari(rs.getInt("ambilhari"));
						cuti.setMengetahuidepdirsdm(rs.getString("mengetahuidepdirsdm"));
						cuti.setTglmulai2(rs.getDate("tglmulai2"));
						cuti.setTglselesai2(rs.getDate("tglselesai2"));
						cuti.setTglhpl(rs.getDate("tglhpl"));
						cuti.setBolehverifkeu(rs.getInt("bolehverifkeu"));
						cuti.setCatatantolak(rs.getString("catatantolak"));
						cuti.setTahuninput(rs.getInt("tahuninput"));
						
						ps = con.prepareStatement("select *, iif(tgl < cast(getdate() as date),0,iif(tgl = cast(getdate() as date) and cast(getdate() as time) > '08:00:00',0,1)) as viewcheck from cuti.cutitgl where kodecuti=?");
						ps.setInt(1, cuti.getKode());
						rs2 = ps.executeQuery();
						ArrayList<SaveCutiTgls> saveCutiTgls = new ArrayList<>();
						while (rs2.next()) {
							SaveCutiTgls tgls = new SaveCutiTgls();
							tgls.setKode(rs2.getInt("kode"));
							tgls.setTgl(rs2.getDate("tgl"));
							tgls.setDeleted(0);
							tgls.setBatalkan(false);
							tgls.setStatus(rs2.getInt("status"));
							tgls.setViewcheck(rs2.getBoolean("viewcheck"));
							saveCutiTgls.add(tgls);
						}
						cuti.setTgls(saveCutiTgls);
						saveCutis.add(cuti);
						
						metadata.setCode(1);
						metadata.setMessage("OK");
					}
					response.setList(saveCutis);
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
				if (rs2 != null) {
					try {
						rs2.close();
					} catch (SQLException e) {
					}
				}
				
				if (rs != null) {
					try {
						rs.close();
					} catch (SQLException e) {
					}
				}

				if (ps != null) {
					try {
						ps.close();
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
	@Path("/gridapproval/{page}/{row}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response ListGridApproval(@Context HttpHeaders headers,
			@PathParam("page") String page, @PathParam("row") String row, String data) {
		
		Respon<SaveCuti> response = new Respon<SaveCuti>();
		Metadata metadata = new Metadata();
		Result<SaveCuti> result = new Result<SaveCuti>();
		
		Connection con = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		CallableStatement cs = null;
		PreparedStatement ps = null;
		String order = null;
		String filter = null;
		String filter2 = null;
		String filter3 = null;
		String query = null;
		Boolean ok = true;

		AuthUser authUser = new AuthUser();
		if (SharedMethod.VerifyToken(authUser, headers, metadata)) {
		//if (true) {
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
				/*
				String npp = "";
				if(json.path("data").path("filter").path("npp").isMissingNode()) {
					throw new MyException("Filter npp tidak ditemukan");
				}
				else {
					String nppFilter = json.path("data").path("filter").path("npp").asText();
					npp = nppFilter.replaceAll("[^0-9.]", "");
					if(!authUser.getNpp().equalsIgnoreCase(npp)) {
						throw new MyException("Data kosong");
					}
				}
				*/
				
				
				if(ok) {
					query = "exec cuti.sp_listcuti ?, ?, ?, ?, ?, ?, ?";
					con = new Koneksi().getConnection();
					cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
					cs.setInt(1, Integer.parseInt(page));
					cs.setInt(2, Integer.parseInt(row));
					cs.setInt(3, 1);
					cs.setString(4, order);
					cs.setString(5, filter);
					cs.setString(6, filter2);
					cs.setString(7, filter3);
					rs = cs.executeQuery();
					metadata.setCode(1);
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
					cs.setString(6, filter2);
					cs.setString(7, filter3);
					rs = cs.executeQuery();
					
					List<SaveCuti> saveCutis = new ArrayList<>();
					while (rs.next()) {
						SaveCuti cuti = new SaveCuti();
						cuti.setKode(rs.getInt("kode"));
						cuti.setNpp(rs.getString("npp"));
						cuti.setNama(rs.getString("nama"));
						cuti.setKodepenugasan(rs.getInt("kodepenugasan"));
						cuti.setKodetipe(rs.getInt("kodetipe"));
						cuti.setNamatipe(rs.getString("namatipe"));
						cuti.setDengantunjangan(rs.getInt("dengantunjangan"));
						cuti.setTelp(rs.getString("telp"));
						cuti.setAlamatcuti(rs.getString("alamatcuti"));
						cuti.setAlasancuti(rs.getString("alasancuti"));
						cuti.setLampiran(rs.getString("lampiran")==null?"":rs.getString("lampiran"));
						cuti.setGajipokok(rs.getBigDecimal("gajipokok"));
						cuti.setTunjanganjabatan(rs.getBigDecimal("tunjjabatan"));
						cuti.setTunjanganprestasi(rs.getBigDecimal("tupres"));
						cuti.setTunjanganutilitas(rs.getBigDecimal("utilitas"));
						cuti.setTotal(rs.getBigDecimal("total"));
						cuti.setTotal2(rs.getBigDecimal("total2"));
						cuti.setTunjanganbbm((rs.getBigDecimal("nominaltunjanganbbm")==null || rs.getBigDecimal("nominaltunjanganbbm").compareTo(new BigDecimal("0.00"))==0)?0:1);
						cuti.setNominaltunjanganbbm(rs.getBigDecimal("nominaltunjanganbbm"));
						cuti.setPangkat(rs.getString("pangkat"));
						cuti.setGrade(rs.getString("grade"));
						cuti.setNamajobtitle(rs.getString("namajobtitle"));
						cuti.setNamaunitkerja(rs.getString("namaunitkerja"));
						cuti.setTglajukan(rs.getTimestamp("tglajukan"));
						cuti.setStatuspersetujuan(rs.getInt("statuspersetujuan"));
						cuti.setTglmulai(rs.getDate("tglmulai"));
						cuti.setTglselesai(rs.getDate("tglselesai"));
						cuti.setStatusterakhir(rs.getString("statusterakhir"));
						cuti.setJenispengajuan(rs.getInt("jenispengajuan"));
						cuti.setIspembatalan(0);
						cuti.setSelected(false);
						cuti.setVselected(0);
						cuti.setNamaoffice(rs.getString("namaoffice"));
						cuti.setNomor(rs.getString("nomor"));
						cuti.setVerifsdm(rs.getInt("verifsdm"));
						cuti.setVerifkeu(rs.getInt("verifkeu"));
						cuti.setStatusverifsdm(rs.getString("statusverifsdm"));
						cuti.setStatusverifkeu(rs.getString("statusverifkeu"));
						cuti.setAmbilhari(rs.getInt("ambilhari"));
						cuti.setMengetahuidepdirsdm(rs.getString("mengetahuidepdirsdm"));
						cuti.setTglmulai2(rs.getDate("tglmulai2"));
						cuti.setTglselesai2(rs.getDate("tglselesai2"));
						cuti.setTglhpl(rs.getDate("tglhpl"));
						cuti.setBolehverifkeu(rs.getInt("bolehverifkeu"));
						cuti.setCatatantolak(rs.getString("catatantolak"));
						cuti.setTahuninput(rs.getInt("tahuninput"));
						
						ps = con.prepareStatement("select *, iif(tgl < cast(getdate() as date),0,iif(tgl = cast(getdate() as date) and cast(getdate() as time) > '08:00:00',0,1)) as viewcheck from cuti.cutitgl where kodecuti=?");
						ps.setInt(1, cuti.getKode());
						rs2 = ps.executeQuery();
						ArrayList<SaveCutiTgls> saveCutiTgls = new ArrayList<>();
						while (rs2.next()) {
							SaveCutiTgls tgls = new SaveCutiTgls();
							tgls.setKode(rs2.getInt("kode"));
							tgls.setTgl(rs2.getDate("tgl"));
							tgls.setDeleted(0);
							tgls.setBatalkan(false);
							tgls.setStatus(rs2.getInt("status"));
							tgls.setViewcheck(rs2.getBoolean("viewcheck"));
							saveCutiTgls.add(tgls);
						}
						cuti.setTgls(saveCutiTgls);
						saveCutis.add(cuti);
						
						metadata.setCode(1);
						metadata.setMessage("OK");
					}
					response.setList(saveCutis);
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
				if (rs2 != null) {
					try {
						rs2.close();
					} catch (SQLException e) {
					}
				}
				
				if (rs != null) {
					try {
						rs.close();
					} catch (SQLException e) {
					}
				}

				if (ps != null) {
					try {
						ps.close();
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
	@Path("/gridsdm/{kodeoffice}/{page}/{row}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response ListGridSDM(@Context HttpHeaders headers,
			@PathParam("kodeoffice") String kodeoffice, @PathParam("page") String page, @PathParam("row") String row, String data) {
		
		Respon<SaveCuti> response = new Respon<SaveCuti>();
		Metadata metadata = new Metadata();
		Result<SaveCuti> result = new Result<SaveCuti>();
		
		Connection con = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		CallableStatement cs = null;
		PreparedStatement ps = null;
		String order = null;
		String filter = null;
		String filter2 = null;
		String filter3 = null;
		String query = null;
		Boolean ok = true;

		if (SharedMethod.VerifyToken(headers, metadata)) {
		//if (true) {
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
				
				if(ok) {
					query = "exec cuti.sp_listsdmcuti ?, ?, ?, ?, ?, ?, ?, ?";
					con = new Koneksi().getConnection();
					cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
					cs.setString(1, kodeoffice);
					cs.setInt(2, Integer.parseInt(page));
					cs.setInt(3, Integer.parseInt(row));
					cs.setInt(4, 1);
					cs.setString(5, order);
					cs.setString(6, filter);
					cs.setString(7, filter2);
					cs.setString(8, filter3);
					rs = cs.executeQuery();
					metadata.setCode(1);
					metadata.setMessage(Response.Status.NO_CONTENT.toString());
					metadata.setRowcount(0);

					if (rs.next()) {
						metadata.setRowcount(rs.getInt("jumlah"));
					}
					rs.close();
					cs.close();
					
					cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
					cs.setString(1, kodeoffice);
					cs.setInt(2, Integer.parseInt(page));
					cs.setInt(3, Integer.parseInt(row));
					cs.setInt(4, 0);
					cs.setString(5, order);
					cs.setString(6, filter);
					cs.setString(7, filter2);
					cs.setString(8, filter3);
					rs = cs.executeQuery();
					
					List<SaveCuti> saveCutis = new ArrayList<>();
					while (rs.next()) {
						SaveCuti cuti = new SaveCuti();
						cuti.setKode(rs.getInt("kode"));
						cuti.setNpp(rs.getString("npp"));
						cuti.setNama(rs.getString("nama"));
						cuti.setKodepenugasan(rs.getInt("kodepenugasan"));
						cuti.setKodetipe(rs.getInt("kodetipe"));
						cuti.setNamatipe(rs.getString("namatipe"));
						cuti.setDengantunjangan(rs.getInt("dengantunjangan"));
						cuti.setTelp(rs.getString("telp"));
						cuti.setAlamatcuti(rs.getString("alamatcuti"));
						cuti.setAlasancuti(rs.getString("alasancuti"));
						cuti.setLampiran(rs.getString("lampiran")==null?"":rs.getString("lampiran"));
						cuti.setGajipokok(rs.getBigDecimal("gajipokok"));
						cuti.setTunjanganjabatan(rs.getBigDecimal("tunjjabatan"));
						cuti.setTunjanganprestasi(rs.getBigDecimal("tupres"));
						cuti.setTunjanganutilitas(rs.getBigDecimal("utilitas"));
						cuti.setTotal(rs.getBigDecimal("total"));
						cuti.setTotal2(rs.getBigDecimal("total2"));
						cuti.setTunjanganbbm((rs.getBigDecimal("nominaltunjanganbbm")==null || rs.getBigDecimal("nominaltunjanganbbm").compareTo(new BigDecimal("0.00"))==0)?0:1);
						cuti.setNominaltunjanganbbm(rs.getBigDecimal("nominaltunjanganbbm"));
						cuti.setPangkat(rs.getString("pangkat"));
						cuti.setGrade(rs.getString("grade"));
						cuti.setNamajobtitle(rs.getString("namajobtitle"));
						cuti.setNamaunitkerja(rs.getString("namaunitkerja"));
						cuti.setTglajukan(rs.getTimestamp("tglajukan"));
						cuti.setStatuspersetujuan(rs.getInt("statuspersetujuan"));
						cuti.setTglmulai(rs.getDate("tglmulai"));
						cuti.setTglselesai(rs.getDate("tglselesai"));
						cuti.setStatusterakhir(rs.getString("statusterakhir"));
						cuti.setJenispengajuan(rs.getInt("jenispengajuan"));
						cuti.setIspembatalan(0);
						cuti.setSelected(false);
						cuti.setVselected(0);
						cuti.setNamaoffice(rs.getString("namaoffice"));
						cuti.setNomor(rs.getString("nomor"));
						cuti.setVerifsdm(rs.getInt("verifsdm"));
						cuti.setVerifkeu(rs.getInt("verifkeu"));
						cuti.setStatusverifsdm(rs.getString("statusverifsdm"));
						cuti.setStatusverifkeu(rs.getString("statusverifkeu"));
						cuti.setAmbilhari(rs.getInt("ambilhari"));
						cuti.setMengetahuidepdirsdm(rs.getString("mengetahuidepdirsdm"));
						cuti.setTglmulai2(rs.getDate("tglmulai2"));
						cuti.setTglselesai2(rs.getDate("tglselesai2"));
						cuti.setTglhpl(rs.getDate("tglhpl"));
						cuti.setTglcuti(rs.getString("tglcuti"));
						cuti.setCetakspm(rs.getString("cetakspm"));
						cuti.setBolehverifsdm(rs.getInt("bolehverifsdm"));
						cuti.setCatatantolak(rs.getString("catatantolak"));
						cuti.setSdmbolehtolak(rs.getInt("sdmbolehtolak"));
						
						ps = con.prepareStatement("select *, iif(tgl < cast(getdate() as date),0,iif(tgl = cast(getdate() as date) and cast(getdate() as time) > '08:00:00',0,1)) as viewcheck from cuti.cutitgl where kodecuti=?");
						ps.setInt(1, cuti.getKode());
						rs2 = ps.executeQuery();
						ArrayList<SaveCutiTgls> saveCutiTgls = new ArrayList<>();
						while (rs2.next()) {
							SaveCutiTgls tgls = new SaveCutiTgls();
							tgls.setKode(rs2.getInt("kode"));
							tgls.setTgl(rs2.getDate("tgl"));
							tgls.setDeleted(0);
							tgls.setBatalkan(false);
							tgls.setStatus(rs2.getInt("status"));
							tgls.setViewcheck(rs2.getBoolean("viewcheck"));
							saveCutiTgls.add(tgls);
						}
						cuti.setTgls(saveCutiTgls);
						saveCutis.add(cuti);
						
						metadata.setCode(1);
						metadata.setMessage("OK");
					}
					response.setList(saveCutis);
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
				if (rs2 != null) {
					try {
						rs2.close();
					} catch (SQLException e) {
					}
				}
				
				if (rs != null) {
					try {
						rs.close();
					} catch (SQLException e) {
					}
				}

				if (ps != null) {
					try {
						ps.close();
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
	@Path("/getkomponengajibypenugasan")
	@Produces("application/json")
	public Response GetKomponenGajiByPenugasan(@Context HttpHeaders headers, String data) {
		
		Respon<KomponenGaji> response = new Respon<KomponenGaji>();
		Metadata metadata = new Metadata();
		Result<KomponenGaji> result = new Result<KomponenGaji>();
		
		Connection con = null;
		ResultSet rs = null;
		CallableStatement cs = null;
		String query = null;
		
		AuthUser authUser = new AuthUser();
		if (SharedMethod.VerifyToken(authUser, headers, metadata)) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				JsonNode json = mapper.readTree(data);
				Boolean ok = true;
				Boolean ok2 = true;
				String msg = "";
				StringBuilder str = new StringBuilder();
				
				if (!data.isEmpty()) {
					if(json.path("kodepenugasan").isMissingNode()){
						str.append("kodepenugasan").append(", ");
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
					Integer kodepenugasan = json.path("kodepenugasan").asInt();
					
					Penugasan penugasan = Tools.getPenugasanByKode(kodepenugasan);
					if(penugasan!=null) {
						if(!penugasan.getPegawai().getNpp().equalsIgnoreCase(authUser.getNpp())) {
							kodepenugasan=0;
						}
					}
					
					query = "exec karyawan.getgajibykodepenugasan ?";
					con = new Koneksi().getConnection();
					
					cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
					cs.setInt(1, kodepenugasan);
					Boolean isresult = cs.execute();
					
					metadata.setCode(0);
					metadata.setMessage("Data kosong.");
					if(isresult) {
						rs = cs.getResultSet();
						while (rs.next()) {
							KomponenGaji komponenGaji = new KomponenGaji();
							komponenGaji.setGajipokok(rs.getBigDecimal("gajipokok"));
							komponenGaji.setTunjprestasi(rs.getBigDecimal("tupres"));
							komponenGaji.setTunjutilitas(rs.getBigDecimal("utilitas"));
							komponenGaji.setTunjjabatan(rs.getBigDecimal("tunjjabatan"));
							komponenGaji.setTotal(rs.getBigDecimal("total"));
							komponenGaji.setTotal2(rs.getBigDecimal("total2"));
							komponenGaji.setPangkat(rs.getString("pangkat"));
							komponenGaji.setGrade(rs.getString("grade"));
							komponenGaji.setTunjanganbbm(rs.getInt("tunjanganbbm"));
							komponenGaji.setNominaltunjanganbbm(rs.getBigDecimal("nominaltunjanganbbm"));
							komponenGaji.setKodejobgrade(rs.getString("kodejobgrade"));
							response.setData(komponenGaji);
							metadata.setCode(1);
							metadata.setMessage("Ok.");
							result.setResponse(response);
						}
						rs.close();
					}
					cs.close();
					
				}
			} catch (Exception e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
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
	public Response simpanCuti(
			@Context HttpHeaders headers, 
			@FormDataParam("file") final InputStream uploadedInputStream, 
			@FormDataParam("file") FormDataContentDisposition fileDetail,
			@FormDataParam("file") String file,
			@FormDataParam("cuti") FormDataBodyPart cuti,
			@FormDataParam("act") String act) {
		
		Map<String, Object> metadata = new HashMap<String, Object>();
		Map<String, Object> metadataobj = new HashMap<String, Object>();
		FTPClient ftpClient = null;

		if (SharedMethod.VerifyToken(headers, metadata)) {
			try {
				cuti.setMediaType(MediaType.APPLICATION_JSON_TYPE);
				SaveCuti saveCuti = cuti.getValueAs(SaveCuti.class);
				
				Boolean adafile = false;
				String pathFile = null;
				String host = null;
	        	Integer port = null;
	        	String user = null;
	        	String pass = null;
				String namaFile = "";
				
				if(!(uploadedInputStream==null || fileDetail==null) && file.length() > 0) {
					adafile = true;
					pathFile = "file_cuti/";
					host = context.getInitParameter("ftp-host");
		        	port = Integer.parseInt(context.getInitParameter("ftp-port"));
		        	user = context.getInitParameter("ftp-user");
		        	pass = context.getInitParameter("ftp-pass");
					
					namaFile = fileDetail.getFileName();
		        	StringTokenizer st = new StringTokenizer(namaFile, ".");
		        	String extension = ""; 
		        	while(st.hasMoreTokens()) {
		        		extension = "."+st.nextToken();
		        	}
		        	String npp = saveCuti.getNpp();
		        	Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		        	namaFile = npp + "-" + timestamp.getTime() + extension;
				}
				
				Connection con = null;
				CallableStatement cs = null;
				String query = null;
				
				if(act.equalsIgnoreCase("create")) {
					try {
						if(saveCuti.getKodetipe()==1) {
							query = "DECLARE @TempList cuti.cutitglAsTable;";
							if(saveCuti.getAmbilhari()==1) {
								for (SaveCutiTgls item : saveCuti.getTgls()) {
									if(item.getDeleted()==0)
										query += "insert into @TempList values (" + item.getKode() + ", '" + item.getTgl() + "', " + item.getDeleted() + ", " + item.getVbatalkan() + ", " + item.getStatus() + ");";
								}
							}
							query += "exec cuti.sp_insertcutiWithTgls ?,?,?,?,?,?,?,?,?,@TempList,?,?,?,?,?,?;";
						}
						else {
							query = "exec cuti.sp_insertcuti ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?;";
						}
						con = new Koneksi().getConnection();
						cs = con.prepareCall(query);
						cs.setInt(1, saveCuti.getKodetipe());
						cs.setString(2, saveCuti.getNpp());
						cs.setInt(3, saveCuti.getKodepenugasan());
						cs.setInt(4, saveCuti.getDengantunjangan());
						cs.setString(5, saveCuti.getTelp());
						cs.setString(6, saveCuti.getAlamatcuti());
						cs.setString(7, saveCuti.getAlasancuti());
						if(adafile)
							cs.setString(8, namaFile);
						else
							cs.setNull(8, java.sql.Types.VARCHAR);
						cs.setInt(9, saveCuti.getUseract());
						if(saveCuti.getKodetipe()!=1) {
							cs.setDate(10, saveCuti.getTglmulai());
							cs.setDate(11, saveCuti.getTglselesai());
							cs.setNull(12, java.sql.Types.DATE);
							cs.setNull(13, java.sql.Types.DATE);
							cs.setNull(14, java.sql.Types.DATE);
							if(saveCuti.getKodetipe()==2) {
								cs.setDate(14, saveCuti.getTglhpl());
							}
							if(saveCuti.getKodetipe()==3) {
								if(saveCuti.getAmbilhari()==1) {
									cs.setDate(12, saveCuti.getTglmulai2());
									cs.setDate(13, saveCuti.getTglselesai2());
								}
							}
							cs.setInt(15, saveCuti.getAmbilhari());
						}
						else {
							cs.registerOutParameter(10, java.sql.Types.VARCHAR);
							cs.setNull(11, java.sql.Types.VARCHAR);
							cs.setNull(12, java.sql.Types.VARCHAR);
							cs.setNull(13, java.sql.Types.VARCHAR);
							cs.setNull(14, java.sql.Types.VARCHAR);
							cs.setInt(15, saveCuti.getAmbilhari());
						}
						cs.execute();
						metadata.put("code", 1);
						metadata.put("message", "Simpan berhasil.");
						if(saveCuti.getKodetipe()==1) {
							if(!(cs.getString(10)==null || cs.getString(10).isEmpty())) {
								metadata.put("message", cs.getString(10));
							}
						}
						
						if(adafile) {
							try {
								ftpClient = new FTPClient();
								ftpClient.connect(host,port);
					        	ftpClient.login(user, pass);
					        	ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
					        	Boolean upload = ftpClient.storeFile(pathFile + namaFile, uploadedInputStream);
					        	if(!upload) {
					        		metadata.put("code", 0);
									metadata.put("message", "Upload gagal.");
					        	}
					        	ftpClient.logout();
							}
							catch (Exception e) {
								e.printStackTrace();
							}
							finally {
								if(ftpClient.isConnected())
									ftpClient.disconnect();
							}
						}
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
						if(saveCuti.getKodetipe()==1) {
							query = "DECLARE @TempList cuti.cutitglAsTable;";
							if(saveCuti.getAmbilhari()==1) {
								for (SaveCutiTgls item : saveCuti.getTgls()) {
									if(item.getDeleted()==0)
										query += "insert into @TempList values (" + item.getKode() + ", '" + item.getTgl() + "', " + item.getDeleted() + ", " + item.getVbatalkan() + ", " + item.getStatus() + ");";
								}
							}
							query += "exec cuti.sp_updatecutiWithTgls ?,?,?,?,?,?,?,?,?,@TempList,?,?,?,?,?,?;";
						}
						else {
							query = "exec cuti.sp_updatecuti ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?;";
						}
						
						con = new Koneksi().getConnection();
						cs = con.prepareCall(query);
						cs.setInt(1, saveCuti.getKodetipe());
						cs.setString(2, saveCuti.getNpp());
						cs.setInt(3, saveCuti.getKodepenugasan());
						cs.setInt(4, saveCuti.getDengantunjangan());
						cs.setString(5, saveCuti.getTelp());
						cs.setString(6, saveCuti.getAlamatcuti());
						cs.setString(7, saveCuti.getAlasancuti());
						if(adafile)
							cs.setString(8, namaFile);
						else
							cs.setNull(8, java.sql.Types.VARCHAR);
						cs.setInt(9, saveCuti.getUseract());
						cs.setInt(10, saveCuti.getKode());
						
						if(saveCuti.getKodetipe()==1) {
							cs.setInt(11, saveCuti.getIspembatalan());
							cs.setString(12, saveCuti.getKodejobtitleatasan());
							cs.setString(13, saveCuti.getKodeofficeatasan());
							cs.setString(14, saveCuti.getNppatasan());
							cs.setInt(15, saveCuti.getAmbilhari());
						}
						else {
							cs.setDate(11, saveCuti.getTglmulai());
							cs.setDate(12, saveCuti.getTglselesai());
							cs.setNull(13, java.sql.Types.DATE);
							cs.setNull(14, java.sql.Types.DATE);
							cs.setNull(15, java.sql.Types.DATE);
							if(saveCuti.getKodetipe()==2) {
								cs.setDate(15, saveCuti.getTglhpl());
							}
							if(saveCuti.getKodetipe()==3) {
								if(saveCuti.getAmbilhari()==1) {
									cs.setDate(13, saveCuti.getTglmulai2());
									cs.setDate(14, saveCuti.getTglselesai2());
								}
							}
							cs.setInt(16, saveCuti.getAmbilhari());
						}
						cs.executeUpdate();
						
						metadata.put("code", 1);
						metadata.put("message", "Simpan berhasil.");
						
						if(adafile) {
							try {
								ftpClient = new FTPClient();
								ftpClient.connect(host,port);
					        	ftpClient.login(user, pass);
					        	ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
					        	
					        	if(!saveCuti.getLampiran().isEmpty()) {
					        		String deletedFile = saveCuti.getLampiran();
					        		ftpClient.deleteFile(pathFile + deletedFile);
					        	}
					        	
					        	Boolean upload = ftpClient.storeFile(pathFile + namaFile, uploadedInputStream);
					        	if(!upload) {
					        		metadata.put("code", 0);
									metadata.put("message", "Upload gagal.");
					        	}
							}
							catch (Exception e) {
								e.printStackTrace();
							}
							finally {
								if(ftpClient.isConnected())
									ftpClient.disconnect();
							}
						}
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
	public Response hapusCuti(@Context HttpHeaders headers, String data) {
		
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
					if(json.path("lampiran").isMissingNode()){
						str.append("lampiran").append(", ");
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
					String lampiran = json.path("lampiran").asText();
					
					query = "exec cuti.sp_deletecuti ?";
					con = new Koneksi().getConnection();
					
					cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
					cs.setInt(1, kode);
					cs.executeUpdate();
					
					if(!lampiran.isEmpty()) {
						FTPClient ftpClient = null;
						try {
							String pathFile = "file_cuti/";
							String host = context.getInitParameter("ftp-host");
							Integer port = Integer.parseInt(context.getInitParameter("ftp-port"));
							String user = context.getInitParameter("ftp-user");
							String pass = context.getInitParameter("ftp-pass");
							ftpClient = new FTPClient();
							ftpClient.connect(host,port);
				        	ftpClient.login(user, pass);
				        	ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
				        	String deletedFile = lampiran;
			        		ftpClient.deleteFile(pathFile + deletedFile);
			        		ftpClient.logout();
						}
						finally {
							if(ftpClient.isConnected())
								ftpClient.disconnect();
						}
					}
					
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
	@Path("/ajukan")
	@Produces("application/json")
	public Response ajukanCuti(@Context HttpHeaders headers, String data) {
		
		Metadata metadata = new Metadata();
		Result<KomponenGaji> result = new Result<KomponenGaji>();
		
		Connection con = null;
		PreparedStatement cs = null;
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
					if(json.path("useract").isMissingNode()){
						str.append("useract").append(", ");
						ok = false;
					}
					if(json.path("npp").isMissingNode()){
						str.append("npp").append(", ");
						ok = false;
					}
					if(json.path("nama").isMissingNode()){
						str.append("nama").append(", ");
						ok = false;
					}
					if(json.path("namatipe").isMissingNode()){
						str.append("namatipe").append(", ");
						ok = false;
					}
					if(json.path("kodepenugasan").isMissingNode()){
						str.append("kodepenugasan").append(", ");
						ok = false;
					}
					if(json.path("kodejobtitleatasan").isMissingNode()){
						str.append("kodejobtitleatasan").append(", ");
						ok = false;
					}
					if(json.path("kodeofficeatasan").isMissingNode()){
						str.append("kodeofficeatasan").append(", ");
						ok = false;
					}
					if(json.path("nppatasan").isMissingNode()){
						str.append("nppatasan").append(", ");
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
					Integer useract = json.path("useract").asInt();
					String npp = json.path("npp").asText();
					String nama = json.path("nama").asText();
					String namatipe = json.path("namatipe").asText();
					String kodejobtitleatasan = json.path("kodejobtitleatasan").asText();
					Integer kodepenugasan = json.path("kodepenugasan").asInt();
					String kodeofficeatasan = json.path("kodeofficeatasan").asText();
					String nppatasan = json.path("nppatasan").asText();
					
					try {
						query = "update cuti.cuti set statuspersetujuan=1, tglajukan=?, lastmodified_by=?, lastmodified_time=? where kode=?";
						con = new Koneksi().getConnection();
						cs = con.prepareStatement(query);
						cs.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
						cs.setInt(2, useract);
						cs.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
						cs.setInt(4, kode);
						cs.executeUpdate();
					} catch (Exception e) {
						throw new Exception(e.getMessage());
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
					
					try {
						String deskripsi = "Pengajuan cuti/izin a.n " + nama;
						
						query = "update cuti.cuti set statuspersetujuan=2 where kode=?; exec hcis.sp_insertnotifikasi ?,?,?,?,98,?,null,1,?,null,?,null,?,?";
						con = new Koneksi().getConnection();
						cs = con.prepareStatement(query);
						cs.setInt(1, kode);
						cs.setString(2, npp);
						cs.setString(3, nppatasan);
						cs.setString(4, deskripsi);
						cs.setString(5, namatipe);
						cs.setInt(6, kode);
						cs.setInt(7, useract);
						cs.setString(8, kodejobtitleatasan);
						cs.setInt(9, kodepenugasan);
						cs.setString(10, kodeofficeatasan);
						cs.executeUpdate();
					} catch (Exception e) {
						throw new Exception(e.getMessage());
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
					
					
					metadata.setCode(1);
					metadata.setMessage("Pengajuan cuti berhasil.");
				}
			} catch (SQLException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
			} catch (Exception e) {
				e.getMessage();
				metadata.setCode(0);
				metadata.setMessage("Transaksi belum dapat diproses, mohon ulangi lagi");
			}
		}
		
		result.setMetadata(metadata);
		return Response.ok(result).build();
	}
	
	/*private void writeToFile(InputStream uploadedInputStream,
		String uploadedFileLocation) {

		try {
			OutputStream out = new FileOutputStream(new File(uploadedFileLocation));
			int read = 0;
			byte[] bytes = new byte[1024];
			out = new FileOutputStream(new File(uploadedFileLocation));
			while ((read = uploadedInputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			uploadedInputStream.close();
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}*/
	
	/*@POST
	@Path("/upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces("application/json")
	public Response upload(
			@Context HttpHeaders headers, 
			@FormDataParam("file") final InputStream uploadedInputStream, 
			@FormDataParam("file") FormDataContentDisposition fileDetail) {
		
		Map<String, Object> metadata = new HashMap<String, Object>();
		Map<String, Object> metadataobj = new HashMap<String, Object>();

		try {
			String uploadedFileLocation = "c://tmp/" + fileDetail.getFileName();
			writeToFile(uploadedInputStream, uploadedFileLocation);
			metadata.put("code", 1);
			metadata.put("message", "Upload berhasil.");
		}
		catch (Exception e) {
			metadata.put("code", 0);
			metadata.put("message", "Upload gagal.");
			e.printStackTrace();
		}
		finally {
			
		}
		
		metadataobj.put("metadata", metadata);
		return Response.ok(metadataobj).build();
	}*/
	
	@POST
	@Path("/updatetunjangan")
	@Produces("application/json")
	public Response updateTunjangan(@Context HttpHeaders headers, String data) {
		
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
					if(json.path("gajipokok").isMissingNode()){
						str.append("gajipokok").append(", ");
						ok = false;
					}
					if(json.path("tunjjabatan").isMissingNode()){
						str.append("tunjjabatan").append(", ");
						ok = false;
					}
					if(json.path("tupres").isMissingNode()){
						str.append("tupres").append(", ");
						ok = false;
					}
					if(json.path("utilitas").isMissingNode()){
						str.append("utilitas").append(", ");
						ok = false;
					}
					if(json.path("nominaltunjanganbbm").isMissingNode()){
						str.append("nominaltunjanganbbm").append(", ");
						ok = false;
					}
					if(json.path("total").isMissingNode()){
						str.append("total").append(", ");
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
					Integer kode = json.path("kode").asInt();
					BigDecimal gajipokok = json.path("gajipokok").decimalValue();
					BigDecimal tunjjabatan = json.path("tunjjabatan").decimalValue();
					BigDecimal tupres = json.path("tupres").decimalValue();
					BigDecimal utilitas = json.path("utilitas").decimalValue();
					BigDecimal nominaltunjanganbbm = json.path("nominaltunjanganbbm").decimalValue();
					BigDecimal total = json.path("total").decimalValue();
					Integer useract = json.path("useract").asInt();
					
					query = "exec cuti.sp_updatetunjangan ?,?,?,?,?,?,?,?";
					con = new Koneksi().getConnection();
					cs = con.prepareCall(query);
					cs.setInt(1, kode);
					cs.setBigDecimal(2, gajipokok);
					cs.setBigDecimal(3, tunjjabatan);
					cs.setBigDecimal(4, tupres);
					cs.setBigDecimal(5, utilitas);
					cs.setBigDecimal(6, nominaltunjanganbbm);
					cs.setBigDecimal(7, total);
					cs.setInt(8, useract);
					cs.executeUpdate();
					
					metadata.setCode(1);
					metadata.setMessage("Update tunjangan berhasil.");
				}
			} catch (SQLException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
			} catch (Exception e) {
				metadata.setCode(0);
				metadata.setMessage("Transaksi belum dapat diproses, mohon ulangi lagi");
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
	@Path("/verifikasi")
	@Produces("application/json")
	public Response verifikasi(@Context HttpHeaders headers, String data) {
		
		Metadata metadata = new Metadata();
		Result<KomponenGaji> result = new Result<KomponenGaji>();
		
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement cs = null;
		String query = null;
		
		if (SharedMethod.VerifyToken(headers, metadata)) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				SaveCutis json = mapper.readValue(data, SaveCutis.class);
				
				query = "DECLARE @TempList1 cuti.verifAsTable;";
				for (SaveCuti item : json.getCuti()) {
					query += "insert into @TempList1 values (" + item.getVselected() + ", " + item.getKode() + ");";
				}
				query += "exec cuti.sp_verifikasi ?, ?, ?, ?, @TempList1;";
				
				con = new Koneksi().getConnection();
				cs = con.prepareStatement(query);
				cs.setString(1, json.getUnitverif());
				cs.setString(2,	json.getActverif());
				cs.setInt(3, json.getUseract());
				cs.setString(4, json.getKodeoffice());
				cs.executeUpdate();
				
				metadata.setCode(1);
				metadata.setMessage("Verifikasi berhasil.");
			} catch (SQLException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
			} catch (Exception e) {
				metadata.setCode(0);
				metadata.setMessage("Transaksi belum dapat diproses, mohon ulangi lagi");
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
	@Path("/getkuota")
	@Produces("application/json")
	public Response GetKuota(@Context HttpHeaders headers, String data) {
		
		Respon<Kuota> response = new Respon<Kuota>();
		Metadata metadata = new Metadata();
		Result<Kuota> result = new Result<Kuota>();
		
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
					if(json.path("kodetipe").isMissingNode()){
						str.append("kodetipe").append(", ");
						ok = false;
					}
					if(json.path("npp").isMissingNode()){
						str.append("npp").append(", ");
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
					Integer kodetipe = json.path("kodetipe").asInt();
					String npp = json.path("npp").asText();
					query = "exec cuti.sp_getkuota ?,?";
					con = new Koneksi().getConnection();
					
					cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
					cs.setInt(1, kodetipe);
					cs.setString(2, npp);
					Boolean isresult = cs.execute();
					
					metadata.setCode(0);
					metadata.setMessage("Data kosong.");
					if(isresult) {
						rs = cs.getResultSet();
						while (rs.next()) {
							Kuota kuota = new Kuota();
							kuota.setSisa(rs.getInt("sisa"));
							kuota.setKuota(rs.getInt("kuota"));
							kuota.setTerpakai(rs.getInt("terpakai"));
							kuota.setTahun7(rs.getString("tahun7"));
							kuota.setTahun8(rs.getString("tahun8"));
							kuota.setTmtmasuk(rs.getDate("tmtmasuk"));
							kuota.setTerakhiraju(rs.getString("pengajuanterakhir"));
							kuota.setJmlcutidiluartanggungan(rs.getInt("jmlcutidiluartanggungan"));
							response.setData(kuota);
							metadata.setCode(1);
							metadata.setMessage("Ok.");
							result.setResponse(response);
						}
						rs.close();
					}
					cs.close();
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
	
	@GET
	@Path("/getinfopribadi/{npp}")
	@Produces("application/json")
	public Response GetInfoPribadi(@Context HttpHeaders headers, @PathParam("npp") String npp) {
		
		Respon<Infopribadi> response = new Respon<Infopribadi>();
		Metadata metadata = new Metadata();
		Result<Infopribadi> result = new Result<Infopribadi>();
		
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String query = null;
		Infopribadi infopribadi = null;
		
		AuthUser authUser = new AuthUser();
		if (SharedMethod.VerifyToken(authUser, headers, metadata)) {
			try {
				query = "select hp, kodestatuskaryawan from karyawan.pegawai where npp=?";
				con = new Koneksi().getConnection();
				
				ps = con.prepareStatement(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
				ps.setString(1, authUser.getNpp());
				rs = ps.executeQuery();
				metadata.setCode(0);
				metadata.setMessage("Data tidak ditemukan.");
				if (rs.next()) {
					infopribadi = new Infopribadi();
					infopribadi.setNpp(npp);
					infopribadi.setHp(rs.getString("hp"));
					infopribadi.setKodestatuskaryawan(rs.getInt("kodestatuskaryawan"));
					metadata.setCode(1);
					metadata.setMessage("Ok.");
				}
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
			
			try {
				query = "select alamat from karyawan.detailalamat2 where npp=?";
				con = new Koneksi().getConnection();
				
				ps = con.prepareStatement(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
				ps.setString(1, npp);
				rs = ps.executeQuery();
				if (rs.next()) {
					infopribadi.setAlamat(rs.getString("alamat"));
				}
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
		
		response.setData(infopribadi);
		result.setResponse(response);
		result.setMetadata(metadata);
		return Response.ok(result).build();
	}
	
	@POST
	@Path("/spm/{act}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response SPM(
			@Context HttpHeaders headers,
			@PathParam("act") String act,
			String data) {
		
		Map<String, Object> metadata = new HashMap<String, Object>();
		Map<String, Object> metadataobj = new HashMap<String, Object>();
		
		if (SharedMethod.VerifyToken(headers, metadata)) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				Spm spm = mapper.readValue(data, Spm.class);
				
				Connection con = null;
				CallableStatement cs = null;
				PreparedStatement ps = null;
				String query = null;
				
				try {
					query = "DECLARE @TempList cuti.spmdetilAsTable;";
					for (SpmDetil spmDetil : spm.getSpmdetil()) {
						if(spmDetil.getDeleted()==0) {
							query += "insert into @TempList values ("
									+ spmDetil.getKode() + ", " 
									+ spmDetil.getKodecuti() + ", " 
									+ spmDetil.getBulanslip() + ", " 
									+ spmDetil.getGajipokok() + ", " 
									+ spmDetil.getTunjjabatan() + ", "
									+ spmDetil.getTupres() + ", "
									+ spmDetil.getUtilitas() + ", "
									+ spmDetil.getTotal() + ", '"
									+ spmDetil.getNorek() + "');";
						}
					}
					
					if(act.equalsIgnoreCase("create")) {
						query += "exec cuti.sp_insertSpm ?,?,?,?,?,?,?,?,?,?,?,@TempList;";
						con = new Koneksi().getConnection();
						cs = con.prepareCall(query);
						cs.setString(1, spm.getKodeoffice());
						cs.setString(2, spm.getNoarsip());
						cs.setBigDecimal(3, spm.getTotal());
						cs.setInt(4, spm.getAnggaran().getTahun());
						cs.setString(5, spm.getAnggaran().getProgram().getKode());
						cs.setString(6, spm.getAnggaran().getAkun().getKode());
						cs.setBigDecimal(7, spm.getAnggaran().getAlokasi());
						cs.setBigDecimal(8, spm.getAnggaran().getRealisasi());
						cs.setBigDecimal(9, spm.getAnggaran().getSaldo());
						cs.setInt(10, spm.getCreated_by());
						cs.setInt(11, spm.getKodetipe());
						cs.executeUpdate();
						metadata.put("code", 1);
						metadata.put("message", "Simpan berhasil.");
					}
					else if(act.equalsIgnoreCase("update")) {
						query += "exec cuti.sp_updateSpm ?,?,?,?,?,?,?,?,?,?,?,@TempList;";
						con = new Koneksi().getConnection();
						cs = con.prepareCall(query);
						cs.setString(1, spm.getKodeoffice());
						cs.setString(2, spm.getNoarsip());
						cs.setBigDecimal(3, spm.getTotal());
						cs.setInt(4, spm.getAnggaran().getTahun());
						cs.setString(5, spm.getAnggaran().getProgram().getKode());
						cs.setString(6, spm.getAnggaran().getAkun().getKode());
						cs.setBigDecimal(7, spm.getAnggaran().getAlokasi());
						cs.setBigDecimal(8, spm.getAnggaran().getRealisasi());
						cs.setBigDecimal(9, spm.getAnggaran().getSaldo());
						cs.setInt(10, spm.getCreated_by());
						cs.setInt(11, spm.getKode());
						cs.executeUpdate();
						metadata.put("code", 1);
						metadata.put("message", "Update berhasil.");
					}
					else if(act.equalsIgnoreCase("delete")) {
						query = "delete from cuti.spm where kode=?";
						con = new Koneksi().getConnection();
						ps = con.prepareStatement(query);
						ps.setInt(1, spm.getKode());
						ps.executeUpdate();
						metadata.put("code", 1);
						metadata.put("message", "Hapus berhasil.");
					}
					else {
						return Response.status(404).build();
					}
				}
				finally {
					if (ps != null) {
						try {
							ps.close();
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
			catch (SQLException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
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
	@Path("/grid/spm/{page}/{row}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response ListGridSPM(@Context HttpHeaders headers,
			@PathParam("page") String page, @PathParam("row") String row, String data) {
		
		Respon<Spm> response = new Respon<Spm>();
		Metadata metadata = new Metadata();
		Result<Spm> result = new Result<Spm>();
		
		Connection con = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		CallableStatement cs = null;
		PreparedStatement ps = null;
		String order = null;
		String filter = null;
		String filter2 = null;
		String filter3 = null;
		String query = null;
		Boolean ok = true;

		if (SharedMethod.VerifyToken(headers, metadata)) {
		//if (true) {
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
				
				if(ok) {
					query = "exec cuti.sp_listspm ?, ?, ?, ?, ?, ?, ?";
					con = new Koneksi().getConnection();
					cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
					cs.setInt(1, Integer.parseInt(page));
					cs.setInt(2, Integer.parseInt(row));
					cs.setInt(3, 1);
					cs.setString(4, order);
					cs.setString(5, filter);
					cs.setString(6, filter2);
					cs.setString(7, filter3);
					rs = cs.executeQuery();
					metadata.setCode(1);
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
					cs.setString(6, filter2);
					cs.setString(7, filter3);
					rs = cs.executeQuery();
					
					metadata.setCode(0);
					metadata.setMessage("Data tidak ditemukan");
					
					List<Spm> spms = new ArrayList<>();
					while (rs.next()) {
						Spm spm = new Spm();
						spm.setKode(rs.getInt("kode"));
						spm.setKodeoffice(rs.getString("kodeoffice"));
						spm.setNoarsip(rs.getString("noarsip"));
						spm.setTgl(rs.getString("tgl"));
						spm.setTahun(rs.getInt("tahun"));
						spm.setBulan(rs.getInt("bulan"));
						spm.setTotal(rs.getBigDecimal("total"));
						spm.setKepada(rs.getString("kepada"));
						Anggaran anggaran = new Anggaran();
						anggaran.setTahun(rs.getInt("tahunanggaran"));
						anggaran.setAlokasi(rs.getBigDecimal("alokasianggaran"));
						anggaran.setRealisasi(rs.getBigDecimal("realisasianggaran"));
						anggaran.setSaldo(rs.getBigDecimal("saldoanggaran"));
						Program program = new Program();
						program.setKode(rs.getString("kodeprogram"));
						program.setNama(rs.getString("namaprogram"));
						anggaran.setProgram(program);
						Akun akun = new Akun();
						akun.setKode(rs.getString("kodeakun"));
						akun.setNama(rs.getString("namaakun"));
						anggaran.setAkun(akun);
						spm.setAnggaran(anggaran);
						
						
						ps = con.prepareStatement("select a.*,d.nama,d.namalengkap,e.nama as namaoffice,g.nama as namajobtitle,h.nama as namaunitkerja,b.nomor,i.nama as namasubgrade "
								+ "from cuti.spmdetil a "
								+ "inner join cuti.cuti b on a.kodecuti=b.kode "
								+ "inner join karyawan.penugasan c on b.kodepenugasan=c.kode "
								+ "inner join karyawan.vw_pegawai d on c.npp=d.npp "
								+ "inner join organisasi.office e on c.kodeoffice=e.kode "
								+ "inner join organisasi.hirarkijabatan f on c.kodehirarkijabatan=f.kode "
								+ "inner join organisasi.jobtitle g on f.kodejobtitle=g.kode "
								+ "inner join organisasi.unitkerja h on f.kodeunitkerja=h.kode "
								+ "inner join organisasi.subgrade i on c.kodesubgrade=i.kode "
								+ "where a.kodespm=? order by a.kode");
						ps.setInt(1, spm.getKode());
						rs2 = ps.executeQuery();
						ArrayList<SpmDetil> spmDetils = new ArrayList<>();
						Integer i = 0;
						while (rs2.next()) {
							SpmDetil spmDetil = new SpmDetil();
							spmDetil.setNo(i+1);
							spmDetil.setKode(rs2.getInt("kode"));
							spmDetil.setNomor(rs2.getString("nomor"));
							spmDetil.setKodespm(rs2.getInt("kodespm"));
							spmDetil.setKodecuti(rs2.getInt("kodecuti"));
							spmDetil.setBulanslip(rs2.getInt("bulanslip"));
							spmDetil.setGajipokok(rs2.getBigDecimal("gajipokok"));
							spmDetil.setTunjjabatan(rs2.getBigDecimal("tunjjabatan"));
							spmDetil.setTupres(rs2.getBigDecimal("tupres"));
							spmDetil.setUtilitas(rs2.getBigDecimal("utilitas"));
							spmDetil.setTotal(rs2.getBigDecimal("total"));
							spmDetil.setNorek(rs2.getString("norek"));
							spmDetil.setNama(rs2.getString("nama"));
							spmDetil.setNamaoffice(rs2.getString("namaoffice"));
							spmDetil.setNamajobtitle(rs2.getString("namajobtitle"));
							spmDetil.setNamaunitkerja(rs2.getString("namaunitkerja"));
							spmDetil.setNamasubgrade(rs2.getString("namasubgrade"));
							spmDetil.setDeleted(0);
							spmDetils.add(spmDetil);
							i++;
						}
						spm.setSpmdetil(spmDetils);
						spms.add(spm);
						
						metadata.setCode(1);
						metadata.setMessage("OK");
					}
					response.setList(spms);
					result.setResponse(response);
				}
			} catch (SQLException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
				e.printStackTrace();
			} catch (Exception e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
				e.printStackTrace();
			} finally {
				if (rs2 != null) {
					try {
						rs2.close();
					} catch (SQLException e) {
					}
				}
				
				if (rs != null) {
					try {
						rs.close();
					} catch (SQLException e) {
					}
				}

				if (ps != null) {
					try {
						ps.close();
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
	@Path("/getanggaran")
	@Produces("application/json")
	public Response GetAnggaran(@Context HttpHeaders headers, String data) {
		
		Respon<Anggaran> response = new Respon<Anggaran>();
		Metadata metadata = new Metadata();
		Result<Anggaran> result = new Result<Anggaran>();
		
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
					if(json.path("tahun").isMissingNode()){
						str.append("tahun").append(", ");
						ok = false;
					}
					if(json.path("kodeoffice").isMissingNode()){
						str.append("kodeoffice").append(", ");
						ok = false;
					}
					if(json.path("kodeprogram").isMissingNode()){
						str.append("kodeprogram").append(", ");
						ok = false;
					}
					if(json.path("kodeakun").isMissingNode()){
						str.append("kodeakun").append(", ");
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
					Integer tahun = json.path("tahun").asInt();
					String kodeoffice = json.path("kodeoffice").asText();
					String kodeprogram = json.path("kodeprogram").asText();
					String kodeakun = json.path("kodeakun").asText();
					
					query = "exec referensi.sp_getanggaran ?,?,?,?";
					con = new Koneksi().getConnection();
					
					cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
					cs.setInt(1, tahun);
					cs.setString(2, kodeoffice);
					cs.setString(3, kodeprogram);
					cs.setString(4, kodeakun);
					rs = cs.executeQuery();
					
					metadata.setCode(0);
					metadata.setMessage("Data kosong.");
					while (rs.next()) {
						Anggaran anggaran = new Anggaran();
						anggaran.setTahun(rs.getInt("tahun"));
						anggaran.setAlokasi(rs.getBigDecimal("alokasi"));
						anggaran.setRealisasi(rs.getBigDecimal("realisasi"));
						anggaran.setSaldo(rs.getBigDecimal("saldo"));
						Office office = new Office();
						office.setKode(rs.getString("kodeoffice"));
						office.setNama(rs.getString("namaoffice"));
						anggaran.setOffice(office);
						Program program = new Program();
						program.setKode(rs.getString("kodeprogram"));
						program.setNama(rs.getString("namaprogram"));
						anggaran.setProgram(program);
						Akun akun = new Akun();
						akun.setKode(rs.getString("kodeakun"));
						akun.setNama(rs.getString("namaakun"));
						anggaran.setAkun(akun);
						
						response.setData(anggaran);
						metadata.setCode(1);
						metadata.setMessage("Ok.");
						result.setResponse(response);
					}
				}
			} catch (SQLException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
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
	@Path("/tolaksdm")
	@Produces("application/json")
	public Response tolakSDM(@Context HttpHeaders headers, String data) {
		
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
					if(json.path("kodeoffice").isMissingNode()){
						str.append("kodeoffice").append(", ");
						ok = false;
					}
					if(json.path("kodecuti").isMissingNode()){
						str.append("kodecuti").append(", ");
						ok = false;
					}
					if(json.path("catatantolak").isMissingNode()){
						str.append("catatantolak").append(", ");
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
					String kodeoffice = json.path("kodeoffice").asText();
					Integer kodecuti = json.path("kodecuti").asInt();
					String catatantolak = json.path("catatantolak").asText();
					Integer useract = json.path("useract").asInt();
					
					query = "exec cuti.sp_tolaksdm ?,?,?,?";
					con = new Koneksi().getConnection();
					cs = con.prepareCall(query);
					cs.setString(1, kodeoffice);
					cs.setInt(2, kodecuti);
					cs.setString(3, catatantolak);
					cs.setInt(4, useract);
					cs.executeUpdate();
					
					metadata.setCode(1);
					metadata.setMessage("Tolak berhasil.");
				}
			} catch (SQLException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
			} catch (Exception e) {
				metadata.setCode(0);
				metadata.setMessage("Transaksi belum dapat diproses, mohon ulangi lagi");
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
	@Path("/getcatatannotifikasi")
	@Produces("application/json")
	public Response GetCatatanNotifikasi(@Context HttpHeaders headers, String data) {
		
		Respon<Notifikasi> response = new Respon<Notifikasi>();
		Metadata metadata = new Metadata();
		Result<Notifikasi> result = new Result<Notifikasi>();
		
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
					if(json.path("kodecuti").isMissingNode()){
						str.append("kodecuti").append(", ");
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
					Integer kodecuti = json.path("kodecuti").asInt();
					query = "exec cuti.sp_getcatatannotifikasi ?";
					con = new Koneksi().getConnection();
					
					cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
					cs.setInt(1, kodecuti);
					rs = cs.executeQuery();
					ArrayList<Notifikasi> notifikasis = new ArrayList<>();
					
					metadata.setCode(0);
					metadata.setMessage("Data kosong.");
					while (rs.next()) {
						Notifikasi notifikasi = new Notifikasi();
						notifikasi.setKode(rs.getInt("kode"));
						notifikasi.setCatatan(rs.getString("catatan"));
						notifikasi.setLastmodified_time(rs.getTimestamp("lastmodified_time"));
						
						notifikasis.add(notifikasi);
						
						metadata.setCode(1);
						metadata.setMessage("Ok.");
					}
					response.setList(notifikasis);
					result.setResponse(response);
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
	@Path("/laporan/cuti/{tgl1}/{tgl2}/{jeniskantor}/{kodelokasi}/{status}/{npp: .*}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response LaporanCuti(@Context HttpHeaders headers,
			@PathParam("tgl1") String tgl1, 
			@PathParam("tgl2") String tgl2, 
			@PathParam("jeniskantor") Integer jeniskantor,
			@PathParam("kodelokasi") String kodelokasi,
			@PathParam("status") Integer status,
			@PathParam("npp") String npp,
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
				java.sql.Date tanggal1 = new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(tgl1).getTime());
				java.sql.Date tanggal2 = new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(tgl2).getTime());
				
				query = "exec cuti.sp_getlaporancuti ?, ?, ?, ?, ?, ?";
				con = new Koneksi().getConnection();
				cs = con.prepareCall(query);
				cs.setDate(1, tanggal1);
				cs.setDate(2, tanggal2);
				cs.setInt(3, jeniskantor);
				cs.setString(4, kodelokasi);
				cs.setInt(5, status);
				cs.setString(6, npp);
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