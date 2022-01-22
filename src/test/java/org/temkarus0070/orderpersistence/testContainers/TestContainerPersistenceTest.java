package org.temkarus0070.orderpersistence.testContainers;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.temkarus0070.orderpersistence.OrdersRepository;
import org.temkarus0070.orderpersistence.models.Good;
import org.temkarus0070.orderpersistence.models.Order;
import org.temkarus0070.orderpersistence.models.Status;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(initializers = {TestContainerPersistenceTest.Initializer.class})
public class TestContainerPersistenceTest {
    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:11.1")
            .withDatabaseName("integration-tests-db")
            .withUsername("sa")
            .withPassword("sa");
    @Autowired
    private OrdersRepository ordersRepository;

    @Test
    @Transactional
    public void testEntitySave() {
        List<Good> list = new ArrayList<>();
        Good good = new Good();
        good.setCount(2);
        good.setPrice(100);
        good.setSum(200);
        good.setId(1);
        good.setName("Vanish");
        list.add(good);
        Order order = new Order();
        good.setOrder(order);
        order.setGoods(list);
        order.setStatus(Status.NEW);
        order.setOrderNum(1L);
        order.setClientFIO("Pupkin");
        final Order savedOrder = ordersRepository.saveAndFlush(order);
        final Order foundOrder = ordersRepository.findAll().get(0);
        foundOrder.getGoods().forEach((good1 -> {
        }));
        Assertions.assertNotNull(foundOrder);
        Assertions.assertEquals(savedOrder, foundOrder);
    }

    static class Initializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
                    "spring.datasource.username=" + postgreSQLContainer.getUsername(),
                    "spring.datasource.password=" + postgreSQLContainer.getPassword()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }
}
