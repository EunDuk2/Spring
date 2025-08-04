package com.beyond.board;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

// ComponentScan은 Application파일을 포함한 경로 하위의 요소들만 scan가능
@SpringBootApplication
@EnableScheduling // 스케줄러 사용 시 필요한 어노테이션
public class BoardApplication {

	public static void main(String[] args) {

		SpringApplication.run(BoardApplication.class, args);
	}

}
