package id.go.bpjskesehatan.service;

import java.lang.reflect.Method;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.go.bpjskesehatan.database.Koneksi;
import id.go.bpjskesehatan.util.SharedMethod;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;


/**
 * @author Bambang Purwanto
 *
 */
@Path("info")
public class InfoRest {

	@GET
	@Path("/service")
	@Produces("application/json")
	public Response getServiceInfo(@Context HttpHeaders headers) {
		List<Class<?>> classes = new ArrayList<Class<?>>();
		classes.add(OrganisasiRest.class);
		classes.add(ReferensiRest.class);
		classes.add(Sso.class);
		Map<String, Object> map = null;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if (SharedMethod.ServiceAuth(headers, map)) {
			for (Class<?> klass : classes) {
				map = new HashMap<String, Object>();
				Path path;
				map.put("Class name", klass.getSimpleName());
				if (klass.getAnnotation(Path.class) != null) {
					path = klass.getAnnotation(Path.class);
					map.put("URL", path.value());
				}
				List<Map<String, Object>> listsubmap = new ArrayList<Map<String, Object>>();
				for (Method method : klass.getDeclaredMethods()) {
					Map<String, Object> submap = new HashMap<String, Object>();

					if (method.getAnnotation(GET.class) != null) {
						submap.put("Method", "GET");
						submap.put("Produces",
								method.getAnnotation(Produces.class).value());
					} else if (method.getAnnotation(POST.class) != null) {
						submap.put("Method", "POST");
						submap.put("Consumes",
								method.getAnnotation(Consumes.class).value());
					} else if (method.getAnnotation(PUT.class) != null) {
						submap.put("Method", "PUT");
						submap.put("Consumes",
								method.getAnnotation(Consumes.class).value());
					}
					if (klass.getAnnotation(Path.class) != null) {
						submap.put("URL", method.getAnnotation(Path.class)
								.value());
					}
					listsubmap.add(submap);
				}
				map.put("Methods", listsubmap);
				list.add(map);
			}
		}
		map = new HashMap<String, Object>();
		map.put("Service List", list);
//		JSONObject json = new JSONObject(map);
		return Response.ok(map).build();

	}

	@GET
	@Path("/service/{servicename}")
	@Produces("application/json")
	public Response getServiceInfo(@Context HttpHeaders headers,
			@PathParam("servicename") String servicename) {
		Class<?> klass = null;
		switch (servicename) {
		case "organisasi":
			klass = OrganisasiRest.class;
			break;
		case "referensi":
			klass = ReferensiRest.class;
			break;
		}

		Map<String, Object> map = new HashMap<String, Object>();
		if (SharedMethod.ServiceAuth(headers, map)) {
			if (klass != null) {
				map = new HashMap<String, Object>();
				Path path;
				map.put("Class name", klass.getSimpleName());
				if (klass.getAnnotation(Path.class) != null) {
					path = klass.getAnnotation(Path.class);
					map.put("URL", path.value());
				}
				List<Map<String, Object>> listsubmap = new ArrayList<Map<String, Object>>();
				for (Method method : klass.getDeclaredMethods()) {
					Map<String, Object> submap = new HashMap<String, Object>();

					if (method.getAnnotation(GET.class) != null) {
						submap.put("Method", "GET");
						submap.put("Produces",
								method.getAnnotation(Produces.class).value());
					} else if (method.getAnnotation(POST.class) != null) {
						submap.put("Method", "POST");
						submap.put("Consumes",
								method.getAnnotation(Consumes.class).value());
					} else if (method.getAnnotation(PUT.class) != null) {
						submap.put("Method", "PUT");
						submap.put("Consumes",
								method.getAnnotation(Consumes.class).value());
					}
					if (klass.getAnnotation(Path.class) != null) {
						submap.put("URL", method.getAnnotation(Path.class)
								.value());
					}
					listsubmap.add(submap);
				}
				map.put("Methods", listsubmap);
			} else {
				return Response.status(Status.NOT_FOUND).build();
			}
		}
//		JSONObject json = new JSONObject(map);
		return Response.ok(map).build();

	}

	@GET
	@Path("/memory")
	@Produces("application/json")
	public Response getInfoUsage(@Context HttpHeaders headers) {
		Map<String, Object> map = new HashMap<String, Object>();
		//if (SharedMethod.ServiceAuth(headers, map)) {
			map = new HashMap<String, Object>();
			long total;
			long max;
			long free;
			long used;
			Runtime runtime = Runtime.getRuntime();
			
			total = (long) runtime.totalMemory() / 1024;
	        max = (long) runtime.maxMemory() / 1024;
	        free = (long) runtime.freeMemory() / 1024;	     
	        used = total - free;
			map.put("Available Memory", String.format("%,2d", (long) free) + " KB");
			map.put("Used Memory", String.format("%,2d", (long) used) + " KB");
			map.put("Max Memory", String.format("%,2d", (long) max) + " KB");
			map.put("Total Memory", String.format("%,2d", (long) total) + " KB");
		//}
//		System.out.println(map.toString());
//		JSONObject json = new JSONObject(map);
		return Response.ok(map).build();
	}
	
	@GET
	@Path("/testoutput")
	@Produces("application/json")
	public Response testOutput(@Context HttpHeaders headers) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		Connection con = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		String query = null;
		
		try {
			query = "exec dbo.testOutput ?,?";
			con = new Koneksi().getConnection();
			cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			cs.setString(1, "testing");
			cs.registerOutParameter(2, Types.VARCHAR);
			cs.execute();
			map.put("Output : ", cs.getString(2));
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
			if (cs != null) {
				try {
					cs.close();
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
		return Response.ok(map).build();
	}
}
