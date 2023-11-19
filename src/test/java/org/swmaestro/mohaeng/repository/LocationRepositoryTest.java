package org.swmaestro.mohaeng.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.swmaestro.mohaeng.domain.Location;
import org.swmaestro.mohaeng.domain.user.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class LocationRepositoryTest {

    @Autowired
    private LocationRepository locationRepository;

    @Test
    public void testSaveAndFindById() {

        // given
        User user = User.createUser(
                "test@mohaeng.org",
                "test",
                "test",
                "test",
                "test"
        );

        Location location = Location.of(user, "test", "123 Test Street", "37.422", "-122.084", true);

        // when
        locationRepository.save(location);
        Optional<Location> foundLocation = locationRepository.findById(location.getId());

        // then
        assertTrue(foundLocation.isPresent());
        assertEquals(location.getId(), foundLocation.get().getId());
        assertEquals(location.getName(), foundLocation.get().getName());
        assertEquals(location.getAddress(), foundLocation.get().getAddress());
        assertEquals(location.getLatitude(), foundLocation.get().getLatitude());
    }

}