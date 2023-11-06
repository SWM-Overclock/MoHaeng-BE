package org.swmaestro.mohaeng.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.swmaestro.mohaeng.domain.PaymentProduct;

public interface PaymentProductRepository extends JpaRepository<PaymentProduct, Long> {
}
