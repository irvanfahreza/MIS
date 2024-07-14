package id.go.bpjskesehatan.service.mobile.v1;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

import org.apache.commons.net.ftp.FTPClient;

import id.go.bpjskesehatan.database.Koneksi;
import id.go.bpjskesehatan.entitas.GenericEntitas;
import id.go.bpjskesehatan.entitas.Metadata;
import id.go.bpjskesehatan.entitas.Respon;
import id.go.bpjskesehatan.entitas.Result;
import id.go.bpjskesehatan.entitas.karyawan.Penugasan;
import id.go.bpjskesehatan.entitas.organisasi.Grade;
import id.go.bpjskesehatan.entitas.organisasi.Jabatan;
import id.go.bpjskesehatan.entitas.organisasi.JobTitle;
import id.go.bpjskesehatan.entitas.organisasi.Office;
import id.go.bpjskesehatan.entitas.organisasi.SubGrade;
import id.go.bpjskesehatan.entitas.organisasi.UnitKerja;

@Path("/mobile/v1/karyawan")
public class KaryawanRest {
	
    @Context
    private ServletContext context;
	
	@GET
	@Path("/karirjabatan/npp/{npp}/start/{start}/limit/{limit}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response karirJabatan(
			@Context HttpHeaders headers, 
			@PathParam("npp") String npp,
			@PathParam("start") Integer start,
			@PathParam("limit") Integer limit
			) {
		
		Respon<Penugasan> response = new Respon<Penugasan>();
		Metadata metadata = new Metadata();
		Result<Penugasan> result = new Result<Penugasan>();
		
		Connection con = null;
		ResultSet rs = null;
		CallableStatement cs = null;
		String query = null;

		//if (AuthMobile.VerifyToken(headers, metadata)) {
		if (true) {
			try {
				query = "exec karyawan.sp_listpenugasanall ?,?,?,?,?,?,?";
				con = new Koneksi().getConnection();
				cs = con.prepareCall(query);
				cs.setInt(1, start);
				cs.setInt(2, limit);
				cs.setInt(3, 0);
				cs.setNull(4, java.sql.Types.VARCHAR);
				cs.setString(5, String.format("npp = \'%s\'", npp));
				cs.setNull(6, java.sql.Types.VARCHAR);
				cs.setNull(7, java.sql.Types.VARCHAR);
				rs = cs.executeQuery();
				
				metadata.setCode(0);
				metadata.setMessage("Data tidak ditemukan");
				List<Penugasan> penugasans = new ArrayList<>();
				while (rs.next()) {
					Penugasan penugasan = new Penugasan();
					penugasan.setKode(rs.getInt("kode"));
					penugasan.setTanggalsk(rs.getDate("tanggalsk"));
					penugasan.setLampiran(rs.getString("lampiran"));
					penugasan.setTanggalmulai(rs.getDate("tanggalmulai"));
					penugasan.setTmtjabatan(rs.getDate("tmtjabatan"));
					penugasan.setRow_status(rs.getShort("row_status"));
					
					GenericEntitas statusJabatan = new GenericEntitas();
					statusJabatan.setNama(rs.getString("namastatusjabatan"));
					penugasan.setStatusjabatan(statusJabatan);
					
					GenericEntitas jenissk = new GenericEntitas();
					jenissk.setNama(rs.getString("namajenissk"));
					penugasan.setJenissk(jenissk);
					
					GenericEntitas grade = new GenericEntitas();
					grade.setNama(rs.getString("namagrade"));
					penugasan.setGrade(grade);
					
					GenericEntitas subGrade = new GenericEntitas();
					subGrade.setNama(rs.getString("namasubgrade"));
					penugasan.setSubgrade(subGrade);
					
					Jabatan jabatan = new Jabatan();
					jabatan.setKode(rs.getString("kodehirarkijabatan"));
					jabatan.setNama(rs.getString("namajabatan"));
					UnitKerja unitkerja = new UnitKerja();
					unitkerja.setKode(rs.getString("kodeunitkerja"));
					unitkerja.setNama(rs.getString("namaunitkerja"));
					Office office = new Office();
					office.setKode(rs.getString("kodeoffice"));
					office.setNama(rs.getString("namaoffice"));
					unitkerja.setOffice(office);
					JobTitle jobtitle = new JobTitle();
					jobtitle.setKode(rs.getString("kodejobtitle"));
					jobtitle.setNama(rs.getString("namajobtitle"));
					jabatan.setJobtitle(jobtitle);
					jabatan.setUnitkerja(unitkerja);
					penugasan.setJabatan(jabatan);
					
					penugasans.add(penugasan);
					metadata.setCode(1);
					metadata.setMessage("OK");
				}
				response.setList(penugasans);
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
		}
		result.setMetadata(metadata);
		return Response.ok(result).build();
	}
	
	@GET
	@Path("/karirjabatan/sk/{filename}/{token}")
	@Produces("application/pdf")
	public Response downloadSK(@Context HttpHeaders headers, @PathParam("filename") String filename, @PathParam("token") String token) {
		ResponseBuilder response;
		OutputStream os = null;
		FTPClient client = null;
		
		Result<Object> result = new Result<Object>();
		Metadata metadata = new Metadata();
		
		//if (AuthMobile.VerifyTokenValue(token, metadata)) {
		if(true) {
			try {
	        	String path = "/tmp/";
	        	client = new FTPClient();
	        	os = new BufferedOutputStream(new FileOutputStream(path + filename));
	        	String host = context.getInitParameter("ftp-host");
				Integer port = Integer.parseInt(context.getInitParameter("ftp-port"));
				String user = context.getInitParameter("ftp-user");
				String pass = context.getInitParameter("ftp-pass");
	        	client.connect(host,port);
	        	client.login(user, pass);
	        	
	        	Boolean zxc = client.retrieveFile("file_penugasan/" + filename, os);
	        	if(zxc) {
	            	File file = new File(path + filename);
	            	response = Response.ok((Object) file);
	            	response.header("Content-Disposition", "attachment; filename=" + filename);
					return response.build();
	            }
	            else {
	            	File del = new File(path + filename);
	            	if(del.exists()) {
	            		del.delete();
	            	}
	            }
	           
	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            try {
	            	if(client != null)
	            		client.disconnect();
	            	if(os != null)
	            		os.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
		}
		
		result.setMetadata(metadata);
		return Response.ok(result).build();
	}
}
