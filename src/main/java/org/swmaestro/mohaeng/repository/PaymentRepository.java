package org.swmaestro.mohaeng.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.swmaestro.mohaeng.domain.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
