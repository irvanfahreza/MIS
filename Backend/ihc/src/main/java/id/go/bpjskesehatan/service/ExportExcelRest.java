package id.go.bpjskesehatan.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import id.go.bpjskesehatan.database.Koneksi;
import id.go.bpjskesehatan.entitas.Metadata;
import id.go.bpjskesehatan.entitas.Result;
import id.go.bpjskesehatan.util.SharedMethod;

@Path("/export")
public class ExportExcelRest {
	static File tempFile = null;

	@GET
	@Path("/excel/{token}/{servicename}")
	@Produces({ MediaType.APPLICATION_OCTET_STREAM, "application/json" })
	public Response downloadFile(@Context HttpHeaders headers, @PathParam("servicename") String servicename, @PathParam("token") String token) {
		Result<Object> result = new Result<Object>();
		Metadata metadata = new Metadata();
		if (SharedMethod.VerifyToken2(token, metadata)) {
			String query = null;
			switch (servicename) {
			case "penugasan":
				query = "exec karyawan.sp_listvwpenugasan ?, ?, ?, ?, ?";
				break;
			case "karirjabatan":
				query = "exec karyawan.sp_listpenugasanall ?, ?, ?, ?, ?";
				break;
			case "jabatanrangkap":
				query = "exec karyawan.sp_listpenugasanrangkapjabatan ?, ?, ?, ?, ?";
				break;
			case "nonaktifpegawai":
				query = "exec karyawan.sp_listpenugasannonaktif ?, ?, ?, ?, ?";
				break;
			case "penugasandanjabatanrangkap":
				query = "exec karyawan.sp_listpenugasan_terakhir ?, ?, ?, ?, ?";
				break;
			default:
				return Response.status(Status.NOT_FOUND).build();
			}
			convertListToExcel(servicename, query);
			ResponseBuilder response = Response.ok((Object) tempFile);
			response.header("Content-Disposition","attachment; filename=" + servicename + " " + SharedMethod.getSQLTime().toString() + ".xlsx");
			tempFile.deleteOnExit();
			return response.build();
		}
		result.setMetadata(metadata);
		return Response.ok(result).build();
	}
	
	@GET
	@Path("/excel/{token}/cuti/{tgl1}/{tgl2}")
	@Produces({ MediaType.APPLICATION_OCTET_STREAM, "application/json" })
	public Response daftarCuti(@Context HttpHeaders headers, @PathParam("tgl1") String tgl1, @PathParam("tgl2") String tgl2, @PathParam("token") String token) {
		Result<Object> result = new Result<Object>();
		Metadata metadata = new Metadata();
		if (SharedMethod.VerifyToken2(token, metadata)) {
			
			Connection con = null;
			CallableStatement cs = null;
			ResultSet rs = null;
			
			Workbook workbook = null;
			workbook = new XSSFWorkbook();
			Sheet sheet = workbook.createSheet("Sheet1");
			
			try {
				Date tglmulai=new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(tgl1).getTime());
				Date tglselesai=new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(tgl2).getTime());
				
				String query = null;
				query = "select \r\n" + 
						"a.nomor,\r\n" + 
						"c.nama as namatipe,\r\n" + 
						"e.npp,\r\n" + 
						"e.namalengkap,\r\n" + 
						"b.tgl,\r\n" + 
						"a.telp,\r\n" + 
						"a.alasancuti,\r\n" + 
						"a.alamatcuti,\r\n" + 
						"g.nama as namajobtitle,\r\n" + 
						"h.nama as namaoffice\r\n" + 
						"from cuti.cuti a\r\n" + 
						"inner join cuti.cutitgl b on a.kode=b.kodecuti\r\n" + 
						"inner join cuti.tipe c on a.kodetipe=c.kode\r\n" + 
						"inner join karyawan.penugasan d on a.kodepenugasan=d.kode\r\n" + 
						"inner join karyawan.vw_pegawai e on d.npp=e.npp\r\n" + 
						"inner join organisasi.hirarkijabatan f on d.kodehirarkijabatan=f.kode\r\n" + 
						"inner join organisasi.jobtitle g on f.kodejobtitle=g.kode\r\n" + 
						"inner join organisasi.office h on d.kodeoffice=h.kode\r\n" + 
						"where a.statuspersetujuan=3 and b.tgl between ? and ?\r\n" + 
						"order by b.tgl, a.npp;";
				con = new Koneksi().getConnection();
				cs = con.prepareCall(query);
				cs.setDate(1, tglmulai);
				cs.setDate(2, tglselesai);
				rs = cs.executeQuery();
				
				
				ResultSetMetaData rsmd = rs.getMetaData();
				int columnsNumber = rsmd.getColumnCount();
				int rowIndex = 0;
				Row row;
				while (rs.next()) {
					row = sheet.createRow(rowIndex++);
					if (rowIndex == 1) {
						for (int i = 0; i < columnsNumber; i++)
							row.createCell(i).setCellValue(rsmd.getColumnName(i + 1));
						row = sheet.createRow(rowIndex++);
					}
					for (int i = 0; i < columnsNumber; i++)
						row.createCell(i).setCellValue(rs.getString(i + 1));
	
				}
				tempFile = File.createTempFile("temp", "hcis");
				FileOutputStream fos = null;
				fos = new FileOutputStream(tempFile);
				workbook.write(fos);
				fos.close();
				ResponseBuilder response = Response.ok((Object) tempFile);
				response.header("Content-Disposition","attachment; filename=daftar cuti " + SharedMethod.getSQLTime().toString() + ".xlsx");
				tempFile.deleteOnExit();
				return response.build();
			}
			catch (Exception e) {
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
				
				if (workbook != null) {
					try {
						workbook.close();
					} catch (IOException e) {
					}
				}
			}
		}
		result.setMetadata(metadata);
		return Response.ok(result).build();
	}

	public static void convertListToExcel(String servicename, String query) {
		Workbook workbook = null;
		workbook = new XSSFWorkbook();
		Connection con = null;
		Sheet sheet = workbook.createSheet("Sheet1");
		
		CallableStatement cs = null;
		ResultSet rs = null;
		
		Integer start = 1;
		Integer number = 1;
		try {
			con = new Koneksi().getConnection();
			cs = con.prepareCall(query);
			cs.setInt(1, 1);
			cs.setInt(2, 1);
			cs.setInt(3, 1);
			cs.setNull(4, java.sql.Types.VARCHAR);
			cs.setNull(5, java.sql.Types.VARCHAR);
			rs = cs.executeQuery();
			if(rs.next()) {
				number = rs.getInt("jumlah");
			}
			rs.close();
			
			cs.setInt(1, start);
			cs.setInt(2, number);
			cs.setInt(3, 0);
			cs.setNull(4, java.sql.Types.VARCHAR);
			cs.setNull(5, java.sql.Types.VARCHAR);
			rs = cs.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			int rowIndex = 0;
			Row row;
			while (rs.next()) {
				row = sheet.createRow(rowIndex++);
				if (rowIndex == 1) {
					for (int i = 0; i < columnsNumber; i++)
						row.createCell(i).setCellValue(rsmd.getColumnName(i + 1));
					row = sheet.createRow(rowIndex++);
				}
				for (int i = 0; i < columnsNumber; i++)
					row.createCell(i).setCellValue(rs.getString(i + 1));

			}
			tempFile = File.createTempFile("temp", "hcis");
			FileOutputStream fos = null;
			fos = new FileOutputStream(tempFile);
			workbook.write(fos);
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
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
			
			if (workbook != null) {
				try {
					workbook.close();
				} catch (IOException e) {
				}
			}
		}

	}

}
