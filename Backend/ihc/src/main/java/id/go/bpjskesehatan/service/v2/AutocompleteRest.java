package id.go.bpjskesehatan.service.v2;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import id.go.bpjskesehatan.database.Koneksi;
import id.go.bpjskesehatan.entitas.*;
import id.go.bpjskesehatan.util.SharedMethod;

@Path("v2/autocomplete")
public class AutocompleteRest {	
	
	@GET
	@Path("/{servicename}/{val1}/{val2}")
	@Produces("application/json")
	public Response GetList(@Context HttpHeaders headers, @PathParam("servicename") String servicename, @PathParam("val1") String val1, @PathParam("val2") String val2) {
		
		Respon<Autocomplete> response = new Respon<Autocomplete>();
		Metadata metadata = new Metadata();
		Result<Autocomplete> result = new Result<Autocomplete>();
		
		Connection con = null;
		ResultSet rs = null;
		CallableStatement cs = null;
		String query = null;
		
		if (SharedMethod.VerifyToken(headers, metadata)) {
			try {
				Boolean ok = true;
				switch (servicename) {
				case "pegawai":
					query = "exec karyawan.sp_autocomplete_pegawai ?, ?";
					break;
				case "pegawaibykantor":
					query = "exec karyawan.sp_list_autocompletepegawaibykantor ?, ?";
					break;
				case "pegawaibykantoronly":
					query = "exec karyawan.sp_list_autocompletepegawaibykantorOnly ?, ?";
					break;
				case "office":
					query = "exec organisasi.sp_autocomplete_office ?, ?";
					break;
				case "hirarkijabatan":
					query = "exec organisasi.sp_autocomplete_hirarkijabatan ?, ?";
					break;
				case "unitkerja":
					query = "exec organisasi.sp_autocomplete_unitkerja ?, ?";
					break;
				case "officesekota":
					query = "exec organisasi.sp_autocomplete_office1kota ?, ?";
					break;	
				case "cutitunjangan":
					query = "exec cuti.sp_autocomplete_cutidengantunjangan ?, ?";
					break;
				case "kodeakun":
					query = "exec cuti.sp_autocomplete_kodeakun ?, ?";
					break;
				case "kodeprogram":
					query = "exec cuti.sp_autocomplete_kodeprogram ?, ?";
					break;
				case "kodeakunbyprogram":
					query = "exec cuti.sp_autocomplete_kodeakunbyprogram ?, ?";
					break;
				case "kodeprogrambytahun":
					query = "exec cuti.sp_autocomplete_kodeprogrambytahun ?, ?";
					break;
				case "institusipendidikan":
					query = "select top 10 kode, nama from referensi.institusipendidikan where row_status=1 and nama like '%"+val1+"%' and kodekategoripendidikan="+val2+" order by nama";
					break;
				case "jurusanpendidikan":
					query = "select top 10 kode, nama from referensi.jurusanpendidikan where nama like '%"+val1+"%'  and row_status=1 order by nama";
					break;
				case "subjurusanpendidikan":
					query = "select top 10 kode, nama from referensi.subjurusanpendidikan where row_status=1 and nama like '%"+val1+"%' and kodejurusanpendidikan="+val2+" order by nama";
					break;
				case "tipepelatihan":
					query = "select top 10 kode, nama from referensi.tipepelatihan where nama like '%"+val1+"%' and row_status=1 order by nama";
					break;
				case "jenissertifikasi":
					query = "select top 10 kode, nama from referensi.jenissertifikasi where nama like '%"+val1+"%' and row_status=1 order by nama";
					break;
				case "lingkupsertifikasi":
					query = "select top 10 kode, nama from referensi.lingkupsertifikasi where nama like '%"+val1+"%' and row_status=1 order by nama";
					break;
				case "jenispenghargaan":
					query = "select top 10 kode, nama from referensi.lingkupsertifikasi where nama like '%"+val1+"%' and row_status=1 order by nama";
					break;
				case "jabatan":
					query = "exec organisasi.sp_listjabatan 1,10,0,null,?";
					break;
				case "jobgrade":
					query = "exec organisasi.sp_listgrade 1,10,0,null,?";
					break;
				case "unitkerjabaganaktif":
					query = "exec organisasi.sp_list_autocompleteunitkerjabaganaktif 1,10,0,null,?";
					break;
				case "jabatanbaganaktif":
					query = "exec organisasi.sp_list_autocompletejabatanbaganaktif 1,10,0,null,?";
					break;
				case "officebaganaktif":
					query = "exec organisasi.sp_list_autocompleteofficebaganaktif ?,?";
					break;
				case "jobdivisionoffice":
					query = "exec organisasi.sp_listunitkerja 1,10,0,null,?";
					break;
				case "kamuskompetensi":
					query = "exec kompetensi.sp_listkompetensi 1,10,0,null,?";
					break;
				case "kompetensi":
					query = "exec kompetensi.sp_listkompetensi 1,10,0,null,?";
					break;
				case "jobtitle":
					query = "exec organisasi.sp_listjobtitle 1,10,0,null,?";
					break;
				case "periodekamuskompetensi":
					query = "exec kompetensi.sp_list_autocompletekamuskompetensi 1,10,0,null,?";
					break;
				case "organizationchart":
					query = "exec organisasi.sp_listorganizationchart 1,10,0,null,?";
					break;
				case "pengalamankerja":
					query = "exec referensi.sp_listdata 'referensi.pengalamankerja', 1, 10, 0, null, ?";
					break;
				case "jobprefix":
					query = "exec organisasi.sp_listjobprefix 1,10,0,null,?";
					break;
				case "functionalscope":
					query = "exec organisasi.sp_listfunctionalscope 1,10,0,null,?";
					break;
				case "unitkerjabybagan":
					query = "exec organisasi.sp_listunitkerja 1,10,0,null,?";
					break;
				case "ubkompetensipeserta":
					query = "exec kinerja.sp_listpesertaubkompetensi2 1,10,0,null,?,?,null";
					break;
				case "ubkomitmenpeserta":
					query = "exec kinerja.sp_listpesertaubkomitmen2 1,10,0,null,?,?,null";
					break;
				case "periodekinerja":
					query = "exec kinerja.sp_listperiodekinerja 1,10,0,null,?";
					break;
				default:
					ok = false;
					return Response.status(404).build();
				}
				
				if(ok){
					con = new Koneksi().getConnection();
					
					cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
					switch (servicename) {
					case "institusipendidikan":
					case "jurusanpendidikan":
					case "subjurusanpendidikan":
					case "tipepelatihan":
					case "jenissertifikasi":
					case "lingkupsertifikasi":
					case "jenispenghargaan":
						break;
					case "jabatan":
					case "jobgrade":
					case "jobdivisionoffice":
					case "kamuskompetensi":
					case "jobtitle":
					case "pengalamankerja":
						cs.setString(1, "nama like '%"+val1+"%' and row_status = 1");
						break;
					case "jobprefix":
					case "functionalscope":
					case "organizationchart":
						cs.setString(1, "nama like '%"+val1+"%'");
						break;
					case "unitkerjabybagan":
						cs.setString(1, "nama like '%"+val1+"%' and kodeorganizationchart = '"+val2+"'");
						break;
					case "unitkerjabaganaktif":
						cs.setString(1, "nama like '%"+val1+"%' and kodeofficetype = '"+val2+"'");
						break;
					case "jabatanbaganaktif":
						cs.setString(1, "nama like '%"+val1+"%' and kodeunitkerja = '"+val2+"'");
						break;
					case "kompetensi":
						cs.setString(1, "nama like '%"+val1+"%' and kodekelompokkompetensi = "+Integer.parseInt(val2)+" and row_status = 1");
						break;
					case "periodekamuskompetensi":
						cs.setString(1, "namakompetensi like '%"+val1+"%' and kodeperiodekompetensi = "+Integer.parseInt(val2)+" and row_status = 1");
						break;
					case "ubkompetensipeserta":
						cs.setString(1, "nama like '%"+val1+"%' or npp like '%"+val1+"%'");
						cs.setString(2, "kodeperiodeubkompetensi = " + val2);
						break;
					case "ubkomitmenpeserta":
						cs.setString(1, "nama like '%"+val1+"%' or npp like '%"+val1+"%'");
						cs.setString(2, "kodeperiodeubkomitmen = " + val2);
						break;
					case "periodekinerja":
						cs.setString(1, "nama like '%"+val1+"%'");
						break;
					default:
						cs.setString(1, val1);
						cs.setString(2, val2);
						break;
					}
					Boolean isresult = cs.execute();
					
					metadata.setCode(1);
					metadata.setMessage("Data kosong.");
					if(isresult) {
						rs = cs.getResultSet();
						
						List<Autocomplete> autocompletes = new ArrayList<Autocomplete>();
						while (rs.next()) {
							Autocomplete autocomplete = new Autocomplete();
							switch (servicename) {
							case "pegawai":
								autocomplete.setNpp(rs.getString("npp"));
								autocomplete.setNama(rs.getString("nama"));
								break;
							case "pegawaibykantor":
							case "pegawaibykantoronly":
								autocomplete.setNpp(rs.getString("npp"));
								autocomplete.setNama(rs.getString("nama"));
								autocomplete.setKodepenugasan(rs.getInt("kodepenugasan"));
								break;
							case "office":
								autocomplete.setKode2(rs.getString("kode"));
								autocomplete.setNama(rs.getString("nama"));
								autocomplete.setNamakelas(rs.getString("namakelas"));
								autocomplete.setKodeofficetype(rs.getString("kodeofficetype"));
								autocomplete.setIk(rs.getInt("ik"));
								autocomplete.setTunjdacil(rs.getBigDecimal("tunjdacil"));
								autocomplete.setTunjkhusus(rs.getBigDecimal("tunjkhusus"));
								break;
							case "hirarkijabatan":
								autocomplete.setKode2(rs.getString("kode"));
								autocomplete.setNama(rs.getString("nama"));
								autocomplete.setKodejobtitle(rs.getString("kodejobtitle"));
								autocomplete.setKodejobgrade(rs.getString("kodejobgrade"));
								break;
							case "officesekota":
							case "unitkerja":
							case "kodeakun":
							case "kodeprogram":
							case "kodeakunbyprogram":
							case "kodeprogrambytahun":
							case "jabatan":
							case "jobgrade":
							case "unitkerjabaganaktif":
							case "jabatanbaganaktif":
							case "jobdivisionoffice":
							case "kamuskompetensi":
							case "kompetensi":
							case "jobtitle":
							case "jobprefix":
							case "functionalscope":
							case "organizationchart":
							case "unitkerjabybagan":
								autocomplete.setKode2(rs.getString("kode"));
								autocomplete.setNama(rs.getString("nama"));
								break;
							case "periodekamuskompetensi":
								autocomplete.setKode2(rs.getString("kodekompetensi"));
								autocomplete.setNama(rs.getString("namakompetensi"));
								break;
							case "officebaganaktif":
								autocomplete.setKode2(rs.getString("kode"));
								autocomplete.setNama(rs.getString("nama"));
								autocomplete.setKodeofficetype(rs.getString("kodeofficetype"));
								break;
							case "cutitunjangan":
								autocomplete.setKode(rs.getInt("kode"));
								autocomplete.setNama(rs.getString("nama"));
								autocomplete.setKodetipe(rs.getInt("kodetipe"));
								autocomplete.setNomor(rs.getString("nomor"));
								autocomplete.setNamapegawai(rs.getString("namapegawai"));
								autocomplete.setNamasubgrade(rs.getString("namasubgrade"));
								autocomplete.setNamajobtitle(rs.getString("namajobtitle"));
								autocomplete.setNamaunitkerja(rs.getString("namaunitkerja"));
								autocomplete.setGajipokok(rs.getBigDecimal("gajipokok"));
								autocomplete.setTunjjabatan(rs.getBigDecimal("tunjjabatan"));
								autocomplete.setTupres(rs.getBigDecimal("tupres"));
								autocomplete.setUtilitas(rs.getBigDecimal("utilitas"));
								autocomplete.setTunjangan(rs.getBigDecimal("tunjangan"));
								autocomplete.setNorek(rs.getString("norek"));
							default:
								autocomplete.setKode(rs.getInt("kode"));
								autocomplete.setNama(rs.getString("nama"));
								break;
							}
							autocompletes.add(autocomplete);
						}
						response.setList(autocompletes);
						metadata.setCode(1);
						metadata.setMessage("Ok.");
						result.setResponse(response);
						rs.close();
					}
					cs.close();
					
				}
			} catch (SQLException e) {
				e.printStackTrace();
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
			} catch (NamingException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
				e.printStackTrace();
			} catch (SecurityException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
				e.printStackTrace();
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
}
