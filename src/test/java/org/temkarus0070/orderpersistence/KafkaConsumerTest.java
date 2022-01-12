package org.temkarus0070.orderpersistence;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.AcknowledgingConsumerAwareMessageListener;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.temkarus0070.orderpersistence.models.Order;
import org.temkarus0070.orderpersistence.models.Status;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
public class KafkaConsumerTest {


    private static String SENDER_TOPIC = "orders";
    private KafkaTemplate<Long, Order> kafkaProducer;
    @Autowired
    private KafkaListenerEndpointRegistry registry;


    @Autowired
    private EmbeddedKafkaBroker embeddedKafka;

    @BeforeEach
    public void setUp() throws Exception {
        Map<String, Object> senderProps = KafkaTestUtils.producerProps(embeddedKafka);
        senderProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_DOC, LongSerializer.class.getName());
        senderProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_DOC, JsonSerializer.class.getName());
        ProducerFactory<Long, Order> pf = new DefaultKafkaProducerFactory<>(senderProps, new LongSerializer(), new JsonSerializer<>());
        this.kafkaProducer = new KafkaTemplate<>(pf);


    }


    @Test
    public void testSend() throws InterruptedException {
        Random random = new Random(new Date().getTime());
        Order order = new Order();
        order.setClientFIO("Pupkin");
        order.setOrderNum(random.nextLong());
        order.setGoods(new ArrayList<>());
        order.setStatus(Status.NEW);

        ConcurrentMessageListenerContainer<?, ?> container = (ConcurrentMessageListenerContainer<?, ?>) registry
                .getListenerContainer("orderListener");
        container.stop();
        AcknowledgingConsumerAwareMessageListener<String, String> messageListener = (AcknowledgingConsumerAwareMessageListener<String, String>) container
                .getContainerProperties().getMessageListener();
        CountDownLatch latch = new CountDownLatch(1);
        container.getContainerProperties()
                .setMessageListener((AcknowledgingConsumerAwareMessageListener<String, String>) (data, acknowledgment, consumer) -> {
                    messageListener.onMessage(data, acknowledgment, consumer);
                    latch.countDown();
                });
        container.start();
        kafkaProducer.send(SENDER_TOPIC, order.getOrderNum(), order);
        Assertions.assertTrue(latch.await(10, TimeUnit.SECONDS));
        container.stop();

    }
}
