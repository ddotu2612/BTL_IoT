package com.iot.service;

import java.util.List;

import com.iot.dto.SensorDataDto;

public interface ISensorDataService {
	List<SensorDataDto> findAllDataSensorId(Long deviceID);
	List<SensorDataDto> findAllDataLastSensorId(Long deviceID);
	Float getSumDataProp(String prop, String date);
	Float getSumDataPropByMonth(String prop, String data);
	List<Long> getDeviceByIdUser(Long id);
	Float getDateUserByMonth(Long id, String month, String year);
}
