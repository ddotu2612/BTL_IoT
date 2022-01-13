package com.iot.authentication;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtTokenProvider {

	private static final Logger logger = Logger.getLogger(JwtTokenProvider.class);

	// Đoạn JWT_SECRET này là bí mật, chỉ có phía server biết
	private final String JWT_SECRET = "jwt_dinh_1008";

	// Thời gian có hiệu lực của chuỗi jwt
	private final long JWT_EXPIRATION = 604800000L;//chia 1000 sẽ ra giây

	// Tạo chuỗi jwt từ thông tin của user
	public String generateJwtTokenUsername(Authentication authentication) {
		MyUser userPrincipal = (MyUser) authentication.getPrincipal();

		return Jwts.builder().setSubject(userPrincipal.getUsername())
				.setExpiration(new Date((new Date()).getTime() + JWT_EXPIRATION))
				.signWith(SignatureAlgorithm.HS512, JWT_SECRET).compact();
	}

	// Lấy thông tin username
	public String getUserNameFromJwtToken(String token) {
		return Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token).getBody().getSubject();
	}
	
	// Tạo chuỗi từ username, deivceid =>dùng để active device
	public String generateTokenAuthActiveDevice(String username, Long deviceid) {
		Claims claims = Jwts.claims().setSubject(username);
		claims.put("deviceId", deviceid);
		
		return Jwts.builder().setClaims(claims)
				.setExpiration(new Date((new Date()).getTime() + 60480000000L)) //thời gian có hiệu lực là 700 ngày
				.signWith(SignatureAlgorithm.HS512, JWT_SECRET).compact();
	}
 	//Lấy thông tin active device
	
	public List<String> parseTokenAuthenActiveData(String token){
		List<String> result=new ArrayList<String>();
		try {
			Claims body = Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token).getBody();
			String username=String.valueOf(body.get("sub"));
			String deviceId=String.valueOf(body.get("deviceId"));
			
			if(StringUtils.isNotBlank(username) && StringUtils.isNotBlank(deviceId)) {
				result.add(username);
				result.add(deviceId);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		
		return result;
	}
	

	// Tạo chuỗi từ userid, deviceid =>DEVICE_TOKEN
	public String generateTokenCollectData(Long userid, Long deviceid) {
		Claims claims = Jwts.claims().setSubject(String.valueOf(userid));
		claims.put("deviceId", deviceid);
		
		return Jwts.builder().setClaims(claims)
				.setExpiration(new Date((new Date()).getTime() + 360000L)) //thời gian có hiệu lực là 6 phút
				.signWith(SignatureAlgorithm.HS512, JWT_SECRET).compact();
	}

	// Lấy thông tin từ chuỗi DEVICE_TOKEN
	public List<Long> parseTokenCollectData(String token) {
		List<Long> result = new ArrayList<Long>();
		try {
			Claims body = Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token).getBody();

			Long userId = Long.parseLong(String.valueOf(body.get("sub")));
			Long deviceId = Long.parseLong(String.valueOf(body.get("deviceId")));
			result.add(userId);
			result.add(deviceId);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return (result.size() == 0) ? null : result;
	}

	// Kiểm tra thông tin token. Có 
	public boolean validateJwtToken(String authToken) {
		try {	
			Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(authToken);
			return true;
		} catch (SignatureException e) {
			logger.error("Invalid JWT signature: " + e.getMessage());
		} catch (MalformedJwtException e) {
			logger.error("Invalid JWT token: " + e.getMessage());
		} catch (ExpiredJwtException e) {
			logger.error("JWT token is expired: " + e.getMessage());
		} catch (UnsupportedJwtException e) {
			logger.error("JWT token is unsupported: " + e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.error("JWT claims string is empty: " + e.getMessage());
		}

		return false;
	}

}
