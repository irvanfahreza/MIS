package id.go.bpjskesehatan.inspired.service;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import id.go.bpjskesehatan.inspired.common.InspiredException;
import id.go.bpjskesehatan.inspired.config.DataSource;
import id.go.bpjskesehatan.inspired.database.Koneksi;
import id.go.bpjskesehatan.inspired.manager.MISManager;
import id.go.bpjskesehatan.inspired.model.klaim_lob;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class MISService implements MISManager {
	
	@Override
	public boolean integrasiDataLOB() throws InspiredException {

		Connection conUtama = null;
        Connection conRekap = null;
        PreparedStatement selectStatement = null;
        PreparedStatement insertStatement = null;
        ResultSet rs = null;

        try {
        	conUtama = new Koneksi(DataSource.DBUTAMA).getConnection();

            String selectQuery = "SELECT lob, penyebab_klaim, jumlah_nasabah, beban_klaim FROM klaim_lob";
            selectStatement = conUtama.prepareStatement(selectQuery);
            rs = selectStatement.executeQuery();

            conRekap = new Koneksi(DataSource.DBREKAP).getConnection();

            String insertQuery = "INSERT INTO rekap_klaim_lob (LOB, penyebab_klaim, jumlah_nasabah, beban_klaim) VALUES (?, ?, ?, ?)";
            insertStatement = conRekap.prepareStatement(insertQuery);

            while (rs.next()) {
                String lob = rs.getString("lob");
                String penyebab = rs.getString("penyebab_klaim");
                Integer jumlah_nasabah = rs.getInt("jumlah_nasabah");
                Float beban_klaim = rs.getFloat("beban_klaim");

                if ("KUR".equalsIgnoreCase(lob) || "PEN".equalsIgnoreCase(lob) || "KUR".equalsIgnoreCase(lob)) {
                    insertStatement.setString(1, lob);
                    insertStatement.setString(2, penyebab);
                    insertStatement.setInt(3, jumlah_nasabah);
                    insertStatement.setFloat(4, beban_klaim);

                    insertStatement.executeUpdate();
                	}
            	}
        	}
            catch (SQLException e) {
				throw new InspiredException(e.getMessage());
			} catch (Exception e) {
				throw new InspiredException(e.getMessage());
			}
			finally {
				if (rs != null) {
					try {
						rs.close();
					} catch (SQLException e) {
					}
				}
				if (conUtama != null) {
					try {
						conUtama.close();
					} catch (SQLException e) {
					}
				}
			}
        return true;
	}
	
	@Override
	public List<klaim_lob> listLOB() throws InspiredException {
		Connection con = null;
        PreparedStatement selectStatement = null;
        ResultSet rs = null;
        CallableStatement cs = null;
        
        try {
        	con = new Koneksi(DataSource.DBUTAMA).getConnection();
        	String selectQuery = "SELECT Id, lob, penyebab_klaim, jumlah_nasabah, beban_klaim FROM klaim_lob";
        	cs = con.prepareCall(selectQuery);
			rs =  cs.executeQuery();
			
			List<klaim_lob> rows = new ArrayList<>();
			while(rs.next()) {
				klaim_lob row = new klaim_lob();
				row.setId(rs.getInt("Id"));
				row.setLob(rs.getString("LOB"));
				row.setPenyebab_klaim(rs.getString("penyebab_klaim"));
				row.setJumlah_nasabah(rs.getInt("jumlah_nasabah"));
				row.setBeban_klaim(rs.getFloat("beban_klaim"));
				rows.add(row);
			}
			if(rows.size()>0) {
				return rows;
			}
        			
        }
        catch (SQLException e) {
			throw new InspiredException(e.getMessage());
		} catch (Exception e) {
			throw new InspiredException(e.getMessage());
		}
		finally {
			if (rs != null) {
				try {
					rs.close();
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
        return null;
	}
	
	public void exportClaimsToExcel(HttpServletResponse response) throws SQLException, IOException, NamingException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=claims.xlsx";
        response.setHeader(headerKey, headerValue);

        Connection sourceConnection = null;
        PreparedStatement selectStatement = null;
        ResultSet resultSet = null;

        try {
            sourceConnection = new Koneksi(DataSource.DBUTAMA).getConnection();
            String selectQuery = "SELECT lob, penyebab_klaim, jumlah_nasabah, beban_klaim FROM klaim_lob";
            selectStatement = sourceConnection.prepareStatement(selectQuery);
            resultSet = selectStatement.executeQuery();

            XSSFWorkbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Claims");

            // Create header row
            Row headerRow = sheet.createRow(0);
            Cell headerCell1 = headerRow.createCell(0);
            headerCell1.setCellValue("LOB");
            Cell headerCell2 = headerRow.createCell(1);
            headerCell2.setCellValue("Penyebab Klaim");
            Cell headerCell3 = headerRow.createCell(2);
            headerCell3.setCellValue("Jumlah Nasabah");
            Cell headerCell4 = headerRow.createCell(3);
            headerCell4.setCellValue("Beban Klaim");

            // Create data rows
            int rowNum = 1;
            while (resultSet.next()) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(resultSet.getString("lob"));
                row.createCell(1).setCellValue(resultSet.getString("penyebab_klaim"));
                row.createCell(2).setCellValue(resultSet.getInt("jumlah_nasabah"));
                row.createCell(3).setCellValue(resultSet.getFloat("beban_klaim"));
            }

            workbook.write(response.getOutputStream());
            workbook.close();
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (sourceConnection != null) {
                sourceConnection.close();
            }
        }
    }

}
