package com.iot.dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.RollbackException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.iot.dao.GenericDao;

import javassist.tools.rmi.ObjectNotFoundException;

public class AbstractDao<ID extends Serializable, T> implements GenericDao<ID, T> {
	@PersistenceContext
	protected EntityManager entityManager;
	protected final Logger logger = Logger.getLogger(this.getClass());
	private Class<T> persistenceClass;

	@SuppressWarnings("unchecked")
	public AbstractDao() {
		this.persistenceClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
				.getActualTypeArguments()[1];
	}

	// lấy tên class truyền vào
	public String getPersistenceClassName() {
		return persistenceClass.getSimpleName();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> findAll(String JOINFETCH) {
		List<T> result = new ArrayList<T>();

		try {
			String sql = "";
			if (StringUtils.isNotBlank(JOINFETCH)) {
				sql = "select t from " + getPersistenceClassName() + " t JOIN FETCH t." + JOINFETCH;
			} else {
				sql = "select t from " + getPersistenceClassName() + " t";
			}
			result = entityManager.createQuery(sql).getResultList();
		} catch (Exception e) {
			return result;
		}
		return result;
	}

	@Transactional
	@SuppressWarnings("unchecked")
	@Override
	public T update(T entity) {
		T result = null;
		try {
			Object object = entityManager.merge(entity);
			result = (T) object;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return result;
	}
	
	@Transactional
	@Override
	public T save(T entity) {
		try {
			entityManager.persist(entity);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
		return entity;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T findById(ID var1) {
		T result = null;
		try {
			Object object = entityManager.find(persistenceClass, var1);
			result = (T) object;
			if (result == null) {
				throw new ObjectNotFoundException("not found " + var1, null);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return result;
	}
	@Transactional( rollbackFor = Exception.class)
	@Override
	public Integer delete(ID[] ids) {
		Integer count = 0;
		try {
			for (ID id : ids) {
				T entity = (T) entityManager.find(persistenceClass, id);
				entityManager.remove(entity);
				count++;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return count;
	}

}
