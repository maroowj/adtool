package com.muzi.adtool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.sql.DataSource;
import java.nio.charset.Charset;

@SpringBootApplication
public class AdtoolApplication extends SpringBootServletInitializer {

	/** 배포 시 수정해야 함 **/

//	String ver="web";
	String ver="local";

	@Bean("uploadPath")
	public String path() {
		if(ver.equals("web")) {
			return "./tomcat/webapps/ROOT/WEB-INF/classes/images/"; // 웹 배포용
		}else if(ver.equals("local")) {
			return "./src/main/resources/images/"; // 로컬작업용
		}
		return "";
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(AdtoolApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(AdtoolApplication.class, args);
	}

	@Bean
	public HttpMessageConverter<String> responseBodyConverter() {
		return new StringHttpMessageConverter(Charset.forName("UTF-8"));
	}

	@Bean
	public CharacterEncodingFilter characterEncodingFilter() {
		CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
		characterEncodingFilter.setEncoding("UTF-8");
		characterEncodingFilter.setForceEncoding(true);
		return characterEncodingFilter;
	}


}
