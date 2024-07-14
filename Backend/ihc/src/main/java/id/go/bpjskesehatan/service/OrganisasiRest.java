package id.go.bpjskesehatan.service;

import id.go.bpjskesehatan.entitas.organisasi.Organizationchartunitkerja;
import id.go.bpjskesehatan.entitas.organisasi.UnitKerja;
import id.go.bpjskesehatan.util.SharedMethod;
import id.go.bpjskesehatan.util.Utils;
import id.go.bpjskesehatan.entitas.organisasi.HirarkiUnitKerja;
import id.go.bpjskesehatan.database.Koneksi;
import id.go.bpjskesehatan.entitas.organisasi.FunctionalScope;
import id.go.bpjskesehatan.entitas.organisasi.Grade;
import id.go.bpjskesehatan.entitas.organisasi.Office;
import id.go.bpjskesehatan.entitas.organisasi.OfficeType;
import id.go.bpjskesehatan.entitas.organisasi.OrganizationChart;
import id.go.bpjskesehatan.entitas.organisasi.Jabatan;
import id.go.bpjskesehatan.entitas.organisasi.JobPrefix;
import id.go.bpjskesehatan.entitas.organisasi.JobPrefixPangkat;
import id.go.bpjskesehatan.entitas.organisasi.SubGrade;
import id.go.bpjskesehatan.entitas.organisasi.JobTitle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.apache.poi.EncryptedDocumentException;
import org.glassfish.jersey.media.multipart.FormDataParam;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Bambang Purwanto
 *
 */
@Path("/organisasi")
public class OrganisasiRest {

	@POST
	@Path("/{servicename}/list/{page}/{row}")
	@Produces("application/json")
	public Response getListData(@Context HttpHeaders headers, @PathParam("servicename") String servicename,
			@PathParam("page") String page, @PathParam("row") String row, String data) {
		String query = null;
		String namasp = null;
		String namaentitas = null;
		Object obj = null;
		namasp = "sp_list" + servicename;
		namaentitas = servicename;
		switch (servicename) {
		case "jobprefix":
			obj = new JobPrefix();
			break;
		case "jobprefixpangkat":
			obj = new JobPrefixPangkat();
			break;
		case "functionalscope":
			obj = new FunctionalScope();
			break;
		case "jobtitle":
			obj = new JobTitle();
			break;
		case "grade":
			obj = new Grade();
			break;
		case "subgrade":
			obj = new SubGrade();
			break;
		case "office":
			obj = new Office();
			break;
		case "officetype":
			obj = new OfficeType();
			break;
		case "hirarkiunitkerja":
			obj = new HirarkiUnitKerja();
			break;
		case "unitkerja":
			obj = new UnitKerja();
			break;
		case "jabatan":
			obj = new Jabatan();
			break;
		case "organizationchart":
			obj = new OrganizationChart();
			break;
		case "organizationchartunitkerja":
			obj = new Organizationchartunitkerja();
			break;
		default:
			return Response.status(Status.NOT_FOUND).build();
		}
		query = "exec organisasi." + namasp + " ?, ?, ?, ?, ?";
		return RestMethod.getListData(headers, page, row, namaentitas, obj, query, data);
	}

	@POST
	@Path("/{servicename}")
	@Produces("application/json")
	public Response createData(@Context HttpHeaders headers, @PathParam("servicename") String servicename, @PathParam("page") String page, @PathParam("row") String row, String data) {

		Object obj = null;
		String namatabel = null;
		namatabel = "organisasi." + servicename;
		switch (servicename) {
		case "jobprefix":
			obj = new JobPrefix();
			break;
		case "jobprefixpangkat":
			obj = new JobPrefixPangkat();
			break;
		case "functionalscope":
			obj = new FunctionalScope();
			break;
		case "jobtitle":
			obj = new JobTitle();
			break;
		case "grade":
			obj = new Grade();
			break;
		case "subgrade":
			obj = new SubGrade();
			break;
		case "office":
			obj = new Office();
			break;
		case "officetype":
			obj = new OfficeType();
			break;
		case "hirarkiunitkerja":
			obj = new HirarkiUnitKerja();
			break;
		case "unitkerja":
			obj = new UnitKerja();
			break;
		case "jabatan":
			obj = new Jabatan();
			break;
		case "organizationchart":
			obj = new OrganizationChart();
			break;
		case "organizationchartunitkerja":
			obj = new Organizationchartunitkerja();
			break;
		default:
			return Response.status(Status.NOT_FOUND).build();
		}

		return RestMethod.createData(headers, data, obj, namatabel);
	}

