package com.everrefine.elms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

/**
 * ステレオタイプアノテーション
 *
 * @Configuration @Bean
 *
 * @Repository コンストラクタ
 * @Service コンストラクタ
 * @Controller コンストラクタ
 * @Component コンストラクタ
 */
@SpringBootApplication
@EnableJdbcRepositories
public class ElmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ElmsApplication.class, args);
	}

}
