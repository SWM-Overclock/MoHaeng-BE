package org.swmaestro.mohaeng.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.swmaestro.mohaeng.domain.user.User;
import org.swmaestro.mohaeng.repository.RepositoryTest;
import org.swmaestro.mohaeng.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@RepositoryTest
class BaseTimeEntityTest {

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("유저를 저장하면 생성 시각이 자동으로 저장된다")
    public void testUserCreatedAtNow() {
        User user = User.createUser("test@mohaeng.org", "test", "test", "test", "test");

        userRepository.save(user);

        assertThat(user.getCreatedAt()).isNotNull();
    }
}