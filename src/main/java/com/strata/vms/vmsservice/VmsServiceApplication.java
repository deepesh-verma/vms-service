package com.strata.vms.vmsservice;

import com.strata.vms.vmsservice.service.DatabaseInitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class VmsServiceApplication {

	@Bean
	public ApplicationRunner applicationRunner(@Autowired DatabaseInitService databaseInitService) {
		return args -> {
			databaseInitService.initMasterData();
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(VmsServiceApplication.class, args);
	}

}
