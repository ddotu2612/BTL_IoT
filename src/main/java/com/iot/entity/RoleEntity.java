package com.iot.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "role")
public class RoleEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "name", columnDefinition = "nvarchar(250)")
	private String name;
	
	@Column(name="code", columnDefinition = "nvarchar(250) NOT NULL UNIQUE")
	private String code;
	
	@JsonIgnore
	@OneToMany(mappedBy = "roleEntity", fetch = FetchType.LAZY)
	private Set<UserEntity> userList=new HashSet<UserEntity>();

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

	public Set<UserEntity> getUserList() {
		return userList;
	}

	public void setUserList(Set<UserEntity> userList) {
		this.userList = userList;
	}

	
	
}
