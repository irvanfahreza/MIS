package id.go.bpjskesehatan.service;

import id.go.bpjskesehatan.database.Koneksi;
import id.go.bpjskesehatan.entitas.BeanEntitas;
import id.go.bpjskesehatan.entitas.GenericEntitas;
import id.go.bpjskesehatan.entitas.JenisSk;
import id.go.bpjskesehatan.entitas.Metadata;
import id.go.bpjskesehatan.entitas.Respon;
import id.go.bpjskesehatan.entitas.Result;
import id.go.bpjskesehatan.entitas.referensi.Anggaran;
import id.go.bpjskesehatan.entitas.referensi.Dati2;
import id.go.bpjskesehatan.entitas.referensi.Kecamatan;
import id.go.bpjskesehatan.entitas.referensi.Kelurahan;
import id.go.bpjskesehatan.entitas.referensi.Propinsi;
import id.go.bpjskesehatan.entitas.referensi.Subjurusanpendidikan;
import id.go.bpjskesehatan.skpd.Jeniskendaraan;
import id.go.bpjskesehatan.util.SharedMethod;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 * @author Bambang Purwanto
 *
 */
@Path("/ref")
public class ReferensiRest {

	@POST
	@Path("/{servicename}/list/{page}/{row}")
	@Produces("application/json")
	public Response getListData(@Context HttpHeaders headers, @PathParam("servicename") String servicename,
			@PathParam("page") String page, @PathParam("row") String row, String data) {
		String query = null;
		Object obj = null;
		switch (servicename) {
		case "jenissk":
			query = "exec referensi.sp_listdata 'referensi." + servicename + "', ?, ?, ?, ?, ?";
			obj = new JenisSk();
			break;
		case "statusnikah":
		case "jeniskelamin":
		case "jurusanpendidikan":
		case "salutasi":
		case "agama":
		case "bahasa":
		case "ukurancelana":
		case "ukuranbaju":
		case "ukuransepatu":
		case "ukurankepala":
		case "warnakulit":
		case "negara":
		case "pendidikan":
		case "hubungankeluarga":
		case "institusipendidikan":
		case "pengalamankerja":
		case "pekerjaan":
		case "tipepelatihan":
		case "jenissertifikasi":
		case "lingkupsertifikasi":
		case "jenispenghargaan":
		case "jenisbaranginventaris":
		case "ikatandinas":
		case "jenisasuransi":
		case "jenisidentitas":
		case "statuskaryawan":
		case "statusjabatan":
		case "penugasankhusus":
		case "jenisalasanresign":
		case "kewarganegaraan":
		case "golongandarah":
		case "tipeproyek":
		case "statusproyek":
		case "sukubangsa":
		case "asalpenerimaan":
		case "masakerja":
		case "tipealamat":
		case "tipealatkomunikasi":
		case "jenispenugasan":
		case "kategoripelanggaran":
			query = "exec referensi.sp_listdata 'referensi." + servicename + "', ?, ?, ?, ?, ?";
			obj = new GenericEntitas();
			break;
		case "grupuser":
			query = "exec referensi.sp_listdata 'hcis." + servicename + "', ?, ?, ?, ?, ?";
			obj = new GenericEntitas();
			break;
		case "subjurusanpendidikan":
			query = "exec referensi.sp_listsubjurusanpendidikan ?, ?, ?, ?, ?";
			obj = new Subjurusanpendidikan();
			break;
		case "kelurahan":
			query = "exec referensi.sp_list" + servicename + " ?, ?, ?, ?, ?";
			obj = new Kelurahan();
			break;
		case "kecamatan":
			query = "exec referensi.sp_list" + servicename + " ?, ?, ?, ?, ?";
			obj = new Kecamatan();
			break;
		case "dati2":
			query = "exec referensi.sp_list" + servicename + " ?, ?, ?, ?, ?";
			obj = new Dati2();
			break;
		case "propinsi":
			query = "exec referensi.sp_list" + servicename + " ?, ?, ?, ?, ?";
			obj = new Propinsi();
			break;
		case "jeniskendaraan":
			query = "exec referensi.sp_list" + servicename + " ?, ?, ?, ?, ?";
			obj = new Jeniskendaraan();
			break;
		case "anggaran":
			query = "exec referensi.sp_list" + servicename + " ?, ?, ?, ?, ?";
			obj = new Anggaran();
			break;
		}
		if (query != null) {
			// System.out.println(query);
			return RestMethod.getListData(headers, page, row, servicename, obj, query, data);
		} else {
			return Response.status(Status.NOT_FOUND).build();
		}
	}

