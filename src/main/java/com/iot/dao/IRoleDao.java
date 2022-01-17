package com.iot.dao;

import com.iot.entity.RoleEntity;

public interface IRoleDao extends GenericDao<Long, RoleEntity>{
	RoleEntity findByCode(String code);
}
