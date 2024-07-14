package id.go.bpjskesehatan.service.v2.payroll;

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
import java.util.concurrent.TimeUnit;

import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.container.TimeoutHandler;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import id.go.bpjskesehatan.database.Koneksi;
import id.go.bpjskesehatan.entitas.KomponenGaji;
import id.go.bpjskesehatan.entitas.Metadata;
import id.go.bpjskesehatan.entitas.Respon;
import id.go.bpjskesehatan.entitas.Result;
import id.go.bpjskesehatan.entitas.cuti.SaveCuti;
import id.go.bpjskesehatan.entitas.cuti.SaveCutiTgls;
//import id.go.bpjskesehatan.entitas.cuti.SaveCutiTgls;
import id.go.bpjskesehatan.entitas.hcis.Notifikasi;
import id.go.bpjskesehatan.entitas.karyawan.Pegawai;
import id.go.bpjskesehatan.entitas.karyawan.Penugasan;
import id.go.bpjskesehatan.entitas.organisasi.Office;
import id.go.bpjskesehatan.entitas.referensi.Dati2;
import id.go.bpjskesehatan.entitas.referensi.Propinsi;
import id.go.bpjskesehatan.service.mobile.v1.AuthUser;
//import id.go.bpjskesehatan.service.v2.cuti.entitas.Kuota;
//import id.go.bpjskesehatan.service.v2.cuti.entitas.Spm;
import id.go.bpjskesehatan.service.v2.entitas.Akun;
import id.go.bpjskesehatan.service.v2.entitas.MataAnggaran;
import id.go.bpjskesehatan.service.v2.entitas.Program;
import id.go.bpjskesehatan.service.v2.payroll.entitas.ListDetilPayroll;
import id.go.bpjskesehatan.service.v2.payroll.entitas.ListPayroll;
import id.go.bpjskesehatan.service.v2.payroll.entitas.ListPayrollHeader;
import id.go.bpjskesehatan.service.v2.payroll.entitas.ProgramDonasi;
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
import id.go.bpjskesehatan.service.v2.promut.entitas.Rangkaian;
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
import id.go.bpjskesehatan.util.Utils;

@Path("/v2/payroll")
public class PayrollRest {	
	
	@Context
    private ServletContext context;

