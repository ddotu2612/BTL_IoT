package com.iot.dao;

import java.util.List;

import com.iot.entity.SensorDataEntity;

public interface ISensorDataDao extends GenericDao<Long, SensorDataEntity>{
	List<SensorDataEntity> findAllDataSensorId(List<Long> ids);
	List<SensorDataEntity> findAllDataLastSensorId(List<Long> ids);
	Float getSumDataProp(String prop, String date);
	Float getSumDataPropByMonth(String prop, String data);
	Float getSumDataUserPropByMonth(Long id, String prop, String data);
}
