package com.airline.ai.entity;

import com.airline.ai.entity.enums.*;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_users_email", columnList = "email", unique = true),
        @Index(name = "idx_users_loyalty", columnList = "loyalty_number")
})

@Getter @Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class User extends BaseEntity{

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "nationality", length = 3)
    private String nationality;

    @Column(name = "loyalty_number", unique = true, length = 20)
    private String loyaltyNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "loyalty_tier", nullable = false)
    @Builder.Default
    private LoyaltyTier loyaltyTier = LoyaltyTier.STANDARD;

    @Column(name = "miles_balance", nullable = false)
    @Builder.Default
    private Long milesBalance = 0L;

    @Column(name = "lifetime_miles", nullable = false)
    @Builder.Default
    private Long lifetimeMiles = 0L;

    @Column(name = "tier_qualifying_miles", nullable = false)
    @Builder.Default
    private Long tierQualifyingMiles = 0L;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private List<UserRole> roles;

    @Column(name = "active", nullable = false)
    @Builder.Default
    private Boolean active = true;

    @Column(name = "email_verified", nullable = false)
    @Builder.Default
    private Boolean emailVerified = false;

    @Column(name = "preferred_currency", length = 3)
    @Builder.Default
    private String preferredCurrency = "USD";

    @Column(name = "preferred_seat",    length = 10)
    private String preferredSeat;

    @Column(name = "meal_preference",   length = 50)
    private String mealPreference;

    @OneToMany(mappedBy = "passenger", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Booking> bookings;

    public String getFullName(){
        return firstName + " " + lastName;
    }
}
