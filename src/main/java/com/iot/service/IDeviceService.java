package com.iot.service;

import java.util.List;

import com.iot.dto.DeviceDto;

public interface IDeviceService {
	DeviceDto findById(Long id);
	DeviceDto save(DeviceDto dto);
	DeviceDto getListSensor(Long id);
	List<DeviceDto> getListDeviceByUser(String username);
	List<DeviceDto> getListDeviceByAdmin(String username);
	DeviceDto getInfoDevice(Long id, String username);
}
