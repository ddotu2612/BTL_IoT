package com.iot.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iot.converter.SensorDataBeanUtil;
import com.iot.dao.IDeviceDao;
import com.iot.dao.ISensorDataDao;
import com.iot.dto.SensorDataDto;
import com.iot.entity.DeviceEntity;
import com.iot.entity.SensorDataEntity;
import com.iot.entity.SensorEntity;
import com.iot.service.ISensorDataService;

@Service
public class SensorDataService implements ISensorDataService {

	@Autowired
	private ISensorDataDao sensorDataDao;

	@Autowired
	private IDeviceDao deviceDao;

	@Override
	public List<SensorDataDto> findAllDataSensorId(Long deviceID) {
		List<Long> ids = new ArrayList<Long>();
		List<SensorDataDto> result = new ArrayList<SensorDataDto>();
		try {
			String fetch = "sensorList";
			DeviceEntity device = deviceDao.findByIdWithProp(deviceID, fetch, 1, "");
			if (device != null) {
				for (SensorEntity sensor : device.getSensorList()) {
					ids.add(sensor.getId());
				}
			}
			for (SensorDataEntity item : sensorDataDao.findAllDataSensorId(ids)) {
				result.add(SensorDataBeanUtil.entity2Dto(item));
			}
		} catch (Exception e) {
		}

		return result;
	}

	@Override
	public List<SensorDataDto> findAllDataLastSensorId(Long deviceID) {
		List<Long> ids = new ArrayList<Long>();
		List<SensorDataDto> result = new ArrayList<SensorDataDto>();
		try {
			String fetch = "sensorList";
			DeviceEntity device = deviceDao.findByIdWithProp(deviceID, fetch, 1, "");
			if (device != null) {
				for (SensorEntity sensor : device.getSensorList()) {
					ids.add(sensor.getId());
				}
			}
			for (SensorDataEntity item : sensorDataDao.findAllDataLastSensorId(ids)) {
				result.add(SensorDataBeanUtil.entity2Dto(item));
			}
		} catch (Exception e) {
		}

		return result;
	}

	@Override
	public Float getSumDataProp(String prop, String date) {
		return sensorDataDao.getSumDataProp(prop, date);
	}
}
