package org.temkarus0070.orderpersistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.temkarus0070.orderpersistence.models.Good;
import org.temkarus0070.orderpersistence.models.Status;

@Component
public class KafkaConsumer {
    private final OrdersRepository ordersRepository;

    private GoodRepository goodRepository;

    private KafkaProducer kafkaProducer;


    @Value("${payService.address}")
    private String payServiceAddress;


    private RestTemplate restTemplate;

    @Autowired
    public KafkaConsumer(final OrdersRepository ordersRepository) {
        this.ordersRepository = ordersRepository;
    }

    @Autowired
    public void setKafkaProducer(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @Autowired
    public void setGoodRepository(GoodRepository goodRepository) {
        this.goodRepository = goodRepository;
    }

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @KafkaListener(topics = {"orders"}, id = "orderListener")
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

    public org.temkarus0070.orderpersistence.models.Order validate(org.temkarus0070.orderpersistence.models.Order order)throws org.springframework.web.client.RestClientException {
        ResponseEntity<org.temkarus0070.orderpersistence.models.Order> responseEntity = null;
        responseEntity = restTemplate.postForEntity(payServiceAddress + "/payment", order, org.temkarus0070.orderpersistence.models.Order.class);
        if (responseEntity != null && responseEntity.getBody() != null)
            order.setStatus(responseEntity.getBody().getStatus());
        return order;
    }



}
