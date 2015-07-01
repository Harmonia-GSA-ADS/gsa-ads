package com.harmonia.medfinder.servlet;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
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
import javax.ws.rs.core.Response;

import org.apache.http.HttpStatus;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.Encoder;
import org.owasp.esapi.errors.EncodingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.harmonia.medfinder.OpenFDASearcher;
import com.harmonia.medfinder.SavedSearchManager;
import com.harmonia.medfinder.ejb.bean.SearchBean;
import com.harmonia.medfinder.model.Search;

/**
 * REST endpoint for MedFinder operations
 * 
 * @author janway
 */
@Transactional
@Path("/")
@RequestScoped
public class MedFinderServlet {

	/**
	 * Static Logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(MedFinderServlet.class);

	/**
	 * Object that performs the business logic for this service.
	 */
	private OpenFDASearcher searcher = new OpenFDASearcher();

	/**
	 * Object that performs the business logic for saved searches.
	 */
	@Inject
	private SavedSearchManager savedSearchManager = new SavedSearchManager();

	/**
	 * Bean for saved search related operations
	 */
	@EJB
	private SearchBean searchBean;

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
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("events")
	public Response getAdverseDrugEvents(@QueryParam("ageStart") String ageStart, @QueryParam("ageEnd") String ageEnd, @QueryParam("dateStart") String dateStart,
	        @QueryParam("dateEnd") String dateEnd, @QueryParam("weightStart") String weightStart, @QueryParam("weightEnd") String weightEnd, @QueryParam("gender") String gender,
	        @QueryParam("indication") String indication, @QueryParam("brandName") String brandName, @QueryParam("genericName") String genericName,
	        @QueryParam("manufacturerName") String manufacturerName, @QueryParam("substanceName") String substanceName, @QueryParam("limit") Integer limit) {

		try {

			// decode values from client
			Encoder encoder = ESAPI.encoder();
			indication = encoder.decodeFromURL(indication);
			brandName = encoder.decodeFromURL(brandName);
			genericName = encoder.decodeFromURL(genericName);
			manufacturerName = encoder.decodeFromURL(manufacturerName);
			substanceName = encoder.decodeFromURL(substanceName);

			return searcher.getAdverseDrugEvents(ageStart, ageEnd, dateStart, dateEnd, weightStart, weightEnd, gender, indication, brandName, genericName, manufacturerName, substanceName, limit);
		}
		catch (EncodingException e) {
			LOGGER.error(e.getMessage(), e);
			return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity("Search parameters could not be decoded.").type(MediaType.APPLICATION_JSON).build();
		}
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
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/routes")
	public Response getRoutes(@QueryParam("indication") String indication, @QueryParam("brandName") String brandName, @QueryParam("genericName") String genericName,
	        @QueryParam("manufacturerName") String manufacturerName, @QueryParam("substanceName") String substanceName, @QueryParam("limit") Integer limit) {

		try {
			// decode values
			Encoder encoder = ESAPI.encoder();
			indication = encoder.decodeFromURL(indication);
			brandName = encoder.decodeFromURL(brandName);
			genericName = encoder.decodeFromURL(genericName);
			manufacturerName = encoder.decodeFromURL(manufacturerName);
			substanceName = encoder.decodeFromURL(substanceName);

			return searcher.getRoutes(indication, brandName, genericName, manufacturerName, substanceName, limit);
		}
		catch (EncodingException e) {
			LOGGER.error(e.getMessage(), e);
			return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity("Search parameters could not be decoded.").type(MediaType.APPLICATION_JSON).build();
		}
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
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/drugs")
	public Response getDrugs(@QueryParam("indication") String indication, @QueryParam("route") String route, @QueryParam("limit") Integer limit) {

		try {
			// decode values
			Encoder encoder = ESAPI.encoder();
			indication = encoder.decodeFromURL(indication);

			return searcher.getDrugs(indication, route, limit);
		}
		catch (EncodingException e) {
			LOGGER.error(e.getMessage(), e);
			return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity("Search parameters could not be decoded.").type(MediaType.APPLICATION_JSON).build();
		}
	}

	/**
	 * Returns all saved searches with the given type
	 * 
	 * @param type Saved search type
	 * @return List of all saved searches with the given type
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/searches")
	public Response getSavedSearchesOfType(@QueryParam("type") Search.SearchType type) {
		Object result = "";
		int responseCode;

		try {
			result = savedSearchManager.getSavedSearchesOfType(type);
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
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/search")
	public Response getSearchById(@QueryParam("id") String id) {
		Object result = "";
		int responseCode;

		try {
			result = searchBean.findById(id);
			responseCode = HttpStatus.SC_OK;
			result = savedSearchManager.getSearchById(id);
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
	@DELETE
	@Path("/search/{id}")
	public Response deleteSavedSearch(@PathParam("id") String id) {
		Object result = null;
		int responseCode;

		try {
			savedSearchManager.deleteSavedSearch(id);
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
	 * @param manufacturerName Search criteria value for manufacturer name
	 * @param substanceName Search criteria value for substance name
	 * @param minAge Search criteria value for min age, in years
	 * @param maxAge Search criteria value for max age, in years
	 * @param minWeight Search criteria value for min weight, in pounds
	 * @param maxWeight Search criteria value for max weight, in pounds
	 * @param gender Search criteria value for gender
	 * @param route Search criteria value for route
	 * @return New saved search
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Path("/search")
	public Response createSavedSearch(@FormParam("name") String name, @FormParam("type") Search.SearchType type, @FormParam("indication") String indication, @FormParam("brandName") String brandName,
	        @FormParam("genericName") String genericName, @FormParam("manufacturerName") String manufacturerName, @FormParam("substanceName") String substanceName,
	        @FormParam("minAge") Integer minAge, @FormParam("maxAge") Integer maxAge, @FormParam("minWeight") Double minWeight, @FormParam("maxWeight") Double maxWeight,
	        @FormParam("gender") String gender, @FormParam("route") String route) {

		try {

			// decode values
			Encoder encoder = ESAPI.encoder();
			name = encoder.decodeFromURL(name);
			indication = encoder.decodeFromURL(indication);
			brandName = encoder.decodeFromURL(brandName);
			genericName = encoder.decodeFromURL(genericName);
			manufacturerName = encoder.decodeFromURL(manufacturerName);
			substanceName = encoder.decodeFromURL(substanceName);

			Search search = savedSearchManager.createSavedSearch(name, type, indication, brandName, genericName, manufacturerName, substanceName, minAge, maxAge, minWeight, maxWeight, gender, route);
			return Response.status(HttpStatus.SC_OK).entity(search).type(MediaType.APPLICATION_JSON).build();
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
