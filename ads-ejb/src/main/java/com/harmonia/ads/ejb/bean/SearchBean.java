package com.harmonia.ads.ejb.bean;

import javax.ejb.Stateless;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.harmonia.ads.ejb.dao.BaseDAO;
import com.harmonia.ads.model.Search;

/**
 * A bean for working with Search entities.
 * 
 * @author keagan
 */
@Stateless(name = "searchbean")
public class SearchBean extends BaseDAO<Search> {

	/**
	 * Logger
	 */
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(SearchBean.class);

	/**
	 * Default constructor
	 */
	public SearchBean() {
		super(Search.class);
	}
}
