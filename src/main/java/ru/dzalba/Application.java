package ru.dzalba;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "ru.dzalba")
public class Application {
    public static void main(String[] args) {
        System.out.println("View test execution.");
    }
}