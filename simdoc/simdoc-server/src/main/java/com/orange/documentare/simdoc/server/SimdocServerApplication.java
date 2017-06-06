package com.orange.documentare.simdoc.server;

/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
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
      .apiInfo(apiInfo())
      .select()
      .paths(paths())
      .build();
  }

  private ApiInfo apiInfo() {
    return new ApiInfoBuilder()
      .title("Simdoc REST API")
      .description("This API provides access to simdoc treatments, such as clustering of documents")
      .termsOfServiceUrl("https://github.com/Orange-OpenSource/documentare-simdoc")
      .contact(new Contact("Joël Gardes & Christophe Maldivi & Denis Boisset", "https://github.com/Orange-OpenSource/documentare-simdoc", "christophe.maldivi@orange.com"))
      .license("GPLv2")
      .licenseUrl("https://github.com/Orange-OpenSource/documentare-simdoc/blob/master/LICENSE")
      .version("0.2")
      .build();
  }

  private Predicate<String> paths() {
    return Predicates.or(
      regex("/clustering"), regex("/distances"), regex("/task.*")
    );
  }
}
