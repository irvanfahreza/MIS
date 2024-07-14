package id.go.bpjskesehatan.service.v2.skpd;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
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
import id.go.bpjskesehatan.entitas.cuti.SaveCutiTgls;
import id.go.bpjskesehatan.entitas.hcis.Notifikasi;
import id.go.bpjskesehatan.entitas.karyawan.Pegawai;
import id.go.bpjskesehatan.entitas.karyawan.Penugasan;
import id.go.bpjskesehatan.entitas.organisasi.Office;
import id.go.bpjskesehatan.entitas.referensi.Dati2;
import id.go.bpjskesehatan.entitas.referensi.Propinsi;
import id.go.bpjskesehatan.service.v2.cuti.entitas.Kuota;
import id.go.bpjskesehatan.service.v2.cuti.entitas.Spm;
import id.go.bpjskesehatan.service.v2.entitas.Akun;
import id.go.bpjskesehatan.service.v2.entitas.MataAnggaran;
import id.go.bpjskesehatan.service.v2.entitas.Program;
import id.go.bpjskesehatan.service.v2.skpd.entitas.Acara;
import id.go.bpjskesehatan.service.v2.skpd.entitas.ListPegawai;
import id.go.bpjskesehatan.service.v2.skpd.entitas.ListPegawaiQR;
import id.go.bpjskesehatan.service.v2.skpd.entitas.MataAnggaranQR;
import id.go.bpjskesehatan.service.v2.skpd.entitas.Skpd;
import id.go.bpjskesehatan.service.v2.skpd.entitas.SkpdQR;
import id.go.bpjskesehatan.service.v2.skpd.entitas.Skpdpegawai;
import id.go.bpjskesehatan.skpd.Jeniskendaraan;
import id.go.bpjskesehatan.util.SharedMethod;
import id.go.bpjskesehatan.util.Utils;

@Path("/v2/skpd")
public class Skpd2Rest {	
	
	@Context
    private ServletContext context;
	
	@POST
	@Path("/grid/{npp}/{page}/{row}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response ListGrid(@Context HttpHeaders headers,
			@PathParam("npp") String npp, @PathParam("page") String page, @PathParam("row") String row, String data) {
		
		Respon<Skpd> response = new Respon<Skpd>();
		Metadata metadata = new Metadata();
		Result<Skpd> result = new Result<Skpd>();
		
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
					query = "exec skpd.sp_listskpd ?, ?, ?, ?, ?, ?, ?, ?";
					con = new Koneksi().getConnection();
					cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
					cs.setString(1, npp);
					cs.setInt(2, Integer.parseInt(page));
					cs.setInt(3, Integer.parseInt(row));
					cs.setInt(4, 1);
					cs.setString(5, order);
					cs.setString(6, filter);
					cs.setString(7, filter2);
					cs.setString(8, filter3);
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
					cs.setString(1, npp);
					cs.setInt(2, Integer.parseInt(page));
					cs.setInt(3, Integer.parseInt(row));
					cs.setInt(4, 0);
					cs.setString(5, order);
					cs.setString(6, filter);
					cs.setString(7, filter2);
					cs.setString(8, filter3);
					rs = cs.executeQuery();
					
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
	@Path("/laporan/{tgl1}/{tgl2}/{kodepic}/{status}/{page}/{row}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response ListGridLaporanSKPD(@Context HttpHeaders headers,
			@PathParam("tgl1") String tgl1, 
			@PathParam("tgl2") String tgl2, 
			@PathParam("kodepic") String kodepic,
			@PathParam("status") Integer status,
			@PathParam("page") String page, @PathParam("row") String row, String data) {
		
		Respon<Skpd> response = new Respon<Skpd>();
		Metadata metadata = new Metadata();
		Result<Skpd> result = new Result<Skpd>();
		
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
						
					}
				}
				
