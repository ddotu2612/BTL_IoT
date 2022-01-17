package com.iot.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.iot.dao.ISensorDao;
import com.iot.dto.DataDto;
import com.iot.dto.SensorAllDto;
import com.iot.entity.SensorEntity;

@Repository
public class SensorDao extends AbstractDao<Long, SensorEntity> implements ISensorDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<SensorEntity> findByListDeviceId(Long deviceId) {
		List<SensorEntity> result = new ArrayList<SensorEntity>();
		String sql = "Select * from sensor t where t.device=:device and t.status=1";
		Query q = entityManager.createNativeQuery(sql, SensorEntity.class);
		q.setParameter("device", deviceId);
		result = q.getResultList();
		return result;
	}

	@Override
	public SensorEntity findAllDataSensor(Long id, String condition) {
		SensorEntity entity = null;
		try {
			String sql = "select t from " + getPersistenceClassName()
					+ " t JOIN FETCH t.sensorDataList where t.id=:id and t.status=1 " + condition;
			Query q = entityManager.createQuery(sql);
			q.setParameter("id", id);
			entity = (SensorEntity) q.getSingleResult();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return entity;
	}

	@Override
	public SensorEntity findByCode(String code, Long deviceId) {
		SensorEntity entity = null;
		try {
			String sql = "Select t from " + getPersistenceClassName()
					+ " t JOIN FETCH t.deviceEntity d where t.code=:code and t.status=1 and d.id=:id";
			Query q = entityManager.createQuery(sql);
			q.setParameter("code", code);
			q.setParameter("id", deviceId);
			entity = (SensorEntity) q.getSingleResult();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return entity;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getAllDataSensorWithProp(Long sensorId, int status, String date) {
		List<Object[]> result = new ArrayList<Object[]>();
		try {
			//Nếu lấy theo tháng tức mỗi ngày trong tháng đó
			
			if (status == 0) {
				String tmp[]=date.split("-");
				
				String sql = "select day(sensor_data.time) as date,sensor,sum(sensor_data.value) as sum, count(sensor_data.value) as count from sensor_data where year(sensor_data.time)=:year and month(sensor_data.time)=:month and sensor_data.sensor=:sensorId group by sensor, day(sensor_data.time)";
				Query q = entityManager.createNativeQuery(sql);
				q.setParameter("sensorId", sensorId);
				q.setParameter("month", tmp[0]);
				q.setParameter("year", tmp[1]);
				result = q.getResultList();
			} else if (status == 1) { //Lấy theo tháng
				String sql = "select month(sensor_data.time) as date,sensor,sum(sensor_data.value) as sum, count(sensor_data.value) as count from sensor_data where year(sensor_data.time)=:year and sensor_data.sensor=:sensorId group by sensor, month(sensor_data.time)";
				Query q = entityManager.createNativeQuery(sql);
				q.setParameter("sensorId", sensorId);
				q.setParameter("year", date);
				result = q.getResultList();
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return result;
	}

}
