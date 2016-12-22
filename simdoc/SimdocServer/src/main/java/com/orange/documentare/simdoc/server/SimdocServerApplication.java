package com.orange.documentare.simdoc.server;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.regex;

@SpringBootApplication
@EnableSwagger2
@ComponentScan
public class SimdocServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimdocServerApplication.class, args);
	}

  @Bean
  public Docket simdocApi() {
    return new Docket(DocumentationType.SWAGGER_2)
      .select()
        .paths(paths())
        .build();
  }

  private Predicate<String> paths() {
    return Predicates.or(
      regex("/clustering")
    );
  }
}
