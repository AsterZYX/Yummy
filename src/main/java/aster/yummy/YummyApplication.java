package aster.yummy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class YummyApplication {

	public static void main(String[] args) {
		SpringApplication.run(YummyApplication.class, args);
	}

}

