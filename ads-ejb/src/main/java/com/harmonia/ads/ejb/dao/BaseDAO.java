package com.harmonia.ads.ejb.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base data access object for entities. Provides various CRUD functions.
 * Implementers must set the entity manager.
 * 
 * @author keagan
 * @param <T> Entity type
 */
public abstract class BaseDAO<T> {

	/**
	 * Logger
	 */
	@SuppressWarnings("unused")
	private final static Logger LOGGER = LoggerFactory.getLogger(BaseDAO.class);

	/**
	 * Entity manager instance
	 */
	@PersistenceContext(unitName = "ADS_PU")
	protected EntityManager em;

	/**
	 * Type of entity this DAO is for
	 */
	protected Class<T> type;

	/**
	 * Constructor that takes the entity type
	 * 
	 * @param type Entity class
	 */
	public BaseDAO(Class<T> type) {
		this.type = type;
	}

	/**
	 * Inserts an instance of the entity class into the database. The entity
	 * will be managed after this call and changes to it will be tracked. When
	 * cascading, persist will ignore entities that are already persisted,
	 * provided they are not detached.
	 * 
	 * @param t Entity to persist
	 */
	public void persist(T t) {
		this.em.persist(t);
	}

	/**
	 * Updates the entity in the database. When cascading, merge will persist or
	 * update entities as needed.
	 * 
	 * @param entity Entity to update
	 * @return Managed entity
	 */
	public T merge(T entity) {
		T t = (T)this.em.merge(entity);
		return t;
	}

	/**
	 * Removes the record in the database that is associated with the entity
	 * 
	 * @param id Id of the entity to delete
	 */
	public void delete(Object id) {
		Object ref = this.em.getReference(this.type, id);
		this.em.remove(ref);
	}

	/**
	 * Retrieves an entity instance that was previously persisted to the
	 * database
	 * 
	 * @param id Id of the entity to find
	 * @return Persisted entity
	 */
	public T findById(Object id) {
		if (id == null) {
			return null;
		}
		return this.em.find(this.type, id);
	}

	/**
	 * Returns all of the entities of a certain type.
	 * 
	 * @return A list of all entities in the database of the type
	 */
	@SuppressWarnings("unchecked")
	public List<T> findAll() {
		CriteriaQuery<Object> cq = em.getCriteriaBuilder().createQuery();
		cq.select(cq.from(this.type));
		return (List<T>)em.createQuery(cq).getResultList();
	}

	/**
	 * Returns the entities found by the named query that meet the specified
	 * criteria. Named query must be a SELECT.
	 * 
	 * @param namedQuery Name of the named query to execute
	 * @param parameters Query parameters
	 * @param resultLimit Maximum number of results to return. -1 will return
	 *        all results.
	 * @param start Index from which to start returning results
	 * @return List of results
	 */
	@SuppressWarnings("unchecked")
	public List<T> findWithNamedQuery(String namedQuery, Map<String, Object> parameters, int resultLimit, int start) {

		// create query
		Query query = this.em.createNamedQuery(namedQuery);

		// set start index
		query.setFirstResult(start);

		// set result limit
		if (resultLimit > -1) {
			query.setMaxResults(resultLimit);
		}

		// set parameters
		if (parameters != null) {
			Set<Map.Entry<String, Object>> rawParameters = parameters.entrySet();
			for (Map.Entry<String, Object> entry : rawParameters) {
				query.setParameter(entry.getKey(), entry.getValue());
			}
		}

		return query.getResultList();
	}

	/**
	 * Returns the entities found by the specified named query. Named query must
	 * be a SELECT.
	 * 
	 * @param namedQuery Name of the named query to execute
	 * @return List of results
	 */
	public List<T> findWithNamedQuery(String namedQuery) {
		return findWithNamedQuery(namedQuery, null, -1, 0);
	}

	/**
	 * Returns the entities found by the specified named query and its
	 * parameters. Named query must be a SELECT.
	 * 
	 * @param namedQuery Name of the query to execute
	 * @param parameters Query parameters
	 * @return List of results
	 */
	public List<T> findWithNamedQuery(String namedQuery, Map<String, Object> parameters) {
		return findWithNamedQuery(namedQuery, parameters, -1, 0);
	}

	/**
	 * Returns the entities found by the specified named query with a limit on
	 * the number of results. Named query must be a SELECT.
	 * 
	 * @param namedQuery Name of the query to execute
	 * @param resultLimit Maximum number of results to return. -1 will return
	 *        all results.
	 * @return List of results
	 */
	public List<T> findWithNamedQuery(String namedQuery, int resultLimit) {
		return findWithNamedQuery(namedQuery, null, resultLimit, 0);
	}

	/**
	 * Returns the entities found by the specified named query with a limit on
	 * the number of results. Named query must be a SELECT.
	 * 
	 * @param namedQuery Name of the query to execute
	 * @param parameters Query parameters
	 * @param resultLimit Maximum number of results to return. -1 will return
	 *        all results.
	 * @return List of results
	 */
	public List<T> findWithNamedQuery(String namedQuery, Map<String, Object> parameters, int resultLimit) {
		return findWithNamedQuery(namedQuery, parameters, resultLimit, 0);
	}

	/**
	 * Runs a named query that is an UPDATE or DELETE.
	 * 
	 * @param namedQuery Name of the query to execute
	 * @return Number of records processed
	 */
	public int runNamedQuery(String namedQuery) {
		return runNamedQuery(namedQuery, null);
	}

	/**
	 * Runs a named query that is an UPDATE or DELETE.
	 * 
	 * @param namedQuery Name of the query to execute
	 * @param parameters Query parameters
	 * @return Number of records processed
	 */
	public int runNamedQuery(String namedQuery, Map<String, Object> parameters) {

		// create query
		Query query = this.em.createNamedQuery(namedQuery);

		// set parameters
		if (parameters != null) {
			Set<Map.Entry<String, Object>> rawParameters = parameters.entrySet();
			for (Map.Entry<String, Object> entry : rawParameters) {
				query.setParameter(entry.getKey(), entry.getValue());
			}
		}

		return query.executeUpdate();
	}
}
