package id.go.bpjskesehatan.inspired.controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.naming.NamingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import id.go.bpjskesehatan.inspired.config.Constant;
import id.go.bpjskesehatan.inspired.service.MISService;
import id.go.bpjskesehatan.inspired.util.Result;
import id.go.bpjskesehatan.inspired.util.SharedMethod;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api")
public class MISController {

	@Autowired
	private MISService service;
	
	@PostMapping("rekap")
	public @ResponseBody ResponseEntity<Result<Object>> rekapLOB(){
		try {
			service.integrasiDataLOB();
			return SharedMethod.getResponse(HttpStatus.OK, Constant.STATUS_SUCCESS_MSG);
		} catch (Exception e) {
	    	return SharedMethod.getResponse(HttpStatus.BAD_REQUEST, e.getMessage());
	    }
	}
	
    @GetMapping("export/excel")
    public void exportClaimsToExcel(HttpServletResponse response) throws NamingException {
        try {
            service.exportClaimsToExcel(response);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}
