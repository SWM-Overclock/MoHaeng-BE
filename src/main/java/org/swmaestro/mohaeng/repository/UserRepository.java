package org.swmaestro.mohaeng.repository;

import org.springframework.data.jpa.repository.JpaRepository;
<<<<<<< HEAD
import org.springframework.stereotype.Repository;
=======
>>>>>>> 26940b0 (Feat: User Entity, Repository 작성)
import org.swmaestro.mohaeng.domain.user.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
<<<<<<< HEAD
}
=======
}
>>>>>>> 26940b0 (Feat: User Entity, Repository 작성)
