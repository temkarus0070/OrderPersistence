package org.temkarus0070.orderpersistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.temkarus0070.orderpersistence.models.Order;

public interface OrdersRepository extends JpaRepository<Order,Long> {
}
