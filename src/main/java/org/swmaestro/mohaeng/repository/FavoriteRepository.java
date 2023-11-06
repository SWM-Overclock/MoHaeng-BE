package org.swmaestro.mohaeng.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.swmaestro.mohaeng.domain.Favorite;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
}
