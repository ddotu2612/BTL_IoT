package com.iot.dao;

import java.util.List;

import com.iot.dto.DataDto;
import com.iot.dto.SensorAllDto;
import com.iot.entity.SensorEntity;

public interface ISensorDao extends GenericDao<Long, SensorEntity>{
	List<SensorEntity> findByListDeviceId(Long deviceId);
	SensorEntity findAllDataSensor(Long id, String condition);
	SensorEntity findByCode(String code,Long deviceId);
	List<Object[]> getAllDataSensorWithProp(Long sensorId, int status, String date);
}
