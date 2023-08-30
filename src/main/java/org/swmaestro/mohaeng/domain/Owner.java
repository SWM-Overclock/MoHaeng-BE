package org.swmaestro.mohaeng.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.swmaestro.mohaeng.domain.shop.ShopDetail;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Owner extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "owner_id", nullable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "shop_detail_id", nullable = false)
    private ShopDetail shopDetail;

    @OneToMany
    @JoinColumn(name = "owner_id")
    private List<Payment> payments = new ArrayList<>();

    void addPayment(Payment payment) {
        payments.add(payment);
    }
}
