package com.iot.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.iot.converter.RoleBeanUtil;
import com.iot.converter.UserBeanUtil;
import com.iot.dao.IRoleDao;
import com.iot.dao.IUserDao;
import com.iot.dto.RoleDto;
import com.iot.dto.UserDto;
import com.iot.entity.RoleEntity;
import com.iot.entity.UserEntity;
import com.iot.service.IUserService;

@Service
public class UserService implements IUserService {
	@Autowired
	private IRoleDao roleDao;
	@Autowired
	private IUserDao userDao;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public UserDto save(UserDto dto) {
		UserDto result = null;
		if (dto != null && dto.getId() != null) {
			UserEntity old = userDao.findByIdUser(dto.getId());
			if (!dto.getPassword().equals(old.getPassword())) {
				dto.setPassword(passwordEncoder.encode(dto.getPassword()));
			}
			RoleDto role = RoleBeanUtil.dto2Entity(roleDao.findByCode(dto.getRoleDto().getCode()));
			if (role != null) {
				dto.setRoleDto(role);
			}
			System.out.println("");
			result = UserBeanUtil.entity2Dto(userDao.update(UserBeanUtil.dto2Entity(dto, old)));
			if (result != null) {
				result = UserBeanUtil.entity2Dto(userDao.findByIdUser(result.getId()));
			}
		} else if (dto != null && dto.getId() == null) {
			if (dto.getRoleDto() != null) {
				RoleDto role = RoleBeanUtil.dto2Entity(roleDao.findByCode(dto.getRoleDto().getCode()));
				if (role != null) {
					dto.setRoleDto(role);
				}
			} else {
				RoleDto role = RoleBeanUtil.dto2Entity(roleDao.findByCode("USER"));
				if (role != null) {
					dto.setRoleDto(role);
				}
			}
			dto.setCreate_time(new Date());
			dto.setPassword(passwordEncoder.encode(dto.getPassword()));
			result = UserBeanUtil.entity2Dto(userDao.save(UserBeanUtil.dto2Entity(dto)));
		}
		return result;
	}

	@Override
	public List<UserDto> findAll() {
		String JOIN_FETCH = "roleEntity";
		// String JOIN_FETCH="";
		List<UserDto> result = new ArrayList<UserDto>();
		for (UserEntity entity : userDao.findAll(JOIN_FETCH)) {
			result.add(UserBeanUtil.entity2Dto(entity));
		}
		return result;
	}

	@Override
	public UserDto getUserWithUsername(String username) {
		return UserBeanUtil.entity2Dto(userDao.findOneUsername(username));
	}

	@Override
	public UserDto findById(Long id) {
		UserDto result = null;
		result = UserBeanUtil.entity2Dto(userDao.findByIdUser(id));
		return result;
	}

	@Override
	public Boolean deleteUser(Long[] ids) {
		try {
			if (ids.length == userDao.delete(ids)) {
				return true;
			}
		} catch (Exception e) {
		}
		return false;
	}

}
