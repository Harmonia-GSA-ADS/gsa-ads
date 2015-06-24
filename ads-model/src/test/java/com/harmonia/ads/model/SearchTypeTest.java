package com.harmonia.ads.model;

import org.junit.Assert;
import org.junit.Test;

import com.harmonia.ads.model.Search.SearchType;

/**
 * A test class for the {@link SearchType} enumeration
 * 
 * @author keagan
 */
public class SearchTypeTest {

	/**
	 * Tests {@link SearchType#values()}
	 */
	@Test
	public void testValues() {
		SearchType[] values = SearchType.values();
		Assert.assertEquals(
				"Number of enum values does not match expected value.", 3,
				values.length);
	}

	/**
	 * Tests {@link SearchType#valueOf(String)}
	 */
	@Test
	public void testValueOf() {
		SearchType type = SearchType.valueOf(SearchType.ADVERSE_EVENTS
				.toString());
		Assert.assertEquals("Type does not match expected value.",
				SearchType.ADVERSE_EVENTS, type);

	}
}
