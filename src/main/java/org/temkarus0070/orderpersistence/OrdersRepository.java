package org.temkarus0070.orderpersistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.temkarus0070.orderpersistence.models.Order;

@Repository
public interface OrdersRepository extends JpaRepository<Order,Long> {
}
