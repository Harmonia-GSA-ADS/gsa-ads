package com.harmonia.ads.servlet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.harmonia.ads.ejb.bean.SearchBean;
import com.harmonia.ads.model.Search;

/**
 * REST endpoint for MedFinder operations
 * 
 * @author janway
 */
@Transactional
@Path("/")
@RequestScoped
public class ADSServlet {

	/**
	 * HTTP client used for contacting the OpenFDA API
	 */
	private CloseableHttpClient httpClient = HttpClients.createDefault();

	/**
	 * Bean for saved search related operations
	 */
	@EJB
	private SearchBean searchBean;

	/**
	 * Returns a list of adverse drug events based on the supplied criteria
	 * 
	 * @param ageStart
	 *            Minimum age of patient, in years
	 * @param ageEnd
	 *            Maximum age of patient, in years
	 * @param dateStart
	 *            Earliest date of event
	 * @param dateEnd
	 *            Latest date of event
	 * @param weightStart
	 *            Minimum weight of patient, in pounds
	 * @param weightEnd
	 *            Maximum weight of patient, in pounds
	 * @param gender
	 *            Gender of the patient
	 * @param indication
	 *            Purpose of drug
	 * @param brandName
	 *            Brand name of drug
	 * @param genericName
	 *            Generic name of drug
	 * @param manufacturerName
	 *            Manufacturer name of drug
	 * @param substanceName
	 *            Substance name (active ingredient) of drug
	 * @param limit
	 *            The maximum number of results to return
	 * @return List of adverse events based on the supplied criteria
	 */
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
			@QueryParam("substanceName") String substanceName,
			@QueryParam("limit") Integer limit) {

		String ageParameter = createAgeParameter(ageStart, ageEnd);
		String dateParameter = createDateParameter(dateStart, dateEnd);
		String genderParameter = createGenderParameter(gender);
		String weightParameter = createWeightParameter(weightStart, weightEnd);
		String drugsParameter = createDrugsParameter(indication, brandName,
				genericName, manufacturerName, substanceName);
		int lim = limit == null ? 5 : limit;

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

		if (search.endsWith(and)) {
			search = search.substring(0, search.length() - and.length());
		}
		search = search.replace(" ", "+");

		String result = "";
		try {
			HttpGet httpget = new HttpGet(
					"https://api.fda.gov/drug/event.json?search=" + search
							+ "&limit=" + lim);
			CloseableHttpResponse response = httpClient.execute(httpget);
			result = EntityUtils.toString(response.getEntity());
		} catch (Exception e) {
			// TODO
			System.out.println(e);
		}

		return result;
	}

	/**
	 * Returns the routes of administration for a drug
	 * 
	 * @param indication
	 *            Purpose of drug
	 * @param brandName
	 *            Brand name of drug
	 * @param genericName
	 *            Generic name of drug
	 * @param manufacturerName
	 *            Manufacturer name of drug
	 * @param substanceName
	 *            Substance name (active ingredient) of drug
	 * @param limit
	 *            The maximum number of results to return
	 * @return Routes of administration for a drug
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/routes")
	public String getRoutes(@QueryParam("indication") String indication,
			@QueryParam("brandName") String brandName,
			@QueryParam("genericName") String genericName,
			@QueryParam("manufacturerName") String manufacturerName,
			@QueryParam("substanceName") String substanceName,
			@QueryParam("limit") Integer limit) {

		String search = createRouteSearch(indication, brandName, genericName,
				manufacturerName, substanceName);
		search = search.replace(" ", "+");
		int lim = limit == null ? 5 : limit;

		String result = "";
		try {
			HttpGet httpget = new HttpGet(
					"https://api.fda.gov/drug/label.json?search=" + search
							+ "&limit=" + lim);
			CloseableHttpResponse response = httpClient.execute(httpget);
			result = EntityUtils.toString(response.getEntity());
		} catch (Exception e) {
			// TODO
			System.out.println(e);
		}

		return result;
	}

	/**
	 * Returns the drugs matching the given indication and route of
	 * administration
	 * 
	 * @param indication
	 *            Purpose of drug
	 * @param route
	 *            Route of administration of the drug
	 * @param limit
	 *            The maximum number of results to return
	 * @return Drugs matching the given indication and route of administration
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/drugs")
	public String getDrugs(@QueryParam("indication") String indication,
			@QueryParam("route") String route,
			@QueryParam("limit") Integer limit) {

		String search = createDrugRouteSearch(indication, route);
		int lim = limit == null ? 5 : limit;

		search = search.replace(" ", "+");
		String result = "";
		try {
			HttpGet httpget = new HttpGet(
					"https://api.fda.gov/drug/label.json?search=" + search
							+ "&limit=" + lim);
			CloseableHttpResponse response = httpClient.execute(httpget);
			result = EntityUtils.toString(response.getEntity());
		} catch (Exception e) {
			// TODO
			System.out.println(e);
		}

		return result;
	}

	/**
	 * Creates the patient age parameter for OpenFDA queries
	 * 
	 * @param ageStart
	 *            Minimum age of patient, in years
	 * @param ageEnd
	 *            Maximum age of patient, in years
	 * @return Query string section for patient age
	 */
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

	/**
	 * Creates the event date range parameter for OpenFDA queries
	 * 
	 * @param dateStart
	 *            Earliest date of event
	 * @param dateEnd
	 *            Latest date of event
	 * @return Query string section for event date range
	 */
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

	/**
	 * Creates the gender parameter for OpenFDA queries
	 * 
	 * @param gender
	 *            Gender of the patient
	 * @return Query string section for gender
	 */
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

	/**
	 * Creates the weight range parameter for OpenFDA queries
	 * 
	 * @param weightStart
	 *            Minimum weight of patient, in pounds
	 * @param weightEnd
	 *            Maximum weight of patient, in pounds
	 * @return Query string section for weight range
	 */
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

	/**
	 * Creates the drugs parameter for OpenFDA queries
	 * 
	 * @param indication
	 *            Purpose of drug
	 * @param brandName
	 *            Brand name of drug
	 * @param genericName
	 *            Generic name of drug
	 * @param manufacturerName
	 *            Manufacturer name of drug
	 * @param substanceName
	 *            Substance name (active ingredient) of drug
	 * @return Query string section for drugs
	 */
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

	/**
	 * Creates the query string for OpenFDA queries to find routes of
	 * administration for drugs
	 * 
	 * @param indication
	 *            Purpose of drug
	 * @param brandName
	 *            Brand name of drug
	 * @param genericName
	 *            Generic name of drug
	 * @param manufacturerName
	 *            Manufacturer name of drug
	 * @param substanceName
	 *            Substance name (active ingredient) of drug
	 * @return Query string for OpenFDA queries to find routes of administration
	 *         for drugs
	 */
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

	/**
	 * Creates the query string for OpenFDA queries to find drugs based on
	 * purpose and route of administration
	 * 
	 * @param indication
	 *            Purpose of drug
	 * @param route
	 *            Route of administration
	 * @return Query string for OpenFDA queries to find drugs based on purpose
	 *         and route of administration
	 */
	private String createDrugRouteSearch(String indication, String route) {

		String search = "";
		if (StringUtils.isNotBlank(indication) && StringUtils.isNotBlank(route)) {
			search = "indications_and_usage:" + indication
					+ "+AND+openfda.route:" + route.toUpperCase();
		}

		return search;
	}

	/**
	 * Returns all saved searches with the given type
	 * 
	 * @param type
	 *            Saved search type
	 * @return List of all saved searches with the given type
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/searches")
	public List<Search> getSavedSearchesOfType(
			@QueryParam("type") Search.SearchType type) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("type", type);
		return searchBean.findWithNamedQuery(Search.Q_FIND_BY_TYPE, param);
	}

	/**
	 * Returns the saved search with the given id
	 * 
	 * @param id
	 *            Id of the saved search
	 * @return Saved search with the given id
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/search")
	public Search getSearchById(@QueryParam("id") String id) {
		return searchBean.findById(id);
	}

	/**
	 * Deletes the saved search with the given id
	 * 
	 * @param id
	 *            Id of the saved search
	 */
	@DELETE
	@Path("/search/{id}")
	public void deleteSavedSearch(@PathParam("id") String id) {
		if (id == null) {
			System.out.println("id is null");
		} else {
			System.out.println(id);
		}
		searchBean.delete(id);
	}

	/**
	 * Creates a saved search
	 * 
	 * @param name
	 *            Name of the saved search
	 * @param type
	 *            Type of the saved search
	 * @param indication
	 *            Search criteria value for indication
	 * @param brandName
	 *            Search criteria value for brand name
	 * @param genericName
	 *            Search criteria value for generic name
	 * @param manufacturer
	 *            Name Search criteria value for manufacturer name
	 * @param substanceName
	 *            Search criteria value for substance name
	 * @param minAge
	 *            Search criteria value for min age, in years
	 * @param maxAge
	 *            Search criteria value for max age, in years
	 * @param minWeight
	 *            Search criteria value for min weight, in pounds
	 * @param maxWeight
	 *            Search criteria value for max weight, in pounds
	 * @param gender
	 *            Search criteria value for gender
	 * @param route
	 *            Search criteria value for route
	 * @return New saved search
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Path("/search")
	public Search createSavedSearch(@FormParam("name") String name,
			@FormParam("type") Search.SearchType type,
			@FormParam("indication") String indication,
			@FormParam("brandName") String brandName,
			@FormParam("genericName") String genericName,
			@FormParam("manufacturerName") String manufacturerName,
			@FormParam("substanceName") String substanceName,
			@FormParam("minAge") Integer minAge,
			@FormParam("maxAge") Integer maxAge,
			@FormParam("minWeight") Double minWeight,
			@FormParam("maxWeight") Double maxWeight,
			@FormParam("gender") String gender, @FormParam("route") String route) {

		Search newSearch = new Search();
		if (StringUtils.isNotBlank(name)) {
			newSearch.setName(name);
		}
		if (type != null) {
			newSearch.setType(type);
		}
		if (StringUtils.isNotBlank(indication)) {
			newSearch.setIndication(indication);
		}
		if (StringUtils.isNotBlank(brandName)) {
			newSearch.setBrandName(brandName);
		}
		if (StringUtils.isNotBlank(genericName)) {
			newSearch.setGenericName(genericName);
		}
		if (StringUtils.isNotBlank(manufacturerName)) {
			newSearch.setManufacturerName(manufacturerName);
		}
		if (StringUtils.isNotBlank(substanceName)) {
			newSearch.setSubstanceName(substanceName);
		}
		if (minAge != null && minAge > -1) {
			newSearch.setMinAge(minAge);
		}
		if (maxAge != null && maxAge > -1) {
			newSearch.setMaxAge(maxAge);
		}
		if (minWeight != null && minWeight > -1) {
			newSearch.setMinWeight(minWeight);
		}
		if (maxWeight != null && maxWeight > -1) {
			newSearch.setMaxWeight(maxWeight);
		}
		if (StringUtils.isNotBlank(gender)) {
			newSearch.setGender(gender);
		}
		if (StringUtils.isNotBlank(route)) {
			newSearch.setRoute(route);
		}

		searchBean.persist(newSearch);

		return newSearch;
	}

}
