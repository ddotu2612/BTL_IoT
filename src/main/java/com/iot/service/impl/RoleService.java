package com.iot.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iot.converter.RoleBeanUtil;
import com.iot.dao.IRoleDao;
import com.iot.dto.RoleDto;
import com.iot.entity.RoleEntity;
import com.iot.service.IRoleService;

@Service
public class RoleService implements IRoleService {

	@Autowired
	private IRoleDao roleDao;

	@Override
	public RoleDto save(RoleDto dto) {
		RoleDto result = null;
		if (dto != null && dto.getId() != null) {
			// update
		}
		if (dto != null && dto.getId() == null) {
			// save
			result = RoleBeanUtil.dto2Entity(roleDao.save(RoleBeanUtil.entity2Dto(dto)));
		}

		return result;
	}

	@Override
	public RoleDto findByCode(String code) {
		return RoleBeanUtil.dto2Entity(roleDao.findByCode(code));
	}

	@Override
	public List<RoleDto> getListRole() {
		List<RoleDto> result=new ArrayList<RoleDto>();
		for(RoleEntity entity:roleDao.findAll("")) {
			result.add(RoleBeanUtil.dto2Entity(entity));
		}
		return result;
	}

}
