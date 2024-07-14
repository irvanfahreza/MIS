package id.go.bpjskesehatan.service.v2.skpd;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.NamingException;

import id.go.bpjskesehatan.database.Koneksi;
import id.go.bpjskesehatan.entitas.karyawan.Pegawai;
import id.go.bpjskesehatan.entitas.karyawan.Penugasan;
import id.go.bpjskesehatan.entitas.organisasi.Office;
import id.go.bpjskesehatan.entitas.referensi.Dati2;
import id.go.bpjskesehatan.entitas.referensi.Propinsi;
import id.go.bpjskesehatan.service.v2.entitas.Akun;
import id.go.bpjskesehatan.service.v2.entitas.MataAnggaran;
import id.go.bpjskesehatan.service.v2.entitas.Program;
import id.go.bpjskesehatan.service.v2.skpd.entitas.Acara;
import id.go.bpjskesehatan.service.v2.skpd.entitas.ListPegawai;
import id.go.bpjskesehatan.service.v2.skpd.entitas.Skpd;
import id.go.bpjskesehatan.skpd.Jeniskendaraan;

public class Skpd2Utils {
	public static ArrayList<ListPegawai> getSkpdPegawai(Integer kodeskpd) throws SQLException, NamingException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = new Koneksi().getConnection();
			ps = con.prepareStatement("select \r\n" + 
					"a.*, c.npp, c.nama\r\n" + 
					"from skpd.skpdpegawai a\r\n" + 
					"inner join karyawan.penugasan b on a.kodepenugasan=b.kode\r\n" + 
					"inner join karyawan.vw_pegawai c on b.npp=c.npp\r\n" + 
					"where a.kodeskpd = ? order by a.kode");
			ps.setInt(1, kodeskpd);
			rs = ps.executeQuery();
			ArrayList<ListPegawai> skpdpegawais = new ArrayList<>();
			Integer no = 0;
			while (rs.next()) {
				no++;
				ListPegawai skpdpegawai = new ListPegawai();
				skpdpegawai.setNo(no);
				skpdpegawai.setKode(rs.getInt("kode"));
				skpdpegawai.setKodepenugasan(rs.getInt("kodepenugasan"));
				skpdpegawai.setNpp(rs.getString("npp"));
				skpdpegawai.setNama(rs.getString("nama"));
				skpdpegawai.setDeleted(0);
				skpdpegawais.add(skpdpegawai);
			}
			if(skpdpegawais.size()>0) {
				return skpdpegawais;
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
	
	public static ArrayList<MataAnggaran> getSkpdMataAnggaran(Integer kodeskpd) throws SQLException, NamingException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = new Koneksi().getConnection();
			ps = con.prepareStatement("select a.kode, KDPROG, nmprog, KDAKUN, nmakun \r\n" + 
					"from skpd.skpdmataanggaran a \r\n" + 
					"outer apply \r\n" + 
					"( \r\n" + 
					"	select top 1 ga.KDAKUN, ga.nmakun from referensi.vw_kodeakun ga where ga.KDAKUN=a.kodeakun \r\n" + 
					") b \r\n" + 
					"outer apply \r\n" + 
					"( \r\n" + 
					"	select top 1 ha.KDPROG, ha.nmprog from referensi.vw_kodeprogram ha where ha.KDPROG=a.kodeprogram \r\n" + 
					") c where a.kodeskpd=?");
			ps.setInt(1, kodeskpd);
			rs = ps.executeQuery();
			ArrayList<MataAnggaran> mataAnggarans = new ArrayList<>();
			Integer no = 0;
			while (rs.next()) {
				no++;
				MataAnggaran mataAnggaran = new MataAnggaran();
				mataAnggaran.setNo(no);
				mataAnggaran.setKode(rs.getInt("kode"));
				Program program2 = new Program();
				program2.setKode(rs.getString("KDPROG"));
				program2.setNama(rs.getString("nmprog"));
				Akun akun2 = new Akun();
				akun2.setKode(rs.getString("KDAKUN"));
				akun2.setNama(rs.getString("nmakun"));
				mataAnggaran.setProgram(program2);
				mataAnggaran.setAkun(akun2);
				mataAnggaran.setDeleted(0);
				mataAnggarans.add(mataAnggaran);
			}
			if(mataAnggarans.size()>0) {
				return mataAnggarans;
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
	
	public static Skpd getSkpdByKode(Integer kodeskpd) throws SQLException, NamingException {
		Connection con = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		String query = null;
		try {
			query = "exec skpd.sp_listskpdall ?, ?, ?, ?, ?";
			con = new Koneksi().getConnection();
			cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			cs.setInt(1, 1);
			cs.setInt(2, 1);
			cs.setInt(3, 0);
			cs.setNull(4, java.sql.Types.VARCHAR);
			cs.setString(5, String.format("kode=%d", kodeskpd));
			rs = cs.executeQuery();
			if (rs.next()) {
				Skpd skpd = new Skpd();
				skpd.setKode(rs.getInt("kode"));
				skpd.setNomor(rs.getString("nomor"));
				skpd.setTujuan(rs.getInt("tujuan"));
				skpd.setJeniskegiatan(rs.getInt("jeniskegiatan"));
				
				skpd.setLaporan_catatan_ringkas(rs.getString("laporan_catatan_ringkas"));
				skpd.setLaporan_tindak_lanjut(rs.getString("laporan_tindak_lanjut"));
				skpd.setLaporan_catatan_atasan(rs.getString("laporan_catatan_atasan"));
				
				Office office = new Office();
				office.setKode(rs.getString("kodeoffice"));
				office.setNama(rs.getString("namaoffice"));
				skpd.setOffice(office);
				
				Acara acara = new Acara();
				acara.setKode(rs.getInt("kodeacara"));
				acara.setNama(rs.getString("namaacara"));
				acara.setTujuan(rs.getInt("tujuan"));
				acara.setTempat(rs.getString("tempat"));
				
				Office officetujuan = new Office();
				officetujuan.setKode(rs.getString("kodeofficetujuan"));
				officetujuan.setNama(rs.getString("namaofficetujuan"));
				acara.setOfficetujuan(officetujuan);
				
				Dati2 dati2tujuan = new Dati2();
				dati2tujuan.setKode(rs.getString("kodedati2tujuan"));
				dati2tujuan.setNama(rs.getString("namadati2tujuan"));
				Propinsi propinsi = new Propinsi();
				propinsi.setKode(rs.getString("kodepropinsitujuan"));
				propinsi.setNama(rs.getString("namapropinsitujuan"));
				dati2tujuan.setPropinsi(propinsi);
				acara.setDati2tujuan(dati2tujuan);
				
				skpd.setAcara(acara);
				
				skpd.setTglmulai(rs.getDate("tglmulai"));
				skpd.setTglselesai(rs.getDate("tglselesai"));
				skpd.setDeskripsi(rs.getString("deskripsi"));
				skpd.setKeperluan(rs.getString("keperluan"));
				
				Akun akun = new Akun();
				akun.setKode(rs.getString("kodeakun"));
				akun.setNama(rs.getString("namaakun"));
				skpd.setAkun(akun);
				
				Program program = new Program();
				program.setKode(rs.getString("kodeprogram"));
				program.setNama(rs.getString("namaprogram"));
				skpd.setProgram(program);
				
				Jeniskendaraan jeniskendaraan = new Jeniskendaraan();
				jeniskendaraan.setKode(rs.getInt("kodejeniskendaraan"));
				jeniskendaraan.setNama(rs.getString("namajeniskendaraan"));
				skpd.setJeniskendaraan(jeniskendaraan);
				
				skpd.setLampiran(rs.getString("lampiran")==null?"":rs.getString("lampiran"));
				skpd.setStatus(rs.getInt("status"));
				skpd.setNamastatus(rs.getString("namastatus"));
				
				Penugasan penugasan = new Penugasan();
				penugasan.setKode(rs.getInt("pembuat"));
				Pegawai pegawai = new Pegawai();
				pegawai.setNpp(rs.getString("npppembuat"));
				pegawai.setNama(rs.getString("namapembuat"));
				penugasan.setPegawai(pegawai);
				skpd.setPembuat(penugasan);
				
				skpd.setPegawai(getSkpdPegawai(skpd.getKode()));
				skpd.setMataanggaran(getSkpdMataAnggaran(skpd.getKode()));
				
				return skpd;
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
