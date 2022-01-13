package com.iot.service;

import java.util.List;

import com.iot.dto.SensorDataDto;

public interface ISensorDataService {
	List<SensorDataDto> findAllDataSensorId(Long deviceID);
	List<SensorDataDto> findAllDataLastSensorId(Long deviceID);
	Float getSumDataProp(String prop, String date);
}
