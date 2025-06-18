package ru.job4j.bmb.services;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import ru.job4j.bmb.content.Content;

@Service
public class TelegramBotService implements BeanNameAware, ApplicationContextAware {
    private ApplicationContext applicationContext;

    public void receive(Content content) {
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void setBeanName(String name) {
        System.out.println("Имя бина: " + name);
    }

    public void displayAllBeanNames() {
        String[] beanNames = applicationContext.getBeanDefinitionNames();
        for (String beaName : beanNames) {
            System.out.println(beaName);
        }
    }
}
