package com.geartracker.geartracker_backend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("report")
public class ReportResource {
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Object> getReport() {
//		Sends a list of static data, only the values are sent and the keys are not sent.
		return null;
	}
}
