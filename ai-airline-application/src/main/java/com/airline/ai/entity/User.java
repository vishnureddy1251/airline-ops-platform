package com.airline.ai.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.*;

@Getter @Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor

@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_users_email", columnList = "email", unique = true),
        @Index(name = "idx_users_loyalty", columnList = "loyalty_number")})

public class User extends BaseEntity{
}
