package com.iot.dao.impl;

import java.util.ArrayList;
import java.util.List;

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

}
