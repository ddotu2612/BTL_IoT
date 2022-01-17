package com.iot.converter;

import com.iot.dto.SensorDataDto;
import com.iot.dto.SensorDto;
import com.iot.entity.SensorDataEntity;
import com.iot.entity.SensorEntity;

public class SensorBeanUtil {

	public static SensorDto entity2Dto(SensorEntity entity, int k) {
		SensorDto dto = new SensorDto();
		dto.setId(entity.getId());
		dto.setName(entity.getName());
		dto.setStatus(entity.getStatus());
		dto.setCode(entity.getCode());
		try {
			if (entity.getDeviceEntity() != null && k != 1 && k != 2) {
				dto.setDeviceDto(DeviceBeanUtil.entity2Dto(entity.getDeviceEntity()));
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		try {
			if (k != 2) {
				for (SensorDataEntity sensorData : entity.getSensorDataList()) {
					dto.getSensorDataList().add(SensorDataBeanUtil.entity2Dto(sensorData, 1));
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return dto;
	}

	public static SensorEntity dto2Entity(SensorDto dto) {
		SensorEntity entity = new SensorEntity();
		entity.setId(dto.getId());
		entity.setName(dto.getName());
		entity.setCode(dto.getCode());
		entity.setStatus(dto.getStatus());
		try {
			if (dto.getDeviceDto() != null) {
				entity.setDeviceEntity(DeviceBeanUtil.dto2Entity(dto.getDeviceDto()));
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		try {
			for (SensorDataDto sensorDataDto : dto.getSensorDataList()) {
				entity.getSensorDataList().add(SensorDataBeanUtil.dto2Entity(sensorDataDto));
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return entity;
	}

	public static SensorEntity dto2Entity(SensorDto dto, SensorEntity entity) {
		entity.setName(dto.getName());
		entity.setStatus(dto.getStatus());

		return entity;
	}

}
