package org.swmaestro.mohaeng.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.swmaestro.mohaeng.domain.shop.ShopDetail;

import java.util.List;

public interface ShopDetailRepository extends JpaRepository<ShopDetail, Long> {

    @Query(value = "SELECT * FROM shop_detail " +
            "WHERE ST_DISTANCE_SPHERE(" +
            "point(shop_longitude, shop_latitude), " +
            "point(:longitude, :latitude)) <= :radius " +
            "AND status = 'ACTIVE'",
            nativeQuery = true)
    List<ShopDetail> findShopDetailsWithinRadius(@Param("longitude") double longitude, @Param("latitude") double latitude, @Param("radius") double radius);
}
