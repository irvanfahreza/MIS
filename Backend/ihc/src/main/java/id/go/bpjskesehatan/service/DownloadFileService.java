package id.go.bpjskesehatan.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import javax.ws.rs.POST;
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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import id.go.bpjskesehatan.database.Koneksi;
import id.go.bpjskesehatan.entitas.karyawan.Detailalamat;
import id.go.bpjskesehatan.entitas.karyawan.Detailalamat2;
import id.go.bpjskesehatan.entitas.karyawan.Detaildimensijabatan;
import id.go.bpjskesehatan.entitas.karyawan.Detailkpi;
import id.go.bpjskesehatan.entitas.karyawan.Detailpendidikanformal;
import id.go.bpjskesehatan.entitas.karyawan.Detailpendidikannonformal;
import id.go.bpjskesehatan.entitas.karyawan.Detailpengalamankerja;
import id.go.bpjskesehatan.entitas.karyawan.Djpindividu;
import id.go.bpjskesehatan.entitas.karyawan.Djpindividuindex;
import id.go.bpjskesehatan.entitas.karyawan.Infoassesment;
import id.go.bpjskesehatan.entitas.karyawan.Infoasuransi;
import id.go.bpjskesehatan.entitas.karyawan.Infobaranginventaris;
import id.go.bpjskesehatan.entitas.karyawan.Infodarurat;
import id.go.bpjskesehatan.entitas.karyawan.Infofisik;
import id.go.bpjskesehatan.entitas.karyawan.Infoidentitas;
import id.go.bpjskesehatan.entitas.karyawan.Infoikatandinas;
import id.go.bpjskesehatan.entitas.karyawan.Infokeluarga;
import id.go.bpjskesehatan.entitas.karyawan.Infopelatihan;
import id.go.bpjskesehatan.entitas.karyawan.Infopendidikan;
import id.go.bpjskesehatan.entitas.karyawan.Infopengalamankerja;
import id.go.bpjskesehatan.entitas.karyawan.Infopengalamanproyek;
import id.go.bpjskesehatan.entitas.karyawan.Infopenghargaan;
import id.go.bpjskesehatan.entitas.karyawan.Infosertifikasi;
import id.go.bpjskesehatan.entitas.karyawan.Pegawai;
import id.go.bpjskesehatan.entitas.karyawan.Penugasan;
import id.go.bpjskesehatan.entitas.karyawan.Tanggungjawab;
import id.go.bpjskesehatan.util.SharedMethod;

@Path("/file")
public class DownloadFileService {
	static File tempFile = null;

