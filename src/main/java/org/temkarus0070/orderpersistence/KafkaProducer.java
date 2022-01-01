package org.temkarus0070.orderpersistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.temkarus0070.orderpersistence.models.Order;

@Service
public class KafkaProducer {
    @Value("${spring.kafka.properties.topic}")
    private String topic;
    private KafkaTemplate<Long,Order> kafkaTemplate;

    @Autowired
    public void setKafkaTemplate(KafkaTemplate<Long, Order> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(Order order){
        kafkaTemplate.send(topic,order.getOrderNum(),order);
    }
}
