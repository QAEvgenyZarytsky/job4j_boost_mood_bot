package ru.job4j.bmb;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import ru.job4j.bmb.config.AppConfig;
import ru.job4j.bmb.services.TelegramBotService;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    public CommandLineRunner initTelegramApi(ApplicationContext ctx) {
        return args -> {
            var bot = ctx.getBean(AppConfig.class);
            bot.getInfo();
            var getBeans = ctx.getBean(TelegramBotService.class);

            getBeans.setBeanName("BeanName");
            getBeans.displayAllBeanNames();
        };
    }
}