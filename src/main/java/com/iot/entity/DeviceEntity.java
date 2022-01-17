package com.iot.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "device")
public class DeviceEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "alive")
	private Integer alive;

	@Column(name = "name", columnDefinition = "nvarchar(250)")
	private String name;

	@Column(name = "token_auth", columnDefinition = "nvarchar(500)")
	private String token_auth;

	@Column(name = "token_collect_data", columnDefinition = "nvarchar(500)")
	private String token_collect_data;

	@Column(name = "created_at")
	private Date created_at;

	@Column(name = "updated_at")
	private Date updated_at;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id")
	private UserEntity userEntity;

	@OneToMany(mappedBy = "deviceEntity", fetch = FetchType.LAZY, cascade = {
			CascadeType.REMOVE }, orphanRemoval = true)
	private Set<SensorEntity> sensorList = new HashSet<SensorEntity>();

	public UserEntity getUserEntity() {
		return userEntity;
	}

	public String getToken_auth() {
		return token_auth;
	}

	public void setToken_auth(String token_auth) {
		this.token_auth = token_auth;
	}

	public String getToken_collect_data() {
		return token_collect_data;
	}

	public void setToken_collect_data(String token_collect_data) {
		this.token_collect_data = token_collect_data;
	}

	public void setUserEntity(UserEntity userEntity) {
		this.userEntity = userEntity;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getAlive() {
		return alive;
	}

	public void setAlive(Integer alive) {
		this.alive = alive;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	public Date getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}

	public Set<SensorEntity> getSensorList() {
		return sensorList;
	}

	public void setSensorList(Set<SensorEntity> sensorList) {
		this.sensorList = sensorList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}