package com.iot.payloads;

import com.iot.authentication.MyUser;

public class JwtAuthResponse {
	MyUser user;
	String jwt;

	public JwtAuthResponse() {

	}

	public JwtAuthResponse(MyUser user, String jwt) {
		this.user = user;
		this.jwt = jwt;
	}

	public MyUser getUser() {
		return user;
	}

	public void setUser(MyUser user) {
		this.user = user;
	}

	public String getJwt() {
		return jwt;
	}

	public void setJwt(String jwt) {
		this.jwt = jwt;
	}

}
