package id.go.MIS.inspired.common;

import java.security.GeneralSecurityException;

import id.go.MIS.inspired.config.Constant;

public class InspiredException extends GeneralSecurityException {
	
	private static final long serialVersionUID = 1L;
	private final String code;

    public InspiredException(String message, Throwable cause, String code){
        super(message, cause);
        this.code = code;
    }

    public InspiredException(String message){
        super(message);
        this.code = Constant.STATUS_ERROR_MSG;
    }

    public InspiredException(String message, String code){
        super(message);
        this.code = code;
    }

    public InspiredException(Throwable cause, String code){
        super(cause);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}