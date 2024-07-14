package id.go.bpjskesehatan.service;

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
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.naming.NamingException;
import javax.servlet.ServletContext;
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

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.poi.EncryptedDocumentException;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import id.go.bpjskesehatan.database.Koneksi;
import id.go.bpjskesehatan.entitas.Metadata;
import id.go.bpjskesehatan.entitas.Respon;
import id.go.bpjskesehatan.entitas.Result;
import id.go.bpjskesehatan.entitas.karyawan.Detailalamat;
import id.go.bpjskesehatan.entitas.karyawan.Detailalamat2;
import id.go.bpjskesehatan.entitas.karyawan.Detaildimensijabatan;
import id.go.bpjskesehatan.entitas.karyawan.Detailkpi;
import id.go.bpjskesehatan.entitas.karyawan.Detailpendidikanformal;
import id.go.bpjskesehatan.entitas.karyawan.Detailpendidikannonformal;
import id.go.bpjskesehatan.entitas.karyawan.Detailpengalamankerja;
import id.go.bpjskesehatan.entitas.karyawan.Djpindividu;
import id.go.bpjskesehatan.entitas.karyawan.Djpindividuindex;
import id.go.bpjskesehatan.entitas.karyawan.Foto;
import id.go.bpjskesehatan.entitas.karyawan.Infoassesment;
import id.go.bpjskesehatan.entitas.karyawan.Infoasuransi;
import id.go.bpjskesehatan.entitas.karyawan.Infobaranginventaris;
import id.go.bpjskesehatan.entitas.karyawan.Infodarurat;
import id.go.bpjskesehatan.entitas.karyawan.Infofisik;
import id.go.bpjskesehatan.entitas.karyawan.Infoidentitas;
import id.go.bpjskesehatan.entitas.karyawan.Infoikatandinas;
import id.go.bpjskesehatan.entitas.karyawan.Infokeluarga;
import id.go.bpjskesehatan.entitas.karyawan.Infopegawai;
import id.go.bpjskesehatan.entitas.karyawan.Infopelanggaran;
import id.go.bpjskesehatan.entitas.karyawan.Infopelatihan;
import id.go.bpjskesehatan.entitas.karyawan.Infopendidikan;
import id.go.bpjskesehatan.entitas.karyawan.Infopengalamankerja;
import id.go.bpjskesehatan.entitas.karyawan.Infopengalamanproyek;
import id.go.bpjskesehatan.entitas.karyawan.Infopenghargaan;
import id.go.bpjskesehatan.entitas.karyawan.Infosertifikasi;
import id.go.bpjskesehatan.entitas.karyawan.KaryawanBaru;
import id.go.bpjskesehatan.entitas.karyawan.Lampiranpenugasan;
import id.go.bpjskesehatan.entitas.karyawan.Pegawai;
import id.go.bpjskesehatan.entitas.karyawan.Penugasan;
import id.go.bpjskesehatan.entitas.karyawan.Tandatangan;
import id.go.bpjskesehatan.entitas.karyawan.Tanggungjawab;
import id.go.bpjskesehatan.entitas.organisasi.Jabatan;
import id.go.bpjskesehatan.entitas.organisasi.UnitKerja;
import id.go.bpjskesehatan.service.mobile.v1.AuthUser;
import id.go.bpjskesehatan.util.SharedMethod;
import id.go.bpjskesehatan.util.Utils;

@Path("karyawan")
public class KaryawanRest {

	Map<String, Object> metadata = new HashMap<String, Object>();
	Map<String, Object> metadataobj = new HashMap<String, Object>();
	
	@Context
    private ServletContext context;

