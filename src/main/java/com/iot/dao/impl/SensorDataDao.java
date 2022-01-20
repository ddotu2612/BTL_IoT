package com.iot.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.iot.dao.ISensorDataDao;
import com.iot.entity.SensorDataEntity;

@Repository
public class SensorDataDao extends AbstractDao<Long, SensorDataEntity> implements ISensorDataDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<SensorDataEntity> findAllDataSensorId(List<Long> ids) {
		List<SensorDataEntity> result = new ArrayList<SensorDataEntity>();
		try {
			String sql = "Select t from " + getPersistenceClassName() + " t JOIN FETCH t.sensorEntity s where s.id in(";
			int flag = 0;
			for (Long id : ids) {
				if (flag == 0) {
					sql += id.toString();
					flag++;
				} else {
					sql += "," + id.toString();
				}
			}
			sql += ")";
			Query q = entityManager.createQuery(sql);
			result = q.getResultList();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SensorDataEntity> findAllDataLastSensorId(List<Long> ids) {
		List<SensorDataEntity> result = new ArrayList<SensorDataEntity>();
		// select * from sensor_data s1 where s1.id in(select max(id) from sensor_data
		// group by sensor having sensor in(1,2,3));
		try {
			String sql = "Select t from " + getPersistenceClassName()
					+ " t JOIN FETCH t.sensorEntity where t.id in (select max(id) from " + getPersistenceClassName()
					+ " group by sensor having sensor in(";
			int flag = 0;
			for (Long id : ids) {
				if (flag == 0) {
					sql += id.toString();
					flag++;
				} else {
					sql += "," + id.toString();
				}
			}
			sql += "))";
			Query q = entityManager.createQuery(sql);
			result = q.getResultList();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return result;
	}

	@Override
	public Float getSumDataProp(String prop, String date) {
		Float result = 0f;
		try {
			if (prop.equals("year")) {
				String sql = "Select sum(sensor_data.value) from sensor_data where year(sensor_data.time)=:year";
				Query q = entityManager.createNativeQuery(sql);
				q.setParameter("year", date);
				Object ob=(Object) q.getSingleResult();
				result=Float.parseFloat(String.valueOf(ob));
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return result;
	}
	
	@Override
	public Float getSumDataPropByMonth(String prop, String date) {
		Float result = 0f;
		String month="", year="";
		String[] dates = date.split("-");
		month = dates[0];
		year = dates[1];
		try {
			if (prop.equals("month")) {
				String sql = "Select sum(sensor_data.value) from sensor_data where (month(sensor_data.time)=:month and year(sensor_data.time)=:year)";
				Query q = entityManager.createNativeQuery(sql);
				q.setParameter("month", month);
				q.setParameter("year", year);
				Object ob=(Object) q.getSingleResult();
				result=Float.parseFloat(String.valueOf(ob));
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return result;
	}
	
	@Override
	public List<Long> getDeviceByIdUser(Long id) {
		List<Long> devices = new ArrayList<Long>();
		
		try {
			String sql = "select device.id from device where device.user_id=:id";
			Query q = entityManager.createNativeQuery(sql);
			q.setParameter("id", id);
			List<?> res = q.getResultList();
			for(Object i:res) {
				devices.add(Long.parseLong(String.valueOf(i)));
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		
		return devices;
	}
	
	@Override
	public List<Long> getSensorIdByIdDeviceId(Long id){
		List<Long> sensors = new ArrayList<Long>();
		
		try {
			String sql = "select sensor.id from sensor where sensor.device=:id";
			Query q = entityManager.createNativeQuery(sql);
			q.setParameter("id", id);
			List<?> res = q.getResultList();
			for(Object i:res) {
				sensors.add(Long.parseLong(String.valueOf(i)));
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		
		return sensors;
	}
	
	@Override
	public Float getSensorDataSensorId(Long idSensor, String month, String year) {
		Float result = 0f;
		
		try {
			String sql = "select sum(sensor_data.value) from sensor_data where (sensor_data.sensor=:id and month(sensor_data.time)=:month and year(sensor_data.time)=:year)";
			Query q = entityManager.createNativeQuery(sql);
			q.setParameter("id", idSensor);
			q.setParameter("month", month);
			q.setParameter("year", year);
			Object ob=(Object) q.getSingleResult();
			result = Float.parseFloat(String.valueOf(ob));

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		
		return result;
	}
	
	@Override
	public Float getDataSensorUserByMonth(Long id, String month, String year){
		Float result = 0f;
		
		List<Long> devices = getDeviceByIdUser(id);
		for(int i = 0; i < devices.size(); i++) {
			Long deviceId = devices.get(i);
			List<Long> sensors = getSensorIdByIdDeviceId(deviceId);
			for(int j = 0; j < sensors.size(); j++){
				Long sensorId = sensors.get(j);
				result += getSensorDataSensorId(sensorId, month, year);
			}
		}
		
		return result;
	}
}
