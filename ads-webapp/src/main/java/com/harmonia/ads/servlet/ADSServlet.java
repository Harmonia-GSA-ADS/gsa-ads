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
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

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

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("events")
	public String getAdverseDrugEvents(@Context UriInfo info) {
		MultivaluedMap<String, String> params = info.getQueryParameters();

		System.out.println("Retrieved query parameters from UriInfo.");

		String ageParameter = createAgeParameter(params.getFirst("ageStart"),
				params.getFirst("ageEnd"));
		System.out.println("age string: " + ageParameter);
		String dateParameter = createDateParameter(
				params.getFirst("dateStart"), params.getFirst("dateEnd"));
		System.out.println("date string: " + dateParameter);
		String genderParameter = createGenderParameter(params
				.getFirst("gender"));
		System.out.println("gender string: " + genderParameter);
		String weightParameter = createWeightParameter(
				params.getFirst("weightStart"), params.getFirst("weightEnd"));
		System.out.println("weight string: " + weightParameter);

		// TODO: need to figure out how to handle the drug parameters
		// Need to receive a list of tuples, one tuple per drug containing a
		// subset of
		// indication, brand name, generic name, manufacturer name, active
		// ingredients
		// then convert to the api format, ex
		// (indication:tuple_1_indication+AND+generic:tuple_1_generic)+AND+(indication:tuple_2_indication)

		String and = "+AND+";
		StringBuilder sb = new StringBuilder();
		sb.append(ageParameter);
		sb.append(and);
		sb.append(dateParameter);
		sb.append(and);
		sb.append(genderParameter);
		sb.append(and);
		sb.append(weightParameter);

		String search = sb.toString();
		System.out.println(search);
		while (search.contains(and + and)) {
			search = search.replace(and + and, and);
		}
		if (search.startsWith(and)) {
			search = search.substring(and.length());
		}
		if (search.endsWith(and)) {
			search = search.substring(0, search.length() - and.length());
		}
		search.replace(" ", "+");

		System.out.println(search);

		String result = "";
		try {
			HttpGet httpget = new HttpGet(
					"https://api.fda.gov/drug/event.json?search=" + search);
			CloseableHttpResponse response = httpClient.execute(httpget);
			result = EntityUtils.toString(response.getEntity());
		} catch (Exception e) {
			// TODO
		}

		return result;
	}

	private String createAgeParameter(String ageStart, String ageEnd) {
		if (ageStart != null && ageEnd == null) {
			ageEnd = ageStart;
		}
		if (ageStart == null && ageEnd != null) {
			ageStart = ageEnd;
		}
		String ageConditionString = "";
		if (ageStart != null) {
			ageConditionString = "patient.patientonsetage:[" + ageStart
					+ "+TO+" + ageEnd + "]+AND+patient.patientonsetageunit:801";
		}
		return ageConditionString;
	}

	private String createDateParameter(String dateStart, String dateEnd) {
		if (dateStart != null && dateEnd == null) {
			dateEnd = dateStart;
		}
		if (dateStart == null && dateEnd != null) {
			dateStart = dateEnd;
		}
		String dateConditionString = "";
		if (dateStart != null) {
			dateConditionString = "receivedate:[" + dateStart + "+TO+"
					+ dateEnd + "]";
		}
		return dateConditionString;
	}

	private String createGenderParameter(String gender) {
		String genderValue = null;
		if (gender != null) {
			if (gender.equalsIgnoreCase("male")) {
				genderValue = "1";
			}
			if (gender.equalsIgnoreCase("female")) {
				genderValue = "2";
			}
			if (gender.equalsIgnoreCase("unknown")) {
				genderValue = "0";
			}
		}
		String genderConditionString = "";
		if (genderValue != null) {
			genderConditionString = "patient.patientsex:" + genderValue;
		}
		return genderConditionString;
	}

	private String createWeightParameter(String weightStart, String weightEnd) {
		if (weightStart != null)
			weightStart = "" + Integer.parseInt(weightStart) * 0.453592; // lbs
																			// to
																			// kg
		if (weightEnd != null)
			weightEnd = "" + Integer.parseInt(weightEnd) * 0.453592;
		if (weightStart != null && weightEnd == null) {
			weightEnd = weightStart;
		}
		if (weightStart == null && weightEnd != null) {
			weightStart = weightEnd;
		}
		String weightConditionString = "";
		if (weightStart != null) {
			weightConditionString = "patient.patientweight:[" + weightStart
					+ "+TO+" + "weightEnd" + "]";
		}
		return weightConditionString;
	}
}
