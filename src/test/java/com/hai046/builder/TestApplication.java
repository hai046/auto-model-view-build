package com.hai046.builder;

import com.hai046.builder.annotation.EnableView;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author denghaizhu
 * * date 2019-02-22
 */

@EnableView(viewBuilderPackages = "com.hai046")
@SpringBootApplication
@ComponentScan("com")
public class TestApplication {
    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }

}