	@PUT
	@Path("/{servicename}")
	@Produces("application/json")
	public Response updateData(@Context HttpHeaders headers, @PathParam("servicename") String servicename,
			@PathParam("page") String page, @PathParam("row") String row, String data) {

		Object obj = null;
		String namatabel = null;
		namatabel = "organisasi." + servicename;
		switch (servicename) {
		case "jobprefix":
			obj = new JobPrefix();
			break;
		case "jobprefixpangkat":
			obj = new JobPrefixPangkat();
			break;
		case "functionalscope":
			obj = new FunctionalScope();
			break;
		case "jobtitle":
			obj = new JobTitle();
			break;
		case "grade":
			obj = new Grade();
			break;
		case "subgrade":
			obj = new SubGrade();
			break;
		case "office":
			obj = new Office();
			break;
		case "officetype":
			obj = new OfficeType();
			break;
		case "hirarkiunitkerja":
			obj = new HirarkiUnitKerja();
			break;
		case "unitkerja":
			obj = new UnitKerja();
			break;
		case "jabatan":
			obj = new Jabatan();
			break;
		case "organizationchart":
			obj = new OrganizationChart();
			break;
		case "organizationchartunitkerja":
			obj = new Organizationchartunitkerja();
			break;
		default:
			return Response.status(Status.NOT_FOUND).build();
		}

		return RestMethod.updateData(headers, data, obj, namatabel);
	}

	@POST
	@Path("/{servicename}/delete")
	@Produces("application/json")
	public Response deleteData(@Context HttpHeaders headers, @PathParam("servicename") String servicename, @PathParam("page") String page, @PathParam("row") String row, String data) {

		Object obj = null;
		String namatabel = null;
		namatabel = "organisasi." + servicename;
		switch (servicename) {
		case "jobprefix":
			obj = new JobPrefix();
			break;
		case "jobprefixpangkat":
			obj = new JobPrefixPangkat();
			break;
		case "functionalscope":
			obj = new FunctionalScope();
			break;
		case "jobtitle":
			obj = new JobTitle();
			break;
		case "grade":
			obj = new Grade();
			break;
		case "subgrade":
			obj = new SubGrade();
			break;
		case "office":
			obj = new Office();
			break;
		case "officetype":
			obj = new OfficeType();
			break;
		case "hirarkiunitkerja":
			obj = new HirarkiUnitKerja();
			break;
		case "unitkerja":
			obj = new UnitKerja();
			break;
		case "jabatan":
			obj = new Jabatan();
			break;
		case "organizationchart":
			obj = new OrganizationChart();
			break;
		case "organizationchartunitkerja":
			obj = new Organizationchartunitkerja();
			break;
		default:
			return Response.status(Status.NOT_FOUND).build();
		}

		return RestMethod.deleteData(headers, data, obj, namatabel);
	}

