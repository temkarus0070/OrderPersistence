package org.temkarus0070.orderpersistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {
    private final OrdersRepository ordersRepository;

    @Autowired
    public KafkaConsumer(OrdersRepository ordersRepository) {
        this.ordersRepository = ordersRepository;
    }

    @KafkaListener(topics = {"orders"})
    public void consume(org.temkarus0070.models.Order order){

    }

    public org.temkarus0070.models.Order validate(org.temkarus0070.models.Order order){

    }

}
