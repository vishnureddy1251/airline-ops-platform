package com.airline.ai.entity;

import com.airline.ai.entity.enums.LoyaltyTier;
import com.airline.ai.entity.enums.UserRole;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor

@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_users_email", columnList = "email", unique = true),
        @Index(name = "idx_users_loyalty", columnList = "loyalty_number")})

public class User extends BaseEntity{


    private String firstName;

    private String lastName;

    private String email;

    private String passwordHash;

    private String phone;

    private LocalDate dateOfBirth;

    private String nationality;

    private String loyaltyNumber;

    private LoyaltyTier loyaltyTier = LoyaltyTier.STANDARD;

    private Long milesBalance = 0L;

    private Long lifetimeMiles = 0L;

    private Long tierQualifyingMiles = 0L;

    private List<UserRole> roles;

    private Boolean active = true;

    private Boolean emailVerified = false;

    private String preferredCurrency = "USD";

    private String preferredSeat;

    private String mealPreference;

    private List<Booking> bookings;

    public String getFullName(){

        return firstName + " " + lastName;
    }
}
