package com.example.movieticket.config;
import com.paypal.base.rest.APIContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class PayPalConfig {
    @Bean
    public APIContext apiContext() {
        // Replace with actual PayPal credentials
        return new APIContext("AZuiTu1oD3On3MhFbpwCqS--C5vPY2jU674KYTMAbTiXWID3jcWKjRO6kYqnBdUGQfn08uX8X9iDYau3", "EAbqhAx7lAntPmAaoGPJrcBNNUuTbm1F8v-B52O1f_7dOyAp5pQKpE01_pSBhgGduCQU3fynT7xegXSi", "sandbox");
    }
}
