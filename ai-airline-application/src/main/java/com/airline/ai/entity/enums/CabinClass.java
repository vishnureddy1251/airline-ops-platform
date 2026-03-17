package com.airline.ai.entity.enums;

import lombok.Getter;

@Getter
public class CabinClass {

    ECONOMY        ("Y", 1.0),
    PREMIUM_ECONOMY("W", 1.8),
    BUSINESS       ("C", 3.5),
    FIRST          ("F", 6.0);

    private final String code;
    private final double priceMultiplier;

    CabinClass(String code, double priceMultiplier) {
        this.code            = code;
        this.priceMultiplier = priceMultiplier;
    }
}
