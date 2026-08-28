package com.equipsphere;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SmartEquipmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartEquipmentApplication.class, args);
        System.out.println("==================================================");
        System.out.println("🚀 Smart Equipment Backend is running on port 8080!");
        System.out.println("==================================================");
    }
}
