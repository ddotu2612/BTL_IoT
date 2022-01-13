package com.iot.dao;

import java.util.List;

import com.iot.entity.SensorDataEntity;

public interface ISensorDataDao extends GenericDao<Long, SensorDataEntity>{
	List<SensorDataEntity> findAllDataSensorId(List<Long> ids);
	List<SensorDataEntity> findAllDataLastSensorId(List<Long> ids);
	Float getSumDataProp(String prop, String date);
}