	@POST
	@Path("/download/{servicename}/list/{page}/{row}")
	@Produces({ MediaType.APPLICATION_OCTET_STREAM, "application/json" })
	public Response downloadFile(@Context HttpHeaders headers, @PathParam("servicename") String servicename,
			@PathParam("page") String page, @PathParam("row") String row, String data) {
		String query = null;
		String namasp = null;
		String namaentitas = null;
		Object obj = null;
		switch (servicename) {
		case "pegawai":
			namasp = "sp_listpegawai_raw";
			namaentitas = "pegawai";
			obj = new Pegawai();
			break;
		case "detailalamat":
			namasp = "sp_listdetailalamat_raw";
			namaentitas = "detailalamat";
			obj = new Detailalamat();
			break;
		case "detailalamat2":
			namasp = "sp_listdetailalamat2_raw";
			namaentitas = "detailalamat2";
			obj = new Detailalamat2();
			break;
		case "infodarurat":
			namasp = "sp_listinfodarurat_raw";
			namaentitas = "infodarurat";
			obj = new Infodarurat();
			break;
		case "penugasan":
			namasp = "sp_listpenugasan_raw";
			namaentitas = "penugasan";
			obj = new Penugasan();
			break;
		case "infoidentitas":
			namasp = "sp_listinfoidentitas";
			namaentitas = "infoidentitas";
			obj = new Infoidentitas();
			break;
		case "infofisik":
			namasp = "sp_listinfofisik";
			namaentitas = "infofisik";
			obj = new Infofisik();
			break;
		case "infokeluarga":
			namasp = "sp_listinfokeluarga";
			namaentitas = "infokeluarga";
			obj = new Infokeluarga();
			break;
		case "infopendidikan":
			namasp = "sp_listinfopendidikan";
			namaentitas = "infopendidikan";
			obj = new Infopendidikan();
			break;
		case "infopengalamankerja":
			namasp = "sp_listinfopengalamankerja";
			namaentitas = "infopengalamankerja";
			obj = new Infopengalamankerja();
			break;
		case "infoasuransi":
			namasp = "sp_listinfoasuransi";
			namaentitas = "infoasuransi";
			obj = new Infoasuransi();
			break;
		case "infobaranginventaris":
			namasp = "sp_listinfobaranginventaris";
			namaentitas = "infobaranginventaris";
			obj = new Infobaranginventaris();
			break;
		case "infoassesment":
			namasp = "sp_listinfoassesment";
			namaentitas = "infoassesment";
			obj = new Infoassesment();
			break;
		case "infopelatihan":
			namasp = "sp_listinfopelatihan";
			namaentitas = "infopelatihan";
			obj = new Infopelatihan();
			break;
		case "infosertifikasi":
			namasp = "sp_listinfosertifikasi";
			namaentitas = "infosertifikasi";
			obj = new Infosertifikasi();
			break;
		case "infopengalamanproyek":
			namasp = "sp_listinfopengalamanproyek";
			namaentitas = "infopengalamanproyek";
			obj = new Infopengalamanproyek();
			break;
		case "infopenghargaan":
			namasp = "sp_listinfopenghargaan";
			namaentitas = "infopenghargaan";
			obj = new Infopenghargaan();
			break;
		case "infoikatandinas":
			namasp = "sp_listinfoikatandinas";
			namaentitas = "infoikatandinas";
			obj = new Infoikatandinas();
			break;
		case "djp":
			namasp = "sp_listdjpindividu";
			namaentitas = "djpindividu";
			obj = new Djpindividu();
			break;
		case "djpindividuindex":
			namasp = "sp_listdjpindividuindex";
			namaentitas = "djpindividuindex";
			obj = new Djpindividuindex();
			break;
		case "tanggungjawab":
			namasp = "sp_listtanggungjawab";
			namaentitas = "tanggungjawabindividu";
			obj = new Tanggungjawab();
			break;
		case "detaildimensijabatan":
			namasp = "sp_listdetaildimensijabatan";
			namaentitas = "detaildimensijabatanindividu";
			obj = new Detaildimensijabatan();
			break;
		case "detailkpi":
			namasp = "sp_listdetailkpi";
			namaentitas = "detailkpiindividu";
			obj = new Detailkpi();
			break;
		case "detailpendidikanformal":
			namasp = "sp_listdetailpendidikanformal";
			namaentitas = "detailpendidikanformalindividu";
			obj = new Detailpendidikanformal();
			break;
		case "detailpendidikannonformal":
			namasp = "sp_listdetailpendidikannonformal";
			namaentitas = "detailpendidikannonformalindividu";
			obj = new Detailpendidikannonformal();
			break;
		case "detailpengalamankerja":
			namasp = "sp_listdetailpengalamankerja";
			namaentitas = "detailpengalamankerjaindividu";
			obj = new Detailpengalamankerja();
			break;
		default:
			return Response.status(Status.NOT_FOUND).build();
		}
		query = "exec karyawan." + namasp + " ?, ?, ?, ?, ?";
		writeEntityListToExcel(obj, namaentitas, query, page, row, data);
		ResponseBuilder response = Response.ok((Object) tempFile);
		response.header("Content-Disposition",
				"attachment; filename=" + servicename + SharedMethod.getTime().toString() + ".xlsx");
		tempFile.deleteOnExit();
		return response.build();
	}

	public static void writeEntityListToExcel(Object entity, String namaTabel, String query, String page, String baris,
			String data) {
		Workbook workbook = null;
		workbook = new XSSFWorkbook();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String order = null, filter = null;
		Sheet sheet = workbook.createSheet(namaTabel);

		try {
			if (data != null) {
				if (!data.isEmpty()) {
					ObjectMapper mapper = new ObjectMapper();
					JsonNode json = mapper.readTree(data);

					order = json.path("sort").isMissingNode() ? null
							: SharedMethod.getSortedColumn(mapper.writeValueAsString(json.path("sort")));

					filter = json.path("filter").isMissingNode() ? null
							: SharedMethod.getFilteredColumn(mapper.writeValueAsString(json.path("filter")),
									null);
				}
			}
			con = new Koneksi().getConnection();
			ps = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			ps.setInt(1, Integer.parseInt(page));
			ps.setInt(2, Integer.parseInt(baris));
			ps.setInt(3, 0);
			ps.setString(4, order);
			ps.setString(5, filter);
			rs = ps.executeQuery();
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
			if (workbook != null)
				try {
					workbook.close();
				} catch (IOException e) {
				}
		}

	}

}
