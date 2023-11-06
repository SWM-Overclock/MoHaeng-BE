package org.swmaestro.mohaeng.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum Status {
    ACTIVE("active"),
    INACTIVE("inactive");

    private String value;
}