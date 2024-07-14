package id.go.bpjskesehatan.service.v2;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import id.go.bpjskesehatan.database.Koneksi;
import id.go.bpjskesehatan.entitas.Metadata;
import id.go.bpjskesehatan.entitas.Result;
import id.go.bpjskesehatan.service.v2.cuti.entitas.Spm;
import id.go.bpjskesehatan.service.v2.cuti.entitas.SpmDetil;
import id.go.bpjskesehatan.service.v2.cuti.entitas.SpmTemplate;
import id.go.bpjskesehatan.util.SharedMethod;

@Path("/v2/excel")
public class ExcelRest {
	static File tempFile = null;

	@GET
	@Path("/spm/{token}/{kode}")
	@Produces({ MediaType.APPLICATION_OCTET_STREAM, "application/json" })
	public Response SPM(@Context HttpHeaders headers, @PathParam("token") String token, @PathParam("kode") Integer kode) {
		Result<Object> result = new Result<Object>();
		Metadata metadata = new Metadata();
		if (SharedMethod.VerifyToken2(token, metadata)) {
			Connection con = null;
			ResultSet rs = null;
			PreparedStatement ps = null;
			String query = null;
			
			SpmTemplate spmTemplate = null;
			Spm spm = null;
			
			try {
				query = "select b.* "
						+ "from cuti.spm a "
						+ "inner join cuti.spmtemplate b on a.kodespmtemplate=b.kode "
						+ "where a.kode=?";
				con = new Koneksi().getConnection();
				ps = con.prepareStatement(query);
				ps.setInt(1, kode);
				rs = ps.executeQuery();
				if(rs.next()) {
					spmTemplate = new SpmTemplate();
					spmTemplate.setKepada(rs.getString("kepada"));
					spmTemplate.setDari(rs.getString("dari"));
					spmTemplate.setDari_deputi(rs.getString("dari_deputi"));
					spmTemplate.setTempat(rs.getString("tempat"));
				}
			}
			catch (Exception e) {
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
			
			try {
				query = "select a.*, aa.nama as namatipe, day(a.created_time) as hari, month(a.created_time) as bulan, year(a.created_time) as tahun "
						+ "from cuti.spm a "
						+ "inner join cuti.tipe aa on a.kodetipe=aa.kode "
						+ "where a.kode=?";
				con = new Koneksi().getConnection();
				ps = con.prepareStatement(query);
				ps.setInt(1, kode);
				rs = ps.executeQuery();
				if(rs.next()) {
					spmTemplate.setTanggal(rs.getInt("hari"));
					spmTemplate.setBulan(rs.getInt("bulan"));
					spmTemplate.setTahun(rs.getInt("tahun"));
					
					spm = new Spm();
					spm.setKode(rs.getInt("kode"));
					spm.setTotal(rs.getBigDecimal("total"));
					spm.setNamatipe(rs.getString("namatipe"));
				}
			}
			catch (Exception e) {
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

			try {
				query = "select d.npp, d.namalengkap, e.nama as namasubgrade, g.nama as namajobtitle, h.nama as namaunitkerja, "
						+ "a.bulanslip, a.gajipokok, a.tunjjabatan, b.nominaltunjanganbbm, a.tupres, a.utilitas, a.total, a.norek "
						+ "from cuti.spmdetil a "
						+ "inner join cuti.cuti b on a.kodecuti=b.kode "
						+ "inner join karyawan.penugasan c on b.kodepenugasan=c.kode "
						+ "inner join karyawan.vw_pegawai d on c.npp=d.npp "
						+ "inner join organisasi.subgrade e on c.kodesubgrade=e.kode "
						+ "inner join organisasi.hirarkijabatan f on c.kodehirarkijabatan=f.kode "
						+ "inner join organisasi.jobtitle g on f.kodejobtitle=g.kode "
						+ "inner join organisasi.unitkerja h on f.kodeunitkerja=h.kode "
						+ "where a.kodespm=?";
				con = new Koneksi().getConnection();
				ps = con.prepareStatement(query);
				ps.setInt(1, kode);
				rs = ps.executeQuery();
				ArrayList<SpmDetil> spmDetils = new ArrayList<>();
				Integer no = 0;
				while(rs.next()) {
					SpmDetil spmDetil = new SpmDetil();
					spmDetil.setNo(no+1);
					spmDetil.setBulanslip(rs.getInt("bulanslip"));
					spmDetil.setGajipokok(rs.getBigDecimal("gajipokok"));
					spmDetil.setTunjjabatan(rs.getBigDecimal("tunjjabatan"));
					spmDetil.setNominaltunjanganbbm(rs.getBigDecimal("nominaltunjanganbbm"));
					spmDetil.setTupres(rs.getBigDecimal("tupres"));
					spmDetil.setUtilitas(rs.getBigDecimal("utilitas"));
					spmDetil.setTotal(rs.getBigDecimal("total"));
					spmDetil.setNorek(rs.getString("norek"));
					spmDetil.setNpp(rs.getString("npp"));
					spmDetil.setNama(rs.getString("namalengkap"));
					spmDetil.setNamajobtitle(rs.getString("namajobtitle"));
					spmDetil.setNamaunitkerja(rs.getString("namaunitkerja"));
					spmDetil.setNamasubgrade(rs.getString("namasubgrade"));
					spmDetils.add(spmDetil);
					no++;
				}
				spm.setSpmdetil(spmDetils);
			}
			catch (Exception e) {
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
			
			String[] namabulan={"","Januari","Februari","Maret","April","Mei","Juni","Juli","Agustus","September","Oktober","Nopember","Desember"};
			
			/*=====================================================================*/
			Workbook workbook = null;
			try {
				workbook = new XSSFWorkbook();
				Sheet sheet = workbook.createSheet("Sheet1");
				
				CellStyle centerStyle = workbook.createCellStyle();
				centerStyle.setAlignment(HorizontalAlignment.CENTER);
				
				Row row;
				row = sheet.createRow(0);
				Cell cell = row.createCell(0);
				cell.setCellStyle(centerStyle);
				cell.setCellValue("REKAP PENGAJUAN TUNJANGAN");
				
				row = sheet.createRow(1);
				cell = row.createCell(0);
				cell.setCellStyle(centerStyle);
				cell.setCellValue(spm.getNamatipe().toUpperCase()+" PEGAWAI TAHUN "+spmTemplate.getTahun());
				
				row = sheet.createRow(2);
				cell = row.createCell(0);
				cell.setCellStyle(centerStyle);
				cell.setCellValue("BPJS KESEHATAN");
				
				sheet.addMergedRegion(CellRangeAddress.valueOf("A1:M1"));
				sheet.addMergedRegion(CellRangeAddress.valueOf("A2:M2"));
				sheet.addMergedRegion(CellRangeAddress.valueOf("A3:M3"));
				row = sheet.createRow(3);
				row.createCell(0).setCellValue("");
				row = sheet.createRow(4);
				row.createCell(0).setCellValue("NO");
				row.createCell(1).setCellValue("NPP");
				row.createCell(2).setCellValue("NAMA");
				row.createCell(3).setCellValue("GRADE");
				row.createCell(4).setCellValue("JABATAN");
				row.createCell(5).setCellValue("UNIT KERJA");
				row.createCell(6).setCellValue("SLIP GAJI BULAN");
				row.createCell(7).setCellValue("GAJI POKOK*IK");
				row.createCell(8).setCellValue("TUPRES*IK*HK");
				row.createCell(9).setCellValue("UTIL");
				row.createCell(10).setCellValue("TUNJAB");
				row.createCell(11).setCellValue("BBM");
				row.createCell(12).setCellValue("TUNJANGAN");
				row.createCell(13).setCellValue("NO REKENING");
				Integer i = 5;
				Integer nomor = 1;
				for (SpmDetil item : spm.getSpmdetil()) {
					row = sheet.createRow(i);
					row.createCell(0).setCellValue(nomor);
					row.createCell(1).setCellValue(item.getNpp());
					row.createCell(2).setCellValue(item.getNama());
					row.createCell(3).setCellValue(item.getNamasubgrade());
					row.createCell(4).setCellValue(item.getNamajobtitle());
					row.createCell(5).setCellValue(item.getNamaunitkerja());
					row.createCell(6).setCellValue(namabulan[item.getBulanslip()]);
					row.createCell(7).setCellValue(item.getGajipokok().doubleValue());
					row.createCell(8).setCellValue(item.getTupres().doubleValue());
					row.createCell(9).setCellValue(item.getUtilitas().doubleValue());
					row.createCell(10).setCellValue(item.getTunjjabatan().doubleValue());
					row.createCell(11).setCellValue(item.getNominaltunjanganbbm().doubleValue());
					row.createCell(12).setCellValue(item.getTotal().doubleValue());
					row.createCell(13).setCellValue(item.getNorek());
					i++;
					nomor++;
				}
				row = sheet.createRow(i++);
				cell = row.createCell(0);
				cell.setCellStyle(centerStyle);
				cell.setCellValue("TOTAL");
				sheet.addMergedRegion(CellRangeAddress.valueOf("A"+i+":L"+i));
				row.createCell(12).setCellFormula("SUM(M6:M"+(i-1)+")");
				
				row = sheet.createRow(i++);
				row.createCell(0).setCellValue("");
				
				row = sheet.createRow(i++);
				cell = row.createCell(10);
				cell.setCellStyle(centerStyle);
				cell.setCellValue(spmTemplate.getTempat()+", "+spmTemplate.getTanggal()+" "+namabulan[spmTemplate.getBulan()]+" "+spmTemplate.getTahun());
				sheet.addMergedRegion(CellRangeAddress.valueOf("K"+i+":N"+i));
				
				row = sheet.createRow(i++);
				cell = row.createCell(10);
				cell.setCellStyle(centerStyle);
				cell.setCellValue(spmTemplate.getDari());
				sheet.addMergedRegion(CellRangeAddress.valueOf("K"+i+":N"+i));
				row = sheet.createRow(i++);
				row.createCell(10).setCellValue("");
				sheet.addMergedRegion(CellRangeAddress.valueOf("K"+i+":N"+i));
				row = sheet.createRow(i++);
				row.createCell(10).setCellValue("");
				sheet.addMergedRegion(CellRangeAddress.valueOf("K"+i+":N"+i));
				row = sheet.createRow(i++);
				row.createCell(10).setCellValue("");
				sheet.addMergedRegion(CellRangeAddress.valueOf("K"+i+":N"+i));
				
				row = sheet.createRow(i++);
				cell = row.createCell(10);
				cell.setCellStyle(centerStyle);
				cell.setCellValue(spmTemplate.getDari_deputi());
				sheet.addMergedRegion(CellRangeAddress.valueOf("K"+i+":N"+i));
				
				tempFile = File.createTempFile("temp", "hcis");
				FileOutputStream fos = null;
				fos = new FileOutputStream(tempFile);
				workbook.write(fos);
			fos.close();
			} catch (Exception e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
			}
			finally {
				if (workbook != null) {
					try {
						workbook.close();
					} catch (IOException e) {
					}
				}
			}
			/*=====================================================================*/
			
			ResponseBuilder response = Response.ok((Object) tempFile);
			response.header("Content-Disposition","attachment; filename=lampiranSPM.xlsx");
			tempFile.deleteOnExit();
			return response.build();
		}
		result.setMetadata(metadata);
		return Response.ok(result).build();
	}
	
	@GET
	@Path("/laporan/skpd/{token}/{tgl1}/{tgl2}/{kodepic}/{status}")
	@Produces({ MediaType.APPLICATION_OCTET_STREAM, "application/json" })
	public Response Laporan_SKPD(@Context HttpHeaders headers, 
			@PathParam("token") String token, 
			@PathParam("tgl1") String tgl1, 
			@PathParam("tgl2") String tgl2, 
			@PathParam("kodepic") String kodepic,
			@PathParam("status") Integer status) {
		Result<Object> result = new Result<Object>();
		Metadata metadata = new Metadata();
		if (SharedMethod.VerifyToken2(token, metadata)) {
			Connection con = null;
			ResultSet rs = null;
			ResultSet rs2 = null;
			CallableStatement cs = null;
			PreparedStatement ps = null;
			String query = null;
			
			/*=====================================================================*/
			Workbook workbook = null;
			try {
				workbook = new XSSFWorkbook();
				Sheet sheet = workbook.createSheet("Sheet1");
				
				CellStyle centerStyle = workbook.createCellStyle();
				centerStyle.setAlignment(HorizontalAlignment.CENTER);
				
				Row row;
				row = sheet.createRow(0);
				row.createCell(0).setCellValue("No");
				row.createCell(1).setCellValue("No SKPD");
				row.createCell(2).setCellValue("Tgl Mulai");
				row.createCell(3).setCellValue("Tgl Selesai");
				row.createCell(4).setCellValue("Asal");
				row.createCell(5).setCellValue("Tujuan");
				row.createCell(6).setCellValue("Maksud Dinas");
				row.createCell(7).setCellValue("Status");
				row.createCell(8).setCellValue("Pembuat");
				row.createCell(9).setCellValue("NPP");
				row.createCell(10).setCellValue("Pegawai");
				
				CellStyle cellStyleTgl = workbook.createCellStyle();
				CreationHelper createHelper = workbook.getCreationHelper();
				cellStyleTgl.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
				
				try {
					//java.sql.Date tanggal1 = new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(tgl1).getTime());
					//java.sql.Date tanggal2 = new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(tgl2).getTime());
					Integer NumRows = 0;
					
					query = "exec skpd.sp_listlaporanskpd ?, ?, ?, ?, ?, ?, ?, ?, ?";
					con = new Koneksi().getConnection();
					cs = con.prepareCall(query);
					cs.setInt(1, 1);
					cs.setInt(2, 1);
					cs.setInt(3, 1);
					cs.setNull(4, java.sql.Types.VARCHAR);
					cs.setNull(5, java.sql.Types.VARCHAR);
					cs.setString(6, tgl1);
					cs.setString(7, tgl2);
					cs.setString(8, kodepic);
					cs.setInt(9, status);
					rs = cs.executeQuery();
					if (rs.next()) {
						NumRows = rs.getInt("jumlah");
					}
					rs.close();
					cs.close();
					
					if(NumRows>0) {
						cs = con.prepareCall(query);
						cs.setInt(1, 1);
						cs.setInt(2, NumRows);
						cs.setInt(3, 0);
						cs.setNull(4, java.sql.Types.VARCHAR);
						cs.setNull(5, java.sql.Types.VARCHAR);
						cs.setString(6, tgl1);
						cs.setString(7, tgl2);
						cs.setString(8, kodepic);
						cs.setInt(9, status);
						rs = cs.executeQuery();
						Integer no = 0;
						while (rs.next()) {
							ps = con.prepareStatement("select \r\n" + 
									"a.*, c.npp, c.nama\r\n" + 
									"from skpd.skpdpegawai a\r\n" + 
									"inner join karyawan.penugasan b on a.kodepenugasan=b.kode\r\n" + 
									"inner join karyawan.vw_pegawai c on b.npp=c.npp\r\n" + 
									"where a.kodeskpd = ? order by a.kode");
							ps.setInt(1, rs.getInt("kode"));
							rs2 = ps.executeQuery();
							while (rs2.next()) {
								no++;
								row = sheet.createRow(no);
								row.createCell(0).setCellValue(no);
								row.createCell(1).setCellValue(rs.getString("nomor"));
								
								Cell cellTgl1 = row.createCell(2);
								cellTgl1.setCellValue(rs.getDate("tglmulai"));
								cellTgl1.setCellStyle(cellStyleTgl);
								
								Cell cellTgl2 = row.createCell(3);
								cellTgl2.setCellValue(rs.getDate("tglselesai"));
								cellTgl2.setCellStyle(cellStyleTgl);
								
								row.createCell(4).setCellValue(rs.getString("namaoffice"));
								if(rs.getInt("jeniskegiatan")==3 || rs.getInt("jeniskegiatan")==4) {
									row.createCell(5).setCellValue(rs.getString("namaofficetujuan"));
								}
								else {
									row.createCell(5).setCellValue(rs.getString("tempat")+" ("+rs.getString("namadati2tujuan")+")");
								}
								row.createCell(6).setCellValue(rs.getString("keperluan"));
								if(rs.getInt("status")==0) {
									row.createCell(7).setCellValue("Belum diajukan");
								}
								else {
									row.createCell(7).setCellValue(rs.getString("namastatus"));
								}
								row.createCell(8).setCellValue(rs.getString("namapembuat"));
								row.createCell(9).setCellValue(rs2.getString("npp"));
								row.createCell(10).setCellValue(rs2.getString("nama"));
							}
						}
					}
				}
				finally {
					if (rs != null) {
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
				
				
				tempFile = File.createTempFile("temp", "hcis");
				FileOutputStream fos = null;
				fos = new FileOutputStream(tempFile);
				workbook.write(fos);
			fos.close();
			} catch (Exception e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
				e.printStackTrace();
			}
			finally {
				if (workbook != null) {
					try {
						workbook.close();
					} catch (IOException e) {
					}
				}
			}
			/*=====================================================================*/
			
			ResponseBuilder response = Response.ok((Object) tempFile);
			response.header("Content-Disposition","attachment; filename=lampiran_SKPD_IHC_PETAKU.xlsx");
			tempFile.deleteOnExit();
			return response.build();
		}
		result.setMetadata(metadata);
		return Response.ok(result).build();
	}
	
	@GET
	@Path("/spm/skpd/{token}/{tgl1}/{tgl2}/{jeniskantor}/{kodelokasi}/{nomor: .*}")
	@Produces({ MediaType.APPLICATION_OCTET_STREAM, "application/json" })
	public Response SPM_SKPD(@Context HttpHeaders headers, 
			@PathParam("token") String token, 
			@PathParam("tgl1") String tgl1, 
			@PathParam("tgl2") String tgl2, 
			@PathParam("jeniskantor") Integer jeniskantor,
			@PathParam("kodelokasi") String kodelokasi,
			@PathParam("nomor") String nomor) {
		Result<Object> result = new Result<Object>();
		Metadata metadata = new Metadata();
		if (SharedMethod.VerifyToken2(token, metadata)) {
			Connection con = null;
			ResultSet rs = null;
			CallableStatement cs = null;
			String query = null;
			
			/*=====================================================================*/
			Workbook workbook = null;
			try {
				workbook = new XSSFWorkbook();
				Sheet sheet = workbook.createSheet("Sheet1");
				
				CellStyle centerStyle = workbook.createCellStyle();
				centerStyle.setAlignment(HorizontalAlignment.CENTER);
				
				Row row;
				row = sheet.createRow(0);
				row.createCell(0).setCellValue("No");
				row.createCell(1).setCellValue("No SKPD");
				row.createCell(2).setCellValue("Maksud Perjalanan Dinas");
				row.createCell(3).setCellValue("Pegawai/Non Pegawai");
				row.createCell(4).setCellValue("Nama Pegawai");
				row.createCell(5).setCellValue("Jabatan");
				row.createCell(6).setCellValue("Nomor Indentitas (NIK)");
				row.createCell(7).setCellValue("NPWP");
				row.createCell(8).setCellValue("Tanggal");
				row.createCell(9).setCellValue("Kota");
				row.createCell(10).setCellValue("Kendaraan Yang Digunakan");
				row.createCell(11).setCellValue("Kode Program");
				row.createCell(12).setCellValue("Kode Akun");
				row.createCell(13).setCellValue("Jumlah");
				
				try {
					java.sql.Date tanggal1 = new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(tgl1).getTime());
					java.sql.Date tanggal2 = new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(tgl2).getTime());
					
					query = "exec skpd.sp_getlaporanskpdpegawai_excel ?,?,?,?,?";
					con = new Koneksi().getConnection();
					cs = con.prepareCall(query);
					cs.setDate(1, tanggal1);
					cs.setDate(2, tanggal2);
					cs.setInt(3, jeniskantor);
					cs.setString(4, kodelokasi);
					cs.setString(5, nomor);
					rs = cs.executeQuery();
					
					Integer i = 1;
					while (rs.next()) {
						row = sheet.createRow(i);
						row.createCell(0).setCellValue(i);
						row.createCell(1).setCellValue(rs.getString("nomor"));
						row.createCell(2).setCellValue(rs.getString("keperluan"));
						row.createCell(3).setCellValue(rs.getString("jenispegawai"));
						row.createCell(4).setCellValue(rs.getString("namalengkap"));
						row.createCell(5).setCellValue(rs.getString("namagrade"));
						row.createCell(6).setCellValue(rs.getString("nik"));
						row.createCell(7).setCellValue(rs.getString("npwp"));
						row.createCell(8).setCellValue(rs.getString("tgl"));
						row.createCell(9).setCellValue(rs.getString("kota"));
						row.createCell(10).setCellValue(rs.getString("namajeniskendaraan"));
						row.createCell(11).setCellValue(rs.getString("kodeprogram"));
						row.createCell(12).setCellValue(rs.getString("kodeakun"));
						row.createCell(13).setCellValue(rs.getBigDecimal("total").doubleValue());
						i++;
					}
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
				
				
				tempFile = File.createTempFile("temp", "hcis");
				FileOutputStream fos = null;
				fos = new FileOutputStream(tempFile);
				workbook.write(fos);
			fos.close();
			} catch (Exception e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
				e.printStackTrace();
			}
			finally {
				if (workbook != null) {
					try {
						workbook.close();
					} catch (IOException e) {
					}
				}
			}
			/*=====================================================================*/
			
			ResponseBuilder response = Response.ok((Object) tempFile);
			response.header("Content-Disposition","attachment; filename=lampiran_SKPD_IHC_PETAKU.xlsx");
			tempFile.deleteOnExit();
			return response.build();
		}
		result.setMetadata(metadata);
		return Response.ok(result).build();
	}
	
	@GET
	@Path("/spm/lembur/{token}/{tgl1}/{tgl2}/{jeniskantor}/{kodelokasi}/{nomor: .*}")
	@Produces({ MediaType.APPLICATION_OCTET_STREAM, "application/json" })
	public Response SPM_Lembur(@Context HttpHeaders headers, 
			@PathParam("token") String token, 
			@PathParam("tgl1") String tgl1, 
			@PathParam("tgl2") String tgl2, 
			@PathParam("jeniskantor") Integer jeniskantor,
			@PathParam("kodelokasi") String kodelokasi,
			@PathParam("nomor") String nomor) {
		Result<Object> result = new Result<Object>();
		Metadata metadata = new Metadata();
		if (SharedMethod.VerifyToken2(token, metadata)) {
			Connection con = null;
			ResultSet rs = null;
			ResultSet rs2 = null;
			ResultSet rs3 = null;
			ResultSet rs4 = null;
			PreparedStatement ps = null;
			CallableStatement cs = null;
			CallableStatement cs2 = null;
			CallableStatement cs3 = null;
			String query = null;
			String query2 = null;
			String query3 = null;
			String query4 = null;
			
			/*=====================================================================*/
			String[] hari={"","Minggu","Senin","Selasa","Rabu","Kamis","Jum'at","Sabtu"};
			
			Workbook workbook = null;
			try {
				workbook = new XSSFWorkbook();
				Sheet sheet = workbook.createSheet("Sheet1");
				
				CellStyle centerStyle = workbook.createCellStyle();
				centerStyle.setAlignment(HorizontalAlignment.CENTER);
				
				Row row;
				row = sheet.createRow(0);
				row.createCell(0).setCellValue("No");
				sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
				row.createCell(1).setCellValue("Nama");
				sheet.addMergedRegion(new CellRangeAddress(0, 1, 1, 1));
				row.createCell(2).setCellValue("No Rekening");
				sheet.addMergedRegion(new CellRangeAddress(0, 1, 2, 2));
				row.createCell(3).setCellValue("Tanggal");
				sheet.addMergedRegion(new CellRangeAddress(0, 1, 3, 3));
				row.createCell(4).setCellValue("Hari");
				sheet.addMergedRegion(new CellRangeAddress(0, 1, 4, 4));
				row.createCell(5).setCellValue("Jam");
				sheet.addMergedRegion(new CellRangeAddress(0, 0, 5, 7));
				row.createCell(8).setCellValue("Uang Lembur");
				sheet.addMergedRegion(new CellRangeAddress(0, 0, 8, 9));
				row.createCell(10).setCellValue("Uang Makan");
				sheet.addMergedRegion(new CellRangeAddress(0, 1, 10, 10));
				row.createCell(11).setCellValue("Uang Transport");
				sheet.addMergedRegion(new CellRangeAddress(0, 1, 11, 11));
				row.createCell(12).setCellValue("Jumlah");
				sheet.addMergedRegion(new CellRangeAddress(0, 1, 12, 12));
				
				
				row = sheet.createRow(1);
				row.createCell(5).setCellValue("Datang");
				row.createCell(6).setCellValue("Pulang");
				row.createCell(7).setCellValue("Lembur");
				row.createCell(8).setCellValue("Per Jam");
				row.createCell(9).setCellValue("Jumlah");
				
				row = sheet.createRow(2);
				row.createCell(0).setCellValue(1);
				row.createCell(1).setCellValue(2);
				row.createCell(2).setCellValue(3);
				row.createCell(3).setCellValue(4);
				row.createCell(4).setCellValue(5);
				row.createCell(5).setCellValue(6);
				row.createCell(6).setCellValue(7);
				row.createCell(7).setCellValue(8);
				row.createCell(8).setCellValue(9);
				row.createCell(9).setCellValue(10);
				row.createCell(10).setCellValue(11);
				row.createCell(11).setCellValue(12);
				row.createCell(12).setCellValue(13);
				
				Integer baris = 2;
				try {
					java.sql.Date tanggal1 = new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(tgl1).getTime());
					java.sql.Date tanggal2 = new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(tgl2).getTime());
					
					query = "exec lembur.sp_getlaporanlembur ?,?,?,?,?";
					con = new Koneksi().getConnection();
					cs = con.prepareCall(query);
					cs.setDate(1, tanggal1);
					cs.setDate(2, tanggal2);
					cs.setInt(3, jeniskantor);
					cs.setString(4, kodelokasi);
					cs.setString(5, nomor);
					rs = cs.executeQuery();
					Integer no = 0;
					while (rs.next()) {
						try {
							query2 = "exec lembur.sp_getlaporanlemburpegawai ?";
							cs2 = con.prepareCall(query2);
							cs2.setInt(1, rs.getInt("kode"));
							rs2 = cs2.executeQuery();
							while (rs2.next()) {
								no++;
								
								String norek = "";
								try {
									query4 = "select top 1 norek from karyawan.inforekeningbank where npp=? and row_status=1 order by kode";
									ps = con.prepareStatement(query4);
									ps.setString(1, rs2.getString("npp"));
									rs4 = ps.executeQuery();
									if(rs4.next()) {
										norek = rs4.getString("norek");
									}
								}
								finally {
									if (rs4 != null) {
										try {
											rs4.close();
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
									query3 = "exec lembur.sp_getlaporanlemburtgl ?, ?, ?";
									cs3 = con.prepareCall(query3);
									cs3.setInt(1, rs.getInt("kode"));
									cs3.setInt(2, rs2.getInt("kodepenugasan"));
									cs3.setShort(3, rs.getShort("isverif"));
									rs3 = cs3.executeQuery();
									Integer i = 0;
									while (rs3.next()) {
										baris++;
										row = sheet.createRow(baris);
										if(i==0) {
											row.createCell(0).setCellValue(no);
											row.createCell(1).setCellValue(rs2.getString("namalengkap")+" ("+rs2.getString("npp")+")");
											row.createCell(2).setCellValue(norek);
										}
										//System.out.println(baris+" | "+rs2.getString("namalengkap"));
										
										CellStyle cellStyleTgl = workbook.createCellStyle();
										CreationHelper createHelper = workbook.getCreationHelper();
										cellStyleTgl.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
										
										CellStyle cellStyleTime = workbook.createCellStyle();
										CreationHelper createHelperTime = workbook.getCreationHelper();
										cellStyleTime.setDataFormat(createHelperTime.createDataFormat().getFormat("HH:mm:ss"));
										
										Cell cellTgl = row.createCell(3);
										cellTgl.setCellValue(rs3.getDate("tgl"));
										cellTgl.setCellStyle(cellStyleTgl);
										
										row.createCell(4).setCellValue(hari[rs3.getInt("hari")]);
										
										if(rs3.getTime("absenmasuk")==null) {
											row.createCell(5).setCellValue("-");
										}
										else {
											Cell cellMasuk = row.createCell(5);
											cellMasuk.setCellValue(rs3.getTime("absenmasuk"));
											cellMasuk.setCellStyle(cellStyleTime);
										}
										if(rs3.getTime("absenpulang")==null) {
											row.createCell(6).setCellValue("-");
										}
										else {
											Cell cellPulang = row.createCell(6);
											cellPulang.setCellValue(rs3.getTime("absenpulang"));
											cellPulang.setCellStyle(cellStyleTime);
										}
										
										row.createCell(7).setCellValue(rs3.getInt("jmljam"));
										row.createCell(8).setCellValue(rs3.getBigDecimal("perjam").doubleValue());
										row.createCell(9).setCellValue(rs3.getBigDecimal("uanglembur").doubleValue());
										row.createCell(10).setCellValue(rs3.getBigDecimal("uangmakan").doubleValue());
										row.createCell(11).setCellValue(rs3.getBigDecimal("uangtransport").doubleValue());
										row.createCell(12).setCellValue(rs3.getBigDecimal("subtotal").doubleValue());
										i++;
									}
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
							}
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
					}
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
				
				baris++;
				row = sheet.createRow(baris);
				/*Cell cell = row.createCell(11);
				cell.setCellStyle(centerStyle);
				cell.setCellValue("TOTAL");
				sheet.addMergedRegion(CellRangeAddress.valueOf("A"+i+":K"+i));*/
				row.createCell(12).setCellFormula("SUM(M4:M"+(baris)+")");
				
				tempFile = File.createTempFile("temp", "hcis");
				FileOutputStream fos = null;
				fos = new FileOutputStream(tempFile);
				workbook.write(fos);
			fos.close();
			} catch (Exception e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
				e.printStackTrace();
			}
			finally {
				if (workbook != null) {
					try {
						workbook.close();
					} catch (IOException e) {
					}
				}
			}
			/*=====================================================================*/
			
			ResponseBuilder response = Response.ok((Object) tempFile);
			response.header("Content-Disposition","attachment; filename=Daftar_Pembayaran_Uang_Lembur.xlsx");
			tempFile.deleteOnExit();
			return response.build();
		}
		result.setMetadata(metadata);
		return Response.ok(result).build();
	}
	
	@GET
	@Path("/laporan/cuti/{token}/{tgl1}/{tgl2}/{jeniskantor}/{kodelokasi}/{status}/{npp: .*}")
	@Produces({ MediaType.APPLICATION_OCTET_STREAM, "application/json" })
	public Response Laporan_Cuti(@Context HttpHeaders headers, 
			@PathParam("token") String token, 
			@PathParam("tgl1") String tgl1, 
			@PathParam("tgl2") String tgl2, 
			@PathParam("jeniskantor") Integer jeniskantor,
			@PathParam("kodelokasi") String kodelokasi,
			@PathParam("status") Integer status,
			@PathParam("npp") String npp) {
		Result<Object> result = new Result<Object>();
		Metadata metadata = new Metadata();
		if (SharedMethod.VerifyToken2(token, metadata)) {
			Connection con = null;
			ResultSet rs = null;
			CallableStatement cs = null;
			String query = null;
			
			/*=====================================================================*/
			Workbook workbook = null;
			try {
				workbook = new XSSFWorkbook();
				Sheet sheet = workbook.createSheet("Sheet1");
				
				CellStyle centerStyle = workbook.createCellStyle();
				centerStyle.setAlignment(HorizontalAlignment.CENTER);
				
				Row row;
				row = sheet.createRow(0);
				row.createCell(0).setCellValue("No");
				row.createCell(1).setCellValue("Nomor");
				row.createCell(2).setCellValue("NPP");
				row.createCell(3).setCellValue("Nama");
				row.createCell(4).setCellValue("Tanggal");
				row.createCell(5).setCellValue("Keperluan");
				row.createCell(6).setCellValue("Status");
				
				CellStyle cellStyleTgl = workbook.createCellStyle();
				CreationHelper createHelper = workbook.getCreationHelper();
				cellStyleTgl.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
				
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
					Integer no = 0;
					while (rs.next()) {
						no++;
						row = sheet.createRow(no);
						row.createCell(0).setCellValue(no);
						row.createCell(1).setCellValue(rs.getString("nomor"));
						row.createCell(2).setCellValue(rs.getString("npp"));
						row.createCell(3).setCellValue(rs.getString("namalengkap"));
						
						Cell cellTgl2 = row.createCell(4);
						cellTgl2.setCellValue(rs.getDate("tgl"));
						cellTgl2.setCellStyle(cellStyleTgl);
						
						row.createCell(5).setCellValue(rs.getString("alasancuti"));
						row.createCell(6).setCellValue(rs.getString("namastatus"));
					}
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
				
				
				tempFile = File.createTempFile("temp", "hcis");
				FileOutputStream fos = null;
				fos = new FileOutputStream(tempFile);
				workbook.write(fos);
			fos.close();
			} catch (Exception e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
				e.printStackTrace();
			}
			finally {
				if (workbook != null) {
					try {
						workbook.close();
					} catch (IOException e) {
					}
				}
			}
			/*=====================================================================*/
			
			ResponseBuilder response = Response.ok((Object) tempFile);
			response.header("Content-Disposition","attachment; filename=Laporan_Cuti.xlsx");
			tempFile.deleteOnExit();
			return response.build();
		}
		result.setMetadata(metadata);
		return Response.ok(result).build();
	}
	
	@GET
	@Path("/laporan/telaah/{token}/{tgl1}/{tgl2}/{kodeoffice}/{kodereftelaah}")
	@Produces({ MediaType.APPLICATION_OCTET_STREAM, "application/json" })
	public Response Laporan_Telaah_PM(@Context HttpHeaders headers, 
			@PathParam("token") String token, 
			@PathParam("tgl1") String tgl1, 
			@PathParam("tgl2") String tgl2, 
			@PathParam("kodereftelaah") String kodereftelaah,
			@PathParam("kodeoffice") String kodeoffice) {
		Result<Object> result = new Result<Object>();
		Metadata metadata = new Metadata();
		if (SharedMethod.VerifyToken2(token, metadata)) {
			Connection con = null;
			ResultSet rs = null;
			CallableStatement cs = null;
			String query = null;
			
			/*=====================================================================*/
			Workbook workbook = null;
			try {
				workbook = new XSSFWorkbook();
				Sheet sheet = workbook.createSheet("Sheet1");
				
				CellStyle centerStyle = workbook.createCellStyle();
				centerStyle.setAlignment(HorizontalAlignment.CENTER);
				
				Row row;
				row = sheet.createRow(0);
				row.createCell(0).setCellValue("No");
				row.createCell(1).setCellValue("Jenis Telaah");
				row.createCell(2).setCellValue("NPP");
				row.createCell(3).setCellValue("Nama");
				row.createCell(4).setCellValue("Jabatan Asal");
				row.createCell(5).setCellValue("Unit Kerja Asal");
				row.createCell(6).setCellValue("Kantor Asal");
				row.createCell(7).setCellValue("Jabatan Tujuan");
				row.createCell(8).setCellValue("Unit Kerja Tujuan");
				row.createCell(9).setCellValue("Kantor Tujuan");
				row.createCell(10).setCellValue("Status Jabatan Tujuan");
				row.createCell(11).setCellValue("Status Telaah");
				
				CellStyle cellStyleTgl = workbook.createCellStyle();
				CreationHelper createHelper = workbook.getCreationHelper();
				cellStyleTgl.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
				
				try {
					
					if(kodereftelaah.equals("0")) {
						kodereftelaah = "";
					}
					if(kodeoffice.equals("0")) {
						kodeoffice = "";
					}
					java.sql.Date tanggal1 = new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(tgl1).getTime());
					java.sql.Date tanggal2 = new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(tgl2).getTime());
					
					query = "exec telaah.sp_get_laporan_telaah ?, ?, ?, ?";
					con = new Koneksi().getConnection();
					cs = con.prepareCall(query);
					cs.setDate(1, tanggal1);
					cs.setDate(2, tanggal2);
					cs.setString(3, kodereftelaah);
					cs.setString(4, kodeoffice);
//					cs.setInt(5, status);
//					cs.setString(6, npp);
					rs = cs.executeQuery();
					Integer no = 0;
					while (rs.next()) {
						no++;
						row = sheet.createRow(no);
						row.createCell(0).setCellValue(no);
						row.createCell(1).setCellValue(rs.getString("jenistelaah"));
						row.createCell(2).setCellValue(rs.getString("npp"));
						row.createCell(3).setCellValue(rs.getString("namalengkap"));
						row.createCell(4).setCellValue(rs.getString("jabatanasal"));
						row.createCell(5).setCellValue(rs.getString("unitkerjaasal"));
						row.createCell(6).setCellValue(rs.getString("kantorasal"));
						row.createCell(7).setCellValue(rs.getString("jabatantujuan"));
						row.createCell(8).setCellValue(rs.getString("unitkerjatujuan"));
						row.createCell(9).setCellValue(rs.getString("kantortujuan"));
						row.createCell(10).setCellValue(rs.getString("statusjabatantujuan"));
						row.createCell(11).setCellValue(rs.getString("statustelaah"));
					}
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
				
				
				tempFile = File.createTempFile("temp", "hcis");
				FileOutputStream fos = null;
				fos = new FileOutputStream(tempFile);
				workbook.write(fos);
			fos.close();
			} catch (Exception e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
				e.printStackTrace();
			}
			finally {
				if (workbook != null) {
					try {
						workbook.close();
					} catch (IOException e) {
					}
				}
			}
			/*=====================================================================*/
			
			ResponseBuilder response = Response.ok((Object) tempFile);
			response.header("Content-Disposition","attachment; filename=Laporan_Telaah.xlsx");
			tempFile.deleteOnExit();
			return response.build();
		}
		result.setMetadata(metadata);
		return Response.ok(result).build();
	}
	
	@GET
	@Path("/laporan/payroll/pergrade/tahun/{tahun}/bulan/{bulan}/kodeoffice/{kodeoffice}/namaoffice/{namaoffice}/{token}")
	@Produces({ MediaType.APPLICATION_OCTET_STREAM, "application/json" })
	public Response Laporan_Payroll_Pergrade(@Context HttpHeaders headers, 
			@PathParam("token") String token, 
			@PathParam("tahun") Integer tahun, 
			@PathParam("bulan") Integer bulan, 
			@PathParam("kodeoffice") String kodeoffice,
			@PathParam("namaoffice") String namaoffice) {
		Result<Object> result = new Result<Object>();
		Metadata metadata = new Metadata();
		if (SharedMethod.VerifyToken2(token, metadata)) {
			Connection con = null;
			ResultSet rs = null;
			CallableStatement cs = null;
			String query = null;
			
			String[] namabulan={"","Januari","Februari","Maret","April","Mei","Juni","Juli","Agustus","September","Oktober","Nopember","Desember"};
			/*=====================================================================*/
			Workbook workbook = null;
			try {
				workbook = new XSSFWorkbook();
				Sheet sheet = workbook.createSheet("Sheet1");
				
				CellStyle centerStyle = workbook.createCellStyle();
				centerStyle.setAlignment(HorizontalAlignment.CENTER);
				
				Integer baris = 0;
				Row row;
				row = sheet.createRow(baris);
				row.createCell(0).setCellValue("Rekapitulasi Pembayaran Penghasilan Per Pangkat".toUpperCase());
				baris++;
				row = sheet.createRow(baris);
				row.createCell(0).setCellValue(namabulan[bulan].toUpperCase() + " " + tahun);
				baris++;
				row = sheet.createRow(baris);
				row.createCell(0).setCellValue(namaoffice.toUpperCase());
				
				baris++;
				baris++;
				row = sheet.createRow(baris);
				row.createCell(0).setCellValue("No");
				sheet.addMergedRegion(new CellRangeAddress(baris, (baris+1), 0, 0));
				row.createCell(1).setCellValue("Pangkat");
				sheet.addMergedRegion(new CellRangeAddress(baris, (baris+1), 1, 1));
				row.createCell(2).setCellValue("Jumlah Pegawai");
				sheet.addMergedRegion(new CellRangeAddress(baris, (baris+1), 2, 2));
				row.createCell(3).setCellValue("Indeks Konjungtur");
				sheet.addMergedRegion(new CellRangeAddress(baris, (baris+1), 3, 3));
				row.createCell(4).setCellValue("Pendapatan");
				sheet.addMergedRegion(new CellRangeAddress(baris, baris, 4, (4+10)));
				row.createCell(15).setCellValue("Potongan");
				sheet.addMergedRegion(new CellRangeAddress(baris, baris, 15, (15+15)));
				row.createCell(31).setCellValue("TOTAL");
				sheet.addMergedRegion(new CellRangeAddress(baris, (baris+1), 31, 31));
				baris++;
				row = sheet.createRow(baris);
				row.createCell(4).setCellValue("Gaji Pokok");
				row.createCell(5).setCellValue("Tunjangan Prestasi");
				row.createCell(6).setCellValue("Tunjangan Utilitas");
				row.createCell(7).setCellValue("Tunjangan Jabatan");
				row.createCell(8).setCellValue("Tunjangan Daerah Terpencil");
				//row.createCell(9).setCellValue("Tunjangan Khusus");
				row.createCell(9).setCellValue("Tunjangan BBM");
				row.createCell(10).setCellValue("Tunjangan Komunikasi");
				row.createCell(11).setCellValue("Tunjangan Transportasi");
				row.createCell(12).setCellValue("Bantuan Perumahan");
				row.createCell(13).setCellValue("Pendapatan Lain - lain");
				row.createCell(14).setCellValue("Jumlah");
				
				row.createCell(15).setCellValue("JPK Pegawai");
				row.createCell(16).setCellValue("JPK-CoB Tambahan");
				row.createCell(17).setCellValue("JPK-CoB Mantan Pegawai");
				row.createCell(18).setCellValue("BPJS-K Keluarga Tambahan");
				row.createCell(19).setCellValue("BPJS-TK");
				row.createCell(20).setCellValue("JHT");
				//row.createCell(20).setCellValue("Iuran Korpri");
				row.createCell(21).setCellValue("Iuran IIKA");
				row.createCell(22).setCellValue("Koperasi Bhakti");
				row.createCell(23).setCellValue("Kekurangan SPJ UMK");
				row.createCell(24).setCellValue("Carloan");
				row.createCell(25).setCellValue("Potongan Ketidakdisiplinan");
				row.createCell(26).setCellValue("Kelebihan Uang Pulsa");
				row.createCell(27).setCellValue("Baznas");
				row.createCell(28).setCellValue("Donasi");
				row.createCell(29).setCellValue("Potongan Lain - lain");
				row.createCell(30).setCellValue("Jumlah");
				
				CellStyle cellStyleTgl = workbook.createCellStyle();
				CreationHelper createHelper = workbook.getCreationHelper();
				cellStyleTgl.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
				
				try {
					query = "exec payroll.sp_laporan_rekap_grade ?, ?, ?";
					con = new Koneksi().getConnection();
					cs = con.prepareCall(query);
					cs.setInt(1, tahun);
					cs.setInt(2, bulan);
					cs.setString(3, kodeoffice);
					rs = cs.executeQuery();
					Integer no = 0;
					while (rs.next()) {
						no++;
						baris++;
						row = sheet.createRow(baris);
						row.createCell(0).setCellValue(no);
						row.createCell(1).setCellValue(rs.getString("namagrade"));
						row.createCell(2).setCellValue(rs.getInt("jml"));
						row.createCell(3).setCellValue(rs.getInt("ik"));
						
						row.createCell(4).setCellValue(rs.getBigDecimal("gapok").doubleValue());
						row.createCell(5).setCellValue(rs.getBigDecimal("tupres").doubleValue());
						row.createCell(6).setCellValue(rs.getBigDecimal("utilitas").doubleValue());
						row.createCell(7).setCellValue(rs.getBigDecimal("tunjjab").doubleValue());
						row.createCell(8).setCellValue(rs.getBigDecimal("tunjdacil").doubleValue());
						//row.createCell(9).setCellValue(rs.getBigDecimal("tunjkhusus").doubleValue());
						row.createCell(9).setCellValue(rs.getBigDecimal("tunjbbm").doubleValue());
						row.createCell(10).setCellValue(rs.getBigDecimal("tunjkomunikasi").doubleValue());
						row.createCell(11).setCellValue(rs.getBigDecimal("tunjtransportasi").doubleValue());
						row.createCell(12).setCellValue(rs.getBigDecimal("bantuanperumahan").doubleValue());
						row.createCell(13).setCellValue(rs.getBigDecimal("pendapatanlain").doubleValue());
						row.createCell(14).setCellValue(rs.getBigDecimal("totalpendapatan").doubleValue());
						
						row.createCell(15).setCellValue(rs.getBigDecimal("jpkpegawai").doubleValue());
						row.createCell(16).setCellValue(rs.getBigDecimal("jpkcobtambahan").doubleValue());
						row.createCell(17).setCellValue(rs.getBigDecimal("jpkcobmantan").doubleValue());
						row.createCell(18).setCellValue(rs.getBigDecimal("bpjsktambahan").doubleValue());
						row.createCell(19).setCellValue(rs.getBigDecimal("bpjstk").doubleValue());
						row.createCell(20).setCellValue(rs.getBigDecimal("jht").doubleValue());
						//row.createCell(20).setCellValue(rs.getBigDecimal("korpri").doubleValue());
						row.createCell(21).setCellValue(rs.getBigDecimal("iika").doubleValue());
						row.createCell(22).setCellValue(rs.getBigDecimal("koperasi").doubleValue());
						row.createCell(23).setCellValue(rs.getBigDecimal("kekuranganspj").doubleValue());
						row.createCell(24).setCellValue(rs.getBigDecimal("carloan").doubleValue());
						row.createCell(25).setCellValue(rs.getBigDecimal("potketidakdisiplinan").doubleValue());
						row.createCell(26).setCellValue(rs.getBigDecimal("kelebihanuangpulsa").doubleValue());
						row.createCell(27).setCellValue(rs.getBigDecimal("baznas").doubleValue());
						row.createCell(28).setCellValue(rs.getBigDecimal("donasi").doubleValue());
						row.createCell(29).setCellValue(rs.getBigDecimal("potonganlain").doubleValue());
						row.createCell(30).setCellValue(rs.getBigDecimal("totalpotongan").doubleValue());
						row.createCell(31).setCellValue(rs.getBigDecimal("thp").doubleValue());
					}
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
				
				tempFile = File.createTempFile("temp", "hcis");
				FileOutputStream fos = null;
				fos = new FileOutputStream(tempFile);
				workbook.write(fos);
			fos.close();
			} catch (Exception e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
				e.printStackTrace();
			}
			finally {
				if (workbook != null) {
					try {
						workbook.close();
					} catch (IOException e) {
					}
				}
			}
			/*=====================================================================*/
			
			ResponseBuilder response = Response.ok((Object) tempFile);
			response.header("Content-Disposition","attachment; filename=Laporan_Payroll_Perpangkat_" + new Date().getTime() + ".xlsx");
			tempFile.deleteOnExit();
			return response.build();
		}
		result.setMetadata(metadata);
		return Response.ok(result).build();
	}
	
	@GET
	@Path("/laporan/payroll/pegawaitetap/tahun/{tahun}/bulan/{bulan}/kodeoffice/{kodeoffice}/namaoffice/{namaoffice}/{token}")
	@Produces({ MediaType.APPLICATION_OCTET_STREAM, "application/json" })
	public Response Laporan_Payroll_Pegawai_Tetap(@Context HttpHeaders headers, 
			@PathParam("token") String token, 
			@PathParam("tahun") Integer tahun, 
			@PathParam("bulan") Integer bulan, 
			@PathParam("kodeoffice") String kodeoffice,
			@PathParam("namaoffice") String namaoffice) {
		Result<Object> result = new Result<Object>();
		Metadata metadata = new Metadata();
		if (SharedMethod.VerifyToken2(token, metadata)) {
			Connection con = null;
			ResultSet rs = null;
			CallableStatement cs = null;
			String query = null;
			
			String[] namabulan={"","Januari","Februari","Maret","April","Mei","Juni","Juli","Agustus","September","Oktober","Nopember","Desember"};
			/*=====================================================================*/
			Workbook workbook = null;
			try {
				workbook = new XSSFWorkbook();
				Sheet sheet = workbook.createSheet("Sheet1");
				
				CellStyle centerStyle = workbook.createCellStyle();
				centerStyle.setAlignment(HorizontalAlignment.CENTER);
				
				Integer baris = 0;
				Row row;
				row = sheet.createRow(baris);
				row.createCell(0).setCellValue("Daftar Pembayaran Penghasilan Pegawai Tetap".toUpperCase());
				baris++;
				row = sheet.createRow(baris);
				row.createCell(0).setCellValue(namabulan[bulan].toUpperCase() + " " + tahun);
				baris++;
				row = sheet.createRow(baris);
				row.createCell(0).setCellValue(namaoffice.toUpperCase());
				
				baris++;
				baris++;
				row = sheet.createRow(baris);
				row.createCell(0).setCellValue("No");
				sheet.addMergedRegion(new CellRangeAddress(baris, (baris+1), 0, 0));
				row.createCell(1).setCellValue("NPP");
				sheet.addMergedRegion(new CellRangeAddress(baris, (baris+1), 1, 1));
				row.createCell(2).setCellValue("Nama");
				sheet.addMergedRegion(new CellRangeAddress(baris, (baris+1), 2, 2));
				row.createCell(3).setCellValue("Grade - Skala");
				sheet.addMergedRegion(new CellRangeAddress(baris, (baris+1), 3, 3));
				row.createCell(4).setCellValue("Indeks Konjungtur");
				sheet.addMergedRegion(new CellRangeAddress(baris, (baris+1), 4, 4));
				row.createCell(5).setCellValue("Gaji Pokok (Base)");
				sheet.addMergedRegion(new CellRangeAddress(baris, (baris+1), 5, 5));
				row.createCell(6).setCellValue("% Predikat Kinerja");
				sheet.addMergedRegion(new CellRangeAddress(baris, (baris+1), 6, 6));
				row.createCell(7).setCellValue("Tunjangan Prestasi (Base)");
				sheet.addMergedRegion(new CellRangeAddress(baris, (baris+1), 7, 7));
				row.createCell(8).setCellValue("Pendapatan");
				sheet.addMergedRegion(new CellRangeAddress(baris, baris, 8, (8+10)));
				row.createCell(19).setCellValue("Potongan");
				sheet.addMergedRegion(new CellRangeAddress(baris, baris, 19, (19+15)));
				row.createCell(35).setCellValue("THP");
				sheet.addMergedRegion(new CellRangeAddress(baris, (baris+1), 35, 35));
				row.createCell(36).setCellValue("No Rekening");
				sheet.addMergedRegion(new CellRangeAddress(baris, (baris+1), 36, 36));
				baris++;
				row = sheet.createRow(baris);
				row.createCell(8).setCellValue("Gaji Pokok");
				row.createCell(9).setCellValue("Tunjangan Prestasi");
				row.createCell(10).setCellValue("Tunjangan Utilitas");
				row.createCell(11).setCellValue("Tunjangan Jabatan");
				row.createCell(12).setCellValue("Tunjangan Daerah Terpencil");
				//row.createCell(13).setCellValue("Tunjangan Khusus");
				row.createCell(13).setCellValue("Tunjangan BBM");
				row.createCell(14).setCellValue("Tunjangan Komunikasi");
				row.createCell(15).setCellValue("Tunjangan Transportasi");
				row.createCell(16).setCellValue("Bantuan Perumahan");
				row.createCell(17).setCellValue("Pendapatan Lain - lain");
				row.createCell(18).setCellValue("Jumlah");
				
				row.createCell(19).setCellValue("JPK Pegawai");
				row.createCell(20).setCellValue("JPK-CoB Tambahan");
				row.createCell(21).setCellValue("JPK-CoB Mantan Pegawai");
				row.createCell(22).setCellValue("BPJS-K Keluarga Tambahan");
				row.createCell(23).setCellValue("BPJS-TK");
				row.createCell(24).setCellValue("JHT");
				//row.createCell(24).setCellValue("Iuran Korpri");
				row.createCell(25).setCellValue("Iuran IIKA");
				row.createCell(26).setCellValue("Koperasi Bhakti");
				row.createCell(27).setCellValue("Kekurangan SPJ UMK");
				row.createCell(28).setCellValue("Carloan");
				row.createCell(29).setCellValue("Potongan Ketidakdisiplinan");
				row.createCell(30).setCellValue("Kelebihan Uang Pulsa");
				row.createCell(31).setCellValue("Baznas");
				row.createCell(32).setCellValue("Donasi");
				row.createCell(33).setCellValue("Potongan Lain - lain");
				row.createCell(34).setCellValue("Jumlah");
				
				CellStyle cellStyleTgl = workbook.createCellStyle();
				CreationHelper createHelper = workbook.getCreationHelper();
				cellStyleTgl.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
				
				try {
					query = "exec payroll.sp_laporan_rekap_pegawaitetap ?, ?, ?";
					con = new Koneksi().getConnection();
					cs = con.prepareCall(query);
					cs.setInt(1, tahun);
					cs.setInt(2, bulan);
					cs.setString(3, kodeoffice);
					rs = cs.executeQuery();
					Integer no = 0;
					while (rs.next()) {
						no++;
						baris++;
						row = sheet.createRow(baris);
						row.createCell(0).setCellValue(no);
						row.createCell(1).setCellValue(rs.getString("npp"));
						row.createCell(2).setCellValue(rs.getString("namalengkap"));
						row.createCell(3).setCellValue(rs.getString("namagrade") + " - " + rs.getString("namasubgrade"));
						row.createCell(4).setCellValue(rs.getInt("ik"));
						row.createCell(5).setCellValue(rs.getBigDecimal("gajipokok").doubleValue());
						row.createCell(6).setCellValue(rs.getInt("persentupres"));
						row.createCell(7).setCellValue(rs.getBigDecimal("tupres100").doubleValue());
						
						row.createCell(8).setCellValue(rs.getBigDecimal("gapok").doubleValue());
						row.createCell(9).setCellValue(rs.getBigDecimal("tupres").doubleValue());
						row.createCell(10).setCellValue(rs.getBigDecimal("utilitas").doubleValue());
						row.createCell(11).setCellValue(rs.getBigDecimal("tunjjab").doubleValue());
						row.createCell(12).setCellValue(rs.getBigDecimal("tunjdacil").doubleValue());
						//row.createCell(13).setCellValue(rs.getBigDecimal("tunjkhusus").doubleValue());
						row.createCell(13).setCellValue(rs.getBigDecimal("tunjbbm").doubleValue());
						row.createCell(14).setCellValue(rs.getBigDecimal("tunjkomunikasi").doubleValue());
						row.createCell(15).setCellValue(rs.getBigDecimal("tunjtransportasi").doubleValue());
						row.createCell(16).setCellValue(rs.getBigDecimal("bantuanperumahan").doubleValue());
						row.createCell(17).setCellValue(rs.getBigDecimal("pendapatanlain").doubleValue());
						row.createCell(18).setCellValue(rs.getBigDecimal("totalpendapatan").doubleValue());
						
						row.createCell(19).setCellValue(rs.getBigDecimal("jpkpegawai").doubleValue());
						row.createCell(20).setCellValue(rs.getBigDecimal("jpkcobtambahan").doubleValue());
						row.createCell(21).setCellValue(rs.getBigDecimal("jpkcobmantan").doubleValue());
						row.createCell(22).setCellValue(rs.getBigDecimal("bpjsktambahan").doubleValue());
						row.createCell(23).setCellValue(rs.getBigDecimal("bpjstk").doubleValue());
						row.createCell(24).setCellValue(rs.getBigDecimal("jht").doubleValue());
						//row.createCell(24).setCellValue(rs.getBigDecimal("korpri").doubleValue());
						row.createCell(25).setCellValue(rs.getBigDecimal("iika").doubleValue());
						row.createCell(26).setCellValue(rs.getBigDecimal("koperasi").doubleValue());
						row.createCell(27).setCellValue(rs.getBigDecimal("kekuranganspj").doubleValue());
						row.createCell(28).setCellValue(rs.getBigDecimal("carloan").doubleValue());
						row.createCell(29).setCellValue(rs.getBigDecimal("potketidakdisiplinan").doubleValue());
						row.createCell(30).setCellValue(rs.getBigDecimal("kelebihanuangpulsa").doubleValue());
						row.createCell(31).setCellValue(rs.getBigDecimal("baznas").doubleValue());
						row.createCell(32).setCellValue(rs.getBigDecimal("donasi").doubleValue());
						row.createCell(33).setCellValue(rs.getBigDecimal("potonganlain").doubleValue());
						row.createCell(34).setCellValue(rs.getBigDecimal("totalpotongan").doubleValue());
						row.createCell(35).setCellValue(rs.getBigDecimal("thp").doubleValue());
						row.createCell(36).setCellValue(rs.getString("norek"));
					}
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
				
				tempFile = File.createTempFile("temp", "hcis");
				FileOutputStream fos = null;
				fos = new FileOutputStream(tempFile);
				workbook.write(fos);
			fos.close();
			} catch (Exception e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
				e.printStackTrace();
			}
			finally {
				if (workbook != null) {
					try {
						workbook.close();
					} catch (IOException e) {
					}
				}
			}
			/*=====================================================================*/
			
			ResponseBuilder response = Response.ok((Object) tempFile);
			response.header("Content-Disposition","attachment; filename=Laporan_Payroll_Pegawai_Tetap_" + new Date().getTime() + ".xlsx");
			tempFile.deleteOnExit();
			return response.build();
		}
		result.setMetadata(metadata);
		return Response.ok(result).build();
	}
	
	@GET
	@Path("/laporan/payroll/calonpegawai/tahun/{tahun}/bulan/{bulan}/kodeoffice/{kodeoffice}/namaoffice/{namaoffice}/{token}")
	@Produces({ MediaType.APPLICATION_OCTET_STREAM, "application/json" })
	public Response Laporan_Payroll_Calon_Pegawai(@Context HttpHeaders headers, 
			@PathParam("token") String token, 
			@PathParam("tahun") Integer tahun, 
			@PathParam("bulan") Integer bulan, 
			@PathParam("kodeoffice") String kodeoffice,
			@PathParam("namaoffice") String namaoffice) {
		Result<Object> result = new Result<Object>();
		Metadata metadata = new Metadata();
		if (SharedMethod.VerifyToken2(token, metadata)) {
			Connection con = null;
			ResultSet rs = null;
			CallableStatement cs = null;
			String query = null;
			
			String[] namabulan={"","Januari","Februari","Maret","April","Mei","Juni","Juli","Agustus","September","Oktober","Nopember","Desember"};
			/*=====================================================================*/
			Workbook workbook = null;
			try {
				workbook = new XSSFWorkbook();
				Sheet sheet = workbook.createSheet("Sheet1");
				
				CellStyle centerStyle = workbook.createCellStyle();
				centerStyle.setAlignment(HorizontalAlignment.CENTER);
				
				Integer baris = 0;
				Row row;
				row = sheet.createRow(baris);
				row.createCell(0).setCellValue("Daftar Pembayaran Penghasilan Calon Pegawai".toUpperCase());
				baris++;
				row = sheet.createRow(baris);
				row.createCell(0).setCellValue(namabulan[bulan].toUpperCase() + " " + tahun);
				baris++;
				row = sheet.createRow(baris);
				row.createCell(0).setCellValue(namaoffice.toUpperCase());
				
				baris++;
				baris++;
				row = sheet.createRow(baris);
				row.createCell(0).setCellValue("No");
				sheet.addMergedRegion(new CellRangeAddress(baris, (baris+1), 0, 0));
				row.createCell(1).setCellValue("NPP");
				sheet.addMergedRegion(new CellRangeAddress(baris, (baris+1), 1, 1));
				row.createCell(2).setCellValue("Nama");
				sheet.addMergedRegion(new CellRangeAddress(baris, (baris+1), 2, 2));
				row.createCell(3).setCellValue("Grade - Skala");
				sheet.addMergedRegion(new CellRangeAddress(baris, (baris+1), 3, 3));
				row.createCell(4).setCellValue("Indeks Konjungtur");
				sheet.addMergedRegion(new CellRangeAddress(baris, (baris+1), 4, 4));
				row.createCell(5).setCellValue("Gaji Pokok (Base)");
				sheet.addMergedRegion(new CellRangeAddress(baris, (baris+1), 5, 5));
				row.createCell(6).setCellValue("% Predikat Kinerja");
				sheet.addMergedRegion(new CellRangeAddress(baris, (baris+1), 6, 6));
				row.createCell(7).setCellValue("Tunjangan Prestasi (Base)");
				sheet.addMergedRegion(new CellRangeAddress(baris, (baris+1), 7, 7));
				row.createCell(8).setCellValue("Pendapatan");
				sheet.addMergedRegion(new CellRangeAddress(baris, baris, 8, (8+10)));
				row.createCell(19).setCellValue("Potongan");
				sheet.addMergedRegion(new CellRangeAddress(baris, baris, 19, (19+15)));
				row.createCell(35).setCellValue("THP");
				sheet.addMergedRegion(new CellRangeAddress(baris, (baris+1), 35, 35));
				row.createCell(36).setCellValue("No Rekening");
				sheet.addMergedRegion(new CellRangeAddress(baris, (baris+1), 36, 36));
				baris++;
				row = sheet.createRow(baris);
				row.createCell(8).setCellValue("Gaji Pokok");
				row.createCell(9).setCellValue("Tunjangan Prestasi");
				row.createCell(10).setCellValue("Tunjangan Utilitas");
				row.createCell(11).setCellValue("Tunjangan Jabatan");
				row.createCell(12).setCellValue("Tunjangan Daerah Terpencil");
				//row.createCell(13).setCellValue("Tunjangan Khusus");
				row.createCell(13).setCellValue("Tunjangan BBM");
				row.createCell(14).setCellValue("Tunjangan Komunikasi");
				row.createCell(15).setCellValue("Tunjangan Transportasi");
				row.createCell(16).setCellValue("Bantuan Perumahan");
				row.createCell(17).setCellValue("Pendapatan Lain - lain");
				row.createCell(18).setCellValue("Jumlah");
				
				row.createCell(19).setCellValue("JPK Pegawai");
				row.createCell(20).setCellValue("JPK-CoB Tambahan");
				row.createCell(21).setCellValue("JPK-CoB Mantan Pegawai");
				row.createCell(22).setCellValue("BPJS-K Keluarga Tambahan");
				row.createCell(23).setCellValue("BPJS-TK");
				row.createCell(24).setCellValue("JHT");
				//row.createCell(24).setCellValue("Iuran Korpri");
				row.createCell(25).setCellValue("Iuran IIKA");
				row.createCell(26).setCellValue("Koperasi Bhakti");
				row.createCell(27).setCellValue("Kekurangan SPJ UMK");
				row.createCell(28).setCellValue("Carloan");
				row.createCell(29).setCellValue("Potongan Ketidakdisiplinan");
				row.createCell(30).setCellValue("Kelebihan Uang Pulsa");
				row.createCell(31).setCellValue("Baznas");
				row.createCell(32).setCellValue("Donasi");
				row.createCell(33).setCellValue("Potongan Lain - lain");
				row.createCell(34).setCellValue("Jumlah");
				
				CellStyle cellStyleTgl = workbook.createCellStyle();
				CreationHelper createHelper = workbook.getCreationHelper();
				cellStyleTgl.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
				
				try {
					query = "exec payroll.sp_laporan_rekap_calonpegawai ?, ?, ?";
					con = new Koneksi().getConnection();
					cs = con.prepareCall(query);
					cs.setInt(1, tahun);
					cs.setInt(2, bulan);
					cs.setString(3, kodeoffice);
					rs = cs.executeQuery();
					Integer no = 0;
					while (rs.next()) {
						no++;
						baris++;
						row = sheet.createRow(baris);
						row.createCell(0).setCellValue(no);
						row.createCell(1).setCellValue(rs.getString("npp"));
						row.createCell(2).setCellValue(rs.getString("namalengkap"));
						row.createCell(3).setCellValue(rs.getString("namagrade") + " - " + rs.getString("namasubgrade"));
						row.createCell(4).setCellValue(rs.getInt("ik"));
						row.createCell(5).setCellValue(rs.getBigDecimal("gajipokok").doubleValue());
						row.createCell(6).setCellValue(rs.getInt("persentupres"));
						row.createCell(7).setCellValue(rs.getBigDecimal("tupres100").doubleValue());
						
						row.createCell(8).setCellValue(rs.getBigDecimal("gapok").doubleValue());
						row.createCell(9).setCellValue(rs.getBigDecimal("tupres").doubleValue());
						row.createCell(10).setCellValue(rs.getBigDecimal("utilitas").doubleValue());
						row.createCell(11).setCellValue(rs.getBigDecimal("tunjjab").doubleValue());
						row.createCell(12).setCellValue(rs.getBigDecimal("tunjdacil").doubleValue());
						//row.createCell(13).setCellValue(rs.getBigDecimal("tunjkhusus").doubleValue());
						row.createCell(13).setCellValue(rs.getBigDecimal("tunjbbm").doubleValue());
						row.createCell(14).setCellValue(rs.getBigDecimal("tunjkomunikasi").doubleValue());
						row.createCell(15).setCellValue(rs.getBigDecimal("tunjtransportasi").doubleValue());
						row.createCell(16).setCellValue(rs.getBigDecimal("bantuanperumahan").doubleValue());
						row.createCell(17).setCellValue(rs.getBigDecimal("pendapatanlain").doubleValue());
						row.createCell(18).setCellValue(rs.getBigDecimal("totalpendapatan").doubleValue());
						
						row.createCell(19).setCellValue(rs.getBigDecimal("jpkpegawai").doubleValue());
						row.createCell(20).setCellValue(rs.getBigDecimal("jpkcobtambahan").doubleValue());
						row.createCell(21).setCellValue(rs.getBigDecimal("jpkcobmantan").doubleValue());
						row.createCell(22).setCellValue(rs.getBigDecimal("bpjsktambahan").doubleValue());
						row.createCell(23).setCellValue(rs.getBigDecimal("bpjstk").doubleValue());
						row.createCell(24).setCellValue(rs.getBigDecimal("jht").doubleValue());
						//row.createCell(26).setCellValue(rs.getBigDecimal("korpri").doubleValue());
						row.createCell(25).setCellValue(rs.getBigDecimal("iika").doubleValue());
						row.createCell(26).setCellValue(rs.getBigDecimal("koperasi").doubleValue());
						row.createCell(27).setCellValue(rs.getBigDecimal("kekuranganspj").doubleValue());
						row.createCell(28).setCellValue(rs.getBigDecimal("carloan").doubleValue());
						row.createCell(29).setCellValue(rs.getBigDecimal("potketidakdisiplinan").doubleValue());
						row.createCell(30).setCellValue(rs.getBigDecimal("kelebihanuangpulsa").doubleValue());
						row.createCell(31).setCellValue(rs.getBigDecimal("baznas").doubleValue());
						row.createCell(32).setCellValue(rs.getBigDecimal("donasi").doubleValue());
						row.createCell(33).setCellValue(rs.getBigDecimal("potonganlain").doubleValue());
						row.createCell(34).setCellValue(rs.getBigDecimal("totalpotongan").doubleValue());
						row.createCell(35).setCellValue(rs.getBigDecimal("thp").doubleValue());
						row.createCell(36).setCellValue(rs.getString("norek"));
					}
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
				
				tempFile = File.createTempFile("temp", "hcis");
				FileOutputStream fos = null;
				fos = new FileOutputStream(tempFile);
				workbook.write(fos);
			fos.close();
			} catch (Exception e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
				e.printStackTrace();
			}
			finally {
				if (workbook != null) {
					try {
						workbook.close();
					} catch (IOException e) {
					}
				}
			}
			/*=====================================================================*/
			
			ResponseBuilder response = Response.ok((Object) tempFile);
			response.header("Content-Disposition","attachment; filename=Laporan_Payroll_Calon_Pegawai_" + new Date().getTime() + ".xlsx");
			tempFile.deleteOnExit();
			return response.build();
		}
		result.setMetadata(metadata);
		return Response.ok(result).build();
	}
	
	@GET
	@Path("/laporan/payroll/ptt/tahun/{tahun}/bulan/{bulan}/kodeoffice/{kodeoffice}/namaoffice/{namaoffice}/{token}")
	@Produces({ MediaType.APPLICATION_OCTET_STREAM, "application/json" })
	public Response Laporan_Payroll_PTT(@Context HttpHeaders headers, 
			@PathParam("token") String token, 
			@PathParam("tahun") Integer tahun, 
			@PathParam("bulan") Integer bulan, 
			@PathParam("kodeoffice") String kodeoffice,
			@PathParam("namaoffice") String namaoffice) {
		Result<Object> result = new Result<Object>();
		Metadata metadata = new Metadata();
		if (SharedMethod.VerifyToken2(token, metadata)) {
			Connection con = null;
			ResultSet rs = null;
			CallableStatement cs = null;
			String query = null;
			
			String[] namabulan={"","Januari","Februari","Maret","April","Mei","Juni","Juli","Agustus","September","Oktober","Nopember","Desember"};
			/*=====================================================================*/
			Workbook workbook = null;
			try {
				workbook = new XSSFWorkbook();
				Sheet sheet = workbook.createSheet("Sheet1");
				
				CellStyle centerStyle = workbook.createCellStyle();
				centerStyle.setAlignment(HorizontalAlignment.CENTER);
				
				Integer baris = 0;
				Row row;
				row = sheet.createRow(baris);
				row.createCell(0).setCellValue("Daftar Pembayaran Penghasilan PTT".toUpperCase());
				baris++;
				row = sheet.createRow(baris);
				row.createCell(0).setCellValue(namabulan[bulan].toUpperCase() + " " + tahun);
				baris++;
				row = sheet.createRow(baris);
				row.createCell(0).setCellValue(namaoffice.toUpperCase());
				
				baris++;
				baris++;
				row = sheet.createRow(baris);
				row.createCell(0).setCellValue("No");
				sheet.addMergedRegion(new CellRangeAddress(baris, (baris+1), 0, 0));
				row.createCell(1).setCellValue("NPP");
				sheet.addMergedRegion(new CellRangeAddress(baris, (baris+1), 1, 1));
				row.createCell(2).setCellValue("Nama");
				sheet.addMergedRegion(new CellRangeAddress(baris, (baris+1), 2, 2));
				row.createCell(3).setCellValue("Grade - Skala");
				sheet.addMergedRegion(new CellRangeAddress(baris, (baris+1), 3, 3));
				row.createCell(4).setCellValue("Indeks Konjungtur");
				sheet.addMergedRegion(new CellRangeAddress(baris, (baris+1), 4, 4));
				row.createCell(5).setCellValue("Gaji Pokok (Base)");
				sheet.addMergedRegion(new CellRangeAddress(baris, (baris+1), 5, 5));
				row.createCell(6).setCellValue("Unit Kerja");
				sheet.addMergedRegion(new CellRangeAddress(baris, (baris+1), 6, 6));
				row.createCell(7).setCellValue("Nomor Kontrak");
				sheet.addMergedRegion(new CellRangeAddress(baris, (baris+1), 7, 7));
				row.createCell(8).setCellValue("Pendapatan");
				sheet.addMergedRegion(new CellRangeAddress(baris, baris, 8, (8+10)));
				row.createCell(19).setCellValue("Potongan");
				sheet.addMergedRegion(new CellRangeAddress(baris, baris, 19, (19+15)));
				row.createCell(35).setCellValue("THP");
				sheet.addMergedRegion(new CellRangeAddress(baris, (baris+1), 35, 35));
				row.createCell(36).setCellValue("No Rekening");
				sheet.addMergedRegion(new CellRangeAddress(baris, (baris+1), 36, 36));
				baris++;
				row = sheet.createRow(baris);
				row.createCell(8).setCellValue("Gaji Pokok");
				row.createCell(9).setCellValue("Tunjangan Prestasi");
				row.createCell(10).setCellValue("Tunjangan Utilitas");
				row.createCell(11).setCellValue("Tunjangan Jabatan");
				row.createCell(12).setCellValue("Tunjangan Daerah Terpencil");
				//row.createCell(13).setCellValue("Tunjangan Khusus");
				row.createCell(13).setCellValue("Tunjangan BBM");
				row.createCell(14).setCellValue("Tunjangan Komunikasi");
				row.createCell(15).setCellValue("Tunjangan Transportasi");
				row.createCell(16).setCellValue("Bantuan Perumahan");
				row.createCell(17).setCellValue("Pendapatan Lain - lain");
				row.createCell(18).setCellValue("Jumlah");
				
				row.createCell(19).setCellValue("JPK Pegawai");
				row.createCell(20).setCellValue("JPK-CoB Tambahan");
				row.createCell(21).setCellValue("JPK-CoB Mantan Pegawai");
				row.createCell(22).setCellValue("BPJS-K Keluarga Tambahan");
				row.createCell(23).setCellValue("BPJS-TK");
				row.createCell(24).setCellValue("JHT");
				//row.createCell(24).setCellValue("Iuran Korpri");
				row.createCell(25).setCellValue("Iuran IIKA");
				row.createCell(26).setCellValue("Koperasi Bhakti");
				row.createCell(27).setCellValue("Kekurangan SPJ UMK");
				row.createCell(28).setCellValue("Carloan");
				row.createCell(29).setCellValue("Potongan Ketidakdisiplinan");
				row.createCell(30).setCellValue("Kelebihan Uang Pulsa");
				row.createCell(31).setCellValue("Baznas");
				row.createCell(32).setCellValue("Donasi");
				row.createCell(33).setCellValue("Potongan Lain - lain");
				row.createCell(34).setCellValue("Jumlah");
				
				CellStyle cellStyleTgl = workbook.createCellStyle();
				CreationHelper createHelper = workbook.getCreationHelper();
				cellStyleTgl.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
				
				try {
					query = "exec payroll.sp_laporan_rekap_ptt ?, ?, ?";
					con = new Koneksi().getConnection();
					cs = con.prepareCall(query);
					cs.setInt(1, tahun);
					cs.setInt(2, bulan);
					cs.setString(3, kodeoffice);
					rs = cs.executeQuery();
					Integer no = 0;
					while (rs.next()) {
						no++;
						baris++;
						row = sheet.createRow(baris);
						row.createCell(0).setCellValue(no);
						row.createCell(1).setCellValue(rs.getString("npp"));
						row.createCell(2).setCellValue(rs.getString("namalengkap"));
						row.createCell(3).setCellValue(rs.getString("namagrade") + " - " + rs.getString("namasubgrade"));
						row.createCell(4).setCellValue(rs.getInt("ik"));
						row.createCell(5).setCellValue(rs.getBigDecimal("gajipokok").doubleValue());
						row.createCell(6).setCellValue(rs.getString("namaunitkerja"));
						row.createCell(7).setCellValue("");
						
						row.createCell(8).setCellValue(rs.getBigDecimal("gapok").doubleValue());
						row.createCell(9).setCellValue(rs.getBigDecimal("tupres").doubleValue());
						row.createCell(10).setCellValue(rs.getBigDecimal("utilitas").doubleValue());
						row.createCell(11).setCellValue(rs.getBigDecimal("tunjjab").doubleValue());
						row.createCell(12).setCellValue(rs.getBigDecimal("tunjdacil").doubleValue());
						//row.createCell(13).setCellValue(rs.getBigDecimal("tunjkhusus").doubleValue());
						row.createCell(13).setCellValue(rs.getBigDecimal("tunjbbm").doubleValue());
						row.createCell(14).setCellValue(rs.getBigDecimal("tunjkomunikasi").doubleValue());
						row.createCell(15).setCellValue(rs.getBigDecimal("tunjtransportasi").doubleValue());
						row.createCell(16).setCellValue(rs.getBigDecimal("bantuanperumahan").doubleValue());
						row.createCell(17).setCellValue(rs.getBigDecimal("pendapatanlain").doubleValue());
						row.createCell(18).setCellValue(rs.getBigDecimal("totalpendapatan").doubleValue());
						
						row.createCell(19).setCellValue(rs.getBigDecimal("jpkpegawai").doubleValue());
						row.createCell(20).setCellValue(rs.getBigDecimal("jpkcobtambahan").doubleValue());
						row.createCell(21).setCellValue(rs.getBigDecimal("jpkcobmantan").doubleValue());
						row.createCell(22).setCellValue(rs.getBigDecimal("bpjsktambahan").doubleValue());
						row.createCell(23).setCellValue(rs.getBigDecimal("bpjstk").doubleValue());
						row.createCell(24).setCellValue(rs.getBigDecimal("jht").doubleValue());
						//row.createCell(24).setCellValue(rs.getBigDecimal("korpri").doubleValue());
						row.createCell(25).setCellValue(rs.getBigDecimal("iika").doubleValue());
						row.createCell(26).setCellValue(rs.getBigDecimal("koperasi").doubleValue());
						row.createCell(27).setCellValue(rs.getBigDecimal("kekuranganspj").doubleValue());
						row.createCell(28).setCellValue(rs.getBigDecimal("carloan").doubleValue());
						row.createCell(29).setCellValue(rs.getBigDecimal("potketidakdisiplinan").doubleValue());
						row.createCell(30).setCellValue(rs.getBigDecimal("kelebihanuangpulsa").doubleValue());
						row.createCell(31).setCellValue(rs.getBigDecimal("baznas").doubleValue());
						row.createCell(32).setCellValue(rs.getBigDecimal("donasi").doubleValue());
						row.createCell(33).setCellValue(rs.getBigDecimal("potonganlain").doubleValue());
						row.createCell(34).setCellValue(rs.getBigDecimal("totalpotongan").doubleValue());
						row.createCell(35).setCellValue(rs.getBigDecimal("thp").doubleValue());
						row.createCell(36).setCellValue(rs.getString("norek"));
					}
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
				
				tempFile = File.createTempFile("temp", "hcis");
				FileOutputStream fos = null;
				fos = new FileOutputStream(tempFile);
				workbook.write(fos);
			fos.close();
			} catch (Exception e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
				e.printStackTrace();
			}
			finally {
				if (workbook != null) {
					try {
						workbook.close();
					} catch (IOException e) {
					}
				}
			}
			/*=====================================================================*/
			
			ResponseBuilder response = Response.ok((Object) tempFile);
			response.header("Content-Disposition","attachment; filename=Laporan_Payroll_PTT_" + new Date().getTime() + ".xlsx");
			tempFile.deleteOnExit();
			return response.build();
		}
		result.setMetadata(metadata);
		return Response.ok(result).build();
	}
	
	@GET
	@Path("/laporan/payroll/potcarloan/tahun/{tahun}/bulan/{bulan}/kodeoffice/{kodeoffice}/namaoffice/{namaoffice}/{token}")
	@Produces({ MediaType.APPLICATION_OCTET_STREAM, "application/json" })
	public Response Laporan_Payroll_Pot_Carloan(@Context HttpHeaders headers, 
			@PathParam("token") String token, 
			@PathParam("tahun") Integer tahun, 
			@PathParam("bulan") Integer bulan, 
			@PathParam("kodeoffice") String kodeoffice,
			@PathParam("namaoffice") String namaoffice) {
		Result<Object> result = new Result<Object>();
		Metadata metadata = new Metadata();
		if (SharedMethod.VerifyToken2(token, metadata)) {
			Connection con = null;
			ResultSet rs = null;
			CallableStatement cs = null;
			String query = null;
			
			String[] namabulan={"","Januari","Februari","Maret","April","Mei","Juni","Juli","Agustus","September","Oktober","Nopember","Desember"};
			/*=====================================================================*/
			Workbook workbook = null;
			try {
				workbook = new XSSFWorkbook();
				Sheet sheet = workbook.createSheet("Sheet1");
				
				CellStyle centerStyle = workbook.createCellStyle();
				centerStyle.setAlignment(HorizontalAlignment.CENTER);
				
				Integer baris = 0;
				Row row;
				row = sheet.createRow(baris);
				row.createCell(0).setCellValue("Laporan Potongan Carloan".toUpperCase());
				baris++;
				row = sheet.createRow(baris);
				row.createCell(0).setCellValue(namabulan[bulan].toUpperCase() + " " + tahun);
				baris++;
				row = sheet.createRow(baris);
				row.createCell(0).setCellValue(namaoffice.toUpperCase());
				
				baris++;
				baris++;
				row = sheet.createRow(baris);
				row.createCell(0).setCellValue("No");
				row.createCell(1).setCellValue("NPP");
				row.createCell(2).setCellValue("Nama");
				row.createCell(3).setCellValue("Unit Kerja");
				row.createCell(4).setCellValue("Nilai");
				
				CellStyle cellStyleTgl = workbook.createCellStyle();
				CreationHelper createHelper = workbook.getCreationHelper();
				cellStyleTgl.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
				
				try {
					query = "exec payroll.sp_laporan_pot_carloan ?, ?, ?";
					con = new Koneksi().getConnection();
					cs = con.prepareCall(query);
					cs.setInt(1, tahun);
					cs.setInt(2, bulan);
					cs.setString(3, kodeoffice);
					rs = cs.executeQuery();
					Integer no = 0;
					while (rs.next()) {
						no++;
						baris++;
						row = sheet.createRow(baris);
						row.createCell(0).setCellValue(no);
						row.createCell(1).setCellValue(rs.getString("npp"));
						row.createCell(2).setCellValue(rs.getString("namalengkap"));
						row.createCell(3).setCellValue(rs.getString("namaunitkerja"));
						row.createCell(4).setCellValue(rs.getBigDecimal("carloan").doubleValue());
					}
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
				
				tempFile = File.createTempFile("temp", "hcis");
				FileOutputStream fos = null;
				fos = new FileOutputStream(tempFile);
				workbook.write(fos);
			fos.close();
			} catch (Exception e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
				e.printStackTrace();
			}
			finally {
				if (workbook != null) {
					try {
						workbook.close();
					} catch (IOException e) {
					}
				}
			}
			/*=====================================================================*/
			
			ResponseBuilder response = Response.ok((Object) tempFile);
			response.header("Content-Disposition","attachment; filename=Laporan_Payroll_Pot_Carloan_" + new Date().getTime() + ".xlsx");
			tempFile.deleteOnExit();
			return response.build();
		}
		result.setMetadata(metadata);
		return Response.ok(result).build();
	}
	
	@GET
	@Path("/laporan/payroll/potzakat/tahun/{tahun}/bulan/{bulan}/kodeoffice/{kodeoffice}/namaoffice/{namaoffice}/{token}")
	@Produces({ MediaType.APPLICATION_OCTET_STREAM, "application/json" })
	public Response Laporan_Payroll_Pot_Zakat(@Context HttpHeaders headers, 
			@PathParam("token") String token, 
			@PathParam("tahun") Integer tahun, 
			@PathParam("bulan") Integer bulan, 
			@PathParam("kodeoffice") String kodeoffice,
			@PathParam("namaoffice") String namaoffice) {
		Result<Object> result = new Result<Object>();
		Metadata metadata = new Metadata();
		if (SharedMethod.VerifyToken2(token, metadata)) {
			Connection con = null;
			ResultSet rs = null;
			CallableStatement cs = null;
			String query = null;
			
			String[] namabulan={"","Januari","Februari","Maret","April","Mei","Juni","Juli","Agustus","September","Oktober","Nopember","Desember"};
			/*=====================================================================*/
			Workbook workbook = null;
			try {
				workbook = new XSSFWorkbook();
				Sheet sheet = workbook.createSheet("Sheet1");
				
				CellStyle centerStyle = workbook.createCellStyle();
				centerStyle.setAlignment(HorizontalAlignment.CENTER);
				
				Integer baris = 0;
				Row row;
				row = sheet.createRow(baris);
				row.createCell(0).setCellValue("Laporan Potongan Zakat".toUpperCase());
				baris++;
				row = sheet.createRow(baris);
				row.createCell(0).setCellValue(namabulan[bulan].toUpperCase() + " " + tahun);
				baris++;
				row = sheet.createRow(baris);
				row.createCell(0).setCellValue(namaoffice.toUpperCase());
				
				baris++;
				baris++;
				row = sheet.createRow(baris);
				row.createCell(0).setCellValue("No");
				row.createCell(1).setCellValue("NPP");
				row.createCell(2).setCellValue("Nama");
				row.createCell(3).setCellValue("Jabatan");
				row.createCell(4).setCellValue("Kantor");
				row.createCell(5).setCellValue("HP");
				row.createCell(6).setCellValue("NIK");
				row.createCell(7).setCellValue("Nilai");
				
				CellStyle cellStyleTgl = workbook.createCellStyle();
				CreationHelper createHelper = workbook.getCreationHelper();
				cellStyleTgl.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
				
				try {
					query = "exec payroll.sp_laporan_pot_zakat ?, ?, ?";
					con = new Koneksi().getConnection();
					cs = con.prepareCall(query);
					cs.setInt(1, tahun);
					cs.setInt(2, bulan);
					cs.setString(3, kodeoffice);
					rs = cs.executeQuery();
					Integer no = 0;
					while (rs.next()) {
						no++;
						baris++;
						row = sheet.createRow(baris);
						row.createCell(0).setCellValue(no);
						row.createCell(1).setCellValue(rs.getString("npp"));
						row.createCell(2).setCellValue(rs.getString("namalengkap"));
						row.createCell(3).setCellValue(rs.getString("namajobtitle"));
						row.createCell(4).setCellValue(rs.getString("namaoffice"));
						row.createCell(5).setCellValue(rs.getString("hp"));
						row.createCell(6).setCellValue(rs.getString("noidentitas"));
						row.createCell(7).setCellValue(rs.getBigDecimal("zakat").doubleValue());
					}
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
				
				tempFile = File.createTempFile("temp", "hcis");
				FileOutputStream fos = null;
				fos = new FileOutputStream(tempFile);
				workbook.write(fos);
			fos.close();
			} catch (Exception e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
				e.printStackTrace();
			}
			finally {
				if (workbook != null) {
					try {
						workbook.close();
					} catch (IOException e) {
					}
				}
			}
			/*=====================================================================*/
			
			ResponseBuilder response = Response.ok((Object) tempFile);
			response.header("Content-Disposition","attachment; filename=Laporan_Payroll_Pot_Zakat_" + new Date().getTime() + ".xlsx");
			tempFile.deleteOnExit();
			return response.build();
		}
		result.setMetadata(metadata);
		return Response.ok(result).build();
	}
	
	@GET
	@Path("/laporan/payroll/potkoperasi/tahun/{tahun}/bulan/{bulan}/kodeoffice/{kodeoffice}/namaoffice/{namaoffice}/{token}")
	@Produces({ MediaType.APPLICATION_OCTET_STREAM, "application/json" })
	public Response Laporan_Payroll_Pot_Koperasi(@Context HttpHeaders headers, 
			@PathParam("token") String token, 
			@PathParam("tahun") Integer tahun, 
			@PathParam("bulan") Integer bulan, 
			@PathParam("kodeoffice") String kodeoffice,
			@PathParam("namaoffice") String namaoffice) {
		Result<Object> result = new Result<Object>();
		Metadata metadata = new Metadata();
		if (SharedMethod.VerifyToken2(token, metadata)) {
			Connection con = null;
			ResultSet rs = null;
			CallableStatement cs = null;
			String query = null;
			
			String[] namabulan={"","Januari","Februari","Maret","April","Mei","Juni","Juli","Agustus","September","Oktober","Nopember","Desember"};
			/*=====================================================================*/
			Workbook workbook = null;
			try {
				workbook = new XSSFWorkbook();
				Sheet sheet = workbook.createSheet("Sheet1");
				
				CellStyle centerStyle = workbook.createCellStyle();
				centerStyle.setAlignment(HorizontalAlignment.CENTER);
				
				Integer baris = 0;
				Row row;
				row = sheet.createRow(baris);
				row.createCell(0).setCellValue("Laporan Potongan Koperasi".toUpperCase());
				baris++;
				row = sheet.createRow(baris);
				row.createCell(0).setCellValue(namabulan[bulan].toUpperCase() + " " + tahun);
				baris++;
				row = sheet.createRow(baris);
				row.createCell(0).setCellValue(namaoffice.toUpperCase());
				
				baris++;
				baris++;
				row = sheet.createRow(baris);
				row.createCell(0).setCellValue("No");
				row.createCell(1).setCellValue("NPP");
				row.createCell(2).setCellValue("Nama");
				row.createCell(3).setCellValue("Jabatan");
				row.createCell(4).setCellValue("Kantor");
				row.createCell(5).setCellValue("HP");
				row.createCell(6).setCellValue("Nilai");
				
				CellStyle cellStyleTgl = workbook.createCellStyle();
				CreationHelper createHelper = workbook.getCreationHelper();
				cellStyleTgl.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
				
				try {
					query = "exec payroll.sp_laporan_pot_koperasi ?, ?, ?";
					con = new Koneksi().getConnection();
					cs = con.prepareCall(query);
					cs.setInt(1, tahun);
					cs.setInt(2, bulan);
					cs.setString(3, kodeoffice);
					rs = cs.executeQuery();
					Integer no = 0;
					while (rs.next()) {
						no++;
						baris++;
						row = sheet.createRow(baris);
						row.createCell(0).setCellValue(no);
						row.createCell(1).setCellValue(rs.getString("npp"));
						row.createCell(2).setCellValue(rs.getString("namalengkap"));
						row.createCell(3).setCellValue(rs.getString("namajobtitle"));
						row.createCell(4).setCellValue(rs.getString("namaoffice"));
						row.createCell(5).setCellValue(rs.getString("hp"));
						row.createCell(6).setCellValue(rs.getBigDecimal("koperasi").doubleValue());
					}
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
				
				tempFile = File.createTempFile("temp", "hcis");
				FileOutputStream fos = null;
				fos = new FileOutputStream(tempFile);
				workbook.write(fos);
			fos.close();
			} catch (Exception e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
				e.printStackTrace();
			}
			finally {
				if (workbook != null) {
					try {
						workbook.close();
					} catch (IOException e) {
					}
				}
			}
			/*=====================================================================*/
			
			ResponseBuilder response = Response.ok((Object) tempFile);
			response.header("Content-Disposition","attachment; filename=Laporan_Payroll_Pot_Koperasi_" + new Date().getTime() + ".xlsx");
			tempFile.deleteOnExit();
			return response.build();
		}
		result.setMetadata(metadata);
		return Response.ok(result).build();
	}
	
	@GET
	@Path("/laporan/payroll/setorbank/tahun/{tahun}/bulan/{bulan}/kodeoffice/{kodeoffice}/namaoffice/{namaoffice}/{token}")
	@Produces({ MediaType.APPLICATION_OCTET_STREAM, "application/json" })
	public Response Laporan_Payroll_Setor_Bank(@Context HttpHeaders headers, 
			@PathParam("token") String token, 
			@PathParam("tahun") Integer tahun, 
			@PathParam("bulan") Integer bulan, 
			@PathParam("kodeoffice") String kodeoffice,
			@PathParam("namaoffice") String namaoffice) {
		Result<Object> result = new Result<Object>();
		Metadata metadata = new Metadata();
		if (SharedMethod.VerifyToken2(token, metadata)) {
			Connection con = null;
			ResultSet rs = null;
			CallableStatement cs = null;
			String query = null;
			
			String[] namabulan={"","Januari","Februari","Maret","April","Mei","Juni","Juli","Agustus","September","Oktober","Nopember","Desember"};
			/*=====================================================================*/
			Workbook workbook = null;
			try {
				workbook = new XSSFWorkbook();
				Sheet sheet = workbook.createSheet("Sheet1");
				
				CellStyle centerStyle = workbook.createCellStyle();
				centerStyle.setAlignment(HorizontalAlignment.CENTER);
				
				Integer baris = 0;
				Row row;
				row = sheet.createRow(baris);
				row.createCell(0).setCellValue("Daftar Penghasilan Pegawai".toUpperCase());
				baris++;
				row = sheet.createRow(baris);
				row.createCell(0).setCellValue(namabulan[bulan].toUpperCase() + " " + tahun);
				baris++;
				row = sheet.createRow(baris);
				row.createCell(0).setCellValue(namaoffice.toUpperCase());
				
				baris++;
				baris++;
				row = sheet.createRow(baris);
				row.createCell(0).setCellValue("No");
				row.createCell(1).setCellValue("NPP");
				row.createCell(2).setCellValue("Nama");
				row.createCell(3).setCellValue("THP");
				row.createCell(4).setCellValue("Nama Bank");
				row.createCell(5).setCellValue("No Rekening");
				
				CellStyle cellStyleTgl = workbook.createCellStyle();
				CreationHelper createHelper = workbook.getCreationHelper();
				cellStyleTgl.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
				
				try {
					query = "exec payroll.sp_laporan_setor_bank ?, ?, ?";
					con = new Koneksi().getConnection();
					cs = con.prepareCall(query);
					cs.setInt(1, tahun);
					cs.setInt(2, bulan);
					cs.setString(3, kodeoffice);
					rs = cs.executeQuery();
					Integer no = 0;
					while (rs.next()) {
						no++;
						baris++;
						row = sheet.createRow(baris);
						row.createCell(0).setCellValue(no);
						row.createCell(1).setCellValue(rs.getString("npp"));
						row.createCell(2).setCellValue(rs.getString("namalengkap"));
						row.createCell(3).setCellValue(rs.getBigDecimal("thp").doubleValue());
						row.createCell(4).setCellValue(rs.getString("namabank"));
						row.createCell(5).setCellValue(rs.getString("norek"));
					}
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
				
				tempFile = File.createTempFile("temp", "hcis");
				FileOutputStream fos = null;
				fos = new FileOutputStream(tempFile);
				workbook.write(fos);
			fos.close();
			} catch (Exception e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
				e.printStackTrace();
			}
			finally {
				if (workbook != null) {
					try {
						workbook.close();
					} catch (IOException e) {
					}
				}
			}
			/*=====================================================================*/
			
			ResponseBuilder response = Response.ok((Object) tempFile);
			response.header("Content-Disposition","attachment; filename=Laporan_Payroll_Pegawai_SetorBank_" + new Date().getTime() + ".xlsx");
			tempFile.deleteOnExit();
			return response.build();
		}
		result.setMetadata(metadata);
		return Response.ok(result).build();
	}
	
	@GET
	@Path("/laporan/payroll/pajak/tahun/{tahun}/bulan/{bulan}/kodeoffice/{kodeoffice}/namaoffice/{namaoffice}/{token}")
	@Produces({ MediaType.APPLICATION_OCTET_STREAM, "application/json" })
	public Response Laporan_Payroll_Pajak(@Context HttpHeaders headers, 
			@PathParam("token") String token, 
			@PathParam("tahun") Integer tahun, 
			@PathParam("bulan") Integer bulan, 
			@PathParam("kodeoffice") String kodeoffice,
			@PathParam("namaoffice") String namaoffice) {
		Result<Object> result = new Result<Object>();
		Metadata metadata = new Metadata();
		if (SharedMethod.VerifyToken2(token, metadata)) {
			Connection con = null;
			ResultSet rs = null;
			CallableStatement cs = null;
			String query = null;
			
			String[] namabulan={"","Januari","Februari","Maret","April","Mei","Juni","Juli","Agustus","September","Oktober","Nopember","Desember"};
			/*=====================================================================*/
			Workbook workbook = null;
			try {
				workbook = new XSSFWorkbook();
				Sheet sheet = workbook.createSheet("Sheet1");
				
				CellStyle centerStyle = workbook.createCellStyle();
				centerStyle.setAlignment(HorizontalAlignment.CENTER);
				
				Integer baris = 0;
				Row row;
				row = sheet.createRow(baris);
				row.createCell(0).setCellValue("Laporan Pajak Payroll".toUpperCase());
				baris++;
				row = sheet.createRow(baris);
				row.createCell(0).setCellValue(namabulan[bulan].toUpperCase() + " " + tahun);
				baris++;
				row = sheet.createRow(baris);
				row.createCell(0).setCellValue(namaoffice.toUpperCase());
				
				baris++;
				baris++;
				row = sheet.createRow(baris);
				row.createCell(0).setCellValue("No");
				row.createCell(1).setCellValue("Kantor");
				row.createCell(2).setCellValue("NPP");
				row.createCell(3).setCellValue("Nama");
				row.createCell(4).setCellValue("NIK");
				row.createCell(5).setCellValue("NPWP");
				row.createCell(6).setCellValue("Tanggal Melaksanakan Tugas");
				row.createCell(7).setCellValue("L/P");
				row.createCell(8).setCellValue("Status");
				row.createCell(9).setCellValue("Jabatan");
				row.createCell(10).setCellValue("Unit Kerja");
				row.createCell(11).setCellValue("Asal KC");
				row.createCell(12).setCellValue("Alamat");
				row.createCell(13).setCellValue("Gapok x IK");
				row.createCell(14).setCellValue("Tupres x IK x HK");
				row.createCell(15).setCellValue("Utilitas");
				row.createCell(16).setCellValue("Tunjangan Jabatan");
				row.createCell(17).setCellValue("BBM");
				row.createCell(18).setCellValue("Tanggungan BPJS Kes Badan (4%)");
				row.createCell(19).setCellValue("Tanggungan Badan COB Inhealth");
				row.createCell(20).setCellValue("JKK BPJS TK Tanggungan Badan");
				row.createCell(21).setCellValue("JKM BPJS TK Tanggungan Badan");
				row.createCell(22).setCellValue("JHT BPJS TK Tanggungan Pegawai");
				row.createCell(23).setCellValue("JP BPJS TK (1%)"); //THT BPJS TK (1%) 
				row.createCell(24).setCellValue("JHT Jiwasraya (7,5%)");
				
				CellStyle cellStyleTgl = workbook.createCellStyle();
				CreationHelper createHelper = workbook.getCreationHelper();
				cellStyleTgl.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
				
				try {
					query = "exec payroll.sp_laporan_pajak ?, ?, ?";
					con = new Koneksi().getConnection();
					cs = con.prepareCall(query);
					cs.setInt(1, tahun);
					cs.setInt(2, bulan);
					cs.setString(3, kodeoffice);
					rs = cs.executeQuery();
					Integer no = 0;
					while (rs.next()) {
						no++;
						baris++;
						row = sheet.createRow(baris);
						row.createCell(0).setCellValue(no);
						row.createCell(1).setCellValue(rs.getString("namaoffice"));
						row.createCell(2).setCellValue(rs.getString("npp"));
						row.createCell(3).setCellValue(rs.getString("namalengkap"));
						row.createCell(4).setCellValue(rs.getString("nik"));
						row.createCell(5).setCellValue(rs.getString("npwp"));
						if(rs.getDate("tmttugas")!=null) {
							Cell cellTgl = row.createCell(6);
							cellTgl.setCellValue(rs.getDate("tmttugas"));
							cellTgl.setCellStyle(cellStyleTgl);
						}
						row.createCell(7).setCellValue(rs.getString("jk"));
						row.createCell(8).setCellValue(rs.getString("statuskawin"));
						row.createCell(9).setCellValue(rs.getString("namajobtitle"));
						row.createCell(10).setCellValue(rs.getString("namaunitkerja"));
						row.createCell(11).setCellValue(rs.getString("asaloffice"));
						row.createCell(12).setCellValue(rs.getString("alamat"));
						row.createCell(13).setCellValue(rs.getBigDecimal("gapok").doubleValue());
						row.createCell(14).setCellValue(rs.getBigDecimal("tupres").doubleValue());
						row.createCell(15).setCellValue(rs.getBigDecimal("utilitas").doubleValue());
						row.createCell(16).setCellValue(rs.getBigDecimal("tunjjab").doubleValue());
						row.createCell(17).setCellValue(rs.getBigDecimal("tunjbbm").doubleValue());
						row.createCell(18).setCellValue(rs.getBigDecimal("potbadan_bpjsk").doubleValue());
						row.createCell(19).setCellValue(rs.getBigDecimal("potbadan_cob_inhealth").doubleValue());
						row.createCell(20).setCellValue(rs.getBigDecimal("potbadan_jkk_bpjstk").doubleValue());
						row.createCell(21).setCellValue(rs.getBigDecimal("potbadan_jkm_bpjstk").doubleValue());
						row.createCell(22).setCellValue(rs.getBigDecimal("pot_bpjstk_jht").doubleValue());
						row.createCell(23).setCellValue(rs.getBigDecimal("pot_bpjstk_tht").doubleValue());
						row.createCell(24).setCellValue(rs.getBigDecimal("jiwasraya_jht").doubleValue());
					}
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
				
				tempFile = File.createTempFile("temp", "hcis");
				FileOutputStream fos = null;
				fos = new FileOutputStream(tempFile);
				workbook.write(fos);
			fos.close();
			} catch (Exception e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
				e.printStackTrace();
			}
			finally {
				if (workbook != null) {
					try {
						workbook.close();
					} catch (IOException e) {
					}
				}
			}
			/*=====================================================================*/
			
			ResponseBuilder response = Response.ok((Object) tempFile);
			response.header("Content-Disposition","attachment; filename=Laporan_Pajak_Payroll_" + new Date().getTime() + ".xlsx");
			tempFile.deleteOnExit();
			return response.build();
		}
		result.setMetadata(metadata);
		return Response.ok(result).build();
	}
	
	@GET
	@Path("/laporan/payroll/donasi/tahun/{tahun}/bulan/{bulan}/kodeoffice/{kodeoffice}/namaoffice/{namaoffice}/{token}")
	@Produces({ MediaType.APPLICATION_OCTET_STREAM, "application/json" })
	public Response Laporan_Payroll_Pot_Donasi(@Context HttpHeaders headers, 
			@PathParam("token") String token, 
			@PathParam("tahun") Integer tahun, 
			@PathParam("bulan") Integer bulan, 
			@PathParam("kodeoffice") String kodeoffice,
			@PathParam("namaoffice") String namaoffice) {
		Result<Object> result = new Result<Object>();
		Metadata metadata = new Metadata();
		if (SharedMethod.VerifyToken2(token, metadata)) {
			Connection con = null;
			ResultSet rs = null;
			CallableStatement cs = null;
			String query = null;
			
			String[] namabulan={"","Januari","Februari","Maret","April","Mei","Juni","Juli","Agustus","September","Oktober","Nopember","Desember"};
			/*=====================================================================*/
			Workbook workbook = null;
			try {
				workbook = new XSSFWorkbook();
				Sheet sheet = workbook.createSheet("Sheet1");
				
				CellStyle centerStyle = workbook.createCellStyle();
				centerStyle.setAlignment(HorizontalAlignment.CENTER);
				
				Integer baris = 0;
				Row row;
				row = sheet.createRow(baris);
				row.createCell(0).setCellValue("Laporan Donasi Program PIPMP JKN".toUpperCase());
				baris++;
				row = sheet.createRow(baris);
				row.createCell(0).setCellValue(namabulan[bulan].toUpperCase() + " " + tahun);
				baris++;
				row = sheet.createRow(baris);
				row.createCell(0).setCellValue(namaoffice.toUpperCase());
				
				baris++;
				baris++;
				row = sheet.createRow(baris);
				row.createCell(0).setCellValue("No");
				row.createCell(1).setCellValue("NPP");
				row.createCell(2).setCellValue("Nama");
				row.createCell(3).setCellValue("Jabatan");
				row.createCell(4).setCellValue("Kantor");
				row.createCell(5).setCellValue("Program");
				row.createCell(6).setCellValue("Nilai");
				
				CellStyle cellStyleTgl = workbook.createCellStyle();
				CreationHelper createHelper = workbook.getCreationHelper();
				cellStyleTgl.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
				
				try {
					query = "exec payroll.sp_laporan_pot_donasi ?, ?, ?";
					con = new Koneksi().getConnection();
					cs = con.prepareCall(query);
					cs.setInt(1, tahun);
					cs.setInt(2, bulan);
					cs.setString(3, kodeoffice);
					rs = cs.executeQuery();
					Integer no = 0;
					while (rs.next()) {
						no++;
						baris++;
						row = sheet.createRow(baris);
						row.createCell(0).setCellValue(no);
						row.createCell(1).setCellValue(rs.getString("npp"));
						row.createCell(2).setCellValue(rs.getString("namalengkap"));
						row.createCell(3).setCellValue(rs.getString("namajobtitle"));
						row.createCell(4).setCellValue(rs.getString("namaoffice"));
						row.createCell(5).setCellValue(rs.getString("namapaketdonasi"));
						row.createCell(6).setCellValue(rs.getBigDecimal("donasi").doubleValue());
					}
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
				
				tempFile = File.createTempFile("temp", "hcis");
				FileOutputStream fos = null;
				fos = new FileOutputStream(tempFile);
				workbook.write(fos);
			fos.close();
			} catch (Exception e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
				e.printStackTrace();
			}
			finally {
				if (workbook != null) {
					try {
						workbook.close();
					} catch (IOException e) {
					}
				}
			}
			/*=====================================================================*/
			
			ResponseBuilder response = Response.ok((Object) tempFile);
			response.header("Content-Disposition","attachment; filename=Laporan_Payroll_Pot_Donasi_" + new Date().getTime() + ".xlsx");
			tempFile.deleteOnExit();
			return response.build();
		}
		result.setMetadata(metadata);
		return Response.ok(result).build();
	}
	
	@GET
	@Path("/format/{schema}/{tabel}/{token}")
	@Produces({ MediaType.APPLICATION_OCTET_STREAM, "application/json" })
	public Response DownloadFormat(@Context HttpHeaders headers, 
			@PathParam("token") String token,
			@PathParam("schema") String schema,
			@PathParam("tabel") String tabel) {
		Result<Object> result = new Result<Object>();
		Metadata metadata = new Metadata();
		if (SharedMethod.VerifyToken2(token, metadata)) {
			final Integer _MAXROWS = 1048576;
			//String[] namabulan={"","Januari","Februari","Maret","April","Mei","Juni","Juli","Agustus","September","Oktober","Nopember","Desember"};
			/*=====================================================================*/
			Workbook workbook = null;
			try {
				workbook = new XSSFWorkbook();
				Sheet sheet = workbook.createSheet("Sheet1");
				
				CellStyle centerStyle = workbook.createCellStyle();
				centerStyle.setAlignment(HorizontalAlignment.CENTER);
				Integer baris = 0;
				Row row;
				switch (schema) {
				case "organisasi":
					switch (tabel) {
					case "jobprefix":
						row = sheet.createRow(baris);
						row.createCell(0).setCellValue("No");
						row.createCell(1).setCellValue("Kode Job Prefix");
						row.createCell(2).setCellValue("Nama Job Prefix");
						row.createCell(3).setCellValue("is Struktural");
						row.createCell(4).setCellValue("Status Aktif");
						break;
					case "functionalscope":
						row = sheet.createRow(baris);
						row.createCell(0).setCellValue("No");
						row.createCell(1).setCellValue("Kode Functional Scope");
						row.createCell(2).setCellValue("Nama Functional Scope");
						row.createCell(3).setCellValue("Status Aktif");
						break;
					case "jobtitle":
						row = sheet.createRow(baris);
						row.createCell(0).setCellValue("No");
						row.createCell(1).setCellValue("Kode Job Prefix");
						row.createCell(2).setCellValue("Nama Job Prefix");
						row.createCell(3).setCellValue("Kode Functional Scope");
						row.createCell(4).setCellValue("Nama Functional Scope");
						row.createCell(5).setCellValue("Kode Job Title");
						row.createCell(6).setCellValue("Nama Job Title");
						row.createCell(7).setCellValue("Tunj. BBM [0=Tidak;1=Ya;2=Ya(adcost)]");
						row.createCell(8).setCellValue("Status Aktif");
						break;
					case "jobtitlerules":
						row = sheet.createRow(baris);
						row.createCell(0).setCellValue("No");
						row.createCell(1).setCellValue("Kode Job Title");
						row.createCell(2).setCellValue("Kode Job Title Asal");
						row.createCell(3).setCellValue("Kode Job Title Tujuan");
						row.createCell(4).setCellValue("Level");
						row.createCell(5).setCellValue("Utama");
						row.createCell(6).setCellValue("Status Aktif");
						break;
					case "grade":
						row = sheet.createRow(baris);
						row.createCell(0).setCellValue("No");
						row.createCell(1).setCellValue("Kode Grade");
						row.createCell(2).setCellValue("Nama Grade");
						row.createCell(3).setCellValue("Status Aktif");
						break;
					case "subgrade":
						row = sheet.createRow(baris);
						row.createCell(0).setCellValue("No");
						row.createCell(1).setCellValue("Kode Sub Grade");
						row.createCell(2).setCellValue("Nama Sub Grade");
						row.createCell(3).setCellValue("Kode Grade");
						row.createCell(4).setCellValue("Nama Grade");
						row.createCell(5).setCellValue("Gaji Pokok");
						row.createCell(6).setCellValue("Tunjangan Prestasi");
						row.createCell(7).setCellValue("Tunjangan Utilitas");
						row.createCell(8).setCellValue("Status Aktif");
						break;
					case "jobgrade":
						row = sheet.createRow(baris);
						row.createCell(0).setCellValue("No");
						row.createCell(1).setCellValue("Kode Job Grade");
						row.createCell(2).setCellValue("Tunjangan Jabatan");
						row.createCell(3).setCellValue("Uang Saku");
						row.createCell(4).setCellValue("Uang Lembur");
						row.createCell(5).setCellValue("Status Aktif");
						break;
					case "lokasikantor":
						row = sheet.createRow(baris);
						row.createCell(0).setCellValue("No");
						row.createCell(1).setCellValue("Kode Parent Lokasi Kantor");
						row.createCell(2).setCellValue("Nama Parent Lokasi Kantor");
						row.createCell(3).setCellValue("Kode Jenis Lokasi Kantor");
						row.createCell(4).setCellValue("Nama Jenis Lokasi Kantor");
						row.createCell(5).setCellValue("Kode Lokasi Kantor");
						row.createCell(6).setCellValue("Nama Lokasi Kantor");
						row.createCell(7).setCellValue("Alamat");
						row.createCell(8).setCellValue("Kodepos");
						row.createCell(9).setCellValue("Telp");
						row.createCell(10).setCellValue("Fax");
						row.createCell(11).setCellValue("Kode Dati2");
						row.createCell(12).setCellValue("Nama Dati2");
						row.createCell(13).setCellValue("Indek Konjungtur");
						row.createCell(14).setCellValue("Tunjangan Daerah Terpencil");
						row.createCell(15).setCellValue("Tunjangan Khusus");
						row.createCell(16).setCellValue("No Surat Eksternal");
						row.createCell(17).setCellValue("Status Aktif");
						break;
					case "unitkerja":
						row = sheet.createRow(baris);
						row.createCell(0).setCellValue("No");
						row.createCell(1).setCellValue("Kode Parent Unit Kerja");
						row.createCell(2).setCellValue("Nama Parent Unit Kerja");
						row.createCell(3).setCellValue("Kode Unit Kerja");
						row.createCell(4).setCellValue("Nama Unit Kerja");
						row.createCell(5).setCellValue("Kode Hirarki Unit Kerja");
						row.createCell(6).setCellValue("Nama Hirarki Unit Kerja");
						row.createCell(7).setCellValue("Kode Jenis Lokasi Kantor");
						row.createCell(8).setCellValue("Nama Jenis Lokasi Kantor");
						row.createCell(9).setCellValue("Kode Bagan Organisasi");
						row.createCell(10).setCellValue("Nama Bagan Organisasi");
						row.createCell(11).setCellValue("No Surat Internal");
						row.createCell(12).setCellValue("No Surat Eksternal");
						row.createCell(13).setCellValue("Status Aktif");
						break;
					case "jabatan":
						row = sheet.createRow(baris);
						row.createCell(0).setCellValue("No");
						row.createCell(1).setCellValue("Kode Parent Job Title");
						row.createCell(2).setCellValue("Nama Parent Job Title");
						row.createCell(3).setCellValue("Kode Job Title");
						row.createCell(4).setCellValue("Nama Job Title");
						row.createCell(5).setCellValue("Kode Unit Kerja");
						row.createCell(6).setCellValue("Nama Unit Kerja");
						row.createCell(7).setCellValue("Kode Jabatan");
						row.createCell(8).setCellValue("Kuota");
						row.createCell(9).setCellValue("Kode Job Grade");
						row.createCell(10).setCellValue("Status Aktif");
						break;
					case "pegawai":
						row = sheet.createRow(baris);
						row.createCell(0).setCellValue("No");
						row.createCell(1).setCellValue("NPP");
						row.createCell(2).setCellValue("Nama Depan");
						row.createCell(3).setCellValue("Nama Tengah");
						row.createCell(4).setCellValue("Nama Belakang");
						row.createCell(5).setCellValue("Nama Panggilan");
						row.createCell(6).setCellValue("Kode Jenis Kelamin");
						row.createCell(7).setCellValue("Nama Jenis Kelamin");
						row.createCell(8).setCellValue("Gelar Depan");
						row.createCell(9).setCellValue("Gelar Belakang");
						row.createCell(10).setCellValue("Kode Agama");
						row.createCell(11).setCellValue("Nama Agama");
						row.createCell(12).setCellValue("Website");
						row.createCell(13).setCellValue("Hobi");
						row.createCell(14).setCellValue("Kode Suku Bangsa");
						row.createCell(15).setCellValue("Nama Suku Bangsa");
						row.createCell(16).setCellValue("Kode Kewarganegaraan");
						row.createCell(17).setCellValue("Nama Kewarganegaraan");
						row.createCell(18).setCellValue("Tempat Lahir");
						row.createCell(19).setCellValue("Tanggal Lahir");
						row.createCell(20).setCellValue("Kode Status Nikah");
						row.createCell(21).setCellValue("Nama Status Nikah");
						row.createCell(22).setCellValue("Telp Rumah");
						row.createCell(23).setCellValue("HP");
						row.createCell(24).setCellValue("HP2");
						row.createCell(25).setCellValue("Email");
						row.createCell(26).setCellValue("Email2");
						row.createCell(27).setCellValue("Kode Salutasi");
						row.createCell(28).setCellValue("Nama Salutasi");
						row.createCell(29).setCellValue("Kode Asal Penerimaan");
						row.createCell(30).setCellValue("Nama Asal Penerimaan");
						row.createCell(31).setCellValue("Kode Dati2 Asal");
						row.createCell(32).setCellValue("Nama Dati2 Asal");
						row.createCell(33).setCellValue("TMT Masuk");
						row.createCell(34).setCellValue("Kode Status Karyawan");
						row.createCell(35).setCellValue("Nama Status Karyawan");
						row.createCell(36).setCellValue("Status Aktif");
						break;
					case "penugasan":
						row = sheet.createRow(baris);
						row.createCell(0).setCellValue("No");
						row.createCell(1).setCellValue("NPP");
						row.createCell(2).setCellValue("Nama");
						row.createCell(3).setCellValue("Kode Lokasi Kantor");
						row.createCell(4).setCellValue("Nama Lokasi Kantor");
						row.createCell(5).setCellValue("Kode Jabatan");
						row.createCell(6).setCellValue("Nama Jabatan");
						row.createCell(7).setCellValue("Tanggal SK");
						row.createCell(8).setCellValue("Kode Jenis SK");
						row.createCell(9).setCellValue("Nama Jenis SK");
						row.createCell(10).setCellValue("Nomor SK");
						row.createCell(11).setCellValue("TMT Jabatan");
						row.createCell(12).setCellValue("TAT Jabatan");
						row.createCell(13).setCellValue("Kode Grade");
						row.createCell(14).setCellValue("Nama Grade");
						row.createCell(15).setCellValue("Kode Status Jabatan");
						row.createCell(16).setCellValue("Nama Status Jabatan");
						row.createCell(17).setCellValue("Masa Percobaan");
						row.createCell(18).setCellValue("Tanggal Mulai Melaksanakan Tugas");
						row.createCell(19).setCellValue("Status Aktif");
						break;
					default:
						break;
					}	
				break;
				case "kompetensi":
					switch (tabel) {
					case "kelompokkompetensi":
						row = sheet.createRow(baris);
						row.createCell(0).setCellValue("No");
						row.createCell(1).setCellValue("Kode Klasifikasi");
						row.createCell(2).setCellValue("Nama Klasifikasi");
						row.createCell(3).setCellValue("Kode Kelompok");
						row.createCell(4).setCellValue("Nama Kelompok");
						row.createCell(5).setCellValue("Deskripsi");
						row.createCell(6).setCellValue("Status Aktif");
						break;
					case "kamuskompetensi":
						row = sheet.createRow(baris);
						row.createCell(0).setCellValue("No");
						row.createCell(1).setCellValue("Kode Kompetensi");
						row.createCell(2).setCellValue("Nama Kompetensi");
						row.createCell(3).setCellValue("Deskripsi Kompetensi");
						row.createCell(4).setCellValue("Kode Kelompok Kompetensi");
						row.createCell(5).setCellValue("Nama Kelompok Kompetensi");
						row.createCell(6).setCellValue("Level Kompetensi");
						row.createCell(7).setCellValue("Deskripsi Level Kompetensi");
						row.createCell(8).setCellValue("Status Aktif");
						break;
					case "periodekamuskompetensi":
						row = sheet.createRow(baris);
						row.createCell(0).setCellValue("No");
						row.createCell(1).setCellValue("Kode Periode Kompetensi");
						row.createCell(2).setCellValue("Nama Periode Kompetensi");
						row.createCell(3).setCellValue("Kode Kompetensi");
						row.createCell(4).setCellValue("Nama Kompetensi");
						row.createCell(5).setCellValue("Status Aktif");
						break;
					case "modelkompetensi":
						try {
							Connection con = null;
							ResultSet rs = null;
							CallableStatement cs = null;
							String query = null;
							try {
								row = sheet.createRow(baris);
								row.createCell(0).setCellValue("");
								row.createCell(1).setCellValue("");
								row.createCell(2).setCellValue("");
								row.createCell(3).setCellValue("");
								row.createCell(4).setCellValue("");
								row.createCell(5).setCellValue("");
								
								query = "exec kompetensi.sp_format_upload_modelkompetensi 1,"+_MAXROWS+",0,'kodekompetensi asc',null";
								con = new Koneksi().getConnection();
								cs = con.prepareCall(query);
								rs = cs.executeQuery();
								ResultSetMetaData metaData = rs.getMetaData();
								int kol = 0;
								for (int i = 5; i <= metaData.getColumnCount(); i++) {
									StringTokenizer st = new StringTokenizer(metaData.getColumnName(i),"|");
									String title1 = st.nextToken();
									String title2 = st.nextToken();
									row.createCell(6+kol).setCellValue(title2.toUpperCase());
									kol++;
								}
								
								baris++;
								row = sheet.createRow(baris);
								row.createCell(0).setCellValue("NO");
								row.createCell(1).setCellValue("KODE JOB TITLE");
								row.createCell(2).setCellValue("NAMA JOB TITLE");
								row.createCell(3).setCellValue("STATUS AKTIF");
								row.createCell(4).setCellValue("");
								row.createCell(5).setCellValue("");
								kol = 0;
								for (int i = 5; i <= metaData.getColumnCount(); i++) {
									StringTokenizer st = new StringTokenizer(metaData.getColumnName(i),"|");
									String title1 = st.nextToken();
									String title2 = st.nextToken();
									row.createCell(6+kol).setCellValue(title1.toUpperCase());
									kol++;
								}
								
								Integer no = 0;
								while (rs.next()) {
									no++;
									baris++;
									row = sheet.createRow(baris);
									row.createCell(0).setCellValue(no);
									row.createCell(1).setCellValue(rs.getString("kodejobtitle"));
									row.createCell(2).setCellValue(rs.getString("namajobtitle"));
									row.createCell(3).setCellValue(rs.getInt("row_status")==1?"On":"Off");
									row.createCell(4).setCellValue("");
									row.createCell(5).setCellValue("");
									kol = 0;
									for (int i = 5; i <= metaData.getColumnCount(); i++) {
										if(rs.getInt(i)==0) {
											row.createCell(6+kol).setCellValue("");
										}
										else {
											row.createCell(6+kol).setCellValue(rs.getInt(i));
										}
										//row.createCell(6+kol).setCellValue(rs.getInt(i)==0?"":rs.getInt(i));
										kol++;
									}
								}
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
						} catch (Exception e) {
							// TODO: handle exception
						}
						break;
					case "kompetensiindividu":
						try {
							Connection con = null;
							ResultSet rs = null;
							CallableStatement cs = null;
							String query = null;
							try {
								row = sheet.createRow(baris);
								row.createCell(0).setCellValue("");
								row.createCell(1).setCellValue("");
								row.createCell(2).setCellValue("");
								row.createCell(3).setCellValue("");
								
								query = "exec kompetensi.sp_format_upload_kompetensiindividu 1,"+_MAXROWS+",0,'kodekompetensi asc',null";
								con = new Koneksi().getConnection();
								cs = con.prepareCall(query);
								rs = cs.executeQuery();
								ResultSetMetaData metaData = rs.getMetaData();
								int kol = 0;
								for (int i = 4; i <= metaData.getColumnCount(); i++) {
									StringTokenizer st = new StringTokenizer(metaData.getColumnName(i),"|");
									String title1 = st.nextToken();
									String title2 = st.nextToken();
									row.createCell(4+kol).setCellValue(title2.toUpperCase());
									kol++;
								}
								
								baris++;
								row = sheet.createRow(baris);
								row.createCell(0).setCellValue("NO");
								row.createCell(1).setCellValue("NPP");
								row.createCell(2).setCellValue("NAMA");
								row.createCell(3).setCellValue("");
								kol = 0;
								for (int i = 4; i <= metaData.getColumnCount(); i++) {
									StringTokenizer st = new StringTokenizer(metaData.getColumnName(i),"|");
									String title1 = st.nextToken();
									String title2 = st.nextToken();
									row.createCell(4+kol).setCellValue(title1.toUpperCase());
									kol++;
								}
								
								Integer no = 0;
								while (rs.next()) {
									no++;
									baris++;
									row = sheet.createRow(baris);
									row.createCell(0).setCellValue(no);
									row.createCell(1).setCellValue(rs.getString("npp"));
									row.createCell(2).setCellValue(rs.getString("nama"));
									row.createCell(3).setCellValue("");
									kol = 0;
									for (int i = 4; i <= metaData.getColumnCount(); i++) {
										if(rs.getInt(i)==0) {
											row.createCell(4+kol).setCellValue("");
										}
										else {
											row.createCell(4+kol).setCellValue(rs.getInt(i));
										}
										//row.createCell(4+kol).setCellValue(rs.getInt(i)==0?"":rs.getInt(i));
										kol++;
									}
								}
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
						} catch (Exception e) {
							// TODO: handle exception
						}
						break;
					default:
						break;
					}
					break;
				case "cuti":
					switch (tabel) {
					case "kuota":
						row = sheet.createRow(baris);
						row.createCell(0).setCellValue("No");
						row.createCell(1).setCellValue("NPP");
						row.createCell(2).setCellValue("Nama Pegawai");
						row.createCell(3).setCellValue("Sisa");
						row.createCell(4).setCellValue("Tahun");
						break;
					default:
						break;
					}
					break;
				default:
					break;
				}
				
				CellStyle cellStyleTgl = workbook.createCellStyle();
				CreationHelper createHelper = workbook.getCreationHelper();
				cellStyleTgl.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
				
				Integer no = 0;
				switch (schema) {
				case "organisasi":
					switch (tabel) {
					case "jobprefix":
						try {
							Connection con = null;
							ResultSet rs = null;
							CallableStatement cs = null;
							String query = null;
							try {
								query = "exec organisasi.sp_listjobprefix 1, "+_MAXROWS+", 0, 'nama asc', null";
								con = new Koneksi().getConnection();
								cs = con.prepareCall(query);
								rs = cs.executeQuery();
								no = 0;
								while (rs.next()) {
									no++;
									baris++;
									row = sheet.createRow(baris);
									row.createCell(0).setCellValue(no);
									row.createCell(1).setCellValue(rs.getString("kode"));
									row.createCell(2).setCellValue(rs.getString("nama"));
									row.createCell(3).setCellValue(rs.getInt("isstruktural"));
									row.createCell(4).setCellValue(rs.getInt("row_status"));
								}
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
						} catch (Exception e) {
							// TODO: handle exception
						}
						break;
					case "functionalscope":
						try {
							Connection con = null;
							ResultSet rs = null;
							CallableStatement cs = null;
							String query = null;
							try {
								query = "exec organisasi.sp_listfunctionalscope 1, "+_MAXROWS+", 0, 'nama asc', null";
								con = new Koneksi().getConnection();
								cs = con.prepareCall(query);
								rs = cs.executeQuery();
								no = 0;
								while (rs.next()) {
									no++;
									baris++;
									row = sheet.createRow(baris);
									row.createCell(0).setCellValue(no);
									row.createCell(1).setCellValue(rs.getString("kode"));
									row.createCell(2).setCellValue(rs.getString("nama"));
									row.createCell(3).setCellValue(rs.getInt("row_status"));
								}
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
						} catch (Exception e) {
							// TODO: handle exception
						}
						break;
					case "jobtitle":
						try {
							Connection con = null;
							ResultSet rs = null;
							CallableStatement cs = null;
							String query = null;
							try {
								query = "exec organisasi.sp_listjobtitle 1, "+_MAXROWS+", 0, 'nama asc', null";
								con = new Koneksi().getConnection();
								cs = con.prepareCall(query);
								rs = cs.executeQuery();
								no = 0;
								while (rs.next()) {
									no++;
									baris++;
									row = sheet.createRow(baris);
									row.createCell(0).setCellValue(no);
									row.createCell(1).setCellValue(rs.getString("kodejobprefix"));
									row.createCell(2).setCellValue(rs.getString("namajobprefix"));
									row.createCell(3).setCellValue(rs.getString("kodefunctionalscope"));
									row.createCell(4).setCellValue(rs.getString("namafunctionalscope"));
									row.createCell(5).setCellValue(rs.getString("kode"));
									row.createCell(6).setCellValue(rs.getString("nama"));
									row.createCell(7).setCellValue(rs.getInt("tunjanganbbm"));
									row.createCell(8).setCellValue(rs.getInt("row_status"));
								}
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
						} catch (Exception e) {
							// TODO: handle exception
						}
						break;
					case "jobtitlerules":
						try {
							Connection con = null;
							ResultSet rs = null;
							CallableStatement cs = null;
							String query = null;
							try {
								query = "exec organisasi.sp_listjobtitlerules 1, "+_MAXROWS+", 0, 'kodejobtitle asc,lvl asc,utama desc', null";
								con = new Koneksi().getConnection();
								cs = con.prepareCall(query);
								rs = cs.executeQuery();
								no = 0;
								while (rs.next()) {
									no++;
									baris++;
									row = sheet.createRow(baris);
									row.createCell(0).setCellValue(no);
									row.createCell(1).setCellValue(rs.getString("kodejobtitle"));
									row.createCell(2).setCellValue(rs.getString("kodejobtitle1"));
									row.createCell(3).setCellValue(rs.getString("kodejobtitle2"));
									row.createCell(4).setCellValue(rs.getInt("lvl"));
									row.createCell(5).setCellValue(rs.getInt("utama"));
									row.createCell(6).setCellValue(rs.getInt("row_status"));
								}
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
						} catch (Exception e) {
							// TODO: handle exception
						}
						break;
					case "grade":
						try {
							Connection con = null;
							ResultSet rs = null;
							CallableStatement cs = null;
							String query = null;
							try {
								query = "exec organisasi.sp_listgrade 1, "+_MAXROWS+", 0, null, null";
								con = new Koneksi().getConnection();
								cs = con.prepareCall(query);
								rs = cs.executeQuery();
								no = 0;
								while (rs.next()) {
									no++;
									baris++;
									row = sheet.createRow(baris);
									row.createCell(0).setCellValue(no);
									row.createCell(1).setCellValue(rs.getString("kode"));
									row.createCell(2).setCellValue(rs.getString("nama"));
									row.createCell(3).setCellValue(rs.getInt("row_status"));
								}
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
						} catch (Exception e) {
							// TODO: handle exception
						}
						break;
					case "subgrade":
						try {
							Connection con = null;
							ResultSet rs = null;
							CallableStatement cs = null;
							String query = null;
							try {
								query = "exec organisasi.sp_listsubgrade 1, "+_MAXROWS+", 0, null, null";
								con = new Koneksi().getConnection();
								cs = con.prepareCall(query);
								rs = cs.executeQuery();
								no = 0;
								while (rs.next()) {
									no++;
									baris++;
									row = sheet.createRow(baris);
									row.createCell(0).setCellValue(no);
									row.createCell(1).setCellValue(rs.getString("kode"));
									row.createCell(2).setCellValue(rs.getString("nama"));
									row.createCell(3).setCellValue(rs.getString("kodegrade"));
									row.createCell(4).setCellValue(rs.getString("namagrade"));
									row.createCell(5).setCellValue(rs.getBigDecimal("gajipokok").doubleValue());
									row.createCell(6).setCellValue(rs.getBigDecimal("tupres").doubleValue());
									row.createCell(7).setCellValue(rs.getBigDecimal("utilitas").doubleValue());
									row.createCell(8).setCellValue(rs.getInt("row_status"));
								}
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
						} catch (Exception e) {
							// TODO: handle exception
						}
						break;
					case "jobgrade":
						try {
							Connection con = null;
							ResultSet rs = null;
							CallableStatement cs = null;
							String query = null;
							try {
								query = "exec organisasi.sp_listjobgrading 1, "+_MAXROWS+", 0, null, null";
								con = new Koneksi().getConnection();
								cs = con.prepareCall(query);
								rs = cs.executeQuery();
								no = 0;
								while (rs.next()) {
									no++;
									baris++;
									row = sheet.createRow(baris);
									row.createCell(0).setCellValue(no);
									row.createCell(1).setCellValue(rs.getString("kode"));
									row.createCell(2).setCellValue(rs.getBigDecimal("tunjjabatan").doubleValue());
									row.createCell(3).setCellValue(rs.getBigDecimal("uangsaku").doubleValue());
									row.createCell(4).setCellValue(rs.getBigDecimal("uanglembur").doubleValue());
									row.createCell(5).setCellValue(rs.getInt("row_status"));
								}
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
						} catch (Exception e) {
							// TODO: handle exception
						}
						break;
					case "lokasikantor":
						try {
							Connection con = null;
							ResultSet rs = null;
							CallableStatement cs = null;
							String query = null;
							try {
								query = "exec organisasi.sp_listoffice 1, "+_MAXROWS+", 0, null, null";
								con = new Koneksi().getConnection();
								cs = con.prepareCall(query);
								rs = cs.executeQuery();
								no = 0;
								while (rs.next()) {
									no++;
									baris++;
									row = sheet.createRow(baris);
									row.createCell(0).setCellValue(no);
									row.createCell(1).setCellValue(rs.getString("kodeparent"));
									row.createCell(2).setCellValue(rs.getString("namaparent"));
									row.createCell(3).setCellValue(rs.getString("kodeofficetype"));
									row.createCell(4).setCellValue(rs.getString("namaofficetype"));
									row.createCell(5).setCellValue(rs.getString("kode"));
									row.createCell(6).setCellValue(rs.getString("nama"));
									row.createCell(7).setCellValue(rs.getString("alamat"));
									row.createCell(8).setCellValue(rs.getString("kodepos"));
									row.createCell(9).setCellValue(rs.getString("telp"));
									row.createCell(10).setCellValue(rs.getString("fax"));
									row.createCell(11).setCellValue(rs.getString("kodedati2"));
									row.createCell(12).setCellValue(rs.getString("namadati2"));
									row.createCell(13).setCellValue(rs.getInt("ik"));
									row.createCell(14).setCellValue(rs.getBigDecimal("tunjdacil").doubleValue());
									row.createCell(15).setCellValue(rs.getBigDecimal("tunjkhusus").doubleValue());
									row.createCell(16).setCellValue(rs.getString("nosurateksternal"));
									row.createCell(17).setCellValue(rs.getInt("row_status"));
								}
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
						} catch (Exception e) {
							// TODO: handle exception
						}
						break;
					case "unitkerja":
						try {
							Connection con = null;
							ResultSet rs = null;
							CallableStatement cs = null;
							String query = null;
							try {
								query = "exec organisasi.sp_listunitkerja 1, "+_MAXROWS+", 0, null, null";
								con = new Koneksi().getConnection();
								cs = con.prepareCall(query);
								rs = cs.executeQuery();
								no = 0;
								while (rs.next()) {
									no++;
									baris++;
									row = sheet.createRow(baris);
									row.createCell(0).setCellValue(no);
									row.createCell(1).setCellValue(rs.getString("kodeparent"));
									row.createCell(2).setCellValue(rs.getString("namaparent"));
									row.createCell(3).setCellValue(rs.getString("kode"));
									row.createCell(4).setCellValue(rs.getString("nama"));
									row.createCell(5).setCellValue(rs.getString("kodehirarkiunitkerja"));
									row.createCell(6).setCellValue(rs.getString("namahirarkiunitkerja"));
									row.createCell(7).setCellValue(rs.getString("kodeofficetype"));
									row.createCell(8).setCellValue(rs.getString("namaofficetype"));
									row.createCell(9).setCellValue(rs.getString("kodeorganizationchart"));
									row.createCell(10).setCellValue(rs.getString("namaorganizationchart"));
									row.createCell(11).setCellValue(rs.getString("nosuratinternal"));
									row.createCell(12).setCellValue(rs.getString("nosurateksternal"));
									row.createCell(13).setCellValue(rs.getInt("row_status"));
								}
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
						} catch (Exception e) {
							// TODO: handle exception
						}
						break;
					case "jabatan":
						try {
							Connection con = null;
							ResultSet rs = null;
							CallableStatement cs = null;
							String query = null;
							try {
								query = "exec organisasi.sp_listhirarkijabatan 1, "+_MAXROWS+", 0, null, null";
								con = new Koneksi().getConnection();
								cs = con.prepareCall(query);
								rs = cs.executeQuery();
								no = 0;
								while (rs.next()) {
									no++;
									baris++;
									row = sheet.createRow(baris);
									row.createCell(0).setCellValue(no);
									row.createCell(1).setCellValue(rs.getString("kodeparentjobtitle"));
									row.createCell(2).setCellValue(rs.getString("namaparentjobtitle"));
									row.createCell(3).setCellValue(rs.getString("kodejobtitle"));
									row.createCell(4).setCellValue(rs.getString("namajobtitle"));
									row.createCell(5).setCellValue(rs.getString("kodeunitkerja"));
									row.createCell(6).setCellValue(rs.getString("namaunitkerja"));
									row.createCell(7).setCellValue(rs.getString("kode"));
									row.createCell(8).setCellValue(rs.getInt("jumlah"));
									row.createCell(9).setCellValue(rs.getString("kodejobgrade"));
									row.createCell(10).setCellValue(rs.getInt("row_status"));
								}
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
						} catch (Exception e) {
							// TODO: handle exception
						}
						break;
					case "pegawai":
						try {
							Connection con = null;
							ResultSet rs = null;
							CallableStatement cs = null;
							String query = null;
							try {
								query = "exec karyawan.sp_format_upload_pegawai2 1, 10, 0, null, null";
								con = new Koneksi().getConnection();
								cs = con.prepareCall(query);
								rs = cs.executeQuery();
								no = 0;
								while (rs.next()) {
									no++;
									baris++;
									row = sheet.createRow(baris);
									row.createCell(0).setCellValue(no);
									row.createCell(1).setCellValue(rs.getString("npp"));
									row.createCell(2).setCellValue(rs.getString("namadepan"));
									row.createCell(3).setCellValue(rs.getString("namatengah"));
									row.createCell(4).setCellValue(rs.getString("namabelakang"));
									row.createCell(5).setCellValue(rs.getString("namapanggilan"));
									row.createCell(6).setCellValue(rs.getInt("kodejeniskelamin"));
									row.createCell(7).setCellValue(rs.getString("namajeniskelamin"));
									row.createCell(8).setCellValue(rs.getString("gelardepan"));
									row.createCell(9).setCellValue(rs.getString("gelarbelakang"));
									row.createCell(10).setCellValue(rs.getInt("kodeagama"));
									row.createCell(11).setCellValue(rs.getString("namaagama"));
									row.createCell(12).setCellValue(rs.getString("website"));
									row.createCell(13).setCellValue(rs.getString("hobi"));
									row.createCell(14).setCellValue(rs.getInt("kodesukubangsa"));
									row.createCell(15).setCellValue(rs.getString("namasukubangsa"));
									row.createCell(16).setCellValue(rs.getString("kodekewarganegaraan"));
									row.createCell(17).setCellValue(rs.getString("namakewarganegaraan"));
									row.createCell(18).setCellValue(rs.getString("tempatlahir"));
									row.createCell(19).setCellValue(rs.getString("tgllahir"));
									row.createCell(20).setCellValue(rs.getInt("kodestatusnikah"));
									row.createCell(21).setCellValue(rs.getString("namastatusnikah"));
									row.createCell(22).setCellValue(rs.getString("telprumah"));
									row.createCell(23).setCellValue(rs.getString("hp"));
									row.createCell(24).setCellValue(rs.getString("hp2"));
									row.createCell(25).setCellValue(rs.getString("email"));
									row.createCell(26).setCellValue(rs.getString("email2"));
									row.createCell(27).setCellValue(rs.getInt("kodesalutasi"));
									row.createCell(28).setCellValue(rs.getString("namasalutasi"));
									row.createCell(29).setCellValue(rs.getString("kodeasalpenerimaan"));
									row.createCell(30).setCellValue(rs.getString("namaasalpenerimaan"));
									row.createCell(31).setCellValue(rs.getString("kodedati2asal"));
									row.createCell(32).setCellValue(rs.getString("namadati2asal"));
									row.createCell(33).setCellValue(rs.getString("tmtmasuk"));
									row.createCell(34).setCellValue(rs.getInt("kodestatuskaryawan"));
									row.createCell(35).setCellValue(rs.getString("namastatuskaryawan"));
									row.createCell(36).setCellValue(rs.getInt("row_status"));
								}
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
						} catch (Exception e) {
							// TODO: handle exception
						}
						break;
					case "penugasan":
						try {
							Connection con = null;
							ResultSet rs = null;
							CallableStatement cs = null;
							String query = null;
							try {
								query = "exec karyawan.sp_format_upload_penugasan 1, 10, 0, null, null";
								con = new Koneksi().getConnection();
								cs = con.prepareCall(query);
								rs = cs.executeQuery();
								no = 0;
								while (rs.next()) {
									no++;
									baris++;
									row = sheet.createRow(baris);
									row.createCell(0).setCellValue(no);
									row.createCell(1).setCellValue(rs.getString("npp"));
									row.createCell(2).setCellValue(rs.getString("nama"));
									row.createCell(3).setCellValue(rs.getString("kodeoffice"));
									row.createCell(4).setCellValue(rs.getString("namaoffice"));
									row.createCell(5).setCellValue(rs.getString("kodejabatan"));
									row.createCell(6).setCellValue(rs.getString("namajabatan"));
									row.createCell(7).setCellValue(rs.getString("tanggalsk"));
									row.createCell(8).setCellValue(rs.getInt("kodejenissk"));
									row.createCell(9).setCellValue(rs.getString("namajenissk"));
									row.createCell(10).setCellValue(rs.getString("nomorsk"));
									row.createCell(11).setCellValue(rs.getString("tmtjabatan"));
									row.createCell(12).setCellValue(rs.getString("tatjabatan"));
									row.createCell(13).setCellValue(rs.getString("kodesubgrade"));
									row.createCell(14).setCellValue(rs.getString("namasubgrade"));
									row.createCell(15).setCellValue(rs.getInt("kodestatusjabatan"));
									row.createCell(16).setCellValue(rs.getString("namastatusjabatan"));
									row.createCell(17).setCellValue(rs.getInt("masapercobaan"));
									row.createCell(18).setCellValue(rs.getString("tanggalmulai"));
									row.createCell(19).setCellValue(rs.getInt("row_status"));
								}
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
						} catch (Exception e) {
							// TODO: handle exception
						}
						break;
					}
				break;
				case "kompetensi":
					switch (tabel) {
					case "kelompokkompetensi":
						try {
							Connection con = null;
							ResultSet rs = null;
							CallableStatement cs = null;
							String query = null;
							try {
								query = "exec kompetensi.sp_listkelompokkompetensi 1, "+_MAXROWS+", 0, null, null";
								con = new Koneksi().getConnection();
								cs = con.prepareCall(query);
								rs = cs.executeQuery();
								no = 0;
								while (rs.next()) {
									no++;
									baris++;
									row = sheet.createRow(baris);
									row.createCell(0).setCellValue(no);
									row.createCell(1).setCellValue(rs.getString("kodeklaskelompokkompetensi"));
									row.createCell(2).setCellValue(rs.getString("namaklaskelompokkompetensi"));
									row.createCell(3).setCellValue(rs.getString("kode"));
									row.createCell(4).setCellValue(rs.getString("nama"));
									row.createCell(5).setCellValue(rs.getString("deskripsi"));
									row.createCell(6).setCellValue(rs.getInt("row_status"));
								}
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
						} catch (Exception e) {
							// TODO: handle exception
						}
						break;
					case "kamuskompetensi":
						try {
							Connection con = null;
							ResultSet rs = null;
							CallableStatement cs = null;
							String query = null;
							try {
								query = "exec kompetensi.sp_listlevelkompetensi 1, "+_MAXROWS+", 0, null, null";
								con = new Koneksi().getConnection();
								cs = con.prepareCall(query);
								rs = cs.executeQuery();
								no = 0;
								while (rs.next()) {
									no++;
									baris++;
									row = sheet.createRow(baris);
									row.createCell(0).setCellValue(no);
									row.createCell(1).setCellValue(rs.getString("kodekompetensi"));
									row.createCell(2).setCellValue(rs.getString("namakompetensi"));
									row.createCell(3).setCellValue(rs.getString("deskripsikompetensi"));
									row.createCell(4).setCellValue(rs.getString("kodekelompokkompetensi"));
									row.createCell(5).setCellValue(rs.getString("namakelompokkompetensi"));
									row.createCell(6).setCellValue(rs.getInt("level"));
									row.createCell(7).setCellValue(rs.getString("deskripsi"));
									row.createCell(8).setCellValue(rs.getInt("row_status"));
								}
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
						} catch (Exception e) {
							// TODO: handle exception
						}
						break;
					case "periodekamuskompetensi":
						try {
							Connection con = null;
							ResultSet rs = null;
							CallableStatement cs = null;
							String query = null;
							try {
								query = "exec kompetensi.sp_listkompetensi 1, "+_MAXROWS+", 0, null, null";
								con = new Koneksi().getConnection();
								cs = con.prepareCall(query);
								rs = cs.executeQuery();
								no = 0;
								while (rs.next()) {
									no++;
									baris++;
									row = sheet.createRow(baris);
									row.createCell(0).setCellValue(no);
									row.createCell(1).setCellValue("");
									row.createCell(2).setCellValue("");
									row.createCell(3).setCellValue(rs.getString("kode"));
									row.createCell(4).setCellValue(rs.getString("nama"));
									row.createCell(5).setCellValue(rs.getInt("row_status"));
								}
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
						} catch (Exception e) {
							// TODO: handle exception
						}
						
						break;
					default:
						break;
					}
					break;
				default:
					break;
				}
				
				tempFile = File.createTempFile("temp", "hcis");
				FileOutputStream fos = null;
				fos = new FileOutputStream(tempFile);
				workbook.write(fos);
			fos.close();
			} catch (Exception e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
				e.printStackTrace();
			}
			finally {
				if (workbook != null) {
					try {
						workbook.close();
					} catch (IOException e) {
					}
				}
			}
			/*=====================================================================*/
			
			ResponseBuilder response = Response.ok((Object) tempFile);
			response.header("Content-Disposition","attachment; filename=format_"+schema+"_"+tabel+"_" + new Date().getTime() + ".xlsx");
			tempFile.deleteOnExit();
			return response.build();
		}
		result.setMetadata(metadata);
		return Response.ok(result).build();
	}
	
	@GET
	@Path("/format/djp/djp/{token}")
	@Produces({ MediaType.APPLICATION_OCTET_STREAM, "application/json" })
	public Response DownloadFormatDJP(@Context HttpHeaders headers, 
			@PathParam("token") String token) {
		Result<Object> result = new Result<Object>();
		Metadata metadata = new Metadata();
		if (SharedMethod.VerifyToken2(token, metadata)) {
			final Integer _MAXROWS = 1048576;
			//String[] namabulan={"","Januari","Februari","Maret","April","Mei","Juni","Juli","Agustus","September","Oktober","Nopember","Desember"};
			/*=====================================================================*/
			Workbook workbook = null;
			try {
				workbook = new XSSFWorkbook();
				CellStyle centerStyle = workbook.createCellStyle();
				centerStyle.setAlignment(HorizontalAlignment.CENTER);
				
				Sheet sheet = workbook.createSheet("General");
				Integer baris = 0;
				Row row;
				row = sheet.createRow(baris);
				row.createCell(0).setCellValue("No");
				row.createCell(1).setCellValue("Kode Job Title");
				row.createCell(2).setCellValue("Nama Job Title");
				row.createCell(3).setCellValue("Misi");
				row.createCell(4).setCellValue("Tanggung Jawab");
				row.createCell(5).setCellValue("Lingkungan Kerja");
				row.createCell(6).setCellValue("Hubungan Kerja");
				row.createCell(7).setCellValue("Persyaratan Mutlak Jabatan");
				
				sheet = workbook.createSheet("KPI");
				baris = 0;
				row = sheet.createRow(baris);
				row.createCell(0).setCellValue("No");
				row.createCell(1).setCellValue("Kode Job Title");
				row.createCell(2).setCellValue("Nama Job Title");
				row.createCell(3).setCellValue("Tanggung Jawab Utama");
				row.createCell(4).setCellValue("Wewenang Utama");
				row.createCell(5).setCellValue("KPI (#)");
				row.createCell(6).setCellValue("Dimensi Jabatan (#)");
				
				sheet = workbook.createSheet("Pendidikan Formal");
				baris = 0;
				row = sheet.createRow(baris);
				row.createCell(0).setCellValue("No");
				row.createCell(1).setCellValue("Kode Job Title");
				row.createCell(2).setCellValue("Nama Job Title");
				row.createCell(3).setCellValue("Kode Pendidikan");
				row.createCell(4).setCellValue("Nama Pendidikan");
				row.createCell(5).setCellValue("Kode Jurusan");
				row.createCell(6).setCellValue("Nama Jurusan");
				
				sheet = workbook.createSheet("Pendidikan Non Formal");
				baris = 0;
				row = sheet.createRow(baris);
				row.createCell(0).setCellValue("No");
				row.createCell(1).setCellValue("Kode Job Title");
				row.createCell(2).setCellValue("Nama Job Title");
				row.createCell(3).setCellValue("Nama");
				row.createCell(4).setCellValue("Deskripsi");
				
				sheet = workbook.createSheet("Pengalaman Kerja");
				baris = 0;
				row = sheet.createRow(baris);
				row.createCell(0).setCellValue("No");
				row.createCell(1).setCellValue("Kode Job Title");
				row.createCell(2).setCellValue("Nama Job Title");
				row.createCell(3).setCellValue("Kode Pengalaman Kerja");
				row.createCell(4).setCellValue("Nama Pengalaman Kerja");
				row.createCell(5).setCellValue("Deskripsi");
				
				tempFile = File.createTempFile("temp", "hcis");
				FileOutputStream fos = null;
				fos = new FileOutputStream(tempFile);
				workbook.write(fos);
			fos.close();
			} catch (Exception e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
				e.printStackTrace();
			}
			finally {
				if (workbook != null) {
					try {
						workbook.close();
					} catch (IOException e) {
					}
				}
			}
			/*=====================================================================*/
			
			ResponseBuilder response = Response.ok((Object) tempFile);
			response.header("Content-Disposition","attachment; filename=format_djp_djp_" + new Date().getTime() + ".xlsx");
			tempFile.deleteOnExit();
			return response.build();
		}
		result.setMetadata(metadata);
		return Response.ok(result).build();
	}
	
}
