package com.iot.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class SensorDto implements Serializable {

	private static final long serialVersionUID = -6910416474795713503L;

	private Long id;
	private String name;
	private String code;
	private Integer status;
	private Set<SensorDataDto> sensorDataList = new HashSet<SensorDataDto>();
	private DeviceDto deviceDto;

	public Long getId() {
		return id;
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

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<SensorDataDto> getSensorDataList() {
		return sensorDataList;
	}

	public void setSensorDataList(Set<SensorDataDto> sensorDataList) {
		this.sensorDataList = sensorDataList;
	}

	public DeviceDto getDeviceDto() {
		return deviceDto;
	}

	public void setDeviceDto(DeviceDto deviceDto) {
		this.deviceDto = deviceDto;
	}

}
