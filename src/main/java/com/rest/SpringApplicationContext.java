package com.rest;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

// This class is used to force Spring to load the application context.
// This is needed to access the beans in the application context.
// We created this class, so we can call UserServiceImpl in the AuthenticationFilter class
public class SpringApplicationContext implements ApplicationContextAware {

    private static ApplicationContext CONTEXT;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        CONTEXT = applicationContext;
    }

    public static Object getBean(String name) {
        return CONTEXT.getBean(name);
    }
}