package net.lakis.jersey_test;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/checknumber")
public class CheckNumber {




	@GET
	@Path("{number}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getNumberInfo(@PathParam("number") long number, @HeaderParam("Authorization") String token,
			@QueryParam("country") String country)   {
		return String.valueOf(number);
	}
 

}
