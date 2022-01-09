package org.temkarus0070.orderpersistence;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.temkarus0070.orderpersistence.models.Good;
import org.temkarus0070.orderpersistence.models.Order;
import org.temkarus0070.orderpersistence.models.Status;

import java.util.ArrayList;
import java.util.List;


@DataJpaTest
@ExtendWith(SpringExtension.class)
public class PersistenceTest {


    private OrdersRepository ordersRepository;


    @Autowired
    public void setOrdersRepository(OrdersRepository orderRepository) {
        this.ordersRepository = orderRepository;
    }

    @Test
    void test() {
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

        order = ordersRepository.saveAndFlush(order);


        final Order order1 = ordersRepository.findAll().get(0);

        Assertions.assertNotNull(order1);
        Assertions.assertEquals(order, order1);

    }
}
