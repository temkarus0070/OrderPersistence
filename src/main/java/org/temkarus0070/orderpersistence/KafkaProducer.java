package org.temkarus0070.orderpersistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.temkarus0070.orderpersistence.models.GoodDTO;
import org.temkarus0070.orderpersistence.models.Order;
import org.temkarus0070.orderpersistence.models.OrderDTO;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class KafkaProducer {
    @Value("${spring.kafka.properties.topic}")
    private String topic;
    private KafkaTemplate<Long,OrderDTO> kafkaTemplate;

    @Autowired
    public void setKafkaTemplate(KafkaTemplate<Long, OrderDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(Order order){
        List<GoodDTO> list=order.getGoods().stream().map((good)->new GoodDTO(good.getId(),good.getName(),good.getPrice(),good.getCount(),good.getSum())).collect(Collectors.toList());
        OrderDTO orderDTO=new OrderDTO(order.getOrderNum(),order.getClientFIO(),list,order.getStatus());
        kafkaTemplate.send(topic,orderDTO.getOrderNum(),orderDTO);
    }
}
