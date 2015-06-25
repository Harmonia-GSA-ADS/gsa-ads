package com.harmonia.medfinder.model;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import com.harmonia.medfinder.model.Search;
import com.harmonia.medfinder.model.Search.SearchType;

/**
 * A test class for the {@link Search} model
 * 
 * @author keagan
 */
public class SearchTest {

	/**
	 * Tests {@link Search#getId()}
	 */
	@Test
	public void testGetId() {
		Search search = new Search();
		String id = search.getId();

		Assert.assertNotNull("Search id is null.", id);
		Assert.assertEquals("Id is of unexpected length.", 36, id.length());
	}

	/**
	 * Tests {@link Search#getDatetime()}
	 */
	@Test
	public void testGetDatetime() {
		Search search = new Search();
		Date datetime = search.getDatetime();

		Assert.assertNotNull("Search data is null.", datetime);
	}

	/**
	 * Tests {@link Search#getBrandName()} and
	 * {@link Search#setBrandName(String)}
	 */
	@Test
	public void testGetSetBrandName() {
		Search search = new Search();

		String brandName = "Brand Name";
		search.setBrandName(brandName);

		Assert.assertEquals("Brand name does not match expected value.",
				brandName, search.getBrandName());
	}

	/**
	 * Tests {@link Search#getGender()} and {@link Search#setGender(String)}
	 */
	@Test
	public void testGetSetGender() {
		Search search = new Search();

		String gender = "Male";
		search.setGender(gender);

		Assert.assertEquals("Gender does not match expected value.", gender,
				search.getGender());
	}

	/**
	 * Tests {@link Search#getGenericName()} and
	 * {@link Search#setGenericName(String)}
	 */
	@Test
	public void testGetSetGenericName() {
		Search search = new Search();

		String genericName = "Generic Name";
		search.setGenericName(genericName);

		Assert.assertEquals("Generic name does not match expected value.",
				genericName, search.getGenericName());
	}

	/**
	 * Tests {@link Search#getIndication()} and
	 * {@link Search#setIndication(String)}
	 */
	@Test
	public void testGetSetIndication() {
		Search search = new Search();

		String indication = "Indication";
		search.setIndication(indication);

		Assert.assertEquals("Indication does not match expected value.",
				indication, search.getIndication());
	}

	/**
	 * Tests {@link Search#getManufacturerName()} and
	 * {@link Search#setManufacturerName(String)}
	 */
	@Test
	public void testGetSetManufacturerName() {
		Search search = new Search();

		String manufacturerName = "Manufacturer Name";
		search.setManufacturerName(manufacturerName);

		Assert.assertEquals("Manufacturer name does not match expected value.",
				manufacturerName, search.getManufacturerName());
	}

	/**
	 * Tests {@link Search#getMinAge()} and {@link Search#setMinAge(int)}
	 */
	@Test
	public void testGetSetMinAge() {
		Search search = new Search();

		int minAge = 20;
		search.setMinAge(minAge);

		Assert.assertEquals("Min age does not match expected value.", minAge,
				search.getMinAge());
	}

	/**
	 * Tests {@link Search#getMaxAge()} and {@link Search#setMaxAge(int)}
	 */
	@Test
	public void testGetSetMaxAge() {
		Search search = new Search();

		int maxAge = 45;
		search.setMaxAge(maxAge);

		Assert.assertEquals("Max age does not match expected value.", maxAge,
				search.getMaxAge());
	}

	/**
	 * Tests {@link Search#getMinWeight()} and
	 * {@link Search#setMinWeight(double))}
	 */
	@Test
	public void testGetSetMinWeight() {
		Search search = new Search();

		double minWeight = 110.5;
		search.setMinWeight(minWeight);

		Assert.assertEquals("Min weight does not match expected value.",
				minWeight, search.getMinWeight(), 0);
	}

	/**
	 * Tests {@link Search#getMaxWeight()} and
	 * {@link Search#setMaxWeight(double))}
	 */
	@Test
	public void testGetSetMaxWeight() {
		Search search = new Search();

		double maxWeight = 150.7;
		search.setMaxWeight(maxWeight);

		Assert.assertEquals("Max weight does not match expected value.",
				maxWeight, search.getMaxWeight(), 0);
	}

	/**
	 * Tests {@link Search#getName()} and {@link Search#setName(String))}
	 */
	@Test
	public void testGetSetName() {
		Search search = new Search();

		String name = "Name";
		search.setName(name);

		Assert.assertEquals("Name does not match expected value.", name,
				search.getName());
	}

	/**
	 * Tests {@link Search#getRoute()} and {@link Search#setRoute(String))}
	 */
	@Test
	public void testGetSetRoute() {
		Search search = new Search();

		String route = "Route";
		search.setRoute(route);

		Assert.assertEquals("Route does not match expected value.", route,
				search.getRoute());
	}

	/**
	 * Tests {@link Search#getSubstanceName()} and
	 * {@link Search#setSubstanceName(String))}
	 */
	@Test
	public void testGetSetSubstanceName() {
		Search search = new Search();

		String substanceName = "Substance Name";
		search.setSubstanceName(substanceName);

		Assert.assertEquals("Substance name does not match expected value.",
				substanceName, search.getSubstanceName());
	}

	/**
	 * Tests {@link Search#getType()} and {@link Search#setType(SearchType))}
	 */
	@Test
	public void testGetSetType() {
		Search search = new Search();
		SearchType type = SearchType.ADVERSE_EVENTS;
		search.setType(type);

		Assert.assertEquals("Search type does not match expected value.", type,
				search.getType());
	}
}
