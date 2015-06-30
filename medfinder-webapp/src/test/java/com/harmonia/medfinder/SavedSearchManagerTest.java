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
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.harmonia.medfinder.ejb.bean.SearchBean;
import com.harmonia.medfinder.model.Search;

/**
 * Test class for {@link SavedSearchManager}
 * 
 * @author keagan
 */
public class SavedSearchManagerTest {

	/**
	 * Saved search manager
	 */
	private SavedSearchManager savedSearchManager;

	/**
	 * Mocked search bean
	 */
	private SearchBean searchBeanMock;

	/**
	 * Set up for the test
	 * 
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	@Before
	public void beforeTest() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		savedSearchManager = new SavedSearchManager();
		searchBeanMock = mock(SearchBean.class);
	}

	/**
	 * Tests
	 * {@link SavedSearchManager#getSavedSearchesOfType(SearchType, SearchBean)}
	 */
	@Test
	public void testGetSavedSearchesOfType() {
		List<Search> list = new LinkedList<Search>();
		when(searchBeanMock.findWithNamedQuery(anyString(), Mockito.anyMapOf(String.class, Object.class))).thenReturn(list);
		Response resp = savedSearchManager.getSavedSearchesOfType(Search.SearchType.ADVERSE_EVENTS, searchBeanMock);
		assertEquals("Returned list was not expected.", list, resp.getEntity());
		assertEquals("Response code was not expected.", HttpStatus.SC_OK, resp.getStatus());
	}

	/**
	 * Tests the error case for
	 * {@link SavedSearchManager#getSavedSearchesOfType(SearchType, SearchBean)}
	 */
	@Test
	public void testGetSavedSearchesOfTypeError() {
		when(searchBeanMock.findWithNamedQuery(anyString(), Mockito.anyMapOf(String.class, Object.class))).thenThrow(new RuntimeException());
		Response resp = savedSearchManager.getSavedSearchesOfType(Search.SearchType.ADVERSE_EVENTS, searchBeanMock);
		assertTrue("Unexpected error message.", ((String)resp.getEntity()).startsWith("Side Effect"));
		assertEquals("Unexpected response code.", HttpStatus.SC_INTERNAL_SERVER_ERROR, resp.getStatus());
	}

	/**
	 * Tests {@link SavedSearchManager#getSearchById(String, SearchBean)}
	 */
	@Test
	public void testGetSearchById() {
		Search search = new Search();
		when(searchBeanMock.findById(anyObject())).thenReturn(search);
		Response resp = savedSearchManager.getSearchById("", searchBeanMock);
		assertEquals("Returned search was not expected.", search, resp.getEntity());
		assertEquals("Response code was not expected.", HttpStatus.SC_OK, resp.getStatus());
	}

	/**
	 * Tests error case for
	 * {@link SavedSearchManager#getSearchById(String, SearchBean)}
	 */
	@Test
	public void testGetSearchByIdError() {
		when(searchBeanMock.findById(anyObject())).thenThrow(new RuntimeException());
		Response resp = savedSearchManager.getSearchById("", searchBeanMock);
		assertTrue("Unexpected error message.", ((String)resp.getEntity()).equals("Requested saved search could not be retrieved."));
		assertEquals("Unexpected response code.", HttpStatus.SC_INTERNAL_SERVER_ERROR, resp.getStatus());
	}

	/**
	 * Tests {@link SavedSearchManager#deleteSavedSearch(String, SearchBean)}
	 */
	@Test
	public void testDeleteSavedSearch() {
		String id = "1234";
		Response resp = savedSearchManager.deleteSavedSearch(id, searchBeanMock);
		Mockito.verify(searchBeanMock).delete(id);
		assertNull("Unexpected response entity.", resp.getEntity());
		assertEquals("Unexpected response code.", HttpStatus.SC_OK, resp.getStatus());
	}

	/**
	 * Tests error case when id is null for
	 * {@link SavedSearchManager#deleteSavedSearch(String, SearchBean)}
	 */
	@Test
	public void testDeleteSavedSearchNull() {
		String id = null;
		Response resp = savedSearchManager.deleteSavedSearch(id, searchBeanMock);
		assertEquals("Unexpected response code.", HttpStatus.SC_BAD_REQUEST, resp.getStatus());
		assertTrue("Unexpected error message.", ((String)resp.getEntity()).equals("Saved search id must not be null."));
	}

	/**
	 * Tests error case when id is invalid for
	 * {@link SavedSearchManager#deleteSavedSearch(String, SearchBean)}
	 */
	@Test
	public void testDeleteSavedSearchError() {
		String id = "1234";
		Mockito.doThrow(new RuntimeException()).when(searchBeanMock).delete(anyString());
		Response resp = savedSearchManager.deleteSavedSearch(id, searchBeanMock);
		assertEquals("Unexpected response code.", HttpStatus.SC_INTERNAL_SERVER_ERROR, resp.getStatus());
		assertTrue("Unexpected error message.", ((String)resp.getEntity()).equals("Saved search could not be deleted."));
	}

	/**
	 * Tests
	 * {@link SavedSearchManager#createSavedSearch(String, com.harmonia.medfinder.model.Search.SearchType, String, String, String, String, String, Integer, Integer, Double, Double, String, String, SearchBean)}
	 */
	@Test
	public void testCreateSavedSearch() {
		Response resp = savedSearchManager.createSavedSearch(null, null, null, null, null, null, null, null, null, null, null, null, null, searchBeanMock);
		Mockito.verify(searchBeanMock).persist(Mockito.any(Search.class));
		assertEquals("Unexpected response code.", HttpStatus.SC_OK, resp.getStatus());
		assertNotNull("Returned search was null.", resp.getEntity());
	}

	/**
	 * Test the encoding error case for
	 * {@link SavedSearchManager#createSavedSearch(String, com.harmonia.medfinder.model.Search.SearchType, String, String, String, String, String, Integer, Integer, Double, Double, String, String, SearchBean)}
	 */
	@Test
	public void testCreateSavedSearchEncodingError() {
		Response resp = savedSearchManager.createSavedSearch("%=?", null, null, null, null, null, null, null, null, null, null, null, null, searchBeanMock);
		assertEquals("Unexpected response code.", HttpStatus.SC_INTERNAL_SERVER_ERROR, resp.getStatus());
		assertTrue("Unexpected error message.", ((String)resp.getEntity()).equals("Request parameters could not be decoded."));
	}

	@Test
	public void testCreateSavedSearchError() {
		Mockito.doThrow(new RuntimeException()).when(searchBeanMock).persist(Mockito.any(Search.class));
		Response resp = savedSearchManager.createSavedSearch("name", null, null, null, null, null, null, null, null, null, null, null, null, searchBeanMock);
		assertEquals("Unexpected response code.", HttpStatus.SC_INTERNAL_SERVER_ERROR, resp.getStatus());
		assertTrue("Unexpected error message.", ((String)resp.getEntity()).equals("Search 'name' could not be saved."));
	}
}
