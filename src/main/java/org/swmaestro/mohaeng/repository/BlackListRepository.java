package org.swmaestro.mohaeng.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.swmaestro.mohaeng.domain.BlackList;

public interface BlackListRepository extends JpaRepository<BlackList, Long> {
}
