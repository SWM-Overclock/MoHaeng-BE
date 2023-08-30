package org.swmaestro.mohaeng.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.swmaestro.mohaeng.domain.Location;


public interface LocationRepository extends JpaRepository<Location, Long> {

}