	@POST
	@Path("/grid/{page}/{row}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response ListGrid(@Context HttpHeaders headers,
			@PathParam("page") String page, @PathParam("row") String row, String data) {
		
		Respon<ListPayroll> response = new Respon<ListPayroll>();
		Metadata metadata = new Metadata();
		Result<ListPayroll> result = new Result<ListPayroll>();
		
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
					query = "exec payroll.sp_get_list_payroll ?, ?, ?, ?, ?";
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
					
					List<ListPayroll> payrolls = new ArrayList<>();
					while (rs.next()) {
						ListPayroll payroll = new ListPayroll();
						payroll.setNum(rs.getInt("RowNumber"));
						payroll.setKode(rs.getInt("kode"));
						payroll.setTahun(rs.getInt("tahun"));
						payroll.setBulan(rs.getInt("bulan"));
						payroll.setKodepenugasan(rs.getInt("kodepenugasan"));
						payroll.setGajipokok(rs.getBigDecimal("gajipokok"));
						payroll.setIk(rs.getInt("ik"));
						payroll.setPersentupres(rs.getInt("persentupres"));
						payroll.setPendapatan(rs.getBigDecimal("pendapatan"));
						payroll.setPotongan(rs.getBigDecimal("potongan"));
						payroll.setTotal(rs.getBigDecimal("total"));
						payroll.setRow_status(rs.getInt("row_status"));
						payroll.setUseract(rs.getInt("created_by"));
						payroll.setNpp(rs.getString("npp"));
						payroll.setNama(rs.getString("nama"));
						payroll.setJabatan(rs.getString("jabatan"));
						payroll.setUnitkerja(rs.getString("unitkerja"));
						payroll.setKodekantor(rs.getString("kodekantor"));
						payroll.setKantor(rs.getString("kantor"));
						payroll.setKodestatuskaryawan(rs.getInt("kodestatuskaryawan"));
						payroll.setSubgrade(rs.getString("subgrade"));
						payroll.setGrade(rs.getString("grade"));
						payroll.setJobgrade(rs.getString("jobgrade"));
						payroll.setStatusapprove(rs.getInt("statusapprove"));
						payroll.setKodejenispegawai(rs.getInt("kodejenispegawai"));
						payroll.setTahunslip(rs.getInt("tahunslip"));
						payroll.setBulanslip(rs.getInt("bulanslip"));
						ArrayList<ListDetilPayroll> pendapatans = new ArrayList<>();
						ArrayList<ListDetilPayroll> potongans = new ArrayList<>();
						ArrayList<ListDetilPayroll> pendapatanlains = new ArrayList<>();
						ArrayList<ListDetilPayroll> potonganlains = new ArrayList<>();
						try {
							ps = con.prepareStatement("EXEC payroll.sp_get_detil_payroll ?, ?, ?");
							ps.setInt(1, payroll.getKode());
							ps.setInt(2, payroll.getKodestatuskaryawan());
							ps.setInt(3, 1);
							rs3 = ps.executeQuery();
							Integer no = 0;
							while (rs3.next()) {
								no++;
								ListDetilPayroll pendapatan = new ListDetilPayroll();
								pendapatan.setKode(rs3.getInt("kode"));
								pendapatan.setKodepayroll(rs3.getInt("kodepayroll"));
								pendapatan.setKodekomponen(rs3.getInt("kodekomponen"));
								pendapatan.setKeterangan(rs3.getString("keterangan"));
								pendapatan.setNilai(rs3.getBigDecimal("nilai"));
								pendapatan.setRow_status(rs3.getInt("row_status"));
								pendapatan.setUseract(rs3.getInt("created_by"));
								pendapatan.setDisabled(rs3.getInt("disabled"));
								pendapatan.setNama(rs3.getString("nama"));
								pendapatans.add(pendapatan);
							}
							payroll.setDetilPendapatan(pendapatans);
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
							ps = con.prepareStatement("EXEC payroll.sp_get_detil_payroll_lain ?, ?, ?");
							ps.setInt(1, payroll.getKode());
							ps.setInt(2, payroll.getKodestatuskaryawan());
							ps.setInt(3, 1);
							rs3 = ps.executeQuery();
							Integer no = 0;
							while (rs3.next()) {
								no++;
								ListDetilPayroll pendapatan = new ListDetilPayroll();
								pendapatan.setKode(rs3.getInt("kode"));
								pendapatan.setKodepayroll(rs3.getInt("kodepayroll"));
								pendapatan.setKodekomponen(rs3.getInt("kodekomponen"));
								pendapatan.setKeterangan(rs3.getString("keterangan"));
								pendapatan.setNilai(rs3.getBigDecimal("nilai"));
								pendapatan.setRow_status(rs3.getInt("row_status"));
								pendapatan.setUseract(rs3.getInt("created_by"));
								pendapatan.setDisabled(rs3.getInt("disabled"));
								pendapatan.setNama(rs3.getString("nama"));
								pendapatan.setDeleted(0);
								pendapatanlains.add(pendapatan);
							}
							payroll.setDetilPendapatanLain(pendapatanlains);
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
							ps = con.prepareStatement("EXEC payroll.sp_get_detil_payroll ?, ?, ?");
							ps.setInt(1, payroll.getKode());
							ps.setInt(2, payroll.getKodestatuskaryawan());
							ps.setInt(3, 2);
							rs3 = ps.executeQuery();
							Integer no = 0;
							while (rs3.next()) {
								no++;
								ListDetilPayroll potongan = new ListDetilPayroll();
								potongan.setKode(rs3.getInt("kode"));
								potongan.setKodepayroll(rs3.getInt("kodepayroll"));
								potongan.setKodekomponen(rs3.getInt("kodekomponen"));
								potongan.setKeterangan(rs3.getString("keterangan"));
								potongan.setNilai(rs3.getBigDecimal("nilai"));
								potongan.setRow_status(rs3.getInt("row_status"));
								potongan.setUseract(rs3.getInt("created_by"));
								potongan.setDisabled(rs3.getInt("disabled"));
								potongan.setNama(rs3.getString("nama"));
								potongans.add(potongan);
							}
							payroll.setDetilPotongan(potongans);
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
							ps = con.prepareStatement("EXEC payroll.sp_get_detil_payroll_lain ?, ?, ?");
							ps.setInt(1, payroll.getKode());
							ps.setInt(2, payroll.getKodestatuskaryawan());
							ps.setInt(3, 2);
							rs3 = ps.executeQuery();
							Integer no = 0;
							while (rs3.next()) {
								no++;
								ListDetilPayroll potongan = new ListDetilPayroll();
								potongan.setKode(rs3.getInt("kode"));
								potongan.setKodepayroll(rs3.getInt("kodepayroll"));
								potongan.setKodekomponen(rs3.getInt("kodekomponen"));
								potongan.setKeterangan(rs3.getString("keterangan"));
								potongan.setNilai(rs3.getBigDecimal("nilai"));
								potongan.setRow_status(rs3.getInt("row_status"));
								potongan.setUseract(rs3.getInt("created_by"));
								potongan.setDisabled(rs3.getInt("disabled"));
								potongan.setNama(rs3.getString("nama"));
								potongan.setDeleted(0);
								potonganlains.add(potongan);
							}
							payroll.setDetilPotonganLain(potonganlains);
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
						payrolls.add(payroll);
						metadata.setCode(1);
						metadata.setMessage("OK");
					}
					response.setList(payrolls);
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
	@Path("/header/{page}/{row}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response ListHeader(@Context HttpHeaders headers,
			@PathParam("page") String page, @PathParam("row") String row, String data) {

		Respon<ListPayrollHeader> response = new Respon<ListPayrollHeader>();
		Metadata metadata = new Metadata();
		Result<ListPayrollHeader> result = new Result<ListPayrollHeader>();

		Connection con = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		CallableStatement cs = null;
		PreparedStatement ps = null;
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
					query = "exec payroll.sp_get_list_payroll_header ?, ?, ?, ?, ?";
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

					List<ListPayrollHeader> payrollheaders = new ArrayList<>();
					while (rs.next()) {
						ListPayrollHeader payrollheader = new ListPayrollHeader();
						payrollheader.setNum(rs.getInt("RowNumber"));
						payrollheader.setKode(rs.getInt("kode"));
						payrollheader.setTahun(rs.getInt("tahun"));
						payrollheader.setBulan(rs.getInt("bulan"));
						payrollheader.setKodeoffice(rs.getString("kodeoffice"));
						payrollheader.setTotalpendapatan(rs.getBigDecimal("totalpendapatan"));
						payrollheader.setTotalpotongan(rs.getBigDecimal("totalpotongan"));
						payrollheader.setGrandtotal(rs.getBigDecimal("grandtotal"));
						payrollheader.setTotalpendapatan2(rs.getString("totalpendapatan2"));
						payrollheader.setTotalpotongan2(rs.getString("totalpotongan2"));
						payrollheader.setGrandtotal2(rs.getString("grandtotal2"));
						payrollheader.setTipehitung(rs.getInt("tipehitung"));
						payrollheader.setStatusapprove(rs.getInt("statusapprove"));
						payrollheader.setIsdone(rs.getInt("isdone"));
						payrollheader.setUseract(rs.getInt("created_by"));
						payrollheader.setNama(rs.getString("nama"));
						payrollheader.setTglappr(rs.getTimestamp("tglappr"));
						payrollheader.setKodejenispegawai(rs.getInt("kodejenispegawai"));
						payrollheader.setCatatan(rs.getString("catatan"));
						
						payrollheaders.add(payrollheader);
						metadata.setCode(1);
						metadata.setMessage("OK");
					}
					response.setList(payrollheaders);
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
	public Response simpanPayroll(
			@Context HttpHeaders headers, 
			@FormDataParam("payroll") FormDataBodyPart post,
			@FormDataParam("act") String act) {
		
		Map<String, Object> metadata = new HashMap<String, Object>();
		Map<String, Object> metadataobj = new HashMap<String, Object>();

		if (SharedMethod.VerifyToken(headers, metadata)) {
			try {
				post.setMediaType(MediaType.APPLICATION_JSON_TYPE);
				ListPayroll payroll = post.getValueAs(ListPayroll.class);
				
				Connection con = null;
				CallableStatement cs = null;
				String query = null;
				
				if(act.equalsIgnoreCase("create")) {
					try {
						query = "DECLARE @TempList payroll.payrolldetilAsTable;";
						for (ListDetilPayroll item : payroll.getDetilPendapatan()) {
							if(item.getRow_status()==1) {
								query += "insert into @TempList (kodepayroll,kodekomponen,keterangan,nilai,created_by) values "
										+ "(" + item.getKodepayroll() +
										"," + item.getKodekomponen() +
										",'" + item.getKeterangan() +
										"'," + item.getNilai() +
										"," + item.getUseract() +
										");";
							}
						}
						query += "DECLARE @TempList1 payroll.payrolldetilAsTable;";
						for (ListDetilPayroll item : payroll.getDetilPotongan()) {
							if(item.getRow_status()==1) {
								query += "insert into @TempList1 (kodepayroll,kodekomponen,keterangan,nilai,created_by) values "
										+ "(" + item.getKodepayroll() +
										"," + item.getKodekomponen() +
										",'" + item.getKeterangan() +
										"'," + item.getNilai() +
										"," + item.getUseract() +
										");";
							}
						}
						query += "DECLARE @TempList2 payroll.payrolldetilAsTable;";
						for (ListDetilPayroll item : payroll.getDetilPendapatanLain()) {
							if(item.getRow_status()==1) {
								if(item.getDeleted()==0) {
									query += "insert into @TempList2 (kodepayroll,kodekomponen,keterangan,nilai,created_by) values "
											+ "(" + item.getKodepayroll() +
											"," + item.getKodekomponen() +
											",'" + item.getKeterangan() +
											"'," + item.getNilai() +
											"," + item.getUseract() +
											");";
								}
							}
						}
						query += "DECLARE @TempList3 payroll.payrolldetilAsTable;";
						for (ListDetilPayroll item : payroll.getDetilPotonganLain()) {
							if(item.getRow_status()==1) {
								if(item.getDeleted()==0) {
									query += "insert into @TempList3 (kodepayroll,kodekomponen,keterangan,nilai,created_by) values "
											+ "(" + item.getKodepayroll() +
											"," + item.getKodekomponen() +
											",'" + item.getKeterangan() +
											"'," + item.getNilai() +
											"," + item.getUseract() +
											");";
								}
							}
						}
						query += "exec payroll.sp_update_payroll ?,?,@TempList,@TempList1,@TempList2,@TempList3;";
						con = new Koneksi().getConnection();
						cs = con.prepareCall(query);
						cs.setInt(1, payroll.getUseract());
						cs.setInt(2, payroll.getKode());
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
						query = "DECLARE @TempList payroll.payrolldetilAsTable;";
						for (ListDetilPayroll item : payroll.getDetilPendapatan()) {
								query += "insert into @TempList (kodepayroll,kodekomponen,keterangan,nilai,created_by) values "
										+ "(" + item.getKodepayroll() +
										"," + item.getKodekomponen() +
										",'" + item.getKeterangan() +
										"'," + item.getNilai() +
										"," + item.getUseract() +
										");";
						}
						query += "DECLARE @TempList1 payroll.payrolldetilAsTable;";
						for (ListDetilPayroll item : payroll.getDetilPotongan()) {
								query += "insert into @TempList1 (kodepayroll,kodekomponen,keterangan,nilai,created_by) values "
										+ "(" + item.getKodepayroll() +
										"," + item.getKodekomponen() +
										",'" + item.getKeterangan() +
										"'," + item.getNilai() +
										"," + item.getUseract() +
										");";
						}
						query += "DECLARE @TempList2 payroll.payrolldetilAsTable;";
						for (ListDetilPayroll item : payroll.getDetilPendapatanLain()) {
								query += "insert into @TempList2 (kodepayroll,kodekomponen,keterangan,nilai,created_by) values "
										+ "(" + item.getKodepayroll() +
										"," + item.getKodekomponen() +
										",'" + item.getKeterangan() +
										"'," + item.getNilai() +
										"," + item.getUseract() +
										");";
						}
						query += "DECLARE @TempList3 payroll.payrolldetilAsTable;";
						for (ListDetilPayroll item : payroll.getDetilPotonganLain()) {
								query += "insert into @TempList3 (kodepayroll,kodekomponen,keterangan,nilai,created_by) values "
										+ "(" + item.getKodepayroll() +
										"," + item.getKodekomponen() +
										",'" + item.getKeterangan() +
										"'," + item.getNilai() +
										"," + item.getUseract() +
										");";
						}
						query += "exec payroll.sp_update_payroll ?,?,@TempList,@TempList1,@TempList2,@TempList3;";
						con = new Koneksi().getConnection();
						cs = con.prepareCall(query);
						cs.setInt(1, payroll.getUseract());
						cs.setInt(2, payroll.getKode());
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
	@Path("/approve/{aktor}")
	@Produces("application/json")
	public Response approvePayroll(@Context HttpHeaders headers, String data, @PathParam("aktor") final String aktor) {
		
		Metadata metadata = new Metadata();
		Result<KomponenGaji> result = new Result<KomponenGaji>();
		
		Connection con = null;
		ResultSet rs = null;
		CallableStatement cs = null;
		String query = null;
		
		if (true) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				JsonNode json = mapper.readTree(data);
				Boolean ok = true;
				Boolean ok2 = true;
				String msg = "";
				StringBuilder str = new StringBuilder();
				
				if(true){
					Integer kodeheader = json.path("kodeheader").asInt();
					Integer created_by = json.path("useract").asInt();
					Integer approve = json.path("approve").asInt();
					String catatan = json.path("catatan").asText();
					
					switch (aktor) {
					case "asdep":
						query = "exec payroll.sp_approve_asdep ?,?,?,?";
						break;
					case "depdir":
						query = "exec payroll.sp_approve_depdir ?,?,?,?";
						break;
					default:
						break;
					}
					
					con = new Koneksi().getConnection();
					
					cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
					cs.setInt(1, kodeheader);
					cs.setInt(2, created_by);
					cs.setInt(3, approve);
					cs.setString(4, catatan);
					cs.executeUpdate();
					
					metadata.setCode(1);
					metadata.setMessage("Proses berhasil.");
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
	public Response ajukanPayroll(@Context HttpHeaders headers, String data) {
		
		Metadata metadata = new Metadata();
		Result<KomponenGaji> result = new Result<KomponenGaji>();
		
		Connection con = null;
		ResultSet rs = null;
		CallableStatement cs = null;
		String query = null;
		
		if (true) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				JsonNode json = mapper.readTree(data);
				Boolean ok = true;
				Boolean ok2 = true;
				String msg = "";
				StringBuilder str = new StringBuilder();
				
				if(true){
					String kodeoffice = json.path("kodeoffice").asText();
					Integer created_by = json.path("useract").asInt();
					Integer tahun = json.path("tahun").asInt();
					Integer bulan = json.path("bulan").asInt();
					Integer kodejenispegawai = json.path("kodejenispegawai").asInt();
					String catatan = json.path("catatan").asText();
					
					
					query = "exec payroll.sp_approve_ajukan ?,?,?,?,?,?";
						
					
					con = new Koneksi().getConnection();
					
					cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
					cs.setInt(1, tahun);
					cs.setInt(2, bulan);
					cs.setString(3, kodeoffice);
					cs.setInt(4, kodejenispegawai);
					cs.setInt(5, created_by);
					cs.setString(6, catatan);
					cs.executeUpdate();
					
					metadata.setCode(1);
					metadata.setMessage("Ajukan berhasil.");
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
	@Path("/proses/{proses}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response hitungSalinDetilPayroll(
			@Suspended final AsyncResponse asyncResponse,
			@Context HttpHeaders headers,
			@PathParam("proses") final String proses,
			String data) {
		
		final Metadata metadata = new Metadata();
		final Result<KomponenGaji> result = new Result<KomponenGaji>();

		//if (SharedMethod.VerifyToken(headers, metadata)) {
		if (true) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				final JsonNode json = mapper.readTree(data);
				
				if(true){
					asyncResponse.setTimeoutHandler(new TimeoutHandler() {
						@Override
						public void handleTimeout(AsyncResponse asyncResponse) {
							metadata.setCode(1);
							metadata.setMessage("Proses dilakukan di-background karena waktu proses lebih dari 5 detik");
							asyncResponse.resume(metadata);
						}
					});
					asyncResponse.setTimeout(5, TimeUnit.SECONDS);
					Runnable runnable = null;
					Thread thread = null;
					runnable = (new Runnable() {
						private volatile boolean running = true;

						@Override
						public void run() {
							doProses();
							asyncResponse.resume(metadata);
						}

						private void doProses() {
							Connection con = null;
							CallableStatement cs = null;
							Integer created_by = json.path("useract").asInt();
							Integer bulan = json.path("bulan").asInt();
							Integer tahun = json.path("tahun").asInt();
							String kodeoffice = json.path("kodeoffice").asText();
							Integer kodejenispegawai = json.path("jenispegawai").asInt();
							Integer kodekomponen = 0;
							if(!json.path("kodekomponen").isMissingNode()) {
								kodekomponen = json.path("kodekomponen").asInt();
							}
							Integer kodepenugasan = 0;
							if(!json.path("kodepenugasan").isMissingNode()) {
								kodepenugasan = json.path("kodepenugasan").asInt();
							}
							
							try {
								String query = null;
								switch (proses) {
								case "hitung":
									query = "exec payroll.sp_hitung ?,?,?,?,?,?,?";
									break;
								case "salin":
									query = "exec payroll.sp_copy_bulan_terakhir ?,?,?,?,?";
									break;
								default:
									break;
								}
								con = new Koneksi().getConnection();
								cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
								cs.setInt(1, tahun);
								cs.setInt(2, bulan);
								cs.setInt(3, created_by);
								cs.setString(4, kodeoffice);
								cs.setInt(5, kodejenispegawai);
								if(proses.equalsIgnoreCase("hitung")) {
									if(kodekomponen!=0 && kodepenugasan==0) {
										cs.setNull(6, java.sql.Types.INTEGER);
										cs.setInt(7, kodekomponen);
									}
									else if(kodepenugasan!=0 && kodekomponen==0) {
										cs.setInt(6, kodepenugasan);
										cs.setNull(7, java.sql.Types.INTEGER);
									}
									else {
										cs.setNull(6, java.sql.Types.INTEGER);
										cs.setNull(7, java.sql.Types.INTEGER);
									}
								}
								cs.executeUpdate();
								
								metadata.setCode(1);
								metadata.setMessage(proses + " berhasil.");
							} catch (SQLException e) {
								running = false;
								metadata.setCode(0);
								metadata.setMessage(e.getMessage());
								//e.printStackTrace();
							} catch (NamingException e) {
								metadata.setCode(0);
								metadata.setMessage(e.getMessage());
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
					});

					thread = new Thread(runnable);
					thread.start();
				}
			} catch (Exception e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
				e.printStackTrace();
			}
		}
		result.setMetadata(metadata);
		return Response.ok(result).build();
	}
	
	@POST
	@Path("/grid/{npp}/{page}/{row}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response ListGridPerPegawai(@Context HttpHeaders headers,
			@PathParam("npp") String npp, @PathParam("page") String page, @PathParam("row") String row, String data) {
		
		Respon<ListPayroll> response = new Respon<ListPayroll>();
		Metadata metadata = new Metadata();
		Result<ListPayroll> result = new Result<ListPayroll>();
		
		Connection con = null;
		ResultSet rs = null;
		CallableStatement cs = null;
		String order = null;
		String filter = null;
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

						order = json.path("sort").isMissingNode() ? null
								: SharedMethod.getSortedColumn(mapper.writeValueAsString(json.path("sort")));

						filter = json.path("filter").isMissingNode() ? null
								: SharedMethod.getFilteredColumn(mapper.writeValueAsString(json.path("filter")), null);
						
					}
				}
				
				if(ok) {
					query = "exec payroll.sp_get_list_payroll ?, ?, ?, ?, ?";
					String fconcat = null;
					fconcat = String.format("npp = \'%s\'", npp);
					if(filter==null) {
						filter = fconcat;
					}
					else {
						filter = fconcat.concat(" and ").concat(filter);
					}
					
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
					
					List<ListPayroll> payrolls = new ArrayList<>();
					while (rs.next()) {
						ListPayroll payroll = new ListPayroll();
						payroll.setNum(rs.getInt("RowNumber"));
						payroll.setKode(rs.getInt("kode"));
						payroll.setTahun(rs.getInt("tahun"));
						payroll.setBulan(rs.getInt("bulan"));
						payroll.setNpp(rs.getString("npp"));
						payroll.setNama(rs.getString("nama"));
						payroll.setJabatan(rs.getString("jabatan"));
						payroll.setUnitkerja(rs.getString("unitkerja"));
						payroll.setKantor(rs.getString("kantor"));
						payroll.setKodestatuskaryawan(rs.getInt("kodestatuskaryawan"));
						payroll.setNmfile(rs.getString("nmfile"));
						payroll.setTahunslip(rs.getInt("tahunslip"));
						payroll.setBulanslip(rs.getInt("bulanslip"));
						payrolls.add(payroll);
						
						metadata.setCode(1);
						metadata.setMessage("OK");
					}
					response.setList(payrolls);
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
	
	@GET
	@Path("/referensi/rekeningdonasi")
	@Consumes("application/json")
	@Produces("application/json")
	public Response listRekeningDonasi(@Context HttpHeaders headers) {
		
		Map<String, Object> response = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> metadata = new HashMap<String, Object>();
		List<Map<String, Object>> list = null;
		
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String query = null;

		if (SharedMethod.VerifyToken(headers, metadata)) {
			try {
				
				query = "select a.kode, a.nama, b.nama as namabank, a.nomor \r\n" + 
						"from referensi.rekeningdonasi a \r\n" + 
						"inner join referensi.bank b on a.kodebank=b.kode \r\n" + 
						"where a.row_status=1";
				con = new Koneksi().getConnection();
				ps = con.prepareStatement(query);
				rs = ps.executeQuery();
				
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
		result.put("metadata", metadata);
		return Response.ok(result).build();
	}
	
	@POST
	@Path("/programdonasi/grid/{page}/{row}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response ListGridProgramDonasi(@Context HttpHeaders headers,
			@PathParam("page") String page, @PathParam("row") String row, String data) {
		
		Respon<ProgramDonasi> response = new Respon<ProgramDonasi>();
		Metadata metadata = new Metadata();
		Result<ProgramDonasi> result = new Result<ProgramDonasi>();
		
		Connection con = null;
		ResultSet rs = null;
		CallableStatement cs = null;
		String order = null;
		String filter = null;
		String query = null;
		Boolean ok = true;

		AuthUser authUser = new AuthUser();
		if (SharedMethod.VerifyToken(authUser, headers, metadata)) {
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
				
				if(ok) {
					query = "exec payroll.sp_get_list_programdonasi ?, ?, ?, ?, ?, ?";
					con = new Koneksi().getConnection();
					cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
					cs.setString(1, authUser.getNpp());
					cs.setInt(2, Integer.parseInt(page));
					cs.setInt(3, Integer.parseInt(row));
					cs.setInt(4, 1);
					cs.setString(5, order);
					cs.setString(6, filter);
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
					cs.setString(1, authUser.getNpp());
					cs.setInt(2, Integer.parseInt(page));
					cs.setInt(3, Integer.parseInt(row));
					cs.setInt(4, 0);
					cs.setString(5, order);
					cs.setString(6, filter);
					rs = cs.executeQuery();
					
					List<ProgramDonasi> programDonasis = new ArrayList<>();
					while (rs.next()) {
						ProgramDonasi dt = new ProgramDonasi();
						dt.setKode(rs.getInt("kode"));
						dt.setKodepenugasan(rs.getInt("kodepenugasan"));
						dt.setKodepaketdonasi(rs.getInt("kodepaketdonasi"));
						dt.setNamapaketdonasi(rs.getString("namapaketdonasi"));
						dt.setBulanmulai(rs.getString("bulanmulai"));
						dt.setBulanakhir(rs.getString("bulanakhir"));
						dt.setNominal(rs.getBigDecimal("nominal"));
						dt.setStatusaktif(rs.getString("statusaktif"));
						
						programDonasis.add(dt);
						metadata.setCode(1);
						metadata.setMessage("OK");
					}
					response.setList(programDonasis);
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
	@Path("/programdonasi/act")
	@Consumes("application/json")
	@Produces("application/json")
	public Response programDonasiAction(
			@Context HttpHeaders headers, 
			String post) {
		
		Map<String, Object> metadata = new HashMap<String, Object>();
		Map<String, Object> metadataobj = new HashMap<String, Object>();

		AuthUser authUser = new AuthUser();
		if (SharedMethod.VerifyToken(authUser, headers, metadata)) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				//mapper.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, true);
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				ProgramDonasi data = mapper.readValue(post, ProgramDonasi.class);
				
				Connection con = null;
				CallableStatement cs = null;
				String query = null;
				
				if(data.getAct().equalsIgnoreCase("create")) {
					try {
						query = "exec payroll.sp_insertdonasi ?,?,?,?,?,?,?";
						con = new Koneksi().getConnection();
						cs = con.prepareCall(query);
						cs.setString(1, authUser.getNpp());
						cs.setInt(2, data.getKodepenugasan());
						cs.setInt(3, data.getKodepaketdonasi());
						cs.setString(4, data.getBulanmulai());
						cs.setString(5, data.getBulanakhir());
						cs.setBigDecimal(6, data.getNominal());
						cs.setInt(7, data.getUseract());
						cs.execute();
						
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
				else if(data.getAct().equalsIgnoreCase("update")) {
					try {
						query = "exec payroll.sp_updatedonasi ?,?,?,?,?,?,?,?";
						con = new Koneksi().getConnection();
						cs = con.prepareCall(query);
						cs.setInt(1, data.getKode());
						cs.setString(2, authUser.getNpp());
						cs.setInt(3, data.getKodepenugasan());
						cs.setInt(4, data.getKodepaketdonasi());
						cs.setString(5, data.getBulanmulai());
						cs.setString(6, data.getBulanakhir());
						cs.setBigDecimal(7, data.getNominal());
						cs.setInt(8, data.getUseract());
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
				else if(data.getAct().equalsIgnoreCase("delete")) {
					try {
						query = "exec payroll.sp_deletedonasi ?,?";
						con = new Koneksi().getConnection();
						cs = con.prepareCall(query);
						cs.setInt(1, data.getKode());
						cs.setInt(2, data.getUseract());
						cs.executeUpdate();
						
						metadata.put("code", 1);
						metadata.put("message", "Hapus berhasil.");
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
				metadata.put("message", "Proses gagal.");
				e.printStackTrace();
			}
		}
		
		metadataobj.put("metadata", metadata);
		return Response.ok(metadataobj).build();
	}
	
}