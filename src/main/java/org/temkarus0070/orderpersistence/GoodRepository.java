package org.temkarus0070.orderpersistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.temkarus0070.orderpersistence.models.Good;

@Repository
public interface GoodRepository extends JpaRepository<Good,Long> {
}
