package com.ndsc.myClickCal.start;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaRepositories("com.ndsc.**.dao") // JPA扫描该包路径下的Repositorie
@EntityScan("com.ndsc.**.entity") // 扫描Entity实体类
@ComponentScan(basePackages = {"com.**"})
@EnableScheduling
public class BootstrapApplication {

	public static void main(String[] args) {

		SpringApplication.run(BootstrapApplication.class, args);
	}
}
