package com.beyond.basic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

// ComponentScan은 Application파일을 포함한 경로 하위의 요소들만 scan가능
@SpringBootApplication

// 주로 Web서블릿 기반의 구성 요소(@WebServlet 등)를 스캔, 자동으로 빈으로 등록
@ServletComponentScan
@EnableScheduling // 스케줄러 사용 시 필요한 어노테이션
public class BasicApplication {

	public static void main(String[] args) {

		SpringApplication.run(BasicApplication.class, args);
	}

}
