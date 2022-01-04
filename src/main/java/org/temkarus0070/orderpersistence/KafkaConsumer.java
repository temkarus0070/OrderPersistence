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
import org.temkarus0070.orderpersistence.models.Good;
import org.temkarus0070.orderpersistence.models.GoodDTO;
import org.temkarus0070.orderpersistence.models.Order;
import org.temkarus0070.orderpersistence.models.Status;

import java.util.UUID;

@Service()
public class KafkaConsumer {
    private final OrdersRepository ordersRepository;

    private GoodRepository goodRepository;

    private KafkaProducer kafkaProducer;

    @Value("${payService.address}")
    private String payServiceAddress;


    @Autowired
    public void setKafkaProducer(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @Autowired
    public void setGoodRepository(GoodRepository goodRepository) {
        this.goodRepository = goodRepository;
    }

    @Autowired
    public KafkaConsumer(OrdersRepository ordersRepository) {
        this.ordersRepository = ordersRepository;
    }

    @KafkaListener(topics = {"orders"})
    public void consume(org.temkarus0070.orderpersistence.models.Order order){
        for (Good good:order.getGoods()){
            good.setOrder(order);
        }
        order.setStatus(Status.PENDING);
       order=ordersRepository.saveAndFlush(order);
        try {
            order = validate(order);
        }
        catch (org.springframework.web.client.RestClientException exception){
            exception.printStackTrace();
        }
        ordersRepository.save(order);
        kafkaProducer.send(order);
    }

    public org.temkarus0070.orderpersistence.models.Order validate(org.temkarus0070.orderpersistence.models.Order order)throws org.springframework.web.client.RestClientException{
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<org.temkarus0070.orderpersistence.models.Order> responseEntity=null;
         responseEntity = restTemplate.postForEntity(payServiceAddress + "/payment", order, org.temkarus0070.orderpersistence.models.Order.class);

        order.setStatus(responseEntity.getBody().getStatus());
        return order;
    }



}
