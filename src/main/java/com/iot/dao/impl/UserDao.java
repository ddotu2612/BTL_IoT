package com.iot.dao.impl;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.iot.dao.IUserDao;
import com.iot.entity.UserEntity;

@Repository
public class UserDao extends AbstractDao<Long, UserEntity> implements IUserDao {

	@Override
	public UserEntity findOneUsername(String username) {
		UserEntity result=null;
		String sql="Select t from "+ getPersistenceClassName()+" t JOIN FETCH t.roleEntity where username=:username";
		Query q = entityManager.createQuery(sql);
		q.setParameter("username", username);
		result=(UserEntity) q.getSingleResult();
		return result;
	}

	@Override
	public UserEntity findByIdUser(Long id) {
		UserEntity result=null;
		String sql="Select t from "+ getPersistenceClassName()+" t JOIN FETCH t.roleEntity where t.id=:id";
		Query q = entityManager.createQuery(sql);
		q.setParameter("id", id);
		result=(UserEntity) q.getSingleResult();
		return result;
	}
}
