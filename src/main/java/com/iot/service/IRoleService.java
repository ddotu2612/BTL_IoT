package com.iot.service;

import java.util.List;

import com.iot.dto.RoleDto;

public interface IRoleService {
	RoleDto save(RoleDto dto);
	RoleDto findByCode(String code);
	List<RoleDto> getListRole();
}
