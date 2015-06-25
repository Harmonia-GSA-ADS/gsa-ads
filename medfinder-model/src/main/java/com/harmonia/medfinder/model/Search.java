package com.harmonia.medfinder.model;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.harmonia.medfinder.converters.SearchTypeConverter;

/**
 * Entity representing an saved search.
 * 
 * @author keagan
 */
@Entity
@Table(name = "search")
@NamedQuery(name = Search.Q_FIND_BY_TYPE, query = "SELECT s FROM Search s WHERE s.type = :type")
public class Search {

	public static final String Q_FIND_BY_TYPE = "Search.findByType"; 
	
	/**
	 * Enumeration for search type
	 * 
	 * @author keagan
	 */
	public enum SearchType {
		ADVERSE_EVENTS, ROUTES, DRUGS
	}

	/**
	 * Unique id of the saved search
	 */
	@Id
	@Column(name = "ID", length = 36)
	private String id;

	/**
	 * Name of the saved search
	 */
	@Column(name = "name")
	private String name;

	/**
	 * Date and time that the search was saved
	 */
	@Column(name = "datetime")
	private Date datetime;

	/**
	 * Search type
	 */
	@Column(name = "type", length = 10, nullable = false)
	@Convert(converter = SearchTypeConverter.class)
	private SearchType type;

	/**
	 * Search criteria: Purpose of a drug
	 */
	@Column(name = "indication")
	private String indication;

	/**
	 * Search criteria: Brand name of a drug
	 */
	@Column(name = "brandName")
	private String brandName;

	/**
	 * Search criteria: Generic name of a drug
	 */
	@Column(name = "genericName")
	private String genericName;

	/**
	 * Search criteria: Manufacturer name of a drug
	 */
	@Column(name = "manufacturerName")
	private String manufacturerName;

	/**
	 * Search criteria: Substance name of a drug
	 */
	@Column(name = "substanceName")
	private String substanceName;

	/**
	 * Search criteria: Minimum age of a patient
	 */
	@Column(name = "minAge")
	private int minAge;

	/**
	 * Search criteria: Maximum age of a patient
	 */
	@Column(name = "maxAge")
	private int maxAge;

	/**
	 * Search criteria: Minimum weight of a patient
	 */
	@Column(name = "minWeight")
	private double minWeight;

	/**
	 * Search criteria: Maximum weight of a patient
	 */
	@Column(name = "maxWeight")
	private double maxWeight;

	/**
	 * Search criteria: Gender of a patient
	 */
	@Column(name = "gender")
	private String gender;

	/**
	 * Search criteria: Route of administration for a drug
	 */
	@Column(name = "route")
	private String route;

	/**
	 * Constructor. Sets id and datetime
	 */
	public Search() {
		this.id = UUID.randomUUID().toString();
		this.datetime = new Date();
	}

	/**
	 * Returns the unique id of the saved search
	 * 
	 * @return Unique id of the saved search
	 */
	public String getId() {
		return id;
	}

	/**
	 * Returns the name of the saved search
	 * 
	 * @return Name of the saved search
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the saved search
	 * 
	 * @param name
	 *            Name of the saved search
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the date and time that the search was saved
	 * 
	 * @return Date and time that the search was saved
	 */
	public Date getDatetime() {
		return datetime;
	}

	/**
	 * Returns the Search type
	 * 
	 * @return Search type
	 */
	public SearchType getType() {
		return type;
	}

	/**
	 * Sets the search type
	 * 
	 * @param type
	 *            Search type
	 */
	public void setType(SearchType type) {
		this.type = type;
	}

	/**
	 * Returns the search criteria: Purpose of a drug
	 * 
	 * @return Search criteria: Purpose of a drug
	 */
	public String getIndication() {
		return indication;
	}

	/**
	 * Sets the search criteria: Purpose of a drug
	 * 
	 * @param indication
	 *            Search criteria: Purpose of a drug
	 */
	public void setIndication(String indication) {
		this.indication = indication;
	}

