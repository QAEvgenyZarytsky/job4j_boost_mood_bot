package ru.job4j.recommendation;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

public class RecommendationEngine {

    @PostConstruct
    public void init() {
        System.out.println("RecommendationEngine init");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("RecommendationEngine destroy");
    }
}
