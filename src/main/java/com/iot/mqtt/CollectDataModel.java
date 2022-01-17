package com.iot.mqtt;

import java.util.HashSet;
import java.util.Set;

public class CollectDataModel {
	private Long deviceId;
	private String token;
	private Long time;
	private Set<DataModel> sensorDataList = new HashSet<DataModel>();

	public Long getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public Set<DataModel> getSensorDataList() {
		return sensorDataList;
	}

	public void setSensorDataList(Set<DataModel> sensorDataList) {
		this.sensorDataList = sensorDataList;
	}

}
