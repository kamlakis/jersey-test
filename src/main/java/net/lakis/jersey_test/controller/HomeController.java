package net.lakis.jersey_test.controller;

import org.glassfish.jersey.server.mvc.Viewable;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

@Path("/tt")
public class HomeController {

	@GET
	@Produces(MediaType.TEXT_HTML)
	public Object index() {
		try {
			System.out.println("asa");
			Map<String, String> model = new HashMap<>();
			model.put("hello", "Hello");
			model.put("world", "World");
			return new Viewable("/index", model);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return "asd";
	}
}