	@POST
	@Path("/{jnsref}/{row}/{where}")
	@Produces("application/json")
	public Response getReferensi(@Context HttpHeaders headers, @PathParam("jnsref") String jnsref,
			@PathParam("row") String row, @PathParam("where") String where) {

		if (where.length() >= 3) {
			Result<GenericEntitas> result = new Result<GenericEntitas>();
			Metadata metadata = new Metadata();
			Connection con = null;
			ResultSet rs = null;
			PreparedStatement ps = null;
			Koneksi koneksi = null;
			if (SharedMethod.ServiceAuth(headers, metadata)) {
				try {
					koneksi = new Koneksi();
					con = koneksi.getConnection();
					String query = null;
					switch (jnsref) {
					case "statusnikah":
					case "statuskaryawan":
					case "jeniskelamin":
					case "propinsi":
					case "salutasi":
					case "agama":
					case "bahasa":
					case "ukurancelana":
					case "ukuranbaju":
					case "ukuransepatu":
					case "ukurankepala":
					case "warnakulit":
					case "hubungankeluarga":
					case "pekerjaan":
					case "sukubangsa":
					case "jenispenugasan":
					case "jenissk":
					case "statusjabatan":
					case "negara":
					case "jenisidentitas":
					case "jenisalasanresign":
					case "golongandarah":
					case "tipeproyek":
					case "tipealamat":
					case "tipekomunikasi":
						query = selectQuery("referensi." + jnsref, true, row);
						break;
					case "kelurahan":
					case "kecamatan":
					case "dati2":
					case "pendidikan":
					case "institusipendidikan":
					case "jurusanpendidikan":
					case "subjurusanpendidikan":
					case "pengalamankerja":
					case "tipepelatihan":
					case "jenissertifikasi":
					case "lingkupsertifikasi":
					case "jenispenghargaan":
					case "jenisbaranginventaris":
					case "ikatandinas":
					case "jenisasuransi":
						query = selectQuery("referensi." + jnsref, false, row);
						break;
					case "officetype":
					case "hirarkiunitkerja":
					case "organizationchart":
						query = selectQuery("organisasi." + jnsref, true, row);
						break;
					case "jobprefix":
					case "functionalscope":
					case "jobtitle":
					case "grade":
					case "office":
					case "unitkerja":
					case "jabatan":
						query = selectQuery("organisasi." + jnsref, false, row);
						break;
					case "grupuser":
						query = selectQuery("hcis." + jnsref, true, row);
						break;
					case "pegawai":
						query = "select top " + Integer.parseInt(row)
								+ " npp as kode, ltrim(isnull(namadepan,'') + ' ' + isnull(namatengah,'') + ' ' + isnull(namabelakang,'')) as nama from karyawan.pegawai"
								+ " where row_status = 1 and isnull(namadepan,'') + ' ' + isnull(namatengah,'') + ' ' + isnull(namabelakang,'') like ?";
						break;
					case "kompetensi":
						query = selectQuery("kompetensi." + jnsref, false, row);
						break;
					case "periodekompetensi":
					case "kelompokkompetensi":
					case "klaskelompokkompetensi":
						query = selectQuery("kompetensi." + jnsref, true, row);
						break;
					}

					if (query != null) {
						ps = con.prepareStatement(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
						if (ps.getParameterMetaData().getParameterCount() > 0)
							ps.setString(1, "%" + where + "%");

						rs = ps.executeQuery();
						List<GenericEntitas> listdata = new ArrayList<GenericEntitas>();
						Respon<GenericEntitas> response = new Respon<GenericEntitas>();
						metadata.setCode(2);
						metadata.setMessage(Response.Status.NO_CONTENT.toString());
						while (rs.next()) {
							GenericEntitas data = new GenericEntitas();
							data.setKode(rs.getString("kode"));
							data.setNama(rs.getString("nama"));
							listdata.add(data);
							metadata.setCode(1);
							metadata.setMessage("OK");
						}
						response.setList(listdata);
						result.setResponse(response);
						rs.close();

					} else {
						metadata.setCode(0);
						metadata.setMessage("Service Not Found");
					}
				} catch (SQLException e) {
					metadata.setCode(0);
					metadata.setMessage(e.getMessage());
					// TODO
					e.printStackTrace();
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

					if (ps != null) {
						try {
							ps.close();
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
		return Response.status(Status.NOT_FOUND).build();
	}

	@POST
	@Path("/{jnsref}/id/{kode}")
	@Produces("application/json")
	public Response getReferensiById(@Context HttpHeaders headers, @PathParam("jnsref") String jnsref,
			@PathParam("kode") String kode) {
		Result<BeanEntitas> result = new Result<BeanEntitas>();
		Metadata metadata = new Metadata();
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		Koneksi koneksi = null;
		if (SharedMethod.ServiceAuth(headers, metadata)) {
			try {
				koneksi = new Koneksi();
				// koneksi.BuatKoneksi();
				con = koneksi.getConnection();
				String query = null;
				switch (jnsref) {
				case "kelurahan":
					query = "select kode, nama from referensi.kelurahan where row_status = 1 and kodekecamatan = ?";
					break;
				case "kecamatan":
					query = "select kode, nama, kodedati2 from referensi.kecamatan where row_status = 1 and kodedati2 = ?";
					break;
				case "dati2":
					query = "select kode, nama from referensi.dati2 where row_status = 1 and kodepropinsi = ?";
					break;
				case "subgrade":
					query = "select kode, nama from organisasi.subgrade where row_status = 1 and kodegrade = ?";
					break;
				// case "detailalamat":
				// query = selectQuery("referensi." + jnsref, true, null);
				// query = query.concat(" and npp = ?");
				// break;
				}

				if (query != null) {
					ps = con.prepareStatement(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
					ps.setString(1, kode);
					rs = ps.executeQuery();
					List<BeanEntitas> listdata = new ArrayList<BeanEntitas>();
					Respon<BeanEntitas> response = new Respon<BeanEntitas>();
					metadata.setCode(2);
					metadata.setMessage(Response.Status.NO_CONTENT.toString());
					while (rs.next()) {
						BeanEntitas data = new BeanEntitas();
						// data.setId(rs.getInt("id"));
						data.setKode(rs.getString("kode"));
						data.setNama(rs.getString("nama"));
						listdata.add(data);
						metadata.setCode(1);
						metadata.setMessage("OK");
					}
					response.setList(listdata);
					result.setResponse(response);
					rs.close();

				} else {
					metadata.setCode(0);
					metadata.setMessage("Service Not Found");
				}
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

				if (ps != null) {
					try {
						ps.close();
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

	// @GET
	// @Path("/{jnsref}/{id}")
	// @Produces("application/json")
	// public Response getReferensiByAnotherCode(@Context HttpHeaders headers,
	// @PathParam("jnsref") String jnsref, @PathParam("id") String id) {
	//
	// Result<BeanEntitas> result = new Result<BeanEntitas>();
	// Metadata metadata = new Metadata();
	// Connection con = null;
	// ResultSet rs = null;
	// PreparedStatement ps = null;
	// Koneksi koneksi = null;
	// if (SharedMethod.ServiceAuth(headers, metadata)) {
	// try {
	// koneksi = new Koneksi();
	// koneksi.BuatKoneksi();
	// con = koneksi.getConnection();
	// String query = null;
	// switch (jnsref) {
	// case "kelurahan":
	// query =
	// "select masterkelurahanid as id, masterkelurahancode as kode,
	// masterkelurahanname as nama from master_kelurahan where row_status = 1
	// and masterkecamatanid = ?";
	// break;
	// case "kecamatan":
	// query =
	// "select masterkecamatanid as id, masterkecamatancode as kode,
	// masterkecamatanname as nama from master_kecamatan where row_status = 1
	// and masterdati2id = ?";
	// break;
	// case "dati2":
	// query =
	// "select kode, nama from referensi.dati2 where row_status = 1 and
	// kodepropinsi = ?";
	// break;
	// }
	//
	// if (query != null) {
	// ps = con.prepareStatement(query,
	// ResultSet.TYPE_FORWARD_ONLY,
	// ResultSet.CONCUR_READ_ONLY);
	// ps.setString(1, id);
	// rs = ps.executeQuery();
	// List<BeanEntitas> listdata = new ArrayList<BeanEntitas>();
	// Respon<BeanEntitas> response = new Respon<BeanEntitas>();
	// while (rs.next()) {
	// BeanEntitas data = new BeanEntitas();
	// // data.setId(rs.getInt("id"));
	// data.setKode(rs.getString("kode"));
	// data.setNama(rs.getString("nama"));
	// listdata.add(data);
	// metadata.setCode(1);
	// metadata.setMessage("OK");
	// }
	// response.setList(listdata);
	// result.setResponse(response);
	// rs.close();
	//
	// } else {
	// metadata.setCode(0);
	// metadata.setMessage("Service Not Found");
	// }
	// } catch (SQLException e) {
	// metadata.setCode(0);
	// metadata.setMessage(e.getMessage());
	// } catch (NumberFormatException e) {
	// metadata.setCode(0);
	// metadata.setMessage(e.getMessage());
	// } catch (NamingException e) {
	// metadata.setCode(0);
	// metadata.setMessage(e.getMessage());
	// } finally {
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
	//
	// if (koneksi != null)
	// koneksi.closeConnection();
	// if (con != null) {
	// try {
	// con.close();
	// } catch (SQLException e) {
	// }
	// }
	// }
	// }
	// result.setMetadata(metadata);
	// JSONObject xmlJSONObj = new JSONObject(result);
	// return Response.ok(xmlJSONObj.toString()).build();
	//
	// }

	@POST
	@Path("/{jnsref}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response createRef(@Context HttpHeaders headers, @PathParam("jnsref") String jnsref, String data) {

		Object obj = null;
		String namatabel = null;
		switch (jnsref) {
		case "jenissk":
			obj = new JenisSk();
			namatabel = "referensi." + jnsref;
			break;
		case "statusnikah":
		case "jeniskelamin":
		case "propinsi":
		case "salutasi":
		case "agama":
		case "bahasa":
		case "ukurancelana":
		case "ukuranbaju":
		case "ukuransepatu":
		case "ukurankepala":
		case "warnakulit":
		case "negara":
		case "hubungankeluarga":
		case "pendidikan":
		case "institusipendidikan":
		case "jurusanpendidikan":
		case "pengalamankerja":
		case "pekerjaan":
		case "tipepelatihan":
		case "jenissertifikasi":
		case "lingkupsertifikasi":
		case "jenispenghargaan":
		case "jenisbaranginventaris":
		case "ikatandinas":
		case "jenisasuransi":
		case "statuskaryawan":
		case "statusjabatan":
		case "sukubangsa":
		case "hubungan":
		case "jenispenugasan":
		case "jenisidentitas":
		case "jenisalasanresign":
		case "golongandarah":
		case "tipeproyek":
		case "tipealamat":
		case "tipealatkomunikasi":
		case "statusproyek":
		case "jeniskendaraan":
			obj = new GenericEntitas();
			namatabel = "referensi." + jnsref;
			break;
		case "grupuser":
			obj = new GenericEntitas();
			namatabel = "hcis." + jnsref;
			break;
		case "kelurahan":
			obj = new Kelurahan();
			namatabel = "referensi." + jnsref;
			break;
		case "kecamatan":
			obj = new Kecamatan();
			namatabel = "referensi." + jnsref;
			break;
		case "dati2":
			obj = new Dati2();
			namatabel = "referensi." + jnsref;
			break;
		case "subjurusanpendidikan":
			obj = new Subjurusanpendidikan();
			namatabel = "referensi." + jnsref;
			break;
		default:
			jnsref = null;
		}
		if (jnsref == null) {
			return Response.status(Status.NOT_FOUND).build();
		} else
			return RestMethod.createData(headers, data, obj, namatabel);
	}

	@PUT
	@Path("/{jnsref}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response updateData(@Context HttpHeaders headers, @PathParam("jnsref") String jnsref, String data) {

		Object obj = null;
		String namatabel = null;
		switch (jnsref) {
		case "jenissk":
			obj = new JenisSk();
			namatabel = "referensi." + jnsref;
			break;
		case "statusnikah":
		case "jeniskelamin":
		case "propinsi":
		case "salutasi":
		case "agama":
		case "bahasa":
		case "ukurancelana":
		case "ukuranbaju":
		case "ukuransepatu":
		case "ukurankepala":
		case "warnakulit":
		case "negara":
		case "hubungankeluarga":
		case "pendidikan":
		case "institusipendidikan":
		case "jurusanpendidikan":
		case "pengalamankerja":
		case "pekerjaan":
		case "tipepelatihan":
		case "jenissertifikasi":
		case "lingkupsertifikasi":
		case "jenispenghargaan":
		case "jenisbaranginventaris":
		case "ikatandinas":
		case "jenisasuransi":
		case "statuskaryawan":
		case "statusjabatan":
		case "jenispenugasan":
		case "jenisidentitas":
		case "jenisalasanresign":
		case "golongandarah":
		case "tipeproyek":
		case "tipealamat":
		case "tipealatkomunikasi":
		case "statusproyek":
		case "sukubangsa":
		case "jeniskendaraan":
			obj = new GenericEntitas();
			namatabel = "referensi." + jnsref;
			break;
		case "grupuser":
			obj = new GenericEntitas();
			namatabel = "hcis." + jnsref;
			break;
		case "kelurahan":
			obj = new Kelurahan();
			namatabel = "referensi." + jnsref;
			break;
		case "kecamatan":
			obj = new Kecamatan();
			namatabel = "referensi." + jnsref;
			break;
		case "dati2":
			obj = new Dati2();
			namatabel = "referensi." + jnsref;
			break;
		case "subjurusanpendidikan":
			obj = new Subjurusanpendidikan();
			namatabel = "referensi." + jnsref;
			break;
		default:
			jnsref = null;
		}
		if (jnsref == null) {
			return Response.status(Status.NOT_FOUND).build();
		} else
			return RestMethod.updateData(headers, data, obj, namatabel);
	}

	@POST
	@Path("/{jnsref}/delete")
	@Consumes("application/json")
	@Produces("application/json")
	public Response deleteData(@Context HttpHeaders headers, String data, @PathParam("jnsref") String jnsref) {
		Object obj = null;
		String namatabel = null;
		switch (jnsref) {
		case "jenissk":
			obj = new JenisSk();
			namatabel = "referensi." + jnsref;
			break;
		case "statusnikah":
		case "jeniskelamin":
		case "propinsi":
		case "salutasi":
		case "agama":
		case "bahasa":
		case "ukurancelana":
		case "ukuranbaju":
		case "ukuransepatu":
		case "ukurankepala":
		case "warnakulit":
		case "negara":
		case "hubungankeluarga":
		case "pendidikan":
		case "institusipendidikan":
		case "jurusanpendidikan":
		case "pengalamankerja":
		case "pekerjaan":
		case "tipepelatihan":
		case "jenissertifikasi":
		case "lingkupsertifikasi":
		case "jenispenghargaan":
		case "jenisbaranginventaris":
		case "ikatandinas":
		case "jenisasuransi":
		case "statuskaryawan":
		case "statusjabatan":
		case "jenispenugasan":
		case "jenisidentitas":
		case "jenisalasanresign":
		case "golongandarah":
		case "tipeproyek":
		case "tipealamat":
		case "tipealatkomunikasi":
		case "statusproyek":
		case "sukubangsa":
		case "jeniskendaraan":
			obj = new GenericEntitas();
			namatabel = "referensi." + jnsref;
			break;
		case "grupuser":
			obj = new GenericEntitas();
			namatabel = "hcis." + jnsref;
			break;
		case "kelurahan":
			obj = new Kelurahan();
			namatabel = "referensi." + jnsref;
			break;
		case "kecamatan":
			obj = new Kecamatan();
			namatabel = "referensi." + jnsref;
			break;
		case "dati2":
			obj = new Dati2();
			namatabel = "referensi." + jnsref;
			break;
		case "subjurusanpendidikan":
			obj = new Subjurusanpendidikan();
			namatabel = "referensi." + jnsref;
			break;
		default:
			jnsref = null;
		}
		if (jnsref == null) {
			return Response.status(Status.NOT_FOUND).build();
		} else
			return RestMethod.deleteData(headers, data, obj, namatabel);
	}

	private static String selectQuery(String namatabel, boolean all, String row) {
		if (all)
			return "select kode, nama from " + namatabel + " where row_status = 1";
		else
			return "select " + Integer.parseInt(row) + " kode, nama from " + namatabel
					+ " where row_status = 1 and nama like ?";
	}

}
