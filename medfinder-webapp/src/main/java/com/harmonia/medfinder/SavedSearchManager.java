package com.harmonia.medfinder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;

import com.harmonia.medfinder.ejb.bean.SearchBean;
import com.harmonia.medfinder.model.Search;

/**
 * Class for dealing with saved searches
 * 
 * @author keagan
 * @author janway
 */
@ApplicationScoped
@Named
public class SavedSearchManager {

	/**
	 * Bean to use for saved search activities
	 */
	@EJB
	private SearchBean searchBean;

	/**
	 * Returns all saved searches with the given type
	 * 
	 * @param type Saved search type
	 * @return List of all saved searches with the given type
	 * @throws IllegalArgumentException if type is null
	 */
	public List<Search> getSavedSearchesOfType(Search.SearchType type) {
		if (type == null) {
			throw new IllegalArgumentException("Saved search type must not be null.");
		}
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("type", type);
		return searchBean.findWithNamedQuery(Search.Q_FIND_BY_TYPE, param);
	}

	/**
	 * Returns the saved search with the given id
	 * 
	 * @param id Id of the saved search
	 * @return Saved search with the given id
	 * @throws IllegalArgumentException if id is null
	 */
	public Search getSearchById(String id) {
		if (StringUtils.isBlank(id)) {
			throw new IllegalArgumentException("Saved search id must not be blank.");
		}
		return searchBean.findById(id);
	}

	/**
	 * Deletes the saved search with the given id
	 * 
	 * @param id Id of the saved search
	 * @throws IllegalArgumentException if id is blank
	 */
	public void deleteSavedSearch(String id) {
		if (StringUtils.isBlank(id)) {
			throw new IllegalArgumentException("Saved search id must not be blank.");
		}
		searchBean.delete(id);
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
	public Search createSavedSearch(String name, Search.SearchType type, String indication, String brandName, String genericName, String manufacturerName, String substanceName, Integer minAge,
	        Integer maxAge, Double minWeight, Double maxWeight, String gender, String route) {

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