	@POST
	@Path("/{servicename}/list/{page}/{row}")
	@Produces("application/json")
	public Response getListData(@Context HttpHeaders headers, @PathParam("servicename") String servicename,
			@PathParam("page") String page, @PathParam("row") String row, String data) {
		String query = null;
		String namasp = null;
		String namaentitas = null;
		Object obj = null;
		switch (servicename) {
		case "pegawai":
			namasp = "sp_listpegawai";
			namaentitas = "pegawai";
			obj = new Pegawai();
			break;
		case "detailalamat":
			namasp = "sp_listdetailalamat";
			namaentitas = "detailalamat";
			obj = new Detailalamat();
			break;
		case "detailalamat2":
			namasp = "sp_listdetailalamat2";
			namaentitas = "detailalamat2";
			obj = new Detailalamat2();
			break;
		case "infodarurat":
			namasp = "sp_listinfodarurat";
			namaentitas = "infodarurat";
			obj = new Infodarurat();
			break;
		case "penugasan":
			namasp = "sp_listpenugasan";
			namaentitas = "penugasan";
			obj = new Penugasan();
			break;
		case "tandatangan":
			namasp = "sp_listtandatangan";
			namaentitas = "tandatangan";
			obj = new Tandatangan();
			break;
		case "lampiranpenugasan":
			namasp = "sp_listlampiranpenugasan";
			namaentitas = "lampiranpenugasan";
			obj = new Lampiranpenugasan();
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
		return RestMethod.getListData(headers, page, row, namaentitas, obj, query, data);
	}

	@GET
	@Path("/{servicename}/id/{npp}")
	@Produces("application/json")
	public Response getListDataByNPP(@Context HttpHeaders headers, @PathParam("servicename") String servicename,
			@PathParam("npp") String npp) {
		String query = null;
		String namasp = null;
		String namaentitas = null;
		Object obj = null;
		switch (servicename) {
		case "pegawai":
			namasp = "sp_listpegawai";
			namaentitas = "pegawai";
			obj = new Pegawai();
			break;
		case "penugasan":
			namasp = "sp_listpenugasan";
			namaentitas = "penugasan";
			obj = new Penugasan();
			break;
		case "pelanggaran":
			namasp = "sp_listinfopelanggaran";
			namaentitas = "pelanggaran";
			obj = new Infopelanggaran();
			break;
		default:
			return Response.status(Status.NOT_FOUND).build();
		}
		query = "exec karyawan." + namasp + " ?, ?, ?, ?, ?";
		String data = "{\"filter\" : {\"npp\" : \"" + npp + "\"}}";
		return RestMethod.getListData(headers, "1", "10", namaentitas, obj, query, data);
	}

	@GET
	@Path("/infopegawai/{npp}")
	@Produces("application/json")
	public Response getPegawaiByNPP(@Context HttpHeaders headers, @PathParam("npp") String npp) {
		//Result<Infopegawai> result = new Result<Infopegawai>();
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> metadata = new HashMap<String, Object>();
		//Metadata metadata = new Metadata();
		Connection con = null;
		ResultSet rs = null;
		CallableStatement cs = null;
		Koneksi koneksi = null;
		if (SharedMethod.ServiceToken(headers, metadata)) {
			try {
				koneksi = new Koneksi();
				// koneksi.BuatKoneksi();
				con = koneksi.getConnection();
				String query = null;
				query = "exec karyawan.sp_infopegawai_by_npp ?";

				cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
				cs.setString(1, npp);
				rs = cs.executeQuery();
				// List<Infopegawai> listdata = new ArrayList<Infopegawai>();
				//Respon<Infopegawai> response = new Respon<Infopegawai>();
				Map<String, Object> response = new HashMap<String, Object>();
				metadata.put("code", 2);
				metadata.put("message", Response.Status.NO_CONTENT.toString());
				//metadata.setCode(2);
				//metadata.setMessage(Response.Status.NO_CONTENT.toString());
				Infopegawai data = new Infopegawai();
				if (rs.next()) {
					data.setNpp(rs.getString("npp"));
					data.setNama(rs.getString("nama"));
					data.setKodekantor(rs.getString("kodekantor"));
					data.setNamakantor(rs.getString("namakantor"));
					data.setKodejabatan(rs.getString("kodejabatan"));
					data.setNamajabatan(rs.getString("namajabatan"));
					data.setEmail(rs.getString("email"));
					data.setNotelp(rs.getString("telepon"));
					data.setKodeunitkerja(rs.getString("kodeunitkerja"));
					data.setNamaunitkerja(rs.getString("namaunitkerja"));
					data.setKodedeputi(rs.getString("kodedeputi"));
					data.setNamadeputi(rs.getString("namadeputi"));
					data.setUpdatedate(rs.getDate("updatedate"));
					data.setCreatedate(rs.getDate("createdate"));
					// listdata.add(data);
					metadata.put("code", 1);
					metadata.put("message", "OK");
					//metadata.setCode(1);
					//metadata.setMessage("OK");
				}
				// response.setList(listdata);
				response.put("data", data);
				result.put("response", response);
				//response.setData(data);
				//result.setResponse(response);
				rs.close();
			} catch (SQLException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
			} catch (NumberFormatException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
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

				if (cs != null) {
					try {
						cs.close();
					} catch (SQLException e) {
					}
				}

				if (koneksi != null)
					koneksi.closeConnection();
				if (con != null) {
					try {
						con.close();
					} catch (SQLException e) {
					}
				}
			}
		}
		//result.setMetadata(metadata);
		result.put("metadata", metadata);
		// JSONObject xmlJSONObj = new JSONObject(result);
		return Response.ok(result).build();
	}

	@GET
	@Path("/pegawai.info1/{npp}")
	@Produces("application/json")
	public Response getPegawai2ByNPP(@Context HttpHeaders headers, @PathParam("npp") String npp) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> metadata = new HashMap<String, Object>();
		Connection con = null;
		ResultSet rs = null;
		CallableStatement cs = null;
		Koneksi koneksi = null;
		if (SharedMethod.ServiceToken(headers, metadata)) {
			try {
				koneksi = new Koneksi();
				con = koneksi.getConnection();
				String query = null;
				query = "select * from karyawan.vpegawai_info1 where npp = ?";

				cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
				cs.setString(1, npp);
				rs = cs.executeQuery();

				Map<String, Object> response = new HashMap<String, Object>();
				metadata.put("code", 2);
				metadata.put("message", Response.Status.NO_CONTENT.toString());
				Map<String, Object> data = new HashMap<String, Object>();
				if (rs.next()) {
					data.put("npp", rs.getString("npp"));
					data.put("nama", rs.getString("nama"));
					data.put("hp", rs.getString("hp"));
					data.put("hp2", rs.getString("hp2"));
					data.put("noid", rs.getString("noidentitas"));
					data.put("tgllahir", rs.getDate("tgllahir"));
					metadata.put("code", 1);
					metadata.put("message", "OK");
				}
				response.put("data", data);
				result.put("response", response);
				rs.close();
			} catch (SQLException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());

			} catch (NumberFormatException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());

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

				if (cs != null) {
					try {
						cs.close();
					} catch (SQLException e) {
					}
				}

				if (koneksi != null)
					koneksi.closeConnection();
				if (con != null) {
					try {
						con.close();
					} catch (SQLException e) {
					}
				}
			}
		} else {
			result.put("metadata", metadata);
			return Response.status(Status.UNAUTHORIZED).entity(result).build();
		}
		result.put("metadata", metadata);
		return Response.ok(result).build();
	}

	@GET
	@Path("/atasan/{npp}")
	@Produces("application/json")
	public Response getAtasan(@Context HttpHeaders headers, @PathParam("npp") String npp) {
		Result<Penugasan> result = new Result<Penugasan>();
		Metadata metadata = new Metadata();
		Connection con = null;
		ResultSet rs = null;
		CallableStatement cs = null;
		Koneksi koneksi = null;
		if (SharedMethod.ServiceAuth(headers, metadata)) {
			try {
				koneksi = new Koneksi();
				// koneksi.BuatKoneksi();
				con = koneksi.getConnection();
				String query = null;
				query = "exec karyawan.sp_infoatasan ?";

				cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
				cs.setString(1, npp);
				rs = cs.executeQuery();
				// List<Infopegawai> listdata = new ArrayList<Infopegawai>();
				Respon<Penugasan> response = new Respon<Penugasan>();
				metadata.setCode(2);
				metadata.setMessage(Response.Status.NO_CONTENT.toString());
				Penugasan data = new Penugasan();
				if (rs.next()) {
					Jabatan jabatan = new Jabatan();
					Jabatan parent = new Jabatan();
					Jabatan parent2 = new Jabatan();
					Pegawai atasan = new Pegawai();
					Pegawai atasan2 = new Pegawai();
					Penugasan penugasanatasan = new Penugasan();
					Penugasan penugasanatasan2 = new Penugasan();

					atasan2.setNpp(rs.getString("nppatasan2"));
					atasan2.setNama(rs.getString("namaatasan2"));
					penugasanatasan2.setPegawai(atasan2);
					parent2.setKode(rs.getString("kodejabatanatasan2"));
					parent2.setNama(rs.getString("namajabatanatasan2"));
					parent2.setPenugasan(penugasanatasan2);
					parent.setParent(parent2);

					atasan.setNpp(rs.getString("nppatasan"));
					atasan.setNama(rs.getString("namaatasan"));
					penugasanatasan.setPegawai(atasan);
					parent.setKode(rs.getString("kodejabatanatasan"));
					parent.setNama(rs.getString("namajabatanatasan"));
					parent.setPenugasan(penugasanatasan);
					jabatan.setParent(parent);

					UnitKerja unitkerja = new UnitKerja();
					jabatan.setKode(rs.getString("kodejabatan"));
					jabatan.setNama(rs.getString("namajabatan"));
					unitkerja.setKode(rs.getString("kodeunitkerja"));
					unitkerja.setNama(rs.getString("namaunitkerja"));
					jabatan.setUnitkerja(unitkerja);

					data.setJabatan(jabatan);
					Pegawai pegawai = new Pegawai();
					pegawai.setNpp(rs.getString("npp"));
					pegawai.setNama(rs.getString("nama"));
					data.setPegawai(pegawai);
					// listdata.add(data);
					metadata.setCode(1);
					metadata.setMessage("OK");
				}
				// response.setList(listdata);
				response.setData(data);
				result.setResponse(response);
				rs.close();
			} catch (SQLException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());

			} catch (NumberFormatException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());

			} catch (NamingException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());

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

				if (koneksi != null)
					koneksi.closeConnection();
				if (con != null) {
					try {
						con.close();
					} catch (SQLException e) {
					}
				}
			}
		}
		result.setMetadata(metadata);
		// JSONObject xmlJSONObj = new JSONObject(result);
		return Response.ok(result).build();
	}

	@POST
	@Path("/{servicename}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response createData(@Context HttpHeaders headers, @PathParam("servicename") String servicename,
			String data) {

		Object obj = null;
		String namatabel = null;
		namatabel = "karyawan." + servicename;
		switch (servicename) {
		case "pegawai":
			obj = new Pegawai();
			break;
		case "detailalamat":
			obj = new Detailalamat();
			break;
		case "detailalamat2":
			obj = new Detailalamat2();
			break;
		case "infodarurat":
			obj = new Infodarurat();
			break;
		case "penugasan":
			obj = new Penugasan();
			break;
		case "infoidentitas":
			obj = new Infoidentitas();
			break;
		case "infofisik":
			obj = new Infofisik();
			break;
		case "infokeluarga":
			obj = new Infokeluarga();
			break;
		case "infopendidikan":
			obj = new Infopendidikan();
			break;
		case "infopengalamankerja":
			obj = new Infopengalamankerja();
			break;
		case "infoasuransi":
			obj = new Infoasuransi();
			break;
		case "infobaranginventaris":
			obj = new Infobaranginventaris();
			break;
		case "infoassesment":
			obj = new Infoassesment();
			break;
		case "infopelatihan":
			obj = new Infopelatihan();
			break;
		case "infosertifikasi":
			obj = new Infosertifikasi();
			break;
		case "infopengalamanproyek":
			obj = new Infopengalamanproyek();
			break;
		case "infopenghargaan":
			obj = new Infopenghargaan();
			break;
		case "infoikatandinas":
			obj = new Infoikatandinas();
			break;
		case "djp":
			obj = new Djpindividu();
			break;
		case "tanggungjawab":
			obj = new Tanggungjawab();
			break;
		case "detaildimensijabatan":
			obj = new Detaildimensijabatan();
			break;
		case "detailkpi":
			obj = new Detailkpi();
			break;
		case "detailpendidikanformal":
			obj = new Detailpendidikanformal();
			break;
		case "detailpendidikannonformal":
			obj = new Detailpendidikannonformal();
			break;
		case "detailpengalamankerja":
			obj = new Detailpengalamankerja();
			break;
		default:
			return Response.status(Status.NOT_FOUND).build();
		}
		return RestMethod.createData(headers, data, obj, namatabel);
	}

	@PUT
	@Path("/{servicename}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response updateData(@Context HttpHeaders headers, @PathParam("servicename") String servicename,
			String data) {

		Object obj = null;
		String namatabel = null;
		namatabel = "karyawan." + servicename;
		switch (servicename) {
		case "pegawai":
			obj = new Pegawai();
			break;
		case "detailalamat":
			obj = new Detailalamat();
			break;
		case "detailalamat2":
			obj = new Detailalamat2();
			break;
		case "infodarurat":
			obj = new Infodarurat();
			break;
		case "penugasan":
			obj = new Penugasan();
			break;
		case "infoidentitas":
			obj = new Infoidentitas();
			break;
		case "infofisik":
			obj = new Infofisik();
			break;
		case "infokeluarga":
			obj = new Infokeluarga();
			break;
		case "infopendidikan":
			obj = new Infopendidikan();
			break;
		case "infopengalamankerja":
			obj = new Infopengalamankerja();
			break;
		case "infoasuransi":
			obj = new Infoasuransi();
			break;
		case "infobaranginventaris":
			obj = new Infobaranginventaris();
			break;
		case "infoassesment":
			obj = new Infoassesment();
			break;
		case "infopelatihan":
			obj = new Infopelatihan();
			break;
		case "infosertifikasi":
			obj = new Infosertifikasi();
			break;
		case "infopengalamanproyek":
			obj = new Infopengalamanproyek();
			break;
		case "infopenghargaan":
			obj = new Infopenghargaan();
			break;
		case "infoikatandinas":
			obj = new Infoikatandinas();
			break;
		case "djp":
			obj = new Djpindividu();
			break;
		case "tanggungjawab":
			obj = new Tanggungjawab();
			break;
		case "detaildimensijabatan":
			obj = new Detaildimensijabatan();
			break;
		case "detailkpi":
			obj = new Detailkpi();
			break;
		case "detailpendidikanformal":
			obj = new Detailpendidikanformal();
			break;
		case "detailpendidikannonformal":
			obj = new Detailpendidikannonformal();
			break;
		case "detailpengalamankerja":
			obj = new Detailpengalamankerja();
			break;
		default:
			return Response.status(Status.NOT_FOUND).build();
		}
		return RestMethod.updateData(headers, data, obj, namatabel);
	}

	@POST
	@Path("/{servicename}/delete")
	@Produces("application/json")
	@Consumes("application/json")
	public Response deleteData(@Context HttpHeaders headers, String data,
			@PathParam("servicename") String servicename) {
		Object obj = null;
		String namatabel = null;
		switch (servicename) {
		case "pegawai":
			obj = new Pegawai();
			namatabel = "karyawan.pegawai";
			break;
		case "detailalamat":
			obj = new Detailalamat();
			namatabel = "karyawan.detailalamat";
			break;
		case "detailalamat2":
			obj = new Detailalamat2();
			namatabel = "karyawan.detailalamat2";
			break;
		case "infodarurat":
			obj = new Infodarurat();
			namatabel = "karyawan.infodarurat";
			break;
		case "penugasan":
			obj = new Penugasan();
			namatabel = "karyawan.penugasan";
			break;
		case "lampiranpenugasan":
			obj = new Lampiranpenugasan();
			namatabel = "karyawan.lampiranpenugasan";
			break;
		case "foto":
			obj = new Foto();
			namatabel = "karyawan.foto";
			break;
		case "tandatangan":
			obj = new Tandatangan();
			namatabel = "karyawan.tandatangan";
			break;
		case "infoidentitas":
			obj = new Infoidentitas();
			namatabel = "karyawan.infoidentitas";
			break;
		case "infofisik":
			obj = new Infofisik();
			namatabel = "karyawan.infofisik";
			break;
		case "infokeluarga":
			obj = new Infokeluarga();
			namatabel = "karyawan.infokeluarga";
			break;
		case "infopendidikan":
			obj = new Infopendidikan();
			namatabel = "karyawan.infopendidikan";
			break;
		case "infopengalamankerja":
			obj = new Infopengalamankerja();
			namatabel = "karyawan.infopengalamankerja";
			break;
		case "infoasuransi":
			obj = new Infoasuransi();
			namatabel = "karyawan.infoasuransi";
			break;
		case "infobaranginventaris":
			obj = new Infobaranginventaris();
			namatabel = "karyawan.infobaranginventaris";
			break;
		case "infoassesment":
			obj = new Infoassesment();
			namatabel = "karyawan.infoassesment";
			break;
		case "infopelatihan":
			obj = new Infopelatihan();
			namatabel = "karyawan.infopelatihan";
			break;
		case "infosertifikasi":
			obj = new Infosertifikasi();
			namatabel = "karyawan.infosertifikasi";
			break;
		case "infopengalamanproyek":
			obj = new Infopengalamanproyek();
			namatabel = "karyawan.infopengalamanproyek";
			break;
		case "infopenghargaan":
			obj = new Infopenghargaan();
			namatabel = "karyawan.infopenghargaan";
			break;
		case "infoikatandinas":
			obj = new Infoikatandinas();
			namatabel = "karyawan.infoikatandinas";
			break;
		case "djp":
			obj = new Djpindividu();
			namatabel = "karyawan.djp";
			break;
		case "tanggungjawab":
			obj = new Tanggungjawab();
			namatabel = "karyawan.tanggungjawab";
			break;
		case "detaildimensijabatan":
			obj = new Detaildimensijabatan();
			break;
		case "detailkpi":
			obj = new Detailkpi();
			namatabel = "karyawan.detailkpi";
			break;
		case "detailpendidikanformal":
			obj = new Detailpendidikanformal();
			namatabel = "karyawan.detailpendidikanformal";
			break;
		case "detailpendidikannonformal":
			obj = new Detailpendidikannonformal();
			namatabel = "karyawan.detailpendidikannonformal";
			break;
		case "detailpengalamankerja":
			obj = new Detailpengalamankerja();
			namatabel = "karyawan.detailpengalamankerja";
			break;
		}
		return RestMethod.deleteData(headers, data, obj, namatabel);
	}

	@GET
	@Path("/foto/{npp}")
	@Produces({ MediaType.APPLICATION_OCTET_STREAM, "application/json" })
	public Response getFoto(@Context HttpHeaders headers, @PathParam("npp") String npp) {
		// Map<String, Object> metadata = new HashMap<String, Object>();
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

				ps = con.prepareStatement("select lampiran, ekstensi from karyawan.foto where npp = ?");
				ps.setString(1, npp);
				rs = ps.executeQuery();

				File folder = new File("/tmp");
				File[] listOfFiles = folder.listFiles();

				for (int i = 0; i < listOfFiles.length; i++) {
					if (listOfFiles[i].isFile()) {
						if (listOfFiles[i].getName().startsWith("foto-")) {
							if (listOfFiles[i].exists()) {
								listOfFiles[i].delete();
							}
						}
					}
				}

				if (rs.next()) {
					blob = rs.getBlob("lampiran");
					// lampiran = blob.getBytes(1, (int) blob.length());
					path = "/tmp/";
					filename = "foto-" + npp + "-" + SharedMethod.getTime() + "." + rs.getString("ekstensi");
					fos = new FileOutputStream(path + filename);
					int b = 0;
					InputStream is = blob.getBinaryStream();
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
	
	@GET
	@Path("/foto/{token}/{npp}")
	@Produces({ MediaType.APPLICATION_OCTET_STREAM, "application/json" })
	public Response getTokenFoto(@Context HttpHeaders headers, @PathParam("token") String token, @PathParam("npp") String npp) {
		// Map<String, Object> metadata = new HashMap<String, Object>();
		if (SharedMethod.VerifyToken2(token, metadata)) {
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

				ps = con.prepareStatement("select lampiran, ekstensi from karyawan.foto where npp = ?");
				ps.setString(1, npp);
				rs = ps.executeQuery();

				File folder = new File("/tmp");
				File[] listOfFiles = folder.listFiles();

				for (int i = 0; i < listOfFiles.length; i++) {
					if (listOfFiles[i].isFile()) {
						if (listOfFiles[i].getName().startsWith("foto-")) {
							if (listOfFiles[i].exists()) {
								listOfFiles[i].delete();
							}
						}
					}
				}

				if (rs.next()) {
					blob = rs.getBlob("lampiran");
					// lampiran = blob.getBytes(1, (int) blob.length());
					path = "/tmp/";
					filename = "foto-" + npp + "-" + SharedMethod.getTime() + "." + rs.getString("ekstensi");
					fos = new FileOutputStream(path + filename);
					int b = 0;
					InputStream is = blob.getBinaryStream();
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

	@GET
	@Path("/tandatangan/{npp}")
	@Produces({ MediaType.APPLICATION_OCTET_STREAM, "application/json" })
	public Response getTandatangan(@Context HttpHeaders headers, @PathParam("npp") String npp) {
		// Map<String, Object> metadata = new HashMap<String, Object>();
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

				ps = con.prepareStatement("select lampiran, ekstensi from karyawan.tandatangan where npp = ?");
				ps.setString(1, npp);
				rs = ps.executeQuery();
				// byte[] lampiran;
				// byte[] buffer = new byte[1024];

				File folder = new File("/tmp");
				File[] listOfFiles = folder.listFiles();

				for (int i = 0; i < listOfFiles.length; i++) {
					if (listOfFiles[i].isFile()) {
						if (listOfFiles[i].getName().startsWith("tandatangan-")) {
							if (listOfFiles[i].exists()) {
								listOfFiles[i].delete();
							}
						}
					}
				}

				if (rs.next()) {
					blob = rs.getBlob("lampiran");
					// lampiran = blob.getBytes(1, (int) blob.length());
					InputStream is = blob.getBinaryStream();
					path = "/tmp/";
					filename = "tandatangan-" + npp + "-" + SharedMethod.getTime() + "." + rs.getString("ekstensi");
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
	
	@GET
	@Path("/tandatangan/{token}/{npp}")
	@Produces({ MediaType.APPLICATION_OCTET_STREAM, "application/json" })
	public Response getTokenTandatangan(@Context HttpHeaders headers, @PathParam("token") String token, @PathParam("npp") String npp) {
		// Map<String, Object> metadata = new HashMap<String, Object>();
		if (SharedMethod.VerifyToken2(token, metadata)) {
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

				ps = con.prepareStatement("select lampiran, ekstensi from karyawan.tandatangan where npp = ?");
				ps.setString(1, npp);
				rs = ps.executeQuery();
				// byte[] lampiran;
				// byte[] buffer = new byte[1024];

				File folder = new File("/tmp");
				File[] listOfFiles = folder.listFiles();

				for (int i = 0; i < listOfFiles.length; i++) {
					if (listOfFiles[i].isFile()) {
						if (listOfFiles[i].getName().startsWith("tandatangan-")) {
							if (listOfFiles[i].exists()) {
								listOfFiles[i].delete();
							}
						}
					}
				}
				
				if (rs.next()) {
					blob = rs.getBlob("lampiran");
					// lampiran = blob.getBytes(1, (int) blob.length());
					
					path = "/tmp/";
					filename = "tandatangan-" + npp + "-" + SharedMethod.getTime() + "." + rs.getString("ekstensi");
					fos = new FileOutputStream(path + filename);
					int b = 0;
					InputStream is = blob.getBinaryStream();
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

	@GET
	@Path("/lampiranpenugasan/{kodepenugasan}")
	@Produces({ MediaType.APPLICATION_OCTET_STREAM, "application/json" })
	public Response getLampiranpenugasan(@Context HttpHeaders headers,
			@PathParam("kodepenugasan") String kodepenugasan) {
		// Map<String, Object> metadata = new HashMap<String, Object>();
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

				ps = con.prepareStatement(
						"select lampiran, ekstensi from karyawan.lampiranpenugasan where kodepenugasan = ?");
				ps.setString(1, kodepenugasan);
				rs = ps.executeQuery();

				File folder = new File("/tmp");
				File[] listOfFiles = folder.listFiles();
				for (int i = 0; i < listOfFiles.length; i++) {
					if (listOfFiles[i].isFile()) {
						if (listOfFiles[i].getName().startsWith("lampiranpenugasan-")) {
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
					filename = "lampiranpenugasan-" + kodepenugasan + "-" + SharedMethod.getTime() + "."
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

	@GET
	@Path("/lampiranpenugasannonbagan/{kodepenugasan}")
	@Produces({ MediaType.APPLICATION_OCTET_STREAM, "application/json" })
	public Response getLampiranpenugasannonbagan(@Context HttpHeaders headers,
			@PathParam("kodepenugasan") String kodepenugasan) {
		// Map<String, Object> metadata = new HashMap<String, Object>();
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

				ps = con.prepareStatement(
						"select lampiran, ekstensi from karyawan.lampiranpenugasannonbagan where kodepenugasannonbagan = ?");
				ps.setString(1, kodepenugasan);
				rs = ps.executeQuery();

				File folder = new File("/tmp");
				File[] listOfFiles = folder.listFiles();
				for (int i = 0; i < listOfFiles.length; i++) {
					if (listOfFiles[i].isFile()) {
						if (listOfFiles[i].getName().startsWith("lampiranpenugasannonbagan-")) {
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
					filename = "lampiranpenugasannonbagan-" + kodepenugasan + "-" + SharedMethod.getTime() + "."
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
	@Path("/lampiranpenugasan")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces("application/json")
	public Response createLampiranPenugasan(@Context HttpHeaders headers,
			@FormDataParam("kodepenugasan") int kodePenugasan,
			@FormDataParam("lampiran") InputStream uploadedInputStream, @FormDataParam("ekstensi") String ekstensi) {
		// Map<String, Object> metadata = new HashMap<String, Object>();
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

				ps = con.prepareStatement(
						"select kodepenugasan from karyawan.lampiranpenugasan where kodepenugasan = ?");
				ps.setInt(1, kodePenugasan);
				rs = ps.executeQuery();
				if (rs.next()) {
					rs.close();
					ps.close();
					ps = con.prepareStatement(
							"update karyawan.lampiranpenugasan set ekstensi = ?, lampiran = ? where kodepenugasan = ?");
					ps.setString(1, ekstensi.toLowerCase());
					ps.setBlob(2, uploadedInputStream);
					ps.setInt(3, kodePenugasan);
				} else {
					rs.close();
					ps.close();
					ps = con.prepareStatement(
							"insert into karyawan.lampiranpenugasan (kodepenugasan, ekstensi, lampiran) values(?, ?, ?)");
					ps.setInt(1, kodePenugasan);
					ps.setString(2, ekstensi.toLowerCase());
					ps.setBlob(3, uploadedInputStream);
				}

				ps.executeUpdate();
				ps.close();
				con.commit();
				metadata.put("code", 1);
				metadata.put("message", "Data berhasil diupload");

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

	@POST
	@Path("/lampiranpenugasannonbagan")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces("application/json")
	public Response createLampiranPenugasanNonBagan(@Context HttpHeaders headers,
			@FormDataParam("kodepenugasan") int kodePenugasan,
			@FormDataParam("lampiran") InputStream uploadedInputStream, @FormDataParam("ekstensi") String ekstensi) {
		// Map<String, Object> metadata = new HashMap<String, Object>();
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

				ps = con.prepareStatement(
						"select kodepenugasan from karyawan.lampiranpenugasannonbagan where kodepenugasannonbagan = ?");
				ps.setInt(1, kodePenugasan);
				rs = ps.executeQuery();
				if (rs.next()) {
					rs.close();
					ps.close();
					ps = con.prepareStatement(
							"update karyawan.lampiranpenugasannonbagan set ekstensi = ?, lampiran = ? where kodepenugasannonbagan = ?");
					ps.setString(1, ekstensi.toLowerCase());
					ps.setBlob(2, uploadedInputStream);
					ps.setInt(3, kodePenugasan);
				} else {
					rs.close();
					ps.close();
					ps = con.prepareStatement(
							"insert into karyawan.lampiranpenugasannonbagan (kodepenugasannonbagan, ekstensi, lampiran) values(?, ?, ?)");
					ps.setInt(1, kodePenugasan);
					ps.setString(2, ekstensi.toLowerCase());
					ps.setBlob(3, uploadedInputStream);
				}

				ps.executeUpdate();
				ps.close();
				con.commit();
				metadata.put("code", 1);
				metadata.put("message", "Data berhasil diupload");

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

	@POST
	@Path("/foto")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces("application/json")
	public Response createFoto(@Context HttpHeaders headers, @FormDataParam("npp") String Npp,
			@FormDataParam("lampiran") InputStream uploadedInputStream, @FormDataParam("ekstensi") String ekstensi) {
		// Map<String, Object> metadata = new HashMap<String, Object>();
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

				// ps =
				// con.prepareStatement("insert into karyawan.foto (npp,
				// ekstensi,
				// lampiran) values(?, ?, ?)");
				// ps.setString(1, Npp);
				// ps.setString(2, ekstensi.toLowerCase());
				// ps.setBlob(3, uploadedInputStream);

				ps = con.prepareStatement("select npp from karyawan.foto where npp = ?");
				ps.setString(1, Npp);
				rs = ps.executeQuery();
				if (rs.next()) {
					rs.close();
					ps.close();
					ps = con.prepareStatement("update karyawan.foto set ekstensi = ?, lampiran = ? where npp = ?");
					ps.setString(1, ekstensi.toLowerCase());
					ps.setBlob(2, uploadedInputStream);
					ps.setString(3, Npp);
				} else {
					rs.close();
					ps.close();
					ps = con.prepareStatement("insert into karyawan.foto (npp, ekstensi, lampiran) values(?, ?, ?)");
					ps.setString(1, Npp);
					ps.setString(2, ekstensi.toLowerCase());
					ps.setBlob(3, uploadedInputStream);
				}

				ps.executeUpdate();
				ps.close();
				con.commit();
				metadata.put("code", 1);
				metadata.put("message", "Data berhasil diupload");

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
					metadata.put("message", "Lampiran melebihi 512KB");
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

	@POST
	@Path("/tandatangan")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces("application/json")
	public Response createTandatangan(@Context HttpHeaders headers, @FormDataParam("npp") String Npp,
			@FormDataParam("lampiran") InputStream uploadedInputStream, @FormDataParam("ekstensi") String ekstensi) {
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
				ps = con.prepareStatement("select npp from karyawan.tandatangan where npp = ?");
				ps.setString(1, Npp);
				rs = ps.executeQuery();
				if (rs.next()) {
					rs.close();
					ps.close();
					ps = con.prepareStatement(
							"update karyawan.tandatangan set ekstensi = ?, lampiran = ? where npp = ?");
					ps.setString(1, ekstensi.toLowerCase());
					ps.setBlob(2, uploadedInputStream);
					ps.setString(3, Npp);
				} else {
					rs.close();
					ps.close();
					ps = con.prepareStatement(
							"insert into karyawan.tandatangan (npp, ekstensi, lampiran) values(?, ?, ?)");
					ps.setString(1, Npp);
					ps.setString(2, ekstensi.toLowerCase());
					ps.setBlob(3, uploadedInputStream);
				}
				ps.executeUpdate();
				ps.close();
				con.commit();
				metadata.put("code", 1);
				metadata.put("message", "Data berhasil diupload");
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
					metadata.put("message", "Lampiran melebihi 512KB");
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
	@Path("/{servicename}/lampiran/{kode}")
	// @Consumes("application/json")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getLampiran(@Context HttpHeaders headers, @PathParam("kode") String kode,
			@PathParam("servicename") String servicename) {
		Map<String, Object> metadata = new HashMap<String, Object>();
		if (SharedMethod.ServiceAuth(headers, metadata)) {
			switch (servicename) {
			case "infoidentitas":
			case "infoassesment":
			case "infopelatihan":
			case "infosertifikasi":
			case "infopendidikan":
			case "infopengalamankerja":
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

					ps = con.prepareStatement("select lampiran from karyawan." + servicename + " where kode = ?");
					ps.setString(1, kode);
					rs = ps.executeQuery();

					File folder = new File("/tmp");
					File[] listOfFiles = folder.listFiles();

					for (int i = 0; i < listOfFiles.length; i++) {
						if (listOfFiles[i].isFile()) {
							if (listOfFiles[i].getName().startsWith(servicename + "-")) {
								if (listOfFiles[i].exists()) {
									listOfFiles[i].delete();
								}
							}
						}
					}

					if (rs.next()) {
						blob = rs.getBlob("lampiran");
						// lampiran = blob.getBytes(1, (int) blob.length());
						InputStream is = blob.getBinaryStream();
						path = "/tmp/";
						filename = servicename + "-" + SharedMethod.getTime();
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
			default:
				return Response.status(Status.NOT_FOUND).build();
			}

		} else {
			// JSONObject json = new JSONObject();
			// json.put("metadata", new JSONObject(metadata));

			metadataobj.put("metadata", metadata);
			return Response.ok(metadataobj).build();
		}

	}

	@POST
	@Path("/{servicename}/lampiran")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces("application/json")
	public Response createLampiran(@Context HttpHeaders headers, @FormDataParam("kode") String kode,
			@PathParam("servicename") String servicename, @FormDataParam("lampiran") InputStream uploadedInputStream) {
		Map<String, Object> metadata = new HashMap<String, Object>();
		if (SharedMethod.ServiceAuth(headers, metadata)) {
			Connection con = null;
			PreparedStatement ps = null;

			switch (servicename) {
			case "infoidentitas":
			case "infokeluarga":
			case "infoassesment":
			case "infopelatihan":
			case "infosertifikasi":
			case "infopendidikan":
			case "infopengalamankerja":
				ResultSet rs = null;
				Koneksi koneksi = null;
				try {
					koneksi = new Koneksi();
					// koneksi.BuatKoneksi();
					con = koneksi.getConnection();
					con.setAutoCommit(false);

					ps = con.prepareStatement("select npp from karyawan." + servicename + " where kode = ?");
					ps.setString(1, kode);
					rs = ps.executeQuery();
					if (rs.next()) {
						rs.close();
						ps.close();
						ps = con.prepareStatement(
								"update karyawan." + servicename + " set lampiran = ? where kode = ?");
						ps.setBlob(1, uploadedInputStream);
						ps.setString(2, kode);
						ps.executeUpdate();
						ps.close();
						con.commit();
						metadata.put("code", 1);
						metadata.put("message", "Data berhasil diupload");
					} else {
						metadata.put("code", 2);
						metadata.put("message", "Data kode tidak ditemukan");
					}

				} catch (EncryptedDocumentException e) {
					metadata.put("code", 0);
					metadata.put("message", e.getMessage());
				} catch (SQLException e) {
					metadata.put("code", 0);
					metadata.put("message", SharedMethod.getErrorMessage(e));

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
				// JSONObject json = new JSONObject();
				// json.put("metadata", new JSONObject(metadata));

				metadataobj.put("metadata", metadata);
				return Response.ok(metadataobj).build();
			default:
				return Response.status(Status.NOT_FOUND).build();
			}
		}
		// JSONObject json = new JSONObject();
		// json.put("metadata", new JSONObject(metadata));

		metadataobj.put("metadata", metadata);
		return Response.ok(metadataobj).build();
	}

	@POST
	@Path("/rekap/{servicename}/list/{page}/{row}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response rekapPegawai(@Context HttpHeaders headers, @PathParam("servicename") String servicename,
			@PathParam("page") String page, @PathParam("row") String row, String data) {

		String query = null;
		switch (servicename) {
		case "agama":
		case "jeniskelamin":
		case "pangkat":
		case "subgrade":
		case "statuspegawai":
		case "statusjabatan":
		case "masakerja":
		case "usia":
		case "pendidikan":
		case "jeniskantor":
		case "rentangusia":
		case "ulangtahun":
			break;

		default:
			return Response.status(Status.NOT_FOUND).build();
		}
		query = "exec karyawan.sp_rekappegawai_" + servicename + " ?, ?, ?, ?, ?, ?";
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
					}
				}

				if (periode != null) {
					con = new Koneksi().getConnection();
					cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
					cs.setInt(1, Integer.parseInt(page));
					cs.setInt(2, Integer.parseInt(row));
					cs.setInt(3, 1);
					cs.setDate(4, new java.sql.Date(periode.getTime()));
					cs.setString(5, order);
					cs.setString(6, filter);
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
					cs.setString(5, order);
					cs.setString(6, filter);
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
					metadata.put("message", "Periode harus diinput");
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
	
	@POST
	@Path("/addpegawai/{tambahan}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces("application/json")
	public Response addPegawai(
			@Context HttpHeaders headers, 
			@FormDataParam("filepenugasan") final InputStream filePenugasanInputStream, 
			@FormDataParam("filepenugasan") FormDataContentDisposition filePenugasanDetail,
			@FormDataParam("filepenugasan") String filePenugasan,
			@FormDataParam("fileikatandinas") final InputStream fileIkatanDinasInputStream, 
			@FormDataParam("fileikatandinas") FormDataContentDisposition fileIkatanDinasDetail,
			@FormDataParam("fileikatandinas") String fileIkatanDinas,
			@FormDataParam("pegawai") FormDataBodyPart post,
			@PathParam("tambahan") String tambahan) {
		Connection con = null;
		CallableStatement cs = null;
		Respon<Pegawai> response = new Respon<Pegawai>();
		Result<Pegawai> result = new Result<Pegawai>();
		Metadata metadata = new Metadata();
		String queri = null;
		
		AuthUser authUser = new AuthUser();
		if (SharedMethod.VerifyToken(authUser, headers, metadata)) {
			try {
				String npp = null;
				String namadepan = null;
				String namatengah = null;
				String namabelakang = null;
				String kodeasalpenerimaan = null;
				String tmtmasuk = null;
				String tempatlahir = null;
				String tgllahir = null;
				Integer kodejeniskelamin = null;
				Integer kodestatuskaryawan = null;
				Integer row_status = null;
				Integer created_by = null;
				String email = null;
				
				String tglsk = null;
				String nomorsk = null;
				Integer kodejenissk = null;
				String lokasikantor = null;
				String unitkerja = null;
				String jobtitle = null;
				String pangkat = null;
				String grade = null;
				Integer kodestatusjabatan = null;
				Integer kodejenisikatandinas = null;
				String pemberiikatandinas = null;
				String institusiikatandinas = null;
				String lokasiikatandinas = null;
				String tglberlakuikatandinas = null;
				String tglberakhirikatandinas = null;
				String catatanikatandinas = null;
				String lampiranpenugasan = null;
				String lampiranikatandinas = null;
				Integer useract = null;
				
				String npp_output = null;
				
				post.setMediaType(MediaType.APPLICATION_JSON_TYPE);
				KaryawanBaru json = post.getValueAs(KaryawanBaru.class);
				
				switch (tambahan) {
				case "tanpapenugasan":
					npp = json.getNpp().trim();
					namadepan = json.getNamadepan().trim();
					namatengah = json.getNamatengah().trim();
					namabelakang = json.getNamabelakang().trim();
					kodeasalpenerimaan = json.getKodeasalpenerimaan().trim();
					tmtmasuk = json.getTmtmasuk().trim();
					tempatlahir = json.getTempatlahir().trim();
					tgllahir = json.getTgllahir().trim();
					kodejeniskelamin = json.getKodejeniskelamin();
					kodestatuskaryawan = json.getKodestatuskaryawan();
					row_status = 1;
					created_by = authUser.getUserid();
					email = json.getEmail().trim();
					
					try {
						queri = "exec karyawan.sp_insert_pegawai_excludepenugasan ?,?,?,?,?,?,?,?,?,?,?,?,?,?";
						con = new Koneksi().getConnection();
						cs = con.prepareCall(queri);
						cs.setString(1, npp);
						cs.setString(2, namadepan);
						cs.setString(3, namatengah);
						cs.setString(4, namabelakang);
						cs.setString(5, kodeasalpenerimaan);
						cs.setDate(6, Utils.StringDateToSqlDate(tmtmasuk));
						cs.setString(7, tempatlahir);
						cs.setDate(8, Utils.StringDateToSqlDate(tgllahir));
						cs.setInt(9, kodejeniskelamin);
						cs.setInt(10, kodestatuskaryawan);
						cs.setInt(11, row_status);
						cs.setInt(12, created_by);
						cs.setString(13, email);
						cs.registerOutParameter(14, java.sql.Types.VARCHAR);
						cs.execute();
						npp_output = cs.getString(14);
						Pegawai pegawai = new Pegawai();
						pegawai.setNpp(npp_output);
						response.setData(pegawai);
						result.setResponse(response);
						metadata.setCode(1);
						metadata.setMessage("Ok.");
					} catch (Exception e) {
						metadata.setCode(0);
						metadata.setMessage(e.getMessage());
						e.printStackTrace();
					} finally {
						if (cs != null) {
							try {
								cs.close();
							} catch (SQLException e) {
								e.printStackTrace();
							}
						}
						if (con != null) {
							try {
								con.close();
							} catch (SQLException e) {
								e.printStackTrace();
							}
						}	
					}
					break;
				case "denganpenugasan":					
					npp = json.getNpp().trim();
					namadepan = json.getNamadepan().trim();
					namatengah = json.getNamatengah().trim();
					namabelakang = json.getNamabelakang().trim();
					tempatlahir = json.getTempatlahir().trim();
					tgllahir = json.getTgllahir().trim();
					kodejeniskelamin = json.getKodejeniskelamin();
					kodeasalpenerimaan = json.getKodeasalpenerimaan().trim();
					tmtmasuk = json.getTmtmasuk().trim();
					tglsk = json.getTglsk().trim();
					nomorsk = json.getNomorsk().trim();
					kodejenissk = json.getKodejenissk();
					lokasikantor = json.getLokasikantor().trim();
					unitkerja = json.getUnitkerja().trim();
					jobtitle = json.getJobtitle().trim();
					pangkat = json.getPangkat().trim();
					grade = json.getGrade().trim();
					kodestatusjabatan = json.getKodestatusjabatan();
					kodestatuskaryawan = json.getKodestatuskaryawan();
					kodejenisikatandinas = json.getKodejenisikatandinas();
					pemberiikatandinas = json.getPemberiikatandinas().trim();
					institusiikatandinas = json.getInstitusiikatandinas().trim();
					lokasiikatandinas = json.getLokasiikatandinas().trim();
					tglberlakuikatandinas = json.getTglberlakuikatandinas().trim();
					tglberakhirikatandinas = json.getTglberakhirikatandinas().trim();
					catatanikatandinas = json.getCatatanikatandinas().trim();
					//lampiranpenugasan = json.path("lampiranpenugasan").asText().trim();
					//lampiranikatandinas = json.path("lampiranikatandinas").asText().trim();
					row_status = 1;
					useract = authUser.getUserid();
					email = json.getEmail().trim();
					
					try {
						if(!(filePenugasanInputStream==null || filePenugasanDetail==null) && filePenugasan.length() > 0) {
							String pathFile = "file_penugasan/";
							String host = context.getInitParameter("ftp-host");
							Integer port = Integer.parseInt(context.getInitParameter("ftp-port"));
							String user = context.getInitParameter("ftp-user");
							String pass = context.getInitParameter("ftp-pass");
							
							String namaFile = filePenugasanDetail.getFileName();
				        	StringTokenizer st = new StringTokenizer(namaFile, ".");
				        	String extension = ""; 
				        	while(st.hasMoreTokens()) {
				        		extension = "."+st.nextToken();
				        	}
				        	Timestamp timestamp = new Timestamp(System.currentTimeMillis());
				        	lampiranpenugasan = namaFile = npp + "-" + timestamp.getTime() + extension;
				        	
				        	FTPClient ftpClient = null;
				        	try {
								ftpClient = new FTPClient();
								ftpClient.connect(host,port);
					        	ftpClient.login(user, pass);
					        	ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
					        	Boolean upload = ftpClient.storeFile(pathFile + namaFile, filePenugasanInputStream);
					        	if(!upload) {
					        		
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
						
						if(!(fileIkatanDinasInputStream==null || fileIkatanDinasDetail==null) && fileIkatanDinas.length() > 0) {
							String pathFile = "file_infoikatandinas/";
							String host = context.getInitParameter("ftp-host");
							Integer port = Integer.parseInt(context.getInitParameter("ftp-port"));
							String user = context.getInitParameter("ftp-user");
							String pass = context.getInitParameter("ftp-pass");
							
							String namaFile = fileIkatanDinasDetail.getFileName();
				        	StringTokenizer st = new StringTokenizer(namaFile, ".");
				        	String extension = ""; 
				        	while(st.hasMoreTokens()) {
				        		extension = "."+st.nextToken();
				        	}
				        	Timestamp timestamp = new Timestamp(System.currentTimeMillis());
				        	lampiranikatandinas = namaFile = npp + "-" + timestamp.getTime() + extension;
				        	
				        	FTPClient ftpClient = null;
				        	try {
								ftpClient = new FTPClient();
								ftpClient.connect(host,port);
					        	ftpClient.login(user, pass);
					        	ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
					        	Boolean upload = ftpClient.storeFile(pathFile + namaFile, fileIkatanDinasInputStream);
					        	if(!upload) {
					        		
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
						
						queri = "exec karyawan.sp_insert_pegawai ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?";
						con = new Koneksi().getConnection();
						cs = con.prepareCall(queri);
						cs.setString(1, npp);
						cs.setString(2, namadepan);
						cs.setString(3, namatengah);
						cs.setString(4, namabelakang);
						cs.setString(5, tempatlahir);
						cs.setDate(6, Utils.StringDateToSqlDate(tgllahir));
						cs.setInt(7, kodejeniskelamin);
						cs.setString(8, kodeasalpenerimaan);
						cs.setDate(9, Utils.StringDateToSqlDate(tmtmasuk));
						cs.setDate(10, Utils.StringDateToSqlDate(tglsk));
						cs.setString(11, nomorsk);
						cs.setInt(12, kodejenissk);
						cs.setString(13, lokasikantor);
						cs.setString(14, unitkerja);
						cs.setString(15, jobtitle);
						cs.setString(16, pangkat);
						cs.setString(17, grade);
						cs.setInt(18, kodestatusjabatan);
						cs.setInt(19, kodestatuskaryawan);
						cs.setInt(20, kodejenisikatandinas);
						cs.setString(21, pemberiikatandinas);
						cs.setString(22, institusiikatandinas);
						cs.setString(23, lokasiikatandinas);
						cs.setDate(24, Utils.StringDateToSqlDate(tglberlakuikatandinas));
						cs.setDate(25, Utils.StringDateToSqlDate(tglberakhirikatandinas));
						cs.setString(26, catatanikatandinas);
						cs.setString(27, lampiranpenugasan);
						cs.setString(28, lampiranikatandinas);
						cs.setInt(29, row_status);
						cs.setInt(30, useract);
						cs.setString(31, email);
						cs.registerOutParameter(32, java.sql.Types.VARCHAR);
						cs.execute();
						npp_output = cs.getString(32);
						Pegawai pegawai = new Pegawai();
						pegawai.setNpp(npp_output);
						response.setData(pegawai);
						result.setResponse(response);
						metadata.setCode(1);
						metadata.setMessage("Ok.");
					} catch (Exception e) {
						metadata.setCode(0);
						metadata.setMessage(e.getMessage());
						e.printStackTrace();
					} finally {
						if (cs != null) {
							try {
								cs.close();
							} catch (SQLException e) {
								e.printStackTrace();
							}
						}
						if (con != null) {
							try {
								con.close();
							} catch (SQLException e) {
								e.printStackTrace();
							}
						}	
					}
					
					
					break;
				default:
					break;
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
	@Path("/addpenugasanfuture/{tambahan}")
	@Produces("application/json")
	@Consumes("application/json")
	public Response addPenugasanFuture(@Context HttpHeaders headers, @PathParam("tambahan") String tambahan, String data) {
		Connection con = null;
		CallableStatement cs = null;
		Respon<Pegawai> response = new Respon<Pegawai>();
		Result<Pegawai> result = new Result<Pegawai>();
		Metadata metadata = new Metadata();
		String queri = null;
		
		if (SharedMethod.ServiceAuth(headers, metadata)) {
			try {
				String npp = null;
				String namadepan = null;
				String namatengah = null;
				String namabelakang = null;
				String kodeasalpenerimaan = null;
				String tmtmasuk = null;
				String tempatlahir = null;
				String tgllahir = null;
				Integer kodejeniskelamin = null;
				Integer kodestatuskaryawan = null;
				Integer row_status = null;
				Integer created_by = null;
				String email = null;
				
				String tglsk = null;
				String nomorsk = null;
				Integer kodejenissk = null;
				String lokasikantor = null;
				String unitkerja = null;
				String jobtitle = null;
				String pangkat = null;
				String grade = null;
				Integer kodestatusjabatan = null;
				Integer kodejenisikatandinas = null;
				String pemberiikatandinas = null;
				String institusiikatandinas = null;
				String lokasiikatandinas = null;
				String tglberlakuikatandinas = null;
				String tglberakhirikatandinas = null;
				String catatanikatandinas = null;
				String lampiranpenugasan = null;
				String lampiranikatandinas = null;
				Integer useract = null;
				
				String npp_output = null;
				
				ObjectMapper mapper = new ObjectMapper();
				JsonNode json = mapper.readTree(data);
				
				switch (tambahan) {
				case "tanpapenugasan":
					npp = json.path("npp").asText().trim();
					namadepan = json.path("namadepan").asText().trim();
					namatengah = json.path("namatengah").asText().trim();
					namabelakang = json.path("namabelakang").asText().trim();
					kodeasalpenerimaan = json.path("kodeasalpenerimaan").asText().trim();
					tmtmasuk = json.path("tmtmasuk").asText().trim();
					tempatlahir = json.path("tempatlahir").asText().trim();
					tgllahir = json.path("tgllahir").asText().trim();
					kodejeniskelamin = json.path("kodejeniskelamin").asInt();
					kodestatuskaryawan = json.path("kodestatuskaryawan").asInt();
					row_status = json.path("row_status").asInt();
					created_by = json.path("created_by").asInt();
					email = json.path("email").asText().trim();
					
					try {
						queri = "exec karyawan.sp_insert_pegawai_excludepenugasan ?,?,?,?,?,?,?,?,?,?,?,?,?,?";
						con = new Koneksi().getConnection();
						cs = con.prepareCall(queri);
						cs.setString(1, npp);
						cs.setString(2, namadepan);
						cs.setString(3, namatengah);
						cs.setString(4, namabelakang);
						cs.setString(5, kodeasalpenerimaan);
						cs.setDate(6, Utils.StringDateToSqlDate(tmtmasuk));
						cs.setString(7, tempatlahir);
						cs.setDate(8, Utils.StringDateToSqlDate(tgllahir));
						cs.setInt(9, kodejeniskelamin);
						cs.setInt(10, kodestatuskaryawan);
						cs.setInt(11, row_status);
						cs.setInt(12, created_by);
						cs.setString(13, email);
						cs.registerOutParameter(14, java.sql.Types.VARCHAR);
						cs.execute();
						npp_output = cs.getString(14);
						Pegawai pegawai = new Pegawai();
						pegawai.setNpp(npp_output);
						response.setData(pegawai);
						result.setResponse(response);
						metadata.setCode(1);
						metadata.setMessage("Ok.");
					} catch (Exception e) {
						metadata.setCode(0);
						metadata.setMessage(e.getMessage());
						e.printStackTrace();
					} finally {
						if (cs != null) {
							try {
								cs.close();
							} catch (SQLException e) {
								e.printStackTrace();
							}
						}
						if (con != null) {
							try {
								con.close();
							} catch (SQLException e) {
								e.printStackTrace();
							}
						}	
					}
					break;
				case "denganpenugasan":
					npp = json.path("npp").asText().trim();
					namadepan = json.path("namadepan").asText().trim();
					namatengah = json.path("namatengah").asText().trim();
					namabelakang = json.path("namabelakang").asText().trim();
					tempatlahir = json.path("tempatlahir").asText().trim();
					tgllahir = json.path("tgllahir").asText().trim();
					kodejeniskelamin = json.path("kodejeniskelamin").asInt();
					kodeasalpenerimaan = json.path("kodeasalpenerimaan").asText().trim();
					tmtmasuk = json.path("tmtmasuk").asText().trim();
					tglsk = json.path("tglsk").asText().trim();
					nomorsk = json.path("nomorsk").asText().trim();
					kodejenissk = json.path("kodejenissk").asInt();
					lokasikantor = json.path("lokasikantor").asText().trim();
					unitkerja = json.path("unitkerja").asText().trim();
					jobtitle = json.path("jobtitle").asText().trim();
					pangkat = json.path("pangkat").asText().trim();
					grade = json.path("grade").asText().trim();
					kodestatusjabatan = json.path("kodestatusjabatan").asInt();
					kodestatuskaryawan = json.path("kodestatuskaryawan").asInt();
					kodejenisikatandinas = json.path("kodejenisikatandinas").asInt();
					pemberiikatandinas = json.path("pemberiikatandinas").asText().trim();
					institusiikatandinas = json.path("institusiikatandinas").asText().trim();
					lokasiikatandinas = json.path("lokasiikatandinas").asText().trim();
					tglberlakuikatandinas = json.path("tglberlakuikatandinas").asText().trim();
					tglberakhirikatandinas = json.path("tglberakhirikatandinas").asText().trim();
					catatanikatandinas = json.path("catatanikatandinas").asText().trim();
					lampiranpenugasan = json.path("lampiranpenugasan").asText().trim();
					lampiranikatandinas = json.path("lampiranikatandinas").asText().trim();
					row_status = json.path("row_status").asInt();
					useract = json.path("useract").asInt();
					email = json.path("email").asText().trim();
					
					try {
						queri = "exec karyawan.sp_insert_pegawai ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?";
						con = new Koneksi().getConnection();
						cs = con.prepareCall(queri);
						cs.setString(1, npp);
						cs.setString(2, namadepan);
						cs.setString(3, namatengah);
						cs.setString(4, namabelakang);
						cs.setString(5, tempatlahir);
						cs.setDate(6, Utils.StringDateToSqlDate(tgllahir));
						cs.setInt(7, kodejeniskelamin);
						cs.setString(8, kodeasalpenerimaan);
						cs.setDate(9, Utils.StringDateToSqlDate(tmtmasuk));
						cs.setDate(10, Utils.StringDateToSqlDate(tglsk));
						cs.setString(11, nomorsk);
						cs.setInt(12, kodejenissk);
						cs.setString(13, lokasikantor);
						cs.setString(14, unitkerja);
						cs.setString(15, jobtitle);
						cs.setString(16, pangkat);
						cs.setString(17, grade);
						cs.setInt(18, kodestatusjabatan);
						cs.setInt(19, kodestatuskaryawan);
						cs.setInt(20, kodejenisikatandinas);
						cs.setString(21, pemberiikatandinas);
						cs.setString(22, institusiikatandinas);
						cs.setString(23, lokasiikatandinas);
						cs.setDate(24, Utils.StringDateToSqlDate(tglberlakuikatandinas));
						cs.setDate(25, Utils.StringDateToSqlDate(tglberakhirikatandinas));
						cs.setString(26, catatanikatandinas);
						cs.setString(27, lampiranpenugasan);
						cs.setString(28, lampiranikatandinas);
						cs.setInt(29, row_status);
						cs.setInt(30, useract);
						cs.setString(31, email);
						cs.registerOutParameter(32, java.sql.Types.VARCHAR);
						cs.execute();
						npp_output = cs.getString(32);
						Pegawai pegawai = new Pegawai();
						pegawai.setNpp(npp_output);
						response.setData(pegawai);
						result.setResponse(response);
						metadata.setCode(1);
						metadata.setMessage("Ok.");
					} catch (Exception e) {
						metadata.setCode(0);
						metadata.setMessage(e.getMessage());
						e.printStackTrace();
					} finally {
						if (cs != null) {
							try {
								cs.close();
							} catch (SQLException e) {
								e.printStackTrace();
							}
						}
						if (con != null) {
							try {
								con.close();
							} catch (SQLException e) {
								e.printStackTrace();
							}
						}	
					}
					break;
				default:
					break;
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
}
