package org.swmaestro.mohaeng.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.swmaestro.mohaeng.domain.Owner;

public interface OwnerRepository extends JpaRepository<Owner, Long> {
}
