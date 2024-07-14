package id.go.MIS.inspired.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.naming.NamingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import id.go.MIS.inspired.config.Constant;
import id.go.MIS.inspired.model.klaim_lob;
import id.go.MIS.inspired.service.MISService;
import id.go.MIS.inspired.util.Result;
import id.go.MIS.inspired.util.SharedMethod;
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
	
	@GetMapping("list")
	public @ResponseBody ResponseEntity<Result<klaim_lob>> listLOB(
			){
		try {
			List<klaim_lob> list = service.listLOB();
			return SharedMethod.getResponse(HttpStatus.OK, Constant.STATUS_SUCCESS_MSG, list);
		} catch (Exception e) {
        	return SharedMethod.getResponse(HttpStatus.BAD_REQUEST, e.getMessage(), null);
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
