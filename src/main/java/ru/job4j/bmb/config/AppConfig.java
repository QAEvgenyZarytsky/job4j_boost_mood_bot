package ru.job4j.bmb.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

@Component
@EnableAspectJAutoProxy
public class AppConfig {
    @Value("${telegram.bot.name}")
    private String telegramBotName;

    @Value("${telegram.bot.token}")
    private String accessToken;

    @Value("${app.version}")
    private String appVersion;

    @Value("${app.url}")
    private String appBaseUrl;

    @Value("${app.timeout}")
    private Integer sleepTimeout;

    @Value("${app.random.value:Need add random value}")
    private String randomValue;

    public void getInfo() {
        System.out.println("Telegram Bot Name: " + telegramBotName);
        System.out.println("Access token: " + accessToken);
        System.out.println("App Version: " + appVersion);
        System.out.println("App Base Url: " + appBaseUrl);
        System.out.println("Sleep Timeout: " + sleepTimeout);
        System.out.println("Random Value: " + randomValue);
    }
}
