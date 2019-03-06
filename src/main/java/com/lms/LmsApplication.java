package com.lms;

import com.lms.config.JwtFilterAdmin;
import com.lms.config.JwtFilterStudent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LmsApplication {

    @Bean
    public FilterRegistrationBean jwtFilterStudent() {
        final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new JwtFilterStudent());
        registrationBean.addUrlPatterns("/lms/authorizedStudent/*");

        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean jwtFilterAdmin() {
        final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new JwtFilterAdmin());
        registrationBean.addUrlPatterns("/lms/authorizedAdmin/*");

        return registrationBean;
    }

    public static void main(String[] args) {
        SpringApplication.run(LmsApplication.class, args);
    }

}
