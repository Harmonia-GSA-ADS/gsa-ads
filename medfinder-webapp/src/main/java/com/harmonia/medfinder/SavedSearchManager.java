package com.harmonia.medfinder;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.Encoder;
import org.owasp.esapi.errors.EncodingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.harmonia.medfinder.ejb.bean.SearchBean;
import com.harmonia.medfinder.model.Search;

public class SavedSearchManager {

	/**
	 * Static Logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(SavedSearchManager.class);

	/**
	 * Returns all saved searches with the given type
	 * 
	 * @param type Saved search type
	 * @return List of all saved searches with the given type
	 */
	public Response getSavedSearchesOfType(Search.SearchType type, SearchBean searchBean) {
		Object result = "";
		int responseCode;

		try {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("type", type);
			result = searchBean.findWithNamedQuery(Search.Q_FIND_BY_TYPE, param);
			responseCode = HttpStatus.SC_OK;
		}
		catch (Exception e) {
			responseCode = HttpStatus.SC_INTERNAL_SERVER_ERROR;

			String typeString = "";
			switch (type) {
			case ADVERSE_EVENTS:
				typeString = "Side Effect";
				break;
			case ROUTES:
				typeString = "Route";
				break;
			case DRUGS:
				typeString = "Drug";
				break;
			}

			result = typeString + " saved searches could not be retreived.";
			LOGGER.error(e.toString());
		}

		return Response.status(responseCode).entity(result).type(MediaType.APPLICATION_JSON).build();
	}

	/**
	 * Returns the saved search with the given id
	 * 
	 * @param id Id of the saved search
	 * @return Saved search with the given id
	 */
	public Response getSearchById(String id, SearchBean searchBean) {
		Object result = "";
		int responseCode;

		try {
			result = searchBean.findById(id);
			responseCode = HttpStatus.SC_OK;
		}
		catch (Exception e) {
			responseCode = HttpStatus.SC_INTERNAL_SERVER_ERROR;
			result = "Requested saved search could not be retrieved.";
			LOGGER.error(e.toString());
		}

		return Response.status(responseCode).entity(result).type(MediaType.APPLICATION_JSON).build();
	}

	/**
	 * Deletes the saved search with the given id
	 * 
	 * @param id Id of the saved search
	 */
	public Response deleteSavedSearch(String id, SearchBean searchBean) {
		Object result = null;
		int responseCode;

		try {
			if (id == null) {
				throw new IllegalArgumentException("Saved search id must not be null.");
			}
			searchBean.delete(id);
			responseCode = HttpStatus.SC_OK;
		}
		catch (IllegalArgumentException e) {
			responseCode = HttpStatus.SC_BAD_REQUEST;
			result = e.getMessage();
			LOGGER.error(e.toString());
		}
		catch (Exception e) {
			responseCode = HttpStatus.SC_INTERNAL_SERVER_ERROR;
			result = "Saved search could not be deleted.";
			LOGGER.error(e.toString());
		}

		return Response.status(responseCode).entity(result).type(MediaType.APPLICATION_JSON).build();
	}

	/**
	 * Creates a saved search
	 * 
	 * @param name Name of the saved search
	 * @param type Type of the saved search
	 * @param indication Search criteria value for indication
	 * @param brandName Search criteria value for brand name
	 * @param genericName Search criteria value for generic name
	 * @param manufacturer Name Search criteria value for manufacturer name
	 * @param substanceName Search criteria value for substance name
	 * @param minAge Search criteria value for min age, in years
	 * @param maxAge Search criteria value for max age, in years
	 * @param minWeight Search criteria value for min weight, in pounds
	 * @param maxWeight Search criteria value for max weight, in pounds
	 * @param gender Search criteria value for gender
	 * @param route Search criteria value for route
	 * @return New saved search
	 */
	public Response createSavedSearch(String name, Search.SearchType type, String indication, String brandName, String genericName, String manufacturerName, String substanceName, Integer minAge,
	        Integer maxAge, Double minWeight, Double maxWeight, String gender, String route, SearchBean searchBean) {

		try {

			// decode values
			Encoder encoder = ESAPI.encoder();
			name = encoder.decodeFromURL(name);
			indication = encoder.decodeFromURL(indication);
			brandName = encoder.decodeFromURL(brandName);
			genericName = encoder.decodeFromURL(genericName);
			manufacturerName = encoder.decodeFromURL(manufacturerName);
			substanceName = encoder.decodeFromURL(substanceName);

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

			return Response.status(HttpStatus.SC_OK).entity(newSearch).type(MediaType.APPLICATION_JSON).build();
		}
		catch (EncodingException e) {
			LOGGER.error(e.getMessage(), e);
			return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity("Request parameters could not be decoded.").type(MediaType.APPLICATION_JSON).build();
		}
		catch (Exception e) {
			LOGGER.error(e.toString());
			return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity("Search '" + name + "' could not be saved.").type(MediaType.APPLICATION_JSON).build();
		}
	}
}