				if(ok) {
					query = "exec skpd.sp_listlaporanskpd ?, ?, ?, ?, ?, ?, ?, ?, ?";
					con = new Koneksi().getConnection();
					cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
					cs.setInt(1, Integer.parseInt(page));
					cs.setInt(2, Integer.parseInt(row));
					cs.setInt(3, 1);
					cs.setString(4, order);
					cs.setString(5, filter);
					cs.setString(6, tgl1);
					cs.setString(7, tgl2);
					cs.setString(8, kodepic);
					cs.setInt(9, status);
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
					cs.setString(6, tgl1);
					cs.setString(7, tgl2);
					cs.setString(8, kodepic);
					cs.setInt(9, status);
					rs = cs.executeQuery();
					
					List<Skpd> skpds = new ArrayList<>();
					while (rs.next()) {
						Skpd skpd = new Skpd();
						skpd.setKode(rs.getInt("kode"));
						skpd.setNomor(rs.getString("nomor"));
						skpd.setTujuan(rs.getInt("tujuan"));
						skpd.setJeniskegiatan(rs.getInt("jeniskegiatan"));
						
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
									"a.*, c.npp, c.nama\r\n" + 
									"from skpd.skpdpegawai a\r\n" + 
									"inner join karyawan.penugasan b on a.kodepenugasan=b.kode\r\n" + 
									"inner join karyawan.vw_pegawai c on b.npp=c.npp\r\n" + 
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
				}
			} catch (SQLException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
				e.printStackTrace();
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
	@Path("/grid/{perspektif}/{tgl1}/{tgl2}/{jeniskantor}/{kodelokasi}/{nomor: .*}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response ListGridLaporanSKPDPegawai(@Context HttpHeaders headers,
			@PathParam("perspektif") String perspektif,
			@PathParam("tgl1") String tgl1, 
			@PathParam("tgl2") String tgl2, 
			@PathParam("jeniskantor") Integer jeniskantor,
			@PathParam("kodelokasi") String kodelokasi,
			@PathParam("nomor") String nomor,
			String data) {
		
		Map<String, Object> response = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> metadata = new HashMap<String, Object>();
		List<Map<String, Object>> list = null;
		List<Map<String, Object>> list2 = null;
		List<Map<String, Object>> list3 = null;
		List<Map<String, Object>> list4 = null;
		List<Map<String, Object>> list5 = null;
		List<Map<String, Object>> list6 = null;
		
		Connection con = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		ResultSet rs3 = null;
		ResultSet rs4 = null;
		ResultSet rs5 = null;
		ResultSet rs6 = null;
		CallableStatement cs = null;
		CallableStatement cs2 = null;
		CallableStatement cs3 = null;
		CallableStatement cs4 = null;
		CallableStatement cs5 = null;
		CallableStatement cs6 = null;
		String query = null;
		String query2 = null;
		String query3 = null;
		String query4 = null;
		String query5 = null;
		String query6 = null;

		//if (SharedMethod.VerifyToken(headers, metadata)) {
		if (true) {
			try {
				JsonNode json = null;
				if (data != null) {
					if (!data.isEmpty()) {
						ObjectMapper mapper = new ObjectMapper();
						json = mapper.readTree(data);						
					}
				}
				
				java.sql.Date tanggal1 = new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(tgl1).getTime());
				java.sql.Date tanggal2 = new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(tgl2).getTime());
				
				if(perspektif.equalsIgnoreCase("pegawai"))
					query = "exec skpd.sp_getlaporanskpdpegawai ?, ?, ?, ?, ?";
				else if(perspektif.equalsIgnoreCase("kantortujuan"))
					query = "exec skpd.sp_getlaporanskpdpegawai_bykantortujuan ?, ?, ?, ?, ?";
					
				con = new Koneksi().getConnection();
				cs = con.prepareCall(query);
				cs.setDate(1, tanggal1);
				cs.setDate(2, tanggal2);
				cs.setInt(3, jeniskantor);
				cs.setString(4, kodelokasi);
				cs.setString(5, nomor);
				rs = cs.executeQuery();
				
				list = new ArrayList<Map<String, Object>>();
				ResultSetMetaData metaData = rs.getMetaData();
				Map<String, Object> hasil = null;
				
				Integer kodeskpd = 0;
				while (rs.next()) {
					hasil = new HashMap<String, Object>();
					for (int i = 1; i <= metaData.getColumnCount(); i++) {
						if(rs.getObject(i)!=null && metaData.getColumnTypeName(i).equalsIgnoreCase("date")){
							hasil.put(metaData.getColumnName(i).toLowerCase(), Utils.SqlDateToSqlString(rs.getDate(i)));
						}
						else {
							hasil.put(metaData.getColumnName(i).toLowerCase(), rs.getObject(i));
						}
						if(metaData.getColumnName(i).equalsIgnoreCase("kode")) {
							kodeskpd = (Integer) rs.getObject(i);
						}
					}
					
					/*------------------start mataanggaran---------------*/
					try {
						query6 = "exec skpd.sp_getlaporanskpdpegawaimataanggaran ?";
						cs6 = con.prepareCall(query6);
						cs6.setInt(1, kodeskpd);
						rs6 = cs6.executeQuery();
						
						list6 = new ArrayList<Map<String, Object>>();
						ResultSetMetaData metaData6 = rs6.getMetaData();
						Map<String, Object> hasil6 = null;
						while (rs6.next()) {
							hasil6 = new HashMap<String, Object>();
							for (int i = 1; i <= metaData6.getColumnCount(); i++) {
								hasil6.put(metaData6.getColumnName(i).toLowerCase(), rs6.getObject(i));
							}
							list6.add(hasil6);
						}
						hasil.put("mataanggaran", list6);
					}
					finally {
						if (rs6 != null) {
							try {
								rs6.close();
							} catch (SQLException e) {
							}
						}
						if (cs6 != null) {
							try {
								cs6.close();
							} catch (SQLException e) {
							}
						}
					}
					/*------------------end mataanggaran---------------*/
					
					
					/*------------------start pegawai---------------*/
					try {
						if(perspektif.equalsIgnoreCase("pegawai"))
							query2 = "exec skpd.sp_getlaporanskpdpegawaidetail ?,?,?";
						else if(perspektif.equalsIgnoreCase("kantortujuan"))
							query2 = "exec skpd.sp_getlaporanskpdpegawaidetail_bykantortujuan ?,?,?";
						
						cs2 = con.prepareCall(query2);
						cs2.setInt(1, kodeskpd);
						cs2.setInt(2, jeniskantor);
						cs2.setString(3, kodelokasi);	
						rs2 = cs2.executeQuery();
						
						list2 = new ArrayList<Map<String, Object>>();
						ResultSetMetaData metaData2 = rs2.getMetaData();
						Map<String, Object> hasil2 = null;
						
						Integer kodeskpdpegawai = 0;
						while (rs2.next()) {
							hasil2 = new HashMap<String, Object>();
							for (int i = 1; i <= metaData2.getColumnCount(); i++) {
								hasil2.put(metaData2.getColumnName(i).toLowerCase(), rs2.getObject(i));
								if(metaData2.getColumnName(i).equalsIgnoreCase("kode")) {
									kodeskpdpegawai = (Integer) rs2.getObject(i);
								}
							}
							
							/*---------------start tgl------------------*/
							try {
								query3 = "exec skpd.sp_getlaporanskpdpegawaidetailtgl ?";
								cs3 = con.prepareCall(query3);
								cs3.setInt(1, kodeskpdpegawai);
								rs3 = cs3.executeQuery();
								
								list3 = new ArrayList<Map<String, Object>>();
								ResultSetMetaData metaData3 = rs3.getMetaData();
								Map<String, Object> hasil3 = null;
								
								while (rs3.next()) {
									hasil3 = new HashMap<String, Object>();
									for (int i = 1; i <= metaData3.getColumnCount(); i++) {
										hasil3.put(metaData3.getColumnName(i).toLowerCase(), rs3.getObject(i));
									}
									list3.add(hasil3);
								}
								hasil2.put("tgls", list3);
								
							}
							finally {
								if (rs3 != null) {
									try {
										rs3.close();
									} catch (SQLException e) {
									}
								}
								if (cs3 != null) {
									try {
										cs3.close();
									} catch (SQLException e) {
									}
								}
							}
							/* end tgl */
							
							/*---------------start tagihan------------------*/
							try {
								query4 = "exec skpd.sp_getlaporanskpdpegawaidetailtagihan ?";
								cs4 = con.prepareCall(query4);
								cs4.setInt(1, kodeskpdpegawai);
								rs4 = cs4.executeQuery();
								
								list4 = new ArrayList<Map<String, Object>>();
								ResultSetMetaData metaData4 = rs4.getMetaData();
								Map<String, Object> hasil4 = null;
								
								Short jenistagihan = 0;
								while (rs4.next()) {
									hasil4 = new HashMap<String, Object>();
									for (int i = 1; i <= metaData4.getColumnCount(); i++) {
										hasil4.put(metaData4.getColumnName(i).toLowerCase(), rs4.getObject(i));
										if(metaData4.getColumnName(i).equalsIgnoreCase("jenis")) {
											jenistagihan = (Short) rs4.getObject(i);
										}
									}
									/*---------------start tagihan detil------------------*/
									try {
										query5 = "exec skpd.sp_getlaporanskpdpegawaidetailtagihandetail ?, ?";
										cs5 = con.prepareCall(query5);
										cs5.setInt(1, kodeskpdpegawai);
										cs5.setShort(2, jenistagihan);
										rs5 = cs5.executeQuery();
										
										list5 = new ArrayList<Map<String, Object>>();
										ResultSetMetaData metaData5 = rs5.getMetaData();
										Map<String, Object> hasil5 = null;
										
										while (rs5.next()) {
											hasil5 = new HashMap<String, Object>();
											for (int i = 1; i <= metaData5.getColumnCount(); i++) {
												hasil5.put(metaData5.getColumnName(i).toLowerCase(), rs5.getObject(i));
											}
											list5.add(hasil5);
										}
										hasil4.put("reftagihan", list5);
										
									}
									finally {
										if (rs5 != null) {
											try {
												rs5.close();
											} catch (SQLException e) {
											}
										}
										if (cs5 != null) {
											try {
												cs5.close();
											} catch (SQLException e) {
											}
										}
									}
									/* end tagihan detil */
									
									list4.add(hasil4);
								}
								hasil2.put("tagihan", list4);
								
							}
							finally {
								if (rs4 != null) {
									try {
										rs4.close();
									} catch (SQLException e) {
									}
								}
								if (cs4 != null) {
									try {
										cs4.close();
									} catch (SQLException e) {
									}
								}
							}
							/* end tagihan */
							
							list2.add(hasil2);
						}
						hasil.put("pegawai", list2);
						
					}
					finally {
						if (rs2 != null) {
							try {
								rs2.close();
							} catch (SQLException e) {
							}
						}
						if (cs2 != null) {
							try {
								cs2.close();
							} catch (SQLException e) {
							}
						}
					}
					/* end pegawai */
					
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
	
	@POST
	@Path("/laporanskpdpegawai/{jenis}/verif")
	@Consumes("application/json")
	@Produces("application/json")
	public Response verifLaporanSKPDPegawai(@Context HttpHeaders headers,
			@PathParam("jenis") String jenis,
			String data) {
		
		if (!(jenis.equalsIgnoreCase("mataanggaran") || jenis.equalsIgnoreCase("pegawai"))) {
			return Response.status(404).build();
		}
		
		Respon<id.go.bpjskesehatan.service.v2.skpd.entitas.verif.Skpd> response = new Respon<id.go.bpjskesehatan.service.v2.skpd.entitas.verif.Skpd>();
		Metadata metadata = new Metadata();
		Result<id.go.bpjskesehatan.service.v2.skpd.entitas.verif.Skpd> result = new Result<id.go.bpjskesehatan.service.v2.skpd.entitas.verif.Skpd>();
		
		Connection con = null;
		CallableStatement cs = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = null;

		//if (SharedMethod.VerifyToken(headers, metadata)) {
		if (true) {
			try {
				
				ObjectMapper mapper = new ObjectMapper();
				id.go.bpjskesehatan.service.v2.skpd.entitas.verif.Skpd json = mapper.readValue(data, id.go.bpjskesehatan.service.v2.skpd.entitas.verif.Skpd.class);
				
				if(jenis.equalsIgnoreCase("mataanggaran")) {
					try {
						query = "exec skpd.sp_verif_mataanggaran ?,?,?";
						con = new Koneksi().getConnection();
						cs = con.prepareCall(query);
						cs.setInt(1, json.getKode());
						cs.setInt(2, json.getKodeskpdmataanggaran());
						cs.setInt(3, json.getUseract());
						cs.executeUpdate();
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
				else if(jenis.equalsIgnoreCase("pegawai")) {
					for (id.go.bpjskesehatan.service.v2.skpd.entitas.verif.SkpdPegawai peg : json.getSkpdpegawai()) {
						
						for (id.go.bpjskesehatan.service.v2.skpd.entitas.verif.SkpdPegawaiTgl tgl : peg.getSkpdpegawaitgl()) {
							try {
								query = "exec skpd.sp_verif_pegawai_tgl ?,?,?,?";
								con = new Koneksi().getConnection();
								cs = con.prepareCall(query);
								cs.setInt(1, tgl.getKode());
								cs.setInt(2, tgl.getAktif());
								cs.setString(3, tgl.getKeterangan());
								cs.setInt(4, 1);
								cs.executeUpdate();
							}
							catch (Exception e) {
								throw new Exception(e.getMessage());
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
						
						//System.out.println("jalan");
						for (id.go.bpjskesehatan.service.v2.skpd.entitas.verif.SkpdPegawaiTgl tgl : peg.getSkpdpegawaitgl()) {
							try {
								query = "exec skpd.sp_verif_pegawai_tgl ?,?,?,?";
								con = new Koneksi().getConnection();
								cs = con.prepareCall(query);
								cs.setInt(1, tgl.getKode());
								cs.setInt(2, tgl.getAktif());
								cs.setString(3, tgl.getKeterangan());
								cs.setInt(4, 0);
								cs.executeUpdate();
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
						
						for (id.go.bpjskesehatan.service.v2.skpd.entitas.verif.SkpdPegawaiTagihan tgl : peg.getSkpdpegawaitagihan()) {
							try {
								query = "exec skpd.sp_verif_pegawai_tagihan ?,?,?,?,?,?";
								con = new Koneksi().getConnection();
								cs = con.prepareCall(query);
								cs.setInt(1, peg.getKode());
								cs.setInt(2, tgl.getKodereftagihan());
								cs.setBigDecimal(3, tgl.getNilai());
								cs.setInt(4, tgl.getQty());
								cs.setBigDecimal(5, tgl.getSubtotal());
								cs.setInt(6, json.getUseract());
								cs.executeUpdate();
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
						
						try {
							query = "exec skpd.sp_verif_pegawai_tagihan_total ?,?,?";
							con = new Koneksi().getConnection();
							cs = con.prepareCall(query);
							cs.setInt(1, peg.getKode());
							cs.setBigDecimal(2, peg.getTotal());
							cs.setInt(3, json.getUseract());
							cs.executeUpdate();
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
				
				id.go.bpjskesehatan.service.v2.skpd.entitas.verif.Skpd skpd_respon = new id.go.bpjskesehatan.service.v2.skpd.entitas.verif.Skpd();
				skpd_respon.setIsverif(1);
				try {
					query = "select b.nama from hcis.users a \r\n" + 
							"inner join karyawan.vw_pegawai b on a.npp=b.npp \r\n" + 
							"where a.id=?";
					con = new Koneksi().getConnection();
					ps = con.prepareStatement(query);
					ps.setInt(1, json.getUseract());
					rs = ps.executeQuery();
					if(rs.next()) {
						skpd_respon.setNamaverif(rs.getString("nama"));
					}
				}
				finally {
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
				response.setData(skpd_respon);
				result.setResponse(response);
				
				metadata.setCode(1);
				metadata.setMessage("Ok.");
				
			} catch (Exception e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
				e.printStackTrace();
			} finally {

			}
		}
		
		result.setMetadata(metadata);
		return Response.ok(result).build();
	}
	
	@POST
	@Path("/grid/{page}/{row}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response ListGridAll(@Context HttpHeaders headers,
			@PathParam("page") String page, @PathParam("row") String row, String data) {
		
		Respon<Skpd> response = new Respon<Skpd>();
		Metadata metadata = new Metadata();
		Result<Skpd> result = new Result<Skpd>();
		
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
					query = "exec skpd.sp_listskpdall ?, ?, ?, ?, ?, ?, ?";
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
					cs.setString(6, filter2);
					cs.setString(7, filter3);
					rs = cs.executeQuery();
					
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
									"a.*, c.npp, c.nama\r\n" + 
									"from skpd.skpdpegawai a\r\n" + 
									"inner join karyawan.penugasan b on a.kodepenugasan=b.kode\r\n" + 
									"inner join karyawan.vw_pegawai c on b.npp=c.npp\r\n" + 
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
	@Path("/grid/acara/{tglmulai}/{tglselesai}/{kodepic}/{page}/{row}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response ListGridAcara(@Context HttpHeaders headers,
			@PathParam("tglmulai") String tglmulai,
			@PathParam("tglselesai") String tglselesai,
			@PathParam("kodepic") String kodepic,
			@PathParam("page") String page, @PathParam("row") String row, String data) {
		
		Respon<Acara> response = new Respon<Acara>();
		Metadata metadata = new Metadata();
		Result<Acara> result = new Result<Acara>();
		
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
					query = "exec skpd.sp_listacara ?, ?, ?, ?, ?, ?, ?, ?";
					con = new Koneksi().getConnection();
					cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
					cs.setString(1, tglmulai);
					cs.setString(2, tglselesai);
					cs.setString(3, kodepic);
					cs.setInt(4, Integer.parseInt(page));
					cs.setInt(5, Integer.parseInt(row));
					cs.setInt(6, 1);
					cs.setString(7, order);
					cs.setString(8, filter);
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
					cs.setString(1, tglmulai);
					cs.setString(2, tglselesai);
					cs.setString(3, kodepic);
					cs.setInt(4, Integer.parseInt(page));
					cs.setInt(5, Integer.parseInt(row));
					cs.setInt(6, 0);
					cs.setString(7, order);
					cs.setString(8, filter);
					rs = cs.executeQuery();
					
					List<Acara> acaras = new ArrayList<>();
					while (rs.next()) {
						Acara acara = new Acara();
						acara.setKode(rs.getInt("kode"));
						acara.setNama(rs.getString("nama"));
						acara.setTglmulai(rs.getDate("tglmulai"));
						acara.setTglselesai(rs.getDate("tglselesai"));
						acara.setXtglmulai(rs.getString("xtglmulai"));
						acara.setXtglselesai(rs.getString("xtglselesai"));
						acara.setTujuan(rs.getInt("tujuan"));
						acara.setJeniskegiatan(rs.getInt("jeniskegiatan"));
						acara.setTempat(rs.getString("tempat"));
						acara.setJam(rs.getTime("jam"));
						
						Office pic = new Office();
						pic.setKode(rs.getString("kodepic"));
						pic.setNama(rs.getString("namapic"));
						acara.setPic(pic);
						
						Office officetujuan = new Office();
						officetujuan.setKode(rs.getString("kodeofficetujuan"));
						officetujuan.setNama(rs.getString("namaofficetujuan"));
						acara.setOfficetujuan(officetujuan);
						
						Propinsi propinsi = new Propinsi();
						propinsi.setKode(rs.getString("kodepropinsitujuan"));
						propinsi.setNama(rs.getString("namapropinsitujuan"));
						
						Dati2 dati2 = new Dati2();
						dati2.setKode(rs.getString("kodedati2tujuan"));
						dati2.setNama(rs.getString("namadati2tujuan"));
						dati2.setPropinsi(propinsi);
						acara.setDati2tujuan(dati2);
						
						try {
							ps = con.prepareStatement("select a.kode, KDPROG, nmprog, KDAKUN, nmakun \r\n" + 
									"from skpd.acaramataanggaran a \r\n" + 
									"outer apply \r\n" + 
									"( \r\n" + 
									"	select top 1 ga.KDAKUN, ga.nmakun from referensi.vw_kodeakun ga where ga.KDAKUN=a.kodeakun \r\n" + 
									") b \r\n" + 
									"outer apply \r\n" + 
									"( \r\n" + 
									"	select top 1 ha.KDPROG, ha.nmprog from referensi.vw_kodeprogram ha where ha.KDPROG=a.kodeprogram \r\n" + 
									") c where a.kodeacara=?");
							ps.setInt(1, acara.getKode());
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
							acara.setMataanggaran(mataAnggarans);
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
						
						acaras.add(acara);
						
						metadata.setCode(1);
						metadata.setMessage("OK");
					}
					response.setList(acaras);
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
	@Path("/save")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces("application/json")
	public Response simpanSkpd(
			@Context HttpHeaders headers, 
			@FormDataParam("file") final InputStream uploadedInputStream, 
			@FormDataParam("file") FormDataContentDisposition fileDetail,
			@FormDataParam("file") String file,
			@FormDataParam("skpd") FormDataBodyPart post,
			@FormDataParam("act") String act) {
		
		Map<String, Object> metadata = new HashMap<String, Object>();
		Map<String, Object> metadataobj = new HashMap<String, Object>();
		FTPClient ftpClient = null;

		if (SharedMethod.VerifyToken(headers, metadata)) {
			try {
				post.setMediaType(MediaType.APPLICATION_JSON_TYPE);
				Skpd skpd = post.getValueAs(Skpd.class);
				
				Boolean adafile = false;
				String pathFile = null;
				String host = null;
	        	Integer port = null;
	        	String user = null;
	        	String pass = null;
				String namaFile = "";
				
				if(!(uploadedInputStream==null || fileDetail==null) && file.length() > 0) {
					adafile = true;
					pathFile = "file_skpd/";
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
		        	String npp = skpd.getOffice().getKode();
		        	Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		        	namaFile = npp + "-" + timestamp.getTime() + extension;
				}
				
				Connection con = null;
				CallableStatement cs = null;
				String query = null;
				
				if(act.equalsIgnoreCase("create")) {
					try {
						query = "DECLARE @TempList skpd.skpdpegawaiAsTable;";
						for (ListPegawai item : skpd.getListpegawai()) {
							if(item.getDeleted()==0)
								query += "insert into @TempList values (" + item.getNo() + ", " + item.getKode() + ", " + item.getKodepenugasan() + ");";
						}
						query += "DECLARE @TempList1 skpd.skpdmataanggaranAsTable;";
						for (MataAnggaran item : skpd.getMataanggaran()) {
							if(item.getDeleted()==0)
								query += "insert into @TempList1 values (" + item.getKode() + ", '" + item.getAkun().getKode() + "', '" + item.getProgram().getKode() + "');";
						}
						query += "exec skpd.sp_insertskpd ?,?,?,?,?,?,?,?,?,?,?,?,?,?,@TempList,@TempList1;";
						
						con = new Koneksi().getConnection();
						cs = con.prepareCall(query);
						cs.setString(1, skpd.getOffice().getKode());
						cs.setInt(2, skpd.getAcara().getKode());
						cs.setDate(3, skpd.getTglmulai());
						cs.setDate(4, skpd.getTglselesai());
						cs.setString(5, skpd.getDeskripsi());
						cs.setString(6, skpd.getKeperluan());
						cs.setString(7, skpd.getAkun().getKode());
						cs.setString(8, skpd.getProgram().getKode());
						cs.setInt(9, skpd.getJeniskendaraan().getKode());
						if(adafile)
							cs.setString(10, namaFile);
						else
							cs.setNull(10, java.sql.Types.VARCHAR);
						cs.setInt(11, skpd.getUseract());
						cs.setInt(12, skpd.getPembuat().getKode());
						cs.setInt(13, skpd.getTujuan());
						cs.setInt(14, skpd.getJeniskegiatan());
						cs.executeUpdate();
						
						metadata.put("code", 1);
						metadata.put("message", "Simpan berhasil.");
						
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
						query = "DECLARE @TempList skpd.skpdpegawaiAsTable;";
						for (ListPegawai item : skpd.getListpegawai()) {
							if(item.getDeleted()==0)
								query += "insert into @TempList values (" + item.getNo() + ", " + item.getKode() + ", " + item.getKodepenugasan() + ");";
						}
						query += "DECLARE @TempList1 skpd.skpdmataanggaranAsTable;";
						for (MataAnggaran item : skpd.getMataanggaran()) {
							if(item.getDeleted()==0)
								query += "insert into @TempList1 values (" + item.getKode() + ", '" + item.getAkun().getKode() + "', '" + item.getProgram().getKode() + "');";
						}
						query += "exec skpd.sp_updateskpd ?,?,?,?,?,?,?,?,?,?,?,?,?,?,@TempList,@TempList1,?;";
						
						con = new Koneksi().getConnection();
						cs = con.prepareCall(query);
						cs.setString(1, skpd.getOffice().getKode());
						cs.setInt(2, skpd.getAcara().getKode());
						cs.setDate(3, skpd.getTglmulai());
						cs.setDate(4, skpd.getTglselesai());
						cs.setString(5, skpd.getDeskripsi());
						cs.setString(6, skpd.getKeperluan());
						cs.setString(7, skpd.getAkun().getKode());
						cs.setString(8, skpd.getProgram().getKode());
						cs.setInt(9, skpd.getJeniskendaraan().getKode());
						if(adafile)
							cs.setString(10, namaFile);
						else
							cs.setNull(10, java.sql.Types.VARCHAR);
						cs.setInt(11, skpd.getUseract());
						cs.setInt(12, skpd.getPembuat().getKode());
						cs.setInt(13, skpd.getTujuan());
						cs.setInt(14, skpd.getJeniskegiatan());
						cs.setInt(15, skpd.getKode());
						cs.executeUpdate();
						
						metadata.put("code", 1);
						metadata.put("message", "Update berhasil.");
						
						if(adafile) {
							try {
								ftpClient = new FTPClient();
								ftpClient.connect(host,port);
					        	ftpClient.login(user, pass);
					        	ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
					        	
					        	if(!skpd.getLampiran().isEmpty()) {
					        		String deletedFile = skpd.getLampiran();
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
	@Path("/acara/{act}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response simpanAcara(
			@Context HttpHeaders headers,
			@PathParam("act") String act,
			String data) {
		
		Map<String, Object> metadata = new HashMap<String, Object>();
		Map<String, Object> metadataobj = new HashMap<String, Object>();

		if (SharedMethod.VerifyToken(headers, metadata)) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				Acara acara = mapper.readValue(data, Acara.class);
				
				Connection con = null;
				CallableStatement cs = null;
				String query = null;
				
				if(act.equalsIgnoreCase("create")) {
					try {
						query = "DECLARE @TempList1 skpd.skpdmataanggaranAsTable;";
						for (MataAnggaran item : acara.getMataanggaran()) {
							if(item.getDeleted()==0)
								query += "insert into @TempList1 values (" + item.getKode() + ", '" + item.getAkun().getKode() + "', '" + item.getProgram().getKode() + "');";
						}
						query += "exec skpd.sp_insertacara ?,?,?,?,?,?,?,?,?,?,?,?,@TempList1;";
						
						con = new Koneksi().getConnection();
						cs = con.prepareCall(query);
						cs.setString(1, acara.getOffice().getKode());
						cs.setInt(2, acara.getTujuan());
						cs.setInt(3, acara.getJeniskegiatan());
						cs.setString(4, acara.getPic().getKode());
						cs.setString(5, acara.getNama());
						if(acara.getJeniskegiatan() == 3 || acara.getJeniskegiatan() == 4) {
							cs.setString(6, acara.getOfficetujuan().getKode());
							cs.setNull(7, java.sql.Types.VARCHAR);
						} else {
							cs.setNull(6, java.sql.Types.VARCHAR);
							cs.setString(7, acara.getDati2tujuan().getKode());
						}						
						cs.setString(8, acara.getTempat());
						cs.setDate(9, acara.getTglmulai());
						cs.setDate(10, acara.getTglselesai());
						cs.setTime(11, acara.getJam());
						cs.setInt(12, acara.getCreatedby());
						cs.executeUpdate();
						
						metadata.put("code", 1);
						metadata.put("message", "Simpan berhasil.");
						
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
/*				else if(act.equalsIgnoreCase("update")) {
					try {
						query = "DECLARE @TempList skpd.skpdpegawaiAsTable;";
						for (ListPegawai item : skpd.getListpegawai()) {
							if(item.getDeleted()==0)
								query += "insert into @TempList values (" + item.getNo() + ", " + item.getKode() + ", " + item.getKodepenugasan() + ");";
						}
						query += "DECLARE @TempList1 skpd.skpdmataanggaranAsTable;";
						for (MataAnggaran item : skpd.getMataanggaran()) {
							if(item.getDeleted()==0)
								query += "insert into @TempList1 values (" + item.getKode() + ", '" + item.getAkun().getKode() + "', '" + item.getProgram().getKode() + "');";
						}
						query += "exec skpd.sp_updateskpd ?,?,?,?,?,?,?,?,?,?,?,?,?,?,@TempList,@TempList1,?;";
						
						con = new Koneksi().getConnection();
						cs = con.prepareCall(query);
						cs.setString(1, skpd.getOffice().getKode());
						cs.setInt(2, skpd.getAcara().getKode());
						cs.setDate(3, skpd.getTglmulai());
						cs.setDate(4, skpd.getTglselesai());
						cs.setString(5, skpd.getDeskripsi());
						cs.setString(6, skpd.getKeperluan());
						cs.setString(7, skpd.getAkun().getKode());
						cs.setString(8, skpd.getProgram().getKode());
						cs.setInt(9, skpd.getJeniskendaraan().getKode());
						if(adafile)
							cs.setString(10, namaFile);
						else
							cs.setNull(10, java.sql.Types.VARCHAR);
						cs.setInt(11, skpd.getUseract());
						cs.setInt(12, skpd.getPembuat().getKode());
						cs.setInt(13, skpd.getTujuan());
						cs.setInt(14, skpd.getJeniskegiatan());
						cs.setInt(15, skpd.getKode());
						cs.executeUpdate();
						
						metadata.put("code", 1);
						metadata.put("message", "Update berhasil.");
						
						if(adafile) {
							try {
								ftpClient = new FTPClient();
								ftpClient.connect(host,port);
					        	ftpClient.login(user, pass);
					        	ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
					        	
					        	if(!skpd.getLampiran().isEmpty()) {
					        		String deletedFile = skpd.getLampiran();
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
				}*/
				
			}
			catch (SQLException e) {
				metadata.put("code", 0);
				metadata.put("message", "Simpan gagal.");
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
	@Path("/savereport/{jenis}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces("application/json")
	public Response simpanLaporan(
			@Context HttpHeaders headers,
			@PathParam("jenis") Integer jenis,
			
			@FormDataParam("file_lampiran_kegiatan_skpd") final InputStream uploadedInputStream1, 
			@FormDataParam("file_lampiran_kegiatan_skpd") FormDataContentDisposition fileDetail1,
			@FormDataParam("file_lampiran_kegiatan_skpd") String file1,
			
			@FormDataParam("file_lampiran_kegiatan_notulen") final InputStream uploadedInputStream2, 
			@FormDataParam("file_lampiran_kegiatan_notulen") FormDataContentDisposition fileDetail2,
			@FormDataParam("file_lampiran_kegiatan_notulen") String file2,
			
			@FormDataParam("file_lampiran_kegiatan_foto") final InputStream uploadedInputStream3, 
			@FormDataParam("file_lampiran_kegiatan_foto") FormDataContentDisposition fileDetail3,
			@FormDataParam("file_lampiran_kegiatan_foto") String file3,
			
			@FormDataParam("file_lampiran_catatan_revisi_peserta") final InputStream uploadedInputStream4, 
			@FormDataParam("file_lampiran_catatan_revisi_peserta") FormDataContentDisposition fileDetail4,
			@FormDataParam("file_lampiran_catatan_revisi_peserta") String file4,
			
			@FormDataParam("file_lampiran_catatan_revisi_kehadiran") final InputStream uploadedInputStream5, 
			@FormDataParam("file_lampiran_catatan_revisi_kehadiran") FormDataContentDisposition fileDetail5,
			@FormDataParam("file_lampiran_catatan_revisi_kehadiran") String file5,
			
			@FormDataParam("file_lampiran_catatan_foto") final InputStream uploadedInputStream6, 
			@FormDataParam("file_lampiran_catatan_foto") FormDataContentDisposition fileDetail6,
			@FormDataParam("file_lampiran_catatan_foto") String file6,
			
			@FormDataParam("skpd") FormDataBodyPart post) {
		
		Respon<Skpd> response = new Respon<Skpd>();
		Metadata metadata = new Metadata();
		Result<Skpd> result = new Result<Skpd>();

		if (SharedMethod.VerifyToken(headers, metadata)) {
			try {
				String namaFile1, namaFile2, namaFile3, namaFile4, namaFile5, namaFile6 = null;
				
				post.setMediaType(MediaType.APPLICATION_JSON_TYPE);
				Skpd skpd = post.getValueAs(Skpd.class);
				
				namaFile1 = getNamaFile(skpd.getKode(), 1, uploadedInputStream1, fileDetail1, file1);
				namaFile2 = getNamaFile(skpd.getKode(), 2, uploadedInputStream2, fileDetail2, file2);
				namaFile3 = getNamaFile(skpd.getKode(), 3, uploadedInputStream3, fileDetail3, file3);
				namaFile4 = getNamaFile(skpd.getKode(), 4, uploadedInputStream4, fileDetail4, file4);
				namaFile5 = getNamaFile(skpd.getKode(), 5, uploadedInputStream5, fileDetail5, file5);
				namaFile6 = getNamaFile(skpd.getKode(), 6, uploadedInputStream6, fileDetail6, file6);
				
				Connection con = null;
				CallableStatement cs = null;
				String query = null;
				
				try {
					query = "exec skpd.sp_insertskpdlaporan ?,?,?,?,?,?,?,?,?,?,?,?;";
					
					con = new Koneksi().getConnection();
					cs = con.prepareCall(query);
					cs.setInt(1, skpd.getKode());
					cs.setInt(2, skpd.getLaporan_useract());
					cs.setString(3, skpd.getLaporan_catatan_ringkas());
					cs.setString(4, skpd.getLaporan_tindak_lanjut());
					cs.setString(5, skpd.getLaporan_catatan_atasan());
					if(namaFile1!=null)
						cs.setString(6, namaFile1);
					else
						cs.setNull(6, java.sql.Types.VARCHAR);
					if(namaFile2!=null)
						cs.setString(7, namaFile2);
					else
						cs.setNull(7, java.sql.Types.VARCHAR);
					if(namaFile3!=null)
						cs.setString(8, namaFile3);
					else
						cs.setNull(8, java.sql.Types.VARCHAR);
					if(namaFile4!=null)
						cs.setString(9, namaFile4);
					else
						cs.setNull(9, java.sql.Types.VARCHAR);
					if(namaFile5!=null)
						cs.setString(10, namaFile5);
					else
						cs.setNull(10, java.sql.Types.VARCHAR);
					if(namaFile6!=null)
						cs.setString(11, namaFile6);
					else
						cs.setNull(11, java.sql.Types.VARCHAR);
					cs.setInt(12, jenis);
					cs.executeUpdate();
					
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
				
				
				Skpd skpd_respon = new Skpd();
				if(namaFile1!=null) {
					insertFTPFile(namaFile1, uploadedInputStream1);
					skpd_respon.setLampiran_kegiatan_skpd(namaFile1);
				}
				else {
					skpd_respon.setLampiran_kegiatan_skpd(skpd.getLampiran_kegiatan_skpd());
				}
				if(namaFile2!=null) {
					insertFTPFile(namaFile2, uploadedInputStream2);
					skpd_respon.setLampiran_kegiatan_notulen(namaFile2);
				}
				else {
					skpd_respon.setLampiran_kegiatan_notulen(skpd.getLampiran_kegiatan_notulen());
				}
				if(namaFile3!=null) {
					insertFTPFile(namaFile3, uploadedInputStream3);
					skpd_respon.setLampiran_kegiatan_foto(namaFile3);
				}
				else {
					skpd_respon.setLampiran_kegiatan_foto(skpd.getLampiran_kegiatan_foto());
				}
				if(namaFile4!=null) {
					insertFTPFile(namaFile4, uploadedInputStream4);
					skpd_respon.setLampiran_catatan_revisi_peserta(namaFile4);
				}
				else {
					skpd_respon.setLampiran_catatan_revisi_peserta(skpd.getLampiran_catatan_revisi_peserta());
				}
				if(namaFile5!=null) {
					insertFTPFile(namaFile5, uploadedInputStream5);
					skpd_respon.setLampiran_catatan_revisi_kehadiran(namaFile5);
				}
				else {
					skpd_respon.setLampiran_catatan_revisi_kehadiran(skpd.getLampiran_catatan_revisi_kehadiran());
				}
				if(namaFile6!=null) {
					insertFTPFile(namaFile6, uploadedInputStream6);
					skpd_respon.setLampiran_catatan_foto(namaFile6);
				}
				else {
					skpd_respon.setLampiran_catatan_foto(skpd.getLampiran_catatan_foto());
				}
				
				metadata.setCode(1);
				metadata.setMessage("Simpan berhasil.");
				
				skpd_respon.setLaporan_catatan_ringkas(skpd.getLaporan_catatan_ringkas());
				skpd_respon.setLaporan_tindak_lanjut(skpd.getLaporan_tindak_lanjut());
				skpd_respon.setLaporan_catatan_atasan(skpd.getLaporan_catatan_atasan());
				
				response.setData(skpd_respon);
				result.setResponse(response);
				
			}
			catch (SQLException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
				e.printStackTrace();
			}
			catch (Exception e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
				e.printStackTrace();
			}
		}
		
		result.setMetadata(metadata);
		return Response.ok(result).build();
	}
	
	@POST
	@Path("/delete")
	@Produces("application/json")
	public Response hapusSkpd(@Context HttpHeaders headers, String data) {
		
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
					
					query = "exec skpd.sp_deleteskpd ?";
					con = new Koneksi().getConnection();
					
					cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
					cs.setInt(1, kode);
					cs.executeUpdate();
					
					if(!lampiran.isEmpty()) {
						FTPClient ftpClient = null;
						try {
							String pathFile = "file_skpd/";
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
	public Response ajukanSkpd(@Context HttpHeaders headers, String data) {
		
		Metadata metadata = new Metadata();
		Result<KomponenGaji> result = new Result<KomponenGaji>();
		
		Connection con = null;
		ResultSet rs = null;
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
					if(json.path("detil").isMissingNode()){
						str.append("detil").append(", ");
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
					if(json.path("kodepenugasanatasan").isMissingNode()){
						str.append("kodepenugasanatasan").append(", ");
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
					String detil = json.path("detil").asText();
					String kodejobtitleatasan = json.path("kodejobtitleatasan").asText();
					Integer kodepenugasan = json.path("kodepenugasan").asInt();
					String kodeofficeatasan = json.path("kodeofficeatasan").asText();
					String nppatasan = json.path("nppatasan").asText();
					Integer kodepenugasanatasan = json.path("kodepenugasanatasan").asInt();
					
					try {
						query = "update skpd.skpd set [status]=1, tglajukan=?, lastmodified_by=?, lastmodified_time=? where kode=?";
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
						String deskripsi = "Pengajuan SKPD";
						query = "update skpd.skpd set [status]=2, disetujuiolehatasanlangsung=? where kode=?; exec hcis.sp_insertnotifikasi ?,?,?,?,99,?,null,1,?,null,?,null,?,?";
						con = new Koneksi().getConnection();
						cs = con.prepareStatement(query);
						cs.setInt(1, kodepenugasanatasan);
						cs.setInt(2, kode);
						cs.setString(3, npp);
						cs.setString(4, nppatasan);
						cs.setString(5, deskripsi);
						cs.setString(6, detil);
						cs.setInt(7, kode);
						cs.setInt(8, useract);
						cs.setString(9, kodejobtitleatasan);
						cs.setInt(10, kodepenugasan);
						cs.setString(11, kodeofficeatasan);
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
					metadata.setMessage("Pengajuan skpd berhasil.");
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
					if(json.path("kodeskpd").isMissingNode()){
						str.append("kodeskpd").append(", ");
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
					Integer kodeskpd = json.path("kodeskpd").asInt();
					query = "exec skpd.sp_getcatatannotifikasi ?";
					con = new Koneksi().getConnection();
					
					cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
					cs.setInt(1, kodeskpd);
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
	
	@GET
	@Path("/getskpdqr/{kode}")
	@Produces("application/json")
	public Response GetSkpdForQR(@PathParam("kode") Integer kode) {

		Respon<SkpdQR> response = new Respon<SkpdQR>();
		Metadata metadata = new Metadata();
		Result<SkpdQR> result = new Result<SkpdQR>();

		Connection con = null;
		ResultSet rs = null;
		CallableStatement cs = null;
		String query = null;
		SkpdQR skpdqr = new SkpdQR();

		// if (SharedMethod.VerifyToken(headers, metadata)) {
		try {

			// Integer kodeskpd = json.path("kodeskpd").asInt();
			query = "exec skpd.sp_get_skpd_pdf ?";
			con = new Koneksi().getConnection();

			cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			cs.setInt(1, kode);
			rs = cs.executeQuery();

			// ArrayList<ListPegawai> listpegawai = new ArrayList<>();
			// ArrayList<MataAnggaran> mataanggaran = new ArrayList<>();

			metadata.setCode(0);
			metadata.setMessage("Data kosong.");
			while (rs.next()) {
				skpdqr.setKode(rs.getInt("kode"));
				skpdqr.setNomor(rs.getString("nomor"));
				skpdqr.setKeperluan(rs.getString("keperluan"));
				skpdqr.setTglmulai(rs.getString("tglmulai"));
				skpdqr.setTglselesai(rs.getString("tglselesai"));
				skpdqr.setLama(rs.getInt("lama"));
				skpdqr.setNamajeniskendaraan(rs.getString("namajeniskendaraan"));
				skpdqr.setKotaasal(rs.getString("kotaasal"));
				skpdqr.setKotatujuan(rs.getString("kotatujuan"));
				skpdqr.setPejabatberwenang(rs.getString("pejabatberwenang"));
				skpdqr.setNamapejabatberwenang(rs.getString("namapejabatberwenang"));
				skpdqr.setTempat(rs.getString("tempat"));
				skpdqr.setTglsetujui(rs.getString("tglsetujui"));

				metadata.setCode(1);
				metadata.setMessage("Ok.");
			}
			// response.setList(skpdqr);
			response.setData(skpdqr);
			result.setResponse(response);

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
		
		try {
			query = "exec skpd.sp_get_skpd_pegawai_pdf ?";
			con = new Koneksi().getConnection();

			cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			cs.setInt(1, kode);
			rs = cs.executeQuery();

			ArrayList<ListPegawaiQR> listpegawais = new ArrayList<>();

			metadata.setCode(0);
			metadata.setMessage("Data kosong.");
			while (rs.next()) {
				ListPegawaiQR listpegawai = new ListPegawaiQR();
				listpegawai.setNum(rs.getInt("num"));
				listpegawai.setNamalengkap(rs.getString("namalengkap"));
				listpegawai.setNamajobtitle(rs.getString("namajobtitle"));
				listpegawai.setNamagrade(rs.getString("namagrade"));
				listpegawai.setNamasubgrade(rs.getString("namasubgrade"));
				listpegawais.add(listpegawai);

				metadata.setCode(1);
				metadata.setMessage("Ok.");
			}
			
			skpdqr.setListpegawai(listpegawais);
			// response.setList(skpdqr);
			response.setData(skpdqr);
			result.setResponse(response);

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
		
		try {
			query = "exec skpd.sp_get_skpd_matanggaran_pdf ?";
			con = new Koneksi().getConnection();

			cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			cs.setInt(1, kode);
			rs = cs.executeQuery();
			ArrayList<MataAnggaranQR> mataanggarans = new ArrayList<>();

			metadata.setCode(0);
			metadata.setMessage("Data kosong.");
			while (rs.next()) {
				MataAnggaranQR mataanggaran = new MataAnggaranQR();
				mataanggaran.setKodprog(rs.getString("kodprog"));
				mataanggaran.setKodakun(rs.getString("kodakun"));
				mataanggaran.setNamaprogram(rs.getString("namaprogram"));
				mataanggaran.setNamaakun(rs.getString("namaakun"));
				mataanggarans.add(mataanggaran);

				metadata.setCode(1);
				metadata.setMessage("Ok.");
			}
			
			skpdqr.setMataanggaran(mataanggarans);
			// response.setList(skpdqr);
			response.setData(skpdqr);
			result.setResponse(response);

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
		// }

		result.setMetadata(metadata);
		return Response.ok(result).build();
	}
	
	@POST
	@Path("/gettujuanbypenugasan")
	@Produces("application/json")
	public Response GetTujuanByPenugasan(@Context HttpHeaders headers, String data) {
		
		Respon<Acara> response = new Respon<Acara>();
		Metadata metadata = new Metadata();
		Result<Acara> result = new Result<Acara>();
		
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
					if(json.path("kodeacara").isMissingNode()){
						str.append("kodeacara").append(", ");
						ok = false;
					}
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
					Integer kodeacara = json.path("kodeacara").asInt();
					Integer kodepenugasan = json.path("kodepenugasan").asInt();
					query = "exec skpd.sp_sinkronisasi_tujuan_acara ?, ?";
					con = new Koneksi().getConnection();
					
					cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
					cs.setInt(1, kodeacara);
					cs.setInt(2, kodepenugasan);
					rs = cs.executeQuery();
					
					metadata.setCode(0);
					metadata.setMessage("Data kosong.");
					while (rs.next()) {
						Acara acara = new Acara();
						acara.setTujuan(rs.getInt("tujuan"));
						acara.setJeniskegiatan(rs.getInt("jeniskegiatan"));
						
						response.setData(acara);
						
						metadata.setCode(1);
						metadata.setMessage("OK");
					}
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
	
	private String getNamaFile(Integer kode, Integer ke, final InputStream uploadedInputStream, FormDataContentDisposition fileDetail, String file) {
		
		String namaFile = null;
		
		if(!(uploadedInputStream==null || fileDetail==null) && file.length() > 0) {
			namaFile = fileDetail.getFileName();
        	StringTokenizer st = new StringTokenizer(namaFile, ".");
        	String extension = ""; 
        	while(st.hasMoreTokens()) {
        		extension = "."+st.nextToken();
        	}
        	Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        	namaFile = kode + "." + ke + "_" + timestamp.getTime() + extension;
		}
		
		//System.out.println("namafile="+namaFile);
		return namaFile;
	}
	
	private Boolean insertFTPFile(String namaFile, final InputStream uploadedInputStream) {
		FTPClient ftpClient = null;
		Boolean upload = false;
		
		String pathFile = "file_skpd_laporan/";
		String host = null;
    	Integer port = null;
    	String user = null;
    	String pass = null;
		
		try {
			host = context.getInitParameter("ftp-host");
        	port = Integer.parseInt(context.getInitParameter("ftp-port"));
        	user = context.getInitParameter("ftp-user");
        	pass = context.getInitParameter("ftp-pass");
			
			ftpClient = new FTPClient();
			ftpClient.connect(host,port);
        	ftpClient.login(user, pass);
        	ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        	upload = ftpClient.storeFile(pathFile + namaFile, uploadedInputStream);
        	ftpClient.logout();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if(ftpClient.isConnected())
				try {
					ftpClient.disconnect();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
		return upload;
	}
	
}