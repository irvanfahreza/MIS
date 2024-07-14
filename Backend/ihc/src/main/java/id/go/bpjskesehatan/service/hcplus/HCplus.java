package id.go.bpjskesehatan.service.hcplus;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.ObjectMapper;

import id.go.bpjskesehatan.entitas.Metadata;
import id.go.bpjskesehatan.entitas.Respon;
import id.go.bpjskesehatan.entitas.Result;
import id.go.bpjskesehatan.service.hcplus.request.AddJobEmployee;
import id.go.bpjskesehatan.service.hcplus.response.Employee;
import id.go.bpjskesehatan.service.hcplus.response.JobPlan;
import id.go.bpjskesehatan.service.hcplus.response.JobTitle;
import id.go.bpjskesehatan.service.hcplus.response.ResultBase;
import id.go.bpjskesehatan.service.hcplus.response.ResultData;
import id.go.bpjskesehatan.service.hcplus.response.ResultList;

@Path("/hcplus")
public class HCplus {
	
	@Context
	private ServletContext context;
	
	private String getToken() throws Exception {
		
		try {
			String BASEURL = context.getInitParameter("hcplus.baseurl");
			String username = context.getInitParameter("hcplus.user");
			String pass = context.getInitParameter("hcplus.pass");
			
			Form form = new Form();
	        form
	        .param("username", username)
	        .param("password", pass);
	        
	        Response response = null;
	        try {
	        	response = Invoker.Post(BASEURL + "/default/api/loginapps", Invoker.getHeaders(), form);
		        GenericType<ResultData<Employee>> genericType = new GenericType<ResultData<Employee>>() {};
		        ResultData<Employee> emResult = response.readEntity(genericType);
		        if(response.getStatus()==200) {
		        	if(emResult.getStatus()==1) {
		        		return emResult.getData().getToken();
		        	}
		        	else {
		        		throw new Exception(emResult.getMessage());
		        	}
		        }
		        else {
		        	throw new Exception("Server error");
		        }
	        }
	        finally {
	        	if (response != null)
	        		response.close();
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		
	}
	
	private void updateJobEmployee(Integer id) throws Exception {
		
		try {
			Form form = new Form();
	        form
	        .param("oper", "edit")
	        .param("id", id.toString());
	        
	        String token = getToken();
	        String BASEURL = context.getInitParameter("hcplus.baseurl");
	        Response response = null;
	        try {
	        	response = Invoker.Post(BASEURL + "/organization/jobemployee/updatejobemployee?token=" + token, Invoker.getHeaders(), form);
		        ResultBase emResult = response.readEntity(ResultBase.class);
		        if(response.getStatus()==200) {
		        	if(emResult.getStatus()==1) {
		        	}
		        	else {
		        		throw new Exception(emResult.getMessage());
		        	}
		        }
		        else {
		        	throw new Exception("Server error");
		        }
	        }
	        finally {
	        	if (response != null)
	        		response.close();
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		
	}

	@POST
	@Path("/pegawai/tambahpenugasan")
	@Produces("application/json")
	@Consumes("application/json")
	public Response addJobEmployee(@Context HttpHeaders headers, String data) {
		
		Metadata metadata = new Metadata();
		Result<ResultBase> result = new Result<ResultBase>();
		
		try {
			ObjectMapper mapper = new ObjectMapper();
			AddJobEmployee post = mapper.readValue(data, AddJobEmployee.class);
			
			Form form = new Form();
	        form
	        .param("jobpositioncode", post.getKodejobtitle())
	        .param("employeeCode", post.getNpp())
	        .param("jobGradeCategoryCode", post.getKodegrade())
	        .param("jobgradeCode", post.getKodesubgrade())
	        .param("decreeTypeName", post.getNamajenissk())
	        .param("employmentTypeName", post.getNamastatuskaryawan())
	        .param("employmentStateName", post.getNamastatusjabatan())
	        .param("TGLSK", post.getTanggalsk())
	        .param("jobEmployeeStartDate", post.getTanggalmulai())
	        .param("NOSK", post.getNomorsk());
	        
	        String token = getToken();
	        
	        String BASEURL = context.getInitParameter("hcplus.baseurl");
	        Response response = null;
	        try {
	        	response = Invoker.Post(BASEURL + "/organization/jobemployee/addjobemployee?token="+token, Invoker.getHeaders(), form);
		        ResultBase emResult = response.readEntity(ResultBase.class);
		        if(response.getStatus()==200) {
		        	if(emResult.getStatus()==1) {
		        		metadata.setCode(1);
		    			metadata.setMessage(emResult.getMessage());
		        	}
		        	else {
		        		throw new Exception(emResult.getMessage());
		        	}
		        }
		        else {
		        	throw new Exception("Server error");
		        }
			}
	        finally {
	        	if (response != null)
	        		response.close();
			}
			
		} catch (Exception e) {
			metadata.setCode(0);
			metadata.setMessage(e.getMessage());
			e.printStackTrace();
		}
		
		result.setMetadata(metadata);
		return Response.ok(result).build();
	}
	
	@GET
	@Path("/karir/getjobtitle/{search}/{val}")
	@Produces("application/json")
	@Consumes("application/json")
	public Response getJobTitle(@Context HttpHeaders headers, @PathParam("search") String search, @PathParam("val") String val) {
		
		Metadata metadata = new Metadata();
		Result<JobTitle> result = new Result<JobTitle>();
		Respon<JobTitle> respon = null;
		
		try {
			switch (search) {
			case "searchval":
			case "equalsearchval":
				break;
			default:
				return Response.status(404).build();
			}
			
			String token = getToken();
	        
			String BASEURL = context.getInitParameter("hcplus.baseurl");
	        Response response = null;
		    try { 
		    	response = Invoker.Get(BASEURL + "/career/replacementchart/recordjobposition/?token=" + token + "&" + search + "=" + val, Invoker.getHeaders());
	        	GenericType<ResultList<JobTitle>> genericType = new GenericType<ResultList<JobTitle>>() {};
		        ResultList<JobTitle> resultList = response.readEntity(genericType);
		        if(response.getStatus()==200) {
		        	if(resultList.getStatus()==1) {
		        		metadata.setCode(1);
		    			metadata.setMessage("Ok");
		    			
		    			respon = new Respon<JobTitle>();
		    			respon.setList(resultList.getData());
		    			result.setResponse(respon);
		        	}
		        	else {
		        		throw new Exception(resultList.getMessage());
		        	}
		        }
		        else {
		        	throw new Exception("Server error");
		        }
			}
	        finally {
	        	if (response != null)
	        		response.close();
			}
			
		} catch (Exception e) {
			metadata.setCode(0);
			metadata.setMessage(e.getMessage());
			e.printStackTrace();
		}
		
		result.setMetadata(metadata);
		return Response.ok(result).build();
	}
	
	@GET
	@Path("/karir/getjobplan/jobtitleid/{jobtitleid}/limit/{limit}/npp/{npp: .*}")
	@Produces("application/json")
	@Consumes("application/json")
	public Response getJobPlan(
			@Context HttpHeaders headers, 
			@PathParam("jobtitleid") Integer jobtitleid,
			@PathParam("limit") Integer limit,
			@PathParam("npp") String npp) {
		
		Metadata metadata = new Metadata();
		Result<JobPlan> result = new Result<JobPlan>();
		Respon<JobPlan> respon = null;
		
		try {
			String token = getToken();
			String BASEURL = context.getInitParameter("hcplus.baseurl");
	        Response response = null;
	        try {
	        	String url = null;
	        	if(npp.isEmpty()) {
	        		url = BASEURL + "/career/replacementchart/recordplan/careerOtherParameterDetailId/?token=" + token + "&jobPositionId=" + jobtitleid + "&layer=1&recalculate=0&limit=" + limit + "&sidx=totalScore&sord=desc";
	        	}
	        	else {
	        		url = BASEURL + "/career/replacementchart/recordplan/careerOtherParameterDetailId/?token=" + token + "&jobPositionId=" + jobtitleid + "&layer=1&recalculate=0&equalEmployeeCode=" + npp;
	        	}
	        	System.out.println("URl="+url);
	        	response = Invoker.Get(url, Invoker.getHeaders());
	        	GenericType<ResultList<JobPlan>> genericType = new GenericType<ResultList<JobPlan>>() {};
	        	
	        	/*BufferedReader br = new BufferedReader(new InputStreamReader(response.readEntity(InputStream.class)));
				String output;
				StringBuilder builder = new StringBuilder();
				while ((output = br.readLine()) != null) {
					builder.append(output);
				}
				String respon1 = builder.toString();
				System.out.println("RESPON->"+res);*/
	        	
		        ResultList<JobPlan> resultList = response.readEntity(genericType);
		        if(response.getStatus()==200) {
		        	if(resultList.getStatus()==1) {
		        		metadata.setCode(1);
		    			metadata.setMessage("Ok");
		    			
		    			respon = new Respon<JobPlan>();
		    			respon.setList(resultList.getData());
		    			result.setResponse(respon);
		        	}
		        	else {
		        		throw new Exception(resultList.getMessage());
		        	}
		        }
		        else {
		        	throw new Exception("Server error");
		        }
			}
	        finally {
	        	if (response != null)
	        		response.close();
			}
			
		} catch (Exception e) {
			metadata.setCode(0);
			metadata.setMessage(e.getMessage());
			e.printStackTrace();
		}
		
		result.setMetadata(metadata);
		return Response.ok(result).build();
	}
}