	@POST
	@Path("/organizationchart/lampiran")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces("application/json")
	public Response createLampiranPenugasan(@Context HttpHeaders headers, @FormDataParam("kode") String kode,
			@FormDataParam("lampiran") InputStream uploadedInputStream, @FormDataParam("ekstensi") String ekstensi) {
		Map<String, Object> metadataobj = new HashMap<String, Object>();
		Map<String, Object> metadata = new HashMap<String, Object>();
		if (SharedMethod.ServiceAuth(headers, metadata)) {
			Connection con = null;
			PreparedStatement ps = null;

			ResultSet rs = null;
			Koneksi koneksi = null;
			try {
				koneksi = new Koneksi();
				// koneksi.BuatKoneksi();
				con = koneksi.getConnection();
				con.setAutoCommit(false);

				ps = con.prepareStatement("select kode from organisasi.organizationchart where kode = ?");
				ps.setString(1, kode);
				rs = ps.executeQuery();
				if (rs.next()) {
					rs.close();
					ps.close();
					ps = con.prepareStatement(
							"update organisasi.organizationchart set ekstensi = ?, lampiran = ? where kode = ?");
					ps.setString(1, ekstensi.toLowerCase());
					ps.setBlob(2, uploadedInputStream);
					ps.setString(3, kode);
					ps.executeUpdate();
					ps.close();
					con.commit();
					metadata.put("code", 1);
					metadata.put("message", "Data berhasil diupload");
				} else {
					metadata.put("code", 2);
					metadata.put("message", "Kode tidak ditemukan");
				}
			} catch (EncryptedDocumentException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
			} catch (SQLException e) {
				metadata.put("code", 0);
				if (e.getErrorCode() == -407) {
					String tabel = "";
					String kolom = "";

					for (String retval : e.getMessage().split(", ")) {
						if (retval.startsWith("TABLEID")) {
							String[] val = retval.split("=");
							tabel = val[1];
						}
						if (retval.startsWith("COLNO")) {
							String[] val = retval.split("=");
							kolom = val[1];
						}
					}
					String query;
					query = "select COLNAME from SYSCAT.COLUMNS "
							+ "where TABSCHEMA || '.' || TABNAME = (select TABSCHEMA || '.' || TABNAME from SYSCAT.TABLES where tableid = ? and OWNERTYPE = 'U') "
							+ "and COLNO = ?";
					try {
						ps.close();
						ps = con.prepareStatement(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
						ps.setString(1, tabel);
						ps.setString(2, kolom);
						rs = ps.executeQuery();
						if (rs.next()) {
							metadata.put("message", "Kolom " + rs.getString("COLNAME") + " tidak boleh kosong");
						}

					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				} else if (e.getErrorCode() == -302) {
					metadata.put("message", "Lampiran melebihi 1MB");
				} else if (e.getErrorCode() == -530) {
					metadata.put("message", "Foreign key invalid");
				} else if (e.getErrorCode() == -798) {
					metadata.put("message", "Kolom autogenerated tidak bisa diisi");
				} else if (e.getErrorCode() == -803) {
					metadata.put("message", "Constraint kolom karena duplikasi");
				} else
					metadata.put("message", e.getMessage());
				e.printStackTrace();

			} catch (NamingException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
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

				if (koneksi != null)
					if (koneksi.getConnection() != null) {
						koneksi.closeConnection();
						koneksi = null;
					}

			}

		}
		// JSONObject json = new JSONObject();
		// json.put("metadata", new JSONObject(metadata));
		metadataobj.put("metadata", metadata);
		return Response.ok(metadataobj).build();
	}

	@GET
	@Path("/organizationchart/lampiran/{kode}")
	@Produces({ MediaType.APPLICATION_OCTET_STREAM, "application/json" })
	public Response getLampiranpenugasan(@Context HttpHeaders headers, @PathParam("kode") String kode) {
		Map<String, Object> metadataobj = new HashMap<String, Object>();
		Map<String, Object> metadata = new HashMap<String, Object>();
		if (SharedMethod.ServiceAuth(headers, metadata)) {
			Connection con = null;
			PreparedStatement ps = null;
			FileOutputStream fos = null;
			ResultSet rs = null;
			Koneksi koneksi = null;
			Blob blob = null;
			String filename = null;
			String path = null;
			try {
				koneksi = new Koneksi();
				// koneksi.BuatKoneksi();
				con = koneksi.getConnection();

				ps = con.prepareStatement("select lampiran, ekstensi from organisasi.organizationchart where kode = ?");
				ps.setString(1, kode);
				rs = ps.executeQuery();

				File folder = new File("/tmp");
				File[] listOfFiles = folder.listFiles();
				for (int i = 0; i < listOfFiles.length; i++) {
					if (listOfFiles[i].isFile()) {
						if (listOfFiles[i].getName().startsWith("lampiranorganisasi-")) {
							if (listOfFiles[i].exists()) {
								listOfFiles[i].delete();
							}
						}
					}
				}

				if (rs.next()) {
					blob = rs.getBlob("lampiran");

					InputStream is = blob.getBinaryStream();
					path = "/tmp/";
					filename = "lampiranorganisasi-" + kode + "-" + SharedMethod.getTime() + "."
							+ rs.getString("ekstensi");
					fos = new FileOutputStream(path + filename);
					int b = 0;
					while ((b = is.read()) != -1) {
						fos.write(b);
					}
					metadata.put("code", 1);
					metadata.put("message", "Success");
				} else {
					metadata.put("code", 2);
					metadata.put("message", "Lampiran tidak ada");
				}

			} catch (SQLException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				// e.printStackTrace();
			} catch (NamingException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				// e.printStackTrace();
			} catch (FileNotFoundException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				// e.printStackTrace();
			} catch (IOException e) {
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

				if (koneksi != null)
					if (koneksi.getConnection() != null) {
						koneksi.closeConnection();
						koneksi = null;
					}

				if (fos != null)
					try {
						fos.close();
					} catch (IOException e) {
					}
				if (blob != null)
					try {
						blob.free();
					} catch (SQLException e) {
					}
			}

			if ((int) metadata.get("code") == 1) {
				File file = new File(path + filename);
				ResponseBuilder responsee = Response.ok((Object) file);
				responsee.header("Content-Disposition", "inline; filename=" + filename);
				return responsee.build();
			} else {
				// JSONObject json = new JSONObject();
				// json.put("metadata", new JSONObject(metadata));
				metadataobj.put("metadata", metadata);
				return Response.ok(metadataobj).build();
			}
		} else {
			// JSONObject json = new JSONObject();
			// json.put("metadata", new JSONObject(metadata));
			metadataobj.put("metadata", metadata);
			return Response.ok(metadataobj).build();
		}
	}

	@POST
	@Path("/rekap/{servicename}/list/{page}/{row}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response rekapPegawai(@Context HttpHeaders headers, @PathParam("servicename") String servicename,
			@PathParam("page") String page, @PathParam("row") String row, String data) {

		String query = null;
		switch (servicename) {
		case "jobtitle":
		case "pegawai_tanpa_penugasan":
		case "mpp":
			break;

		default:
			return Response.status(Status.NOT_FOUND).build();
		}
		query = "exec organisasi.sp_rekap_" + servicename + " ?, ?, ?, ?, ?, ?, ?";
		return ListReport(headers, page, row, servicename, query, data);

	}

	private static Response ListReport(HttpHeaders headers, String page, String row, String servicename, String query,
			String data) {

		Map<String, Object> response = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> listdata = null;
		Map<String, Object> metadata = new HashMap<String, Object>();
		Connection con = null;
		ResultSet rs = null;
		CallableStatement cs = null;
		// Koneksi koneksi = null;
		String order = null;
		String filter = null;
		Date periode = null;
		String kodeoc = null;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		if (SharedMethod.ServiceAuth(headers, metadata)) {
			try {
				if (data != null) {
					if (!data.isEmpty()) {
						// System.out.println(data);
						ObjectMapper mapper = new ObjectMapper();
						JsonNode json = mapper.readTree(data);

						order = json.path("sort").isMissingNode() ? null
								: SharedMethod.getSortedColumn(mapper.writeValueAsString(json.path("sort")));

						filter = json.path("filter").isMissingNode() ? null
								: SharedMethod.getFilteredColumn(mapper.writeValueAsString(json.path("filter")), null);
						periode = formatter
								.parse(json.path("periode").isMissingNode() ? null : json.path("periode").asText());
						kodeoc = json.path("kodeorganizationchart").isMissingNode() ? null
								: json.path("kodeorganizationchart").asText();
					}
				}

				if ((periode != null) && (kodeoc != null)) {
					con = new Koneksi().getConnection();
					cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
					cs.setInt(1, Integer.parseInt(page));
					cs.setInt(2, Integer.parseInt(row));
					cs.setInt(3, 1);
					cs.setDate(4, new java.sql.Date(periode.getTime()));
					cs.setString(5, kodeoc);
					cs.setString(6, order);
					cs.setString(7, filter);
					rs = cs.executeQuery();
					metadata.put("code", 2);
					metadata.put("message", Response.Status.NO_CONTENT.toString());
					metadata.put("rowcount", 0);

					if (rs.next()) {
						metadata.put("rowcount", rs.getInt("jumlah"));
					}

					listdata = new ArrayList<Map<String, Object>>();

					cs.close();
					rs.close();

					cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
					cs.setInt(1, Integer.parseInt(page));
					cs.setInt(2, Integer.parseInt(row));
					cs.setInt(3, 0);
					cs.setDate(4, new java.sql.Date(periode.getTime()));
					cs.setString(5, kodeoc);
					cs.setString(6, order);
					cs.setString(7, filter);
					rs = cs.executeQuery();
					ResultSetMetaData metaData = rs.getMetaData();
					Map<String, Object> hasil = null;
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
						listdata.add(hasil);
						metadata.put("code", 1);
						metadata.put("message", "OK");
					}
					response.put("listdata", listdata);
					result.put("response", response);
					rs.close();
				} else {
					metadata.put("code", 2);
					metadata.put("message", "Periode dan Kode Bagan Organisasi harus diinput");
				}
			} catch (SQLException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				e.printStackTrace();
			} catch (NamingException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				e.printStackTrace();
			} catch (SecurityException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				e.printStackTrace();
			} catch (InstantiationException e) {
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
}
