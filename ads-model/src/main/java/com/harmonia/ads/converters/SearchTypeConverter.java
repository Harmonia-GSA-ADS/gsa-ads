package com.harmonia.ads.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.harmonia.ads.model.Search.SearchType;

/**
 * Type converter to convert between SearchType values used in the application
 * and String values which are stored in the database.
 * 
 * @author keagan
 */
@Converter(autoApply = true)
public class SearchTypeConverter implements
		AttributeConverter<SearchType, String> {
	/**
	 * Converts a SearchType used in the application to a string to be stored in
	 * the database
	 * 
	 * @param type
	 *            the SearchType to be converted
	 * @return a string representation of the SearchType, which can be stored in
	 *         the database
	 * @throws IllegalArgumentException
	 *             if type does not correspond to a known SearchType
	 */
	@Override
	public String convertToDatabaseColumn(SearchType type)
			throws IllegalArgumentException {
		switch (type) {
		case ADVERSE_EVENTS:
			return "A";
		case ROUTES:
			return "R";
		case DRUGS:
			return "D";
		default:
			throw new IllegalArgumentException("Unknown value: " + type);
		}
	}

	/**
	 * Converts a string value from the database to its associated SearchType
	 * 
	 * @param dbData
	 *            the string representation of the SearchType from the database.
	 * @return the SearchType which corresponds to the dbData string.
	 * @throws IllegalArgumentException
	 *             if dbData does not correspond to a known SearchType
	 */
	@Override
	public SearchType convertToEntityAttribute(String dbData)
			throws IllegalArgumentException {
		switch (dbData) {
		case "A":
			return SearchType.ADVERSE_EVENTS;
		case "R":
			return SearchType.ROUTES;
		case "D":
			return SearchType.DRUGS;
		default:
			throw new IllegalArgumentException("Unknown value: " + dbData);
		}
	}
}
