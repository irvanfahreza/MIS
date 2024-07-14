package id.go.bpjskesehatan.service.v2.lembur;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import javax.naming.NamingException;

import id.go.bpjskesehatan.database.Koneksi;
import id.go.bpjskesehatan.entitas.karyawan.Pegawai;
import id.go.bpjskesehatan.entitas.karyawan.Penugasan;
import id.go.bpjskesehatan.entitas.organisasi.Office;
import id.go.bpjskesehatan.service.v2.entitas.Akun;
import id.go.bpjskesehatan.service.v2.entitas.Program;
import id.go.bpjskesehatan.service.v2.lembur.entitas.Lembur;
import id.go.bpjskesehatan.service.v2.lembur.entitas.ListPegawai;
import id.go.bpjskesehatan.service.v2.lembur.entitas.SaveLemburTgls;

public class Lembur2Utils {
	public static ArrayList<ListPegawai> getLemburPegawai(Integer kodelembur) throws SQLException, NamingException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = new Koneksi().getConnection();
			Integer tahun = Calendar.getInstance().get(Calendar.YEAR);
			ps = con.prepareStatement("select \r\n" + 
					"a.*, c.npp, c.nama, d.totaljam \r\n" + 
					"from lembur.pegawai a \r\n" + 
					"inner join karyawan.penugasan b on a.kodepenugasan=b.kode \r\n" + 
					"inner join karyawan.vw_pegawai c on b.npp=c.npp \r\n" + 
					"left join (select * from lembur.vw_totaljam where tahun=?) d on b.npp=d.npp \r\n" + 
					"where a.kodelembur = ?");
			ps.setInt(1, tahun);
			ps.setInt(2, kodelembur);
			rs = ps.executeQuery();
			ArrayList<ListPegawai> lemburpegawais = new ArrayList<>();
			while (rs.next()) {
				ListPegawai lemburpegawai = new ListPegawai();
				lemburpegawai.setKode(rs.getInt("kode"));
				lemburpegawai.setKodepenugasan(rs.getInt("kodepenugasan"));
				lemburpegawai.setNpp(rs.getString("npp"));
				lemburpegawai.setNama(rs.getString("nama"));
				lemburpegawai.setDeleted(0);
				lemburpegawai.setTotaljam(rs.getInt("totaljam"));
				lemburpegawais.add(lemburpegawai);
			}
			if(lemburpegawais.size()>0) {
				return lemburpegawais;
			}
		} 
		finally {
			if (rs != null) {
				 try {
					 rs.close();
				 } catch (SQLException e1) {
				 }
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e1) {
				}
			}

			if (con != null) {
				try {
					con.close();
				} catch (SQLException e1) {
				}
			}
		}
		return null;
	}
	
	public static ArrayList<SaveLemburTgls> getLemburTgls(Integer kodelembur) throws SQLException, NamingException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = new Koneksi().getConnection();
			ps = con.prepareStatement("select *, iif(tgl < cast(getdate() as date),0,iif(tgl = cast(getdate() as date) and cast(getdate() as time) > '08:00:00',0,1)) as viewcheck from lembur.lemburtgl where kodelembur=?");
			ps.setInt(1, kodelembur);
			rs = ps.executeQuery();
			ArrayList<SaveLemburTgls> saveLemburTgls = new ArrayList<>();
			while (rs.next()) {
				SaveLemburTgls tgls = new SaveLemburTgls();
				tgls.setKode(rs.getInt("kode"));
				tgls.setTgl(rs.getDate("tgl"));
				tgls.setDeleted(0);
				tgls.setBatalkan(false);
				tgls.setStatus(rs.getInt("status"));
				tgls.setViewcheck(rs.getBoolean("viewcheck"));
				saveLemburTgls.add(tgls);
			}
			if(saveLemburTgls.size()>0) {
				return saveLemburTgls;
			}
		} 
		finally {
			if (rs != null) {
				 try {
					 rs.close();
				 } catch (SQLException e1) {
				 }
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e1) {
				}
			}

			if (con != null) {
				try {
					con.close();
				} catch (SQLException e1) {
				}
			}
		}
		return null;
	}
	
	public static Lembur getLemburByKode(Integer kodelembur) throws SQLException, NamingException {
		Connection con = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		String query = null;
		try {
			query = "exec lembur.sp_listlemburall ?, ?, ?, ?, ?";
			con = new Koneksi().getConnection();
			cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			cs.setInt(1, 1);
			cs.setInt(2, 1);
			cs.setInt(3, 0);
			cs.setNull(4, java.sql.Types.VARCHAR);
			cs.setString(5, String.format("kode=%d", kodelembur));
			rs = cs.executeQuery();
			if (rs.next()) {
				Lembur lembur = new Lembur();
				lembur.setKode(rs.getInt("kode"));
				lembur.setNomor(rs.getString("nomor"));
				lembur.setLaporan_isi(rs.getString("laporan_isi"));
				
				Office office = new Office();
				office.setKode(rs.getString("kodeoffice"));
				office.setNama(rs.getString("namaoffice"));
				lembur.setOffice(office);
				
				Akun akun = new Akun();
				akun.setKode(rs.getString("kodeakun"));
				akun.setNama(rs.getString("namaakun"));
				lembur.setAkun(akun);
				
				Program program = new Program();
				program.setKode(rs.getString("kodeprogram"));
				program.setNama(rs.getString("namaprogram"));
				lembur.setProgram(program);
				 
				lembur.setLampiran(rs.getString("lampiran")==null?"":rs.getString("lampiran"));
				lembur.setStatus(rs.getInt("status"));
				lembur.setNamastatus(rs.getString("namastatus"));
				
				lembur.setTgllembur(rs.getString("tgllembur"));
				lembur.setPegawailembur(rs.getString("pegawailembur"));
				
				lembur.setDeskripsi(rs.getString("deskripsi"));
				lembur.setNamakegiatan(rs.getString("namakegiatan"));
				lembur.setCatatantolak(rs.getString("catatantolak"));
				
				Penugasan penugasan = new Penugasan();
				penugasan.setKode(rs.getInt("pembuat"));
				
				Pegawai pegawai = new Pegawai();
				pegawai.setNpp(rs.getString("npppembuat"));
				pegawai.setNama(rs.getString("namapembuat"));
				
				penugasan.setPegawai(pegawai);
				lembur.setPembuat(penugasan);
				
				lembur.setPegawai(getLemburPegawai(lembur.getKode()));
				
				lembur.setTgls(getLemburTgls(lembur.getKode()));
				
				return lembur;
			}
		} 
		finally {
			if (rs != null) {
				 try {
					 rs.close();
				 } catch (SQLException e1) {
				 }
			}
			if (cs != null) {
				try {
					cs.close();
				} catch (SQLException e1) {
				}
			}

			if (con != null) {
				try {
					con.close();
				} catch (SQLException e1) {
				}
			}
		}
		return null;
	}
}
