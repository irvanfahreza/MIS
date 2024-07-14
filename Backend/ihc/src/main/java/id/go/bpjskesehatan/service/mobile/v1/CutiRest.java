package id.go.bpjskesehatan.service.mobile.v1;

import java.io.ByteArrayInputStream;
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
import java.util.Base64;
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
import javax.ws.rs.core.Response;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import id.go.bpjskesehatan.database.Koneksi;
import id.go.bpjskesehatan.entitas.Metadata;
import id.go.bpjskesehatan.entitas.Respon;
import id.go.bpjskesehatan.entitas.Result;
import id.go.bpjskesehatan.entitas.cuti.SaveCuti;
import id.go.bpjskesehatan.entitas.cuti.SaveCutiTgls;
import id.go.bpjskesehatan.service.v2.cuti.entitas.Kuota;
import id.go.bpjskesehatan.util.MyException;
import id.go.bpjskesehatan.util.Utils;

@Path("/mobile/v1/cuti")
public class CutiRest {
	
    @Context
    private ServletContext context;
    
	@GET
	@Path("/list/iscount/{iscount}/start/{start}/limit/{limit}/npp/{npp}")
	@Produces("application/json")
	@Consumes("application/json")
	public Response listCutiPegawai(
			@Context HttpHeaders headers, 
			@PathParam("iscount") Integer iscount,
			@PathParam("start") Integer start,
			@PathParam("limit") Integer limit,
			@PathParam("npp") String npp) {

		Respon<SaveCuti> response = new Respon<SaveCuti>();
		Metadata metadata = new Metadata();
		Result<SaveCuti> result = new Result<SaveCuti>();
		
		//if (AuthMobile.VerifyToken(headers, metadata)) {
		if (true) {
			Connection con = null;
			CallableStatement cs = null;
			PreparedStatement ps = null;
			ResultSet rs = null;
			ResultSet rs2 = null;
			String query = "exec cuti.sp_listcuti ?, ?, ?, ?, ?, ?, ?";
			
			try {
				con = new Koneksi().getConnection();
				cs = con.prepareCall(query);
				
				if(iscount==1) {
					cs.setInt(1, start);
					cs.setInt(2, limit);
					cs.setInt(3, 1);
					cs.setNull(4, java.sql.Types.VARCHAR);
					cs.setString(5, String.format("npp = \'%s\'", npp));
					cs.setNull(6, java.sql.Types.VARCHAR);
					cs.setNull(7, java.sql.Types.VARCHAR);
				}
				else {
					cs.setInt(1, start);
					cs.setInt(2, limit);
					cs.setInt(3, 0);
					cs.setNull(4, java.sql.Types.VARCHAR);
					cs.setString(5, String.format("npp = \'%s\'", npp));
					cs.setNull(6, java.sql.Types.VARCHAR);
					cs.setNull(7, java.sql.Types.VARCHAR);
				}
				
				rs = cs.executeQuery();
				metadata.setCode(0);
				metadata.setMessage("Data tidak ditemukan");
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
			} catch (Exception e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
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
		
		result.setMetadata(metadata);
		return Response.ok(result).build();
	}
	
	@GET
	@Path("/list/persetujuan/iscount/{iscount}/start/{start}/limit/{limit}/npp/{npp}/kodejobtitle/{kodejobtitle}/kodeoffice/{kodeoffice}")
	@Produces("application/json")
	@Consumes("application/json")
	public Response listPersetujuanCutiPegawai(
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
			String query = "exec hcis.sp_listnotifikasiperpegawai ?, ?, ?, ?, ?, ?, ?, ?, ?";
			
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
					cs.setInt(8, 98);
					cs.setInt(9, 3);
				}
				else {
					cs.setString(1, npp);
					cs.setString(2, kodejobtitle);
					cs.setString(3, kodeoffice);
					cs.setInt(4, start);
					cs.setInt(5, limit);
					cs.setInt(6, 0);
					cs.setInt(7, 0);
					cs.setInt(8, 98);
					cs.setInt(9, 3);
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
	
	@GET
	@Path("/laporan/cuti/{tgl1}/{tgl2}/{jeniskantor}/{kodelokasi}/{status}/{npp: .*}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response LaporanCuti(@Context HttpHeaders headers,
			@PathParam("tgl1") String tgl1, 
			@PathParam("tgl2") String tgl2, 
			@PathParam("jeniskantor") Integer jeniskantor,
			@PathParam("kodelokasi") String kodelokasi,
			@PathParam("status") Integer status,
			@PathParam("npp") String npp) {
		
		Map<String, Object> response = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> metadata = new HashMap<String, Object>();
		List<Map<String, Object>> list = null;
		
		Connection con = null;
		ResultSet rs = null;
		CallableStatement cs = null;
		String query = null;

		//if (AuthMobile.VerifyToken(headers, metadata)) {
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
	
	@GET
	@Path("/get/bykode/{kodecuti}")
	@Produces("application/json")
	@Consumes("application/json")
	public Response cutiPegawaiByKode(
			@Context HttpHeaders headers, 
			@PathParam("kodecuti") Integer kodecuti
			) {

		Respon<SaveCuti> response = new Respon<SaveCuti>();
		Metadata metadata = new Metadata();
		Result<SaveCuti> result = new Result<SaveCuti>();
		
		//if (AuthMobile.VerifyToken(headers, metadata)) {
		if (true) {
			Connection con = null;
			CallableStatement cs = null;
			PreparedStatement ps = null;
			ResultSet rs = null;
			ResultSet rs2 = null;
			String query = "exec cuti.sp_listcuti ?, ?, ?, ?, ?, ?, ?";
			
			try {
				con = new Koneksi().getConnection();
				cs = con.prepareCall(query);
				
				cs.setInt(1, 1);
				cs.setInt(2, 1);
				cs.setInt(3, 0);
				cs.setNull(4, java.sql.Types.VARCHAR);
				cs.setString(5, String.format("kode = \'%d\'", kodecuti));
				cs.setNull(6, java.sql.Types.VARCHAR);
				cs.setNull(7, java.sql.Types.VARCHAR);
				
				rs = cs.executeQuery();
				metadata.setCode(0);
				metadata.setMessage("Data tidak ditemukan");
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
			} catch (Exception e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
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
		
		result.setMetadata(metadata);
		return Response.ok(result).build();
	}
	
	@POST
	@Path("/save")
	@Produces("application/json")
	@Consumes("application/json")
	public Response saveAjukan(
			@Context HttpHeaders headers, 
			String post) {

		Map<String, Object> metadata = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		
		//if (AuthMobile.VerifyToken(headers, metadata)) {
		if (true) {
			
			Connection con = null;
			CallableStatement cs = null;
			String query = null;
			
			try {
				ObjectMapper mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, true);
				SaveCuti saveCuti = mapper.readValue(post, SaveCuti.class);
				
				if(saveCuti.getKodetipe()==3) {
					throw new MyException("Fitur pengajuan cuti besar dalam tahap pengembangan, pengajuan dapat dilakukan pada versi web terima kasih");
				}
				
				Boolean adafile = false;
				String pathFile = null;
				String host = null;
	        	Integer port = null;
	        	String user = null;
	        	String pass = null;
				String namaFile = "";
				
				if(saveCuti.getLampiran() != null && !saveCuti.getLampiran().isEmpty()) {
					adafile = true;
					pathFile = "file_cuti/";
					host = context.getInitParameter("ftp-host");
		        	port = Integer.parseInt(context.getInitParameter("ftp-port"));
		        	user = context.getInitParameter("ftp-user");
		        	pass = context.getInitParameter("ftp-pass");
					
					namaFile = saveCuti.getLampiran();
		        	StringTokenizer st = new StringTokenizer(namaFile, ".");
		        	String extension = ""; 
		        	while(st.hasMoreTokens()) {
		        		extension = "."+st.nextToken();
		        	}
		        	String npp = saveCuti.getNpp();
		        	Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		        	namaFile = npp + "-" + timestamp.getTime() + extension;
				}
				
				if(saveCuti.getKodetipe()==1) {
					query = "DECLARE @TempList cuti.cutitglAsTable;";
					for (SaveCutiTgls item : saveCuti.getTgls()) {
						if(item.getDeleted()==0)
							query += "insert into @TempList values (" + item.getKode() + ", '" + item.getTgl() + "', " + item.getDeleted() + ", " + item.getVbatalkan() + ", " + item.getStatus() + ");";
					}
					query += "exec cuti.sp_insertcutiWithTgls ?,?,?,?,?,?,?,?,?,@TempList,?,?,?,?,?,?;";
				}
				else {
					query = "exec cuti.sp_insertcuti ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?;";
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
					//cs.setInt(15, saveCuti.getAmbilhari());
					cs.setInt(15, 1);
					
					cs.setString(16, saveCuti.getKodejobtitleatasan());
					cs.setString(17, saveCuti.getKodeofficeatasan());
					cs.setString(18, saveCuti.getNppatasan());
					cs.setInt(19, saveCuti.getKodepenugasanatasan());
				}
				else {
					cs.registerOutParameter(10, java.sql.Types.VARCHAR);
					cs.setString(11, saveCuti.getKodejobtitleatasan());
					cs.setString(12, saveCuti.getKodeofficeatasan());
					cs.setString(13, saveCuti.getNppatasan());
					cs.setInt(14, saveCuti.getKodepenugasanatasan());
					cs.setInt(15, 1);
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
					FTPClient ftpClient = null;
					try {
						ftpClient = new FTPClient();
						ftpClient.connect(host,port);
			        	ftpClient.login(user, pass);
			        	ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			        	
			        	byte[] decodedBytes = Base64.getDecoder().decode(saveCuti.getStatusterakhir());
			        	ByteArrayInputStream bis = new ByteArrayInputStream(decodedBytes);
			        	
			        	Boolean upload = ftpClient.storeFile(pathFile + namaFile, bis);
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
				
			} catch (SQLException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
			}
			catch (MyException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
			}
			catch (Exception e) {
				metadata.put("code", 0);
				metadata.put("message", "Simpan gagal.");
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
		
		result.put("metadata", metadata);
		return Response.ok(result).build();
	}
	
	@POST
	@Path("/approval")
	@Produces("application/json")
	@Consumes("application/json")
	public Response approval(
			@Context HttpHeaders headers, 
			String data) {

		Map<String, Object> metadata = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		
		//if (AuthMobile.VerifyToken(headers, metadata)) {
		if (true) {
			Connection con = null;
			CallableStatement cs = null;
			String query = null;
			try {
				ObjectMapper mapper = new ObjectMapper();
				JsonNode json = mapper.readTree(data);
				
				Integer kode = json.path("kode").asInt();
				String act = json.path("act").asText();
				Integer useract = json.path("useract").asInt();
				String catatan = json.path("catatan").asText();
				String tokodejobtitle = json.path("tokodejobtitle").asText();
				String tokodeoffice = json.path("tokodeoffice").asText();
				String tonpp = json.path("tonpp").asText();
				Integer tokodepenugasan = json.path("tokodepenugasan").asInt();
				
				query = "exec hcis.sp_updatenotifikasi ?,?,?,?,?,?,?,?";
				con = new Koneksi().getConnection();
				cs = con.prepareCall(query);
				cs.setInt(1, kode);
				cs.setString(2, act);
				cs.setInt(3, useract);
				cs.setString(4, catatan);
				cs.setString(5, tokodejobtitle);
				cs.setString(6, tokodeoffice);
				cs.setString(7, tonpp);
				cs.setInt(8, tokodepenugasan);
				cs.executeUpdate();
				metadata.put("code", 1);
				metadata.put("message", "Ok");
			} catch (Exception e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				e.printStackTrace();
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
		
		result.put("metadata", metadata);
		return Response.ok(result).build();
	}
	
	@POST
	@Path("/approval/batalcuti")
	@Produces("application/json")
	@Consumes("application/json")
	public Response approvalBatalCuti(
			@Context HttpHeaders headers, 
			String data) {

		Map<String, Object> metadata = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		
		//if (AuthMobile.VerifyToken(headers, metadata)) {
		if (true) {
			Connection con = null;
			CallableStatement cs = null;
			String query = null;
			try {
				ObjectMapper mapper = new ObjectMapper();
				JsonNode json = mapper.readTree(data);
				
				Integer kode = json.path("kode").asInt();
				String act = json.path("act").asText();
				Integer useract = json.path("useract").asInt();
				String catatan = json.path("catatan").asText();
				
				query = "exec hcis.sp_batalcuti ?,?,?,?";
				con = new Koneksi().getConnection();
				cs = con.prepareCall(query);
				cs.setInt(1, kode);
				cs.setString(2, act);
				cs.setInt(3, useract);
				cs.setString(4, catatan);
				cs.executeUpdate();
				metadata.put("code", 1);
				metadata.put("message", "Ok");
			} catch (Exception e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				e.printStackTrace();
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
		
		result.put("metadata", metadata);
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
		
		//if (AuthMobile.VerifyToken(headers, metadata)) {
		if (true) {
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
}
