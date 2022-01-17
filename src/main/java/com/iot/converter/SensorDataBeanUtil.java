package com.iot.converter;

import com.iot.dto.SensorDataDto;
import com.iot.entity.SensorDataEntity;

public class SensorDataBeanUtil {

	public static SensorDataDto entity2Dto(SensorDataEntity entity) {
		SensorDataDto dto = new SensorDataDto();
		dto.setId(entity.getId());
		dto.setTime(entity.getTime());
		dto.setValue(entity.getValue());
		try {
			dto.setSensorDto(SensorBeanUtil.entity2Dto(entity.getSensorEntity(), 0));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return dto;
	}

	public static SensorDataEntity dto2Entity(SensorDataDto dto) {
		SensorDataEntity entity = new SensorDataEntity();
		entity.setId(dto.getId());
		entity.setTime(dto.getTime());
		entity.setValue(dto.getValue());
		entity.setSensorEntity(SensorBeanUtil.dto2Entity(dto.getSensorDto()));
		return entity;
	}

	public static SensorDataDto entity2Dto(SensorDataEntity entity, int k) {
		SensorDataDto dto = new SensorDataDto();
		dto.setId(entity.getId());
		dto.setTime(entity.getTime());
		dto.setValue(entity.getValue());
		try {
			if (k != 1) {
				dto.setSensorDto(SensorBeanUtil.entity2Dto(entity.getSensorEntity(), 0));
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return dto;
	}
}
