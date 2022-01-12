package org.temkarus0070.orderpersistence;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class OrderPersistenceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderPersistenceApplication.class, args);
    }

}
