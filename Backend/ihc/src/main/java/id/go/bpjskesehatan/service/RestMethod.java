package id.go.bpjskesehatan.service;

import id.go.bpjskesehatan.database.Koneksi;
import id.go.bpjskesehatan.entitas.GenericEntitas;
import id.go.bpjskesehatan.entitas.Metadata;
import id.go.bpjskesehatan.entitas.Result2;
import id.go.bpjskesehatan.entitas.cuti.Tipe;
import id.go.bpjskesehatan.entitas.djp.Djp;
import id.go.bpjskesehatan.entitas.djp.Tanggungjawab;
import id.go.bpjskesehatan.entitas.hcis.GrupUser;
import id.go.bpjskesehatan.entitas.karyawan.Djpindividu;
import id.go.bpjskesehatan.entitas.karyawan.Pegawai;
import id.go.bpjskesehatan.entitas.karyawan.Penugasan;
import id.go.bpjskesehatan.entitas.kinerja.Gruppenilai;
import id.go.bpjskesehatan.entitas.kinerja.Komponen;
import id.go.bpjskesehatan.entitas.kinerja.Masterkriteriapenilaian;
import id.go.bpjskesehatan.entitas.kinerja.Masternilai;
import id.go.bpjskesehatan.entitas.kinerja.Penilai;
import id.go.bpjskesehatan.entitas.kinerja.Periodekinerja;
import id.go.bpjskesehatan.entitas.kinerja.Periodeubkomitmen;
import id.go.bpjskesehatan.entitas.kinerja.Periodeubkompetensi;
import id.go.bpjskesehatan.entitas.kinerja.Siklus;
import id.go.bpjskesehatan.entitas.kompetensi.Kamuskompetensi;
import id.go.bpjskesehatan.entitas.kompetensi.Kelompokkompetensi;
import id.go.bpjskesehatan.entitas.kompetensi.Klaskelompokkompetensi;
import id.go.bpjskesehatan.entitas.kompetensi.Kompetensi;
import id.go.bpjskesehatan.entitas.kompetensi.Levelkompetensi;
import id.go.bpjskesehatan.entitas.kompetensi.Modelkompetensijobtitle;
import id.go.bpjskesehatan.entitas.kompetensi.Periodekompetensi;
import id.go.bpjskesehatan.entitas.organisasi.OrganizationChart;
import id.go.bpjskesehatan.entitas.organisasi.UnitKerja;
import id.go.bpjskesehatan.entitas.organisasi.HirarkiUnitKerja;
import id.go.bpjskesehatan.entitas.organisasi.FunctionalScope;
import id.go.bpjskesehatan.entitas.organisasi.Grade;
import id.go.bpjskesehatan.entitas.organisasi.Office;
import id.go.bpjskesehatan.entitas.organisasi.OfficeType;
import id.go.bpjskesehatan.entitas.organisasi.Jabatan;
import id.go.bpjskesehatan.entitas.organisasi.JobPrefix;
import id.go.bpjskesehatan.entitas.organisasi.JobTitle;
import id.go.bpjskesehatan.entitas.referensi.Anggaran;
import id.go.bpjskesehatan.entitas.referensi.Dati2;
import id.go.bpjskesehatan.entitas.referensi.Jurusanpendidikan;
import id.go.bpjskesehatan.entitas.referensi.Kecamatan;
import id.go.bpjskesehatan.entitas.referensi.Propinsi;
import id.go.bpjskesehatan.skpd.Jeniskendaraan;
import id.go.bpjskesehatan.skpd.Skpd;
import id.go.bpjskesehatan.util.SharedMethod;
import id.go.bpjskesehatan.util.Utils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Bambang Purwanto
 *
 */
public class RestMethod {

	public static Response createData(@Context HttpHeaders headers, String data, Object entity, String namatabel) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Result2 result = null;
		result = new Result2();

