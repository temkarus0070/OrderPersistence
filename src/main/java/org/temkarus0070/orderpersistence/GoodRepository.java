package org.temkarus0070.orderpersistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.temkarus0070.orderpersistence.models.Good;

public interface GoodRepository extends JpaRepository<Good,Long> {
}
