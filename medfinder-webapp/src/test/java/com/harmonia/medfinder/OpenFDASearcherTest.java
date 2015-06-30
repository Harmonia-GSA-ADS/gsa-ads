package com.harmonia.medfinder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.ws.rs.core.Response;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.owasp.esapi.errors.EncodingException;

/**
 * Test class for {@link OpenFDASearcher}
 * 
 * @author keagan
 */
public class OpenFDASearcherTest {

	/**
	 * OpenFDA search instance
	 */
	private OpenFDASearcher searcher;

	/**
	 * Setup for the test
	 * 
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	@Before
	public void beforeTest() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		searcher = new OpenFDASearcher();
	}

	/**
	 * Tests
	 * {@link OpenFDASearcher#getAdverseDrugEvents(String, String, String, String, String, String, String, String, String, String, String, String, Integer)}
	 * with age criteria
	 * 
	 * @throws EncodingException
	 * @throws InterruptedException
	 */
	@Test
	public void testGetAdverseDrugEventsAge() throws EncodingException, InterruptedException {
		Response resp = searcher.getAdverseDrugEvents("15", "25", null, null, null, null, null, null, null, null, null, null, null);
		assertTrue("Response was not a JSON object.", ((String)resp.getEntity()).startsWith("{"));
		JSONObject obj = new JSONObject((String)resp.getEntity());
		assertEquals("Expected five results.", 5, obj.getJSONObject("meta").getJSONObject("results").getInt("limit"));
		Thread.sleep(2000); // prevent rate limit error
	}

	/**
	 * Tests
	 * {@link OpenFDASearcher#getAdverseDrugEvents(String, String, String, String, String, String, String, String, String, String, String, String, Integer)}
	 * with weight criteria
	 * 
	 * @throws EncodingException
	 * @throws InterruptedException
	 */
	@Test
	public void testGetAdverseDrugEventsWeight() throws EncodingException, InterruptedException {
		Response resp = searcher.getAdverseDrugEvents(null, null, null, null, "20", "40", null, null, null, null, null, null, null);
		assertTrue("Response was not a JSON object.", ((String)resp.getEntity()).startsWith("{"));
		JSONObject obj = new JSONObject((String)resp.getEntity());
		assertEquals("Expected five results.", 5, obj.getJSONObject("meta").getJSONObject("results").getInt("limit"));
		Thread.sleep(2000); // prevent rate limit error
	}

	/**
	 * Tests
	 * {@link OpenFDASearcher#getAdverseDrugEvents(String, String, String, String, String, String, String, String, String, String, String, String, Integer)}
	 * with gender criteria
	 * 
	 * @throws EncodingException
	 * @throws InterruptedException
	 */
	@Test
	public void testGetAdverseDrugEventsGender() throws EncodingException, InterruptedException {
		Response resp = searcher.getAdverseDrugEvents(null, null, null, null, null, null, "male", null, null, null, null, null, null);
		assertTrue("Response was not a JSON object.", ((String)resp.getEntity()).startsWith("{"));
		JSONObject obj = new JSONObject((String)resp.getEntity());
		assertEquals("Expected five results.", 5, obj.getJSONObject("meta").getJSONObject("results").getInt("limit"));
		Thread.sleep(2000); // prevent rate limit error
	}

	/**
	 * Tests
	 * {@link OpenFDASearcher#getAdverseDrugEvents(String, String, String, String, String, String, String, String, String, String, String, String, Integer)}
	 * with indication criteria
	 * 
	 * @throws EncodingException
	 * @throws InterruptedException
	 */
	@Test
	public void testGetAdverseDrugEventsIndication() throws EncodingException, InterruptedException {
		Response resp = searcher.getAdverseDrugEvents(null, null, null, null, null, null, null, "headache", null, null, null, null, null);
		assertTrue("Response was not a JSON object.", ((String)resp.getEntity()).startsWith("{"));
		JSONObject obj = new JSONObject((String)resp.getEntity());
		assertEquals("Expected five results.", 5, obj.getJSONObject("meta").getJSONObject("results").getInt("limit"));
		Thread.sleep(2000); // prevent rate limit error
	}

	/**
	 * Tests
	 * {@link OpenFDASearcher#getAdverseDrugEvents(String, String, String, String, String, String, String, String, String, String, String, String, Integer)}
	 * with brand name criteria
	 * 
	 * @throws EncodingException
	 * @throws InterruptedException
	 */
	@Test
	public void testGetAdverseDrugEventsBrand() throws EncodingException, InterruptedException {
		Response resp = searcher.getAdverseDrugEvents(null, null, null, null, null, null, null, null, "tylenol", null, null, null, null);
		assertTrue("Response was not a JSON object.", ((String)resp.getEntity()).startsWith("{"));
		JSONObject obj = new JSONObject((String)resp.getEntity());
		assertEquals("Expected five results.", 5, obj.getJSONObject("meta").getJSONObject("results").getInt("limit"));
		Thread.sleep(2000); // prevent rate limit error
	}

