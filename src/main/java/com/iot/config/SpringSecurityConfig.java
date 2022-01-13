package com.iot.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.iot.authentication.MyUserDetailsService;



@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
 
	@Autowired
	private MyUserDetailsService myUserDetailsService;
	
	//Sử dụng BCrypt khi đăng ký tài khoản hoặc đăng nhập->chuyển cái mình đăng nhập sang mã rồi làm những cái linh tinh
	//@Autowired
    //private PasswordEncoder passwordEncoder;
	
	//cấu hình BCrypt , phương thức đẻ băm mật khẩu
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	//cấu hình BCrypt
	@Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(myUserDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }
	
	// Thông tin chứng thực
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		// Các User trong bộ nhớ (MEMORY).
		auth.inMemoryAuthentication().withUser("user1").password("12345").roles("USER");
		auth.inMemoryAuthentication().withUser("admin1").password("12345").roles("USER,ADMIN");

		// Các User trong Database
		auth.userDetailsService(myUserDetailsService);
		auth.authenticationProvider(authenticationProvider()); //cấu hình BCrypt
	}

	// Thông tin phân quyền
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();

		// Cấu hình các trang ko cần login
		http.authorizeRequests().antMatchers("/", "/trang-chu", "/login", "/logout", "/register", "/api/admin/auth/signup").permitAll();

		// Trang /profile yêu cầu login với vai trò USER hoặc ADMIN.
		// Nếu chưa login, nó sẽ redirect tới trang /login
		http.authorizeRequests().antMatchers("/profile").access("hasAnyRole('ROLE_USER','ROLE_ADMIN')");
		//http.authorizeRequests().antMatchers("/profile").hasAnyRole("ROLE_USER","ROLE_ADMIN");

		// Trang chỉ dành cho ADMIN
		http.authorizeRequests().antMatchers("/admin").access("hasAnyRole('ROLE_ADMIN')");

		// Khi người dùng đã login, với vai trò XX.
		// Nhưng truy cập vào trang yêu cầu vai trò YY,
		// Ngoại lệ AccessDeniedException sẽ ném ra.
		http.authorizeRequests().and().exceptionHandling().accessDeniedPage("/accessDenied");
		
		//Khi chưa đăng nhập mà vào các trang cần yêu cầu quyền thì nó sẽ mặc định vào trang login nhé.
		
		
		// Cấu hình cho Login Form.
		http.authorizeRequests().and().formLogin()//
				// Submit URL của trang login
				.loginProcessingUrl("/j_spring_security_check") // Submit URL
				.loginPage("/login")//
				.defaultSuccessUrl("/profile")//
				.failureUrl("/login?error=true")//
				.usernameParameter("username")//
				.passwordParameter("password")
				// Cấu hình cho Logout Page.
				.and().logout().logoutUrl("/logout").logoutSuccessUrl("/trang-chu?logout");
		
		http.authorizeRequests().antMatchers("/api/user/me").authenticated();
		http.authorizeRequests().antMatchers("/api/auth/edit").access("hasAnyRole('ROLE_USER','ROLE_ADMIN')");
		http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
		
	}
	
	@Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }
	
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
}
