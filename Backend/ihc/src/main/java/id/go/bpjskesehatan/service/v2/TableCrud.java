package id.go.bpjskesehatan.service.v2;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import javax.naming.NamingException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import id.go.bpjskesehatan.database.Koneksi;
import id.go.bpjskesehatan.service.v2.entitas.GeneratedKey;
import id.go.bpjskesehatan.util.SharedMethod;

public class TableCrud {
	
	public static GeneratedKey insertData(String namatabel, String jsonParam) throws SQLException, NamingException, JsonProcessingException, IOException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		GeneratedKey generatedKey = null;
		
		try {
			con = new Koneksi().getConnection();
			String query = null;
			query = SharedMethod.createQuery(jsonParam, namatabel);
			ps = con.prepareStatement(query, new String[] { "kode" });
			SharedMethod.setParameterCreateQuery(ps, jsonParam);
			ps.execute();
			rs = ps.getResultSet();
			if (rs == null)
				rs = ps.getGeneratedKeys();
			if (rs.next()) {
				if(rs.getString(1)!=null) {
					generatedKey = new GeneratedKey();
					generatedKey.setKode(rs.getString(1));
				}
			}
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
		return generatedKey;
	}
	
	public static void updateData(String namatabel, String jsonParam) throws SQLException, NamingException, JsonProcessingException, IOException {
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = new Koneksi().getConnection();
			String query;
			query = SharedMethod.updateQuery(jsonParam, namatabel);
			ps = con.prepareStatement(query);
			SharedMethod.setParameterEditQuery(ps, jsonParam);
			ps.executeUpdate();
		} finally {
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
	
	public static void deleteData(String namatabel, String jsonParam) throws SQLException, NamingException, JsonProcessingException, IOException {
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = new Koneksi().getConnection();
			String query;
			query = SharedMethod.deleteQuery(jsonParam, namatabel);
			ps = con.prepareStatement(query);
			SharedMethod.setParameterDeleteQuery(ps, jsonParam);
			ps.executeUpdate();
		} finally {
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
	
	public static String setJsonStringParam(String jsonStringSumber, String jsonStringReplace) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
	        Map<String, Object> mapJson = objectMapper.readValue(jsonStringSumber,new TypeReference<Map<String,Object>>(){});
	        Map<String, Object> mapTargetJson = objectMapper.readValue(jsonStringReplace,new TypeReference<Map<String,Object>>(){});

	        /*for (Map.Entry<String,Object> keyVal: mapTargetJson.entrySet()) {
	            mapJson.replace(keyVal.getKey(),keyVal.getValue());
	        }*/
	        
	        for (Map.Entry<String,Object> keyVal: mapTargetJson.entrySet()) {
	        	mapJson.put(keyVal.getKey(),keyVal.getValue());
	        }
	        jsonStringSumber = objectMapper.writeValueAsString(mapJson);
		} catch (Exception e) {
			e.printStackTrace();
		}        
        return jsonStringSumber;
	}
	
}
