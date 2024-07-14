package id.go.MIS.inspired.config;

public class Constant {
	
	public enum APP_WEBSERVER {
	    Wildfly,
	    Tomcat
	}
	
	public enum APP_ENVIRONMENT {
	    DEV,
	    QC,
	    PROD
	}
	
	public static final String STATUS_SUCCESS_MSG = "Ok";
	public static final String STATUS_ERROR_MSG = "Transaksi belum dapat diproses. Silahkan coba beberapa saat lagi";
	
	public static final int STATUS_ERROR_CODE = 0;
	public static final int STATUS_SUCCESS_CODE = 1;
	public static final int STATUS_OK_CODE = 200;
	public static final int STATUS_UNAUTHORIZED_CODE = 401;
	public static final int STATUS_FORBIDDEN_CODE = 403;
}