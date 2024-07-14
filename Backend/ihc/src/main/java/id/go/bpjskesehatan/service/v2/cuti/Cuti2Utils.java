package id.go.bpjskesehatan.service.v2.cuti;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.NamingException;

import id.go.bpjskesehatan.database.Koneksi;
import id.go.bpjskesehatan.entitas.cuti.SaveCuti;
import id.go.bpjskesehatan.entitas.cuti.SaveCutiTgls;

public class Cuti2Utils {
	public static ArrayList<SaveCutiTgls> getCutiTgls(Integer kodecuti) throws SQLException, NamingException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = new Koneksi().getConnection();
			ps = con.prepareStatement("select *, iif(tgl < cast(getdate() as date),0,iif(tgl = cast(getdate() as date) and cast(getdate() as time) > '08:00:00',0,1)) as viewcheck from cuti.cutitgl where kodecuti=?");
			ps.setInt(1, kodecuti);
			rs = ps.executeQuery();
			ArrayList<SaveCutiTgls> saveCutiTgls = new ArrayList<>();
			while (rs.next()) {
				SaveCutiTgls tgls = new SaveCutiTgls();
				tgls.setKode(rs.getInt("kode"));
				tgls.setTgl(rs.getDate("tgl"));
				tgls.setDeleted(0);
				tgls.setBatalkan(false);
				tgls.setStatus(rs.getInt("status"));
				tgls.setViewcheck(rs.getBoolean("viewcheck"));
				saveCutiTgls.add(tgls);
			}
			if(saveCutiTgls.size()>0) {
				return saveCutiTgls;
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
	
	public static SaveCuti getCutiByKode(Integer kodecuti) throws SQLException, NamingException {
		Connection con = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		String query = null;
		try {
			query = "exec cuti.sp_listcuti ?,?,?,?,?";
			con = new Koneksi().getConnection();
			cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			cs.setInt(1, 1);
			cs.setInt(2, 1);
			cs.setInt(3, 0);
			cs.setNull(4, java.sql.Types.VARCHAR);
			cs.setString(5, String.format("kode=%d", kodecuti));
			rs = cs.executeQuery();
			if (rs.next()) {
				SaveCuti cuti = new SaveCuti();
				cuti.setKode(rs.getInt("kode"));
				cuti.setNpp(rs.getString("npp"));
				cuti.setNama(rs.getString("nama"));
				cuti.setKodepenugasan(rs.getInt("kodepenugasan"));
				cuti.setKodetipe(rs.getInt("kodetipe"));
				cuti.setNamatipe(rs.getString("namatipe"));
				cuti.setDengantunjangan(rs.getInt("dengantunjangan"));
				cuti.setTelp(rs.getString("telp"));
				cuti.setAlamatcuti(rs.getString("alamatcuti"));
				cuti.setAlasancuti(rs.getString("alasancuti"));
				cuti.setLampiran(rs.getString("lampiran")==null?"":rs.getString("lampiran"));
				cuti.setGajipokok(rs.getBigDecimal("gajipokok"));
				cuti.setTunjanganjabatan(rs.getBigDecimal("tunjjabatan"));
				cuti.setTunjanganprestasi(rs.getBigDecimal("tupres"));
				cuti.setTunjanganutilitas(rs.getBigDecimal("utilitas"));
				cuti.setTotal(rs.getBigDecimal("total"));
				cuti.setTotal2(rs.getBigDecimal("total2"));
				cuti.setTunjanganbbm((rs.getBigDecimal("nominaltunjanganbbm")==null || rs.getBigDecimal("nominaltunjanganbbm").compareTo(new BigDecimal("0.00"))==0)?0:1);
				cuti.setNominaltunjanganbbm(rs.getBigDecimal("nominaltunjanganbbm"));
				cuti.setPangkat(rs.getString("pangkat"));
				cuti.setGrade(rs.getString("grade"));
				cuti.setNamajobtitle(rs.getString("namajobtitle"));
				cuti.setNamaunitkerja(rs.getString("namaunitkerja"));
				cuti.setTglajukan(rs.getTimestamp("tglajukan"));
				cuti.setStatuspersetujuan(rs.getInt("statuspersetujuan"));
				cuti.setTglmulai(rs.getDate("tglmulai"));
				cuti.setTglselesai(rs.getDate("tglselesai"));
				cuti.setStatusterakhir(rs.getString("statusterakhir"));
				cuti.setJenispengajuan(rs.getInt("jenispengajuan"));
				cuti.setIspembatalan(0);
				cuti.setSelected(false);
				cuti.setVselected(0);
				cuti.setNamaoffice(rs.getString("namaoffice"));
				cuti.setNomor(rs.getString("nomor"));
				cuti.setVerifsdm(rs.getInt("verifsdm"));
				cuti.setVerifkeu(rs.getInt("verifkeu"));
				cuti.setStatusverifsdm(rs.getString("statusverifsdm"));
				cuti.setStatusverifkeu(rs.getString("statusverifkeu"));
				cuti.setAmbilhari(rs.getInt("ambilhari"));
				cuti.setMengetahuidepdirsdm(rs.getString("mengetahuidepdirsdm"));
				cuti.setTglmulai2(rs.getDate("tglmulai2"));
				cuti.setTglselesai2(rs.getDate("tglselesai2"));
				cuti.setTglhpl(rs.getDate("tglhpl"));
				cuti.setBolehverifkeu(rs.getInt("bolehverifkeu"));
				cuti.setCatatantolak(rs.getString("catatantolak"));
				cuti.setTahuninput(rs.getInt("tahuninput"));
				
				cuti.setTgls(getCutiTgls(cuti.getKode()));
				
				return cuti;
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
