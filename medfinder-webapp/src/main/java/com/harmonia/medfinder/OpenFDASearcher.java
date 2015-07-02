package com.harmonia.medfinder;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.Encoder;
import org.owasp.esapi.errors.EncodingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class that can make search requests to OpenFDA's Drugs API.
 * 
 * @author keagan
 * @author janway
 */
public class OpenFDASearcher {

	/**
	 * Static Logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(OpenFDASearcher.class);

	/**
	 * OpenFDA API Key. Associated with keagan@harmonia.com.
	 */
	private static final String API_KEY = "6ErngSrIicSDsiyvtOIOrAxu4BqVUiz7Iav6TVir";

	/**
	 * OpenFDA Drug API URL base
	 */
	private static final String FDA_API_URL = "https://api.fda.gov/drug/";

	/**
	 * REST end point for adverse event search
	 */
	private static final String LABEL_JSON = "label.json";

	/**
	 * REST end point for label search
	 */
	private static final String EVENT_END_POINT = "event.json";

	/**
	 * OR representation in query string
	 */
	private static final String OR = "+";

	/**
	 * AND representation in query string
	 */
	private static final String AND = "+AND+";

	/**
	 * HTTP client used for contacting the OpenFDA API
	 */
	private CloseableHttpClient httpClient = HttpClients.createDefault();

	/**
	 * Handles connecting to a query URI and processing the result
	 * 
	 * @param queryURI The URI of the query
	 * @return A response with an appropriate status code and the content
	 */
	private Response executeQuery(String queryURI) {
		String result = "";
		int responseCode;

		try {
			HttpGet httpget = new HttpGet(queryURI);
			CloseableHttpResponse response = httpClient.execute(httpget);
			result = EntityUtils.toString(response.getEntity());
			responseCode = response.getStatusLine().getStatusCode();
		}
		catch (Exception e) {
			responseCode = HttpStatus.SC_OK;
			result = "OpenFDA query could not be completed.";
			LOGGER.error(e.toString());
		}

		return Response.status(responseCode).entity(result).type(MediaType.APPLICATION_JSON).build();
	}

	/**
	 * Returns a query URL for OpenFDA with the provided values
	 * 
	 * @param endPoint REST end point
	 * @param criteria search criteria
	 * @param limit Result size limit
	 * @return Request URL
	 */
	private String buildQueryURL(String endPoint, String criteria, int limit) {
		return FDA_API_URL + endPoint + "?api_key=" + API_KEY + "&search=" + criteria + "&limit=" + limit;
	}

	/**
	 * Returns a list of adverse drug events based on the supplied criteria
	 * 
	 * @param ageStart Minimum age of patient, in years
	 * @param ageEnd Maximum age of patient, in years
	 * @param dateStart Earliest date of event
	 * @param dateEnd Latest date of event
	 * @param weightStart Minimum weight of patient, in pounds
	 * @param weightEnd Maximum weight of patient, in pounds
	 * @param gender Gender of the patient
	 * @param indication Purpose of drug
	 * @param brandName Brand name of drug
	 * @param genericName Generic name of drug
	 * @param manufacturerName Manufacturer name of drug
	 * @param substanceName Substance name (active ingredient) of drug
	 * @param limit The maximum number of results to return
	 * @return List of adverse events based on the supplied criteria
	 */
	public Response getAdverseDrugEvents(String ageStart, String ageEnd, String dateStart, String dateEnd, String weightStart, String weightEnd, String gender, String indication, String brandName,
	        String genericName, String manufacturerName, String substanceName, Integer limit) throws EncodingException {

		String ageParameter = createAgeParameter(ageStart, ageEnd);
		String dateParameter = createDateParameter(dateStart, dateEnd);
		String genderParameter = createGenderParameter(gender);
		String weightParameter = createWeightParameter(weightStart, weightEnd);
		String drugsParameter = createDrugsParameter(indication, brandName, genericName, manufacturerName, substanceName);
		int lim = limit == null ? 5 : limit;

		StringBuilder sb = new StringBuilder();
		if (StringUtils.isNotBlank(ageParameter)) {
			sb.append(ageParameter);
			sb.append(AND);
		}
		if (StringUtils.isNotBlank(dateParameter)) {
			sb.append(dateParameter);
			sb.append(AND);
		}
		if (StringUtils.isNotBlank(genderParameter)) {
			sb.append(genderParameter);
			sb.append(AND);
		}
		if (StringUtils.isNotBlank(weightParameter)) {
			sb.append(weightParameter);
			sb.append(AND);
		}
		if (StringUtils.isNotBlank(drugsParameter)) {
			sb.append(drugsParameter);
		}

		// strip off trailing "and" if necessary
		String search = sb.toString();
		if (search.endsWith(AND)) {
			search = search.substring(0, search.length() - AND.length());
		}

		// turn spaces into "or"s
		search = search.replace(" ", OR);

		String queryURI = buildQueryURL(EVENT_END_POINT, search, lim);
		return executeQuery(queryURI);
	}

