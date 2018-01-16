package com.vanadis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling//增加支持定时任务的注解
@MapperScan("com.vanadis.mapper")
public class VapApplication {

	public static void main(String[] args) {
		SpringApplication.run(VapApplication.class, args);
	}
}
