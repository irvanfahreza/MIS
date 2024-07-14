package id.go.bpjskesehatan.service.v2.promut;

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
//import id.go.bpjskesehatan.entitas.cuti.SaveCutiTgls;
import id.go.bpjskesehatan.entitas.hcis.Notifikasi;
import id.go.bpjskesehatan.entitas.karyawan.Pegawai;
import id.go.bpjskesehatan.entitas.karyawan.Penugasan;
import id.go.bpjskesehatan.entitas.organisasi.Office;
import id.go.bpjskesehatan.entitas.referensi.Dati2;
import id.go.bpjskesehatan.entitas.referensi.Propinsi;
//import id.go.bpjskesehatan.service.v2.cuti.entitas.Kuota;
//import id.go.bpjskesehatan.service.v2.cuti.entitas.Spm;
import id.go.bpjskesehatan.service.v2.entitas.Akun;
import id.go.bpjskesehatan.service.v2.entitas.MataAnggaran;
import id.go.bpjskesehatan.service.v2.entitas.Program;
import id.go.bpjskesehatan.service.v2.promut.entitas.CciTujuan;
import id.go.bpjskesehatan.service.v2.promut.entitas.ListPendidikan;
import id.go.bpjskesehatan.service.v2.promut.entitas.ListPengalaman;
import id.go.bpjskesehatan.service.v2.promut.entitas.ListPredikat;
import id.go.bpjskesehatan.service.v2.promut.entitas.ListPromosi;
import id.go.bpjskesehatan.service.v2.promut.entitas.ListMutasi;
import id.go.bpjskesehatan.service.v2.promut.entitas.ListPegawaiPromosi;
import id.go.bpjskesehatan.service.v2.promut.entitas.ListPelanggaran;
import id.go.bpjskesehatan.service.v2.promut.entitas.ListPelatihan;
import id.go.bpjskesehatan.service.v2.promut.entitas.PenugasanLama;
import id.go.bpjskesehatan.service.v2.promut.entitas.TelaahMutasi;
import id.go.bpjskesehatan.service.v2.skpd.entitas.Acara;
import id.go.bpjskesehatan.service.v2.skpd.entitas.ListPegawai;
import id.go.bpjskesehatan.service.v2.skpd.entitas.ListPegawaiQR;
import id.go.bpjskesehatan.service.v2.skpd.entitas.MataAnggaranQR;
import id.go.bpjskesehatan.service.v2.skpd.entitas.Skpd;
import id.go.bpjskesehatan.service.v2.skpd.entitas.SkpdQR;
//import id.go.bpjskesehatan.service.v2.skpd.entitas.Skpdpegawai;
import id.go.bpjskesehatan.skpd.Jeniskendaraan;
import id.go.bpjskesehatan.util.SharedMethod;

@Path("/v2/mutasi")
public class MutasiRest {	
	
	@Context
    private ServletContext context;

