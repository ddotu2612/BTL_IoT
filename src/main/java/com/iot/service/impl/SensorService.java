package com.iot.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iot.converter.SensorBeanUtil;
import com.iot.dao.IDeviceDao;
import com.iot.dao.ISensorDao;
import com.iot.dao.ISensorDataDao;
import com.iot.dto.DataDto;
import com.iot.dto.SensorAllDto;
import com.iot.dto.SensorDto;
import com.iot.entity.DeviceEntity;
import com.iot.entity.SensorDataEntity;
import com.iot.entity.SensorEntity;
import com.iot.mqtt.CollectDataModel;
import com.iot.mqtt.DataModel;
import com.iot.service.ISensorService;

@Service
public class SensorService implements ISensorService {

	@Autowired
	private ISensorDao sensorDao;
	@Autowired
	private ISensorDataDao sensorDataDao;
	@Autowired
	private IDeviceDao deviceDao;

	@Override
	public SensorDto save(SensorDto dto) {
		SensorDto result = null;
		if (dto != null && dto.getId() != null) {

		} else if (dto != null && dto.getId() == null) {
			result = SensorBeanUtil.entity2Dto(sensorDao.save(SensorBeanUtil.dto2Entity(dto)), 0);
		}
		return result;
	}

	@Override
	public List<SensorDto> findByListDeviceId(Long id) {
		List<SensorDto> result = new ArrayList<SensorDto>();
		for (SensorEntity entity : sensorDao.findByListDeviceId(id)) {
			result.add(SensorBeanUtil.entity2Dto(entity, 0));
		}
		return result;
	}

	@Override
	public void saveCollectData(CollectDataModel collectData) {
		/*
		 * Gửi là múi giờ thứ 7 (tức 7*60*60=25200) mà hàm dưới tự lấy múi giờ của
		 * mình(+7) =>Bị cộng 2 lần múi giờ thứ 7
		 */
		Date time = new Date(collectData.getTime() * 1000l - 25200000l);
		for (DataModel data : collectData.getSensorDataList()) {
			SensorDataEntity entity = new SensorDataEntity();
			SensorEntity sensor = sensorDao.findByCode(data.getCode(), collectData.getDeviceId());
			if (sensor == null || sensor.getStatus() == 0) {
				continue;
			}
			entity.setSensorEntity(sensor);
			entity.setTime(time);
			entity.setValue(data.getValue());
			sensorDataDao.save(entity);
		}
	}

	@Override
	public List<SensorDto> getAllData(Long deviceid, String prop, String date) {
		List<Long> ids = new ArrayList<Long>();
		List<SensorDto> result = new ArrayList<SensorDto>();
		//
		try {
			String fetch = "sensorList";
			DeviceEntity device = deviceDao.findByIdWithProp(deviceid, fetch, 1, "");
			if (device != null) {
				for (SensorEntity sensor : device.getSensorList()) {
					ids.add(sensor.getId());
				}
			}
			String condition = "";
			if (StringUtils.isNotBlank(prop)) {
				prop = prop.toUpperCase();
				String[] tmp = date.split("-");
				if (prop.equals("MONTH")) {
					// lấy theo tháng + năm
					if (tmp.length == 2) {
						condition = "AND month(time)=" + tmp[0] + " AND year(time)=" + tmp[1];
					}
					// Lấy theo tháng
					if (tmp.length == 1) {
						condition = "AND month(time)=" + tmp[0];
					}
					// prop = "AND month(time)=month(current_date())";
				} else if (prop.equals("YEAR")) {
					condition = "AND year(time)=" + tmp[0];
				} else if (prop.equals("DAY")) {
					if (tmp.length == 1) {
						condition = "AND day(time)=" + tmp[0];
					}
					if (tmp.length == 2) {
						condition = "AND day(time)=" + tmp[0] + " AND month(time)=" + tmp[1];
					}
					if (tmp.length == 3) {
						condition = "AND day(time)=" + tmp[0] + " AND year(time)=" + tmp[2];
						if (StringUtils.isNotBlank(tmp[1])) {
							condition = "AND day(time)=" + tmp[0] + " AND month(time)=" + tmp[1] + " AND year(time)="
									+ tmp[2];
						}
					}
				} else {
					return result;
				}
			}
			for (Long id : ids) {
				SensorEntity entity = sensorDao.findAllDataSensor(id, condition);
				if (entity != null) {
					result.add(SensorBeanUtil.entity2Dto(entity, 0));
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}

	@Override
	public List<SensorAllDto> getAllSensorData(Long deviceid, String prop, String date) {
		List<SensorEntity> sensorList = new ArrayList<SensorEntity>();
		List<SensorAllDto> result = new ArrayList<SensorAllDto>();
		try {
			String fetch = "sensorList";
			DeviceEntity device = deviceDao.findByIdWithProp(deviceid, fetch, 0, "");
			if (device != null) {
				for (SensorEntity sensor : device.getSensorList()) {
					sensorList.add(sensor);
				}
			}
			int status = 0;
			if (prop.equals("year")) {
				status = 1;
			}
			for (SensorEntity sensor : sensorList) {
				SensorAllDto sensorAllDto = new SensorAllDto();
				sensorAllDto.setCode(sensor.getCode());
				sensorAllDto.setId(sensor.getId());
				sensorAllDto.setStatus(sensor.getStatus());
				sensorAllDto.setName(sensor.getName());
				List<Object[]> listData = sensorDao.getAllDataSensorWithProp(sensor.getId(), status, date);
				List<DataDto> listOfDTO = listData.stream().map(DataDto::new).collect(Collectors.toList());
				sensorAllDto.setListData(listOfDTO);
				result.add(sensorAllDto);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return result;
	}
}
