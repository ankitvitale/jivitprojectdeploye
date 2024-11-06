//package com.jivitHealcare.Configuration;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration  // Make sure the class is marked as a Spring configuration class
//public class WebConfig{
//
//    private static final String GET = "GET";
//    private static final String POST = "POST";
//    private static final String PUT = "PUT";
//    private static final String DELETE = "DELETE";
//  //  private static final String OPTIONS = "OPTIONS";
//
//
//    @Bean
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/**") // Allow all URL patterns
//                        .allowedMethods(GET, POST, PUT, DELETE) // Allow these HTTP methods
//                        .allowedHeaders("*") // Allow all headers
//                        .allowedOriginPatterns("*") // Allow all origins (can be customized if needed)
//                        .allowCredentials(true); // Allow credentials like cookies or authorization headers
//            }
//        };
//    }
//}