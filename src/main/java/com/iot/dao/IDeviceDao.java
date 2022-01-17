package com.iot.dao;

import java.util.List;

import com.iot.entity.DeviceEntity;

public interface IDeviceDao extends GenericDao<Long, DeviceEntity>{
	DeviceEntity findByIdAndUsername(String username, Long id);
	DeviceEntity findByIdAndUserID(Long userID, Long id);
	DeviceEntity findByIdWithProp(Long id, String JOIN_FETCH,int status, String username);
	DeviceEntity findByIdUser(Long id);
	void updateKeepAlive(List<Long> ids);
	List<DeviceEntity> getListDeviceByUser(String username);
	List<DeviceEntity> getListDeviceByAdmin();
}
