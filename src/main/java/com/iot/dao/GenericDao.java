package com.iot.dao;

import java.io.Serializable;
import java.util.List;

public interface GenericDao<ID extends Serializable,T>{
	List<T> findAll(String JOINFETCH);
	T update(T entity);
	T save(T entity);
	T findById(ID var1);
	Integer delete(ID[] ids);

}
