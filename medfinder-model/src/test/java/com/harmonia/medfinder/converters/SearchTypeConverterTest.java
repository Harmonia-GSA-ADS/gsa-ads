package com.harmonia.medfinder.converters;

import junit.framework.Assert;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.harmonia.medfinder.converters.SearchTypeConverter;
import com.harmonia.medfinder.model.Search.SearchType;

/**
 * Test class for {@link SearchTypeConverter}
 * 
 * @author keagan
 */
public class SearchTypeConverterTest {

	/**
	 * Enable exception testing
	 */
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	/**
	 * Tests {@link SearchTypeConverter#convertToDatabaseColumn(SearchType)}
	 */
	@Test
	public void testConvertToDatabaseColumn() {
		SearchTypeConverter converter = new SearchTypeConverter();

		String dbAdverseEvents = converter.convertToDatabaseColumn(SearchType.ADVERSE_EVENTS);
		String dbDrugs = converter.convertToDatabaseColumn(SearchType.DRUGS);
		String dbRoutes = converter.convertToDatabaseColumn(SearchType.ROUTES);

		Assert.assertEquals("ADVERSE_EVENTS was not properly converted.", "A", dbAdverseEvents);
		Assert.assertEquals("DRUGS was not properly converted.", "D", dbDrugs);
		Assert.assertEquals("ROUTES was not properly converted.", "R", dbRoutes);
	}

	/**
	 * Tests {@link SearchTypeConverter#convertToEntityAttribute(String)}
	 */
	@Test
	public void testConvertToEntityAttribute() {
		SearchTypeConverter converter = new SearchTypeConverter();

		SearchType eaAdverseEvents = converter.convertToEntityAttribute("A");
		SearchType eaDrugs = converter.convertToEntityAttribute("D");
		SearchType eaRoutes = converter.convertToEntityAttribute("R");

		Assert.assertEquals("ADVERSE_EVENTS entity was not properly resolved.", SearchType.ADVERSE_EVENTS, eaAdverseEvents);
		Assert.assertEquals("DRUGS entity was not properly resolved.", SearchType.DRUGS, eaDrugs);
		Assert.assertEquals("ROUTES entity was not properly resolved.", SearchType.ROUTES, eaRoutes);
	}

	/**
	 * Tests {@link SearchTypeConverter#convertToEntityAttribute(String)} for an
	 * invalid string value
	 */
	@Test
	public void testInvalidData() {
		thrown.expect(IllegalArgumentException.class);

		SearchTypeConverter converter = new SearchTypeConverter();
		converter.convertToEntityAttribute("F");
	}

}
