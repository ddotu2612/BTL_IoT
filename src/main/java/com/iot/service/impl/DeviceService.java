package com.iot.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iot.authentication.JwtTokenProvider;
import com.iot.converter.DeviceBeanUtil;
import com.iot.converter.SensorBeanUtil;
import com.iot.dao.IDeviceDao;
import com.iot.dao.ISensorDao;
import com.iot.dao.IUserDao;
import com.iot.dto.DeviceDto;
import com.iot.dto.SensorDto;
import com.iot.entity.DeviceEntity;
import com.iot.entity.SensorEntity;
import com.iot.entity.UserEntity;
import com.iot.service.IDeviceService;

@Service
public class DeviceService implements IDeviceService {
	@Autowired
	private IDeviceDao deviceDao;
	@Autowired
	private ISensorDao sensorDao;
	@Autowired
	private IUserDao userDao;
	@Autowired
	private JwtTokenProvider tokenProvider;

	@Override
	public DeviceDto findById(Long id) {
		return DeviceBeanUtil.entity2Dto(deviceDao.findById(id));
	}

	@Override
	@Transactional
	public DeviceDto save(DeviceDto dto) {
		DeviceDto result = null;
		if (dto != null && dto.getId() != null) {
			DeviceEntity entity = deviceDao.findById(dto.getId());
			dto.setUpdated_at(new Date());
			entity = DeviceBeanUtil.dto2Entity(dto, entity);
			deviceDao.update(entity);
			for (SensorDto sensor : dto.getSensorList()) {
				SensorEntity sensorEntity = sensorDao.findById(sensor.getId());
				sensorEntity = SensorBeanUtil.dto2Entity(sensor, sensorEntity);
				sensorDao.update(sensorEntity);
			}
			String JOIN_FETCH = "sensorList";
			entity = deviceDao.findByIdWithProp(entity.getId(), JOIN_FETCH, 0, "");
			result = DeviceBeanUtil.entity2Dto(entity, 1);

		} else if (dto != null && dto.getId() == null) {
			dto.setCreated_at(new Date());
			dto.setUpdated_at(new Date());
			try {
				DeviceEntity entity = DeviceBeanUtil.dto2Entity(dto);
				entity = deviceDao.save(entity);
				String tokenAuth=tokenProvider.generateTokenAuthActiveDevice(dto.getUserDto().getUsername(), entity.getId());
				//String code[]= {"SENSOR1","SENSOR2","SENSOR3","SENSOR4","SENSOR5"};
				for (SensorEntity sensor : entity.getSensorList()) {
					sensor.setDeviceEntity(entity);
					sensorDao.save(sensor);
				}
				/*
				 * Khi save thì listSensor của result cũng đã được gắn luôn id
				 */
				entity.setToken_auth(tokenAuth);
				entity=deviceDao.update(entity);
				result = DeviceBeanUtil.entity2Dto(entity);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		return result;
	}

	@Override
	public DeviceDto getListSensor(Long id) {
		DeviceDto result = null;
		try {
			String JOIN_FETCH = "sensorList";
			result = DeviceBeanUtil.entity2Dto(deviceDao.findByIdWithProp(id, JOIN_FETCH, 0, ""));
		} catch (Exception e) {
		}
		return result;
	}

	@Override
	public List<DeviceDto> getListDeviceByUser(String username) {
		List<DeviceDto> result = new ArrayList<DeviceDto>();
		try {
			for (DeviceEntity entity : deviceDao.getListDeviceByUser(username)) {
				result.add(DeviceBeanUtil.entity2Dto(entity, 0));
			}
		} catch (Exception e) {
		}
		return result;
	}

	@Override
	public DeviceDto getInfoDevice(Long id, String username) {
		DeviceDto result = null;
		try {
			String JOIN_FETCH = "sensorList s LEFT JOIN FETCH s.sensorDataList sd";
			result = DeviceBeanUtil.entity2Dto(deviceDao.findByIdWithProp(id, JOIN_FETCH, 0, username), 2);
		} catch (Exception e) {
		}
		return result;
	}

	@Override
	public List<DeviceDto> getListDeviceByAdmin(String username) {
		List<DeviceDto> result = new ArrayList<DeviceDto>();
		try {
			UserEntity user = userDao.findOneUsername(username);
			if (user != null && user.getRoleEntity().getCode().equals("ADMIN")) {
				for (DeviceEntity entity : deviceDao.getListDeviceByAdmin()) {
					result.add(DeviceBeanUtil.entity2Dto(entity, 0));
				}
			}
		} catch (Exception e) {
		}
		return result;
	}

}
