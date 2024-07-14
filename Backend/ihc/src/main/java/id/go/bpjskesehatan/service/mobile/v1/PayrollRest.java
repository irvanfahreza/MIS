package id.go.bpjskesehatan.service.mobile.v1;

import java.io.File;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import id.go.bpjskesehatan.service.v2.payroll.entitas.ListPayroll;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

@Path("/mobile/v1/payroll")
public class PayrollRest {
	
    @Context
    private ServletContext context;
    
    @GET
    @Path("/list/start/{start}/limit/{limit}/npp/{npp}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response ListGrid(
			@Context HttpHeaders headers,
			@PathParam("start") Integer start, 
			@PathParam("limit") Integer limit, 
			@PathParam("npp") String npp) {
		
		Respon<ListPayroll> response = new Respon<ListPayroll>();
		Metadata metadata = new Metadata();
		Result<ListPayroll> result = new Result<ListPayroll>();
		
		Connection con = null;
		ResultSet rs = null;
		CallableStatement cs = null;
		String order = null;
		String query = null;
		Boolean ok = true;

		//if (AuthMobile.VerifyToken(headers, metadata)) {
		if (true) {
			try {
				if(ok) {
					query = "exec payroll.sp_get_list_payroll ?, ?, ?, ?, ?";
					con = new Koneksi().getConnection();
					cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
					cs.setInt(1, start);
					cs.setInt(2, limit);
					cs.setInt(3, 0);
					cs.setString(4, "tahun desc, bulan desc");
					cs.setString(5, String.format("npp = \'%s\' and isdone = 1", npp));
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
	@Path("/cetak/{token}/{npp}/{tahun}/{bulan}")
	@Produces("application/pdf")
	public Response cetakPayroll(
			@Context HttpHeaders headers, 
			@PathParam("token") String token, 
			@PathParam("npp") String npp, 
			@PathParam("tahun") Integer tahun, 
			@PathParam("bulan") Integer bulan) {
		Result<Object> result = new Result<Object>();
		Metadata metadata = new Metadata();
		
		//if (AuthMobile.VerifyTokenValue(token, metadata)) {
		if(true) {
			ResponseBuilder response;
			Connection con = null;
			PreparedStatement ps =null;
			ResultSet rs = null;
			String sumber = "";
			String filename = "payroll";
			
			try {
				String ihc_baseurl = context.getInitParameter("ihc.baseurl");
				
				filename = "SLIP";				
				sumber = "/IHC-Report/SlipPayroll.jasper";
				
				String path = "/tmp/";
				con = new Koneksi().getConnection();
				/*ps = con.prepareStatement("select concat(format(tgllahir,'yyyyMMdd'),npp) as pass from karyawan.pegawai where npp=?");
				ps.setString(1, npp);
				rs = ps.executeQuery();
				String pass = "123456";
				if(rs.next()) {
					pass = rs.getString("pass");
				}*/
				
				HashMap hm = new HashMap();
				hm.put("npp", npp);
				hm.put("bulan", bulan);
				hm.put("tahun", tahun);
				hm.put("baseurl", ihc_baseurl);
				
				File report_file = new File(sumber);
	            JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(report_file.getPath());
	            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, hm, con);
	            jasperPrint.setProperty("net.sf.jasperreports.export.pdf.permissions.allowed", "PRINTING");
	            //jasperPrint.setProperty("net.sf.jasperreports.export.pdf.user.password", pass);
	            JasperExportManager.exportReportToPdfFile(jasperPrint,path + filename + "_" + npp + "_" + bulan + "_" + tahun + ".pdf");
			    
			    File file = new File(path + filename + "_" + npp +"_" + bulan + "_" + tahun + ".pdf");
			    response = Response.ok((Object) file);
	        	response.header("Content-Disposition", "inline; filename=" + filename+"_"+npp+"_" + bulan + "_" + tahun +".pdf");
				return response.build();
			} catch (Exception e) {
				e.printStackTrace();
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
			
	        response = Response.noContent();
			return response.build();
		}
		result.setMetadata(metadata);
		return Response.ok(result).build();
	}
}
