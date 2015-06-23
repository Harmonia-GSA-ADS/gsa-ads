package com.harmonia.ads.servlet;

import java.net.URI;

import javax.enterprise.context.RequestScoped;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

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

	private CloseableHttpClient httpClient = HttpClients.createDefault();

	private final String apiBase = "https://api.fda.gov";

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/test/")
	public String test() {
		return "Test";
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("event")
	public String getOneAdverseDrugEvent() {
		String ret = "";
		try {
			URI uri = new URIBuilder().setScheme("https")
					.setHost("api.fda.gov").setPath("/drug/event.json").build();
			HttpGet httpget = new HttpGet(uri);
			CloseableHttpResponse response = httpClient.execute(httpget);
			ret = EntityUtils.toString(response.getEntity());
		} catch (Exception e) {
			// TODO
		}
		return ret;
	}
}