	/**
	 * Returns the routes of administration for a drug
	 * 
	 * @param indication Purpose of drug
	 * @param brandName Brand name of drug
	 * @param genericName Generic name of drug
	 * @param manufacturerName Manufacturer name of drug
	 * @param substanceName Substance name (active ingredient) of drug
	 * @param limit The maximum number of results to return
	 * @return Routes of administration for a drug
	 */
	public Response getRoutes(String indication, String brandName, String genericName, String manufacturerName, String substanceName, Integer limit) throws EncodingException {

		// decode values
		Encoder encoder = ESAPI.encoder();
		indication = encoder.decodeFromURL(indication);
		brandName = encoder.decodeFromURL(brandName);
		genericName = encoder.decodeFromURL(genericName);
		manufacturerName = encoder.decodeFromURL(manufacturerName);
		substanceName = encoder.decodeFromURL(substanceName);

		String search = createRouteSearch(indication, brandName, genericName, manufacturerName, substanceName);
		search = search.replace(" ", OR);
		int lim = limit == null ? 5 : limit;

		String queryURI = buildQueryURL(LABEL_JSON, search, lim);
		return executeQuery(queryURI);
	}

	/**
	 * Returns the drugs matching the given indication and route of
	 * administration
	 * 
	 * @param indication Purpose of drug
	 * @param route Route of administration of the drug
	 * @param limit The maximum number of results to return
	 * @return Drugs matching the given indication and route of administration
	 */
	public Response getDrugs(String indication, String route, Integer limit) throws EncodingException {

		// decode values
		Encoder encoder = ESAPI.encoder();
		indication = encoder.decodeFromURL(indication);

		String search = createDrugRouteSearch(indication, route);
		int lim = limit == null ? 5 : limit;
		search = search.replace(" ", OR);

		String queryURI = buildQueryURL(LABEL_JSON, search, lim);
		return executeQuery(queryURI);
	}

	/**
	 * Creates the patient age parameter for OpenFDA queries
	 * 
	 * @param ageStart Minimum age of patient, in years
	 * @param ageEnd Maximum age of patient, in years
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
			ageConditionString = "patient.patientonsetage:[" + ageStart + "+TO+" + ageEnd + "]+AND+patient.patientonsetageunit:801";
		}
		return ageConditionString;
	}

	/**
	 * Creates the event date range parameter for OpenFDA queries
	 * 
	 * @param dateStart Earliest date of event
	 * @param dateEnd Latest date of event
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
			dateConditionString = "receivedate:[" + dateStart + "+TO+" + dateEnd + "]";
		}
		return dateConditionString;
	}

	/**
	 * Creates the gender parameter for OpenFDA queries
	 * 
	 * @param gender Gender of the patient
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
	 * @param weightStart Minimum weight of patient, in pounds
	 * @param weightEnd Maximum weight of patient, in pounds
	 * @return Query string section for weight range
	 */
	private String createWeightParameter(String weightStart, String weightEnd) {
		if (StringUtils.isNotBlank(weightStart)) weightStart = "" + Integer.parseInt(weightStart) * 0.453592; // lbs
		                                                                                                      // to
		                                                                                                      // kg
		if (StringUtils.isNotBlank(weightEnd)) weightEnd = "" + Integer.parseInt(weightEnd) * 0.453592;
		if (StringUtils.isNotBlank(weightStart) && StringUtils.isBlank(weightEnd)) {
			weightEnd = weightStart;
		}
		if (StringUtils.isBlank(weightStart) && StringUtils.isNotBlank(weightEnd)) {
			weightStart = weightEnd;
		}
		String weightConditionString = "";
		if (StringUtils.isNotBlank(weightStart)) {
			weightConditionString = "patient.patientweight:[" + weightStart + "+TO+" + weightEnd + "]";
		}
		return weightConditionString;
	}

