package com.airline.ai.entity.enums;

import lombok.Getter;

@Getter
public class LoyaltyTier {

    STANDARD(0L,       1.0),
    SILVER  (25000L,   1.25),
    GOLD    (50000L,   1.5),
    PLATINUM(100000L,  2.0);

    private final long   requiredMiles;
    private final double milesMultiplier;

    LoyaltyTier(long requiredMiles, double milesMultiplier) {
        this.requiredMiles   = requiredMiles;
        this.milesMultiplier = milesMultiplier;
    }
}
