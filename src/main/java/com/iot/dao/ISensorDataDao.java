package com.iot.dao;

import java.util.List;

import com.iot.entity.SensorDataEntity;

public interface ISensorDataDao extends GenericDao<Long, SensorDataEntity>{
	List<SensorDataEntity> findAllDataSensorId(List<Long> ids);
	List<SensorDataEntity> findAllDataLastSensorId(List<Long> ids);
	Float getSumDataProp(String prop, String date);
	Float getSumDataPropByMonth(String prop, String data);
	List<Long> getDeviceByIdUser(Long id);
	List<Long> getSensorIdByIdDeviceId(Long id);
	Float getSensorDataSensorId(Long idSensor, String month, String year);
	Float getDataSensorUserByMonth(Long id, String month, String year);
}
