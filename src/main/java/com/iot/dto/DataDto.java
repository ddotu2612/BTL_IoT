package com.iot.dto;

public class DataDto {
	Integer date;
	Float sum;
	Integer count;

	public DataDto() {

	}

	public DataDto(Object[] object) {
		try {
			this.date = Integer.valueOf(String.valueOf(object[0]));
			this.sum = Float.valueOf(String.valueOf(object[2]));
			this.count = Integer.valueOf(String.valueOf(object[3]));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public Integer getDate() {
		return date;
	}

	public void setDate(Integer date) {
		this.date = date;
	}

	public Float getSum() {
		return sum;
	}

	public void setSum(Float sum) {
		this.sum = sum;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

}