	/**
	 * Tests
	 * {@link OpenFDASearcher#getAdverseDrugEvents(String, String, String, String, String, String, String, String, String, String, String, String, Integer)}
	 * with generic name criteria
	 * 
	 * @throws EncodingException
	 * @throws InterruptedException
	 */
	@Test
	public void testGetAdverseDrugEventsGeneric() throws EncodingException, InterruptedException {
		Response resp = searcher.getAdverseDrugEvents(null, null, null, null, null, null, null, null, null, "ibuprofen", null, null, null);
		assertTrue("Response was not a JSON object.", ((String)resp.getEntity()).startsWith("{"));
		JSONObject obj = new JSONObject((String)resp.getEntity());
		assertEquals("Expected five results.", 5, obj.getJSONObject("meta").getJSONObject("results").getInt("limit"));
		Thread.sleep(2000); // prevent rate limit error
	}

	/**
	 * Tests
	 * {@link OpenFDASearcher#getAdverseDrugEvents(String, String, String, String, String, String, String, String, String, String, String, String, Integer)}
	 * with manufacturer name criteria
	 * 
	 * @throws EncodingException
	 * @throws InterruptedException
	 */
	@Test
	public void testGetAdverseDrugEventsManufacturer() throws EncodingException, InterruptedException {
		Response resp = searcher.getAdverseDrugEvents(null, null, null, null, null, null, null, null, null, null, "Company", null, null);
		assertTrue("Response was not a JSON object.", ((String)resp.getEntity()).startsWith("{"));
		JSONObject obj = new JSONObject((String)resp.getEntity());
		assertEquals("Expected five results.", 5, obj.getJSONObject("meta").getJSONObject("results").getInt("limit"));
		Thread.sleep(2000); // prevent rate limit error
	}

	/**
	 * Tests
	 * {@link OpenFDASearcher#getAdverseDrugEvents(String, String, String, String, String, String, String, String, String, String, String, String, Integer)}
	 * with substance name criteria
	 * 
	 * @throws EncodingException
	 * @throws InterruptedException
	 */
	@Test
	public void testGetAdverseDrugEventsSubstance() throws EncodingException, InterruptedException {
		Response resp = searcher.getAdverseDrugEvents(null, null, null, null, null, null, null, null, null, null, null, "ibuprofen", null);
		assertTrue("Response was not a JSON object.", ((String)resp.getEntity()).startsWith("{"));
		JSONObject obj = new JSONObject((String)resp.getEntity());
		assertEquals("Expected five results.", 5, obj.getJSONObject("meta").getJSONObject("results").getInt("limit"));
		Thread.sleep(2000); // prevent rate limit error
	}

	/**
	 * Tests
	 * {@link OpenFDASearcher#getAdverseDrugEvents(String, String, String, String, String, String, String, String, String, String, String, String, Integer)}
	 * with a result limit
	 * 
	 * @throws EncodingException
	 * @throws InterruptedException
	 */
	@Test
	public void testGetAdverseDrugEventsLimit() throws EncodingException, InterruptedException {
		Response resp = searcher.getAdverseDrugEvents(null, null, null, null, null, null, null, null, null, null, null, null, 10);
		assertTrue("Response was not a JSON object.", ((String)resp.getEntity()).startsWith("{"));
		JSONObject obj = new JSONObject((String)resp.getEntity());
		assertEquals("Expected ten results.", 10, obj.getJSONObject("meta").getJSONObject("results").getInt("limit"));
		Thread.sleep(2000); // prevent rate limit error
	}

	/**
	 * Tests
	 * {@link OpenFDASearcher#getRoutes(String, String, String, String, String, Integer)}
	 * with indication criteria
	 * 
	 * @throws EncodingException
	 * @throws InterruptedException
	 */
	@Test
	public void testGetRoutesIndication() throws EncodingException, InterruptedException {
		Response resp = searcher.getRoutes("headache", null, null, null, null, null);
		assertTrue("Response was not a JSON object.", ((String)resp.getEntity()).startsWith("{"));
		JSONObject obj = new JSONObject((String)resp.getEntity());
		assertEquals("Expected five results.", 5, obj.getJSONObject("meta").getJSONObject("results").getInt("limit"));
		Thread.sleep(2000); // prevent rate limit error
	}

	/**
	 * Tests
	 * {@link OpenFDASearcher#getRoutes(String, String, String, String, String, Integer)}
	 * with brand name criteria
	 * 
	 * @throws EncodingException
	 * @throws InterruptedException
	 */
	@Test
	public void testGetRoutesBrand() throws EncodingException, InterruptedException {
		Response resp = searcher.getRoutes(null, "tylenol", null, null, null, null);
		assertTrue("Response was not a JSON object.", ((String)resp.getEntity()).startsWith("{"));
		JSONObject obj = new JSONObject((String)resp.getEntity());
		assertEquals("Expected five results.", 5, obj.getJSONObject("meta").getJSONObject("results").getInt("limit"));
		Thread.sleep(2000); // prevent rate limit error
	}

