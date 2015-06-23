package com.harmonia.ads.servlet;

import javax.enterprise.context.RequestScoped;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 * REST endpoint for ADS-related operations
 * 
 * @author janway
 */
@Transactional
@Path("/")
@RequestScoped
public class ADSServlet {

	/**
	 * Request object
	 */
	@Context
	private HttpServletRequest httpRequest;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/test/")
	public String test() {
		return "Test";
	}
}
