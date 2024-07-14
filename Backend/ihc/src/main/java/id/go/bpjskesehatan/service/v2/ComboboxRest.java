package id.go.bpjskesehatan.service.v2;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import id.go.bpjskesehatan.entitas.Respon;
import id.go.bpjskesehatan.entitas.Result;
import id.go.bpjskesehatan.service.mobile.v1.AuthMobile;
import id.go.bpjskesehatan.util.SharedMethod;
import id.go.bpjskesehatan.entitas.Metadata;
import id.go.bpjskesehatan.database.Koneksi;
import id.go.bpjskesehatan.entitas.Combobox;

@Path("v2/combobox")
public class ComboboxRest {	
	
	@GET
	@Path("/{servicename}/{row_status}")
	@Produces("application/json")
	public Response GetList(@Context HttpHeaders headers, @PathParam("servicename") String servicename, @PathParam("row_status") String row_status) {
		
		Respon<Combobox> response = new Respon<Combobox>();
		Metadata metadata = new Metadata();
		Result<Combobox> result = new Result<Combobox>();
		
		Connection con = null;
		ResultSet rs = null;
		CallableStatement cs = null;
		String query = null;
		
		if (SharedMethod.VerifyToken(headers, metadata) || AuthMobile.VerifyToken(headers, metadata)) {
			try {
				Boolean ok = true;
				switch (servicename) {
				case "reftipecuti":
					query = "select kode, nama from cuti.tipe where row_status=?";
					break;
				case "reftipecutibyjenis":
					if(row_status.equalsIgnoreCase("0"))
						query = "select kode, nama from cuti.tipe where row_status=1 and jenis=0";
					else
						query = "select kode, nama from cuti.tipe where row_status=1 and jenis in (1,2)";
					break;
				case "refpredikat":
					query = "select kode, nama from kinerja.predikat where row_status=?";
					break;
				case "kodeakun":
					query = "select distinct KDAKUN as kode, concat(KDAKUN,' - ',nmakun) as nama from referensi.vw_mataanggaran where tahun=? order by nama";
					break;
				case "kodeprogram":
					query = "select distinct KDPROG as kode, concat(KDPROG,' - ',nmprog) as nama from referensi.vw_mataanggaran where tahun=? and KDAKUN=? order by nama";
					break;
				case "propinsi":
					query = "select kode, nama from referensi.propinsi where row_status=? order by nama";
					break;
				case "dati2":
					query = "select kode, nama from referensi.dati2 where row_status=1 and kodepropinsi=? order by nama";
					break;
				case "kecamatan":
					query = "select kode, nama from referensi.kecamatan where row_status=1 and kodedati2=? order by nama";
					break;
				case "kelurahan":
					query = "select kode, nama from referensi.kelurahan where row_status=1 and kodekecamatan=? order by nama";
					break;
				case "jeniskendaraan":
					query = "select kode, nama from referensi.jeniskendaraan where row_status=?";
					break;
				case "jenisbbm":
					query = "select kode, nama from referensi.jenisbbm where row_status=?";
					break;
				case "risikopergerakankarir":
					query = "select kode, nama from referensi.risikopergerakankarir where row_status=?";
					break;
				case "rangkaian":
					query = "select kode, nama from referensi.rangkaian where row_status=1 and isKP<?";
					break;
				case "diagramtalenta":
					query = "select kode, nama, poin from referensi.diagramtalenta where row_status=?";
					break;
				case "statusjabatan":
					query = "select kode, nama from referensi.statusjabatan where row_status=?";
					break;
				case "jenistelaah":
					query = "select kode, nama from referensi.telaah where row_status=?";
					break;
				case "jenispelanggaran":
					query = "select kode, nama from referensi.sanksipelanggaran where row_status=?";
					break;
				case "jenispegawai":
					query = "select kode, nama from payroll.jenispegawai where kode >= ?";
					break;
				case "refpicbyjenis":
					query = "exec skpd.sp_listpicbyjenis ?";
					break;
				case "officewilayah":
					query = "exec organisasi.sp_combobox_office_wilayah ?";
					break;
				case "komponengaji":
					query = "select kode, nama from payroll.komponen where row_status=?";
					break;
				case "paketdonasi":
					query = "select kode, nama from referensi.paketdonasi where row_status=?";
					break;
				case "negara":
					query = "select kode, nama from referensi.negara where row_status=?";
					break;
				case "agama":
					query = "select kode, nama from referensi.agama where row_status=?";
					break;
				case "jeniskel":
					query = "select kode, nama from referensi.jeniskelamin where row_status=?";
					break;
				case "sukubangsa":
					query = "select kode, nama from referensi.sukubangsa where row_status=?";
					break;
				case "maritalstatus":
					query = "select kode, nama from referensi.statusnikah where row_status=?";
					break;
				case "salutasi":
					query = "select kode, nama from referensi.salutasi where row_status=?";
					break;
				case "jenisidentitas":
					query = "select kode, nama from referensi.jenisidentitas where row_status=?";
					break;
				case "golongandarah":
					query = "select kode, nama from referensi.golongandarah where row_status=?";
					break;
				case "ukuranbaju":
					query = "select kode, nama from referensi.ukuranbaju where row_status=?";
					break;
				case "ukurankepala":
					query = "select kode, nama from referensi.ukurankepala where row_status=?";
					break;
				case "ukurancelana":
					query = "select kode, nama from referensi.ukurancelana where row_status=?";
					break;
				case "ukuransepatu":
					query = "select kode, nama from referensi.ukuransepatu where row_status=?";
					break;
				case "warnakulit":
					query = "select kode, nama from referensi.warnakulit where row_status=?";
					break;
				case "hubungankeluarga":
					query = "select kode, nama from referensi.hubungankeluarga where row_status=?";
					break;
				case "pekerjaan":
					query = "select kode, nama from referensi.pekerjaan where row_status=?";
					break;
				case "institusipendidikan":
					query = "select kode, nama from referensi.institusipendidikan where row_status=1 and kodekategoripendidikan=?";
					break;
				case "pendidikan":
					query = "select kode, nama, kodekategoripendidikan from referensi.pendidikan where row_status=?";
					break;
				case "jenisasuransi":
					query = "select kode, nama from referensi.jenisasuransi where row_status=?";
					break;
				case "bank":
					query = "select kode, nama from referensi.bank where row_status=?";
					break;
				case "jenisbaranginventaris":
					query = "select kode, nama from referensi.jenisbaranginventaris where row_status=?";
					break;
				case "ikatandinas":
					query = "select kode, nama from referensi.ikatandinas where row_status=?";
					break;
				case "jenispenghargaan":
					query = "select kode, nama from referensi.jenispenghargaan where row_status=?";
					break;
				case "tipeproyek":
					query = "select kode, nama from referensi.tipeproyek where row_status=?";
					break;
				case "statusproyek":
					query = "select kode, nama from referensi.statusproyek where row_status=?";
					break;
				case "jenissertifikasi":
					query = "select kode, nama from referensi.jenissertifikasi where row_status=?";
					break;
				case "lingkupsertifikasi":
					query = "select kode, nama from referensi.lingkupsertifikasi where row_status=?";
					break;
				case "tipepelatihan":
					query = "select kode, nama from referensi.tipepelatihan where row_status=?";
					break;
				case "grupuser":
					query = "select kode, nama from hcis.grupuser where row_status=?";
					break;
				case "komposisikuota":
					query = "select kode, nama from cuti.komposisikuota where row_status=?";
					break;
				case "jurusanpendidikan":
					query = "select kode, nama from referensi.jurusanpendidikan where row_status=?";
					break;
				case "jenissk":
					query = "select kode, nama from referensi.jenissk where row_status=?";
					break;
				case "jenisskresign":
					query = "select kode, nama from referensi.jenissk where row_status=? and isresign=1";
					break;
				case "jenisskpenugasan":
					query = "select kode, nama, lockjabatan from referensi.jenissk where row_status=? and isresign<>1";
					break;
				case "statuskaryawan":
					query = "select kode, nama from referensi.statuskaryawan where row_status=?";
					break;
				case "jenisalasanresign":
					query = "select kode, nama from referensi.jenisalasanresign where row_status=?";
					break;
				case "subgrade":
					query = "select kode, nama from organisasi.subgrade where row_status=1 and kodegrade=?";
					break;
				case "levelkompetensi":
				case "levelkompetensideskripsi":
					query = "exec kompetensi.sp_listlevelkompetensi 1,100,0,null,?";
					break;
				case "levelkompetensiindikator":
					query = "exec kompetensi.sp_listlevelkompetensiindikator 1,100,0,null,?";
					break;
				case "klasifikasikelompokkompetensi":
					query = "exec kompetensi.sp_listklaskelompokkompetensi 1,100,0,null,?";
					break;
				case "kelompokkompetensi":
					query = "exec kompetensi.sp_listkelompokkompetensi 1,100,0,null,?";
					break;
				case "periodekompetensi":
					query = "exec kompetensi.sp_listperiodekompetensi 1,100,0,null,null";
					break;
				case "organizationchart":
					query = "exec organisasi.sp_listorganizationchart 1,100,0,null,?";
					break;
				case "officetype":
					query = "exec organisasi.sp_listofficetype 1,100,0,null,null";
					break;
				case "hirarkiunitkerja":
					query = "exec organisasi.sp_listhirarkiunitkerja 1,100,0,null,null";
					break;
				case "penilai":
					query = "exec kinerja.sp_listpenilai 1,100,0,null,null";
					break;
				case "kriteriapenilaian":
					query = "select kode, nama from kinerja.masterkriteriapenilaian where row_status=1 and tipeumpanbalik=?";
					break;
				case "mastertipejawaban":
					query = "select kode, nama from kinerja.mastertipejawaban where row_status=?";
					break;
				case "komponenkinerja":
					query = "exec kinerja.sp_listkomponenperperiodekinerja ?";
					break;
				default:
					ok = false;
					return Response.status(404).build();
				}
				
				if(ok){
					con = new Koneksi().getConnection();
					
					cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
					switch (servicename) {
					case "reftipecutibyjenis":
					case "officetype":
					case "hirarkiunitkerja":
					case "penilai":
						break;
					case "kodeakun":
						int year = Calendar.getInstance().get(Calendar.YEAR);
						cs.setInt(1, year);
						break;
					case "kodeprogram":
						year = Calendar.getInstance().get(Calendar.YEAR);
						cs.setInt(1, year);
						cs.setString(2, row_status);
						break;
					case "propinsi":
					case "dati2":
					case "kecamatan":
					case "kelurahan":
					case "subgrade":
						cs.setString(1, row_status);
						break;
					case "levelkompetensi":
					case "levelkompetensideskripsi":
						cs.setString(1, "kodekompetensi = '"+row_status+"' and row_status = 1");
						break;
					case "levelkompetensiindikator":
						cs.setString(1, "kodelevelkompetensi = "+row_status+" and row_status = 1");
						break;
					case "klasifikasikelompokkompetensi":
					case "kelompokkompetensi":
					case "organizationchart":
					case "exec":
						cs.setString(1, "row_status = " + Integer.parseInt(row_status));
						break;
					case "periodekompetensi":
						break;
					default:
						cs.setInt(1, Integer.parseInt(row_status));
						break;
					}
					Boolean isresult = cs.execute();
					
					metadata.setCode(1);
					metadata.setMessage("Data kosong.");
					if(isresult) {
						rs = cs.getResultSet();
						
						List<Combobox> comboboxs = new ArrayList<Combobox>();
						switch (servicename) {
							case "komponengaji":
								Combobox comboboxAll = new Combobox();
								comboboxAll.setKode(0);
								comboboxAll.setNama("--SEMUA--");
								comboboxs.add(comboboxAll);
								break;
							default:
								break;
						}
						while (rs.next()) {
							Combobox combobox = new Combobox();
							switch (servicename) {
							case "refpredikat":
							case "kodeakun":
							case "kodeprogram":
							case "officewilayah":
							case "refpicbyjenis":
							case "propinsi":
							case "dati2":
							case "kecamatan":
							case "kelurahan":
							case "subgrade":
							case "organizationchart":
							case "officetype":
							case "hirarkiunitkerja":
								combobox.setKode2(rs.getString("kode"));
								combobox.setNama(rs.getString("nama"));
								break;
							case "levelkompetensi":
								combobox.setKode(rs.getInt("kode"));
								combobox.setNama(rs.getString("level"));
								break;
							case "levelkompetensideskripsi":
								combobox.setKode(rs.getInt("kode"));
								combobox.setNama(rs.getString("level") + " - " + rs.getString("deskripsi"));
								break;
							case "levelkompetensiindikator":
								combobox.setKode(rs.getInt("kode"));
								combobox.setNama(rs.getString("indikator"));
								break;
							case "diagramtalenta":
								combobox.setKode(rs.getInt("kode"));
								combobox.setNama(rs.getString("nama"));
								combobox.setPoin(rs.getInt("poin"));
								break;
							case "pendidikan":
								combobox.setKode(rs.getInt("kode"));
								combobox.setNama(rs.getString("nama"));
								combobox.setKodekategoripendidikan(rs.getInt("kodekategoripendidikan"));
								break;
							case "periodekompetensi":
								combobox.setKode(rs.getInt("kode"));
								combobox.setNama(rs.getString("nama"));
								combobox.setRow_status(rs.getInt("row_status"));
								break;
							case "jenisskpenugasan":
								combobox.setKode(rs.getInt("kode"));
								combobox.setNama(rs.getString("nama"));
								combobox.setLockjabatan(rs.getInt("lockjabatan"));
								break;
							default:
								combobox.setKode(rs.getInt("kode"));
								combobox.setNama(rs.getString("nama"));
								break;
							}
							comboboxs.add(combobox);
						}
						response.setList(comboboxs);
						metadata.setCode(1);
						metadata.setMessage("Ok.");
						result.setResponse(response);
						rs.close();
					}
					cs.close();
					
				}
			} catch (SQLException e) {
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
	
	@GET
	@Path("/{servicename}/{row_status}/{val2}")
	@Produces("application/json")
	public Response GetList2(@Context HttpHeaders headers, @PathParam("servicename") String servicename, @PathParam("row_status") String row_status, @PathParam("val2") String val2) {
		
		Respon<Combobox> response = new Respon<Combobox>();
		Metadata metadata = new Metadata();
		Result<Combobox> result = new Result<Combobox>();
		
		Connection con = null;
		ResultSet rs = null;
		CallableStatement cs = null;
		String query = null;
		
		if (SharedMethod.VerifyToken(headers, metadata) || AuthMobile.VerifyToken(headers, metadata)) {
			try {
				Boolean ok = true;
				switch (servicename) {
				case "periodekamuskompetensilevel":
					query = "exec kompetensi.sp_list_autocompletekamuskompetensilevel 1,100,0,null,?";
					break;
				default:
					ok = false;
					return Response.status(404).build();
				}
				
				if(ok){
					con = new Koneksi().getConnection();
					
					cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
					switch (servicename) {
					case "periodekamuskompetensilevel":
						cs.setString(1, "kodekompetensi = '"+row_status+"' and kodeperiodekompetensi = " + Integer.parseInt(val2));
						break;
					default:
						cs.setInt(1, Integer.parseInt(row_status));
						break;
					}
					Boolean isresult = cs.execute();
					
					metadata.setCode(1);
					metadata.setMessage("Data kosong.");
					if(isresult) {
						rs = cs.getResultSet();
						
						List<Combobox> comboboxs = new ArrayList<Combobox>();
						switch (servicename) {
							case "xxx":
								Combobox comboboxAll = new Combobox();
								comboboxAll.setKode(0);
								comboboxAll.setNama("--SEMUA--");
								comboboxs.add(comboboxAll);
								break;
							default:
								break;
						}
						while (rs.next()) {
							Combobox combobox = new Combobox();
							switch (servicename) {
							case "periodekamuskompetensilevel":
								combobox.setKode(rs.getInt("kode"));
								combobox.setNama(rs.getString("level"));
								break;
							default:
								combobox.setKode(rs.getInt("kode"));
								combobox.setNama(rs.getString("nama"));
								break;
							}
							comboboxs.add(combobox);
						}
						response.setList(comboboxs);
						metadata.setCode(1);
						metadata.setMessage("Ok.");
						result.setResponse(response);
						rs.close();
					}
					cs.close();
					
				}
			} catch (SQLException e) {
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
