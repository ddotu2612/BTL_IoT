package com.iot.service;

import java.util.List;

import com.iot.dto.UserDto;
public interface IUserService {
	
	UserDto save(UserDto dto);
	List<UserDto> findAll();
	UserDto getUserWithUsername(String username);
	UserDto findById(Long id);
	Boolean deleteUser(Long[] ids);
}
