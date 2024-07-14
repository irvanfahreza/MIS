package id.go.bpjskesehatan.inspired.service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
import jakarta.servlet.http.HttpServletResponse;

@Service
public class MISService implements MISManager {
	
	@Override
	public boolean integrasiDataLOB() throws InspiredException {

		Connection sourceConnection = null;
        Connection targetConnection = null;
        PreparedStatement selectStatement = null;
        PreparedStatement insertStatement = null;
        ResultSet resultSet = null;

        try {
            sourceConnection = new Koneksi(DataSource.DBUTAMA).getConnection();

            String selectQuery = "SELECT lob, penyebab_klaim, jumlah_nasabah, beban_klaim FROM klaim_lob";
            selectStatement = sourceConnection.prepareStatement(selectQuery);
            resultSet = selectStatement.executeQuery();

            targetConnection = new Koneksi(DataSource.DBREKAP).getConnection();

            String insertQuery = "INSERT INTO rekap_klaim_lob (LOB, penyebab_klaim, jumlah_nasabah, beban_klaim) VALUES (?, ?, ?, ?)";
            insertStatement = targetConnection.prepareStatement(insertQuery);

            while (resultSet.next()) {
                String lob = resultSet.getString("lob");
                String penyebab = resultSet.getString("penyebab_klaim");
                Integer jumlah_nasabah = resultSet.getInt("jumlah_nasabah");
                Float beban_klaim = resultSet.getFloat("beban_klaim");

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
				if (resultSet != null) {
					try {
						resultSet.close();
					} catch (SQLException e) {
					}
				}
				if (sourceConnection != null) {
					try {
						sourceConnection.close();
					} catch (SQLException e) {
					}
				}
			}
        return true;
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
