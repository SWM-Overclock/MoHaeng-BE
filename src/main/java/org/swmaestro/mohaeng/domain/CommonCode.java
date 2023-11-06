package org.swmaestro.mohaeng.domain;

import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
public class CommonCode {

    @Id
    private String code;

    @Column
    private String codeName;

    @Column
    private String codeDescription;

    @Column
    private String codeType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_code")
    private CommonCode parentCode;

    @OneToMany(mappedBy = "parentCode", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommonCode> childCodes = new ArrayList<>();

}
