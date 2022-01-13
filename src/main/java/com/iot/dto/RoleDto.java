package com.iot.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class RoleDto implements Serializable {

	private static final long serialVersionUID = 6584549310209264564L;

	private Long id;
	private String name;
	private String code;
	private Set<UserDto> userList = new HashSet<UserDto>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Set<UserDto> getUserList() {
		return userList;
	}

	public void setUserList(Set<UserDto> userList) {
		this.userList = userList;
	}
}
