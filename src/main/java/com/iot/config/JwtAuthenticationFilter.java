package com.iot.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.iot.authentication.JwtTokenProvider;
import com.iot.authentication.MyUser;
import com.iot.authentication.MyUserDetailsService;



public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private static final Logger logger = Logger.getLogger(JwtAuthenticationFilter.class);

	@Autowired
	private JwtTokenProvider tokenProvider;
	@Autowired
	private MyUserDetailsService myUserDetailsService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			// Lấy jwt từ request
			String jwt = getJwtFromRequest(request);
			
			if(jwt!=null && tokenProvider.validateJwtToken(jwt)) {
				String username=tokenProvider.getUserNameFromJwtToken(jwt);
				//Lấy thông tin người dùng từ username trong chuỗi jwt
				MyUser myUser=(MyUser) myUserDetailsService.loadUserByUsername(username);
				if(myUser!=null) {
					//Nếu người dùng hợp lệ, set thông tin cho security context
					UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(myUser, null,myUser.getAuthorities());
					
					authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					
					SecurityContextHolder.getContext().setAuthentication(authenticationToken);
				}
				
			}
			
		} catch (Exception e) {
			logger.error("Cannot set user authentication: " + e.getMessage());
		}
		filterChain.doFilter(request, response);
	}

	private String getJwtFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		// Kiểm tra xem header Authorization có chứa thông tin jwt không
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}

}
