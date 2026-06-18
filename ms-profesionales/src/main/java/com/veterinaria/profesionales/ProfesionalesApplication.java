package com.veterinaria.profesionales;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication
@EnableFeignClients
public class ProfesionalesApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProfesionalesApplication.class, args);
    }
}
