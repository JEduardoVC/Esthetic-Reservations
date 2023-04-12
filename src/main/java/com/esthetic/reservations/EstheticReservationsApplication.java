package com.esthetic.reservations;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.util.AntPathMatcher;

@SpringBootApplication
@EntityScan( basePackages = {"com.esthetic.reservations.api.model"} )
@EnableJpaRepositories()
public class EstheticReservationsApplication {

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
	public AntPathMatcher requestMatcher(){
		return new AntPathMatcher();
	}

	public static void main(String[] args) {
		SpringApplication.run(EstheticReservationsApplication.class, args);
	}

}