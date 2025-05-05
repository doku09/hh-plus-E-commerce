package kr.hhplus.be.server.config;

import jakarta.servlet.Filter;
import kr.hhplus.be.server.config.filter.AdvanceLogFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Bean
	public FilterRegistrationBean<Filter> encodingFilter() {
		CharacterEncodingFilter filter = new CharacterEncodingFilter();
		filter.setEncoding("UTF-8");
		filter.setForceEncoding(true); // 요청, 응답 모두 UTF-8로 강제

		FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(filter);
		registrationBean.setOrder(0); // 가장 먼저 실행되도록!
		registrationBean.addUrlPatterns("/*");

		return registrationBean;
	}

	@Bean
	public FilterRegistrationBean<Filter> advanceLogFilter() {
		CharacterEncodingFilter filter = new CharacterEncodingFilter();
		filter.setEncoding("UTF-8");
		filter.setForceEncoding(true);

		FilterRegistrationBean<Filter> filterBean = new FilterRegistrationBean<>();
		filterBean.setFilter(new AdvanceLogFilter());
		filterBean.setOrder(1); // logFilter 다음에 실행됨
		filterBean.addUrlPatterns("/*");
		return filterBean;
	}


}
