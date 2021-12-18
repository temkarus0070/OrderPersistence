package org.temkarus0070.orderpersistence;

import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.temkarus0070.orderpersistence.models.Order;

@Service
public class KafkaConsumer {
    private final OrdersRepository ordersRepository;

    @Value("${payService.address}")
    private String payServiceAddress;



    @Autowired
    public KafkaConsumer(OrdersRepository ordersRepository) {
        this.ordersRepository = ordersRepository;
    }

    @KafkaListener(id = "group_id",topics = {"orders"})
    public void consume(org.temkarus0070.orderpersistence.models.Order order){

        ordersRepository.save(order);
        order=validate(order);
        ordersRepository.save(order);
    }

    public org.temkarus0070.orderpersistence.models.Order validate(org.temkarus0070.orderpersistence.models.Order order){
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<org.temkarus0070.orderpersistence.models.Order> responseEntity =restTemplate.postForEntity(payServiceAddress+"/payment",order, org.temkarus0070.orderpersistence.models.Order.class);
        return responseEntity.getBody();

    }



}
