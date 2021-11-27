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
	public Integer getTotalIssues() {
//		Report for total count of equipment issued
		return RequestRepository.stats_issue.values().stream().reduce(0,Integer::sum);
	}
	
	@GET
	@Path("/issue")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Integer> getIssueReport() {
//		Report for frequency of issues
//		Sends a list of static data, only the values are sent and the keys are not sent.
		List<Integer> list = new ArrayList<Integer>(RequestRepository.stats_issue.values());
		return list;
	}
	
	@GET
	@Path("/discarded")
	public List<Integer> getDiscardedReport() {
//		Report for plotting lost/broken
		List<Integer> list = new ArrayList<Integer>(EquipmentResource.stats_discarded.values());
		return list;
	}

}