	/**
	 * Creates the drugs parameter for OpenFDA queries
	 * 
	 * @param indication Purpose of drug
	 * @param brandName Brand name of drug
	 * @param genericName Generic name of drug
	 * @param manufacturerName Manufacturer name of drug
	 * @param substanceName Substance name (active ingredient) of drug
	 * @return Query string section for drugs
	 * @throws EncodingException
	 */
	private String createDrugsParameter(String indication, String brandName, String genericName, String manufacturerName, String substanceName) throws EncodingException {
		Encoder encoder = ESAPI.encoder();

		StringBuilder sb = new StringBuilder(500);

		if (StringUtils.isNotBlank(indication)) {
			sb.append("patient.drug.drugindication:");
			sb.append(encoder.encodeForURL(indication.toUpperCase()));
			sb.append(OR);
		}
		if (StringUtils.isNotBlank(brandName)) {
			sb.append("patient.drug.openfda.brand_name:");
			sb.append(encoder.encodeForURL(brandName.toUpperCase()));
			sb.append(OR);
		}
		if (StringUtils.isNotBlank(genericName)) {
			sb.append("patient.drug.openfda.generic_name:");
			sb.append(encoder.encodeForURL(genericName.toUpperCase()));
			sb.append(OR);
		}
		if (StringUtils.isNotBlank(manufacturerName)) {
			// This one isn't all caps
			sb.append("patient.drug.openfda.manufacturer_name:");
			sb.append(encoder.encodeForURL("\"" + manufacturerName + "\""));
			sb.append(OR);
		}
		if (StringUtils.isNotBlank(substanceName)) {
			sb.append("patient.drug.openfda.substance_name:");
			sb.append(encoder.encodeForURL(substanceName.toUpperCase()));
		}

		// strip off trailing "or" if necessary
		String drugsConditionString = sb.toString();
		if (drugsConditionString.endsWith(OR)) {
			drugsConditionString = drugsConditionString.substring(0, drugsConditionString.length() - OR.length());
		}

		// group the criteria
		if (StringUtils.isNotBlank(drugsConditionString)) {
			drugsConditionString = "(" + drugsConditionString + ")";
		}

		return drugsConditionString;
	}

	/**
	 * Creates the query string for OpenFDA queries to find routes of
	 * administration for drugs
	 * 
	 * @param indication Purpose of drug
	 * @param brandName Brand name of drug
	 * @param genericName Generic name of drug
	 * @param manufacturerName Manufacturer name of drug
	 * @param substanceName Substance name (active ingredient) of drug
	 * @return Query string for OpenFDA queries to find routes of administration
	 *         for drugs
	 * @throws EncodingException
	 */
	private String createRouteSearch(String indication, String brandName, String genericName, String manufacturerName, String substanceName) throws EncodingException {

		Encoder encoder = ESAPI.encoder();

		StringBuilder sb = new StringBuilder(500);

		if (StringUtils.isNotBlank(indication)) {
			sb.append("indications_and_usage:");
			sb.append(indication);
			sb.append(OR);
		}
		if (StringUtils.isNotBlank(brandName)) {
			brandName = encoder.encodeForURL(brandName);
			sb.append("openfda.brand_name:");
			sb.append(brandName.toUpperCase());
			sb.append(OR);
		}
		if (StringUtils.isNotBlank(genericName)) {
			sb.append("openfda.generic_name:");
			sb.append(genericName.toUpperCase());
			sb.append(OR);
		}
		if (StringUtils.isNotBlank(manufacturerName)) {
			sb.append("openfda.manufacturer_name:");
			sb.append(encoder.encodeForURL("\"" + manufacturerName + "\""));
			sb.append(OR);
		}
		if (StringUtils.isNotBlank(substanceName)) {
			sb.append("openfda.substance_name:");
			sb.append(substanceName.toUpperCase());
		}

		// string off trailing 'or' if necessary
		String search = sb.toString();
		if (search.endsWith(OR)) {
			search = search.substring(0, search.length() - OR.length());
		}

		return search;
	}

	/**
	 * Creates the query string for OpenFDA queries to find drugs based on
	 * purpose and route of administration
	 * 
	 * @param indication Purpose of drug
	 * @param route Route of administration
	 * @return Query string for OpenFDA queries to find drugs based on purpose
	 *         and route of administration
	 * @throws EncodingException
	 */
	private String createDrugRouteSearch(String indication, String route) throws EncodingException {

		Encoder encoder = ESAPI.encoder();

		StringBuilder sb = new StringBuilder(200);
		if (StringUtils.isNotBlank(indication) && StringUtils.isNotBlank(route)) {
			sb.append("indications_and_usage:");
			sb.append(encoder.encodeForURL(indication));
			sb.append(AND);
			sb.append("openfda.route:");
			sb.append(route.toUpperCase());
		}

		return sb.toString();
	}
}
