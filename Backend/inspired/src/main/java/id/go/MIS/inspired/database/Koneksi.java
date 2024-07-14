package id.go.MIS.inspired.database;

import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import id.go.MIS.inspired.config.Config;

public class Koneksi {
	
	private Connection con = null;
	
	public Koneksi(String ds) throws SQLException, NamingException {
		switch (Config.appWebServer) {
		case Wildfly: 
			this.KoneksiJboss(ds);
			break;
		case Tomcat: 
			this.KoneksiTomcat(ds);
			break;
		default:
			throw new IllegalArgumentException("Unexpected value: " + Config.appWebServer);
		}
	}

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

	private void KoneksiTomcat(String CONTEXT) throws SQLException, NamingException {
		DataSource ds;
		try {
			Context initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			ds = (DataSource) envCtx.lookup(CONTEXT);

			con = ds.getConnection();
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw ex;
		} catch (NamingException ex) {
			ex.printStackTrace();
			throw ex;
		}
	}

	private void KoneksiJboss(String CONTEXT) throws SQLException, NamingException {
		InitialContext ctx;
		DataSource ds;
		try {
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
