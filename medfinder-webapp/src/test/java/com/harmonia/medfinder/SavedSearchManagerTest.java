package com.harmonia.medfinder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.harmonia.medfinder.ejb.bean.SearchBean;
import com.harmonia.medfinder.model.Search;
import com.harmonia.medfinder.model.Search.SearchType;

/**
 * Test class for {@link SavedSearchManager}
 * 
 * @author janway
 * @author keagan
 */
@RunWith(MockitoJUnitRunner.class)
public class SavedSearchManagerTest {

	/**
	 * Enable exception testing
	 */
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	/**
	 * Saved search manager
	 */
	@InjectMocks
	private SavedSearchManager savedSearchManager;

	/**
	 * Mocked search bean
	 */
	@Mock
	private SearchBean searchBeanMock;

	/**
	 * Tests {@link SavedSearchManager#getSavedSearchesOfType(SearchType)}
	 */
	@Test
	public void testGetSavedSearchesOfType() {
		List<Search> list = new LinkedList<Search>();
		Search search = new Search();
		search.setType(SearchType.ADVERSE_EVENTS);
		when(searchBeanMock.findWithNamedQuery(anyString(), Mockito.anyMapOf(String.class, Object.class))).thenReturn(list);

		List<Search> result = savedSearchManager.getSavedSearchesOfType(Search.SearchType.ADVERSE_EVENTS);
		assertEquals("Returned list does not match.", list, result);
	}

	/**
	 * Tests the error case for
	 * {@link SavedSearchManager#getSavedSearchesOfType(SearchType)}
	 */
	@Test
	public void testGetSavedSearchesOfTypeError() {
		thrown.expect(IllegalArgumentException.class);
		savedSearchManager.getSavedSearchesOfType(null);
	}

	/**
	 * Tests {@link SavedSearchManager#getSearchById(String)}
	 */
	@Test
	public void testGetSearchById() {
		Search search = new Search();
		when(searchBeanMock.findById(anyObject())).thenReturn(search);
		Search result = savedSearchManager.getSearchById(search.getId());
		assertEquals("Returned search did not match.", search, result);
	}

	/**
	 * Tests error case for {@link SavedSearchManager#getSearchById(String)}
	 */
	@Test
	public void testGetSearchByIdError() {
		thrown.expect(IllegalArgumentException.class);
		savedSearchManager.getSearchById("");
	}

	/**
	 * Tests {@link SavedSearchManager#deleteSavedSearch(String)}
	 */
	@Test
	public void testDeleteSavedSearch() {
		String id = "1234";
		savedSearchManager.deleteSavedSearch(id);
		Mockito.verify(searchBeanMock).delete(id);
	}

	/**
	 * Tests error case when id is null for
	 * {@link SavedSearchManager#deleteSavedSearch(String)}
	 */
	@Test
	public void testDeleteSavedSearchNull() {
		thrown.expect(IllegalArgumentException.class);
		savedSearchManager.deleteSavedSearch(null);
	}

	/**
	 * Tests
	 * {@link SavedSearchManager#createSavedSearch(String, SearchType, String, String, String, String, String, Integer, Integer, Double, Double, String, String)}
	 */
	@Test
	public void testCreateSavedSearch() {
		String name = "test";
		Search search = savedSearchManager.createSavedSearch(name, null, null, null, null, null, null, null, null, null, null, null, null);
		Mockito.verify(searchBeanMock).persist(Mockito.any(Search.class));
		assertNotNull("Returned search was null.", search);
		assertEquals("Search name does not match.", name, search.getName());
	}
}
