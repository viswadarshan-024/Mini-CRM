package com.viswa.crm;

import com.viswa.crm.config.CRMConfig;
import com.viswa.crm.service.impl.AuthServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class App 
{
    public static void main( String[] args )
    {
        ApplicationContext context = new AnnotationConfigApplicationContext(CRMConfig.class);

        AuthServiceImpl authService = (AuthServiceImpl) context.getBean("authService");


        System.out.println( "Hello World!" );
    }
}
