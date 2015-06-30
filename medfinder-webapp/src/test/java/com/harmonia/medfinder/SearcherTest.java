package com.harmonia.medfinder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.apache.commons.httpclient.HttpStatus;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.owasp.esapi.errors.EncodingException;

import com.harmonia.medfinder.ejb.bean.SearchBean;
import com.harmonia.medfinder.model.Search;

public class SearcherTest {

	private Searcher searcher;

	private SearchBean searchBeanMock;

	@Before
	public void beforeTest() throws NoSuchFieldException, SecurityException,
			IllegalArgumentException, IllegalAccessException {
		searcher = new Searcher();
		searchBeanMock = mock(SearchBean.class);
	}

	@Test
	public void testGetAdverseDrugEventsAge() throws EncodingException {
		Response resp = searcher.getAdverseDrugEvents("15", "25", null, null,
				null, null, null, null, null, null, null, null, null);
		assertTrue("Response was not a JSON object.",
				((String) resp.getEntity()).startsWith("{"));
		JSONObject obj = new JSONObject((String) resp.getEntity());
		assertEquals("Expected five results.", 5, obj.getJSONObject("meta")
				.getJSONObject("results").getInt("limit"));
	}

	@Test
	public void testGetAdverseDrugEventsWeight() throws EncodingException {
		Response resp = searcher.getAdverseDrugEvents(null, null, null, null,
				"20", "40", null, null, null, null, null, null, null);
		assertTrue("Response was not a JSON object.",
				((String) resp.getEntity()).startsWith("{"));
		JSONObject obj = new JSONObject((String) resp.getEntity());
		assertEquals("Expected five results.", 5, obj.getJSONObject("meta")
				.getJSONObject("results").getInt("limit"));
	}

	@Test
	public void testGetAdverseDrugEventsGender() throws EncodingException {
		Response resp = searcher.getAdverseDrugEvents(null, null, null, null,
				null, null, "male", null, null, null, null, null, null);
		assertTrue("Response was not a JSON object.",
				((String) resp.getEntity()).startsWith("{"));
		JSONObject obj = new JSONObject((String) resp.getEntity());
		assertEquals("Expected five results.", 5, obj.getJSONObject("meta")
				.getJSONObject("results").getInt("limit"));
	}

	@Test
	public void testGetAdverseDrugEventsIndication() throws EncodingException {
		Response resp = searcher.getAdverseDrugEvents(null, null, null, null,
				null, null, null, "headache", null, null, null, null, null);
		assertTrue("Response was not a JSON object.",
				((String) resp.getEntity()).startsWith("{"));
		JSONObject obj = new JSONObject((String) resp.getEntity());
		assertEquals("Expected five results.", 5, obj.getJSONObject("meta")
				.getJSONObject("results").getInt("limit"));
	}

	@Test
	public void testGetAdverseDrugEventsBrand() throws EncodingException {
		Response resp = searcher.getAdverseDrugEvents(null, null, null, null,
				null, null, null, null, "tylenol", null, null, null, null);
		assertTrue("Response was not a JSON object.",
				((String) resp.getEntity()).startsWith("{"));
		JSONObject obj = new JSONObject((String) resp.getEntity());
		assertEquals("Expected five results.", 5, obj.getJSONObject("meta")
				.getJSONObject("results").getInt("limit"));
	}

	@Test
	public void testGetAdverseDrugEventsGeneric() throws EncodingException {
		Response resp = searcher.getAdverseDrugEvents(null, null, null, null,
				null, null, null, null, null, "ibuprofen", null, null, null);
		assertTrue("Response was not a JSON object.",
				((String) resp.getEntity()).startsWith("{"));
		JSONObject obj = new JSONObject((String) resp.getEntity());
		assertEquals("Expected five results.", 5, obj.getJSONObject("meta")
				.getJSONObject("results").getInt("limit"));
	}

	@Test
	public void testGetAdverseDrugEventsManufacturer() throws EncodingException {
		Response resp = searcher.getAdverseDrugEvents(null, null, null, null,
				null, null, null, null, null, null, "Company", null, null);
		assertTrue("Response was not a JSON object.",
				((String) resp.getEntity()).startsWith("{"));
		JSONObject obj = new JSONObject((String) resp.getEntity());
		assertEquals("Expected five results.", 5, obj.getJSONObject("meta")
				.getJSONObject("results").getInt("limit"));
	}

