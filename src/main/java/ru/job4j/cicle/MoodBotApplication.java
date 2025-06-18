package ru.job4j.cicle;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MoodBotApplication {
    public static void main(String[] args) {
        SpringApplication.run(MoodBotApplication.class, args);

    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            AppConfigTwo appConfig = ctx.getBean(AppConfigTwo.class);
            appConfig.printConfig();
        };
    }
}
