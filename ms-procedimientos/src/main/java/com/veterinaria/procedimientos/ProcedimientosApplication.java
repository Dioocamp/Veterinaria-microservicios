package com.veterinaria.procedimientos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication
@EnableFeignClients
public class ProcedimientosApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProcedimientosApplication.class, args);
    }
}
