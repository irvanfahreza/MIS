/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.go.bpjskesehatan.database;

import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 *
 * @author bambangpurwanto
 */
public class Koneksi {

	private Connection con = null;

	public Koneksi() throws SQLException, NamingException {
		this.KoneksiJboss();
	}

	// public void BuatKoneksi() throws SQLException, NamingException {

	// this.KoneksiTomcat();
	// this.KoneksiJboss();

	// }

	public Connection getConnection() {
		return con;
	}

	public void closeConnection() {
		if (con != null) {
			try {
				con.close();
			} catch (SQLException ex) {
			}
			con = null;
		}
	}

	@SuppressWarnings("unused")
	private void KoneksiTomcat() throws SQLException, NamingException {
		DataSource ds;
		String CONTEXT;
		try {
			CONTEXT = "jdbc/hcis";

			Context initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			ds = (DataSource) envCtx.lookup(CONTEXT);

			con = ds.getConnection();
		} catch (SQLException ex) {

			throw ex;
		} catch (NamingException ex) {

			throw ex;
		}
	}

	private void KoneksiJboss() throws SQLException, NamingException {
		InitialContext ctx;
		DataSource ds;
		String CONTEXT;
		try {
			CONTEXT = "jdbc/hcis";

			ctx = new InitialContext();
			ds = (DataSource) ctx.lookup(CONTEXT);

			con = ds.getConnection();
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw ex;
		} catch (NamingException ex) {
			ex.printStackTrace();
			throw ex;
		}
	}

}
