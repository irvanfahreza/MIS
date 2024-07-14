package id.go.bpjskesehatan.service;

import id.go.bpjskesehatan.database.Koneksi;
import id.go.bpjskesehatan.util.MyException;
import id.go.bpjskesehatan.util.SharedMethod;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.naming.NamingException;
import javax.ws.rs.Consumes;
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

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/file")
public class UploadFileService {

	Map<String, Object> metadata = new HashMap<String, Object>();
	Map<String, Object> metadataobj = new HashMap<String, Object>();
	String kodesessi = null;

	@POST
	@Path("/upload/{tabel}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces("application/json")
	public Response uploadFile(@Context HttpHeaders headers,
			@FormDataParam("file") final InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail, @PathParam("tabel") String tabel,
			@FormDataParam("created_by") final String created_by) {

		if (SharedMethod.ServiceAuth(headers, metadata)) {
			// String uploadedFileLocation = "C://UPLOAD_HCIS/" + "hcis"
			// + SharedMethod.getTime() + ".xlsx";

			// + fileDetail.getFileName();
			String query = "";
			int[] kolom;
			// System.out.println(fileDetail.getFileName());

			// save it
			// if ("xlsx".equals((fileDetail.getFileName().substring(fileDetail
			// .getFileName().length() - 4)))) {
			switch (tabel.toLowerCase()) {
			case "jobprefix":
				kolom = new int[] { 2, 3, 4, 5 };
				// query = "insert into organisasi.jobprefix(nama, row_status,
				// created_by) values (?, ?, ?)";
				query = "exec organisasi.sp_upload_jobprefix ?, ?, ?, ?, ?";
				prosesUpload(uploadedInputStream, tabel.toLowerCase(), query, kolom, created_by);
				break;
			case "jobprefixpangkat":
				kolom = new int[] { 2, 4, 6, 7 };
				// query = "insert into
				// organisasi.jobprefixpangkat(kodejobprefix, kodegrade, level,
				// row_status, created_by) values (?, ?, ?, ?, ?)";
				query = "exec organisasi.sp_upload_jobprefixpangkat ?, ?, ?, ?, ?";
				prosesUpload(uploadedInputStream, tabel.toLowerCase(), query, kolom, created_by);
				break;
			case "functionalscope":
				kolom = new int[] { 2, 3, 4 };
				// query = "insert into organisasi.functionalscope(nama,
				// row_status, created_by) values (?, ?, ?)";
				query = "exec organisasi.sp_upload_functionalscope ?, ?, ?, ?";
				prosesUpload(uploadedInputStream, tabel.toLowerCase(), query, kolom, created_by);
				break;
			case "grade":
				kolom = new int[] { 2, 3, 4 };
				// query = "insert into organisasi.grade(nama, row_status,
				// created_by) values (?, ?, ?)";
				query = "exec organisasi.sp_upload_grade ?, ?, ?, ?";
				prosesUpload(uploadedInputStream, tabel.toLowerCase(), query, kolom, created_by);
				break;
			case "subgrade":
				kolom = new int[] { 2, 3, 4, 6, 7, 8, 9 };
				// query = "insert into organisasi.subgrade(nama, kodegrade,
				// row_status, created_by) values (?, ?, ?, ?)";
				query = "exec organisasi.sp_upload_subgrade ?, ?, ?, ?, ?, ?, ?, ?";
				prosesUpload(uploadedInputStream, tabel.toLowerCase(), query, kolom, created_by);
				break;
			case "jobgrade":
				kolom = new int[] { 2, 3, 4, 5, 6 };
				// query = "insert into organisasi.subgrade(nama, kodegrade,
				// row_status, created_by) values (?, ?, ?, ?)";
				query = "exec organisasi.sp_upload_jobgrade ?, ?, ?, ?, ?, ?";
				prosesUpload(uploadedInputStream, tabel.toLowerCase(), query, kolom, created_by);
				break;
			case "jobtitle":
				kolom = new int[] { 2, 4, 6, 7, 8, 9 };
				query = "exec organisasi.sp_upload_jobtitle ?, ?, ?, ?, ?, ?, ?";
				prosesUpload(uploadedInputStream, tabel.toLowerCase(), query, kolom, created_by);
				break;
			case "jobtitlerules":
				kolom = new int[] { 2, 3, 4, 5, 6, 7 };
				query = "exec organisasi.sp_upload_jobtitlerules ?, ?, ?, ?, ?, ?, ?";
				prosesUpload(uploadedInputStream, tabel.toLowerCase(), query, kolom, created_by);
				break;
			case "unitkerja":
				kolom = new int[] { 2, 4, 5, 6, 8, 10, 12, 13, 14 };
				// query = "insert into organisasi.unitkerja(kodeparent, nama,
				// kodehirarkiunitkerja, kodeoffice, created_by, row_status)
				// values (?, ?, ?, ?, ?, ?)";
				query = "exec organisasi.sp_upload_baganorganisasi_unitkerja ?, ?, ?, ?, ?, ?, ?, ?, ?, ?";
				prosesUpload(uploadedInputStream, tabel.toLowerCase(), query, kolom, created_by);
				break;
			case "office":
				kolom = new int[] { 2, 4, 6, 7, 8, 9, 10, 11, 12, 14, 15, 16, 17, 18 };
				query = "exec organisasi.sp_upload_office ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?";
				prosesUpload(uploadedInputStream, tabel.toLowerCase(), query, kolom, created_by);
				break;
			case "jabatan":
				kolom = new int[] { 2, 4, 6, 8, 9, 10, 11 };
				query = "exec organisasi.sp_upload_jabatan ?, ?, ?, ?, ?, ?, ?, ?";
				prosesUpload(uploadedInputStream, tabel.toLowerCase(), query, kolom, created_by);
				break;
			case "organizationchartunitkerja":
				kolom = new int[] { 2, 4, 6 };
				query = "insert into organisasi.organizationchartunitkerja(kodeorganizationchart, kodeunitkerja, row_status, created_by) values (?, ?, ?, ?)";
				prosesUpload(uploadedInputStream, tabel.toLowerCase(), query, kolom, created_by);
				break;
			case "propinsi":
				kolom = new int[] { 2, 3, 4 };
				query = "insert into referensi.propinsi(kode, nama, row_status, created_by) values (?, ?, ?, ?)";
				prosesUpload(uploadedInputStream, tabel.toLowerCase(), query, kolom, created_by);
				break;
			case "dati2":
				kolom = new int[] { 2, 4, 5, 6 };
				query = "insert into referensi.dati2(kodepropinsi, kode, nama, row_status, created_by) values (?, ?, ?, ?, ?)";
				prosesUpload(uploadedInputStream, tabel.toLowerCase(), query, kolom, created_by);
				break;
			case "kecamatan":
				kolom = new int[] { 2, 4, 5, 6 };
				query = "insert into referensi.kecamatan(kodedati2, kode, nama, row_status, created_by) values (?, ?, ?, ?, ?)";
				prosesUpload(uploadedInputStream, tabel.toLowerCase(), query, kolom, created_by);
				break;
			case "kelurahan":
				kolom = new int[] { 2, 4, 5, 6 };
				query = "insert into referensi.kelurahan(kodekecamatan, kode, nama, row_status, created_by) values (?, ?, ?, ?, ?)";
				prosesUpload(uploadedInputStream, tabel.toLowerCase(), query, kolom, created_by);
				break;
			case "kamuskompetensi":
				kolom = new int[] { 2, 3, 4, 5, 7, 8, 9 };
				// query = "insert into referensi.kelurahan(kodekecamatan, kode,
				// nama, row_status, created_by) values (?, ?, ?, ?, ?)";
				query = "exec kompetensi.sp_upload_kamuskompetensi ?,?,?,?,?,?,?,?";
				prosesUpload(uploadedInputStream, tabel.toLowerCase(), query, kolom, created_by);
				break;
			case "modelkompetensi":
				// kolom = new int[] { 2, 4, 6, 8, 9 };
				// query = "insert into referensi.kelurahan(kodekecamatan, kode,
				// nama, row_status, created_by) values (?, ?, ?, ?, ?)";
				query = "exec kompetensi.sp_upload_modelkompetensi ?,?,?,?,?,?";
				prosesUploadModelKompetensi(uploadedInputStream, query, created_by);
				break;
			/*case "modelkompetensiimportfromhcis":
				// kolom = new int[] { 2, 4, 6, 8, 9 };
				// query = "insert into referensi.kelurahan(kodekecamatan, kode,
				// nama, row_status, created_by) values (?, ?, ?, ?, ?)";
				query = "exec kompetensi.sp_upload_modelkompetensiimportfromhcis ?,?,?,?";
				prosesUploadModelKompetensiImportFromHCIS(uploadedInputStream, query, created_by);
				break;*/
			case "penugasan":
				kolom = new int[] { 2, 4, 6, 8 ,9, 11, 12, 13, 14, 16, 18, 19, 20 };
				query = "exec karyawan.sp_upload_penugasan ?,?,?,?,?,?,?,?,?,?,?,?,?,?";
				prosesUpload(uploadedInputStream, tabel.toLowerCase(), query, kolom, created_by);
				break;
			// case "kompetensiindividu":
			// asyncResponse.setTimeoutHandler(new TimeoutHandler() {
			// @Override
			// public void handleTimeout(AsyncResponse asyncResponse) {
			// metadata.put("code", 1);
			// metadata.put("message",
			// "Proses dilakukan di-background karena waktu proses lebih dari 5
			// detik");
			// asyncResponse.resume(metadata);
			// }
			// });
			// asyncResponse.setTimeout(5, TimeUnit.SECONDS);
			// Runnable runnable = null;
			// Thread thread = null;
			// runnable = (new Runnable() {
			// private volatile boolean running = true;
			//
			// @Override
			// public void run() {
			// doProses();
			// asyncResponse.resume(metadata);
			// }
			//
			// private void doProses() {
			// String query = "exec
			// kompetensi.sp_upload_kompetensiindividu_upload ?,?,?,?";
			// prosesUploadKompetensiIndividu(uploadedInputStream, query,
			// created_by, running);
			// }
			// });
			//
			// thread = new Thread(runnable);
			// thread.start();
			//
			// break;
			case "kelompokkompetensi":
				kolom = new int[] { 2, 4, 5, 6, 7 };
				query = "exec kompetensi.sp_upload_kelompokkompetensi ?,?,?,?,?,?";
				prosesUpload(uploadedInputStream, tabel.toLowerCase(), query, kolom, created_by);
				break;
			case "periodekamuskompetensi":
				kolom = new int[] { 2, 4, 6 };
				query = "exec kompetensi.sp_upload_periodekamuskompetensi ?,?,?,?";
				prosesUpload(uploadedInputStream, tabel.toLowerCase(), query, kolom, created_by);
				break;
			case "pegawai":
				kolom = new int[] { 2, 3, 4, 5, 6, 7, 9, 10, 11, 13, 14, 15, 17, 19, 20, 21, 23, 24, 25, 26, 27, 28, 30,
						32, 34, 35, 37 };
				// query = "insert into referensi.kelurahan(kodekecamatan, kode,
				// nama, row_status, created_by) values (?, ?, ?, ?, ?)";
				query = "exec karyawan.sp_upload_pegawai ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?";
				prosesUpload(uploadedInputStream, tabel.toLowerCase(), query, kolom, created_by);
				break;
			case "poinhasker":
				kolom = new int[] { 2, 4, 5, 6 };
				query = "exec kinerja.sp_upload_poinhasker ?,?,?,?,?";
				prosesUpload(uploadedInputStream, tabel.toLowerCase(), query, kolom, created_by);
				break;
			case "talentapegawai":
				kolom = new int[] { 2, 4 };
				query = "exec kinerja.sp_upload_talentapegawai ?,?,?";
				prosesUpload(uploadedInputStream, tabel.toLowerCase(), query, kolom, created_by);
				break;
			case "payrollpotkoperasi":
				kolom = new int[] { 2, 4, 5, 6 };
				query = "exec payroll.sp_upload_angsurankoperasi ?,?,?,?,?";
				prosesUpload(uploadedInputStream, tabel.toLowerCase(), query, kolom, created_by);
				break;
			case "payrollpotcarloan":
				kolom = new int[] { 2, 4, 5, 6 };
				query = "exec payroll.sp_upload_carloan ?,?,?,?,?";
				prosesUpload(uploadedInputStream, tabel.toLowerCase(), query, kolom, created_by);
				break;
			case "payrollpotkelebihanpulsa":
				kolom = new int[] { 2, 4, 5, 6 };
				query = "exec payroll.sp_upload_kelebihanpulsa ?,?,?,?,?";
				prosesUpload(uploadedInputStream, tabel.toLowerCase(), query, kolom, created_by);
				break;
			case "cutikuota":
				kolom = new int[] { 2, 4, 5 };
				query = "exec cuti.sp_upload_kuota ?,?,?,?";
				prosesUpload(uploadedInputStream, tabel.toLowerCase(), query, kolom, created_by);
				break;
			case "payrollpendlainlain":
				kolom = new int[] { 2, 4, 5, 6, 7, 8, 9, 10, 11 };
				query = "exec payroll.sp_upload_pend_lainlain ?,?,?,?,?,?,?,?,?,?,?";
				prosesUpload(uploadedInputStream, tabel.toLowerCase(), query, kolom, created_by);
				break;
			case "payrollpotlainlain":
				kolom = new int[] { 2, 4, 5, 6, 7, 8, 9, 10, 11 };
				query = "exec payroll.sp_upload_pot_lainlain ?,?,?,?,?,?,?,?,?,?,?";
				prosesUpload(uploadedInputStream, tabel.toLowerCase(), query, kolom, created_by);
				break;
			}
			// } else {
			// metadata.put("code", 2);
			// metadata.put("message", "File bukan format Excel (XLSX)");
			// }

		}
		// JSONObject json = new JSONObject();
		// json.put("metadata", new JSONObject(metadata));
		metadataobj.put("metadata", metadata);
		return Response.ok(metadataobj).build();
	}

	@POST
	@Path("/upload/kompetensiindividu")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces("application/json")
	public Response uploadFileKompetensiIndividu(@Suspended final AsyncResponse asyncResponse,
			@Context HttpHeaders headers, @FormDataParam("file") final InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail,
			@FormDataParam("created_by") final String created_by) {

		if (SharedMethod.ServiceAuth(headers, metadata)) {
			
			asyncResponse.setTimeoutHandler(new TimeoutHandler() {
				@Override
				public void handleTimeout(AsyncResponse asyncResponse) {
					metadata.put("code", 1);
					metadata.put("message", "Proses dilakukan di-background karena waktu proses lebih dari 5 detik");
					metadataobj.put("metadata", metadata);
					asyncResponse.resume(metadataobj);
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
					metadataobj.put("metadata", metadata);
					asyncResponse.resume(metadataobj);
				}

				private void doProses() {
					String query = "exec kompetensi.sp_upload_kompetensiindividu_upload ?,?,?,?";
					prosesUploadKompetensiIndividu(uploadedInputStream, query, created_by, running);
				}
			});

			thread = new Thread(runnable);
			thread.start();

		}
		// JSONObject json = new JSONObject();
		// json.put("metadata", new JSONObject(metadata));
		metadataobj.put("metadata", metadata);
		return Response.ok(metadataobj).build();
	}
	
	@POST
	@Path("/upload/modelkompetensiimportfromhcis")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces("application/json")
	public Response uploadFileModelKompetensiFromHCIS(@Suspended final AsyncResponse asyncResponse,
			@Context HttpHeaders headers, @FormDataParam("file") final InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail,
			@FormDataParam("created_by") final String created_by) {

		if (SharedMethod.ServiceAuth(headers, metadata)) {
			
			asyncResponse.setTimeoutHandler(new TimeoutHandler() {
				@Override
				public void handleTimeout(AsyncResponse asyncResponse) {
					metadata.put("code", 1);
					metadata.put("message", "Proses dilakukan di-background karena waktu proses lebih dari 5 detik");
					metadataobj.put("metadata", metadata);
					asyncResponse.resume(metadataobj);
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
					metadataobj.put("metadata", metadata);
					asyncResponse.resume(metadataobj);
				}

				private void doProses() {
					String query = "exec kompetensi.sp_upload_modelkompetensiimportfromhcis ?,?,?,?";
					prosesUploadModelKompetensiImportFromHCIS(uploadedInputStream, query, created_by, running);
				}
			});

			thread = new Thread(runnable);
			thread.start();

		}
		// JSONObject json = new JSONObject();
		// json.put("metadata", new JSONObject(metadata));
		metadataobj.put("metadata", metadata);
		return Response.ok(metadataobj).build();
	}

	@POST
	@Path("/upload/kompetensiindividu/batal")
	@Consumes("application/json")
	@Produces("application/json")
	public Response batalUploadFileKompetensiIndividu(@Context HttpHeaders headers, String data) {
		if (!data.isEmpty()) {
			ObjectMapper mapper = new ObjectMapper();
			try {
				Map<String, Object> map = new HashMap<String, Object>();
				map = mapper.readValue(data, new TypeReference<Map<String, Object>>() {
				});

				if (map.get("kodesessi") == null || map.get("created_by") == null) {
					metadata.put("code", 2);
					metadata.put("message", "Anda belum memasukan kode sessi/created_by");
				} else {
					metadata.put("code", 1);
					metadata.put("message", "Proses upload dibatalkan");
					batalProsesUpload((String) map.get("kodesessi"), (String) map.get("created_by").toString());
				}
			} catch (IOException | SQLException | NamingException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
			}
		} else {
			metadata.put("code", 2);
			metadata.put("message", "Anda belum memasukan kode sessi/created_by");
		}
		metadataobj.put("metadata", metadata);
		return Response.ok(metadataobj).build();
	}
	
	@POST
	@Path("/upload/djp")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces("application/json")
	public Response uploadFileDJP(@Context HttpHeaders headers,
			@FormDataParam("file") final InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail,
			@FormDataParam("created_by") final String created_by) {

		if (SharedMethod.ServiceAuth(headers, metadata)) {
			try {
				String query = "";
				int[] kolom;
				
				File tempFile = File.createTempFile("temp", "hcis");
				OutputStream outputStream = new FileOutputStream(tempFile);
				
				byte[] buffer = new byte[1024];
				for (int count; (count = uploadedInputStream.read(buffer)) != -1;) {
					outputStream.write(buffer, 0, count);
				}
				
				uploadedInputStream.close();
				outputStream.flush();
				outputStream.close();

				Workbook wb = null;
				
				wb = WorkbookFactory.create(tempFile);
				
				kolom = new int[] { 2, 4, 5, 6, 7, 8 };
				query = "exec djp.sp_upload_djp_general ?,?,?,?,?,?,?";
				prosesUploadDJP(wb, 0, query, kolom, created_by);
				
				kolom = new int[] { 2, 4, 5, 6, 7 };
				query = "exec djp.sp_upload_djp_kpi ?,?,?,?,?,?";
				prosesUploadDJP(wb, 1, query, kolom, created_by);
				
				kolom = new int[] { 2, 4, 6 };
				query = "exec djp.sp_upload_djp_pendidikan_formal ?,?,?,?";
				prosesUploadDJP(wb, 2, query, kolom, created_by);
				
				kolom = new int[] { 2, 4, 5 };
				query = "exec djp.sp_upload_djp_pendidikan_non_formal ?,?,?,?";
				prosesUploadDJP(wb, 3, query, kolom, created_by);
				
				kolom = new int[] { 2, 4, 6 };
				query = "exec djp.sp_upload_djp_pengalaman_kerja ?,?,?,?";
				prosesUploadDJP(wb, 4, query, kolom, created_by);
				
				if (wb != null)
					wb.close();
				if (tempFile != null)
					tempFile.delete();
				
			} catch (MyException e) {
				// TODO: handle exception
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (EncryptedDocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
		}
		
		metadataobj.put("metadata", metadata);
		return Response.ok(metadataobj).build();
	}

	private void prosesUploadModelKompetensi(InputStream uploadedInputStream, String query, String created_by) {
		Connection con = null;
		PreparedStatement ps = null;

		ResultSet rs = null;
		try {

			File tempFile = File.createTempFile("temp", "hcis");
			OutputStream outputStream = new FileOutputStream(tempFile);

			byte[] buffer = new byte[1024];
			for (int count; (count = uploadedInputStream.read(buffer)) != -1;) {
				outputStream.write(buffer, 0, count);
			}
			uploadedInputStream.close();
			outputStream.flush();
			outputStream.close();

			Workbook wb = null;
			Row row = null;
			Cell cell = null;

			int i = 0;
			int kolom = 0;
			try {
				con = new Koneksi().getConnection();
				// con.setAutoCommit(false);

				wb = WorkbookFactory.create(tempFile);
				Sheet sheet = wb.getSheetAt(0);

				List<String> listkompetensi = new ArrayList<String>();

				metadata.put("code", 1);
				metadata.put("message", "Data berhasil diupload");

				for (Iterator<Row> it = sheet.iterator(); it.hasNext();) {
					row = it.next();
					cell = row.getCell(0, MissingCellPolicy.RETURN_BLANK_AS_NULL);
					
					if (i >= 1 && cell != null) {
						if (i == 1) {
							int x = 0;
							Cell kode;
							for (Iterator<Cell> itcell = row.iterator(); itcell.hasNext();) {
								kode = itcell.next();
								if (x >= 5) {
									listkompetensi.add(kode.getStringCellValue());
								}
								x++;
							}
							kolom = listkompetensi.size();
						} else {
							for (int j = 0; j <= kolom - 1; j++) {
								Cell currentCell = row.getCell(5 + j, MissingCellPolicy.RETURN_BLANK_AS_NULL);
								Object hasil = this.getCellValue(currentCell);
								if (hasil != null && ((double) hasil) != 0.0) {
									ps = con.prepareStatement(query);
									ps.setString(1, (String) getCellValue(row.getCell(1, MissingCellPolicy.RETURN_BLANK_AS_NULL)));
									//ps.setDouble(2, (double) getCellValue(row.getCell(4, Row.RETURN_BLANK_AS_NULL)));
									ps.setInt(2, 0); // periode kompetensi yang aktif
									ps.setString(3, listkompetensi.get(j));
									ps.setDouble(4, (double) hasil);
									ps.setDouble(5, (double) getCellValue(row.getCell(3, MissingCellPolicy.RETURN_BLANK_AS_NULL)));
									ps.setInt(6, Integer.parseInt(created_by));
									ps.executeUpdate();
									ps.close();
								}
							}
						}
					}
					i++;
				}

				// con.commit();
			} catch (EncryptedDocumentException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				// e.printStackTrace();
			} catch (SQLException e) {
				metadata.put("code", 0);
				metadata.put("message",
						"Error baris ke-" + (i + 1) + ". Kode error: " + e.getErrorCode()
								+ ". Silah cek data Jobtitle: "
								+ (String) getCellValue(row.getCell(1, MissingCellPolicy.RETURN_BLANK_AS_NULL)));
				// e.printStackTrace();
			} catch (NamingException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				// e.printStackTrace();

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

				// if (koneksi != null)
				// if (koneksi.getConnection() != null) {
				// koneksi.closeConnection();
				// koneksi = null;
				// }
				if (wb != null)
					wb.close();
				if (tempFile != null)
					tempFile.delete();
			}

		} catch (

		IOException e) {
			metadata.put("code", 0);
			metadata.put("message", e.getMessage());
		}

	}
	
	/*private void prosesUploadModelKompetensiImportFromHCIS1(InputStream uploadedInputStream, String query, String created_by) {
		Connection con = null;
		PreparedStatement ps = null;

		ResultSet rs = null;
		try {

			File tempFile = File.createTempFile("temp", "hcis");
			OutputStream outputStream = new FileOutputStream(tempFile);

			byte[] buffer = new byte[1024];
			for (int count; (count = uploadedInputStream.read(buffer)) != -1;) {
				outputStream.write(buffer, 0, count);
			}
			uploadedInputStream.close();
			outputStream.flush();
			outputStream.close();

			Workbook wb = null;
			Row row = null;
			Cell cell = null;

			int i = 0;
			int kolom = 0;
			try {
				con = new Koneksi().getConnection();
				// con.setAutoCommit(false);

				wb = WorkbookFactory.create(tempFile);
				Sheet sheet = wb.getSheetAt(0);

				List<String> listkompetensi = new ArrayList<String>();

				metadata.put("code", 1);
				metadata.put("message", "Data model kompetensi berhasil diupload");
				
				
				for (Iterator<Row> it = sheet.iterator(); it.hasNext();) {
					//System.out.println("baris " + (i+1));
					row = it.next();
					cell = row.getCell(0, Row.RETURN_BLANK_AS_NULL);
					if (i >= 1 && cell != null) {
						if (i == 1) {
							int x = 1;
							Cell kode;
							for (Iterator<Cell> itcell = row.iterator(); itcell.hasNext();) {
								kode = itcell.next();
								if (x >= 7) {
									
									//System.out.println(kode.getStringCellValue());
									
									listkompetensi.add(kode.getStringCellValue());
								}
								x++;
							}
							kolom = listkompetensi.size();
						} else {
							for (int j = 0; j <= kolom - 1; j++) {
								Cell currentCell = row.getCell(7 + j, Row.RETURN_BLANK_AS_NULL);
								Object hasil = this.getCellValue(currentCell);
								if (hasil != null && ((double) hasil) != 0.0) {
									
									System.out.println((String) getCellValue(row.getCell(2, Row.RETURN_BLANK_AS_NULL)) + " | "
											+ listkompetensi.get(j) + " | "
											+ hasil + " | "
											+ (String) getCellValue(row.getCell(3, Row.RETURN_BLANK_AS_NULL)));
									
									ps = con.prepareStatement(query);
									ps.setString(1, (String) getCellValue(row.getCell(2, Row.RETURN_BLANK_AS_NULL)));
									ps.setString(2, listkompetensi.get(j));
									ps.setDouble(3, (double) hasil);
									ps.setString(4, (String) getCellValue(row.getCell(3, Row.RETURN_BLANK_AS_NULL)));
									ps.executeUpdate();
									ps.close();
								}
							}
						}
					}
					i++;
				}

				// con.commit();
			} catch (EncryptedDocumentException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				// e.printStackTrace();
			} catch (InvalidFormatException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				// e.printStackTrace();
			} catch (SQLException e) {
				metadata.put("code", 0);
				metadata.put("message",
						"Error baris ke-" + (i + 1) + ". Kode error: " + e.getErrorCode()
								+ ". Silah cek data Jobtitle: "
								+ (String) getCellValue(row.getCell(1, Row.RETURN_BLANK_AS_NULL)));
				// e.printStackTrace();
			} catch (NamingException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				// e.printStackTrace();

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

				// if (koneksi != null)
				// if (koneksi.getConnection() != null) {
				// koneksi.closeConnection();
				// koneksi = null;
				// }
				if (wb != null)
					wb.close();
				if (tempFile != null)
					tempFile.delete();
			}

		} catch (

		IOException e) {
			metadata.put("code", 0);
			metadata.put("message", e.getMessage());
		}

	}*/
	
	private void prosesUploadModelKompetensiImportFromHCIS(InputStream uploadedInputStream, String query, String created_by, boolean running) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			File tempFile = File.createTempFile("temp", "hcis");
			OutputStream outputStream = new FileOutputStream(tempFile);

			byte[] buffer = new byte[1024];
			for (int count; (count = uploadedInputStream.read(buffer)) != -1;) {
				outputStream.write(buffer, 0, count);
			}
			uploadedInputStream.close();
			outputStream.flush();
			outputStream.close();

			Workbook wb = null;
			Row row = null;
			Cell cell = null;

			int i = 0;
			int kolom = 0;
			try {
				con = new Koneksi().getConnection();
				// con.setAutoCommit(false);
				ps = con.prepareStatement("delete from kompetensi.model_upload");
				ps.executeUpdate();
				ps.close();

				wb = WorkbookFactory.create(tempFile);
				Sheet sheet = wb.getSheetAt(0);

				List<String> listkompetensi = new ArrayList<String>();
				for (Iterator<Row> it = sheet.iterator(); it.hasNext();) {
					row = it.next();
					cell = row.getCell(0, MissingCellPolicy.RETURN_BLANK_AS_NULL);
					if (i >= 1 && cell != null && running) {
						if (i == 1) {
							int x = 0;
							Cell kode;
							for (Iterator<Cell> itcell = row.iterator(); itcell.hasNext();) {
								kode = itcell.next();
								if (x >= 6) {
									listkompetensi.add(kode.getStringCellValue());
								}
								x++;
							}
							kolom = listkompetensi.size();
						} else {
							for (int j = 0; j <= kolom - 1; j++) {
								Cell currentCell = row.getCell(6 + j, MissingCellPolicy.RETURN_BLANK_AS_NULL);
								Object hasil = this.getCellValue(currentCell);
								//if (hasil != null) {
									ps = con.prepareStatement(query);
									
									ps.setString(1, (String) getCellValue(row.getCell(2, MissingCellPolicy.RETURN_BLANK_AS_NULL)));
									ps.setString(2, listkompetensi.get(j));
									if(hasil == null) {
										ps.setNull(3, java.sql.Types.DOUBLE);
									}
									else {
										if(hasil.toString().trim().isEmpty()) {
											ps.setNull(3, java.sql.Types.DOUBLE);
										}
										else {
											ps.setDouble(3, (double) hasil);
										}
									}
									ps.setString(4, (String) getCellValue(row.getCell(3, MissingCellPolicy.RETURN_BLANK_AS_NULL)));
									
									ps.executeUpdate();
									ps.close();
								//}
							}
						}
					}
					i++;
				}

				ps = con.prepareStatement("exec kompetensi.sp_proses_upload_modelkompetensiimportfromhcis ?");
				ps.setInt(1, Integer.parseInt(created_by));
				ps.executeUpdate();
				ps.close();
				metadata.put("code", 1);
				metadata.put("message", "Data berhasil diupload");

				// con.commit();
			} catch (EncryptedDocumentException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				e.printStackTrace();
			} catch (SQLException e) {
				metadata.put("code", 0);
				running = false;
				if (row != null) {
					metadata.put("message",
							"Error baris ke-" + (i + 1) + ". Kode error: " + e.getErrorCode() + ". Silah cek data : "
									+ (String) getCellValue(row.getCell(1, MissingCellPolicy.RETURN_BLANK_AS_NULL)) + ". "
									+ e.getMessage());
				}
				// SharedMethod.ValidasiError(e, metadata, i != 0 ? "Error baris
				// ke-" + i + ". " : "");
				e.printStackTrace();
			} catch (NamingException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				e.printStackTrace();
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

				// if (koneksi != null)
				// if (koneksi.getConnection() != null) {
				// koneksi.closeConnection();
				// koneksi = null;
				// }
				if (wb != null)
					wb.close();
				if (tempFile != null)
					tempFile.delete();
			}

		} catch (IOException e) {
			metadata.put("code", 0);
			metadata.put("message", e.getMessage());
		}
	}

	private void prosesUploadKompetensiIndividu(InputStream uploadedInputStream, String query, String created_by,
			boolean running) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			File tempFile = File.createTempFile("temp", "hcis");
			OutputStream outputStream = new FileOutputStream(tempFile);

			byte[] buffer = new byte[1024];
			for (int count; (count = uploadedInputStream.read(buffer)) != -1;) {
				outputStream.write(buffer, 0, count);
			}
			uploadedInputStream.close();
			outputStream.flush();
			outputStream.close();

			Workbook wb = null;
			Row row = null;
			Cell cell = null;

			int i = 0;
			int kolom = 0;
			try {
				con = new Koneksi().getConnection();
				// con.setAutoCommit(false);
				ps = con.prepareStatement("select * from kompetensi.vw_adaprosesuploadkompetensiindividu");
				rs = ps.executeQuery();
				if (rs.next()) {
					metadata.put("code", "2");
					metadata.put("message", "Upload tidak dapat diproses karena masih ada proses upload lainnya");
					throw new Exception();
				}
				rs.close();
				ps.close();

				ps = con.prepareStatement("delete from kompetensi.individu_upload");
				ps.executeUpdate();
				ps.close();

				ps = con.prepareStatement("insert into kompetensi.individu_upload_status "
						+ "(kodesessi, kodestatusupload, created_by)" + "values (newid(), ?, ?)",
						new String[] { "kode" });
				ps.setInt(1, 1);
				ps.setInt(2, Integer.parseInt(created_by));
				ps.execute();
				rs = ps.getResultSet();
				if (rs == null)
					rs = ps.getGeneratedKeys();
				String kodeupload = null;
				if (rs.next())
					kodeupload = rs.getString(1);
				rs.close();
				ps.close();
				ps = con.prepareStatement("select kodesessi from kompetensi.individu_upload_status where kode = ?");
				ps.setString(1, kodeupload);
				rs = ps.executeQuery();
				if (rs.next()) {
					kodesessi = rs.getString("kodesessi");
					metadata.put("kodesessi", kodesessi);
				}
				rs.close();
				ps.close();

				wb = WorkbookFactory.create(tempFile);
				Sheet sheet = wb.getSheetAt(0);

				List<String> listkompetensi = new ArrayList<String>();
				for (Iterator<Row> it = sheet.iterator(); it.hasNext();) {
					row = it.next();
					cell = row.getCell(0, MissingCellPolicy.RETURN_BLANK_AS_NULL);
					if (i >= 1 && cell != null && running) {
						if (i == 1) {
							int x = 0;
							Cell kode;
							for (Iterator<Cell> itcell = row.iterator(); itcell.hasNext();) {
								kode = itcell.next();
								if (x >= 4) {
									listkompetensi.add(kode.getStringCellValue());
								}
								x++;
							}
							kolom = listkompetensi.size();
						} else {
							for (int j = 0; j <= kolom - 1; j++) {
								Cell currentCell = row.getCell(4 + j, MissingCellPolicy.RETURN_BLANK_AS_NULL);
								Object hasil = this.getCellValue(currentCell);
								if (hasil != null) {
									boolean batal = false;
									ps.close();
									ps = con.prepareStatement(
											"select kode from kompetensi.vw_adabataluploadkompetensiindividu where kodesessi = ?");
									ps.setString(1, kodesessi);
									rs = ps.executeQuery();
									if (rs.next())
										batal = true;
									rs.close();
									ps.close();
									//System.out.println("batal = " + batal);
									if (!batal) {
										//System.out.println((String) getCellValue(row.getCell(1, Row.RETURN_BLANK_AS_NULL)) + " | " + listkompetensi.get(j));
										ps = con.prepareStatement(query);
										ps.setString(1,
												(String) getCellValue(row.getCell(1, MissingCellPolicy.RETURN_BLANK_AS_NULL)));
										ps.setString(2, listkompetensi.get(j));
										if (hasil instanceof Number) {
											ps.setDouble(3, (double) hasil);
										} else {
											ps.setString(3, String.valueOf(hasil));
										}
										ps.setInt(4, Integer.parseInt(created_by));
										ps.executeUpdate();
										ps.close();
									} else {
										throw new Exception();
									}
								}
							}
						}
					}
					i++;
				}

				ps = con.prepareStatement("exec kompetensi.sp_prosesuploadkompetensiindividu ?, ?");
				ps.setString(1, kodesessi);
				ps.setInt(2, Integer.parseInt(created_by));
				ps.executeUpdate();
				ps.close();
				metadata.put("code", 1);
				metadata.put("message", "Data berhasil diupload");

				// con.commit();
			} catch (EncryptedDocumentException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				// e.printStackTrace();
			} catch (InvalidFormatException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				// e.printStackTrace();
			} catch (SQLException e) {
				metadata.put("code", 0);
				running = false;
				if (row != null) {
					metadata.put("message",
							"Error baris ke-" + (i + 1) + ". Kode error: " + e.getErrorCode() + ". Silah cek data NPP: "
									+ (String) getCellValue(row.getCell(1, MissingCellPolicy.RETURN_BLANK_AS_NULL)) + ". "
									+ e.getMessage());
				}
				// SharedMethod.ValidasiError(e, metadata, i != 0 ? "Error baris
				// ke-" + i + ". " : "");
				e.printStackTrace();
			} catch (NamingException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				// e.printStackTrace();
			} catch (Exception e) {
				// e.printStackTrace();
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

				// if (koneksi != null)
				// if (koneksi.getConnection() != null) {
				// koneksi.closeConnection();
				// koneksi = null;
				// }
				if (wb != null)
					wb.close();
				if (tempFile != null)
					tempFile.delete();
			}

		} catch (IOException e) {
			metadata.put("code", 0);
			metadata.put("message", e.getMessage());
		}
	}

	// private boolean prosesDibatalkan(String kodesessi2) {
	// Connection con = null;
	// PreparedStatement ps = null;
	// ResultSet rs = null;
	// boolean batal = false;
	// try {
	// con = new Koneksi().getConnection();
	// ps = con.prepareStatement(
	// "select * from kompetensi.individu_upload_status where kodestatusupload =
	// 3 and kodesessi = ?");
	// ps.setString(1, kodesessi);
	// rs = ps.executeQuery();
	// if (rs.next())
	// batal = true;
	// } catch (SQLException | NamingException e) {
	// e.printStackTrace();
	//
	// } finally {
	//
	// if (rs != null) {
	// try {
	// rs.close();
	// } catch (SQLException e) {
	// }
	// }
	//
	// if (ps != null) {
	// try {
	// ps.close();
	// } catch (SQLException e) {
	// }
	// }
	// if (con != null) {
	// try {
	// con.close();
	// } catch (SQLException e) {
	// }
	// }
	// }
	//
	// return batal;
	// }

	private void batalProsesUpload(String kodesessi, String created_by) throws SQLException, NamingException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean batal = false;
		try {
			con = new Koneksi().getConnection();
			ps = con.prepareStatement(
					"select kode from kompetensi.vw_adabataluploadkompetensiindividu where kodesessi = ?");
			ps.setString(1, kodesessi);
			rs = ps.executeQuery();
			if (rs.next())
				batal = true;
			rs.close();
			ps.close();

			if (!batal) {
				ps = con.prepareStatement("exec kompetensi.sp_bataluploadkompetensiindividu ?, ?");
				ps.setString(1, kodesessi);
				ps.setInt(2, Integer.parseInt(created_by));
				ps.executeUpdate();
				ps.close();
				metadata.put("code", 1);
				metadata.put("message", "Proses dibatalkan");
			} else {
				metadata.put("code", 1);
				metadata.put("message", "Kode Sessi " + kodesessi + " sudah pernah dibatalkan, anda tidak perlu membatalkannya lagi");
			}
		} catch (SQLException | NamingException e) {
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

	private Object getCellValue(Cell cell) {
		Object hasil = null;
		if (cell != null) {
			if (cell.getCellType() == CellType.NUMERIC || (cell.getCellType() == CellType.FORMULA
					&& cell.getCachedFormulaResultType() == CellType.NUMERIC)) {
				if (DateUtil.isCellDateFormatted(cell)) {
					hasil = (cell.getDateCellValue().getTime());
				} else {
					hasil = cell.getNumericCellValue();
				}
			} else if (cell.getCellType() == CellType.FORMULA
					&& cell.getCachedFormulaResultType() == CellType.ERROR) {
				hasil = null;
			} else
				hasil = cell.getStringCellValue().trim();
		}
		return hasil;
	}

	// save uploaded file to new location
	private void prosesUpload(InputStream uploadedInputStream, String tabel, String query, int[] kolom,
			String created_by) {
		Connection con = null;
		PreparedStatement ps = null;

		ResultSet rs = null;
		// Koneksi koneksi = null;
		try {
			// File.createTempFile("temp", "edabulima");
			// File tempFile = new File (uploadedFileLocation);
			File tempFile = File.createTempFile("temp", "hcis");
			OutputStream outputStream = new FileOutputStream(tempFile);
			// System.out.println(tempFile.getName());

			byte[] buffer = new byte[1024];
			for (int count; (count = uploadedInputStream.read(buffer)) != -1;) {
				outputStream.write(buffer, 0, count);
			}
			uploadedInputStream.close();
			outputStream.flush();
			outputStream.close();

			Workbook wb = null;

			int i = 0;
			try {
				// koneksi = new Koneksi();
				// koneksi.BuatKoneksi();
				con = new Koneksi().getConnection();
				con.setAutoCommit(false);

				wb = WorkbookFactory.create(tempFile);
				Sheet sheet = wb.getSheetAt(0);
				Row row;
				Cell cell;
				// String flagvalidasi;
				// String keterangan;
				// int kolom = 0;
				metadata.put("code", 1);
				metadata.put("message", "Data berhasil diupload");
				
				Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			    String idtrans = ""+timestamp.getTime();
			    
				for (Iterator<Row> it = sheet.iterator(); it.hasNext();) {
					row = it.next();
					cell = row.getCell(0, MissingCellPolicy.RETURN_BLANK_AS_NULL);
					if (i >= 1 && cell != null) {
						ps = con.prepareStatement(query);
						// flagvalidasi = "";
						// keterangan = "";
						for (int j = 0; j <= kolom.length - 1; j++) {
							Cell currentCell = row.getCell(kolom[j] - 1, MissingCellPolicy.RETURN_BLANK_AS_NULL);
							// kolom = j;
							if (currentCell != null) {
								//System.out.println(row.getCell(kolom[j] - 1).getStringCellValue().trim());
								//System.out.println("kolom="+kolom[j]);

								if (currentCell.getCellType() == CellType.NUMERIC) {
									//System.out.println("numerik");
									if (DateUtil.isCellDateFormatted(currentCell)) {
										//System.out.println("date");
										ps.setDate(j + 1, new java.sql.Date(currentCell.getDateCellValue().getTime()));
									} else {
										//System.out.println("double");
										ps.setDouble(j + 1, currentCell.getNumericCellValue());
									}
								}
								else {
									//System.out.println("string");
									ps.setString(j + 1, currentCell.getStringCellValue().trim());
								}
									
							} else {
								ps.setString(j + 1, null);
							}

						}
						ps.setInt(kolom.length + 1, Integer.parseInt(created_by));
						switch (tabel) {
						case "payrollpendlainlain":
						case "payrollpotlainlain":
							ps.setString(kolom.length + 2, idtrans);
							break;
						default:
							break;
						}
						/*if(tabel.equalsIgnoreCase("unitkerja")){
							ps.execute();
						}
						else{
							ps.executeUpdate();
						}*/
						ps.execute();
						ps.close();
					}
					i++;
				}
				con.commit();
			} catch (EncryptedDocumentException e) {
				metadata.put("code", 0);
				switch (tabel.toLowerCase()) {
					case "pegawai":
						metadata.put("message", "Row data ke-"+i+" -> Data tidak valid.");
						break;
					default:
						metadata.put("message", "Row data ke-"+i+" -> "+e.getMessage());
						break;
				};
				// e.printStackTrace();
			} catch (SQLException e) {
				metadata.put("code", 0);
				switch (tabel.toLowerCase()) {
					case "pegawai":
						metadata.put("message", "Row data ke-"+i+" -> Data tidak valid.");
						break;
					default:
						metadata.put("message", "Row data ke-"+i+" -> "+e.getMessage());
						break;
				};
				// SharedMethod.ValidasiError(e, metadata, i != 0 ? "Error baris
				// ke-" + i + ". " : "");
				 e.printStackTrace();
			} catch (NamingException e) {
				metadata.put("code", 0);
				switch (tabel.toLowerCase()) {
					case "pegawai":
						metadata.put("message", "Row data ke-"+i+" -> Data tidak valid.");
						break;
					default:
						metadata.put("message", "Row data ke-"+i+" -> "+e.getMessage());
						break;
				};
				e.printStackTrace();
			} catch (Exception e) {
				metadata.put("code", 0);
				switch (tabel.toLowerCase()) {
					case "pegawai":
						metadata.put("message", "Row data ke-"+i+" -> Data tidak valid.");
						break;
					default:
						metadata.put("message", "Row data ke-"+i+" -> "+e.getMessage());
						break;
				};
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

				if (wb != null)
					wb.close();
				if (tempFile != null)
					tempFile.delete();
			}

		} catch (IOException e) {
			metadata.put("code", 0);
			metadata.put("message", e.getMessage());
		}

	}
	
	private void prosesUploadDJP(
			Workbook wb,
			Integer nosheet, 
			String query, 
			int[] kolom,
			String created_by) throws MyException {
		
			Connection con = null;
			PreparedStatement ps = null;

			ResultSet rs = null;
			int i = 0;
			try {
				con = new Koneksi().getConnection();
				con.setAutoCommit(false);
				
				Sheet sheet = wb.getSheetAt(nosheet);
				Row row;
				Cell cell;
				
				metadata.put("code", 1);
				metadata.put("message", "Data berhasil diupload");
			    
				for (Iterator<Row> it = sheet.iterator(); it.hasNext();) {
					row = it.next();
					cell = row.getCell(0, MissingCellPolicy.RETURN_BLANK_AS_NULL);
					if (i >= 1 && cell != null) {
						ps = con.prepareStatement(query);
						for (int j = 0; j <= kolom.length - 1; j++) {
							Cell currentCell = row.getCell(kolom[j] - 1, MissingCellPolicy.RETURN_BLANK_AS_NULL);
							if (currentCell != null) {
								if (currentCell.getCellType() == CellType.NUMERIC) {
									if (DateUtil.isCellDateFormatted(currentCell)) {
										ps.setDate(j + 1, new java.sql.Date(currentCell.getDateCellValue().getTime()));
									} else {
										ps.setDouble(j + 1, currentCell.getNumericCellValue());
									}
								}
								else {
									ps.setString(j + 1, currentCell.getStringCellValue().trim());
								}
									
							} else {
								ps.setString(j + 1, null);
							}

						}
						ps.setInt(kolom.length + 1, Integer.parseInt(created_by));
						ps.execute();
						ps.close();
					}
					i++;
				}
				con.commit();
			} catch (SQLException e) {
				metadata.put("code", 0);
				metadata.put("message", "[sheet "+(nosheet+1)+"] Row data ke-"+i+" -> "+e.getMessage());
				e.printStackTrace();
				throw new MyException("[sheet "+(nosheet+1)+"] Row data ke-"+i+" -> "+e.getMessage());
			} catch (Exception e) {
				metadata.put("code", 0);
				metadata.put("message", "[sheet "+(nosheet+1)+"] Row data ke-"+i+" -> "+e.getMessage());
				e.printStackTrace();
				throw new MyException("[sheet "+(nosheet+1)+"] Row data ke-"+i+" -> "+e.getMessage());
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
}