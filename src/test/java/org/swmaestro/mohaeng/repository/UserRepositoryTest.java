package org.swmaestro.mohaeng.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.swmaestro.mohaeng.domain.user.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    public void setup() {
        // Common setup code for each test
        user = User.createUser(
            "test@mohaeng.org",
            "test",
            "test",
            "test",
            "test"
        );
        userRepository.save(user);
    }

    @Test
    @DisplayName("유저를 이메일로 조회한다.")
    public void testFindByEmail_PositiveCase() {
        // When
        User foundUser = userRepository.findByEmail("test@mohaeng.org");
        // Then
        assertEquals("test@mohaeng.org", foundUser.getEmail());
    }

    @Test
    @DisplayName("유저를 이메일로 조회한다.")
    public void testFindByEmail_NegativeCase() {
        // When
        User foundUser = userRepository.findByEmail("wrong@mohaeng.org");
        // Then
        assertNull(foundUser);
    }
}