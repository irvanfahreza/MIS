package id.go.bpjskesehatan.service.mobile.v1;

import java.io.File;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
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
import javax.ws.rs.core.Response.ResponseBuilder;

import id.go.bpjskesehatan.database.Koneksi;
import id.go.bpjskesehatan.entitas.Metadata;
import id.go.bpjskesehatan.entitas.Respon;
import id.go.bpjskesehatan.entitas.Result;
import id.go.bpjskesehatan.entitas.karyawan.Pegawai;
import id.go.bpjskesehatan.entitas.karyawan.Penugasan;
import id.go.bpjskesehatan.entitas.organisasi.Office;
import id.go.bpjskesehatan.entitas.referensi.Dati2;
import id.go.bpjskesehatan.entitas.referensi.Propinsi;
import id.go.bpjskesehatan.service.v2.entitas.Akun;
import id.go.bpjskesehatan.service.v2.entitas.MataAnggaran;
import id.go.bpjskesehatan.service.v2.entitas.Program;
import id.go.bpjskesehatan.service.v2.skpd.entitas.Acara;
import id.go.bpjskesehatan.service.v2.skpd.entitas.ListPegawai;
import id.go.bpjskesehatan.service.v2.skpd.entitas.Skpd;
import id.go.bpjskesehatan.skpd.Jeniskendaraan;
import id.go.bpjskesehatan.util.SharedMethod;
import id.go.bpjskesehatan.util.Utils;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

@Path("/mobile/v1/skpd")
public class SkpdRest {
	
    @Context
    private ServletContext context;
    
