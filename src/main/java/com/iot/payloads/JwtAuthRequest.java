package com.iot.payloads;

import com.iot.dto.DeviceDto;

public class JwtAuthRequest {
	Long deviceId;
	String token;
	DeviceDto deviceDto;

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

	public DeviceDto getDeviceDto() {
		return deviceDto;
	}

	public void setDeviceDto(DeviceDto deviceDto) {
		this.deviceDto = deviceDto;
	}

}