	/**
	 * Returns the search criteria: Brand name of a drug
	 * 
	 * @return Search criteria: Brand name of a drug
	 */
	public String getBrandName() {
		return brandName;
	}

	/**
	 * Sets the search criteria: Brand name of a drug
	 * 
	 * @param brandName
	 *            Search criteria: Brand name of a drug
	 */
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	/**
	 * Returns the search criteria: Generic name of a drug
	 * 
	 * @return Search criteria: Generic name of a drug
	 */
	public String getGenericName() {
		return genericName;
	}

	/**
	 * Sets the search criteria: Generic name of a drug
	 * 
	 * @param genericName
	 *            Search criteria: Generic name of a drug
	 */
	public void setGenericName(String genericName) {
		this.genericName = genericName;
	}

	/**
	 * Returns the search criteria: Manufacturer name of a drug
	 * 
	 * @return Search criteria: Manufacturer name of a drug
	 */
	public String getManufacturerName() {
		return manufacturerName;
	}

	/**
	 * Sets the search criteria: Manufacturer name of a drug
	 * 
	 * @param manufacturerName
	 *            Search criteria: Manufacturer name of a drug
	 */
	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
	}

	/**
	 * Returns the search criteria: Substance name of a drug
	 * 
	 * @return Search criteria: Substance name of a drug
	 */
	public String getSubstanceName() {
		return substanceName;
	}

	/**
	 * Sets the search criteria: Substance name of a drug
	 * 
	 * @param substanceName
	 *            Search criteria: Substance name of a drug
	 */
	public void setSubstanceName(String substanceName) {
		this.substanceName = substanceName;
	}

	/**
	 * Returns the search criteria: Minimum age of a patient
	 * 
	 * @return Search criteria: Minimum age of a patient
	 */
	public int getMinAge() {
		return minAge;
	}

	/**
	 * Sets the search criteria: Minimum age of a patient
	 * 
	 * @param minAge
	 *            Search criteria: Minimum age of a patient
	 */
	public void setMinAge(int minAge) {
		this.minAge = minAge;
	}

	/**
	 * Returns the search criteria: Maximum age of a patient
	 * 
	 * @return Search criteria: Maximum age of a patient
	 */
	public int getMaxAge() {
		return maxAge;
	}

	/**
	 * Sets the search criteria: Maximum age of a patient
	 * 
	 * @param maxAge
	 *            Search criteria: Maximum age of a patient
	 */
	public void setMaxAge(int maxAge) {
		this.maxAge = maxAge;
	}

	/**
	 * Returns the search criteria: Minimum weight of a patient
	 * 
	 * @return Search criteria: Minimum weight of a patient
	 */
	public double getMinWeight() {
		return minWeight;
	}

	/**
	 * Sets the search criteria: Minimum weight of a patient
	 * 
	 * @param minWeight
	 *            Search criteria: Minimum weight of a patient
	 */
	public void setMinWeight(double minWeight) {
		this.minWeight = minWeight;
	}

	/**
	 * Returns the search criteria: Maximum weight of a patient
	 * 
	 * @return Search criteria: Maximum weight of a patient
	 */
	public double getMaxWeight() {
		return maxWeight;
	}

	/**
	 * Sets the search criteria: Maximum weight of a patient
	 * 
	 * @param maxWeight
	 *            Search criteria: Maximum weight of a patient
	 */
	public void setMaxWeight(double maxWeight) {
		this.maxWeight = maxWeight;
	}

	/**
	 * Returns the search criteria: Gender of a patient
	 * 
	 * @return Search criteria: Gender of a patient
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * Sets the search criteria: Gender of a patient
	 * 
	 * @param gender
	 *            Search criteria: Gender of a patient
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}

	/**
	 * Returns the search criteria: Route of administration for a drug
	 * 
	 * @return Search criteria: Route of administration for a drug
	 */
	public String getRoute() {
		return route;
	}

	/**
	 * Sets the search criteria: Route of administration for a drug
	 * 
	 * @param route
	 *            Search criteria: Route of administration for a drug
	 */
	public void setRoute(String route) {
		this.route = route;
	}
}
