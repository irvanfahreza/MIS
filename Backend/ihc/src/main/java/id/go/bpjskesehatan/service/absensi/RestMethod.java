package id.go.bpjskesehatan.service.absensi;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.NamingException;

import com.fasterxml.jackson.core.JsonProcessingException;

import id.go.bpjskesehatan.database.KoneksiAbsensi;
import id.go.bpjskesehatan.entitas.Metadata;
import id.go.bpjskesehatan.util.SharedMethod;

public class RestMethod {

	public static Metadata createData(String data, String namatabel) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Metadata metadata = new Metadata();
		//if (Auth.ServiceAuth(headers, metadata)) {
			try {
				con = new KoneksiAbsensi().getConnection();
				String query;
				query = SharedMethod.createQuery(data, namatabel);

				ps = con.prepareStatement(query, new String[] { "kode" });
				SharedMethod.setParameterCreateQuery(ps, data);
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
				metadata.setMessage("Create gagal.");
				e.printStackTrace();
			} catch (NamingException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
				//e.printStackTrace();
			} catch (NumberFormatException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
//				e.printStackTrace();
			} catch (JsonProcessingException e) {
				metadata.setCode(0);
				metadata.setMessage(e.getMessage());
//				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
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

		//}
		//result.setMetadata(metadata);
		return metadata;

	}

	public static Metadata updateData(String data, String namatabel) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Metadata metadata = new Metadata();
		//if (Auth.ServiceAuth(headers, metadata, "")) {
			try {
				con = new KoneksiAbsensi().getConnection();
				String query;
				query = SharedMethod.updateQuery(data, namatabel);
				ps = con.prepareStatement(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);

				SharedMethod.setParameterEditQuery(ps, data);

				ps.executeUpdate();
				metadata.setCode(1);
				metadata.setMessage("Data berhasil diupdate");
			} catch (SQLException e) {
				metadata.setCode(0);
				metadata.setMessage("Update gagal.");
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

		//}
		//result.setMetadata(metadata);
		//return Response.ok(result).build();
			return metadata;
	}

	public static Metadata deleteData(String data, String namaTabel) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Metadata metadata = new Metadata();
		//if (Auth.ServiceAuth(headers, metadata, "")) {
			try {
				con = new KoneksiAbsensi().getConnection();
				String query;
				query = SharedMethod.deleteQuery(data, namaTabel);

				ps = con.prepareStatement(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);

				SharedMethod.setParameterDeleteQuery(ps, data);

				ps.executeUpdate();
				metadata.setCode(1);
				metadata.setMessage("Data berhasil dihapus");

			} catch (SQLException e) {
				metadata.setCode(0);
				metadata.setMessage("Hapus gagal.");
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

		//}
		//result.setMetadata(metadata);
		//return Response.ok(result).build();
			return metadata;
	}
	
	

}
