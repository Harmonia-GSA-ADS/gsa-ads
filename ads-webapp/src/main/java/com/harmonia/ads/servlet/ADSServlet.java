package com.harmonia.ads.servlet;

import java.net.URI;

import javax.enterprise.context.RequestScoped;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringUtils;
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
	public String getAdverseDrugEvents(@QueryParam("ageStart") String ageStart,
			@QueryParam("ageEnd") String ageEnd,
			@QueryParam("dateStart") String dateStart,
			@QueryParam("dateEnd") String dateEnd,
			@QueryParam("weightStart") String weightStart,
			@QueryParam("weightEnd") String weightEnd,
			@QueryParam("gender") String gender,
			@QueryParam("indication") String indication,
			@QueryParam("brandName") String brandName,
			@QueryParam("genericName") String genericName,
			@QueryParam("manufacturerName") String manufacturerName,
			@QueryParam("substanceName") String substanceName) {

		String ageParameter = createAgeParameter(ageStart, ageEnd);
		System.out.println("age string: " + ageParameter);
		String dateParameter = createDateParameter(dateStart, dateEnd);
		System.out.println("date string: " + dateParameter);
		String genderParameter = createGenderParameter(gender);
		System.out.println("gender string: " + genderParameter);
		String weightParameter = createWeightParameter(weightStart, weightEnd);
		System.out.println("weight string: " + weightParameter);
		String drugsParameter = createDrugsParameter(indication, brandName,
				genericName, manufacturerName, substanceName);
		System.out.println("drugs string: " + drugsParameter);

		String and = "+AND+";
		StringBuilder sb = new StringBuilder();
		if (StringUtils.isNotBlank(ageParameter)) {
			sb.append(ageParameter);
			sb.append(and);
		}
		if (StringUtils.isNotBlank(dateParameter)) {
			sb.append(dateParameter);
			sb.append(and);
		}
		if (StringUtils.isNotBlank(genderParameter)) {
			sb.append(genderParameter);
			sb.append(and);
		}
		if (StringUtils.isNotBlank(weightParameter)) {
			sb.append(weightParameter);
			sb.append(and);
		}
		if (StringUtils.isNotBlank(drugsParameter)) {
			sb.append(drugsParameter);
		}

		String search = sb.toString();
		System.out.println(search);

		if (search.endsWith(and)) {
			search = search.substring(0, search.length() - and.length());
		}
		search = search.replace(" ", "+");

		System.out.println(search);

		String result = "";
		try {
			HttpGet httpget = new HttpGet(
					"https://api.fda.gov/drug/event.json?search=" + search
							+ "&limit=5");
			CloseableHttpResponse response = httpClient.execute(httpget);
			result = EntityUtils.toString(response.getEntity());
		} catch (Exception e) {
			// TODO
		}

		return result;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/routes")
	public String getRoutes(@QueryParam("indication") String indication,
			@QueryParam("brandName") String brandName,
			@QueryParam("genericName") String genericName,
			@QueryParam("manufacturerName") String manufacturerName,
			@QueryParam("substanceName") String substanceName) {

		String search = createRouteSearch(indication, brandName, genericName,
				manufacturerName, substanceName);
		search = search.replace(" ", "+");
		System.out.println("routes search string: " + search);

		String result = "";
		try {
			HttpGet httpget = new HttpGet(
					"https://api.fda.gov/drug/label.json?search=" + search
							+ "&limit=5");
			System.out.println(httpget.getURI().toString());
			CloseableHttpResponse response = httpClient.execute(httpget);
			result = EntityUtils.toString(response.getEntity());
		} catch (Exception e) {
			// TODO
			System.out.println(e);
		}

		return result;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/drugs")
	public String getDrugs(@QueryParam("indication") String indication,
			@QueryParam("route") String route) {

		String search = createDrugRouteSearch(indication, route);

		search = search.replace(" ", "+");
		System.out.println("drugs search string: " + search);
		String result = "";
		try {
			HttpGet httpget = new HttpGet(
					"https://api.fda.gov/drug/label.json?search=" + search
							+ "&limit=5");
			CloseableHttpResponse response = httpClient.execute(httpget);
			result = EntityUtils.toString(response.getEntity());
		} catch (Exception e) {
			// TODO
		}

		return result;
	}

	private String createAgeParameter(String ageStart, String ageEnd) {
		if (StringUtils.isNotBlank(ageStart) && StringUtils.isBlank(ageEnd)) {
			ageEnd = ageStart;
		}
		if (StringUtils.isBlank(ageStart) && StringUtils.isNotBlank(ageEnd)) {
			ageStart = ageEnd;
		}
		String ageConditionString = "";
		if (StringUtils.isNotBlank(ageStart)) {
			ageConditionString = "patient.patientonsetage:[" + ageStart
					+ "+TO+" + ageEnd + "]+AND+patient.patientonsetageunit:801";
		}
		return ageConditionString;
	}

	private String createDateParameter(String dateStart, String dateEnd) {
		if (StringUtils.isNotBlank(dateStart) && StringUtils.isBlank(dateEnd)) {
			dateEnd = dateStart;
		}
		if (StringUtils.isBlank(dateStart) && StringUtils.isNotBlank(dateEnd)) {
			dateStart = dateEnd;
		}
		String dateConditionString = "";
		if (StringUtils.isNotBlank(dateStart)) {
			dateConditionString = "receivedate:[" + dateStart + "+TO+"
					+ dateEnd + "]";
		}
		return dateConditionString;
	}

	private String createGenderParameter(String gender) {
		String genderValue = null;
		if (StringUtils.isNotBlank(gender)) {
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
		if (StringUtils.isNotBlank(genderValue)) {
			genderConditionString = "patient.patientsex:" + genderValue;
		}
		return genderConditionString;
	}

	private String createWeightParameter(String weightStart, String weightEnd) {
		if (StringUtils.isNotBlank(weightStart))
			weightStart = "" + Integer.parseInt(weightStart) * 0.453592; // lbs
																			// to
																			// kg
		if (StringUtils.isNotBlank(weightEnd))
			weightEnd = "" + Integer.parseInt(weightEnd) * 0.453592;
		if (StringUtils.isNotBlank(weightStart)
				&& StringUtils.isBlank(weightEnd)) {
			weightEnd = weightStart;
		}
		if (StringUtils.isBlank(weightStart)
				&& StringUtils.isNotBlank(weightEnd)) {
			weightStart = weightEnd;
		}
		String weightConditionString = "";
		if (StringUtils.isNotBlank(weightStart)) {
			weightConditionString = "patient.patientweight:[" + weightStart
					+ "+TO+" + weightEnd + "]";
		}
		return weightConditionString;
	}

	private String createDrugsParameter(String indication, String brandName,
			String genericName, String manufacturerName, String substanceName) {
		StringBuilder sb = new StringBuilder("(");
		String or = "+OR+";

		if (StringUtils.isNotBlank(indication)) {
			sb.append("patient.drug.drugindication:" + indication.toUpperCase());
			sb.append(or);
		}
		if (StringUtils.isNotBlank(brandName)) {
			sb.append("patient.drug.openfda.brand_name:"
					+ brandName.toUpperCase());
			sb.append(or);
		}
		if (StringUtils.isNotBlank(genericName)) {
			sb.append("patient.drug.openfda.generic_name:"
					+ genericName.toUpperCase());
			sb.append(or);
		}
		if (StringUtils.isNotBlank(manufacturerName)) {
			// This one isn't all caps
			sb.append("patient.drug.openfda.manufacturer_name:%22"
					+ manufacturerName + "%22");
			sb.append(or);
		}
		if (StringUtils.isNotBlank(substanceName)) {
			sb.append("patient.drug.openfda.substance_name:"
					+ substanceName.toUpperCase());
		}

		String drugsConditionString = sb.toString();
		if (drugsConditionString.endsWith(or)) {
			drugsConditionString = drugsConditionString.substring(0,
					drugsConditionString.length() - or.length());
		}
		if (drugsConditionString.equals("(")) {
			drugsConditionString = "";
		} else {
			drugsConditionString += ")";
		}

		return drugsConditionString;
	}

	private String createRouteSearch(String indication, String brandName,
			String genericName, String manufacturerName, String substanceName) {

		StringBuilder sb = new StringBuilder();
		String or = "+OR+";

		if (StringUtils.isNotBlank(indication)) {
			sb.append("indications_and_usage:" + indication);
			sb.append(or);
		}
		if (StringUtils.isNotBlank(brandName)) {
			sb.append("openfda.brand_name:" + brandName.toUpperCase());
			sb.append(or);
		}
		if (StringUtils.isNotBlank(genericName)) {
			sb.append("openfda.generic_name:" + genericName.toUpperCase());
			sb.append(or);
		}
		if (StringUtils.isNotBlank(manufacturerName)) {
			sb.append("openfda.manufacturer_name:%22" + manufacturerName
					+ "%22"); // %22 = '"'
			sb.append(or);
		}
		if (StringUtils.isNotBlank(substanceName)) {
			sb.append("openfda.substance_name:" + substanceName.toUpperCase());
		}

		String search = sb.toString();
		if (search.endsWith(or)) {
			search = search.substring(0, search.length() - or.length());
		}

		return search;
	}

	private String createDrugRouteSearch(String indication, String route) {

		String search = "";
		if (StringUtils.isNotBlank(indication) && StringUtils.isNotBlank(route)) {
			search = "indications_and_usage:" + indication
					+ "+AND+openfda.route:" + route.toUpperCase();
		}

		return search;
	}
}
