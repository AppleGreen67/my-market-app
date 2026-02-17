package ru.yandex.practicum.mymarket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class Y5MarketApplication {

    public static void main(String[] args) {
        SpringApplication.run(Y5MarketApplication.class, args);
    }

}
