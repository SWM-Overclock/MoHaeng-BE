package org.swmaestro.mohaeng.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.swmaestro.mohaeng.domain.user.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
