package ru.job4j.cicle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class MyService {

    private final MyBeanClass myBean;

    @Autowired
    public MyService(MyBeanClass myBean) {
        this.myBean = myBean;
    }

    public void performService() {
        myBean.doSomething();
    }

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        MyBeanClass myBean = (MyBeanClass) context.getBean("myBean");
        myBean.doSomething();
        System.out.println(myBean.getProperty());
    }
}