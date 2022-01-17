package com.iot.entity;

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
@Table(name = "sensor")
public class SensorEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "name", columnDefinition = "nvarchar(250)")
	private String name;

	@Column(name="code", columnDefinition = "nvarchar(250) NOT NULL")
	private String code;
	@Column(name = "status")
	private Integer status;

	@OneToMany(mappedBy = "sensorEntity", fetch = FetchType.LAZY, cascade = {
			CascadeType.REMOVE }, orphanRemoval = true)
	private Set<SensorDataEntity> sensorDataList = new HashSet<SensorDataEntity>();

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "device")
	private DeviceEntity deviceEntity;

	public DeviceEntity getDeviceEntity() {
		return deviceEntity;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public void setDeviceEntity(DeviceEntity deviceEntity) {
		this.deviceEntity = deviceEntity;
	}

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

	public Set<SensorDataEntity> getSensorDataList() {
		return sensorDataList;
	}

	public void setSensorDataList(Set<SensorDataEntity> sensorDataList) {
		this.sensorDataList = sensorDataList;
	}

}