	@Test
	public void testGetAdverseDrugEventsSubstance() throws EncodingException {
		Response resp = searcher.getAdverseDrugEvents(null, null, null, null,
				null, null, null, null, null, null, null, "ibuprofen", null);
		assertTrue("Response was not a JSON object.",
				((String) resp.getEntity()).startsWith("{"));
		JSONObject obj = new JSONObject((String) resp.getEntity());
		assertEquals("Expected five results.", 5, obj.getJSONObject("meta")
				.getJSONObject("results").getInt("limit"));
	}

	@Test
	public void testGetAdverseDrugEventsLimit() throws EncodingException {
		Response resp = searcher.getAdverseDrugEvents(null, null, null, null,
				null, null, null, null, null, null, null, null, 10);
		assertTrue("Response was not a JSON object.",
				((String) resp.getEntity()).startsWith("{"));
		JSONObject obj = new JSONObject((String) resp.getEntity());
		assertEquals("Expected ten results.", 10, obj.getJSONObject("meta")
				.getJSONObject("results").getInt("limit"));
	}

	@Test
	public void testGetRoutesIndication() throws EncodingException {
		Response resp = searcher.getRoutes("headache", null, null, null, null,
				null);
		assertTrue("Response was not a JSON object.",
				((String) resp.getEntity()).startsWith("{"));
		JSONObject obj = new JSONObject((String) resp.getEntity());
		assertEquals("Expected five results.", 5, obj.getJSONObject("meta")
				.getJSONObject("results").getInt("limit"));
	}

	@Test
	public void testGetRoutesBrand() throws EncodingException {
		Response resp = searcher.getRoutes(null, "tylenol", null, null, null,
				null);
		assertTrue("Response was not a JSON object.",
				((String) resp.getEntity()).startsWith("{"));
		JSONObject obj = new JSONObject((String) resp.getEntity());
		assertEquals("Expected five results.", 5, obj.getJSONObject("meta")
				.getJSONObject("results").getInt("limit"));
	}

	@Test
	public void testGetRoutesGeneric() throws EncodingException {
		Response resp = searcher.getRoutes(null, null, "ibuprofen", null, null,
				null);
		assertTrue("Response was not a JSON object.",
				((String) resp.getEntity()).startsWith("{"));
		JSONObject obj = new JSONObject((String) resp.getEntity());
		assertEquals("Expected five results.", 5, obj.getJSONObject("meta")
				.getJSONObject("results").getInt("limit"));
	}

	@Test
	public void testGetRoutesManufacturer() throws EncodingException {
		Response resp = searcher.getRoutes(null, null, null, "Company", null,
				null);
		assertTrue("Response was not a JSON object.",
				((String) resp.getEntity()).startsWith("{"));
		JSONObject obj = new JSONObject((String) resp.getEntity());
		assertEquals("Expected five results.", 5, obj.getJSONObject("meta")
				.getJSONObject("results").getInt("limit"));
	}

	@Test
	public void testGetRoutesSubstance() throws EncodingException {
		Response resp = searcher.getRoutes(null, null, null, null, "ibuprofen",
				null);
		assertTrue("Response was not a JSON object.",
				((String) resp.getEntity()).startsWith("{"));
		JSONObject obj = new JSONObject((String) resp.getEntity());
		assertEquals("Expected five results.", 5, obj.getJSONObject("meta")
				.getJSONObject("results").getInt("limit"));
	}

	@Test
	public void testGetRoutesLimit() throws EncodingException {
		Response resp = searcher.getRoutes(null, null, null, null, null, 10);
		assertTrue("Response was not a JSON object.",
				((String) resp.getEntity()).startsWith("{"));
		JSONObject obj = new JSONObject((String) resp.getEntity());
		assertEquals("Expected ten results.", 10, obj.getJSONObject("meta")
				.getJSONObject("results").getInt("limit"));
	}

	@Test
	public void testGetDrugs() throws EncodingException {
		Response resp = searcher.getDrugs("headache", "oral", null);
		assertTrue("Response was not a JSON object.",
				((String) resp.getEntity()).startsWith("{"));
		JSONObject obj = new JSONObject((String) resp.getEntity());
		assertEquals("Expected five results.", 5, obj.getJSONObject("meta")
				.getJSONObject("results").getInt("limit"));
	}

	@Test
	public void testGetDrugsLimit() throws EncodingException {
		Response resp = searcher.getDrugs("headache", "oral", 10);
		assertTrue("Response was not a JSON object.",
				((String) resp.getEntity()).startsWith("{"));
		JSONObject obj = new JSONObject((String) resp.getEntity());
		assertEquals("Expected ten results.", 10, obj.getJSONObject("meta")
				.getJSONObject("results").getInt("limit"));
	}