	@POST
	@Path("/grid/{page}/{row}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response ListGrid(@Context HttpHeaders headers,
			@PathParam("page") String page, @PathParam("row") String row, String data) {
		
		Respon<ListPromosi> response = new Respon<ListPromosi>();
		Metadata metadata = new Metadata();
		Result<ListPromosi> result = new Result<ListPromosi>();
		
		Connection con = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		ResultSet rs3 = null;
		CallableStatement cs = null;
		PreparedStatement ps = null;
		String order = null;
		String filter = null;
		//String filter2 = null;
		//String filter3 = null;
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
						/*filter2 = json.path("data").path("filter2").isMissingNode() ? null
								: SharedMethod.getFilteredColumn(mapper.writeValueAsString(json.path("data").path("filter2")), null);
						filter3 = json.path("data").path("filter3").isMissingNode() ? null
								: SharedMethod.getFilteredColumn(mapper.writeValueAsString(json.path("data").path("filter3")), null);
						*/
					}
				}
				
				if(ok) {
					query = "exec telaah.sp_get_list_telaah_mutasi ?, ?, ?, ?, ?";
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
					
					List<ListPromosi> promosis = new ArrayList<>();
					while (rs.next()) {
						ListPromosi promosi = new ListPromosi();
						promosi.setKode(rs.getInt("kode"));
						promosi.setNum(rs.getInt("RowNumber"));
						promosi.setKodehirarkijabatantujuan(rs.getString("kodehirarkijabatantujuan"));
						promosi.setKodeofficetujuan(rs.getString("kodeofficetujuan"));
						promosi.setKodestatusjabatantujuan(rs.getInt("kodestatusjabatantujuan"));
						promosi.setUseract(rs.getInt("created_by"));
						promosi.setKodereftelaah(rs.getInt("kodereftelaah"));
						promosi.setKoderangkaian(rs.getInt("koderangkaian"));
						promosi.setJabatantujuan(rs.getString("jabatantujuan"));
						promosi.setUnitkerjatujuan(rs.getString("unitkerjatujuan"));
						promosi.setKantortujuan(rs.getString("kantortujuan"));
						promosi.setStatusjabatantujuan(rs.getString("statusjabatantujuan"));
						promosi.setNamakelas(rs.getString("namakelas"));
						promosi.setKodejobgrade(rs.getString("kodejobgrade"));
						promosi.setIk(rs.getInt("ik"));
						promosi.setTunjdacil(rs.getBigDecimal("tunjdacil"));
						promosi.setTunjkhusus(rs.getBigDecimal("tunjkhusus"));
						try {
							ps = con.prepareStatement("EXEC telaah.sp_get_pegawai ?");
							ps.setInt(1, promosi.getKode());
							rs2 = ps.executeQuery();
							ArrayList<ListPegawaiPromosi> pegawais = new ArrayList<>();
							while (rs2.next()) {
								ListPegawaiPromosi pegawai = new ListPegawaiPromosi();
								if(rs2.getString("catdepdir") == null) {
									pegawai.setCatdepdir("");
								} if(rs2.getString("catdir") == null) {
									pegawai.setCatdir("");
								}
								pegawai.setKode(rs2.getInt("kode"));
								pegawai.setNpp(rs2.getString("npp"));
								pegawai.setKodepenugasan(rs2.getInt("kodepenugasan"));
								pegawai.setR1(rs2.getInt("r1"));
								pegawai.setR2(rs2.getInt("r2"));
								pegawai.setR3(rs2.getInt("r3"));
								pegawai.setR4(rs2.getInt("r4"));
								pegawai.setR5(rs2.getInt("r5"));
								pegawai.setR6(rs2.getInt("r6"));
								pegawai.setR7(rs2.getInt("r7"));
								pegawai.setR8(rs2.getInt("r8"));
								pegawai.setR9(rs2.getInt("r9"));
								pegawai.setR10(rs2.getInt("r10"));
								pegawai.setR11(rs2.getInt("r11"));
								pegawai.setR12(rs2.getInt("r12"));
								pegawai.setR13(rs2.getInt("r13"));
								pegawai.setRekomendasi(rs2.getInt("rekomendasi"));
								pegawai.setKesimpulan(rs2.getString("kesimpulan"));
								if(rs2.getString("catasdep") == null) {
									pegawai.setCatasdep("");
								} else {
									pegawai.setCatasdep(rs2.getString("catasdep"));
								}
								if(rs2.getString("catdepdir") == null) {
									pegawai.setCatdepdir("");
								} else {
									pegawai.setCatdepdir(rs2.getString("catdepdir"));
								}
								if(rs2.getString("catdir") == null) {
									pegawai.setCatdir("");
								} else {
									pegawai.setCatdir(rs2.getString("catdir"));
								}
								pegawai.setUseract(rs2.getInt("created_by"));
								pegawai.setKoderisikopergerakankarir(rs2.getInt("koderisikopergerakankarir"));
								pegawai.setKodetelaah(rs2.getInt("kodetelaah"));
								pegawai.setCcijabatannett(rs2.getFloat("ccijabatannett"));
								pegawai.setCcijabatantujuannett(rs2.getFloat("ccijabatantujuannett"));
								pegawai.setNama(rs2.getString("nama"));
								pegawai.setTmtjabatan(rs2.getDate("tmtjabatan"));
								pegawai.setJabatanasal(rs2.getString("jabatanasal"));
								pegawai.setUnitkerjaasal(rs2.getString("unitkerjaasal"));
								pegawai.setKantorasal(rs2.getString("kantorasal"));
								pegawai.setRisikopergerakankarir(rs2.getString("risikopergerakankarir"));	
								pegawai.setLabel(rs2.getString("label"));
								try {
									ps = con.prepareStatement("SELECT \r\n" + 
											"	b.kode,\r\n" + 
											"	b.kodepredikat,\r\n" + 
											"	b.poin,\r\n" + 
											"	c.nama AS namapredikat,\r\n" + 
											"	b.tahun,\r\n" + 
											"	b.npp \r\n" + 
											"FROM telaah.pegawaipredikat a\r\n" + 
											"INNER JOIN kinerja.predikatpegawai b ON a.kodepredikat = b.kode\r\n" + 
											"INNER JOIN kinerja.predikat c ON b.kodepredikat = c.kode\r\n" + 
											"WHERE a.kodetelaahpegawai = ? ORDER BY b.tahun DESC");
									ps.setInt(1, pegawai.getKode());
									rs3 = ps.executeQuery();
									ArrayList<ListPredikat> mutasipredikats = new ArrayList<>();
									Integer no = 0;
									while (rs3.next()) {
										no++;
										ListPredikat mutasipredikat = new ListPredikat();
										mutasipredikat.setKode(rs3.getInt("kode"));
										mutasipredikat.setKodepredikat(rs3.getString("kodepredikat"));
										mutasipredikat.setNpp(rs3.getString("npp"));
										mutasipredikat.setNamapredikat(rs3.getString("namapredikat"));
										mutasipredikat.setTahun(rs3.getInt("tahun"));
										mutasipredikat.setPoin(rs3.getFloat("poin"));
										mutasipredikats.add(mutasipredikat);
									}
									pegawai.setPredikat(mutasipredikats);
								}
								finally {
									if (rs3 != null) {
										try {
											rs3.close();
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
									ps = con.prepareStatement("EXEC telaah.sp_get_edit_rerata_predikat ?");
									ps.setInt(1, pegawai.getKode());
									rs3 = ps.executeQuery();
									while (rs3.next()) {
										pegawai.setPoin(rs3.getFloat("poin"));
									}
								}
								finally {
									if (rs3 != null) {
										try {
											rs3.close();
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
									ps = con.prepareStatement("SELECT a.*, b.deskripsi, c.nama \r\n" + 
											"FROM telaah.pegawaipelanggaran a\r\n" + 
											"INNER JOIN karyawan.infopelanggaran b ON a.pelanggaran = b.kode\r\n" + 
											"INNER JOIN referensi.sanksipelanggaran c ON b.kodesanksipelanggaran = c.kode\r\n" + 
											"WHERE a.kodetelaahpegawai = ?");
									ps.setInt(1, pegawai.getKode());
									rs3 = ps.executeQuery();
									ArrayList<ListPelanggaran> pelanggaranpredikats = new ArrayList<>();
									Integer no = 0;
									
									while (rs3.next()) {
										no++;
										ListPelanggaran pelanggaranpredikat = new ListPelanggaran();
										pelanggaranpredikat.setNo(no);
										pelanggaranpredikat.setKode(rs3.getInt("kode"));
										pelanggaranpredikat.setKodeinfopelanggaran(rs3.getInt("pelanggaran"));
										pelanggaranpredikat.setPelanggaran(rs3.getString("deskripsi"));
										pelanggaranpredikat.setNamajenispelanggaran(rs3.getString("nama"));
										pelanggaranpredikat.setNpp(rs3.getString("npp"));
										pelanggaranpredikat.setDeleted(0);
										pelanggaranpredikats.add(pelanggaranpredikat);
									}
									
									if(no==0) {
										ListPelanggaran pelanggaranpredikat = new ListPelanggaran();
										pelanggaranpredikat.setNo(1);
										pelanggaranpredikat.setKode(0);
										pelanggaranpredikat.setPelanggaran("");
										pelanggaranpredikat.setNpp(pegawai.getNpp());
										pelanggaranpredikat.setDeleted(0);
										pelanggaranpredikats.add(pelanggaranpredikat);
									}
									pegawai.setPelanggaran(pelanggaranpredikats);
								}
								finally {
									if (rs3 != null) {
										try {
											rs3.close();
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
									ps = con.prepareStatement("SELECT *\r\n" + 
											"FROM telaah.pegawaipengalaman a\r\n" + 
											"WHERE a.kodetelaahpegawai = ?");
									ps.setInt(1, pegawai.getKode());
									rs3 = ps.executeQuery();
									ArrayList<ListPengalaman> pengalamanpegawais = new ArrayList<>();
									Integer no = 0;
									
									while (rs3.next()) {
										no++;
										ListPengalaman pengalamanpegawai = new ListPengalaman();
										pengalamanpegawai.setNo(no);
										pengalamanpegawai.setKode(rs3.getInt("kode"));
										pengalamanpegawai.setPengalaman(rs3.getString("pengalaman"));
										pengalamanpegawai.setNpp(rs3.getString("npp"));
										pengalamanpegawai.setDeleted(0);
										pengalamanpegawais.add(pengalamanpegawai);
									}
									
									if(no==0) {
										ListPengalaman pengalamanpegawai = new ListPengalaman();
										pengalamanpegawai.setNo(1);
										pengalamanpegawai.setKode(0);
										pengalamanpegawai.setPengalaman("");
										pengalamanpegawai.setNpp(pegawai.getNpp());
										pengalamanpegawai.setDeleted(0);
										pengalamanpegawais.add(pengalamanpegawai);
									}
									pegawai.setPengalaman(pengalamanpegawais);
								}
								finally {
									if (rs3 != null) {
										try {
											rs3.close();
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
									ps = con.prepareStatement("SELECT *\r\n" + 
											"FROM telaah.pegawaipelatihan a\r\n" + 
											"WHERE a.kodetelaahpegawai = ?");
									ps.setInt(1, pegawai.getKode());
									rs3 = ps.executeQuery();
									ArrayList<ListPelatihan> pelatihanpegawais = new ArrayList<>();
									Integer no = 0;
									
									while (rs3.next()) {
										no++;
										ListPelatihan pelatihanpegawai = new ListPelatihan();
										pelatihanpegawai.setNo(no);
										pelatihanpegawai.setKode(rs3.getInt("kode"));
										pelatihanpegawai.setPelatihan(rs3.getString("pelatihan"));
										pelatihanpegawai.setNpp(rs3.getString("npp"));
										pelatihanpegawai.setDeleted(0);
										pelatihanpegawais.add(pelatihanpegawai);
									}
									
									if(no==0) {
										ListPelatihan pelatihanpegawai = new ListPelatihan();
										pelatihanpegawai.setNo(1);
										pelatihanpegawai.setKode(0);
										pelatihanpegawai.setPelatihan("");
										pelatihanpegawai.setNpp(pegawai.getNpp());
										pelatihanpegawai.setDeleted(0);
										pelatihanpegawais.add(pelatihanpegawai);
									}
									pegawai.setPelatihan(pelatihanpegawais);
								}
								finally {
									if (rs3 != null) {
										try {
											rs3.close();
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
								pegawais.add(pegawai);
							}
							promosi.setPegawai(pegawais);
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
						promosis.add(promosi);
						metadata.setCode(1);
						metadata.setMessage("OK");
					}
					response.setList(promosis);
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
	@Path("/getccitujuan")
	@Produces("application/json")
	public Response GetCciJabatanTujuan(@Context HttpHeaders headers, String data) {
		
		Respon<CciTujuan> response = new Respon<CciTujuan>();
		Metadata metadata = new Metadata();
		Result<CciTujuan> result = new Result<CciTujuan>();
		
		Connection con = null;
		ResultSet rs = null;
		CallableStatement cs = null;
		String query = null;
		CciTujuan ccitujuan = new CciTujuan();
		
		if (SharedMethod.VerifyToken(headers, metadata)) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				JsonNode json = mapper.readTree(data);
				Boolean ok = true;
				Boolean ok2 = true;
				String msg = "";
				StringBuilder str = new StringBuilder();
				
				if (!data.isEmpty()) {
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
					String npp = json.path("npp").asText();
					String kodejobtitletujuan = json.path("kodejobtitletujuan").asText();
					query = "EXEC telaah.sp_get_info_cci_jabatan_tujuan ?, ?";
					con = new Koneksi().getConnection();
					
					cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
					cs.setString(1, npp);
					cs.setString(2, kodejobtitletujuan);
					rs = cs.executeQuery();
					
					metadata.setCode(0);
					metadata.setMessage("Data kosong.");
					while (rs.next()) {
						
						ccitujuan.setKodejabatan(rs.getString("kodejabatan"));
						ccitujuan.setNamajabatan(rs.getString("namajabatan"));
						ccitujuan.setTotalmodel(rs.getInt("totalmodel"));
						ccitujuan.setTotalnett(rs.getInt("totalnett"));
						ccitujuan.setTotalgross(rs.getInt("totalgross"));
						ccitujuan.setTotalnettdibawahsatu(rs.getInt("totalnettdibawahsatu"));
						ccitujuan.setTotalgrossdibawahsatu(rs.getInt("totalgrossdibawahsatu"));
						ccitujuan.setCcinett(rs.getFloat("ccinett"));
						ccitujuan.setCcigross(rs.getFloat("ccigross"));
						ccitujuan.setCcinettdibawahsatu(rs.getFloat("ccinettdibawahsatu"));
						ccitujuan.setCcigrossdibawahsatu(rs.getFloat("ccigrossdibawahsatu"));
						ccitujuan.setPoincci(rs.getInt("poincci"));
						
						response.setData(ccitujuan);
						
						metadata.setCode(1);
						metadata.setMessage("Ok.");
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
	
	@POST
	@Path("/save")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces("application/json")
	public Response simpanTelaahMutasi(
			@Context HttpHeaders headers, 
			@FormDataParam("telaah") FormDataBodyPart post,
			@FormDataParam("act") String act) {
		
		Map<String, Object> metadata = new HashMap<String, Object>();
		Map<String, Object> metadataobj = new HashMap<String, Object>();

		if (SharedMethod.VerifyToken(headers, metadata)) {
			try {
				post.setMediaType(MediaType.APPLICATION_JSON_TYPE);
				ListPromosi promosi = post.getValueAs(ListPromosi.class);
				
				Connection con = null;
				CallableStatement cs = null;
				String query = null;
				
				if(act.equalsIgnoreCase("create")) {
					try {
						query = "DECLARE @TempList telaah.telaahanpegawaiAsTable;";
						for (ListPegawaiPromosi item : promosi.getPegawai()) {
								query += "insert into @TempList (kode,npp,kodepenugasan,r1,r2,r3,r4,r5,r6,r7,r8,r9,r10,r11,rekomendasi,kesimpulan,koderisikopergerakankarir,ccijabatannett,ccijabatantujuannett) values "
										+ "(1,'" + item.getNpp() +
										"'," + item.getKodepenugasan() +
										"," + item.getR1() +
										"," + item.getR2() +
										"," + item.getR3() +
										"," + item.getR4() +
										"," + item.getR5() +
										"," + item.getR6() +
										"," + item.getR7() +
										"," + item.getR8() +
										"," + item.getR9() +
										"," + item.getR10() +
										"," + item.getR11() +
										"," + item.getRekomendasi() +
										",'" + item.getKesimpulan() +
										"'," + item.getKoderisikopergerakankarir() +
										"," + item.getCcijabatannett() +
										"," + item.getCcijabatantujuannett() +
										");";
						}
						query += "DECLARE @TempList1 telaah.pegawaipredikatAsTable;";
						for (ListPredikat item : promosi.getPredikat()) {
								query += "insert into @TempList1 values (" + item.getKode() + ",'" + item.getNpp() + "');";
						}
						query += "DECLARE @TempList2 telaah.pegawaipelanggaranAsTable;";
						for (ListPelanggaran item : promosi.getPelanggaran()) {
							if(item.getDeleted()==0)
								query += "insert into @TempList2 values ('" + item.getNpp() + "','"+item.getKodeinfopelanggaran()+"');";
						}
						query += "DECLARE @TempList3 telaah.pegawaipengalamanAsTable;";
						for (ListPengalaman item : promosi.getPengalaman()) {
							if(item.getDeleted()==0)
								query += "insert into @TempList3 values ('" + item.getNpp() + "','"+item.getPengalaman()+"');";
						}
						query += "DECLARE @TempList4 telaah.pegawaipelatihanAsTable;";
						for (ListPelatihan item : promosi.getPelatihan()) {
							if(item.getDeleted()==0)
								query += "insert into @TempList4 values ('" + item.getNpp() + "','"+item.getPelatihan()+"');";
						}
						query += "exec telaah.sp_insert_telaah ?,?,?,?,?,?,@TempList,@TempList1,@TempList2,@TempList3,@TempList4;";
						con = new Koneksi().getConnection();
						cs = con.prepareCall(query);
						cs.setInt(1, promosi.getUseract());
						cs.setString(2, promosi.getKodehirarkijabatantujuan());
						cs.setString(3, promosi.getKodeofficetujuan());
						cs.setInt(4, promosi.getKodestatusjabatantujuan());
						cs.setInt(5, promosi.getKodereftelaah());
						cs.setInt(6, promosi.getKoderangkaian());
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
				else if(act.equalsIgnoreCase("update")) {
					try {
						query = "DECLARE @TempList telaah.telaahanpegawaiAsTable;";
						for (ListPegawaiPromosi item : promosi.getPegawai()) {
								if(item.getCatasdep() == null) {
									item.setCatasdep("");
								} if(item.getCatdepdir() == null) {
									item.setCatdepdir("");
								} if(item.getCatdir() == null) {
									item.setCatdir("");
								}
								query += "insert into @TempList (kode,npp,kodepenugasan,r1,r2,r3,r4,r5,r6,r7,r8,r9,r10,r11,rekomendasi,kesimpulan,koderisikopergerakankarir,ccijabatannett,ccijabatantujuannett,catasdep,catdepdir,catdir) values "
										+ "(1,'" + item.getNpp() +
										"'," + item.getKodepenugasan() +
										"," + item.getR1() +
										"," + item.getR2() +
										"," + item.getR3() +
										"," + item.getR4() +
										"," + item.getR5() +
										"," + item.getR6() +
										"," + item.getR7() +
										"," + item.getR8() +
										"," + item.getR9() +
										"," + item.getR10() +
										"," + item.getR11() +
										"," + item.getRekomendasi() +
										",'" + item.getKesimpulan() +
										"'," + item.getKoderisikopergerakankarir() +
										"," + item.getCcijabatannett() +
										"," + item.getCcijabatantujuannett() +
										",'" + item.getCatasdep() +
										"','" + item.getCatdepdir() +
										"','" + item.getCatdir() +
										"');";
						}
						query += "DECLARE @TempList1 telaah.pegawaipredikatAsTable;";
						for (ListPredikat item : promosi.getPredikat()) {
								query += "insert into @TempList1 values (" + item.getKode() + ",'" + item.getNpp() + "');";
						}
						query += "DECLARE @TempList2 telaah.pegawaipelanggaranAsTable;";
						for (ListPelanggaran item : promosi.getPelanggaran()) {
							if(item.getDeleted()==0)
								query += "insert into @TempList2 values ('" + item.getNpp() + "','"+item.getKodeinfopelanggaran()+"');";
						}
						query += "DECLARE @TempList3 telaah.pegawaipengalamanAsTable;";
						for (ListPengalaman item : promosi.getPengalaman()) {
							if(item.getDeleted()==0)
								query += "insert into @TempList3 values ('" + item.getNpp() + "','"+item.getPengalaman()+"');";
						}
						query += "DECLARE @TempList4 telaah.pegawaipelatihanAsTable;";
						for (ListPelatihan item : promosi.getPelatihan()) {
							if(item.getDeleted()==0)
								query += "insert into @TempList4 values ('" + item.getNpp() + "','"+item.getPelatihan()+"');";
						}
						query += "exec telaah.sp_update_telaah ?,?,?,?,?,?,?,@TempList,@TempList1,@TempList2,@TempList3,@TempList4;";
						con = new Koneksi().getConnection();
						cs = con.prepareCall(query);
						cs.setInt(1, promosi.getUseract());
						cs.setString(2, promosi.getKodehirarkijabatantujuan());
						cs.setString(3, promosi.getKodeofficetujuan());
						cs.setInt(4, promosi.getKodestatusjabatantujuan());
						cs.setInt(5, promosi.getKodereftelaah());
						cs.setInt(6, promosi.getKoderangkaian());
						cs.setInt(7, promosi.getKode());
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
	public Response hapusTelaah(@Context HttpHeaders headers, String data) {
		
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
					
					query = "exec telaah.sp_delete_telaah ?";
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
	
}