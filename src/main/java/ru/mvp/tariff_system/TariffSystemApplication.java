package ru.mvp.tariff_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TariffSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(TariffSystemApplication.class, args);
	}

}