    @GET
	@Path("/list/iscount/{iscount}/start/{start}/limit/{limit}/npp/{npp}")
	@Produces("application/json")
	@Consumes("application/json")
	public Response listSkpdPegawai(
			@Context HttpHeaders headers, 
			@PathParam("iscount") Integer iscount,
			@PathParam("start") Integer start,
			@PathParam("limit") Integer limit,
			@PathParam("npp") String npp) {

		Respon<Skpd> response = new Respon<Skpd>();
		Metadata metadata = new Metadata();
		Result<Skpd> result = new Result<Skpd>();
		
		//if (AuthMobile.VerifyToken(headers, metadata)) {
		if (true) {
			Connection con = null;
			CallableStatement cs = null;
			PreparedStatement ps = null;
			ResultSet rs = null;
			ResultSet rs2 = null;
			String query = "exec skpd.sp_listskpd ?, ?, ?, ?, ?, ?, ?, ?";
			
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
				List<Skpd> skpds = new ArrayList<>();
				while (rs.next()) {
					
					Skpd skpd = new Skpd();
					skpd.setKode(rs.getInt("kode"));
					skpd.setNomor(rs.getString("nomor"));
					skpd.setTujuan(rs.getInt("tujuan"));
					skpd.setJeniskegiatan(rs.getInt("jeniskegiatan"));
					
					skpd.setLaporan_catatan_ringkas(rs.getString("laporan_catatan_ringkas"));
					skpd.setLaporan_tindak_lanjut(rs.getString("laporan_tindak_lanjut"));
					skpd.setLaporan_catatan_atasan(rs.getString("laporan_catatan_atasan"));
					
					skpd.setLampiran_kegiatan_skpd(rs.getString("lampiran_kegiatan_skpd"));
					skpd.setLampiran_kegiatan_notulen(rs.getString("lampiran_kegiatan_notulen"));
					skpd.setLampiran_kegiatan_foto(rs.getString("lampiran_kegiatan_foto"));
					
					skpd.setLampiran_catatan_revisi_peserta(rs.getString("lampiran_catatan_revisi_peserta"));
					skpd.setLampiran_catatan_revisi_kehadiran(rs.getString("lampiran_catatan_revisi_kehadiran"));
					skpd.setLampiran_catatan_foto(rs.getString("lampiran_catatan_foto"));
					
					skpd.setIsverif(rs.getInt("isverif"));
					
					Office office = new Office();
					office.setKode(rs.getString("kodeoffice"));
					office.setNama(rs.getString("namaoffice"));
					skpd.setOffice(office);
					
					Acara acara = new Acara();
					acara.setKode(rs.getInt("kodeacara"));
					acara.setNama(rs.getString("namaacara"));
					acara.setTujuan(rs.getInt("tujuan"));
					acara.setTempat(rs.getString("tempat"));
					acara.setJam(rs.getTime("jam"));
					
					Office officetujuan = new Office();
					officetujuan.setKode(rs.getString("kodeofficetujuan"));
					officetujuan.setNama(rs.getString("namaofficetujuan"));
					acara.setOfficetujuan(officetujuan);
					
					Dati2 dati2tujuan = new Dati2();
					dati2tujuan.setKode(rs.getString("kodedati2tujuan"));
					dati2tujuan.setNama(rs.getString("namadati2tujuan"));
					Propinsi propinsi = new Propinsi();
					propinsi.setKode(rs.getString("kodepropinsitujuan"));
					propinsi.setNama(rs.getString("namapropinsitujuan"));
					dati2tujuan.setPropinsi(propinsi);
					acara.setDati2tujuan(dati2tujuan);
					
					skpd.setAcara(acara);
					
					skpd.setTglmulai(rs.getDate("tglmulai"));
					skpd.setTglselesai(rs.getDate("tglselesai"));
					skpd.setDeskripsi(rs.getString("deskripsi"));
					skpd.setKeperluan(rs.getString("keperluan"));
					
					Akun akun = new Akun();
					akun.setKode(rs.getString("kodeakun"));
					akun.setNama(rs.getString("namaakun"));
					skpd.setAkun(akun);
					
					Program program = new Program();
					program.setKode(rs.getString("kodeprogram"));
					program.setNama(rs.getString("namaprogram"));
					skpd.setProgram(program);
					
					Jeniskendaraan jeniskendaraan = new Jeniskendaraan();
					jeniskendaraan.setKode(rs.getInt("kodejeniskendaraan"));
					jeniskendaraan.setNama(rs.getString("namajeniskendaraan"));
					skpd.setJeniskendaraan(jeniskendaraan);
					
					skpd.setLampiran(rs.getString("lampiran")==null?"":rs.getString("lampiran"));
					skpd.setStatus(rs.getInt("status"));
					skpd.setNamastatus(rs.getString("namastatus"));
					
					Penugasan penugasan = new Penugasan();
					penugasan.setKode(rs.getInt("pembuat"));
					Pegawai pegawai = new Pegawai();
					pegawai.setNpp(rs.getString("npppembuat"));
					pegawai.setNama(rs.getString("namapembuat"));
					penugasan.setPegawai(pegawai);
					skpd.setPembuat(penugasan);
					
					try {
						ps = con.prepareStatement("select \r\n" + 
								"a.*, c.npp, c.nama, isnull(a.verif,0) as isverif \r\n" + 
								"from skpd.skpdpegawai a \r\n" + 
								"inner join karyawan.penugasan b on a.kodepenugasan=b.kode \r\n" + 
								"inner join karyawan.vw_pegawai c on b.npp=c.npp \r\n" + 
								"where a.kodeskpd = ? order by a.kode");
						ps.setInt(1, skpd.getKode());
						rs2 = ps.executeQuery();
						ArrayList<ListPegawai> skpdpegawais = new ArrayList<>();
						Integer no = 0;
						while (rs2.next()) {
							no++;
							ListPegawai skpdpegawai = new ListPegawai();
							skpdpegawai.setNo(no);
							skpdpegawai.setKode(rs2.getInt("kode"));
							skpdpegawai.setKodepenugasan(rs2.getInt("kodepenugasan"));
							skpdpegawai.setNpp(rs2.getString("npp"));
							skpdpegawai.setNama(rs2.getString("nama"));
							skpdpegawai.setDeleted(0);
							skpdpegawai.setIsverif(rs2.getInt("isverif"));
							skpdpegawais.add(skpdpegawai);
						}
						skpd.setPegawai(skpdpegawais);
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
						ps = con.prepareStatement("select a.kode, KDPROG, nmprog, KDAKUN, nmakun \r\n" + 
								"from skpd.skpdmataanggaran a \r\n" + 
								"outer apply \r\n" + 
								"( \r\n" + 
								"	select top 1 ga.KDAKUN, ga.nmakun from referensi.vw_kodeakun ga where ga.KDAKUN=a.kodeakun \r\n" + 
								") b \r\n" + 
								"outer apply \r\n" + 
								"( \r\n" + 
								"	select top 1 ha.KDPROG, ha.nmprog from referensi.vw_kodeprogram ha where ha.KDPROG=a.kodeprogram \r\n" + 
								") c where a.kodeskpd=?");
						ps.setInt(1, skpd.getKode());
						rs2 = ps.executeQuery();
						ArrayList<MataAnggaran> mataAnggarans = new ArrayList<>();
						Integer no = 0;
						while (rs2.next()) {
							no++;
							MataAnggaran mataAnggaran = new MataAnggaran();
							mataAnggaran.setNo(no);
							mataAnggaran.setKode(rs2.getInt("kode"));
							Program program2 = new Program();
							program2.setKode(rs2.getString("KDPROG"));
							program2.setNama(rs2.getString("nmprog"));
							Akun akun2 = new Akun();
							akun2.setKode(rs2.getString("KDAKUN"));
							akun2.setNama(rs2.getString("nmakun"));
							mataAnggaran.setProgram(program2);
							mataAnggaran.setAkun(akun2);
							mataAnggaran.setDeleted(0);
							mataAnggarans.add(mataAnggaran);
						}
						skpd.setMataanggaran(mataAnggarans);
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
					
					skpds.add(skpd);
					metadata.setCode(1);
					metadata.setMessage("OK");
				}
				response.setList(skpds);
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
					cs.setInt(8, 99);
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
					cs.setInt(8, 99);
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
	public Response skpdByKode(
			@Context HttpHeaders headers, 
			@PathParam("kodeskpd") Integer kodeskpd
			) {

		Respon<Skpd> response = new Respon<Skpd>();
		Metadata metadata = new Metadata();
		Result<Skpd> result = new Result<Skpd>();
		
		//if (AuthMobile.VerifyToken(headers, metadata)) {
		if (true) {
			Connection con = null;
			CallableStatement cs = null;
			PreparedStatement ps = null;
			ResultSet rs = null;
			ResultSet rs2 = null;
			String query = "exec skpd.sp_listskpdall ?, ?, ?, ?, ?, ?, ?";
			
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
				List<Skpd> skpds = new ArrayList<>();
				while (rs.next()) {
					Skpd skpd = new Skpd();
					skpd.setKode(rs.getInt("kode"));
					skpd.setNomor(rs.getString("nomor"));
					skpd.setTujuan(rs.getInt("tujuan"));
					skpd.setJeniskegiatan(rs.getInt("jeniskegiatan"));
					
					skpd.setLaporan_catatan_ringkas(rs.getString("laporan_catatan_ringkas"));
					skpd.setLaporan_tindak_lanjut(rs.getString("laporan_tindak_lanjut"));
					skpd.setLaporan_catatan_atasan(rs.getString("laporan_catatan_atasan"));
					
					skpd.setLampiran_kegiatan_skpd(rs.getString("lampiran_kegiatan_skpd"));
					skpd.setLampiran_kegiatan_notulen(rs.getString("lampiran_kegiatan_notulen"));
					skpd.setLampiran_kegiatan_foto(rs.getString("lampiran_kegiatan_foto"));
					
					skpd.setLampiran_catatan_revisi_peserta(rs.getString("lampiran_catatan_revisi_peserta"));
					skpd.setLampiran_catatan_revisi_kehadiran(rs.getString("lampiran_catatan_revisi_kehadiran"));
					skpd.setLampiran_catatan_foto(rs.getString("lampiran_catatan_foto"));
					
					Office office = new Office();
					office.setKode(rs.getString("kodeoffice"));
					office.setNama(rs.getString("namaoffice"));
					skpd.setOffice(office);
					
					Acara acara = new Acara();
					acara.setKode(rs.getInt("kodeacara"));
					acara.setNama(rs.getString("namaacara"));
					acara.setTujuan(rs.getInt("tujuan"));
					acara.setTempat(rs.getString("tempat"));
					
					Office officetujuan = new Office();
					officetujuan.setKode(rs.getString("kodeofficetujuan"));
					officetujuan.setNama(rs.getString("namaofficetujuan"));
					acara.setOfficetujuan(officetujuan);
					
					Dati2 dati2tujuan = new Dati2();
					dati2tujuan.setKode(rs.getString("kodedati2tujuan"));
					dati2tujuan.setNama(rs.getString("namadati2tujuan"));
					Propinsi propinsi = new Propinsi();
					propinsi.setKode(rs.getString("kodepropinsitujuan"));
					propinsi.setNama(rs.getString("namapropinsitujuan"));
					dati2tujuan.setPropinsi(propinsi);
					acara.setDati2tujuan(dati2tujuan);
					
					skpd.setAcara(acara);
					
					skpd.setTglmulai(rs.getDate("tglmulai"));
					skpd.setTglselesai(rs.getDate("tglselesai"));
					skpd.setDeskripsi(rs.getString("deskripsi"));
					skpd.setKeperluan(rs.getString("keperluan"));
					
					Akun akun = new Akun();
					akun.setKode(rs.getString("kodeakun"));
					akun.setNama(rs.getString("namaakun"));
					skpd.setAkun(akun);
					
					Program program = new Program();
					program.setKode(rs.getString("kodeprogram"));
					program.setNama(rs.getString("namaprogram"));
					skpd.setProgram(program);
					
					Jeniskendaraan jeniskendaraan = new Jeniskendaraan();
					jeniskendaraan.setKode(rs.getInt("kodejeniskendaraan"));
					jeniskendaraan.setNama(rs.getString("namajeniskendaraan"));
					skpd.setJeniskendaraan(jeniskendaraan);
					
					skpd.setLampiran(rs.getString("lampiran")==null?"":rs.getString("lampiran"));
					skpd.setStatus(rs.getInt("status"));
					skpd.setNamastatus(rs.getString("namastatus"));
					
					Penugasan penugasan = new Penugasan();
					penugasan.setKode(rs.getInt("pembuat"));
					Pegawai pegawai = new Pegawai();
					pegawai.setNpp(rs.getString("npppembuat"));
					pegawai.setNama(rs.getString("namapembuat"));
					penugasan.setPegawai(pegawai);
					skpd.setPembuat(penugasan);
					
					try {
						ps = con.prepareStatement("select \r\n" + 
								"a.*, c.npp, c.nama, isnull(a.verif,0) as isverif \r\n" + 
								"from skpd.skpdpegawai a \r\n" + 
								"inner join karyawan.penugasan b on a.kodepenugasan=b.kode \r\n" + 
								"inner join karyawan.vw_pegawai c on b.npp=c.npp \r\n" + 
								"where a.kodeskpd = ? order by a.kode");
						ps.setInt(1, skpd.getKode());
						rs2 = ps.executeQuery();
						ArrayList<ListPegawai> skpdpegawais = new ArrayList<>();
						Integer no = 0;
						while (rs2.next()) {
							no++;
							ListPegawai skpdpegawai = new ListPegawai();
							skpdpegawai.setNo(no);
							skpdpegawai.setKode(rs2.getInt("kode"));
							skpdpegawai.setKodepenugasan(rs2.getInt("kodepenugasan"));
							skpdpegawai.setNpp(rs2.getString("npp"));
							skpdpegawai.setNama(rs2.getString("nama"));
							skpdpegawai.setDeleted(0);
							skpdpegawai.setIsverif(rs2.getInt("isverif"));
							skpdpegawais.add(skpdpegawai);
						}
						skpd.setPegawai(skpdpegawais);
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
						ps = con.prepareStatement("select a.kode, KDPROG, nmprog, KDAKUN, nmakun \r\n" + 
								"from skpd.skpdmataanggaran a \r\n" + 
								"outer apply \r\n" + 
								"( \r\n" + 
								"	select top 1 ga.KDAKUN, ga.nmakun from referensi.vw_kodeakun ga where ga.KDAKUN=a.kodeakun \r\n" + 
								") b \r\n" + 
								"outer apply \r\n" + 
								"( \r\n" + 
								"	select top 1 ha.KDPROG, ha.nmprog from referensi.vw_kodeprogram ha where ha.KDPROG=a.kodeprogram \r\n" + 
								") c where a.kodeskpd=?");
						ps.setInt(1, skpd.getKode());
						rs2 = ps.executeQuery();
						ArrayList<MataAnggaran> mataAnggarans = new ArrayList<>();
						Integer no = 0;
						while (rs2.next()) {
							no++;
							MataAnggaran mataAnggaran = new MataAnggaran();
							mataAnggaran.setNo(no);
							mataAnggaran.setKode(rs2.getInt("kode"));
							Program program2 = new Program();
							program2.setKode(rs2.getString("KDPROG"));
							program2.setNama(rs2.getString("nmprog"));
							Akun akun2 = new Akun();
							akun2.setKode(rs2.getString("KDAKUN"));
							akun2.setNama(rs2.getString("nmakun"));
							mataAnggaran.setProgram(program2);
							mataAnggaran.setAkun(akun2);
							mataAnggaran.setDeleted(0);
							mataAnggarans.add(mataAnggaran);
						}
						skpd.setMataanggaran(mataAnggarans);
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
					
					skpds.add(skpd);
					metadata.setCode(1);
					metadata.setMessage("OK");
				}
				response.setList(skpds);
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
	@Path("/cetak/{token}/{kode}")
	@Produces("application/pdf")
	public Response cetakSKPD(@Context HttpHeaders headers, @PathParam("token") String token, @PathParam("kode") Integer kode) {
		Result<Object> result = new Result<Object>();
		Metadata metadata = new Metadata();
		
		//if (AuthMobile.VerifyToken(headers, metadata)) {
		if (true) {
			ResponseBuilder response;
			Connection con = null;
			String sumber = "";
			String filename = "";
			
			try {
				String ihc_baseurl = context.getInitParameter("ihc.baseurl");
				
				filename = "SKPD";				
				sumber = "/IHC-Report/eskapede.jasper";
				
				String path = "/tmp/";
				con = new Koneksi().getConnection();
				
				HashMap hm = new HashMap();
				hm.put("kode", kode);
				hm.put("baseurl", ihc_baseurl);

				File report_file = new File(sumber);
	            JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(report_file.getPath());
	            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, hm, con);
	            JasperExportManager.exportReportToPdfFile(jasperPrint,path + filename + "_" + kode + ".pdf");
			    
			    File file = new File(path + filename + "_" + kode + ".pdf");
			    response = Response.ok((Object) file);
	        	response.header("Content-Disposition", "attachment; filename=" + filename+"_"+kode+".pdf");
				return response.build();
			} catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				if (con != null) {
					try {
						con.close();
					} catch (SQLException e) {
					}
				}
			}
			
	        response = Response.noContent();
			return response.build();
		}
		result.setMetadata(metadata);
		return Response.ok(result).build();
	}
}
