package org.swmaestro.mohaeng.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.swmaestro.mohaeng.domain.Admin;

public interface AdminRepository extends JpaRepository<Admin, String> {
}
