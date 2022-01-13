package com.iot.dao.impl;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.iot.dao.IRoleDao;
import com.iot.entity.RoleEntity;

@Repository
public class RoleDao extends AbstractDao<Long, RoleEntity> implements IRoleDao{

	@Override
	public RoleEntity findByCode(String code) {
		RoleEntity result = null;
		try {
			StringBuilder sql = new StringBuilder("");
			sql.append("Select * from role Where code=:code");
			Query q = entityManager.createNativeQuery(sql.toString(), RoleEntity.class);
			q.setParameter("code", code);
			result = (RoleEntity) q.getSingleResult();

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return result;
	}

}
