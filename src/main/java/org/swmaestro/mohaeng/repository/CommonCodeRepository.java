package org.swmaestro.mohaeng.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.swmaestro.mohaeng.domain.CommonCode;

public interface CommonCodeRepository extends JpaRepository<CommonCode, String> {
}
