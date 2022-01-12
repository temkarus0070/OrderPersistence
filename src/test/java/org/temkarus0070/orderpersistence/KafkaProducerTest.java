package org.temkarus0070.orderpersistence;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.temkarus0070.orderpersistence.models.Order;
import org.temkarus0070.orderpersistence.models.Status;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@EmbeddedKafka(topics = "orders", bootstrapServersProperty = "spring.kafka.bootstrap-servers", partitions = 1)
@DirtiesContext
public class KafkaProducerTest {
    private static String SENDER_TOPIC = "ordersToAnalyze";
    @Autowired
    private KafkaProducer kafkaProducer;
    private KafkaMessageListenerContainer<Long, Order> container;

    private BlockingQueue<ConsumerRecord<Long, Order>> records;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafka;

    @BeforeEach
    public void setUp() throws Exception {
        // set up the Kafka consumer properties
        Map<String, Object> consumerProperties =
                KafkaTestUtils.consumerProps("sender", "false",
                        embeddedKafka);
        consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class.getName());
        consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class.getName());
        consumerProperties.put("spring.json.value.default.type", Order.class.getName());

        // create a Kafka consumer factory
        DefaultKafkaConsumerFactory<Long, Order> consumerFactory =
                new DefaultKafkaConsumerFactory<>(
                        consumerProperties);

        // set the topic that needs to be consumed
        ContainerProperties containerProperties =
                new ContainerProperties(SENDER_TOPIC);

        // create a Kafka MessageListenerContainer
        container = new KafkaMessageListenerContainer<>(consumerFactory,
                containerProperties);

        // create a thread safe queue to store the received message
        records = new LinkedBlockingQueue<>();

        // setup a Kafka message listener
        container
                .setupMessageListener((MessageListener<Long, Order>) record -> records.add(record));

        // start the container and underlying message listener
        container.start();

        // wait until the container has the required number of assigned partitions
        ContainerTestUtils.waitForAssignment(container,
                embeddedKafka.getPartitionsPerTopic());
    }

    @AfterEach
    public void tearDown() {
        // stop the container
        container.stop();
    }

    @Test
    public void testSend() throws InterruptedException {
        Random random = new Random(new Date().getTime());
        Order order = new Order();
        order.setClientFIO("Pupkin");
        order.setOrderNum(random.nextLong());
        order.setGoods(new ArrayList<>());
        order.setStatus(Status.NEW);
        kafkaProducer.send(order);
        ConsumerRecord<Long, Order> received =
                records.poll(10000, TimeUnit.MILLISECONDS);
        Assertions.assertEquals(received.value(), order);
        container.stop();
    }
}
