package com.iot.service;

import java.util.List;

import com.iot.dto.SensorAllDto;
import com.iot.dto.SensorDto;
import com.iot.mqtt.CollectDataModel;

public interface ISensorService {
	SensorDto save(SensorDto dto);
	List<SensorDto> findByListDeviceId(Long id);
	void saveCollectData(CollectDataModel collectData);
	List<SensorDto> getAllData(Long deviceid, String prop, String date);
	List<SensorAllDto> getAllSensorData(Long deviceid, String prop, String date);
}
