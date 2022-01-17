package com.iot.converter;

import com.iot.dto.UserDto;
import com.iot.entity.UserEntity;

public class UserBeanUtil {

	public static UserDto entity2Dto(UserEntity entity) {
		UserDto dto = new UserDto();
		dto.setId(entity.getId());
		dto.setUsername(entity.getUsername());
		dto.setFull_name(entity.getFull_name());
		dto.setEmail(entity.getEmail());
		dto.setPassword(entity.getPassword());
		dto.setStatus(entity.getStatus());
		dto.setCreate_time(entity.getCreate_time());
		try {
			if (entity.getRoleEntity() != null) {
				dto.setRoleDto(RoleBeanUtil.dto2Entity(entity.getRoleEntity()));
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return dto;
	}

	public static UserEntity dto2Entity(UserDto dto) {
		UserEntity entity = new UserEntity();
		entity.setId(dto.getId());
		entity.setUsername(dto.getUsername());
		entity.setFull_name(dto.getFull_name());
		entity.setEmail(dto.getEmail());
		entity.setPassword(dto.getPassword());
		entity.setStatus(dto.getStatus());
		entity.setCreate_time(dto.getCreate_time());
		try {
			if (dto.getRoleDto() != null) {
				entity.setRoleEntity(RoleBeanUtil.entity2Dto(dto.getRoleDto()));
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return entity;
	}

	/*
	 * entity là thực thể cũ dto là thực thể mới => Khi cập nhập dữ liệu thì nó sẽ
	 * cập nhật dựa vào thằng mới và cả thằng cũ những trường dữ liệu cố định.
	 */
	public static UserEntity dto2Entity(UserDto dto, UserEntity entity) {
		entity.setFull_name(dto.getFull_name());
		entity.setPassword(dto.getPassword());
		entity.setEmail(dto.getEmail());
		entity.setStatus(dto.getStatus());
		if (dto.getRoleDto() != null) {
			entity.setRoleEntity(RoleBeanUtil.entity2Dto(dto.getRoleDto()));
		}
		return entity;
	}

}