		Metadata metadata = new Metadata();
		if (SharedMethod.ServiceAuth(headers, metadata)) {
			try {
				con = new Koneksi().getConnection();

				String query = null;
				query = SharedMethod.createQuery(entity, data, namatabel);
				ps = con.prepareStatement(query, new String[] { "kode" });
				// ps = con.prepareStatement(query,
				// Statement.RETURN_GENERATED_KEYS);
				SharedMethod.setParameterCreateQuery(ps, entity, data);
				// boolean hasil = false;
				ps.execute();
				rs = ps.getResultSet();
				if (rs == null)
					rs = ps.getGeneratedKeys();
				metadata.setMessage("Data berhasil disimpan");
				metadata.setCode(1);
				if (rs.next()) {
					metadata.setId(rs.getString(1));
				}

			} catch (SQLException e) {
				metadata.setCode(0);
				metadata.setMessage(SharedMethod.getErrorMessage(e));
				if(e.getErrorCode()==50000) {
					metadata.setMessage(e.getMessage());
				}
				// e.printStackTrace();
			} catch (NamingException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
				// e.printStackTrace();
			} catch (NumberFormatException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
				// e.printStackTrace();
			} catch (JsonProcessingException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
				// e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
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
		result.setMetadata(metadata);
		return Response.ok(result).build();
	}

	public static Response createData(@Context HttpHeaders headers, String data, String namatabel) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Result2 result = null;
		result = new Result2();

		Metadata metadata = new Metadata();
		if (SharedMethod.ServiceAuth(headers, metadata)) {
			try {
				con = new Koneksi().getConnection();
				String query;
				query = SharedMethod.createQuery(data, namatabel);

				ps = con.prepareStatement(query, new String[] { "kode" });
				SharedMethod.setParameterCreateQuery(ps, data);
				// boolean hasil = false;
				ps.execute();
				rs = ps.getResultSet();
				if (rs == null)
					rs = ps.getGeneratedKeys();

				metadata.setMessage("Data berhasil disimpan");
				metadata.setCode(1);
				if (rs.next())
					metadata.setId(rs.getString(1));

			} catch (SQLException e) {
				metadata.setCode(0);
				metadata.setMessage(SharedMethod.getErrorMessage(e));
				if(e.getErrorCode()==50000) {
					metadata.setMessage(e.getMessage());
				}
				else {
					e.printStackTrace();
				}
			} catch (NamingException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
				// e.printStackTrace();
			} catch (NumberFormatException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
				// e.printStackTrace();
			} catch (JsonProcessingException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
				// e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
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
		result.setMetadata(metadata);
		return Response.ok(result).build();

	}

	public static Response updateData(@Context HttpHeaders headers, String data, Object entity, String namatabel) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Result2 result = null;
		result = new Result2();

		Metadata metadata = new Metadata();
		if (SharedMethod.ServiceAuth(headers, metadata)) {
			try {
				con = new Koneksi().getConnection();
				String query;
				query = SharedMethod.updateQuery(entity, data, namatabel);
				ps = con.prepareStatement(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);

				SharedMethod.setParameterEditQuery(ps, entity, data);

				ps.executeUpdate();
				metadata.setCode(1);
				metadata.setMessage("Data berhasil diupdate");
			} catch (SQLException e) {
				metadata.setCode(0);
				metadata.setMessage("Update Gagal.");
				if(e.getErrorCode()==50000) {
					metadata.setMessage(e.getMessage());
				}
				else {
					e.printStackTrace();
				}
			} catch (NamingException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
			} catch (NumberFormatException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
			} catch (JsonProcessingException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
			} catch (IOException e) {
				// TODO Auto-generated catch block
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
		result.setMetadata(metadata);
		return Response.ok(result).build();
	}

	public static Response updateData(@Context HttpHeaders headers, String data, String namatabel) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Result2 result = null;
		result = new Result2();

		Metadata metadata = new Metadata();
		if (SharedMethod.ServiceAuth(headers, metadata)) {
			try {
				con = new Koneksi().getConnection();
				String query;
				query = SharedMethod.updateQuery(data, namatabel);
				ps = con.prepareStatement(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);

				SharedMethod.setParameterEditQuery(ps, data);

				ps.executeUpdate();
				metadata.setCode(1);
				metadata.setMessage("Data berhasil diupdate");
			} catch (SQLException e) {
				metadata.setCode(0);
				metadata.setMessage(SharedMethod.getErrorMessage(e));
				if(e.getErrorCode()==50000) {
					metadata.setMessage(e.getMessage());
				}
				else {
					e.printStackTrace();
				}
			} catch (NamingException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
			} catch (NumberFormatException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
			} catch (JsonProcessingException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
			} catch (IOException e) {
				// TODO Auto-generated catch block
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
		result.setMetadata(metadata);
		return Response.ok(result).build();
	}

	public static Response deleteData(HttpHeaders headers, String data, Object entity, String namaTabel) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Result2 result = null;
		result = new Result2();

		Metadata metadata = new Metadata();
		if (SharedMethod.ServiceAuth(headers, metadata)) {
			try {
				con = new Koneksi().getConnection();
				String query;
				query = SharedMethod.deleteQuery(entity, data, namaTabel);

				ps = con.prepareStatement(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);

				SharedMethod.setParameterDeleteQuery(ps, entity, data);

				ps.executeUpdate();
				metadata.setCode(1);
				metadata.setMessage("Data berhasil dihapus");

			} catch (SQLException e) {
				metadata.setCode(0);
				metadata.setMessage(SharedMethod.getErrorMessage(e));
				if(e.getErrorCode()==50000) {
					metadata.setMessage(e.getMessage());
				}
				else {
					e.printStackTrace();
				}
			} catch (NamingException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
			} catch (NumberFormatException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
			} catch (JsonProcessingException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
			} catch (IOException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
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
		result.setMetadata(metadata);
		return Response.ok(result).build();
	}

	public static Response deleteData(HttpHeaders headers, String data, String namaTabel) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Result2 result = null;
		result = new Result2();

		Metadata metadata = new Metadata();
		if (SharedMethod.ServiceAuth(headers, metadata)) {
			try {
				con = new Koneksi().getConnection();
				String query;
				query = SharedMethod.deleteQuery(data, namaTabel);

				ps = con.prepareStatement(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);

				SharedMethod.setParameterDeleteQuery(ps, data);

				ps.executeUpdate();
				metadata.setCode(1);
				metadata.setMessage("Data berhasil dihapus");

			} catch (SQLException e) {
				metadata.setCode(0);
				metadata.setMessage(SharedMethod.getErrorMessage(e));
				if(e.getErrorCode()==50000) {
					metadata.setMessage(e.getMessage());
				}
				else {
					e.printStackTrace();
				}
			} catch (NamingException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
			} catch (NumberFormatException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
			} catch (JsonProcessingException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
			} catch (IOException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
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
		result.setMetadata(metadata);
		return Response.ok(result).build();
	}

	public static Response getListData(@Context HttpHeaders headers, @PathParam("page") String page,
			@PathParam("row") String row, Object entity, String query) {
		return getListData(headers, page, row, null, entity, query);
	}

	public static Response getListData(@Context HttpHeaders headers, @PathParam("page") String page,
			@PathParam("row") String row, String namaentity, Object entity, String query) {
		return getListData(headers, page, row, namaentity, entity, query, null);
	}

	public static Response getListData(@Context HttpHeaders headers, @PathParam("page") String page,
			@PathParam("row") String row, Object entity, String query, String data) {
		return getListData(headers, page, row, null, entity, query, data);
	}

	@SuppressWarnings("deprecation")
	public static Response getListData(@Context HttpHeaders headers, @PathParam("page") String page,
			@PathParam("row") String row, String namaentity, Object entity, String query, String data) {

		Map<String, Object> response = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		List<Object> listdata = null;
		Map<String, Object> metadata = new HashMap<String, Object>();
		Connection con = null;
		ResultSet rs = null;
		CallableStatement cs = null;
		String order = null;
		String filter = null;
		boolean auth = false;

		if (SharedMethod.ServiceAuth(headers, metadata)) {
			auth = true;
		}

		if (!auth && SharedMethod.ServiceToken(headers, metadata)) {
			auth = true;
		}

		if (auth) {
			try {
				if (data != null) {
					if (!data.isEmpty()) {
						ObjectMapper mapper = new ObjectMapper();
						JsonNode json = mapper.readTree(data);

						order = json.path("sort").isMissingNode() ? null
								: SharedMethod.getSortedColumn(mapper.writeValueAsString(json.path("sort")));

						filter = json.path("filter").isMissingNode() ? null
								: SharedMethod.getFilteredColumn(mapper.writeValueAsString(json.path("filter")), null);
					}
				}

				con = new Koneksi().getConnection();
				cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
				cs.setInt(1, Integer.parseInt(page));
				cs.setInt(2, Integer.parseInt(row));
				cs.setInt(3, 1);
				cs.setString(4, order);
				cs.setString(5, filter);
				rs = cs.executeQuery();
				metadata.put("code", 2);
				metadata.put("message", Response.Status.NO_CONTENT.toString());
				metadata.put("rowcount", 0);

				if (rs.next()) {
					metadata.put("rowcount", rs.getInt("jumlah"));
				}

				listdata = new ArrayList<Object>();

				rs.close();
				cs.setInt(1, Integer.parseInt(page));
				cs.setInt(2, Integer.parseInt(row));
				cs.setInt(3, 0);
				cs.setString(4, order);
				cs.setString(5, filter);
				rs = cs.executeQuery();

				if (namaentity == null) {
					namaentity = entity.getClass().getSimpleName().toLowerCase();
				}

				while (rs.next()) {
					entity = entity.getClass().newInstance();
					entity = SharedMethod.setAllSetters(rs, entity);
					switch (namaentity) {
					case "user":
					case "grupusermenu":
						setReferensiUser(entity, rs, namaentity);
						break;
					case "jobprefixpangkat":
					case "jobtitle":
					case "subgrade":
					case "office":
					case "unitkerja":
					case "jabatan":
					case "organizationchartunitkerja":
						setReferensiOrganisasi(entity, rs, namaentity);
						break;
					case "pegawai":
					case "detailalamat":
					case "detailalamat2":
					case "infodarurat":
					case "penugasan":
					case "pegawaijabatan":
					case "infoidentitas":
					case "infofisik":
					case "infokeluarga":
					case "infopendidikan":
					case "infoasuransi":
					case "infobaranginventaris":
					case "infoassesment":
					case "infopelatihan":
					case "infosertifikasi":
					case "infopengalamanproyek":
					case "infopenghargaan":
					case "infoikatandinas":
					case "djpindividu":
					case "tanggungjawabindividu":
					case "detailpengalamankerjaindividu":
					case "detailpendidikanformalindividu":
					case "detailpendidikannonformalindividu":
					case "detailkpiindividu":
					case "detaildimensijabatanindividu":
						setReferensiKaryawan(entity, rs, namaentity);
						break;
					case "dati2":
					case "kecamatan":
					case "kelurahan":
					case "subjurusanpendidikan":
					case "anggaran":
						setReferensi(entity, rs, namaentity);
						break;
					case "kelompokkompetensi":
					case "kompetensi":
					case "levelkompetensi":
					case "kamuskompetensi":
					case "modelkompetensijobtitle":
					case "listmodelkompetensijobtitle":
					case "detailmodelkompetensijobtitle":
						setReferensiKompetensi(entity, rs, namaentity);
						break;
					case "djp":
					case "tanggungjawab":
					case "detailpengalamankerja":
					case "detailpendidikanformal":
					case "detailpendidikannonformal":
					case "detailkpi":
					case "detaildimensijabatan":
						setReferensiDjp(entity, rs, namaentity);
						break;
					case "periodekinerjadetail":
					case "bobotkomponen":
					case "gruppenilaikomposisi":
					case "gruppenilaijobtitle":
					case "kinerja":
					case "interpretasi":
					case "kriteria":
					case "peserta":
					case "masterkomunikasi":
					case "masterkriteriapenilaian":
					case "masternilai":
					case "masternilaidetail":
					case "periodeubkomitmen":
					case "periodeubkompetensi":
					case "ubkomitmenkomunikasi":
					case "ubkomitmenkriteriapenilaian":
					case "ubkomitmennilai":
					case "ubkompetensikomunikasi":
					case "ubkompetensikriteriapenilaian":
						setReferensiKinerja(entity, rs, namaentity);
						break;
					case "skpd":
					case "detailskpd":
						setReferensiSkpd(entity, rs, namaentity);
						break;
					case "cuti":
						setReferensiCuti(entity, rs, namaentity);
						break;
					}

					listdata.add(entity);
					metadata.put("code", 1);
					metadata.put("message", "OK");
				}
				response.put("list", listdata);
				result.put("response", response);
				rs.close();
			} catch (SQLException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				// e.printStackTrace();
			} catch (NamingException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				// e.printStackTrace();
			} catch (IllegalAccessException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				// e.printStackTrace();
			} catch (InvocationTargetException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				// e.printStackTrace();
			} catch (SecurityException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				// e.printStackTrace();
			} catch (InstantiationException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				// e.printStackTrace();
			} catch (ClassNotFoundException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				// e.printStackTrace();
			} catch (Exception e) {
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
			result.put("metadata", metadata);
			return Response.ok(result).build();
		} else {
			result.put("metadata", metadata);
			return Response.status(Status.UNAUTHORIZED).entity(result).build();
		}
	}

	public static Response getListDataUsingMap(@Context HttpHeaders headers, @PathParam("page") String page,
			@PathParam("row") String row, String query, String data) {
		Map<String, Object> response = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> listdata = null;
		Map<String, Object> metadata = new HashMap<String, Object>();
		Connection con = null;
		ResultSet rs = null;
		CallableStatement cs = null;
		String order = null;
		String filter = null;

		if (SharedMethod.ServiceAuth(headers, metadata)) {
			try {
				if (data != null) {
					if (!data.isEmpty()) {
						ObjectMapper mapper = new ObjectMapper();
						JsonNode json = mapper.readTree(data);

						order = json.path("sort").isMissingNode() ? null
								: SharedMethod.getSortedColumn(mapper.writeValueAsString(json.path("sort")));

						filter = json.path("filter").isMissingNode() ? null
								: SharedMethod.getFilteredColumn(mapper.writeValueAsString(json.path("filter")), null);

					}
				}

				con = new Koneksi().getConnection();
				cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
				cs.setInt(1, Integer.parseInt(page));
				cs.setInt(2, Integer.parseInt(row));
				cs.setInt(3, 1);
				cs.setString(4, order);
				cs.setString(5, filter);
				rs = cs.executeQuery();
				metadata.put("code", 2);
				metadata.put("message", Response.Status.NO_CONTENT.toString());
				metadata.put("rowcount", 0);

				if (rs.next()) {
					metadata.put("rowcount", rs.getInt("jumlah"));
				}

				listdata = new ArrayList<Map<String, Object>>();

				rs.close();
				cs.setInt(1, Integer.parseInt(page));
				cs.setInt(2, Integer.parseInt(row));
				cs.setInt(3, 0);
				cs.setString(4, order);
				cs.setString(5, filter);
				rs = cs.executeQuery();
				ResultSetMetaData metaData = rs.getMetaData();
				Map<String, Object> hasil = null;
				while (rs.next()) {
					hasil = new HashMap<String, Object>();
					for (int i = 1; i <= metaData.getColumnCount(); i++) {
						if (rs.getObject(i) != null){
							if(metaData.getColumnTypeName(i).equalsIgnoreCase("date")){
								hasil.put(metaData.getColumnName(i).toLowerCase(), Utils.SqlDateToSqlString(rs.getDate(i)));
							}
							else {
								hasil.put(metaData.getColumnName(i).toLowerCase(), rs.getObject(i));
							}
						}
					}
					listdata.add(hasil);
					metadata.put("code", 1);
					metadata.put("message", "OK");
				}
				response.put("listdata", listdata);
				result.put("response", response);
				rs.close();

			} catch (SQLException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				// e.printStackTrace();
			} catch (NamingException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				// e.printStackTrace();
			} catch (IllegalAccessException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				// e.printStackTrace();
			} catch (InvocationTargetException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				// e.printStackTrace();
			} catch (SecurityException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				// e.printStackTrace();
			} catch (InstantiationException e) {
				metadata.put("code", 0);
				metadata.put("message", e.getMessage());
				// e.printStackTrace();
			} catch (Exception e) {
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

	private static void setReferensi(Object entity, ResultSet rs, String namaentity) throws SQLException {
		switch (namaentity) {
		case "dati2":
			Propinsi propinsi = new Propinsi();
			propinsi.setKode(rs.getString("kodepropinsi"));
			propinsi.setNama(rs.getString("namapropinsi"));
			SharedMethod.Setter(entity, "setPropinsi", propinsi);
			break;
		case "kecamatan":
			Dati2 dati2 = new Dati2();
			dati2.setKode(rs.getString("kodedati2"));
			dati2.setNama(rs.getString("namadati2"));
			propinsi = new Propinsi();
			propinsi.setKode(rs.getString("kodepropinsi"));
			propinsi.setNama(rs.getString("namapropinsi"));
			SharedMethod.Setter(dati2, "setPropinsi", propinsi);
			SharedMethod.Setter(entity, "setDati2", dati2);
			break;
		case "kelurahan":
			Kecamatan kecamatan = new Kecamatan();
			kecamatan.setKode(rs.getString("kodekecamatan"));
			kecamatan.setNama(rs.getString("namakecamatan"));
			dati2 = new Dati2();
			dati2.setKode(rs.getString("kodedati2"));
			dati2.setNama(rs.getString("namadati2"));
			propinsi = new Propinsi();
			propinsi.setKode(rs.getString("kodepropinsi"));
			propinsi.setNama(rs.getString("namapropinsi"));
			SharedMethod.Setter(dati2, "setPropinsi", propinsi);
			SharedMethod.Setter(kecamatan, "setDati2", dati2);
			SharedMethod.Setter(entity, "setKecamatan", kecamatan);
			break;
		case "subjurusanpendidikan":
			Jurusanpendidikan jurusan = new Jurusanpendidikan();
			jurusan.setKode(rs.getInt("kodejurusanpendidikan"));
			jurusan.setNama(rs.getString("namajurusanpendidikan"));
			SharedMethod.Setter(entity, "setJurusanpendidikan", jurusan);
			break;
		case "anggaran":
			GenericEntitas programkegiatan = new GenericEntitas();
			programkegiatan.setKode(rs.getString("kodeprogramkegiatan"));
			programkegiatan.setNama(rs.getString("namaprogramkegiatan"));
			SharedMethod.Setter(entity, "setProgramkegiatan", programkegiatan);
			GenericEntitas akun = new GenericEntitas();
			akun.setKode(rs.getString("kodeakun"));
			akun.setNama(rs.getString("namaakun"));
			SharedMethod.Setter(entity, "setAkun", akun);
			break;
		case "notifikasi":
			// Pegawai pegawai = new Pegawai();
			// pegawai.setNpp(rs.getString("tonpp"));
			// pegawai.setNama(rs.getString("nama"));
			// Jabatan jabatan = new Jabatan();
			// jabatan.setKode(rs.getString("tokodejabatan"));
			// jabatan.setNama(rs.getString("namajabatan"));
			// SharedMethod.Setter(entity, "setPegawai", pegawai);
			// SharedMethod.Setter(entity, "setJabatan", jabatan);
			break;
		}

	}

	private static void setReferensiSkpd(Object entity, ResultSet rs, String namaentity) throws SQLException {
		switch (namaentity) {
		case "skpd":
			Jabatan jabatan = new Jabatan();
			jabatan.setKode(rs.getString("kodejabatanpemberisetuju"));
			jabatan.setNama(rs.getString("namajabatanpemberisetuju"));
			SharedMethod.Setter(entity, "setJabatanpemberisetuju", jabatan);
			Jeniskendaraan jeniskendaraan = new Jeniskendaraan();
			jeniskendaraan.setKode(rs.getInt(rs.getInt("kodejeniskendaraan")));
			jeniskendaraan.setNama(rs.getString("namajeniskendaraan"));
			SharedMethod.Setter(entity, "setJeniskendaraan", jeniskendaraan);
			Dati2 dati2 = new Dati2();
			dati2.setKode(rs.getString("kodeasal"));
			dati2.setNama(rs.getString("namaasal"));
			SharedMethod.Setter(entity, "setAsal", dati2);
			dati2 = new Dati2();
			dati2.setKode(rs.getString("kodetujuan"));
			dati2.setNama(rs.getString("namatujuan"));
			SharedMethod.Setter(entity, "setTujuan", dati2);
			Anggaran anggaran = new Anggaran();
			anggaran.setKode(rs.getInt("kodeanggaran"));
			GenericEntitas programkegiatan = new GenericEntitas();
			programkegiatan.setKode(rs.getString("kodeprogramkegiatan"));
			programkegiatan.setNama(rs.getString("namaprogramkegiatan"));
			GenericEntitas akun = new GenericEntitas();
			akun.setKode(rs.getString("kodeakun"));
			akun.setNama(rs.getString("namaakun"));
			SharedMethod.Setter(anggaran, "setProgramkegiatan", programkegiatan);
			SharedMethod.Setter(anggaran, "setAkun", akun);
			SharedMethod.Setter(entity, "setAnggaran", anggaran);
			break;
		case "detailskpd":
			Pegawai pegawai = new Pegawai();
			pegawai.setNpp(rs.getString("npp"));
			pegawai.setNama(rs.getString("nama"));
			SharedMethod.Setter(entity, "setPegawai", pegawai);
			jabatan = new Jabatan();
			jabatan.setKode(rs.getString("kodejabatan"));
			jabatan.setNama(rs.getString("namajabatan"));
			SharedMethod.Setter(entity, "setJabatan", jabatan);
			Skpd skpd = new Skpd();
			skpd.setKode(rs.getInt("kodeskpd"));
			SharedMethod.Setter(entity, "setSkpd", skpd);
			break;
		}

	}

	private static void setReferensiKinerja(Object entity, ResultSet rs, String namaentity) throws SQLException {
		switch (namaentity) {
		case "periodekinerjadetail":
			Periodekinerja periodekinerja = new Periodekinerja();
			periodekinerja.setKode(rs.getInt("kodeperiodekinerja"));
			periodekinerja.setNama(rs.getString("namaperiodekinerja"));
			SharedMethod.Setter(entity, "setPeriodekinerja", periodekinerja);
			Siklus siklus = new Siklus();
			siklus.setKode(rs.getInt("kodesiklus"));
			siklus.setNama(rs.getString("namasiklus"));
			SharedMethod.Setter(entity, "setSiklus", siklus);
			break;
		case "bobotkomponen":
			periodekinerja = new Periodekinerja();
			periodekinerja.setKode(rs.getInt("kodeperiodekinerja"));
			periodekinerja.setNama(rs.getString("namaperiodekinerja"));
			SharedMethod.Setter(entity, "setPeriodekinerja", periodekinerja);
			Komponen komponen = new Komponen();
			komponen.setKode(rs.getInt("kodekomponen"));
			komponen.setNama(rs.getString("namakomponen"));
			SharedMethod.Setter(entity, "setKomponen", komponen);
			break;
		case "gruppenilaikomposisi":
			Gruppenilai gruppenilai = new Gruppenilai();
			gruppenilai.setKode(rs.getInt("kodegruppenilai"));
			gruppenilai.setNama(rs.getString("namagruppenilai"));
			Penilai penilai = new Penilai();
			penilai.setKode(rs.getInt("kodepenilai"));
			penilai.setNama(rs.getString("namapenilai"));
			SharedMethod.Setter(entity, "setGruppenilai", gruppenilai);
			SharedMethod.Setter(entity, "setPenilai", penilai);
			break;
		case "gruppenilaijobtitle":
			gruppenilai = new Gruppenilai();
			gruppenilai.setKode(rs.getInt("kodegruppenilai"));
			gruppenilai.setNama(rs.getString("namagruppenilai"));
			JobTitle jobtitle = new JobTitle();
			jobtitle.setKode(rs.getString("kodejobtitle"));
			jobtitle.setNama(rs.getString("namajobtitle"));
			SharedMethod.Setter(entity, "setGruppenilai", gruppenilai);
			SharedMethod.Setter(entity, "setJobtitle", jobtitle);
			break;
		case "kinerja":
			periodekinerja = new Periodekinerja();
			periodekinerja.setKode(rs.getInt("kodeperiodekinerja"));
			periodekinerja.setNama(rs.getString("namaperiodekinerja"));
			SharedMethod.Setter(entity, "setPeriodekinerja", periodekinerja);
			komponen = new Komponen();
			komponen.setKode(rs.getInt("kodekomponen"));
			komponen.setNama(rs.getString("namakomponen"));
			SharedMethod.Setter(entity, "setKomponen", komponen);
			break;
		case "interpretasi":
			periodekinerja = new Periodekinerja();
			periodekinerja.setKode(rs.getInt("kodeperiodekinerja"));
			periodekinerja.setNama(rs.getString("namaperiodekinerja"));
			SharedMethod.Setter(entity, "setPeriodekinerja", periodekinerja);
			break;
		case "kriteria":
			periodekinerja = new Periodekinerja();
			periodekinerja.setKode(rs.getInt("kodeperiodekinerja"));
			periodekinerja.setNama(rs.getString("namaperiodekinerja"));
			SharedMethod.Setter(entity, "setPeriodekinerja", periodekinerja);
			komponen = new Komponen();
			komponen.setKode(rs.getInt("kodekomponen"));
			komponen.setNama(rs.getString("namakomponen"));
			SharedMethod.Setter(entity, "setKomponen", komponen);
			break;
		case "peserta":
			periodekinerja = new Periodekinerja();
			periodekinerja.setKode(rs.getInt("kodeperiodekinerja"));
			periodekinerja.setNama(rs.getString("namaperiodekinerja"));
			SharedMethod.Setter(entity, "setPeriodekinerja", periodekinerja);
			Penugasan penugasan = new Penugasan();
			penugasan.setKode(rs.getInt("kodepenugasan"));
			Pegawai pegawai = new Pegawai();
			pegawai.setNpp(rs.getString("npppegawai"));
			pegawai.setNama(rs.getString("namapegawai"));
			penugasan.setPegawai(pegawai);
			Jabatan jabatan = new Jabatan();
			jabatan.setKode(rs.getString("kodejabatan"));
			jabatan.setNama(rs.getString("namajabatan"));
			JobTitle jt = new JobTitle();
			jt.setKode(rs.getString("kodejobtitle"));
			jt.setNama(rs.getString("namajabatan"));
			jabatan.setJobtitle(jt);
			UnitKerja unitkerja = new UnitKerja();
			unitkerja.setKode(rs.getString("kodeunitkerja"));
			unitkerja.setNama(rs.getString("namaunitkerja"));
			Office office = new Office();
			office.setKode(rs.getString("kodeoffice"));
			office.setNama(rs.getString("namaoffice"));
			unitkerja.setOffice(office);
			jabatan.setUnitkerja(unitkerja);
			penugasan.setJabatan(jabatan);
			penugasan.setIsmutation(rs.getInt("ismutation"));
			penugasan.setTatjabatan(rs.getDate("tatjabatan"));
			SharedMethod.Setter(entity, "setPenugasan", penugasan);
			break;
		case "masternilaidetail":
			Masternilai masternilai = new Masternilai();
			masternilai.setKode(rs.getInt("kodemasternilai"));
			masternilai.setNama(rs.getString("namamasternilai"));
			SharedMethod.Setter(entity, "setMasternilai", masternilai);
			break;
		case "periodeubkomitmen":
			periodekinerja = new Periodekinerja();
			periodekinerja.setKode(rs.getInt("kodeperiodekinerja"));
			periodekinerja.setNama(rs.getString("namaperiodekinerja"));
			SharedMethod.Setter(entity, "setPeriodekinerja", periodekinerja);
			break;
		case "periodeubkompetensi":
			periodekinerja = new Periodekinerja();
			periodekinerja.setKode(rs.getInt("kodeperiodekinerja"));
			periodekinerja.setNama(rs.getString("namaperiodekinerja"));
			SharedMethod.Setter(entity, "setPeriodekinerja", periodekinerja);
			break;
		case "ubkomitmenkomunikasi":
			Periodeubkomitmen periodeubkomitmen = new Periodeubkomitmen();
			periodeubkomitmen.setKode(rs.getInt("kodeperiodeubkomitmen"));
			periodeubkomitmen.setNama(rs.getString("namaperiodeubkomitmen"));
			SharedMethod.Setter(entity, "setPeriodeubkomitmen", periodeubkomitmen);
			// Masterkomunikasi masterkomunikasi = new Masterkomunikasi();
			// masterkomunikasi.setKode(rs.getInt("kodemasterkomunikasi"));
			// masterkomunikasi.setNama(rs.getString("namamasterkomunikasi"));
			// SharedMethod.Setter(entity, "setMasterkomunikasi",
			// masterkomunikasi);
			break;
		case "ubkomitmenkriteriapenilaian":
			periodeubkomitmen = new Periodeubkomitmen();
			periodeubkomitmen.setKode(rs.getInt("kodeperiodeubkomitmen"));
			periodeubkomitmen.setNama(rs.getString("namaperiodeubkomitmen"));
			SharedMethod.Setter(entity, "setPeriodeubkomitmen", periodeubkomitmen);
			Masterkriteriapenilaian masterkriteriapenilaian = new Masterkriteriapenilaian();
			masterkriteriapenilaian.setKode(rs.getInt("kodemasterkriteriapenilaian"));
			masterkriteriapenilaian.setNama(rs.getString("namamasterkriteriapenilaian"));
			masterkriteriapenilaian.setDeskripsi(rs.getString("deskripsi"));
			masterkriteriapenilaian.setSkor(rs.getInt("skor"));
			SharedMethod.Setter(entity, "setMasterkriteriapenilaian", masterkriteriapenilaian);
			break;
		case "ubkomitmennilai":
			periodeubkomitmen = new Periodeubkomitmen();
			periodeubkomitmen.setKode(rs.getInt("kodeperiodeubkomitmen"));
			periodeubkomitmen.setNama(rs.getString("namaperiodeubkomitmen"));
			SharedMethod.Setter(entity, "setPeriodeubkomitmen", periodeubkomitmen);
			masternilai = new Masternilai();
			masternilai.setKode(rs.getInt("kodemasternilai"));
			masternilai.setNama(rs.getString("namamasternilai"));
			masternilai.setDeskripsi(rs.getString("deskripsi"));
			SharedMethod.Setter(entity, "setMasternilai", masternilai);
			break;
		case "ubkompetensikomunikasi":
			Periodeubkompetensi periodeubkompetensi = new Periodeubkompetensi();
			periodeubkompetensi.setKode(rs.getInt("kodeperiodeubkompetensi"));
			periodeubkompetensi.setNama(rs.getString("namaperiodeubkompetensi"));
			SharedMethod.Setter(entity, "setPeriodeubkompetensi", periodeubkompetensi);
			// masterkomunikasi = new Masterkomunikasi();
			// masterkomunikasi.setKode(rs.getInt("kodemasterkomunikasi"));
			// masterkomunikasi.setNama(rs.getString("namamasterkomunikasi"));
			// SharedMethod.Setter(entity, "setMasterkomunikasi",
			// masterkomunikasi);
			break;
		case "ubkompetensikriteriapenilaian":
			periodeubkompetensi = new Periodeubkompetensi();
			periodeubkompetensi.setKode(rs.getInt("kodeperiodeubkompetensi"));
			periodeubkompetensi.setNama(rs.getString("namaperiodeubkompetensi"));
			SharedMethod.Setter(entity, "setPeriodeubkompetensi", periodeubkompetensi);
			masterkriteriapenilaian = new Masterkriteriapenilaian();
			masterkriteriapenilaian.setKode(rs.getInt("kodemasterkriteriapenilaian"));
			masterkriteriapenilaian.setNama(rs.getString("namamasterkriteriapenilaian"));
			masterkriteriapenilaian.setDeskripsi(rs.getString("deskripsi"));
			masterkriteriapenilaian.setSkor(rs.getInt("skor"));
			SharedMethod.Setter(entity, "setMasterkriteriapenilaian", masterkriteriapenilaian);
			break;
		}
	}

	private static void setReferensiOrganisasi(Object entity, ResultSet rs, String namaentity) throws SQLException {
		switch (namaentity) {
		case "jobprefixpangkat":
			JobPrefix jobprefix = new JobPrefix();
			Grade jobgrade = new Grade();
			jobprefix.setKode(rs.getString("kodejobprefix"));
			jobprefix.setNama(rs.getString("namajobprefix"));
			jobgrade.setKode(rs.getString("kodegrade"));
			jobgrade.setNama(rs.getString("namagrade"));

			SharedMethod.Setter(entity, "setJobprefix", jobprefix);
			SharedMethod.Setter(entity, "setGrade", jobgrade);
			break;
		case "jobtitle":
			jobprefix = new JobPrefix();
			FunctionalScope jobfunctionalscope = new FunctionalScope();
			jobprefix.setKode(rs.getString("kodejobprefix"));
			jobprefix.setNama(rs.getString("namajobprefix"));
			jobfunctionalscope.setKode(rs.getString("kodefunctionalscope"));
			jobfunctionalscope.setNama(rs.getString("namafunctionalscope"));

			SharedMethod.Setter(entity, "setJobprefix", jobprefix);
			SharedMethod.Setter(entity, "setFunctionalscope", jobfunctionalscope);
			break;
		case "subgrade":
			jobgrade = new Grade();
			jobgrade.setKode(rs.getString("kodegrade"));
			jobgrade.setNama(rs.getString("namagrade"));
			SharedMethod.Setter(entity, "setGrade", jobgrade);
			break;
		case "office":
			Office officeparent = new Office();
			OfficeType officetype = new OfficeType();
			Dati2 dati2 = new Dati2();
			Propinsi propinsi = new Propinsi();
			officetype.setKode(rs.getString("kodeofficetype"));
			officetype.setNama(rs.getString("namaofficetype"));

			officeparent.setKode(rs.getString("kodeparent"));
			officeparent.setNama(rs.getString("namaparent"));
			dati2.setKode(rs.getString("kodedati2"));
			dati2.setNama(rs.getString("namadati2"));
			propinsi.setKode(rs.getString("kodepropinsi"));
			propinsi.setNama(rs.getString("namapropinsi"));
			dati2.setPropinsi(propinsi);
			SharedMethod.Setter(entity, "setOfficetype", officetype);
			SharedMethod.Setter(entity, "setParent", officeparent);
			SharedMethod.Setter(entity, "setDati2", dati2);
			break;
		case "unitkerja":
			HirarkiUnitKerja hirarkiunitkerja = new HirarkiUnitKerja();
			UnitKerja unitkerjaparent = new UnitKerja();
			Office kantor = new Office();
			hirarkiunitkerja.setKode(rs.getString("kodehirarkiunitkerja"));
			hirarkiunitkerja.setNama(rs.getString("namahirarkiunitkerja"));
			unitkerjaparent.setKode(rs.getString("kodeparent"));
			unitkerjaparent.setNama(rs.getString("namaparent"));
			kantor.setKode(rs.getString("kodeoffice"));
			kantor.setNama(rs.getString("namaoffice"));
			OrganizationChart orgc = new OrganizationChart();
			orgc.setKode(rs.getString("kodeorganizationchart"));
			orgc.setNama(rs.getString("namaorganizationchart"));
			SharedMethod.Setter(entity, "setHirarkiunitkerja", hirarkiunitkerja);
			SharedMethod.Setter(entity, "setParent", unitkerjaparent);
			SharedMethod.Setter(entity, "setOffice", kantor);
			SharedMethod.Setter(entity, "setOrganizationchart", orgc);
			break;
		case "jabatan":
			UnitKerja unitkerja = new UnitKerja();
			JobTitle jobtitle = new JobTitle();
			Jabatan parent = new Jabatan();
			unitkerja.setKode(rs.getString("kodeunitkerja"));
			unitkerja.setNama(rs.getString("namaunitkerja"));
			jobtitle.setKode(rs.getString("kodejobtitle"));
			jobtitle.setNama(rs.getString("namajobtitle"));
			parent.setKode(rs.getString("kodeparent"));
			parent.setNama(rs.getString("namaparent"));
			SharedMethod.Setter(entity, "setUnitkerja", unitkerja);
			SharedMethod.Setter(entity, "setJobtitle", jobtitle);
			SharedMethod.Setter(entity, "setParent", parent);
			break;
		case "organizationchartunitkerja":
			unitkerja = new UnitKerja();
			OrganizationChart oc = new OrganizationChart();
			unitkerja.setKode(rs.getString("kodeunitkerja"));
			unitkerja.setNama(rs.getString("namaunitkerja"));
			oc.setKode(rs.getString("kodeorganizationchart"));
			oc.setNama(rs.getString("namaorganizationchart"));

			SharedMethod.Setter(entity, "setUnitkerja", unitkerja);
			SharedMethod.Setter(entity, "setOrganizationchart", oc);
			break;
		}

	}

	private static void setReferensiDjp(Object entity, ResultSet rs, String namaentity) throws SQLException {
		switch (namaentity) {
		case "djp":
			// Modelkompetensijobtitle data = new Modelkompetensijobtitle();
			// data.setKode(rs.getInt("kodemodelkompetensijobtitle"));

			// Kamuskompetensi kamuskompetensi = new Kamuskompetensi();
			// kamuskompetensi.setKode(rs.getInt("kodekamuskompetensi"));
			//
			// Kompetensi kompetensi = new Kompetensi();
			// kompetensi.setKode(rs.getString("kodekompetensi"));
			// kompetensi.setNama(rs.getString("namakompetensi"));
			// Levelkompetensi levelkompetensi = new Levelkompetensi();
			// levelkompetensi.setKode(rs.getInt("kodelevelkompetensi"));
			// levelkompetensi.setLevel(rs.getShort("levellevelkompetensi"));
			// levelkompetensi.setDeskripsi(rs.getString("deskripsilevelkompetensi"));
			//
			// SharedMethod.Setter(levelkompetensi, "setKompetensi",
			// kompetensi);
			// SharedMethod.Setter(kamuskompetensi, "setLevelkompetensi",
			// levelkompetensi);
			// SharedMethod.Setter(data, "setKamuskompetensi", kamuskompetensi);

			JobTitle jt = new JobTitle();
			jt.setKode(rs.getString("kodejobtitle"));
			jt.setNama(rs.getString("namajobtitle"));
			JobPrefix jp = new JobPrefix();
			jp.setKode(rs.getString("kodejobprefix"));
			jp.setNama(rs.getString("namajobprefix"));
			jt.setJobprefix(jp);
			SharedMethod.Setter(entity, "setJobtitle", jt);
			break;
		case "tanggungjawab":
		case "detailpendidikannonformal":
			Djp djp = new Djp();
			djp.setKode(rs.getInt("kodedjp"));
			SharedMethod.Setter(entity, "setDjp", djp);
			break;
		case "detailpendidikanformal":
			djp = new Djp();
			djp.setKode(rs.getInt("kodedjp"));
			SharedMethod.Setter(entity, "setDjp", djp);
			GenericEntitas pendidikan = new GenericEntitas();
			GenericEntitas jurusanpendidikan = new GenericEntitas();
			pendidikan.setKode(rs.getString("kodependidikan"));
			pendidikan.setNama(rs.getString("namapendidikan"));
			jurusanpendidikan.setKode(rs.getString("kodejurusanpendidikan"));
			jurusanpendidikan.setNama(rs.getString("namajurusanpendidikan"));
			SharedMethod.Setter(entity, "setPendidikan", pendidikan);
			SharedMethod.Setter(entity, "setJurusanpendidikan", jurusanpendidikan);
			break;
		case "detailpengalamankerja":
			djp = new Djp();
			djp.setKode(rs.getInt("kodedjp"));
			SharedMethod.Setter(entity, "setDjp", djp);
			GenericEntitas ref = new GenericEntitas();
			ref.setKode(rs.getString("kodepengalamankerja"));
			ref.setNama(rs.getString("namapengalamankerja"));
			SharedMethod.Setter(entity, "setPengalamankerja", ref);
			break;
		case "detailkpi":
		case "detaildimensijabatan":
			Tanggungjawab tanggungjawab = new Tanggungjawab();
			tanggungjawab.setKode(rs.getInt("kodetanggungjawab"));
			SharedMethod.Setter(entity, "setTanggungjawab", tanggungjawab);
			break;
		}
	}

	private static void setReferensiKompetensi(Object entity, ResultSet rs, String namaentity) throws SQLException {
		switch (namaentity) {
		case "kelompokkompetensi":
			Klaskelompokkompetensi data = new Klaskelompokkompetensi();
			data.setKode(rs.getInt("kodeklaskelompokkompetensi"));
			data.setNama(rs.getString("namaklaskelompokkompetensi"));
			SharedMethod.Setter(entity, "setKlaskelompokkompetensi", data);
			break;
		case "kompetensi":
			Kelompokkompetensi kelompokkompetensi = new Kelompokkompetensi();
			kelompokkompetensi.setKode(rs.getInt("kodekelompokkompetensi"));
			kelompokkompetensi.setNama(rs.getString("namakelompokkompetensi"));
			SharedMethod.Setter(entity, "setKelompokkompetensi", kelompokkompetensi);
			break;
		case "levelkompetensi":
			Kompetensi kompetensi = new Kompetensi();
			kompetensi.setKode(rs.getString("kodekompetensi"));
			kompetensi.setNama(rs.getString("namakompetensi"));
			kompetensi.setDeskripsi(rs.getString("deskripsikompetensi"));
			Kelompokkompetensi kk = new Kelompokkompetensi();
			kk.setKode(rs.getInt("kodekelompokkompetensi"));
			kk.setNama(rs.getString("namakelompokkompetensi"));
			SharedMethod.Setter(entity, "setKompetensi", kompetensi);
			SharedMethod.Setter(entity, "setKelompokkompetensi", kk);
			break;
		case "kamuskompetensi":
			Periodekompetensi periode = new Periodekompetensi();
			periode.setKode(rs.getInt("kodeperiodekompetensi"));
			periode.setNama(rs.getString("namaperiodekompetensi"));
			periode.setTanggalefektif(rs.getDate("tanggalefektifperiodekompetensi"));
			SharedMethod.Setter(entity, "setPeriodekompetensi", periode);
			kompetensi = new Kompetensi();
			kompetensi.setKode(rs.getString("kodekompetensi"));
			kompetensi.setNama(rs.getString("namakompetensi"));
			kompetensi.setDeskripsi(rs.getString("deskripsikompetensi"));
			Levelkompetensi levelkompetensi = new Levelkompetensi();
			levelkompetensi.setKode(rs.getInt("kodelevelkompetensi"));
			levelkompetensi.setLevel(rs.getShort("levellevelkompetensi"));
			levelkompetensi.setDeskripsi(rs.getString("deskripsilevelkompetensi"));
			Kelompokkompetensi kelompok = new Kelompokkompetensi();
			kelompok.setKode(rs.getInt("kodekelompokkompetensi"));
			kelompok.setNama(rs.getString("namakelompokkompetensi"));
			kelompok.setDeskripsi(rs.getString("deskripsikelompokkompetensi"));
			Klaskelompokkompetensi klaskelompok = new Klaskelompokkompetensi();
			klaskelompok.setKode(rs.getInt("kodeklaskelompokkompetensi"));
			klaskelompok.setNama(rs.getString("namaklaskelompokkompetensi"));
			klaskelompok.setDeskripsi(rs.getString("deskripsiklaskelompokkompetensi"));
			SharedMethod.Setter(kelompok, "setKlaskelompokkompetensi", klaskelompok);
			SharedMethod.Setter(kompetensi, "setKelompokkompetensi", kelompok);
			SharedMethod.Setter(levelkompetensi, "setKompetensi", kompetensi);
			// SharedMethod.Setter(entity, "setKompetensi", kompetensi);
			SharedMethod.Setter(entity, "setLevelkompetensi", levelkompetensi);
			break;
		case "modelkompetensijobtitle":
			JobTitle jt = new JobTitle();
			jt.setKode(rs.getString("kodejobtitle"));
			jt.setNama(rs.getString("namajobtitle"));
			SharedMethod.Setter(entity, "setJobtitle", jt);
			break;
		case "detailmodelkompetensijobtitle":
			Modelkompetensijobtitle modelkompetensijobtitle = new Modelkompetensijobtitle();
			modelkompetensijobtitle.setKode(rs.getInt("kodemodelkompetensijobtitle"));
			jt = new JobTitle();
			jt.setKode(rs.getString("kodejobtitle"));
			jt.setNama(rs.getString("namajobtitle"));
			SharedMethod.Setter(modelkompetensijobtitle, "setJobtitle", jt);

			Kamuskompetensi kamuskompetensi = new Kamuskompetensi();
			kamuskompetensi.setKode(rs.getInt("kodekamuskompetensi"));

			periode = new Periodekompetensi();
			periode.setKode(rs.getInt("kodeperiodekompetensi"));
			periode.setNama(rs.getString("namaperiodekompetensi"));
			periode.setTanggalefektif(rs.getDate("tanggalefektifperiodekompetensi"));
			SharedMethod.Setter(kamuskompetensi, "setPeriodekompetensi", periode);

			kompetensi = new Kompetensi();
			kompetensi.setKode(rs.getString("kodekompetensi"));
			kompetensi.setNama(rs.getString("namakompetensi"));
			levelkompetensi = new Levelkompetensi();
			levelkompetensi.setKode(rs.getInt("kodelevelkompetensi"));
			levelkompetensi.setLevel(rs.getShort("levellevelkompetensi"));
			levelkompetensi.setDeskripsi(rs.getString("deskripsilevelkompetensi"));
			kelompok = new Kelompokkompetensi();
			kelompok.setKode(rs.getInt("kodekelompokkompetensi"));
			kelompok.setNama(rs.getString("namakelompokkompetensi"));
			kelompok.setDeskripsi(rs.getString("deskripsikelompokkompetensi"));
			klaskelompok = new Klaskelompokkompetensi();
			klaskelompok.setKode(rs.getInt("kodeklaskelompokkompetensi"));
			klaskelompok.setNama(rs.getString("namaklaskelompokkompetensi"));
			klaskelompok.setDeskripsi(rs.getString("deskripsiklaskelompokkompetensi"));
			SharedMethod.Setter(kelompok, "setKlaskelompokkompetensi", klaskelompok);
			SharedMethod.Setter(kompetensi, "setKelompokkompetensi", kelompok);
			SharedMethod.Setter(levelkompetensi, "setKompetensi", kompetensi);
			SharedMethod.Setter(kamuskompetensi, "setLevelkompetensi", levelkompetensi);
			SharedMethod.Setter(entity, "setModelkompetensijobtitle", modelkompetensijobtitle);
			SharedMethod.Setter(entity, "setKamuskompetensi", kamuskompetensi);
			break;
		case "listmodelkompetensijobtitle":
			modelkompetensijobtitle = new Modelkompetensijobtitle();
			jt = new JobTitle();
			jt.setKode(rs.getString("kodejobtitle"));
			jt.setNama(rs.getString("namajobtitle"));
			SharedMethod.Setter(modelkompetensijobtitle, "setJobtitle", jt);

			kamuskompetensi = new Kamuskompetensi();
			periode = new Periodekompetensi();
			periode.setKode(rs.getInt("kodeperiodekompetensi"));
			periode.setNama(rs.getString("namaperiodekompetensi"));
			periode.setTanggalefektif(rs.getDate("tanggalefektifperiodekompetensi"));
			SharedMethod.Setter(kamuskompetensi, "setPeriodekompetensi", periode);

			SharedMethod.Setter(entity, "setModelkompetensijobtitle", modelkompetensijobtitle);
			SharedMethod.Setter(entity, "setKamuskompetensi", kamuskompetensi);
			SharedMethod.Setter(entity, "setJumlahkompetensi", rs.getInt("jumlahkompetensi"));
			// ((Detailmodelkompetensijobtitle)
			// entity).setKode(rs.getInt("kode"));
			// ((Detailmodelkompetensijobtitle) entity).setRowStatus(rs
			// .getShort("row_status"));
			break;
		}
	}

	private static void setReferensiPenugasan(Object entity, ResultSet rs) throws SQLException {
		GenericEntitas ent = new GenericEntitas();
		Jabatan jabatan = new Jabatan();
		UnitKerja unitkerja = new UnitKerja();
		Office office = new Office();
		jabatan.setKode(rs.getString("kodejabatan"));
		jabatan.setNama(rs.getString("namajabatan"));
		unitkerja.setKode(rs.getString("kodeunitkerja"));
		unitkerja.setNama(rs.getString("namaunitkerja"));
		office.setKode(rs.getString("kodeoffice"));
		office.setNama(rs.getString("namaoffice"));
		unitkerja.setOffice(office);
		jabatan.setUnitkerja(unitkerja);
		SharedMethod.Setter(entity, "setJabatan", jabatan);
		ent = new GenericEntitas();
		ent.setKode(rs.getString("kodejenissk"));
		ent.setNama(rs.getString("namajenissk"));
		SharedMethod.Setter(entity, "setJenissk", ent);
		ent = new GenericEntitas();
		ent.setKode(rs.getString("kodesubgrade"));
		ent.setNama(rs.getString("namasubgrade"));
		SharedMethod.Setter(entity, "setSubgrade", ent);
		ent = new GenericEntitas();
		ent.setKode(rs.getString("kodegrade"));
		ent.setNama(rs.getString("namagrade"));
		SharedMethod.Setter(entity, "setGrade", ent);
		ent = new GenericEntitas();
		ent.setKode(rs.getString("kodestatusjabatan"));
		ent.setNama(rs.getString("namastatusjabatan"));
		SharedMethod.Setter(entity, "setStatusjabatan", ent);
		Pegawai pegawai = new Pegawai();
		pegawai.setNpp(rs.getString("npp"));
		pegawai.setNama(rs.getString("namapegawai"));
		SharedMethod.Setter(entity, "setPegawai", pegawai);

	}

	private static void setReferensiPegawaiJabatan(Object entity, ResultSet rs) throws SQLException {
		GenericEntitas ent = new GenericEntitas();
		ent.setKode(rs.getString("kodejabatan"));
		ent.setNama(rs.getString("namajabatan"));
		SharedMethod.Setter(entity, "setGenericEntitas2", ent);
		ent = new GenericEntitas();
		ent.setKode(rs.getString("kodeunitkerja"));
		ent.setNama(rs.getString("namaunitkerja"));
		SharedMethod.Setter(entity, "setGenericEntitas3", ent);

	}

	private static void setReferensiInfoDarurat(Object entity, ResultSet rs) throws SQLException {
		GenericEntitas ent = new GenericEntitas();
		ent.setKode(rs.getString("kodehubungankeluarga"));
		ent.setNama(rs.getString("namahubungankeluarga"));
		SharedMethod.Setter(entity, "setHubungankeluarga", ent);
		ent = new GenericEntitas();
		ent.setKode(rs.getString("kodepekerjaan"));
		ent.setNama(rs.getString("namapekerjaan"));
		SharedMethod.Setter(entity, "setPekerjaan", ent);
		Pegawai pegawai = new Pegawai();
		pegawai.setNpp(rs.getString("npp"));
		SharedMethod.Setter(entity, "setPegawai", pegawai);
	}

	private static void setReferensiKaryawan(Object entity, ResultSet rs, String namaentity) throws SQLException {
		switch (namaentity) {
		case "pegawai":
			GenericEntitas ent = new GenericEntitas();
			ent.setKode(rs.getString("kodejeniskelamin"));
			ent.setNama(rs.getString("namajeniskelamin"));
			SharedMethod.Setter(entity, "setJeniskelamin", ent);
			ent = new GenericEntitas();
			ent.setKode(rs.getString("kodestatuskaryawan"));
			ent.setNama(rs.getString("namastatuskaryawan"));
			SharedMethod.Setter(entity, "setStatuskaryawan", ent);
			ent = new GenericEntitas();
			ent.setKode(rs.getString("kodestatusnikah"));
			ent.setNama(rs.getString("namastatusnikah"));
			SharedMethod.Setter(entity, "setStatusnikah", ent);
			ent = new GenericEntitas();
			ent.setKode(rs.getString("kodeagama"));
			ent.setNama(rs.getString("namaagama"));
			SharedMethod.Setter(entity, "setAgama", ent);
			ent = new GenericEntitas();
			ent.setKode(rs.getString("kodesalutasi"));
			ent.setNama(rs.getString("namasalutasi"));
			SharedMethod.Setter(entity, "setSalutasi", ent);
			ent = new GenericEntitas();
			ent.setKode(rs.getString("kodesukubangsa"));
			ent.setNama(rs.getString("namasukubangsa"));
			SharedMethod.Setter(entity, "setSukubangsa", ent);
			ent = new GenericEntitas();
			ent.setKode(rs.getString("kewarganegaraan"));
			ent.setNama(rs.getString("namakewarganegaraan"));
			SharedMethod.Setter(entity, "setKewarganegaraan", ent);
			ent = new GenericEntitas();
			ent.setKode(rs.getString("kodeasalpenerimaan"));
			ent.setNama(rs.getString("namaasalpenerimaan"));
			SharedMethod.Setter(entity, "setAsalpenerimaan", ent);
			ent = new GenericEntitas();
			ent.setKode(rs.getString("kodedati2asal"));
			ent.setNama(rs.getString("namadati2asal"));
			SharedMethod.Setter(entity, "setKotaasal", ent);
			ent = new GenericEntitas();
			ent.setKode(rs.getString("kodepropinsiasal"));
			ent.setNama(rs.getString("namapropinsiasal"));
			SharedMethod.Setter(entity, "setPropinsiasal", ent);
			ent = new GenericEntitas();
			ent.setKode(rs.getString("kodedati2asal2"));
			ent.setNama(rs.getString("namadati2asal2"));
			SharedMethod.Setter(entity, "setKotaasal2", ent);
			ent = new GenericEntitas();
			ent.setKode(rs.getString("kodepropinsiasal2"));
			ent.setNama(rs.getString("namapropinsiasal2"));
			SharedMethod.Setter(entity, "setPropinsiasal2", ent);
			break;
		case "detailalamat":
		case "detailalamat2":
			setReferensiDetailAlamat(entity, rs);
			break;
		case "infodarurat":
			setReferensiInfoDarurat(entity, rs);
			break;
		case "penugasan":
			setReferensiPenugasan(entity, rs);
			break;
		case "pegawaijabatan":
			setReferensiPegawaiJabatan(entity, rs);
			break;
		case "lampiranpenugasan":
			Penugasan penugasan = new Penugasan();
			penugasan.setKode(rs.getInt("kodepenugasan"));
			Pegawai pegawai = new Pegawai();
			pegawai.setNpp(rs.getString("npppegawai"));
			SharedMethod.Setter(penugasan, "setPegawai", pegawai);
			SharedMethod.Setter(entity, "setPenugasan", penugasan);
			break;
		case "infoidentitas":
			Dati2 dati2 = new Dati2();
			GenericEntitas jenisid = new GenericEntitas();
			Propinsi propinsi = new Propinsi();
			propinsi.setKode(rs.getString("kodepropinsi"));
			propinsi.setNama(rs.getString("namapropinsi"));
			dati2.setKode(rs.getString("kodedati2"));
			dati2.setNama(rs.getString("namadati2"));
			SharedMethod.Setter(dati2, "setPropinsi", propinsi);
			jenisid.setKode(rs.getString("kodejenisid"));
			jenisid.setNama(rs.getString("namajenisidentitas"));
			SharedMethod.Setter(entity, "setDati2", dati2);
			SharedMethod.Setter(entity, "setJenisidentitas", jenisid);
			break;
		case "infofisik":
			GenericEntitas golongandarah = new GenericEntitas();
			golongandarah.setKode(rs.getString("kodegolongandarah"));
			golongandarah.setNama(rs.getString("namagolongandarah"));
			SharedMethod.Setter(entity, "setGolongandarah", golongandarah);
			GenericEntitas ukuranbaju = new GenericEntitas();
			ukuranbaju.setKode(rs.getString("kodeukuranbaju"));
			ukuranbaju.setNama(rs.getString("namaukuranbaju"));
			SharedMethod.Setter(entity, "setUkuranbaju", ukuranbaju);
			GenericEntitas ukurancelana = new GenericEntitas();
			ukurancelana.setKode(rs.getString("kodeukurancelana"));
			ukurancelana.setNama(rs.getString("namaukurancelana"));
			SharedMethod.Setter(entity, "setUkurancelana", ukurancelana);
			GenericEntitas ukurankepala = new GenericEntitas();
			ukurankepala.setKode(rs.getString("kodeukurankepala"));
			ukurankepala.setNama(rs.getString("namaukurankepala"));
			SharedMethod.Setter(entity, "setUkurankepala", ukurankepala);
			GenericEntitas ukuransepatu = new GenericEntitas();
			ukuransepatu.setKode(rs.getString("kodeukuransepatu"));
			ukuransepatu.setNama(rs.getString("namaukuransepatu"));
			SharedMethod.Setter(entity, "setUkuransepatu", ukuransepatu);
			GenericEntitas warnakulit = new GenericEntitas();
			warnakulit.setKode(rs.getString("kodewarnakulit"));
			warnakulit.setNama(rs.getString("namawarnakulit"));
			SharedMethod.Setter(entity, "setWarnakulit", warnakulit);
			break;
		case "infokeluarga":
			GenericEntitas hubungankeluarga = new GenericEntitas();
			GenericEntitas jeniskel = new GenericEntitas();
			GenericEntitas pekerjaan = new GenericEntitas();
			Dati2 dati21 = new Dati2();
			Propinsi propinsi1 = new Propinsi();
			propinsi1.setKode(rs.getString("kodepropinsi"));
			propinsi1.setNama(rs.getString("namapropinsi"));
			dati21.setKode(rs.getString("kodedati2"));
			dati21.setNama(rs.getString("namadati2"));
			SharedMethod.Setter(dati21, "setPropinsi", propinsi1);
			hubungankeluarga.setKode(rs.getString("kodehubungankeluarga"));
			hubungankeluarga.setNama(rs.getString("namahubungankeluarga"));
			jeniskel.setKode(rs.getString("kodejeniskelamin"));
			jeniskel.setNama(rs.getString("namajeniskelamin"));
			pekerjaan.setKode(rs.getString("kodepekerjaan"));
			pekerjaan.setNama(rs.getString("namapekerjaan"));
			SharedMethod.Setter(entity, "setHubungankeluarga", hubungankeluarga);
			SharedMethod.Setter(entity, "setJeniskelamin", jeniskel);
			SharedMethod.Setter(entity, "setPekerjaan", pekerjaan);
			SharedMethod.Setter(entity, "setDati2", dati21);
			break;
		case "infopendidikan":
			GenericEntitas institusipendidikan = new GenericEntitas();
			GenericEntitas jurusanpendidikan = new GenericEntitas();
			GenericEntitas kategoripendidikan = new GenericEntitas();
			// GenericEntitas subjurusanpendidikan = new GenericEntitas();
			GenericEntitas pendidikan = new GenericEntitas();
			institusipendidikan.setKode(rs.getString("kodeinstitusipendidikan"));
			institusipendidikan.setNama(rs.getString("namainstitusipendidikan"));
			jurusanpendidikan.setKode(rs.getString("kodejurusanpendidikan"));
			jurusanpendidikan.setNama(rs.getString("namajurusanpendidikan"));
			kategoripendidikan.setKode(rs.getString("kodekategoripendidikan"));
			kategoripendidikan.setNama(rs.getString("namakategoripendidikan"));
			/*
			 * subjurusanpendidikan.setKode(rs.getString(
			 * "kodesubjurusanpendidikan"));
			 * subjurusanpendidikan.setNama(rs.getString(
			 * "namasubjurusanpendidikan"));
			 */
			pendidikan.setKode(rs.getString("kodependidikan"));
			pendidikan.setNama(rs.getString("namapendidikan"));
			SharedMethod.Setter(entity, "setInstitusipendidikan", institusipendidikan);
			SharedMethod.Setter(entity, "setJurusanpendidikan", jurusanpendidikan);
			SharedMethod.Setter(entity, "setKategoripendidikan", kategoripendidikan);
			// SharedMethod.Setter(entity, "setSubjurusanpendidikan",
			// subjurusanpendidikan);
			SharedMethod.Setter(entity, "setPendidikan", pendidikan);
			break;
		case "infoasuransi":
			GenericEntitas ref = new GenericEntitas();
			ref.setKode(rs.getString("kodejenisasuransi"));
			ref.setNama(rs.getString("namajenisasuransi"));
			SharedMethod.Setter(entity, "setJenisasuransi", ref);
			break;
		case "infobaranginventaris":
			ref = new GenericEntitas();
			ref.setKode(rs.getString("kodejenisbaranginventaris"));
			ref.setNama(rs.getString("namajenisbaranginventaris"));
			SharedMethod.Setter(entity, "setJenisbaranginventaris", ref);
			break;
		case "infopelatihan":
			ref = new GenericEntitas();
			ref.setKode(rs.getString("kodetipepelatihan"));
			ref.setNama(rs.getString("namatipepelatihan"));
			SharedMethod.Setter(entity, "setTipepelatihan", ref);
			break;
		case "infosertifikasi":
			ref = new GenericEntitas();
			ref.setKode(rs.getString("kodejenissertifikasi"));
			ref.setNama(rs.getString("namajenissertifikasi"));
			SharedMethod.Setter(entity, "setJenissertifikasi", ref);
			ref = new GenericEntitas();
			ref.setKode(rs.getString("kodelingkupsertifikasi"));
			ref.setNama(rs.getString("namalingkupsertifikasi"));
			SharedMethod.Setter(entity, "setLingkupsertifikasi", ref);
			break;
		case "infopengalamanproyek":
			ref = new GenericEntitas();
			ref.setKode(rs.getString("kodetipeproyek"));
			ref.setNama(rs.getString("namatipeproyek"));
			SharedMethod.Setter(entity, "setTipeproyek", ref);
			ref = new GenericEntitas();
			ref.setKode(rs.getString("kodestatusproyek"));
			ref.setNama(rs.getString("namastatusproyek"));
			SharedMethod.Setter(entity, "setStatusproyek", ref);
			break;
		case "infopenghargaan":
			ref = new GenericEntitas();
			ref.setKode(rs.getString("kodejenispenghargaan"));
			ref.setNama(rs.getString("namajenispenghargaan"));
			SharedMethod.Setter(entity, "setJenispenghargaan", ref);
			break;
		case "infoikatandinas":
			ref = new GenericEntitas();
			ref.setKode(rs.getString("kodeikatandinas"));
			ref.setNama(rs.getString("namaikatandinas"));
			SharedMethod.Setter(entity, "setIkatandinas", ref);
			break;
		case "djpindividu":
			// penugasan = new Penugasan();
			// ent = new GenericEntitas();
			// Jabatan jabatan = new Jabatan();
			// UnitKerja unitkerja = new UnitKerja();
			// Office office = new Office();
			// jabatan.setKode(rs.getString("kodejabatan"));
			// jabatan.setNama(rs.getString("namajabatan"));
			// unitkerja.setKode(rs.getString("kodeunitkerja"));
			// unitkerja.setNama(rs.getString("namaunitkerja"));
			// office.setKode(rs.getString("kodeoffice"));
			// office.setNama(rs.getString("namaoffice"));
			// unitkerja.setOffice(office);
			// jabatan.setUnitkerja(unitkerja);
			// SharedMethod.Setter(penugasan, "setJabatan", jabatan);
			// ent = new GenericEntitas();
			// ent.setKode(rs.getString("kodejenissk"));
			// ent.setNama(rs.getString("namajenissk"));
			// SharedMethod.Setter(penugasan, "setJenissk", ent);
			// ent = new GenericEntitas();
			// ent.setKode(rs.getString("kodesubgrade"));
			// ent.setNama(rs.getString("namasubgrade"));
			// SharedMethod.Setter(penugasan, "setSubgrade", ent);
			// ent = new GenericEntitas();
			// ent.setKode(rs.getString("kodegrade"));
			// ent.setNama(rs.getString("namagrade"));
			// SharedMethod.Setter(penugasan, "setGrade", ent);
			// ent = new GenericEntitas();
			// ent.setKode(rs.getString("kodestatusjabatan"));
			// ent.setNama(rs.getString("namastatusjabatan"));
			// SharedMethod.Setter(penugasan, "setStatusjabatan", ent);
			// pegawai = new Pegawai();
			// pegawai.setNpp(rs.getString("npp"));
			// pegawai.setNama(rs.getString("namapegawai"));
			// SharedMethod.Setter(penugasan, "setPegawai", pegawai);
			// SharedMethod.Setter(entity, "setPenugasan", penugasan);

			// Modelkompetensijobtitle data = new Modelkompetensijobtitle();
			// data.setKode(rs.getInt("kodemodelkompetensijobtitle"));
			//
			// Kamuskompetensi kamuskompetensi = new Kamuskompetensi();
			// kamuskompetensi.setKode(rs.getInt("kodekamuskompetensi"));
			//
			// Kompetensi kompetensi = new Kompetensi();
			// kompetensi.setKode(rs.getString("kodekompetensi"));
			// kompetensi.setNama(rs.getString("namakompetensi"));
			// Levelkompetensi levelkompetensi = new Levelkompetensi();
			// levelkompetensi.setKode(rs.getInt("kodelevelkompetensi"));
			// levelkompetensi.setLevel(rs.getShort("levellevelkompetensi"));
			// levelkompetensi.setDeskripsi(rs.getString("deskripsilevelkompetensi"));
			//
			// SharedMethod.Setter(levelkompetensi, "setKompetensi",
			// kompetensi);
			// SharedMethod.Setter(kamuskompetensi, "setLevelkompetensi",
			// levelkompetensi);
			// SharedMethod.Setter(data, "setKamuskompetensi", kamuskompetensi);

			// JobTitle jt = new JobTitle();
			// jt.setKode(rs.getString("kodejobtitle"));
			// jt.setNama(rs.getString("namajobtitle"));
			// SharedMethod.Setter(data, "setJobtitle", jt);
			// SharedMethod.Setter(entity, "setModelkompetensijobtitle", data);
			break;
		case "detailpendidikannonformalindividu":
			Djpindividu djp = new Djpindividu();
			djp.setKode(rs.getInt("kodedjp"));
			SharedMethod.Setter(entity, "setDjp", djp);
			break;
		case "detailpendidikanformalindividu":
			djp = new Djpindividu();
			djp.setKode(rs.getInt("kodedjp"));
			SharedMethod.Setter(entity, "setDjp", djp);
			pendidikan = new GenericEntitas();
			jurusanpendidikan = new GenericEntitas();
			pendidikan.setKode(rs.getString("kodependidikan"));
			pendidikan.setNama(rs.getString("namapendidikan"));
			jurusanpendidikan.setKode(rs.getString("kodejurusanpendidikan"));
			jurusanpendidikan.setNama(rs.getString("namajurusanpendidikan"));
			SharedMethod.Setter(entity, "setPendidikan", pendidikan);
			SharedMethod.Setter(entity, "setJurusanpendidikan", jurusanpendidikan);
			break;
		case "detailpengalamankerjaindividu":
			djp = new Djpindividu();
			djp.setKode(rs.getInt("kodedjp"));
			SharedMethod.Setter(entity, "setDjp", djp);
			ref = new GenericEntitas();
			ref.setKode(rs.getString("kodepengalamankerja"));
			ref.setNama(rs.getString("namapengalamankerja"));
			SharedMethod.Setter(entity, "setPengalamankerja", ref);
			break;
		case "detailkpiindividu":
		case "detaildimensijabatanindividu":
			Tanggungjawab tanggungjawab = new Tanggungjawab();
			tanggungjawab.setKode(rs.getInt("kodetanggungjawab"));
			SharedMethod.Setter(entity, "setTanggungjawab", tanggungjawab);
			break;
		}
	}

	private static void setReferensiDetailAlamat(Object entity, ResultSet rs) throws SQLException {
		GenericEntitas ent = new GenericEntitas();
		ent.setKode(rs.getString("kodenegara"));
		ent.setNama(rs.getString("namanegara"));
		SharedMethod.Setter(entity, "setNegara", ent);
		ent = new GenericEntitas();
		ent.setKode(rs.getString("kodepropinsi"));
		ent.setNama(rs.getString("namapropinsi"));
		SharedMethod.Setter(entity, "setPropinsi", ent);
		ent = new GenericEntitas();
		ent.setKode(rs.getString("kodedati2"));
		ent.setNama(rs.getString("namadati2"));
		SharedMethod.Setter(entity, "setDati2", ent);
		ent = new GenericEntitas();
		ent.setKode(rs.getString("kodekecamatan"));
		ent.setNama(rs.getString("namakecamatan"));
		SharedMethod.Setter(entity, "setKecamatan", ent);
		ent = new GenericEntitas();
		ent.setKode(rs.getString("kodekelurahan"));
		ent.setNama(rs.getString("namakelurahan"));
		SharedMethod.Setter(entity, "setKelurahan", ent);
		Pegawai pegawai = new Pegawai();
		pegawai.setNpp(rs.getString("npp"));
		SharedMethod.Setter(entity, "setPegawai", pegawai);
	}

	private static void setReferensiUser(Object entity, ResultSet rs, String namaentity) throws SQLException {
		switch (namaentity) {
		case "user":
			GrupUser grupuser = new GrupUser();
			Pegawai pegawai = new Pegawai();
			pegawai.setNpp(rs.getString("npp"));
			pegawai.setNama(rs.getString("nama"));
			pegawai.setEmail(rs.getString("email"));
			SharedMethod.Setter(entity, "setId", rs.getInt("id"));
			SharedMethod.Setter(entity, "setUsername", rs.getString("username"));
			SharedMethod.Setter(entity, "setNpp", rs.getString("npp"));
			grupuser.setKode(rs.getInt("kodegrupuser"));
			grupuser.setNama(rs.getString("namagrupuser"));

			// GrupUser grupuser = new GrupUser();
			// Pegawai pegawai = new Pegawai();
			// pegawai.setNama(rs.getString("nama"));
			// grupuser.setKode(rs.getInt("kodegrupuser"));
			// grupuser.setNama(rs.getString("namagrupuser"));
			SharedMethod.Setter(entity, "setGrupuser", grupuser);
			SharedMethod.Setter(entity, "setPegawai", pegawai);
			break;
		case "grupusermenu":
			grupuser = new GrupUser();
			grupuser.setKode(rs.getInt("kodegrupuser"));
			grupuser.setNama(rs.getString("namagrupuser"));
			SharedMethod.Setter(entity, "setGrupuser", grupuser);
			break;
		}
	}

	private static void setReferensiCuti(Object entity, ResultSet rs, String namaentity) throws SQLException {
		switch (namaentity) {
		case "cuti":
			Pegawai pegawai = new Pegawai();
			Tipe tipe = new Tipe();
			pegawai.setNpp(rs.getString("npp"));
			pegawai.setNama(rs.getString("nama"));
			tipe.setKode(rs.getInt("kodetipe"));
			tipe.setNama(rs.getString("namatipe"));
			SharedMethod.Setter(entity, "setPegawai", pegawai);
			SharedMethod.Setter(entity, "setTipe", tipe);
			break;
		}
	}

}
