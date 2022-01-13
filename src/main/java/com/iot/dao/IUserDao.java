package com.iot.dao;

import com.iot.entity.UserEntity;

public interface IUserDao extends GenericDao<Long, UserEntity>{
	UserEntity findOneUsername(String username);
	UserEntity findByIdUser(Long id);
}
