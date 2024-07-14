package id.go.bpjskesehatan.service.mobile.v1;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import id.go.bpjskesehatan.database.Koneksi;
import id.go.bpjskesehatan.entitas.Metadata;
import id.go.bpjskesehatan.entitas.Respon;
import id.go.bpjskesehatan.entitas.Result;
import id.go.bpjskesehatan.entitas.karyawan.Pegawai;
import id.go.bpjskesehatan.entitas.karyawan.Penugasan;
import id.go.bpjskesehatan.entitas.organisasi.Office;
import id.go.bpjskesehatan.service.v2.entitas.Akun;
import id.go.bpjskesehatan.service.v2.entitas.Program;
import id.go.bpjskesehatan.service.v2.lembur.entitas.Lembur;
import id.go.bpjskesehatan.service.v2.lembur.entitas.ListPegawai;
import id.go.bpjskesehatan.service.v2.lembur.entitas.SaveLemburTgls;
import id.go.bpjskesehatan.util.Utils;

@Path("/mobile/v1/lembur")
public class LemburRest {
	
    @Context
    private ServletContext context;
    
    @GET
	@Path("/list/iscount/{iscount}/start/{start}/limit/{limit}/npp/{npp}")
	@Produces("application/json")
	@Consumes("application/json")
	public Response listLemburPegawai(
			@Context HttpHeaders headers, 
			@PathParam("iscount") Integer iscount,
			@PathParam("start") Integer start,
			@PathParam("limit") Integer limit,
			@PathParam("npp") String npp) {

		Respon<Lembur> response = new Respon<Lembur>();
		Metadata metadata = new Metadata();
		Result<Lembur> result = new Result<Lembur>();
		
		//if (AuthMobile.VerifyToken(headers, metadata)) {
		if (true) {
			Connection con = null;
			ResultSet rs = null;
			ResultSet rs2 = null;
			ResultSet rs3 = null;
			CallableStatement cs = null;
			PreparedStatement ps = null;
			PreparedStatement pstgls = null;
			String query = "exec lembur.sp_listlembur ?, ?, ?, ?, ?, ?, ?, ?";
			
			try {
				con = new Koneksi().getConnection();
				cs = con.prepareCall(query);
				
				if(iscount==1) {
					cs.setString(1, npp);
					cs.setInt(2, start);
					cs.setInt(3, limit);
					cs.setInt(4, 1);
					cs.setString(5, "kode desc");
					cs.setNull(6, java.sql.Types.VARCHAR);
					cs.setNull(7, java.sql.Types.VARCHAR);
					cs.setNull(8, java.sql.Types.VARCHAR);
				}
				else {
					cs.setString(1, npp);
					cs.setInt(2, start);
					cs.setInt(3, limit);
					cs.setInt(4, 0);
					cs.setString(5, "kode desc");
					cs.setNull(6, java.sql.Types.VARCHAR);
					cs.setNull(7, java.sql.Types.VARCHAR);
					cs.setNull(8, java.sql.Types.VARCHAR);
				}
				
				rs = cs.executeQuery();
				metadata.setCode(0);
				metadata.setMessage("Data tidak ditemukan");
				List<Lembur> lemburs = new ArrayList<>();
				while (rs.next()) {
					
					Lembur lembur = new Lembur();
					lembur.setKode(rs.getInt("kode"));
					lembur.setNomor(rs.getString("nomor"));
					lembur.setLaporan_isi(rs.getString("laporan_isi"));
					lembur.setLaporan_lampiran(rs.getString("laporan_lampiran"));
					lembur.setIsverif(rs.getInt("isverif"));
					
					Office office = new Office();
					office.setKode(rs.getString("kodeoffice"));
					office.setNama(rs.getString("namaoffice"));
					lembur.setOffice(office);
					
					Akun akun = new Akun();
					akun.setKode(rs.getString("kodeakun"));
					akun.setNama(rs.getString("namaakun"));
					lembur.setAkun(akun);
					
					Program program = new Program();
					program.setKode(rs.getString("kodeprogram"));
					program.setNama(rs.getString("namaprogram"));
					lembur.setProgram(program);
					
					lembur.setLampiran(rs.getString("lampiran")==null?"":rs.getString("lampiran"));
					lembur.setStatus(rs.getInt("status"));
					lembur.setNamastatus(rs.getString("namastatus"));
					
					lembur.setTgllembur(rs.getString("tgllembur"));
					lembur.setPegawailembur(rs.getString("pegawailembur"));
					
					lembur.setDeskripsi(rs.getString("deskripsi"));
					lembur.setNamakegiatan(rs.getString("namakegiatan"));
					lembur.setCatatantolak(rs.getString("catatantolak"));
					
					Penugasan penugasan = new Penugasan();
					penugasan.setKode(rs.getInt("pembuat"));
					Pegawai pegawai = new Pegawai();
					pegawai.setNpp(rs.getString("npppembuat"));
					pegawai.setNama(rs.getString("namapembuat"));
					penugasan.setPegawai(pegawai);
					lembur.setPembuat(penugasan);
					
					try {
						Integer tahun = Calendar.getInstance().get(Calendar.YEAR);
						ps = con.prepareStatement("select \r\n" + 
								"a.*, c.npp, c.nama, d.totaljam \r\n" + 
								"from lembur.pegawai a \r\n" + 
								"inner join karyawan.penugasan b on a.kodepenugasan=b.kode \r\n" + 
								"inner join karyawan.vw_pegawai c on b.npp=c.npp \r\n" + 
								"left join (select * from lembur.vw_totaljam where tahun=?) d on b.npp=d.npp \r\n" + 
								"where a.kodelembur = ? order by a.kode");
						ps.setInt(1, tahun);
						ps.setInt(2, lembur.getKode());
						rs2 = ps.executeQuery();
						ArrayList<ListPegawai> lemburpegawais = new ArrayList<>();
						while (rs2.next()) {
							ListPegawai lemburpegawai = new ListPegawai();
							lemburpegawai.setKode(rs2.getInt("kode"));
							lemburpegawai.setKodepenugasan(rs2.getInt("kodepenugasan"));
							lemburpegawai.setNpp(rs2.getString("npp"));
							lemburpegawai.setNama(rs2.getString("nama"));
							lemburpegawai.setDeleted(0);
							lemburpegawai.setTotaljam(rs2.getInt("totaljam"));
							lemburpegawais.add(lemburpegawai);
						}
						lembur.setPegawai(lemburpegawais);
					}
					finally {
						if (rs2 != null) {
							try {
								rs2.close();
							} catch (SQLException e) {
							}
						}
						if (ps != null) {
							try {
								ps.close();
							} catch (SQLException e) {
							}
						}
					}
					
					try {
						pstgls = con.prepareStatement("select *, iif(tgl < cast(getdate() as date),0,iif(tgl = cast(getdate() as date) and cast(getdate() as time) > '08:00:00',0,1)) as viewcheck from lembur.lemburtgl where kodelembur=?");
						pstgls.setInt(1, lembur.getKode());
						rs3 = pstgls.executeQuery();
						ArrayList<SaveLemburTgls> saveLemburTgls = new ArrayList<>();
						while (rs3.next()) {
							SaveLemburTgls tgls = new SaveLemburTgls();
							tgls.setKode(rs3.getInt("kode"));
							tgls.setTgl(rs3.getDate("tgl"));
							tgls.setDeleted(0);
							tgls.setBatalkan(false);
							tgls.setStatus(rs3.getInt("status"));
							tgls.setViewcheck(rs3.getBoolean("viewcheck"));
							saveLemburTgls.add(tgls);
						}
						lembur.setTgls(saveLemburTgls);
					}
					finally {
						if (rs3 != null) {
							try {
								rs3.close();
							} catch (SQLException e) {
							}
						}	
						if (pstgls != null) {
							try {
								pstgls.close();
							} catch (SQLException e) {
							}
						}	
					}
					
					/*ps = con.prepareStatement("select \r\n" + 
							"a.*, c.npp, c.nama\r\n" + 
							"from lembur.pegawai a\r\n" + 
							"inner join karyawan.penugasan b on a.kodepenugasan=b.kode\r\n" + 
							"inner join karyawan.vw_pegawai c on b.npp=c.npp\r\n" + 
							"where a.kodelembur = ?");
					ps.setInt(1, lembur.getKode());
					rs2 = ps.executeQuery();
					ArrayList<ListPegawai> lemburpegawais = new ArrayList<>();
					while (rs2.next()) {
						ListPegawai lemburpegawai = new ListPegawai();
						lemburpegawai.setKode(rs2.getInt("kode"));
						lemburpegawai.setKodepenugasan(rs2.getInt("kodepenugasan"));
						lemburpegawai.setNpp(rs2.getString("npp"));
						lemburpegawai.setNama(rs2.getString("nama"));
						lemburpegawai.setDeleted(0);
						lemburpegawais.add(lemburpegawai);
					}*/
					lemburs.add(lembur);
					
					metadata.setCode(1);
					metadata.setMessage("OK");
				}
				response.setList(lemburs);
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
	public Response listPersetujuan(
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
					cs.setInt(8, 96);
					cs.setInt(9, 1);
				}
				else {
					cs.setString(1, npp);
					cs.setString(2, kodejobtitle);
					cs.setString(3, kodeoffice);
					cs.setInt(4, start);
					cs.setInt(5, limit);
					cs.setInt(6, 0);
					cs.setInt(7, 0);
					cs.setInt(8, 96);
					cs.setInt(9, 1);
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
	@Path("/get/bykode/{kodeskpd}")
	@Produces("application/json")
	@Consumes("application/json")
	public Response lemburByKode(
			@Context HttpHeaders headers, 
			@PathParam("kodeskpd") Integer kodeskpd
			) {

		Respon<Lembur> response = new Respon<Lembur>();
		Metadata metadata = new Metadata();
		Result<Lembur> result = new Result<Lembur>();
		
		//if (AuthMobile.VerifyToken(headers, metadata)) {
		if (true) {
			Connection con = null;
			ResultSet rs = null;
			ResultSet rs2 = null;
			ResultSet rs3 = null;
			CallableStatement cs = null;
			PreparedStatement ps = null;
			PreparedStatement pstgls = null;
			
			String query = "exec lembur.sp_listlemburall ?, ?, ?, ?, ?, ?, ?";
			
			try {
				con = new Koneksi().getConnection();
				cs = con.prepareCall(query);
				cs.setInt(1, 1);
				cs.setInt(2, 1);
				cs.setInt(3, 0);
				cs.setNull(4, java.sql.Types.VARCHAR);
				cs.setString(5, String.format("kode = \'%d\'", kodeskpd));
				cs.setNull(6, java.sql.Types.VARCHAR);
				cs.setNull(7, java.sql.Types.VARCHAR);
				rs = cs.executeQuery();
				metadata.setCode(0);
				metadata.setMessage("Data tidak ditemukan");
				List<Lembur> lemburs = new ArrayList<>();
				while (rs.next()) {
					Lembur lembur = new Lembur();
					lembur.setKode(rs.getInt("kode"));
					lembur.setNomor(rs.getString("nomor"));
					lembur.setLaporan_isi(rs.getString("laporan_isi"));
					lembur.setLaporan_lampiran(rs.getString("laporan_lampiran"));
					
					Office office = new Office();
					office.setKode(rs.getString("kodeoffice"));
					office.setNama(rs.getString("namaoffice"));
					lembur.setOffice(office);
					
					Akun akun = new Akun();
					akun.setKode(rs.getString("kodeakun"));
					akun.setNama(rs.getString("namaakun"));
					lembur.setAkun(akun);
					
					Program program = new Program();
					program.setKode(rs.getString("kodeprogram"));
					program.setNama(rs.getString("namaprogram"));
					lembur.setProgram(program);
					
					lembur.setLampiran(rs.getString("lampiran")==null?"":rs.getString("lampiran"));
					lembur.setStatus(rs.getInt("status"));
					lembur.setNamastatus(rs.getString("namastatus"));
					
					lembur.setTgllembur(rs.getString("tgllembur"));
					lembur.setPegawailembur(rs.getString("pegawailembur"));
					
					lembur.setDeskripsi(rs.getString("deskripsi"));
					lembur.setNamakegiatan(rs.getString("namakegiatan"));
					lembur.setCatatantolak(rs.getString("catatantolak"));
					
					Penugasan penugasan = new Penugasan();
					penugasan.setKode(rs.getInt("pembuat"));
					Pegawai pegawai = new Pegawai();
					pegawai.setNpp(rs.getString("npppembuat"));
					pegawai.setNama(rs.getString("namapembuat"));
					penugasan.setPegawai(pegawai);
					lembur.setPembuat(penugasan);
					
					try {
						Integer tahun = Calendar.getInstance().get(Calendar.YEAR);
						ps = con.prepareStatement("select \r\n" + 
								"a.*, c.npp, c.nama, d.totaljam \r\n" + 
								"from lembur.pegawai a \r\n" + 
								"inner join karyawan.penugasan b on a.kodepenugasan=b.kode \r\n" + 
								"inner join karyawan.vw_pegawai c on b.npp=c.npp \r\n" + 
								"left join (select * from lembur.vw_totaljam where tahun=?) d on b.npp=d.npp \r\n" + 
								"where a.kodelembur = ? order by a.kode");
						ps.setInt(1, tahun);
						ps.setInt(2, lembur.getKode());
						rs2 = ps.executeQuery();
						ArrayList<ListPegawai> lemburpegawais = new ArrayList<>();
						while (rs2.next()) {
							ListPegawai lemburpegawai = new ListPegawai();
							lemburpegawai.setKode(rs2.getInt("kode"));
							lemburpegawai.setKodepenugasan(rs2.getInt("kodepenugasan"));
							lemburpegawai.setNpp(rs2.getString("npp"));
							lemburpegawai.setNama(rs2.getString("nama"));
							lemburpegawai.setDeleted(0);
							lemburpegawai.setTotaljam(rs2.getInt("totaljam"));
							lemburpegawais.add(lemburpegawai);
						}
						lembur.setPegawai(lemburpegawais);
					}
					finally {
						if (rs2 != null) {
							try {
								rs2.close();
							} catch (SQLException e) {
							}
						}
						if (ps != null) {
							try {
								ps.close();
							} catch (SQLException e) {
							}
						}
					}
					
					try {
						pstgls = con.prepareStatement("select *, iif(tgl < cast(getdate() as date),0,iif(tgl = cast(getdate() as date) and cast(getdate() as time) > '08:00:00',0,1)) as viewcheck from lembur.lemburtgl where kodelembur=?");
						pstgls.setInt(1, lembur.getKode());
						rs3 = pstgls.executeQuery();
						ArrayList<SaveLemburTgls> saveLemburTgls = new ArrayList<>();
						while (rs3.next()) {
							SaveLemburTgls tgls = new SaveLemburTgls();
							tgls.setKode(rs3.getInt("kode"));
							tgls.setTgl(rs3.getDate("tgl"));
							tgls.setDeleted(0);
							tgls.setBatalkan(false);
							tgls.setStatus(rs3.getInt("status"));
							tgls.setViewcheck(rs3.getBoolean("viewcheck"));
							saveLemburTgls.add(tgls);
						}
						lembur.setTgls(saveLemburTgls);
					}
					finally {
						if (rs3 != null) {
							try {
								rs3.close();
							} catch (SQLException e) {
							}
						}	
						if (pstgls != null) {
							try {
								pstgls.close();
							} catch (SQLException e) {
							}
						}	
					}
					
					/*ps = con.prepareStatement("select \r\n" + 
							"a.*, c.npp, c.nama\r\n" + 
							"from lembur.pegawai a\r\n" + 
							"inner join karyawan.penugasan b on a.kodepenugasan=b.kode\r\n" + 
							"inner join karyawan.vw_pegawai c on b.npp=c.npp\r\n" + 
							"where a.kodelembur = ?");
					ps.setInt(1, lembur.getKode());
					rs2 = ps.executeQuery();
					ArrayList<ListPegawai> lemburpegawais = new ArrayList<>();
					while (rs2.next()) {
						ListPegawai lemburpegawai = new ListPegawai();
						lemburpegawai.setKode(rs2.getInt("kode"));
						lemburpegawai.setKodepenugasan(rs2.getInt("kodepenugasan"));
						lemburpegawai.setNpp(rs2.getString("npp"));
						lemburpegawai.setNama(rs2.getString("nama"));
						lemburpegawai.setDeleted(0);
						lemburpegawais.add(lemburpegawai);
					}*/
					lemburs.add(lembur);
					
					metadata.setCode(1);
					metadata.setMessage("OK");
				}
				response.setList(lemburs);
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
}