	/**
	 * Tests
	 * {@link OpenFDASearcher#getRoutes(String, String, String, String, String, Integer)}
	 * with generic name criteria
	 * 
	 * @throws EncodingException
	 * @throws InterruptedException
	 */
	@Test
	public void testGetRoutesGeneric() throws EncodingException, InterruptedException {
		Response resp = searcher.getRoutes(null, null, "ibuprofen", null, null, null);
		assertTrue("Response was not a JSON object.", ((String)resp.getEntity()).startsWith("{"));
		JSONObject obj = new JSONObject((String)resp.getEntity());
		assertEquals("Expected five results.", 5, obj.getJSONObject("meta").getJSONObject("results").getInt("limit"));
		Thread.sleep(2000); // prevent rate limit error
	}

	/**
	 * Tests
	 * {@link OpenFDASearcher#getRoutes(String, String, String, String, String, Integer)}
	 * with manufacturer name criteria
	 * 
	 * @throws EncodingException
	 * @throws InterruptedException
	 */
	@Test
	public void testGetRoutesManufacturer() throws EncodingException, InterruptedException {
		Response resp = searcher.getRoutes(null, null, null, "Company", null, null);
		assertTrue("Response was not a JSON object.", ((String)resp.getEntity()).startsWith("{"));
		JSONObject obj = new JSONObject((String)resp.getEntity());
		assertEquals("Expected five results.", 5, obj.getJSONObject("meta").getJSONObject("results").getInt("limit"));
		Thread.sleep(2000); // prevent rate limit error
	}

	/**
	 * Tests
	 * {@link OpenFDASearcher#getRoutes(String, String, String, String, String, Integer)}
	 * with substance name criteria
	 * 
	 * @throws EncodingException
	 * @throws InterruptedException
	 */
	@Test
	public void testGetRoutesSubstance() throws EncodingException, InterruptedException {
		Response resp = searcher.getRoutes(null, null, null, null, "ibuprofen", null);
		assertTrue("Response was not a JSON object.", ((String)resp.getEntity()).startsWith("{"));
		JSONObject obj = new JSONObject((String)resp.getEntity());
		assertEquals("Expected five results.", 5, obj.getJSONObject("meta").getJSONObject("results").getInt("limit"));
		Thread.sleep(2000); // prevent rate limit error
	}

	/**
	 * Tests
	 * {@link OpenFDASearcher#getRoutes(String, String, String, String, String, Integer)}
	 * with a result limit
	 * 
	 * @throws EncodingException
	 * @throws InterruptedException
	 */
	@Test
	public void testGetRoutesLimit() throws EncodingException, InterruptedException {
		Response resp = searcher.getRoutes(null, null, null, null, null, 10);
		assertTrue("Response was not a JSON object.", ((String)resp.getEntity()).startsWith("{"));
		JSONObject obj = new JSONObject((String)resp.getEntity());
		assertEquals("Expected ten results.", 10, obj.getJSONObject("meta").getJSONObject("results").getInt("limit"));
		Thread.sleep(2000); // prevent rate limit error
	}

	/**
	 * Tests {@link OpenFDASearcher#getDrugs(String, String, Integer)}
	 * 
	 * @throws EncodingException
	 * @throws InterruptedException
	 */
	@Test
	public void testGetDrugs() throws EncodingException, InterruptedException {
		Response resp = searcher.getDrugs("headache", "oral", null);
		assertTrue("Response was not a JSON object.", ((String)resp.getEntity()).startsWith("{"));
		JSONObject obj = new JSONObject((String)resp.getEntity());
		assertEquals("Expected five results.", 5, obj.getJSONObject("meta").getJSONObject("results").getInt("limit"));
		Thread.sleep(2000); // prevent rate limit error
	}

	/**
	 * Tests {@link OpenFDASearcher#getDrugs(String, String, Integer)} with a
	 * result limit
	 * 
	 * @throws EncodingException
	 * @throws InterruptedException
	 */
	@Test
	public void testGetDrugsLimit() throws EncodingException, InterruptedException {
		Response resp = searcher.getDrugs("headache", "oral", 10);
		assertTrue("Response was not a JSON object.", ((String)resp.getEntity()).startsWith("{"));
		JSONObject obj = new JSONObject((String)resp.getEntity());
		assertEquals("Expected ten results.", 10, obj.getJSONObject("meta").getJSONObject("results").getInt("limit"));
		Thread.sleep(2000); // prevent rate limit error
	}
}
