package org.sid;

import org.sid.Controller.HomeController;
import org.sid.service.ICovidInitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.List;


@SpringBootApplication
@EnableScheduling
public class Covid19TrackerApplication implements CommandLineRunner {

	@Autowired
	private ICovidInitService covidInitService;

	public static void main(String[] args) {
		SpringApplication.run(Covid19TrackerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		covidInitService.initInfected();
		covidInitService.initDeaths();
		covidInitService.initRecovered();

	}
}
