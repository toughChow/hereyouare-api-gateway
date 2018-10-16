package org.toughchow.hereyouare.web.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class InitController {

    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/userDetails/{userId}")
    @HystrixCommand(fallbackMethod = "fallbackMethod")
    public Object getStudents(@PathVariable int userId) {
        System.out.println("Getting User details fot " + userId);

        String response = restTemplate.exchange("http://hereyouare-service/findUserDetails/{userId}", HttpMethod.GET, null, new ParameterizedTypeReference<String>() {
        },userId).getBody();

        System.out.println("Response body " + response);

        return "User Id " + userId + " [User details " + response + " ]";
    }

    public String fallbackMethod(int userId) {
        return "Fallback response:: No employee details available temporarily";
    }

    @Bean
    @LoadBalanced
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}
