package com.sparta.scv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ScvApplication {

  public static void main(String[] args) {
    SpringApplication.run(ScvApplication.class, args);
  }

}
