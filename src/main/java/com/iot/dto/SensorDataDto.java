package com.iot.dto;

import java.io.Serializable;
import java.util.Date;

public class SensorDataDto implements Serializable {

	private static final long serialVersionUID = 2397926905014562098L;

	private Long id;
	private Float value;
	private Date time;
	private SensorDto sensorDto;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Float getValue() {
		return value;
	}

	public void setValue(Float value) {
		this.value = value;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public SensorDto getSensorDto() {
		return sensorDto;
	}

	public void setSensorDto(SensorDto sensorDto) {
		this.sensorDto = sensorDto;
	}

}
