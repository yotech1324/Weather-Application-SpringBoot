// outgoing HTTP requests (like fetching data from the OpenWeather API). 

package com.example.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestConfiguration {

    @Bean //defining bean kyunki mutiple jagah use krna hai
    public RestTemplate restTemplate() {
        return new RestTemplate();
    } //use Rest client


}