	@Test
	public void testGetSavedSearchesOfType() {
		List<Search> list = new LinkedList<Search>();
		when(
				searchBeanMock.findWithNamedQuery(anyString(),
						Mockito.anyMapOf(String.class, Object.class)))
				.thenReturn(list);
		Response resp = searcher
				.getSavedSearchesOfType(Search.SearchType.ADVERSE_EVENTS, searchBeanMock);
		assertEquals("Returned list was not expected.", list, resp.getEntity());
		assertEquals("Response code was not expected.", HttpStatus.SC_OK, resp.getStatus());
	}

	@Test
	public void testGetSavedSearchesOfTypeError() {
		when(
				searchBeanMock.findWithNamedQuery(anyString(),
						Mockito.anyMapOf(String.class, Object.class)))
				.thenThrow(new RuntimeException());
		Response resp = searcher
				.getSavedSearchesOfType(Search.SearchType.ADVERSE_EVENTS, searchBeanMock);
		assertTrue("Unexpected error message.", ((String)resp.getEntity()).startsWith("Side Effect"));
		assertEquals("Unexpected response code.", HttpStatus.SC_INTERNAL_SERVER_ERROR, resp.getStatus());
	}

	@Test
	public void testGetSearchById() {
		Search search = new Search();
		when(searchBeanMock.findById(anyObject())).thenReturn(search);
		Response resp = searcher.getSearchById("", searchBeanMock);
		assertEquals("Returned search was not expected.", search, resp.getEntity());
		assertEquals("Response code was not expected.", HttpStatus.SC_OK, resp.getStatus());
	}
	
	@Test
	public void testGetSearchByIdError() {
		when(searchBeanMock.findById(anyObject())).thenThrow(new RuntimeException());
		Response resp = searcher.getSearchById("", searchBeanMock);
		assertTrue("Unexpected error message.", ((String)resp.getEntity()).equals("Requested saved search could not be retrieved."));
		assertEquals("Unexpected response code.", HttpStatus.SC_INTERNAL_SERVER_ERROR, resp.getStatus());
	}

	@Test
	public void testDeleteSavedSearch() {
		String id = "1234";
		Response resp = searcher.deleteSavedSearch(id, searchBeanMock);
		Mockito.verify(searchBeanMock).delete(id);
		assertNull("Unexpected response entity.", resp.getEntity());
		assertEquals("Unexpected response code.", HttpStatus.SC_OK, resp.getStatus());
	}
	
	@Test
	public void testDeleteSavedSearchNull() {
		String id = null;
		Response resp = searcher.deleteSavedSearch(id, searchBeanMock);
		assertEquals("Unexpected response code.", HttpStatus.SC_BAD_REQUEST, resp.getStatus());
		assertTrue("Unexpected error message.", ((String)resp.getEntity()).equals("Saved search id must not be null."));
	}
	
	@Test
	public void testDeleteSavedSearchError() {
		String id = "1234";
		Mockito.doThrow(new RuntimeException()).when(searchBeanMock).delete(anyString());
		Response resp = searcher.deleteSavedSearch(id, searchBeanMock);
		assertEquals("Unexpected response code.", HttpStatus.SC_INTERNAL_SERVER_ERROR, resp.getStatus());
		assertTrue("Unexpected error message.", ((String)resp.getEntity()).equals("Saved search could not be deleted."));
	}

	@Test
	public void testCreateSavedSearch() {
		Response resp = searcher.createSavedSearch(null, null, null, null, null, null, null, null, null, null, null, null, null, searchBeanMock);
		Mockito.verify(searchBeanMock).persist(Mockito.any(Search.class));
		assertEquals("Unexpected response code.", HttpStatus.SC_OK, resp.getStatus());
		assertNotNull("Returned search was null.", resp.getEntity());
	}
	
	@Test
	public void testCreateSavedSearchEncodingError() {
		Response resp = searcher.createSavedSearch("%=?", null, null, null, null, null, null, null, null, null, null, null, null, searchBeanMock);
		assertEquals("Unexpected response code.", HttpStatus.SC_INTERNAL_SERVER_ERROR, resp.getStatus());
		assertTrue("Unexpected error message.", ((String)resp.getEntity()).equals("Request parameters could not be decoded."));
	}

	@Test
	public void testCreateSavedSearchError() {
		Mockito.doThrow(new RuntimeException()).when(searchBeanMock).persist(Mockito.any(Search.class));
		Response resp = searcher.createSavedSearch("name", null, null, null, null, null, null, null, null, null, null, null, null, searchBeanMock);
		assertEquals("Unexpected response code.", HttpStatus.SC_INTERNAL_SERVER_ERROR, resp.getStatus());
		assertTrue("Unexpected error message.", ((String)resp.getEntity()).equals("Search 'name' could not be saved."));
	}
}
