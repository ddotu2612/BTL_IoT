package com.iot.converter;

import com.iot.dto.RoleDto;
import com.iot.dto.UserDto;
import com.iot.entity.RoleEntity;
import com.iot.entity.UserEntity;

public class RoleBeanUtil {

	public static RoleDto dto2Entity(RoleEntity entity) {
		RoleDto dto = new RoleDto();
		dto.setId(entity.getId());
		dto.setCode(entity.getCode());
		dto.setName(entity.getName());
		try {
			for (UserEntity user : entity.getUserList()) {
				dto.getUserList().add(UserBeanUtil.entity2Dto(user));
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return dto;
	}

	public static RoleEntity entity2Dto(RoleDto dto) {
		RoleEntity entity = new RoleEntity();
		entity.setId(dto.getId());
		entity.setCode(dto.getCode());
		entity.setName(dto.getName());
		try {
			for (UserDto user : dto.getUserList()) {
				entity.getUserList().add(UserBeanUtil.dto2Entity(user));
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return entity;
	}

}
